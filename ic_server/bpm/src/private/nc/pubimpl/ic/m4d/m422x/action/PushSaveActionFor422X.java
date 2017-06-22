package nc.pubimpl.ic.m4d.m422x.action;

import nc.bs.ic.general.rule.before.IgnoreSafetyStockCheck;
import nc.bs.ic.m4d.insert.InsertBP;
import nc.bs.ic.m4d.sign.SignBP;
import nc.bs.ic.pub.base.ICAroundProcesser;
import nc.impl.ic.general.GeneralPushSaveAction;
import nc.impl.pubapp.pattern.rule.plugin.IPluginPoint;
import nc.vo.ic.m4d.entity.MaterialOutVO;

public class PushSaveActionFor422X extends GeneralPushSaveAction<MaterialOutVO> {

	/**
	 * @param point
	 * @param insertBP
	 */
	public PushSaveActionFor422X(IPluginPoint point) {
		super(point, new InsertBP(), new SignBP());
	}

	@Override
	public void addBeforeRule(ICAroundProcesser<MaterialOutVO> processor) {
		processor.addBeforeRule(new Push422Xto4DProcess());
		processor.addBeforeRule(new IgnoreSafetyStockCheck<MaterialOutVO>());
	}

}
