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
 * for BPM �ʲ����ٵ��ӿڴ�����
 *
 */
public class AddReducePlugin extends AbstractPfxxPlugin {
	// Map<pk_card, ģ���۾�ֵ>
    private Map<String, UFDouble> simDepamountMap = new HashMap<String, UFDouble>();
    // Map<pk_card, �ۼ��۾�>
    private Map<String, UFDouble> simAccudepMap = new HashMap<String, UFDouble>();
    
	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		ReduceVO billVO = (ReduceVO) vo;
        
        // �����µĵ��ݺ�
        // ���õ��ݺŽӿڵ��ݺ�
        // ��ȡ���ݺŹ������
        IBillcodeManage billCodeManager = AMProxy.lookup(IBillcodeManage.class);
        // ȡ���µĵ��ݺ�
        String newBill_code =
                billCodeManager.getPreBillCode_RequiresNew(BillTypeConst.REDUCE,
                        ((ReduceHeadVO) billVO.getParent()).getPk_group(),
                        ((ReduceHeadVO) billVO.getParent()).getPk_org());
        ((ReduceHeadVO) billVO.getParent()).setBill_code(newBill_code);
        
        ReduceHeadVO headVO = (ReduceHeadVO) billVO.getParentVO();
        // ���ݱ�����߸���֮ǰ���õĲ�ѯ��������ѯ��ǰ������ǰ�Ƿ񱻵����
        // ����Ѿ������������ô�����Ѿ����뵥�ݵ�PK�����򷵻�NULLֵ
        String billPK =
                PfxxPluginUtils.queryBillPKBeforeSaveOrUpdate(swapContext.getBilltype(), swapContext.getDocID(),
                        swapContext.getOrgPk());
        
        // ��������
        if (null != billPK && billPK.length() > 0) {
            // �鿴�����ļ���Ϣ�Ƿ��������ظ�����
            if (swapContext.getReplace().equalsIgnoreCase("N")) {
                throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("farule_0",
                        "02012060-0037")/*
                                         * @res
                                         * "�������ظ����뵥�ݣ����������ѵ��뵥�ݣ���������ļ���replace��־��Ϊ��Y����"
                                         */);
            }
            // ��������
            headVO.setPrimaryKey(billPK);
            // ��ѯ���ݿ����Ƿ���ڸõ���
            ReduceVO reduceVO = ArrayUtils.getFirstElem(AMProxy.lookup(IReduceImport.class).queryReduceVO(billPK));
            if (null != reduceVO) {
                throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("farule_0",
                        "02012060-0038")/* @res "�õ����Ѿ����룬�����Ѿ���ˣ��������ظ����롣" */);
            }
        }
        
        // 2. ����У��
        checkBill(billVO);
        
        // 3. ģ���۾�
        querySimulateDep(billVO);
        
        // 3. ��ѯ��Ƭ
        AssetVO[] cardVOs = queryCardVOs(billVO);
        
        // 4. ���ü��ٵ�
        setBillVOValue(billVO, cardVOs);
        
        // 5. ���ɼ��ٵ�
        ReduceVO reduceVO = AMProxy.lookup(IReduceImport.class).importInsert(billVO, null);
        
        return reduceVO;
	}
	
	/**
     * <p>
     * У�鵼�����ݡ�
     * 
     * @param billVO
     * @throws BusinessException
     */
    private void checkBill(ReduceVO billVO) throws BusinessException {
        
        // У����ֵ�Ϸ��ԡ�
        checkNumDataLegal(billVO);
        
        // У�����ںϷ��ԡ�
        checkDateLegal(billVO);
        
        // У����ٷ�ʽ�Ϸ��ԡ�
        checkReduceStyel(billVO);
    }
    
    /**
     * <p>
     * У����ֵ�Ϸ��ԡ�
     * 
     * @param billVO
     * @throws BusinessException
     */
    private void checkNumDataLegal(ReduceVO billVO) throws BusinessException {
        
        ReduceBodyVO[] bodyVOs = (ReduceBodyVO[]) billVO.getChildrenVO();
        
        if (ArrayUtils.isNotEmpty(bodyVOs)) {
            for (ReduceBodyVO bodyVO : bodyVOs) {
                // У�顰�������롱�Ƿ�Ǹ���
                UFDouble purgefee = bodyVO.getPurgefee();
                if (isSmallerThanZero(purgefee)) {
                    /*@res "��ȷ�ϵ�����������ֵ���ڵ���0�� \n"*/
                    throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                            "farule_0","02012060-0070"));
                }
                
                // У�顰������á��Ƿ�Ǹ���
                UFDouble purgerevenue = bodyVO.getPurgerevenue();
                if (isSmallerThanZero(purgerevenue)) {
                    /*@res "��ȷ�ϵ������������ֵ���ڵ���0�� \n"*/
                    throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                            "farule_0","02012060-0071"));
                }
            }
        }
    }
    
    /**
     * <p>
     * �ж�ָ������ֵ�Ƿ�С��0��
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
     * У�����ںϷ��ԡ�
     * 
     * @param billVO
     * @throws BusinessException
     */
    private void checkDateLegal(ReduceVO billVO) throws BusinessException {
        
        ReduceHeadVO headVO = (ReduceHeadVO) billVO.getParentVO();
        
        if (headVO != null) {
            // У�顰�Ƶ�ʱ�䡱�Ƿ����ڡ�����ʱ�䡱
            UFDateTime audittime = headVO.getAudittime();
            if (audittime != null) {
                UFDateTime billMakeTime = headVO.getBillmaketime();
                if (billMakeTime != null 
                        && billMakeTime.compareTo(audittime) > 0) {
                    /*@res "��ȷ�ϵ������ݵ��Ƶ�ʱ�������������ʱ�䡣 \n"*/
                    throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                            "farule_0","02012060-0072"));
                }
            }
        }
    }
    
    /**
     * <p>
     * У����ٷ�ʽ�Ϸ��ԡ�
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
                    // �Ƿ��ĩ��
                    if (children != null && children.size() > 0) {
                        /*@res "��ȷ�ϵ������ݵļ��ٷ�ʽΪĩ�������� \n"*/
                        throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                                "farule_0","02012060-0074"));
                    }
                }
            }
        }
    }
    
    /**
     * ���ü��ٵ��Ļ���ֵ
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
            // �ۼ��۾�(����ģ���۾�)
            bodyVO.setAccudep(simAccudepMap.get(bodyVO.getPk_card()));
            // ģ���۾�
            bodyVO.setSimulatedep(simDepamountMap.get(bodyVO.getPk_card()));
            // ����
            bodyVO.setPk_currency(cardVO.getPk_currency());
        }
        
    }
    /**
     * 
     * ��ѯ��Ƭ
     * <p>
     * <b>examples:</b>
     * <p>
     * ʹ��ʾ��
     * <p>
     * <b>����˵��</b>
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
        
        // ��Ӳ�ѯ�ֶ�
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
                    "02012060-0041")/* @res " û�в�ѯ���κ�һ�ſ�Ƭ, ���� \n " */);
        }
        
        return assetVOs;
    }
    
    /**
     * 
     * �õ�ģ���۾�,�ۼ��۾�
     */
    private void querySimulateDep(ReduceVO billVO) throws BusinessException {
    
        ReduceHeadVO headVO = (ReduceHeadVO) billVO.getParentVO();
        ReduceBodyVO[] bodyVOs = (ReduceBodyVO[]) billVO.getChildrenVO();
        
        // Map<pk_card,ģ���۾�ֵ>
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
