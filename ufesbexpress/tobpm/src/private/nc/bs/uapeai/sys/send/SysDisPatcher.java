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

		// 1. 总控参数，检查是否关闭，则设置返回success
//		if (!totalControl()) {
//			return "success";
//		}

		// 客商基本档案，前台按钮同步：同时同步基本档案+管理档案
		// 借用，已经实现的同步功能，不再单独实现
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
		//使用中文，方便后续生成xml文件的命名		
		if ("bpm_422X".equalsIgnoreCase(pk_billtype)) {
			sender = new ApproveSenderFor422X();
			filter = new ApproveFilterFor422X();
			return;

		}else {
			throw new BusinessException("未支持的同步类型");
		}

	}

}
