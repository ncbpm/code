package nc.bs.ic.fivemetals.maintain.rule;

import nc.bs.dao.DAOException;
import nc.impl.ic.fivemetals.FivemetalsDao;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.ic.fivemetals.FiveMetalsBVO;
import nc.vo.ic.fivemetals.FiveMetalsHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class RowNODealRule implements IRule<AggFiveMetalsVO> {

	@Override
	public void process(AggFiveMetalsVO[] vos) {
		if (vos == null || vos.length == 0)
			return;

		for (AggFiveMetalsVO vo : vos) {
			setBillno(vo);
		}
	}

	private void setBillno(AggFiveMetalsVO vo) {
		FivemetalsDao dao = new FivemetalsDao();
		FiveMetalsHVO hvo = vo.getParentVO();
		String condition = " pk_fivemetals_h = '" + hvo.getPk_fivemetals_h()
				+ "'";
		String billno = null;
		try {
			billno = dao.getNewBillNo("ic_fivemetals_b", condition, "rowno");

		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("数据库查询异常");

		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}

		FiveMetalsBVO[] vos = (FiveMetalsBVO[]) vo.getChildrenVO();

		if (vos == null || vos.length == 0)
			return;

		int len = vos.length;

		if (len == 1) {
			vo.getChildrenVO()[0].setAttributeValue("rowno", billno);
			return;
		}

		for (int i = 0; i < len; i++) {
			vo.getChildrenVO()[i].setAttributeValue("rowno",
					Integer.parseInt(billno) + i);
		}
	}

}
