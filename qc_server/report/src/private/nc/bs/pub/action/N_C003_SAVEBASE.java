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
 * ��ע���ʼ챨��ı��� ���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ �������ڣ�(2010-6-3)
 * 
 * @author ƽ̨�ű�����
 */
public class N_C003_SAVEBASE extends AbstractCompiler2 {

  /**
   * N_C003_SAVEBASE ������ע�⡣
   */
  public N_C003_SAVEBASE() {
    super();
  }

  /*
   * ��ע��ƽ̨��дԭʼ�ű�
   */
  @Override
  public String getCodeRemark() {
    return "	/*********************** �����ʼ챨��Ĳ��� �����޸� ******************/\n      Object retValue = null;\n      nc.vo.qc.c003.entity.ReportVO[] inObject = (nc.vo.qc.c003.entity.ReportVO[]) getVos();\n      \n      retValue = nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.qc.c003.maintain.IReportMaintain.class)\n          .saveBase(inObject);\n      /***********************************************************************/\n      return retValue;\n";/*
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  * -=
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  * notranslate
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  * =
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  * -
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  */
  }

  /*
   * ��ע��ƽ̨��д������ �ӿ�ִ����
   */
  @Override
  public Object runComClass(PfParameterVO vo) throws BusinessException {
    try {
      super.m_tmpVo = vo;
      Object userObj = vo.m_userObj;
      /*********************** �����ʼ챨��Ĳ��� �����޸� ******************/
      Object retValue = null;
      nc.vo.qc.c003.entity.ReportVO[] vos =
          (nc.vo.qc.c003.entity.ReportVO[]) this.getVos();

      /***********************************************************************/
      //2017-06-25 1. �����жϱ���ϸ�Ʒû�й�ѡ������Ϊ�ǲ��ϸ�Ʒ����Ҫ�Զ�����ͷ����Ҫ���ϸ�Ʒ���� ��ѡ
      new RejectBeforCheckRule().process(vos);
      //2017-06-25 1. �����жϱ���ϸ�Ʒû�й�ѡ������Ϊ�ǲ��ϸ�Ʒ����Ҫ�Զ�����ͷ����Ҫ���ϸ�Ʒ���� ��ѡ
      PfParameterUtil<ReportVO> util =
          new PfParameterUtil<ReportVO>(this.getPfParameterVO(), vos);
      ReportVO[] clientFullBills = util.getClientFullInfoBill();
      ReportVO[] originBills = null;
      // �޸�ʱȡ��ԭʼVO
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
