package nc.ui.bd.ref.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.mmbd.adapter.uap.UAPAdapter;
import nc.pubitf.bd.feature.ffile.IPubFFileQueryService;
import nc.pubitf.uapbd.IMaterialPubService;
import nc.ui.ml.NCLangRes;
import nc.util.mmf.busi.service.MaterialPubService;
import nc.util.mmf.framework.base.MMStringUtil;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.bd.bom.bom0202.entity.BomVO;
import nc.vo.bd.bom.bom0202.enumeration.BomTypeEnum;
import nc.vo.bd.bom.bom0202.message.MMBDLangConstBom0202;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.MaterialVersionVO;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * ����Bom�汾����
 * 
 * @author���ܾ�
 */
public class BomVerRefModel extends nc.ui.bd.ref.AbstractRefModel {

    public static final int OID = 1;

    public static final int VID = 2;

    public static final int OIDANDVID = 3;

    private int joinType;

    public int getJoinType() {
        return this.joinType;
    }

    public void setJoinType(int joinType) {
        this.joinType = joinType;
    }

    private String featurecode;

    public String getFeaturecode() {
        return this.featurecode;
    }

    public void setFeaturecode(String featurecode) {
        this.featurecode = featurecode;
    }

    /**
     * �ֶ�����
     */
    private final String[] fieldname = {
        NCLangRes.getInstance().getStrByID("1014362_0", "01014362-0419")/* BOM����֯ */,
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002911")/* @res "���ϱ���" */,
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002908")/* @res "��������" */,
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1014362_0", "01014362-0049")/* @res "���ϰ汾" */,
        NCLangRes.getInstance().getStrByID("1014362_0", "01014362-0420")/* ������ */,
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000001")/* @res "BOM�汾" */,
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1014362_0", "01014362-0178") /* @res "�Ƿ�Ĭ��" */,
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1014362_0", "01014362-0247")
    /* @res "BOM����" */
        ,"BOM��ע"
    };

    /**
     * �ֶα���
     */
    private final String[] fieldcode = {
        BomVO.TABLE_NAME + "." + BomVO.PK_ORG, MaterialVO.getDefaultTableName() + "." + MaterialVO.CODE,
        MaterialVO.getDefaultTableName() + "." + MaterialVO.PK_MATERIAL,
        MaterialVO.getDefaultTableName() + "." + MaterialVO.VERSION, BomVO.TABLE_NAME + "." + BomVO.HCFEATURECODE,
        BomVO.TABLE_NAME + "." + BomVO.HVERSION, BomVO.TABLE_NAME + "." + BomVO.HBDEFAULT,
        BomVO.TABLE_NAME + "." + BomVO.FBOMTYPE,
        BomVO.TABLE_NAME + "." + BomVO.HVNOTE,
    };

    /**
     * ��������
     */
    private final String title = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1014362_0", "01014362-0057")/*
                                                                                                                     * @res
                                                                                                                     * "���������嵥"
                                                                                                                     */;

    /**
     * ����
     */
    private final String pkcode = BomVO.TABLE_NAME + "." + BomVO.CBOMID;

    /**
     * ���ձ���
     */
    private final String vidtablename = BomVO.TABLE_NAME + " inner join " + MaterialVO.getDefaultTableName() + " on "
            + BomVO.TABLE_NAME + "." + BomVO.HCMATERIALVID + "=" + MaterialVO.getDefaultTableName() + "."
            + MaterialVO.PK_MATERIAL;

    private final String oidandvidtablename = BomVO.TABLE_NAME + " left join " + MaterialVO.getDefaultTableName()
            + " on " + BomVO.TABLE_NAME + "." + BomVO.HCMATERIALVID + "=" + MaterialVO.getDefaultTableName() + "."
            + MaterialVO.PK_MATERIAL;

    /**
     * ���ձ���
     */
    private final String oidtablename = BomVO.TABLE_NAME + " inner join " + MaterialVersionVO.getDefaultTableName()
            + " on " + BomVO.TABLE_NAME + "." + BomVO.HCMATERIALID + "=" + MaterialVersionVO.getDefaultTableName()
            + "." + MaterialVersionVO.PK_SOURCE + " inner join " + MaterialVO.getDefaultTableName() + " on "
            + MaterialVO.getDefaultTableName() + "." + MaterialVO.PK_MATERIAL + "="
            + MaterialVersionVO.getDefaultTableName() + "." + MaterialVersionVO.PK_MATERIAL;

    /**
     * ���������ֶ�
     */
    private final String[] hiddenFieldCode = {
        BomVO.TABLE_NAME + "." + BomVO.CBOMID, BomVO.TABLE_NAME + "." + BomVO.HCMATERIALID,
        BomVO.TABLE_NAME + "." + BomVO.HCMATERIALVID
    };

