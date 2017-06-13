package nc.bpm.fa.pfxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.xml.out.tool.XmlOutTool;
import nc.itf.fa.prv.IReduce;
import nc.itf.fa.service.IAssetService;
import nc.itf.fa.service.IReduceImport;
import nc.itf.so.m30.revise.ISaleOrderReviseMaintainApp;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.pub.fa.card.AssetFieldConst;
import nc.pub.fa.common.consts.BillTypeConst;
import nc.pub.fa.common.consts.ClassIDConst;
import nc.pub.fa.common.manager.VOManager;
import nc.pub.fa.common.util.StringUtils;
import nc.pub.fa.dep.ReduceSimulateManager;
import nc.pubitf.bd.accessor.GeneralAccessorFactory;
import nc.pubitf.bd.accessor.IGeneralAccessor;
import nc.pubitf.org.IAccountingBookPubService;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.common.util.ExceptionUtils;
import nc.vo.am.common.util.StringTools;
import nc.vo.am.proxy.AMProxy;
import nc.vo.bd.accessor.IBDData;
import nc.vo.fa.asset.AssetVO;
import nc.vo.fa.reduce.ReduceBodyVO;
import nc.vo.fa.reduce.ReduceHeadVO;
import nc.vo.fa.reduce.ReduceVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.m30.revise.entity.SaleOrderHistoryVO;

/**
 * 
 * for BPM 资产减少单接口处理插件
 *
 */
public class AddReducePlugin extends AbstractPfxxPlugin {
	// Map<pk_card, 模拟折旧值>
    private Map<String, UFDouble> simDepamountMap = new HashMap<String, UFDouble>();
    // Map<pk_card, 累计折旧>
    private Map<String, UFDouble> simAccudepMap = new HashMap<String, UFDouble>();
    
	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		ReduceVO billVO = (ReduceVO) vo;
        
        // 设置新的单据号
        // 调用单据号接口单据号
        // 获取单据号管理服务
        IBillcodeManage billCodeManager = AMProxy.lookup(IBillcodeManage.class);
        // 取得新的单据号
        String newBill_code =
                billCodeManager.getPreBillCode_RequiresNew(BillTypeConst.REDUCE,
                        ((ReduceHeadVO) billVO.getParent()).getPk_group(),
                        ((ReduceHeadVO) billVO.getParent()).getPk_org());
        ((ReduceHeadVO) billVO.getParent()).setBill_code(newBill_code);
        
        ReduceHeadVO headVO = (ReduceHeadVO) billVO.getParentVO();
        // 单据保存或者更新之前调用的查询操作，查询当前单据以前是否被导入过
        // 如果已经被导入过，那么返回已经导入单据的PK，否则返回NULL值
        String billPK =
                PfxxPluginUtils.queryBillPKBeforeSaveOrUpdate(swapContext.getBilltype(), swapContext.getDocID(),
                        swapContext.getOrgPk());
        
