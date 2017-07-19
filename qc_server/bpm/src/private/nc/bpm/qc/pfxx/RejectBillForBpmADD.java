package nc.bpm.qc.pfxx;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.qc.c003.maintain.rule.ReportSendMessageWithPlatTemplateRule;
import nc.bs.qc.c003.maintain.rule.UpdateSnDocRule;
import nc.bs.qc.c004.approve.action.rule.RejectWriteBackRuleForAppr;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.qc.c003.approve.action.rule.CreateStockStateChangeBill;
import nc.impl.qc.c003.approve.action.rule.Write55D2WhenAuditRule;
import nc.impl.qc.c003.approve.action.rule.WriteC001WhenAuditRule;
import nc.impl.qc.c003.approve.action.rule.WriteC005WhenAuditRule;
import nc.impl.qc.c003.approve.action.rule.WriteContinueBatchWhenAuditRule;
import nc.impl.qc.c003.approve.action.rule.WriteICBatchDocWhenAuditRule;
import nc.itf.qc.c003.maintain.IReportMaintain;
import nc.itf.qc.c004.maintain.IRejectMaintain;
import nc.itf.qc.c004.page.IRejectBillMaintainApp;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.AppContext;
import nc.vo.qc.c003.entity.ReportHeaderVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.c003.rule.Write23WhenAuditRule;
import nc.vo.qc.c003.rule.Write4331WhenAuditRule;
import nc.vo.qc.c003.rule.Write4ZRuleWhenAuditRule;
import nc.vo.qc.c003.rule.WriteMMWhenAuditRule;
import nc.vo.qc.c004.entity.RejectBillHeadVO;
import nc.vo.qc.c004.entity.RejectBillItemVO;
import nc.vo.qc.c004.entity.RejectBillVO;
import nc.vo.qc.pub.enumeration.QCBillStatusEnum;
import nc.vo.scmf.qc.dealfashion.entity.DealFashionVO;

import org.apache.commons.lang.StringUtils;

/**
 * ���ϸ�Ʒ����
 * 
 * @author liyf
 * 
 */
