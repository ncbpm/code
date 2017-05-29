package nc.bs.hr.trn.plugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.hr.utils.PubEnv;
import nc.hr.utils.ResHelper;
import nc.itf.hr.frame.IPersistenceUpdate;
import nc.itf.trn.IItemSetAdapter;
import nc.itf.trn.TrnDelegator;
import nc.itf.trn.regmng.IRegmngManageService;
import nc.itf.trn.regmng.IRegmngQueryService;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.data.access.NCObject;
import nc.md.model.IBean;
import nc.vo.arap.utils.ArrayUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pflow.PfUserObject;
import nc.vo.pubapp.util.NCPfServiceUtils;
import nc.vo.trn.pub.BeanUtil;
import nc.vo.trn.pub.TRNConst;
import nc.vo.trn.regmng.AggRegapplyVO;
import nc.vo.trn.regmng.RegapplyVO;
import nc.vo.wfengine.definition.WorkflowTypeEnum;

import org.apache.commons.lang.StringUtils;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>//转正申请
 * 
 * @author zhaoruic
 * @version Your Project V60
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
//		if (head.getPk_billtype() == null) {
//			throw new BusinessException("单据的单据类型编码字段不能为空，请输入值");
//		}

		if (head.getPk_group() == null) {
			throw new BusinessException("单据的所属集团字段不能为空，请输入值");
		}
		if (head.getPk_org() == null) {
			throw new BusinessException("单据的财务组织字段不能为空，请输入值");
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

		AggRegapplyVO newBill = this.insertBill(bill, head);

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

	private AggRegapplyVO insertBill(AggRegapplyVO bill, RegapplyVO head)
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
		// AggRegapplyVO preVO =
		// voucherbo1.queryByPk(res.getParentVO().getPrimaryKey());
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
		voucherbo.updateBill(res, false);
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
	   private boolean isNull(Object o)
	    {
	        if (o == null || o.toString() == null || o.toString().trim().equals(""))
	        {
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

}