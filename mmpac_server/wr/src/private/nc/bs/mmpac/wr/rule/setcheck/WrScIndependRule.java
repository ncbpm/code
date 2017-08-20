package nc.bs.mmpac.wr.rule.setcheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.mmpac.pickm.adapter.MaterialServiceAdapter;
import nc.bs.mmpac.wr.adapter.WrPickmServiceAdapter;
import nc.bs.mmpac.wr.rule.setcheck.bo.WrScAlgorithmBO;
import nc.bs.mmpac.wr.rule.setcheck.bo.WrScIndependCoDataBO;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.util.mmf.framework.base.MMNumberUtil;
import nc.util.mmf.framework.base.MMVOUtil;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.util.mmpac.wr.WrVOCommonUtil;
import nc.vo.bd.material.IMaterialEnumConst;
import nc.vo.mmpac.pickm.entity.AggPickmVO;
import nc.vo.mmpac.pickm.entity.PickmItemVO;
import nc.vo.mmpac.pickm.param.WrCheckParam;
import nc.vo.mmpac.wr.consts.WrptLangConst;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.mmpac.wr.entity.WrQualityVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MapList;
import nc.vo.util.BDPKLockUtil;
import nc.vo.util.BDVersionValidationUtil;

/**
 * ����������׼���㷨����
 * 
 * @since 6.0
 * @version 2013-7-10 ����11:47:23
 * @author liweiz
 */
public class WrScIndependRule implements IRule<AggWrVO> {

