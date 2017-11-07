package nc.impl.wa.adjust;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.core.service.TimeService;
import nc.bs.logging.Logger;
import nc.hr.frame.persistence.BaseDAOManager;
import nc.hr.frame.persistence.IValidatorFactory;
import nc.hr.frame.persistence.SimpleDocServiceTemplate;
import nc.hr.utils.PubEnv;
import nc.hr.utils.ResHelper;
import nc.itf.hr.frame.IHrBillCode;
import nc.itf.hr.pf.HrPfHelper;
import nc.itf.hr.pf.IHrPf;
import nc.itf.hr.wa.IPsndocwadocQueryService;
import nc.itf.hr.wa.IWaAdjustManageService;
import nc.itf.hr.wa.IWaAdjustQueryService;
import nc.itf.hr.wa.IWaGradeService;
import nc.itf.hr.wa.IWaPsnHiService;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.md.persist.framework.MDPersistenceService;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.pub.billcode.vo.BillCodeContext;
import nc.pub.tools.HiCacheUtils;
import nc.ui.hr.comp.sort.UFDoubleCompare;
import nc.vo.hi.wadoc.PsndocWadocVO;
import nc.vo.hr.pf.PFAggVO;
import nc.vo.hr.pf.PFQueryParams;
import nc.vo.hr.rf.HrBillRefSupVO;
import nc.vo.om.pub.SuperVOHelper;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.uap.pf.PfProcessBatchRetObject;
import nc.vo.uap.rbac.constant.INCSystemUserConst;
import nc.vo.uif2.LoginContext;
import nc.vo.util.AuditInfoUtil;
import nc.vo.wa.adjust.AdjustWadocVO;
import nc.vo.wa.adjust.AggPsnappaproveVO;
import nc.vo.wa.adjust.BatchAdjustVO;
import nc.vo.wa.adjust.PsnappaproveBVO;
import nc.vo.wa.adjust.PsnappaproveVO;
import nc.vo.wa.adjust.WaAdjustParaTool;
import nc.vo.wa.grade.WaCriterionVO;
import nc.vo.wa.grade.WaGradeVO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * ������ ServiceImpl
 *
 * @author: xuhw
 * @date: 2009-12-17 ����12:56:56
 * @since: eHR V6.0
 * @�߲���:
 * @�߲�����:
 * @�޸���:
 * @�޸�����:
 */
public class WaAdjustManageServiceImpl implements IWaAdjustManageService, IWaAdjustQueryService, IWaPsnHiService
{
	/**
	 * ȡ�ö�������Ϣά�� ��ѯ�ӿ�
	 *
	 * @author xuhw on 2010-1-7
	 * @return
	 */
	private static IPsndocwadocQueryService lookupPsndocwadocQueryService()
	{
		return (IPsndocwadocQueryService) NCLocator.getInstance().lookup(IPsndocwadocQueryService.class.getName());
	}

	private final String DOC_NAME = "Adjust";

	private SimpleDocServiceTemplate serviceTemplate;

	private WaAdjujstDAO waAdjujstDao;
	private IHrBillCode hrBillCode = null;
	
	
	/**
	 * ����Ա���� ��ȡԱ������
	 * 
	 * */
	public Object queryPsnPK(String PK_PSNJOB) throws BusinessException{
		String sql = "select pk_psndoc,assgid from hi_psnjob where pk_psnjob = '"+ PK_PSNJOB + "'";
	    SQLParameter parameter = null;
		return new BaseDAO().executeQuery(sql, parameter, /*new ColumnProcessor(1)*/new ResultSetProcessor() {
			@Override
			public Object handleResultSet(ResultSet arg0) throws SQLException {
				Object[] objs = new Object[2];
				while(arg0.next()){
					objs[0] = arg0.getString(1); 
					objs[1] = arg0.getInt(2);
				}
				return objs;
			}
		});
	}
	
	/**
	 * ������ɵĲ�������
	 *
	 * @return java.lang.String
	 * @param billPk
	 */
	public String approveBill(PFAggVO vo) throws BusinessException
	{
		AggPsnappaproveVO aggVO = new AggPsnappaproveVO();
		aggVO.setParentVO(vo.getParentVO());
		aggVO.setChildrenVO(vo.getChildrenVO());
		return noWorkflowApproveBill(aggVO, null, null, null, IBillStatus.CHECKPASS);
	}

	/**
	 * �������������е���Ϣ��ת��Ϊ��������Ϣά���ĸ�ʽ���ء�
	 *
	 * @author xuhw on 2010-1-7
	 * @param pvo
	 * @param bvos
	 * @return
	 * @throws BusinessException
	 */
	private PsndocWadocVO[] covertBillVO(PsnappaproveVO pvo, PsnappaproveBVO[] bvos) throws BusinessException
	{
		List<PsndocWadocVO> list = new ArrayList<PsndocWadocVO>();
		HashMap<String, PsndocWadocVO> map = new HashMap<String, PsndocWadocVO>();
		for (PsnappaproveBVO bvo : bvos)
		{
			if (bvo.getApproved().booleanValue())
			{
				PsndocWadocVO psndocWadocVO = new PsndocWadocVO();
				psndocWadocVO.setRecordnum(Integer.valueOf(0));
				psndocWadocVO.setWorkflowflag(UFBoolean.valueOf(true));
				psndocWadocVO.setIadjustmatter(Integer.valueOf(1));
				psndocWadocVO.setLastflag(UFBoolean.valueOf(true));
				if(bvo.getUsedate()!=null){
					psndocWadocVO.setBegindate(bvo.getUsedate());
				}else{
					psndocWadocVO.setBegindate(pvo.getUsedate());
				}
				if(psndocWadocVO.getBegindate() != null){
					psndocWadocVO.setChangedate(psndocWadocVO.getBegindate());
				}
				
				psndocWadocVO.setPk_changecause(bvo.getPk_changecause());
				psndocWadocVO.setVbasefile(pvo.getVbasefile());
				psndocWadocVO.setPk_wa_item(bvo.getPk_wa_item());
				psndocWadocVO.setPk_wa_grd(bvo.getPk_wa_grd());
				psndocWadocVO.setNmoney(bvo.getWa_cofm_money());
				psndocWadocVO.setPk_psndoc(bvo.getPk_psndoc());
				psndocWadocVO.setVnote(bvo.getVnote());
				psndocWadocVO.setNegotiation_wage(bvo.getNegotiation());
				psndocWadocVO.setPk_psnjob(bvo.getPk_psnjob());

				//��������Pk_wa_crt,����ȡ������ֵ
				psndocWadocVO.setPk_wa_crt(bvo.getPk_wa_crt());


				psndocWadocVO.setPk_wa_prmlv(bvo.getPk_wa_prmlv_cofm());
				psndocWadocVO.setPk_wa_seclv(bvo.getPk_wa_seclv_cofm());
				psndocWadocVO.setCriterionvalue(bvo.getWa_crt_cofm_money());
				psndocWadocVO.setPk_group(pvo.getPk_group());
				psndocWadocVO.setPk_org(pvo.getPk_org());
				psndocWadocVO.setAssgid(bvo.getAssgid());
				psndocWadocVO.setPartflag(bvo.getPartflag());
				String key = psndocWadocVO.getPk_psndoc() +psndocWadocVO.getAssgid()+ psndocWadocVO.getPk_wa_item();
				if (map.get(key) == null)
				{
					// �Զ����÷��ű�־
					map.put(key, psndocWadocVO);
					psndocWadocVO.setWaflag(UFBoolean.valueOf(true));
				}
				else
				{
					psndocWadocVO.setWaflag(UFBoolean.valueOf(false));
				}

				list.add(psndocWadocVO);
			}
		}
		if (list == null)
		{
			return null;
		}
		PsndocWadocVO[] vos = list.toArray(new PsndocWadocVO[list.size()]);

		return vos;
	}

	@Override
	public void delete(AggPsnappaproveVO vo) throws BusinessException
	{
		getServiceTemplate().delete(vo);
	}

