package nc.impl.cm.fetchdata;

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
import nc.bs.cm.fetchdata.factory.FetchSchemaFactory;
import nc.bs.cm.fetchdata.uidataset.IUIDataSet;
import nc.bs.cm.fetchdata.util.QueryUtil;
import nc.bs.framework.common.NCLocator;
import nc.cmpub.business.adapter.BDAdapter;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.itf.cm.fetchdata.IPullDataQueryService;
import nc.pubitf.cm.fetchset.cm.iaretrieval.IFetchSetPubQueryService;
import nc.vo.cm.autocostcalculation.enumeration.AutoCostStateEnum;
import nc.vo.cm.fetchdata.entity.FetchKeyVO;
import nc.vo.cm.fetchdata.entity.PullDataStateVO;
import nc.vo.cm.fetchdata.entity.UIAllDataVO;
import nc.vo.cm.fetchdata.enumeration.FetchDataObjEnum;
import nc.vo.cm.fetchdata.enumeration.PullDataTypeEnum;
import nc.vo.cm.fetchset.entity.AggFetchSetVO;
import nc.vo.cm.fetchset.entity.FetchSetHeadVO;
import nc.vo.cm.fetchset.entity.FetchSetItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 取数查询实现类
 */
public class PullDataQueryServImpl implements IPullDataQueryService {
    /**
     * 外系统取数设置接口
     */
    private IFetchSetPubQueryService fetchsetQueryService = null;

    /**
     * 获取外系统设置接口
     */
    private IFetchSetPubQueryService getIFetchsetQueryService() {
        if (this.fetchsetQueryService == null) {
            this.fetchsetQueryService = NCLocator.getInstance().lookup(IFetchSetPubQueryService.class);
        }
        return this.fetchsetQueryService;
    }

    private String nullToEmpty(Object value) {
        if (value == null || value.toString().equals("~")) {
            return "";
        }
        else {
            return value.toString();
        }
    }

    private String getKeyForPullDataStateVO(PullDataStateVO vo) {
        String key =this.nullToEmpty(vo.getIfetchobjtype())+
                this.nullToEmpty(vo.getCtranstypeid())
                + this.nullToEmpty(vo.getCcostcenterid())+this.nullToEmpty(vo.getPk_workitem())
                +this.nullToEmpty(vo.getPk_costobject())+this.nullToEmpty(vo.getPk_qcdept())
                +this.nullToEmpty(vo.getPk_serverdept())+this.nullToEmpty(vo.getPk_largeritem())
                +this.nullToEmpty(vo.getPk_factor()) ;
                		
        return key;	
    }

    private UFBoolean isExistInFetchSet(PullDataStateVO targetVO, List<PullDataStateVO> constructItemLst)
            throws BusinessException {
        UFBoolean isExist = UFBoolean.FALSE;
        if (CMCollectionUtil.isEmpty(constructItemLst)) {
            return isExist;	
        }
        else {
            // 单据类型+交易类型+成本中心
            String key1 = this.getKeyForPullDataStateVO(targetVO);
            for (PullDataStateVO infoVO : constructItemLst) {
                String key2 = this.getKeyForPullDataStateVO(infoVO);
                if (key1.equals(key2)) {
                    isExist = UFBoolean.TRUE;
                    return isExist;
                }
            }
        }
        return isExist;
    }

