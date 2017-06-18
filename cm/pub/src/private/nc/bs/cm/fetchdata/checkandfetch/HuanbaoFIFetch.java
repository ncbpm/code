package nc.bs.cm.fetchdata.checkandfetch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bd.framework.base.CMStringUtil;
import nc.bs.cm.fetchdata.checkandfetch.AbstractCheckAndFetch.AbstractFetchStrategy;
import nc.bs.cm.fetchdata.fetchcheck.AbstractCheckStrategy;
import nc.bs.cm.fetchdata.fetchcheck.MMFetchCheckStrategy;
import nc.bs.cm.fetchdata.groupdata.IGroupStrategy;
import nc.bs.framework.common.NCLocator;
import nc.cmpub.business.adapter.BDAdapter;
import nc.cmpub.business.adapter.FIAdapter;
import nc.cmpub.business.adapter.MMAdapter;
import nc.cmpub.business.enumeration.CMAllocStatusEnum;
import nc.cmpub.business.enumeration.CMSourceTypeEnum;
import nc.cmpub.business.enumeration.CMStatusEnum;
import nc.pubitf.cm.activitynum.cm.mmfetch.IActivityNumRewriteForMMFetch;
import nc.vo.bd.bdactivity.entity.BDActivityVO;
import nc.vo.cm.activitynum.entity.ActivityNumAggVO;
import nc.vo.cm.activitynum.entity.ActivityNumHeadVO;
import nc.vo.cm.activitynum.entity.ActivityNumItemVO;
import nc.vo.cm.costobject.entity.CostObjectGenerateVO;
import nc.vo.cm.costobject.enumeration.ProductTypeEnum;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.cm.fetchdata.entity.PullDataErroInfoVO;
import nc.vo.cm.fetchdata.entity.adapter.AggActNumVOAdapter;
import nc.vo.cm.fetchdata.entity.adapter.IFetchData;
import nc.vo.cm.fetchdata.entity.adapter.IMMFetchData;
import nc.vo.cm.fetchdata.enumeration.CMMesTypeEnum;
import nc.vo.cm.fetchdata.enumeration.FetchDataSchemaEnum;
import nc.vo.mmpac.apply.task.param.MMFetchDataVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.Constructor;
import nc.vo.util.AuditInfoUtil;

/**
 * 定额取数
 */
public class HuanbaoFIFetch  extends AbstractCheckAndFetch<IMMFetchData> {
	/**
	 * 作业消耗单接口
	 */
	private IActivityNumRewriteForMMFetch activityNumPubService = null;

	@Override
	public CircularlyAccessibleValueObject[] getData4OutSystem(
			FetchParamVO paramvo, boolean isCheckFlag) throws BusinessException {
		// 得到mm生产制造的完工单或者工序完工单据
		return this.getFinishOrGxFinish(paramvo);
	}

	/**
	 * 根据维护的交易类型，查询指定库存的出入库单的出入库数量，负数的取绝对值，
	 * 汇总依据：作业+消耗成本中心+成本对象+完工成本中心
	 * 分单依据：消耗成本中心+成本对象
	 * 
	 * @param paramvo
	 *            FetchParamVO 取数参数
	 * @return wkidAndMMfetchDataVOMap 取数得到的vo
	 * @throws BusinessException
	 *             BusinessException'
	 */
	public CircularlyAccessibleValueObject[] getFinishOrGxFinish(
			FetchParamVO paramvo) throws BusinessException {
		Integer fetchscheme = paramvo.getIfetchscheme();
		UFDate beginDay = new UFDate();
		UFDate endDay = new UFDate();
		List<String> costCenterid = null;
		List<String> wkids = new ArrayList<String>();
		List<String> deptids = new ArrayList<String>();

		// 会计月取数方案： 开始时间，结束时间为会计月份的开始时间和结束时间。
		if (FetchDataSchemaEnum.PERIODSCHEMA.equalsValue(fetchscheme)) {
			beginDay = paramvo.getBeginEndDate()[0];
			endDay = paramvo.getBeginEndDate()[1];
			costCenterid = paramvo.getCcostcenterid();
			this.setWkidAndDept(wkids, deptids, costCenterid);
		} else if (FetchDataSchemaEnum.WEEKSCHEMA.equalsValue(fetchscheme)) {
			// 周期取数，只是一条数据
			beginDay = paramvo.getBeginenddate().get(0).get(0); // 周期取数，只是一条数据
			endDay = paramvo.getBeginenddate().get(0).get(1);
			costCenterid = Arrays.asList(FIAdapter
					.getAllCostCenterByOrg(this.pkOrg));
			this.setWkidAndDept(wkids, deptids, costCenterid);
		}

		// 进行取数:工作工作中心为空时,按照部门进行取数
		CircularlyAccessibleValueObject[] mmfetchDataVOs = this
				.getMMDataOfOutSystem(beginDay, endDay, this.pkOrg,
						this.pkGroup, wkids.toArray(new String[0]),
						deptids.toArray(new String[0]));
		return mmfetchDataVOs;
	}

