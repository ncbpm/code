package nc.vo.ia.detailledger.view.cm;

import nc.vo.ia.detailledger.entity.DetailLedgerVO;
import nc.vo.ia.mi3.entity.I3ItemVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.view.AbstractDataView;
import nc.vo.pubapp.pattern.model.meta.entity.view.DataViewMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.view.IDataViewMeta;

/**
 * 成本管理取数，存货核算返回的视图VO
 * 
 * @since 6.0
 * @version 2011-11-30 上午10:20:15
 * @author 皮之兵
 */
public class CMDataVO extends AbstractDataView {

  // 是否委外
  public static final String BWWFLAG = "bwwflag";

  private static final long serialVersionUID = 430329191933736929L;

  /**
   * 是否返修
   */
  public static final String BREWORKFLAG = "breworkflag";
  
  public String ccostobjectid;//成本对象，用来给定额取数
  
  

  public String getCcostobjectid() {
	return ccostobjectid;
}

public void setCcostobjectid(String ccostobjectid) {
	this.ccostobjectid = ccostobjectid;
}

/**
   * 
   * @return UFBoolean
   */
  public UFBoolean getBreworkflag() {
    return (UFBoolean) this.getAttributeValue(CMDataVO.BREWORKFLAG);
  }

  /**
   * 
   * @param breworkflag
   */
  public void setBreworkflag(UFBoolean breworkflag) {
    this.setAttributeValue(CMDataVO.BREWORKFLAG, breworkflag);
  }

  // 是否已经成本计算
  // 对成本管理来说, 如果没有计算差异率或全月平均, 都是未成本计算.
  private UFBoolean bauditflag;

  public UFBoolean getBauditflag() {
    return this.bauditflag;
  }

  public UFBoolean getBwwflag() {
    return (UFBoolean) this.getAttributeValue(CMDataVO.BWWFLAG);
  }

  public String getCbilltypecode() {
    return (String) this.getAttributeValue(DetailLedgerVO.CBILLTYPECODE);
  }

