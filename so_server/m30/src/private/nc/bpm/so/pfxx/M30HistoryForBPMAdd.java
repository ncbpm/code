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
		// TODO �Զ����ɵķ������
		SaleOrderHistoryVO bill = (SaleOrderHistoryVO)vo;
		//����ҵ����
		String vrevisereason = bill.getParentVO().getVrevisereason();
//		if(){}
		
		return bill.getParentVO().getPrimaryKey();
	}

}
