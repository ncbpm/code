package nc.bs.hr.wa.paydata.plugin;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.itf.hr.wa.IPaydataManageService;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.wa.adjust.AggPsnappaproveVO;
import nc.vo.wa.pub.WaLoginContext;


/**
 * Ð½×Ê·¢·Å
 */
public class BpmPayDataExpPfxxPlugin < T extends AggPsnappaproveVO> extends
nc.bs.pfxx.plugin.AbstractPfxxPlugin {

	private IPaydataManageService manageService;
	
	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		
		WaLoginContext loginContext = new WaLoginContext();
		
		getManageService().onPay(loginContext);
		return null;
	}

	
	protected IPaydataManageService getManageService() {
		if (manageService == null) {
			manageService = NCLocator.getInstance().lookup(IPaydataManageService.class);
		}
		return manageService;
	}
}
