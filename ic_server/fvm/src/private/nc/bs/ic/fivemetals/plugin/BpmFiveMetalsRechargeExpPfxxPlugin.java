package nc.bs.ic.fivemetals.plugin;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.itf.ic.fivemetals.IFivemetalsMaintain;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;

//  ��ֵ
public class BpmFiveMetalsRechargeExpPfxxPlugin<T extends AggFiveMetalsVO>
		extends nc.bs.pfxx.plugin.AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {

		 AggFiveMetalsVO bill = (AggFiveMetalsVO) vo;
		
		 if (bill == null || bill.getParentVO() == null)
		 throw new BusinessException("�������ݳ���");
		
		 IFivemetalsMaintain manageService = NCLocator.getInstance().lookup(
		 IFivemetalsMaintain.class);
		 AggFiveMetalsVO returnvo = manageService.operatebill(bill);
		 return returnvo.getPrimaryKey();

//		PurchaseInVO[] icbills = new BillQuery<PurchaseInVO>(PurchaseInVO.class)
//				.query(new String[] { "1001ZZ1000000001IFWX" });
//		IPFBusiAction service = NCLocator.getInstance().lookup(
//				IPFBusiAction.class);
//		icbills = (PurchaseInVO[]) service.processAction("CANCELSIGN", "45", null,
//				icbills[0], null, null);
//		icbills = (PurchaseInVO[]) service.processAction("SIGN", "45", null,
//				icbills[0], null, null);
//
//		return null;
	}

}
