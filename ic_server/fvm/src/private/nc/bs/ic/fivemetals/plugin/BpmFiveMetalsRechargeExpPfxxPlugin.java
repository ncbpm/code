package nc.bs.ic.fivemetals.plugin;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;

//  充值
public class BpmFiveMetalsRechargeExpPfxxPlugin<T extends AggFiveMetalsVO>
		extends nc.bs.pfxx.plugin.AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {

		// AggFiveMetalsVO bill = (AggFiveMetalsVO) vo;
		//
		// if (bill == null || bill.getParentVO() == null)
		// throw new BusinessException("传入数据出错");
		//
		// IFivemetalsMaintain manageService = NCLocator.getInstance().lookup(
		// IFivemetalsMaintain.class);
		// AggFiveMetalsVO returnvo = manageService.operatebill(bill);
		// return returnvo.getPrimaryKey();

		PurchaseInVO[] icbills = new BillQuery<PurchaseInVO>(PurchaseInVO.class)
				.query(new String[] { "1001ZZ1000000001I2S7" });
		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		icbills = (PurchaseInVO[]) service.processAction("CANCELSIGN", "45", null,
				icbills[0], null, null);
		icbills = (PurchaseInVO[]) service.processAction("SIGN", "45", null,
				icbills[0], null, null);

		return null;
	}

}
