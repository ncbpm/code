package nc.bs.hr.trn.plugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.hr.utils.PubEnv;
import nc.hr.utils.ResHelper;
import nc.itf.hr.frame.IPersistenceRetrieve;
import nc.itf.hr.frame.IPersistenceUpdate;
import nc.itf.trn.IItemSetAdapter;
import nc.itf.trn.TrnDelegator;
import nc.itf.trn.transmng.ITransmngManageService;
import nc.itf.trn.transmng.ITransmngQueryService;
import nc.md.model.IBean;
import nc.vo.arap.utils.ArrayUtil;
import nc.vo.hi.psndoc.CtrtVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
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
import nc.vo.trn.regmng.RegapplyVO;
import nc.vo.trn.transmng.AggStapply;
import nc.vo.trn.transmng.StapplyVO;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

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
	 * @return 调配 离职申请
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

		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		String strCode = formatter.format(new Date());
		head.setBill_code(strCode); //

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
		res = voucherbo.updateBill(res, false);

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

}
