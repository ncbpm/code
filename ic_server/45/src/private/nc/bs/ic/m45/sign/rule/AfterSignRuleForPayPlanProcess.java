package nc.bs.ic.m45.sign.rule;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.pu.m21.IOrderPayPlanWriteBack;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * ��д����ƻ�
 * 
 * @author
 * 
 */
public class AfterSignRuleForPayPlanProcess implements IRule<PurchaseInVO> {

	@Override
	public void process(PurchaseInVO[] vos) {

		if (vos == null || vos.length == 0) {
			return;
		}
		try {
			IOrderPayPlanWriteBack service = NCLocator.getInstance().lookup(
					IOrderPayPlanWriteBack.class);
			for (PurchaseInVO vo : vos) {

				if (vo.getHead() == null)
					continue;
				if (vo.getHead().getFreplenishflag() == null
						|| !vo.getHead().getFreplenishflag().booleanValue()) {
					service.writeBackOrderPayPlanFor45(vo);
				} else {
					service.writeBackOrderPayPlanForBack45(vo);
				}
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}

}
