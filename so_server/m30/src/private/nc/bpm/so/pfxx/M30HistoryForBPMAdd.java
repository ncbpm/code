package nc.bpm.so.pfxx;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.so.m30.revise.entity.SaleOrderHistoryVO;

public class M30HistoryForBPMAdd extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		SaleOrderHistoryVO bill = (SaleOrderHistoryVO)vo;
		//具体业务处理
		String vrevisereason = bill.getParentVO().getVrevisereason();
//		if(){}
		
		return bill.getParentVO().getPrimaryKey();
	}

}
