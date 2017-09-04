package nc.bs.cm.fetchdata.fetchcheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bd.framework.base.CMArrayUtil;
import nc.bd.framework.base.CMCollectionUtil;
import nc.bd.framework.base.CMMapUtil;
import nc.bd.framework.base.CMStringUtil;
import nc.cmpub.business.adapter.BDAdapter;
import nc.cmpub.business.adapter.FIAdapter;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.prod.MaterialProdVO;
import nc.vo.cm.fetchdata.cmconst.FetchDataLangConst;
import nc.vo.cm.fetchdata.cmconst.FetchKeyConst;
import nc.vo.cm.fetchdata.entity.PullDataErroInfoVO;
import nc.vo.cm.fetchdata.entity.adapter.IFIFetchData;
import nc.vo.cm.fetchdata.entity.adapter.IFetchData;
import nc.vo.cm.fetchdata.enumeration.CMMesTypeEnum;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * �������ȡ��������
 */
public abstract class FICheckStrategy extends AbstractCheckStrategy {

    protected String acountBook;

    protected String[] costDomain;

    protected String pk_Org = null;

    protected String pk_Group = null;

    public FICheckStrategy(String pkOrg, String pkGroup) {
        this.pk_Org = pkOrg;
        this.pk_Group = pkGroup;
    }

    public FICheckStrategy(String pkOrg, String pkGroup, String[] costDomain, String acountBook) {
        this.pk_Org = pkOrg;
        this.pk_Group = pkGroup;
        this.acountBook = acountBook;
        this.costDomain = costDomain;
    }

    @Override
    public Map<IFetchData, PullDataErroInfoVO> setSubVosIllegalData(IFetchData[] vos, String cperiod) {
        // ��ɱ����³ɱ���Ϣҳǩ���õ����ϸ������Ժ��㲻һ��
        return this.checkMaterialCost(vos);
    }

    private Map<IFetchData, PullDataErroInfoVO> checkMaterialCost(IFetchData[] vos) {
        Set<String> materials =
                CheckMaterialCostInfo.getNotUniqueMaterials(vos, this.pk_Org, this.acountBook, this.costDomain);
        Map<IFetchData, PullDataErroInfoVO> voAndErroInfoVoMap = new HashMap<IFetchData, PullDataErroInfoVO>();
        if (CMCollectionUtil.isEmpty(materials)) {
            return voAndErroInfoVoMap;
        }

        Map<String, MaterialVO> materialVOMap =
                BDAdapter.queryMaterialBaseInfoByPks(materials.toArray(new String[materials.size()]), new String[] {
                    MaterialVO.CODE
                });

        // ���ô�����Ϣ
        for (IFetchData data : vos) {
            IFIFetchData vo = (IFIFetchData) data;
            StringBuilder codes = new StringBuilder();

            // ���ϳ��ⵥ
            //2107-09-01 �жϴ��ڣ�������쳣
//            if (CMStringUtil.isNotEmpty(data.getCproductid())) {
            if (CMStringUtil.isNotEmpty(data.getCproductid()) && materialVOMap.containsKey(vo.getCproductid())) {
             //2107-09-01 �жϴ��ڷ�����쳣
                codes.append(materialVOMap.get(vo.getCproductid()).getCode());
            }
            else {
                // ����Ʒ��ⵥ��ί��ӹ���ⵥ
                if (CMStringUtil.isNotEmpty(vo.getCmaterialid()) && materials.contains(vo.getCmaterialid())) {
                    // ��Ʒ
                    codes.append(materialVOMap.get(vo.getCmaterialid()).getCode());
                }
                if (CMStringUtil.isNotEmpty(vo.getPrimaryproductid()) && materials.contains(vo.getPrimaryproductid())) {
                    // ����Ʒ
                    if (CMStringUtil.isNotEmpty(codes.toString())) {
                        codes.append(",");
                    }
                    codes.append(materialVOMap.get(vo.getPrimaryproductid()).getCode());
                }
            }

            if (CMStringUtil.isNotEmpty(codes.toString())) {
                StringBuilder msg = new StringBuilder();
                msg.append("[" + codes + "]");
                msg.append(FetchDataLangConst.getERR_NOTUNIQUEMATERIALCOST());// ��ɱ����³ɱ���Ϣҳǩ���õ����ϸ������Ժ��㲻һ��
                PullDataErroInfoVO pullDataErroInfoVO = this.getErroInfoPosition(vo);
                pullDataErroInfoVO.setErroinfo(msg.toString());
                voAndErroInfoVoMap.put(vo, pullDataErroInfoVO);
            }

        }

        return voAndErroInfoVoMap;
    }

