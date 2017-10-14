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
 * 根据核算要素对应的分配系数，自动分配到分配系数物料
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
		// 根据核算要素查询对应的分配系数
		if (bills == null || bills.length == 0) {
			return;
		}
		for (CostBomAggVO aggvo : bills) {
			try {
				processBill(aggvo);
			} catch (BusinessException e) {
				ExceptionUtils.wrappBusinessException("成本定额同步异常:"
						+ e.getMessage());
			}

		}

	}

	private void processBill(CostBomAggVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		Set<String> factorAsoaList = new HashSet<String>();
		CostBomHeadVO parentVO = (CostBomHeadVO) aggvo.getParentVO();
		boolean checkAssInfo = checkAssInfo(parentVO.getPk_org(),
				parentVO.getCmaterialid());
		if (!checkAssInfo)
			return;
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
		// 根据核算要素查询 核心要素与分配系数对照表，是否有对应
		Factorofinv[] factor_fenpeixishu = queryFenPeixi(
				parentVO.getPk_group(), parentVO.getPk_org(), null);
		if (factor_fenpeixishu == null || factor_fenpeixishu.length == 0) {
			return;
		}
		Map<String, String> fator_map = new HashMap<String, String>();
		for (Factorofinv vo : factor_fenpeixishu) {
			fator_map.put(vo.getPk_factor(), vo.getPk_allocfac());
		}
		// 根据对照表维护的分配系数主键 查询分配系数VO
		// Map<String, AllocfacAggVO> allocfac =
		// queryAlloffac(fator_map.values());

		// 判断成本BOM的表体核算要素是否 有对应的在对照表。
		// 如果是新增行，则判断如果在对照表中则讲成本BOM表头的VO对应的产品增加分配系数的表体产品
		// 如果是删除行，则取消分配
		// 如果是修改行，判断核算要素如果有变化，则删除后增加.
		for (CircularlyAccessibleValueObject vo : childrenVos) {
			// 得到子项的核算要素信息
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
				// 查询修改之前的成本BOM核算要素表体
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
	 *            核算要素A对应的成本BOM
	 * @param allocfacAggVO
	 *            核算要素A对应的 分配系数
	 * @throws BusinessException
	 */
	private void delXiShu(CostBomHeadVO head, String pk_alloffac)
			throws BusinessException {
		// 重新查询发，防止并发问题
		AllocfacAggVO allocfacAggVO = queryAlloffac(pk_alloffac);
		AllocfacItemVO[] itemVOS = allocfacAggVO.getItemVO();
		for (AllocfacItemVO body : itemVOS) {
			// 如果已经存在，则跳过
			if (body.getCmaterialid() != null && isEqualses(body, head)) {
				body.setStatus(VOStatus.DELETED);
			}
		}
		NCLocator.getInstance().lookup(IAllocfacMaintainService.class)
				.update(new AllocfacAggVO[] { allocfacAggVO });

	}

	private void setNfactor(AllocfacItemVO body,
			CircularlyAccessibleValueObject bvo) {
		// / 1. 分配系数，费用的取金额，材料取数量
		if (bvo instanceof CostBomStuffItemVO) {
			body.setNfactor(((CostBomStuffItemVO) bvo).getNnum());

		} else {
			body.setNfactor((UFDouble) bvo.getAttributeValue("nmoney"));

		}
	}

	/***
	 * 将陈本BOM对应的表头物料增加成分配系数的表体 如果物料已经存在，则跳过
	 * 
	 * 
	 * @param head
	 *            核算要素A对应的成本BOM
	 * @param bvo
	 * @param allocfacAggVO
	 *            核算要素A对应的 分配系数
	 * @throws BusinessException
	 */
	private void addXiShu(CostBomHeadVO head, String pk_alloffac,
			CircularlyAccessibleValueObject bvo) throws BusinessException {
		// TODO 自动生成的方法存根
		AllocfacAggVO allocfacAggVO = queryAlloffac(pk_alloffac);
		AllocfacItemVO[] itemVOS = allocfacAggVO.getItemVO();
		boolean isEXist = false;
		for (AllocfacItemVO body : itemVOS) {
			// 如果已经存在，则跳过
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
			// 物料新
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
			// 1. 分配系数，费用的取金额，材料取数量
			setNfactor(item, bvo);
			asList.add(item);
			allocfacAggVO.setChildrenVO(asList.toArray(new AllocfacItemVO[0]));

		}

		NCLocator.getInstance().lookup(IAllocfacMaintainService.class)
				.update(new AllocfacAggVO[] { allocfacAggVO });

	}

	/**
	 * 判断表头物料是否成本对象
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
		// 查询物料生产信息页签
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
	 * /查询分配系数
	 * 
	 * @param values
	 * @return
	 */
	private Map<String, AllocfacAggVO> queryAlloffac(Collection<String> values) {
		// TODO 自动生成的方法存根
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
	 * /查询分配系数
	 * 
	 * @param values
	 * @return
	 */
	private AllocfacAggVO queryAlloffac(String pk) {
		// TODO 自动生成的方法存根
		BillQuery<AllocfacAggVO> billqueyr = new BillQuery<>(
				AllocfacAggVO.class);
		AllocfacAggVO[] bills = billqueyr.query(new String[] { pk });

		return bills[0];
	}

	/**
	 * 根据核算要素，查询分配系数对照表
	 * 
	 * @param factorAsoaList
	 * @return
	 */

	private Factorofinv[] queryFenPeixi(String pk_group, String pk_org,
			Set<String> factorAsoaList) {
		// TODO 自动生成的方法存根
		VOQuery<Factorofinv> queyr = new VOQuery<Factorofinv>(Factorofinv.class);
		SqlBuilder sql = new SqlBuilder();
		sql.append(" and nvl(dr,0)=0   ");
		sql.append(" and pk_group", pk_group);
		sql.append(" and pk_org", pk_org);
		// sql.append(" and pk_factor", factorAsoaList.toArray(new String[0]));
		return queyr.query(sql.toString(), null);
	}

}
