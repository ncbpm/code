package nc.bs.hr.trn.plugin;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.naming.NamingException;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pub.SystemException;
import nc.hr.frame.persistence.SimpleDocServiceTemplate;
import nc.hr.utils.PubEnv;
import nc.hr.utils.ResHelper;
import nc.itf.hi.IPersonRecordService;
import nc.itf.hr.frame.IHrBillCode;
import nc.itf.hr.frame.IPersistenceRetrieve;
import nc.itf.hr.frame.IPersistenceUpdate;
import nc.itf.trn.IItemSetAdapter;
import nc.itf.trn.TrnDelegator;
import nc.itf.trn.regmng.IRegmngManageService;
import nc.itf.trn.regmng.IRegmngQueryService;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.itf.uap.pf.IplatFormEntry;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.data.access.NCObject;
import nc.md.model.IBean;
import nc.md.model.impl.MDEnum;
import nc.md.persist.framework.IMDPersistenceService;
import nc.md.persist.framework.MDPersistenceService;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.pub.billcode.itf.IBillcodeRuleQryService;
import nc.pub.billcode.vo.BillCodeContext;
import nc.pub.billcode.vo.BillCodeElemVO;
import nc.pub.billcode.vo.BillCodeRuleVO;
import nc.vo.arap.utils.ArrayUtil;
import nc.vo.hi.entrymng.HiSendMsgHelper;
import nc.vo.hi.psndoc.PsnJobVO;
import nc.vo.hi.psndoc.PsndocVO;
import nc.vo.hi.psndoc.TrialVO;
import nc.vo.hi.psndoc.enumeration.TrnseventEnum;
import nc.vo.hi.pub.HICommonValue;
import nc.vo.hr.tools.pub.HRConstEnum;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pflow.PfUserObject;
import nc.vo.pubapp.util.NCPfServiceUtils;
import nc.vo.trn.pub.BeanUtil;
import nc.vo.trn.pub.TRNConst;
import nc.vo.trn.regitem.TrnRegItemVO;
import nc.vo.trn.regmng.AggRegapplyVO;
import nc.vo.trn.regmng.RegapplyVO;
import nc.vo.uif2.LoginContext;
import nc.vo.wfengine.definition.WorkflowTypeEnum;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * //转正申请
 */
