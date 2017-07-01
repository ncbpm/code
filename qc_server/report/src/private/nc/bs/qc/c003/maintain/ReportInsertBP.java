package nc.bs.qc.c003.maintain;

import nc.bs.qc.c003.maintain.rule.ChkBillCodeUniqueRule;
import nc.bs.qc.c003.maintain.rule.InsertAndDelBatchCodeBeforeRule;
import nc.bs.qc.c003.maintain.rule.ReportChkChkBatUnique;
import nc.bs.qc.c003.maintain.rule.insert.CreatChkBatchRule;
import nc.bs.qc.c003.maintain.rule.insert.FillUpDataRule;
import nc.bs.qc.c003.maintain.rule.insert.InsertBatchCodeAfterRule;
import nc.bs.qc.c003.maintain.rule.insert.InsertBillCodeRule;
import nc.bs.qc.c003.maintain.rule.insert.MatchReportTypeRule;
import nc.bs.qc.c003.maintain.rule.insert.RowNoFillUpRule;
import nc.bs.qc.c003.maintain.rule.insert.WriteC001WhenInsertRule;
import nc.bs.qc.c003.maintain.rule.insert.WriteC002WhenInsertRule;
import nc.bs.qc.c003.plugin.ReportBPPlugInPoint;
import nc.bs.qc.pub.rule.QCRowNoCheckRule;
import nc.impl.pubapp.bd.material.assistant.MarAssistantSaveRule;
import nc.impl.pubapp.bd.userdef.UserDefSaveRule;
import nc.impl.pubapp.pattern.data.bill.BillInsert;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.pubapp.util.SetAddAuditInfoRule;
import nc.vo.qc.c003.entity.ReportHeaderVO;
import nc.vo.qc.c003.entity.ReportItemVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.c003.rule.FillupFacceptjudgeRule;
import nc.vo.qc.c003.rule.RejectAfterCheckRule;
import nc.vo.qc.c003.rule.RejectBeforCheckRule;
import nc.vo.qc.c003.rule.ReportCheckFlowRule;
import nc.vo.qc.c003.rule.ReportItemsCheckRule;
import nc.vo.qc.c003.rule.ReportNumCheckRule;
import nc.vo.qc.c003.util.ReportAddStockStateRule;
import nc.vo.qc.pub.rule.CheckBillNumRule;
import nc.vo.scmpub.rule.QcCenterEnableCheckRule;

/**
 * <p>
 * <b>������Ҫ������¹��ܣ�</b>
 * <ul>
 * <li>�������汨�쵥BP��
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author hanbin
 * @time 2010-1-13 ����10:17:34
 */
public class ReportInsertBP {
	--�ǵúϲ���Ʒ����
  public ReportVO[] insertReport(ReportVO[] aggVO) {
    AroundProcesser<ReportVO> processer =
        new AroundProcesser<ReportVO>(ReportBPPlugInPoint.ReportInsertBP);

    // ���BP����
    this.addBeforeRule(processer);
    this.addAfterFinalRule(processer);

    processer.before(aggVO);

    BillInsert<ReportVO> bo = new BillInsert<ReportVO>();
    ReportVO[] vos = bo.insert(aggVO);

    processer.after(aggVO);

    return vos;
  }

  private void addAfterFinalRule(AroundProcesser<ReportVO> processer) {
    // ���ݺ�Ψһ��У��
    processer.addAfterFinalRule(new ChkBillCodeUniqueRule());
    // ��д���쵥
    processer.addAfterFinalRule(new WriteC001WhenInsertRule());
    // ��д���鵥
    processer.addAfterFinalRule(new WriteC002WhenInsertRule());
    // ������������κŴ���
    processer.addAfterFinalRule(new InsertBatchCodeAfterRule());
    //2017-06-25
//    1. �����жϱ���ϸ�Ʒû�й�ѡ������Ϊ�ǲ��ϸ�Ʒ����Ҫ�Զ�����ͷ����Ҫ���ϸ�Ʒ���� ��ѡ
//    2. �����жϱ���ϸ�Ʒû�й�ѡ������Ϊ�ǲ��ϸ��Զ����ɲ��ϸ�Ʒ����
//    3. ��Ҫ���� ��ʽ���棬 .���� ���� ����--�Զ��ʼ챨��--�Զ����ϸ�Ʒ����
//    processer.addAfterRule(new RejectAfterCheckRule());
  }

