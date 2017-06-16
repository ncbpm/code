package nc.bs.cm.fetchdata.checkandfetch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bd.framework.base.CMArrayUtil;
import nc.bd.framework.base.CMCollectionUtil;
import nc.bd.framework.base.CMNumberUtil2;
import nc.bd.framework.base.CMValueCheck;
import nc.bs.cm.fetchdata.fetchcheck.AbstractCheckStrategy;
import nc.cmpub.business.adapter.BDAdapter;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.vo.cm.costobject.entity.CostObjectGenerateVO;
import nc.vo.cm.costobject.enumeration.CostObjInStorageTypeEnum;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.cm.fetchdata.entity.PullDataErroInfoVO;
import nc.vo.cm.fetchdata.entity.adapter.IFIFetchData;
import nc.vo.cm.fetchdata.entity.adapter.IFetchData;
import nc.vo.cm.fetchdata.entity.adapter.StuffVOAdapter;
import nc.vo.cm.fetchdata.enumeration.CMMesTypeEnum;
import nc.vo.cm.stuff.entity.StuffAggVO;
import nc.vo.ia.detailledger.para.cm.GetDataPara;
import nc.vo.ia.detailledger.view.cm.CMDataVO;
import nc.vo.ia.pub.util.ToArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

/**
 * ����ȡ��
 */