public class BpmRegmngExpPfxxPlugin<T extends AggRegapplyVO> extends
		nc.bs.pfxx.plugin.AbstractPfxxPlugin {

	private PfUserObject[] userObjs;

	/**
	 * 将由XML转换过来的VO导入NC系统。业务插件实现此方法即可。<br>
	 * 请注意，业务方法的校验一定要充分
	 * 
	 * @param vo
	 *            转换后的vo数据，在NC系统中可能为ValueObject,SuperVO,AggregatedValueObject,
	 *            IExAggVO等。
	 * @param swapContext
	 *            各种交换参数，组织，接受方，发送方，帐套等等
	 * @param aggxsysvo
	 *            辅助信息vo
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggxsysvo) throws BusinessException {

		// 1.得到转换后的VO数据,取决于向导第一步注册的VO信息
		AggRegapplyVO bill = (AggRegapplyVO) vo;
		RegapplyVO head = setHeaderDefault((RegapplyVO) bill.getParentVO());
		// if (head.getPk_billtype() == null) {
		// throw new BusinessException("单据的单据类型编码字段不能为空，请输入值");
		// }

		if (head.getPk_group() == null) {
			throw new BusinessException("单据的所属集团字段不能为空，请输入值");
		}
		if (head.getPk_org() == null) {
			throw new BusinessException("单据的财务组织字段不能为空，请输入值");
		}
		if (head.getRegulardate() == null) {
			throw new BusinessException("单据的生效日期字段不能为空，请输入值");
		}

		// 2.查询此单据是否已经被导入过
		String oldPk = PfxxPluginUtils.queryBillPKBeforeSaveOrUpdate(
				swapContext.getBilltype(), swapContext.getDocID());
		if (oldPk != null) {

			// 这个判断，好像平台已经过，如果单据已导入，且replace="N"，那么平台就会抛出异常，提示不可重复
			if (swapContext.getReplace().equalsIgnoreCase("N"))
				throw new BusinessException(
						"不允许重复导入单据，请检查是否是操作错误；如果想更新已导入单据，请把数据文件的replace标志设为‘Y’");

			IRegmngQueryService voucherbo = (IRegmngQueryService) NCLocator
					.getInstance().lookup(IRegmngQueryService.class.getName());
			AggRegapplyVO preVO = voucherbo.queryByPk(oldPk);

			if (preVO != null && preVO.getParentVO() != null) {

				throw new BusinessException("单据已存在，不允许重复导入单据。");

			}
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		String strCode = formatter.format(new Date());
		head.setBill_code(strCode); //

		AggRegapplyVO newBill = this.doActionBill(bill, head);

		String pk = null;
		if (newBill != null) {
			pk = newBill.getParentVO().getPrimaryKey();
		}
		if (oldPk != null) {
			PfxxPluginUtils.deleteIDvsPKByDocPK(oldPk);
		}
		PfxxPluginUtils.addDocIDVsPKContrast(swapContext.getBilltype(),
				swapContext.getDocID(), pk);
		return pk;
	}

	private AggRegapplyVO doActionBill(AggRegapplyVO bill, RegapplyVO head)
			throws BusinessException {

		IRegmngManageService voucherbo = (IRegmngManageService) NCLocator
				.getInstance().lookup(IRegmngManageService.class.getName());

		AggRegapplyVO res = voucherbo.insertBill(bill);
		res = (AggRegapplyVO) ArrayUtil
				.getFirstInArrays((Object[]) NCPfServiceUtils.processBatch(
						IPFActionName.SAVE, head.getPk_billtype(),
						new AggRegapplyVO[] { (AggRegapplyVO) bill },
						getUserObj(), new WorkflownoteVO()));

		// IRegmngQueryService voucherbo1 = (IRegmngQueryService) NCLocator
		// .getInstance().lookup(IRegmngQueryService.class.getName());
		// AggRegapplyVO preVO = voucherbo1.queryByPk(res.getParentVO()
		// .getPrimaryKey());
		IFlowBizItf itf = NCObject.newInstance(res).getBizInterface(
				IFlowBizItf.class);
		// 校验必输项
		int blPassed = IPfRetCheckInfo.PASSING;
		if (blPassed == IPfRetCheckInfo.PASSING
				&& IPfRetCheckInfo.COMMIT == itf.getApproveStatus()) {
			if (((RegapplyVO) res.getParentVO()).getTrialresult() == null) {
				throw new BusinessException(ResHelper.getString("6009reg",
						"06009reg0030")/* @res "单据未填写试用结果" */);
			}
			if (StringUtils.isNotBlank(getMsg((RegapplyVO) res.getParentVO()))) {
				throw new BusinessException(ResHelper.getString("6009reg",
						"06009reg0031")/*
										 * @res "单据中存在未填写的必输项"
										 */);
			}
		}
		changeBillData(itf, PubEnv.getPk_user(), PubEnv.getServerTime(),
				"外部导入审批", blPassed);
		// 执行审批操作前，将审批信息写到pub_workflownote中
		WorkflownoteVO worknoteVO = buildWorkflownoteVO(itf,
				PubEnv.getPk_user(), "外部导入审批", blPassed, itf.getBilltype());
		getIPersistenceUpdate().insertVO(null, worknoteVO, null);
		((RegapplyVO) bill.getParentVO()).setApprove_state(1);
		res = voucherbo.updateBill(res, false);

		LoginContext context = new LoginContext();
		context.setPk_group(head.getPk_group());
		// 执行
		IRegmngQueryService voucherbo2 = (IRegmngQueryService) NCLocator
				.getInstance().lookup(IRegmngQueryService.class.getName());
		AggRegapplyVO vo = voucherbo2.queryByPk(res.getParentVO()
				.getPrimaryKey());
		HashMap<String, Object> map = manualExecBills(
				new AggRegapplyVO[] { (AggRegapplyVO) vo }, context,
				head.getRegulardate());

		// result.put(TRNConst.RESULT_MSG, isRunBackgroundTask ? msg :
		// msg.replaceAll("<br>", '\n' + ""));
		// result.put(TRNConst.RESULT_BILLS, passBills);
		if (map != null) {
			String msg = (String) map.get(TRNConst.RESULT_MSG);
			if (msg != null && !"".equals(msg)) {
				throw new BusinessException(msg);
			}
		}
		return res;
	}

	/**
	 * 插入一条审批意见
	 * 
	 * @param itf
	 * @param strApproveId
	 * @param strCheckNote
	 * @param blPassed
	 * @param billtype
	 * @return WorkflownoteVO
	 * @throws BusinessException
	 */
	private WorkflownoteVO buildWorkflownoteVO(IFlowBizItf itf,
			String strApproveId, String strCheckNote, int blPassed,
			String billtype) throws BusinessException {

		WorkflownoteVO worknoteVO = new WorkflownoteVO();
		worknoteVO.setBillid(itf.getBillId());// 单据ID
		worknoteVO.setBillVersionPK(itf.getBillId());
		worknoteVO.setChecknote(strCheckNote);// 审批意见
		// 发送日期
		worknoteVO.setSenddate(PubEnv.getServerTime());
		worknoteVO.setDealdate(PubEnv.getServerTime());// 审批日期
		// 组织
		worknoteVO.setPk_org(itf.getPkorg());
		// 单据编号
		worknoteVO.setBillno(itf.getBillNo());
		// 发送人
		String sendman = itf.getApprover() == null ? itf.getBillMaker() : itf
				.getApprover();
		worknoteVO.setSenderman(sendman);
		// Y,审批通过，N，审批不通过
		worknoteVO.setApproveresult(IPfRetCheckInfo.NOSTATE == blPassed ? "R"
				: IPfRetCheckInfo.PASSING == blPassed ? "Y" : "N");
		worknoteVO.setApprovestatus(1);// 直批的状态
		worknoteVO.setIscheck(IPfRetCheckInfo.PASSING == blPassed ? "Y"
				: IPfRetCheckInfo.NOPASS == blPassed ? "N" : "X");
		worknoteVO.setActiontype("APPROVE");
		worknoteVO.setCheckman(strApproveId);
		// 单据类型
		worknoteVO.setPk_billtype(billtype);
		worknoteVO.setWorkflow_type(WorkflowTypeEnum.Approveflow.getIntValue());
		return worknoteVO;
	}

	private IPersistenceUpdate getIPersistenceUpdate() {

		return NCLocator.getInstance().lookup(IPersistenceUpdate.class);
	}

	private String getMsg(RegapplyVO billvo) throws BusinessException {

		// 对于审批通过的单据,校验所有必输的项目是否都输入完毕
		SuperVO[] itemvos = TrnDelegator.getIItemSetQueryService()
				.queryItemSetByOrg(TRNConst.REGITEM_BEANID,
						billvo.getPk_group(), billvo.getPk_org(),
						billvo.getProbation_type());
		IBean ibean = BeanUtil.getBeanEntity(TRNConst.REGITEM_BEANID);
		List<IItemSetAdapter> iitemadpls = BeanUtil.getBizImpObjFromVo(ibean,
				IItemSetAdapter.class, itemvos);
		for (IItemSetAdapter item : iitemadpls) {
			if (item == null || item.getItemkey().startsWith("old")) {
				// 前项目不校验
				continue;
			}
			if (item.getIsnotnull().booleanValue()
					&& isNull(billvo.getAttributeValue(item.getItemkey()))) {
				return '\n' + billvo.getBill_code();
			}
		}
		return "";
	}

	private boolean isNull(Object o) {
		if (o == null || o.toString() == null || o.toString().trim().equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 回写入职表的审批信息
	 * 
	 * @param itf
	 * @param strApproveId
	 * @param strApproveDate
	 * @param strCheckNote
	 * @param intAppState
	 * @throws BusinessException
	 */
	private void changeBillData(IFlowBizItf itf, String strApproveId,
			UFDateTime strApproveDate, String strCheckNote, Integer intAppState)
			throws BusinessException {

		if (itf == null) {
			return;
		}
		itf.setApprover(strApproveId);
		itf.setApproveNote(strCheckNote);
		itf.setApproveDate(strApproveDate);
		itf.setApproveStatus(intAppState);
	}

	public PfUserObject[] getUserObj() {
		if (userObjs == null) {
			userObjs = new PfUserObject[] { new PfUserObject() };
		}
		return userObjs;
	}

	/**
	 * 设置表头默认信息
	 * 
	 * @param headerVo
	 * @return
	 * @throws BusinessException
	 */
	private RegapplyVO setHeaderDefault(RegapplyVO header)
			throws BusinessException {
		Integer ZERO = Integer.valueOf(0);
		/* 单据状态为未审核 */
		header.setApprove_state(-1);
		header.setDr(ZERO);
		header.setCreationtime(new UFDateTime());
		header.setPk_billtype(TRNConst.BillTYPE_REG);

		return header;
	}

	public HashMap<String, Object> manualExecBills(AggRegapplyVO[] bills,
			LoginContext context, UFLiteralDate effectDate)
			throws BusinessException {
		if (!ArrayUtils.isEmpty(bills)) {
			for (int i = 0; i < bills.length; i++) {
				bills[i].getParentVO().setAttributeValue(
						RegapplyVO.REGULARDATE, effectDate);
			}
		}
		HashMap<String, Object> result = execBills(bills, context, true);
		// modify start: yunana 2013-5-10
		// 这个异常在在前台抛，避免超编通知NC消息发送之后回滚
		// String msg = (String) result.get(TRNConst.RESULT_MSG);
		// if (StringUtils.isNotBlank(msg))
		// {
		// throw new BusinessException(ResHelper.getString("6009tran",
		// "06009tran0110")/* @res
		// "由于如下原因,部分单据没有执行成功:" */+ '\n' + msg);
		// }
		// modify end : yunana 2013-5-10
		return result;
	}

	/**
	 * 执行单据
	 * 
	 * @param billvos
	 * @param context
	 * @param isRunBackgroundTask
	 * @return HashMap<String, Object>
	 * @throws BusinessException
	 */
	public HashMap<String, Object> execBills(AggRegapplyVO[] billVOs,
			LoginContext context, boolean isRunBackgroundTask)
			throws BusinessException {
		IRegmngQueryService voucherbo = (IRegmngQueryService) NCLocator
				.getInstance().lookup(IRegmngQueryService.class.getName());
		HashMap<String, Object> result = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();// 错误信息

		AggregatedValueObject[] retVOs = null;
		if (ArrayUtils.isEmpty(billVOs)) {
			return result;
		}
		retVOs = voucherbo.validateBudget(billVOs, context);

		// 添加一段逻辑,将编制校验没通过的单据,也加入到错误提示中
		for (int i = 0; i < billVOs.length; i++) {
			if (isExit(retVOs, billVOs[i])) {
				continue;
			}
			sb.append(ResHelper.getString(
					"6009reg",
					"06009reg0033"/* @res "单据{0}由于编制校验未通过不能成功执行 " */,
					(String) billVOs[i].getParentVO().getAttributeValue(
							RegapplyVO.BILL_CODE))
			/* + "<br>" */);
		}

		if (retVOs == null || retVOs.length == 0) {
			// 两部分都没有可执行单据,直接返回
			String msg = sb.length() == 0 ? "" : sb.toString();
			result.put(
					TRNConst.RESULT_MSG,
					isRunBackgroundTask ? msg : msg.replaceAll("<br>",
							'\n' + ""));
			result.put(TRNConst.RESULT_BILLS, null);
			return result;
		}

		// 只对单据进行编制校验
		ArrayList<AggRegapplyVO> passBills = new ArrayList<AggRegapplyVO>();// 调配执行成功的单据
		IRegmngManageService regService = NCLocator.getInstance().lookup(
				IRegmngManageService.class);
		for (int i = 0; i < retVOs.length; i++) {
			try {
				// 转正执行
				doPerfromBill_RequiresNew((AggRegapplyVO) retVOs[i]);
				passBills.add((AggRegapplyVO) retVOs[i]);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				String billcode = (String) retVOs[i].getParentVO()
						.getAttributeValue(RegapplyVO.BILL_CODE);
				if (StringUtils.isBlank(e.getMessage())) {
					sb.append(i
							+ 1
							+ ":"
							+ ResHelper.getString("6009reg",
									"06009reg0034"/*
												 * @res
												 * "单据{0}由于如下未知异常[{1}]不能成功执行,具体异常信息请查看日志"
												 */, billcode, e.getMessage()) /*
																				 * +
																				 * "<br>"
																				 */);
				} else {
					if (e.getMessage().indexOf(billcode) < 0) {
						// 如果异常信息中没有出现单据号,则重组异常信息
						sb.append(i
								+ 1
								+ ":"
								+ ResHelper.getString("6009reg",
										"06009reg0034"/*
													 * @res
													 * "单据{0}由于如下未知异常[{1}]不能成功执行,具体异常信息请查看日志"
													 */, billcode,
										e.getMessage()) /*
														 * + "<br>"
														 */);
					} else {
						sb.append(i + 1 + ":" + e.getMessage()/* + "<br>" */);
					}
				}
				continue;
			}

			try {
				AggRegapplyVO agg = voucherbo.queryByPk(retVOs[i].getParentVO()
						.getPrimaryKey());
				doPushBill_RequiresNew(agg);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		// 执行完成后,发送通知
		// 1)发送转正的通知,按照转正后组织发送 1001Z7PSN00000000003
		// key-org value-bill_list
		HashMap<String, ArrayList<AggRegapplyVO>> hmTrans = new HashMap<String, ArrayList<AggRegapplyVO>>();
		for (AggregatedValueObject bill : retVOs) {
			String pk_org = (String) bill.getParentVO().getAttributeValue(
					RegapplyVO.PK_ORG);
			if (hmTrans.get(pk_org) == null) {
				hmTrans.put(pk_org, new ArrayList<AggRegapplyVO>());
			}
			hmTrans.get(pk_org).add((AggRegapplyVO) bill);
		}
		for (String key : hmTrans.keySet()) {
			if (hmTrans.get(key) == null || hmTrans.get(key).size() <= 0) {
				continue;
			}

			String tempCode = HICommonValue.msgcode_zhuanzheng;
			HiSendMsgHelper.sendMessage1(tempCode,
					hmTrans.get(key).toArray(new AggRegapplyVO[0]), key);
		}

		// end
		String msg = sb.length() == 0 ? "" : sb.toString();
		result.put(TRNConst.RESULT_MSG,
				isRunBackgroundTask ? msg : msg.replaceAll("<br>", '\n' + ""));
		result.put(TRNConst.RESULT_BILLS, passBills);
		return result;
	}
    public void doPushBill_RequiresNew(AggRegapplyVO aggVO) throws BusinessException
    {
        
        HashMap<String, String> hashPara = new HashMap<String, String>();
        hashPara.put(PfUtilBaseTools.PARAM_NOFLOW, PfUtilBaseTools.PARAM_NOFLOW);
        NCLocator.getInstance().lookup(IplatFormEntry.class).processAction("PUSH", TRNConst.BillTYPE_REG, null, aggVO, null, hashPara);
    }
	 
    private boolean isExit(AggregatedValueObject[] retVOs, AggRegapplyVO billVO) throws BusinessException
    {
        for (int i = 0; retVOs != null && i < retVOs.length; i++)
        {
            if (billVO.getParentVO().getPrimaryKey().equals(retVOs[i].getParentVO().getPrimaryKey()))
            {
                return true;
            }
        }
        return false;
    }
    
	/**
	 * 处理单据信息_执行
	 */
	public void doPerfromBill_RequiresNew(AggRegapplyVO aggVO)
			throws BusinessException {
		if (aggVO == null) {
			return;
		}
		RegapplyVO vo = (RegapplyVO) aggVO.getParentVO();
		if (vo.getTrialresult() == null) {
			throw new BusinessException(ResHelper.getString("6009reg",
					"06009reg0032")/* @res "试用结果不能为空!" */);
		}
		switch (vo.getTrialresult()) {
		case TRNConst.TRIALRESULT_PASS:// 试用结果－转正
			updateTrialForPASS(vo);
			break;
		case TRNConst.TRIALRESULT_DELAY:// 试用结果－延长试用期限
			updateTrialForDelay(vo);
			break;
		case TRNConst.TRIALRESULT_FALL:// 试用结果－未通过试用
			updateTrialForFall(vo);
			break;
		default:
			break;
		}

		// 人员变动时，如果人员编码设置了后编码规则，根据参数更新人员编码
		updatePsncode_Apply(vo.getPk_psndoc(), vo.getPk_org(), vo);

		// 更新单据状态为已执行
		aggVO.getParentVO().setAttributeValue(RegapplyVO.APPROVE_STATE,
				HRConstEnum.EXECUTED);
//		IRegmngManageService regService = NCLocator.getInstance().lookup(
//				IRegmngManageService.class);
//		regService.updateBill(aggVO, false);
		
		aggVO.getParentVO().setStatus(VOStatus.UPDATED);
//         setAuditInfoAndTs((SuperVO) billvo.getParentVO(), blChangeAuditInfo);
        getMDPersistenceService().saveBill(aggVO);
//		getServiceTemplate().update(aggVO, false);
	}

	  /***************************************************************************
     * 返回元数据持久化服务对象
     * @return IMDPersistenceService
     *****************************************************************************/
    protected static IMDPersistenceService getMDPersistenceService()
    {
        return MDPersistenceService.lookupPersistenceService();
    }
	/**
	 * 延长试用期限
	 */
	private void updateTrialForDelay(RegapplyVO vo) throws BusinessException {

		if (vo == null) {
			return;
		}
		// 取得当前有效的试用信息
		TrialVO[] trialVOs = (TrialVO[]) getIPersistenceRetrieve()
				.retrieveByClause(
						null,
						TrialVO.class,
						"  endflag = 'N' and pk_psnorg ='" + vo.getPk_psnorg()
								+ "' ");
		TrialVO trialVO = trialVOs[0];
		// 更新试用结果和计划转正日期
		String[] updateFields = { TrialVO.TRIALRESULT, TrialVO.ENDDATE };
		trialVO.setAttributeValue(updateFields[0], vo.getTrialresult());
		trialVO.setAttributeValue(updateFields[1], vo.getTrialdelaydate());
		getPersistenceUpdate().updateVO(null, trialVO, updateFields, null);
	}

	/**
	 * 转正
	 */
	private void updateTrialForFall(RegapplyVO vo) throws BusinessException {

		if (vo == null) {
			return;
		}
		// 取得当前有效的试用信息
		TrialVO[] trialVOs = getValidVO(TrialVO.class, vo.getPk_psnorg(), 1);
		// 更新有效的试用信息
		if (trialVOs != null && trialVOs.length > 0) {
			TrialVO trialVO = trialVOs[0];
			// 更新试用结果和计划转正日期
			trialVO.setTrialresult(vo.getTrialresult());
			trialVO.setEnddate(vo.getEnd_date());
			trialVO.setEndflag(UFBoolean.TRUE);
			// 转正单中的备注字段，需要同步到试用记录中 heqiaoa 20150609
			trialVO.setMemo(vo.getNewmemo());
			getServiceTemplate().update(trialVO, false);
		}
		// 更新工作记录的试用信息

		PsnJobVO jobVO = getServiceTemplate().queryByPk(PsnJobVO.class,
				vo.getPk_psnjob());
		String[] updateFields = { PsnJobVO.TRIAL_FLAG, PsnJobVO.TRIAL_TYPE };
		jobVO.setAttributeValue(updateFields[0], false);
		jobVO.setAttributeValue(updateFields[1], null);
		getPersistenceUpdate().updateVO(null, jobVO, updateFields, null);
	}

	private IPersistenceRetrieve getIPersistenceRetrieve() {

		return NCLocator.getInstance().lookup(IPersistenceRetrieve.class);
	}

	private IPersistenceUpdate getPersistenceUpdate() {

		return NCLocator.getInstance().lookup(IPersistenceUpdate.class);
	}

	// 人员转正时，如果人员编码设置了后编码规则，根据参数更新人员编码
	public void updatePsncode_Apply(String pk_psndoc, String pk_hrorg,
			RegapplyVO regapplyvo) throws BusinessException {
		// 参数为是的情况下才考虑这些
		UFBoolean para = SysinitAccessor.getInstance().getParaBoolean(pk_hrorg,
				"TRN0005");
		if (para != null && para.booleanValue()) {
			BillCodeContext billContext = NCLocator
					.getInstance()
					.lookup(IBillcodeManage.class)
					.getBillCodeContext(HICommonValue.NBCR_PSNDOC_CODE,
							PubEnv.getPk_group(), pk_hrorg);
			// 人员编码规则设置为后编码
			if (billContext != null && !billContext.isPrecode()) {
				// 如果设置的是后编码，当人员变动时，需要判断编码规则中定义的业务实体的值是否发生了变化，来决定是否重新生成人员编码
				HashMap<String, Object> map = getCodeRule_Apply(pk_hrorg,
						regapplyvo);
				if (map == null) {
					return;
				}
				boolean ischange = (boolean) map.get("ischange");

				if (ischange) {
					PsndocVO[] psndocvo = (PsndocVO[]) NCLocator
							.getInstance()
							.lookup(IPersistenceRetrieve.class)
							.retrieveByClause(null, PsndocVO.class,
									" pk_psndoc = '" + pk_psndoc + "' ");
					IHrBillCode service = NCLocator.getInstance().lookup(
							IHrBillCode.class);
					PsnJobVO psnjobvo = (PsnJobVO) map.get("psnjobvo");
					String[] strCode = service.getLeveledBillCode(
							HICommonValue.NBCR_PSNDOC_CODE,
							PubEnv.getPk_group(), pk_hrorg, psnjobvo, 1);
					psndocvo[0].setCode(strCode[0]);
					psndocvo[0].setStatus(VOStatus.UPDATED);
					NCLocator
							.getInstance()
							.lookup(IPersistenceUpdate.class)
							.updateVO(null, psndocvo[0],
									new String[] { PsndocVO.CODE }, null);
				}
			}
		}
	}

	// 如果设置的是后编码，当人员变动时，需要判断编码规则中定义的业务实体的值是否发生了变化，来决定是否重新生成人员编码
	public HashMap<String, Object> getCodeRule_Apply(String pk_hrorg,
			RegapplyVO regapplyvo) throws BusinessException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean ischange = false;
		BillCodeRuleVO rulevo = getBillCodeRuleVOFromDB(
				HICommonValue.NBCR_PSNDOC_CODE, PubEnv.getPk_group(), pk_hrorg);
		BillCodeElemVO[] elems = rulevo.getElems();
		// 针对业务实体elemtype=1的字段抓取出来，需要对比变动前后的值来决定是否重新生成人员编码
		if (!ArrayUtils.isEmpty(elems)) {
			List<String> liststr = new ArrayList<String>();
			for (int i = 0; i < elems.length; i++) {
				if (elems[i].getElemtype() == 1) {
					liststr.add(elems[i].getElemvalue());
				}
			}
			String[] str = liststr.toArray(new String[0]);
			if (ArrayUtils.isEmpty(str)) {// 定义的后编码规则中没有选择业务实体，则编码一直都不需要重新生成
				return null;
			}

			PsnJobVO psnjobvo = new PsnJobVO();
			for (int i = 0; i < str.length; i++) {
				String name = str[i];
				String newdata = regapplyvo.getAttributeValue("new" + name) == null ? ""
						: regapplyvo.getAttributeValue("new" + name).toString();
				String olddata = regapplyvo.getAttributeValue("old" + name) == null ? ""
						: regapplyvo.getAttributeValue("old" + name).toString();
				if (!newdata.equals(olddata)) {
					ischange = true;
					psnjobvo.setAttributeValue(name, newdata);
				}
			}

			map.put("ischange", ischange);
			map.put("psnjobvo", psnjobvo);

		}
		return map;
	}

	/**
	 * 根据单据类型编码得到该单据类型的单据号规则。
	 * 
	 * @param billTypeCode
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 * @throws SystemException
	 * @throws ValidationException
	 */
	private BillCodeRuleVO getBillCodeRuleVOFromDB(String nbcrcode,
			String pk_group, String pk_org) throws BusinessException {
		BillCodeRuleVO rulevo;
		try {
			IBillcodeRuleQryService service = NCLocator.getInstance().lookup(
					IBillcodeRuleQryService.class);
			rulevo = service.qryBillCodeRule(nbcrcode, pk_group, pk_org);
		} catch (Exception e) {
			Logger.error("Error occurs while querying BillCodeRule", e);
			throw new BusinessException(
					"Error occurs while querying BillCodeRule", e);
		}
		if (rulevo == null)
			throw new nc.vo.pub.ValidationException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("101704", "UPP101704-000009",
							null, new String[] { nbcrcode }));// /*@res
		// "该单据类型还没有定义编码规则，获得单据号失败：单据类型编码为{0}"*/+billTypeCode+"'");
		return rulevo;
	}

	/**
	 * 转正
	 */
	private void updateTrialForPASS(RegapplyVO vo) throws BusinessException {

		if (vo == null) {
			return;
		}
		// 取得当前有效的试用信息
		TrialVO[] trialVOs = getValidVO(TrialVO.class, vo.getPk_psnorg(), 1);
		// 更新有效的试用信息
		if (trialVOs != null && trialVOs.length > 0) {
			TrialVO trialVO = trialVOs[0];
			// 更新试用结果和计划转正日期
			if (trialVO.getEnddate() == null) {
				trialVO.setEnddate(vo.getRegulardate());
			}
			trialVO.setTrialresult(vo.getTrialresult());
			trialVO.setEnddate(vo.getEnd_date());
			trialVO.setRegulardate(vo.getRegulardate());
			trialVO.setEndflag(UFBoolean.TRUE);
			// 转正单中的备注字段，需要同步到试用记录中 heqiaoa 20150609
			trialVO.setMemo(vo.getNewmemo());
			getServiceTemplate().update(trialVO, false);
		}
		// 4-2)新增一条工作记录
		PsnJobVO newVO = createNewPsnjob(vo);
		newVO = NCLocator
				.getInstance()
				.lookup(IPersonRecordService.class)
				.addNewPsnjob(
						newVO,
						vo.getIfsynwork() == null ? false : vo.getIfsynwork()
								.booleanValue(), vo.getPk_org());
	}

	/**
	 * 根据组织关系主键与任职ID得到某子集的最新记录
	 * 
	 * @param <T>
	 * @param className
	 * @param pk_psnorg
	 * @param assgid
	 * @return < T extends SuperVO > T
	 * @throws BusinessException
	 */
	private <T> T[] getValidVO(Class<T> className, String pk_psnorg,
			Integer assgid) throws BusinessException {

		String where = " pk_psnorg = '" + pk_psnorg + "' and endflag = 'N' ";
		if (assgid != null) {
			where += " and assgid = " + assgid.intValue();
		}
		return getServiceTemplate().queryByCondition(className, where);
	}

	/**
	 * 根据组织关系主键与任职ID得到某子集的最新记录
	 * 
	 * @param <T>
	 * @param className
	 * @param pk_psnorg
	 * @param assgid
	 * @return < T extends SuperVO > T
	 * @throws BusinessException
	 */
	private <T> T[] getLastVO(Class<T> className, String pk_psnorg,
			Integer assgid) throws BusinessException {

		String where = " pk_psnorg = '" + pk_psnorg + "' and lastflag = 'Y' ";
		if (assgid != null) {
			where += " and assgid = " + assgid.intValue();
		}
		return getServiceTemplate().queryByCondition(className, where);
	}

	private final String DOC_NAME = "RegmngManage";

	private SimpleDocServiceTemplate serviceTemplate;

	private SimpleDocServiceTemplate getServiceTemplate() {

		if (serviceTemplate == null) {
			serviceTemplate = new SimpleDocServiceTemplate(DOC_NAME);
		}
		return serviceTemplate;
	}

	private PsnJobVO createNewPsnjob(RegapplyVO bill) throws BusinessException {

		// 给转正项目对应的字段赋值如果单据中有值则使用单据中的值否则使用上一条记录的值
		// 得到上一条记录
		PsnJobVO[] lastvo = getLastVO(PsnJobVO.class, bill.getPk_psnorg(), 1);
		//

		PsnJobVO psnjob = new PsnJobVO();
		// 新任职开始日期 = 转正日期
		if (bill.getRegulardate() != null) {
			psnjob.setBegindate(bill.getRegulardate());
		} else {
			psnjob.setBegindate(PubEnv.getServerLiteralDate());
		}
		psnjob.setEnddate(null);
		psnjob.setEndflag(UFBoolean.FALSE);
		psnjob.setIsmainjob(UFBoolean.TRUE);
		psnjob.setLastflag(UFBoolean.TRUE);
		psnjob.setPk_hrgroup(bill.getPk_group());
		psnjob.setPk_group(bill.getPk_group());
		psnjob.setPk_hrorg(bill.getPk_org());
		psnjob.setPk_psndoc(bill.getPk_psndoc());
		psnjob.setPk_psnorg(bill.getPk_psnorg());
		psnjob.setPk_psnjob(null);
		psnjob.setPsntype(0);
		psnjob.setAssgid(1);
		// 转正后默认为
		psnjob.setPoststat(UFBoolean.TRUE);
		// 试用信息
		psnjob.setTrial_type(null);
		psnjob.setRecordnum(0);
		psnjob.setTrnsevent((Integer) ((MDEnum) TrnseventEnum.REGAPPLY).value());
		psnjob.setTrnstype(TRNConst.TRANSTYPE_REG);
		psnjob.setTrial_flag(UFBoolean.FALSE);
		psnjob.setShoworder(9999999);
		// 单据信息
		psnjob.setOribilltype(TRNConst.BillTYPE_REG);
		psnjob.setOribillpk(bill.getPk_hi_regapply());
		// 员工号要使用上一条的员工号
		psnjob.setClerkcode(lastvo[0].getClerkcode());

		for (String name : bill.getAttributeNames()) {
			// 岗位、职务等信息不用前一条的数据
			if (name.startsWith("new")) {
				Object value = bill.getAttributeValue(name);
				psnjob.setAttributeValue(name.substring(3), value);
			}
		}

		UFBoolean blPoststate = UFBoolean.FALSE;

		// 是否在岗 在调配申请时要特殊处理
		TrnRegItemVO[] tempvos = TrnDelegator.getIItemSetQueryService()
				.queryItemSetByOrg(TRNConst.REGITEM_BEANID, bill.getPk_group(),
						bill.getPk_org(), bill.getProbation_type());
		TrnRegItemVO vo = null;
		for (int i = 0; tempvos != null && i < tempvos.length; i++) {
			if (!"newpoststat".equals(tempvos[i].getItemkey())) {
				continue;
			}
			vo = tempvos[i];
		}
		if (vo == null) {
			// 调配项目中，没有是否在岗，使用上一条的信息
			blPoststate = lastvo[0].getPoststat();
		} else {
			if (vo.getIsedit() != null && vo.getIsedit().booleanValue()) {
				// 调配项目中用,并且可以调整,使用单据中的
				blPoststate = bill.getNewpoststat();
			} else {
				// 不能调整 使用工作记录中的
				blPoststate = lastvo[0].getPoststat();
			}
		}

		psnjob.setPoststat(blPoststate);

		return psnjob;
	}

	/**
	 * 校验通过的单据信息
	 * 
	 * @param vos
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	private void validate(AggRegapplyVO[] vos) throws BusinessException {
		String errMsg = "";
		for (AggRegapplyVO vo : vos) {
			RegapplyVO billvo = (RegapplyVO) vo.getParentVO();
			if (IPfRetCheckInfo.PASSING == billvo.getApprove_state()) {
				if (billvo.getTrialresult() == null) {
					errMsg += ResHelper.getString("6009reg", "06009reg0030")/*
																			 * @res
																			 * "单据未填写试用结果"
																			 */;
					continue;
				}
				if (StringUtils.isNotBlank(getMsg(billvo))) {
					errMsg += ResHelper.getString("6009reg", "06009reg0031")/*
																			 * @res
																			 * "单据中存在未填写的必输项"
																			 */;
					continue;
				}
			}
		}
		if (StringUtils.isNotBlank(errMsg)) {
			throw new BusinessException(errMsg);
		}
	}

}