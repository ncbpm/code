package nc.pubimpl.cm.fetchset.cm.iaretrieval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bd.framework.base.CMArrayUtil;
import nc.bd.framework.db.CMSqlBuilder;
import nc.impl.pubapp.pattern.data.bill.EfficientBillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.pubitf.cm.fetchset.cm.iaretrieval.IFetchSetPubQueryService;
import nc.pubitf.ic.m4x.cm.DiscardInRetVO;
import nc.vo.cm.fetchdata.entity.FetchKeyVO;
import nc.vo.cm.fetchdata.enumeration.FetchDataObjEnum;
import nc.vo.cm.fetchset.entity.AggFetchSetVO;
import nc.vo.cm.fetchset.entity.FetchSetHeadVO;
import nc.vo.cm.fetchset.entity.FetchSetItemVO;
import nc.vo.cm.fetchset.entity.FetchsetExchangeVO;
import nc.vo.cm.fetchset.enumeration.FetchSchemeEnum;
import nc.vo.cm.fetchset.enumeration.FetchTypeEnum;
import nc.vo.cm.product.entity.ProductAggVO;
import nc.vo.cm.product.entity.ProductHeadVO;
import nc.vo.cm.product.entity.ProductItemVO;
import nc.vo.cm.stuff.entity.StuffAggVO;
import nc.vo.cm.stuff.entity.StuffHeadVO;
import nc.vo.cm.stuff.entity.StuffItemVO;
import nc.vo.ia.detailledger.view.cm.CMDataVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.entity.view.AbstractDataView;
import nc.vo.pubapp.pattern.pub.Constructor;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 外系统取数设置对外查询接口实现类
 * </p>
 */
public class FetchSetPubQueryServiceImpl implements IFetchSetPubQueryService {

