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
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * Ӧ�� ���
 */
public class BpmArapExpPfxxPlugin<T extends BaseAggVO> extends
		nc.bs.pfxx.plugin.AbstractPfxxPlugin {

	private PfUserObject[] userObjs;

	/**
	 * ����XMLת��������VO����NCϵͳ��ҵ����ʵ�ִ˷������ɡ�<br>
	 * ��ע�⣬ҵ�񷽷���У��һ��Ҫ���
	 * 
	 * @param vo
	 *            ת�����vo���ݣ���NCϵͳ�п���ΪValueObject,SuperVO,AggregatedValueObject,
	 *            IExAggVO�ȡ�
	 * @param swapContext
	 *            ���ֽ�����������֯�����ܷ������ͷ������׵ȵ�
	 * @param aggxsysvo
	 *            ������Ϣvo
	 * @return
	 * @throws BusinessException
	 */
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggxsysvo) throws BusinessException {

		BaseAggVO newBill = null;
		// 1.�õ�ת�����VO����,ȡ�����򵼵�һ��ע���VO��Ϣ
		BaseAggVO bill = (BaseAggVO) vo;
		checkData(bill.getHeadVO());
		// ���õ�ǰ������
		InvocationInfoProxy.getInstance().setUserId(
				bill.getHeadVO().getBillmaker());
		BaseBillVO head = setHeaderDefault(bill.getHeadVO());
		setBodyDefault(head, (BaseItemVO[]) bill.getChildrenVO());
		BillMoneyVUtils
				.sumBodyToHead(head, (BaseItemVO[]) bill.getChildrenVO());

		// 2.��ѯ�˵����Ƿ��Ѿ��������
		String oldPk = PfxxPluginUtils.queryBillPKBeforeSaveOrUpdate(
				swapContext.getBilltype(), swapContext.getDocID());
		if (oldPk != null) {
			// ����жϣ�����ƽ̨�Ѿ�������������ѵ��룬��replace="N"����ôƽ̨�ͻ��׳��쳣����ʾ�����ظ�
			if (swapContext.getReplace().equalsIgnoreCase("N"))
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("2006pub_0", "02006pub-0319")/*
																 * @res
																 * "�������ظ����뵥�ݣ������Ƿ��ǲ����������������ѵ��뵥�ݣ���������ļ���replace��־��Ϊ��Y��"
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
									"02006pub-0320")/* @res "����״̬�����ڣ�" */);
				}
				if (((BaseBillVO) preVO.getParentVO()).getBillstatus() == BillEnumCollection.BillSatus.Audit.VALUE) {
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("2006pub_0",
									"02006pub-0321")/* @res "�����Ѿ���ˣ��������ظ����뵥�ݡ�" */);
				}

				NCLocator.getInstance().lookup(IArapBillPubService.class)
						.delete(preVO);

			}
		}

		ArapExpPfxxValidater.getInstance().validate(bill);

		ArapVOUtils.validateVoCopyRed(bill);

		head.setBillno(null); // ���billno���Ա����������ɣ������ظ�

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
		// TODO �Զ����ɵķ������
		if (head.getPk_billtype() == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006pub_0", "02006pub-0316")/*
																			 * @res
																			 * "���ݵĵ������ͱ����ֶβ���Ϊ�գ�������ֵ"
																			 */);
		}
		if (!head.getPk_billtype().equals(IArapBillTypeCons.F0)
				&& !head.getPk_billtype().equals(IArapBillTypeCons.F1)
				&& !head.getPk_billtype().equals(IArapBillTypeCons.F2)
				&& !head.getPk_billtype().equals(IArapBillTypeCons.F3)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006pub_0", "02006pub-0650")/*
																			 * @res
																			 * "���ݵĵ������ͱ����ֶδ���"
																			 */);
		}
		if (head.getPk_tradetype() == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006pub_0", "02006pub-0649")/*
																			 * @res
																			 * "���ݵĽ������ͱ����ֶβ���Ϊ�գ�������ֵ"
																			 */);
		}
		if (head.getPk_group() == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006pub_0", "02006pub-0317")/*
																			 * @res
																			 * "���ݵ����������ֶβ���Ϊ�գ�������ֵ"
																			 */);
		}
		if (head.getPk_org() == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006pub_0", "02006pub-0318")/*
																			 * @res
																			 * "���ݵĲ�����֯�ֶβ���Ϊ�գ�������ֵ"
																			 */);
		}
		if (head.getBillmaker() == null) {
			throw new BusinessException("billmaker����Ϊ��");
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
	 * ���ñ�ͷĬ����Ϣ
	 * 
	 * @param headerVo
	 * @return
	 * @throws BusinessException
	 */
	private BaseBillVO setHeaderDefault(BaseBillVO head)
			throws BusinessException {
		Integer ZERO = Integer.valueOf(0);
		/* ����״̬Ϊδ��� */
		head.setBillstatus(BillEnumCollection.BillSatus.Save.VALUE);
		head.setEffectstatus(BillEnumCollection.InureSign.NOINURE.VALUE);
		head.setDr(ZERO);
		// ��Դϵͳ���ⲿ����ƽ̨
		head.setSrc_syscode(BillEnumCollection.FromSystem.WBJHPT.VALUE);
		head.setCreationtime(new UFDateTime());
		head.setCoordflag(null);

		// ���û����ͻ���ڼ䡣���������ڲ鲻������ڼ䣬�򲶻��쳣����������
		try {
			AccountCalendar ac = AccountCalendar.getInstanceByPk_org(head
					.getPk_org());
			ac.setDate(head.getBilldate());
			head.setBillyear(ac.getYearVO().getPeriodyear());
			head.setBillperiod(ac.getMonthVO().getAccperiodmth());
		} catch (BusinessException ex) {
		}

		// ���ý�������pk
		head.setPk_tradetypeid(PfDataCache.getBillTypeInfo(head.getPk_group(),
				head.getPk_tradetype()).getPk_billtypeid());
		// ����ҵ������
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
					"2006pub_0", "02006pub-0127")/* @res "��������" */
					+ head.getPk_tradetype()
					+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"2006pub_0", "02006pub-0239")/*
														 * @res
														 * "û���ҵ���Ӧ������,����[ҵ��������]����"
														 */
					+ head.getPk_tradetype()
					+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"2006pub_0", "02006pub-0240")/* @res "��������" */;
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
		/* ���Ƶ�λ */
		item.setPk_org(head.getPk_org());
		item.setDr(Integer.valueOf(0));
		item.setPk_tradetypeid(PfDataCache.getBillTypeInfo(head.getPk_group(),
				head.getPk_tradetype()).getPk_billtypeid());
		item.setCoordflag(null);
	}

}