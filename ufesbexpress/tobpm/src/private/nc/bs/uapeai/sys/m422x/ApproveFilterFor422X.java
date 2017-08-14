package nc.bs.uapeai.sys.m422x;

import nc.bs.uapeai.sys.AbstractFilter;
import nc.vo.pu.m422x.entity.StoreReqAppVO;
import nc.vo.pub.BusinessException;

public class ApproveFilterFor422X extends AbstractFilter {

	public Object filtdata(Object aggvo, Object param)
			throws BusinessException {
		
		StoreReqAppVO bill = (StoreReqAppVO) aggvo;
		//��ǰ���յ��ݽ������ͱ�������жϵ�,�Ƿ�̶��ʲ������빺��422X-Cxx-01
		if(!"422X-Cxx-01".equalsIgnoreCase(bill.getHVO().getVtrantypecode())){
			return null;
		}
		
		return bill;
	}

	

}
