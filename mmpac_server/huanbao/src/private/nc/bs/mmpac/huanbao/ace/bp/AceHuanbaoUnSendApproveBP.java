package nc.bs.mmpac.huanbao.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.mmpac.huanbao.AggHuanbaoHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceHuanbaoUnSendApproveBP {

	public AggHuanbaoHVO[] unSend(AggHuanbaoHVO[] clientBills,
			AggHuanbaoHVO[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggHuanbaoHVO> update = new BillUpdate<AggHuanbaoHVO>();
		AggHuanbaoHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggHuanbaoHVO[] clientBills) {
		for (AggHuanbaoHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