    // ��Ҫ�������Ϻ����ϰ汾����
    private String materialoid = null;

    private String materialvid = null;

    private List<String> oidList = new ArrayList<String>();

    private List<String> vidList = new ArrayList<String>();

    /**
     * BomVerRefModel ������ע�⡣
     */
    public BomVerRefModel() {
        super();
    }

    /**
     * getDefaultFieldCount ����ע�⡣
     */
    @Override
    public int getDefaultFieldCount() {
        return this.fieldcode.length;
    }

    /**
     * ��ʾ�ֶ��б�
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String[] getFieldCode() {
        return this.fieldcode;
    }

    /**
     * ��ʾ�ֶ�������
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String[] getFieldName() {
        return this.fieldname;
    }

    /**
     * @return ���� formulas��
     */
    @Override
    public String[][] getFormulas() {
        String materialFomula =
                "getMLCValue(\"" + MaterialVO.getDefaultTableName() + "\",\"" + MaterialVO.NAME + "\",\""
                        + MaterialVO.PK_MATERIAL + "\"," + MaterialVO.getDefaultTableName() + "."
                        + MaterialVO.PK_MATERIAL + ")";
        return new String[][] {
            {
                MaterialVO.getDefaultTableName() + "." + MaterialVO.PK_MATERIAL, materialFomula
            }
        };
    }

    /**
     * ��ȡ���ص��ֶ�
     * 
     * @return �����ֶ�
     */
    @Override
    public String[] getHiddenFieldCode() {
        return this.hiddenFieldCode;
    }

    /**
     * �����ֶ���
     * 
     * @return ��ȡ�����ֶ�
     */
    @Override
    public String getPkFieldCode() {
        return this.pkcode;
    }

    /**
     * ���������ֶ�
     * 
     * @return ��������
     */
    @Override
    public String getRefNameField() {
        return BomVO.TABLE_NAME + "." + BomVO.HVERSION;
    }

    /**
     * ���ձ����ֶ�
     * 
     * @return �����ֶ�
     */
    @Override
    public String getRefCodeField() {
        return BomVO.TABLE_NAME + "." + BomVO.HVERSION;
    }

    /**
     * ���ձ���
     * 
     * @return ���ձ���
     */
    @Override
    public String getRefTitle() {
        return this.title;
    }

    /**
     * �������ݿ�������ͼ��
     * 
     * @return �������ݿ���
     */
    @Override
    public String getTableName() {
        if (this.getJoinType() == BomVerRefModel.OID) {
            return this.oidtablename;
        }
        else if (this.getJoinType() == BomVerRefModel.VID) {
            return this.vidtablename;
        }
        else if (this.getJoinType() == BomVerRefModel.OIDANDVID) {
            return this.oidandvidtablename;
        }
        return this.oidtablename;

    }

    private boolean needUseorg = true;

    @Override
    protected String getEnvWherePart() {
        StringBuilder wherePart = new StringBuilder();

        wherePart.append(BomVO.TABLE_NAME).append(".").append(BomVO.PK_GROUP).append("='").append(this.getPk_group())
                .append("'").append(" and ").append(BomVO.TABLE_NAME).append(".").append(BomVO.PK_ORG);

        if (this.needUseorg) {
            wherePart
                    .append(" in ")
                    .append("(select useorg.pk_org from bd_bom_useorg useorg where useorg.pk_useorg='"
                            + this.getPk_org() + "'").append(" and ").append(BomVO.TABLE_NAME).append(".")
                    .append(BomVO.CBOMID).append("=").append("useorg.cbomid").append(" and ").append("useorg.dr=0)");
        }
        else {
            wherePart.append("='" + this.getPk_org() + "'");
        }
        wherePart.append(" and ")
        // .append(BomVO.TABLE_NAME).append(".").append(BomVO.HBCUSTOMIZED).append("='N'").append(" and ")
                .append(BomVO.TABLE_NAME).append(".dr = 0 ");
        this.addMaterialFilter(wherePart);
        this.addMaterialvidFilter(wherePart);

        // ���������ж��Ƿ��Ƕ���bom
        this.addCustomizedFilter(wherePart);

        return wherePart.toString();
    }

