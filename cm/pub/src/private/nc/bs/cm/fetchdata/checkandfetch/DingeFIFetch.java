package nc.bs.cm.fetchdata.checkandfetch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bd.framework.base.CMArrayUtil;
import nc.bd.framework.base.CMCollectionUtil;
import nc.bd.framework.base.CMNumberUtil2;
import nc.bd.framework.base.CMValueCheck;
import nc.bd.framework.db.CMSqlBuilder;
import nc.bs.cm.fetchdata.factory.FetchPersistentFactory;
import nc.bs.cm.fetchdata.fetchPersistent.AbstractFetchPersistent;
import nc.bs.cm.fetchdata.fetchcheck.CLCKCheckStrategy;
import nc.cmpub.business.adapter.BDAdapter;
import nc.cmpub.business.enumeration.CMSourceTypeEnum;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.vo.cm.costobject.enumeration.CostObjInStorageTypeEnum;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.cm.fetchdata.entity.PullDataErroInfoVO;
import nc.vo.cm.fetchdata.entity.PullDataStateVO;
import nc.vo.cm.fetchdata.entity.adapter.IFIFetchData;
import nc.vo.cm.fetchdata.entity.adapter.IFetchData;
import nc.vo.cm.fetchdata.entity.adapter.StuffVOAdapter;
import nc.vo.cm.fetchdata.enumeration.CMMesTypeEnum;
import nc.vo.cm.fetchdata.enumeration.FetchDataSchemaEnum;
import nc.vo.cm.stuff.entity.StuffAggVO;
import nc.vo.cm.stuff.entity.StuffHeadVO;
import nc.vo.cm.stuff.entity.StuffItemVO;
import nc.vo.ia.detailledger.para.cm.GetDataPara;
import nc.vo.ia.detailledger.view.cm.CMDataVO;
import nc.vo.ia.pub.util.ToArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

public class DingeFIFetch extends MaterialOutFIFetch {

	@Override
	public PullDataErroInfoVO[] checkAndFetchCommon(FetchParamVO paramvo,
			boolean isCheckFlag) throws BusinessException {

		Map<IFetchData, PullDataErroInfoVO> totalErrorMap = new HashMap<IFetchData, PullDataErroInfoVO>();
		// ȡĳ���������͵Ĳ���
		GetDataPara getDataParamVO = this.getFIDataParamVO(paramvo,
				isCheckFlag, 2);

		// ��ѯ����
		CMDataVO[] dataVOArr = queryCMDataVO(getDataParamVO, paramvo);
		// ��������
		Map<IFetchData, PullDataErroInfoVO> errorMap = this.batchExecute(
				paramvo, isCheckFlag, dataVOArr, 7);
		if (CMValueCheck.isNotEmpty(errorMap)) {
			totalErrorMap.putAll(errorMap);
		} // ����ȡ��״̬(��תʱ�����±���״̬)
		if (!paramvo.isForTrans() && !isCheckFlag) {
			boolean sign = true;
			for (Map.Entry<IFetchData, PullDataErroInfoVO> e : totalErrorMap
					.entrySet()) {
				if (e.getKey().getImestype()
						.equals(CMMesTypeEnum.ERROR.toIntValue())) {
					sign = false;
					break;
				}
			}
			if (sign) {
				this.saveFetchStatus(paramvo, isCheckFlag);
			}
		}
		return totalErrorMap.values().toArray(
				new PullDataErroInfoVO[totalErrorMap.size()]);

	}
	
	@Override
	protected void saveFetchStatus(FetchParamVO paramvo, boolean isCheckFlag)
			throws BusinessException {
        if (!isCheckFlag) {
            // modify by zhangchdV65 2015.01.21
            // ��ѯ���ݿ��pccm_fetchinfo���е�����
            Set<String> cbillTypeSet = new HashSet<String>();
            // Ҫ�����״̬
            Set<PullDataStateVO> savePullDataStateSet = new HashSet<PullDataStateVO>();
            // Ҫ���µ�״̬ 	
            Set<PullDataStateVO> updatePullDataStateSet = new HashSet<PullDataStateVO>();
            CMSqlBuilder sb = new CMSqlBuilder();
            sb.select();
            sb.append(nc.vo.cmpub.fetchdata.PullDataStateVO.CBILLTYPE);
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
            DataAccessUtils dataAccessUtils = new DataAccessUtils();
            IRowSet result = dataAccessUtils.query(sb.toString());
            while (result.next()) {
                cbillTypeSet.add(result.getString(0));
            }

            for (PullDataStateVO pullDataStateVOArr : paramvo.getPullDataStateVOArr()) {
                // ����״̬
                String cbilltype =
                        pullDataStateVOArr.getCbilltype() == null ? "" : pullDataStateVOArr.getCbilltype().toString();
                if (!cbillTypeSet.contains(cbilltype)) {
                    savePullDataStateSet.add(pullDataStateVOArr);
                }
                // ����״̬
                else {
                    updatePullDataStateSet.add(pullDataStateVOArr);
                }
            }

            if (CMValueCheck.isNotEmpty(savePullDataStateSet)) {
                this.saveStatusDB(paramvo.getAllFetchParamVo(), paramvo.getPullDataStateVOArr());
            }

            if (CMValueCheck.isNotEmpty(updatePullDataStateSet)) {
                AbstractFetchPersistent.updateFetchStates(updatePullDataStateSet
                        .toArray(new PullDataStateVO[savePullDataStateSet.size()]));
            }
        }
    }
	
