package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.mmpac.huanbao.plugin.bpplugin.HuanbaoPluginPoint;
import nc.vo.mmpac.huanbao.AggHuanbaoHVO;
import nc.itf.mmpac.IHuanbaoMaintain;

public class N_55Z1_UNSAVEBILL extends AbstractPfAction<AggHuanbaoHVO> {

	@Override
	protected CompareAroundProcesser<AggHuanbaoHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggHuanbaoHVO> processor = new CompareAroundProcesser<AggHuanbaoHVO>(
				HuanbaoPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggHuanbaoHVO[] processBP(Object userObj,
			AggHuanbaoHVO[] clientFullVOs, AggHuanbaoHVO[] originBills) {
		IHuanbaoMaintain operator = NCLocator.getInstance().lookup(
				IHuanbaoMaintain.class);
		AggHuanbaoHVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
