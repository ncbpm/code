package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.mmpac.huanbao.plugin.bpplugin.HuanbaoPluginPoint;
import nc.vo.mmpac.huanbao.AggHuanbaoHVO;
import nc.itf.mmpac.IHuanbaoMaintain;

public class N_55Z1_DELETE extends AbstractPfAction<AggHuanbaoHVO> {

	@Override
	protected CompareAroundProcesser<AggHuanbaoHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggHuanbaoHVO> processor = new CompareAroundProcesser<AggHuanbaoHVO>(
				HuanbaoPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggHuanbaoHVO[] processBP(Object userObj,
			AggHuanbaoHVO[] clientFullVOs, AggHuanbaoHVO[] originBills) {
		IHuanbaoMaintain operator = NCLocator.getInstance().lookup(
				IHuanbaoMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
