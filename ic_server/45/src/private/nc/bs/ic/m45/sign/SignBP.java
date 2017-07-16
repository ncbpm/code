package nc.bs.ic.m45.sign;

import nc.bs.ic.general.sign.ISignBP;
import nc.bs.ic.general.sign.ISignRuleProvider;
import nc.bs.ic.general.sign.SignBPTemplate;
import nc.bs.ic.m45.base.BPPlugInPoint;
import nc.bs.ic.m45.sign.rule.AfterRuleForPush5X;
import nc.bs.ic.m45.sign.rule.AfterRuleForRewritePUSettle;
import nc.bs.ic.m45.sign.rule.AfterSignRuleForFinanceProcess;
import nc.bs.ic.m45.sign.rule.AfterSignRuleForLiabilityProcess;
import nc.bs.ic.m45.sign.rule.AfterSignRuleForReserveProcess;
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
    //2017-07-15 liyf
//    1.产成品入库如果是按照销售订单生成的销售订单，且入库的物料是销售订单上物料，则改物料+批次动预留给该销售订单
    processor.addAfterRule(new AfterSignRuleForReserveProcess());

  }

  @Override
  public void addBeforeRule(PurchaseInVO[] vos,
      AroundProcesser<PurchaseInVO> processor) {
    processor.addBeforeRule(new BeforeRuleForCheckPU());
  }

}