  public String getCbomcodeid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CCOSTOBJID);
  }

  public String getCcostcenterid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CCOSTCENTERID);
  }

  public String getCdeptid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CDEPTID);
  }

  public String getCinventoryid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CINVENTORYID);
  }

  public String getCinventoryvid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CINVENTORYVID);
  }

  public String getCproductgroupid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CCOSTCLASSID);
  }

  public String getCprofitcenterid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CPROFITCENTERID);
  }

  public String getCprojectid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CPROJECTID);
  }

  public String getCstordocid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CSTORDOCID);
  }

  public String getCtrantypeid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CTRANTYPEID);
  }

  public String getCunitid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CUNITID);
  }

  public String getCvendorid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CVENDORID);
  }

  public String getCworkcenterid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CWORKCENTERID);
  }

  /**
   * 获取产品类型
   * 
   * @return 产品类型
   */
  public Integer getFProductFlag() {
    return (Integer) this.getAttributeValue(I3ItemVO.FPRODUCTFLAG);
  }

  @Override
  public IDataViewMeta getMetaData() {
    return DataViewMetaFactory.getInstance()
        .getDataViewMeta(CMDataVOMeta.class);
  }

  public UFDouble getNcost() {
    return (UFDouble) this.getAttributeValue(DetailLedgerVO.NCOST);
  }

  public UFDouble getNmny() {
    return (UFDouble) this.getAttributeValue(DetailLedgerVO.NMNY);
  }

  public UFDouble getNnum() {
    return (UFDouble) this.getAttributeValue(DetailLedgerVO.NNUM);
  }

  public UFDouble getNplanedmny() {
    return (UFDouble) this.getAttributeValue(DetailLedgerVO.NPLANEDMNY);
  }

  public String getPk_org() {
    return (String) this.getAttributeValue(DetailLedgerVO.PK_ORG);
  }

  /**
   * 获取主产品
   * 
   * @return 主产品
   */
  public String getPrimaryProductid() {
    return (String) this.getAttributeValue(I3ItemVO.CMAINPRODUCTID);
  }

  public String getVproducebatch() {
    return (String) this.getAttributeValue(DetailLedgerVO.VPRODUCEBATCH);
  }

  public String getVproduceordercode() {
    return (String) this.getAttributeValue(DetailLedgerVO.VPRODUCEORDERCODE);
  }

  public String getVfree1() {
    return (String) this.getAttributeValue(DetailLedgerVO.VFREE1);
  }

  public String getVfree2() {
    return (String) this.getAttributeValue(DetailLedgerVO.VFREE2);
  }

  public String getVfree3() {
    return (String) this.getAttributeValue(DetailLedgerVO.VFREE3);
  }

  public String getVfree4() {
    return (String) this.getAttributeValue(DetailLedgerVO.VFREE4);
  }

  public String getVfree5() {
    return (String) this.getAttributeValue(DetailLedgerVO.VFREE5);
  }

  public String getVfree6() {
    return (String) this.getAttributeValue(DetailLedgerVO.VFREE6);
  }

  public String getVfree7() {
    return (String) this.getAttributeValue(DetailLedgerVO.VFREE7);
  }

  public String getVfree8() {
    return (String) this.getAttributeValue(DetailLedgerVO.VFREE8);
  }

  public String getVfree9() {
    return (String) this.getAttributeValue(DetailLedgerVO.VFREE9);
  }

  public String getVfree10() {
    return (String) this.getAttributeValue(DetailLedgerVO.VFREE10);
  }

  public String getCasscustid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CASSCUSTID);
  }

  public String getCproductorid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CPRODUCTORID);
  }

  public String getVcostfree1() {
    return (String) this.getAttributeValue(DetailLedgerVO.VCOSTFREE1);
  }

  public String getVcostfree2() {
    return (String) this.getAttributeValue(DetailLedgerVO.VCOSTFREE2);
  }

  public String getVcostfree3() {
    return (String) this.getAttributeValue(DetailLedgerVO.VCOSTFREE3);
  }

  public String getVcostfree4() {
    return (String) this.getAttributeValue(DetailLedgerVO.VCOSTFREE4);
  }

  public String getVcostfree5() {
    return (String) this.getAttributeValue(DetailLedgerVO.VCOSTFREE5);
  }

  public String getVcostfree6() {
    return (String) this.getAttributeValue(DetailLedgerVO.VCOSTFREE6);
  }

  public String getVcostfree7() {
    return (String) this.getAttributeValue(DetailLedgerVO.VCOSTFREE7);
  }

  public String getVcostfree8() {
    return (String) this.getAttributeValue(DetailLedgerVO.VCOSTFREE8);
  }

  public String getVcostfree9() {
    return (String) this.getAttributeValue(DetailLedgerVO.VCOSTFREE9);
  }

  public String getVcostfree10() {
    return (String) this.getAttributeValue(DetailLedgerVO.VCOSTFREE10);
  }

  public String getCcostprojectid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CCOSTPROJECTID);
  }

  public String getCcostasscustid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CCOSTASSCUSTID);
  }

  public String getCcostproductorid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CCOSTPRODUCTORID);
  }

  public String getCcostvendorid() {
    return (String) this.getAttributeValue(DetailLedgerVO.CCOSTVENDORID);
  }

  public void setBauditflag(UFBoolean bauditflag) {
    this.bauditflag = bauditflag;
  }

  public void setBwwflag(UFBoolean bwwflag) {
    this.setAttributeValue(CMDataVO.BWWFLAG, bwwflag);
  }

  public void setCbilltypecode(String cbilltypecode) {
    this.setAttributeValue(DetailLedgerVO.CBILLTYPECODE, cbilltypecode);
  }

  public void setCbomcodeid(String cbomcodeid) {
    this.setAttributeValue(DetailLedgerVO.CCOSTOBJID, cbomcodeid);
  }

  public void setCcostcenterid(String ccostcenterid) {
    this.setAttributeValue(DetailLedgerVO.CCOSTCENTERID, ccostcenterid);
  }

  public void setCdeptid(String cdeptid) {
    this.setAttributeValue(DetailLedgerVO.CDEPTID, cdeptid);
  }

  public void setCinventoryid(String cinventoryid) {
    this.setAttributeValue(DetailLedgerVO.CINVENTORYID, cinventoryid);
  }

  public void setCinventoryvid(String cinventoryvid) {
    this.setAttributeValue(DetailLedgerVO.CINVENTORYVID, cinventoryvid);
  }

  public void setCproductgroupid(String cproductgroupid) {
    this.setAttributeValue(DetailLedgerVO.CCOSTCLASSID, cproductgroupid);
  }

  public void setCprofitcenterid(String cprofitcenterid) {
    this.setAttributeValue(DetailLedgerVO.CPROFITCENTERID, cprofitcenterid);
  }

  public void setCprojectid(String cprojectid) {
    this.setAttributeValue(DetailLedgerVO.CPROJECTID, cprojectid);
  }

  public void setCstordocid(String cstordocid) {
    this.setAttributeValue(DetailLedgerVO.CSTORDOCID, cstordocid);
  }

  public void setCtrantypeid(String ctrantypeid) {
    this.setAttributeValue(DetailLedgerVO.CTRANTYPEID, ctrantypeid);
  }

  public void setCunitid(String cunitid) {
    this.setAttributeValue(DetailLedgerVO.CUNITID, cunitid);
  }

  public void setCvendorid(String cvendorid) {
    this.setAttributeValue(DetailLedgerVO.CVENDORID, cvendorid);
  }

  public void setCworkcenterid(String cworkcenterid) {
    this.setAttributeValue(DetailLedgerVO.CWORKCENTERID, cworkcenterid);
  }

  /**
   * 设置产品类型
   * 
   * @param fproductflag
   */
  public void setFProductFlag(Integer fproductflag) {
    this.setAttributeValue(I3ItemVO.FPRODUCTFLAG, fproductflag);
  }

  public void setNcost(UFDouble ncost) {
    this.setAttributeValue(DetailLedgerVO.NCOST, ncost);
  }

  public void setNmny(UFDouble nmny) {
    this.setAttributeValue(DetailLedgerVO.NMNY, nmny);
  }

  public void setNnum(UFDouble nnum) {
    this.setAttributeValue(DetailLedgerVO.NNUM, nnum);
  }

  public void setNplanedmny(UFDouble nplanedmny) {
    this.setAttributeValue(DetailLedgerVO.NPLANEDMNY, nplanedmny);
  }

  public void setPk_org(String pk_org) {
    this.setAttributeValue(DetailLedgerVO.PK_ORG, pk_org);
  }

  /**
   * 设置主产品
   * 
   * @param primaryproductid
   */
  public void setPrimaryProductid(String primaryproductid) {
    this.setAttributeValue(I3ItemVO.CMAINPRODUCTID, primaryproductid);
  }

  public void setVproducebatch(String vproducebatch) {
    this.setAttributeValue(DetailLedgerVO.VPRODUCEBATCH, vproducebatch);
  }

  public void setVproduceordercode(String vproduceordercode) {
    this.setAttributeValue(DetailLedgerVO.VPRODUCEORDERCODE, vproduceordercode);
  }

  public void setVfree1(String vfree1) {
    this.setAttributeValue(DetailLedgerVO.VFREE1, vfree1);
  }

  public void setVfree2(String vfree2) {
    this.setAttributeValue(DetailLedgerVO.VFREE2, vfree2);
  }

  public void setVfree3(String vfree3) {
    this.setAttributeValue(DetailLedgerVO.VFREE3, vfree3);
  }

  public void setVfree4(String vfree4) {
    this.setAttributeValue(DetailLedgerVO.VFREE4, vfree4);
  }

  public void setVfree5(String vfree5) {
    this.setAttributeValue(DetailLedgerVO.VFREE5, vfree5);
  }

  public void setVfree6(String vfree6) {
    this.setAttributeValue(DetailLedgerVO.VFREE6, vfree6);
  }

  public void setVfree7(String vfree7) {
    this.setAttributeValue(DetailLedgerVO.VFREE7, vfree7);
  }

  public void setVfree8(String vfree8) {
    this.setAttributeValue(DetailLedgerVO.VFREE8, vfree8);
  }

  public void setVfree9(String vfree9) {
    this.setAttributeValue(DetailLedgerVO.VFREE9, vfree9);
  }

  public void setVfree10(String vfree10) {
    this.setAttributeValue(DetailLedgerVO.VFREE10, vfree10);
  }

  public void setCasscustid(String casscustid) {
    this.setAttributeValue(DetailLedgerVO.CASSCUSTID, casscustid);
  }

  public void setCproductorid(String cproductorid) {
    this.setAttributeValue(DetailLedgerVO.CPRODUCTORID, cproductorid);
  }

  public void setVcostfree1(String vcostfree1) {
    this.setAttributeValue(DetailLedgerVO.VCOSTFREE1, vcostfree1);
  }

  public void setVcostfree2(String vcostfree2) {
    this.setAttributeValue(DetailLedgerVO.VCOSTFREE2, vcostfree2);
  }

  public void setVcostfree3(String vcostfree3) {
    this.setAttributeValue(DetailLedgerVO.VCOSTFREE3, vcostfree3);
  }

  public void setVcostfree4(String vcostfree4) {
    this.setAttributeValue(DetailLedgerVO.VCOSTFREE4, vcostfree4);
  }

  public void setVcostfree5(String vcostfree5) {
    this.setAttributeValue(DetailLedgerVO.VCOSTFREE5, vcostfree5);
  }

  public void setVcostfree6(String vcostfree6) {
    this.setAttributeValue(DetailLedgerVO.VCOSTFREE6, vcostfree6);
  }

  public void setVcostfree7(String vcostfree7) {
    this.setAttributeValue(DetailLedgerVO.VCOSTFREE7, vcostfree7);
  }

  public void setVcostfree8(String vcostfree8) {
    this.setAttributeValue(DetailLedgerVO.VCOSTFREE8, vcostfree8);
  }

  public void setVcostfree9(String vcostfree9) {
    this.setAttributeValue(DetailLedgerVO.VCOSTFREE9, vcostfree9);
  }

  public void setVcostfree10(String vcostfree10) {
    this.setAttributeValue(DetailLedgerVO.VCOSTFREE10, vcostfree10);
  }

  public void setCcostprojectid(String ccostprojectid) {
    this.setAttributeValue(DetailLedgerVO.CCOSTPROJECTID, ccostprojectid);
  }

  public void setCcostasscustid(String ccostasscustid) {
    this.setAttributeValue(DetailLedgerVO.CCOSTASSCUSTID, ccostasscustid);
  }

  public void setCcostproductorid(String ccostproductorid) {
    this.setAttributeValue(DetailLedgerVO.CCOSTPRODUCTORID, ccostproductorid);
  }

  public void setCcostvendorid(String ccostvendorid) {
    this.setAttributeValue(DetailLedgerVO.CCOSTVENDORID, ccostvendorid);
  }
}
