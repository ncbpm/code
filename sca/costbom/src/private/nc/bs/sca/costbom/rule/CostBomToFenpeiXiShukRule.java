package nc.bs.sca.costbom.rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bd.framework.base.CMValueCheck;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.mapub.allocfac.IAllocfacMaintainService;
import nc.pubitf.uapbd.IMaterialPubService_C;
import nc.vo.bd.material.prod.MaterialProdVO;
import nc.vo.cmpub.framework.assistant.CMAssInfoItemVO;
import nc.vo.mapub.allocfac.entity.AllocfacAggVO;
import nc.vo.mapub.allocfac.entity.AllocfacItemVO;
import nc.vo.mapub.allocfac.util.AllocfacItemUtil;
import nc.vo.mapub.allocfacotor.Factorofinv;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
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
		if (bills == null || bills.length == 0) {
			return;
		}
		for (CostBomAggVO aggvo : bills) {
			try {
				processBill(aggvo);
			} catch (BusinessException e) {
				ExceptionUtils.wrappBusinessException("�ɱ�����ͬ���쳣:"
						+ e.getMessage());
			}

		}

	}

	private void processBill(CostBomAggVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		Set<String> factorAsoaList = new HashSet<String>();
		CostBomHeadVO parentVO = (CostBomHeadVO) aggvo.getParentVO();
		boolean checkAssInfo = checkAssInfo(parentVO.getPk_org(),
				parentVO.getCmaterialid());
		if (!checkAssInfo)
			return;
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
		// ���ݺ���Ҫ�ز�ѯ ����Ҫ�������ϵ�����ձ��Ƿ��ж�Ӧ
		Factorofinv[] factor_fenpeixishu = queryFenPeixi(
				parentVO.getPk_group(), parentVO.getPk_org(), null);
		if (factor_fenpeixishu == null || factor_fenpeixishu.length == 0) {
			return;
		}
		Map<String, String> fator_map = new HashMap<String, String>();
		for (Factorofinv vo : factor_fenpeixishu) {
			fator_map.put(vo.getPk_factor(), vo.getPk_allocfac());
		}
		// ���ݶ��ձ�ά���ķ���ϵ������ ��ѯ����ϵ��VO
		// Map<String, AllocfacAggVO> allocfac =
		// queryAlloffac(fator_map.values());

		// �жϳɱ�BOM�ı������Ҫ���Ƿ� �ж�Ӧ���ڶ��ձ�
		// ����������У����ж�����ڶ��ձ����򽲳ɱ�BOM��ͷ��VO��Ӧ�Ĳ�Ʒ���ӷ���ϵ���ı����Ʒ
		// �����ɾ���У���ȡ������
		// ������޸��У��жϺ���Ҫ������б仯����ɾ��������.
		for (CircularlyAccessibleValueObject vo : childrenVos) {
			// �õ�����ĺ���Ҫ����Ϣ
			String pk_factorasoa = (String) vo
					.getAttributeValue(CostBomStuffItemVO.CELEMENTID);
			if (vo.getStatus() == VOStatus.NEW) {
				if (fator_map.containsKey(pk_factorasoa)) {
					addXiShu((CostBomHeadVO) aggvo.getParentVO(),
							fator_map.get(pk_factorasoa), vo);
				}
			}
			if (vo.getStatus() == VOStatus.DELETED) {
				if (fator_map.containsKey(pk_factorasoa)) {
					delXiShu((CostBomHeadVO) aggvo.getParentVO(),
							fator_map.get(pk_factorasoa));

				}
			}
			if (vo.getStatus() == VOStatus.UPDATED) {
				// ��ѯ�޸�֮ǰ�ĳɱ�BOM����Ҫ�ر���
				BaseDAO dao = new BaseDAO();
				CircularlyAccessibleValueObject oldvo = (CircularlyAccessibleValueObject) dao
						.retrieveByPK(vo.getClass(), vo.getPrimaryKey());
				String old_pk_factorasoa = (String) oldvo
						.getAttributeValue(CostBomStuffItemVO.CELEMENTID);

				if (old_pk_factorasoa != null
						&& !old_pk_factorasoa.equals(pk_factorasoa)) {
					if (fator_map.containsKey(old_pk_factorasoa)) {
						delXiShu((CostBomHeadVO) aggvo.getParentVO(),
								fator_map.get(old_pk_factorasoa));

					}

				}

				if (fator_map.containsKey(pk_factorasoa)) {
					addXiShu((CostBomHeadVO) aggvo.getParentVO(),
							fator_map.get(pk_factorasoa), vo);

				}
			}

			if (vo.getStatus() == VOStatus.UNCHANGED) {
				if (fator_map.containsKey(pk_factorasoa)) {
					addXiShu((CostBomHeadVO) aggvo.getParentVO(),
							fator_map.get(pk_factorasoa), vo);
				}
			}

		}

	}

	private boolean isEqualses(AllocfacItemVO body, CostBomHeadVO head) {

		String[] cmaterialinfor = new String[] { CMAssInfoItemVO.CPROJECTID,
				CMAssInfoItemVO.CVENDORID, CMAssInfoItemVO.CPRODUCTORID,
				CMAssInfoItemVO.CCUSTOMERID };

		String key1 = body.getCmaterialid();
		String key2 = head.getCmaterialid();
		for (String free : cmaterialinfor) {
			key1 = key1 + body.getAttributeValue(free);
			key2 = key2 + head.getAttributeValue(free);
		}
		for (int i = 1; i <= 10; i++) {
			key1 = key1 + body.getAttributeValue("vbfree" + i);
			key2 = key2 + head.getAttributeValue("vfree" + i);
		}
		return key1.equalsIgnoreCase(key2);
	}

	/**
	 * 
	 * @param head
	 *            ����Ҫ��A��Ӧ�ĳɱ�BOM
	 * @param allocfacAggVO
	 *            ����Ҫ��A��Ӧ�� ����ϵ��
	 * @throws BusinessException
	 */
	private void delXiShu(CostBomHeadVO head, String pk_alloffac)
			throws BusinessException {
		// ���²�ѯ������ֹ��������
		AllocfacAggVO allocfacAggVO = queryAlloffac(pk_alloffac);
		AllocfacItemVO[] itemVOS = allocfacAggVO.getItemVO();
		for (AllocfacItemVO body : itemVOS) {
			// ����Ѿ����ڣ�������
			if (body.getCmaterialid() != null && isEqualses(body, head)) {
				body.setStatus(VOStatus.DELETED);
			}
		}
		NCLocator.getInstance().lookup(IAllocfacMaintainService.class)
				.update(new AllocfacAggVO[] { allocfacAggVO });

	}

	private void setNfactor(AllocfacItemVO body,
			CircularlyAccessibleValueObject bvo) {
		// / 1. ����ϵ�������õ�ȡ������ȡ����
		if (bvo instanceof CostBomStuffItemVO) {
			body.setNfactor(((CostBomStuffItemVO) bvo).getNnum());

		} else {
			body.setNfactor((UFDouble) bvo.getAttributeValue("nmoney"));

		}
	}

	/***
	 * ���±�BOM��Ӧ�ı�ͷ�������ӳɷ���ϵ���ı��� ��������Ѿ����ڣ�������
	 * 
	 * 
	 * @param head
	 *            ����Ҫ��A��Ӧ�ĳɱ�BOM
	 * @param bvo
	 * @param allocfacAggVO
	 *            ����Ҫ��A��Ӧ�� ����ϵ��
	 * @throws BusinessException
	 */
	private void addXiShu(CostBomHeadVO head, String pk_alloffac,
			CircularlyAccessibleValueObject bvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		AllocfacAggVO allocfacAggVO = queryAlloffac(pk_alloffac);
		AllocfacItemVO[] itemVOS = allocfacAggVO.getItemVO();
		boolean isEXist = false;
		for (AllocfacItemVO body : itemVOS) {
			// ����Ѿ����ڣ�������
			if (body.getCmaterialid() != null && isEqualses(body, head)) {
				body.setStatus(VOStatus.UPDATED);
				setNfactor(body, bvo);
				isEXist = true;
				break;

			}
		}
		if (!isEXist) {
			List<AllocfacItemVO> asList = new ArrayList<AllocfacItemVO>();
			for (AllocfacItemVO item : itemVOS) {
				asList.add(item);
			}
			AllocfacItemVO item = new AllocfacItemVO();
			item.setPk_group(allocfacAggVO.getParentVO().getPk_group());
			item.setPk_org(allocfacAggVO.getParentVO().getPk_org());
			item.setPk_org_v(allocfacAggVO.getParentVO().getPk_org_v());

			item.setCallocfacid(allocfacAggVO.getParentVO().getCallocfacid());
			// ������
			item.setCmaterialid(head.getCmaterialid());
			String[] cmaterialinfor = new String[] {
					CMAssInfoItemVO.CPROJECTID, CMAssInfoItemVO.CVENDORID,
					CMAssInfoItemVO.CPRODUCTORID, CMAssInfoItemVO.CCUSTOMERID };
			for (String name : cmaterialinfor) {
				item.setAttributeValue(name, head.getAttributeValue(name));
			}

			for (int i = 1; i <= 10; i++) {
				item.setAttributeValue("vbfree" + i,
						head.getAttributeValue("vfree" + i));
			}
			item.setStatus(VOStatus.NEW);
			// 1. ����ϵ�������õ�ȡ������ȡ����
			setNfactor(item, bvo);
			asList.add(item);
			allocfacAggVO.setChildrenVO(asList.toArray(new AllocfacItemVO[0]));

		}

		NCLocator.getInstance().lookup(IAllocfacMaintainService.class)
				.update(new AllocfacAggVO[] { allocfacAggVO });

	}

	/**
	 * �жϱ�ͷ�����Ƿ�ɱ�����
	 * 
	 * @param pk_org
	 * @param itemvos
	 * @param nameMap
	 */
	private boolean checkAssInfo(String pk_org, String matId) {

		Set<String> matIdSet = new HashSet<String>();
		matIdSet.add(matId);
		Map<String, MaterialProdVO> prodVOMap = this.getProdVOMap(pk_org,
				matIdSet.toArray(new String[0]));
		MaterialProdVO prodVO = prodVOMap.get(matId);
		if (null != prodVO && !prodVO.getSfcbdx().booleanValue()) {
			return false;
		}

		return true;

	}

	private Map<String, MaterialProdVO> getProdVOMap(String pk_org,
			String[] mats) {
		// ��ѯ����������Ϣҳǩ
		Map<String, MaterialProdVO> prodVOMap = new HashMap<String, MaterialProdVO>();
		try {
			prodVOMap = NCLocator
					.getInstance()
					.lookup(IMaterialPubService_C.class)
					.queryMaterialProduceInfoByPks(mats, pk_org,
							AllocfacItemUtil.MATERIALPRODFIELDS);
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		return prodVOMap;
	}

	/**
	 * /��ѯ����ϵ��
	 * 
	 * @param values
	 * @return
	 */
	private Map<String, AllocfacAggVO> queryAlloffac(Collection<String> values) {
		// TODO �Զ����ɵķ������
		Map<String, AllocfacAggVO> map = new HashMap<String, AllocfacAggVO>();
		BillQuery<AllocfacAggVO> billqueyr = new BillQuery<>(
				AllocfacAggVO.class);
		AllocfacAggVO[] bills = billqueyr.query(values.toArray(new String[0]));
		if (bills != null && bills.length > 0) {
			for (AllocfacAggVO bill : bills) {
				map.put(bill.getPrimaryKey(), bill);
			}
		}
		return map;
	}

	/**
	 * /��ѯ����ϵ��
	 * 
	 * @param values
	 * @return
	 */
	private AllocfacAggVO queryAlloffac(String pk) {
		// TODO �Զ����ɵķ������
		BillQuery<AllocfacAggVO> billqueyr = new BillQuery<>(
				AllocfacAggVO.class);
		AllocfacAggVO[] bills = billqueyr.query(new String[] { pk });

		return bills[0];
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
		sql.append(" and nvl(dr,0)=0   ");
		sql.append(" and pk_group", pk_group);
		sql.append(" and pk_org", pk_org);
		// sql.append(" and pk_factor", factorAsoaList.toArray(new String[0]));
		return queyr.query(sql.toString(), null);
	}

}
