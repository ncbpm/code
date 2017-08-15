package nc.impl.pu.m25.action.rule.unapprove;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.pu.m21.IOrderPayPlanWriteBack;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.pu.m25.entity.InvoiceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.apache.commons.lang.ArrayUtils;

/**
 * 回写付款计划
 * 
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
				if (vo == null || vo.getChildrenVO() == null
						|| vo.getChildrenVO().length == 0)
					continue;

				VOQuery<ISuperVO> query = new VOQuery(PurchaseInVO.class);
				PurchaseInVO[] hvos = (PurchaseInVO[]) query
						.query(new String[] { vo.getChildrenVO()[0]
								.getCsourceid() });

				if (hvos == null || hvos.length == 0)
					continue;
				PurchaseInVO hvo = hvos[0];
			//  退库不处理
				if (hvo.getHead().getFreplenishflag() == null
						|| !hvo.getHead().getFreplenishflag().booleanValue()) {
					service.writeBackCancelSignFor25(vo);
				} else {
				}
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}

	}

}
