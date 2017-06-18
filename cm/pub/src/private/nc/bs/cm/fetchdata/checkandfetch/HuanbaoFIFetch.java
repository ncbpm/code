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
 * ����ȡ��
 */
public class HuanbaoFIFetch  extends AbstractCheckAndFetch<IMMFetchData> {
	/**
	 * ��ҵ���ĵ��ӿ�
	 */
	private IActivityNumRewriteForMMFetch activityNumPubService = null;

	@Override
	public CircularlyAccessibleValueObject[] getData4OutSystem(
			FetchParamVO paramvo, boolean isCheckFlag) throws BusinessException {
		// �õ�mm����������깤�����߹����깤����
		return this.getFinishOrGxFinish(paramvo);
	}

	/**
	 * ����ά���Ľ������ͣ���ѯָ�����ĳ���ⵥ�ĳ����������������ȡ����ֵ��
	 * �������ݣ���ҵ+���ĳɱ�����+�ɱ�����+�깤�ɱ�����
	 * �ֵ����ݣ����ĳɱ�����+�ɱ�����
	 * 
	 * @param paramvo
	 *            FetchParamVO ȡ������
	 * @return wkidAndMMfetchDataVOMap ȡ���õ���vo
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

		// �����ȡ�������� ��ʼʱ�䣬����ʱ��Ϊ����·ݵĿ�ʼʱ��ͽ���ʱ�䡣
		if (FetchDataSchemaEnum.PERIODSCHEMA.equalsValue(fetchscheme)) {
			beginDay = paramvo.getBeginEndDate()[0];
			endDay = paramvo.getBeginEndDate()[1];
			costCenterid = paramvo.getCcostcenterid();
			this.setWkidAndDept(wkids, deptids, costCenterid);
		} else if (FetchDataSchemaEnum.WEEKSCHEMA.equalsValue(fetchscheme)) {
			// ����ȡ����ֻ��һ������
			beginDay = paramvo.getBeginenddate().get(0).get(0); // ����ȡ����ֻ��һ������
			endDay = paramvo.getBeginenddate().get(0).get(1);
			costCenterid = Arrays.asList(FIAdapter
					.getAllCostCenterByOrg(this.pkOrg));
			this.setWkidAndDept(wkids, deptids, costCenterid);
		}

		// ����ȡ��:������������Ϊ��ʱ,���ղ��Ž���ȡ��
		CircularlyAccessibleValueObject[] mmfetchDataVOs = this
				.getMMDataOfOutSystem(beginDay, endDay, this.pkOrg,
						this.pkGroup, wkids.toArray(new String[0]),
						deptids.toArray(new String[0]));
		return mmfetchDataVOs;
	}

	/**
	 * ��ϵͳȡ��
	 * 
	 * @param beginDay
	 *            ��ʼʱ��
	 * @param endDay
	 *            ����ʱ��
	 * @param pkOrg
	 *            ����
	 * @param pkGroup
	 *            ����
	 * @param wkids
	 *            ��������
	 * @throws BusinessException
	 *             ҵ���쳣
	 * @return ��ƽ���vo����
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
					// ����key�ֶΣ�����ͬ��keyֵ��vo���浽cachedVOMap����ҵ���ĵ��Ա���
					// ���ĳɱ����ġ��ɱ������������
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

	// ����������ҵͳ�Ƶ���
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
	 * ת��Ϊ��ҵ�������ĵ�
	 * 
	 * @param cachedVOMap
	 *            �ѻ���vo����ͬkey���ݣ��ŵ�һ��list��
	 * @param correctData
	 *            ��ȷ����
	 * @param paramvo
	 *            paramvo ȡ������
	 * @return ActivityNumAggVO[] aggvo
	 * @throws BusinessException
	 *             BusinessException
	 */
	public ActivityNumAggVO[] transActActNumVO(
			Map<String, List<IMMFetchData>> cachedVOMap,
			IMMFetchData[] correctData, final FetchParamVO paramvo)
			throws BusinessException {
		// ����processValues������VO�����ǳ�ʼ����
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
								key.append(vo.getCactivityid()); // ��ҵ����
								return key.toString();
							}

							@Override
							public String[] getSumAttrs(ActivityNumItemVO vo) {
								return new String[] { ActivityNumItemVO.NNUM }; // ��ҵ��
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
	 * ��֯�ɱ��������
	 * 
	 * @param datas
	 *            IMMFetchData ȡ������
	 * @return �ɱ�����vo
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
					.valueOf(ProductTypeEnum.MAINPRODUCT.value().toString()));// ����Ʒ��ʾ
			costObjectGenerateVOs[i].setCmainmaterialid(data.getMaterialid());// ��Ӧ����Ʒid
			// ��Ʒ��������
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
	 * ������ҵ���ĵ�headvo
	 * 
	 * @param paramvo
	 *            ȡ������
	 * @param datavos
	 *            ȡ����������
	 * @return ActivityNumHeadVO ��ҵ���ĵ���ͷ
	 */
	public ActivityNumHeadVO setActNumHeadVO(FetchParamVO paramvo,
			IMMFetchData[] datavos) {
		ActivityNumHeadVO ActivityNumHeadVO = new ActivityNumHeadVO();
		ActivityNumHeadVO.setCcostcenterid(datavos[0].getCcostcenterid()); // �ɱ�����
		ActivityNumHeadVO.setCcostobjectid(datavos[0].getCcostobjectid()); // �ɱ��������
		ActivityNumHeadVO.setPk_group(paramvo.getPk_group()); // ����
		ActivityNumHeadVO.setPk_org(paramvo.getPk_org()); // ��֯
		ActivityNumHeadVO.setPk_org_v(paramvo.getPk_org_v());
		ActivityNumHeadVO.setCperiod(paramvo.getDaccountperiod()); // ����ڼ�

		// ��Դ�����깤����
		ActivityNumHeadVO.setIsourcetype(datavos[0].getResourcetype());
		ActivityNumHeadVO.setStatus(CMStatusEnum.SFREEDOM.valueIndex()); // ����̬
		ActivityNumHeadVO.setDbusinessdate(this.getBusinessdate());

		// ��Ӵ����˴���ʱ��
		ActivityNumHeadVO.setCreator(AuditInfoUtil.getCurrentUser());
		ActivityNumHeadVO.setCreationtime(AuditInfoUtil.getCurrentTime());
		return ActivityNumHeadVO;
	}

	/**
	 * ������ҵ���ĵ�headvo
	 * 
	 * @param vos
	 *            ȡ������
	 * @param listItemvo
	 *            List<ActivityNumItemVO>
	 * @return ActivityNumHeadVO
	 */
	public ActivityNumItemVO[] setActNumItemVO(IMMFetchData[] vos,
			List<ActivityNumItemVO> listItemvo) {
		for (IMMFetchData vo : vos) {
			ActivityNumItemVO ActivityNumItemVO = new ActivityNumItemVO();
			// ��ͨ���������Ļ�ȡ�����ɱ�����ʱ,ͨ�����Ż�ȡ�ɱ�����
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
	 * ��ҵ���ĵ���������ӿ�
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
			// �������
			vo.setFinstoragetype(this.getFinstoragetype(data
					.getFinstoragetype()));
			// �������ͣ����������Ϣ�;�����Ϣ
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
					.getEnumValue().getValue()));// �깤����->���Ϊ����������
			// ���ϸ�������
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
		// ���ü�����λ
		this.getMeasureId(aggActNumVOList);
		return aggActNumVOList.toArray(new AggActNumVOAdapter[0]);

	}

	/**
	 * ��ȡ������λ
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
