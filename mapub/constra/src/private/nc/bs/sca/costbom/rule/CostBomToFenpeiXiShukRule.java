package nc.bs.sca.costbom.rule;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bd.framework.base.CMValueCheck;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.mapub.allocfac.entity.AllocfacAggVO;
import nc.vo.mapub.allocfacotor.Factorofinv;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.resa.factor.FactorVO;
import nc.vo.sca.costbom.entity.CostBomAggVO;
import nc.vo.sca.costbom.entity.CostBomHeadVO;
import nc.vo.sca.costbom.entity.CostBomStuffItemVO;

/**
 * ���ݺ���Ҫ�ض�Ӧ�ķ���ϵ�����Զ����䵽����ϵ������
 * 
 * @author liyf
 * 
 */
public class CostBomToFenpeiXiShukRule implements IRule<CostBomAggVO> {

	private Map<String, FactorVO> factorMap = null;

	public CostBomToFenpeiXiShukRule(Map<String, FactorVO> factorMap) {
		this.factorMap = factorMap;
	}

	@Override
	public void process(CostBomAggVO[] bills) {
		// ���ݺ���Ҫ�ز�ѯ��Ӧ�ķ���ϵ��
		if (bills != null && bills.length > 0) {
			for (CostBomAggVO aggvo : bills) {
				Set<String> factorAsoaList = new HashSet<String>();
				CostBomHeadVO parentVO = (CostBomHeadVO) aggvo.getParentVO();
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
				//���ݺ���Ҫ�ز�ѯ ����Ҫ�������ϵ�����ձ��Ƿ��ж�Ӧ
				Factorofinv[] factor_fenpeixishu = queryFenPeixi(
						parentVO.getPk_group(), parentVO.getPk_org(),
						factorAsoaList);
				if (factor_fenpeixishu == null
						|| factor_fenpeixishu.length == 0) {
					continue;
				}
				Map<String, String> fator_map = new HashMap<String, String>();
				for (Factorofinv vo : factor_fenpeixishu) {
					fator_map.put(vo.getPk_factor(), vo.getPk_allocfac());
				}
				//��ѯ����ϵ��
				Map<String, AllocfacAggVO>  allocfac= queryAlloffac(fator_map.values());

				for (CircularlyAccessibleValueObject vo : childrenVos) {
					// �õ�����ĺ���Ҫ����Ϣ
					String pk_factorasoa = (String) vo
							.getAttributeValue(CostBomStuffItemVO.CELEMENTID);
					if (vo.getStatus() == VOStatus.NEW) {
						if(fator_map.containsKey(pk_factorasoa)){
//							addxishu(factorMap);/
						}
					}
					
					if (vo.getStatus() == VOStatus.DELETED) {

					}
					if (vo.getStatus() == VOStatus.UPDATED) {

					}

				}

			}
		}

	}

	/**
	 * /��ѯ����ϵ��
	 * @param values
	 * @return
	 */
	private Map<String, AllocfacAggVO> queryAlloffac(Collection<String> values) {
		// TODO �Զ����ɵķ������
		Map<String,AllocfacAggVO> map = new HashMap<String,AllocfacAggVO>();
		BillQuery<AllocfacAggVO> billqueyr = new BillQuery<>(AllocfacAggVO.class);
		AllocfacAggVO[] bills = billqueyr.query(values.toArray(new String[0]));
		if(bills !=null && bills.length >0){
			for(AllocfacAggVO bill:bills){
				map.put(bill.getPrimaryKey(), bill);
			}
		}
		return map;
	}

	/**
	 * ���ݺ���Ҫ�أ���ѯ����ϵ�����ձ�
	 * 
	 * @param factorAsoaList
	 * @return
	 */

	private Factorofinv[] queryFenPeixi(String pk_group, String pk_org,
			Set<String> factorAsoaList) {
		// TODO �Զ����ɵķ������
		VOQuery<Factorofinv> queyr = new VOQuery<Factorofinv>(Factorofinv.class);
		SqlBuilder sql = new SqlBuilder();
		sql.append(" nvl(dr,0)=0   ");
		sql.append(" and pk_group", pk_group);
		sql.append(" and pk_org", pk_org);
		sql.append(" and pk_factor", factorAsoaList.toArray(new String[0]));
		return queyr.query(sql.toString(), null);
	}

}