	 /**
     * ����ȡ��״̬��
     */
    private void saveStatusDB(FetchParamVO paramvo, PullDataStateVO[] pullDataStateVOArr) throws BusinessException {
        Integer pullDataType = paramvo == null ? null : paramvo.getPulldatatype();
        AbstractFetchPersistent tsp = FetchPersistentFactory.createFetchPersistentFactory(pullDataType);
        tsp.saveStatus(paramvo, pullDataStateVOArr);
    }

	@Override
	protected GetDataPara getFIDataParamVO(FetchParamVO paramvo,
			boolean isCheckFlag, int billType) throws BusinessException {
		// TODO �Զ����ɵķ������
		this.pkGroup = paramvo.getPk_group();
		this.pkOrg = paramvo.getPk_org();
		this.pkOrgv = paramvo.getPk_org_v();
		this.businessdate = this.getbusinessdate(paramvo);
		this.getBookAndCostDomain(paramvo);
		this.financeOrgId = BDAdapter
				.getFinanceOrgIDsByStockOrgIDs(new String[] { this.pkOrg });
		Integer fetchscheme = paramvo.getIfetchscheme();
		UFDate beginDay = new UFDate();
		UFDate endDay = new UFDate();
		// �����ȡ�������� ��ʼʱ�䣬����ʱ��Ϊ����·ݵĿ�ʼʱ��ͽ���ʱ�䡣
		if (FetchDataSchemaEnum.PERIODSCHEMA.equalsValue(fetchscheme)) {
			beginDay = paramvo.getBeginEndDate()[0];
			endDay = paramvo.getBeginEndDate()[1];
		} else if (FetchDataSchemaEnum.WEEKSCHEMA.equalsValue(fetchscheme)) {
			// ����ȡ����ֻ��һ������
			beginDay = paramvo.getBeginenddate().get(0).get(0); // ����ȡ����ֻ��һ������
			endDay = paramvo.getBeginenddate().get(0).get(1);

		}

		GetDataPara icParamvos = new GetDataPara();
		icParamvos.setGroupFields(null);
		icParamvos.setPk_book(this.acountBook);
		icParamvos.setPk_orgs(this.costDomain);
		icParamvos.setCstockorgid(this.pkOrg);
		icParamvos.setBeginDate(beginDay);
		icParamvos.setEndDate(endDay);
		// ����ڼ�
		icParamvos.setCaccountperiod(paramvo.getDaccountperiod());
		return icParamvos;
	}