    @Override
    public void process(AggWrVO[] vos) {
        if (MMValueCheck.isEmpty(vos)) {
            ExceptionUtils.wrappBusinessException(WrptLangConst.getSetCheck_Msg());
        }
        Integer prodMode = vos[0].getParentVO().getFprodmode();
        // ���ѡ�е�������������VO����
        WrItemVO[] selectedItems = this.getPresentItemVOs(vos);
        if (MMValueCheck.isEmpty(selectedItems)) {
            return;
        }
        // �����깤�����Ե�ǰѡ��ı����н�������
        this.sortItemVOByWrNum(selectedItems);
        // ���������ӱ�ת��Ϊmap�ṹ
        Map<String, WrItemVO> selecteddItemVOMap = WrVOCommonUtil.groupSuperVOPk(selectedItems, WrItemVO.PK_WR_PRODUCT);
        // ���ն���ID����
        MapList<String, WrItemVO> seldItemVOByMoid = null;
        if (Integer.valueOf(IMaterialEnumConst.PRODMODE_PROD).equals(prodMode)) {
            seldItemVOByMoid = WrVOCommonUtil.groupSuperVOByField(selectedItems, WrItemVO.CBMOBID);
        }
        else {
            seldItemVOByMoid = WrVOCommonUtil.groupSuperVOByField(selectedItems, WrItemVO.CBMOID);
        }
        // ��ѯ�����е��������棬���ն���id����
        WrScIndependCoDataBO scBO = new WrScIndependCoDataBO();
        MapList<String, WrItemVO> itemVOByMoidMap = scBO.process(vos);
        // ���ն���ID�Դ����������л��ܼ�¼ѡ����֮��Ĵ�������֮��
        Map<String, UFDouble> extraNumMap = new HashMap<String, UFDouble>();
        this.getNotSeledNumByMoid(extraNumMap, itemVOByMoidMap, selecteddItemVOMap);
        // ��ȡ�����ϼƻ�
        WrCheckParam[] setParams = this.prepareParams4Pickm(vos);
        if (MMValueCheck.isEmpty(setParams)) {
            return;
        }
        Map<String, AggPickmVO> moPickmMap = null;
        moPickmMap = WrPickmServiceAdapter.queryPickmMapByMOIds(setParams);
        if (null == moPickmMap) {
            return;
        }
        // ��¼���Ľ�������ڸ������ݿ�
        List<WrItemVO> checkResultItems = new ArrayList<WrItemVO>();
        // ȡ���δ��������
        for (Map.Entry<String, List<WrItemVO>> entry : seldItemVOByMoid.entrySet()) {
            // ����IDά����ͨ������Ŀ
            List<WrItemVO> throuByMOIDs = new ArrayList<WrItemVO>();
            // ����IDά�����깤����С�Ĳ�ͨ���ı�ʶ
            boolean isFailFlag = false;
            // �Ƿ��ǵ�һ��ѭ��
            boolean isFirstFlag = true;
            int i = 0;
            for (WrItemVO itemVO : entry.getValue()) {
                if (i == 0) {
                    isFirstFlag = true;
                }
                else {
                    isFirstFlag = false;
                }
                if (isFailFlag) {
                    itemVO.setBbsetmark(UFBoolean.FALSE);
                    checkResultItems.add(itemVO);
                    continue;
                }
                Map<String, UFDouble> numMapByMoid = new HashMap<String, UFDouble>();
                // ����ȡ��ID����ɢȡֵͷid
                String moid = this.getMOidByProdMode(itemVO, prodMode);
                UFDouble ufdNum = itemVO.getNbwrnum();
                if (extraNumMap.keySet().contains(moid) && !MMValueCheck.isEmpty(extraNumMap.get(moid))) {
                    ufdNum = MMNumberUtil.add(extraNumMap.get(moid), ufdNum);
                }
                // ���ǵ�һ����Ҫȡ�ϼ��μ��ͨ�����깤����
                if (!isFirstFlag) {
                    if (!MMValueCheck.isEmpty(throuByMOIDs)) {
                        for (WrItemVO throughItem : throuByMOIDs) {
                            ufdNum = MMNumberUtil.add(ufdNum, throughItem.getNbwrnum());
                        }
                    }

                }
                numMapByMoid.put(moid, ufdNum);
                // ��Ҫ��鱸�ϼƻ����������Map��key����Դ�����������У�ID��value����Ӧ�ı��ϼƻ����깤���׼���ʶΪ�ǵ����Ա���
                WrScAlgorithmBO checkBO = new WrScAlgorithmBO();
                Map<String, UFBoolean> resultMap =
                        checkBO.checkPickmKitItem(numMapByMoid, moPickmMap.values().toArray(new AggPickmVO[0]),
                                this.getAllowedLimit(moPickmMap.values().toArray(new AggPickmVO[0])));
                if (!MMValueCheck.isEmpty(moid)) {
                    if (UFBoolean.TRUE.equals(resultMap.get(moid))) {
                        // ��ͨ���󽫱�ʶ����Ϊtrue
                        itemVO.setBbsetmark(UFBoolean.TRUE);
                        throuByMOIDs.add(itemVO);
                        checkResultItems.add(itemVO);
                    }
                    else {
                        itemVO.setBbsetmark(UFBoolean.FALSE);
                        isFailFlag = true;
                        //checkResultItems.add(itemVO);��鲻ͨ���ģ�������
                        continue;
                    }
                }
                i++;
            }
        }
        // �������׼��ͨ����ʶ�����ݿ���
        if (!MMValueCheck.isEmpty(checkResultItems)) {
            try {
                BDPKLockUtil.lockSuperVO(checkResultItems.toArray(new SuperVO[0]));
                BDVersionValidationUtil.validateSuperVO(checkResultItems.toArray(new SuperVO[0]));
            }
            catch (BusinessException e) {
                ExceptionUtils.wrappException(e);
            }
            VOUpdate<WrItemVO> voUpdate = new VOUpdate<WrItemVO>();
            WrItemVO[] upResults = voUpdate.update(checkResultItems.toArray(new WrItemVO[0]), new String[] {
                WrItemVO.BBSETMARK
            });
            this.rewriteItem(upResults, checkResultItems);
        }

    }

