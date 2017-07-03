package nc.bpm.so.mcredit;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.xml.out.tool.XmlOutTool;
import nc.itf.so.m30.revise.ISaleOrderReviseMaintainApp;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.so.m30.revise.entity.SaleOrderHistoryVO;

public class ExportXml extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		// //查询销售订单修改VO
		//
		ISaleOrderReviseMaintainApp service = NCLocator.getInstance().lookup(
				ISaleOrderReviseMaintainApp.class);
		SaleOrderHistoryVO[] historyVOs = service
				.queryM30ReviseApp(new String[] { "1001A41000000000CEOX" });

		try {
			XmlOutTool.votoXmlFile("bpm_30_history", historyVOs, historyVOs[0]
					.getParentVO().getPk_org(), historyVOs[0].getParentVO()
					.getVbillcode());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		// OrderVO historyVO = queryVOByPk("1001A410000000001FL2");
		//
		// try {
		// XmlOutTool.votoXmlFile("bpm_21_add", new OrderVO[] { historyVO },
		// historyVO.getHVO().getPk_org(), historyVO.getHVO()
		// .getVbillcode());
		// } catch (Exception e) {
		// // TODO 自动生成的 catch 块
		// e.printStackTrace();
		// }
		return "successs";
	}

}
