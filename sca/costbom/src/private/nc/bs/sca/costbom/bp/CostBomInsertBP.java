package nc.bs.sca.costbom.bp;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bd.business.rule.CheckMaterialRule;
import nc.bd.framework.base.CMValueCheck;
import nc.bs.pubapp.pub.rule.CheckNotNullRule;
import nc.bs.pubapp.pub.rule.FieldLengthCheckRule;
import nc.bs.pubapp.pub.rule.OrgDisabledCheckRule;
import nc.bs.sca.costbom.plugin.bpplugin.CostBomPluginPoint;
import nc.bs.sca.costbom.rule.CostBomAuditRule;
import nc.bs.sca.costbom.rule.CostBomDefaultsValueRule;
import nc.bs.sca.costbom.rule.CostBomExtraElementdCheckRule;
import nc.bs.sca.costbom.rule.CostBomExtraUniqueCheckRule;
import nc.bs.sca.costbom.rule.CostBomFactorEnableRule;
import nc.bs.sca.costbom.rule.CostBomFactorNotNullRule;
import nc.bs.sca.costbom.rule.CostBomFeeFactorRelatedRule;
import nc.bs.sca.costbom.rule.CostBomFeeQuantumCheckRule;
import nc.bs.sca.costbom.rule.CostBomFeeUniqueCheckRule;
import nc.bs.sca.costbom.rule.CostBomFinstoragetypeCheck;
import nc.bs.sca.costbom.rule.CostBomMaterialAndElementCheckRule;
import nc.bs.sca.costbom.rule.CostBomMaterialRepeatRule;
import nc.bs.sca.costbom.rule.CostBomMaterialTypeCheckRule;
import nc.bs.sca.costbom.rule.CostBomMaterialUniqueCheckRule;
import nc.bs.sca.costbom.rule.CostBomNullElementdCheckRule;
import nc.bs.sca.costbom.rule.CostBomStuffFactorRelateRule;
import nc.bs.sca.costbom.rule.CostBomToFenpeiXiShukRule;
import nc.bs.sca.costbom.rule.CostBomUniqueCheckRule;
import nc.bs.sca.pub.business.rule.CheckLockRule;
import nc.cmpub.business.adapter.BDAdapter;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.org.IOrgConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.resa.factor.FactorVO;
import nc.vo.sca.costbom.entity.CostBomAggVO;
import nc.vo.sca.costbom.entity.CostBomHeadVO;
import nc.vo.sca.costbom.entity.CostBomStuffItemVO;

/**
 * 成本BOM 新增BP
 * 
 */
public class CostBomInsertBP {

	private Map<String, FactorVO> factorMap = null;

	public CostBomAggVO[] insert(CostBomAggVO[] bills) {
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

		// 调用新增模板
		InsertBPTemplate<CostBomAggVO> bp = new InsertBPTemplate<CostBomAggVO>(
				CostBomPluginPoint.INSERT);
		// 执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 执行后规则
		this.addAfterRule(bp.getAroundProcesser());

		return bp.insert(bills);
	}

	/**
	 * 新增前规则
	 */
	@SuppressWarnings("unchecked")
	private void addBeforeRule(AroundProcesser<CostBomAggVO> processor) {
		// 检查单据表体不能为空
		processor.addBeforeRule(new CheckNotNullRule());
		// 长度检查
        processor.addBeforeFinalRule(new FieldLengthCheckRule());
		// 新增时创建人等审计信息设置
		processor
				.addBeforeRule(new CostBomAuditRule(CostBomPluginPoint.INSERT));
		processor.addBeforeRule(new CostBomDefaultsValueRule());
		// 核算要素是否停用校验
		processor.addBeforeRule(new CostBomFactorEnableRule(factorMap));
		// 费用子项核算要素校验
		processor.addBeforeRule(new CostBomFeeFactorRelatedRule(factorMap));
		// 材料子项核算要素校验
		processor.addBeforeRule(new CostBomStuffFactorRelateRule(factorMap));
		// 材料子项和费用子项核算要素不能重复
		// processor.addBeforeRule(new CostBomFactorRepeatRule());
		// 检查物料是否分配到组织
		processor.addBeforeRule(new CheckMaterialRule());
		// 检查副产品对应的核算要素不能为空
		processor.addBeforeRule(new CostBomExtraElementdCheckRule());
		// 检查作业对应的核算要素不能为空
		processor.addBeforeRule(new CostBomNullElementdCheckRule());
		// 材料子项单价金额数量校验
		// processor.addBeforeRule(new
		// CostBomVOCalculatorRule(CostBomStuffItemVO.class));
		// 多产出单价金额数量校验
		// processor.addBeforeRule(new
		// CostBomVOCalculatorRule(CostBomFeeVO.class));
		// 材料子项以及多产出页签物料与核算要素对照关系检查
		processor.addBeforeRule(new CostBomMaterialAndElementCheckRule());
		// 多产出页签物料与核算要素对照关系检查
		// processor.addBeforeRule(new
		// CostBomMaterialAndElementCheckRule(CostBomExtraVO.class));
		// 材料子项对应核算要素不能为空校验
		processor.addBeforeRule(new CostBomFactorNotNullRule());
		// 费用子项费率金额等的校验
		processor.addBeforeRule(new CostBomFeeQuantumCheckRule());
		// 入库类型不能为空校验
		processor.addBeforeRule(new CostBomFinstoragetypeCheck());
		// 清除成本BOM上未启用的布尔型辅助属性默认值
		//processor.addBeforeRule(new CostBomClearDisableBooleanAssValueRule());
	}

	/**
	 * 新增后规则
	 */
	@SuppressWarnings("unchecked")
	private void addAfterRule(AroundProcesser<CostBomAggVO> processor) {
		processor.addAfterRule(new OrgDisabledCheckRule(CostBomHeadVO.PK_ORG,
				IOrgConst.FACTORYTYPE));
		// 工厂成本类型锁
		processor.addAfterRule(new CheckLockRule());
		// 校验Bom唯一性
		processor.addAfterRule(new CostBomUniqueCheckRule());
		// 校验材料子项唯一性
		processor.addAfterRule(new CostBomMaterialUniqueCheckRule());
		// 校验联副产品的惟一性
		processor.addAfterRule(new CostBomExtraUniqueCheckRule());
		// 表头材料与表体的校验
		processor.addAfterRule(new CostBomMaterialRepeatRule());
		// 校验当前产品与其他产品的主联关系
		processor.addAfterRule(new CostBomMaterialTypeCheckRule());
		// 校验费用子项唯一性
		processor.addAfterRule(new CostBomFeeUniqueCheckRule());
		
		//207-06-22 新增后规则 根据核算要素与成本系数对照，将成本BOM的产品同步到分配系数中
		processor.addAfterRule(new CostBomToFenpeiXiShukRule(factorMap));


	}
}