public class DingeFIFetch extends AbstractFIFetch {
	
	
	@Override
	public PullDataErroInfoVO[] checkAndFetchCommon(FetchParamVO paramvo,
			boolean isCheckFlag) throws BusinessException {
     
        this.pkGroup = paramvo.getPk_group();
        this.pkOrg = paramvo.getPk_org();
        this.pkOrgv = paramvo.getPk_org_v();
        this.businessdate = this.getbusinessdate(paramvo);
        this.getBookAndCostDomain(paramvo);
        this.financeOrgId = BDAdapter.getFinanceOrgIDsByStockOrgIDs(new String[] {
            this.pkOrg
        });
        Map<IFetchData, PullDataErroInfoVO> totalErrorMap = new HashMap<IFetchData, PullDataErroInfoVO>();
        Map<Integer, List<String>> billTypeMap = paramvo.getBillTypeMap();

        // ȡĳ���������͵Ĳ���
        GetDataPara getDataParamVO = this.getFIDataParamVO(paramvo, isCheckFlag, 2);
      
        //��ѯ����
        CMDataVO[] dataVOArr = queryCMDataVO(getDataParamVO,paramvo);
        //��������
        Map<IFetchData, PullDataErroInfoVO> errorMap =
                this.batchExecute(paramvo, isCheckFlag, dataVOArr, 2);
        if (CMValueCheck.isNotEmpty(errorMap)) {
            totalErrorMap.putAll(errorMap);
        }        // ����ȡ��״̬(��תʱ�����±���״̬)
        if (!paramvo.isForTrans() && !isCheckFlag) {
            boolean sign = true;
            for (Map.Entry<IFetchData, PullDataErroInfoVO> e : totalErrorMap.entrySet()) {
                if (e.getKey().getImestype().equals(CMMesTypeEnum.ERROR.toIntValue())) {
                    sign = false;
                    break;
                }
            }
            if (sign) {
                this.saveFetchStatus(paramvo, isCheckFlag);
            }
        }
        return totalErrorMap.values().toArray(new PullDataErroInfoVO[totalErrorMap.size()]);
    
       }
	
	
	  private Map<IFetchData, PullDataErroInfoVO> batchExecute(FetchParamVO paramvo, boolean isCheckFlag,
	            CMDataVO[] dataVOArr, int billType) throws BusinessException {
	        Map<IFetchData, PullDataErroInfoVO> errorMap = new HashMap<IFetchData, PullDataErroInfoVO>();
	        if (CMValueCheck.isNotEmpty(dataVOArr)) {
	            this.setBillTypeToReturnDatas(dataVOArr, billType);// ���õ�������
	            List<Map<IFetchData, PullDataErroInfoVO>> tmpListVos = null;
	            List<CircularlyAccessibleValueObject> needDealList = new ArrayList<CircularlyAccessibleValueObject>();
	            List<IFIFetchData> allTransDatas = new ArrayList<IFIFetchData>();
	            for (CircularlyAccessibleValueObject data : dataVOArr) {
	                if (needDealList.size() == AbstractCheckAndFetch.CAL_NUM) {
	                    // ����ȡ������ת��Ϊ�м�����
	                    IFIFetchData[] transDatas =
	                            this.transDataByBillType(needDealList.toArray(new CircularlyAccessibleValueObject[0]),
	                                    paramvo);
	                    tmpListVos = this.getErrorInfo(transDatas, paramvo.getDaccountperiod(), isCheckFlag);
	                    allTransDatas.addAll(Arrays.asList(transDatas));
	                    if (tmpListVos != null && tmpListVos.get(0) != null) {
	                        errorMap.putAll(tmpListVos.get(0));
	                    }
	                    needDealList.clear();
	                    needDealList.add(data);
	                }
	                else {
	                    needDealList.add(data);
	                }
	            }
	            if (needDealList.size() > 0) {
	                // ����������ʣ���Ʒ
	                IFIFetchData[] transDatas =
	                        this.transDataByBillType(needDealList.toArray(new CircularlyAccessibleValueObject[0]), paramvo);
	                tmpListVos = this.getErrorInfo(transDatas, paramvo.getDaccountperiod(), isCheckFlag);
	                allTransDatas.addAll(Arrays.asList(transDatas));
	                if (CMCollectionUtil.isNotEmpty(tmpListVos) && tmpListVos.get(0) != null) {
	                    errorMap.putAll(tmpListVos.get(0));
	                }
	                needDealList.clear();
	            }
	            if (!isCheckFlag) {
	                // ת�����ݣ������浽���ݿ���
	                this.fetchProcessData(errorMap, allTransDatas.toArray(new IFIFetchData[allTransDatas.size()]), true,
	                        paramvo);
	            }

	        }
	        return errorMap;
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
        for (int i = 0; i < leftDatas.length; i++) {
            // �������ĵ��е��ֶ�
            StuffVOAdapter adapter = new StuffVOAdapter(new StuffAggVO());
            // �������ͣ����������Ϣ�;�����Ϣ
            adapter.setImestype(CMMesTypeEnum.ERROR.toIntValue());
            adapter.setPk_org(this.pkOrg);
//            // ��������
//            adapter.setCmaterialid(leftDatas[i].getCinventoryid());
//            // �������ϵĸ�������
//            this.setVbfrees(adapter, leftDatas[i]);
//            // ���ò�Ʒ�ĸ�������
//            this.setVmainfrees(adapter, leftDatas[i], FetchKeyConst.CM_IA_ForStuffProd);
            // ������λ
//            adapter.setCmeasdocid(leftDatas[i].getCunitid());
//            // ����
//            String batchCode = leftDatas[i].getVproducebatch();
//            adapter.setVproducebatch(batchCode);
            // ���ù�������
            adapter.setWkid(leftDatas[i].getCworkcenterid());
            // ��������
            adapter.setCdeptid(leftDatas[i].getCdeptid());
            // ����Ҫ��
            adapter.setCelementid(paramvo.getAttributeValue("pk_factor") == null ?"":paramvo.getAttributeValue("pk_factor").toString());
              
            // �ɱ�����
            adapter.setCcostcenterid(leftDatas[i].getCcostcenterid());
            // ��������
            adapter.setCtrantypeid(leftDatas[i].getCtrantypeid());
            // ����������
            adapter.setCmocode(leftDatas[i].getVproduceordercode());
            // ��ƷID
            adapter.setCproductid(leftDatas[i].getCbomcodeid());
   
            // ʵ�ʽ��
            adapter.setNmoney(leftDatas[i].getNmny());
       
            // �������
            adapter.setFinstoragetype(CostObjInStorageTypeEnum.HOME_MAKE.toIntValue());

            adapter.setNnum(leftDatas[i].getNnum()); // ����
            if (null == adapter.getNnum() || adapter.getNnum().compareTo(UFDouble.ZERO_DBL) == 0) {
                adapter.setNprice(null);
            }
            else {
                adapter.setNprice(CMNumberUtil2.div(adapter.getNmoney(), adapter.getNnum())); // ��� /����=����
            }
            adapter.setNplanmoney(null); // ���˲��ϳ���������ȡ�ƻ����

            adapter.setCproductgroupid(leftDatas[i].getCproductgroupid());// �ɱ�����ɱ�����
            adapter.setBauditflag(leftDatas[i].getBauditflag());
            // ��������
            adapter.setCbilltypecode(2);
            // �ɱ���
            adapter.setCsrccostdomainid(leftDatas[i].getPk_org());
            // ��������˲�
            stuffVOList.add(adapter);
        }
        retList.addAll(stuffVOList);
            return retList.toArray(new StuffVOAdapter[0]);
    
	}
	
	

