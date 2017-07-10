package nc.so.prealarm;

import nc.bs.pub.pa.IPreAlertPlugin;
import nc.bs.pub.pa.PreAlertContext;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.vo.hi.psndoc.PsndocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.sm.UserVO;

public class LockUserPreAlamPlugin implements IPreAlertPlugin{

	@Override
	public PreAlertObject executeTask(PreAlertContext context)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		//��ѯ���е���Ա������Ϣ
		//���� �Ͷ���ͬ�������ڣ�������ڣ������pk_psndoc �� sm_user�����user���������� . IUserManagerQuery ->UserVO[] queryUserVOsByPsnDocID(String pk_psndoc) 
		SqlBuilder sql = new SqlBuilder();
		sql.append(" select pk_psndoc from bd_psndoc psndoc ");
		sql.append(" where ");
		sql.append(" enablestate = 2");

		DataAccessUtils utils = new DataAccessUtils();
		IRowSet set = utils.query(sql.toString());
		if (set.size() == 0) {
			return null;
		}
		VOQuery<PsndocVO> bquery = new VOQuery<>(PsndocVO.class);
		PsndocVO[] userVos = bquery.query("and enablestate = 2", "");
		for(PsndocVO user : userVos){
			System.out.println(user.getPk_psndoc());
		}
		//BillQuery<PsndocAggVO> query = new BillQuery<PsndocAggVO>(PsndocAggVO.class);
		//bills = query.query(set.toOneDimensionStringArray());
		
		
		PreAlertObject retObj = new PreAlertObject();
		retObj.setReturnType(PreAlertReturnType.RETURNMULTILANGTEXT);
		return retObj;
	}

}
