package nc.bpm.pu.pfxx;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;

/**
 * 采购订单付款计划BPM回写接口
 * @author liyf
 *
 */
public class OrderPayPlan extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		return null;
	}

}