    // �������ϵĸ�������
    private void setVbfrees(StuffVOAdapter adapter, CMDataVO vo) {
        // adapter.setCcustomerid((String) vo.getAttributeValue("ccustomerid"));
        adapter.setCcustomerid(vo.getCasscustid());
        adapter.setCprojectid(vo.getCprojectid());
        adapter.setCvendorid(vo.getCvendorid());
        adapter.setCproductorid(vo.getCproductorid());
        adapter.setVbfree1(vo.getVfree1());
        adapter.setVbfree2(vo.getVfree2());
        adapter.setVbfree3(vo.getVfree3());
        adapter.setVbfree4(vo.getVfree4());
        adapter.setVbfree5(vo.getVfree5());
        adapter.setVbfree6(vo.getVfree6());
        adapter.setVbfree7(vo.getVfree7());
        adapter.setVbfree8(vo.getVfree8());
        adapter.setVbfree9(vo.getVfree9());
        adapter.setVbfree10(vo.getVfree10());
    }

    // �ò�Ʒ�ĸ�������
    private void setVmainfrees(StuffVOAdapter adapter, CMDataVO vo, String[][] strs) {
        for (String[] str : strs) {
            adapter.setAttributeValue(str[0], vo.getAttributeValue(str[1]));
        }
    }


