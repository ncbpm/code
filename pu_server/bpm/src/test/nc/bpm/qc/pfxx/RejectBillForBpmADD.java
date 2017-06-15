package nc.bpm.qc.pfxx;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.xml.out.tool.XmlOutTool;
import nc.itf.qc.c004.maintain.IRejectMaintain;
import nc.itf.qc.c004.page.IRejectBillMaintainApp;
import nc.vo.am.proxy.AMProxy;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.qc.c004.entity.RejectBillHeadVO;
import nc.vo.qc.c004.entity.RejectBillVO;

public class RejectBillForBpmADD extends AbstractPfxxPlugin{

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		RejectBillVO billVO = (RejectBillVO)vo;
		//设置审批通过
		RejectBillHeadVO headVO = (RejectBillHeadVO)billVO.getParentVO();
		headVO.setFbillstatus(3);
		
		IRejectMaintain service = AMProxy.lookup(IRejectMaintain.class);
		IRejectBillMaintainApp queryService = AMProxy.lookup(IRejectBillMaintainApp.class);
		RejectBillVO originVO = queryService.queryMC004App(new String[]{headVO.getPk_rejectbill()})[0];
		
		RejectBillVO[] temps = new RejectBillVO[1];
		temps[0] = originVO;
		headVO = (RejectBillHeadVO)originVO.getParentVO();
		headVO.setFbillstatus(2);
		RejectBillVO[] originVOs = new RejectBillVO[1];
		originVOs[0] = new RejectBillVO();
		((RejectBillHeadVO)originVOs[0].getParentVO()).setPk_rejectbill(headVO.getPk_rejectbill());
		service.saveBase(temps, new Object(), originVOs);	
		
		return billVO;
	}

}