    /**
     * ���������ж��Ƿ��Ƕ���bom
     * 
     * @param wherePart
     */
    private void addCustomizedFilter(StringBuilder wherePart) {
        String vid = this.materialvid;
        String oid = this.materialoid;
        if (MMValueCheck.isEmpty(vid)) {
            if (MMValueCheck.isEmpty(oid)) {
                // wherePart.append(" and ").append(BomVO.TABLE_NAME).append(".")
                // .append(BomVO.HBCUSTOMIZED + " = '" + UFBoolean.FALSE + "' ");
                return;
            }
            Map<String, String> oidMap = UAPAdapter.getVidByOid(new String[] {
                oid
            });
            vid = oidMap.get(oid);
        }
        try {
            Map<String, String> featureMap =
                    NCLocator.getInstance().lookup(IMaterialPubService.class)
                            .queryFeatureClassByMaterialVID(new String[] {
                                vid
                            }, this.getPk_group());
            if (MMValueCheck.isNotEmpty(featureMap.get(vid))) {// ������
                // �����벻Ϊ�գ����ն���BOM
                if (MMStringUtil.isNotEmpty(this.featurecode)) {
                    wherePart.append(" and ").append(BomVO.TABLE_NAME).append(".")
                            .append(BomVO.HBCUSTOMIZED + " = '" + UFBoolean.TRUE + "' ");
                    wherePart.append(" and ").append(BomVO.TABLE_NAME).append(".")
                            .append(BomVO.HCFEATURECODE + " = '" + this.featurecode + "' ");
                }
                // ������Ϊ�գ���������BOM
                else {
                    wherePart.append(" and ").append(BomVO.TABLE_NAME).append(".")
                            .append(BomVO.HBCUSTOMIZED + " = '" + UFBoolean.FALSE + "' ");
                    wherePart.append(" and ").append(BomVO.TABLE_NAME).append(".")
                            .append(BomVO.FBOMTYPE + " = '" + BomTypeEnum.CONFIGBOM.toIntValue() + "' ");
                }
            }
            else {
                wherePart.append(" and ").append(BomVO.TABLE_NAME).append(".")
                        .append(BomVO.HBCUSTOMIZED + " = '" + UFBoolean.FALSE + "' ");
            }
        }
        catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
    }

    private void addMaterialvidFilter(StringBuilder wherePart) {
        if (MMValueCheck.isEmpty(this.materialvid)) {
            return;
        }
        wherePart.append(" and (").append(BomVO.TABLE_NAME).append(".")
                .append(BomVO.HCMATERIALVID + " = '" + this.materialvid + "' ").append(" or ").append(BomVO.TABLE_NAME)
                .append(".").append(BomVO.HCMATERIALVID).append(" = '~') ");
    }

    private void addMaterialFilter(StringBuilder wherePart) {
        if (MMValueCheck.isEmpty(this.materialoid)) {
            return;
        }
        wherePart.append(" and ").append(BomVO.TABLE_NAME).append(".")
                .append(BomVO.HCMATERIALID + " = '" + this.materialoid + "' ");
    }

    // @Override
    // public Vector matchPkData(String[] strPkValues) {
    //
    // Vector v = null;
    // String matchsql = null;
    // matchsql = this.getMatchSql(strPkValues);
    // Vector matchVec2 = this.getMatchVectorFromLRU(strPkValues);
    // Vector matchVec = new Vector();
    // // ��������Ҫ�Ĳ�����
    // Vector value = new Vector();
    // value.setSize(this.fieldcode.length + this.hiddenFieldCode.length);
    // value.set(0, strPkValues[0]);// bd_material.code
    // value.set(1, strPkValues[0]);// bd_material.name
    // value.set(2, 1);// bd_material.version
    // value.set(3, strPkValues[0]);// bd_bom.version
    // matchVec.add(value);
    // this.setSelectedData(matchVec);
    // return matchVec;
    // }

    @Override
    public String getOrderPart() {
        return BomVO.TABLE_NAME + "." + BomVO.HCMATERIALVID + "," + BomVO.TABLE_NAME + "." + BomVO.HVERSION;
    }

    /**
     * ���������
     * 
     * @param iSelectFieldCount
     * @param strSql
     * @param strFieldCode
     * @param hiddenFields
     */
    @Override
    public void addQueryColumn(int iSelectFieldCount, StringBuffer strSql, String[] strFieldCode, String[] hiddenFields) {

        int nameFieldIndex = this.getFieldIndex(this.getRefNameField());

        for (int i = 0; i < iSelectFieldCount; i++) {
            if (this.isMutilLangNameRef() && i == nameFieldIndex) {

                // edit by yangwhc. ���ö���
                strSql.append(this.getRefNameField());

            }
            else {
                strSql.append(strFieldCode[i]);
            }

            if (i < iSelectFieldCount - 1) {
                strSql.append(",");
            }
        }
        // ���������ֶ�
        if (hiddenFields != null && hiddenFields.length > 0) {
            for (int k = 0; k < hiddenFields.length; k++) {
                if (hiddenFields[k] != null && hiddenFields[k].trim().length() > 0) {
                    strSql.append(",");
                    strSql.append(hiddenFields[k]);
                }
            }
        }
    }

