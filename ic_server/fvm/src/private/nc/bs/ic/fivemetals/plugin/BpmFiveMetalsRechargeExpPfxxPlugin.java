package nc.bs.ic.fivemetals.plugin;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.itf.ic.fivemetals.IFivemetalsMaintain;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;

//  充值
public class BpmFiveMetalsRechargeExpPfxxPlugin<T extends AggFiveMetalsVO>
		extends nc.bs.pfxx.plugin.AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {

		AggFiveMetalsVO bill = (AggFiveMetalsVO) vo;

		if (bill == null || bill.getParentVO() == null)
			throw new BusinessException("传入数据出错");

		IFivemetalsMaintain manageService = NCLocator.getInstance().lookup(
				IFivemetalsMaintain.class);
		AggFiveMetalsVO returnvo = manageService.operatebill(bill);
		return returnvo.getPrimaryKey();
	}

}