	private CMDataVO[] queryCMDataVO(GetDataPara para, FetchParamVO fetchPara) {
		// TODO �Զ����ɵķ������
		SqlBuilder sql = new SqlBuilder();
		sql.append(" select d.pk_org, d.pk_book, d.ccostcenterid, d.cdeptid ,SUM(d.nnum) nnum");
		sql.append(" FROM ia_detailledger d	");
		sql.append(" INNER JOIN ia_i3bill_b b  ON d.cbill_bid= b.cbill_bid");
		sql.append(" WHERE d.dr              = 0");
		sql.append(" and d.cbilltypecode", "I3");
		sql.append(" and d.pk_org", para.getPk_orgs());
		sql.append(" and d.pk_book", para.getPk_book());
		sql.append(" and d.caccountperiod", para.getCaccountperiod());
		sql.append(" and d.cstockorgid", para.getCstockorgid());
		if (para.getBeginDate() != null) {
			sql.append(" and d.dbizdate", ">=", para.getBeginDate().toString());
		}
		if (para.getEndDate() != null) {
			sql.append(" and d.dbizdate", "<=", para.getEndDate().toString());
		}
		sql.append(" GROUP BY d.pk_org,  d.pk_book,d.ccostcenterid,  d.cdeptid");

		DataAccessUtils util = new DataAccessUtils();
		IRowSet rowset = util.query(sql.toString());
		CMDataVO[] vos = constructVOs(para, rowset);
		//��ѯ�ɱ������Լ��ɱ��������Ҫ�ض�Ӧ�ĳɱ�BOM�� ����Ҫ�صĽ��
		SqlBuilder  pricesql = new SqlBuilder();
		pricesql.append("SELECT fee.nmoney FROM sca_bom_fee fee");//�ɱ�BOM������ϸ
		pricesql.append(" LEFT JOIN sca_bom bom ON fee.ccmbomid = bom.ccmbomid ");//�ɱ�BOM
		pricesql.append(" left join  cm_costobject costo");//�ɱ����� �����Ϻ��������Զ���ͬ
		pricesql.append(" on (bom.pk_org= costo.pk_org and bom.cmaterialid = costo.cmaterialid and   ");
		pricesql.append(" and bom.vfree1  = costo.vfree1  ");
		pricesql.append(" and bom.vfree2 = costo.vfree2    ");
		pricesql.append(" and bom.vfree3 = costo.vfree3    ");
		pricesql.append(" and  bom.vfree4 = costo.vfree4 ");
		pricesql.append(" and bom.vfree5 = costo.vfree5 ");
		pricesql.append(" and bom.vfree6 = costo.vfree6");
		pricesql.append(" and bom.vfree7 = costo.vfree7  ");
		pricesql.append(" and  bom.vfree8 = costo.vfree8  ");
		pricesql.append(" and bom.vfree9 = costo.vfree9    ");
		pricesql.append(" and bom.vfree10 = costo.vfree10 ) ");
		pricesql.append(" and bom.pk_org", para.getPk_orgs());
		pricesql.append(" and costo.ccostobjectid","1001A41000000000CB8M");
		pricesql.append(" and fee.celementid", "1001A4100000000019NB");
		IRowSet rowset2 = util.query(pricesql.toString());
		
		UFDouble ncost = UFDouble.ZERO_DBL;
		while(rowset2.next()){
			ncost = rowset2.getUFDouble(0);
		}
		//
		for(CMDataVO vo: vos){
			vo.setNcost(ncost);
			vo.setNmny(vo.getNnum().multiply(ncost));
		}
		return vos;
	}

	
	private CMDataVO[] constructVOs(GetDataPara para, IRowSet rowset) {
		String[] groupfields = new String[] { "pk_org", "pk_book",
				"ccostcenterid", "cdeptid" };
		String[] sumFields = new String[] { "nnum" };
		List<CMDataVO> list = new ArrayList<CMDataVO>();
		while (rowset.next()) {
			CMDataVO vo = new CMDataVO();
			int len = groupfields.length;
			for (int i = 0; i < len; i++) {
				String name = groupfields[i];
				Object value = rowset.getString(i);
				if (value != null) {
					vo.setAttributeValue(name, value);
				}
			}

			vo.setAttributeValue(CMDataVO.BWWFLAG, rowset.getUFBoolean(len));
			for (int i = 0; i < sumFields.length; i++) {
				String name = sumFields[i];
				Object value = rowset.getUFDouble(len + 1 + i);
				if (value != null) {
					vo.setAttributeValue(name, value);
				}
			}

			list.add(vo);
		}

		CMDataVO[] vos = ToArrayUtil.convert(list, CMDataVO.class);
		return vos;
	}


	@Override
	public void deleteBill(FetchParamVO paramvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	protected CircularlyAccessibleValueObject[] getFIDataOfOutSystem(
			GetDataPara paramVO, int billType) throws BusinessException {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public List<String> getSubGroupByFields(int billType) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	protected CostObjectGenerateVO[] getAndSetCostOjbParam(
			IFetchData[] correctData, AbstractCheckStrategy strategy,
			Map<IFetchData, PullDataErroInfoVO> vbFreeErrorVoMap) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	protected IFetchData[] transDatas(CircularlyAccessibleValueObject[] datas,
			FetchParamVO paramvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	protected void transVOInserDB(IFIFetchData[] correctData,
			FetchParamVO paramVo) throws BusinessException {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	protected List<Map<IFetchData, PullDataErroInfoVO>> getErrorInfo(
			IFIFetchData[] datas, String cperiod, boolean isCheckFlag)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		return null;
	}
	
	
	
}
