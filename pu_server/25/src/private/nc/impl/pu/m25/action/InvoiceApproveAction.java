/**
 * $文件说明$
 * 
 * @author zhaoyha
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-1-26 下午02:37:09
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
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>发票审批动作
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author zhaoyha
 * @time 2010-1-26 下午02:37:09
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
        // 日志异常
        ExceptionUtils.wrappException(e);
      }
    }
    prcr.after(clientBills, originBills);
    // 在后规则中，有自动结算。不会更新到clientBills中，所以要重新查询
    String[] ids = AggVOUtil.getPrimaryKeys(clientBills);
    InvoiceVO[] approvedVOs =
        new BillQuery<InvoiceVO>(InvoiceVO.class).query(ids);

    // 更新流程平台中的VO
    PfScriptClassAdaptor pfadptor = new PfScriptClassAdaptor(script);
    pfadptor.setVo(
        null != approvedVOs && approvedVOs.length > 0 ? approvedVOs[0] : null);
    pfadptor.setVos(approvedVOs);
    
    return approvedVOs;
  }

  private void addRule(CompareAroundProcesser<InvoiceVO> prcr,
      InvoiceUIToBSEnv[] envs, PfParameterVO pfParameterVO) {
    // 发票能否审批的状态(冻结、审批过等检查、虚拟发票)
    prcr.addBeforeFinalRule(new ApproveStatusChkRule());
    // 供应商开票冻结检查
    prcr.addBeforeFinalRule(new SupplierInvoiceFrozenChkRule());
    // 业务事件 前
    prcr.addBeforeFinalRule(new ApproveBeforeEventRuel());
    // 写业务日志
    prcr.addAfterFinalRule(new WriteOperateLogRule<InvoiceVO>(
        PuBusiLogPathCode.invoiceApprovePath.getCode(),
        PuBusiLogActionCode.approve.getCode()));
    // 更新审批后信息
    prcr.addAfterFinalRule(new UpdateApproveInfoRule());
   // 回写付款计划
    prcr.addAfterRule(new AfterApproveRuleForPayPlanProcess());
    // 审批的后规则，只过滤到所有已经审批过的VO，后续规则注意判断空
    prcr.addAfterFinalRule(new ApprovedVOFilterRule<InvoiceVO>());
    // 业务事件 后
    prcr.addAfterFinalRule(new ApproveAfterEventRuel());
    // 过滤掉工序委外加工费结算单
    prcr.addAfterFinalRule(new VOFilterFor55E6Rule());
    // 为结算检查是否有未审核的费用发票
    prcr.addAfterRule(new ApproveTogetherFeeProcRule(envs));
    // 发票审批后自动结算
    prcr.addAfterFinalRule(new InvoiceAutoSettleRule());
    // 更新流程平台的VO--必须处理，如果前面规则已经过滤为空，则后规则就不执行了
    // 平台脚本中还是原来的VO，则还会驱动下游
    prcr.addAfterFinalRule(new UpdatePflowVORule<InvoiceVO>(pfParameterVO));
    
  }

}