	/**
	 * 外系统取数
	 * 
	 * @param beginDay
	 *            开始时间
	 * @param endDay
	 *            结束时间
	 * @param pkOrg
	 *            工厂
	 * @param pkGroup
	 *            集团
	 * @param wkids
	 *            工作中心
	 * @throws BusinessException
	 *             业务异常
	 * @return 拉平后的vo数组
	 */
	public CircularlyAccessibleValueObject[] getMMDataOfOutSystem(
			UFDate beginDay, UFDate endDay, String pkOrg, String pkGroup,
			String[] wkids, String[] deptids) throws BusinessException {
		return MMAdapter.getWorkReport4FetchData(beginDay, endDay, pkOrg,
				pkGroup, wkids, deptids);

	}

	@Override
	public void transVOInserDB(IMMFetchData[] correctData, FetchParamVO paramvo)
			throws BusinessException {
		Map<String, List<IMMFetchData>> cachedVOMap = super.transVO(
				correctData, new AbstractFetchStrategy() {
					// 根据key字段，分组同样key值的vo，存到cachedVOMap，作业消耗单以标题
					// 消耗成本中心、成本对象进行区分
					@Override
					public String getUniqueKey(IMMFetchData vo) {
						StringBuffer key = new StringBuffer();
						key.append(vo.getCcostcenterid());
						key.append(vo.getCcostobjectid());
						return key.toString();
					}
				});
		ActivityNumAggVO[] ActivityNumAggVO = this.transActActNumVO(
				cachedVOMap, correctData, paramvo);
		this.insertByGroup(ActivityNumAggVO);
	}

	// 分批插入作业统计单中
	private void insertByGroup(ActivityNumAggVO[] activityNumAggVO)
			throws BusinessException {
		List<ActivityNumAggVO> needDealList = new ArrayList<ActivityNumAggVO>();
		for (ActivityNumAggVO vo : activityNumAggVO) {
			needDealList.add(vo);
			if (needDealList.size() == 1000) {
				this.getActivityNumPubService().mmCreateBill(
						needDealList.toArray(new ActivityNumAggVO[0]));
				needDealList.clear();
			}
		}
		if (needDealList.size() > 0) {
			this.getActivityNumPubService().mmCreateBill(
					needDealList.toArray(new ActivityNumAggVO[0]));
		}
	}

