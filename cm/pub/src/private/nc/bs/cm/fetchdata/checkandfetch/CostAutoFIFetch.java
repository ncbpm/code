package nc.bs.cm.fetchdata.checkandfetch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bd.framework.base.CMNumberUtil;
import nc.bd.framework.base.CMStringUtil;
import nc.bs.cm.fetchdata.fetchcheck.AbstractCheckStrategy;
import nc.bs.cm.fetchdata.fetchcheck.CCPRKCheckStrategy;
import nc.bs.cm.fetchdata.fetchcheck.WTJGRKWGCheckStrtegy;
import nc.bs.cm.fetchdata.groupdata.IGroupStrategy;
import nc.bs.framework.common.NCLocator;
import nc.cmpub.business.enumeration.CMAllocStatusEnum;
import nc.cmpub.business.enumeration.CMSourceTypeEnum;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.pubitf.cm.product.cm.iaretrieval.IProductforIARetrieval;
import nc.vo.bd.material.prod.MaterialProdVO;
import nc.vo.cm.costobject.entity.CostObjectGenerateVO;
import nc.vo.cm.costobject.enumeration.CostObjInStorageTypeEnum;
import nc.vo.cm.fetchdata.cmconst.FetchKeyConst;
import nc.vo.cm.fetchdata.entity.CostAutoVO;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.cm.fetchdata.entity.PullDataErroInfoVO;
import nc.vo.cm.fetchdata.entity.adapter.IFIFetchData;
import nc.vo.cm.fetchdata.entity.adapter.IFetchData;
import nc.vo.cm.fetchdata.entity.adapter.ProductVOAdapter;
import nc.vo.cm.fetchdata.enumeration.CMMesTypeEnum;
import nc.vo.cm.fetchdata.enumeration.FetchDataSchemaEnum;
import nc.vo.cm.fetchset.enumeration.CMBillEnum;
import nc.vo.cm.product.entity.ProductAggVO;
import nc.vo.cm.product.entity.ProductFetchVO;
import nc.vo.cm.product.entity.ProductHeadVO;
import nc.vo.cm.product.entity.ProductItemVO;
import nc.vo.cm.product.enumeration.ProductInStorageEnum;
import nc.vo.ia.detailledger.para.cm.GetDataPara;
import nc.vo.ia.pub.util.ToArrayUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.Constructor;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

public class CostAutoFIFetch extends AbstractFIFetch {
    /**
     * 完工单
     */
    IProductforIARetrieval productBillPubQueryService = null;

    /**
     * 完工单表头必须设置字段
     */
    private static final String[] PRODUCT_HEAD = new String[] {
        ProductHeadVO.CCOSTCENTERID, ProductHeadVO.CPERIOD, ProductHeadVO.CLASTCOSTCENTER, ProductHeadVO.VDEF1,
        ProductHeadVO.VDEF2, ProductHeadVO.VDEF3, ProductHeadVO.VDEF4, ProductHeadVO.VDEF5, ProductHeadVO.VDEF6,
        ProductHeadVO.VDEF7, ProductHeadVO.VDEF8, ProductHeadVO.VDEF9, ProductHeadVO.VDEF10, ProductHeadVO.VDEF11,
        ProductHeadVO.VDEF12, ProductHeadVO.VDEF13, ProductHeadVO.VDEF14, ProductHeadVO.VDEF15, ProductHeadVO.VDEF16,
        ProductHeadVO.VDEF17, ProductHeadVO.VDEF18, ProductHeadVO.VDEF19, ProductHeadVO.VDEF20
    };

