package nc.so.prealarm;

import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.IPreAlertPlugin;
import nc.bs.pub.pa.PreAlertContext;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.hr.utils.PubEnv;
import nc.hr.utils.ResHelper;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.itf.hi.IHRAlert;
import nc.itf.uap.rbac.IUserManage;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.am.proxy.AMProxy;
import nc.vo.hi.psndoc.CtrtVO;
import nc.vo.hi.psndoc.PsndocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.sm.UserVO;

public class LockUserPreAlamPlugin implements IPreAlertPlugin{

	@Override
	public PreAlertObject executeTask(PreAlertContext context)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		//��ѯ���е���Ա������Ϣ
		//���� �Ͷ���ͬ�������ڣ�������ڣ������pk_psndoc �� sm_user�����user���������� . IUserManageQuery ->UserVO[] queryUserVOsByPsnDocID(String pk_psndoc) 
//		SqlBuilder sql = new SqlBuilder();
//		sql.append(" select pk_psndoc from bd_psndoc psndoc ");
//		sql.append(" where ");
//		sql.append(" enablestate = 2");

//		DataAccessUtils utils = new DataAccessUtils();
//		IRowSet set = utils.query(sql.toString());
//		if (set.size() == 0) {
//			return null;
//		}
//		VOQuery<PsndocVO> bquery = new VOQuery<>(PsndocVO.class);
//		PsndocVO[] userVos = bquery.query("and enablestate = 2", "");
//		for(PsndocVO user : userVos){
//			System.out.println(user.getPk_psndoc());
//		}
		//BillQuery<PsndocAggVO> query = new BillQuery<PsndocAggVO>(PsndocAggVO.class);
		//bills = query.query(set.toOneDimensionStringArray());
		
		PreAlertObject retObj = new PreAlertObject();
		//�·����� ֱ�Ӹ���enddate��ѯ�Ͷ���ͬ����ȡpk_psndoc 
		//Ȼ�� ��һ lock
		CtrtVO[] datas = this.queryContractData(context);
		if(datas == null){
			retObj.setReturnType(PreAlertReturnType.RETURNNOTHING);
		}else{
			IUserManageQuery queryUserSrv = AMProxy.lookup(IUserManageQuery.class);
			IUserManage optSrv = AMProxy.lookup(IUserManage.class);
			for(CtrtVO doc : datas){
				UserVO[] users = queryUserSrv.queryUserVOsByPsnDocID(doc.getPk_psndoc()); 
				if(users != null){
					//lock
					for(UserVO user : users){
						optSrv.updateLockedTagByAdmin(user.getCuserid(),true);
					}
				}
			}
			//Ŀǰ������Ԥ����Ϣ
			retObj.setReturnType(PreAlertReturnType.RETURNNOTHING);
		}
		return retObj;
	}
	
	private CtrtVO[] queryContractData(PreAlertContext context)throws BusinessException {
		CtrtVO[] datas = null;
		try {
			String pk_group = context.getGroupId();
			String[] pkHrorg = context.getPk_orgs();

			UFLiteralDate curdate = PubEnv.getServerLiteralDate();
			UFLiteralDate enddate = curdate;

			Vector vpara = new Vector();
			vpara.addElement(curdate);
			vpara.addElement(enddate);
			vpara.addElement(pk_group);
			vpara.addElement(0);
			vpara.addElement(pkHrorg);

			datas = NCLocator.getInstance().lookup(IHRAlert.class).queryContractData(vpara);

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
