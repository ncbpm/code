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
 * 权限校验工具类
 * 
 * @since 6.0
 * @version 2011-3-18 上午11:48:27
 * @author 李瑜
 */
public class PowerCheckUtils {

  /**
   * 校验传入的单据是否满足定义的权限规则
   * 
   * @param bills：需要校验的单据
   * @param permissioncode：权限资源实体的编码：如单据类型(如5X)/业务组件、业务单元名称(如batchcode)
   * @param actioncode：元数据操作编码：如(insert、edit、delete、approve、unapprove)
   * @param billcodefiledname：单据号字段名：如(vbillcode)
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
//            "pubapp_0", "0pubapp-0102")/*@res "单据"*/);
//        if (!PubAppTool.isNull(billcodefiledname)) {
//          String vbillcode =
//              ValueUtils.getString(bill.getParent().getAttributeValue(
//                  billcodefiledname));
//          if (!PubAppTool.isNull(vbillcode)) {
//            error.append(vbillcode);
//          }
//          error.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
//              "pubapp_0", "0pubapp-0103")/*@res "不满足操作权限规则，请调整"*/);
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
   * 校验传入的单据是否满足定义的权限规则
   * 
   * @param bills：需要校验的单据
   * @param permissioncode：权限资源实体的编码：如单据类型(如5X)/业务组件、业务单元名称(如batchcode)
   * @param actioncode：元数据操作编码：如(insert、edit、delete、approve、unapprove)
   * @param billcodefiledname：单据号字段名：如(vbillcode)
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
            "pubapp_0", "0pubapp-0102")/*@res "单据"*/);
        if (!PubAppTool.isNull(billcodefiledname)) {
          String vbillcode =
              ValueUtils.getString(bill.getParentVO().getAttributeValue(
                  billcodefiledname));
          if (!PubAppTool.isNull(vbillcode)) {
            error.append(vbillcode);
          }
          error.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
              "pubapp_0", "0pubapp-0103")/*@res "不满足操作权限规则，请调整"*/);
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
   * 校验传入的单据是否满足定义的权限规则
   * 
   * @param bills：需要校验的单据
   * @param permissioncode：权限资源实体的编码：如单据类型(如5X)/业务组件、业务单元名称(如batchcode)
   * @param actioncode：元数据操作编码：如(insert、edit、delete、approve、unapprove)
   * @param billcodefiledname：单据号字段名：如(vbillcode)
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
            "pubapp_0", "0pubapp-0102")/*@res "单据"*/);
        if (!PubAppTool.isNull(billcodefiledname)) {
          String vbillcode =
              ValueUtils.getString(bill.getAttributeValue(billcodefiledname));
          if (!PubAppTool.isNull(vbillcode)) {
            error.append(vbillcode);
          }
        }
        error.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
            "pubapp_0", "0pubapp-0103")/*@res "不满足操作权限规则，请调整"*/);
        error.append(",");
      }
    }

    if (error.length() > 0) {
      error.deleteCharAt(error.length() - 1);
      ExceptionUtils.wrappBusinessException(error.toString());
    }

  }

}