	/**
	 * 转换为作业材料消耗单
	 * 
	 * @param cachedVOMap
	 *            把汇总vo中相同key数据，放到一个list中
	 * @param correctData
	 *            正确数据
	 * @param paramvo
	 *            paramvo 取数参数
	 * @return ActivityNumAggVO[] aggvo
	 * @throws BusinessException
	 *             BusinessException
	 */
	public ActivityNumAggVO[] transActActNumVO(
			Map<String, List<IMMFetchData>> cachedVOMap,
			IMMFetchData[] correctData, final FetchParamVO paramvo)
			throws BusinessException {
		// 传给processValues方法的VO必须是初始化的
		ActivityNumAggVO[] ActivityNumAggVO = Constructor.construct(
				ActivityNumAggVO.class, cachedVOMap.size());
		return super
				.processValues(
						cachedVOMap,
						ActivityNumAggVO,
						new IGroupStrategy<ActivityNumAggVO, ActivityNumHeadVO, ActivityNumItemVO, IMMFetchData>() {
							@Override
							public ActivityNumHeadVO getHeadVO(
									List<IMMFetchData> volist) {
								IMMFetchData[] datavos = volist
										.toArray(new IMMFetchData[0]);
								return HuanbaoFIFetch.this.setActNumHeadVO(
										paramvo, datavos);
							}

							@Override
							public String getItemKey(ActivityNumItemVO vo) {
								StringBuilder key = new StringBuilder();
								key.append(vo.getCactivityid()); // 作业编码
								return key.toString();
							}

							@Override
							public String[] getSumAttrs(ActivityNumItemVO vo) {
								return new String[] { ActivityNumItemVO.NNUM }; // 作业量
							}

							@Override
							public ActivityNumItemVO[] getItemVO(
									List<IMMFetchData> voList) {

								List<ActivityNumItemVO> listItemvo = new ArrayList<ActivityNumItemVO>();

								return HuanbaoFIFetch.this.setActNumItemVO(
										voList.toArray(new IMMFetchData[0]),
										listItemvo);
							}
						});
	}

	@Override
	protected List<Map<IFetchData, PullDataErroInfoVO>> getErrorInfo(
			IMMFetchData[] datas, String cperiod, boolean isCheckFlag)
			throws BusinessException {
		return super.getErrorInfoAndFilteredData(datas,
				new MMFetchCheckStrategy(this.pkOrg), cperiod, isCheckFlag);
	}

	/**
	 * 组织成本对象参数
	 * 
	 * @param datas
	 *            IMMFetchData 取的数据
	 * @return 成本对象vo
	 */
	@Override
	protected CostObjectGenerateVO[] getAndSetCostOjbParam(IFetchData[] datas,
			AbstractCheckStrategy strategy,
			Map<IFetchData, PullDataErroInfoVO> vbFreeErrorVoMap) {
		Set<String> matrPKs = new HashSet<String>();
		for (IFetchData vo : datas) {
			IMMFetchData data = (IMMFetchData) vo;
			if (CMStringUtil.isNotEmpty(data.getMaterialid())) {
				matrPKs.add(data.getMaterialid());
			}
		}
		CostObjectGenerateVO[] costObjectGenerateVOs = new CostObjectGenerateVO[datas.length];
		for (int i = 0; i < datas.length; i++) {
			AggActNumVOAdapter data = (AggActNumVOAdapter) datas[i];
			costObjectGenerateVOs[i] = new CostObjectGenerateVO();
			costObjectGenerateVOs[i].setCmaterialid(data.getMaterialid());
			costObjectGenerateVOs[i].setVmocode(data.getCmocode());
			costObjectGenerateVOs[i].setCprojectid(data.getProjectId());
			costObjectGenerateVOs[i]
					.setFinstoragetype(data.getFinstoragetype());
			costObjectGenerateVOs[i].setPk_org(this.pkOrg);
			costObjectGenerateVOs[i].setPk_org_v(this.pkOrgv);
			costObjectGenerateVOs[i].setPk_group(this.pkGroup);
			costObjectGenerateVOs[i].setFproducttype(Integer
					.valueOf(ProductTypeEnum.MAINPRODUCT.value().toString()));// 主产品标示
			costObjectGenerateVOs[i].setCmainmaterialid(data.getMaterialid());// 对应主产品id
			// 产品辅助属性
			costObjectGenerateVOs[i].setCprojectid(data.getCprojectid());
			costObjectGenerateVOs[i].setCproductorid(data.getCproductorid());
			costObjectGenerateVOs[i].setCvendorid(data.getCvendorid());
			costObjectGenerateVOs[i].setCcustomerid(data.getCcustomerid());
			costObjectGenerateVOs[i].setVfree1(data.getVfree1());
			costObjectGenerateVOs[i].setVfree2(data.getVfree2());
			costObjectGenerateVOs[i].setVfree3(data.getVfree3());
			costObjectGenerateVOs[i].setVfree4(data.getVfree4());
			costObjectGenerateVOs[i].setVfree5(data.getVfree5());
			costObjectGenerateVOs[i].setVfree6(data.getVfree6());
			costObjectGenerateVOs[i].setVfree7(data.getVfree7());
			costObjectGenerateVOs[i].setVfree8(data.getVfree8());
			costObjectGenerateVOs[i].setVfree9(data.getVfree9());
			costObjectGenerateVOs[i].setVfree10(data.getVfree10());
			data.setCostObjectGenerateVO(costObjectGenerateVOs[i]);
		}
		return costObjectGenerateVOs;
	}

