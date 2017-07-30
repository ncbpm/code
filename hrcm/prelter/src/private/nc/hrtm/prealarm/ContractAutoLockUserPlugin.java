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
 *1.��ͬ���ڣ�������Ա��Ӧ�� �û�
 * 
 * @author liyf
 * 
 */
public class ContractAutoLockUserPlugin extends HRPreAlertPlugin {

	@Override
	public PreAlertObject executeTask(PreAlertContext context)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		MultiReceiverPreAlertObject retObj = new MultiReceiverPreAlertObject();
		// �·����� ֱ�Ӹ���enddate��ѯ�Ͷ���ͬ����ȡpk_psndoc
		// Ȼ�� ��һ lock
		CtrtVO[] datas = this.queryContractData(context);
		List<CtrtVO> needDeal = new ArrayList<CtrtVO>();
		if (datas == null || datas.length == 0) {
			return null;
		}
		InvocationInfoProxy.getInstance().setCallId("contract");
		IUserManageQuery queryUserSrv = AMProxy.lookup(IUserManageQuery.class);
		IUserManage optSrv = AMProxy.lookup(IUserManage.class);
		String info = "��ã� ����Ա����ͬ�Ѿ�����,NC��¼�˺��Ѿ�ִ���������뾡�촦��.����Ա����鿴����";
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
				//δ������δ���
			}
		}

		if (needDeal.size() == 0) {
			return null;
		}
		retObj = HRPreAlertUtils.addAllReceiver(context,
				POICtrtMetaDataDataSource.class, needDeal.toArray(new CtrtVO[0]));
		// ����Ԥ����Ϣ
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

			// �����
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