	private CMDataVO[] queryCMDataVO(GetDataPara para, FetchParamVO fetchPara) {
		// TODO �Զ����ɵķ������
		SqlBuilder sql = new SqlBuilder();
		sql.append("SELECT ph.pk_group ,  ph.pk_org,  ph.cperiod as caccountperiod,");// ���ţ���֯���ڼ�
		sql.append("  ph.ccostcenterid as ccostcenterid,");// �ɱ�����
		sql.append(" (select def1 from resa_costcenter r where r.pk_costcenter =ph.ccostcenterid) as cdeptid ,");// ���˲���
		sql.append("  pb.ccostobjectid as ccostobjectid,");// �ɱ�����
		sql.append(" pb.nnum*fb.nfactor as nmny,");
		sql.append("  pb.nnum ,");// �깤��
		sql.append("  costb.cmaterialid,");// �ɱ���������
		sql.append("  costb.vfree1,");// �ɱ��������Ϲ��
		sql.append("  fb.nfactor");// ϵ��
		sql.append(" FROM cm_product ph ");//�깤��
		sql.append(" INNER JOIN cm_product_b pb  ON ph.cproductid = pb.cproductid");
		sql.append(" INNER JOIN cm_costobject costb ON pb.ccostobjectid=costb.ccostobjectid");//--�ɱ�����
		sql.append(" INNER JOIN  ");   //--����ϵ��
		sql.append(" (SELECT *  FROM cm_allocfac_b facb  WHERE NVL(dr,0)   =0  AND facb.callocfacid IN    ( SELECT callocfacid FROM cm_allocfac WHERE vcode='RF006')  ) fb");
		sql.append(" ON (ph.pk_group =fb.pk_group  AND ph.pk_org   =fb.pk_org AND costb.cmaterialid =fb.cmaterialid AND costb.vfree1 =fb.vbfree1)");
		sql.append(" where ");
		sql.append(" nvl(ph.dr,0)=0  and nvl(pb.dr,0)=0 ");
		sql.append(" and ph.pk_group",this.pkGroup);
		sql.append(" and ph.pk_org",this.pkOrg);
		sql.append(" and ph.cperiod",fetchPara.getDaccountperiod());
		//����
//		sql.append(" and pb.ccostobjectid","1001A110000000162TFI");
		DataAccessUtils util = new DataAccessUtils();
		IRowSet rowset = util.query(sql.toString());
		CMDataVO[] vos = constructVOs(para, rowset);
		
		return vos;
	}

	private CMDataVO[] constructVOs(GetDataPara para, IRowSet rowset) {
		String[] groupfields = new String[] {"pk_group", "pk_org", "caccountperiod","ccostcenterid",
				 "cdeptid","ccostobjectid" };
		String[] sumFields = new String[] { "nmny" };
		List<CMDataVO> list = new ArrayList<CMDataVO>();
		while (rowset.next()) {
			CMDataVO vo = new CMDataVO();
			int len = groupfields.length;
			for (int i = 0; i < len; i++) {
				String name = groupfields[i];
				Object value = rowset.getString(i);
				if (value != null) {
					if("ccostobjectid".equalsIgnoreCase(name)){
						vo.setCcostobjectid(value.toString());
					}else{
						vo.setAttributeValue(name, value);

					}
				}
			}
			vo.setAttributeValue("nmny", rowset.getUFDouble(6));

			list.add(vo);
		}

		CMDataVO[] vos = ToArrayUtil.convert(list, CMDataVO.class);
		return vos;
	}

	private Map<IFetchData, PullDataErroInfoVO> batchExecute(
			FetchParamVO paramvo, boolean isCheckFlag, CMDataVO[] dataVOArr,
			int billType) throws BusinessException {
		Map<IFetchData, PullDataErroInfoVO> errorMap = new HashMap<IFetchData, PullDataErroInfoVO>();
		if (CMValueCheck.isNotEmpty(dataVOArr)) {
			this.setBillTypeToReturnDatas(dataVOArr, billType);// ���õ�������
			List<Map<IFetchData, PullDataErroInfoVO>> tmpListVos = null;
			List<CircularlyAccessibleValueObject> needDealList = new ArrayList<CircularlyAccessibleValueObject>();
			List<IFIFetchData> allTransDatas = new ArrayList<IFIFetchData>();
			for (CircularlyAccessibleValueObject data : dataVOArr) {
				if (needDealList.size() == AbstractCheckAndFetch.CAL_NUM) {
					// ����ȡ������ת��Ϊ�м�����
					IFIFetchData[] transDatas = this
							.transDataByBillType(
									needDealList
											.toArray(new CircularlyAccessibleValueObject[0]),
									paramvo);
					tmpListVos = this.getErrorInfo(transDatas,
							paramvo.getDaccountperiod(), isCheckFlag);
					allTransDatas.addAll(Arrays.asList(transDatas));
					if (tmpListVos != null && tmpListVos.get(0) != null) {
						errorMap.putAll(tmpListVos.get(0));
					}
					needDealList.clear();
					needDealList.add(data);
				} else {
					needDealList.add(data);
				}
			}
			if (needDealList.size() > 0) {
				// ����������ʣ���Ʒ
				IFIFetchData[] transDatas = this
						.transDataByBillType(
								needDealList
										.toArray(new CircularlyAccessibleValueObject[0]),
								paramvo);
				tmpListVos = this.getErrorInfo(transDatas,
						paramvo.getDaccountperiod(), isCheckFlag);
				allTransDatas.addAll(Arrays.asList(transDatas));
				if (CMCollectionUtil.isNotEmpty(tmpListVos)
						&& tmpListVos.get(0) != null) {
					errorMap.putAll(tmpListVos.get(0));
				}
				needDealList.clear();
			}
			if (!isCheckFlag) {
				// ת�����ݣ������浽���ݿ���
				this.fetchProcessData(errorMap, allTransDatas
						.toArray(new IFIFetchData[allTransDatas.size()]), true,
						paramvo);
			}

		}
		return errorMap;
	}