	/**
	 * ������ֱ��
	 *
	 * @author xuhw on 2010-5-13
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	public PfProcessBatchRetObject directApprove(AggregatedValueObject[] billvos, String pk_user, UFDateTime serverTime, String approveNote,
			int directApproveResult) throws BusinessException
			{
		PfProcessBatchRetObject retObj = NCLocator.getInstance().lookup(IHrPf.class).directApprove(
				billvos, PubEnv.getPk_user(), PubEnv.getServerTime(),
				approveNote, directApproveResult,IWaAdjustManageService.class.getName(),"doApprove",AggPsnappaproveVO.class.getName());
		//		for(int i = 0;i<retObj.getRetObj().length;i++){
		//			doApprove((AggPsnappaproveVO)retObj.getRetObj()[i]);
		//		}
		return retObj;
			}

	/**
	 * ������ֱ��
	 *
	 * @author xuhw on 2010-5-13
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	public AggPsnappaproveVO doRecall(AggPsnappaproveVO vo) throws BusinessException {
		PsnappaproveVO psnappaproveVO = (PsnappaproveVO) vo.getParentVO();
		//		psnappaproveVO.setApplydate(null);
		PsnappaproveBVO[] psnappaproveBVOs  = (PsnappaproveBVO[])vo.getChildrenVO();
		for(PsnappaproveBVO psnappaproveBVO:psnappaproveBVOs){
			psnappaproveBVO.setWa_crt_cofm_money(null);
			psnappaproveBVO.setPk_wa_crt_cofm_showname(null);
			psnappaproveBVO.setPk_wa_seclv_cofm(null);
			psnappaproveBVO.setPk_wa_prmlv_cofm(null);
			psnappaproveBVO.setWa_cofm_money(null);
			psnappaproveBVO.setStatus(VOStatus.UPDATED);
		}
		psnappaproveVO.setConfirmdate(null);
		psnappaproveVO.setConfirmstate(IPfRetCheckInfo.NOSTATE);
		return getServiceTemplate().update(vo,true);
	}
	/**
	 * ������ɵĲ�������
	 *
	 * @return java.lang.String
	 * @param billPk
	 *            java.lang.String
	 */
	public AggPsnappaproveVO doApprove(AggPsnappaproveVO aggVO) throws BusinessException
	{

		PsnappaproveVO mainvo = (PsnappaproveVO) aggVO.getParentVO();
		return queryPsnappaproveVOByPk(noWorkflowApproveBill(aggVO, null, null, null, mainvo.getConfirmstate().intValue()));
	}

	/**
	 * ������ֱ�������Ƿ񲵻��������Ƿ���ά���ڵ��������
	 *
	 * @author suihang on 2011-5-13
	 * @param aggVO
	 * @param psnappaproveVO
	 * @return
	 * @throws BusinessException
	 */
	public AggPsnappaproveVO directApproveAndUpdate(AggPsnappaproveVO aggVO,PsnappaproveVO psnappaproveVO) throws BusinessException
	{
		//		if(psnappaproveVO.getConfirmstate() != -1){
		//			return directApprove(aggVO);
		//		}
		return updateApproveDate(psnappaproveVO);

	}
	/**
	 * �ύ����ҵ������
	 *
	 * @author xuhw on 2010-1-6
	 * @param psnappaproveAggVO
	 * @throws BusinessException
	 */
	public AggPsnappaproveVO doSubmit(AggPsnappaproveVO psnappaproveAggVO) throws BusinessException
	{
		// ��Ч����У��
		PsnappaproveVO psnappaproveVO = (PsnappaproveVO) psnappaproveAggVO.getParentVO();

		// ����Ա�Ǳ��ƹ����ĵ��� �������˺ʹ����˶���NCϵͳ�û�������Ҫ����Ϊ��ǰ��½�û�
		// �Ƴ����ݵ�������Ϊ��ϵͳ�û�������ҵ������ڵ��ѯʱ���ѯ��½�û���ϵͳ
		// �û��ĵ��ݣ��ύǰ��ϵͳ�û���Ϊ��½�û���
		// NCϵͳ�û���INCSystemUserConst.NC_USER_PK
		/*		if (INCSystemUserConst.NC_USER_PK.equals(psnappaproveVO.getApprover()) || psnappaproveVO.getApprover() == null)
		{
			psnappaproveVO.setApprover(PubEnv.getPk_user());
		}*/
		/*		if (INCSystemUserConst.NC_USER_PK.equals(psnappaproveVO.getOperator()) || psnappaproveVO.getOperator() == null)
		{
			psnappaproveVO.setOperator(PubEnv.getPk_user());
		}*/
		if (INCSystemUserConst.NC_USER_PK.equals(psnappaproveVO.getCreator()) || psnappaproveVO.getCreator() == null)
		{
			psnappaproveVO.setCreator(PubEnv.getPk_user());
		}

		//		validateUsedate(psnappaproveVO.getUsedate());
		psnappaproveVO.setConfirmdate(null);// TODO ������������ģ��ύʱ�͸�����ȷ�����ڣ���������ô�����
		psnappaproveVO.setConfirmstate(IPfRetCheckInfo.COMMIT);

		//		getAdjustDao().getBaseDao().updateVO(psnappaproveVO);
		//		AuditInfoUtil.updateData(psnappaproveVO);
		getServiceTemplate().update(psnappaproveVO,false);

		PsnappaproveBVO[] psnappaproveBVOs = (PsnappaproveBVO[]) psnappaproveAggVO.getChildrenVO();
		// �����Щ���صĵ��ݣ�Ҫ���¸���������־��Ĭ��Ϊ��ѡ
		UFBoolean  partflagShow= WaAdjustParaTool.getPartjob_Adjmgt(psnappaproveVO.getPk_group());
		int rownum = 0;
		for (PsnappaproveBVO psnappaproveBVO : psnappaproveBVOs)
		{
			rownum++;
			psnappaproveBVO.setApproved(UFBoolean.valueOf(true));
			validateUsedate(psnappaproveBVO, partflagShow, rownum);
		}
		AuditInfoUtil.updateData(psnappaproveBVOs);
		getAdjustDao().getBaseDao().updateVOArray(psnappaproveBVOs);
		//		List<AggPsnappaproveVO> list = new ArrayList<AggPsnappaproveVO>();

		return queryPsnappaproveVOByPk(psnappaproveAggVO.getParentVO().getPrimaryKey());
	}

	/**
	 * �ܾ�������ʹ��������
	 *
	 * @param billPk
	 */
	public AggPsnappaproveVO doUnapprove(AggPsnappaproveVO aggVO) throws BusinessException
	{
		PsnappaproveVO parentVO = (PsnappaproveVO) aggVO.getParentVO();
		AggPsnappaproveVO oldaggvos = getServiceTemplate().queryByPk(
				AggPsnappaproveVO.class, parentVO.getPk_psnapp());

		checkPFPassingState(((PsnappaproveVO) oldaggvos.getParentVO())
				.getConfirmstate());
		return update(aggVO);
		//		String strPKPsnapp = ((PsnappaproveVO) aggVO.getParentVO()).getPk_psnapp();
		//		boolean blnEdit = this.getAdjustDao().isPsnAppBByPkForUnapprove(strPKPsnapp);
		//		if (blnEdit)
		//		{
		//			// ������ص���д���򽫰���������ϢҲɾ����
		//			this.getAdjustDao().updatePsnAppAprove(strPKPsnapp);
		//			this.getAdjustDao().updatePsnAppBByPkForUnapprove(strPKPsnapp);
		//		}

		//		return queryByPk(strPKPsnapp);
	}

	public void checkPFPassingState(int pfsate) throws BusinessException {
		//guoqt�������ڵ������������nc.ui.hr.pf.action.PFUnApproveAction��doAction()��������������Ϣ���ĵĹ������������߸÷���������Ҫͬʱ�жϵ�������ͨ�����������������������ȡ������
		if (IPfRetCheckInfo.PASSING == pfsate||IPfRetCheckInfo.NOPASS == pfsate) {
			throw new BusinessException(ResHelper.getString("6001pf", "06001pf0059")
	                /* @res "��������ͨ����δͨ��,����ȡ������." */);
		}
	}
	/**
	 * �õ�ԭ�еĺ�Ĭ�ϵ�н����Ϣ
	 */
	public PsnappaproveBVO[] findOldAndApplayInfo(PsnappaproveBVO[] psnappaproveBVOs, boolean OldAndApplay) throws BusinessException
	{

		getOldInfos(psnappaproveBVOs,OldAndApplay);

		if (OldAndApplay)
		{
			// Ĭ������ĵ��𼶱�
			getAdjustDao().getDefultApplys(psnappaproveBVOs);
		}

		return psnappaproveBVOs;
	}

