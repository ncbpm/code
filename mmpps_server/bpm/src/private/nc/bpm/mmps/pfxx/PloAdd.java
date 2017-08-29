package nc.bpm.mmps.pfxx;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.itf.mmpps.plo.IAcePoMaintainService;
import nc.itf.mmpps.plo.IPloSimpleBusiService;
import nc.vo.mmpps.mps0202.AggregatedPoVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;


/**
 * MRP 计划订单BPM导入
 * @author liyf
 *
 */
public class PloAdd extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		AggregatedPoVO billVOs[] = new AggregatedPoVO[1];
		billVOs[0] = (AggregatedPoVO)vo;
		IAcePoMaintainService service = NCLocator.getInstance().lookup(IAcePoMaintainService.class);
		AggregatedPoVO retVOs[] = service.insert(billVOs);
		if(retVOs != null && retVOs.length >0){
			//确认
			IPloSimpleBusiService simp = NCLocator.getInstance().lookup(IPloSimpleBusiService.class);
			simp.doPoConfirm(retVOs);
			
			return retVOs[0].getPrimaryKey();
		}
		return null;
	}

}
