package nc.bpm.qc.pfxx;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.xml.out.tool.XmlOutTool;
import nc.itf.qc.c003.maintain.IReportMaintain;
import nc.itf.qc.c004.maintain.IRejectMaintain;
import nc.itf.qc.c004.page.IRejectBillMaintainApp;
import nc.vo.am.proxy.AMProxy;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.qc.c003.entity.ReportHeaderVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.c004.entity.RejectBillHeadVO;
import nc.vo.qc.c004.entity.RejectBillItemVO;
import nc.vo.qc.c004.entity.RejectBillVO;
import nc.vo.so.pub.enumeration.BillStatus;

public class RejectBillForBpmADD extends AbstractPfxxPlugin{

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		/*
		IRejectMaintain service = AMProxy.lookup(IRejectMaintain.class);
		IRejectBillMaintainApp queryService = AMProxy.lookup(IRejectBillMaintainApp.class);
		RejectBillVO[] exportBillVOs = queryService.queryMC004App(new String[]{"1001A41000000000DY5F"});
		RejectBillHeadVO headVO =  (RejectBillHeadVO)(exportBillVOs[0].getParentVO());
		try {
			XmlOutTool.votoXmlFile("bpm_rejcet_add", exportBillVOs, headVO.getPk_org(), headVO.getVbillcode());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		*/
		final Integer PASSED = 3;
		RejectBillVO billVO = (RejectBillVO)vo;
		RejectBillHeadVO headVO = (RejectBillHeadVO)billVO.getParentVO();
		
		IRejectMaintain service = AMProxy.lookup(IRejectMaintain.class);
		IRejectBillMaintainApp queryService = AMProxy.lookup(IRejectBillMaintainApp.class);
		//根据主键 查询origin VO
		RejectBillVO[] originVOs = new RejectBillVO[1];
		RejectBillVO originVO = queryService.queryMC004App(new String[]{headVO.getPk_rejectbill()})[0];
		originVOs[0] = originVO;
		
		RejectBillVO[] temps = new RejectBillVO[1];
		temps[0] = queryService.queryMC004App(new String[]{headVO.getPk_rejectbill()})[0];	
		RejectBillHeadVO tempHeadVO = (RejectBillHeadVO)temps[0].getParentVO();
		//设置审批状态--通过
		tempHeadVO.setFbillstatus(PASSED);
		tempHeadVO.setStatus(VOStatus.UPDATED);
		//tempHeadVO.setModifiedtime(new UFDateTime());
		//设置明细 判定结果
		RejectBillItemVO[] modifyItems = (RejectBillItemVO[])billVO.getChildrenVO();
		RejectBillItemVO[] originItems = (RejectBillItemVO[])temps[0].getChildrenVO();
		
		for(RejectBillItemVO ele : modifyItems){
			for(RejectBillItemVO org: originItems){
				if(ele.getPk_rejectbill_b().equals(org.getPk_rejectbill_b())){
					org.setFprocessjudge(ele.getFprocessjudge());
					org.setStatus(VOStatus.UPDATED);
					break;
				}
			}
		}
		service.saveBase(temps, new Object(), originVOs);	
		
		//设置质检报告 审批状态
		 //根据不合格单的 pk_reportbill 查出质检报告
		IReportMaintain reportService = AMProxy.lookup(IReportMaintain.class);
		ReportVO orgReport = reportService.querySingleBillByPkAndBatchInfo(tempHeadVO.getPk_reportbill());
		ReportHeaderVO reportHead = (ReportHeaderVO)orgReport.getParentVO();
		reportHead.setFbillstatus(PASSED);
		reportHead.setStatus(VOStatus.UPDATED);
		//reportHead.setModifiedtime(new UFDateTime());
		//update
		ReportVO[] tempReports = new ReportVO[1];
		tempReports[0] = orgReport;
		ReportVO[] orgReports = new ReportVO[1];
		orgReports[0] = reportService.querySingleBillByPkAndBatchInfo(tempHeadVO.getPk_reportbill());
		reportService.saveBase(tempReports, new Object(), orgReports);
		return billVO;
	}

}
