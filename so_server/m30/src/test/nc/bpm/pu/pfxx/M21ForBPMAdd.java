package nc.bpm.pu.pfxx;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.pu.m21.maintain.OrderSaveBP;
import nc.bs.pu.m21.maintain.rule.SupplierFrozeChkRule;
import nc.bs.pu.m21.plugin.OrderPluginPoint;
import nc.impl.pu.m21.action.rule.approve.ApproveAfterEventRule;
import nc.impl.pu.m21.action.rule.approve.ApproveBeforeEventRule;
import nc.impl.pu.m21.action.rule.approve.ApproveBudgetCtrlRule;
import nc.impl.pu.m21.action.rule.approve.ApproveSupplyRule;
import nc.impl.pu.m21.action.rule.approve.ApproveVOValidateRule;
import nc.impl.pu.m21.action.rule.approve.FillNcaninnumRule;
import nc.impl.pu.m21.action.rule.approve.FilterOrderByStatusRule;
import nc.impl.pu.m21.action.rule.approve.InsertPayPlanRule;
import nc.impl.pu.m21.action.rule.approve.InsertStatusOnWayRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.uap.pf.IplatFormEntry;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m21.context.OrderContext;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderPaymentVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.m21.rule.PaymentInfo;
import nc.vo.pu.m21.rule.RelationCalculate;
import nc.vo.pu.pub.enumeration.POEnumBillStatus;
import nc.vo.pu.pub.enumeration.PuBusiLogActionCode;
import nc.vo.pu.pub.enumeration.PuBusiLogPathCode;
import nc.vo.pu.pub.rule.busilog.WriteOperateLogRule;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 
 * @author zhw
 * 
 */

