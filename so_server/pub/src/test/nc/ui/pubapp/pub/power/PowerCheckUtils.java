package nc.ui.pubapp.pub.power;

import java.util.Map;

import nc.desktop.ui.WorkbenchEnvironment;
import nc.uap.rbac.core.dataperm.DataPermissionFacade;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.pub.PubAppTool;

/**
 * Ȩ��У�鹤����
 * 
 * @since 6.0
 * @version 2011-3-18 ����11:48:27
 * @author ���
 */
public class PowerCheckUtils {

  /**
   * У�鴫��ĵ����Ƿ����㶨���Ȩ�޹���
   * 
   * @param bills����ҪУ��ĵ���
   * @param permissioncode��Ȩ����Դʵ��ı��룺�絥������(��5X)/ҵ�������ҵ��Ԫ����(��batchcode)
   * @param actioncode��Ԫ���ݲ������룺��(insert��edit��delete��approve��unapprove)
   * @param billcodefiledname�����ݺ��ֶ�������(vbillcode)
   */
  public static void checkHasPermission(IBill[] bills, String permissioncode,
      String actioncode, String billcodefiledname) {
//    if (bills == null || bills.length == 0) {
//      return;
//    }
//    String pk_group =
//        WorkbenchEnvironment.getInstance().getGroupVO().getPk_group();
//    String cuserid =
//        WorkbenchEnvironment.getInstance().getLoginUser().getCuserid();
//    Map<Object, UFBoolean> map =
//        DataPermissionFacade.isUserHasPermission(cuserid, permissioncode,
//            actioncode, pk_group, bills);
//    StringBuilder error = new StringBuilder();
//    for (IBill bill : bills) {
//      UFBoolean flag = map.get(bill);
//      if (!flag.booleanValue()) {
//        error.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
//            "pubapp_0", "0pubapp-0102")/*@res "����"*/);
//        if (!PubAppTool.isNull(billcodefiledname)) {
//          String vbillcode =
//              ValueUtils.getString(bill.getParent().getAttributeValue(
//                  billcodefiledname));
//          if (!PubAppTool.isNull(vbillcode)) {
//            error.append(vbillcode);
//          }
//          error.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
//              "pubapp_0", "0pubapp-0103")/*@res "���������Ȩ�޹��������"*/);
//          error.append(",");
//        }
//      }
//    }
//
//    if (error.length() > 0) {
//      error.deleteCharAt(error.length() - 1);
//      ExceptionUtils.wrappBusinessException(error.toString());
//    }

  }

  /**
   * У�鴫��ĵ����Ƿ����㶨���Ȩ�޹���
   * 
   * @param bills����ҪУ��ĵ���
   * @param permissioncode��Ȩ����Դʵ��ı��룺�絥������(��5X)/ҵ�������ҵ��Ԫ����(��batchcode)
   * @param actioncode��Ԫ���ݲ������룺��(insert��edit��delete��approve��unapprove)
   * @param billcodefiledname�����ݺ��ֶ�������(vbillcode)
   */
  public static void checkHasPermissionForAggVO(AggregatedValueObject[] bills,
      String permissioncode, String actioncode, String billcodefiledname) {
    String pk_group =
        WorkbenchEnvironment.getInstance().getGroupVO().getPk_group();
    String cuserid =
        WorkbenchEnvironment.getInstance().getLoginUser().getCuserid();
    Map<Object, UFBoolean> map =
        DataPermissionFacade.isUserHasPermission(cuserid, permissioncode,
            actioncode, pk_group, bills);
    StringBuilder error = new StringBuilder();
    for (AggregatedValueObject bill : bills) {
      UFBoolean flag = map.get(bill);
      if (!flag.booleanValue()) {
        error.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
            "pubapp_0", "0pubapp-0102")/*@res "����"*/);
        if (!PubAppTool.isNull(billcodefiledname)) {
          String vbillcode =
              ValueUtils.getString(bill.getParentVO().getAttributeValue(
                  billcodefiledname));
          if (!PubAppTool.isNull(vbillcode)) {
            error.append(vbillcode);
          }
          error.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
              "pubapp_0", "0pubapp-0103")/*@res "���������Ȩ�޹��������"*/);
          error.append(",");
        }
      }
    }

    if (error.length() > 0) {
      error.deleteCharAt(error.length() - 1);
      ExceptionUtils.wrappBusinessException(error.toString());
    }

  }

  /**
   * У�鴫��ĵ����Ƿ����㶨���Ȩ�޹���
   * 
   * @param bills����ҪУ��ĵ���
   * @param permissioncode��Ȩ����Դʵ��ı��룺�絥������(��5X)/ҵ�������ҵ��Ԫ����(��batchcode)
   * @param actioncode��Ԫ���ݲ������룺��(insert��edit��delete��approve��unapprove)
   * @param billcodefiledname�����ݺ��ֶ�������(vbillcode)
   */
  public static void checkHasPermissionForCirVO(
      CircularlyAccessibleValueObject[] vos, String permissioncode,
      String actioncode, String billcodefiledname) {
    String pk_group =
        WorkbenchEnvironment.getInstance().getGroupVO().getPk_group();
    String cuserid =
        WorkbenchEnvironment.getInstance().getLoginUser().getCuserid();
    Map<Object, UFBoolean> map =
        DataPermissionFacade.isUserHasPermission(cuserid, permissioncode,
            actioncode, pk_group, vos);
    StringBuilder error = new StringBuilder();
    for (CircularlyAccessibleValueObject bill : vos) {
      UFBoolean flag = map.get(bill);
      if (!flag.booleanValue()) {
        error.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
            "pubapp_0", "0pubapp-0102")/*@res "����"*/);
        if (!PubAppTool.isNull(billcodefiledname)) {
          String vbillcode =
              ValueUtils.getString(bill.getAttributeValue(billcodefiledname));
          if (!PubAppTool.isNull(vbillcode)) {
            error.append(vbillcode);
          }
        }
        error.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
            "pubapp_0", "0pubapp-0103")/*@res "���������Ȩ�޹��������"*/);
        error.append(",");
      }
    }

    if (error.length() > 0) {
      error.deleteCharAt(error.length() - 1);
      ExceptionUtils.wrappBusinessException(error.toString());
    }

  }

}
