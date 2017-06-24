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
 * �ɱ�BOM ����BP
 * 
 */
public class CostBomInsertBP {

	private Map<String, FactorVO> factorMap = null;

	public CostBomAggVO[] insert(CostBomAggVO[] bills) {
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

		// ��������ģ��
		InsertBPTemplate<CostBomAggVO> bp = new InsertBPTemplate<CostBomAggVO>(
				CostBomPluginPoint.INSERT);
		// ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ִ�к����
		this.addAfterRule(bp.getAroundProcesser());

		return bp.insert(bills);
	}

	/**
	 * ����ǰ����
	 */
	@SuppressWarnings("unchecked")
	private void addBeforeRule(AroundProcesser<CostBomAggVO> processor) {
		// ��鵥�ݱ��岻��Ϊ��
		processor.addBeforeRule(new CheckNotNullRule());
		// ���ȼ��
        processor.addBeforeFinalRule(new FieldLengthCheckRule());
		// ����ʱ�����˵������Ϣ����
		processor
				.addBeforeRule(new CostBomAuditRule(CostBomPluginPoint.INSERT));
		processor.addBeforeRule(new CostBomDefaultsValueRule());
		// ����Ҫ���Ƿ�ͣ��У��
		processor.addBeforeRule(new CostBomFactorEnableRule(factorMap));
		// �����������Ҫ��У��
		processor.addBeforeRule(new CostBomFeeFactorRelatedRule(factorMap));
		// �����������Ҫ��У��
		processor.addBeforeRule(new CostBomStuffFactorRelateRule(factorMap));
		// ��������ͷ����������Ҫ�ز����ظ�
		// processor.addBeforeRule(new CostBomFactorRepeatRule());
		// ��������Ƿ���䵽��֯
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
	 * ���������
	 */
	@SuppressWarnings("unchecked")
	private void addAfterRule(AroundProcesser<CostBomAggVO> processor) {
		processor.addAfterRule(new OrgDisabledCheckRule(CostBomHeadVO.PK_ORG,
				IOrgConst.FACTORYTYPE));
		// �����ɱ�������
		processor.addAfterRule(new CheckLockRule());
		// У��BomΨһ��
		processor.addAfterRule(new CostBomUniqueCheckRule());
		// У���������Ψһ��
		processor.addAfterRule(new CostBomMaterialUniqueCheckRule());
		// У��������Ʒ��Ωһ��
		processor.addAfterRule(new CostBomExtraUniqueCheckRule());
		// ��ͷ����������У��
		processor.addAfterRule(new CostBomMaterialRepeatRule());
		// У�鵱ǰ��Ʒ��������Ʒ��������ϵ
		processor.addAfterRule(new CostBomMaterialTypeCheckRule());
		// У���������Ψһ��
		processor.addAfterRule(new CostBomFeeUniqueCheckRule());
		
		//207-06-22 ��������� ���ݺ���Ҫ����ɱ�ϵ�����գ����ɱ�BOM�Ĳ�Ʒͬ��������ϵ����
		processor.addAfterRule(new CostBomToFenpeiXiShukRule(factorMap));


	}
}
