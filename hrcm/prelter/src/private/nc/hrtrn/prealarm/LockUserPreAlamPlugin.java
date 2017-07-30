package nc.hrtrn.prealarm;

import java.util.Vector;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.IPreAlertPlugin;
import nc.bs.pub.pa.PreAlertContext;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.hr.utils.PubEnv;
import nc.itf.hi.IHRAlert;
import nc.itf.uap.rbac.IUserManage;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.am.proxy.AMProxy;
import nc.vo.hi.psndoc.CtrtVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.MultiLangText;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.sm.UserVO;

public class LockUserPreAlamPlugin implements IPreAlertPlugin {

	@Override
	public PreAlertObject executeTask(PreAlertContext context)
			throws BusinessException {
		// TODO 自动生成的方法存根
		PreAlertObject retObj = new PreAlertObject();
		// 新方案： 直接根据enddate查询劳动合同，获取pk_psndoc
		// 然后 逐一 lock
		CtrtVO[] datas = this.queryContractData(context);
		if (datas == null) {
			retObj.setReturnType(PreAlertReturnType.RETURNNOTHING);
		} else {
			InvocationInfoProxy.getInstance().setCallId("contract");
			String info = "如下人员账号因劳动合同到期被锁定：";
			IUserManageQuery queryUserSrv = AMProxy
					.lookup(IUserManageQuery.class);
			IUserManage optSrv = AMProxy.lookup(IUserManage.class);
			for (CtrtVO doc : datas) {
				UserVO[] users = queryUserSrv.queryUserVOsByPsnDocID(doc
						.getPk_psndoc());
				if (users != null) {
					// lock
					for (UserVO user : users) {
						if(user.getIsLocked().booleanValue()){
							continue;
						}
						info += ("[" + user.getUser_code() + ",");
						info += user.getUser_name() + "]";
				
						optSrv.updateLockedTag(user.getCuserid(),true);
					}
				}
			}
			// 发送预警信息
			 MultiLangText retMsg = new MultiLangText();
			 retMsg.setText(info);
			 retObj.setReturnObj(retMsg);
			 retObj.setReturnType(PreAlertReturnType.RETURNMULTILANGTEXT);
		}
		return retObj;
	}

	private CtrtVO[] queryContractData(PreAlertContext context)
			throws BusinessException {
		CtrtVO[] datas = null;
		try {
			String pk_group = context.getGroupId();
			String[] pkHrorg = context.getPk_orgs();

			UFLiteralDate curdate = new UFLiteralDate("2017-01-01");
			UFLiteralDate enddate = PubEnv.getServerLiteralDate();

			Vector vpara = new Vector();
			vpara.addElement(curdate);
			vpara.addElement(enddate);
			vpara.addElement(pk_group);
			vpara.addElement(0);
			vpara.addElement(pkHrorg);

			datas = NCLocator.getInstance().lookup(IHRAlert.class)
					.queryContractData(vpara);

			if (datas == null || datas.length == 0)
				return null;

			// 加序号
			for (int i = 0; i < datas.length; i++) {
				datas[i].setAttributeValue("rownum", i + 1);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e);
		}

		return datas;
	}

}
