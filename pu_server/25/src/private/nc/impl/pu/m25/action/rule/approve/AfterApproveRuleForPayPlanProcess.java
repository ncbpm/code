package nc.impl.pu.m25.action.rule.approve;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.pu.m21.IOrderPayPlanWriteBack;
import nc.vo.ic.m45.entity.PurchaseInHeadVO;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pu.m25.entity.InvoiceItemVO;
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
public class AfterApproveRuleForPayPlanProcess implements IRule<InvoiceVO> {

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
				InvoiceItemVO item = vo.getChildrenVO()[0];
				// 来源采购入库的采购发票回写付款计划
				if (!StringUtil.isSEmptyOrNull(item.getCsourcetypecode())
						&& "45".equalsIgnoreCase(item.getCsourcetypecode())) {
					VOQuery<ISuperVO> query = new VOQuery(
							PurchaseInHeadVO.class);
					PurchaseInHeadVO[] hvos = (PurchaseInHeadVO[]) query
							.query(new String[] { vo.getChildrenVO()[0]
									.getCsourceid() });

					if (hvos == null || hvos.length == 0)
						continue;
					PurchaseInHeadVO hvo = hvos[0];
					// 退库不处理
					if (hvo.getFreplenishflag() == null
							|| !hvo.getFreplenishflag().booleanValue()) {
						service.writeBackOrderPayPlanFor25(vo);
					} else {
					}
				}
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	}
}
