package nc.bs.cm.fetchdata.checkandfetch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bd.framework.base.CMStringUtil;
import nc.bd.framework.base.CMValueCheck;
import nc.bd.framework.db.CMSqlBuilder;
import nc.bs.cm.fetchdata.factory.FetchPersistentFactory;
import nc.bs.cm.fetchdata.fetchPersistent.AbstractFetchPersistent;
import nc.bs.cm.fetchdata.fetchcheck.AbstractCheckStrategy;
import nc.bs.cm.fetchdata.fetchcheck.MMFetchCheckStrategy;
import nc.bs.cm.fetchdata.groupdata.IGroupStrategy;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.cmpub.business.adapter.BDAdapter;
import nc.cmpub.business.enumeration.CMAllocStatusEnum;
import nc.cmpub.business.enumeration.CMSourceTypeEnum;
import nc.cmpub.business.enumeration.CMStatusEnum;
import nc.impl.cm.fetchdata.PullDataTool;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pubitf.cm.activitynum.cm.mmfetch.IActivityNumRewriteForMMFetch;
import nc.vo.bd.bdactivity.entity.BDActivityVO;
import nc.vo.cm.activitynum.entity.ActivityNumAggVO;
import nc.vo.cm.activitynum.entity.ActivityNumHeadVO;
import nc.vo.cm.activitynum.entity.ActivityNumItemVO;
import nc.vo.cm.costobject.entity.CostObjectGenerateVO;
import nc.vo.cm.costobject.enumeration.CostObjInStorageTypeEnum;
import nc.vo.cm.fetchdata.entity.ChuyunFetchDataVO;
import nc.vo.cm.fetchdata.entity.FetchKeyVO;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.cm.fetchdata.entity.PullDataErroInfoVO;
import nc.vo.cm.fetchdata.entity.PullDataStateVO;
import nc.vo.cm.fetchdata.entity.adapter.AggActNumVOAdapter;
import nc.vo.cm.fetchdata.entity.adapter.IFetchData;
import nc.vo.cm.fetchdata.entity.adapter.IMMFetchData;
import nc.vo.cm.fetchdata.enumeration.CMMesTypeEnum;
import nc.vo.cm.fetchdata.enumeration.FetchDataSchemaEnum;
import nc.vo.ia.pub.util.ToArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.Constructor;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.util.AuditInfoUtil;

/**
 * ����ȡ��
 * 
 */
public class ChuyunFIFetch extends AbstractCheckAndFetch<IMMFetchData> {
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
	
