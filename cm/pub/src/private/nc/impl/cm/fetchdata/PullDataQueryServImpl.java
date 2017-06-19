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
 * ȡ����ѯʵ����
 */
public class PullDataQueryServImpl implements IPullDataQueryService {
    /**
     * ��ϵͳȡ�����ýӿ�
     */
    private IFetchSetPubQueryService fetchsetQueryService = null;

    /**
     * ��ȡ��ϵͳ���ýӿ�
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
            // ��������+��������+�ɱ�����
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
        // ����ȡ������õ���ϵͳȡ������vo   	
    	pullDataParamVos.setAttributeValue("fetcheSetAggVO", null);
        if (fetchSetAggVo == null) {
            return null;
        }        
        //2017-06-15 ���ö�Ӧ����ϵͳȡ������VO����Ϊ�����ļ������ݣ����ǰ��յ������������Ψһ��
    	pullDataParamVos.setAttributeValue("fetcheSetAggVO", fetchSetAggVo);   	
        String pk_org = keyVO.getPk_org();
        Integer ifetchobjtype = keyVO.getIfetchobjtype();
        // ȡ����ʼ����
        pullDataParamVos.setDbegindate(beginEndDate[0]);
        pullDataParamVos.setDenddate(beginEndDate[1]);
        // �������ȡ������null
        pullDataParamVos.setNday(this.geWeekNday(fetchSetAggVo));
        // ȡ����������/����
        pullDataParamVos.setIfetchscheme(this.getFetchSchema(fetchSetAggVo));
        // ȡ������
        pullDataParamVos.setIfetchobjtype(ifetchobjtype);
        // ȡ������
        pullDataParamVos.setPulldatatype(this.getPullDataType(ifetchobjtype));
        // ���ý�������
        this.getTranIds(pullDataParamVos, fetchSetAggVo, ifetchobjtype);
        // ���óɱ�����
        this.getCostCenterIds(pullDataParamVos, fetchSetAggVo, ifetchobjtype);
        // ����ȡ��״̬
        pullDataParamVos.setBfetched(UFBoolean.FALSE);
   
        IUIDataSet strategy =
                new FetchSchemaFactory().createSchemaFactory(pullDataParamVos.getIfetchscheme(), ifetchobjtype);
        UIAllDataVO vos = strategy.getVos4UiDataSet(pullDataParamVos);
        if (vos != null && vos.getVos() != null) {
            // ���ÿ����֯��Ϣ
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
     * ��ѯ��ʼ������ key=����+ȡ������,1 ���ϳ���2����Ʒ�� 3 ��Ʒ 4 ��ҵ 5 ����ί�� 6���������
     */
    @Override
    public Map<FetchKeyVO, UIAllDataVO> getVos4UiDataSet(PullDataStateVO pullDataParamVos) throws BusinessException {
        Map<FetchKeyVO, UIAllDataVO> resultMap = new HashMap<FetchKeyVO, UIAllDataVO>();

        String pk_accperiodscheme = "";
        String accountPriod = pullDataParamVos.getCperiod();
        try {
            pk_accperiodscheme = BDAdapter.getPKAccountSchemeByOrg(pullDataParamVos.getPk_org());

            if (CMStringUtil.isEmpty(pullDataParamVos.getCperiod())) {
                // ��ȡ����ڼ�
                accountPriod = BDAdapter.getAccountPriod(pullDataParamVos.getPk_org(), pullDataParamVos.getBusiDate());
                pullDataParamVos.setCperiod(accountPriod);

            }
            UFDate[] beginEndDate =
                    BDAdapter.getBeginAndEndDateByPeriod(pullDataParamVos.getPk_org(), pullDataParamVos.getCperiod());

            // ����ȡ������õ���ϵͳȡ������vo
            // key=����+ȡ������{1 ���ϳ���2����Ʒ�� 3 ��Ʒ 4 ��ҵ 5 ����ί�� 6 ���������}
            Map<FetchKeyVO, AggFetchSetVO> fetchSetAggVoMap = this.getFetchSetAggVo(pullDataParamVos);

            // ��ϵͳȡ�����õ��޸�ֻӰ��δ�����������ڼ��Ѿ�ȡ������
            // �޸���ϵͳȡ������ʱ��ֻӰ��δȡ�����ڼ䣨��û��ִ�й�ȡ�����ڼ䣩
            // key=����+ȡ������{1 ���ϳ���2����Ʒ�� 3 ��Ʒ 4 ��ҵ 5 ����ί�� 6 ���������}
            Map<FetchKeyVO, List<PullDataStateVO>> pullDataStateMap = this.getAlreadyExistFetchInfos(pullDataParamVos);

            for (Entry<FetchKeyVO, AggFetchSetVO> entry : fetchSetAggVoMap.entrySet()) {
                // key=����+ȡ������
                FetchKeyVO keyVO = entry.getKey();
                // ����ȡ������õ���ϵͳȡ������vo
                AggFetchSetVO fetchSetAggVo = fetchSetAggVoMap.get(keyVO);
                String pk_org = keyVO.getPk_org();
                Integer ifetchobjtype = keyVO.getIfetchobjtype();

                // ����ȡ�����ù���ȡ��״̬��Ϣ
                List<PullDataStateVO> constructItemLst =
                        this.getPullDataStateVOByFetchSet(pullDataParamVos, fetchSetAggVo, beginEndDate, keyVO);
                // �������ݿ�����ȡ��״̬��Ϣ
                List<PullDataStateVO> dbItemLst = new ArrayList<PullDataStateVO>();
                if (CMMapUtil.isNotEmpty(pullDataStateMap)) {
                    dbItemLst = pullDataStateMap.get(entry.getKey());
                }

                UIAllDataVO allVO = new UIAllDataVO();
                allVO.setPk_accperiodscheme(pk_accperiodscheme);
                allVO.setDaccountperiod(accountPriod);
                allVO.setPk_org(pk_org);
                // �Ѿ�ȡ��ֵ
                if (CMCollectionUtil.isNotEmpty(dbItemLst)) {
                    List<PullDataStateVO> resultItemLst = new ArrayList<PullDataStateVO>();
                    Map<String, Boolean> resultItemStatusMap = new HashMap<String, Boolean>();
                    for (PullDataStateVO info : dbItemLst) {
                        UFBoolean isFetched = info.getBfetched();
                        UFBoolean isExistInFetchSet = this.isExistInFetchSet(info, constructItemLst);
                        String key = this.getKeyForPullDataStateVO(info);
                        if (isFetched.booleanValue()) {
                            // ȡ����:ֱ��װ��
                            resultItemLst.add(info);
                            resultItemStatusMap.put(key, true);

                        }
                        else if (isExistInFetchSet.booleanValue()) {
                            // δȡ����:ȡ�������д��ڣ������
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
                        // ��ȡ������ϵͳȡ����������һ������
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
                // δȡ��ֵ
                else {
                    if (fetchSetAggVo == null) {
                        continue;
                    }
                    // ����ȡ��״̬��Ϣ
                    if (CMCollectionUtil.isNotEmpty(constructItemLst)) {
                        allVO.setVos(constructItemLst.toArray(new PullDataStateVO[0]));
                    }
                    // ����ȡ��������Ϣ
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

    // ����ȡ�������ȡȡ������
    private Integer getPullDataType(Integer ifechType) {
        if (FetchDataObjEnum.ISSTUFF.toIntValue() == ifechType.intValue()) {
            return PullDataTypeEnum.FIFETCHDATA.toIntValue();// �������ȡ��
        }
        else if (FetchDataObjEnum.MATERIALOUT.toIntValue() == ifechType.intValue()) {
            return PullDataTypeEnum.FIFETCHDATA.toIntValue();// �������ȡ��
        }
        else if (FetchDataObjEnum.PRODIN.toIntValue() == ifechType.intValue()) {
            return PullDataTypeEnum.FIFETCHDATA.toIntValue();// �������ȡ��
        }
        else if (FetchDataObjEnum.ACT.toIntValue() == ifechType.intValue()) {
            return PullDataTypeEnum.MMFETCHDATA.toIntValue();// ����ȡ��
        }
        else if (FetchDataObjEnum.GXWW.toIntValue() == ifechType.intValue()) {
            return PullDataTypeEnum.MMFETCHDATA.toIntValue();// ����ȡ��
        }
        else if (FetchDataObjEnum.SPOIL.toIntValue() == ifechType.intValue()) {
            return PullDataTypeEnum.SCRAPFETCHDATA.toIntValue();// ���---��Ʒȡ��
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
            return PullDataTypeEnum.FIFETCHDATA.toIntValue();// Ĭ��:�������ȡ��
        }
    }

    /**
     * ȡ�Ѿ����ڵ�ȡ����Ϣ key=����+ȡ������value=ȡ����Ϣ
     */
    private Map<FetchKeyVO, List<PullDataStateVO>> getAlreadyExistFetchInfos(PullDataStateVO pullDataParamVos) {
        String sql = this.getComonSql(pullDataParamVos);
        VOQuery<PullDataStateVO> query = new VOQuery<PullDataStateVO>(PullDataStateVO.class);
        PullDataStateVO[] stateVos = query.queryWithWhereKeyWord(sql, "order by dbegindate,ctranstypeid");
        Map<FetchKeyVO, List<PullDataStateVO>> result = new HashMap<FetchKeyVO, List<PullDataStateVO>>();
        if (CMArrayUtil.isNotEmpty(stateVos)) {
            for (PullDataStateVO vo : stateVos) {
                // ȡ������ 1 ���ϳ���2����Ʒ�� 3 ��Ʒ 4 ��ҵ 5 ����ί�� 6 ���������
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
     * �õ�ȡ������:������֯and���ţ�ȡ��������ȡ���������ڸü��ţ�����֯��ֻ������һ��ȡ������
     *
     * @param factoryPk
     *            ��֯
     * @param defaultFetchObj
     *            Ĭ��ȡ������
     * @return key=����OID+ȡ������
     */
    private Map<FetchKeyVO, AggFetchSetVO> getFetchSetAggVo(PullDataStateVO pullDataParamVos) {

        // key=����OID+ȡ������
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
     * �õ�ȡ������
     */
    private Integer getFetchSchema(AggFetchSetVO fetchSetAggVo) {
        return (Integer) fetchSetAggVo.getParentVO().getAttributeValue(FetchSetHeadVO.IFETCHSCHEME);
    }

    /**
     * �õ���������
     *
     * @param fetchSetAggVo
     *            ��ϵͳȡ������vo
     * @return ����
     */
    private Integer geWeekNday(AggFetchSetVO fetchSetAggVo) {
        return (Integer) fetchSetAggVo.getParentVO().getAttributeValue(FetchSetHeadVO.NDAY);
    }

    /**
     * ��ȡ��ϵͳȡ�������н�������
     */
    private void getTranIds(PullDataStateVO paramvo, AggFetchSetVO fetchSetAggVo, Integer ifetchType) {

        if (ifetchType.equals(FetchDataObjEnum.ISSTUFF.toIntValue())
                || ifetchType.equals(FetchDataObjEnum.GXWW.toIntValue())
                || ifetchType.equals(FetchDataObjEnum.MATERIALOUT.toIntValue())
                || ifetchType.equals(FetchDataObjEnum.PRODIN.toIntValue())
                || ifetchType.equals(FetchDataObjEnum.SPOIL.toIntValue())) {
            FetchSetItemVO[] itemArr = (FetchSetItemVO[]) fetchSetAggVo.getChildren(FetchSetItemVO.class);
            if (CMArrayUtil.isEmpty(itemArr)) {// ��ϵͳȡ��û�����ý�������
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
     * TODO����·���Ƿ�����
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
     * ����sql����ƴ��
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
        // ȡ�����֯��Ӧ������֯�����˲�
        String pk_accountingbook = null;
        try {
            pk_accountingbook = BDAdapter.getMainAccountBookByStockOrgId(pk_org);
            if (CMStringUtil.isEmpty(pk_accountingbook)) {
                ExceptionUtils.wrappBusinessException("û���ҵ�������֯��Ӧ�Ĳ�����֯�����˲���");
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
