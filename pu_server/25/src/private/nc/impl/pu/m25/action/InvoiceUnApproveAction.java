/**
 * $�ļ�˵��$
 * 
 * @author zhaoyha
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-1-26 ����02:38:25
 */
package nc.impl.pu.m25.action;

import nc.bs.pu.m25.plugin.InvoicePluginPoint;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.scmpub.pf.PfParameterUtil;
import nc.impl.pu.m25.action.rule.unapprove.AfterUnApproveRuleForPayPlanProcess;
import nc.impl.pu.m25.action.rule.unapprove.CancelSendAPDriveChkRule;
import nc.impl.pu.m25.action.rule.unapprove.SettledCheckRule;
import nc.impl.pu.m25.action.rule.unapprove.UnApproveAfterEventRuel;
import nc.impl.pu.m25.action.rule.unapprove.UnApproveBeforeEventRuel;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pu.m25.entity.InvoiceVO;
import nc.vo.pu.m25.rule.approve.UnApproveStatusChkRule;
import nc.vo.pu.pub.enumeration.PuBusiLogActionCode;
import nc.vo.pu.pub.enumeration.PuBusiLogPathCode;
import nc.vo.pu.pub.rule.ApproverPermissionRule;
import nc.vo.pu.pub.rule.busilog.WriteOperateLogRule;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.res.billtype.POBillType;

/**
 * <p>
 * <b>������Ҫ������¹��ܣ�</b>
 * <ul>
 * <li>�ɹ���Ʊ������
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author zhaoyha
 * @time 2010-1-26 ����02:38:25
 */
public class InvoiceUnApproveAction {
  public InvoiceVO[] unapprove(InvoiceVO[] invoiceVOs, AbstractCompiler2 script) {
    PfParameterUtil<InvoiceVO> util =
        new PfParameterUtil<InvoiceVO>(script == null ? null
            : script.getPfParameterVO(), invoiceVOs);
    InvoiceVO[] orgVos = util.getClientOrignBills();
    InvoiceVO[] vos = util.getClientFullInfoBill();
    CompareAroundProcesser<InvoiceVO> prcr =
        new CompareAroundProcesser<InvoiceVO>(InvoicePluginPoint.UNAPPROVE);
    this.addRule(prcr);
    prcr.before(vos, orgVos);
    if (null != script) {
      try {
        script.procUnApproveFlow(script.getPfParameterVO());
        for (InvoiceVO vo : vos) {
          vo.getParentVO().setStatus(VOStatus.UPDATED);
        }
      }
      catch (Exception e) {
        // ��־�쳣
        ExceptionUtils.wrappException(e);

      }
    }
    BillUpdate<InvoiceVO> update = new BillUpdate<InvoiceVO>();
    InvoiceVO[] returnVos = update.update(vos, orgVos);
    
    prcr.after(returnVos, orgVos);
    
    return returnVos;
  }

  private void addRule(CompareAroundProcesser<InvoiceVO> prcr) {
    // ��Ʊ��ȡ������״̬���
    prcr.addBeforeFinalRule(new UnApproveStatusChkRule());
    // ��д����ƻ�
    prcr.addAfterRule(new AfterUnApproveRuleForPayPlanProcess());
    // �Ƿ��Ѿ����������
    prcr.addBeforeFinalRule(new SettledCheckRule());
    prcr.addBeforeRule(new ApproverPermissionRule<InvoiceVO>(POBillType.Invoice
        .getCode()));
    // �ж��Ƿ�������ȡ����Ӧ��
    prcr.addBeforeFinalRule(new CancelSendAPDriveChkRule());
    // ҵ���¼� ǰ
    prcr.addBeforeFinalRule(new UnApproveBeforeEventRuel());
    // ҵ���¼� ��
    prcr.addAfterFinalRule(new UnApproveAfterEventRuel());
    // дҵ����־
    prcr.addAfterFinalRule(new WriteOperateLogRule<InvoiceVO>(
        PuBusiLogPathCode.invoiceApprovePath.getCode(),
        PuBusiLogActionCode.unapprove.getCode()));
  }
}