  @SuppressWarnings({
    "unchecked", "rawtypes"
  })
  private void addBeforeRule(AroundProcesser<ReportVO> processer) {
    // �����
    processer.addBeforeRule(new FillUpDataRule());
    // �����Ϣ
    processer.addBeforeRule(new SetAddAuditInfoRule<ReportVO>());
    // �������ģ���е������Ĺ���
    String[] hKeys = new String[2];
    hKeys[0] = ReportHeaderVO.NCHECKNUM;
    hKeys[1] = ReportHeaderVO.NCHECKASTNUM;
    String[] bKeys = new String[2];
    bKeys[0] = ReportItemVO.NNUM;
    bKeys[1] = ReportItemVO.NASTNUM;
    processer.addBeforeRule(new CheckBillNumRule<ReportVO>(hKeys, bKeys));
    // ���ݺŴ���
    processer.addBeforeRule(new InsertBillCodeRule());
    // �ʼ챨�汣��ǰ������������
    processer.addBeforeRule(new CreatChkBatchRule());
    // У���������Ψһ��
    processer.addBeforeRule(new ReportChkChkBatUnique());
    // ������ж�
    processer.addBeforeRule(new FillupFacceptjudgeRule());
    // �ʼ챨����кŴ�������ࣨ���쵥��ʽ�����ʼ챨��ʱ����Ҫ��кţ�
    processer.addBeforeRule(new RowNoFillUpRule());
    // �к��ظ�У��
    processer.addBeforeRule(new QCRowNoCheckRule(ReportItemVO.class,
        ReportItemVO.CROWNO));
    // ƥ�䱨������
    processer.addBeforeRule(new MatchReportTypeRule());
    // �ǿ�У��
    processer.addBeforeRule(new ReportItemsCheckRule());
    // ����У��
    processer.addBeforeRule(new ReportNumCheckRule());
    // ��������ǰ���κŴ���
    processer.addBeforeRule(new InsertAndDelBatchCodeBeforeRule(true));
    // ���ϸ�������У��(�ʼ챨���������ɸ���������Դ�ڱ������ϸ��)(ֻ��������Ƿ�ƥ�䡢�����ǿ�)
    // IRule<ReportVO> marRule = new MarAssistantSaveRule<ReportVO>();
    // processer.addBeforeRule(marRule);
    MarAssistantSaveRule<ReportVO> mar = new MarAssistantSaveRule<ReportVO>();
    // ���ý��зǿ�У�飬���������ֶ�����
    // mar.setNotNullValidate(ReportHeaderVO.PK_MATERIAL);
    processer.addBeforeRule(mar);
    // ����֯������У��
    processer.addBeforeRule(new QcCenterEnableCheckRule<ReportVO>());
    // У���Զ�����
    processer.addBeforeRule(new UserDefSaveRule<ReportVO>(new Class[] {
      ReportHeaderVO.class, ReportItemVO.class
    }));
    // // ����Ԫ����-->����ʱ����Ӧ���и���ʱҲ��Ҫ�������Զ����¿��״̬ʱ���κͿ��״̬��У��
    processer.addBeforeRule(new ReportAddStockStateRule());
    // ��������ǰ���κŴ���
    processer.addBeforeRule(new InsertAndDelBatchCodeBeforeRule(true));
    // �ʼ�����
    processer.addBeforeRule(new ReportCheckFlowRule());
    // 1. �����жϱ���ϸ�Ʒû�й�ѡ������Ϊ�ǲ��ϸ�Ʒ����Ҫ�Զ�����ͷ����Ҫ���ϸ�Ʒ���� ��ѡ
//    processer.addBeforeRule(new RejectBeforCheckRule());
  }
}
