package nc.bpm.mmps.pfxx;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.itf.mmpps.plo.IAcePoMaintainService;
import nc.itf.mmpps.plo.IAcePoQueryService;
import nc.vo.am.proxy.AMProxy;
import nc.vo.mmpps.mps0202.AggregatedPoVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;


public class PloAddPlugin  extends AbstractPfxxPlugin  {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		IAcePoQueryService service = AMProxy.lookup(IAcePoQueryService.class);
		AggregatedPoVO[] tempVOs = service.queryByQueryForIDS(new String[]{"1001A31000000000KL2M"});
		AggregatedPoVO billVO = tempVOs[0];
		try {
			XmlOutTool.votoXmlFile("bpm_plo_add", tempVOs, billVO.getParentVO().getPk_org(), billVO.getParentVO().getVbillcode());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
//		
//		AggregatedPoVO billVOs[] = new AggregatedPoVO[1];
//		billVOs[0] = (AggregatedPoVO)vo;
//		IAcePoMaintainService service = AMProxy.lookup(IAcePoMaintainService.class);
//		AggregatedPoVO retVOs[] = service.insert(billVOs);
//		if(retVOs != null && retVOs.length >0){
//			return retVOs[0];
//		}
		return null;
	}



}
