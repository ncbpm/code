package nc.bs.ic.fivemetals.maintain.rule;

import java.math.BigDecimal;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.ic.fivemetals.FiveMetalsHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class CheckNmny implements IRule<AggFiveMetalsVO> {

	@Override
	public void process(AggFiveMetalsVO[] vos) {

		if (vos == null || vos.length == 0)
			return;

		for (AggFiveMetalsVO vo : vos) {
			checknmy(vo);
		}
	}

	private void checknmy(AggFiveMetalsVO vo) {
		FiveMetalsHVO hvo = vo.getParentVO();
		try {
			String sql = " select sum(nmny) from  ic_fivemetals_b where nvl(dr,0) = 0 and   pk_fivemetals_h = '"
					+ hvo.getPk_fivemetals_h() + "'";
			BaseDAO dao = new BaseDAO();
			Object objCodeAndDefkey[] = (Object[]) dao.executeQuery(sql, null,
					new ArrayProcessor());
			if (objCodeAndDefkey != null && objCodeAndDefkey.length > 0) {
				BigDecimal i = (BigDecimal) objCodeAndDefkey[0];
				if (i.compareTo(BigDecimal.ZERO) < 0) {
					throw new BusinessException("卡号" + hvo.getVcardno()
							+ "的余额不能小于零！");
				}
			}
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("数据库查询异常");

		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}

}
