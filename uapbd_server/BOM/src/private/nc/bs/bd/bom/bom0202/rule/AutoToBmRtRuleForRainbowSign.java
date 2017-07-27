package nc.bs.bd.bom.bom0202.rule;

import java.util.ArrayList;
import java.util.List;

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
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 3.审批生成对应的使用组织的bom/工艺路线匹配规则，取消审批，删除对应的使用组织的使用规则 4. *
 * 分配生成分配的组织的bom/工艺路线匹配规则，取消分配删除取消分配的bom/工艺路线匹配规则 5. 按照组织+物料+版本号控制唯一性
 * 
 * @author liyf
 * 
 */

public class AutoToBmRtRuleForRainbowSign implements IRule<AggBomVO> {

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
			// 分配到对应的使用组织：
			List<BomMatchRtVO> bmrtList = new ArrayList<BomMatchRtVO>();
			List<BomMatchRtVO> delBmrtList = new ArrayList<BomMatchRtVO>();

			for (BomUseOrgVO useOrg : useOrgs) {
				BomMatchRtVO rtVO = isExist(bmrtvos, useOrg.getPk_useorg());
				// 如果是取消分配
				if (useOrg.getStatus() == VOStatus.DELETED) {
					if (rtVO != null) {
						delBmrtList.add(rtVO);
					}

				} else {
					if (isExist(bmrtvos, useOrg.getPk_useorg()) != null) {
						continue;
					}
					bmrtList.add(assmbleBmrt(head, useOrg));

				}

			}
			try {
				if (bmrtList.size() > 0) {
					NCLocator.getInstance().lookup(IBmrtMaintain.class)
							.insert(bmrtList.toArray(new BomMatchRtVO[0]));
				}
				if (delBmrtList.size() > 0) {
					NCLocator.getInstance().lookup(IBmrtMaintain.class)
							.batchDelete(delBmrtList.toArray(new BomMatchRtVO[0]));
				}

			} catch (BusinessException e) {
				// TODO 自动生成的 catch 块
				ExceptionUtils.wrappException(e);
			}
		}

	}

	/**
	 * 
	 * @param head
	 * @param useOrg
	 * @return
	 */
	private BomMatchRtVO assmbleBmrt(BomVO head, BomUseOrgVO useOrg) {
		// TODO 自动生成的方法存根
		BomMatchRtVO vo = new BomMatchRtVO();
		vo.setAttributeValue(BomMatchRtVO.PK_GROUP,
				head.getAttributeValue(BomVO.PK_GROUP));
		vo.setAttributeValue(BomMatchRtVO.PK_ORG,
				useOrg.getAttributeValue(BomUseOrgVO.PK_USEORG));
		vo.setAttributeValue(BomMatchRtVO.PK_ORG_V,
				useOrg.getAttributeValue(BomUseOrgVO.PK_ORG_V));
		// fbomtype BOM类型 1=生产BOM，2=包装BOM，3=配置BOM，
		if (1 == head.getFbomtype()) {
			vo.setAttributeValue(BomMatchRtVO.CBOMID,
					head.getAttributeValue(BomVO.CBOMID));
		} else {
			vo.setAttributeValue(BomMatchRtVO.CPACKBOMID,
					head.getAttributeValue(BomVO.CBOMID));
		}
		vo.setAttributeValue(BomMatchRtVO.CMATERIALID,
				head.getAttributeValue(BomVO.HCMATERIALID));
		vo.setAttributeValue(BomMatchRtVO.CMATERIALVID,
				head.getAttributeValue(BomVO.HCMATERIALVID));
		vo.setAttributeValue(BomMatchRtVO.CMFGTYPE, 0);
		vo.setAttributeValue(BomMatchRtVO.ENTRUST, UFBoolean.FALSE);
		vo.setAttributeValue(BomMatchRtVO.PRODUCTION, UFBoolean.TRUE);
		vo.setDeffectdate(new UFDate());
		vo.setDloseeffectdate(new UFDate("2999-12-31 23:59:59"));

		for (int i = 1; i < 10; i++) {
			String key = "vfree" + i;
			vo.setAttributeValue(key, vo.getAttributeValue(key));
		}
		vo.setAttributeValue("vfree3", head.getHversion());
		return vo;
	}

	private BomMatchRtVO isExist(BomMatchRtVO[] bmrtvos, String pk_org) {
		if (bmrtvos == null || bmrtvos.length == 0) {
			return null;
		}
		for (BomMatchRtVO bmrtvo : bmrtvos) {
			if (pk_org.equalsIgnoreCase(bmrtvo.getPk_org())) {
				return bmrtvo;
			}
		}
		return null;
	}

}
