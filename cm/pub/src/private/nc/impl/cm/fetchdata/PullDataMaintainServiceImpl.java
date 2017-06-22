package nc.impl.cm.fetchdata;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bd.framework.base.CMArrayUtil;
import nc.bd.framework.base.CMCollectionUtil;
import nc.bd.framework.base.CMMapUtil;
import nc.bd.framework.base.CMStringUtil;
import nc.bd.framework.db.CMSqlBuilder;
import nc.bs.cm.fetchdata.allcancel.AllCancelFactroy;
import nc.bs.cm.fetchdata.allcancel.IAllCancel;
import nc.bs.cm.fetchdata.checkandfetch.ICheckAndFetch;
import nc.bs.cm.fetchdata.factory.FetchTypeFactroy;
import nc.bs.cm.fetchdata.fetchPersistent.AbstractFetchPersistent;
import nc.bs.cm.fetchdata.fetchcheck.CheckBeforeFetch;
import nc.bs.cm.pub.framework.CMAlgorithmBaseFramework;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.IAttributeManager;
import nc.bs.framework.common.NCLocator;
import nc.cmpub.business.adapter.BDAdapter;
import nc.cmpub.business.adapter.IAAdapter;
import nc.cmpub.framework.reqattribute.ReqAttributeManager;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.cm.fetchdata.IPullDataMaintainService;
import nc.itf.cm.fetchdata.IPullDataQueryService;
import nc.itf.cm.iastuff.IIastuffMaintainService;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.pub.billcode.BillCodeReturnExecutor;
import nc.pubitf.cm.costdataclose.cm.pub.ICostDataCloseForCMService;
import nc.pubitf.cm.fetchset.cm.iaretrieval.IFetchSetPubQueryService;
import nc.ui.cm.fetchdata.util.PullDataUtil;
import nc.vo.cm.autocostcalculation.enumeration.TaskItemEnum;
import nc.vo.cm.fetchdata.cmconst.FetchDataLangConst;
import nc.vo.cm.fetchdata.entity.FetchKeyVO;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.cm.fetchdata.entity.PullDataErroInfoVO;
import nc.vo.cm.fetchdata.entity.PullDataStateVO;
import nc.vo.cm.fetchdata.entity.UIAllDataVO;
import nc.vo.cm.fetchdata.enumeration.CMMesTypeEnum;
import nc.vo.cm.fetchdata.enumeration.FetchDataObjEnum;
import nc.vo.cm.fetchdata.enumeration.FetchDataSchemaEnum;
import nc.vo.cm.fetchset.entity.AggFetchSetVO;
import nc.vo.cm.fetchset.entity.FetchSetItemVO;
import nc.vo.cm.fetchset.enumeration.CMBillEnum;
import nc.vo.cm.pc.pc0410.CMTProxySrv;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * ȡ���־û�����ʵ���࣬��Ҫ��ȡ����顢ȡ�������ӿ�
 */
