package nc.bs.hr.trn.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.hr.utils.PubEnv;
import nc.hr.utils.ResHelper;
import nc.itf.hr.frame.IPersistenceRetrieve;
import nc.itf.hr.frame.IPersistenceUpdate;
import nc.itf.trn.IItemSetAdapter;
import nc.itf.trn.TrnDelegator;
import nc.itf.trn.rds.IRdsManageService;
import nc.itf.trn.regmng.IRegmngQueryService;
import nc.itf.trn.transmng.ITransmngManageService;
import nc.itf.trn.transmng.ITransmngQueryService;
import nc.md.model.IBean;
import nc.message.util.IDefaultMsgConst;
import nc.message.util.MessageCenter;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.vo.arap.utils.ArrayUtil;
import nc.vo.hi.entrymng.HiSendMsgHelper;
import nc.vo.hi.psndoc.CtrtVO;
import nc.vo.hi.pub.HICommonValue;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
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
import nc.vo.trn.transmng.AggStapply;
import nc.vo.trn.transmng.StapplyVO;
import nc.vo.uap.rbac.constant.INCSystemUserConst;
import nc.vo.uif2.LoginContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 调配 离职申请
 */
public class BpmTransExpPfxxPlugin<T extends AggStapply> extends
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
		AggStapply bill = (AggStapply) vo;
		StapplyVO head = setHeaderDefault((StapplyVO) bill.getParentVO());
		if (head.getPk_billtype() == null) {
			throw new BusinessException("单据的单据类型编码字段不能为空，请输入值");
		}

		if (head.getPk_group() == null) {
			throw new BusinessException("单据的所属集团字段不能为空，请输入值");
		}
		if (head.getPk_org() == null) {
			throw new BusinessException("单据的财务组织字段不能为空，请输入值");
		}

		if (head.getBill_code() == null) {
			throw new BusinessException("单据的单据编号字段不能为空，请输入值");
		}
		// 2.查询此单据是否已经被导入过
		String oldPk = PfxxPluginUtils.queryBillPKBeforeSaveOrUpdate(
				swapContext.getBilltype(), swapContext.getDocID());
		if (oldPk != null) {

			// 这个判断，好像平台已经过，如果单据已导入，且replace="N"，那么平台就会抛出异常，提示不可重复
			if (swapContext.getReplace().equalsIgnoreCase("N"))
				throw new BusinessException(
						"不允许重复导入单据，请检查是否是操作错误；如果想更新已导入单据，请把数据文件的replace标志设为‘Y’");

			ITransmngQueryService voucherbo = (ITransmngQueryService) NCLocator
					.getInstance()
					.lookup(ITransmngQueryService.class.getName());
			AggStapply preVO = voucherbo.queryByPk(oldPk);

			if (preVO != null && preVO.getParentVO() != null) {

				throw new BusinessException("单据已存在，不允许重复导入单据。");

			}
		}

		// SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		// String strCode = formatter.format(new Date());
		// head.setBill_code(strCode); //

		AggStapply newBill = this.insertBill(bill, head);

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

	private AggStapply insertBill(AggStapply bill, StapplyVO head)
			throws BusinessException {

		ITransmngManageService voucherbo = (ITransmngManageService) NCLocator
				.getInstance().lookup(ITransmngManageService.class.getName());

		if (head.getCreator() == null)
			throw new BusinessException("创建人不能为空。");
		InvocationInfoProxy.getInstance().setUserId(head.getCreator());
		AggStapply res = voucherbo.insertBill(bill);
		res = (AggStapply) ArrayUtil
				.getFirstInArrays((Object[]) NCPfServiceUtils.processBatch(
						IPFActionName.SAVE, head.getPk_billtype(),
						new AggStapply[] { (AggStapply) bill }, getUserObj(),
						new WorkflownoteVO()));

		validate(new AggStapply[] { (AggStapply) res });
		// validate(vos);

		// 处理审批流中的合同
		StapplyVO billvo = (StapplyVO) res.getParentVO();
		if (IPfRetCheckInfo.PASSING == billvo.getApprove_state()) {
			if (billvo.getIsend() != null && billvo.getIsend().booleanValue()
					|| billvo.getIsrelease() != null
					&& billvo.getIsrelease().booleanValue()) {
				handleCtrtInfo(billvo);
			}
		}
		((StapplyVO) res.getParentVO()).setApprove_state(1);
		if (head.getApprover() != null)
			InvocationInfoProxy.getInstance().setUserId(head.getApprover());
		((StapplyVO) res.getParentVO()).setApprover(InvocationInfoProxy
				.getInstance().getUserId());
		res = voucherbo.updateBill(res, false);
		LoginContext context = new LoginContext();
		context.setPk_group(head.getPk_group());
		// 执行
		ITransmngQueryService voucherbo2 = (ITransmngQueryService) NCLocator
				.getInstance().lookup(ITransmngQueryService.class.getName());
		AggStapply vo = voucherbo2.queryByPk(res.getParentVO().getPrimaryKey());
		execBills(new AggStapply[] { vo });
		return res;
	}

	/**
	 * 审批通过时，处理合同信息 <br>
	 * Created on 2014-3-18 19:48:57<br>
	 * 
	 * @param billvo
	 * @throws BusinessException
	 * @author caiqm
	 */
	private void handleCtrtInfo(StapplyVO billvo) throws BusinessException {
		boolean isCMStart = PubEnv.isModuleStarted(PubEnv.getPk_group(),
				PubEnv.MODULE_HRCM);
		if (isCMStart && IPfRetCheckInfo.PASSING == billvo.getApprove_state())// 为了适配提交即通过--
																				// &&
																				// IPfRetCheckInfo.PASSING
																				// ==
																				// billvo.getApprove_state()
		{
			// 如果存在生效的合同，而且没有未生效的结解除或者终止合同，生成一条。
			String cond_hasCtrt = "pk_psnorg = '"
					+ billvo.getPk_psnorg()
					+ "' and lastflag = 'Y' and conttype in (1, 2, 3) and isrefer = 'Y'";
			CtrtVO[] ctrtAllVOs = (CtrtVO[]) NCLocator.getInstance()
					.lookup(IPersistenceRetrieve.class)
					.retrieveByClause(null, CtrtVO.class, cond_hasCtrt);
			boolean isIsreferCtrt = false;
			if (!ArrayUtils.isEmpty(ctrtAllVOs)) {
				isIsreferCtrt = true;
			}
			if (isIsreferCtrt) {
				String condition = "recordnum = 0 and isrefer = 'N' and pk_psnorg = '"
						+ billvo.getPk_psnorg() + "'";
				CtrtVO[] ctrtVOs = (CtrtVO[]) NCLocator.getInstance()
						.lookup(IPersistenceRetrieve.class)
						.retrieveByClause(null, CtrtVO.class, condition);
				if (ArrayUtils.isEmpty(ctrtVOs)) {
					// 插入一条新的未生效合同
					CtrtVO newCtrtVO = new CtrtVO();
					try {
						BeanUtils.copyProperties(newCtrtVO, ctrtAllVOs[0]);
					} catch (Exception e) {
						Logger.error(e.getMessage(), e);
					}
					newCtrtVO.setRecordnum(0);
					newCtrtVO.setLastflag(UFBoolean.FALSE);
					newCtrtVO.setIsrefer(UFBoolean.FALSE);
					int conttype = billvo.getIsrelease() == UFBoolean.TRUE ? 4
							: 5;
					newCtrtVO.setConttype(conttype);
					newCtrtVO.setSigndate(new UFLiteralDate());
					newCtrtVO
							.setTermmonth(ctrtAllVOs[0].getTermmonth() == null ? null
									: ctrtAllVOs[0].getTermmonth());// 合同期限
					newCtrtVO
							.setPromonth(ctrtAllVOs[0].getPromonth() == null ? null
									: ctrtAllVOs[0].getPromonth());// 试用期限
					newCtrtVO.setPresenter(1);// 解除提出方---默认为用人单位

					NCLocator.getInstance().lookup(IPersistenceUpdate.class)
							.insertVO(null, newCtrtVO, null);

				} else if (ctrtVOs[0].getConttype() == 1
						|| ctrtVOs[0].getConttype() == 2
						|| ctrtVOs[0].getConttype() == 3) {
					throw new BusinessException(ResHelper.getString("6009tran",
							"X6009tran0060")/*
											 * @res "存在未生效的续签或变更的合同记录！"
											 */);
				}
			}
		}
	}

	private void validate(AggStapply[] vos) throws BusinessException {

		String errMsg = "";
		for (AggStapply vo : vos) {
			StapplyVO billvo = (StapplyVO) vo.getParentVO();
			if (IPfRetCheckInfo.PASSING == billvo.getApprove_state()) {
				errMsg += getMsg(billvo);
			}
		}
		if (!StringUtils.isBlank(errMsg)) {
			throw new BusinessException(ResHelper.getString("6009tran",
					"06009tran0153")/* @res "单据中存在未填写的必输项" */);
		}
	}

	private String getMsg(StapplyVO billvo) throws BusinessException {

		// 对于审批通过的单据,校验所有必输的项目是否都输入完毕
		SuperVO[] itemvos = TrnDelegator.getIItemSetQueryService()
				.queryItemSetByOrg(TRNConst.TRNSITEM_BEANID,
						billvo.getPk_group(), billvo.getPk_org(),
						billvo.getPk_trnstype());
		IBean ibean = BeanUtil.getBeanEntity(TRNConst.TRNSITEM_BEANID);
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
	private StapplyVO setHeaderDefault(StapplyVO header)
			throws BusinessException {
		Integer ZERO = Integer.valueOf(0);
		/* 单据状态为未审核 */
		header.setApprove_state(-1);
		header.setDr(ZERO);
		header.setCreationtime(new UFDateTime());
		return header;
	}

	private boolean isqueryctrt = false;

	/**
	 * 为了支持消息模板上的审批和移动审批，将执行单据的操作移至后台处理 将原来在前台Model中实现的execBills函数移至后台实现
	 * 
	 * @param datas
	 * @return
	 * @throws BusinessException
	 * @author heqiaoa 2014-11-18
	 */
	public AggStapply[] execBills(AggStapply[] vos) throws BusinessException {
		ArrayList<AggStapply> al = new ArrayList<AggStapply>();
		ArrayList<AggStapply> allvo = new ArrayList<AggStapply>();
		for (int i = 0; i < vos.length; i++) {
			AggStapply agg = vos[i];
			Integer apprState = ((StapplyVO) agg.getParentVO())
					.getApprove_state();
			UFLiteralDate effectDate = ((StapplyVO) agg.getParentVO())
					.getEffectdate();
			if (effectDate != null
					&& effectDate.compareTo(PubEnv.getServerLiteralDate()) <= 0
					&& apprState != null
					&& apprState == IPfRetCheckInfo.PASSING) {
				al.add(agg);
			}

			allvo.add(agg);
		}

		ArrayList<AggStapply> bills = new ArrayList<AggStapply>();
		if (al.size() > 0) {
			// 后台校验时需要查询合同
			isqueryctrt = true;
			// 审批后自动执行情况
			// 为了传入合适的参数进行编制校验，这里用pk_org和pk_group构造一个LoginContext
			LoginContext tempContext = new LoginContext();
			AggStapply aggvo = al.get(0);
			StapplyVO parentVO = (StapplyVO) aggvo.getParentVO();
			tempContext.setPk_group(parentVO.getPk_group());
			tempContext.setPk_org(parentVO.getPk_org());

			// 返回一个map 包括执行成功的单据，及执行不成功的信息
			HashMap<String, Object> result = execBills(
					al.toArray(new AggStapply[0]), tempContext, false);
			bills = (ArrayList<AggStapply>) result.get(TRNConst.RESULT_BILLS);
			String msg = (String) result.get(TRNConst.RESULT_MSG);
			if (!StringUtils.isBlank(msg)) {
				// 构造NCMessage
				NCMessage ncMessage = new NCMessage();
				MessageVO messageVO = new MessageVO();
				messageVO.setMsgsourcetype(IDefaultMsgConst.NOTICE);// 消息源类型
				messageVO.setReceiver(PubEnv.getPk_user());// 设置接收人 多个接收人之间以逗号隔开
				messageVO.setIsdelete(UFBoolean.FALSE);// 接收删除标记
				messageVO.setSender(INCSystemUserConst.NC_USER_PK);
				// 如果发送时间为空则默认为服务器时间
				messageVO.setSendtime(PubEnv.getServerTime());
				messageVO.setDr(0);
				messageVO.setSubject(ResHelper.getString("6007entry",
						"16007entry0015")/*
										 * res "单据审批失败"
										 */);
				messageVO.setContent(msg);
				ncMessage.setMessage(messageVO);
				NCMessage[] message = new NCMessage[1];
				message[0] = ncMessage;
				try {
					MessageCenter.sendMessage(message);
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
			}
		}

		// 将执行成功的和不成功的合并
		if (bills != null && bills.size() > 0)// 若没有执行成功的单据，则直接将审批通过的单据返回回去刷新界面
		{
			for (int i = 0; i < allvo.size(); i++) {
				AggStapply aggivo = allvo.get(i);
				for (int j = 0; j < bills.size(); j++) {
					AggStapply aggjvo = allvo.get(j);
					if (aggivo.getParentVO().getPrimaryKey()
							.equals(aggjvo.getParentVO().getPrimaryKey())) {
						allvo.remove(i);
					}
				}
			}
			vos = (AggStapply[]) ArrayUtils.addAll(
					allvo.toArray(new AggStapply[0]),
					bills.toArray(new AggStapply[0]));
		}

		return vos;
	}

	public HashMap<String, Object> execBills(AggStapply[] bills,
			LoginContext context, boolean isRunBackgroundTask)
			throws BusinessException {
		HashMap<String, Object> result = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();// 错误信息
		ArrayList<AggStapply> alTrans = new ArrayList<AggStapply>();// 调配单据
		ArrayList<AggStapply> alDimission = new ArrayList<AggStapply>();// 离职单据
		for (int i = 0; bills != null && i < bills.length; i++) {
			if (TRNConst.BUSITYPE_TRANSITION.equals(bills[i].getParentVO()
					.getAttributeValue(StapplyVO.PK_BILLTYPE))) {
				alTrans.add(bills[i]);
			} else {
				alDimission.add(bills[i]);
			}
		}
		AggregatedValueObject[] retVOs = null;
		if (alTrans.size() > 0) {
			retVOs = NCLocator
					.getInstance()
					.lookup(IRegmngQueryService.class)
					.validateBudget(alTrans.toArray(new AggStapply[0]), context);
		}

		// 添加一段逻辑,将编制校验没通过的单据,也加入到错误提示中
		for (int i = 0; i < alTrans.size(); i++) {
			if (isExit(retVOs, alTrans.get(i))) {
				continue;
			}
			sb.append(ResHelper.getString(
					"6009tran",
					"06009tran0166"/* @res "单据{0}由于编制校验未通过不能成功执行" */,
					(String) alTrans.get(i).getParentVO()
							.getAttributeValue(StapplyVO.BILL_CODE)));
		}

		if ((retVOs == null || retVOs.length == 0) && alDimission.size() == 0) {
			// 两部分都没有可执行单据,直接返回
			String msg = sb.length() == 0 ? "" : sb.toString();
			result.put(
					TRNConst.RESULT_MSG,
					isRunBackgroundTask ? msg : msg.replaceAll("<br>",
							'\n' + ""));
			result.put(TRNConst.RESULT_BILLS, null);
			return result;
		}
		// 只对调配单据进行编制校验
		ArrayList<AggStapply> transBill = new ArrayList<AggStapply>();// 调配执行成功的单据
		ArrayList<AggStapply> dimisBill = new ArrayList<AggStapply>();// 离职执行成功的单据
		AggregatedValueObject[] aggs = (AggregatedValueObject[]) ArrayUtils
				.addAll(retVOs, alDimission.toArray(new AggStapply[0]));

		ITransmngQueryService voucherbo = (ITransmngQueryService) NCLocator
				.getInstance().lookup(ITransmngQueryService.class.getName());
		for (int i = 0; i < aggs.length; i++) {
			String billtype = (String) aggs[i].getParentVO().getAttributeValue(
					StapplyVO.PK_BILLTYPE);
			try {

				if (TRNConst.BUSITYPE_TRANSITION.equals(billtype)) {
					// 调配校验&执行
					Object obj = getRdsService().perfromStaff_RequiresNew(
							(AggStapply) aggs[i], isqueryctrt);
					if (null == obj) {
						continue;
					}
					if (obj instanceof String) {
						transBill.add(voucherbo.queryByPk(aggs[i].getParentVO()
								.getPrimaryKey()));
						continue;
					}
					transBill.add(voucherbo.queryByPk(aggs[i].getParentVO()
							.getPrimaryKey()));
				} else {
					// 离职校验&执行
					Object obj = getRdsService().perfromTurnOver_RequiresNew(
							(AggStapply) aggs[i], isqueryctrt);
					if (null == obj) {
						continue;
					}
					if (obj instanceof String) {
						dimisBill.add(voucherbo.queryByPk(aggs[i].getParentVO()
								.getPrimaryKey()));
						continue;
					}
					dimisBill.add(voucherbo.queryByPk(aggs[i].getParentVO()
							.getPrimaryKey()));
				}

			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				String billcode = (String) aggs[i].getParentVO()
						.getAttributeValue(StapplyVO.BILL_CODE);
				if (StringUtils.isBlank(e.getMessage())) {

					sb.append((i + 1)
							+ ":"
							+ ResHelper.getString("6009tran",
									"06009tran0167"/*
													 * @res
													 * "单据{0}由于如下未知异常[{1}]不能成功执行,具体异常信息请查看日志."
													 */, billcode,
									e.getMessage()) /*
													 * + " <br>"
													 */);
				} else {
					if (e.getMessage().indexOf(billcode) < 0) {
						// 如果异常信息中没有出现单据号,则重组异常信息
						sb.append((i + 1)
								+ ":"
								+ ResHelper.getString("6009tran",
										"06009tran0167"/*
														 * @res
														 * "单据{0}由于如下未知异常[{1}]不能成功执行,具体异常信息请查看日志."
														 */, billcode,
										e.getMessage()) /*
														 * + " <br>"
														 */);
					} else {
						sb.append((i + 1) + ":" + e.getMessage() /* + "<br>" */);
					}
				}
				continue;
			}
			try {
				AggStapply agg = voucherbo.queryByPk(aggs[i].getParentVO()
						.getPrimaryKey());
				getRdsService().pushWorkflow_RequiresNew(billtype, agg);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		// 执行完成后,发送通知
		// 1001Z7PSN00000000004
		// 1001Z7PSN00000000005
		// 1)发送调配的通知,按照调配后组织发送
		// key-org value-bill_list
		HashMap<String, ArrayList<AggStapply>> hmTrans = new HashMap<String, ArrayList<AggStapply>>();
		for (AggStapply bill : transBill) {
			String pk_org = (String) bill.getParentVO().getAttributeValue(
					StapplyVO.PK_HI_ORG);
			if (hmTrans.get(pk_org) == null) {
				hmTrans.put(pk_org, new ArrayList<AggStapply>());
			}
			hmTrans.get(pk_org).add(bill);
		}
		for (String key : hmTrans.keySet()) {
			if (hmTrans.get(key) == null || hmTrans.get(key).size() <= 0) {
				continue;
			}

			// 为调配单据时
			String tempCode = HICommonValue.msgcode_trns;// 调配直批通知消息模板源编码
			HiSendMsgHelper.sendMessage1(tempCode,
					hmTrans.get(key).toArray(new AggStapply[0]), key);
		}
		// 1)发送离职的通知,按照调配后组织发送
		// key-org value-bill_list
		HashMap<String, ArrayList<AggStapply>> hmDimis = new HashMap<String, ArrayList<AggStapply>>();
		for (AggStapply bill : dimisBill) {
			String pk_org = (String) bill.getParentVO().getAttributeValue(
					StapplyVO.PK_HI_ORG);
			if (hmDimis.get(pk_org) == null) {
				hmDimis.put(pk_org, new ArrayList<AggStapply>());
			}
			hmDimis.get(pk_org).add(bill);
		}
		for (String key : hmDimis.keySet()) {
			if (hmDimis.get(key) == null || hmDimis.get(key).size() <= 0) {
				continue;
			}

			// 为离职单据时
			String tempCode = HICommonValue.msgcode_dimission;// 离职直批通知消息模板编码
			HiSendMsgHelper.sendMessage1(tempCode,
					hmDimis.get(key).toArray(new AggStapply[0]), key);
		}
		// end
		String msg = sb.length() == 0 ? "" : sb.toString();
		result.put(TRNConst.RESULT_MSG,
				isRunBackgroundTask ? msg : msg.replaceAll("<br>", '\n' + ""));
		transBill.addAll(dimisBill);
		result.put(TRNConst.RESULT_BILLS, transBill);
		return result;
	}

	private boolean isExit(AggregatedValueObject[] retVOs, AggStapply aggStapply)
			throws BusinessException {
		for (int i = 0; retVOs != null && i < retVOs.length; i++) {
			if (aggStapply.getParentVO().getPrimaryKey()
					.equals(retVOs[i].getParentVO().getPrimaryKey())) {
				return true;
			}
		}
		return false;
	}

	private IRdsManageService getRdsService() {
		return NCLocator.getInstance().lookup(IRdsManageService.class);
	}
}
