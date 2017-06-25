package nc.bs.ic.fivemetals.maintain.rule;

import nc.bs.dao.DAOException;
import nc.impl.ic.fivemetals.FivemetalsDao;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.ic.fivemetals.FiveMetalsHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class CardNoUniqueRule implements IRule<AggFiveMetalsVO> {

	@Override
	public void process(AggFiveMetalsVO[] vos) {

		if (vos == null || vos.length == 0)
			return;

		for (AggFiveMetalsVO vo : vos) {
			uniqueCardNO(vo);
		}
	}

	private void uniqueCardNO(AggFiveMetalsVO vo) {
		FivemetalsDao dao = new FivemetalsDao();

		FiveMetalsHVO hvo = vo.getParentVO();
		String condition = " pk_group = '" + hvo.getPk_group()
				+ "' and pk_org ='" + hvo.getPk_org() + "' and vcardno = '"
				+ hvo.getVcardno() + "'";
		boolean isexists = false;
		try {
			isexists = dao.isExists(hvo.getTableName(), condition);

		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("数据库查询异常");
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		if (isexists) {
			ExceptionUtils.wrappBusinessException(" 该卡号已经存在，请确认！");
		}
	}

}
