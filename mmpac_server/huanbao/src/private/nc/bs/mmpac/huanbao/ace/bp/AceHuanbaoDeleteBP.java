package nc.bs.mmpac.huanbao.ace.bp;

import nc.bs.mmpac.huanbao.plugin.bpplugin.HuanbaoPluginPoint;
import nc.vo.mmpac.huanbao.AggHuanbaoHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceHuanbaoDeleteBP {

	public void delete(AggHuanbaoHVO[] bills) {

		DeleteBPTemplate<AggHuanbaoHVO> bp = new DeleteBPTemplate<AggHuanbaoHVO>(
				HuanbaoPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggHuanbaoHVO> processer) {
		// TODO 前规则
		IRule<AggHuanbaoHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggHuanbaoHVO> processer) {
		// TODO 后规则

	}
}