        // 如果导入过
        if (null != billPK && billPK.length() > 0) {
            // 查看配置文件信息是否允许导入重复数据
            if (swapContext.getReplace().equalsIgnoreCase("N")) {
                throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("farule_0",
                        "02012060-0037")/*
                                         * @res
                                         * "不允许重复导入单据，如果想更新已导入单据，请把数据文件的replace标志设为‘Y’！"
                                         */);
            }
            // 设置主键
            headVO.setPrimaryKey(billPK);
            // 查询数据库中是否存在该单据
            ReduceVO reduceVO = ArrayUtils.getFirstElem(AMProxy.lookup(IReduceImport.class).queryReduceVO(billPK));
            if (null != reduceVO) {
                throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("farule_0",
                        "02012060-0038")/* @res "该单据已经导入，并且已经审核，不允许重复导入。" */);
            }
        }
        
        // 2. 基本校验
        checkBill(billVO);
        
        // 3. 模拟折旧
        querySimulateDep(billVO);
        
        // 3. 查询卡片
        AssetVO[] cardVOs = queryCardVOs(billVO);
        
        // 4. 设置减少单
        setBillVOValue(billVO, cardVOs);
        
        // 5. 生成减少单
        ReduceVO reduceVO = AMProxy.lookup(IReduceImport.class).importInsert(billVO, null);
        
        return reduceVO;
	}
	
	/**
     * <p>
     * 校验导入数据。
     * 
     * @param billVO
     * @throws BusinessException
     */
    private void checkBill(ReduceVO billVO) throws BusinessException {
        
        // 校验数值合法性。
        checkNumDataLegal(billVO);
        
        // 校验日期合法性。
        checkDateLegal(billVO);
        
        // 校验减少方式合法性。
        checkReduceStyel(billVO);
    }
    
    /**
     * <p>
     * 校验数值合法性。
     * 
     * @param billVO
     * @throws BusinessException
     */
    private void checkNumDataLegal(ReduceVO billVO) throws BusinessException {
        
        ReduceBodyVO[] bodyVOs = (ReduceBodyVO[]) billVO.getChildrenVO();
        
        if (ArrayUtils.isNotEmpty(bodyVOs)) {
            for (ReduceBodyVO bodyVO : bodyVOs) {
                // 校验“清理收入”是否非负。
                UFDouble purgefee = bodyVO.getPurgefee();
                if (isSmallerThanZero(purgefee)) {
                    /*@res "请确认导入的清理费用值大于等于0。 \n"*/
                    throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                            "farule_0","02012060-0070"));
                }
                
                // 校验“清理费用”是否非负。
                UFDouble purgerevenue = bodyVO.getPurgerevenue();
                if (isSmallerThanZero(purgerevenue)) {
                    /*@res "请确认导入的清理收入值大于等于0。 \n"*/
                    throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                            "farule_0","02012060-0071"));
                }
            }
        }
    }
    
    /**
     * <p>
     * 判断指定的数值是否小于0。
     * 
     * @param num
     * @return
     */
    private boolean isSmallerThanZero(UFDouble num) {
        
        boolean result = false;
        
        if (num != null 
                && num.compareTo(UFDouble.ZERO_DBL) < 0) {
            result = true;
        }
        
        return result;
    }
    
    /**
     * <p>
     * 校验日期合法性。
     * 
     * @param billVO
     * @throws BusinessException
     */
    private void checkDateLegal(ReduceVO billVO) throws BusinessException {
        
        ReduceHeadVO headVO = (ReduceHeadVO) billVO.getParentVO();
        
        if (headVO != null) {
            // 校验“制单时间”是否早于“审批时间”
            UFDateTime audittime = headVO.getAudittime();
            if (audittime != null) {
                UFDateTime billMakeTime = headVO.getBillmaketime();
                if (billMakeTime != null 
                        && billMakeTime.compareTo(audittime) > 0) {
                    /*@res "请确认导入数据的制单时间必须早于审批时间。 \n"*/
                    throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                            "farule_0","02012060-0072"));
                }
            }
        }
    }
    
    /**
     * <p>
     * 校验减少方式合法性。
     * 
     * @param billVO
     * @throws BusinessException
     */
    private void checkReduceStyel(ReduceVO billVO) throws BusinessException {
        
        ReduceBodyVO[] bodyVOs = (ReduceBodyVO[]) billVO.getChildrenVO();
        
        if (ArrayUtils.isNotEmpty(bodyVOs)) {
            for (ReduceBodyVO bodyVO : bodyVOs) {
                String reduceStyle = bodyVO.getPk_reducestyle();
                if (StringUtils.isNotEmpty(reduceStyle)) {
                    IGeneralAccessor general = GeneralAccessorFactory.getAccessor(ClassIDConst.ADDREDUCESTYLE_ID);
                    List<IBDData> children = general.getChildDocs(bodyVO.getPk_org(), reduceStyle, false);
                    // 是否非末级
                    if (children != null && children.size() > 0) {
                        /*@res "请确认导入数据的减少方式为末级档案。 \n"*/
                        throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                                "farule_0","02012060-0074"));
                    }
                }
            }
        }
    }
    
    /**
     * 设置减少单的基本值
     * 
     */
    private void setBillVOValue(ReduceVO billVO, AssetVO[] cardVOs) {
    
        Map<String, AssetVO> cardsMap = new HashMap<String, AssetVO>();
        for (AssetVO cardvo : cardVOs) {
            cardsMap.put(cardvo.getPk_card(), cardvo);
        }
        
        ReduceBodyVO[] bodyVOs = (ReduceBodyVO[]) billVO.getChildrenVO();
        for (ReduceBodyVO bodyVO : bodyVOs) {
            AssetVO cardVO = cardsMap.get(bodyVO.getPk_card());
            bodyVO.setRed_localoriginvalue(cardVO.getLocaloriginvalue());
            bodyVO.setRed_accudep(cardVO.getAccudep());
            bodyVO.setRed_predevaluate(cardVO.getPredevaluate());
            // 累计折旧(包括模拟折旧)
            bodyVO.setAccudep(simAccudepMap.get(bodyVO.getPk_card()));
            // 模拟折旧
            bodyVO.setSimulatedep(simDepamountMap.get(bodyVO.getPk_card()));
            // 币种
            bodyVO.setPk_currency(cardVO.getPk_currency());
        }
        
    }
    /**
     * 
     * 查询卡片
     * <p>
     * <b>examples:</b>
     * <p>
     * 使用示例
     * <p>
     * <b>参数说明</b>
     * 
     * @param billVO
     * @param showKeys
     * @return
     * @throws BusinessException
     *             <p>
     */
    private AssetVO[] queryCardVOs(ReduceVO billVO) throws BusinessException {
    
        String[] pk_cards = VOManager.getAttributeValueArray(billVO.getChildrenVO(), AssetFieldConst.PK_CARD);
        
        ReduceHeadVO headVO = (ReduceHeadVO) billVO.getParentVO();
        String whereSQL =
                " fa_card.pk_card in (" + StringTools.buildStringUseSpliterWithQuotes(pk_cards, ",") + ") "
                        + " and laststate_flag = 'Y' ";
        if (StringUtils.isNotEmpty(headVO.getPk_accbook())) {
            whereSQL += " and pk_accbook = '" + headVO.getPk_accbook() + "'";
        } else {
            whereSQL += " and business_flag = '" + UFBoolean.TRUE + "'";
        }
        
        // 添加查询字段
        List<String> keysList = new ArrayList<String>();
        keysList.add(AssetFieldConst.PK_CARD);
        keysList.add(AssetFieldConst.LOCALORIGINVALUE);
        keysList.add(AssetFieldConst.PREDEVALUATE);
        keysList.add(AssetFieldConst.ACCUDEP);
        keysList.add(AssetFieldConst.PK_CURRENCY);
        
        AssetVO[] assetVOs =
                AMProxy.lookup(IAssetService.class).queryAssetFieldValues(whereSQL, keysList.toArray(new String[0]));
        
        if (assetVOs == null || assetVOs.length == 0) {
            ExceptionUtils.asBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("farule_0",
                    "02012060-0041")/* @res " 没有查询到任何一张卡片, 请检查 \n " */);
        }
        
        return assetVOs;
    }
    
    /**
     * 
     * 得到模拟折旧,累计折旧
     */
    private void querySimulateDep(ReduceVO billVO) throws BusinessException {
    
        ReduceHeadVO headVO = (ReduceHeadVO) billVO.getParentVO();
        ReduceBodyVO[] bodyVOs = (ReduceBodyVO[]) billVO.getChildrenVO();
        
        // Map<pk_card,模拟折旧值>
        simDepamountMap = new HashMap<String, UFDouble>();
        simAccudepMap = new HashMap<String, UFDouble>();
        
        String pk_org = headVO.getPk_org();
        String pk_group = headVO.getPk_group();
        String[] pk_cards = VOManager.getAttributeValueArray(bodyVOs, AssetFieldConst.PK_CARD);
        
        String pk_accbook = headVO.getPk_accbook();
        if (StringUtils.isEmpty(pk_accbook)) {
            Map<String, String> orgMainAccbook =
                    AMProxy.lookup(IAccountingBookPubService.class)
                            .queryAccountingBookIDByFinanceOrgIDWithMainAccountBook(new String[] { pk_org });
            pk_accbook = orgMainAccbook.get(pk_org);
        }
        
        ReduceSimulateManager depFactory = new ReduceSimulateManager();
        depFactory.procSimulateDep(pk_cards, pk_group, pk_org, pk_accbook,headVO.getBusiness_date(),UFBoolean.FALSE); 
        
        simDepamountMap.putAll(depFactory.getSimulateDepamoutMap());
        simAccudepMap.putAll(depFactory.getSimulateAccudepMap());
        
    }

}