    /**
     * �������ԡ��ɱ�����ȷ�Ϸ�ʽ��Ϊ�������ĳɱ����Ĵ���
     *
     * @param correctData
     * @throws BusinessException
     */
    protected void setCostCenterByIA(IFetchData[] correctData, Map<String, String> wkidAndCostCenridMap)
            throws BusinessException {
        // ��ѯ�����еĸù����µĳɱ�����pk
        String[] costCenterpks = FIAdapter.getAllCostCenter();
        Set<String> sets = new HashSet<String>();
        for (String costcenterpk : costCenterpks) {
            sets.add(costcenterpk);
        }
        for (IFetchData fiData : correctData) {
            if (CMStringUtil.isNotEmpty(fiData.getCcostcenterid())) {
                if (!sets.contains(fiData.getCcostcenterid())) {// ���û��˵���ɱ�����Ϊ�Ѿ�ͣ�õ�
                    // �����ڹ������Ķ�Ӧ�ĳɱ����ģ���鲿�Ŷ�Ӧ�ĳɱ�����
                    if (!wkidAndCostCenridMap.containsKey(fiData.getWkid())) {
                        if (wkidAndCostCenridMap.containsKey(fiData.getCdeptid())) {
                            // ���������ö�Ӧ�ĳɱ�����
                            fiData.setCcostcenterid(wkidAndCostCenridMap.get(fiData.getCdeptid()));
                        }
                        else {// ����û�ҵ���Ч�ĳɱ���������Ϊ��
                            fiData.setCcostcenterid(null);
                        }
                    }
                    else {// ���������ö�Ӧ�ĳɱ�����
                        fiData.setCcostcenterid(wkidAndCostCenridMap.get(fiData.getWkid()));
                    }
                }
            }
            else {// �ɱ�����Ϊ��
                // �����ڹ������Ķ�Ӧ�ĳɱ����ģ���鲿�Ŷ�Ӧ�ĳɱ�����
                if (!wkidAndCostCenridMap.containsKey(fiData.getWkid())) {
                    if (wkidAndCostCenridMap.containsKey(fiData.getCdeptid())) {
                        // ���������ö�Ӧ�ĳɱ�����
                        fiData.setCcostcenterid(wkidAndCostCenridMap.get(fiData.getCdeptid()));
                    }
                    else {// ����û�ҵ���Ч�ĳɱ���������Ϊ��
                        fiData.setCcostcenterid(null);
                    }
                }
                else {// ���������ö�Ӧ�ĳɱ�����
                    fiData.setCcostcenterid(wkidAndCostCenridMap.get(fiData.getWkid()));
                }
            }
        }
    }

    @Override
    public Map<IFetchData, PullDataErroInfoVO> setVosCostCenterIllegalNullData(IFetchData[] vos,
            Map<String, String> wkidAndCostCenridMap) {
        if (CMArrayUtil.isEmpty(vos)) {
            return new HashMap<IFetchData, PullDataErroInfoVO>();
        }
        List<IFetchData> costCenterByIAList = new ArrayList<IFetchData>();
        for (IFetchData vo : vos) {
            costCenterByIAList.add(vo);
        }
        try {// �ɱ�����ȷ�Ϸ�ʽ������㴦��
            this.setCostCenterByIA(costCenterByIAList.toArray(new IFIFetchData[0]), wkidAndCostCenridMap);
        }
        catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return this.judgeCostCenterErrorNull(vos);
    }

    /**
     * �жϳɱ������Ƿ�Ϊ��
     */
    private StringBuffer getCostCenterErrorInfo(IFetchData vo) {
        StringBuffer erros = new StringBuffer();
        if (CMStringUtil.isEmpty(vo.getCcostcenterid())) { // δ�ҵ���Ч�ĳɱ�����
            erros.append(FetchDataLangConst.getERR_FICOSTCENTRERNULL());
        }
        return erros;
    }

