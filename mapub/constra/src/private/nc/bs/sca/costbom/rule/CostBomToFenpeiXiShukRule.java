package nc.bs.sca.costbom.rule;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bd.framework.base.CMValueCheck;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.mapub.allocfacotor.Factorofinv;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.sca.costbom.entity.CostBomAggVO;
import nc.vo.sca.costbom.entity.CostBomHeadVO;
import nc.vo.sca.costbom.entity.CostBomStuffItemVO;

/**
 * 根据核算要素对应的分配系数，自动分配到分配系数物料
 * 
 * @author liyf
 *
 */
public class CostBomToFenpeiXiShukRule implements IRule<CostBomAggVO> {

	


	@Override
	public void process(CostBomAggVO[] bills) {
		//根据核算要素查询对应的分配系数
		if (bills != null && bills.length > 0) {
			for (CostBomAggVO aggvo : bills) {
				Set<String> factorAsoaList = new HashSet<String>();
				CostBomHeadVO parentVO = (CostBomHeadVO) aggvo.getParentVO();
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
				Factorofinv[] factor_fenpeixishu = queryFenPeixi(parentVO.getPk_group(), parentVO.getPk_org(), factorAsoaList);
				if(factor_fenpeixishu == null || factor_fenpeixishu.length ==0){
					continue;
				}
				
				//
			
				
				
			}
		}
			
		
	}
	
	/**
	 * 根据核算要素，查询分配系数对照表
	 * @param factorAsoaList
	 * @return
	 */

	private Factorofinv[] queryFenPeixi(String pk_group, String pk_org,Set<String> factorAsoaList) {
		// TODO 自动生成的方法存根
		VOQuery<Factorofinv> queyr = new VOQuery<Factorofinv>(Factorofinv.class);
		SqlBuilder sql = new SqlBuilder();
		sql.append(" nvl(dr,0)=0   ");
		sql.append(" and pk_group",pk_group);
		sql.append(" and pk_org",pk_org);
		sql.append(" and pk_factor",factorAsoaList.toArray(new String[0]));
		return  queyr.query(sql.toString(), null);
	}


}
