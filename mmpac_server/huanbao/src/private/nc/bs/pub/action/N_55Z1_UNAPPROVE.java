package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.mmpac.huanbao.plugin.bpplugin.HuanbaoPluginPoint;
import nc.vo.mmpac.huanbao.AggHuanbaoHVO;
import nc.itf.mmpac.IHuanbaoMaintain;

public class N_55Z1_UNAPPROVE extends AbstractPfAction<AggHuanbaoHVO> {

	@Override
	protected CompareAroundProcesser<AggHuanbaoHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggHuanbaoHVO> processor = new CompareAroundProcesser<AggHuanbaoHVO>(
				HuanbaoPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggHuanbaoHVO[] processBP(Object userObj,
			AggHuanbaoHVO[] clientFullVOs, AggHuanbaoHVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggHuanbaoHVO[] bills = null;
		try {
			IHuanbaoMaintain operator = NCLocator.getInstance()
					.lookup(IHuanbaoMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