	@Override
	public IFetchData[] transDatas(CircularlyAccessibleValueObject[] datas,
			FetchParamVO paramVo) {
		List<AggActNumVOAdapter> aggActNumVOList = new ArrayList<AggActNumVOAdapter>();
		PullDataStateVO vo2 = paramVo.getPullDataStateVOArr()[0];
		for (CircularlyAccessibleValueObject cadata : datas) {
			ChuyunFetchDataVO data = (ChuyunFetchDataVO) cadata;
			AggActNumVOAdapter vo = new AggActNumVOAdapter();
			
			 // ���ĳɱ�����
			vo.setCcostcenterid( data.getCcostcenterid());
			// �ɱ��������
			vo.setCcostobjectid(vo2.getPk_costobject());
			// �������
			vo.setFinstoragetype(CostObjInStorageTypeEnum.HOME_MAKE.toIntValue());
			// �������ͣ����������Ϣ�;�����Ϣ
			vo.setImestype(CMMesTypeEnum.ERROR.toIntValue());
			//��ҵ����
			vo.setBdActivityId(vo2.getPk_workitem());
			
//			vo.setCmoid(data.getCmoid());
//			vo.setCmocode(data.getCmocode());
			
//			vo.setMaterialid(data.getMaterialid());
			
			vo.setHeadwkid(data.getHeadwkid());
			//
			vo.setWkid(data.getWkid());
			vo.setCdeptid(vo2.getPk_serverdept());//������--�������ձ����깤�ɱ�����
			vo.setWknum(data.getWknum());
			vo.setResourcetype(Integer.valueOf(CMSourceTypeEnum.HOME_MAKE
					.getEnumValue().getValue()));// 
			// ���ϸ�������
//			vo.setCprojectid(data.getCprojectid());
//			vo.setCproductorid(data.getCproductorid());
//			vo.setCvendorid(data.getCvendorid());
//			vo.setCcustomerid(data.getCcustomerid());
//			vo.setVfree1(data.getVfree1());
//			vo.setVfree2(data.getVfree2());
//			vo.setVfree3(data.getVfree3());
//			vo.setVfree4(data.getVfree4());
//			vo.setVfree5(data.getVfree5());
//			vo.setVfree6(data.getVfree6());
//			vo.setVfree7(data.getVfree7());
//			vo.setVfree8(data.getVfree8());
//			vo.setVfree9(data.getVfree9());
//			vo.setVfree10(data.getVfree10());
			aggActNumVOList.add(vo);
		}
		// ���ü�����λ
		this.getMeasureId(aggActNumVOList);
		return aggActNumVOList.toArray(new AggActNumVOAdapter[0]);

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
	
	@Override
	protected void saveFetchStatus(FetchParamVO paramvo, boolean isCheckFlag)
			throws BusinessException {
        if (!isCheckFlag) {
            // modify by zhangchdV65 2015.01.21
            // ��ѯ���ݿ��pccm_fetchinfo���е�����
            // Ҫ�����״̬
            Set<PullDataStateVO> savePullDataStateSet = new HashSet<PullDataStateVO>();
            // Ҫ���µ�״̬
            Set<PullDataStateVO> updatePullDataStateSet = new HashSet<PullDataStateVO>();       
            CMSqlBuilder sb = new CMSqlBuilder();
            sb.select();
            sb.append(" * ");   
            sb.from(PullDataStateVO.getDefaultTableName());
            sb.where();
            sb.append(nc.vo.cmpub.fetchdata.PullDataStateVO.PK_GROUP, paramvo.getPk_group());
            sb.append(" and ");
            sb.append(nc.vo.cmpub.fetchdata.PullDataStateVO.PK_ORG, paramvo.getPk_org());
            sb.append(" and ");
            sb.append(nc.vo.cmpub.fetchdata.PullDataStateVO.IFETCHOBJTYPE, paramvo.getIfetchobjtype());
            sb.append(" and ");
            sb.append(nc.vo.cmpub.fetchdata.PullDataStateVO.CPERIOD, paramvo.getPullDataStateVOArr()[0].getCperiod());
            sb.append(" and ");
            sb.append(" dr = 0 ");
        
            BaseDAO dao = new BaseDAO();
            List<PullDataStateVO> queryVOS = (List<PullDataStateVO>) dao.executeQuery(sb.toString(), new BeanListProcessor(PullDataStateVO.class));
            
            //����ִ��ȡ����VO
            Map<FetchKeyVO, PullDataStateVO> clientVOS = PullDataTool.splitBykey( paramvo.getPullDataStateVOArr());
            //���ݿ����Ѿ����ڵ�VO
            Map<FetchKeyVO, PullDataStateVO> orignVOS = PullDataTool.splitBykey(queryVOS);
            
            for (FetchKeyVO key :clientVOS.keySet()) {
                // ����״̬                  
                if (!orignVOS.containsKey(key)) {
                    savePullDataStateSet.add(clientVOS.get(key));
                }
                // ����״̬
                else {
                    updatePullDataStateSet.add(clientVOS.get(key));
                }
            }

            if (CMValueCheck.isNotEmpty(savePullDataStateSet)) {
                this.saveStatusDB(paramvo.getAllFetchParamVo(), savePullDataStateSet
                        .toArray(new PullDataStateVO[savePullDataStateSet.size()]));
            }

            if (CMValueCheck.isNotEmpty(updatePullDataStateSet)) {
                AbstractFetchPersistent.updateFetchStates(updatePullDataStateSet
                        .toArray(new PullDataStateVO[savePullDataStateSet.size()]));
            }

        }
    }

	 private void saveStatusDB(FetchParamVO paramvo, PullDataStateVO[] pullDataStateVOArr) throws BusinessException {
	        Integer pullDataType = paramvo == null ? null : paramvo.getPulldatatype();
	        AbstractFetchPersistent tsp = FetchPersistentFactory.createFetchPersistentFactory(pullDataType);
	        tsp.saveStatus(paramvo, pullDataStateVOArr);
	    }
	/**
	 * ����ά���Ľ������ͣ���ѯָ�����ĳ���ⵥ�ĳ����������������ȡ����ֵ��
	 * �������ݣ���ҵ+���ĳɱ�����+�ɱ�����+�깤�ɱ�����
	 * �ֵ����ݣ����ĳɱ�����+�ɱ�����
	 * 
	 * @param paramvo
	 *            FetchParamVO ȡ������
	 * @return wkidAndChuyunFetchDataVOMap ȡ���õ���vo
	 * @throws BusinessException
	 *             BusinessException'
	 */
	public CircularlyAccessibleValueObject[] getFinishOrGxFinish(
			FetchParamVO paramvo) throws BusinessException {
		Integer fetchscheme = paramvo.getIfetchscheme();
		UFDate beginDay = new UFDate();
		UFDate endDay = new UFDate();


		// �����ȡ�������� ��ʼʱ�䣬����ʱ��Ϊ����·ݵĿ�ʼʱ��ͽ���ʱ�䡣
		if (FetchDataSchemaEnum.PERIODSCHEMA.equalsValue(fetchscheme)) {
			beginDay = paramvo.getBeginEndDate()[0];
			endDay = paramvo.getBeginEndDate()[1];
//			costCenterid = paramvo.getCcostcenterid();
//			this.setWkidAndDept(wkids, deptids, costCenterid);
		} else if (FetchDataSchemaEnum.WEEKSCHEMA.equalsValue(fetchscheme)) {
			// ����ȡ����ֻ��һ������
			beginDay = paramvo.getBeginenddate().get(0).get(0); // ����ȡ����ֻ��һ������
			endDay = paramvo.getBeginenddate().get(0).get(1);
//			costCenterid = Arrays.asList(FIAdapter
//					.getAllCostCenterByOrg(this.pkOrg));
//			this.setWkidAndDept(wkids, deptids, costCenterid);
		}
		PullDataStateVO pullData = paramvo.getPullDataStateVOArr()[0];

		// ����ȡ��:������������Ϊ��ʱ,���ղ��Ž���ȡ��
		CircularlyAccessibleValueObject[] ChuyunFetchDataVOs = this
				.getMMDataOfOutSystem(beginDay, endDay, this.pkOrg,
						this.pkGroup,pullData);
		return ChuyunFetchDataVOs;
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
	 * @return ��ƽ���vo����:��ΪҪ������ҵͳ�Ƶ������вο���ǰ���д�����ҵ��ͳ�Ʒ�ʽ
	 * 
	 */
	public CircularlyAccessibleValueObject[] getMMDataOfOutSystem(
			UFDate beginDay, UFDate endDay, String pkOrg, String pkGroup,
			PullDataStateVO pullData ) throws BusinessException {
		SqlBuilder sql = new SqlBuilder();
		sql.append(" select dp.pk_org,  dp.pk_costcenter as ccostcenterid,  sum(chuyun.nnum) wknum ");
		sql.append(" from  view_nc_zuoyechuyun chuyun");
		sql.append(" inner join  resa_ccdepts  dp on chuyun.cdptid = dp.pk_dept");
		sql.append(" where nvl(dp.dr,0)=0 ");
		sql.append(" and dp.pk_org", pullData.getPk_org());
		sql.append(" and chuyun.ctrantypeid", pullData.getCtranstypeid());
		sql.append(" and chuyun.taudittime  between '"+beginDay+"' and '"+endDay+"'");
		sql.append(" GROUP BY dp.pk_org,  dp.pk_costcenter");
		DataAccessUtils util = new DataAccessUtils();
		IRowSet rowset = util.query(sql.toString());
		ChuyunFetchDataVO[] vos = constructVOs(pullData, rowset);
		
		return vos;

	}
	
	private ChuyunFetchDataVO[] constructVOs(PullDataStateVO pullData, IRowSet rowset) {
		String[] groupfields = new String[] { "pk_org", "ccostcenterid"};
		String[] sumFields = new String[] { "wknum" };
		List<ChuyunFetchDataVO> list = new ArrayList<ChuyunFetchDataVO>();
		while (rowset.next()) {
			ChuyunFetchDataVO vo = new ChuyunFetchDataVO();
			int len = groupfields.length;
			for (int i = 0; i < len; i++) {
				String name = groupfields[i];
				Object value = rowset.getString(i);
				if (value != null) {
					vo.setAttributeValue(name, value);
				}
			}

			for (int i = 0; i < sumFields.length; i++) {
				String name = sumFields[i];
				Object value = rowset.getUFDouble(len  + i);
				if (value != null) {
					vo.setAttributeValue(name, value);
				}
			}
			list.add(vo);
		}
		ChuyunFetchDataVO[] vos = ToArrayUtil.convert(list, ChuyunFetchDataVO.class);
		return vos;
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
								return ChuyunFIFetch.this.setActNumHeadVO(
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

								return ChuyunFIFetch.this.setActNumItemVO(
										voList.toArray(new IMMFetchData[0]),
										listItemvo);
							}
						});
	}

	@Override
	protected List<Map<IFetchData, PullDataErroInfoVO>> getErrorInfo(
			IMMFetchData[] datas, String cperiod, boolean isCheckFlag)
			throws BusinessException {
	     // ��ȡȫ���ɱ�����
        super.getAllCostCenterIds(datas);
		return null;
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
		return null;
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
		ActivityNumHeadVO.setCcostcenterid(datavos[0].getCcostcenterid()); // ���ĳɱ�����
		ActivityNumHeadVO.setCcostobjectid(datavos[0].getCcostobjectid()); // �ɱ��������
		ActivityNumHeadVO.setPk_group(paramvo.getPk_group()); // ����
		ActivityNumHeadVO.setPk_org(paramvo.getPk_org()); // ��֯
		ActivityNumHeadVO.setPk_org_v(paramvo.getPk_org_v());
		ActivityNumHeadVO.setCperiod(paramvo.getDaccountperiod()); // ����ڼ�
		ActivityNumHeadVO.setVdef20("����");

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