    private List<PullDataStateVO> getPullDataStateVOByFetchSet(PullDataStateVO pullDataParamVos,
            AggFetchSetVO fetchSetAggVo, UFDate[] beginEndDate, FetchKeyVO keyVO) throws BusinessException {
        // 根据取数对象得到外系统取数方案vo   	
    	pullDataParamVos.setAttributeValue("fetcheSetAggVO", null);
        if (fetchSetAggVo == null) {
            return null;
        }        
        //2017-06-15 设置对应的外系统取数方案VO，因为新增的几个单据，不是按照单据类型类控制唯一性
    	pullDataParamVos.setAttributeValue("fetcheSetAggVO", fetchSetAggVo);   	
        String pk_org = keyVO.getPk_org();
        Integer ifetchobjtype = keyVO.getIfetchobjtype();
        // 取数起始日期
        pullDataParamVos.setDbegindate(beginEndDate[0]);
        pullDataParamVos.setDenddate(beginEndDate[1]);
        // 如果是月取数则是null
        pullDataParamVos.setNday(this.geWeekNday(fetchSetAggVo));
        // 取数方案：月/周期
        pullDataParamVos.setIfetchscheme(this.getFetchSchema(fetchSetAggVo));
        // 取数对象
        pullDataParamVos.setIfetchobjtype(ifetchobjtype);
        // 取数类型
        pullDataParamVos.setPulldatatype(this.getPullDataType(ifetchobjtype));
        // 设置交易类型
        this.getTranIds(pullDataParamVos, fetchSetAggVo, ifetchobjtype);
        // 设置成本中心
        this.getCostCenterIds(pullDataParamVos, fetchSetAggVo, ifetchobjtype);
        // 设置取数状态
        pullDataParamVos.setBfetched(UFBoolean.FALSE);
   
        IUIDataSet strategy =
                new FetchSchemaFactory().createSchemaFactory(pullDataParamVos.getIfetchscheme(), ifetchobjtype);
        UIAllDataVO vos = strategy.getVos4UiDataSet(pullDataParamVos);
        if (vos != null && vos.getVos() != null) {
            // 设置库存组织信息
            for (PullDataStateVO vo : vos.getVos()) {
                vo.setPk_org(pk_org);
            }
            return Arrays.asList(vos.getVos());
        }
        else {
            return null;
        }

    }

