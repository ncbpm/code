package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.mmpac.huanbao.plugin.bpplugin.HuanbaoPluginPoint;
import nc.vo.mmpac.huanbao.AggHuanbaoHVO;
import nc.itf.mmpac.IHuanbaoMaintain;

public class N_55Z1_APPROVE extends AbstractPfAction<AggHuanbaoHVO> {

	public N_55Z1_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggHuanbaoHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggHuanbaoHVO> processor = new CompareAroundProcesser<AggHuanbaoHVO>(
				HuanbaoPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggHuanbaoHVO[] processBP(Object userObj,
			AggHuanbaoHVO[] clientFullVOs, AggHuanbaoHVO[] originBills) {
		AggHuanbaoHVO[] bills = null;
		IHuanbaoMaintain operator = NCLocator.getInstance().lookup(
				IHuanbaoMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
