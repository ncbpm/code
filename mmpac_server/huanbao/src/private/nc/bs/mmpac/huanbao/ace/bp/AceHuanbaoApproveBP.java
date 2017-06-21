package nc.bs.mmpac.huanbao.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.mmpac.huanbao.AggHuanbaoHVO;

/**
 * 标准单据审核的BP
 */
public class AceHuanbaoApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggHuanbaoHVO[] approve(AggHuanbaoHVO[] clientBills,
			AggHuanbaoHVO[] originBills) {
		for (AggHuanbaoHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggHuanbaoHVO> update = new BillUpdate<AggHuanbaoHVO>();
		AggHuanbaoHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
