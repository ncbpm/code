/**
 * $�ļ�˵��$
 * 
 * @author tianft
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-7-21 ����04:59:00
 */
package nc.ui.qc.c002.editor.card.beforeedit.body;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.qc.checkstandard.IQueryCheckStandard;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.ui.pubapp.uif2app.view.util.BillPanelUtils;
import nc.ui.pubapp.util.CardPanelValueUtils;
import nc.ui.qc.pub.editor.card.listener.ICardBodyBeforeEditEventListener;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.c002.entity.CheckBillItemVO;
import nc.vo.qc.checkitem.enumeration.CheckTypeEnum;
import nc.vo.qc.checkstandard.data.CheckStandardDataUtil;
import nc.vo.qc.checkstandard.entity.CheckStandardItemVO;
import nc.vo.qc.checkstandard.entity.CheckStandardVO;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * ����ֵ�༭ǰ����
 * 
 * @since 6.0
 * @version 2011-3-24 ����09:25:31
 * @author �����
 */
public class CheckValue implements ICardBodyBeforeEditEventListener {

  /**
   * ���෽����д
   * 
   * @see nc.ui.qc.pub.editor.card.listener.ICardHeadTailBeforeEditEventListener#beforeEdit(nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent)
   */
  @Override
  public void beforeEdit(CardBodyBeforeEditEvent e) {

    // �������ֵ������ö���ͣ����Ϊö���������г�����ö��ֵ��ֻ������������ѡ��
    // ����ֵ��������ֵ��ʱΪ�ı��򣬿��ֹ����룬ֻ��������ֵ��
    CardPanelValueUtils util = new CardPanelValueUtils(e.getBillCardPanel());
    Integer valueType =
        (Integer) util.getBodyValue(e.getRow(), CheckBillItemVO.ICHKVALUETYPE);
    Object objValue = util.getBodyValue(e.getRow(), CheckBillItemVO.VCHKVALUE);
    if (CheckTypeEnum.NUMBERTYPE.toInteger().equals(valueType)) {
      // ��ֵ��
      // ģ��ȡֵ����ΪString �����ó���ֵ���ı���֮��String�޷���ʾ
      this.tranDecimalForChkValue(e);
    }
    //2017-09-06 liyf ��ĿҪ�����ε�����¼�룬���������ı�����
//    else if (CheckTypeEnum.ENUMTYPE.toInteger().equals(valueType)) {
//      // ö����,������
//      this.buildComboBox(e, objValue);
//    }
    else {
      this.buildRefStr(e);
      if (null == objValue) {
        return;
      }
      util.setBodyValue(objValue, e.getRow(), CheckBillItemVO.VCHKVALUE);
    }
  }

  /**
   * ����������
   * 
   * @param e
   */
  private void buildComboBox(CardBodyBeforeEditEvent e, Object obj) {

    String chkValue = this.getChkValueFromStd(e);
    if (StringUtils.isBlank(chkValue)) {
      this.tranDecimalForChkValue(e);
    }
    else {
      this.creatComboBox(e, chkValue);
    }
  }

  /**
   * ������ֵ��
   * 
   * @param e
   */
  private UIRefPane buildRefDecimal(CardBodyBeforeEditEvent e) {
    CardPanelValueUtils util = new CardPanelValueUtils(e.getBillCardPanel());
    Integer valueType =
        (Integer) util.getBodyValue(e.getRow(), CheckBillItemVO.ICHKVALUETYPE);
    UIRefPane stringRef = null;
    if (CheckTypeEnum.NUMBERTYPE.toInteger().equals(valueType)) {
      // ��ֵ��
      stringRef =
          (UIRefPane) BillPanelUtils
              .createEditComponent(IBillItem.STRING, null);
      stringRef.getUITextField().setAllowAlphabetic(false);
      stringRef.getUITextField().setAllowUnicode(false);
      stringRef.getUITextField().setAllowNumeric(true);

    }
    else if (CheckTypeEnum.ENUMTYPE.toInteger().equals(valueType)) {
      // ö����,������
      stringRef =
          (UIRefPane) BillPanelUtils
              .createEditComponent(IBillItem.STRING, null);
    }
    BillCellEditor cellEditorStr = new BillCellEditor(stringRef);
    e.getBillCardPanel().getBodyPanel()
        .setTableCellEditor(CheckBillItemVO.VCHKVALUE, cellEditorStr);

    return stringRef;
  }

  /**
   * �����ı���
   * 
   * @param e
   */
  private UIRefPane buildRefStr(CardBodyBeforeEditEvent e) {
    UIRefPane stringRef =
        (UIRefPane) BillPanelUtils.createEditComponent(IBillItem.STRING, null);
    BillCellEditor cellEditorStr = new BillCellEditor(stringRef);
    e.getBillCardPanel().getBodyPanel()
        .setTableCellEditor(CheckBillItemVO.VCHKVALUE, cellEditorStr);
    return stringRef;
  }

