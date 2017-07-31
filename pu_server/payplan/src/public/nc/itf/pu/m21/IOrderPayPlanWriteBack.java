package nc.itf.pu.m21;

import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.pu.m25.entity.InvoiceVO;
import nc.vo.pub.BusinessException;

public interface IOrderPayPlanWriteBack {
	void writeBackOrderPayPlanFor45(PurchaseInVO invo) throws BusinessException;

	void writeBackOrderPayPlanFor25(InvoiceVO invo) throws BusinessException;

}