    /**
     * 查询初始化数据 key=工厂+取数对象,1 材料出，2产成品入 3 废品 4 作业 5 工序委外 6其他出入库
     */
    @Override
    public Map<FetchKeyVO, UIAllDataVO> getVos4UiDataSet(PullDataStateVO pullDataParamVos) throws BusinessException {
        Map<FetchKeyVO, UIAllDataVO> resultMap = new HashMap<FetchKeyVO, UIAllDataVO>();

        String pk_accperiodscheme = "";
        String accountPriod = pullDataParamVos.getCperiod();
        try {
            pk_accperiodscheme = BDAdapter.getPKAccountSchemeByOrg(pullDataParamVos.getPk_org());

            if (CMStringUtil.isEmpty(pullDataParamVos.getCperiod())) {
                // 获取会计期间
                accountPriod = BDAdapter.getAccountPriod(pullDataParamVos.getPk_org(), pullDataParamVos.getBusiDate());
                pullDataParamVos.setCperiod(accountPriod);

            }
            UFDate[] beginEndDate =
                    BDAdapter.getBeginAndEndDateByPeriod(pullDataParamVos.getPk_org(), pullDataParamVos.getCperiod());

            // 根据取数对象得到外系统取数方案vo
            // key=工厂+取数对象{1 材料出，2产成品入 3 废品 4 作业 5 工序委外 6 其它出入库}
            Map<FetchKeyVO, AggFetchSetVO> fetchSetAggVoMap = this.getFetchSetAggVo(pullDataParamVos);

            // 外系统取数设置的修改只影响未来，如果会计期间已经取过数，
            // 修改外系统取数方案时，只影响未取数的期间（从没有执行过取数的期间）
            // key=工厂+取数对象{1 材料出，2产成品入 3 废品 4 作业 5 工序委外 6 其它出入库}
            Map<FetchKeyVO, List<PullDataStateVO>> pullDataStateMap = this.getAlreadyExistFetchInfos(pullDataParamVos);

            for (Entry<FetchKeyVO, AggFetchSetVO> entry : fetchSetAggVoMap.entrySet()) {
                // key=工厂+取数对象
                FetchKeyVO keyVO = entry.getKey();
                // 根据取数对象得到外系统取数方案vo
                AggFetchSetVO fetchSetAggVo = fetchSetAggVoMap.get(keyVO);
                String pk_org = keyVO.getPk_org();
                Integer ifetchobjtype = keyVO.getIfetchobjtype();

                // 根据取数设置构造取数状态信息
                List<PullDataStateVO> constructItemLst =
                        this.getPullDataStateVOByFetchSet(pullDataParamVos, fetchSetAggVo, beginEndDate, keyVO);
                // 根据数据库表加载取数状态信息
                List<PullDataStateVO> dbItemLst = new ArrayList<PullDataStateVO>();
                if (CMMapUtil.isNotEmpty(pullDataStateMap)) {
                    dbItemLst = pullDataStateMap.get(entry.getKey());
                }

                UIAllDataVO allVO = new UIAllDataVO();
                allVO.setPk_accperiodscheme(pk_accperiodscheme);
                allVO.setDaccountperiod(accountPriod);
                allVO.setPk_org(pk_org);
                // 已经取过值
                if (CMCollectionUtil.isNotEmpty(dbItemLst)) {
                    List<PullDataStateVO> resultItemLst = new ArrayList<PullDataStateVO>();
                    Map<String, Boolean> resultItemStatusMap = new HashMap<String, Boolean>();
                    for (PullDataStateVO info : dbItemLst) {
                        UFBoolean isFetched = info.getBfetched();
                        UFBoolean isExistInFetchSet = this.isExistInFetchSet(info, constructItemLst);
                        String key = this.getKeyForPullDataStateVO(info);
                        if (isFetched.booleanValue()) {
                            // 取过数:直接装载
                            resultItemLst.add(info);
                            resultItemStatusMap.put(key, true);

                        }
                        else if (isExistInFetchSet.booleanValue()) {
                            // 未取过数:取数设置中存在，则加载
                            resultItemLst.add(info);
                            resultItemStatusMap.put(key, true);
                        }
                    }
                    if (CMCollectionUtil.isNotEmpty(constructItemLst)) {
                        for (PullDataStateVO info : constructItemLst) {
                            String key = this.getKeyForPullDataStateVO(info);
                            if (!resultItemStatusMap.containsKey(key)) {
                                resultItemLst.add(info);
                                resultItemStatusMap.put(key, true);
                            }
                        }
                    }

                    if (CMCollectionUtil.isNotEmpty(resultItemLst)) {
                        // 将取到的外系统取数方案设置一个返回
                        AggFetchSetVO fetchSetVo = new AggFetchSetVO();
                        FetchSetHeadVO headvo = new FetchSetHeadVO();
                        headvo.setPk_org(pk_org);
                        headvo.setIfetchtype(ifetchobjtype);
                        headvo.setIfetchscheme(resultItemLst.get(0).getIfetchscheme());
                        fetchSetVo.setParentVO(headvo);
                        allVO.setFetchSetAggVo(new AggFetchSetVO[] {
                            fetchSetVo
                        });
                        allVO.setFetchBefore(true);
                        allVO.setVos(resultItemLst.toArray(new PullDataStateVO[0]));
                        resultMap.put(keyVO, allVO);
                    }
                }
                // 未取过值
                else {
                    if (fetchSetAggVo == null) {
                        continue;
                    }
                    // 设置取数状态信息
                    if (CMCollectionUtil.isNotEmpty(constructItemLst)) {
                        allVO.setVos(constructItemLst.toArray(new PullDataStateVO[0]));
                    }
                    // 设置取数方案信息
                    allVO.setFetchSetAggVo(new AggFetchSetVO[] {
                            fetchSetAggVoMap.get(keyVO)
                    });

                    resultMap.put(keyVO, allVO);

                }

            }

        }
        catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
        return resultMap;
    }

