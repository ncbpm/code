package nc.bs.mmpac.huanbao.ace.bp;

import nc.bs.mmpac.huanbao.plugin.bpplugin.HuanbaoPluginPoint;
import nc.vo.mmpac.huanbao.AggHuanbaoHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceHuanbaoDeleteBP {

	public void delete(AggHuanbaoHVO[] bills) {

		DeleteBPTemplate<AggHuanbaoHVO> bp = new DeleteBPTemplate<AggHuanbaoHVO>(
				HuanbaoPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggHuanbaoHVO> processer) {
		// TODO ǰ����
		IRule<AggHuanbaoHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggHuanbaoHVO> processer) {
		// TODO �����

	}
}