@SuppressWarnings("unchecked")
public class PullDataMaintainServiceImpl extends CMAlgorithmBaseFramework
		implements IPullDataMaintainService {
	@Override
	protected int getTaskItem() {
		return TaskItemEnum.FETCH_DATA.toIntValue();
	}

	@Override
	public boolean fetchDataForCostTran(FetchParamVO paramvo,
			List<String> cycObjMaterialLst, Set<String> iaMaterialSet)
			throws BusinessException {
		// ��ȡȡ��״̬��Ϣ
		PullDataStateVO[] result = this.getAlreadyExistFetchInfos(paramvo);

		// ���û��ȡ��������ȡ
		if (CMArrayUtil.isEmpty(result)) {
			return true;
		}
		// ȡ������
		Integer fetchschema = result[0].getIfetchscheme();
		paramvo.setIfetchscheme(fetchschema);
		// �����еĽ�������
		List<String> tranpks = new ArrayList<String>();
		List<List<UFDate>> dateList = new ArrayList<List<UFDate>>();
		for (PullDataStateVO vo : result) {
			if (FetchDataSchemaEnum.WEEKSCHEMA.equalsValue(fetchschema)) {
				List<UFDate> beginEndDateList = new ArrayList<UFDate>();
				beginEndDateList.add(vo.getDbegindate());
				beginEndDateList.add(vo.getDenddate());
				dateList.add(beginEndDateList);
			} else {
				if (!tranpks.contains(vo.getCtranstypeid())) {
					tranpks.add(vo.getCtranstypeid());
				}
			}
		}
		paramvo.setBeginenddate(dateList);
		// ����ʱ��������,������������
		Map<Integer, List<String>> billTypeMap = new HashMap<Integer, List<String>>();
		billTypeMap.put(
				Integer.valueOf(CMBillEnum.MATEROUT.getEnumValue().getValue()),
				tranpks);
		paramvo.setBillTypeMap(billTypeMap);
		// ȥ���ϳ��ⵥ��
		paramvo.setIfetchobjtype(CMBillEnum.MATEROUT.toIntValue());
		paramvo.setForTrans(true);
		// ����ʱ������
		this.getBillTypeMap(paramvo);
		// ����Ϊ�ɱ���ת
		paramvo.setIfetchobjtype(Integer
				.valueOf(FetchDataObjEnum.MATERIALOUT_COSTTRAN.getEnumValue()
						.getValue()));
		// �˲�
		String acountBook = IAAdapter.getAcountBookParam4ICInteface(paramvo
				.getPk_org());
		// �ɱ���
		Set<String> costDomain = BDAdapter.getAllCostRegionIDByPkOrg(paramvo
				.getPk_org());
		paramvo.setAcountBook(acountBook);
		paramvo.setCostDomain(costDomain);
		paramvo.setIfetchscheme(fetchschema);
		paramvo.setNday(result[0].getNday());
		PullDataErroInfoVO[] errorVOs = this.getCheckAndFetchComom(paramvo,
				false, cycObjMaterialLst, iaMaterialSet);
		if (CMArrayUtil.isEmpty(errorVOs)) {
			return false;
		} else {
			StringBuilder msg = new StringBuilder();
			for (PullDataErroInfoVO pullDataErroInfoVO : errorVOs) {
				msg.append("The error is:[" + pullDataErroInfoVO.getErroinfo()
						+ "]");
				msg.append("MoCode��[" + pullDataErroInfoVO.getCmoid() + "]");
				msg.append("ProductID��[" + pullDataErroInfoVO.getCmaterialid()
						+ "]");
				msg.append("MaterialID��["
						+ pullDataErroInfoVO.getCmaterialvid() + "]");
				msg.append("CostCenterID��["
						+ pullDataErroInfoVO.getCcostcenterid() + "]");
				msg.append("Number��[" + pullDataErroInfoVO.getNnum() + "]");
				msg.append("\r\n");
			}
			if (CMStringUtil.isNotEmpty(msg.toString())) {
				ExceptionUtils.wrappBusinessException(msg.toString());
			}
		}
		return true;
	}

	/**
	 * ȡ�Ѿ����ڵ�ȡ����Ϣ
	 */
	private PullDataStateVO[] getAlreadyExistFetchInfos(FetchParamVO paramvo) {
		String sql = this.getComonSql(paramvo);
		VOQuery<PullDataStateVO> query = new VOQuery<PullDataStateVO>(
				PullDataStateVO.class);
		PullDataStateVO[] stateVos = query.queryWithWhereKeyWord(sql, null);
		return stateVos;
	}

	/**
	 * ����sql����ƴ��
	 */
	private String getComonSql(FetchParamVO pullDataParamVos) {
		CMSqlBuilder sb = new CMSqlBuilder();
		sb.where();
		sb.append(PullDataStateVO.PK_GROUP, pullDataParamVos.getPk_group());
		sb.append(" and ");
		sb.append(PullDataStateVO.PK_ORG, pullDataParamVos.getPk_org());
		sb.append(" and ");
		sb.append(PullDataStateVO.IFETCHOBJTYPE, pullDataParamVos
				.getIfetchobjtype().toString());
		sb.append(" and ");
		sb.append(PullDataStateVO.CPERIOD, pullDataParamVos.getDaccountperiod());
		sb.append(" and ");
		sb.append(PullDataStateVO.BFETCHED, UFBoolean.TRUE);
		sb.append(" and ");
		sb.append(" dr = 0 ");
		return sb.toString();
	}

	/**
	 * ���ý������Ͷ�Ӧ�Ľ�������
	 */
	private void getBillTypeMap(FetchParamVO paramvo) {
		// ����ǰ�����ȡ�������뽻�����ͣ���������ȡ������billTypeMap
		if (FetchDataSchemaEnum.WEEKSCHEMA.getEnumValue().getValue()
				.equals(paramvo.getIfetchscheme().toString())
				&& paramvo.getIfetchobjtype() != FetchDataObjEnum.ACT
						.toIntValue()) {
			Integer fetchType = paramvo.getIfetchobjtype();
			// ��ѯȡ�����ͺͽ������͵Ķ��չ�ϵ
			FetchSetItemVO[] fetchSetItems = null;
			try {
				fetchSetItems = NCLocator
						.getInstance()
						.lookup(IFetchSetPubQueryService.class)
						.getFetchSetItems(fetchType, paramvo.getPk_org(),
								paramvo.getPk_group());
			} catch (BusinessException e) {
				ExceptionUtils.wrappException(e);
			}
			if (CMArrayUtil.isEmpty(fetchSetItems)) {
				ExceptionUtils.wrappBusinessException(FetchDataLangConst
						.getNO_TRANSTYPE_DEFINED());
				return;
			}
			Map<Integer, List<String>> billTypeMap = PullDataUtil
					.createBillTypeMap(fetchSetItems, fetchType);
			// ����ȡ������
			paramvo.setBillTypeMap(billTypeMap);
		}
	}

	public Integer fetchData(FetchParamVO paramvo) throws BusinessException {
		try {
			if (!new CheckBeforeFetch().checkBeforeFetchData(paramvo, false)) {
				return CMMesTypeEnum.ERROR.toIntValue();
			}

			this.checkTS(paramvo);
			this.getBillTypeMap(paramvo);
			boolean isCoverData = false;
			if (this.isFethed(paramvo.getPullDataStateVOArr())) {
				// ����ȡ����ť����
				isCoverData = true;
			}
			// �Ƿ񸲸�
			if (isCoverData) {
				FetchTypeFactroy fatory = new FetchTypeFactroy();
				Integer ifetchobjtype = paramvo.getIfetchobjtype();
				ICheckAndFetch type = fatory.createFetchTypeFactory(
						ifetchobjtype, null, null);
				type.deleteBill(paramvo);
			}

			// �ò���������������
			PullDataErroInfoVO[] errorVOs = this.getCheckAndFetchComom(paramvo,
					false, null, null);
			Integer status = null;
			for (PullDataErroInfoVO infoVO : errorVOs) {
				if (infoVO.getImestype() == null
						|| infoVO.getImestype().equals(
								CMMesTypeEnum.ERROR.toIntValue())) {
					return CMMesTypeEnum.ERROR.toIntValue();
				} else if (infoVO.getImestype().equals(
						CMMesTypeEnum.WARN.toIntValue())) {
					status = CMMesTypeEnum.WARN.toIntValue();
				}
			}
			return status;
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
		}
		return null;
	}

	// ����ts

	private void checkTS(FetchParamVO param) {
		PullDataStateVO[] vos = param.getPullDataStateVOArr();
		CMSqlBuilder sql = new CMSqlBuilder();
		sql.append("select cbilltype,ccostcenterid,ctranstypeid,dbegindate,denddate,ts from cm_fetchinfo where dr=0 and pk_org='");
		sql.append(param.getPk_org() + "'and cperiod='"
				+ param.getDaccountperiod() + "' and pulldatatype=");
		sql.append(param.getPulldatatype() + " and ifetchobjtype="
				+ param.getIfetchobjtype() + " and ifetchscheme="
				+ param.getIfetchscheme());
		BaseDAO dao = new BaseDAO();
		Map<String, String> tsMap = null;
		try {
			tsMap = (Map<String, String>) dao.executeQuery(sql.toString(),
					new ResultSetProcessor() {
						private static final long serialVersionUID = 1L;

						@Override
						public Object handleResultSet(ResultSet rs)
								throws SQLException {
							Map<String, String> tsMap = new HashMap<String, String>();
							while (rs.next()) {
								tsMap.put(
										rs.getInt(1) + rs.getString(2)
												+ rs.getString(3)
												+ rs.getString(4)
												+ rs.getString(5),
										rs.getString(6));
							}
							return tsMap;
						}
					});
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		if (CMMapUtil.isEmpty(tsMap)) {
			return;
		}
		for (PullDataStateVO vo : vos) {
			String ts = tsMap.get((vo.getCbilltype() == null ? "0" : vo
					.getCbilltype())
					+ vo.getCcostcenterid()
					+ vo.getCtranstypeid()
					+ vo.getDbegindate()
					+ vo.getDenddate());
			if (vo.getTs() == null || !ts.equals(vo.getTs().toString())) {
				ExceptionUtils.wrappBusinessException(FetchDataLangConst
						.getNEW_STATUS());
			}
		}
	}

	@Override
	public Map<FetchKeyVO, Integer> allCancel(FetchParamVO paramvo)
			throws BusinessException {
		String pkOrg = paramvo.getPk_org();
		String period = paramvo.getDaccountperiod();

		Class<?>[] parameterTypes = new Class<?>[1];
		parameterTypes[0] = FetchParamVO.class;

		Object[] param = new Object[1];
		param[0] = paramvo;

		return (Map<FetchKeyVO, Integer>) this.doCancelExecute(pkOrg, period,
				"doAllCancel", parameterTypes, param);
	}

	public Map<FetchKeyVO, Integer> doAllCancel(FetchParamVO paramvo)
			throws BusinessException {
		try {
			// ��ȡȡ��״̬��Ϣ
			Map<FetchKeyVO, List<PullDataStateVO>> fetchAllDataMap = this
					.getFetchAllDataMap(paramvo);
			if (CMMapUtil.isEmpty(fetchAllDataMap)) {
				return null;
			}
			Map<FetchKeyVO, Integer> resultMap = new HashMap<FetchKeyVO, Integer>();
			Set<String> orgSet = new HashSet<String>();
			Map<String, OrgVO> orgMap = new HashMap<String, OrgVO>();
			for (FetchKeyVO keyVO : fetchAllDataMap.keySet()) {
				orgSet.add(keyVO.getPk_org());
			}
			// ���ɱ������Ƿ��Ѿ�����
			orgMap = BDAdapter.getOrgMap(orgSet);
			StringBuilder msg = new StringBuilder();
			for (String pk_org : orgSet) {
				if (NCLocator
						.getInstance()
						.lookup(ICostDataCloseForCMService.class)
						.isCloseAccount(paramvo.getPk_group(), pk_org,
								new String[] { paramvo.getDaccountperiod() })) {
					msg.append(FetchDataLangConst.getSTR_FACTORY());
					msg.append("[" + orgMap.get(pk_org).getCode() + "]");
					msg.append(FetchDataLangConst.getALLCALCEL_CMACCOUNTCLOSE());
					msg.append("\r\n");
				}
			}
			if (CMStringUtil.isNotEmpty(msg.toString())) {
				ExceptionUtils.wrappBusinessException(msg.toString());
			}
			// ����Ƿ�������ģ��
			PullDataUtil.moduleEnable(this
					.getFetchDataMainSelectVO(fetchAllDataMap));

			for (Entry<FetchKeyVO, List<PullDataStateVO>> entry : fetchAllDataMap
					.entrySet()) {
				String pk_group = paramvo.getPk_group();
				String pk_org = entry.getKey().getPk_org();
				Integer ifetchType = entry.getKey().getIfetchobjtype();
				String cperiod = paramvo.getDaccountperiod();
				List<PullDataStateVO> lst = entry.getValue();

				FetchKeyVO keyVO = new FetchKeyVO();
				keyVO.setPk_org(pk_org);
				keyVO.setIfetchobjtype(ifetchType);

				// 2017-06-18
				keyVO.setFator1(entry.getKey().getFator1());
				keyVO.setFator2(entry.getKey().getFator2());
				keyVO.setFator3(entry.getKey().getFator3());
				keyVO.setFator4(entry.getKey().getFator4());
				keyVO.setFator5(entry.getKey().getFator5());
				keyVO.setFator6(entry.getKey().getFator6());
				keyVO.setFator7(entry.getKey().getFator7());
				keyVO.setFator8(entry.getKey().getFator8());
				keyVO.setFator9(entry.getKey().getFator9());
				keyVO.setFator10(entry.getKey().getFator10());

				if (CMCollectionUtil.isEmpty(lst)) {
					continue;
				}
				if (!this.isFethed(lst.toArray(new PullDataStateVO[0]))) {
					continue;
				}

				if (ifetchType.equals(FetchDataObjEnum.ISSTUFF.toIntValue())) {
					// ȡ��ȡ��---������������ĵ�
					resultMap.put(keyVO, this.cancelFetchIAStuffData(pk_group,
							pk_org, cperiod));
					continue;
				}

				PullDataStateVO[] allVO = entry.getValue().toArray(
						new PullDataStateVO[0]);
				FetchParamVO fetchParamvo = this.getParamVO(pk_group, pk_org,
						cperiod, allVO);
				// ȡ��ȡ��
				resultMap.put(keyVO, this.doCancel(fetchParamvo));
			}

			return resultMap;
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
		}
		return null;
	}

	// key=����+ȡ������value=ȡ����Ϣvo
	private Map<FetchKeyVO, List<PullDataStateVO>> getFetchAllDataMap(
			FetchParamVO paramvo) throws BusinessException {
		if (CMMapUtil.isEmpty(paramvo.getFetchAllDataMap())) {
			List<AggFetchSetVO> fetchSetLst = new ArrayList<AggFetchSetVO>();
			PullDataStateVO pullParamvo = this.getPullParamVO(
					paramvo.getPk_group(), paramvo.getPk_org(),
					paramvo.getDaccountperiod());
			Map<FetchKeyVO, UIAllDataVO> vosMap = NCLocator.getInstance()
					.lookup(IPullDataQueryService.class)
					.getVos4UiDataSet(pullParamvo);
			if (CMMapUtil.isNotEmpty(vosMap)) {
				Map<FetchKeyVO, List<PullDataStateVO>> fetchAllDataMap = new HashMap<FetchKeyVO, List<PullDataStateVO>>();
				for (Entry<FetchKeyVO, UIAllDataVO> entry : vosMap.entrySet()) {
					UIAllDataVO vo = entry.getValue();
					if (CMArrayUtil.isNotEmpty(vo.getVos())) {

						fetchAllDataMap.put(entry.getKey(),
								Arrays.asList(vo.getVos()));
					}
					if (CMArrayUtil.isNotEmpty(vo.getFetchSetAggVo())) {
						fetchSetLst
								.addAll(Arrays.asList(vo.getFetchSetAggVo()));
					}
				}

				paramvo.setFetchAllDataMap(fetchAllDataMap);
			}

			if (CMCollectionUtil.isEmpty(fetchSetLst)) {
				ExceptionUtils.wrappBusinessException(FetchDataLangConst
						.getERR_NORELATIONFETCHSET());
			}

		}

		return paramvo.getFetchAllDataMap();
	}

	private PullDataStateVO getPullParamVO(String pk_group, String pk_org,
			String cperiod) {

		PullDataStateVO paramvo = new PullDataStateVO();
		paramvo.setPk_group(pk_group);
		paramvo.setPk_org(pk_org);
		// ���ݲ�����֯��ѯ���й�������
		paramvo.setPk_orgs(new String[] { paramvo.getPk_org() });
		paramvo.setIfetchobjtype(FetchDataObjEnum.ALL); // ȡ������ 1 ���ϳ���2����Ʒ�� 3
														// ��Ʒ 4 ��ҵ
		paramvo.setCperiod(cperiod); // �ڼ�
		paramvo.setBusiDate(AppContext.getInstance().getBusiDate());// ҵ������
		return paramvo;
	}

	public Integer doCancel(FetchParamVO paramvo) throws BusinessException {
		// 1.����ȡ��״̬��Ϊδȥ����
		AbstractFetchPersistent.deleteRelationData(paramvo);
		// 2.����ȡ������ɾ������
		IAllCancel allcancel = new AllCancelFactroy()
				.createAllCancelFactory(paramvo.getIfetchobjtype());
		allcancel.allCancel(paramvo);

		return CMMesTypeEnum.SUCCESS.toIntValue();

	}

	/**
	 * ����߼�
	 * 
	 * @param paramvo
	 *            [] PullDataStateVO
	 * @return PullDataErroInfoVO[] ������Ϣvo
	 */
	@Override
	public PullDataErroInfoVO[] getErroInfoVos(FetchParamVO paramvo)
			throws BusinessException {
		try {
			// ��ȡȡ��״̬��Ϣ
			if (CMMapUtil.isEmpty(paramvo.getFetchSelectDataMap())) {
				paramvo.setFetchSelectDataMap(this.getFetchAllDataMap(paramvo));
				paramvo.setFetchAllDataMap(paramvo.getFetchSelectDataMap());
			}
			if (CMMapUtil.isEmpty(paramvo.getFetchSelectDataMap())) {
				return new PullDataErroInfoVO[0];
			}

			// ����Ƿ�������ģ��
			PullDataUtil.moduleEnable(this.getFetchDataMainSelectVO(paramvo
					.getFetchSelectDataMap()));

			List<PullDataErroInfoVO> errovolst = new ArrayList<PullDataErroInfoVO>();
			for (Entry<FetchKeyVO, List<PullDataStateVO>> entry : paramvo
					.getFetchSelectDataMap().entrySet()) {
				List<PullDataStateVO> lst = entry.getValue();
				// 1�õ���ҳ�����ѡ���е�vo
				PullDataStateVO[] pullDataStateVOs = lst
						.toArray(new PullDataStateVO[0]);
				// 2 ��ѯ ��̨���м�鲢����ǰ̨
				FetchParamVO fetchParamvo = this.getParamVO(
						paramvo.getPk_group(), entry.getKey().getPk_org(),
						paramvo.getDaccountperiod(), pullDataStateVOs);
				PullDataErroInfoVO[] errovos = this
						.doEachCheckErro(fetchParamvo);
				if (CMArrayUtil.isNotEmpty(errovos)) {
					errovolst.addAll(Arrays.asList(errovos));
				}

			}

			return errovolst.toArray(new PullDataErroInfoVO[0]);
		}

		catch (Exception ex) {

			ExceptionUtils.marsh(ex);
		}
		return null;
	}

	public PullDataErroInfoVO[] doEachCheckErro(FetchParamVO paramvo)
			throws BusinessException {
		PullDataErroInfoVO[] errorVOs = null;
		try {
			if (!new CheckBeforeFetch().checkBeforeFetchData(paramvo, true)) {
				return new PullDataErroInfoVO[0];
			}
			this.getBillTypeMap(paramvo);
			errorVOs = this.getCheckAndFetchComom(paramvo, true, null, null);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		if (CMArrayUtil.isEmpty(errorVOs)) {
			return new PullDataErroInfoVO[0];
		}
		// ���ش�����Ϣ
		return errorVOs;
	}

	/**
	 * �õ�ȡ���ͼ�鹫�����
	 * <p>
	 * �����ȡ���ͼ��ŵ�һ����Ҫ����Ϊ������Ҫһ��������ɣ����ò�
	 * 
	 * @param paramvo
	 *            ѡ�������
	 * @param isCheckFlag
	 *            �Ƿ��Ǽ�����
	 * @return ������Ϣ
	 */
	private PullDataErroInfoVO[] getCheckAndFetchComom(FetchParamVO paramvo,
			boolean isCheckFlag, List<String> cycObjMaterialLst,
			Set<String> iaMaterialSet) throws BusinessException {
		Integer ifetchobjtype = paramvo.getIfetchobjtype();
		PullDataErroInfoVO[] errorVOs = null;
		try {
			// ����ȡ������<���ϳ��ⵥ/����Ʒ��ⵥ/�깤������߹����깤����>���ü�鷽��
			FetchTypeFactroy fatory = new FetchTypeFactroy();
			ICheckAndFetch type = fatory.createFetchTypeFactory(ifetchobjtype,
					cycObjMaterialLst, iaMaterialSet);
			errorVOs = type.checkAndFetchCommon(paramvo, isCheckFlag);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return errorVOs;
	}

	@Override
	public Object[] fetchAllData(FetchParamVO paramvo) throws BusinessException {
		String pkOrg = paramvo.getPk_org();
		String period = paramvo.getDaccountperiod();
		Class<?>[] parameterTypes = new Class<?>[1];
		parameterTypes[0] = FetchParamVO.class;
		Object[] param = new Object[1];
		param[0] = paramvo;
		// 2-17-06-20 lify ��������Զ��Ƶ�ȡ��
		if (100 == paramvo.getIfetchobjtype()) {
			return doCostAutoIn(paramvo);

		}
		if (101 == paramvo.getIfetchobjtype()) {
			return doCostAutoInCancle(paramvo);
		}

		return (Object[]) this.doExecute(pkOrg, period, "dofetchAllData",
				parameterTypes, param);
	}

	/**
	 * ��������Զ��Ƶ�ȡ��
	 * 
	 * @param paramvo
	 * @param isCheckFlag
	 * @param cycObjMaterialLst
	 * @param iaMaterialSet
	 * @return
	 * @throws BusinessException
	 */
	public PullDataErroInfoVO[] doCostAutoInCancle(FetchParamVO paramvo)
			throws BusinessException {
		Integer ifetchobjtype = paramvo.getIfetchobjtype();
		PullDataErroInfoVO[] errorVOs = null;
		try {
			FetchTypeFactroy fatory = new FetchTypeFactroy();
			ICheckAndFetch type = fatory.createFetchTypeFactory(ifetchobjtype,
					null, null);
			errorVOs = type.checkAndFetchCommon(paramvo, false);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return errorVOs;
	}

	/**
	 * ��������Զ��Ƶ�
	 * 
	 * @param paramvo
	 * @param isCheckFlag
	 * @param cycObjMaterialLst
	 * @param iaMaterialSet
	 * @return
	 * @throws BusinessException
	 */
	public PullDataErroInfoVO[] doCostAutoIn(FetchParamVO paramvo)
			throws BusinessException {
		Integer ifetchobjtype = paramvo.getIfetchobjtype();
		PullDataErroInfoVO[] errorVOs = null;
		// ��ֹ����,����ȡ����ȫ������
		Map<FetchKeyVO, List<PullDataStateVO>> fetchAllDataMap = this.getFetchAllDataMap(paramvo);
		if (CMMapUtil.isEmpty(paramvo.getFetchAllDataMap())) {
			return null;
		}
		try {
			FetchTypeFactroy fatory = new FetchTypeFactroy();
			ICheckAndFetch type = fatory.createFetchTypeFactory(ifetchobjtype,
					null, null);
			errorVOs = type.checkAndFetchCommon(paramvo, false);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return errorVOs;
	}

	private void dealFetchSelectData(FetchParamVO paramvo) {
		Map<FetchKeyVO, List<PullDataStateVO>> fetchSelectDataMap = paramvo
				.getFetchSelectDataMap();
		Map<FetchKeyVO, List<PullDataStateVO>> fetchAllDataMap = paramvo
				.getFetchAllDataMap();

		// key=����+ȡ������
		Map<FetchKeyVO, List<PullDataStateVO>> allDataMap = new HashMap<FetchKeyVO, List<PullDataStateVO>>();
		for (Entry<FetchKeyVO, List<PullDataStateVO>> entry : fetchAllDataMap
				.entrySet()) {
			for (PullDataStateVO stateVO : entry.getValue()) {
				// ����ȡ�����Ķ�����Ϣ
				if (stateVO.getBfetched().booleanValue() == true) {
					if (allDataMap.containsKey(entry.getKey())) {
						allDataMap.get(entry.getKey()).add(stateVO);
					} else {
						List<PullDataStateVO> lst = new ArrayList<PullDataStateVO>();
						lst.add(stateVO);
						allDataMap.put(entry.getKey(), lst);
					}

				}
			}
		}

		if (CMMapUtil.isNotEmpty(allDataMap)) {
			for (Entry<FetchKeyVO, List<PullDataStateVO>> entry : allDataMap
					.entrySet()) {
				FetchKeyVO keyVO = entry.getKey();
				// ����ͬ������+ȡ�������Ѿ�ȡ������������ȡ��
				if (fetchSelectDataMap.containsKey(keyVO)) {
					fetchSelectDataMap.get(keyVO).addAll(entry.getValue());
				}
			}
		}

	}

	public Object dofetchAllData(FetchParamVO paramvo) throws BusinessException {
		try {
			// ��ֹ����,����ȡ����ȫ������
			paramvo.setFetchAllDataMap(this.getFetchAllDataMap(paramvo));
			if (CMMapUtil.isEmpty(paramvo.getFetchSelectDataMap())) {
				paramvo.setFetchSelectDataMap(paramvo.getFetchAllDataMap());
			} else {
				// ��ͬ������+ȡ������������ȡ��������Ҫȫ������ȡ��;
				// �����ڵ���checkandfetch.ICheckAndFetch.deleteBill(FetchParamVO)�ᵼ����ɾ����
				this.dealFetchSelectData(paramvo);
			}

			if (CMMapUtil.isEmpty(paramvo.getFetchSelectDataMap())) {
				return new HashMap<FetchKeyVO, Integer>();
			}
			// ����Ƿ�������ģ��
			PullDataUtil.moduleEnable(this.getFetchDataMainSelectVO(paramvo
					.getFetchSelectDataMap()));

			// �����ü���߼��ռ����桢������Ϣ
			// TODO
			PullDataErroInfoVO[] errovolst = new PullDataErroInfoVO[0];// this.getErroInfoVos(paramvo);
			// �ռ����桢������Ϣ
			List<String> warnningList = new ArrayList<String>();
			if (CMArrayUtil.isNotEmpty(errovolst)) {
				List<String> errorList = new ArrayList<String>();
				for (PullDataErroInfoVO infoVO : errovolst) {
					if (infoVO.getImestype() == null
							|| infoVO.getImestype().equals(
									CMMesTypeEnum.ERROR.toIntValue())) {
						errorList.add(infoVO.getErroinfo());
					} else if (infoVO.getImestype().equals(
							CMMesTypeEnum.WARN.toIntValue())) {
						warnningList.add(infoVO.getErroinfo());
					}
				}
				if (CMCollectionUtil.isNotEmpty(errorList)) {
					String errorStr = "";
					for (String error : errorList) {
						errorStr += error + "\r\n";
					}
					ExceptionUtils.wrappBusinessException(errorStr);
				}
			}

			Map<FetchKeyVO, Integer> errorStatusMap = new HashMap<FetchKeyVO, Integer>();
			IAttributeManager reqAttributeManager = new ReqAttributeManager()
					.getBillNOReqAttributeManager();
			for (Entry<FetchKeyVO, List<PullDataStateVO>> entry : paramvo
					.getFetchSelectDataMap().entrySet()) {
				if (CMCollectionUtil.isEmpty(entry.getValue())) {
					continue;
				}

				// ȡ����Ϣ
				String pk_group = AppContext.getInstance().getPkGroup();
				String pk_org = entry.getKey().getPk_org();
				String cperiod = paramvo.getDaccountperiod();
				Integer ifetchType = entry.getKey().getIfetchobjtype();

				// ȡ������KEY
				FetchKeyVO keyVO = new FetchKeyVO();
				keyVO.setPk_org(pk_org);
				keyVO.setIfetchobjtype(ifetchType);

				PullDataStateVO[] pullDataStateVOs = entry.getValue().toArray(
						new PullDataStateVO[0]);
				// ����ȡ��������Ϣ
				FetchParamVO paramNewVO = this.getParamVO(pk_group, pk_org,
						cperiod, pullDataStateVOs);

				paramNewVO.setPullDataStateVOArr(pullDataStateVOs);

				// ����ǵ�һ��ȡ���������е�vo����̨����
				if (!paramNewVO.isFetchBefore()) {

					List<PullDataStateVO> lst = paramvo.getFetchAllDataMap()
							.get(keyVO);
					if (CMCollectionUtil.isEmpty(lst)) {
						continue;
					}

					PullDataStateVO[] allVO = lst
							.toArray(new PullDataStateVO[0]);
					FetchParamVO allParamvo = this.getParamVO(pk_group, pk_org,
							cperiod, allVO);
					paramNewVO.setAllFetchParamVo(allParamvo);
					// ����ȡ��״̬Ϊȡ����
					PullDataUtil.setFetchedStatusTOallVO(allVO,
							pullDataStateVOs);
					allParamvo.setPullDataStateVOArr(allVO);
				}
				// ȡ��
				Method method = this.getMethod(this);
				Object[] paras = new Object[1];
				paras[0] = paramNewVO;
				// try {
				Object ret = CMTProxySrv.delegate_RequiresNew(this, method,
						paras);
				if (ret != null) {
					errorStatusMap.put(keyVO, (Integer) ret);
				} else {
					errorStatusMap.put(keyVO,
							CMMesTypeEnum.SUCCESS.toIntValue());
				}
				// }
				// catch (Exception e) {
				// errorStatusMap.put(keyVO, CMMesTypeEnum.ERROR.toIntValue());
				// // TODO ���������½�����Ϣ�ռ�
				//
				// break;
				// }
				reqAttributeManager.setAttribute(
						BillCodeReturnExecutor.REQ_REGION_BILLCODE, null);
			}

			this.showMsg(errorStatusMap);

			Object[] result = new Object[2];
			// result[0] = errorStatusMap;
			result[1] = warnningList;

			return result;
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
		}

		return null;
	}

	private void showMsg(Map<FetchKeyVO, Integer> errorStatusMap) {
		if (CMMapUtil.isEmpty(errorStatusMap)) {
			return;
		}
		StringBuilder errMsg = new StringBuilder();
		for (Entry<FetchKeyVO, Integer> entry : errorStatusMap.entrySet()) {
			String ifetchobjName = this.getFetchObjName(entry.getKey()
					.getIfetchobjtype());
			if (entry.getValue().equals(CMMesTypeEnum.ERROR.toIntValue())) {
				// ȡ������:ȡ��ʧ�ܣ�����м�������ȡʧ��ԭ��
				errMsg.append(FetchDataLangConst.getCOMBOX_FETCHOBJ() + "["
						+ ifetchobjName + "]");
				errMsg.append(FetchDataLangConst.getWARN_ERRORSINFOOFCHECK());
				errMsg.append("\r\n");
			}
		}
		if (CMStringUtil.isNotEmpty(errMsg.toString())) {
			ExceptionUtils.wrappBusinessException(errMsg.toString());
		}
	}

	private String getFetchObjName(Integer ifetchobj) {
		String fetchObjName = "";

		if (ifetchobj.equals(FetchDataObjEnum.MATERIALOUT.toIntValue())) {
			fetchObjName = FetchDataObjEnum.MATERIALOUT.getName();
		}
		if (ifetchobj.equals(FetchDataObjEnum.PRODIN.toIntValue())) {
			fetchObjName = FetchDataObjEnum.PRODIN.getName();
		}
		if (ifetchobj.equals(FetchDataObjEnum.SPOIL.toIntValue())) {
			fetchObjName = FetchDataObjEnum.SPOIL.getName();
		}
		if (ifetchobj.equals(FetchDataObjEnum.ACT.toIntValue())) {
			fetchObjName = FetchDataObjEnum.ACT.getName();
		}
		if (ifetchobj.equals(FetchDataObjEnum.GXWW.toIntValue())) {
			fetchObjName = FetchDataObjEnum.GXWW.getName();
		}
		if (ifetchobj.equals(FetchDataObjEnum.ISSTUFF.toIntValue())) {
			fetchObjName = FetchDataObjEnum.ISSTUFF.getName();
		}
		return fetchObjName;
	}

	@Override
	protected String getWarrningReprot(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Object[]) {
			List<String> warnningList = (List<String>) ((Object[]) obj)[1];
			String warnningStr = null;
			if (CMCollectionUtil.isNotEmpty(warnningList)) {
				for (String warnning : warnningList) {
					warnningStr += warnning + "\r\n";
				}
			}
			return warnningStr;
		}
		return null;
	}

	private Method getMethod(Object target) {
		Class<?>[] parameterTypes = new Class<?>[1];
		parameterTypes[0] = FetchParamVO.class;
		Method method = null;
		try {
			method = target.getClass().getDeclaredMethod("fetchData",
					parameterTypes);
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		return method;
	}

	/**
	 * ��֯����vo ���в�ѯ
	 * 
	 * @param pullDataStateVOs
	 *            ѡ�е�����
	 * @return ����vo
	 */
	private FetchParamVO getParamVO(String pk_group, String pk_org,
			String cperiod, PullDataStateVO[] pullDataStateVOs)
			throws BusinessException {
		if (CMArrayUtil.isEmpty(pullDataStateVOs)) {
			return null;
		}

		List<String> costCenteridList = new ArrayList<String>();
		List<List<UFDate>> dateList = new ArrayList<List<UFDate>>();
		FetchParamVO paramvo = new FetchParamVO();
		boolean isFetched = false;
		for (PullDataStateVO vo : pullDataStateVOs) {
			if (vo.getBfetched().booleanValue()) {
				isFetched = true;// ֻҪ��һ��ȡ��ֵ���ʹ�������ȡ��ֵ
			}

			if (CMStringUtil.isNotEmpty(vo.getCcostcenterid())
					&& !costCenteridList.contains(vo.getCcostcenterid())) {
				costCenteridList.add(vo.getCcostcenterid());
			}
			List<UFDate> beginEndDateList = new ArrayList<UFDate>();
			beginEndDateList.add(vo.getDbegindate());
			beginEndDateList.add(vo.getDenddate());
			dateList.add(beginEndDateList);
			// �浥�����ͺͽ������͵�����
			if (Integer.valueOf(
					FetchDataSchemaEnum.PERIODSCHEMA.getEnumValue().getValue())
					.compareTo(pullDataStateVOs[0].getIfetchscheme()) == 0) {// ����
				if (!paramvo.getBillTypeMap().containsKey(vo.getCbilltype())) {
					List<String> list = new ArrayList<String>();
					list.add(vo.getCtranstypeid());
					paramvo.getBillTypeMap().put(vo.getCbilltype(), list);
				} else {
					List<String> list = paramvo.getBillTypeMap().get(
							vo.getCbilltype());
					if (!list.contains(vo.getCtranstypeid())) {
						paramvo.getBillTypeMap().get(vo.getCbilltype())
								.add(vo.getCtranstypeid());
					}
				}
			}
		}
		// �浥�����ͺͽ������͵�����
		if (Integer.valueOf(
				FetchDataSchemaEnum.WEEKSCHEMA.getEnumValue().getValue())
				.compareTo(pullDataStateVOs[0].getIfetchscheme()) == 0) {// ����ȡ����
			if (Integer.valueOf(
					FetchDataObjEnum.MATERIALOUT.getEnumValue().getValue())
					.compareTo(pullDataStateVOs[0].getIfetchobjtype()) == 0) {// ���ϳ�
				paramvo.getBillTypeMap().put(
						Integer.valueOf(CMBillEnum.MATEROUT.getEnumValue()
								.getValue()), null);
				paramvo.getBillTypeMap().put(
						Integer.valueOf(CMBillEnum.OUTADJUST.getEnumValue()
								.getValue()), null);
				paramvo.getBillTypeMap().put(
						Integer.valueOf(CMBillEnum.PROCESSIN.getEnumValue()
								.getValue()), null);
			} else if (Integer.valueOf(
					FetchDataObjEnum.PRODIN.getEnumValue().getValue())
					.compareTo(pullDataStateVOs[0].getIfetchobjtype()) == 0) {// 2����Ʒ
				paramvo.getBillTypeMap().put(
						Integer.valueOf(CMBillEnum.PRODUCTIN.getEnumValue()
								.getValue()), null);
				paramvo.getBillTypeMap().put(
						Integer.valueOf(CMBillEnum.PROCESSIN.getEnumValue()
								.getValue()), null);
			} else if (Integer.valueOf(
					FetchDataObjEnum.SPOIL.getEnumValue().getValue())
					.compareTo(pullDataStateVOs[0].getIfetchobjtype()) == 0) {// 5����������ⵥ
																				// modified
																				// by
																				// sunyqb
																				// at
																				// 2011.12.29
				paramvo.getBillTypeMap().put(
						Integer.valueOf(CMBillEnum.DISCARDIN.getEnumValue()
								.getValue()), null);
			}
		}

		paramvo.setCcostcenterid(costCenteridList);
		paramvo.setBeginenddate(dateList);
		paramvo.setPk_group(pk_group);
		paramvo.setPk_org(pk_org);
		paramvo.setPk_org_v(pk_org);

		paramvo.setIfetchobjtype(pullDataStateVOs[0].getIfetchobjtype()); // ȡ������
																			// 1
																			// ���ϳ���2����Ʒ��
																			// 3
																			// ��ҵ
		paramvo.setDaccountperiod(cperiod); // �ڼ�
		paramvo.setPulldatatype(pullDataStateVOs[0].getPulldatatype()); // ȡ�����ͣ�1������㣬2�������죬4��Ʒȡ��
		paramvo.setIfetchscheme(pullDataStateVOs[0].getIfetchscheme()); // ȡ������
		paramvo.setFetchBefore(isFetched);// ��¼֮ǰ�Ƿ�ȡ��������һ��ȡ����Ҫ��¼���еı����¼
		// ����/����
		paramvo.setNday(pullDataStateVOs[0].getNday()); // �������ȡ������null
		paramvo.setBusiDate(AppContext.getInstance().getBusiDate());// ҵ������
		return paramvo;
	}

	private boolean isFethed(PullDataStateVO[] paramvos)
			throws BusinessException {
		for (PullDataStateVO paramvo : paramvos) {
			if (paramvo.getBfetched().booleanValue()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �õ���ҳ�����ѡ���е�vo
	 **/
	private PullDataStateVO[] getFetchDataMainSelectVO(
			Map<FetchKeyVO, List<PullDataStateVO>> fetchSelectDataMap) {
		List<PullDataStateVO> lst = new ArrayList<PullDataStateVO>();
		for (Entry<FetchKeyVO, List<PullDataStateVO>> entry : fetchSelectDataMap
				.entrySet()) {
			lst.addAll(entry.getValue());
		}
		return lst.toArray(new PullDataStateVO[0]);
	}

	private Integer cancelFetchIAStuffData(String pk_group, String pk_org,
			String cperiod) throws BusinessException {
		NCLocator.getInstance().lookup(IIastuffMaintainService.class)
				.cancelFetchData(pk_group, pk_org, cperiod);
		return CMMesTypeEnum.SUCCESS.toIntValue();
	}
}
