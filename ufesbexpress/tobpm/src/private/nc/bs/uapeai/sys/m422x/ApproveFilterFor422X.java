package nc.bs.uapeai.sys.m422x;

import nc.bs.uapeai.sys.AbstractFilter;
import nc.vo.pu.m422x.entity.StoreReqAppVO;
import nc.vo.pub.BusinessException;

public class ApproveFilterFor422X extends AbstractFilter {

	public Object filtdata(Object aggvo, Object param)
			throws BusinessException {
		
		StoreReqAppVO bill = (StoreReqAppVO) aggvo;
		//当前按照单据交易类型编码进行判断的,是否固定资产类型请购，422X-Cxx-01
		if(!"422X-Cxx-01".equalsIgnoreCase(bill.getHVO().getVtrantypecode())){
			return null;
		}
		
		return bill;
	}

	

}