  /**
   * ����һ��������
   * 
   * @param e
   * @param chkValue
   */
  private void creatComboBox(CardBodyBeforeEditEvent e, String chkValue) {
    UIComboBox cbbBType =
        (UIComboBox) BillPanelUtils.createEditComponent(IBillItem.COMBO, null);
    cbbBType.setFont(e.getBillCardPanel().getFont());
    cbbBType.setBackground(e.getBillCardPanel().getBackground());
    this.setChkValueForComboBox(cbbBType, chkValue);
    if (cbbBType.getItemCount() > 0) {
      // add by hanbin 2011-9-13 ���ֿ�ָ���쳣
      cbbBType.setTranslate(true);
      cbbBType.setSelectedIndex(0);
      BillCellEditor cellEditor = new BillCellEditor(cbbBType);
      e.getBillCardPanel().getBodyPanel()
          .setTableCellEditor(CheckBillItemVO.VCHKVALUE, cellEditor);
    }
  }

  /**
   * ��ȡ��ǰ�м�����Ŀ��Ӧ�ļ��鷽��
   */
  private String getChkStdForCurItem(CardBodyBeforeEditEvent e,
      CardPanelValueUtils util) {
    for (int i = 0; i < 10; i++) {
      UFBoolean bincludedObj =
          util.getBodyUFBooleanValue(e.getRow(), "bincluded" + i);
      if (bincludedObj != null && bincludedObj.booleanValue()) {
        return util.getBodyStringValue(e.getRow(), "pk_chkstd" + i);
      }
    }
    return util.getBodyStringValue(e.getRow(), CheckBillItemVO.PK_CHKSTD1_V);
  }

  /**
   * ȡ�ñ�׼ֵ
   * 
   * @param e
   * @return
   */
  private String getChkValueFromStd(CardBodyBeforeEditEvent e) {
    try {
      CardPanelValueUtils util = new CardPanelValueUtils(e.getBillCardPanel());
      String pk_defstd = this.getChkStdForCurItem(e, util);
      // util.getHeadTailStringValue(CheckBillHeaderVO.PK_DEFAULTSTD_V);

      // ȡ��Ĭ�ϼ��鷽��
      IQueryCheckStandard service =
          NCLocator.getInstance().lookup(IQueryCheckStandard.class);

      Map<String, CheckStandardVO> chkstdMap =
          service.queryChkStdVOs(new String[] {
            pk_defstd
          });

      String pk_chkitem =
          util.getBodyStringValue(e.getRow(), CheckBillItemVO.PK_CHECKITEM);

      CheckStandardVO chkstdVO = chkstdMap.get(pk_defstd);
      if (null == chkstdVO) {
        return null;
      }
      CheckStandardItemVO[] chkstdBVOs = chkstdVO.getBVO();

      for (CheckStandardItemVO chkstdBVO : chkstdBVOs) {
        if (chkstdBVO.getPk_checkitem().equals(pk_chkitem)) {
          return chkstdBVO.getVstandardvalue();
        }
      }
    }
    catch (BusinessException e1) {
      ExceptionUtils.wrappException(e1);
    }
    return null;
  }

  /**
   * ��������������
   * 
   * @param cbbBType
   * @param chkvalue
   */
  private void setChkValueForComboBox(UIComboBox cbbBType, String chkvalue) {
    Object[][] data =
        CheckStandardDataUtil.analyseEnumerateStandardValue(chkvalue);

    if (ArrayUtils.isEmpty(data)) {
      // add by hanbin 2011-9-13 ���ֿ�ָ���쳣
      return;
    }
    for (int i = 0; i < data.length; i++) {
      cbbBType.addItem(data[i][1]);
    }
  }

  /**
   * ����ֵ
   * 
   * @param objValue
   *          ��ֵ
   * @param row
   * @param fieldKey
   *          :�ֶ�����
   * @param valueType
   *          ��ֵ����
   * @param panel
   */
  private void setValue(Object objValue, int row, String fieldKey,
      BillCardPanel panel) {
    if (null == objValue) {
      return;
    }
    CardPanelValueUtils util = new CardPanelValueUtils(panel);
    if (objValue instanceof String) {
      util.setBodyValue(objValue, row, fieldKey);
    }
    else {
      UFDouble value = ValueUtils.getUFDouble(objValue);
      util.setBodyValue(objValue, row, fieldKey);
    }
  }

  private void tranDecimalForChkValue(CardBodyBeforeEditEvent e) {
    UIRefPane refPane = this.buildRefDecimal(e);
    BillCardPanel panel = e.getBillCardPanel();
    Object obj = panel.getBodyValueAt(e.getRow(), CheckBillItemVO.VCHKVALUE);
    CardPanelValueUtils util = new CardPanelValueUtils(e.getBillCardPanel());
    Integer valueType =
        (Integer) util.getBodyValue(e.getRow(), CheckBillItemVO.ICHKVALUETYPE);
    if (null == obj) {
      return;
    }
    CardPanelValueUtils utiltool = new CardPanelValueUtils(panel);
    if (CheckTypeEnum.NUMBERTYPE.toInteger().equals(valueType)) {
      // ��ֵ��
      String value = ValueUtils.getString(obj);
      utiltool.setBodyValue(value, e.getRow(), CheckBillItemVO.VCHKVALUE);
    }
    else if (CheckTypeEnum.ENUMTYPE.toInteger().equals(valueType)) {
      // ö����,������
      utiltool.setBodyValue(obj, e.getRow(), CheckBillItemVO.VCHKVALUE);
    }
  }
}