    /**
     * 完工单表体必须设置字段
     */
    private static final String[] PRODUCT_ITEM_ITEMS = new String[] {
        ProductItemVO.NNUM, ProductItemVO.CCOSTOBJECTID, ProductItemVO.CMEASDOCID, ProductItemVO.VBDEF1,
        ProductItemVO.VBDEF2, ProductItemVO.VBDEF3, ProductItemVO.VBDEF4, ProductItemVO.VBDEF5, ProductItemVO.VBDEF6,
        ProductItemVO.VBDEF7, ProductItemVO.VBDEF8, ProductItemVO.VBDEF9, ProductItemVO.VBDEF10, ProductItemVO.VBDEF11,
        ProductItemVO.VBDEF12, ProductItemVO.VBDEF13, ProductItemVO.VBDEF14, ProductItemVO.VBDEF15,
        ProductItemVO.VBDEF16, ProductItemVO.VBDEF17, ProductItemVO.VBDEF18, ProductItemVO.VBDEF19,
        ProductItemVO.VBDEF20
    };

    /**
     * 完工单表体自定义项字段
     */
    private static final String[] PRODUCT_SELFDEFINE_ITEMS = new String[] {
        ProductItemVO.VBDEF1, ProductItemVO.VBDEF2, ProductItemVO.VBDEF3, ProductItemVO.VBDEF4, ProductItemVO.VBDEF5,
        ProductItemVO.VBDEF6, ProductItemVO.VBDEF7, ProductItemVO.VBDEF8, ProductItemVO.VBDEF9, ProductItemVO.VBDEF10,
        ProductItemVO.VBDEF11, ProductItemVO.VBDEF12, ProductItemVO.VBDEF13, ProductItemVO.VBDEF14,
        ProductItemVO.VBDEF15, ProductItemVO.VBDEF16, ProductItemVO.VBDEF17, ProductItemVO.VBDEF18,
        ProductItemVO.VBDEF19, ProductItemVO.VBDEF20
    };

    
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
	 * @return wkidAndCostAutoVOMap 取数得到的vo
	 * @throws BusinessException
	 *             BusinessException'
	 */
	public CircularlyAccessibleValueObject[] getFinishOrGxFinish(
			FetchParamVO paramvo) throws BusinessException {
		Integer fetchscheme = paramvo.getIfetchscheme();
		UFDate beginDay = new UFDate();
		UFDate endDay = new UFDate();
		// 会计月取数方案： 开始时间，结束时间为会计月份的开始时间和结束时间。
		if (FetchDataSchemaEnum.PERIODSCHEMA.equalsValue(fetchscheme)) {
			beginDay = paramvo.getBeginEndDate()[0];
			endDay = paramvo.getBeginEndDate()[1];

		} else if (FetchDataSchemaEnum.WEEKSCHEMA.equalsValue(fetchscheme)) {
			// 周期取数，只是一条数据
			beginDay = paramvo.getBeginenddate().get(0).get(0); // 周期取数，只是一条数据
			endDay = paramvo.getBeginenddate().get(0).get(1);
		}
		// 进行取数:工作工作中心为空时,按照部门进行取数
		CircularlyAccessibleValueObject[] CostAutoVOs = this
				.getMMDataOfOutSystem(this.pkGroup,this.pkOrg,paramvo.getDaccountperiod(), beginDay, endDay);
		return CostAutoVOs;
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
	 * @return 拉平后的vo数组:因为要生成作业统计单，所有参考当前已有代码作业的统计方式
	 * 
	 */
	public CircularlyAccessibleValueObject[] getMMDataOfOutSystem(
			 String pk_group,String pkOrg,String cperiod ,UFDate beginDay, UFDate endDay) throws BusinessException {
		SqlBuilder sql = new SqlBuilder();
		sql.append(" select pk_org, pk_costcenter AS ccostcenterid,  ccostobjectid,  pk_measdoc as cunitid,  nnum as nnum ");
		sql.append(" from  view_nc_zuoyeautocost cost"); 
		sql.append(" where  ");
		sql.append("  pk_group", pk_group);
		sql.append(" and Pk_org", pkOrg);
		sql.append(" and  (bcost='0' or (bcost='1' and cperiod='"+cperiod+"'))");
		DataAccessUtils util = new DataAccessUtils();
		IRowSet rowset = util.query(sql.toString());
		CostAutoVO[] vos = constructVOs( rowset);
		
		return vos;

	}
	
	private CostAutoVO[] constructVOs(IRowSet rowset) {
		String[] groupfields = new String[] { "pk_org", "ccostcenterid","ccostobjectid","cunitid"};
		String[] sumFields = new String[] { "nnum" };
		List<CostAutoVO> list = new ArrayList<CostAutoVO>();
		while (rowset.next()) {
			CostAutoVO vo = new CostAutoVO();
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
		CostAutoVO[] vos = ToArrayUtil.convert(list, CostAutoVO.class);
		return vos;
	}


    @Override
    public IFetchData[] transDatas(CircularlyAccessibleValueObject[] datas, FetchParamVO paramvo)
            throws BusinessException {
        IFetchData[] retData = this.transDataByBillType(datas, paramvo);
        return retData;
    }

    @Override
    public void transVOInserDB(IFIFetchData[] correctData, FetchParamVO paramvo) throws BusinessException {
        // 所有的需要保存到数据库的完工数据
        ProductAggVO[] productAggVOs = this.dealEachList(correctData, paramvo);
        // 设置组织的版本号
        this.setOrgVid(productAggVOs);
        // add by wtf at 2011-05-18 start
        // 设置组织的版本号
        // 批量插入完工单中
        try {
            NCLocator.getInstance().lookup(IProductforIARetrieval.class).insertProduct4IaRetrieval(productAggVOs);
        }
        catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
    }
    
    /**
     * 处理每个单据的数据
     *
     * @param allList 待处理的所有数据
     * @param paramvo 传入的参数
     */
    private ProductAggVO[] dealEachList(IFIFetchData[] correctData, FetchParamVO paramvo)
            throws BusinessException {
        // 临时存储所有的转化完后的消耗单数组列表
        List<ProductAggVO[]> resultArrList = new ArrayList<ProductAggVO[]>();
        Map<String, List<IFIFetchData>> cachedVOMap = super.transVO(correctData, new AbstractFetchStrategy() {
            // 会计期间 消耗成本中心 成本对象编码
            @Override
            public String getUniqueKey(IFIFetchData vo) {
                StringBuffer key = new StringBuffer();
                key.append(vo.getCcostcenterid());
                key.append(vo.getCperiod());
                return key.toString();
            }
        });
        // 转换为完工单
        ProductAggVO[] aggVOs = this.transProductAggVO(cachedVOMap, correctData, paramvo);
        resultArrList.add(aggVOs);
        // 转化为完工单数组返回
        List<ProductAggVO> resultList = new ArrayList<ProductAggVO>();
        for (ProductAggVO[] stuffArr : resultArrList) {
            resultList.addAll(Arrays.asList(stuffArr));
        }
        return resultList.toArray(new ProductAggVO[0]);
    }

    private ProductAggVO[] processValues(Map<String, List<IFIFetchData>> cachedVOMap, ProductAggVO[] aggvo,
            IGroupStrategy<ProductAggVO, ProductHeadVO, ProductItemVO, IFIFetchData> strategy) throws BusinessException {
        int i = 0;
        for (Map.Entry<String, List<IFIFetchData>> entry : cachedVOMap.entrySet()) {
            Map<String, ProductItemVO> result = new HashMap<String, ProductItemVO>();
            ProductHeadVO headVO = strategy.getHeadVO(entry.getValue());
            ProductItemVO[] itemvos = strategy.getItemVO(entry.getValue());
            // 对表体相同关键字，汇总sum字段
            for (ProductItemVO itemvo : itemvos) {
                String itemKey = strategy.getItemKey(itemvo);
                ProductItemVO datas = result.get(itemKey);
                if (datas == null) {
                    List<ProductFetchVO> list = new ArrayList<ProductFetchVO>();
                    result.put(itemKey, itemvo);
                    // 增加孙表数据
                    this.addProductFetchVOs(list, itemvo, headVO.getCcostcenterid());
                    // 设置到结果中
                    result.get(itemKey).setProductFetchVOs(list.toArray(new ProductFetchVO[0]));
                }
                else {
                    ProductItemVO existVO = result.get(itemKey);
                    String[] sumAttrs = strategy.getSumAttrs(itemvo);
                    for (String sumAttr : sumAttrs) {
                        UFDouble value1 = (UFDouble) existVO.getAttributeValue(sumAttr);
                        UFDouble value2 = (UFDouble) itemvo.getAttributeValue(sumAttr);
                        existVO.setAttributeValue(sumAttr, CMNumberUtil.add(value1, value2));
                    }
                    // 已存在的明细
                    List<ProductFetchVO> alreadExistList = Arrays.asList(result.get(itemKey).getProductFetchVOs());
                    // 本次新增的明细
                    List<ProductFetchVO> newList = new ArrayList<ProductFetchVO>();
                    // 增加孙表数据
                    this.addProductFetchVOs(newList, itemvo, headVO.getCcostcenterid());
                    // 合并
                    newList.addAll(alreadExistList);

                    // 设置到结果中
                    result.get(itemKey).setProductFetchVOs(newList.toArray(new ProductFetchVO[0]));
                }
            }
            if (result.values().size() != 0) {
                aggvo[i].setChildrenVO(result.values().toArray(Constructor.declareArray(itemvos[0].getClass(), 0)));
            }
            aggvo[i].setParentVO(headVO);
            i++;
        }
        return aggvo;

    }
    /**
     * 根据单据类型转换数据
     *
     * @param datas 原始数据
     * @param fetchParamVO 取数参数
     */
    protected IFIFetchData[] transDataByBillType(CircularlyAccessibleValueObject[] datas, FetchParamVO fetchParamVO)
            throws BusinessException {
        // 转化为数组
        CostAutoVO[] prodatas = Arrays.asList(datas).toArray(new CostAutoVO[0]);
        List<ProductVOAdapter> retList = new ArrayList<ProductVOAdapter>();
        // 每个单据类型的处理
        for (int i = 0; i < prodatas.length; i++) {
            // 设置完工单中有的字段
            ProductVOAdapter adapter = new ProductVOAdapter(new ProductAggVO());
            this.setVmainfrees(adapter, prodatas[i], FetchKeyConst.CM_IA_ForMainProd);
            this.setVmainfrees(adapter, prodatas[i], FetchKeyConst.CM_IA_ForProd);
            // 设置类型，区别错误信息和警告信息
            adapter.setImestype(CMMesTypeEnum.ERROR.toIntValue());
            // 设置完工单缺少的字段
            adapter.setPk_org(this.pkOrg);
//            adapter.setWkid(prodatas[i].getCworkcenterid());
//            adapter.setCdeptid(prodatas[i].getCdeptid());
//            adapter.setCprojectid(prodatas[i].getCprojectid());
//            adapter.setVproducebatch(prodatas[i].getVproducebatch());
//            adapter.setCmocode(orderCode);// 订单号
            // 成本中心
            adapter.setCcostcenterid(prodatas[i].getCcostcenterid());
            // 交易类型
//            adapter.setCtrantypeid(prodatas[i].getCtrantypeid());
            adapter.setNnum(prodatas[i].getNnum());
            // 产品ID
            adapter.setCproductid(prodatas[i].getCinventoryid());
            adapter.setCmeasdocid(prodatas[i].getCunitid());
            // 物料id
            String materialId = prodatas[i].getCinventoryid();
            adapter.setCmaterialid(materialId);
            // 成本域
            adapter.setCsrccostdomainid(prodatas[i].getPk_org());
            // 设置加工费
            adapter.setNmoney(prodatas[i].getNcost());
            // 主产品类型
            adapter.setFproductflag(String.valueOf(prodatas[i].getFProductFlag()));
            // 主产品
            adapter.setPrimaryproductid(prodatas[i].getPrimaryProductid());
            // 入库类型
            adapter.setFinstoragetype( CostObjInStorageTypeEnum.HOME_MAKE.toIntValue());
            // 单据类型
//            adapter.setCbilltypecode(billType);
            // 设置成本对象
            adapter.setCcostobjectid(prodatas[i].getCcostobjectid());
            retList.add(adapter);
        }
        return retList.toArray(new ProductVOAdapter[0]);
    }

    // 置产品的辅助属性
    private void setVmainfrees(ProductVOAdapter adapter, CostAutoVO vo, String[][] strs) {
        for (String[] str : strs) {
            adapter.setAttributeValue(str[0], vo.getAttributeValue(str[1]));
        }
    }

    /**
     * 孙表明细数据合并到列表中保存
     *
     * @param list 列表
     * @param itemVo 完工单表体
     */
    private void addProductFetchVOs(List<ProductFetchVO> list, ProductItemVO itemVo, String costcenter) {
        // 合并明细数据
        for (ProductFetchVO vo : itemVo.getProductFetchVOs()) {
            vo.setCcostcenterid(costcenter);
        }
        ProductFetchVO[] productFetchVOs = itemVo.getProductFetchVOs();
        if (productFetchVOs == null || productFetchVOs.length <= 0) {
            return;
        }
        for (int j = 0; j < productFetchVOs.length; j++) {
            list.add(productFetchVOs[j]);
        }
    }

    /**
     * 转换为转换为完工单
     *
     * @param cachedVOMap
     *            把汇总vo中相同key数据，放到一个list中
     * @param correctData
     *            正确数据
     */
    private ProductAggVO[] transProductAggVO(Map<String, List<IFIFetchData>> cachedVOMap, IFIFetchData[] correctData,
            final FetchParamVO paramvo) throws BusinessException {
        ProductAggVO[] productAggVO = Constructor.construct(ProductAggVO.class, cachedVOMap.size());
        return this.processValues(cachedVOMap, productAggVO,
                new IGroupStrategy<ProductAggVO, ProductHeadVO, ProductItemVO, IFIFetchData>() {
            @Override
            public String getItemKey(ProductItemVO vo) {
                StringBuilder key = new StringBuilder();
                key.append(vo.getCcostobjectid()); // 成本对象编码
                for (String itemKey : CostAutoFIFetch.PRODUCT_SELFDEFINE_ITEMS) {
                    key.append(vo.getAttributeValue(itemKey));
                }
                return key.toString();
            }

            @Override
            public String[] getSumAttrs(ProductItemVO vo) {
                // 完工数量
                return new String[] {
                        ProductItemVO.NNUM
                };
            }

            @Override
            public ProductHeadVO getHeadVO(List<IFIFetchData> volist) throws BusinessException {
                IFIFetchData[] datavos = volist.toArray(new IFIFetchData[0]);
                return CostAutoFIFetch.this.setProdHeadVO(paramvo, datavos);
            }

            @Override
            public ProductItemVO[] getItemVO(List<IFIFetchData> voList) {
                List<ProductItemVO> listItemvo = new ArrayList<ProductItemVO>();

                return CostAutoFIFetch.this.setProductItemVO(voList.toArray(new IFIFetchData[0]), listItemvo);
            }
        });
    }

    /**
     * 设置完工单ItemVO
     */
    private ProductItemVO[] setProductItemVO(IFIFetchData[] vos, List<ProductItemVO> listItemvo) {
        for (IFIFetchData vo : vos) {
            ProductItemVO productItemVO = new ProductItemVO();
            for (String itemItem : CostAutoFIFetch.PRODUCT_ITEM_ITEMS) {
                productItemVO.setAttributeValue(itemItem, vo.getAttributeValue(itemItem));
            }
            this.setItemVOSpeicalValue(productItemVO);
            // 保存取数明细信息
            ProductFetchVO productFetchVO = new ProductFetchVO();
            productFetchVO.setPk_group(productItemVO.getPk_group());
            productFetchVO.setCcostregionid(((ProductVOAdapter) vo).getCsrccostdomainid());
            productFetchVO.setCmaterialid(vo.getCmaterialid());
            productFetchVO.setNnum(vo.getNnum());
            productFetchVO.setPk_org(productItemVO.getPk_org());
            productFetchVO.setPk_org_v(productItemVO.getPk_org_v());
            productItemVO.setProductFetchVOs(new ProductFetchVO[] {
                    productFetchVO
            });
            listItemvo.add(productItemVO);
        }
        return listItemvo.toArray(new ProductItemVO[0]);
    }

    /**
     * 设置特殊参数
     */
    private void setItemVOSpeicalValue(ProductItemVO productItemVO) {
        productItemVO.setPk_group(this.pkGroup);
        productItemVO.setPk_org(this.pkOrg);
        productItemVO.setPk_org_v(this.pkOrgv);
        productItemVO.setIstatus(Integer.valueOf(String.valueOf(CMAllocStatusEnum.ALLOCATE.value())));
    }

    /**
     * 设置完工单headvo
     */
    private ProductHeadVO setProdHeadVO(FetchParamVO paramvo, IFIFetchData[] datavos) throws BusinessException {
        IFIFetchData vo = datavos[0];
        ProductHeadVO productHeadVO = new ProductHeadVO();
        productHeadVO.setInstoragetype(Integer
                .valueOf(ProductInStorageEnum.QUALIFIED_PRODUCT.getEnumValue().getValue()));// 合格品
        for (String headItem : CostAutoFIFetch.PRODUCT_HEAD) {
            productHeadVO.setAttributeValue(headItem, vo.getAttributeValue(headItem));
        }
        this.setHeadVOSpecialValue(paramvo, productHeadVO, vo);
        return productHeadVO;
    }

    /**
     * 设置特殊参数
     */
    private void setHeadVOSpecialValue(FetchParamVO paramvo, ProductHeadVO productHeadVO, IFIFetchData vo)
            throws BusinessException {
        productHeadVO.setPk_group(this.pkGroup); // 集团
        productHeadVO.setPk_org(this.pkOrg); // 组织
        productHeadVO.setPk_org_v(this.pkOrgv);
        // 会计期间
        productHeadVO.setCperiod(paramvo.getDaccountperiod());
        // 状态
        if (vo.getCbilltypecode().equals(Integer.valueOf(CMBillEnum.PROCESSIN.getEnumValue().getValue()))) {
            // 委外
            productHeadVO.setIsourcetype(Integer.valueOf(String.valueOf(CMSourceTypeEnum.IA_OUTSOURCING.value())));

        }
        else {
            // 产成品入库单
            productHeadVO.setIsourcetype(Integer.valueOf(String.valueOf(CMSourceTypeEnum.IA_PRODUCT_IN.value())));

        }
        // 末到成本中心
        productHeadVO.setClastcostcenter(productHeadVO.getCcostcenterid()); // 末到成本中心
        // 业务日期
        UFDate businessdate = super.getBusinessdate();
        productHeadVO.setDbusinessdate(businessdate); // 业务日期
    }

    @Override
    protected List<Map<IFetchData, PullDataErroInfoVO>> getErrorInfo(IFIFetchData[] datas, String cperiod,
            boolean isCheckFlag) throws BusinessException {
        List<Map<IFetchData, PullDataErroInfoVO>> list = new ArrayList<Map<IFetchData, PullDataErroInfoVO>>();
        // 产产品入库
        List<IFIFetchData> ccprkList = this.getDataByBillTypeFromAdapter(datas, CMBillEnum.PRODUCTIN.toIntValue());
        // 委托加工入库-完工
        List<IFIFetchData> wtjgrkList = this.getDataByBillTypeFromAdapter(datas, CMBillEnum.PROCESSIN.toIntValue());
        // 存储所有的错误信息
        Map<IFetchData, PullDataErroInfoVO> allMap = new HashMap<IFetchData, PullDataErroInfoVO>();

        // 产成品入库
        Map<IFetchData, PullDataErroInfoVO> ccprkMap =
                super.getErrorInfoAndFilteredData(ccprkList.toArray(new IFIFetchData[0]),
                        new CCPRKCheckStrategy(this.pkOrg, this.pkGroup, this.costDomain, this.acountBook), cperiod,
                        isCheckFlag).get(0);
        // 委托加工入库
        Map<IFetchData, PullDataErroInfoVO> wtjgMap =
                super.getErrorInfoAndFilteredData(wtjgrkList.toArray(new IFIFetchData[0]),
                        new WTJGRKWGCheckStrtegy(this.pkOrg, this.pkGroup, this.costDomain, this.acountBook), cperiod,
                        isCheckFlag).get(0);

        allMap.putAll(ccprkMap);
        allMap.putAll(wtjgMap);
        list.add(allMap);
        return list;
    }

    /**
     * 根据交易类型摘选需要的数据
     *
     * @param datas 总数据
     * @param billType 单据类型
     * @return 某种交易类型的数据
     */
    private List<IFIFetchData> getDataByBillTypeFromAdapter(IFIFetchData[] datas, Integer billType) {
        List<IFIFetchData> clckList = new ArrayList<IFIFetchData>();
        for (IFIFetchData data : datas) {
            if (billType.intValue() == data.getCbilltypecode().intValue()) {
                clckList.add(data);
            }
        }
        return clckList;
    }

    /**
     * 组织成本对象参数
     *
     * @param datas
     *            IMMFetchData
     * @return 成本对象vo
     */
    @Override
    protected CostObjectGenerateVO[] getAndSetCostOjbParam(IFetchData[] datas, AbstractCheckStrategy strategy,
            Map<IFetchData, PullDataErroInfoVO> vbFreeErrorVoMap) {
        Map<String, MaterialProdVO> prodVOMap = this.getProdVO(datas);
        List<CostObjectGenerateVO> costOjbVoList = new ArrayList<CostObjectGenerateVO>();
        CostObjectGenerateVO costOjbVO;
        for (int i = 0; i < datas.length; i++) {
            IFIFetchData data = (IFIFetchData) datas[i];
            costOjbVO = new CostObjectGenerateVO();
            costOjbVO.setPk_org(this.pkOrg);
            costOjbVO.setPk_org_v(this.pkOrgv);
            costOjbVO.setPk_group(this.pkGroup);

            costOjbVO.setCmaterialid(data.getCmaterialid());
            costOjbVO.setVmocode(data.getCmocode());
            costOjbVO.setFinstoragetype(data.getFinstoragetype());// 入库类型
            costOjbVO.setFproducttype(Integer.valueOf(data.getFproductflag()));// 主产品标示
            costOjbVO.setCmainmaterialid(data.getPrimaryproductid());// 对应主产品id
            // 错误信息：存货核算取数未录入产品,主产品启用的辅助属性信息
            if (strategy.setVbfreeWarnData(vbFreeErrorVoMap, prodVOMap, data)) {
                continue;
            }
            this.setVmainfrees(costOjbVO, data, FetchKeyConst.CM_Prod_ForObj);
            this.setVmainfrees(costOjbVO, data, FetchKeyConst.CM_MainProd_ForObj);
            data.setCostObjectGenerateVO(costOjbVO);
            costOjbVoList.add(costOjbVO);
        }
        return costOjbVoList.toArray(new CostObjectGenerateVO[costOjbVoList.size()]);
    }

    private Map<String, MaterialProdVO> getProdVO(IFetchData[] datas) {
        Set<String> marIdSet = new HashSet<String>();
        for (IFetchData idata : datas) {
            IFIFetchData data = (IFIFetchData) idata;
            if (CMStringUtil.isNotEmpty(data.getCproductid())) {
                marIdSet.add(data.getCproductid());
            }
            if (CMStringUtil.isNotEmpty(data.getPrimaryproductid())) {
                marIdSet.add(data.getPrimaryproductid());
            }
        }

        Map<String, MaterialProdVO> prodVOMap =
                this.getMaterialProdVOMap(this.pkOrg, marIdSet.toArray(new String[marIdSet.size()]));
        return prodVOMap;
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
}