	/**
	 * �õ�ԭ�еĺ�Ĭ�ϵ�н����Ϣ
	 */
	/*	public PsnappaproveBVO[] findOldAndApplayInfo4Psn(PsnappaproveBVO[] psnappaproveBVOs, boolean OldAndApplay) throws BusinessException
	{
		getOldInfos(psnappaproveBVOs,OldAndApplay);

		if (OldAndApplay)
		{
			// Ĭ������ĵ��𼶱�
			getAdjustDao().getDefultApplys4Psn(psnappaproveBVOs);
		}
		return psnappaproveBVOs;
	}*/

	private WaAdjujstDAO getAdjustDao()
	{
		if (waAdjujstDao == null)
		{
			waAdjujstDao = new WaAdjujstDAO();
		}

		return waAdjujstDao;
	}

	@Override
	public String getCriterion(String pk_wa_grd, String pk_psndoc) throws BusinessException
	{
		return new WaPsnHiDAO().getCriterion(pk_wa_grd, pk_psndoc);
	}

	@Override
	public String getCriterionForCaculate(String pk_wa_grd) throws BusinessException
	{
		return new WaPsnHiDAO().getCriterionOnly4Caculate(pk_wa_grd);
	}

	/**
	 * ȡ��ԭʼ��н����Ϣ
	 *
	 * @author xuhw on 2009-12-22
	 * @param psnappaproveBVO
	 * @param OldAndApplay
	 * @return
	 * @throws BusinessException
	 */
	/*	private PsnappaproveBVO getOldInfo(PsnappaproveBVO psnappaproveBVO, boolean OldAndApplay) throws BusinessException
	{
		String pk_wa_item = psnappaproveBVO.getPk_wa_item();
		String pk_wa_grd = psnappaproveBVO.getPk_wa_grd();
		String pk_psndoc = psnappaproveBVO.getPk_psndoc();
		if (pk_psndoc != null && pk_wa_grd != null && pk_wa_item != null)
		{
			boolean negotiation_wage = false;
			IPsndocwadocQueryService psndocwadocQueryService = lookupPsndocwadocQueryService();
			PsndocWadocVO psndocWadocVO = psndocwadocQueryService.queryAllVOsByPsnPK(pk_psndoc, pk_wa_grd, pk_wa_item);
			if (psndocWadocVO != null)
			{
				negotiation_wage = psndocWadocVO.getNegotiation_wage().booleanValue();
				psnappaproveBVO.setWa_old_money(psndocWadocVO.getNmoney());

				if (!StringUtils.isEmpty(psndocWadocVO.getPk_wa_prmlv()))
				{
//					String[] oldCrtInfos = getAdjustDao().queryVOByCrtPK(psndocWadocVO.getPk_wa_crt());
					psnappaproveBVO.setWa_crt_old_money(psndocWadocVO.getCriterionvalue());
//					psnappaproveBVO.setPk_wa_crt_old(psndocWadocVO.getPk_wa_crt());
					psnappaproveBVO.setPk_wa_prmlv_old(psndocWadocVO.getPk_wa_prmlv());
					psnappaproveBVO.setPk_wa_seclv_old(psndocWadocVO.getPk_wa_seclv());
					psnappaproveBVO.setPk_wa_crt_old_showname(getAdjustDao().getCrtName(psndocWadocVO.getPk_wa_prmlv(),psndocWadocVO.getPk_wa_seclv(), psndocWadocVO.getIsmultsec().booleanValue()));
				}
			}
			if (OldAndApplay)
			{
				// �����޸ĵ�ʱ�� ������ļ���
				// Ĭ������ͬԭ����״̬һ��
				psnappaproveBVO.setNegotiation(UFBoolean.valueOf(negotiation_wage));
			}
			else
			{
				if (psnappaproveBVO.getNegotiation() == null)
				{
					psnappaproveBVO.setNegotiation(UFBoolean.valueOf(false));
				}
			}
		}
		return psnappaproveBVO;
	}
	 */
	/**
	 * ȡ��ԭʼ��н����Ϣ
	 *
	 * @author suihang on 2011-8-22
	 * @param psnappaproveBVO
	 * @param OldAndApplay
	 * @return
	 * @throws BusinessException
	 */
	private PsnappaproveBVO[] getOldInfos(PsnappaproveBVO[] psnappaproveBVOs, boolean OldAndApplay) throws BusinessException
	{
		String pk_wa_item = psnappaproveBVOs[0].getPk_wa_item();
		String pk_wa_grd = psnappaproveBVOs[0].getPk_wa_grd();
		String[] pk_psndocs = new String[psnappaproveBVOs.length];

		if (ArrayUtils.isEmpty(psnappaproveBVOs)){
			return psnappaproveBVOs;
		}
		int i=0;
		for(PsnappaproveBVO psnappaproveBVO:psnappaproveBVOs){
			pk_psndocs[i++] = psnappaproveBVO.getPk_psndoc();
		}

		if (!ArrayUtils.isEmpty(pk_psndocs) && pk_wa_grd != null && pk_wa_item != null)
		{
			boolean negotiation_wage = false;
			IPsndocwadocQueryService psndocwadocQueryService = lookupPsndocwadocQueryService();
			PsndocWadocVO[] psndocWadocVOs = psndocwadocQueryService.queryAllVOsByPsnPKs(pk_psndocs, pk_wa_grd, pk_wa_item);
			HashMap<String,PsndocWadocVO> psndocWadocVOMap = new HashMap<String,PsndocWadocVO>();
			if (ArrayUtils.isEmpty(psndocWadocVOs)){
				return psnappaproveBVOs;
			}
			for(PsnappaproveBVO psnappaproveBVO:psnappaproveBVOs){
				for(PsndocWadocVO psndocWadocVO:psndocWadocVOs){
					if(psnappaproveBVO.getPk_psndoc().equals(psndocWadocVO.getPk_psndoc())&&psnappaproveBVO.getAssgid().equals(psndocWadocVO.getAssgid())){
						psndocWadocVOMap.put(psnappaproveBVO.getPk_psndoc()+psnappaproveBVO.getAssgid(), psndocWadocVO);
					}
				}
			}

			HashMap<String, String> namemap = null;
			namemap = getAdjustDao().getCrtName(psnappaproveBVOs,psndocWadocVOs);//(psndocWadocVO.getPk_wa_prmlv(),psndocWadocVO.getPk_wa_seclv(), psndocWadocVO.getIsmultsec().booleanValue())
			for(PsnappaproveBVO psnappaproveBVO:psnappaproveBVOs){
				PsndocWadocVO psndocWadocVO = psndocWadocVOMap.get(psnappaproveBVO.getPk_psndoc()+psnappaproveBVO.getAssgid());
				if(psndocWadocVO==null){
					psnappaproveBVO.setNegotiation(UFBoolean.valueOf(false));
					continue;
				}
				negotiation_wage = psndocWadocVO.getNegotiation_wage().booleanValue();
				if (OldAndApplay)
				{
					// �����޸ĵ�ʱ�� ������ļ���
					// Ĭ������ͬԭ����״̬һ��
					psnappaproveBVO.setNegotiation(UFBoolean.valueOf(negotiation_wage));
				}
				else
				{
					if (psnappaproveBVO.getNegotiation() == null)
					{
						psnappaproveBVO.setNegotiation(UFBoolean.valueOf(false));
					}
				}
				psnappaproveBVO.setWa_old_money(psndocWadocVO.getNmoney());
				if (!StringUtils.isEmpty(psndocWadocVO.getPk_wa_prmlv()))
				{
					psnappaproveBVO.setWa_crt_old_money(psndocWadocVO.getCriterionvalue());
					psnappaproveBVO.setPk_wa_prmlv_old(psndocWadocVO.getPk_wa_prmlv());
					psnappaproveBVO.setPk_wa_seclv_old(psndocWadocVO.getPk_wa_seclv());
					String showname = null;
					if(psndocWadocVO.getIsmultsec().booleanValue()){
						showname = namemap.get(psndocWadocVO.getPk_wa_prmlv()+psndocWadocVO.getPk_wa_seclv());
					}else{
						showname = namemap.get(psndocWadocVO.getPk_wa_prmlv());
					}
					psnappaproveBVO.setPk_wa_crt_old_showname(showname);
				}
			}
			psndocWadocVOMap.clear();

		}
		return psnappaproveBVOs;
	}

