package nc.bs.mmpac.wr.rule.setcheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.mmpac.wr.adapter.WrPickmServiceAdapter;
import nc.bs.mmpac.wr.rule.setcheck.bo.WrScAlgorithmBO;
import nc.bs.mmpac.wr.rule.setcheck.bo.WrScForceCollectionDataBO;
import nc.bs.mmpac.wr.util.rewrite.prodin.WrProdInCommonCacheUtil;
import nc.bsutil.mmpac.pacpub.PACParameterManager;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.ui.pcm.view.AskDialog;
import nc.util.mmf.framework.base.MMMapUtil;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.bd.bom.bom0202.enumeration.OutputTypeEnum;
import nc.vo.bd.material.IMaterialEnumConst;
import nc.vo.mmpac.pickm.entity.AggPickmVO;
import nc.vo.mmpac.pickm.param.WrCheckParam;
import nc.vo.mmpac.wr.consts.WrptLangConst;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.mmpac.wr.entity.WrQualityVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class WrScForceRule implements IRule<AggWrVO> {
    // ��дVO��pk
    private Set<String> rewritePks = new HashSet<String>();

    // ��дVO��λ��bodyΪ���壬granΪ�������
    private String rewritePosition = null;

    public WrScForceRule(Set<String> rewritePks, String position) {
        this.rewritePks = rewritePks;
        this.rewritePosition = position;
    }

    @Override
    public void process(AggWrVO[] aggWrVOs) {
        if (null == aggWrVOs || aggWrVOs.length <= 0) {
            return;
        }
        // ��ȡ����ģʽ��������Ϊֻ��һ�ֱ���ƴдsql
        Integer prodMode = aggWrVOs[0].getParentVO().getFprodmode();
        // ��û�д����Ӧ���ӱ����������
        WrItemVO[] vos = this.findItemVOs(aggWrVOs);
        if (MMValueCheck.isEmpty(vos)) {
            return;
        }
        // ���˳����Խ����깤���׼��ı�����
        List<WrItemVO> results = new ArrayList<WrItemVO>();
        WrProdInCommonCacheUtil util = new WrProdInCommonCacheUtil();
        //��̨�����깤���
//        for (WrItemVO itemVO : vos) {
//            // ���˵��޶��������л�������Ʒ��
//            if (MMValueCheck.isEmpty(itemVO.getCbmoid())
//                    || !OutputTypeEnum.MAIN_PRODUCT.equalsValue(itemVO.getFbproducttype())) {
//                continue;
//            }
//            // �깤���ױ�ʶΪnull��falseʱ������Ҫ�����깤���׼��
//            if (UFBoolean.TRUE.equals(itemVO.getBbsetmark())) {
//                continue;
//            }
//            
//            // �깤���׼�鹤���������ж�
//            PACParameterManager orgPara = util.getOrgPara(itemVO.getPk_org());
//            // �깤���׵Ĺ��������Ƿ�����
//            if (!orgPara.isWorkDoneSetCheck()) {
//                // û�����������κβ���
//                continue;
//            }
//            results.add(itemVO);
//        }
        if (MMValueCheck.isEmpty(results)) {
            return;
        }
        WrScForceCollectionDataBO collectionDataBO = new WrScForceCollectionDataBO();
        WrCheckParam[] setParams = collectionDataBO.process(aggWrVOs, vos);
        //
        // ���ñ��ϼƻ��ӿڿ��Ƿ�ͨ��
        // ��ѯ���ϼƻ�����
        Map<String, AggPickmVO> moPickmMap = null;
        moPickmMap = WrPickmServiceAdapter.queryPickmMapByMOIds(setParams);
        if (null == moPickmMap) {
            return;
        }
        Map<String, UFDouble> moNumMap = new HashMap<String, UFDouble>();
        for (WrCheckParam param : setParams) {
            if (MMValueCheck.isEmpty(param.getMorowid())) {
                moNumMap.put(param.getMoid(), param.getNnum());
            }
            else {
                moNumMap.put(param.getMorowid(), param.getNnum());
            }
        }
        // �п�
        if (MMMapUtil.isEmpty(moNumMap)) {
            return;
        }
        WrScAlgorithmBO scAlgrithmBO = new WrScAlgorithmBO();
        Map<String, UFBoolean> resultMap =
                scAlgrithmBO.checkPickmKitItem(moNumMap, moPickmMap.values().toArray(new AggPickmVO[0]), null);

        for (WrItemVO itemVO : results) {
            if (Integer.valueOf(IMaterialEnumConst.PRODMODE_SEPPROD).equals(prodMode)) {
                if (!MMValueCheck.isEmpty(itemVO.getCbmoid())) {
                    if (UFBoolean.FALSE.equals(resultMap.get(itemVO.getCbmoid()))) {
                    	ExceptionUtils.wrappBusinessException(WrptLangConst.SETCHECK_WRMSG());
                    }
                }
            }
            else {
                if (!MMValueCheck.isEmpty(itemVO.getCbmobid())) {
                    if (UFBoolean.FALSE.equals(resultMap.get(itemVO.getCbmobid()))) {
                        ExceptionUtils.wrappBusinessException(WrptLangConst.SETCHECK_WRMSG());
                    }
                }
            }
        }

    }

    /**
     * �ӽ�����л����Ҫ��д���ӱ�VO
     * 
     * @param aggWrVOs
     * @param paraCache
     * @return
     */
    private WrItemVO[] findItemVOs(AggWrVO[] aggWrVOs) {
        if (MMValueCheck.isEmpty(aggWrVOs)) {
            return new WrItemVO[] {};
        }
        Set<WrItemVO> results = new HashSet<WrItemVO>();
        for (AggWrVO aggWrVO : aggWrVOs) {
            WrItemVO[] itemVOs = (WrItemVO[]) aggWrVO.getChildren(WrItemVO.class);
            if (MMValueCheck.isEmpty(itemVOs)) {
                continue;
            }
            for (WrItemVO itemVO : itemVOs) {
                if ("body".equals(this.rewritePosition)) {
                    if (this.rewritePks.contains(itemVO.getPk_wr_product()) && null != itemVO.getPk_wr_product()) {
                        results.add(itemVO);
                    }
                }

                if ("grand".equals(this.rewritePosition) && null != itemVO.getQualityvos()) {
                    for (WrQualityVO qualityVO : itemVO.getQualityvos()) {
                        if (this.rewritePks.contains(qualityVO.getPk_wr_quality())) {
                            results.add(itemVO);
                            break;
                        }
                    }
                }

            }

        }
        return results.toArray(new WrItemVO[0]);
    }
}
