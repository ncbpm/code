package nc.bs.pfxx.test.pub;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.xml.out.tool.XmlOutTool;
import nc.itf.pu.m21.IOrderQuery;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class Testout extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		AggregatedValueObject resvo = (AggregatedValueObject) vo;
		String vtrantypecode = (String) resvo.getParentVO().getAttributeValue("vtrantypecode");
		//采购订单付款计划
		if("bpm_payplan_add".equalsIgnoreCase(vtrantypecode)){
			IOrderQuery query = NCLocator.getInstance().lookup(IOrderQuery.class);
			OrderVO[] vos = query.queryOrderVOsByIds(new String[]{"1001B11000000001D20X"}, UFBoolean.FALSE);
			String pk_org = vos[0].getHVO().getPk_org();
			String vbillcode = vos[0].getHVO().getVbillcode();
			try {
				XmlOutTool.votoXmlFile("bpm_payplan_add", vos, pk_org, vbillcode);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			};
		}
		return null;
	}

}