	private SimpleDocServiceTemplate getServiceTemplate()
	{
		if (serviceTemplate == null)
		{
			serviceTemplate = new SimpleDocServiceTemplate(DOC_NAME);
		}

		return serviceTemplate;
	}

	@Override
	public AggPsnappaproveVO insert(AggPsnappaproveVO vo) throws BusinessException
	{
		PsnappaproveVO ppprove = ((PsnappaproveVO) vo.getParentVO());
		// �����ͬ����֯�Ͷ���������� �����ظ����������
		boolean blnIsUse = this.getAdjustDao().checkBillCodeUseable(ppprove.getBillcode(), ppprove.getPk_org());
		if (blnIsUse)
		{
			Logger.debug("���ݱ����ظ��� ����ʹ�øñ��룡");
			throw new BusinessException(ResHelper.getString("60130adjapprove","060130adjapprove0118",ppprove.getBillcode())/*@res "�����ֶ�ֵ�Ѵ��ڣ��������ظ������飺\n[���뵥���룺{0}]"*/);
		}
		Object returnObj = getServiceTemplate().insert(clone(vo));
		//		String parentPk = getAdjustDao().getBaseDao().insertVO(ppprove);
		//		PsnappaproveBVO[] psnappaproveBVOs = (PsnappaproveBVO[]) vo.getChildrenVO();
		//		for (PsnappaproveBVO psnappaproveBVO : psnappaproveBVOs)
		//        {
		//            psnappaproveBVO.setPk_psnapp(parentPk);
		//        }
		//        getAdjustDao().getBaseDao().insertVOArray(psnappaproveBVOs);
		//		BillCodeContext billCodeContext = NCLocator.getInstance().lookup(IBillcodeManage.class).getBillCodeContext("6301", ppprove.getPk_group(), ppprove.getPk_org());
		//		// ������Զ����ɱ��룬����Ҫ�ύ
		//		if (billCodeContext != null)
		//		{
		//			getHrBillCode().commitPreBillCode("6301", ppprove.getPk_group(), ppprove.getPk_org(), ppprove.getBillcode());
		//		}
		return (AggPsnappaproveVO) returnObj;
	}

	/**
	 * Ϊҵ����д�ķ���
	 *
	 * @param psnappaproveAggVO
	 * @throws BusinessException
	 */
	public String doSubmit4Psn(AggPsnappaproveVO[] psnappaproveAggVOsFPsn) throws BusinessException{
		AggPsnappaproveVO[] psnappaproveAggVOs = BTOBXVOConversionUtility.retChangeBusiVO(psnappaproveAggVOsFPsn);
		if(ArrayUtils.isEmpty(psnappaproveAggVOs))
			return null;
		PsnappaproveVO[] parentVOs = SuperVOHelper.getParentVOArrayFromAggVOs(psnappaproveAggVOs, PsnappaproveVO.class);
		String[] pks = getAdjustDao().getBaseDao().insertVOArray(parentVOs);
		List<PsnappaproveBVO> subVOList = new ArrayList<PsnappaproveBVO>();
		List<HrBillRefSupVO> billRefList = new ArrayList<HrBillRefSupVO>();
		for(int i = 0; i < psnappaproveAggVOs.length;i++) {
			PsnappaproveBVO[] subVOs = (PsnappaproveBVO[]) psnappaproveAggVOs[i].getChildrenVO();
			// �ҵ�ԭʼ�ļ��𵵱� ��Ĭ�ϵ����뼶�𵵱�(TODO �˴���Ҫ�����Ż���������)
			subVOs = findOldAndApplayInfo(subVOs,true);
			if(ArrayUtils.isEmpty(subVOs))
				continue;
			for(PsnappaproveBVO subVO:subVOs){
				subVO.setPk_psnapp(pks[i]);
				subVOList.add(subVO);
			}
			HrBillRefSupVO refVO = parentVOs[i].getRefVO();
			// �鿴������Դʱ����
			if (refVO == null)
				continue;
			refVO.setDest_pk(pks[i]);
			//ͬһ����������Ӷ�Σ�����������ͻ heqiaoa 20150421
			if(!billRefList.contains(refVO)){
				billRefList.add(refVO);
			}
//			billRefList.add(refVO);
		}
		// ����
		if(CollectionUtils.isNotEmpty(subVOList))
			getAdjustDao().getBaseDao().insertVOArray(subVOList.toArray(new PsnappaproveBVO[0]));
		if(CollectionUtils.isNotEmpty(billRefList))
			getAdjustDao().getBaseDao().insertVOArray(billRefList.toArray(new HrBillRefSupVO[0]));
		return null;
	}

	//	public String doSubmit4Psn(AggPsnappaproveVO[] psnappaproveAggVOsFPsn) throws BusinessException
	//	{
	//		if (ArrayUtils.isEmpty(psnappaproveAggVOsFPsn))
	//		{
	//			return null;
	//		}
	//
	//		for (AggPsnappaproveVO psnappaproveAggVO : psnappaproveAggVOsFPsn)
	//		{
	//			AggPsnappaproveVO[] psnappaproveAggVOs = BTOBXVOConversionUtility.retChangeBusiVO(psnappaproveAggVO);
	//
	//			if (ArrayUtils.isEmpty(psnappaproveAggVOs))
	//			{
	//				return null;
	//			}
	//			for (AggPsnappaproveVO aggVO : psnappaproveAggVOs)
	//			{
	//				PsnappaproveVO psnappaproveVO = (PsnappaproveVO) aggVO.getParentVO();
	//				PsnappaproveBVO[] psnappaproveBVOs = (PsnappaproveBVO[]) aggVO.getChildrenVO();
	//				String pk_psnapp = null;
	//				// �ҵ�ԭʼ�ļ��𵵱� ��Ĭ�ϵ����뼶�𵵱�
	//				psnappaproveBVOs = findOldAndApplayInfo(psnappaproveBVOs,true);
	//				if (psnappaproveBVOs != null && psnappaproveBVOs.length > 0)
	//				{
	//					pk_psnapp = getAdjustDao().getBaseDao().insertVO(psnappaproveVO);
	//					for (PsnappaproveBVO psnappaproveBVO : psnappaproveBVOs)
	//					{
	//						psnappaproveBVO.setPk_psnapp(pk_psnapp);
	//					}
	//					getAdjustDao().getBaseDao().insertVOArray(psnappaproveBVOs);
	//					HrBillRefSupVO refVO = psnappaproveVO.getRefVO();
	//					// �鿴������Դʱ����
	//					if (refVO != null)
	//					{
	//						refVO.setDest_pk(pk_psnapp);
	//						getAdjustDao().getBaseDao().insertVO(refVO);
	//					}
	//				}
	//			}
	//		}
	//		return null;
	//	}

