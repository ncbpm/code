package nc.bs.ic.fivemetals.action;

import nc.bs.ic.fivemetals.maintain.FivemetalsSaveBP;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;

public class FivemetalsSaveAction {

	public AggFiveMetalsVO[] save(AggFiveMetalsVO[] orderVos) {
		BillTransferTool<AggFiveMetalsVO> tool = new BillTransferTool<AggFiveMetalsVO>(
				orderVos);
		AggFiveMetalsVO[] originVos = tool.getOriginBills();
		return new FivemetalsSaveBP().save(orderVos, originVos);
	}
}
