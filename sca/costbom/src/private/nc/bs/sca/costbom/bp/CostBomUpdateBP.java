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
import nc.bs.sca.costbom.rule.CostBomUniqueCheckRule;
import nc.bs.sca.pub.business.rule.CheckLockRule;
import nc.cmpub.business.adapter.BDAdapter;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.org.IOrgConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.resa.factor.FactorVO;
import nc.vo.sca.costbom.entity.CostBomAggVO;
import nc.vo.sca.costbom.entity.CostBomHeadVO;
import nc.vo.sca.costbom.entity.CostBomStuffItemVO;

/**
 * �ɱ�BOM�޸� BP
 * 
 * @since 6.0
 * @version 2011-7-13 ����03:01:58
 * @author hupeng
 */

public class CostBomUpdateBP {

	private Map<String, FactorVO> factorMap = null;

	public CostBomAggVO[] update(CostBomAggVO[] bills,
			CostBomAggVO[] originBills) {
		// ����ظ�sql����
		Set<String> factorAsoaList = new HashSet<String>();

		if (bills != null && bills.length > 0) {
			for (CostBomAggVO aggvo : bills) {
				CircularlyAccessibleValueObject[] childrenVos = aggvo
						.getAllChildrenVO();
				for (CircularlyAccessibleValueObject vo : childrenVos) {
					// �õ�����ĺ���Ҫ����Ϣ
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

		// �����޸�ģ��
		UpdateBPTemplate<CostBomAggVO> bp = new UpdateBPTemplate<CostBomAggVO>(
				CostBomPluginPoint.UPDATE);

		// ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ִ�к����
		this.addAfterRule(bp.getAroundProcesser());

		return bp.update(bills, originBills);
	}

	/**
	 * �޸�ǰҵ�����
	 */
	@SuppressWarnings("unchecked")
	private void addBeforeRule(CompareAroundProcesser<CostBomAggVO> processor) {
		// У����岻��Ϊ��
		processor.addBeforeRule(new CheckNotNullRule());
		// ���ȼ��
        processor.addBeforeFinalRule(new FieldLengthCheckRule());
		// �����ϢУ�飬�����޸�����Ϣ
		processor
				.addBeforeRule(new CostBomAuditRule(CostBomPluginPoint.UPDATE));
		processor.addBeforeRule(new OrgDisabledCheckRule(CostBomHeadVO.PK_ORG,
				IOrgConst.FACTORYTYPE));
		processor.addBeforeRule(new CostBomDefaultsValueRule());
		// ��ȷ��
		// processor.addBeforeRule(new CostBomCCenterRule());
		// ����Ҫ���Ƿ�ͣ��У��
		processor.addBeforeRule(new CostBomFactorEnableRule(factorMap));
		// ��������ͷ����������Ҫ�ز����ظ�
		// processor.addBeforeRule(new CostBomFactorRepeatRule());
		// �����������Ҫ��У��
		processor.addBeforeRule(new CostBomStuffFactorRelateRule(factorMap));
		// �����������Ҫ��У��
		processor.addBeforeRule(new CostBomFeeFactorRelatedRule(factorMap));
		// У�������Ƿ���䵽��֯
		processor.addBeforeRule(new CheckMaterialRule());
		// ��鸱��Ʒ��Ӧ�ĺ���Ҫ�ز���Ϊ��
		processor.addBeforeRule(new CostBomExtraElementdCheckRule());
		// �����ҵ��Ӧ�ĺ���Ҫ�ز���Ϊ��
		processor.addBeforeRule(new CostBomNullElementdCheckRule());
		// ��������۽������У��
		// processor.addBeforeRule(new
		// CostBomVOCalculatorRule(CostBomStuffItemVO.class));
		// ��������۽������У��
		// processor.addBeforeRule(new
		// CostBomVOCalculatorRule(CostBomFeeVO.class));
		// ���������Լ������ҳǩ���������Ҫ�ض��չ�ϵ���
		processor.addBeforeRule(new CostBomMaterialAndElementCheckRule());
		// �����ҳǩ���������Ҫ�ض��չ�ϵ���
		// processor.addBeforeRule(new
		// CostBomMaterialAndElementCheckRule(CostBomExtraVO.class));
		// ���������Ӧ����Ҫ�ز���Ϊ��У��
		processor.addBeforeRule(new CostBomFactorNotNullRule());
		// ����������ʽ��ȵ�У��
		processor.addBeforeRule(new CostBomFeeQuantumCheckRule());
		// ������Ͳ���Ϊ��У��
		processor.addBeforeRule(new CostBomFinstoragetypeCheck());
		// ����ɱ�BOM��δ���õĲ����͸�������Ĭ��ֵ
		//processor.addBeforeRule(new CostBomClearDisableBooleanAssValueRule());
	}

	/**
	 * �޸ĺ�ҵ�����
	 */
	@SuppressWarnings("unchecked")
	private void addAfterRule(CompareAroundProcesser<CostBomAggVO> processor) {
		// �����ɱ�������
		processor.addAfterRule(new CheckLockRule());
		// У��BomΨһ��
		processor.addAfterRule(new CostBomUniqueCheckRule());
		// У��������Ʒ��Ωһ��
		processor.addAfterRule(new CostBomExtraUniqueCheckRule());
		// У���������Ψһ��
		processor.addAfterRule(new CostBomMaterialUniqueCheckRule());
		// ��ͷ����������У��
		processor.addAfterRule(new CostBomMaterialRepeatRule());
		// У�鵱ǰ��Ʒ��������Ʒ��������ϵ
		processor.addAfterRule(new CostBomMaterialTypeCheckRule());
		// У���������Ψһ��
		processor.addAfterRule(new CostBomFeeUniqueCheckRule());
	}
}
