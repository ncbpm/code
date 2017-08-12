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
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 3. .�������ɶ�Ӧ��ʹ����֯��bom/����·��ƥ�����ȡ��������ɾ����Ӧ��ʹ����֯��ʹ�ù��� 4.
 * �������ɷ������֯��bom/����·��ƥ�����ȡ������ɾ��ȡ�������bom/����·��ƥ����� 5. ������֯+����+�汾�ſ���Ψһ��
 * 
 * @author liyf
 * 
 */

public class AutoToBmRtRuleForRainbowAudit implements IRule<AggBomVO> {

	@Override
	public void process(AggBomVO[] vos) {
		if (MMArrayUtil.isEmpty(vos)) {
			return;
		}
		VOQuery<BomMatchRtVO> query = new VOQuery<BomMatchRtVO>(
				BomMatchRtVO.class);
		CHGBomToBmrt  tool = new CHGBomToBmrt();

		for (AggBomVO aggBomVO : vos) {
			BomVO head = (BomVO) aggBomVO.getParentVO();
			BomUseOrgVO[] useOrgs = (BomUseOrgVO[]) aggBomVO
					.getChildren(BomUseOrgVO.class);
			if (useOrgs == null || useOrgs.length == 0) {
				return;
			}
			// ��ѯ
			String continon = " and cbomid='" + head.getCbomid()
					+ "'";
			if(1 != head.getFbomtype()){
				continon = " and cpackbomid='" + head.getCbomid()
						+ "'";
			}
			BomMatchRtVO[] bmrtvos = query.query(continon, null);
			// ���䵽��Ӧ��ʹ����֯��
			List<BomMatchRtVO> bmrtList = new ArrayList<BomMatchRtVO>();
			for (BomUseOrgVO useOrg : useOrgs) {
				if (tool.isExist2(bmrtvos, useOrg.getPk_useorg())) {
					continue;
				}
				bmrtList.add(tool.assmbleBmrt(head, useOrg));
			}
			try {
				NCLocator.getInstance().lookup(IBmrtMaintain.class)
						.insert(bmrtList.toArray(new BomMatchRtVO[0]));
			} catch (BusinessException e) {
				// TODO �Զ����ɵ� catch ��
				ExceptionUtils.wrappException(e);
			}
		}

	}

	



}
