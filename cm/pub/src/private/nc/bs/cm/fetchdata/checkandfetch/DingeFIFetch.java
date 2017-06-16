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
 * 定额取数
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

        // 取某个单据类型的参数
        GetDataPara getDataParamVO = this.getFIDataParamVO(paramvo, isCheckFlag, 2);
      
        //查询数据
        CMDataVO[] dataVOArr = queryCMDataVO(getDataParamVO,paramvo);
        //保存数据
        Map<IFetchData, PullDataErroInfoVO> errorMap =
                this.batchExecute(paramvo, isCheckFlag, dataVOArr, 2);
        if (CMValueCheck.isNotEmpty(errorMap)) {
            totalErrorMap.putAll(errorMap);
        }        // 保存取数状态(结转时不更新保存状态)
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
	            this.setBillTypeToReturnDatas(dataVOArr, billType);// 设置单据类型
	            List<Map<IFetchData, PullDataErroInfoVO>> tmpListVos = null;
	            List<CircularlyAccessibleValueObject> needDealList = new ArrayList<CircularlyAccessibleValueObject>();
	            List<IFIFetchData> allTransDatas = new ArrayList<IFIFetchData>();
	            for (CircularlyAccessibleValueObject data : dataVOArr) {
	                if (needDealList.size() == AbstractCheckAndFetch.CAL_NUM) {
	                    // 将获取的数据转换为中间数据
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
	                // 处理分批后的剩余产品
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
	                // 转化数据，并保存到数据库中
	                this.fetchProcessData(errorMap, allTransDatas.toArray(new IFIFetchData[allTransDatas.size()]), true,
	                        paramvo);
	            }

	        }
	        return errorMap;
	    }

	
	protected IFIFetchData[] transDataByBillType(
			CircularlyAccessibleValueObject[] datas, FetchParamVO paramvo) {
        // 转化为数组
        CMDataVO[] leftDatas = Arrays.asList(datas).toArray(new CMDataVO[0]);
        List<StuffVOAdapter> retList = new ArrayList<StuffVOAdapter>();
        // 得到组织参数核算要素的主键
      
        // 每个单据类型的处理
        // 外系统取数vo对照 ，外系统取数设置中设置的对照关系
        // 同时根据转换规则把获取到的数据转化为消耗单vo
        if (CMArrayUtil.isEmpty(leftDatas)) {
            return null;
        }
   
        List<StuffVOAdapter> stuffVOList = new ArrayList<StuffVOAdapter>();
        // 创建中间数据，同时把消耗单vo放入中间数据
        for (int i = 0; i < leftDatas.length; i++) {
            // 设置消耗单有的字段
            StuffVOAdapter adapter = new StuffVOAdapter(new StuffAggVO());
            // 设置类型，区别错误信息和警告信息
            adapter.setImestype(CMMesTypeEnum.ERROR.toIntValue());
            adapter.setPk_org(this.pkOrg);
//            // 设置物料
//            adapter.setCmaterialid(leftDatas[i].getCinventoryid());
//            // 设置物料的辅助属性
//            this.setVbfrees(adapter, leftDatas[i]);
//            // 设置产品的辅助属性
//            this.setVmainfrees(adapter, leftDatas[i], FetchKeyConst.CM_IA_ForStuffProd);
            // 计量单位
//            adapter.setCmeasdocid(leftDatas[i].getCunitid());
//            // 批次
//            String batchCode = leftDatas[i].getVproducebatch();
//            adapter.setVproducebatch(batchCode);
            // 设置工作中心
            adapter.setWkid(leftDatas[i].getCworkcenterid());
            // 部门中心
            adapter.setCdeptid(leftDatas[i].getCdeptid());
            // 核算要素
            adapter.setCelementid(paramvo.getAttributeValue("pk_factor") == null ?"":paramvo.getAttributeValue("pk_factor").toString());
              
            // 成本中心
            adapter.setCcostcenterid(leftDatas[i].getCcostcenterid());
            // 交易类型
            adapter.setCtrantypeid(leftDatas[i].getCtrantypeid());
            // 生产订单号
            adapter.setCmocode(leftDatas[i].getVproduceordercode());
            // 产品ID
            adapter.setCproductid(leftDatas[i].getCbomcodeid());
   
            // 实际金额
            adapter.setNmoney(leftDatas[i].getNmny());
       
            // 入库类型
            adapter.setFinstoragetype(CostObjInStorageTypeEnum.HOME_MAKE.toIntValue());

            adapter.setNnum(leftDatas[i].getNnum()); // 数量
            if (null == adapter.getNnum() || adapter.getNnum().compareTo(UFDouble.ZERO_DBL) == 0) {
                adapter.setNprice(null);
            }
            else {
                adapter.setNprice(CMNumberUtil2.div(adapter.getNmoney(), adapter.getNnum())); // 金额 /数量=单价
            }
            adapter.setNplanmoney(null); // 除了材料出库其它不取计划金额

            adapter.setCproductgroupid(leftDatas[i].getCproductgroupid());// 成本对象成本分类
            adapter.setBauditflag(leftDatas[i].getBauditflag());
            // 单据类型
            adapter.setCbilltypecode(2);
            // 成本域
            adapter.setCsrccostdomainid(leftDatas[i].getPk_org());
            // 财务核算账簿
            stuffVOList.add(adapter);
        }
        retList.addAll(stuffVOList);
            return retList.toArray(new StuffVOAdapter[0]);
    
	}
	
	

    // 设置物料的辅助属性
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

    // 置产品的辅助属性
    private void setVmainfrees(StuffVOAdapter adapter, CMDataVO vo, String[][] strs) {
        for (String[] str : strs) {
            adapter.setAttributeValue(str[0], vo.getAttributeValue(str[1]));
        }
    }


	private CMDataVO[] queryCMDataVO(GetDataPara para, FetchParamVO fetchPara) {
		// TODO 自动生成的方法存根
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
		//查询成本对象以及成本对象核算要素对应的成本BOM的 核算要素的金额
		SqlBuilder  pricesql = new SqlBuilder();
		pricesql.append("SELECT fee.nmoney FROM sca_bom_fee fee");//成本BOM费用明细
		pricesql.append(" LEFT JOIN sca_bom bom ON fee.ccmbomid = bom.ccmbomid ");//成本BOM
		pricesql.append(" left join  cm_costobject costo");//成本对象 ：物料和自由属性都相同
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
		// TODO 自动生成的方法存根
		
	}

	@Override
	protected CircularlyAccessibleValueObject[] getFIDataOfOutSystem(
			GetDataPara paramVO, int billType) throws BusinessException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public List<String> getSubGroupByFields(int billType) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	protected CostObjectGenerateVO[] getAndSetCostOjbParam(
			IFetchData[] correctData, AbstractCheckStrategy strategy,
			Map<IFetchData, PullDataErroInfoVO> vbFreeErrorVoMap) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	protected IFetchData[] transDatas(CircularlyAccessibleValueObject[] datas,
			FetchParamVO paramvo) throws BusinessException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	protected void transVOInserDB(IFIFetchData[] correctData,
			FetchParamVO paramVo) throws BusinessException {
		// TODO 自动生成的方法存根
		
	}

	@Override
	protected List<Map<IFetchData, PullDataErroInfoVO>> getErrorInfo(
			IFIFetchData[] datas, String cperiod, boolean isCheckFlag)
			throws BusinessException {
		// TODO 自动生成的方法存根
		return null;
	}
	
	
	
}
