package nc.bs.pub.action;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.scmpub.pf.PfParameterUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.c003.rule.RejectBeforCheckRule;
import nc.vo.uap.pf.PFBusinessException;

/**
 * 备注：质检报告的保存 单据动作执行中的动态执行类的动态执行类。 创建日期：(2010-6-3)
 * 
 * @author 平台脚本生成
 */
public class N_C003_SAVEBASE extends AbstractCompiler2 {

  /**
   * N_C003_SAVEBASE 构造子注解。
   */
  public N_C003_SAVEBASE() {
    super();
  }

  /*
   * 备注：平台编写原始脚本
   */
  @Override
  public String getCodeRemark() {
    return "	/*********************** 调用质检报告的操作 不可修改 ******************/\n      Object retValue = null;\n      nc.vo.qc.c003.entity.ReportVO[] inObject = (nc.vo.qc.c003.entity.ReportVO[]) getVos();\n      \n      retValue = nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.qc.c003.maintain.IReportMaintain.class)\n          .saveBase(inObject);\n      /***********************************************************************/\n      return retValue;\n";/*
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  * -=
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  * notranslate
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  * =
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  * -
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  */
  }

  /*
   * 备注：平台编写规则类 接口执行类
   */
  @Override
  public Object runComClass(PfParameterVO vo) throws BusinessException {
    try {
      super.m_tmpVo = vo;
      Object userObj = vo.m_userObj;
      /*********************** 调用质检报告的操作 不可修改 ******************/
      Object retValue = null;
      nc.vo.qc.c003.entity.ReportVO[] vos =
          (nc.vo.qc.c003.entity.ReportVO[]) this.getVos();

      /***********************************************************************/
      //2017-06-25 1. 保存判断表体合格品没有勾选，则认为是不合格品，需要自动将表头的需要不合格品处理 勾选
      new RejectBeforCheckRule().process(vos);
      //2017-06-25 1. 保存判断表体合格品没有勾选，则认为是不合格品，需要自动将表头的需要不合格品处理 勾选
      PfParameterUtil<ReportVO> util =
          new PfParameterUtil<ReportVO>(this.getPfParameterVO(), vos);
      ReportVO[] clientFullBills = util.getClientFullInfoBill();
      ReportVO[] originBills = null;
      // 修改时取下原始VO
      if (!(vos[0].getPrimaryKey() == null)
          || vos[0].getParent().getStatus() != VOStatus.NEW) {
        originBills = util.getOrginBills();
      }
      retValue =
          nc.bs.framework.common.NCLocator.getInstance()
              .lookup(nc.itf.qc.c003.maintain.IReportMaintain.class)
              .saveBase(clientFullBills, userObj, originBills);
      return retValue;
    }
    catch (Exception ex) {
      if (ex instanceof BusinessException) {
        throw (BusinessException) ex;
      }
      throw new PFBusinessException(ex.getMessage(), ex);
    }
  }

}
