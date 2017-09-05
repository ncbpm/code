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
 * 单据表头表尾字段编辑前事件
 * 
 * @since 6.0
 * @version 2010-12-21 下午02:17:56
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
      // 报告类型
      new Ctrantypeid().beforeEdit(e);
    }
    else if (key.equals(ReportHeaderVO.VCHKBATCH)) {
      // 检验批次号
      new Vchkbatch().beforeEdit(e);
    }
    else if (key.equals(ReportHeaderVO.NCHECKASTNUM)) {
      // 检验数量
      new Ncheckastnum().beforeEdit(e);
    }
    else if (key.equals(ReportHeaderVO.NCHECKNUM)) {
      // 检验主数量
      new Nchecknum().beforeEdit(e);
    }
    else if (key.equals(ReportHeaderVO.BNEEDDEAL)) {
      // 需要不合格品处理
      new Bneeddeal().beforeEdit(e);
    }
    else if (key.indexOf("vdef") != -1 || key.indexOf("vbdef") != -1) {
      // 自定义项处理(如果自定义项是档案类型,按照主组织进行过滤)
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
      // 序列号设置不可编辑
      new Vsncode().beforeEdit(e);
    }
    else if (key.equals(ReportHeaderVO.PK_REPORTER)) {
      // 报告人
      this.filterPk_reporter(e);
    }
    else if (key.equals(ReportHeaderVO.PK_APPLYER)) {
      // 报检人
      this.filterPk_reporter(e);
    }
    else if (key.equals(ReportHeaderVO.PK_PUPSNDOC)) {
      // 采购员
      this.filterPk_pupsndoc(e);
    }
    else if (key.equals(ReportHeaderVO.PK_CONTINUEBATCH)) {
      // 质检连续批
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
    // 质检中心可见的部门档案
    utils.filterItemRefByOrg(pk_org);
  }

  private void filterDeptPu(CardHeadTailBeforeEditEvent e) {
    FilterDeptRefUtils utils =
        FilterDeptRefUtils.createFilterDeptRefUtilsOfPU((UIRefPane) e
            .getBillCardPanel().getHeadItem(e.getKey()).getComponent());
    String pk_org =
        e.getBillCardPanel().getHeadItem(ReportHeaderVO.PK_ORG)
            .getValueObject().toString();
    // 采购组织可见的部门档案
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
    // 采购组织可见的人员档案
    utils.filterItemRefByOrg(pk_org);
  }

  private void filterPk_reporter(CardHeadTailBeforeEditEvent e) {
    FilterPsndocRefUtils utils =
        FilterPsndocRefUtils.createFilterPsndocRefUtilsOfQC((UIRefPane) e
            .getBillCardPanel().getHeadItem(e.getKey()).getComponent());
    String pk_org =
        e.getBillCardPanel().getHeadItem(ReportHeaderVO.PK_ORG)
            .getValueObject().toString();
    // 质检中心可见的人员档案
    utils.filterItemRefByOrg(pk_org);
  }
}