	@Override
	public String noWorkflowApproveBill(AggPsnappaproveVO vo, String oper, String opinion, UFDateTime dt, int blApproved) throws BusinessException
	{
		// �趨Ĭ��ֵ
		saveDefaultConfirmValue(vo);
		PsnappaproveVO psnappaproveVO = (PsnappaproveVO) vo.getParentVO();
		// ��Ҫ�ж������ȼ��Ƿ�Ϊ�գ���-->���ݿ������
		String billpk = psnappaproveVO.getPk_psnapp();
		if (blApproved == IBillStatus.CHECKPASS)
		{
			// ȷ����Ч���ڵ�����
			/*			String[] period = this.getAdjustDao().getPeriod(psnappaproveVO.getUsedate().toString());
			if (period == null || period[0] == null || period[1] == null)
			{
				throw new BusinessException(ResHelper.getString("60130adjapprove","060130adjapprove0119")@res "��Ч����û�ж�Ӧ��н���ڼ�");
			}*/
			//			validateUsedate(psnappaproveVO.getUsedate());
			PsnappaproveBVO[] bvos = (PsnappaproveBVO[]) vo.getChildrenVO();
			UFBoolean  partflagShow= WaAdjustParaTool.getPartjob_Adjmgt(psnappaproveVO.getPk_group());
			for(int i=0;i<bvos.length;i++){
				if(bvos[i].getWa_cofm_money() == null){
					bvos[i].setWa_cofm_money(bvos[i].getWa_apply_money());
				}
				if(bvos[i].getWa_crt_cofm_money() == null){
					bvos[i].setWa_crt_cofm_money(bvos[i].getWa_crt_apply_money());
				}
				validateUsedate(bvos[i], partflagShow, i + 1);
			}

			PsndocWadocVO[] wvos = covertBillVO(psnappaproveVO, bvos);
			this.getAdjustDao().insertArray(wvos);

			//ͬ��hi���ݡ�����
			BTOBXVOConversionUtility.psndocWadocSaveToWainfoVO(wvos);

		}
		AuditInfoUtil.updateData(psnappaproveVO);
		getAdjustDao().updateApproveDate(psnappaproveVO);
		return billpk;
	}

	private void   saveDefaultConfirmValue(AggregatedValueObject hrAggVO) throws BusinessException{

		//�趨��������
		PsnappaproveVO  vo = 	(PsnappaproveVO)hrAggVO.getParentVO();
		vo.setAttributeValue(PsnappaproveVO.CONFIRMDATE, PubEnv.getServerTime());
		CircularlyAccessibleValueObject[] psnappaproveBVOs =  hrAggVO.getChildrenVO();
		vo.setStatus(VOStatus.UPDATED);

		//Ϊ������¼�趨  PsnappaproveBVO.PK_WA_PRMLV_COFM��PsnappaproveBVO.PK_WA_SECLV_COFM PsnappaproveBVO.WA_PRMLV_COFM��WA_SECLV_COFM WA_COFM_MONEY WA_CRT_COFM_MONEY
		for (int i = 0; i < psnappaproveBVOs.length; i++) {
			PsnappaproveBVO psnappaproveBVO = (PsnappaproveBVO) psnappaproveBVOs[i];
			if(psnappaproveBVO.getWa_cofm_money()==null){
				//Ĭ������ͨ��
				psnappaproveBVOs[i].setAttributeValue(PsnappaproveBVO.APPROVED,UFBoolean.TRUE);
				psnappaproveBVOs[i].setAttributeValue(PsnappaproveBVO.PK_WA_PRMLV_COFM, psnappaproveBVOs[i].getAttributeValue(PsnappaproveBVO.PK_WA_PRMLV_APPLY));
				psnappaproveBVOs[i].setAttributeValue(PsnappaproveBVO.PK_WA_SECLV_COFM, psnappaproveBVOs[i].getAttributeValue(PsnappaproveBVO.PK_WA_SECLV_APPLY));
				psnappaproveBVOs[i].setAttributeValue(PsnappaproveBVO.WA_COFM_MONEY, psnappaproveBVOs[i].getAttributeValue(PsnappaproveBVO.WA_APPLY_MONEY));
				psnappaproveBVOs[i].setAttributeValue(PsnappaproveBVO.WA_CRT_COFM_MONEY, psnappaproveBVOs[i].getAttributeValue(PsnappaproveBVO.WA_CRT_APPLY_MONEY));
				psnappaproveBVOs[i].setStatus(VOStatus.UPDATED);
			}


		}
		getServiceTemplate().update(hrAggVO,true);

	}

	/**
	 * ����������Ϣ
	 *
	 * @author xuhw on 2010-4-28
	 * @param psnappaprovervo
	 * @throws BusinessException
	 */
	public AggPsnappaproveVO updateApproveDate(PsnappaproveVO psnappaprovervo) throws BusinessException
	{
		AuditInfoUtil.updateData(psnappaprovervo);
		getAdjustDao().updateApproveDate(psnappaprovervo);
		return queryPsnappaproveVOByPk(psnappaprovervo.getPk_psnapp());
	}

	public AggPsnappaproveVO[] queryPsnappsByCondition(LoginContext context, String condition, PFQueryParams queryParams,Boolean isApprove) throws BusinessException
	{
		//		if (queryParams == null) {
		//			return new AggPsnappaproveVO[0];
		//		}
		String orderby = " wa_psnappaprove.confirmstate asc, wa_psnappaprove.billcode";

		if(null!=queryParams){

			IFlowBizItf itf = HrPfHelper.getFlowBizItf(AggPsnappaproveVO.class);

			String strApproveDatePeriod = HrPfHelper.getApproveDatePeriod(itf, "wa_psnappaprove", queryParams.getApproveDateParam(),queryParams.getBillState());


			if (StringUtils.isNotBlank(strApproveDatePeriod))
			{
				condition += " and " + strApproveDatePeriod;
			}
		}
		if(null!=condition){
			condition = condition.replace("select pk_psnapp from wa_psnappaprove_b", "select wa_psnappaprove_b.pk_psnapp from wa_psnappaprove_b");
		}

		return getAdjustDao().querySumApplyAndSumConfimMoney(getServiceTemplate().queryByCondition(context, AggPsnappaproveVO.class, condition, orderby));
	}



	@Override
	public AggPsnappaproveVO queryPsnappaproveVOByPk(String pk) throws BusinessException
	{
		AggPsnappaproveVO vo = getServiceTemplate().queryByPk(AggPsnappaproveVO.class, pk);
		AggPsnappaproveVO[] vos = new AggPsnappaproveVO[1];
		vos[0]= vo;
		vos = getAdjustDao().querySumApplyAndSumConfimMoney(vos);
		return vos[0];
	}


	public AggPsnappaproveVO queryByPkWithName(String pk) throws BusinessException{
		//��ѯ��ͷ
		BaseDAOManager manager =  new BaseDAOManager();
		PsnappaproveVO headvo = manager.retrieveByPK(PsnappaproveVO.class, pk);

		//��ѯ���� ������
		PsnappaproveBVO[] vos =  getAdjustDao().queryPsnappaproveByHeaderPK(pk);
		AggPsnappaproveVO  aggPsnappaproveVO = new AggPsnappaproveVO();

		aggPsnappaproveVO.setParentVO(headvo);
		aggPsnappaproveVO.setChildrenVO(vos);


		return aggPsnappaproveVO;
	}

	/**
	 * �������� �ڶ��ַ�ʽ ��Ա ��ѯ�Ի���
	 */
	public PsnappaproveBVO[] queryPsnappaproveBVOForAdd(String conditions, LoginContext context) throws BusinessException
	{
		return this.getAdjustDao().queryPsnappaproveBVOForadd(conditions, context);
	}

	public PsnappaproveVO[] queryPsnappaproveByCon(String strWhere, String strTable) throws BusinessException
	{
		return getAdjustDao().queryByCon(strWhere, strTable);
	}

	/**
	 * �������Ӹ��ݲ���PK��ѯ ��һ�ַ�ʽ
	 */
	public PsnappaproveBVO[] queryPsnappaproveByDepts(String[] pk_deptdocs, LoginContext context) throws BusinessException
	{
		return this.getAdjustDao().queryPsnappaproveByDepts(pk_deptdocs, context);
	}

	/**
	 * �������� ��ѡ��Ա��ѯ �����ַ�ʽ
	 *
	 * @param strPKdeptdocs
	 * @return PsnappaproveBVO[]
	 * @throws DAOException
	 */
	//	public PsnappaproveBVO[] queryPsnappaproveByPsnPks(String[] strPKpsndoc, LoginContext context) throws BusinessException
	//	{
	//		return this.getAdjustDao().queryPsnappaproveByPsnPks(strPKpsndoc, context);
	//	}

	public PsnappaproveBVO[] queryPsnappaproveByPsnjob(String[] strPKPsnjob, LoginContext context) throws BusinessException
	{
		return this.getAdjustDao().queryPsnappaproveByPKpsnjob(strPKPsnjob, context);
	}


