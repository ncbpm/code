package nc.vo.cm.fetchdata.entity;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class  ChuyunFetchDataVO extends SuperVO {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1367006057245476048L;
	
	
private String ccostcenterid;//���ĳɱ�����

    public String getCcostcenterid() {
	return ccostcenterid;
}

public void setCcostcenterid(String ccostcenterid) {
	this.ccostcenterid = ccostcenterid;
}

	/**
     * ������������
     */
    private String cmoid;

    /**
     * ������������
     */
    private String cmocode;

    /**
     * ԭʼ������������
     */
    // private String moOriginalCode;

    /**
     * ����
     */
    private String cdptid;

    /**
     * ͷ��������
     */
    private String headwkid;

    /**
     * ������������
     */
    private String wkid;

    /**
     * ��������
     */
    private String materialid;

    /**
     * ��ҵ����
     */
    private String bdActivityId;

    /**
     * ��Դ����
     */
    private String sourcetype;

    /**
     * ʵ����ҵ��
     */
    private UFDouble wknum;

    /**
     * ��Ӧ��
     */
    private String cvendorid;

    /**
     * ��������
     */
    private String cproductorid;

    /**
     * ��Ŀ
     */
    private String cprojectid;

    /**
     * �ͻ�
     */
    private String ccustomerid;

    /**
     * ���ɸ�������1
     */
    private String vfree1;

    /**
     * ���ɸ�������2
     */
    private String vfree2;

    /**
     * ���ɸ�������3
     */
    private String vfree3;

    /**
     * ���ɸ�������4
     */
    private String vfree4;

    /**
     * ���ɸ�������5
     */
    private String vfree5;

    /**
     * ���ɸ�������6
     */
    private String vfree6;

    /**
     * ���ɸ�������7
     */
    private String vfree7;

    /**
     * ���ɸ�������8
     */
    private String vfree8;

    /**
     * ���ɸ�������9
     */
    private String vfree9;

    /**
     * ���ɸ�������10
     */
    private String vfree10;

    /**
     * �������
     */
    private Integer finstoragetype;

    public String getCmoid() {
        return this.cmoid;
    }

    public void setCmoid(String moid) {
        this.cmoid = moid;
    }

    public String getCmocode() {
        return this.cmocode;
    }

    public void setCmocode(String mocode) {
        this.cmocode = mocode;
    }

    // public String getMoOriginalCode() {
    // return this.moOriginalCode;
    // }
    //
    // public void setMoOriginalCode(String moOriginalCode) {
    // this.moOriginalCode = moOriginalCode;
    // }

    public String getCdptid() {
        return this.cdptid;
    }

    public void setCdptid(String cdptid) {
        this.cdptid = cdptid;
    }

    public String getWkid() {
        return this.wkid;
    }

    public void setWkid(String wkid) {
        this.wkid = wkid;
    }

    public String getMaterialid() {
        return this.materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid;
    }

    public String getBdActivityId() {
        return this.bdActivityId;
    }

    public void setBdActivityId(String bdActivityId) {
        this.bdActivityId = bdActivityId;
    }

    public String getSourcetype() {
        return this.sourcetype;
    }

    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype;
    }

    public UFDouble getWknum() {
        return this.wknum;
    }

    public void setWknum(UFDouble wknum) {
        this.wknum = wknum;
    }

    public void setHeadwkid(String headwkid) {
        this.headwkid = headwkid;
    }

    public String getHeadwkid() {
        return this.headwkid;
    }

    /**
     * ȡ�ù�Ӧ��
     * 
     * @return String
     */
    public String getCvendorid() {
        return this.cvendorid;
    }

    /**
     * ���ù�Ӧ��
     * 
     * @param cvendorid
     */
    public void setCvendorid(String cvendorid) {
        this.cvendorid = cvendorid;
    }

    /**
     * ȡ����������
     * 
     * @return String
     */
    public String getCproductorid() {
        return this.cproductorid;
    }

    /**
     * ������������
     * 
     * @param cvendorid
     */
    public void setCproductorid(String cproductorid) {
        this.cproductorid = cproductorid;
    }

    /**
     * ȡ����Ŀ
     * 
     * @return String
     */
    public String getCprojectid() {
        return this.cprojectid;
    }

    /**
     * ������Ŀ
     * 
     * @param cvendorid
     */
    public void setCprojectid(String cprojectid) {
        this.cprojectid = cprojectid;
    }

    /**
     * ȡ�ÿͻ�
     * 
     * @return String
     */
    public String getCcustomerid() {
        return this.ccustomerid;
    }

    /**
     * ���ÿͻ�
     * 
     * @param cvendorid
     */
    public void setCcustomerid(String ccustomerid) {
        this.ccustomerid = ccustomerid;
    }

    /**
     * ȡ�����ɸ�������1
     * 
     * @return String
     */
    public String getVfree1() {
        return this.vfree1;
    }

    /**
     * �������ɸ�������1
     * 
     * @param vfree1
     */
    public void setVfree1(String vfree1) {
        this.vfree1 = vfree1;
    }

    /**
     * ȡ�����ɸ�������2
     * 
     * @return String
     */
    public String getVfree2() {
        return this.vfree2;
    }

    /**
     * �������ɸ�������2
     * 
     * @param vfree2
     */
    public void setVfree2(String vfree2) {
        this.vfree2 = vfree2;
    }

    /**
     * ȡ�����ɸ�������3
     * 
     * @return String
     */
    public String getVfree3() {
        return this.vfree3;
    }

    /**
     * �������ɸ�������3
     * 
     * @param vfree3
     */
    public void setVfree3(String vfree3) {
        this.vfree3 = vfree3;
    }

    /**
     * ȡ�����ɸ�������4
     * 
     * @return String
     */
    public String getVfree4() {
        return this.vfree4;
    }

    /**
     * �������ɸ�������4
     * 
     * @param vfree4
     */
    public void setVfree4(String vfree4) {
        this.vfree4 = vfree4;
    }

    /**
     * ȡ�����ɸ�������5
     * 
     * @return String
     */
    public String getVfree5() {
        return this.vfree5;
    }

    /**
     * �������ɸ�������5
     * 
     * @param vfree5
     */
    public void setVfree5(String vfree5) {
        this.vfree5 = vfree5;
    }

    /**
     * ȡ�����ɸ�������6
     * 
     * @return String
     */
    public String getVfree6() {
        return this.vfree6;
    }

    /**
     * �������ɸ�������6
     * 
     * @param vfree6
     */
    public void setVfree6(String vfree6) {
        this.vfree6 = vfree6;
    }

    /**
     * ȡ�����ɸ�������7
     * 
     * @return String
     */
    public String getVfree7() {
        return this.vfree7;
    }

    /**
     * �������ɸ�������7
     * 
     * @param vfree7
     */
    public void setVfree7(String vfree7) {
        this.vfree7 = vfree7;
    }

    /**
     * ȡ�����ɸ�������8
     * 
     * @return String
     */
    public String getVfree8() {
        return this.vfree8;
    }

    /**
     * �������ɸ�������8
     * 
     * @param vfree8
     */
    public void setVfree8(String vfree8) {
        this.vfree8 = vfree8;
    }

    /**
     * ȡ�����ɸ�������9
     * 
     * @return String
     */
    public String getVfree9() {
        return this.vfree9;
    }

    /**
     * �������ɸ�������9
     * 
     * @param vfree9
     */
    public void setVfree9(String vfree9) {
        this.vfree9 = vfree9;
    }

    /**
     * ȡ�����ɸ�������10
     * 
     * @return String
     */
    public String getVfree10() {
        return this.vfree10;
    }

    /**
     * �������ɸ�������10
     * 
     * @param vfree10
     */
    public void setVfree10(String vfree10) {
        this.vfree10 = vfree10;
    }

    /**
     * ����������
     * 
     * @return
     */
    public Integer getFinstoragetype() {
        return this.finstoragetype;
    }

    /**
     * �����������
     * 
     * @param finstoragetype
     */
    public void setFinstoragetype(Integer finstoragetype) {
        this.finstoragetype = finstoragetype;
    }
}