public class M21ForBPMAdd extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������

		// 1.�õ�ת�����VO����,ȡ�����򵼵�һ��ע���VO��Ϣ
		AggregatedValueObject resvo = (AggregatedValueObject) vo;
		// 2. У�����ݵĺϷ���:1.���ݽṹ���� 2.������֯+���ݺ�У���Ƿ��ظ�.
		checkData(resvo);
		// 3.��ȫ����,���ҵ�������״̬
		fillData(resvo);
		//
		AggregatedValueObject bill2 = insert(resvo);

		return bill2.getParentVO().getPrimaryKey();
	}

	protected AggregatedValueObject insert(AggregatedValueObject vo) throws BusinessException {
		OrderVO order = (OrderVO) vo;
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

	private AggregatedValueObject save(AggregatedValueObject updatevo,
			AggregatedValueObject origVO) {
		OrderVO[] returnVOs = new OrderSaveBP(new OrderContext()).save(
				new OrderVO[] { (OrderVO) updatevo },
				new OrderVO[] { (OrderVO) origVO });
		return returnVOs[0];
	}

	private OrderVO approve(AggregatedValueObject vo, String approver,
			UFDate taudittime) throws BusinessException {
		AroundProcesser<OrderVO> processer = new AroundProcesser<OrderVO>(
				OrderPluginPoint.APPROVE);
		OrderVO order = (OrderVO) vo;
		OrderVO [] vos = new OrderVO[] { order };
		this.addBeforeRule(processer);
		this.addAfterRule(processer);
		// ǰ����
		processer.before(vos);
		
		OrderHeaderVO hvo = order.getHVO();
		hvo.setApprover(approver);
		hvo.setTaudittime(taudittime);
		
		IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator
				.getInstance().lookup(IplatFormEntry.class.getName());
		Object retObj = iIplatFormEntry.processAction("APPROVE", "21", null,
				order, null, null);
		
//		approveImpl.approve(vos, null);
		processer.after(vos);
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

		processer.addAfterRule(new FilterOrderByStatusRule(
				POEnumBillStatus.APPROVE.toInt()));
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

	private void fillData(AggregatedValueObject resvo) {
		// TODO �Զ����ɵķ������
		// ��ȫ������Ϣ��BPM��������������ȫ���������Ƽ�����
		OrderVO bill = (OrderVO) resvo;
		OrderHeaderVO parentVO = bill.getHVO();
		parentVO.setStatus(VOStatus.NEW);
		OrderItemVO[] bvos = bill.getBVO();
		// ����״̬������
		parentVO.setForderstatus(POEnumBillStatus.FREE.toInteger());

		// ��ͷĬ��ֵ
		parentVO.setIprintcount(0);
		parentVO.setBrefwhenreturn(UFBoolean.FALSE);
		parentVO.setBfrozen(UFBoolean.FALSE);
		parentVO.setBisreplenish(UFBoolean.FALSE);
		parentVO.setBfinalclose(UFBoolean.FALSE);
		parentVO.setBpublish(UFBoolean.FALSE);
		parentVO.setBislatest(UFBoolean.TRUE);
		parentVO.setNversion(1);
		parentVO.setBislatest(UFBoolean.TRUE);
		//
		// --���������Դ��ѯ����
		// <vsourcetrantype>0001A510000000001SOR</vsourcetrantype>
		// <vsourcecode>QG2017051000000014</vsourcecode>
		// <vsourcerowno>10</vsourcerowno>
		if ("20".equalsIgnoreCase(bvos[0].getCsourcetypecode())) {
			String[] formulas = new String[] {
					"vsourcetrantype->getColValue(po_praybill,ctrantypeid ,pk_praybill ,csourceid)",
					"vsourcecode->getColValue(po_praybill,vbillcode ,pk_praybill ,csourceid)",
					"vsourcerowno->getColValue(po_praybill_b,crowno,  pk_praybill_b ,csourcebid)"

			};
			SuperVOUtil.execFormulaWithVOs(bvos, formulas);

		} else if ("30".equalsIgnoreCase(bvos[0].getCsourcetypecode())) {
			String[] formulas = new String[] {
					"vsourcetrantype->getColValue(so_saleorder,ctrantypeid,csaleorderid  ,csourceid)",
					"vsourcecode->getColValue(so_saleorder,vbillcode ,csaleorderid  ,csourceid)",
					"vsourcerowno->getColValue(so_saleorder_b,crowno,  csaleorderbid  ,csourcebid)"

			};
			SuperVOUtil.execFormulaWithVOs(bvos, formulas);
		}
		for (OrderItemVO bvo : bvos) {

			bvo.setStatus(VOStatus.NEW);

			// --������ݱ�ͷ����
			// <dbilldate>2017-05-21 20:20:16</dbilldate>
			// <pk_supplier>1001A5100000000008J1</pk_supplier>
			// <corigcurrencyid>1002Z0100000000001K1</corigcurrencyid>
			bvo.setDbilldate(parentVO.getDbilldate());
			bvo.setPk_supplier(parentVO.getPk_supplier());
			bvo.setCorigcurrencyid(parentVO.getCorigcurrencyid());
			
			//���ȸ��ݵ�λ�����㱨�۵�λ
			bvo.setCqtunitid(bvo.getCunitid());
			bvo.setVqtunitrate(bvo.getVchangerate());
			// --����Ĭ��ֵ
			// <fisactive>0</fisactive>
			bvo.setFisactive(0);
			// <btriatradeflag>N</btriatradeflag>
			bvo.setBtriatradeflag(UFBoolean.FALSE);
			// <bborrowpur>N</bborrowpur>
			bvo.setBborrowpur(UFBoolean.FALSE);
			// <binvoiceclose>N</binvoiceclose>
			bvo.setBinvoiceclose(UFBoolean.FALSE);
			// <barriveclose>N</barriveclose>
			bvo.setBarriveclose(UFBoolean.FALSE);
			// <bpayclose>N</bpayclose>
			bvo.setBpayclose(UFBoolean.FALSE);
			// <breceiveplan>N</breceiveplan>
			bvo.setBreceiveplan(UFBoolean.FALSE);
			// <blargess>N</blargess>
			bvo.setBlargess(UFBoolean.FALSE);
			// <btransclosed>N</btransclosed>
			bvo.setBtransclosed(UFBoolean.FALSE);

			// ���ͬ����ҵ��Դֻ���빺��Эͬ���۶���
			// <vfirsttrantype>0001A510000000001SOR</vfirsttrantype>
			bvo.setVfirsttrantype(bvo.getVsourcetrantype());
			// <cfirsttypecode>20</cfirsttypecode>
			bvo.setCfirsttypecode(bvo.getCsourcetypecode());
			// <cfirstid>1001A41000000000BOZQ</cfirstid>
			bvo.setCfirstid(bvo.getCsourceid());
			// <cfirstbid>1001A41000000000BOZR</cfirstbid>
			bvo.setCfirstbid(bvo.getCsourcebid());
			// <vfirstcode>QG2017051000000014</vfirstcode>
			bvo.setVfirstcode(bvo.getVsourcecode());
			// <vfirstrowno>10</vfirstrowno>
			bvo.setVfirstrowno(bvo.getVsourcerowno());

			
		}
		// --�������еĵ��ۺ��Խ����Ϣ
		for (int i = 0; i < bvos.length; i++) {
			calculate(bill, i);
		}

		// ���ݱ�ͷ�ĸ���Э����������ȫ����ĸ���Э��
		paymentInfor(bill);
	}

	private void paymentInfor(OrderVO bill) {
		// TODO �Զ����ɵķ������
		OrderHeaderVO hvo = bill.getHVO();
		String payterm = hvo.getPk_payterm();
		if (payterm != null) {
			OrderPaymentVO[] paymentVOs = PaymentInfo
					.getOrderPaymentVOs(payterm);
			bill.setChildren(OrderPaymentVO.class, paymentVOs);
		}
	}

	private void calculate(OrderVO vo, int row) {
		RelationCalculate cal = new RelationCalculate();
		//
		cal.calculate(vo, "nnum");
		cal.calculate(vo, "norigtaxmny");
	}

	private boolean isFixUnitRate(String material, String cunitid,
			String castunitid) {
		boolean flag = true;
		if (material == null || cunitid == null || castunitid == null) {
			return flag;
		}
		flag = MaterialPubService.queryIsFixedRateByMaterialAndMeasdoc(
				material, cunitid, castunitid);
		return flag;
	}

	private void checkData(AggregatedValueObject resvo)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("δ��ȡ��ת���������");
		if (resvo.getChildrenVO() == null || resvo.getChildrenVO().length == 0) {
			throw new BusinessException("���岻����Ϊ��");
		}
		OrderVO order = (OrderVO) resvo;
		String approver = order.getHVO().getApprover();
		if (approver == null || approver.isEmpty()) {
			ExceptionUtils.wrappBusinessException(" û��������:approver");
		}

		checkMny(resvo);

	}

	private void checkMny(AggregatedValueObject vo) {
		OrderItemVO[] bodyvos = ((OrderVO) vo).getBVO();
		for (OrderItemVO orderItemVO : bodyvos) {
			Integer fbuysellflag = orderItemVO.getFbuysellflag();

			if (BuySellFlagEnum.IMPORT.value().equals(fbuysellflag)
					|| BuySellFlagEnum.NATIONAL_BUY.value()
							.equals(fbuysellflag)) {
				return;
			} else {
				ExceptionUtils
						.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes()
								.getStrByID(
										"4004000_0",
										"04004000-0139",
										null,
										new String[] { orderItemVO.getCrowno() })/*
																				 * @
																				 * res
																				 * "��:{0}�ɹ��������幺���������ò���ȷ��"
																				 */);
			}
		}

	}

}
