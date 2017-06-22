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
     * �깤��
     */
    IProductforIARetrieval productBillPubQueryService = null;

    /**
     * �깤����ͷ���������ֶ�
     */
    private static final String[] PRODUCT_HEAD = new String[] {
        ProductHeadVO.CCOSTCENTERID, ProductHeadVO.CPERIOD, ProductHeadVO.CLASTCOSTCENTER, ProductHeadVO.VDEF1,
        ProductHeadVO.VDEF2, ProductHeadVO.VDEF3, ProductHeadVO.VDEF4, ProductHeadVO.VDEF5, ProductHeadVO.VDEF6,
        ProductHeadVO.VDEF7, ProductHeadVO.VDEF8, ProductHeadVO.VDEF9, ProductHeadVO.VDEF10, ProductHeadVO.VDEF11,
        ProductHeadVO.VDEF12, ProductHeadVO.VDEF13, ProductHeadVO.VDEF14, ProductHeadVO.VDEF15, ProductHeadVO.VDEF16,
        ProductHeadVO.VDEF17, ProductHeadVO.VDEF18, ProductHeadVO.VDEF19, ProductHeadVO.VDEF20
    };

    /**
     * �깤��������������ֶ�
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
     * �깤�������Զ������ֶ�
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
	 * @return wkidAndCostAutoVOMap ȡ���õ���vo
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

		} else if (FetchDataSchemaEnum.WEEKSCHEMA.equalsValue(fetchscheme)) {
			// ����ȡ����ֻ��һ������
			beginDay = paramvo.getBeginenddate().get(0).get(0); // ����ȡ����ֻ��һ������
			endDay = paramvo.getBeginenddate().get(0).get(1);
		}
		// ����ȡ��:������������Ϊ��ʱ,���ղ��Ž���ȡ��
		CircularlyAccessibleValueObject[] CostAutoVOs = this
				.getMMDataOfOutSystem(this.pkGroup,this.pkOrg,paramvo.getDaccountperiod(), beginDay, endDay);
		return CostAutoVOs;
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
        // ���е���Ҫ���浽���ݿ���깤����
        ProductAggVO[] productAggVOs = this.dealEachList(correctData, paramvo);
        // ������֯�İ汾��
        this.setOrgVid(productAggVOs);
        // add by wtf at 2011-05-18 start
        // ������֯�İ汾��
        // ���������깤����
        try {
            NCLocator.getInstance().lookup(IProductforIARetrieval.class).insertProduct4IaRetrieval(productAggVOs);
        }
        catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
    }
    
    /**
     * ����ÿ�����ݵ�����
     *
     * @param allList ���������������
     * @param paramvo ����Ĳ���
     */
    private ProductAggVO[] dealEachList(IFIFetchData[] correctData, FetchParamVO paramvo)
            throws BusinessException {
        // ��ʱ�洢���е�ת���������ĵ������б�
        List<ProductAggVO[]> resultArrList = new ArrayList<ProductAggVO[]>();
        Map<String, List<IFIFetchData>> cachedVOMap = super.transVO(correctData, new AbstractFetchStrategy() {
            // ����ڼ� ���ĳɱ����� �ɱ��������
            @Override
            public String getUniqueKey(IFIFetchData vo) {
                StringBuffer key = new StringBuffer();
                key.append(vo.getCcostcenterid());
                key.append(vo.getCperiod());
                return key.toString();
            }
        });
        // ת��Ϊ�깤��
        ProductAggVO[] aggVOs = this.transProductAggVO(cachedVOMap, correctData, paramvo);
        resultArrList.add(aggVOs);
        // ת��Ϊ�깤�����鷵��
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
            // �Ա�����ͬ�ؼ��֣�����sum�ֶ�
            for (ProductItemVO itemvo : itemvos) {
                String itemKey = strategy.getItemKey(itemvo);
                ProductItemVO datas = result.get(itemKey);
                if (datas == null) {
                    List<ProductFetchVO> list = new ArrayList<ProductFetchVO>();
                    result.put(itemKey, itemvo);
                    // �����������
                    this.addProductFetchVOs(list, itemvo, headVO.getCcostcenterid());
                    // ���õ������
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
                    // �Ѵ��ڵ���ϸ
                    List<ProductFetchVO> alreadExistList = Arrays.asList(result.get(itemKey).getProductFetchVOs());
                    // ������������ϸ
                    List<ProductFetchVO> newList = new ArrayList<ProductFetchVO>();
                    // �����������
                    this.addProductFetchVOs(newList, itemvo, headVO.getCcostcenterid());
                    // �ϲ�
                    newList.addAll(alreadExistList);

                    // ���õ������
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
     * ���ݵ�������ת������
     *
     * @param datas ԭʼ����
     * @param fetchParamVO ȡ������
     */
    protected IFIFetchData[] transDataByBillType(CircularlyAccessibleValueObject[] datas, FetchParamVO fetchParamVO)
            throws BusinessException {
        // ת��Ϊ����
        CostAutoVO[] prodatas = Arrays.asList(datas).toArray(new CostAutoVO[0]);
        List<ProductVOAdapter> retList = new ArrayList<ProductVOAdapter>();
        // ÿ���������͵Ĵ���
        for (int i = 0; i < prodatas.length; i++) {
            // �����깤�����е��ֶ�
            ProductVOAdapter adapter = new ProductVOAdapter(new ProductAggVO());
            this.setVmainfrees(adapter, prodatas[i], FetchKeyConst.CM_IA_ForMainProd);
            this.setVmainfrees(adapter, prodatas[i], FetchKeyConst.CM_IA_ForProd);
            // �������ͣ����������Ϣ�;�����Ϣ
            adapter.setImestype(CMMesTypeEnum.ERROR.toIntValue());
            // �����깤��ȱ�ٵ��ֶ�
            adapter.setPk_org(this.pkOrg);
//            adapter.setWkid(prodatas[i].getCworkcenterid());
//            adapter.setCdeptid(prodatas[i].getCdeptid());
//            adapter.setCprojectid(prodatas[i].getCprojectid());
//            adapter.setVproducebatch(prodatas[i].getVproducebatch());
//            adapter.setCmocode(orderCode);// ������
            // �ɱ�����
            adapter.setCcostcenterid(prodatas[i].getCcostcenterid());
            // ��������
//            adapter.setCtrantypeid(prodatas[i].getCtrantypeid());
            adapter.setNnum(prodatas[i].getNnum());
            // ��ƷID
            adapter.setCproductid(prodatas[i].getCinventoryid());
            adapter.setCmeasdocid(prodatas[i].getCunitid());
            // ����id
            String materialId = prodatas[i].getCinventoryid();
            adapter.setCmaterialid(materialId);
            // �ɱ���
            adapter.setCsrccostdomainid(prodatas[i].getPk_org());
            // ���üӹ���
            adapter.setNmoney(prodatas[i].getNcost());
            // ����Ʒ����
            adapter.setFproductflag(String.valueOf(prodatas[i].getFProductFlag()));
            // ����Ʒ
            adapter.setPrimaryproductid(prodatas[i].getPrimaryProductid());
            // �������
            adapter.setFinstoragetype( CostObjInStorageTypeEnum.HOME_MAKE.toIntValue());
            // ��������
//            adapter.setCbilltypecode(billType);
            // ���óɱ�����
            adapter.setCcostobjectid(prodatas[i].getCcostobjectid());
            retList.add(adapter);
        }
        return retList.toArray(new ProductVOAdapter[0]);
    }

    // �ò�Ʒ�ĸ�������
    private void setVmainfrees(ProductVOAdapter adapter, CostAutoVO vo, String[][] strs) {
        for (String[] str : strs) {
            adapter.setAttributeValue(str[0], vo.getAttributeValue(str[1]));
        }
    }

    /**
     * �����ϸ���ݺϲ����б��б���
     *
     * @param list �б�
     * @param itemVo �깤������
     */
    private void addProductFetchVOs(List<ProductFetchVO> list, ProductItemVO itemVo, String costcenter) {
        // �ϲ���ϸ����
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
     * ת��Ϊת��Ϊ�깤��
     *
     * @param cachedVOMap
     *            �ѻ���vo����ͬkey���ݣ��ŵ�һ��list��
     * @param correctData
     *            ��ȷ����
     */
    private ProductAggVO[] transProductAggVO(Map<String, List<IFIFetchData>> cachedVOMap, IFIFetchData[] correctData,
            final FetchParamVO paramvo) throws BusinessException {
        ProductAggVO[] productAggVO = Constructor.construct(ProductAggVO.class, cachedVOMap.size());
        return this.processValues(cachedVOMap, productAggVO,
                new IGroupStrategy<ProductAggVO, ProductHeadVO, ProductItemVO, IFIFetchData>() {
            @Override
            public String getItemKey(ProductItemVO vo) {
                StringBuilder key = new StringBuilder();
                key.append(vo.getCcostobjectid()); // �ɱ��������
                for (String itemKey : CostAutoFIFetch.PRODUCT_SELFDEFINE_ITEMS) {
                    key.append(vo.getAttributeValue(itemKey));
                }
                return key.toString();
            }

            @Override
            public String[] getSumAttrs(ProductItemVO vo) {
                // �깤����
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
     * �����깤��ItemVO
     */
    private ProductItemVO[] setProductItemVO(IFIFetchData[] vos, List<ProductItemVO> listItemvo) {
        for (IFIFetchData vo : vos) {
            ProductItemVO productItemVO = new ProductItemVO();
            for (String itemItem : CostAutoFIFetch.PRODUCT_ITEM_ITEMS) {
                productItemVO.setAttributeValue(itemItem, vo.getAttributeValue(itemItem));
            }
            this.setItemVOSpeicalValue(productItemVO);
            // ����ȡ����ϸ��Ϣ
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
     * �����������
     */
    private void setItemVOSpeicalValue(ProductItemVO productItemVO) {
        productItemVO.setPk_group(this.pkGroup);
        productItemVO.setPk_org(this.pkOrg);
        productItemVO.setPk_org_v(this.pkOrgv);
        productItemVO.setIstatus(Integer.valueOf(String.valueOf(CMAllocStatusEnum.ALLOCATE.value())));
    }

    /**
     * �����깤��headvo
     */
    private ProductHeadVO setProdHeadVO(FetchParamVO paramvo, IFIFetchData[] datavos) throws BusinessException {
        IFIFetchData vo = datavos[0];
        ProductHeadVO productHeadVO = new ProductHeadVO();
        productHeadVO.setInstoragetype(Integer
                .valueOf(ProductInStorageEnum.QUALIFIED_PRODUCT.getEnumValue().getValue()));// �ϸ�Ʒ
        for (String headItem : CostAutoFIFetch.PRODUCT_HEAD) {
            productHeadVO.setAttributeValue(headItem, vo.getAttributeValue(headItem));
        }
        this.setHeadVOSpecialValue(paramvo, productHeadVO, vo);
        return productHeadVO;
    }

    /**
     * �����������
     */
    private void setHeadVOSpecialValue(FetchParamVO paramvo, ProductHeadVO productHeadVO, IFIFetchData vo)
            throws BusinessException {
        productHeadVO.setPk_group(this.pkGroup); // ����
        productHeadVO.setPk_org(this.pkOrg); // ��֯
        productHeadVO.setPk_org_v(this.pkOrgv);
        // ����ڼ�
        productHeadVO.setCperiod(paramvo.getDaccountperiod());
        // ״̬
        if (vo.getCbilltypecode().equals(Integer.valueOf(CMBillEnum.PROCESSIN.getEnumValue().getValue()))) {
            // ί��
            productHeadVO.setIsourcetype(Integer.valueOf(String.valueOf(CMSourceTypeEnum.IA_OUTSOURCING.value())));

        }
        else {
            // ����Ʒ��ⵥ
            productHeadVO.setIsourcetype(Integer.valueOf(String.valueOf(CMSourceTypeEnum.IA_PRODUCT_IN.value())));

        }
        // ĩ���ɱ�����
        productHeadVO.setClastcostcenter(productHeadVO.getCcostcenterid()); // ĩ���ɱ�����
        // ҵ������
        UFDate businessdate = super.getBusinessdate();
        productHeadVO.setDbusinessdate(businessdate); // ҵ������
    }

    @Override
    protected List<Map<IFetchData, PullDataErroInfoVO>> getErrorInfo(IFIFetchData[] datas, String cperiod,
            boolean isCheckFlag) throws BusinessException {
        List<Map<IFetchData, PullDataErroInfoVO>> list = new ArrayList<Map<IFetchData, PullDataErroInfoVO>>();
        // ����Ʒ���
        List<IFIFetchData> ccprkList = this.getDataByBillTypeFromAdapter(datas, CMBillEnum.PRODUCTIN.toIntValue());
        // ί�мӹ����-�깤
        List<IFIFetchData> wtjgrkList = this.getDataByBillTypeFromAdapter(datas, CMBillEnum.PROCESSIN.toIntValue());
        // �洢���еĴ�����Ϣ
        Map<IFetchData, PullDataErroInfoVO> allMap = new HashMap<IFetchData, PullDataErroInfoVO>();

        // ����Ʒ���
        Map<IFetchData, PullDataErroInfoVO> ccprkMap =
                super.getErrorInfoAndFilteredData(ccprkList.toArray(new IFIFetchData[0]),
                        new CCPRKCheckStrategy(this.pkOrg, this.pkGroup, this.costDomain, this.acountBook), cperiod,
                        isCheckFlag).get(0);
        // ί�мӹ����
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
     * ���ݽ�������ժѡ��Ҫ������
     *
     * @param datas ������
     * @param billType ��������
     * @return ĳ�ֽ������͵�����
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
     * ��֯�ɱ��������
     *
     * @param datas
     *            IMMFetchData
     * @return �ɱ�����vo
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
            costOjbVO.setFinstoragetype(data.getFinstoragetype());// �������
            costOjbVO.setFproducttype(Integer.valueOf(data.getFproductflag()));// ����Ʒ��ʾ
            costOjbVO.setCmainmaterialid(data.getPrimaryproductid());// ��Ӧ����Ʒid
            // ������Ϣ���������ȡ��δ¼���Ʒ,����Ʒ���õĸ���������Ϣ
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
}
