/**
 * $�ļ�˵��$
 * 
 * @author zhaoyha
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-1-26 ����02:37:09
 */
package nc.impl.pu.m25.action;

import nc.bs.pu.m25.plugin.InvoicePluginPoint;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.scmpub.pf.PfParameterUtil;
import nc.impl.pu.m25.action.rule.approve.AfterApproveRuleForPayPlanProcess;
import nc.impl.pu.m25.action.rule.approve.ApproveAfterEventRuel;
import nc.impl.pu.m25.action.rule.approve.ApproveBeforeEventRuel;
import nc.impl.pu.m25.action.rule.approve.ApproveTogetherFeeProcRule;
import nc.impl.pu.m25.action.rule.approve.SupplierInvoiceFrozenChkRule;
import nc.impl.pu.m25.action.rule.approve.UpdateApproveInfoRule;
import nc.impl.pu.m25.action.rule.approve.VOFilterFor55E6Rule;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.scmpub.reference.uap.pf.PfScriptClassAdaptor;
import nc.vo.pu.m25.entity.InvoiceVO;
import nc.vo.pu.m25.env.InvoiceUIToBSEnv;
import nc.vo.pu.m25.rule.InvoiceAutoSettleRule;
import nc.vo.pu.m25.rule.approve.ApproveStatusChkRule;
import nc.vo.pu.pub.enumeration.PuBusiLogActionCode;
import nc.vo.pu.pub.enumeration.PuBusiLogPathCode;
import nc.vo.pu.pub.rule.busilog.WriteOperateLogRule;
import nc.vo.pu.pub.rule.pf.ApprovedVOFilterRule;
import nc.vo.pu.pub.rule.pf.UpdatePflowVORule;
import nc.vo.pu.pub.util.AggVOUtil;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <p>
 * <b>������Ҫ������¹��ܣ�</b>
 * <ul>
 * <li>��Ʊ��������
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author zhaoyha
 * @time 2010-1-26 ����02:37:09
 */
public class InvoiceApproveAction {
  public InvoiceVO[] approve(InvoiceVO[] invoiceVOs, AbstractCompiler2 script,
      InvoiceUIToBSEnv[] envs) {
    PfParameterUtil<InvoiceVO> util =
        new PfParameterUtil<InvoiceVO>(script == null ? null
            : script.getPfParameterVO(), invoiceVOs);
    InvoiceVO[] originBills = util.getClientOrignBills();
    InvoiceVO[] clientBills = util.getClientFullInfoBill();
    CompareAroundProcesser<InvoiceVO> prcr =
        new CompareAroundProcesser<InvoiceVO>(InvoicePluginPoint.APPROVE);
    this.addRule(prcr, envs, null != script ? script.getPfParameterVO() : null);
    prcr.before(clientBills, originBills);
    if (null != script) {
      try {
        script.procFlowBacth(script.getPfParameterVO());
      }
      catch (Exception e) {
        // ��־�쳣
        ExceptionUtils.wrappException(e);
      }
    }
    prcr.after(clientBills, originBills);
    // �ں�����У����Զ����㡣������µ�clientBills�У�����Ҫ���²�ѯ
    String[] ids = AggVOUtil.getPrimaryKeys(clientBills);
    InvoiceVO[] approvedVOs =
        new BillQuery<InvoiceVO>(InvoiceVO.class).query(ids);

    // ��������ƽ̨�е�VO
    PfScriptClassAdaptor pfadptor = new PfScriptClassAdaptor(script);
    pfadptor.setVo(
        null != approvedVOs && approvedVOs.length > 0 ? approvedVOs[0] : null);
    pfadptor.setVos(approvedVOs);
    
    return approvedVOs;
  }

  private void addRule(CompareAroundProcesser<InvoiceVO> prcr,
      InvoiceUIToBSEnv[] envs, PfParameterVO pfParameterVO) {
    // ��Ʊ�ܷ�������״̬(���ᡢ�������ȼ�顢���ⷢƱ)
    prcr.addBeforeFinalRule(new ApproveStatusChkRule());
    // ��Ӧ�̿�Ʊ������
    prcr.addBeforeFinalRule(new SupplierInvoiceFrozenChkRule());
    // ҵ���¼� ǰ
    prcr.addBeforeFinalRule(new ApproveBeforeEventRuel());
    // дҵ����־
    prcr.addAfterFinalRule(new WriteOperateLogRule<InvoiceVO>(
        PuBusiLogPathCode.invoiceApprovePath.getCode(),
        PuBusiLogActionCode.approve.getCode()));
    // ������������Ϣ
    prcr.addAfterFinalRule(new UpdateApproveInfoRule());
   // ��д����ƻ�
    prcr.addAfterRule(new AfterApproveRuleForPayPlanProcess());
    // �����ĺ����ֻ���˵������Ѿ���������VO����������ע���жϿ�
    prcr.addAfterFinalRule(new ApprovedVOFilterRule<InvoiceVO>());
    // ҵ���¼� ��
    prcr.addAfterFinalRule(new ApproveAfterEventRuel());
    // ���˵�����ί��ӹ��ѽ��㵥
    prcr.addAfterFinalRule(new VOFilterFor55E6Rule());
    // Ϊ�������Ƿ���δ��˵ķ��÷�Ʊ
    prcr.addAfterRule(new ApproveTogetherFeeProcRule(envs));
    // ��Ʊ�������Զ�����
    prcr.addAfterFinalRule(new InvoiceAutoSettleRule());
    // ��������ƽ̨��VO--���봦�����ǰ������Ѿ�����Ϊ�գ�������Ͳ�ִ����
    // ƽ̨�ű��л���ԭ����VO���򻹻���������
    prcr.addAfterFinalRule(new UpdatePflowVORule<InvoiceVO>(pfParameterVO));
    
  }

}
