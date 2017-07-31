package nc.bs.ic.m45.cancelsign;

import nc.bs.ic.general.cancelsign.CancelSignBPTemplate;
import nc.bs.ic.general.cancelsign.ICancelSignBP;
import nc.bs.ic.general.cancelsign.ICancelSignRuleProvider;
import nc.bs.ic.general.cancelsign.rule.CancelSignCheckAssetFlag;
import nc.bs.ic.m45.base.BPPlugInPoint;
import nc.bs.ic.m45.cancelsign.rule.AfterCancelSignRuleForLiabilityProcess;
import nc.bs.ic.m45.cancelsign.rule.AfterRuleForCancelRewritePUSettle;
import nc.bs.ic.m45.cancelsign.rule.AfterUnsignRuleForFinanceProcess;
import nc.bs.ic.m45.cancelsign.rule.BeforeRuleForDelete5X;
import nc.bs.ic.m45.cancelsign.rule.CancelSignCheckEtpickup;
import nc.bs.ic.m45.cancelsign.rule.CheckCancelSignRule;
import nc.bs.ic.m45.cancelsign.rule.EtdetlpickRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.ic.m45.entity.PurchaseInVO;

/**
 * @author songhy
 *
 * 2010-1-21 ÏÂÎç02:14:48
 */
public class CancelSignBP 
  implements ICancelSignBP<PurchaseInVO>, 
  ICancelSignRuleProvider<PurchaseInVO> {

  @Override
  public PurchaseInVO[] cancelSign(PurchaseInVO[] bills) {
    CancelSignBPTemplate<PurchaseInVO> cancelSignBP 
      = new CancelSignBPTemplate<PurchaseInVO>(BPPlugInPoint.CancelSignAction, this);
    PurchaseInVO[] resultVOs = cancelSignBP.cancelSign(bills);
    return resultVOs;
  }

  @Override
  public void addAfterRule(PurchaseInVO[] vos,
      AroundProcesser<PurchaseInVO> processor) {
    processor.addAfterRule( new AfterRuleForCancelRewritePUSettle() );
    processor.addAfterRule( new AfterUnsignRuleForFinanceProcess() );
    processor.addAfterRule( new AfterCancelSignRuleForLiabilityProcess() );
  }

  @Override
  public void addBeforeRule(PurchaseInVO[] vos,
      AroundProcesser<PurchaseInVO> processor) {
    processor.addBeforeRule(new CheckCancelSignRule());
    processor.addBeforeRule(new BeforeRuleForDelete5X());
    processor.addBeforeRule(new CancelSignCheckAssetFlag<PurchaseInVO>());
    processor.addBeforeRule(new CancelSignCheckEtpickup());
    processor.addBeforeRule(new EtdetlpickRule());
  }

}