	/**
	 * 设置作业消耗单headvo
	 * 
	 * @param paramvo
	 *            取数参数
	 * @param datavos
	 *            取得数据数组
	 * @return ActivityNumHeadVO 作业消耗单表头
	 */
	public ActivityNumHeadVO setActNumHeadVO(FetchParamVO paramvo,
			IMMFetchData[] datavos) {
		ActivityNumHeadVO ActivityNumHeadVO = new ActivityNumHeadVO();
		ActivityNumHeadVO.setCcostcenterid(datavos[0].getCcostcenterid()); // 成本中心
		ActivityNumHeadVO.setCcostobjectid(datavos[0].getCcostobjectid()); // 成本对象编码
		ActivityNumHeadVO.setPk_group(paramvo.getPk_group()); // 集团
		ActivityNumHeadVO.setPk_org(paramvo.getPk_org()); // 组织
		ActivityNumHeadVO.setPk_org_v(paramvo.getPk_org_v());
		ActivityNumHeadVO.setCperiod(paramvo.getDaccountperiod()); // 会计期间

		// 来源类型完工报告
		ActivityNumHeadVO.setIsourcetype(datavos[0].getResourcetype());
		ActivityNumHeadVO.setStatus(CMStatusEnum.SFREEDOM.valueIndex()); // 自由态
		ActivityNumHeadVO.setDbusinessdate(this.getBusinessdate());

		// 添加创建人创建时间
		ActivityNumHeadVO.setCreator(AuditInfoUtil.getCurrentUser());
		ActivityNumHeadVO.setCreationtime(AuditInfoUtil.getCurrentTime());
		return ActivityNumHeadVO;
	}

	/**
	 * 设置作业消耗单headvo
	 * 
	 * @param vos
	 *            取的数据
	 * @param listItemvo
	 *            List<ActivityNumItemVO>
	 * @return ActivityNumHeadVO
	 */
	public ActivityNumItemVO[] setActNumItemVO(IMMFetchData[] vos,
			List<ActivityNumItemVO> listItemvo) {
		for (IMMFetchData vo : vos) {
			ActivityNumItemVO ActivityNumItemVO = new ActivityNumItemVO();
			// 当通过工作中心获取不到成本中心时,通过部门获取成本中心
			if (CMStringUtil.isNotEmpty(vo.getWkid())
					&& super.wkidAndCostCenridMap.get(vo.getWkid()) != null) {
				ActivityNumItemVO.setCcostcenterid(super.wkidAndCostCenridMap
						.get(vo.getWkid()));
			} else {
				ActivityNumItemVO.setCcostcenterid(super.wkidAndCostCenridMap
						.get(vo.getCdeptid()));
			}

			ActivityNumItemVO.setCactivityid(vo.getBdActivityId());
			ActivityNumItemVO.setNnum(vo.getWknum());
			ActivityNumItemVO.setCmeasdocid(vo.getCmeasdocid());
			ActivityNumItemVO.setIstatus(Integer.valueOf(String
					.valueOf(CMAllocStatusEnum.UNALLOCATE.value())));
			listItemvo.add(ActivityNumItemVO);
		}
		return listItemvo.toArray(new ActivityNumItemVO[0]);
	}