	public void transVOInserDB(IFIFetchData[] correctData, FetchParamVO paramvo)
			throws BusinessException {

		// �������⣺���ֱ�ӵ��ø÷�������ڣ����š���֯����Ϊ�յ�����
		this.pkGroup = paramvo.getPk_group();
		this.pkOrg = paramvo.getPk_org();
		this.pkOrgv = paramvo.getPk_org_v();

		// �浽list���������
		List<List<IFIFetchData>> allList = new ArrayList<List<IFIFetchData>>();
		allList.add(Arrays.asList(correctData));

		// ���е���Ҫ���浽���ݿ�����ĵ�����
		StuffAggVO[] aggVOs = this.dealEachList(allList, paramvo);
		aggVOs = this.setMoneyNullToData(aggVOs);
		this.reCalcItemPrice(aggVOs);

		// ����������ҵ���ĵ���
		// �������ݿ���޸ķ��������ڴ������ȡ������insert�����ڳɱ���ת�л�ȡ���ϳ��ⵥ������Ϊ���޸�����
		if (CMArrayUtil.isNotEmpty(aggVOs)) {
			this.modifyDB(aggVOs);
		}

	};

	/**
	 * ���¼��㵥��
	 */
	private void reCalcItemPrice(StuffAggVO[] aggVOs) {
		if (CMArrayUtil.isEmpty(aggVOs)) {
			return;
		}
		for (StuffAggVO aggvo : aggVOs) {
			CircularlyAccessibleValueObject[] cavoArr = aggvo
					.getAllChildrenVO();
			for (CircularlyAccessibleValueObject cavo : cavoArr) {
				StuffItemVO vo = (StuffItemVO) cavo;
				UFDouble nprice = null;
				UFDouble nplanprice = null;
				UFDouble ndiff = CMNumberUtil2.sub(vo.getNmoney(),
						vo.getNplanmoney());
				if (vo.getNmoney() != null && vo.getNnum() != null
						&& vo.getNnum().compareTo(UFDouble.ZERO_DBL) != 0) {
					nprice = CMNumberUtil2.div(vo.getNmoney(), vo.getNnum());
				}

				if (vo.getNplanmoney() != null && vo.getNnum() != null
						&& vo.getNnum().compareTo(UFDouble.ZERO_DBL) != 0) {
					nplanprice = CMNumberUtil2.div(vo.getNplanmoney(),
							vo.getNnum());
				}

				if (vo.getNmoney() == null && vo.getNplanmoney() == null) {
					ndiff = null;
				}
				if (CMValueCheck.isNotEmpty(nprice)) {
					nprice = nprice.setScale(this.scaleutil.getScaleVO()
							.getPriceScale(), UFDouble.ROUND_HALF_UP);
				}
				if (CMValueCheck.isNotEmpty(nplanprice)) {
					nplanprice = nplanprice.setScale(this.scaleutil
							.getScaleVO().getPriceScale(),
							UFDouble.ROUND_HALF_UP);
				}
				vo.setNprice(nprice); // ��� /����=����
				vo.setNplanprice(nplanprice); // �ƻ���� /����=�ƻ�����
				vo.setNdiff(ndiff); // ʵ�ʽ��-�ƻ����
			}
		}
	}

	@Override
	protected List<Map<IFetchData, PullDataErroInfoVO>> getErrorInfo(
			IFIFetchData[] datas, String cperiod, boolean isCheckFlag)
			throws BusinessException {
		List<Map<IFetchData, PullDataErroInfoVO>> list = new ArrayList<Map<IFetchData, PullDataErroInfoVO>>();

		// �洢���еĴ�����Ϣ
		Map<IFetchData, PullDataErroInfoVO> allMap = new HashMap<IFetchData, PullDataErroInfoVO>();

		Map<IFetchData, PullDataErroInfoVO> clckMap = super
				.getErrorInfoAndFilteredData(
						datas,
						new CLCKCheckStrategy(this.pkOrg, this.pkGroup,
								this.costDomain, this.acountBook), cperiod,
						isCheckFlag).get(0);
		allMap.putAll(clckMap);
		list.add(allMap);
		return list;
	}

