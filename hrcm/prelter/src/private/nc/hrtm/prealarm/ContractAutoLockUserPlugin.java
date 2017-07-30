package nc.hrtm.prealarm;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.hr.prealert.HRPreAlertPlugin;
import nc.bs.hr.prealert.HRPreAlertUtils;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertContext;
import nc.bs.pub.pa.PreAlertObject;
import nc.ds.hi.psndoc.POICtrtMetaDataDataSource;
import nc.hr.utils.PubEnv;
import nc.itf.hi.IHRAlert;
import nc.itf.uap.rbac.IUserManage;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.am.proxy.AMProxy;
import nc.vo.hi.psndoc.CtrtVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.sm.UserVO;
import uap.vo.prealert.engine.MultiReceiverPreAlertObject;

/**
 *1.合同到期，锁定人员对应的 用户
 * 
 * @author liyf
 * 
 */
public class ContractAutoLockUserPlugin extends HRPreAlertPlugin {

	@Override
	public PreAlertObject executeTask(PreAlertContext context)
			throws BusinessException {
		// TODO 自动生成的方法存根
		MultiReceiverPreAlertObject retObj = new MultiReceiverPreAlertObject();
		// 新方案： 直接根据enddate查询劳动合同，获取pk_psndoc
		// 然后 逐一 lock
		CtrtVO[] datas = this.queryContractData(context);
		List<CtrtVO> needDeal = new ArrayList<CtrtVO>();
		if (datas == null || datas.length == 0) {
			return null;
		}
		InvocationInfoProxy.getInstance().setCallId("contract");
		IUserManageQuery queryUserSrv = AMProxy.lookup(IUserManageQuery.class);
		IUserManage optSrv = AMProxy.lookup(IUserManage.class);
		String info = "你好： 以下员工合同已经到期,NC登录账号已经执行锁定，请尽快处理.具体员工请查看附件";
		for (CtrtVO doc : datas) {
			UserVO[] users = queryUserSrv.queryUserVOsByPsnDocID(doc
					.getPk_psndoc());
			if (users != null) {
				// lock
				for (UserVO user : users) {
					if (user.getIsLocked().booleanValue()) {
						continue;
					}
					info += ("[" + user.getUser_code() + ",");
					info += user.getUser_name() + "]";
					optSrv.updateLockedTag(user.getCuserid(), true);
					needDeal.add(doc);
				}
			} else {
				//未关联如何处理？
			}
		}

		if (needDeal.size() == 0) {
			return null;
		}
		retObj = HRPreAlertUtils.addAllReceiver(context,
				POICtrtMetaDataDataSource.class, needDeal.toArray(new CtrtVO[0]));
		// 发送预警信息
		return retObj;
	}

	@SuppressWarnings("unchecked")
	private CtrtVO[] queryContractData(PreAlertContext context)
			throws BusinessException {
		CtrtVO[] datas = null;
		try {
			String pk_group = context.getGroupId();
			String[] pkHrorg = context.getPk_orgs();
			String[] keys = context.getKeyMap().keySet().toArray(new String[0]);
			

			UFLiteralDate curdate = new UFLiteralDate("2015-01-01");
			UFLiteralDate enddate = PubEnv.getServerLiteralDate();
			for (String key : keys) {
				if (key.equals("startdate")) {
					String value = (String) context.getKeyMap().get(key);
					curdate = new UFLiteralDate(value);
				}
			}

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
