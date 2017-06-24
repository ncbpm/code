package nc.bs.sca.costbom.bp;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bd.framework.base.CMValueCheck;
import nc.bs.sca.costbom.plugin.bpplugin.CostBomPluginPoint;
import nc.bs.sca.costbom.rule.CostBomCalcCheckRule;
import nc.bs.sca.costbom.rule.CostBomToFenpeiXiShukRule;
import nc.bs.sca.pub.business.rule.CheckLockRule;
import nc.cmpub.business.adapter.BDAdapter;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.resa.factor.FactorVO;
import nc.vo.sca.costbom.entity.CostBomAggVO;
import nc.vo.sca.costbom.entity.CostBomStuffItemVO;

public class CostBomDeleteBP {
	private Map<String, FactorVO> factorMap = null;
	public void delete(CostBomAggVO[] bills) {

		DeleteBPTemplate<CostBomAggVO> bp = new DeleteBPTemplate<CostBomAggVO>(
				CostBomPluginPoint.DELETE);

		// 解决重复sql问题
		Set<String> factorAsoaList = new HashSet<String>();
		if (bills != null && bills.length > 0) {
			for (CostBomAggVO aggvo : bills) {
				CircularlyAccessibleValueObject[] childrenVos = aggvo
						.getAllChildrenVO();
				for (CircularlyAccessibleValueObject vo : childrenVos) {
					// 得到子项的核算要素信息
					String pk_factorasoa = (String) vo
							.getAttributeValue(CostBomStuffItemVO.CELEMENTID);
					if (CMValueCheck.isNotEmpty(pk_factorasoa)) {
						factorAsoaList.add(pk_factorasoa);
					}
				}
			}
		}
		try {
			factorMap = BDAdapter.queryFactorsFromAsoaPks(
					factorAsoaList.toArray(new String[0]), null);
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		// CostBomHeadVO headVO = (CostBomHeadVO) bills[0].getParent();
		//
		// if(Integer.parseInt(CalcStatusEnum.BEENCALC.getEnumValue().getValue())
		// == headVO.getIcalcstatus()){
		//
		// }

		// 执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());

		bp.delete(bills);
	}

	/**
	 * 删除前业务规则
	 */
	private void addBeforeRule(AroundProcesser<CostBomAggVO> processor) {
		// processor.addAfterRule(new CostBomDeleteCheck());
		processor.addBeforeRule(new CostBomCalcCheckRule());

		// 207-06-22 新增后规则 根据核算要素与成本系数对照，将成本BOM的产品同步到分配系数中
		processor.addAfterRule(new CostBomToFenpeiXiShukRule(factorMap));

	}

	/**
	 * 删除后业务规则
	 */
	@SuppressWarnings("unchecked")
	private void addAfterRule(AroundProcesser<CostBomAggVO> processor) {
		// 工厂成本类型锁
		processor.addAfterRule(new CheckLockRule());
	}
}
