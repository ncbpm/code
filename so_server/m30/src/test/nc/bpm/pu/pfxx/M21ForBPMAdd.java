package nc.bpm.pu.pfxx;

import nc.bs.pu.m21.maintain.OrderSaveBP;
import nc.bs.pu.m21.maintain.rule.SupplierFrozeChkRule;
import nc.bs.pu.m21.plugin.OrderPluginPoint;
import nc.impl.pu.m21.action.OrderDeleteAction;
import nc.impl.pu.m21.action.rule.approve.ApproveAfterEventRule;
import nc.impl.pu.m21.action.rule.approve.ApproveBeforeEventRule;
import nc.impl.pu.m21.action.rule.approve.ApproveBudgetCtrlRule;
import nc.impl.pu.m21.action.rule.approve.ApproveSupplyRule;
import nc.impl.pu.m21.action.rule.approve.ApproveVOValidateRule;
import nc.impl.pu.m21.action.rule.approve.FillNcaninnumRule;
import nc.impl.pu.m21.action.rule.approve.FilterOrderByStatusRule;
import nc.impl.pu.m21.action.rule.approve.InsertPayPlanRule;
import nc.impl.pu.m21.action.rule.approve.InsertStatusOnWayRule;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.vo.pu.m21.context.OrderContext;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.pfxx.plugins.AbstractPuPfxxPlugin;
import nc.vo.pu.pub.enumeration.POEnumBillStatus;
import nc.vo.pu.pub.enumeration.PuBusiLogActionCode;
import nc.vo.pu.pub.enumeration.PuBusiLogPathCode;
import nc.vo.pu.pub.rule.busilog.WriteOperateLogRule;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmf.pub.util.BatchNCBaseTypeUtils;

import org.apache.commons.lang.ArrayUtils;


/**
 * 
 * @author zhw
 *
 */

public class M21ForBPMAdd extends AbstractPuPfxxPlugin {


	private AggregatedValueObject save(AggregatedValueObject updatevo,
			AggregatedValueObject origVO) {
		OrderVO[] returnVOs = new OrderSaveBP(new OrderContext()).save(
				new OrderVO[] { (OrderVO) updatevo },
				new OrderVO[] { (OrderVO) origVO });
		return returnVOs[0];
	}

	/**
	 * ����vo�Ƿ�ɵ���
	 * 
	 * @param vo
	 */
	@Override
	protected void checkCanInster(AggregatedValueObject vo) {
		OrderVO order = (OrderVO) vo;
		int status = order.getHVO().getForderstatus();
		if (POEnumBillStatus.APPROVE.toInt() == status) {
			String approver = order.getHVO().getApprover();
			if (approver == null || approver.isEmpty()) {
				ExceptionUtils.wrappBusinessException("����ʧ�ܣ�����̬���ݲ���û�������ˣ�");
			}
			UFDate ufDate = order.getHVO().getTaudittime();
			if (ufDate == null) {
				ExceptionUtils.wrappBusinessException("����ʧ�ܣ�����̬�����������ڲ���Ϊ�գ�");
			}
			this.checkMny(vo);
			// this.checkRwite(vo);
		} else {
			super.checkCanInster(vo);
			this.checkMny(vo);
			this.checkRwite(vo);
		}
	}

	@Override
	protected void deleteVO(AggregatedValueObject vo) {
		new OrderDeleteAction().delete(new OrderVO[] { (OrderVO) vo }, null);
	}

	@Override
	protected String getBillStatusKey() {
		return OrderHeaderVO.FORDERSTATUS;
	}

	@Override
	protected String getChildrenPkFiled() {
		return OrderItemVO.PK_ORDER_B;
	}

	@Override
	protected String getParentPkFiled() {
		return OrderHeaderVO.PK_ORDER;
	}

	@Override
	protected AggregatedValueObject insert(AggregatedValueObject vo) {
		if (vo != null) {
			this.checkCanInster(vo);
		}
		OrderVO order = (OrderVO) vo;
		int status = order.getHVO().getForderstatus();
		if (POEnumBillStatus.APPROVE.toInt() == status) {
			OrderHeaderVO hvo = order.getHVO();
			String approver = hvo.getApprover();
			hvo.setApprover(null);
			hvo.setForderstatus(POEnumBillStatus.FREE.toInt());
			UFDate taudittime = hvo.getTaudittime();
			hvo.setTaudittime(null);
			AggregatedValueObject save = this.save(vo, null);
			OrderVO orderVO = this.approve(save, approver, taudittime);
			return orderVO;
		}
		return this.save(vo, null);
	}