	/**
	 * ����pk��ѯн�ʱ�׼���
	 *
	 * @author xuhw on 2009-12-22
	 * @param strPK
	 * @throws BusinessException
	 */
	public WaGradeVO queryWagradeVoByGradePk(String strPK) throws BusinessException
	{
		return this.getServiceTemplate().queryByPk(WaGradeVO.class, strPK);
	}

	public void setValidatorFactory(IValidatorFactory docValidatorFactory)
	{
		getServiceTemplate().setValidatorFactory(docValidatorFactory);
	}

	public AggPsnappaproveVO update(AggPsnappaproveVO vo) throws BusinessException
	{
		//20151022 shenliangc NCdp205487551 ���������뵥�޸ı������Ψһ��У�顣 begin
		PsnappaproveVO ppprove = ((PsnappaproveVO) vo.getParentVO());
		// �����ͬ����֯�Ͷ���������� �����ظ����������
		boolean blnIsUse = this.getAdjustDao().checkBillCodeUseable(ppprove.getPk_psnapp(), ppprove.getBillcode(), ppprove.getPk_org());
		if (blnIsUse)
		{
			Logger.debug("���ݱ����ظ��� ����ʹ�øñ��룡");
			throw new BusinessException(ResHelper.getString("60130adjapprove","060130adjapprove0118",ppprove.getBillcode())/*@res "�����ֶ�ֵ�Ѵ��ڣ��������ظ������飺\n[���뵥���룺{0}]"*/);
		}
		//20151022 shenliangc NCdp205487551 ���������뵥�޸ı������Ψһ��У�顣 end
		
		UFDouble sum_confim_money=new UFDouble(0);
		UFDouble sum_apply_money=new UFDouble(0);

		for (int i = 0; null != vo.getChildrenVO() && i < vo.getChildrenVO().length; i++)
		{
			PsnappaproveBVO psnappaproveBVO = ((PsnappaproveBVO) vo.getChildrenVO()[i]);
			//���������ܽ��
			//20151113 shenliangc NCdp205541307 ���������뵥���޸��ӱ�һ����Ա�滻��һ����Ա�󣬱��棬��ɾ�������汨δ֪����
			if(psnappaproveBVO.getWa_apply_money()!=null){
			sum_apply_money = sum_apply_money.add(psnappaproveBVO.getWa_apply_money());
			}
			//���������ܽ��
			if(psnappaproveBVO.getApproved().toString().equalsIgnoreCase("Y")){
		    	if(psnappaproveBVO.getWa_cofm_money()!=null){
		    		sum_confim_money = sum_confim_money.add(psnappaproveBVO.getWa_cofm_money());
		    	}
			}
		}
		((PsnappaproveVO) vo.getParentVO()).setSum_apply_money(sum_apply_money);
		((PsnappaproveVO) vo.getParentVO()).setSum_confim_money(sum_confim_money);
//		return    getServiceTemplate().update(vo,true);
//----------------------------------------------------------------------------------
//		20151119  xiejie3  NCdp205545197  ��ʱ��������begin
		 getServiceTemplate().update(vo,true);
		
		AggPsnappaproveVO    voNew=queryPsnappaproveVOByPk(vo.getParentVO().getPrimaryKey());
		
		UFDouble sum_confim_moneyNew=new UFDouble(0);
		UFDouble sum_apply_moneyNew=new UFDouble(0);
		for (int i = 0; null != voNew.getChildrenVO() && i < voNew.getChildrenVO().length; i++)
		{
			PsnappaproveBVO psnappaproveBVO = ((PsnappaproveBVO) voNew.getChildrenVO()[i]);
			//���������ܽ��
			//20151113 shenliangc NCdp205541307 ���������뵥���޸��ӱ�һ����Ա�滻��һ����Ա�󣬱��棬��ɾ�������汨δ֪����
			
			if(psnappaproveBVO.getWa_apply_money()!=null){
				sum_apply_moneyNew = sum_apply_moneyNew.add(psnappaproveBVO.getWa_apply_money());
			}
			if(psnappaproveBVO.getApproved().toString().equalsIgnoreCase("Y")){
		     	//���������ܽ��
		    	if(psnappaproveBVO.getWa_cofm_money()!=null){
					sum_confim_moneyNew = sum_confim_moneyNew.add(psnappaproveBVO.getWa_cofm_money());
				}
			}
		}
		((PsnappaproveVO) voNew.getParentVO()).setSum_apply_money(sum_apply_moneyNew);
		((PsnappaproveVO) voNew.getParentVO()).setSum_confim_money(sum_confim_moneyNew);
		return    getServiceTemplate().update(voNew,true);
//		end
	}

	/**
	 * ����
	 */
	public Object copyTable(AggPsnappaproveVO vo) throws BusinessException
	{
		PsnappaproveVO approve = (PsnappaproveVO) vo.getParentVO();
		// �����ͬ����֯�Ͷ���������� �����ظ����������
		boolean blnIsUse = this.getAdjustDao().checkBillCodeUseable(approve.getBillcode(), approve.getPk_org());
		if (blnIsUse)
		{
			Logger.debug("���ݱ����ظ��� ����ʹ�øñ��룡");
			throw new BusinessException(ResHelper.getString("60130adjapprove","060130adjapprove0118",approve.getBillcode())/*@res "�����ֶ�ֵ�Ѵ��ڣ��������ظ������飺\n[���뵥���룺{0}]"*/);
		}
		Object returnObj = getServiceTemplate().insert(clone(vo));
		BillCodeContext billCodeContext = NCLocator.getInstance().lookup(IBillcodeManage.class).getBillCodeContext("6301", approve.getPk_group(), approve.getPk_org());
		// ������Զ����ɱ��룬����Ҫ�ύ
		if (billCodeContext != null)
		{
			getHrBillCode().commitPreBillCode("6301", approve.getPk_group(), approve.getPk_org(), approve.getBillcode());
		}
		return returnObj;
	}

	private IHrBillCode getHrBillCode()
	{
		if (hrBillCode == null)
		{
			hrBillCode = NCLocator.getInstance().lookup(IHrBillCode.class);
		}
		return hrBillCode;
	}

	/**
	 * ����һ���ۺ�VO
	 *
	 * @throws BusinessException
	 */
	private AggPsnappaproveVO clone(AggPsnappaproveVO billVO) throws BusinessException
	{

		UFDouble sum_apply_money=new UFDouble(0);

		// ���������޸�״̬
		billVO.getParentVO().setStatus(VOStatus.NEW);
		// ������������
		billVO.getParentVO().setPrimaryKey(null);
		// �����ӱ�����
		for (int i = 0; i < billVO.getChildrenVO().length; i++)
		{
			PsnappaproveBVO psnappaproveBVO = ((PsnappaproveBVO) billVO.getChildrenVO()[i]);
			psnappaproveBVO.setPk_psnapp_b(null);
			psnappaproveBVO.setPk_psnapp(null);
			psnappaproveBVO.setStatus(VOStatus.NEW);
			//���������ܽ��
			sum_apply_money = sum_apply_money.add(psnappaproveBVO.getWa_apply_money());
		}
		((PsnappaproveVO) billVO.getParentVO()).setSum_apply_money(sum_apply_money);

		// ���������Ϣ
		resetAudit((PsnappaproveVO) billVO.getParentVO());
		return billVO;
	}

	/**
	 * ����ʱ���������Ϣ
	 *
	 * @author xuhw on 2009-11-12
	 * @param vo
	 */
	private void resetAudit(PsnappaproveVO vo)
	{
		String user = InvocationInfoProxy.getInstance().getUserId();
		UFDateTime dt = new UFDateTime(new Date(TimeService.getInstance().getTime()));
		// ������
		vo.setAttributeValue(PsnappaproveVO.CREATOR, user);
		// ����ʱ��
		vo.setAttributeValue(PsnappaproveVO.CREATIONTIME, dt);
		// �޸���
		vo.setAttributeValue(PsnappaproveVO.MODIFIER, null);
		// �޸�ʱ��
		vo.setAttributeValue(PsnappaproveVO.MODIFIEDTIME, null);
	}

