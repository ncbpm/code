package nc.bpm.ic.pfxx;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.impl.ic.m4c.action.PushSaveAction;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.so.m4331.IDeliveryMaintain;
import nc.vo.am.proxy.AMProxy;
import nc.vo.ic.m4c.entity.SaleOutBodyVO;
import nc.vo.ic.m4c.entity.SaleOutVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.res.billtype.ICBillType;
import nc.vo.scmpub.res.billtype.POBillType;
import nc.vo.scmpub.res.billtype.SOBillType;
import nc.vo.so.m4331.entity.DeliveryVO;


/**
 * 销售出库单，通过计量系统生成
 * @author liyf
 *
 */
public class M4CForJLAdd extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		SaleOutVO importVO = (SaleOutVO )vo;
		//根据发货单pk 查询相应 发货单信息
		IDeliveryMaintain queryService = AMProxy.lookup(IDeliveryMaintain.class);
		DeliveryVO[] deliveryVOs = queryService.queryDelivery(importVO.getParentVO().getPrimaryKey());
		if(deliveryVOs == null || deliveryVOs.length < 1){
			throw new BusinessException("根据指定的主键，"+ importVO.getParentVO().getPrimaryKey() + "未查询到发货单！");
		}
		SaleOutVO[] resultVOs = doTranslate(importVO, deliveryVOs);
		if(resultVOs == null || resultVOs.length < 1){
			throw new BusinessException("根据发货单转换销售出库单处理异常！发货单主键："+importVO.getParentVO().getPrimaryKey());
		}
		return resultVOs[0];
	}
	
	private SaleOutVO[] doTranslate(SaleOutVO importVO, DeliveryVO[] vos) {
	      PushSaveAction action = new PushSaveAction();
	      String scode = SOBillType.Delivery.getCode();
	      String dcode = ICBillType.SaleOut.getCode();//QCBillType.ApplyBill.getCode();
	      SaleOutVO[] voArray = PfServiceScmUtil.executeVOChange(scode, dcode, vos);
	      //retObj = action.matchStdQCCenterTranContbatch(voArray);
	      //根据传入的数据填充-库管员，仓库id，出库时间，多个{物料id，实发数量}
	      voArray[0].getHead().setCwarehouseid(importVO.getHead().getCwarehouseid());
	      voArray[0].getHead().setCwhsmanagerid(importVO.getHead().getCwhsmanagerid());
	      voArray[0].getHead().setDbilldate(importVO.getHead().getDbilldate());
	      //遍历物料
	      //同时计算总数量
	      double TotalWeight = 0;
	      for(SaleOutBodyVO item : voArray[0].getBodys()){
	    	  for(SaleOutBodyVO inItem : importVO.getBodys()){
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
