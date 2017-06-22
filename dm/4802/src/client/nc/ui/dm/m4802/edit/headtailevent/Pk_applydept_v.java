package nc.ui.dm.m4802.edit.headtailevent;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.ui.pubapp.util.CardPanelValueUtils;
import nc.ui.scmpub.ref.FilterDeptRefUtils;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.qc.c001.entity.ApplyHeaderVO;
import nc.vo.qc.pub.enumeration.ApplySourceTypeEnum;

/**
 * 报检部门的处理类
 * 
 * @since 6.0
 * @version 2011-1-19 下午06:56:05
 * @author hanbin
 */
public class Pk_applydept_v {

  public void beforeEdit(CardHeadTailBeforeEditEvent e) {
    BillCardPanel card = e.getBillCardPanel();
    // 主组织
    String org = e.getContext().getPk_org();
    //集团
    String pk_group=e.getContext().getPk_group();
    
    UIRefPane deptRef =
        (UIRefPane) card.getHeadItem(e.getKey()).getComponent();
    FilterDeptRefUtils.createFilterDeptRefUtilsOfQC(deptRef)
        .groupFilterRef(pk_group);

    UIRefPane deptRef_v =
        (UIRefPane) card.getHeadItem(e.getKey())
            .getComponent();
    FilterDeptRefUtils.createFilterDeptRefUtilsOfQC(deptRef_v)
        .groupFilterRef(pk_group);

    e.setReturnValue(Boolean.TRUE);

  }
}