    /**
 * 
 */
    private Map<String, UFDouble> getAllowedLimit(AggPickmVO[] vos) {
        Map<String, UFDouble> nlowerLimitMapTemp = new HashMap<String, UFDouble>();
        if (MMValueCheck.isEmpty(vos)) {
            return nlowerLimitMapTemp;
        }
        // ����Vid����
        List<String> materilIds = new ArrayList<String>();
        for (AggPickmVO vo : vos) {
            if (MMValueCheck.isEmpty(vo.getChildrenVO())) {
                continue;
            }
            for (PickmItemVO item : (PickmItemVO[]) vo.getChildrenVO()) {
                // ����깤���׼���ʶΪ��
                if (UFBoolean.TRUE.equals(item.getBchkitemforwr())) {
                    // �����������У�ȡ�ñ���Ͳ���vid
                    if (MMValueCheck.isEmpty(item.getCreplacesrcid())) {
                        materilIds.add(item.getCbmaterialvid());
                    }
                }
            }
        }
        if (MMValueCheck.isEmpty(materilIds)) {
            return nlowerLimitMapTemp;
        }
        return MaterialServiceAdapter.getOutCloseLowerLismitPercentMap(materilIds.toArray(new String[0]));
    }

    private void rewriteItem(WrItemVO[] upResults, List<WrItemVO> checkResultItems) {
        if (MMValueCheck.isEmpty(upResults) || MMValueCheck.isEmpty(checkResultItems)) {
            return;
        }
        Map<String, WrItemVO> itemVOMap = MMVOUtil.groupToMap(upResults, WrItemVO.PK_WR_PRODUCT);
        for (WrItemVO itemVO : checkResultItems) {
            if (itemVOMap.keySet().contains(itemVO.getPk_wr_product())
                    && MMValueCheck.isNotEmpty(itemVOMap.get(itemVO.getPk_wr_product()))) {
                WrItemVO tempVO = itemVOMap.get(itemVO.getPk_wr_product());
                itemVO.setTs(tempVO.getTs());
            }
        }

    }

    /**
     * ��������ģʽȥ��������id
     * ����ȡ��ID����ɢȡֵͷid
     * 
     * @param itemVO
     * @param prodMode
     * @return
     */
    private String getMOidByProdMode(WrItemVO itemVO, Integer prodMode) {
        if (Integer.valueOf(IMaterialEnumConst.PRODMODE_SEPPROD).equals(prodMode)) {
            return itemVO.getCbmoid();
        }
        return itemVO.getCbmobid();
    }

    private WrCheckParam[] prepareParams4Pickm(AggWrVO[] vos) {
        List<WrCheckParam> results = new ArrayList<WrCheckParam>();
        WrCheckParam checkParam = null;
        for (AggWrVO aggWrVO : vos) {
            Integer proMode = aggWrVO.getParentVO().getFprodmode();
            WrItemVO[] itemVOs = (WrItemVO[]) aggWrVO.getChildren(WrItemVO.class);
            if (MMValueCheck.isEmpty(itemVOs)) {
                continue;
            }
            for (WrItemVO itemVO : itemVOs) {
                checkParam = new WrCheckParam();
                if (null != proMode && IMaterialEnumConst.PRODMODE_PROD == proMode.intValue()) {
                    if (!MMValueCheck.isEmpty(itemVO.getCbmobid())) {
                        checkParam.setMoid(itemVO.getCbmoid());
                        checkParam.setMorowid(itemVO.getCbmobid());
                        results.add(checkParam);
                    }
                }
                if (null != proMode && IMaterialEnumConst.PRODMODE_SEPPROD == proMode.intValue()) {
                    if (!MMValueCheck.isEmpty(itemVO.getCbmoid())) {
                        checkParam.setMoid(itemVO.getCbmoid());
                        results.add(checkParam);
                    }

                }
            }
        }
        return results.toArray(new WrCheckParam[0]);
    }

