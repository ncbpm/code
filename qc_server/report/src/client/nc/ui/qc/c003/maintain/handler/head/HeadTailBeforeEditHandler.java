package nc.ui.qc.c003.maintain.handler.head;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.ui.qc.c003.maintain.handler.body.Pk_qualitylv_b;
import nc.ui.qc.pub.edit.Vsncode;
import nc.ui.qc.pub.editor.card.QCDefItemHandler;
import nc.ui.scmpub.ref.FilterDeptRefUtils;
import nc.ui.scmpub.ref.FilterPsndocRefUtils;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.c003.entity.ReportHeaderVO;

import org.apache.commons.lang.StringUtils;

/**
 * ���ݱ�ͷ��β�ֶα༭ǰ�¼�
 * 
 * @since 6.0
 * @version 2010-12-21 ����02:17:56
 * @author hanbin
 */
public class HeadTailBeforeEditHandler implements
    IAppEventHandler<CardHeadTailBeforeEditEvent> {
  // private LoginContext context;

  @Override
  public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
    e.setReturnValue(Boolean.TRUE);
    String key = e.getKey();
    if (key.equals(ReportHeaderVO.CTRANTYPEID)) {
      // ��������
      new Ctrantypeid().beforeEdit(e);
    }
    else if (key.equals(ReportHeaderVO.VCHKBATCH)) {
      // �������κ�
      new Vchkbatch().beforeEdit(e);
    }
    else if (key.equals(ReportHeaderVO.NCHECKASTNUM)) {
      // ��������
      new Ncheckastnum().beforeEdit(e);
    }
    else if (key.equals(ReportHeaderVO.NCHECKNUM)) {
      // ����������
      new Nchecknum().beforeEdit(e);
    }
    else if (key.equals(ReportHeaderVO.BNEEDDEAL)) {
      // ��Ҫ���ϸ�Ʒ����
      new Bneeddeal().beforeEdit(e);
    }
    else if (key.indexOf("vdef") != -1 || key.indexOf("vbdef") != -1) {
      // �Զ������(����Զ������ǵ�������,��������֯���й���)
      new QCDefItemHandler(e.getContext()).headBefore(e);
    }
    else if (key.equals(ReportHeaderVO.PK_APPLYDEPT)
        || key.equals(ReportHeaderVO.PK_CHKDEPT)
        || key.equals(ReportHeaderVO.PK_APPLYDEPT_V)
        || key.equals(ReportHeaderVO.PK_CHKDEPT_V)) {
      this.filterDept(e);
    }
    else if (key.equals(ReportHeaderVO.PK_PUDEPT)
        || key.equals(ReportHeaderVO.PK_PUDEPT_V)) {
      this.filterDeptPu(e);
    }
    else if (key.equals(ReportHeaderVO.VSNCODE)) {
      // ���к����ò��ɱ༭
      new Vsncode().beforeEdit(e);
    }
    else if (key.equals(ReportHeaderVO.PK_REPORTER)) {
      // ������
      this.filterPk_reporter(e);
    }
    else if (key.equals(ReportHeaderVO.PK_APPLYER)) {
      // ������
      this.filterPk_reporter(e);
    }
    else if (key.equals(ReportHeaderVO.PK_PUPSNDOC)) {
      // �ɹ�Ա
      this.filterPk_pupsndoc(e);
    }
    else if (key.equals(ReportHeaderVO.PK_CONTINUEBATCH)) {
      // �ʼ�������
      new ContinueBatchHandler().beforeEdit(e);
    }
    
    if(key.equals(ReportHeaderVO.VDEF2)){
    	new Pk_qualitylv_b(e.getContext()).beforeEdit(e);
    }
  }

  private void filterDept(CardHeadTailBeforeEditEvent e) {
    FilterDeptRefUtils utils =
        FilterDeptRefUtils.createFilterDeptRefUtilsOfQC((UIRefPane) e
            .getBillCardPanel().getHeadItem(e.getKey()).getComponent());
    String pk_org =
        e.getBillCardPanel().getHeadItem(ReportHeaderVO.PK_ORG)
            .getValueObject().toString();
    // �ʼ����Ŀɼ��Ĳ��ŵ���
    utils.filterItemRefByOrg(pk_org);
  }

  private void filterDeptPu(CardHeadTailBeforeEditEvent e) {
    FilterDeptRefUtils utils =
        FilterDeptRefUtils.createFilterDeptRefUtilsOfPU((UIRefPane) e
            .getBillCardPanel().getHeadItem(e.getKey()).getComponent());
    String pk_org =
        e.getBillCardPanel().getHeadItem(ReportHeaderVO.PK_ORG)
            .getValueObject().toString();
    // �ɹ���֯�ɼ��Ĳ��ŵ���
    utils.filterItemRefByOrg(pk_org);
  }

  private void filterPk_pupsndoc(CardHeadTailBeforeEditEvent e) {
    FilterPsndocRefUtils utils =
        FilterPsndocRefUtils.createFilterPsndocRefUtilsOfPU((UIRefPane) e
            .getBillCardPanel().getHeadItem(e.getKey()).getComponent());
    BillItem item = e.getBillCardPanel().getHeadItem(ReportHeaderVO.PK_PUORG);
    if (StringUtils.isBlank((String) item.getValueObject())) {
      String msg =
          NCLangRes4VoTransl.getNCLangRes().getStrByID("c010003_0",
              "0c010003-0124");
      ExceptionUtils.wrappBusinessException(msg);
    }
    String pk_org = item.getValueObject().toString();
    // �ɹ���֯�ɼ�����Ա����
    utils.filterItemRefByOrg(pk_org);
  }

  private void filterPk_reporter(CardHeadTailBeforeEditEvent e) {
    FilterPsndocRefUtils utils =
        FilterPsndocRefUtils.createFilterPsndocRefUtilsOfQC((UIRefPane) e
            .getBillCardPanel().getHeadItem(e.getKey()).getComponent());
    String pk_org =
        e.getBillCardPanel().getHeadItem(ReportHeaderVO.PK_ORG)
            .getValueObject().toString();
    // �ʼ����Ŀɼ�����Ա����
    utils.filterItemRefByOrg(pk_org);
  }
}
