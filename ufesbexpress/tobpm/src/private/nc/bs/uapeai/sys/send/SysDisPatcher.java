package nc.bs.uapeai.sys.send;

import nc.bs.dao.BaseDAO;
import nc.bs.uapeai.sys.m422x.ApproveFilterFor422X;
import nc.bs.uapeai.sys.m422x.ApproveSenderFor422X;
import nc.itf.uapeai.sys.IHKFilter;
import nc.itf.uapeai.sys.IHKSender;
import nc.itf.uapeai.sys.ISysDisPatcher;
import nc.vo.pub.BusinessException;

/**
 * 
 * @author liyf_brave
 * 
 */
public class SysDisPatcher implements ISysDisPatcher {

	private BaseDAO dao = null;

	public BaseDAO getDao() {
		if (dao == null)
			dao = new BaseDAO();
		return dao;
	}

	IHKSender sender;

	IHKFilter filter;

	public IHKSender getSender() {
		return sender;
	}

	public void setSender(IHKSender sender) {
		this.sender = sender;
	}

	public IHKFilter getFilter() {
		return filter;
	}

	public void setFilter(IHKFilter filter) {
		this.filter = filter;
	}


	public Object handleRequest(Object reqObje, String pk_billtype, Object param)
			throws BusinessException {

		// 1. �ܿز���������Ƿ�رգ������÷���success
//		if (!totalControl()) {
//			return "success";
//		}

		// ���̻���������ǰ̨��ťͬ����ͬʱͬ����������+������
		// ���ã��Ѿ�ʵ�ֵ�ͬ�����ܣ����ٵ���ʵ��
		Object o = null;
		o = dealSend(reqObje, pk_billtype, param);
		return o;

	}

	private Object dealSend(Object reqObje, String pk_billtype, Object param)
			throws BusinessException {
		// TODO Auto-generated method stub
		createSenderAndFilter(pk_billtype);
		try {
			if (getFilter() != null) {

				Object filtdata = getFilter().filtdata(reqObje, param);
				if (filtdata == null)
					return "success";
				return getSender().process(filtdata);
			}
			return getSender().process(reqObje);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}

	private void createSenderAndFilter(String pk_billtype)
			throws BusinessException {
		// TODO Auto-generated method stub
		sender = null;
		filter = null;
		//ʹ�����ģ������������xml�ļ�������		
		if ("bpm_422X".equalsIgnoreCase(pk_billtype)) {
			sender = new ApproveSenderFor422X();
			filter = new ApproveFilterFor422X();
			return;

		}else {
			throw new BusinessException("δ֧�ֵ�ͬ������");
		}

	}

}
