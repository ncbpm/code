package nc.bpm.ic.pfxx;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.xml.out.tool.XmlOutTool;
import nc.impl.ic.m45.action.PushSaveAction;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.pubitf.pu.m23.api.IArriveBillQueryAPI;
import nc.vo.am.proxy.AMProxy;
import nc.vo.ic.m45.entity.PurchaseInBodyVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scmpub.res.billtype.ICBillType;
import nc.vo.scmpub.res.billtype.POBillType;

/**
 * 采购入库单通过计量系统生成
 * @author liyf
 *
 */
public class M45ForJLAdd  extends AbstractPfxxPlugin{

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		/*
		IPurchaseInQueryAPI queryService = AMProxy.lookup(IPurchaseInQueryAPI.class);
		PurchaseInVO [] exportBillVOs = queryService.queryVOByIDs(new String[]{"1001A410000000001GIX"});
		PurchaseInHeadVO headVO =  (PurchaseInHeadVO)(exportBillVOs[0].getParentVO());
		try {
			XmlOutTool.votoXmlFile("JL_45_add", exportBillVOs, headVO.getPk_org(), headVO.getVbillcode());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		*/
		//计量系统会传入 到货单pk，库管员，仓库id，入库时间，多个{物料id，实收数量}
		//nc需要根据到货单pk，抽出其他信息，填充全部字段
		PurchaseInVO importVO = (PurchaseInVO)vo;
		IArriveBillQueryAPI queryService = AMProxy.lookup(IArriveBillQueryAPI.class);
		ArriveVO[] arriveVOs = queryService.queryVOByIDs(new String[]{importVO.getParentVO().getPrimaryKey()});
		if(arriveVOs == null || arriveVOs.length < 1){
			throw new BusinessException("根据指定的主键，"+ importVO.getParentVO().getPrimaryKey() + "未查询到到货单！");
		}
		//ArriveVO arriveVO = arriveVOs[0];
		PurchaseInVO[] resultVOs = doTranslate(importVO, arriveVOs);
		if(resultVOs == null || resultVOs.length < 1){
			throw new BusinessException("根据到货单转换采购入库单处理异常！到货单主键："+importVO.getParentVO().getPrimaryKey());
		}
		return resultVOs[0];
	}
	
	private PurchaseInVO[] doTranslate(PurchaseInVO importVO, ArriveVO[] vos) throws BusinessException{
	      PushSaveAction action = new PushSaveAction();
	      String scode = POBillType.Arrive.getCode();
	      String dcode = ICBillType.PurchaseIn.getCode();//QCBillType.ApplyBill.getCode();
	      PurchaseInVO[] voArray = PfServiceScmUtil.executeVOChange(scode, dcode, vos);
	      //retObj = action.matchStdQCCenterTranContbatch(voArray);
	      //根据传入的数据填充-库管员，仓库id，入库时间，多个{物料id，实收数量}
	      voArray[0].getHead().setCwarehouseid(importVO.getHead().getCwarehouseid());
	      voArray[0].getHead().setCwhsmanagerid(importVO.getHead().getCwhsmanagerid());
	      voArray[0].getHead().setDbilldate(importVO.getHead().getDbilldate());
	      //遍历物料
	      //同时计算总数量
	      double TotalWeight = 0;
	      for(PurchaseInBodyVO item : voArray[0].getBodys()){
	    	  for(PurchaseInBodyVO inItem : importVO.getBodys()){
	    		  if(item.getCmaterialoid() == inItem.getCmaterialoid()){
	    			  TotalWeight += inItem.getNassistnum().doubleValue();
	    			  item.setNassistnum(inItem.getNassistnum());
	    			  item.setDbizdate(importVO.getHead().getDbilldate());
	    		  }
	    	  }
	      }
	      //设置总数量
	      voArray[0].getHead().setNtotalnum(new UFDouble(TotalWeight));
	      return action.pushSave(voArray, false);
	}

}
