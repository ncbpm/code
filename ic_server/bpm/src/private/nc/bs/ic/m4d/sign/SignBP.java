package nc.bs.ic.m4d.sign;

import nc.bs.ic.general.sign.ISignBP;
import nc.bs.ic.general.sign.ISignRuleProvider;
import nc.bs.ic.general.sign.SignBPTemplate;
import nc.bs.ic.m4d.base.BPPlugInPoint;
import nc.bs.ic.m4d.base.UpdateSCOnhandRule;
import nc.bs.ic.m4d.sign.rule.AfterSignRuleFivemetals;
import nc.bs.ic.m4d.sign.rule.AfterSignRuleForLiabilityProcess;
import nc.bs.ic.m4d.sign.rule.PushSaveIAandTOBill;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.ic.m4d.entity.MaterialOutVO;

/**
 * <p>
 * <b>材料出库签字：</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author chennn
 * @time 2010-4-19 上午09:53:49
 */
public class SignBP implements ISignBP<MaterialOutVO>,
		ISignRuleProvider<MaterialOutVO> {

	@Override
	public void addAfterRule(MaterialOutVO[] vos,
			AroundProcesser<MaterialOutVO> processor) {

		processor.addAfterRule(new UpdateSCOnhandRule(false));
		processor.addAfterRule(new PushSaveIAandTOBill());
		processor.addAfterRule(new AfterSignRuleForLiabilityProcess());
		processor.addAfterRule(new AfterSignRuleFivemetals());
	}

	@Override
	public void addBeforeRule(MaterialOutVO[] vos,
			AroundProcesser<MaterialOutVO> processor) {
		// TODO chennn 补充规则

	}

	@Override
	public MaterialOutVO[] sign(MaterialOutVO[] vos) {
		SignBPTemplate<MaterialOutVO> signBP = new SignBPTemplate<MaterialOutVO>(
				BPPlugInPoint.SignBP, this);
		return signBP.sign(vos);
	}

}