	private OrderVO approve(AggregatedValueObject vo, String approver,
			UFDate taudittime) {
		AroundProcesser<OrderVO> processer = new AroundProcesser<OrderVO>(
				OrderPluginPoint.APPROVE);
		OrderVO order = (OrderVO) vo;
		this.addBeforeRule(processer);
		this.addAfterRule(processer);
		// ǰ����
		processer.before(new OrderVO[] { order });
		OrderHeaderVO hvo = order.getHVO();
		hvo.setApprover(approver);
		hvo.setForderstatus(POEnumBillStatus.APPROVE.toInt());
		hvo.setTaudittime(taudittime);
		processer.after(new OrderVO[] { order });
		return order;
	}

	private void addBeforeRule(AroundProcesser<OrderVO> processer) {
		// ���VO״̬���
		processer.addBeforeRule(new ApproveVOValidateRule());
		// ��Ӧ�̶�����
		processer.addBeforeRule(new SupplierFrozeChkRule());
		// ����ǰ�¼�
		processer.addBeforeRule(new ApproveBeforeEventRule());
	}

	private void addAfterRule(AroundProcesser<OrderVO> processer) {
		// дҵ����־Ҫ�ŵ�FilterOrderByStatusRule��ǰ�棬����vo�п��ܻᱻ���˵�����û����¼��־��
		processer.addAfterRule(new WriteOperateLogRule<OrderVO>(
				PuBusiLogPathCode.orderApprovePath.getCode(),
				PuBusiLogActionCode.approve.getCode()));

		processer.addAfterRule(new FilterOrderByStatusRule(POEnumBillStatus.APPROVE
				.toInt()));
		processer.addAfterRule(new ApproveSupplyRule());
		// �ɹ��ƻ����
		processer.addAfterRule(new ApproveBudgetCtrlRule());
		// ����;״̬���в�������
		processer.addAfterRule(new InsertStatusOnWayRule());
		processer.addAfterRule(new InsertPayPlanRule());
		// ���ÿ����������Ϊ��˺�ֱ������ⵥ�ṩ֧��
		processer.addAfterRule(new FillNcaninnumRule());
		// �������¼�
		processer.addAfterRule(new ApproveAfterEventRule());
	}

	@Override
	protected AggregatedValueObject queryVOByPk(String voPk) {
		BillQuery<OrderVO> billquery = new BillQuery<OrderVO>(OrderVO.class);
		OrderVO[] vos = billquery.query(new String[] { voPk });

		if (ArrayUtils.isEmpty(vos)) {
			return null;
		}
		return vos[0];
	}

	@Override
	protected AggregatedValueObject update(AggregatedValueObject updatevo,
			AggregatedValueObject origVO) {
		return this.save(updatevo, origVO);
	}

