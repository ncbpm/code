package nc.bs.ic.fivemetals.maintain.rule;

import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.uap.rbac.constant.INCSystemUserConst;
import nc.vo.util.AuditInfoUtil;

public class SetAuditInfoRule implements IRule<AggFiveMetalsVO> {

	@Override
	public void process(AggFiveMetalsVO[] vos) {
		if (vos == null || vos.length == 0)
			return;

		setAuditInfo(vos);
	}

	private void setAuditInfo(AggFiveMetalsVO[] vos) {
		for (AggFiveMetalsVO vo : vos) {
			setHeadInfo(vo.getParentVO());
			if (vo.getChildrenVO() == null || vo.getChildrenVO().length == 0)
				continue;
			for (CircularlyAccessibleValueObject body : vo.getChildrenVO()) {
				setBodyInfo(body);
			}
		}
	}

	private void setHeadInfo(SuperVO vo) {
		if (vo == null) {
			return;
		}
		switch (vo.getStatus()) {
		case VOStatus.NEW:
			AuditInfoUtil.addData(vo);
			if (vo.getAttributeValue("creator") == null) {
				vo.setAttributeValue("creator", INCSystemUserConst.NC_USER_PK);
			}
			break;
		case VOStatus.UPDATED:
			AuditInfoUtil.updateData(vo);
			if (vo.getAttributeValue("modifier") == null) {
				vo.setAttributeValue("modifier", INCSystemUserConst.NC_USER_PK);
			}
			break;
		default:
			Logger.error("VO状态不正确");
		}
	}

	private void setBodyInfo(CircularlyAccessibleValueObject vo) {
		if (vo == null) {
			return;
		}
		switch (vo.getStatus()) {
		case VOStatus.NEW:
			vo.setAttributeValue("maketime", AuditInfoUtil.getCurrentTime());
			break;
		case VOStatus.UPDATED:
			vo.setAttributeValue("lastmaketime", AuditInfoUtil.getCurrentTime());
			break;
		default:
			Logger.error("VO状态不正确");
		}
	}

}
