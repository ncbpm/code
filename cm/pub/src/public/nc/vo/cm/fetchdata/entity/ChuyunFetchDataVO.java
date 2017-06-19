package nc.vo.cm.fetchdata.entity;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class  ChuyunFetchDataVO extends SuperVO {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1367006057245476048L;
	
	
private String ccostcenterid;//消耗成本中心

    public String getCcostcenterid() {
	return ccostcenterid;
}

public void setCcostcenterid(String ccostcenterid) {
	this.ccostcenterid = ccostcenterid;
}

	/**
     * 生产订单主键
     */
    private String cmoid;

    /**
     * 生产订单编码
     */
    private String cmocode;

    /**
     * 原始生产订单编码
     */
    // private String moOriginalCode;

    /**
     * 部门
     */
    private String cdptid;

    /**
     * 头工作中心
     */
    private String headwkid;

    /**
     * 工作中心主键
     */
    private String wkid;

    /**
     * 物料主键
     */
    private String materialid;

    /**
     * 作业档案
     */
    private String bdActivityId;

    /**
     * 来源类型
     */
    private String sourcetype;

    /**
     * 实际作业量
     */
    private UFDouble wknum;

    /**
     * 供应商
     */
    private String cvendorid;

    /**
     * 生产厂商
     */
    private String cproductorid;

    /**
     * 项目
     */
    private String cprojectid;

    /**
     * 客户
     */
    private String ccustomerid;

    /**
     * 自由辅助属性1
     */
    private String vfree1;

    /**
     * 自由辅助属性2
     */
    private String vfree2;

    /**
     * 自由辅助属性3
     */
    private String vfree3;

    /**
     * 自由辅助属性4
     */
    private String vfree4;

    /**
     * 自由辅助属性5
     */
    private String vfree5;

    /**
     * 自由辅助属性6
     */
    private String vfree6;

    /**
     * 自由辅助属性7
     */
    private String vfree7;

    /**
     * 自由辅助属性8
     */
    private String vfree8;

    /**
     * 自由辅助属性9
     */
    private String vfree9;

    /**
     * 自由辅助属性10
     */
    private String vfree10;

    /**
     * 入库类型
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
     * 取得供应商
     * 
     * @return String
     */
    public String getCvendorid() {
        return this.cvendorid;
    }

    /**
     * 设置供应商
     * 
     * @param cvendorid
     */
    public void setCvendorid(String cvendorid) {
        this.cvendorid = cvendorid;
    }

    /**
     * 取得生产厂商
     * 
     * @return String
     */
    public String getCproductorid() {
        return this.cproductorid;
    }

    /**
     * 设置生产厂商
     * 
     * @param cvendorid
     */
    public void setCproductorid(String cproductorid) {
        this.cproductorid = cproductorid;
    }

    /**
     * 取得项目
     * 
     * @return String
     */
    public String getCprojectid() {
        return this.cprojectid;
    }

    /**
     * 设置项目
     * 
     * @param cvendorid
     */
    public void setCprojectid(String cprojectid) {
        this.cprojectid = cprojectid;
    }

    /**
     * 取得客户
     * 
     * @return String
     */
    public String getCcustomerid() {
        return this.ccustomerid;
    }

    /**
     * 设置客户
     * 
     * @param cvendorid
     */
    public void setCcustomerid(String ccustomerid) {
        this.ccustomerid = ccustomerid;
    }

    /**
     * 取得自由辅助属性1
     * 
     * @return String
     */
    public String getVfree1() {
        return this.vfree1;
    }

    /**
     * 设置自由辅助属性1
     * 
     * @param vfree1
     */
    public void setVfree1(String vfree1) {
        this.vfree1 = vfree1;
    }

    /**
     * 取得自由辅助属性2
     * 
     * @return String
     */
    public String getVfree2() {
        return this.vfree2;
    }

    /**
     * 设置自由辅助属性2
     * 
     * @param vfree2
     */
    public void setVfree2(String vfree2) {
        this.vfree2 = vfree2;
    }

    /**
     * 取得自由辅助属性3
     * 
     * @return String
     */
    public String getVfree3() {
        return this.vfree3;
    }

    /**
     * 设置自由辅助属性3
     * 
     * @param vfree3
     */
    public void setVfree3(String vfree3) {
        this.vfree3 = vfree3;
    }

    /**
     * 取得自由辅助属性4
     * 
     * @return String
     */
    public String getVfree4() {
        return this.vfree4;
    }

    /**
     * 设置自由辅助属性4
     * 
     * @param vfree4
     */
    public void setVfree4(String vfree4) {
        this.vfree4 = vfree4;
    }

    /**
     * 取得自由辅助属性5
     * 
     * @return String
     */
    public String getVfree5() {
        return this.vfree5;
    }

    /**
     * 设置自由辅助属性5
     * 
     * @param vfree5
     */
    public void setVfree5(String vfree5) {
        this.vfree5 = vfree5;
    }

    /**
     * 取得自由辅助属性6
     * 
     * @return String
     */
    public String getVfree6() {
        return this.vfree6;
    }

    /**
     * 设置自由辅助属性6
     * 
     * @param vfree6
     */
    public void setVfree6(String vfree6) {
        this.vfree6 = vfree6;
    }

    /**
     * 取得自由辅助属性7
     * 
     * @return String
     */
    public String getVfree7() {
        return this.vfree7;
    }

    /**
     * 设置自由辅助属性7
     * 
     * @param vfree7
     */
    public void setVfree7(String vfree7) {
        this.vfree7 = vfree7;
    }

    /**
     * 取得自由辅助属性8
     * 
     * @return String
     */
    public String getVfree8() {
        return this.vfree8;
    }

    /**
     * 设置自由辅助属性8
     * 
     * @param vfree8
     */
    public void setVfree8(String vfree8) {
        this.vfree8 = vfree8;
    }

    /**
     * 取得自由辅助属性9
     * 
     * @return String
     */
    public String getVfree9() {
        return this.vfree9;
    }

    /**
     * 设置自由辅助属性9
     * 
     * @param vfree9
     */
    public void setVfree9(String vfree9) {
        this.vfree9 = vfree9;
    }

    /**
     * 取得自由辅助属性10
     * 
     * @return String
     */
    public String getVfree10() {
        return this.vfree10;
    }

    /**
     * 设置自由辅助属性10
     * 
     * @param vfree10
     */
    public void setVfree10(String vfree10) {
        this.vfree10 = vfree10;
    }

    /**
     * 获得入库类型
     * 
     * @return
     */
    public Integer getFinstoragetype() {
        return this.finstoragetype;
    }

    /**
     * 设置入库类型
     * 
     * @param finstoragetype
     */
    public void setFinstoragetype(Integer finstoragetype) {
        this.finstoragetype = finstoragetype;
    }
}
