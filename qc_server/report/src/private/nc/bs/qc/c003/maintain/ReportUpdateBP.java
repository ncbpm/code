package nc.bs.qc.c003.maintain;

import nc.bs.qc.c003.maintain.rule.ChkBillCodeUniqueRule;
import nc.bs.qc.c003.maintain.rule.ReportChkChkBatUnique;
import nc.bs.qc.c003.maintain.rule.update.FillUpDataUpdateRule;
import nc.bs.qc.c003.maintain.rule.update.UpdChkBatCode;
import nc.bs.qc.c003.maintain.rule.update.UpdateBatchCodeAfterRule;
import nc.bs.qc.c003.maintain.rule.update.UpdateBatchCodeBeforeRule;
import nc.bs.qc.c003.maintain.rule.update.UpdateBillCodeRule;
import nc.bs.qc.c003.maintain.rule.update.WriteC001WhenUpdateRule;
import nc.bs.qc.c003.plugin.ReportBPPlugInPoint;
import nc.bs.qc.pub.rule.QCRowNoCheckRule;
import nc.impl.pubapp.bd.material.assistant.MarAssistantSaveRule;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pubapp.util.SetUpdateAuditInfoRule;
import nc.vo.qc.c003.entity.ReportHeaderVO;
import nc.vo.qc.c003.entity.ReportItemVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.c003.rule.RejectAfterCheckRule;
import nc.vo.qc.c003.rule.RejectBeforCheckRule;
import nc.vo.qc.c003.rule.ReportItemsCheckRule;
import nc.vo.qc.c003.rule.ReportNumCheckRule;
import nc.vo.qc.c003.util.ReportAddStockStateRule;
import nc.vo.qc.c003.util.ReportVOInfoUtils;
import nc.vo.qc.pub.rule.CheckBillNumRule;
import nc.vo.qc.pub.rule.NoPassUpdateRule;

/**
 * ������������������޸ı��汨�쵥
 * <p>
 * <b>����˵��</b>
 * 
 * @param aggVO����
 *            <p>
 * @since 6.0
 * @author hanbin
 * @time 2010-1-13 ����10:18:08
 */
public class ReportUpdateBP{
	--�ǵúϲ���Ʒ����
	public ReportVO[] updateReport(ReportVO[] voArray, ReportVO[] originBills) {
		CompareAroundProcesser<ReportVO> processer = new CompareAroundProcesser<ReportVO>(
				ReportBPPlugInPoint.ReportUpdateBP);

		// ���BP����
		this.addBeforeRule(processer);
		this.addAfterRule(processer);

		// ��Ϊ�״��������û�д洢����ִ�к��֮ǰ�����״���������ٲ���ȥ��
		// һ��Ҫ�ں����ִ��ǰ
		ReportVOInfoUtils.handlerIndate(null, voArray);

		processer.before(voArray, originBills);

		BillUpdate<ReportVO> bo = new BillUpdate<ReportVO>();
		ReportVO[] vos = bo.update(voArray, originBills);

		processer.after(voArray, originBills);

		return vos;
	}

	private void addAfterRule(CompareAroundProcesser<ReportVO> processer) {
		// ��鵥�ݺŵ�Ψһ��
		processer.addAfterRule(new ChkBillCodeUniqueRule());
		// ͬ�����¼�������
		processer.addAfterRule(new UpdChkBatCode());
		// ��������Ψһ��
		processer.addAfterRule(new ReportChkChkBatUnique());
		// ��д���쵥
		processer.addAfterRule(new WriteC001WhenUpdateRule());
		// �޸ı�������κŴ���
		processer.addAfterRule(new UpdateBatchCodeAfterRule());
		// 2017-06-25
		// 1. �����жϱ���ϸ�Ʒû�й�ѡ������Ϊ�ǲ��ϸ�Ʒ����Ҫ�Զ�����ͷ����Ҫ���ϸ�Ʒ���� ��ѡ
		// 2. �����жϱ���ϸ�Ʒû�й�ѡ������Ϊ�ǲ��ϸ��Զ����ɲ��ϸ�Ʒ����
		// 3. ��Ҫ���� ��ʽ���棬 .���� ���� ����--�Զ��ʼ챨��--�Զ����ϸ�Ʒ����
//		processer.addAfterRule(new RejectAfterCheckRule());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addBeforeRule(CompareAroundProcesser<ReportVO> processer) {

		// ���ݺŴ���
		processer.addBeforeRule(new UpdateBillCodeRule());
		// �����
		processer.addBeforeRule(new FillUpDataUpdateRule());
		// �������ģ���е������Ĺ���
		String[] hKeys = new String[2];
		hKeys[0] = ReportHeaderVO.NCHECKNUM;
		hKeys[1] = ReportHeaderVO.NCHECKASTNUM;
		String[] bKeys = new String[2];
		bKeys[0] = ReportItemVO.NNUM;
		bKeys[1] = ReportItemVO.NASTNUM;
		processer.addBeforeRule(new CheckBillNumRule<ReportVO>(hKeys, bKeys));
		// �����Ϣ
		processer.addBeforeRule(new SetUpdateAuditInfoRule<ReportVO>());
		// �ǿ�У��
		processer.addBeforeRule(new ReportItemsCheckRule());
		// �к��ظ�У��
		processer.addBeforeRule(new QCRowNoCheckRule(ReportItemVO.class,
				ReportItemVO.CROWNO));
		// ����У��
		processer.addBeforeRule(new ReportNumCheckRule());
		// �޸ı���ǰ���κŴ���
		processer.addBeforeRule(new UpdateBatchCodeBeforeRule());
		// ���ɸ������Եļ����(ֻ��������Ƿ�ƥ�䡢�����ǿ�)
		MarAssistantSaveRule<ReportVO> mar = new MarAssistantSaveRule<ReportVO>();
		// ���ý��зǿ�У�飬���������ֶ�����
		// mar.setNotNullValidate(ReportHeaderVO.PK_MATERIAL);
		processer.addBeforeRule(mar);
		// Ϊ����ͨ���ĵ����޸Ĺ���
		processer.addBeforeRule(new NoPassUpdateRule<ReportVO>());
		// // ����Ԫ����-->����ʱ����Ӧ���и���ʱҲ��Ҫ����
		processer.addBeforeRule(new ReportAddStockStateRule());

	}
}
