package nc.bs.arap.plugin;

import nc.bs.arap.util.ArapFlowUtil;
import nc.bs.arap.util.ArapVOUtils;
import nc.bs.arap.util.BillMoneyVUtils;
import nc.bs.arap.util.IArapBillTypeCons;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.pfxx.ISwapContext;
import nc.itf.uap.pf.IPFConfig;
import nc.md.persist.framework.MDPersistenceService;
import nc.pubitf.accperiod.AccountCalendar;
import nc.pubitf.arap.bill.IArapBillPubService;
import nc.vo.arap.basebill.BaseAggVO;
import nc.vo.arap.basebill.BaseBillVO;
import nc.vo.arap.basebill.BaseItemVO;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.arap.utils.ArrayUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pflow.PfUserObject;
import nc.vo.pubapp.util.NCPfServiceUtils;

import org.apache.commons.lang.StringUtils;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * 应付 付款单
 */
public class BpmArapExpPfxxPlugin<T extends BaseAggVO> extends
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
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggxsysvo) throws BusinessException {

		BaseAggVO newBill = null;
		// 1.得到转换后的VO数据,取决于向导第一步注册的VO信息
		BaseAggVO bill = (BaseAggVO) vo;
		checkData(bill.getHeadVO());
		// 设置当前操作人
		InvocationInfoProxy.getInstance().setUserId(
				bill.getHeadVO().getBillmaker());
		BaseBillVO head = setHeaderDefault(bill.getHeadVO());
		setBodyDefault(head, (BaseItemVO[]) bill.getChildrenVO());
		BillMoneyVUtils
				.sumBodyToHead(head, (BaseItemVO[]) bill.getChildrenVO());

		// 2.查询此单据是否已经被导入过
		String oldPk = PfxxPluginUtils.queryBillPKBeforeSaveOrUpdate(
				swapContext.getBilltype(), swapContext.getDocID());
		if (oldPk != null) {
			// 这个判断，好像平台已经过，如果单据已导入，且replace="N"，那么平台就会抛出异常，提示不可重复
			if (swapContext.getReplace().equalsIgnoreCase("N"))
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("2006pub_0", "02006pub-0319")/*
																 * @res
																 * "不允许重复导入单据，请检查是否是操作错误；如果想更新已导入单据，请把数据文件的replace标志设为‘Y’"
																 */);

			BaseAggVO preVO = null;
			if (head.getPk_billtype().equals(IArapBillTypeCons.F2)) {
				preVO = MDPersistenceService.lookupPersistenceQueryService()
						.queryBillOfVOByPK(AggGatheringBillVO.class, oldPk,
								false);
			} else if (head.getPk_billtype().equals(IArapBillTypeCons.F3)) {
				preVO = MDPersistenceService.lookupPersistenceQueryService()
						.queryBillOfVOByPK(AggPayBillVO.class, oldPk, false);
			} else if (head.getPk_billtype().equals(IArapBillTypeCons.F0)) {
				preVO = MDPersistenceService.lookupPersistenceQueryService()
						.queryBillOfVOByPK(AggReceivableBillVO.class, oldPk,
								false);
			} else if (head.getPk_billtype().equals(IArapBillTypeCons.F1)) {
				preVO = MDPersistenceService
						.lookupPersistenceQueryService()
						.queryBillOfVOByPK(AggPayableBillVO.class, oldPk, false);
			}

			if (preVO != null && preVO.getParentVO() != null) {

				if (((BaseBillVO) preVO.getParentVO()).getBillstatus() == null) {
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("2006pub_0",
									"02006pub-0320")/* @res "单据状态不存在！" */);
				}
				if (((BaseBillVO) preVO.getParentVO()).getBillstatus() == BillEnumCollection.BillSatus.Audit.VALUE) {
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("2006pub_0",
									"02006pub-0321")/* @res "单据已经审核，不允许重复导入单据。" */);
				}

				NCLocator.getInstance().lookup(IArapBillPubService.class)
						.delete(preVO);

			}
		}

		ArapExpPfxxValidater.getInstance().validate(bill);

		ArapVOUtils.validateVoCopyRed(bill);

		head.setBillno(null); // 清空billno，以便于重新生成，避免重复

		newBill = this.insertBill(bill);

		String pk = null;
		if (newBill != null) {
			pk = newBill.getParent().getPrimaryKey();
		}
		if (oldPk != null) {
			PfxxPluginUtils.deleteIDvsPKByDocPK(oldPk);
		}
		PfxxPluginUtils.addDocIDVsPKContrast(swapContext.getBilltype(),
				swapContext.getDocID(), pk);
		return pk;
	}

	private void checkData(BaseBillVO head) throws BusinessException {
		// TODO 自动生成的方法存根
		if (head.getPk_billtype() == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006pub_0", "02006pub-0316")/*
																			 * @res
																			 * "单据的单据类型编码字段不能为空，请输入值"
																			 */);
		}
		if (!head.getPk_billtype().equals(IArapBillTypeCons.F0)
				&& !head.getPk_billtype().equals(IArapBillTypeCons.F1)
				&& !head.getPk_billtype().equals(IArapBillTypeCons.F2)
				&& !head.getPk_billtype().equals(IArapBillTypeCons.F3)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006pub_0", "02006pub-0650")/*
																			 * @res
																			 * "单据的单据类型编码字段错误"
																			 */);
		}
		if (head.getPk_tradetype() == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006pub_0", "02006pub-0649")/*
																			 * @res
																			 * "单据的交易类型编码字段不能为空，请输入值"
																			 */);
		}
		if (head.getPk_group() == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006pub_0", "02006pub-0317")/*
																			 * @res
																			 * "单据的所属集团字段不能为空，请输入值"
																			 */);
		}
		if (head.getPk_org() == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006pub_0", "02006pub-0318")/*
																			 * @res
																			 * "单据的财务组织字段不能为空，请输入值"
																			 */);
		}
		if (head.getBillmaker() == null) {
			throw new BusinessException("billmaker不能为空");
		}
	}

	private BaseAggVO insertBill(BaseAggVO bill) throws BusinessException {
		BaseAggVO res = null;
		if (bill.getHeadVO().getPk_billtype().equals(IArapBillTypeCons.F0)) {
			res = (AggReceivableBillVO) ArrayUtil
					.getFirstInArrays((Object[]) NCPfServiceUtils.processBatch(
							ArapFlowUtil.getCommitActionCode(bill.getHeadVO()
									.getPk_org(), IArapBillTypeCons.F0),
							bill.getHeadVO().getPk_billtype(),
							new AggReceivableBillVO[] { (AggReceivableBillVO) bill },
							getUserObj(), new WorkflownoteVO()));
			res = (AggReceivableBillVO) ArrayUtil
					.getFirstInArrays((Object[]) NCPfServiceUtils.processBatch(
							ArapFlowUtil.getApproveActionCode(res.getHeadVO()
									.getPk_org(), IArapBillTypeCons.F0),
							res.getHeadVO().getPk_billtype(),
							new AggReceivableBillVO[] { (AggReceivableBillVO) res },
							getUserObj(), null));
		} else if (bill.getHeadVO().getPk_billtype()
				.equals(IArapBillTypeCons.F1)) {
			res = (AggPayableBillVO) ArrayUtil
					.getFirstInArrays((Object[]) NCPfServiceUtils.processBatch(
							ArapFlowUtil.getCommitActionCode(bill.getHeadVO()
									.getPk_org(), IArapBillTypeCons.F1), bill
									.getHeadVO().getPk_billtype(),
							new AggPayableBillVO[] { (AggPayableBillVO) bill },
							getUserObj(), new WorkflownoteVO()));
			res = (AggPayableBillVO) ArrayUtil
					.getFirstInArrays((Object[]) NCPfServiceUtils.processBatch(
							ArapFlowUtil.getApproveActionCode(res.getHeadVO()
									.getPk_org(), IArapBillTypeCons.F1), res
									.getHeadVO().getPk_billtype(),
							new AggPayableBillVO[] { (AggPayableBillVO) res },
							getUserObj(), null));
		} else if (bill.getHeadVO().getPk_billtype()
				.equals(IArapBillTypeCons.F2)) {
			res = (AggGatheringBillVO) ArrayUtil
					.getFirstInArrays((Object[]) NCPfServiceUtils.processBatch(
							ArapFlowUtil.getCommitActionCode(bill.getHeadVO()
									.getPk_org(), IArapBillTypeCons.F2),
							bill.getHeadVO().getPk_billtype(),
							new AggGatheringBillVO[] { (AggGatheringBillVO) bill },
							getUserObj(), new WorkflownoteVO()));
			res = (AggGatheringBillVO) ArrayUtil
					.getFirstInArrays((Object[]) NCPfServiceUtils.processBatch(
							ArapFlowUtil.getApproveActionCode(res.getHeadVO()
									.getPk_org(), IArapBillTypeCons.F2),
							res.getHeadVO().getPk_billtype(),
							new AggGatheringBillVO[] { (AggGatheringBillVO) res },
							getUserObj(), null));
		} else if (bill.getHeadVO().getPk_billtype()
				.equals(IArapBillTypeCons.F3)) {
			res = (AggPayBillVO) ArrayUtil
					.getFirstInArrays((Object[]) NCPfServiceUtils.processBatch(
							ArapFlowUtil.getCommitActionCode(bill.getHeadVO()
									.getPk_org(), IArapBillTypeCons.F3), bill
									.getHeadVO().getPk_billtype(),
							new AggPayBillVO[] { (AggPayBillVO) bill },
							getUserObj(), new WorkflownoteVO()));
			res = (AggPayBillVO) ArrayUtil
					.getFirstInArrays((Object[]) NCPfServiceUtils.processBatch(
							ArapFlowUtil.getApproveActionCode(res.getHeadVO()
									.getPk_org(), IArapBillTypeCons.F3), res
									.getHeadVO().getPk_billtype(),
							new AggPayBillVO[] { (AggPayBillVO) res },
							getUserObj(), null));
		}

		return res;
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
	private BaseBillVO setHeaderDefault(BaseBillVO head)
			throws BusinessException {
		Integer ZERO = Integer.valueOf(0);
		/* 单据状态为未审核 */
		head.setBillstatus(BillEnumCollection.BillSatus.Save.VALUE);
		head.setEffectstatus(BillEnumCollection.InureSign.NOINURE.VALUE);
		head.setDr(ZERO);
		// 来源系统是外部交换平台
		head.setSrc_syscode(BillEnumCollection.FromSystem.WBJHPT.VALUE);
		head.setCreationtime(new UFDateTime());
		head.setCoordflag(null);

		// 设置会计年和会计期间。若根据日期查不到会计期间，则捕获异常，不作处理
		try {
			AccountCalendar ac = AccountCalendar.getInstanceByPk_org(head
					.getPk_org());
			ac.setDate(head.getBilldate());
			head.setBillyear(ac.getYearVO().getPeriodyear());
			head.setBillperiod(ac.getMonthVO().getAccperiodmth());
		} catch (BusinessException ex) {
		}

		// 设置交易类型pk
		head.setPk_tradetypeid(PfDataCache.getBillTypeInfo(head.getPk_group(),
				head.getPk_tradetype()).getPk_billtypeid());
		// 设置业务流程
		try {
			IPFConfig ipf = NCLocator.getInstance().lookup(IPFConfig.class);
			if (!StringUtils.isEmpty(head.getPk_billtype())
					&& !StringUtils.isEmpty(head.getPk_tradetype())) {
				if (head.getCreator() == null) {
					head.setCreator(InvocationInfoProxy.getInstance()
							.getUserId());
				}
				String pk_busitype = ipf.retBusitypeCanStart(
						head.getPk_billtype(), head.getPk_tradetype(),
						head.getPk_org(), head.getCreator());
				if (pk_busitype == null) {
					throw new BusinessException("busitype is null");
				}
				head.setPk_busitype(pk_busitype);
			}
		} catch (Exception e) {
			String msg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"2006pub_0", "02006pub-0127")/* @res "交易类型" */
					+ head.getPk_tradetype()
					+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"2006pub_0", "02006pub-0239")/*
														 * @res
														 * "没有找到相应的流程,请在[业务流定义]配置"
														 */
					+ head.getPk_tradetype()
					+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"2006pub_0", "02006pub-0240")/* @res "自制流程" */;
			throw new BusinessRuntimeException(msg);
		}
		return head;
	}

	void setBodyDefault(BaseBillVO head, BaseItemVO[] items)
			throws BusinessException {
		int len = items == null ? 0 : items.length;

		for (int i = 0; i < len; i++) {
			setBodyDefault(head, items[i]);
		}
	}

	void setBodyDefault(BaseBillVO head, BaseItemVO item)
			throws BusinessException {
		/* 复制单位 */
		item.setPk_org(head.getPk_org());
		item.setDr(Integer.valueOf(0));
		item.setPk_tradetypeid(PfDataCache.getBillTypeInfo(head.getPk_group(),
				head.getPk_tradetype()).getPk_billtypeid());
		item.setCoordflag(null);
	}

}