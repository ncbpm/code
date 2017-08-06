package nc.bs.ic.m45.sign;

import nc.bs.ic.general.sign.ISignBP;
import nc.bs.ic.general.sign.ISignRuleProvider;
import nc.bs.ic.general.sign.SignBPTemplate;
import nc.bs.ic.m45.base.BPPlugInPoint;
import nc.bs.ic.m45.sign.rule.AfterRuleForPush5X;
import nc.bs.ic.m45.sign.rule.AfterRuleForRewritePUSettle;
import nc.bs.ic.m45.sign.rule.AfterSignRuleForFinanceProcess;
import nc.bs.ic.m45.sign.rule.AfterSignRuleForLiabilityProcess;
import nc.bs.ic.m45.sign.rule.AfterSignRuleForPayPlanProcess;
import nc.bs.ic.m45.sign.rule.BeforeRuleForCheckPU;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.ic.m45.entity.PurchaseInVO;

/**
 * 采购入库单后台签字BP
 * 
 * @author songhy
 */
public class SignBP implements ISignBP<PurchaseInVO>,
    ISignRuleProvider<PurchaseInVO> {

  @Override
  public PurchaseInVO[] sign(PurchaseInVO[] bills) {
    SignBPTemplate<PurchaseInVO> signBP =
        new SignBPTemplate<PurchaseInVO>(BPPlugInPoint.SignAction, this);
    PurchaseInVO[] resultVOs = signBP.sign(bills);
    return resultVOs;
  }

  @Override
  public void addAfterRule(PurchaseInVO[] vos,
      AroundProcesser<PurchaseInVO> processor) {
    processor.addAfterRule(new AfterRuleForRewritePUSettle());
    processor.addAfterRule(new AfterRuleForPush5X());
    processor.addAfterRule(new AfterSignRuleForFinanceProcess());
    processor.addAfterRule(new AfterSignRuleForLiabilityProcess());
    //2017-07-30 回写付款计划
    processor.addAfterRule(new AfterSignRuleForPayPlanProcess());

    
    
  }

  @Override
  public void addBeforeRule(PurchaseInVO[] vos,
      AroundProcesser<PurchaseInVO> processor) {
    processor.addBeforeRule(new BeforeRuleForCheckPU());
  }

}