    // 根据取数对象获取取数类型
    private Integer getPullDataType(Integer ifechType) {
        if (FetchDataObjEnum.ISSTUFF.toIntValue() == ifechType.intValue()) {
            return PullDataTypeEnum.FIFETCHDATA.toIntValue();// 存货核算取数
        }
        else if (FetchDataObjEnum.MATERIALOUT.toIntValue() == ifechType.intValue()) {
            return PullDataTypeEnum.FIFETCHDATA.toIntValue();// 存货核算取数
        }
        else if (FetchDataObjEnum.PRODIN.toIntValue() == ifechType.intValue()) {
            return PullDataTypeEnum.FIFETCHDATA.toIntValue();// 存货核算取数
        }
        else if (FetchDataObjEnum.ACT.toIntValue() == ifechType.intValue()) {
            return PullDataTypeEnum.MMFETCHDATA.toIntValue();// 制造取数
        }
        else if (FetchDataObjEnum.GXWW.toIntValue() == ifechType.intValue()) {
            return PullDataTypeEnum.MMFETCHDATA.toIntValue();// 制造取数
        }
        else if (FetchDataObjEnum.SPOIL.toIntValue() == ifechType.intValue()) {
            return PullDataTypeEnum.SCRAPFETCHDATA.toIntValue();// 库存---废品取数
        }else if(FetchDataObjEnum.DINGE.toIntValue() == ifechType.intValue()){
        	
            return PullDataTypeEnum.ALL;

        }else if(FetchDataObjEnum.CHUYUN.toIntValue() == ifechType.intValue()){
        	
            return PullDataTypeEnum.ALL;

        }else if(FetchDataObjEnum.JIANYAN.toIntValue() == ifechType.intValue()){
        	
            return PullDataTypeEnum.ALL;

        }else if(FetchDataObjEnum.HUANBAO.toIntValue() == ifechType.intValue()){
        	
            return PullDataTypeEnum.ALL;

        }
        else {
            return PullDataTypeEnum.FIFETCHDATA.toIntValue();// 默认:存货核算取数
        }
    }

    /**
     * 取已经存在的取数信息 key=工厂+取数对象，value=取数信息
     */
    private Map<FetchKeyVO, List<PullDataStateVO>> getAlreadyExistFetchInfos(PullDataStateVO pullDataParamVos) {
        String sql = this.getComonSql(pullDataParamVos);
        VOQuery<PullDataStateVO> query = new VOQuery<PullDataStateVO>(PullDataStateVO.class);
        PullDataStateVO[] stateVos = query.queryWithWhereKeyWord(sql, "order by dbegindate,ctranstypeid");
        Map<FetchKeyVO, List<PullDataStateVO>> result = new HashMap<FetchKeyVO, List<PullDataStateVO>>();
        if (CMArrayUtil.isNotEmpty(stateVos)) {
            for (PullDataStateVO vo : stateVos) {
                // 取数对象： 1 材料出，2产成品入 3 废品 4 作业 5 工序委外 6 其它出入库
                FetchKeyVO keyVO = new FetchKeyVO();
                keyVO.setPk_org(vo.getPk_org());
                keyVO.setIfetchobjtype(vo.getIfetchobjtype());
            	keyVO.setFator1(vo.getCtranstypeid());
                keyVO.setFator2(vo.getPk_qcdept());
                keyVO.setFator3(vo.getPk_costobject());
                keyVO.setFator4(vo.getPk_serverdept());
                keyVO.setFator5(vo.getPk_largeritem());
                keyVO.setFator6(vo.getPk_factor());
                keyVO.setFator7(vo.getPk_workitem());


                if (result.containsKey(keyVO)) {
                    result.get(keyVO).add(vo);
                }
                else {
                    List<PullDataStateVO> list = new ArrayList<PullDataStateVO>();
                    list.add(vo);
                    result.put(keyVO, list);
                }

            }
        }
        return result;
    }

    /**
     * 得到取数方案:根据组织and集团，取数对象获得取数方案，在该集团，该组织下只可能有一个取数方案
     *
     * @param factoryPk
     *            组织
     * @param defaultFetchObj
     *            默认取数对象
     * @return key=工厂OID+取数对象
     */
    private Map<FetchKeyVO, AggFetchSetVO> getFetchSetAggVo(PullDataStateVO pullDataParamVos) {

        // key=工厂OID+取数对象
        Map<FetchKeyVO, AggFetchSetVO> resultMap = new HashMap<FetchKeyVO, AggFetchSetVO>();
        try {
            resultMap =
                    this.getIFetchsetQueryService().getAggfetchset(pullDataParamVos.getPk_group(),
                            pullDataParamVos.getPk_orgs());
        }
        catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }

