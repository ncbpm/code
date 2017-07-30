package nc.bs.bd.bom.bom0202.rule;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.bd.vermatch.IBmrtMaintain;
import nc.util.mmf.framework.base.MMArrayUtil;
import nc.vo.bd.bom.bom0202.entity.AggBomVO;
import nc.vo.bd.bom.bom0202.entity.BomUseOrgVO;
import nc.vo.bd.bom.bom0202.entity.BomVO;
import nc.vo.bd.vermatch.entity.BomMatchRtVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 3. .审批生成对应的使用组织的bom/工艺路线匹配规则，取消审批，删除对应的使用组织的使用规则 4.
 * 分配生成分配的组织的bom/工艺路线匹配规则，取消分配删除取消分配的bom/工艺路线匹配规则 5. 按照组织+物料+版本号控制唯一性
 * 
 * @author liyf
 * 
 */

public class AutoToBmRtRuleForRainbowUnAudit implements IRule<AggBomVO> {

	@Override
	public void process(AggBomVO[] vos) {
		if (MMArrayUtil.isEmpty(vos)) {
			return;
		}
		VOQuery<BomMatchRtVO> query = new VOQuery<BomMatchRtVO>(
				BomMatchRtVO.class);
		for (AggBomVO aggBomVO : vos) {
			BomVO head = (BomVO) aggBomVO.getParentVO();
			BomUseOrgVO[] useOrgs = (BomUseOrgVO[]) aggBomVO
					.getChildren(BomUseOrgVO.class);
			if (useOrgs == null || useOrgs.length == 0) {
				return;
			}
			// 查询
			String continon = " and cbomid='" + head.getCbomid() + "'";
			if (1 != head.getFbomtype()) {
				continon = " and cpackbomid='" + head.getCbomid() + "'";
			}
			BomMatchRtVO[] bmrtvos = query.query(continon, null);

			try {
				NCLocator.getInstance().lookup(IBmrtMaintain.class)
						.batchDelete(bmrtvos);
			} catch (BusinessException e) {
				// TODO 自动生成的 catch 块
				ExceptionUtils.wrappException(e);
			}
		}

	}

}