public class RejectBillForBpmADD extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		RejectBillVO billVO = (RejectBillVO) vo;
		RejectBillHeadVO headVO = (RejectBillHeadVO) billVO.getParentVO();
		// 1.У�鵼�����������
		checkData(billVO);
		// 2.���²��ϸ�Ʒ���������Զ�����ͨ��
		IRejectBillMaintainApp queryService = NCLocator.getInstance().lookup(
				IRejectBillMaintainApp.class);
		RejectBillVO[] originVOs = queryService
				.queryMC004App(new String[] { headVO.getPk_rejectbill() });
		// ��鲻�ϸ�pk�Ϸ���
		if (originVOs == null || originVOs.length < 1) {
			throw new BusinessException("����ָ����������" + headVO.getPk_rejectbill()
					+ "�Ҳ�����Ӧ�Ĳ��ϸ���,���鱨�Ļ���NC��Ӧ���쵥�Ƿ�ɾ��.");
		}
		// 3.���²��ϸ�Ʒ�����ж���ⷽʽ
		updateRejct(billVO, originVOs[0]);
		// 4. ���Զ�����ͨ�����ϸ�Ʒ����
		// ���²�ѯ����ֹ����
		originVOs = queryService.queryMC004App(new String[] { headVO
				.getPk_rejectbill() });
		approveRejct(billVO, originVOs[0]);
		// 5.�����ʼ챨�� ����״̬
		approveReportBill(billVO, originVOs[0]);

		return "��дNC�ɹ�";
	}

	/**
	 * �ʼ챨�� ����״̬
	 * 
	 * @param billVO
	 * @param rejectBillVO
	 */
	private void approveReportBill(RejectBillVO bpmVO, RejectBillVO rejectBillVO) {
		// TODO �Զ����ɵķ������
		String pk_reportbill = rejectBillVO.getHVO().getPk_reportbill();
		IReportMaintain reportService = NCLocator.getInstance().lookup(
				IReportMaintain.class);
		ReportVO originVO = reportService
				.querySingleBillByPkAndBatchInfo(pk_reportbill);
		ReportVO clientVO = reportService
				.querySingleBillByPkAndBatchInfo(pk_reportbill);
		ReportHeaderVO clientHead = (ReportHeaderVO) clientVO.getParentVO();
		clientHead.setApprover(bpmVO.getHVO().getApprover());
		clientHead.setFbillstatus(QCBillStatusEnum.APPROVE.toIntValue());
		clientHead.setStatus(VOStatus.UPDATED);

		BillUpdate<ReportVO> update = new BillUpdate<ReportVO>();
		ReportVO[] vos = update.update(new ReportVO[] { clientVO },
				new ReportVO[] { originVO });

		// // ���ɿ��״̬��������ֻ�������깤�Ͳɹ����������������ұ��������κŲ�����
		new CreateStockStateChangeBill().process(vos);
		// ��д���쵥�Ƿ�����ͨ��
		new WriteC001WhenAuditRule(UFBoolean.TRUE).process(vos);  
		// ��д�������ļ�����
		new Write23WhenAuditRule(UFBoolean.TRUE).process(vos);
		// ��д���۷������ļ�����
		new Write4331WhenAuditRule(UFBoolean.TRUE).process(vos);
		// ��д��涳��ⶳ�ļ�����
		new Write4ZRuleWhenAuditRule(UFBoolean.TRUE).process(vos);
		// ��д��������ļ�����
		new WriteMMWhenAuditRule(UFBoolean.TRUE).process(vos);
		// ��д�������е�
		new WriteC005WhenAuditRule(UFBoolean.TRUE).process(vos);
		// ��д��д�ʼ�������
		new WriteContinueBatchWhenAuditRule(UFBoolean.TRUE).process(vos);
		// ��������ʱ��д��д������ε����������ȼ����ϴ��ʼ����ڡ��ڼ��־��
		new WriteICBatchDocWhenAuditRule(UFBoolean.TRUE).process(vos);
		// ��д�����깤����
		new Write55D2WhenAuditRule(UFBoolean.TRUE).process(vos);
		new ReportSendMessageWithPlatTemplateRule().process(vos);
		// �������к������ȼ�
		new UpdateSnDocRule(UFBoolean.TRUE).process(vos);

	}

	/**
	 * ����ͨ�����ϸ�Ʒ���� �����ˣ�ͨ��BPM������
	 * 
	 * @param billVO
	 * @throws BusinessException
	 */
	private void approveRejct(RejectBillVO bpmVO, RejectBillVO originVO)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		RejectBillHeadVO headVO = (RejectBillHeadVO) originVO.getParentVO();
		IRejectBillMaintainApp queryService = NCLocator.getInstance().lookup(
				IRejectBillMaintainApp.class);
		// ���²�ѯһ�������޸ģ�ģ��ǰ̨��������VO
		RejectBillVO clientVO = queryService
				.queryMC004App(new String[] { headVO.getPk_rejectbill() })[0];
		// ������ϸ �ж����
		clientVO.getHVO().setApprover(bpmVO.getHVO().getApprover());
		clientVO.getHVO().setFbillstatus(QCBillStatusEnum.APPROVE.toIntValue());
		clientVO.getHVO().setTaudittime(AppContext.getInstance().getBusiDate());
		clientVO.getParentVO().setStatus(VOStatus.UPDATED);

		BillUpdate<RejectBillVO> update = new BillUpdate<RejectBillVO>();
		RejectBillVO[] vos = update.update(new RejectBillVO[] { clientVO },
				new RejectBillVO[] { originVO });
		// ��д�ʼ챨��
		new RejectWriteBackRuleForAppr().process(vos);

	}

	/**
	 * �����ʼ챨����ж���ⷽʽ
	 * 
	 * @param bpmVO
	 * @param originVO
	 * @throws BusinessException
	 */
	private void updateRejct(RejectBillVO bpmVO, RejectBillVO originVO)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		RejectBillHeadVO headVO = (RejectBillHeadVO) originVO.getParentVO();
		IRejectBillMaintainApp queryService = NCLocator.getInstance().lookup(
				IRejectBillMaintainApp.class);
		// ���²�ѯһ�������޸ģ�ģ��ǰ̨��������VO
		RejectBillVO bill = queryService.queryMC004App(new String[] { headVO
				.getPk_rejectbill() })[0];
		// ������ϸ �ж����
		RejectBillItemVO[] modifyItems = (RejectBillItemVO[]) bpmVO
				.getChildrenVO();
		RejectBillItemVO[] clientItems = (RejectBillItemVO[]) bill
				.getChildrenVO();
		for (RejectBillItemVO ele : modifyItems) {
			boolean ismached = false;
			for (RejectBillItemVO org : clientItems) {
				if (ele.getPk_rejectbill_b().equals(org.getPk_rejectbill_b())) {
					org.setFprocessjudge(ele.getFprocessjudge());

					org.setStatus(VOStatus.UPDATED);
					ismached = true;
					break;
				}
			}
			if (!ismached) {
				throw new BusinessException("���ݱ���ָ���ı�������: "
						+ ele.getPk_rejectbill_b() + "δƥ�䵽NC��Ӧ�ı�����.");
			}

		}
		IRejectMaintain service = NCLocator.getInstance().lookup(
				IRejectMaintain.class);
		service.saveBase(new RejectBillVO[] { bill }, new Object(),
				new RejectBillVO[] { originVO });

	}

	private void checkData(RejectBillVO resvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("δ��ȡ��ת���������");
		if (resvo.getChildrenVO() == null || resvo.getChildrenVO().length == 0) {
			throw new BusinessException("���岻����Ϊ��");
		}
		if (StringUtils.isEmpty(resvo.getHVO().getPk_rejectbill())) {
			throw new BusinessException("Pk_rejectbill ����Ϊ��");
		}
		if (StringUtils.isEmpty(resvo.getHVO().getApprover())) {
			throw new BusinessException("approver ����Ϊ��");
		}

		for (RejectBillItemVO item : resvo.getBVO()) {
			if (StringUtils.isEmpty(item.getPk_rejectbill_b())) {
				throw new BusinessException("pk_rejectbill_b ����Ϊ��");
			}

		}

	}

}
