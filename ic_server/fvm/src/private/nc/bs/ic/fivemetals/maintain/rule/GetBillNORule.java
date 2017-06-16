package nc.bs.ic.fivemetals.maintain.rule;

import nc.bs.dao.DAOException;
import nc.impl.ic.fivemetals.FivemetalsDao;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.ic.fivemetals.FiveMetalsHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class GetBillNORule implements IRule<AggFiveMetalsVO> {

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
		String condition = " pk_group = '" + hvo.getPk_group()
				+ "' and pk_org ='" + hvo.getPk_org() + "'  and cperiod = '"
				+ hvo.getCperiod() + "'";
		String billno = null;
		try {
			billno = dao.getNewBillNo(hvo.getTableName(), condition, "code");
			hvo.setCode(billno);
			hvo.setName(billno);
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("数据库查询异常");

		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}

}
