package nc.bs.mmpac.huanbao.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.mmpac.huanbao.AggHuanbaoHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceHuanbaoSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggHuanbaoHVO[] sendApprove(AggHuanbaoHVO[] clientBills,
			AggHuanbaoHVO[] originBills) {
		for (AggHuanbaoHVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggHuanbaoHVO[] returnVos = new BillUpdate<AggHuanbaoHVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
