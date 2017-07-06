package nc.bs.ic.fivemetals.maintain.rule;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.ic.fivemetals.FivemetalsDao;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.ic.fivemetals.FiveMetalsBVO;
import nc.vo.ic.fivemetals.FiveMetalsHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class CheckNmny implements IRule<AggFiveMetalsVO> {

	@Override
	public void process(AggFiveMetalsVO[] vos) {

		if (vos == null || vos.length == 0)
			return;

		for (AggFiveMetalsVO vo : vos) {
			checknmy(vo);
			checkBodyNmny(vo);
		}
	}

	private void checknmy(AggFiveMetalsVO vo) {
		FiveMetalsHVO hvo = vo.getParentVO();

		try {
			if (hvo == null)
				throw new BusinessException("单据信息不完整");

			String sql = " select sum(nmny*itype) from  ic_fivemetals_b where nvl(dr,0) = 0 and   pk_fivemetals_h = '"
					+ hvo.getPk_fivemetals_h() + "'";
			BaseDAO dao = new BaseDAO();
			Object objCodeAndDefkey[] = (Object[]) dao.executeQuery(sql, null,
					new ArrayProcessor());
			if (objCodeAndDefkey != null && objCodeAndDefkey.length > 0) {

				Object nmny = objCodeAndDefkey[0];
				if (nmny == null)
					return;
				if (nmny instanceof BigDecimal) {
					BigDecimal i = (BigDecimal) objCodeAndDefkey[0];
					if (i.compareTo(BigDecimal.ZERO) < 0) {
						throw new BusinessException("卡号" + hvo.getVcardno()
								+ "的余额不能小于零！");
					}
				} else if (nmny instanceof Integer) {
					Integer i = (Integer) objCodeAndDefkey[0];
					if (i.intValue() < 0) {
						throw new BusinessException("卡号" + hvo.getVcardno()
								+ "的余额不能小于零！");
					}
				}
			}
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("数据库查询异常");

		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}

	private void checkBodyNmny(AggFiveMetalsVO vo) {
		FiveMetalsHVO hvo = vo.getParentVO();
		FiveMetalsBVO[] bvos = (FiveMetalsBVO[]) vo.getChildrenVO();

		try {
			if (hvo == null || bvos == null || bvos.length == 0)
				throw new BusinessException("单据信息不完整");
			Set<String> set = new HashSet<String>();
			for (FiveMetalsBVO bvo : bvos) {
				set.add(bvo.getCperiod());
			}

			FivemetalsDao dao = new FivemetalsDao();
			Map<String, UFDouble> retMap = dao.getFivemetalsBalance(bvos[0]
					.getPk_fivemetals_h());
			for (String str : set) {
				Object nmny = retMap.get(str);
				if (nmny == null)
					continue;
				if (nmny instanceof BigDecimal) {
					BigDecimal i = (BigDecimal) nmny;
					if (i.compareTo(BigDecimal.ZERO) < 0) {
						throw new BusinessException("卡号" + hvo.getVcardno()
								+ "的余额不能小于零！");
					}
				} else if (nmny instanceof Integer) {
					Integer i = (Integer) nmny;
					if (i.intValue() < 0) {
						throw new BusinessException("卡号" + hvo.getVcardno()
								+ "的余额不能小于零！");
					}
				}
				
			}
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("数据库查询异常");

		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}

	}

}
