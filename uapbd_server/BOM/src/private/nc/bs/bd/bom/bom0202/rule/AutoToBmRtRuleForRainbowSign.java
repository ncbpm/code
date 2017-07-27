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
 * 3.�������ɶ�Ӧ��ʹ����֯��bom/����·��ƥ�����ȡ��������ɾ����Ӧ��ʹ����֯��ʹ�ù��� 4. *
 * �������ɷ������֯��bom/����·��ƥ�����ȡ������ɾ��ȡ�������bom/����·��ƥ����� 5. ������֯+����+�汾�ſ���Ψһ��
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
			// ��ѯ
			String continon = " and cbomid='" + head.getCbomid() + "'";
			if (1 != head.getFbomtype()) {
				continon = " and cpackbomid='" + head.getCbomid() + "'";
			}
			BomMatchRtVO[] bmrtvos = query.query(continon, null);
			// ���䵽��Ӧ��ʹ����֯��
			List<BomMatchRtVO> bmrtList = new ArrayList<BomMatchRtVO>();
			List<BomMatchRtVO> delBmrtList = new ArrayList<BomMatchRtVO>();

			for (BomUseOrgVO useOrg : useOrgs) {
				BomMatchRtVO rtVO = isExist(bmrtvos, useOrg.getPk_useorg());
				// �����ȡ������
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
				// TODO �Զ����ɵ� catch ��
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
		// TODO �Զ����ɵķ������
		BomMatchRtVO vo = new BomMatchRtVO();
		vo.setAttributeValue(BomMatchRtVO.PK_GROUP,
				head.getAttributeValue(BomVO.PK_GROUP));
		vo.setAttributeValue(BomMatchRtVO.PK_ORG,
				useOrg.getAttributeValue(BomUseOrgVO.PK_USEORG));
		vo.setAttributeValue(BomMatchRtVO.PK_ORG_V,
				useOrg.getAttributeValue(BomUseOrgVO.PK_ORG_V));
		// fbomtype BOM���� 1=����BOM��2=��װBOM��3=����BOM��
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
