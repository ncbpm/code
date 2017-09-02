package nc.impl.pu.m25.action.rule.unapprove;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.pu.m21.IOrderPayPlanWriteBack;
import nc.vo.pu.m25.entity.InvoiceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.apache.commons.lang.ArrayUtils;

/**
 * 回写付款计划
 * @author 
 *
 */
public class AfterUnApproveRuleForPayPlanProcess implements IRule<InvoiceVO> {

	@Override
	public void process(InvoiceVO[] vos) {
		if (ArrayUtils.isEmpty(vos)) {
			return;
		}
		try {
			IOrderPayPlanWriteBack service = NCLocator.getInstance().lookup(
					IOrderPayPlanWriteBack.class);
			for (InvoiceVO vo : vos) {
				service.writeBackCancelSignFor25(vo);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}

	}

}
