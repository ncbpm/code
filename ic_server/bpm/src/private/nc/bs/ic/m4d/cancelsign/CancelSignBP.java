package nc.bs.ic.m4d.cancelsign;

import nc.bs.ic.general.cancelsign.CancelSignBPTemplate;
import nc.bs.ic.general.cancelsign.ICancelSignBP;
import nc.bs.ic.general.cancelsign.ICancelSignRuleProvider;
import nc.bs.ic.general.cancelsign.rule.CancelSignCheckAssetFlag;
import nc.bs.ic.general.cancelsign.rule.CheckHandoverState;
import nc.bs.ic.m4d.base.BPPlugInPoint;
import nc.bs.ic.m4d.base.UpdateSCOnhandRule;
import nc.bs.ic.m4d.cancelsign.rule.AfterCancelSignRuleFivemetals;
import nc.bs.ic.m4d.cancelsign.rule.AfterCancelSignRuleForLiabilityProcess;
import nc.bs.ic.m4d.cancelsign.rule.PushDeleteIAandTOBills;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.ic.m4d.entity.MaterialOutVO;

/**
 * <p>
 * <b>材料出库单取消签字：</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author chennn
 * @time 2010-4-19 上午09:41:30
 */
public class CancelSignBP implements ICancelSignBP<MaterialOutVO>,
		ICancelSignRuleProvider<MaterialOutVO> {

	@Override
	public void addAfterRule(MaterialOutVO[] vos,
			AroundProcesser<MaterialOutVO> processor) {
		processor.addAfterRule(new UpdateSCOnhandRule(true));
		processor.addAfterRule(new PushDeleteIAandTOBills());
		processor.addAfterRule(new AfterCancelSignRuleForLiabilityProcess());
		processor.addAfterRule(new AfterCancelSignRuleFivemetals());
	}

	@Override
	public void addBeforeRule(MaterialOutVO[] vos,
			AroundProcesser<MaterialOutVO> processor) {
		processor.addBeforeRule(new CancelSignCheckAssetFlag<MaterialOutVO>());
		processor.addBeforeRule(new CheckHandoverState<MaterialOutVO>());

		// 检查单据是否来自委外（委外核销、委外调整）
		// processor.addBeforeRule(new CheckBillSourceFromSC());

	}

	@Override
	public MaterialOutVO[] cancelSign(MaterialOutVO[] vos) {
		CancelSignBPTemplate<MaterialOutVO> cancelBP = new CancelSignBPTemplate<MaterialOutVO>(
				BPPlugInPoint.CancelSignBP, this);
		return cancelBP.cancelSign(vos);

	}

}
