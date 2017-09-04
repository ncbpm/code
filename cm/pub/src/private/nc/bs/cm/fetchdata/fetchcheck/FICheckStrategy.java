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
 * 存货核算取数抽象检查
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
        // 多成本域下成本信息页签启用的物料辅助属性核算不一致
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

        // 设置错误信息
        for (IFetchData data : vos) {
            IFIFetchData vo = (IFIFetchData) data;
            StringBuilder codes = new StringBuilder();

            // 材料出库单
            //2107-09-01 判断存在，否则空异常
//            if (CMStringUtil.isNotEmpty(data.getCproductid())) {
            if (CMStringUtil.isNotEmpty(data.getCproductid()) && materialVOMap.containsKey(vo.getCproductid())) {
             //2107-09-01 判断存在否则空异常
                codes.append(materialVOMap.get(vo.getCproductid()).getCode());
            }
            else {
                // 产成品入库单、委外加工入库单
                if (CMStringUtil.isNotEmpty(vo.getCmaterialid()) && materials.contains(vo.getCmaterialid())) {
                    // 产品
                    codes.append(materialVOMap.get(vo.getCmaterialid()).getCode());
                }
                if (CMStringUtil.isNotEmpty(vo.getPrimaryproductid()) && materials.contains(vo.getPrimaryproductid())) {
                    // 主产品
                    if (CMStringUtil.isNotEmpty(codes.toString())) {
                        codes.append(",");
                    }
                    codes.append(materialVOMap.get(vo.getPrimaryproductid()).getCode());
                }
            }

            if (CMStringUtil.isNotEmpty(codes.toString())) {
                StringBuilder msg = new StringBuilder();
                msg.append("[" + codes + "]");
                msg.append(FetchDataLangConst.getERR_NOTUNIQUEMATERIALCOST());// 多成本域下成本信息页签启用的物料辅助属性核算不一致
                PullDataErroInfoVO pullDataErroInfoVO = this.getErroInfoPosition(vo);
                pullDataErroInfoVO.setErroinfo(msg.toString());
                voAndErroInfoVoMap.put(vo, pullDataErroInfoVO);
            }

        }

        return voAndErroInfoVoMap;
    }

    /**
     * 物料属性【成本中心确认方式】为存货核算的成本中心处理
     *
     * @param correctData
     * @throws BusinessException
     */
    protected void setCostCenterByIA(IFetchData[] correctData, Map<String, String> wkidAndCostCenridMap)
            throws BusinessException {
        // 查询出所有的该工厂下的成本中心pk
        String[] costCenterpks = FIAdapter.getAllCostCenter();
        Set<String> sets = new HashSet<String>();
        for (String costcenterpk : costCenterpks) {
            sets.add(costcenterpk);
        }
        for (IFetchData fiData : correctData) {
            if (CMStringUtil.isNotEmpty(fiData.getCcostcenterid())) {
                if (!sets.contains(fiData.getCcostcenterid())) {// 如果没有说明成本中心为已经停用的
                    // 不存在工作中心对应的成本中心，检查部门对应的成本中心
                    if (!wkidAndCostCenridMap.containsKey(fiData.getWkid())) {
                        if (wkidAndCostCenridMap.containsKey(fiData.getCdeptid())) {
                            // 存在则设置对应的成本中心
                            fiData.setCcostcenterid(wkidAndCostCenridMap.get(fiData.getCdeptid()));
                        }
                        else {// 最终没找到有效的成本中心设置为空
                            fiData.setCcostcenterid(null);
                        }
                    }
                    else {// 存在则设置对应的成本中心
                        fiData.setCcostcenterid(wkidAndCostCenridMap.get(fiData.getWkid()));
                    }
                }
            }
            else {// 成本中心为空
                // 不存在工作中心对应的成本中心，检查部门对应的成本中心
                if (!wkidAndCostCenridMap.containsKey(fiData.getWkid())) {
                    if (wkidAndCostCenridMap.containsKey(fiData.getCdeptid())) {
                        // 存在则设置对应的成本中心
                        fiData.setCcostcenterid(wkidAndCostCenridMap.get(fiData.getCdeptid()));
                    }
                    else {// 最终没找到有效的成本中心设置为空
                        fiData.setCcostcenterid(null);
                    }
                }
                else {// 存在则设置对应的成本中心
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
        try {// 成本中心确认方式存货核算处理
            this.setCostCenterByIA(costCenterByIAList.toArray(new IFIFetchData[0]), wkidAndCostCenridMap);
        }
        catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return this.judgeCostCenterErrorNull(vos);
    }

    /**
     * 判断成本中心是否为空
     */
    private StringBuffer getCostCenterErrorInfo(IFetchData vo) {
        StringBuffer erros = new StringBuffer();
        if (CMStringUtil.isEmpty(vo.getCcostcenterid())) { // 未找到有效的成本中心
            erros.append(FetchDataLangConst.getERR_FICOSTCENTRERNULL());
        }
        return erros;
    }

    /**
     * 判断单据的信息是否为null
     *
     * @param vos
     *            取过来的数据
     * @return Map<MMFetchDataVO, PullDataErroInfoVO> 原数据和错误数据vo的Map
     */
    public Map<IFetchData, PullDataErroInfoVO> judgeCostCenterErrorNull(IFetchData[] vos) {
        Map<IFetchData, PullDataErroInfoVO> voAndErroInfoVoMap = new HashMap<IFetchData, PullDataErroInfoVO>();
        for (IFetchData fetchDataVO : vos) {
            // 错误信息
            StringBuffer erros = this.getCostCenterErrorInfo(fetchDataVO);
            if (erros == null || erros.length() == 0) {
                continue;
            }
            // 错误位置
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
        // 设置错误信息，所属工作中心，部门信息
        pullDataErroInfoVO.setWkcenterid(fetchDataVO.getWkid()); // 设置检查错误数据中工作中心信息
        pullDataErroInfoVO.setCdptid(fetchDataVO.getCdeptid()); // 设置检查错误数据中部门编码信息
        // 设置检查错误数据的成本中心ID
        pullDataErroInfoVO.setCcostcenterid(fetchDataVO.getCcostcenterid());
        pullDataErroInfoVO.setCmaterialid(fetchDataVO.getCproductid());
        pullDataErroInfoVO.setCmaterialvid(fetchDataVO.getCmaterialid());// 物料
        pullDataErroInfoVO.setCmeasdocid(fetchDataVO.getCmeasdocid());// 计量单位
        pullDataErroInfoVO.setNnum(fetchDataVO.getNnum());
        pullDataErroInfoVO.setNprice(fetchDataVO.getNprice());
        pullDataErroInfoVO.setNmoney(fetchDataVO.getNmoney());
        pullDataErroInfoVO.setCmoid(fetchDataVO.getCmocode());
        pullDataErroInfoVO.setVbilltype(fetchDataVO.getCbilltypecode());// 单据类型
        pullDataErroInfoVO.setCsrccostdomainid(fetchDataVO.getCsrccostdomainid());// 成本域

        return pullDataErroInfoVO;
    }

    /**
     * 得到物料的部分生产信息
     *
     * @param matrvids
     *            物料版本id数组
     * @return Map<物料版本id, 生产信息>
     * @throws BusinessException
     *             业务异常
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