	private void checkMny(AggregatedValueObject vo) {
		StringBuilder errrows = new StringBuilder();
		OrderItemVO[] bodyvos = ((OrderVO) vo).getBVO();
		for (OrderItemVO orderItemVO : bodyvos) {
			Integer fbuysellflag = orderItemVO.getFbuysellflag();
			// ��˰���
			UFDouble nmny = orderItemVO.getNmny();
			// ��˰�ϼ�
			UFDouble ntaxmny = orderItemVO.getNtaxmny();
			// ˰��
			UFDouble ntax = orderItemVO.getNtax();
			// ���ڲɹ�
			if (BuySellFlagEnum.IMPORT.value().equals(fbuysellflag)) {
				if (!nmny.equals(ntaxmny)) {
					errrows.append(orderItemVO.getCrowno() + ",");
				}
			}
			// ���ڲɹ�
			else if (BuySellFlagEnum.NATIONAL_BUY.value().equals(fbuysellflag)) {
				if (!nmny.add(ntax).equals(ntaxmny)) {
					errrows.append(orderItemVO.getCrowno() + ",");
				}
			} else {
				ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("4004000_0", "04004000-0139", null,
								new String[] { orderItemVO.getCrowno() })/*
																													 * @res
																													 * "��:{0}�ɹ��������幺���������ò���ȷ��"
																													 */);
			}
		}
		if (errrows.length() <= 0) {
			return;
		}
		errrows.deleteCharAt(errrows.length() - 1);
		ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
				.getNCLangRes().getStrByID("4004000_0", "04004000-0137", null,
						new String[] { errrows.toString() })/*
																								 * @res
																								 * "��{0}��˰��˰���˰�ϼƼ����ϵ���󲻿ɵ��룡"
																								 */);
	}

	/**
	 * �������λ�д��Ϣ�ĵ��ݿ��Ʋ��ɵ���
	 * 
	 * @param vo
	 */
	private void checkRwite(AggregatedValueObject vo) {
		OrderItemVO[] bodyvos = ((OrderVO) vo).getBVO();
		for (OrderItemVO orderItemVO : bodyvos) {
			StringBuilder errstr = new StringBuilder();
			// �ۼ��Ѻ������ҿ�Ʊ���
			UFDouble nacccancelinvmny = orderItemVO.getNacccancelinvmny();
			if (!BatchNCBaseTypeUtils.isNullOrZero(nacccancelinvmny)) {
				errstr.append(OrderItemVO.NACCCANCELINVMNY + ",");
			}
			// �ۼƱ��ҿ�Ʊ���
			UFDouble naccuminvoicemny = orderItemVO.getNaccuminvoicemny();
			if (!BatchNCBaseTypeUtils.isNullOrZero(naccuminvoicemny)) {
				errstr.append(OrderItemVO.NACCUMINVOICEMNY + ",");
			}
			// �����ۼƿ�Ʊ���
			UFDouble nfeemny = orderItemVO.getNfeemny();
			if (!BatchNCBaseTypeUtils.isNullOrZero(nfeemny)) {
				errstr.append(OrderItemVO.NFEEMNY + ",");
			}

			// �ۼƵ���������
			UFDouble naccumarrvnum = orderItemVO.getNaccumarrvnum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(naccumarrvnum)) {
				errstr.append(OrderItemVO.NACCUMARRVNUM + ",");
			}
			// �ۼ����������
			UFDouble naccumstorenum = orderItemVO.getNaccumstorenum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(naccumstorenum)) {
				errstr.append(OrderItemVO.NACCUMSTORENUM + ",");
			}
			// �ۼƿ�Ʊ������
			UFDouble naccuminvoicenum = orderItemVO.getNaccuminvoicenum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(naccuminvoicenum)) {
				errstr.append(OrderItemVO.NACCUMINVOICENUM + ",");
			}
			// �ۼ�;��������
			UFDouble naccumwastnum = orderItemVO.getNaccumwastnum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(naccumwastnum)) {
				errstr.append(OrderItemVO.NACCUMWASTNUM + ",");
			}
			// �ۼƵ����ƻ�������
			UFDouble naccumrpnum = orderItemVO.getNaccumrpnum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(naccumrpnum)) {
				errstr.append(OrderItemVO.NACCUMRPNUM + ",");
			}
			// �ۼ�����������
			UFDouble naccumdevnum = orderItemVO.getNaccumdevnum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(naccumdevnum)) {
				errstr.append(OrderItemVO.NACCUMDEVNUM + ",");
			}

			// �ۼ��˻�������
			UFDouble nbackarrvnum = orderItemVO.getNbackarrvnum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(nbackarrvnum)) {
				errstr.append(OrderItemVO.NBACKARRVNUM + ",");
			}
			// �ۼ��˿�������
			UFDouble nbackstorenum = orderItemVO.getNbackstorenum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(nbackstorenum)) {
				errstr.append(OrderItemVO.NBACKSTORENUM + ",");
			}

			if (errstr.length() <= 0) {
				return;
			}
			errstr.deleteCharAt(errstr.length() - 1);
			ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4004000_0", "04004000-0138", null,
							new String[] { orderItemVO.getCrowno(), errstr.toString() })/*
																																					 * @res
																																					 * "��:{0}���ڻ�д����[{1}],���ɵ��룡"
																																					 */);
		}
	}
}