	/**
	 * У������������Ľ��ĺϷ���<BR>
	 * 1) ��н�ʱ�׼�����н��������0 < =��׼���� <= ����������Ľ�� <= ��׼����<BR>
	 * 1) ��н�ʱ�׼���ǿ��н������������������Ľ�� == н�ʱ�׼ >= 0<BR>
	 * 3) ̸�й��ʣ�����������Ľ�� >= 0 <BR>
	 *
	 * @author xuhw on 2009-12-29
	 */
	public void validateMoneyLimit(AggPsnappaproveVO aggVO, boolean isApprove) throws BusinessException
	{
		if (aggVO == null)
		{
			return;
		}
		PsnappaproveBVO[] psnappaproveVOs = (PsnappaproveBVO[]) aggVO.getChildrenVO();
		UFDoubleCompare doubleCompare = new UFDoubleCompare();
		// ������
		UFDouble money = null;
		// �����׼PK
		//		String strPkCrt = null;
		String strPrmlvPK = null;
		String strSeclvPK = null;
		// �Ƿ���
		String strIsRange = null;
		// �Ƿ�̸��
		UFBoolean bnNegotiation = null;
		// ��Ա����
		//		String strPsnname = null;
		WaCriterionVO criterionVo = null;

		HashMap<String, WaCriterionVO> criterionVoMap = ((NCLocator
				.getInstance().lookup(IWaGradeService.class)))
				.getCrierionVOMapByPrmSec(psnappaproveVOs, isApprove);

		int rowCnt = 0;
		for (PsnappaproveBVO psnappaproveVO : psnappaproveVOs)
		{
			rowCnt++;
			money = psnappaproveVO.getWa_apply_money();
			//			strPkCrt = psnappaproveVO.getPk_wa_crt_apply();
			strPrmlvPK = psnappaproveVO.getPk_wa_prmlv_apply();
			strSeclvPK = psnappaproveVO.getPk_wa_seclv_apply();

			//			strPsnname = psnappaproveVO.getPsnname();
			bnNegotiation = psnappaproveVO.getNegotiation();
			strIsRange = psnappaproveVO.getIs_range();
			if (isApprove)
			{
				money = psnappaproveVO.getWa_cofm_money();
				//				strPkCrt = psnappaproveVO.getPk_wa_crt_cofm();
				strPrmlvPK = psnappaproveVO.getPk_wa_prmlv_cofm();
				strSeclvPK = psnappaproveVO.getPk_wa_seclv_cofm();
			}
			// ��̸��
			if (bnNegotiation.booleanValue())
			{
				continue;
			}
			//			if (StringUtils.isEmpty(strPrmlvPK))
			//			{
			//				String info = MessageFormat.format(ResHelper.getString("60130adjapprove","060130adjapprove0120")/*@res "�� {0}�У�н�ʱ�׼����Ϊ��, ���޸�."*/, rowCnt);
			//				Logger.debug(info);
			//				throw new BusinessException(info);
			//			}

			if (doubleCompare.lessThan(money, new UFDouble(0)))
			{
				String info = MessageFormat.format(ResHelper.getString("60130adjapprove","060130adjapprove0121")/*@res "�� {0}�У�����Ϊ��, ���޸�."*/, rowCnt);
				Logger.debug(info);
				throw new BusinessException(info);
			}
			
			// 2015-10-09 zhousze ������������������У�飺������һ��������ǣ�����н�ʱ�׼��������ʱ�����û��ѡ�С�н�ʱ�׼����
			// ֱ����д���ᱨ�����ڸ���Ϊ����У��һ�Σ����û����дн�ʱ�׼�Ͳ��ܱ��棬���׳�������Ϣ begin
			if(StringUtils.isEmpty(strPrmlvPK)){
				throw new BusinessException(ResHelper.getString("60130adjapprove","060130adjapprove0172")/*@res "н�ʱ�׼����Ϊ�գ�"*/);
			}
			// end
			
			criterionVo = criterionVoMap.get(strPrmlvPK
					+ strSeclvPK);
			// ���ǿ��
			if (!UFBoolean.TRUE.toString().equals(strIsRange))
			{
			}
			else
			{
				if (doubleCompare.lessThan(money, criterionVo.getMin_value()))
				{
					Logger.debug("���н������������С��н�ʱ�׼�����ޣ�");

					throw new BusinessException(MessageFormat.format(ResHelper.getString("60130adjapprove","060130adjapprove0122")/*@res "�� {0}�У����н������������С��н�ʱ�׼������, ���޸�."*/, rowCnt));
				}
				else if (doubleCompare.lessThan(criterionVo.getMax_value(), money))
				{
					Logger.debug("���н�����������ܴ���н�ʱ�׼�����ޣ�");

					throw new BusinessException(MessageFormat.format(ResHelper.getString("60130adjapprove","060130adjapprove0123")/*@res "�� {0}�У����н�����������ܴ���н�ʱ�׼������, ���޸�."*/, rowCnt));

				}
			}
		}
	}

	/**
	 * ����λ�Ͳ��ŷ����ϲ��ͳ���ʱ������ʱ�䣬�ж϶�������Ϣ���Ƿ��������Ա
	 *
	 * @author xuhw on 2010-7-1
	 * @param clazz
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	public Object[] queryAdjustVOByCondition(String condition) throws BusinessException
	{
		return getAdjustDao().queryAdjustInfoWhenDeptOrPostChange(condition);
	}

	/**
	 * ɾ��
	 *
	 * @author xuhw on 2010-1-7
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	public AggPsnappaproveVO doDelete(AggPsnappaproveVO vo) throws BusinessException
	{
		String billType = (String) vo.getParentVO().getAttributeValue(PsnappaproveVO.BILLTYPE);
		String pk_group = (String) vo.getParentVO().getAttributeValue(PsnappaproveVO.PK_GROUP);
		String pk_org = (String) vo.getParentVO().getAttributeValue(PsnappaproveVO.PK_ORG);
		String bill_code = (String) vo.getParentVO().getAttributeValue(PsnappaproveVO.BILLCODE);
		if (isAutoGenerateBillCode(billType, pk_group, pk_org))
		{
			getIBillcodeManage().returnBillCodeOnDelete(billType, pk_group, pk_org, bill_code, null);
		}
		Logger.debug("doDelete(AggPsnappaproveVO vo)");
		MDPersistenceService.lookupPersistenceService().deleteBillFromDB(vo);
		return vo;
	}

	private IBillcodeManage getIBillcodeManage()
	{

		return NCLocator.getInstance().lookup(IBillcodeManage.class);
	}


	private boolean isAutoGenerateBillCode(String billType, String pk_group, String pk_org) throws BusinessException
	{
		BillCodeContext billCodeContext = HiCacheUtils.getBillCodeContext(billType, pk_group, pk_org);
		return billCodeContext != null;
	}


	/**
	 * ���������뵥������Ч���ڡ����ڻ���ڼ�ʱ�䷶Χ������û��У�顣<BR>
	 * �����жԴ˵���Ҫ��:�ڶ����������ύʱ��ϵͳ�Զ�У����Ч���ڵĺϷ��ԣ�<BR>
	 * ������뵥���е���Ч�������ڻ���ڼ��ʱ�䷶Χ��ϵͳҪ������ʾ���Ҳ������ύ��<BR>
	 */
	private void validateUsedate(UFLiteralDate usedate) throws BusinessException
	{
		if (usedate != null && usedate.toString().length() > 0)
		{
//			xiejie3 2015-1-7 11:25:01  ����IConfigFileService�ӿ��Ѳ����ڣ����� aIConfigFileService.isValidDate������v633ʵ����ֱ�ӷ��ص�true��������ο���ֱ��ע��
//			IConfigFileService aIConfigFileService = (IConfigFileService) NCLocator.getInstance().lookup(IConfigFileService.class.getName());
//			try
//			{
//				boolean isin = aIConfigFileService.isValidDate(new UFDate(usedate.getMillis()));
//				if (!isin)
//				{
//					Logger.error("��Ч���ڲ���ϵͳ����Ļ�׼�ڼ�֮��, ���޸�.");
//					throw new BusinessException(ResHelper.getString("60130adjapprove","060130adjapprove0124")/*@res "��Ч���ڲ���ϵͳ����Ļ�׼�ڼ�֮��, ���޸�."*/);
//				}
//			}
//			catch (BusinessException ex)
//			{
//				Logger.error("��Ч���ڲ���ϵͳ����Ļ�׼�ڼ�֮��, ���޸�.", ex);
//				throw new BusinessException(ResHelper.getString("60130adjapprove","060130adjapprove0124")/*@res "��Ч���ڲ���ϵͳ����Ļ�׼�ڼ�֮��, ���޸�."*/);
//			}
		}
		else
		{
			Logger.debug("��Ч����Ϊ��, ���޸�.");
			throw new BusinessException(ResHelper.getString("60130adjapprove","060130adjapprove0033")/*@res "��Ч����Ϊ��, ���޸�."*/);
		}
	}