    @Override
    public Map<String, String> fetchMappingByType(int fetchmapType, int billType, String pkOrg, String pkGroup)
            throws BusinessException {
        try {

            CMSqlBuilder whereSql = new CMSqlBuilder();
            whereSql.append(" and " + FetchsetExchangeVO.IFETCHMAPTYPE + " = " + fetchmapType);

            // modify by liwzh 2011-11-25 begin
            whereSql.append(" and " + FetchsetExchangeVO.IBILLTYPE, billType);

            // 过滤 掉vsourcefield为空的
            whereSql.append(" and ");
            whereSql.appendIDIsNotNull(FetchsetExchangeVO.VSOURCEFIELD);
            whereSql.append(" and ");
            whereSql.appendIDIsNotNull(FetchsetExchangeVO.VTARGETFIELDCODE);
            // whereSql.append(" and " + FetchsetExchangeVO.VSOURCEFIELD + " is not null");
            // // 过滤掉vtargetfield为空的
            // whereSql.append(" and " + FetchsetExchangeVO.VTARGETFIELD + " is not null");

            // end
            if (pkOrg != null) {
                whereSql.append(" and " + FetchsetExchangeVO.PK_ORG, pkOrg);
            }
            if (pkGroup != null) {
                whereSql.append(" and " + FetchsetExchangeVO.PK_GROUP, pkGroup);
            }

            VOQuery<FetchsetExchangeVO> query = new VOQuery<FetchsetExchangeVO>(FetchsetExchangeVO.class);
            FetchsetExchangeVO[] fetchsetExchangeVOs = query.query(whereSql.toString(), null);

            Map<String, String> resultMap = new HashMap<String, String>();

            // 组合映射
            if (fetchsetExchangeVOs != null && fetchsetExchangeVOs.length > 0) {
                for (FetchsetExchangeVO fetchsetExchangeVO : fetchsetExchangeVOs) {
                    resultMap.put(fetchsetExchangeVO.getVsourcefield(), fetchsetExchangeVO.getVtargetfieldcode());
                }
            }
            // add by wtf at 2011-05-18 end
            return resultMap;
        }
        catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    @Override
    public List<String> getBillTypeIds(int mapType, String pkOrg, String pkGroup) throws BusinessException {
        try {
            // 查询出聚合VO
            AggFetchSetVO[] fetchSetAggVOs =
                    this.fetchsetQueryByType(Integer.valueOf(mapType), pkOrg, pkGroup, FetchSchemeEnum.NOTEXIST);

            if (fetchSetAggVOs == null || fetchSetAggVOs.length == 0) {
                return new ArrayList<String>();
            }

            AggFetchSetVO fetchSetAggVO = fetchSetAggVOs[0];
            FetchSetItemVO[] itemVOs = (FetchSetItemVO[]) fetchSetAggVO.getChildren(FetchSetItemVO.class);

            // 组装返回值
            List<String> returnValue = new ArrayList<String>();

            if (itemVOs == null || itemVOs.length == 0) {
                return new ArrayList<String>();
            }

            for (FetchSetItemVO itemVO : itemVOs) {
                // midify by wtf at 201-06-23
                // 存货核算的接口已经修改为传递单据号id start
                // returnValue.add(itemVO.getPk_billtypecode());
                returnValue.add(itemVO.getPk_billtypeid());
                // end
            }

            return returnValue;
        }
        catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    @Override
    public StuffAggVO[] fetchMapMaterialDataVO(CMDataVO[] materialsDataVOs, int billType, String pkOrg, String pkGroup)
            throws BusinessException {
        try {
            // get the field mapping
            Map<String, String> stuff2materials =
                    this.fetchMappingByType(FetchTypeEnum.MATEROUT.toIntValue(), billType, pkOrg, pkGroup);

            List<StuffAggVO> stuffAggVOs = new ArrayList<StuffAggVO>();

            // map every VO value
            for (CMDataVO materialsDataVO : materialsDataVOs) {
                stuffAggVOs.add((StuffAggVO) this.createAggVO(StuffAggVO.class, StuffHeadVO.class, StuffItemVO.class,
                        stuff2materials, materialsDataVO));
            }

            return stuffAggVOs.toArray(new StuffAggVO[stuffAggVOs.size()]);
        }
        catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;

    }

    @Override
    public ProductAggVO[] fetchMapProductDataVO(CMDataVO[] productsDataVOs, int billType, String pkOrg, String pkGroup)
            throws BusinessException {
        try {
            // get field mapping
            Map<String, String> fieldMap =
                    this.fetchMappingByType(FetchTypeEnum.OVERIN.toIntValue(), billType, pkOrg, pkGroup);

            List<ProductAggVO> productAggVOs = new ArrayList<ProductAggVO>();

            // map every VO value
            for (CMDataVO productsDataVO : productsDataVOs) {
                productAggVOs.add((ProductAggVO) this.createAggVO(ProductAggVO.class, ProductHeadVO.class,
                        ProductItemVO.class, fieldMap, productsDataVO));
            }

            return productAggVOs.toArray(new ProductAggVO[productAggVOs.size()]);
        }
        catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    @Override
    public ProductAggVO[] fetchMapProductDataVO(DiscardInRetVO[] productsDataVOs, int billType, String pkOrg,
            String pkGroup) throws BusinessException {
        try {
            // get field mapping
            Map<String, String> fieldMap =
                    this.fetchMappingByType(Integer.valueOf(FetchTypeEnum.SPOIL.getEnumValue().getValue()).intValue(),
                            billType, pkOrg, pkGroup);

            List<ProductAggVO> productAggVOs = new ArrayList<ProductAggVO>();

            // map every VO value
            for (DiscardInRetVO productsDataVO : productsDataVOs) {
                productAggVOs.add((ProductAggVO) this.createAggVO(ProductAggVO.class, ProductHeadVO.class,
                        ProductItemVO.class, fieldMap, productsDataVO));
            }

            return productAggVOs.toArray(new ProductAggVO[productAggVOs.size()]);
        }
        catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    /**
     * 根据字段对照创建聚合VO
     *
     * @param bill
     *            聚合VO
     * @param head
     *            表头VO
     * @param item
     *            表体VO
     * @param filedMap
     *            字段映射
     * @param dataViewVO
     *            AbstractDataView
     * @return 聚合VO
     * @throws BusinessException
     *             异常
     */
    private IBill createAggVO(Class<? extends AbstractBill> bill, Class<? extends SuperVO> head,
            Class<? extends SuperVO> item, Map<String, String> filedMap, AbstractDataView dataViewVO)
                    throws BusinessException {

        // 实例化聚合VO、表头VO、表体VO
        AbstractBill aggVO = Constructor.construct(bill);

        SuperVO headVO = Constructor.construct(head);

        SuperVO itemVO = Constructor.construct(item);

        // 填充Value
        for (Entry<String, String> entry : filedMap.entrySet()) {
            String stuffFiled = entry.getKey();
            String materialField = entry.getValue();
            if (stuffFiled.contains(".")) {
                // 包含.说明是表体数据
                String[] splitStr = stuffFiled.split("\\.");
                String itemFiled = splitStr[splitStr.length - 1];

                // 此处可能抛出类型不匹配异常或者是设置字段找不到异常
                if (materialField != null) {
                    itemVO.setAttributeValue(itemFiled, dataViewVO.getAttributeValue(materialField));
                }
            }
            else {
                if (materialField != null) {
                    headVO.setAttributeValue(stuffFiled, dataViewVO.getAttributeValue(materialField));
                }
            }
        }

        // 组装VO
        aggVO.setParentVO(headVO);
        aggVO.setChildrenVO(new SuperVO[] {
                itemVO
        });
        return aggVO;
    }

    /**
     * 根据字段对照创建聚合VO
     *
     * @param bill
     *            聚合VO
     * @param head
     *            表头VO
     * @param item
     *            表体VO
     * @param filedMap
     *            字段映射
     * @param dataViewVO
     *            AbstractDataView
     * @return 聚合VO
     * @throws BusinessException
     *             异常
     */
    private IBill createAggVO(Class<? extends AbstractBill> bill, Class<? extends SuperVO> head,
            Class<? extends SuperVO> item, Map<String, String> filedMap, CircularlyAccessibleValueObject dataViewVO)
                    throws BusinessException {

        // 实例化聚合VO、表头VO、表体VO
        AbstractBill aggVO = Constructor.construct(bill);

        SuperVO headVO = Constructor.construct(head);

        SuperVO itemVO = Constructor.construct(item);

        // 填充Value
        for (Entry<String, String> entry : filedMap.entrySet()) {
            String stuffFiled = entry.getKey();
            String materialField = entry.getValue();
            if (stuffFiled.contains(".")) {
                // 包含.说明是表体数据
                String[] splitStr = stuffFiled.split("\\.");
                String itemFiled = splitStr[splitStr.length - 1];

                // 此处可能抛出类型不匹配异常或者是设置字段找不到异常
                if (materialField != null) {
                    itemVO.setAttributeValue(itemFiled, dataViewVO.getAttributeValue(materialField));
                }
            }
            else {
                if (materialField != null) {
                    headVO.setAttributeValue(stuffFiled, dataViewVO.getAttributeValue(materialField));
                }
            }
        }

        // 组装VO
        aggVO.setParentVO(headVO);
        aggVO.setChildrenVO(new SuperVO[] {
                itemVO
        });
        return aggVO;
    }

    @Override
    public FetchSetItemVO[] getFetchSetItems(Integer fetchType, String pkOrg, String pkGroup) throws BusinessException {

        try {
            // 查询出聚合VO
            AggFetchSetVO[] fetchSetAggVOs =
                    this.fetchsetQueryByType(fetchType, pkOrg, pkGroup, FetchSchemeEnum.NOTEXIST);

            if (fetchSetAggVOs == null || fetchSetAggVOs.length == 0) {
                return new FetchSetItemVO[0];
            }

            AggFetchSetVO fetchSetAggVO = fetchSetAggVOs[0];
            FetchSetItemVO[] itemVOs = (FetchSetItemVO[]) fetchSetAggVO.getChildren(FetchSetItemVO.class);

            if (itemVOs == null || itemVOs.length == 0) {
                return new FetchSetItemVO[0];
            }
            return itemVOs;
        }
        catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }

    @Override
    public AggFetchSetVO[] getAggfetchsetByType(Integer fetchType, String pk_org, String pk_group, int fetchScheme)
            throws BusinessException {
        return this.fetchsetQueryByType(fetchType, pk_org, pk_group, fetchScheme);

    }

    /**
     * @param fetchType
     * @param pk_org
     * @param pk_group
     * @param fetchScheme
     * @return
     * @throws BusinessException
     */
    private AggFetchSetVO[] fetchsetQueryByType(Integer fetchType, String pk_org, String pk_group, int fetchScheme)
            throws BusinessException {
        CMSqlBuilder fromWhereSql = new CMSqlBuilder();
        fromWhereSql.from(FetchSetHeadVO.getDefaultTableName());
        fromWhereSql.where();
        fromWhereSql.append("dr = 0 ");
        if (FetchTypeEnum.NOTEXIST != fetchType.intValue()) {
            fromWhereSql.append(" and " + FetchSetHeadVO.IFETCHTYPE + " = " + fetchType);
        }
        if (pk_org != null) {
            fromWhereSql.append(" and " + FetchSetHeadVO.PK_ORG, pk_org);
        }
        else {
            fromWhereSql.append(" And ");
            fromWhereSql.appendIDIsNull(FetchSetHeadVO.PK_ORG);
        }

        if (pk_group != null) {
            fromWhereSql.append(" and " + FetchSetHeadVO.PK_GROUP, pk_group);
        }
        else {
            fromWhereSql.append(" And ");
            fromWhereSql.append(FetchSetHeadVO.PK_GROUP);
        }

        if (FetchSchemeEnum.NOTEXIST != fetchScheme) {
            fromWhereSql.append(" and " + FetchSetHeadVO.IFETCHSCHEME + " = " + fetchScheme);
        }

        // 3 进行查询
        AggFetchSetVO[] obj = new EfficientBillQuery<AggFetchSetVO>(AggFetchSetVO.class).query(fromWhereSql.toString());

        if (obj == null) {
            return new AggFetchSetVO[0];
        }
        return obj;
    }

    @Override
    public Map<FetchKeyVO, AggFetchSetVO> getAggfetchset(String pk_group, String[] pk_orgs) throws BusinessException {
        try {
            Map<FetchKeyVO, AggFetchSetVO> resultMap = new HashMap<FetchKeyVO, AggFetchSetVO>();
            Map<Integer, Integer> relationMap = FetchDataObjEnum.getFetchTypeAndFetchObjRelationMap();

            CMSqlBuilder fromWhereSql = new CMSqlBuilder();
            fromWhereSql.from(FetchSetHeadVO.getDefaultTableName());
            fromWhereSql.where();
            fromWhereSql.append("dr = 0 ");

            if (CMArrayUtil.isNotEmpty(pk_orgs)) {
                fromWhereSql.append(" and " + FetchSetHeadVO.PK_ORG, pk_orgs);
            }
            else {
                fromWhereSql.append(" And ");
                fromWhereSql.appendIDIsNull(FetchSetHeadVO.PK_ORG);
            }

            if (pk_group != null) {
                fromWhereSql.append(" and " + FetchSetHeadVO.PK_GROUP, pk_group);
            }
            else {
                fromWhereSql.append(" And ");
                fromWhereSql.append(FetchSetHeadVO.PK_GROUP);
            }

            AggFetchSetVO[] objs =
                    new EfficientBillQuery<AggFetchSetVO>(AggFetchSetVO.class).query(fromWhereSql.toString());

            if (CMArrayUtil.isNotEmpty(objs)) {
                for (AggFetchSetVO vo : objs) {
                    FetchSetHeadVO headVO = vo.getParentVO();
                    if (headVO != null) {
                        // key=工厂+取数对象
                        FetchKeyVO keyVO = new FetchKeyVO();
                        keyVO.setPk_org(headVO.getPk_org());
                        keyVO.setIfetchobjtype(relationMap.get(headVO.getIfetchtype()));
//                        if(vo.getChildrenVO()!=null && vo.getChildrenVO().length >0){
//                        	FetchSetItemVO item = (FetchSetItemVO) vo.getChildrenVO()[0];
//                        	keyVO.setFator1(item.getPk_billtypeid());
//                            keyVO.setFator2(item.getPk_qcdept());
//                            keyVO.setFator3(item.getPk_costobject());
//                            keyVO.setFator4(item.getPk_serverdept());
//                            keyVO.setFator5(item.getPk_largeritem());
//                            keyVO.setFator6(item.getPk_factor());
//                        	keyVO.setFator7(item.getPk_workitem());
//
//                        }
                        

                        if (!resultMap.containsKey(keyVO)) {
                            resultMap.put(keyVO, vo);
                        }
                        else {
                            // 同一工厂、同一取数对象不能设置多个取数方案
                            ExceptionUtils
                            .wrappBusinessException(nc.vo.cm.fetchset.multilangconst.FetchsetMultiLangConst
                                    .checkFetchSet());
                        }
                    }
                }
            }
            return resultMap;
        }
        catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return null;
    }
}
