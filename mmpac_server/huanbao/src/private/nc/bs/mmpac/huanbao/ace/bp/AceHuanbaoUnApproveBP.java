package nc.bs.mmpac.huanbao.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.mmpac.huanbao.AggHuanbaoHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceHuanbaoUnApproveBP {

	public AggHuanbaoHVO[] unApprove(AggHuanbaoHVO[] clientBills,
			AggHuanbaoHVO[] originBills) {
		for (AggHuanbaoHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggHuanbaoHVO> update = new BillUpdate<AggHuanbaoHVO>();
		AggHuanbaoHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