        return resultMap;

    }

    /**
     * 得到取数方案
     */
    private Integer getFetchSchema(AggFetchSetVO fetchSetAggVo) {
        return (Integer) fetchSetAggVo.getParentVO().getAttributeValue(FetchSetHeadVO.IFETCHSCHEME);
    }

    /**
     * 得到周期天数
     *
     * @param fetchSetAggVo
     *            外系统取数设置vo
     * @return 天数
     */
    private Integer geWeekNday(AggFetchSetVO fetchSetAggVo) {
        return (Integer) fetchSetAggVo.getParentVO().getAttributeValue(FetchSetHeadVO.NDAY);
    }

    /**
     * 获取外系统取数的所有交易类型
     */
    private void getTranIds(PullDataStateVO paramvo, AggFetchSetVO fetchSetAggVo, Integer ifetchType) {

        if (ifetchType.equals(FetchDataObjEnum.ISSTUFF.toIntValue())
                || ifetchType.equals(FetchDataObjEnum.GXWW.toIntValue())
                || ifetchType.equals(FetchDataObjEnum.MATERIALOUT.toIntValue())
                || ifetchType.equals(FetchDataObjEnum.PRODIN.toIntValue())
                || ifetchType.equals(FetchDataObjEnum.SPOIL.toIntValue())) {
            FetchSetItemVO[] itemArr = (FetchSetItemVO[]) fetchSetAggVo.getChildren(FetchSetItemVO.class);
            if (CMArrayUtil.isEmpty(itemArr)) {// 外系统取数没有设置交易类型
                return;
            }
            FetchSetItemVO mid = null;
            int j;
            for (int i = 0; i < itemArr.length - 1; i++) {
                j = itemArr.length - 1;
                while (j > i) {

                    if (itemArr[j].getPk_billtypecode().compareTo(itemArr[j - 1].getPk_billtypecode()) < 0) {
                        mid = itemArr[j];
                        itemArr[j] = itemArr[j - 1];
                        itemArr[j - 1] = mid;
                    }
                    j--;
                }
            }
            List<String> tranpks = new ArrayList<String>();
            for (FetchSetItemVO itemvo : itemArr) {
                tranpks.add(itemvo.getPk_billtypeid());
            }
            paramvo.setCtranstypeids(tranpks.toArray(new String[0]));
        }
   
    }

    private void getCostCenterIds(PullDataStateVO paramvo, AggFetchSetVO fetchSetAggVo, Integer ifetchType) {
        if (Integer.valueOf(FetchDataObjEnum.ACT.getEnumValue().getValue()).compareTo(ifetchType) == 0) {
            FetchSetItemVO[] itemArr = (FetchSetItemVO[]) fetchSetAggVo.getChildren(FetchSetItemVO.class);
            Set<String> costcenterids = new HashSet<String>();
            if (CMArrayUtil.isNotEmpty(itemArr)) {
                for (FetchSetItemVO itemvo : itemArr) {
                    costcenterids.add(itemvo.getPk_costcenter());
                }
            }
            paramvo.setCostcenterids(costcenterids.toArray(new String[0]));
        }
    }

    /**
     * TODO工艺路线是否启用
     */
    public boolean getIsGylxqy(String pkOrg, String pkGroup) {
        boolean gylxqy = false;
        return gylxqy;
    }

    @Override
    public Integer[] queryVoFromPullStatusInfo(PullDataStateVO pullDataStateVOs) throws BusinessException {
        return QueryUtil.queryVoFromPullStatusInfo(pullDataStateVOs);
    }

    /**
     * 公共sql进行拼接
     */
    private String getComonSql(PullDataStateVO pullDataParamVos) {
        CMSqlBuilder sb = new CMSqlBuilder();
        sb.where();
        sb.append(PullDataStateVO.PK_GROUP, pullDataParamVos.getPk_group());
        sb.append(" and ");
        sb.append(PullDataStateVO.PK_ORG, pullDataParamVos.getPk_orgs());
        sb.append(" and ");
        if (FetchDataObjEnum.ALL.equals(pullDataParamVos.getIfetchobjtype())) {
            int[] all = FetchDataObjEnum.ALL_FETCHDATAOBJ;

            sb.append(PullDataStateVO.IFETCHOBJTYPE, all);
        }
        else {
            sb.append(PullDataStateVO.IFETCHOBJTYPE, pullDataParamVos.getIfetchobjtype().toString());
        }
        sb.append(" and ");
        sb.append(PullDataStateVO.CPERIOD, pullDataParamVos.getCperiod());
        sb.append(" and ");
        sb.append(" dr = 0 ");
        return sb.toString();
    }

    @Override
    public int checkFetchDataState(String pkOrg, String period) throws BusinessException {
        CMSqlBuilder sql = new CMSqlBuilder();
        sql.select();
        sql.append(FetchSetHeadVO.getDefaultTableName() + "." + FetchSetHeadVO.IFETCHTYPE);
        sql.append(" , case when info.bfetched is null then 'N' else info.bfetched end ");
        sql.from(FetchSetHeadVO.getDefaultTableName());
        sql.append(" left join ");
        sql.l();
        sql.select();
        sql.append(PullDataStateVO.PK_ORG);
        sql.append("," + PullDataStateVO.IFETCHOBJTYPE);
        sql.append("," + PullDataStateVO.BFETCHED);
        sql.from(PullDataStateVO.getDefaultTableName());
        sql.where();
        sql.append(PullDataStateVO.PK_ORG, pkOrg);
        sql.and();
        sql.append(PullDataStateVO.CPERIOD, period);
        sql.appendDr();
        sql.r();
        sql.append(" info ");
        sql.on("info", PullDataStateVO.IFETCHOBJTYPE, FetchSetHeadVO.getDefaultTableName(), FetchSetHeadVO.IFETCHTYPE);
        sql.and();
        sql.append(" info.pk_org = " + FetchSetHeadVO.getDefaultTableName() + "." + FetchSetHeadVO.PK_ORG);
        sql.where();
        sql.append(FetchSetHeadVO.getDefaultTableName() + "." + FetchSetHeadVO.PK_ORG, pkOrg);
        sql.and();
        sql.appendDr(FetchSetHeadVO.getDefaultTableName());

        IRowSet rows = new DataAccessUtils().query(sql.toString());
        if (rows.size() < 1) {
            return AutoCostStateEnum.NOSET.toIntValue();
        }
        rows.next();
        String state = rows.getString(1);
        while (rows.next()) {
            if (!state.equals(rows.getString(1))) {
                return AutoCostStateEnum.PARTFINISH.toIntValue();
            }
        }
        if (state.equals(UFBoolean.FALSE.toString())) {
            return AutoCostStateEnum.BEGIN.toIntValue();
        }
        return AutoCostStateEnum.FINISH.toIntValue();

    }

    @Override
    public String getAccountTypeByOrg(String pk_org) throws BusinessException {
        // 取库存组织对应财务组织的主账簿
        String pk_accountingbook = null;
        try {
            pk_accountingbook = BDAdapter.getMainAccountBookByStockOrgId(pk_org);
            if (CMStringUtil.isEmpty(pk_accountingbook)) {
                ExceptionUtils.wrappBusinessException("没有找到工厂组织对应的财务组织的主账簿！");
            }
            CMSqlBuilder sql = new CMSqlBuilder();
            sql.append("select pk_setofbook from ORG_ACCOUNTINGBOOK where PK_ACCOUNTINGBOOK = '" + pk_accountingbook
                    + "'");
            DataAccessUtils dao = new DataAccessUtils();
            String[] pk_setofbook = dao.query(sql.toString()).toOneDimensionStringArray();
            return pk_setofbook[0];
        }
        catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return null;
    }
}
