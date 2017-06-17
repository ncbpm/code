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
 * ���۳��ⵥ��ͨ������ϵͳ����
 * @author liyf
 *
 */
public class M4CForJLAdd extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		SaleOutVO importVO = (SaleOutVO )vo;
		//���ݷ�����pk ��ѯ��Ӧ ��������Ϣ
		IDeliveryMaintain queryService = AMProxy.lookup(IDeliveryMaintain.class);
		DeliveryVO[] deliveryVOs = queryService.queryDelivery(importVO.getParentVO().getPrimaryKey());
		if(deliveryVOs == null || deliveryVOs.length < 1){
			throw new BusinessException("����ָ����������"+ importVO.getParentVO().getPrimaryKey() + "δ��ѯ����������");
		}
		SaleOutVO[] resultVOs = doTranslate(importVO, deliveryVOs);
		if(resultVOs == null || resultVOs.length < 1){
			throw new BusinessException("���ݷ�����ת�����۳��ⵥ�����쳣��������������"+importVO.getParentVO().getPrimaryKey());
		}
		return resultVOs[0];
	}
	
	private SaleOutVO[] doTranslate(SaleOutVO importVO, DeliveryVO[] vos) {
	      PushSaveAction action = new PushSaveAction();
	      String scode = SOBillType.Delivery.getCode();
	      String dcode = ICBillType.SaleOut.getCode();//QCBillType.ApplyBill.getCode();
	      SaleOutVO[] voArray = PfServiceScmUtil.executeVOChange(scode, dcode, vos);
	      //retObj = action.matchStdQCCenterTranContbatch(voArray);
	      //���ݴ�����������-���Ա���ֿ�id������ʱ�䣬���{����id��ʵ������}
	      voArray[0].getHead().setCwarehouseid(importVO.getHead().getCwarehouseid());
	      voArray[0].getHead().setCwhsmanagerid(importVO.getHead().getCwhsmanagerid());
	      voArray[0].getHead().setDbilldate(importVO.getHead().getDbilldate());
	      //��������
	      //ͬʱ����������
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
	      //����������
	      voArray[0].getHead().setNtotalnum(new UFDouble(TotalWeight));
	      return action.pushSave(voArray, false);
	}

}
