package nc.bs.bd.bom.bom0202.rule;

import nc.vo.bd.bom.bom0202.entity.BomUseOrgVO;
import nc.vo.bd.bom.bom0202.entity.BomVO;
import nc.vo.bd.vermatch.entity.BomMatchRtVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

public class CHGBomToBmrt {

	
	/**
	 * 
	 * @param head
	 * @param useOrg
	 * @return
	 */
	public BomMatchRtVO assmbleBmrt(BomVO head, BomUseOrgVO useOrg) {
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
		
		vo.setDeffectdate(head.getTmaketime().asBegin());
		vo.setDloseeffectdate(new UFDate("2999-12-31 23:59:59"));

		for (int i = 1; i < 10; i++) {
			String key = "vfree" + i;
			vo.setAttributeValue(key, head.getAttributeValue(key));
		}
		return vo;
	}

	public  BomMatchRtVO isExist(BomMatchRtVO[] bmrtvos, String pk_org) {
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
	public boolean isExist2(BomMatchRtVO[] bmrtvos, String pk_org) {
		if (bmrtvos == null || bmrtvos.length == 0) {
			return false;
		}
		for (BomMatchRtVO bmrtvo : bmrtvos) {
			if (pk_org.equalsIgnoreCase(bmrtvo.getPk_org())) {
				return true;
			}
		}
		return false;
	}
}
