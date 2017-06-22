package nc.bs.sca.costbom.bp;

import nc.bs.sca.costbom.plugin.bpplugin.CostBomPluginPoint;
import nc.bs.sca.costbom.rule.CostBomCalcCheckRule;
import nc.bs.sca.pub.business.rule.CheckLockRule;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.sca.costbom.entity.CostBomAggVO;

public class CostBomDeleteBP {

	public void delete(CostBomAggVO[] bills) {

		DeleteBPTemplate<CostBomAggVO> bp = new DeleteBPTemplate<CostBomAggVO>(CostBomPluginPoint.DELETE);

		// CostBomHeadVO headVO = (CostBomHeadVO) bills[0].getParent();
		//
		// if(Integer.parseInt(CalcStatusEnum.BEENCALC.getEnumValue().getValue())
		// == headVO.getIcalcstatus()){
		//
		// }

		// ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());

		bp.delete(bills);
	}

	/**
	 * ɾ��ǰҵ�����
	 */
	private void addBeforeRule(AroundProcesser<CostBomAggVO> processor) {
		// processor.addAfterRule(new CostBomDeleteCheck());
		processor.addBeforeRule(new CostBomCalcCheckRule());
	}

	/**
	 * ɾ����ҵ�����
	 */
	@SuppressWarnings("unchecked")
	private void addAfterRule(AroundProcesser<CostBomAggVO> processor) {
		// �����ɱ�������
		processor.addAfterRule(new CheckLockRule());
	}
}