	@Override
	public void deleteBill(FetchParamVO paramVOs) throws BusinessException {
		if (FetchDataSchemaEnum.PERIODSCHEMA.equalsValue(paramVOs
				.getIfetchscheme())) {
			this.getActivityNumPubService().deleteActNumsData4FetchData(
					paramVOs, true);
		} else {
			this.getActivityNumPubService().deleteActNumsData4FetchData(
					paramVOs, false);
		}

	}

	/**
	 * 作业消耗单批量插入接口
	 */
	protected IActivityNumRewriteForMMFetch getActivityNumPubService() {
		if (this.activityNumPubService == null) {
			this.activityNumPubService = NCLocator.getInstance().lookup(
					IActivityNumRewriteForMMFetch.class);
		}
		return this.activityNumPubService;
	}

	@Override
	public IFetchData[] transDatas(CircularlyAccessibleValueObject[] datas,
			FetchParamVO paramVo) {
		List<AggActNumVOAdapter> aggActNumVOList = new ArrayList<AggActNumVOAdapter>();
		for (CircularlyAccessibleValueObject cadata : datas) {
			MMFetchDataVO data = (MMFetchDataVO) cadata;
			AggActNumVOAdapter vo = new AggActNumVOAdapter();
			// 入库类型
			vo.setFinstoragetype(this.getFinstoragetype(data
					.getFinstoragetype()));
			// 设置类型，区别错误信息和警告信息
			vo.setImestype(CMMesTypeEnum.ERROR.toIntValue());
			vo.setBdActivityId(data.getBdActivityId());
			vo.setCmoid(data.getCmoid());
			vo.setCmocode(data.getCmocode());
			vo.setMaterialid(data.getMaterialid());
			vo.setHeadwkid(data.getHeadwkid());
			vo.setWkid(data.getWkid());
			vo.setCdeptid(data.getCdptid());
			vo.setWknum(data.getWknum());
			vo.setResourcetype(Integer.valueOf(CMSourceTypeEnum.PRO_REPORT
					.getEnumValue().getValue()));// 完工报告->变更为：生产制造
			// 物料辅助属性
			vo.setCprojectid(data.getCprojectid());
			vo.setCproductorid(data.getCproductorid());
			vo.setCvendorid(data.getCvendorid());
			vo.setCcustomerid(data.getCcustomerid());
			vo.setVfree1(data.getVfree1());
			vo.setVfree2(data.getVfree2());
			vo.setVfree3(data.getVfree3());
			vo.setVfree4(data.getVfree4());
			vo.setVfree5(data.getVfree5());
			vo.setVfree6(data.getVfree6());
			vo.setVfree7(data.getVfree7());
			vo.setVfree8(data.getVfree8());
			vo.setVfree9(data.getVfree9());
			vo.setVfree10(data.getVfree10());
			aggActNumVOList.add(vo);
		}
		// 设置计量单位
		this.getMeasureId(aggActNumVOList);
		return aggActNumVOList.toArray(new AggActNumVOAdapter[0]);

	}

	/**
	 * 获取计量单位
	 */
	private void getMeasureId(List<AggActNumVOAdapter> datas) {
		List<String> actIdList = new ArrayList<String>();
		for (AggActNumVOAdapter cadata : datas) {
			actIdList.add(cadata.getBdActivityId());
		}
		BDActivityVO[] actVoList = null;
		try {
			actVoList = BDAdapter.queryBDActivityVOByPK(actIdList);
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		if (actVoList == null || actVoList.length == 0) {
			return;
		}
		Map<String, String> actMap = new HashMap<String, String>();
		for (BDActivityVO vo : actVoList) {
			actMap.put(vo.getCactivityid(), vo.getCmeasureid());
		}
		for (AggActNumVOAdapter cadata : datas) {
			cadata.setCmeasdocid(actMap.get(cadata.getBdActivityId()));
		}
	}

}