    /**
     * ��ȡѡ����֮�����������Ĵ�������
     * 
     * @param moidNum
     * @param itemVOByMoidMap
     * @param selecteddItemVOMap
     */
    private void getNotSeledNumByMoid(Map<String, UFDouble> moidNum, MapList<String, WrItemVO> itemVOByMoidMap,
            Map<String, WrItemVO> selecteddItemVOMap) {
        if (MMValueCheck.isEmpty(itemVOByMoidMap) || MMValueCheck.isEmpty(selecteddItemVOMap)) {
            return;
        }
        for (Map.Entry<String, List<WrItemVO>> entry : itemVOByMoidMap.entrySet()) {
            UFDouble ufd = UFDouble.ZERO_DBL;
            for (WrItemVO itemVO : entry.getValue()) {
                // ѡ�е�����������֮���ͳ��
                if (!selecteddItemVOMap.keySet().contains(itemVO.getPk_wr_product())) {
                    // ���׼��ͨ����ȡ�깤����
                    if (UFBoolean.TRUE.equals(itemVO.getBbsetmark())) {
                        ufd = MMNumberUtil.add(ufd, itemVO.getNbwrnum());
                    }
                    else {
                        ufd = MMNumberUtil.add(ufd, itemVO.getNbsldinnum(), itemVO.getNbaldempinnum());
                        if (MMValueCheck.isEmpty(itemVO.getQualityvos())) {
                            continue;
                        }
                        for (WrQualityVO qualityVO : itemVO.getQualityvos()) {
                            ufd =
                                    MMNumberUtil.add(ufd, qualityVO.getNginnum(), qualityVO.getNgsldinnum(),
                                            qualityVO.getNgaldrmknum());
                        }
                    }
                }
            }
            moidNum.put(entry.getKey(), ufd);
        }
    }

    /**
     * �����깤������������
     * 
     * @param presentItemVOs
     */
    private void sortItemVOByWrNum(WrItemVO[] presentItemVOs) {
        Arrays.sort(presentItemVOs, new Comparator<SuperVO>() {
            @Override
            public int compare(SuperVO o1, SuperVO o2) {
                int flag = 0;
                UFDouble ufd1 = (UFDouble) o1.getAttributeValue(WrItemVO.NBWRNUM);
                UFDouble ufd2 = (UFDouble) o2.getAttributeValue(WrItemVO.NBWRNUM);
                if (null == ufd1 && null == ufd2) {
                    flag = 0;
                }
                else if (null == ufd1) {
                    flag = 1;
                }
                else if (null == ufd2) {
                    flag = -1;
                }
                else {
                    flag = ufd1.compareTo(ufd2);
                }
                if (flag != 0) {
                    return flag;
                }
                return flag;
            }
        });
    }

    /**
     * ��ȡ����������ǰѡ��VO
     * 
     * @param vos
     * @return
     */
    private WrItemVO[] getPresentItemVOs(AggWrVO[] vos) {
        // ���ѡ�е�������������VO����
        List<WrItemVO> resultLists = new ArrayList<WrItemVO>();
        for (AggWrVO aggVO : vos) {
            Integer prodMode = aggVO.getParentVO().getFprodmode();
            WrItemVO[] itemVOs = (WrItemVO[]) aggVO.getChildren(WrItemVO.class);
            if (MMValueCheck.isEmpty(itemVOs)) {
                continue;
            }
            for (WrItemVO itemVO : itemVOs) {
                if (Integer.valueOf(IMaterialEnumConst.PRODMODE_PROD).equals(prodMode)) {
                    if (!MMValueCheck.isEmpty(itemVO.getCbmobid())) {
                        resultLists.add(itemVO);
                    }
                }
                else {
                    if (!MMValueCheck.isEmpty(itemVO.getCbmoid())) {
                        resultLists.add(itemVO);
                    }
                }
            }
        }
        return resultLists.toArray(new WrItemVO[0]);
    }
}