    /**
     * �жϵ��ݵ���Ϣ�Ƿ�Ϊnull
     *
     * @param vos
     *            ȡ����������
     * @return Map<MMFetchDataVO, PullDataErroInfoVO> ԭ���ݺʹ�������vo��Map
     */
    public Map<IFetchData, PullDataErroInfoVO> judgeCostCenterErrorNull(IFetchData[] vos) {
        Map<IFetchData, PullDataErroInfoVO> voAndErroInfoVoMap = new HashMap<IFetchData, PullDataErroInfoVO>();
        for (IFetchData fetchDataVO : vos) {
            // ������Ϣ
            StringBuffer erros = this.getCostCenterErrorInfo(fetchDataVO);
            if (erros == null || erros.length() == 0) {
                continue;
            }
            // ����λ��
            PullDataErroInfoVO pullDataErroInfoVO = this.getErroInfoPosition(fetchDataVO);
            pullDataErroInfoVO.setErroinfo(erros.toString());
            voAndErroInfoVoMap.put(fetchDataVO, pullDataErroInfoVO);
        }
        return voAndErroInfoVoMap;
    }

    @Override
    public PullDataErroInfoVO getErroInfoPosition(IFetchData data) {
        IFIFetchData fetchDataVO = (IFIFetchData) data;
        PullDataErroInfoVO pullDataErroInfoVO = new PullDataErroInfoVO();
        pullDataErroInfoVO.setImestype(CMMesTypeEnum.ERROR.toIntValue());
        // ���ô�����Ϣ�������������ģ�������Ϣ
        pullDataErroInfoVO.setWkcenterid(fetchDataVO.getWkid()); // ���ü����������й���������Ϣ
        pullDataErroInfoVO.setCdptid(fetchDataVO.getCdeptid()); // ���ü����������в��ű�����Ϣ
        // ���ü��������ݵĳɱ�����ID
        pullDataErroInfoVO.setCcostcenterid(fetchDataVO.getCcostcenterid());
        pullDataErroInfoVO.setCmaterialid(fetchDataVO.getCproductid());
        pullDataErroInfoVO.setCmaterialvid(fetchDataVO.getCmaterialid());// ����
        pullDataErroInfoVO.setCmeasdocid(fetchDataVO.getCmeasdocid());// ������λ
        pullDataErroInfoVO.setNnum(fetchDataVO.getNnum());
        pullDataErroInfoVO.setNprice(fetchDataVO.getNprice());
        pullDataErroInfoVO.setNmoney(fetchDataVO.getNmoney());
        pullDataErroInfoVO.setCmoid(fetchDataVO.getCmocode());
        pullDataErroInfoVO.setVbilltype(fetchDataVO.getCbilltypecode());// ��������
        pullDataErroInfoVO.setCsrccostdomainid(fetchDataVO.getCsrccostdomainid());// �ɱ���

        return pullDataErroInfoVO;
    }

    /**
     * �õ����ϵĲ���������Ϣ
     *
     * @param matrvids
     *            ���ϰ汾id����
     * @return Map<���ϰ汾id, ������Ϣ>
     * @throws BusinessException
     *             ҵ���쳣
     */
    protected Map<String, MaterialProdVO> queryMatrProd(String[] matrvids, String[] fields) throws BusinessException {
        Map<String, MaterialProdVO> matrProdMap =
                BDAdapter.queryMaterialProduceInfoByPks(matrvids, this.pk_Org, fields);
        if (CMMapUtil.isEmpty(matrProdMap)) {
            matrProdMap = new HashMap<String, MaterialProdVO>();
        }
        return matrProdMap;
    }

    protected boolean checkFrees(MaterialProdVO prodVO, IFIFetchData data, Map<String, String> columnMap) {
        for (String name : FetchKeyConst.CMATERIAL_PLAN_FREE_COLUMN) {
            if (UFBoolean.TRUE.equals(prodVO.getAttributeValue(name))
                    && data.getAttributeValue(columnMap.get(name)) == null) {
                return true;
            }
        }
        return false;
    }
}