	/**
	 * н���յ���̨������-�����ʵĳ���
	 *
	 * @param batchadjustVO
	 * @return
	 * @throws BusinessException
	 */
	public AdjustWadocVO[] getAdjustWadocVOs4Adjust(
			BatchAdjustVO batchadjustVO, PsnappaproveBVO[] psnappaproveBVOs)
					throws BusinessException {
		WaGradeVO gradeVO = (WaGradeVO)getAdjustDao().getBaseDao().retrieveByPK(WaGradeVO.class, batchadjustVO.getPk_wa_grd());
		if (gradeVO != null) {
			batchadjustVO.setPrmlv_money_sort(gradeVO.getPrmlv_money_sort());
			batchadjustVO.setSeclv_money_sort(gradeVO.getSeclv_money_sort());
		}

		batchadjustVO.setIs_multsecckeck(gradeVO.getIsmultsec());

		//        // ����ԭʼ�ļ��𵵱� ��Ĭ�ϵ����뼶�𵵱�
		//        psnappaproveBVOs = AdjustUtil.getExtraInfo(psnappaproveBVOs, true);
		AdjustWadocVO[] adjustWadocPsnInfoVOs = getAdjustTool().filterPsnInfo(psnappaproveBVOs, batchadjustVO);
		return getAdjustTool().batchAdjust(batchadjustVO, adjustWadocPsnInfoVOs);
	}

	private PsndocWadocAdjustTool adjustTool;

	public PsndocWadocAdjustTool getAdjustTool(){
		if (adjustTool == null) {
			adjustTool = new PsndocWadocAdjustTool();
		}
		return adjustTool;
	}

	public void setAdjustTool(PsndocWadocAdjustTool adjustTool){
		this.adjustTool = adjustTool;
	}

	/**
	 * ���������뵥������Ч���ڡ����ڻ���ڼ�ʱ�䷶Χ������û��У�顣<BR>
	 * �����жԴ˵���Ҫ��:�ڶ����������ύʱ��ϵͳ�Զ�У����Ч���ڵĺϷ��ԣ�<BR>
	 * ������뵥���е���Ч�������ڻ���ڼ��ʱ�䷶Χ��ϵͳҪ������ʾ���Ҳ������ύ��<BR>
	 */
	private void validateUsedate(PsnappaproveBVO psnappaproveBVO,
			UFBoolean partflagShow, int rownum) throws BusinessException
			{
		UFLiteralDate usedate = psnappaproveBVO.getUsedate();
		if (usedate != null && usedate.toString().length() > 0)
		{
//			xiejie3 2015-1-7 11:25:01  ����IConfigFileService�ӿ��Ѳ����ڣ����� aIConfigFileService.isValidDate������v633ʵ����ֱ�ӷ��ص�true��������ο���ֱ��ע��  
//			IConfigFileService aIConfigFileService = (IConfigFileService) NCLocator.getInstance().lookup(IConfigFileService.class.getName());
//			try
//			{
//				boolean isin = aIConfigFileService.isValidDate(new UFDate(usedate.getMillis()));
//				if (!isin)
//				{
//					Logger.error("��Ч���ڲ���ϵͳ����Ļ�׼�ڼ�֮��, ���޸�.");
//					throw new BusinessException(ResHelper.getString("60130adjapprove","060130adjapprove0124")/*@res "��Ч���ڲ���ϵͳ����Ļ�׼�ڼ�֮��, ���޸�."*/);
//				}
//			}
//			catch (BusinessException ex)
//			{
//				Logger.error("��Ч���ڲ���ϵͳ����Ļ�׼�ڼ�֮��, ���޸�.", ex);
//				throw new BusinessException(ResHelper.getString("60130adjapprove","060130adjapprove0124")/*@res "��Ч���ڲ���ϵͳ����Ļ�׼�ڼ�֮��, ���޸�."*/);
//			}
		}
		else
		{
			Logger.debug("��Ч����Ϊ��, ���޸�.");
			throw new BusinessException(ResHelper.getString("60130adjapprove","060130adjapprove0033")/*@res "��Ч����Ϊ��, ���޸�."*/);
		}

		if(getAdjustDao().validateUsedate(psnappaproveBVO)){
			Logger.error("��ʼ���ڲ���������һ����¼�Ľ�ֹ����/��ֹ���ڲ���������һ����¼�Ŀ�ʼ����, ���޸�.");
			throw new BusinessException(MessageFormat.format(ResHelper.getString("60130adjapply","060130adjapply0216")
					/*
					 * @res
					 * "�����Ӽ�¼��{0}{1} ��Ч���ڲ������������¶����ʼ�¼�Ľ�ֹ����/��ʼ����, ���޸�."
					 */,
					 rownum,
					 StringUtils.isEmpty(psnappaproveBVO.getPsnname())?"":ResHelper.getString("60130adjapply","060130adjapply0217")
							 /* @res "��Ա��" */+ psnappaproveBVO
							 .getPsnname()
							 + (partflagShow.booleanValue() ? " ["
									 + (psnappaproveBVO
											 .getAssgid()
											 .equals(Integer
													 .valueOf(1)) ? ResHelper
															 .getString(
																	 "60130adjapprove",
																	 "060130adjapprove0142")/*
																	  * @
																	  * res
																	  * "��ְ  "
																	  */
																	 : ResHelper
																	 .getString(
																			 "60130adjapprove",
																			 "060130adjapprove0140")/*
																			  * @
																			  * res
																			  * "��ְ�� "
																			  */)
																			  + "] "
																			  : " "),
																			  StringUtils.isEmpty(psnappaproveBVO
																					  .getPk_wa_item_showname()) ? "" : ResHelper
																							  .getString("60130adjapply",
																									  "060130adjapply0218")
																									  /*@res " н����Ŀ��"*/+psnappaproveBVO.getPk_wa_item_showname()));
		}
			}

	@Override
	public AggPsnappaproveVO[] queryObjectByPks(String[] pks) throws BusinessException {
		
		AggPsnappaproveVO[] vos = getServiceTemplate().queryByPks(AggPsnappaproveVO.class, pks);;
		return getAdjustDao().querySumApplyAndSumConfimMoney(vos);
		
	}

	@Override
	public String[] queryPksByCondition(LoginContext context,
			String condition, PFQueryParams queryParams, Boolean isApprove)
			throws BusinessException {
		if(null!=queryParams){

			IFlowBizItf itf = HrPfHelper.getFlowBizItf(AggPsnappaproveVO.class);

			String strApproveDatePeriod = HrPfHelper.getApproveDatePeriod(itf, "wa_psnappaprove", queryParams.getApproveDateParam(),queryParams.getBillState());


			if (StringUtils.isNotBlank(strApproveDatePeriod))
			{
				condition += " and " + strApproveDatePeriod;
			}
		}
		if(null!=condition){
			condition = condition.replace("select pk_psnapp from wa_psnappaprove_b", "select wa_psnappaprove_b.pk_psnapp from wa_psnappaprove_b");
		}
		String sql = "select wa_psnappaprove.pk_psnapp from wa_psnappaprove where "+condition;
		// 2015-11-3 zhousze ����������/�������ݵ�ǰ��֯���˵��� begin
		sql = sql + " and pk_org = '" + context.getPk_org() + "' ";
		// end
		List<String> pk_psnappsList = (List<String>) new BaseDAO().executeQuery(sql.toString(), new ColumnListProcessor());
	     if(pk_psnappsList == null){
	        	return new String[0];
	     }
	     return pk_psnappsList.toArray(new String[0]);
	}
}