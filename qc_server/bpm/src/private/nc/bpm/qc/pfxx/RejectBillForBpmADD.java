package nc.bpm.qc.pfxx;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.xml.out.tool.XmlOutTool;
import nc.itf.qc.c004.maintain.IRejectMaintain;
import nc.itf.qc.c004.page.IRejectBillMaintainApp;
import nc.vo.am.proxy.AMProxy;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.qc.c004.entity.RejectBillHeadVO;
import nc.vo.qc.c004.entity.RejectBillItemVO;
import nc.vo.qc.c004.entity.RejectBillVO;

public class RejectBillForBpmADD extends AbstractPfxxPlugin{

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		/*
		IRejectMaintain service = AMProxy.lookup(IRejectMaintain.class);
		IRejectBillMaintainApp queryService = AMProxy.lookup(IRejectBillMaintainApp.class);
		RejectBillVO[] exportBillVOs = queryService.queryMC004App(new String[]{"1001A41000000000DY5F"});
		RejectBillHeadVO headVO =  (RejectBillHeadVO)(exportBillVOs[0].getParentVO());
		try {
			XmlOutTool.votoXmlFile("bpm_rejcet_add", exportBillVOs, headVO.getPk_org(), headVO.getVbillcode());
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		*/
		RejectBillVO billVO = (RejectBillVO)vo;
		RejectBillHeadVO headVO = (RejectBillHeadVO)billVO.getParentVO();
		
		IRejectMaintain service = AMProxy.lookup(IRejectMaintain.class);
		IRejectBillMaintainApp queryService = AMProxy.lookup(IRejectBillMaintainApp.class);
		//�������� ��ѯorigin VO
		RejectBillVO[] originVOs = new RejectBillVO[1];
		RejectBillVO originVO = queryService.queryMC004App(new String[]{headVO.getPk_rejectbill()})[0];
		originVOs[0] = originVO;
		
		RejectBillVO[] temps = new RejectBillVO[1];
		temps[0] = queryService.queryMC004App(new String[]{headVO.getPk_rejectbill()})[0];	
		RejectBillHeadVO tempHeadVO = (RejectBillHeadVO)temps[0].getParentVO();
		//��������״̬--ͨ��
		tempHeadVO.setFbillstatus(3);
		//������ϸ �ж����
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
		
		//�����ʼ챨�� ����״̬
		
		return billVO;
	}

}
