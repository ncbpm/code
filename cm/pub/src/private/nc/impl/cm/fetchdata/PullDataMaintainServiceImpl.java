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
 * 取数持久化服务实现类，主要有取数检查、取数两个接口
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
		// 获取取数状态信息
		PullDataStateVO[] result = this.getAlreadyExistFetchInfos(paramvo);

		// 如果没有取过数据则不取
		if (CMArrayUtil.isEmpty(result)) {
			return true;
		}
		// 取数方案
		Integer fetchschema = result[0].getIfetchscheme();
		paramvo.setIfetchscheme(fetchschema);
		// 存所有的交易类型
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
		// 按月时单据类型,交易类型设置
		Map<Integer, List<String>> billTypeMap = new HashMap<Integer, List<String>>();
		billTypeMap.put(
				Integer.valueOf(CMBillEnum.MATEROUT.getEnumValue().getValue()),
				tranpks);
		paramvo.setBillTypeMap(billTypeMap);
		// 去材料出库单的
		paramvo.setIfetchobjtype(CMBillEnum.MATEROUT.toIntValue());
		paramvo.setForTrans(true);
		// 周期时的设置
		this.getBillTypeMap(paramvo);
		// 设置为成本结转
		paramvo.setIfetchobjtype(Integer
				.valueOf(FetchDataObjEnum.MATERIALOUT_COSTTRAN.getEnumValue()
						.getValue()));
		// 账簿
		String acountBook = IAAdapter.getAcountBookParam4ICInteface(paramvo
				.getPk_org());
		// 成本域
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
				msg.append("MoCode：[" + pullDataErroInfoVO.getCmoid() + "]");
				msg.append("ProductID：[" + pullDataErroInfoVO.getCmaterialid()
						+ "]");
				msg.append("MaterialID：["
						+ pullDataErroInfoVO.getCmaterialvid() + "]");
				msg.append("CostCenterID：["
						+ pullDataErroInfoVO.getCcostcenterid() + "]");
				msg.append("Number：[" + pullDataErroInfoVO.getNnum() + "]");
				msg.append("\r\n");
			}
			if (CMStringUtil.isNotEmpty(msg.toString())) {
				ExceptionUtils.wrappBusinessException(msg.toString());
			}
		}
		return true;
	}

	/**
	 * 取已经存在的取数信息
	 */
	private PullDataStateVO[] getAlreadyExistFetchInfos(FetchParamVO paramvo) {
		String sql = this.getComonSql(paramvo);
		VOQuery<PullDataStateVO> query = new VOQuery<PullDataStateVO>(
				PullDataStateVO.class);
		PullDataStateVO[] stateVos = query.queryWithWhereKeyWord(sql, null);
		return stateVos;
	}

	/**
	 * 公共sql进行拼接
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
	 * 设置交易类型对应的交易类型
	 */
	private void getBillTypeMap(FetchParamVO paramvo) {
		// 如果是按周期取数，加入交易类型，重新设置取数参数billTypeMap
		if (FetchDataSchemaEnum.WEEKSCHEMA.getEnumValue().getValue()
				.equals(paramvo.getIfetchscheme().toString())
				&& paramvo.getIfetchobjtype() != FetchDataObjEnum.ACT
						.toIntValue()) {
			Integer fetchType = paramvo.getIfetchobjtype();
			// 查询取数类型和交易类型的对照关系
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
			// 设置取数参数
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
				// 调用取数按钮操作
				isCoverData = true;
			}
			// 是否覆盖
			if (isCoverData) {
				FetchTypeFactroy fatory = new FetchTypeFactroy();
				Integer ifetchobjtype = paramvo.getIfetchobjtype();
				ICheckAndFetch type = fatory.createFetchTypeFactory(
						ifetchobjtype, null, null);
				type.deleteBill(paramvo);
			}

			// 该操作包括保存数据
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

	// 检验ts

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
			// 获取取数状态信息
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
			// 检查成本资料是否已经关帐
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
			// 检查是否有启用模块
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
					// 取消取数---其他出入库消耗单
					resultMap.put(keyVO, this.cancelFetchIAStuffData(pk_group,
							pk_org, cperiod));
					continue;
				}

				PullDataStateVO[] allVO = entry.getValue().toArray(
						new PullDataStateVO[0]);
				FetchParamVO fetchParamvo = this.getParamVO(pk_group, pk_org,
						cperiod, allVO);
				// 取消取数
				resultMap.put(keyVO, this.doCancel(fetchParamvo));
			}

			return resultMap;
		} catch (Exception ex) {
			ExceptionUtils.marsh(ex);
		}
		return null;
	}

	// key=工厂+取数对象，value=取数信息vo
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
		// 根据财务组织查询所有关联工厂
		paramvo.setPk_orgs(new String[] { paramvo.getPk_org() });
		paramvo.setIfetchobjtype(FetchDataObjEnum.ALL); // 取数对象： 1 材料出，2产成品入 3
														// 废品 4 作业
		paramvo.setCperiod(cperiod); // 期间
		paramvo.setBusiDate(AppContext.getInstance().getBusiDate());// 业务日期
		return paramvo;
	}

	public Integer doCancel(FetchParamVO paramvo) throws BusinessException {
		// 1.设置取数状态表为未去过数
		AbstractFetchPersistent.deleteRelationData(paramvo);
		// 2.根据取数类型删除数据
		IAllCancel allcancel = new AllCancelFactroy()
				.createAllCancelFactory(paramvo.getIfetchobjtype());
		allcancel.allCancel(paramvo);

		return CMMesTypeEnum.SUCCESS.toIntValue();

	}

	/**
	 * 检查逻辑
	 * 
	 * @param paramvo
	 *            [] PullDataStateVO
	 * @return PullDataErroInfoVO[] 错误信息vo
	 */
	@Override
	public PullDataErroInfoVO[] getErroInfoVos(FetchParamVO paramvo)
			throws BusinessException {
		try {
			// 获取取数状态信息
			if (CMMapUtil.isEmpty(paramvo.getFetchSelectDataMap())) {
				paramvo.setFetchSelectDataMap(this.getFetchAllDataMap(paramvo));
				paramvo.setFetchAllDataMap(paramvo.getFetchSelectDataMap());
			}
			if (CMMapUtil.isEmpty(paramvo.getFetchSelectDataMap())) {
				return new PullDataErroInfoVO[0];
			}

			// 检查是否有启用模块
			PullDataUtil.moduleEnable(this.getFetchDataMainSelectVO(paramvo
					.getFetchSelectDataMap()));

			List<PullDataErroInfoVO> errovolst = new ArrayList<PullDataErroInfoVO>();
			for (Entry<FetchKeyVO, List<PullDataStateVO>> entry : paramvo
					.getFetchSelectDataMap().entrySet()) {
				List<PullDataStateVO> lst = entry.getValue();
				// 1得到主页面表体选择行的vo
				PullDataStateVO[] pullDataStateVOs = lst
						.toArray(new PullDataStateVO[0]);
				// 2 查询 后台进行检查并返回前台
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
		// 返回错误信息
		return errorVOs;
	}

	/**
	 * 得到取数和检查公共组件
	 * <p>
	 * 这里把取数和检查放到一起，主要是因为里面需要一个事务完成，不好拆开
	 * 
	 * @param paramvo
	 *            选择的数据
	 * @param isCheckFlag
	 *            是否是检查操作
	 * @return 错误信息
	 */
	private PullDataErroInfoVO[] getCheckAndFetchComom(FetchParamVO paramvo,
			boolean isCheckFlag, List<String> cycObjMaterialLst,
			Set<String> iaMaterialSet) throws BusinessException {
		Integer ifetchobjtype = paramvo.getIfetchobjtype();
		PullDataErroInfoVO[] errorVOs = null;
		try {
			// 根据取数类型<材料出库单/产成品入库单/完工报告或者工序完工报告>调用检查方法
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
		// 2-17-06-20 lify 费用入库自动制单取数
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
	 * 费用入库自动制单取消
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
	 * 费用入库自动制单
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
		// 防止并发,重新取界面全部数据
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

		// key=工厂+取数对象
		Map<FetchKeyVO, List<PullDataStateVO>> allDataMap = new HashMap<FetchKeyVO, List<PullDataStateVO>>();
		for (Entry<FetchKeyVO, List<PullDataStateVO>> entry : fetchAllDataMap
				.entrySet()) {
			for (PullDataStateVO stateVO : entry.getValue()) {
				// 所有取过数的对象信息
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
				// 对于同【工厂+取数对象】已经取过数，则重新取数
				if (fetchSelectDataMap.containsKey(keyVO)) {
					fetchSelectDataMap.get(keyVO).addAll(entry.getValue());
				}
			}
		}

	}

	public Object dofetchAllData(FetchParamVO paramvo) throws BusinessException {
		try {
			// 防止并发,重新取界面全部数据
			paramvo.setFetchAllDataMap(this.getFetchAllDataMap(paramvo));
			if (CMMapUtil.isEmpty(paramvo.getFetchSelectDataMap())) {
				paramvo.setFetchSelectDataMap(paramvo.getFetchAllDataMap());
			} else {
				// 相同【工厂+取数对象】若部分取过数则需要全部重新取数;
				// 否则在调用checkandfetch.ICheckAndFetch.deleteBill(FetchParamVO)会导致误删数据
				this.dealFetchSelectData(paramvo);
			}

			if (CMMapUtil.isEmpty(paramvo.getFetchSelectDataMap())) {
				return new HashMap<FetchKeyVO, Integer>();
			}
			// 检查是否有启用模块
			PullDataUtil.moduleEnable(this.getFetchDataMainSelectVO(paramvo
					.getFetchSelectDataMap()));

			// 先启用检查逻辑收集警告、错误信息
			// TODO
			PullDataErroInfoVO[] errovolst = new PullDataErroInfoVO[0];// this.getErroInfoVos(paramvo);
			// 收集警告、错误信息
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

				// 取数信息
				String pk_group = AppContext.getInstance().getPkGroup();
				String pk_org = entry.getKey().getPk_org();
				String cperiod = paramvo.getDaccountperiod();
				Integer ifetchType = entry.getKey().getIfetchobjtype();

				// 取数对象KEY
				FetchKeyVO keyVO = new FetchKeyVO();
				keyVO.setPk_org(pk_org);
				keyVO.setIfetchobjtype(ifetchType);

				PullDataStateVO[] pullDataStateVOs = entry.getValue().toArray(
						new PullDataStateVO[0]);
				// 最终取数参数信息
				FetchParamVO paramNewVO = this.getParamVO(pk_group, pk_org,
						cperiod, pullDataStateVOs);

				paramNewVO.setPullDataStateVOArr(pullDataStateVOs);

				// 如果是第一次取则设置所有的vo到后台保存
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
					// 设置取数状态为取过数
					PullDataUtil.setFetchedStatusTOallVO(allVO,
							pullDataStateVOs);
					allParamvo.setPullDataStateVOArr(allVO);
				}
				// 取数
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
				// // TODO 单独调用月结框架信息收集
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
				// 取数对象:取数失败，请进行检查操作获取失败原因。
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
	 * 组织参数vo 进行查询
	 * 
	 * @param pullDataStateVOs
	 *            选中的数据
	 * @return 参数vo
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
				isFetched = true;// 只要有一条取过值，就代表曾经取过值
			}

			if (CMStringUtil.isNotEmpty(vo.getCcostcenterid())
					&& !costCenteridList.contains(vo.getCcostcenterid())) {
				costCenteridList.add(vo.getCcostcenterid());
			}
			List<UFDate> beginEndDateList = new ArrayList<UFDate>();
			beginEndDateList.add(vo.getDbegindate());
			beginEndDateList.add(vo.getDenddate());
			dateList.add(beginEndDateList);
			// 存单据类型和交易类型的数据
			if (Integer.valueOf(
					FetchDataSchemaEnum.PERIODSCHEMA.getEnumValue().getValue())
					.compareTo(pullDataStateVOs[0].getIfetchscheme()) == 0) {// 按月
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
		// 存单据类型和交易类型的数据
		if (Integer.valueOf(
				FetchDataSchemaEnum.WEEKSCHEMA.getEnumValue().getValue())
				.compareTo(pullDataStateVOs[0].getIfetchscheme()) == 0) {// 周期取数用
			if (Integer.valueOf(
					FetchDataObjEnum.MATERIALOUT.getEnumValue().getValue())
					.compareTo(pullDataStateVOs[0].getIfetchobjtype()) == 0) {// 材料出
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
					.compareTo(pullDataStateVOs[0].getIfetchobjtype()) == 0) {// 2产成品
				paramvo.getBillTypeMap().put(
						Integer.valueOf(CMBillEnum.PRODUCTIN.getEnumValue()
								.getValue()), null);
				paramvo.getBillTypeMap().put(
						Integer.valueOf(CMBillEnum.PROCESSIN.getEnumValue()
								.getValue()), null);
			} else if (Integer.valueOf(
					FetchDataObjEnum.SPOIL.getEnumValue().getValue())
					.compareTo(pullDataStateVOs[0].getIfetchobjtype()) == 0) {// 5生产报废入库单
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

		paramvo.setIfetchobjtype(pullDataStateVOs[0].getIfetchobjtype()); // 取数对象：
																			// 1
																			// 材料出，2产成品入
																			// 3
																			// 作业
		paramvo.setDaccountperiod(cperiod); // 期间
		paramvo.setPulldatatype(pullDataStateVOs[0].getPulldatatype()); // 取数类型：1存货核算，2生产制造，4废品取数
		paramvo.setIfetchscheme(pullDataStateVOs[0].getIfetchscheme()); // 取数方案
		paramvo.setFetchBefore(isFetched);// 记录之前是否取过数，第一次取数需要记录所有的表体记录
		// ：月/周期
		paramvo.setNday(pullDataStateVOs[0].getNday()); // 如果是月取数则是null
		paramvo.setBusiDate(AppContext.getInstance().getBusiDate());// 业务日期
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
	 * 得到主页面表体选择行的vo
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