    @Override
    public java.util.Vector getData() {

        String sql = this.getRefSql();

        Vector v = null;
        if (this.isCacheEnabled()) {
            /** �ӻ�������� */
            v = this.modelHandler.getFromCache(this.getRefDataCacheKey(), this.getRefCacheSqlKey());
        }

        if (v == null) {

            // �����ݿ��--Ҳ�����ڴ˶�������

            try {

                if (this.isFromTempTable()) {
                    v = this.modelHandler.queryRefDataFromTemplateTable(sql);
                }
                else {
                    // ͬʱ��ȡ������Ŀ����
                    v = this.getQueryResultVO();
                }

            }
            catch (Exception e) {
                Debug.debug(e.getMessage(), e);
            }

        }
        this.setEnumDate(v);

        if (v != null && this.isCacheEnabled()) {
            /** ���뵽������ */
            this.modelHandler.putToCache(this.getRefDataCacheKey(), this.getRefCacheSqlKey(), v);
        }

        this.m_vecData = v;
        return this.m_vecData;
    }

    // ��ö��ֵ��ʾ����
    @SuppressWarnings("unchecked")
    private void setEnumDate(Vector v) {
        if (MMValueCheck.isEmpty(v)) {
            return;
        }
        Map<String, MaterialVO> materialMap = new HashMap<String, MaterialVO>();
        for (int i = 0; i < v.size(); i++) {
            Vector item = (Vector) v.get(i);
            String featurecode = (String) item.get(4);// ������Ҫ��idװ������
            if (MMStringUtil.isNotEmpty(featurecode)) {
                try {
                    item.set(4, this.getFFileQueryService().querySkuCodeByFFileid(new String[] {
                        featurecode
                    }).get(featurecode));
                }
                catch (BusinessException e) {
                    ExceptionUtils.wrappException(e);
                }
            }
            Integer value = (Integer) item.get(7);
            if (BomTypeEnum.PRODUCTFINISH.equalsValue(value)) {
                item.set(7, MMBDLangConstBom0202.getREFPRODUCTBOM());
            }
            else if (BomTypeEnum.RTFINISH.equalsValue(value)) {
                item.set(7, MMBDLangConstBom0202.getREFPACKAGEBOM());
            }
            else if (BomTypeEnum.CONFIGBOM.equalsValue(value)) {
                item.set(7, NCLangRes.getInstance().getStrByID("1014362_0", "01014362-0393")/* ����BOM */);
            }
            String cmaterilvid = (String) item.get(10);
            if (cmaterilvid == null) {
                String cmaterialoid = (String) item.get(9);
                String cmaterialvid = MaterialPubService.convertMaterialid2Vid(cmaterialoid);
                Map<String, MaterialVO> materialVOMap = MaterialPubService.queryMaterialBaseInfoByPks(new String[] {
                    cmaterialvid
                }, new String[] {
                    MaterialVO.CODE, MaterialVO.NAME
                });
                if (materialVOMap.containsKey(cmaterialvid)) {
                    materialMap.putAll(materialVOMap);
                    item.set(1, materialVOMap.get(cmaterialvid).getCode());
                    item.set(2, cmaterialvid);
                }
            }
            String pkorg = (String) item.get(0);
            try {
                item.set(0, UAPAdapter.getOrgBaseInfos(new String[] {
                    pkorg
                })[0].getName());
            }
            catch (BusinessException e) {
                ExceptionUtils.wrappException(e);
            }
        }

    }

    private IPubFFileQueryService getFFileQueryService() {
        return NCLocator.getInstance().lookup(IPubFFileQueryService.class);
    }

    public String getMaterialvid() {
        return this.materialvid;
    }

    public void setMaterialvid(String materialvid) {
        this.materialvid = materialvid;
    }

    public String getMaterialoid() {
        return this.materialoid;
    }

    public void setMaterialoid(String materialoid) {
        this.materialoid = materialoid;
    }

    public boolean isNeedUseorg() {
        return this.needUseorg;
    }

    public void setNeedUseorg(boolean needUseorg) {
        this.needUseorg = needUseorg;
    }

    public List<String> getOidList() {
        return this.oidList;
    }

    public void setOidList(List<String> oidList) {
        this.oidList = oidList;
    }

    public List<String> getVidList() {
        return this.vidList;
    }

    public void setVidList(List<String> vidList) {
        this.vidList = vidList;
    }

}
