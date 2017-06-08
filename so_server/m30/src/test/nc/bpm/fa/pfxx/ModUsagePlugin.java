package nc.bpm.fa.pfxx;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.xml.out.tool.XmlOutTool;
import nc.itf.fa.prv.IAlter;
import nc.vo.fa.alter.AlterHeadVO;
import nc.vo.fa.alter.AlterVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;

public class ModUsagePlugin extends AbstractPfxxPlugin{

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		IAlter service = NCLocator.getInstance().lookup(IAlter.class);
		//ReduceVO[] reduceVOs = service.queryReduceVOByPKs(new String[]{"ZCJS201702150001"});
		AlterVO[] alterVOs = service.queryAlters(new String[]{"1001ZZ1000000000DUGS"});
		
		try {
			AlterVO alterVO = alterVOs[0];
			AlterHeadVO headVO = (AlterHeadVO) alterVO.getParent();
			XmlOutTool.votoXmlFile("bpm_HG_modify_usage", alterVOs, headVO.getPk_org(), headVO.getBill_code());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}

}