	protected IFIFetchData[] transDataByBillType(
			CircularlyAccessibleValueObject[] datas, FetchParamVO paramvo) {
		// ת��Ϊ����
		CMDataVO[] leftDatas = Arrays.asList(datas).toArray(new CMDataVO[0]);
		List<StuffVOAdapter> retList = new ArrayList<StuffVOAdapter>();
		// �õ���֯��������Ҫ�ص�����

		// ÿ���������͵Ĵ���
		// ��ϵͳȡ��vo���� ����ϵͳȡ�����������õĶ��չ�ϵ
		// ͬʱ����ת������ѻ�ȡ��������ת��Ϊ���ĵ�vo
		if (CMArrayUtil.isEmpty(leftDatas)) {
			return null;
		}

		List<StuffVOAdapter> stuffVOList = new ArrayList<StuffVOAdapter>();
		// �����м����ݣ�ͬʱ�����ĵ�vo�����м�����
		PullDataStateVO pullData = paramvo.getPullDataStateVOArr()[0];
		for (int i = 0; i < leftDatas.length; i++) {
			// �������ĵ��е��ֶ�
			StuffAggVO bill = new StuffAggVO();
			bill.setParent(new StuffHeadVO());
			StuffItemVO itemVO = new StuffItemVO();
			bill.setChildrenVO(new StuffItemVO[] { itemVO });
			StuffVOAdapter adapter = new StuffVOAdapter(bill);
			// �������ͣ����������Ϣ�;�����Ϣ
			adapter.setImestype(CMMesTypeEnum.ERROR.toIntValue());
			adapter.setPk_org(this.pkOrg);
			// // ��������
			// adapter.setCmaterialid(leftDatas[i].getCinventoryid());
			// // �������ϵĸ�������
			// this.setVbfrees(adapter, leftDatas[i]);
			// // ���ò�Ʒ�ĸ�������
			// this.setVmainfrees(adapter, leftDatas[i],
			// FetchKeyConst.CM_IA_ForStuffProd);
			// ������λ
			// adapter.setCmeasdocid(leftDatas[i].getCunitid());
			// // ����
			// String batchCode = leftDatas[i].getVproducebatch();
			// adapter.setVproducebatch(batchCode);
			// ���ù�������
			adapter.setWkid(leftDatas[i].getCworkcenterid());
			// ��������
			adapter.setCdeptid(leftDatas[i].getCdeptid());
			// ����Ҫ��
			String pk_factor = pullData.getAttributeValue("pk_factor") == null ? ""
					: pullData.getAttributeValue("pk_factor").toString();
			adapter.setCelementid(pk_factor);
			// �ɱ�����
			adapter.setCcostcenterid(leftDatas[i].getCcostcenterid());
			//�ɱ�����
			adapter.setCcostobjectid(leftDatas[i].getCcostobjectid());

			// ��������
			adapter.setCtrantypeid(leftDatas[i].getCtrantypeid());
			// ����������
			adapter.setCmocode(leftDatas[i].getVproduceordercode());
			// ��ƷID
			adapter.setCproductid(leftDatas[i].getCbomcodeid());
			// ʵ�ʽ��
			adapter.setNmoney(leftDatas[i].getNmny());

			// �������
			adapter.setFinstoragetype(CostObjInStorageTypeEnum.HOME_MAKE
					.toIntValue());

			adapter.setNnum(leftDatas[i].getNnum()); // ����
			if (null == adapter.getNnum()
					|| adapter.getNnum().compareTo(UFDouble.ZERO_DBL) == 0) {
				adapter.setNprice(null);
			} else {
				adapter.setNprice(CMNumberUtil2.div(adapter.getNmoney(),
						adapter.getNnum())); // ��� /����=����
			}
			adapter.setNplanmoney(null); // ���˲��ϳ���������ȡ�ƻ����

			adapter.setCproductgroupid(leftDatas[i].getCproductgroupid());// �ɱ�����ɱ�����
			adapter.setBauditflag(leftDatas[i].getBauditflag());
			// ��������
			adapter.setCbilltypecode(1);
			// �ɱ���
			adapter.setCsrccostdomainid(leftDatas[i].getPk_org());
			// ��������˲�
			stuffVOList.add(adapter);
		}
		retList.addAll(stuffVOList);
		return retList.toArray(new StuffVOAdapter[0]);

	}

	@Override
	protected void setHeadVOSepcailValue(FetchParamVO paramvo,
			StuffHeadVO stuffHeadVO, IFIFetchData vo) throws BusinessException {
		// TODO �Զ����ɵķ������
		super.setHeadVOSepcailValue(paramvo, stuffHeadVO, vo);
		stuffHeadVO.setIsourcetype(Integer.valueOf(String
				.valueOf(CMSourceTypeEnum.HOME_MAKE.value())));
		stuffHeadVO.setVdef20("����");
	}

}
