package nc.bpm.ic.pfxx;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.xml.out.tool.XmlOutTool;
import nc.pubitf.ic.m45.api.IPurchaseInQueryAPI;
import nc.vo.am.proxy.AMProxy;
import nc.vo.ic.m45.entity.PurchaseInHeadVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;

/**
 * �ɹ���ⵥͨ������ϵͳ����
 * @author liyf
 *
 */
public class M45ForJLAdd  extends AbstractPfxxPlugin{

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		IPurchaseInQueryAPI queryService = AMProxy.lookup(IPurchaseInQueryAPI.class);
		PurchaseInVO [] exportBillVOs = queryService.queryVOByIDs(new String[]{"1001A410000000001GIX"});
		PurchaseInHeadVO headVO =  (PurchaseInHeadVO)(exportBillVOs[0].getParentVO());
		try {
			XmlOutTool.votoXmlFile("JL_45_add", exportBillVOs, headVO.getPk_org(), headVO.getVbillcode());
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return null;
	}

}
