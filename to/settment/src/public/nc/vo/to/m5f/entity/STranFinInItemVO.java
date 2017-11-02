package nc.vo.to.m5f.entity;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class STranFinInItemVO extends SuperVO {

//	cbill_bid
  public static final String CBILL_BID = "cbill_bid";

  // 是否可传应付
  public static final String BCANAPFLAG = "bcanapflag";

  // 是否可传存货
  public static final String BCANIAFLAG = "bcaniaflag";

  public static final String BLARGESSFLAG = "blargessflag";

  // 转财务调入主实体_主键
  public static final String BODYFK = "bodyfk";

  // 逆向征税标志
  public static final String BOPPTAXFLAG = "bopptaxflag";

  // 客户-固定属性
  public static final String CASSCUSTID = "casscustid";

  // 幅计量单位
  public static final String CASTUNITID = "castunitid";

  // 业务类型
  public static final String CBIZTYPEID = "cbiztypeid";

  // 源头单据明细主键
  public static final String CFIRSTBID = "cfirstbid";

  // 源头单据主键
  public static final String CFIRSTID = "cfirstid";

  // 物料最新版本
  public static final String CINVENTORYID = "cinventoryid";

  // 物料
  public static final String CINVENTORYVID = "cinventoryvid";

  // 结算调入子子实体
  public static final String CIPST_BBID = "cipst_bbid";

  // 结算调入子实体
  public static final String CIPST_BID = "cipst_bid";

  // 结算调入主实体
  public static final String CIPSTID = "cipstid";

  // 生产厂商-固定属性
  public static final String CPRODUCTORID = "cproductorid";

  // 利润中心最新版本
  public static final String CPROFITCENTERID = "cprofitcenterid";

  // 利润中心
  public static final String CPROFITCENTERVID = "cprofitcentervid";
  /**
   *特征码
   */
   public static final String CFFILEID="cffileid"; 

  // 项目-固定属性
  public static final String CPROJECTID = "cprojectid";

  // 报价计量单位
  public static final String CQTUNITID = "cqtunitid";

  // 结算清单子子实体
  public static final String CSLIST_BBID = "cslist_bbid";

  // 结算清单子实体
  public static final String CSLIST_BID = "cslist_bid";

  // 结算清单主实体
  public static final String CSLISTID = "cslistid";

  // 来源库存单据明细主键
  public static final String CSRCBID = "csrcbid";

  // 来源库存单据主键
  public static final String CSRCID = "csrcid";

  // 税码
  public static final String CTAXCODEID = "ctaxcodeid";

  // 主计量单位
  public static final String CUNITID = "cunitid";

  // 供应商-固定属性
  public static final String CVENDORID = "cvendorid";

  // 业务日期
  public static final String DBIZDATE = "dbizdate";

  // dr
  public static final String DR = "dr";

  // 传应付状态
  public static final String FAPSTATUSFLAG = "fapstatusflag";

  // 传存货状态
  public static final String FIASTATUSFLAG = "fiastatusflag";

  // 计税类别
  public static final String FTAXTYPEFLAG = "ftaxtypeflag";

  // 计税金额
  public static final String NCALTAXMNY = "ncaltaxmny";

  // 本币无税金额
  public static final String NMNY = "nmny";

  // 不可抵扣税额
  public static final String NNOSUBTAX = "nnosubtax";

  // 不可抵扣税率
  public static final String NNOSUBTAXRATE = "nnosubtaxrate";

  // 主数量
  public static final String NNUMBER = "nnumber";

  // 原币无税金额
  public static final String NORIGMNY = "norigmny";

  // 原币无税单价
  public static final String NORIGPRICE = "norigprice";

  // 原币报价无税单价
  public static final String NORIGQTPRICE = "norigqtprice";

  // 原币含税单价
  public static final String NORIGQTTAXPRICE = "norigqttaxprice";

  // 原币含税金额
  public static final String NORIGTAXMNY = "norigtaxmny";

  // 原币含税单价
  public static final String NORIGTAXPRICE = "norigtaxprice";

  // 主本币无税单价
  public static final String NPRICE = "nprice";

  // 报价单位数量
  public static final String NQTNUM = "nqtnum";

  // 报价本币无税单价
  public static final String NQTPRICE = "nqtprice";

  // 报价本币含税单价
  public static final String NQTTAXPRICE = "nqttaxprice";

  // 回冲应付无税金额
  public static final String NREDBACKAPMNY = "nredbackapmny";

  // 回冲应付税额
  public static final String NREDBACKAPTAX = "nredbackaptax";

  // 回冲应付价税合计
  public static final String NREDBACKAPTAXMNY = "nredbackaptaxmny";

  // 回冲入库成本
  public static final String NREDBACKINMNY = "nredbackinmny";

  // 本币税额
  public static final String NTAX = "ntax";

  // 本币价税合计
  public static final String NTAXMNY = "ntaxmny";

  // 主本币含税单价
  public static final String NTAXPRICE = "ntaxprice";

  // 税率
  public static final String NTAXRATE = "ntaxrate";

  // pk_group
  public static final String PK_GROUP = "pk_group";

  // 调入财务组织
  public static final String PK_ORG = "pk_org";

  private static final long serialVersionUID = 3816618207994696326L;

  // 时间戳
  public static final String TS = "ts";

  // 批次号
  public static final String VBATCHCODE = "vbatchcode";

  // 表体自定义项10
  public static final String VBDEF10 = "vbdef10";
  // 表体自定义项1
  public static final String VBDEF1 = "vbdef1";

  // 表体自定义项11
  public static final String VBDEF11 = "vbdef11";

  // 表体自定义项12
  public static final String VBDEF12 = "vbdef12";

  // 表体自定义项13
  public static final String VBDEF13 = "vbdef13";

  // 表体自定义项14
  public static final String VBDEF14 = "vbdef14";

  // 表体自定义项15
  public static final String VBDEF15 = "vbdef15";

  // 表体自定义项16
  public static final String VBDEF16 = "vbdef16";

  // 表体自定义项17
  public static final String VBDEF17 = "vbdef17";

  // 表体自定义项18
  public static final String VBDEF18 = "vbdef18";

  // 表体自定义项19
  public static final String VBDEF19 = "vbdef19";

  // 表体自定义项2
  public static final String VBDEF2 = "vbdef2";

  // 表体自定义项20
  public static final String VBDEF20 = "vbdef20";

  // 表体自定义项3
  public static final String VBDEF3 = "vbdef3";

  // 表体自定义项4
  public static final String VBDEF4 = "vbdef4";

  // 表体自定义项5
  public static final String VBDEF5 = "vbdef5";

  // 表体自定义项6
  public static final String VBDEF6 = "vbdef6";

  // 表体自定义项7
  public static final String VBDEF7 = "vbdef7";

  // 表体自定义项8
  public static final String VBDEF8 = "vbdef8";

  // 表体自定义项9
  public static final String VBDEF9 = "vbdef9";

  // 换算率
  public static final String VCHANGERATE = "vchangerate";

  // 结算表体自定义项20
  public static final String VDEF20 = "vdef20";

  // 源头单据号
  public static final String VFIRSTCODE = "vfirstcode";

  // 源头单据行号
  public static final String VFIRSTROWNO = "vfirstrowno";

  // 源头交易类型
  public static final String VFIRSTTRANTYPE = "vfirsttrantype";

  // 源头单据类型
  public static final String VFIRSTTYPE = "vfirsttype";

  // 自由辅助属性1
  public static final String VFREE1 = "vfree1";

  // 自由辅助属性10
  public static final String VFREE10 = "vfree10";

  // 自由辅助属性2
  public static final String VFREE2 = "vfree2";

  // 自由辅助属性3
  public static final String VFREE3 = "vfree3";

  // 自由辅助属性4
  public static final String VFREE4 = "vfree4";

  // 自由辅助属性5
  public static final String VFREE5 = "vfree5";

  // 自由辅助属性6
  public static final String VFREE6 = "vfree6";

  // 自由辅助属性7
  public static final String VFREE7 = "vfree7";

  // 自由辅助属性8
  public static final String VFREE8 = "vfree8";

  // 自由辅助属性9
  public static final String VFREE9 = "vfree9";

  // 报价换算率
  public static final String VQTUNITRATE = "vqtunitrate";

  // 来源单据号
  public static final String VSRCCODE = "vsrccode";

  // 来源单据行号
  public static final String VSRCROWNO = "vsrcrowno";

  // 来源单据类型
  public static final String VSRCTRANTYPE = "vsrctrantype";

  // 来源交易类型
  public static final String VSRCTYPE = "vsrctype";

  // 结算表体自定义项1
  public static final String VSTBDEF1 = "vstbdef1";

  // 结算表体自定义项10
  public static final String VSTBDEF10 = "vstbdef10";

  // 结算表体自定义项11
  public static final String VSTBDEF11 = "vstbdef11";

  // 结算表体自定义项12
  public static final String VSTBDEF12 = "vstbdef12";

  // 结算表体自定义项13
  public static final String VSTBDEF13 = "vstbdef13";

  // 结算表体自定义项14
  public static final String VSTBDEF14 = "vstbdef14";

  // 结算表体自定义项15
  public static final String VSTBDEF15 = "vstbdef15";

  // 结算表体自定义项16
  public static final String VSTBDEF16 = "vstbdef16";

  // 结算表体自定义项17
  public static final String VSTBDEF17 = "vstbdef17";

  // 结算表体自定义项18
  public static final String VSTBDEF18 = "vstbdef18";

  // 结算表体自定义项19
  public static final String VSTBDEF19 = "vstbdef19";

  // 结算表体自定义项2
  public static final String VSTBDEF2 = "vstbdef2";

  // 表体自定义项1
  public static final String VSTBDEF20 = "vstbdef20";

  // 结算表体自定义项3
  public static final String VSTBDEF3 = "vstbdef3";

  // 结算表体自定义项4
  public static final String VSTBDEF4 = "vstbdef4";

  // 结算表体自定义项5
  public static final String VSTBDEF5 = "vstbdef5";

  // 结算表体自定义项6
  public static final String VSTBDEF6 = "vstbdef6";

  // 结算表体自定义项7
  public static final String VSTBDEF7 = "vstbdef7";

  // 结算表体自定义项8
  public static final String VSTBDEF8 = "vstbdef8";

  // 结算表体自定义项9
  public static final String VSTBDEF9 = "vstbdef9";

  // 结算单号
  public static final String VSTCODE = "vstcode";

  // 结算明细行号
  public static final String VSTROWNO = "vstrowno";

  // 回冲暂估单价
  private UFDouble ninprice;
  
  public String getCbill_bid() {
    return (String) this.getAttributeValue(STranFinOutItemVO.CBILL_BID);
  }
  
  public void setCbill_bid(String cbill_bid) {
    this.setAttributeValue(STranFinOutItemVO.CBILL_BID, cbill_bid);
  }

  public UFBoolean getBcanapflag() {
    return (UFBoolean) this.getAttributeValue(STranFinInItemVO.BCANAPFLAG);
  }

  public UFBoolean getBcaniaflag() {
    return (UFBoolean) this.getAttributeValue(STranFinInItemVO.BCANIAFLAG);
  }

  public UFBoolean getBlargessflag() {
    return (UFBoolean) this.getAttributeValue(STranFinInItemVO.BLARGESSFLAG);
  }

  public String getBodyfk() {
    return (String) this.getAttributeValue(STranFinInItemVO.BODYFK);
  }

  public UFBoolean getBopptaxflag() {
    return (UFBoolean) this.getAttributeValue(STranFinInItemVO.BOPPTAXFLAG);
  }

  public String getCasscustid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CASSCUSTID);
  }

  public String getCastunitid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CASTUNITID);
  }

  public String getCbiztypeid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CBIZTYPEID);
  }

  public String getCfirstbid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CFIRSTBID);
  }

  public String getCfirstid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CFIRSTID);
  }

  public String getCinventoryid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CINVENTORYID);
  }

  public String getCinventoryvid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CINVENTORYVID);
  }

  public String getCipst_bbid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CIPST_BBID);
  }

  public String getCipst_bid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CIPST_BID);
  }

  public String getCipstid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CIPSTID);
  }

  public String getCproductorid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CPRODUCTORID);
  }

  public String getCprofitcenterid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CPROFITCENTERID);
  }

  public String getCprofitcentervid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CPROFITCENTERVID);
  }

  public String getCprojectid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CPROJECTID);
  }

  public String getCqtunitid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CQTUNITID);
  }

  public String getCslist_bbid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CSLIST_BBID);
  }

  public String getCslist_bid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CSLIST_BID);
  }

  public String getCslistid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CSLISTID);
  }

  public String getCsrcbid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CSRCBID);
  }

  public String getCsrcid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CSRCID);
  }

  public String getCtaxcodeid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CTAXCODEID);
  }

  public String getCunitid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CUNITID);
  }

  public String getCvendorid() {
    return (String) this.getAttributeValue(STranFinInItemVO.CVENDORID);
  }

  public UFDate getDbizdate() {
    return (UFDate) this.getAttributeValue(STranFinInItemVO.DBIZDATE);
  }

  public Integer getDr() {
    return (Integer) this.getAttributeValue(STranFinInItemVO.DR);
  }

  public Integer getFapstatusflag() {
    return (Integer) this.getAttributeValue(STranFinInItemVO.FAPSTATUSFLAG);
  }

  public Integer getFiastatusflag() {
    return (Integer) this.getAttributeValue(STranFinInItemVO.FIASTATUSFLAG);
  }

  public Integer getFtaxtypeflag() {
    return (Integer) this.getAttributeValue(STranFinInItemVO.FTAXTYPEFLAG);
  }

  @Override
  public IVOMeta getMetaData() {
    IVOMeta meta = VOMetaFactory.getInstance().getVOMeta("to.to_settlein_b");
    return meta;
  }

  public UFDouble getNcaltaxmny() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NCALTAXMNY);
  }

  public UFDouble getNinprice() {
    return this.ninprice;
  }

  public UFDouble getNmny() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NMNY);
  }

  public UFDouble getNnosubtax() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NNOSUBTAX);
  }

  public UFDouble getNnosubtaxrate() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NNOSUBTAXRATE);
  }

  public UFDouble getNnumber() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NNUMBER);
  }

  public UFDouble getNorigmny() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NORIGMNY);
  }

  public UFDouble getNorigprice() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NORIGPRICE);
  }

  public UFDouble getNorigqtprice() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NORIGQTPRICE);
  }

  public UFDouble getNorigqttaxprice() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NORIGQTTAXPRICE);
  }

  public UFDouble getNorigtaxmny() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NORIGTAXMNY);
  }

  public UFDouble getNorigtaxprice() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NORIGTAXPRICE);
  }

  public UFDouble getNprice() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NPRICE);
  }

  public UFDouble getNqtnum() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NQTNUM);
  }

  public UFDouble getNqtprice() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NQTPRICE);
  }

  public UFDouble getNqttaxprice() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NQTTAXPRICE);
  }

  public UFDouble getNredbackapmny() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NREDBACKAPMNY);
  }

  public UFDouble getNredbackaptax() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NREDBACKAPTAX);
  }

  public UFDouble getNredbackaptaxmny() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NREDBACKAPTAXMNY);
  }

  public UFDouble getNredbackinmny() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NREDBACKINMNY);
  }

  public UFDouble getNtax() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NTAX);
  }

  public UFDouble getNtaxmny() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NTAXMNY);
  }

  public UFDouble getNtaxprice() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NTAXPRICE);
  }

  public UFDouble getNtaxrate() {
    return (UFDouble) this.getAttributeValue(STranFinInItemVO.NTAXRATE);
  }

  public String getPk_org() {
    return (String) this.getAttributeValue(STranFinInItemVO.PK_ORG);
  }

  public String getPkGroup() {
    return (String) this.getAttributeValue(STranFinInItemVO.PK_GROUP);
  }

  public UFDateTime getTs() {
    return (UFDateTime) this.getAttributeValue(STranFinInItemVO.TS);
  }

  public String getVbatchcode() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBATCHCODE);
  }

  public String getVbdef10() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF10);
  }

  public String getVbdef11() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF11);
  }

  public String getVbdef12() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF12);
  }

  public String getVbdef13() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF13);
  }

  public String getVbdef14() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF14);
  }

  public String getVbdef15() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF15);
  }

  public String getVbdef16() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF16);
  }

  public String getVbdef17() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF17);
  }

  public String getVbdef18() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF18);
  }

  public String getVbdef19() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF19);
  }

  public String getVbdef2() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF2);
  }

  public String getVbdef20() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF20);
  }

  public String getVbdef3() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF3);
  }

  public String getVbdef4() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF4);
  }

  public String getVbdef5() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF5);
  }

  public String getVbdef6() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF6);
  }

  public String getVbdef7() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF7);
  }

  public String getVbdef8() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF8);
  }

  public String getVbdef9() {
    return (String) this.getAttributeValue(STranFinInItemVO.VBDEF9);
  }
  public String getVbdef1() {
	  return (String) this.getAttributeValue(STranFinInItemVO.VBDEF1);
  }

  public String getVchangerate() {
    return (String) this.getAttributeValue(STranFinInItemVO.VCHANGERATE);
  }

  public String getVdef20() {
    return (String) this.getAttributeValue(STranFinInItemVO.VDEF20);
  }

  public String getVfirstcode() {
    return (String) this.getAttributeValue(STranFinInItemVO.VFIRSTCODE);
  }

  public String getVfirstrowno() {
    return (String) this.getAttributeValue(STranFinInItemVO.VFIRSTROWNO);
  }

  public String getVfirsttrantype() {
    return (String) this.getAttributeValue(STranFinInItemVO.VFIRSTTRANTYPE);
  }

  public String getVfirsttype() {
    return (String) this.getAttributeValue(STranFinInItemVO.VFIRSTTYPE);
  }

  public String getVfree1() {
    return (String) this.getAttributeValue(STranFinInItemVO.VFREE1);
  }

  public String getVfree10() {
    return (String) this.getAttributeValue(STranFinInItemVO.VFREE10);
  }

  public String getVfree2() {
    return (String) this.getAttributeValue(STranFinInItemVO.VFREE2);
  }

  public String getVfree3() {
    return (String) this.getAttributeValue(STranFinInItemVO.VFREE3);
  }

  public String getVfree4() {
    return (String) this.getAttributeValue(STranFinInItemVO.VFREE4);
  }

  public String getVfree5() {
    return (String) this.getAttributeValue(STranFinInItemVO.VFREE5);
  }

  public String getVfree6() {
    return (String) this.getAttributeValue(STranFinInItemVO.VFREE6);
  }

  public String getVfree7() {
    return (String) this.getAttributeValue(STranFinInItemVO.VFREE7);
  }

  public String getVfree8() {
    return (String) this.getAttributeValue(STranFinInItemVO.VFREE8);
  }

  public String getVfree9() {
    return (String) this.getAttributeValue(STranFinInItemVO.VFREE9);
  }

  public String getVqtunitrate() {
    return (String) this.getAttributeValue(STranFinInItemVO.VQTUNITRATE);
  }

  public String getVsrccode() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSRCCODE);
  }

  public String getVsrcrowno() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSRCROWNO);
  }

  public String getVsrctrantype() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSRCTRANTYPE);
  }

  public String getVsrctype() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSRCTYPE);
  }

  public String getVstbdef1() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF1);
  }

  public String getVstbdef10() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF10);
  }

  public String getVstbdef11() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF11);
  }

  public String getVstbdef12() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF12);
  }

  public String getVstbdef13() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF13);
  }

  public String getVstbdef14() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF14);
  }

  public String getVstbdef15() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF15);
  }

  public String getVstbdef16() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF16);
  }

  public String getVstbdef17() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF17);
  }

  public String getVstbdef18() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF18);
  }

  public String getVstbdef19() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF19);
  }

  public String getVstbdef2() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF2);
  }

  public String getVstbdef20() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF20);
  }

  public String getVstbdef3() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF3);
  }

  public String getVstbdef4() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF4);
  }

  public String getVstbdef5() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF5);
  }

  public String getVstbdef6() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF6);
  }

  public String getVstbdef7() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF7);
  }

  public String getVstbdef8() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF8);
  }

  public String getVstbdef9() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTBDEF9);
  }

  public String getVstcode() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTCODE);
  }

  public String getVstrowno() {
    return (String) this.getAttributeValue(STranFinInItemVO.VSTROWNO);
  }

  public void setBcanapflag(UFBoolean bcanapflag) {
    this.setAttributeValue(STranFinInItemVO.BCANAPFLAG, bcanapflag);
  }

  public void setBcaniaflag(UFBoolean bcaniaflag) {
    this.setAttributeValue(STranFinInItemVO.BCANIAFLAG, bcaniaflag);
  }

  public void setBlargessflag(UFBoolean blargessflag) {
    this.setAttributeValue(STranFinInItemVO.BLARGESSFLAG, blargessflag);
  }

  public void setBodyfk(String bodyfk) {
    this.setAttributeValue(STranFinInItemVO.BODYFK, bodyfk);
  }

  public void setBopptaxflag(UFBoolean bopptaxflag) {
    this.setAttributeValue(STranFinInItemVO.BOPPTAXFLAG, bopptaxflag);
  }

  public void setCasscustid(String casscustid) {
    this.setAttributeValue(STranFinInItemVO.CASSCUSTID, casscustid);
  }

  public void setCastunitid(String castunitid) {
    this.setAttributeValue(STranFinInItemVO.CASTUNITID, castunitid);
  }

  public void setCbiztypeid(String cbiztypeid) {
    this.setAttributeValue(STranFinInItemVO.CBIZTYPEID, cbiztypeid);
  }

  public void setCfirstbid(String cfirstbid) {
    this.setAttributeValue(STranFinInItemVO.CFIRSTBID, cfirstbid);
  }

  public void setCfirstid(String cfirstid) {
    this.setAttributeValue(STranFinInItemVO.CFIRSTID, cfirstid);
  }

  public void setCinventoryid(String cinventoryid) {
    this.setAttributeValue(STranFinInItemVO.CINVENTORYID, cinventoryid);
  }

  public void setCinventoryvid(String cinventoryvid) {
    this.setAttributeValue(STranFinInItemVO.CINVENTORYVID, cinventoryvid);
  }

  public void setCipst_bbid(String cipst_bbid) {
    this.setAttributeValue(STranFinInItemVO.CIPST_BBID, cipst_bbid);
  }

  public void setCipst_bid(String cipst_bid) {
    this.setAttributeValue(STranFinInItemVO.CIPST_BID, cipst_bid);
  }

  public void setCipstid(String cipstid) {
    this.setAttributeValue(STranFinInItemVO.CIPSTID, cipstid);
  }

  public void setCproductorid(String cproductorid) {
    this.setAttributeValue(STranFinInItemVO.CPRODUCTORID, cproductorid);
  }

  public void setCprofitcenterid(String cprofitcenterid) {
    this.setAttributeValue(STranFinInItemVO.CPROFITCENTERID, cprofitcenterid);
  }

  public void setCprofitcentervid(String cprofitcentervid) {
    this.setAttributeValue(STranFinInItemVO.CPROFITCENTERVID, cprofitcentervid);
  }

  public void setCprojectid(String cprojectid) {
    this.setAttributeValue(STranFinInItemVO.CPROJECTID, cprojectid);
  }

  public void setCqtunitid(String cqtunitid) {
    this.setAttributeValue(STranFinInItemVO.CQTUNITID, cqtunitid);
  }

  public void setCslist_bbid(String cslist_bbid) {
    this.setAttributeValue(STranFinInItemVO.CSLIST_BBID, cslist_bbid);
  }

  public void setCslist_bid(String cslist_bid) {
    this.setAttributeValue(STranFinInItemVO.CSLIST_BID, cslist_bid);
  }

  public void setCslistid(String cslistid) {
    this.setAttributeValue(STranFinInItemVO.CSLISTID, cslistid);
  }

  public void setCsrcbid(String csrcbid) {
    this.setAttributeValue(STranFinInItemVO.CSRCBID, csrcbid);
  }

  public void setCsrcid(String csrcid) {
    this.setAttributeValue(STranFinInItemVO.CSRCID, csrcid);
  }

  public void setCtaxcodeid(String ctaxcodeid) {
    this.setAttributeValue(STranFinInItemVO.CTAXCODEID, ctaxcodeid);
  }

  public void setCunitid(String cunitid) {
    this.setAttributeValue(STranFinInItemVO.CUNITID, cunitid);
  }

  public void setCvendorid(String cvendorid) {
    this.setAttributeValue(STranFinInItemVO.CVENDORID, cvendorid);
  }

  public void setDbizdate(UFDate dbizdate) {
    this.setAttributeValue(STranFinInItemVO.DBIZDATE, dbizdate);
  }

  public void setDr(Integer dr) {
    this.setAttributeValue(STranFinInItemVO.DR, dr);
  }

  public void setFapstatusflag(Integer fapstatusflag) {
    this.setAttributeValue(STranFinInItemVO.FAPSTATUSFLAG, fapstatusflag);
  }

  public void setFiastatusflag(Integer fiastatusflag) {
    this.setAttributeValue(STranFinInItemVO.FIASTATUSFLAG, fiastatusflag);
  }

  public void setFtaxtypeflag(Integer ftaxtypeflag) {
    this.setAttributeValue(STranFinInItemVO.FTAXTYPEFLAG, ftaxtypeflag);
  }

  public void setNcaltaxmny(UFDouble ncaltaxmny) {
    this.setAttributeValue(STranFinInItemVO.NCALTAXMNY, ncaltaxmny);
  }

  public void setNinprice(UFDouble ninprice) {
    this.ninprice = ninprice;
  }

  public void setNmny(UFDouble nmny) {
    this.setAttributeValue(STranFinInItemVO.NMNY, nmny);
  }

  public void setNnosubtax(UFDouble nnosubtax) {
    this.setAttributeValue(STranFinInItemVO.NNOSUBTAX, nnosubtax);
  }

  public void setNnosubtaxrate(UFDouble nnosubtaxrate) {
    this.setAttributeValue(STranFinInItemVO.NNOSUBTAXRATE, nnosubtaxrate);
  }

  public void setNnumber(UFDouble nnumber) {
    this.setAttributeValue(STranFinInItemVO.NNUMBER, nnumber);
  }

  public void setNorigmny(UFDouble norigmny) {
    this.setAttributeValue(STranFinInItemVO.NORIGMNY, norigmny);
  }

  public void setNorigprice(UFDouble norigprice) {
    this.setAttributeValue(STranFinInItemVO.NORIGPRICE, norigprice);
  }

  public void setNorigqtprice(UFDouble norigqtprice) {
    this.setAttributeValue(STranFinInItemVO.NORIGQTPRICE, norigqtprice);
  }

  public void setNorigqttaxprice(UFDouble norigqttaxprice) {
    this.setAttributeValue(STranFinInItemVO.NORIGQTTAXPRICE, norigqttaxprice);
  }

  public void setNorigtaxmny(UFDouble norigtaxmny) {
    this.setAttributeValue(STranFinInItemVO.NORIGTAXMNY, norigtaxmny);
  }

  public void setNorigtaxprice(UFDouble norigtaxprice) {
    this.setAttributeValue(STranFinInItemVO.NORIGTAXPRICE, norigtaxprice);
  }

  public void setNprice(UFDouble nprice) {
    this.setAttributeValue(STranFinInItemVO.NPRICE, nprice);
  }

  public void setNqtnum(UFDouble nqtnum) {
    this.setAttributeValue(STranFinInItemVO.NQTNUM, nqtnum);
  }

  public void setNqtprice(UFDouble nqtprice) {
    this.setAttributeValue(STranFinInItemVO.NQTPRICE, nqtprice);
  }

  public void setNqttaxprice(UFDouble nqttaxprice) {
    this.setAttributeValue(STranFinInItemVO.NQTTAXPRICE, nqttaxprice);
  }

  public void setNredbackapmny(UFDouble nredbackapmny) {
    this.setAttributeValue(STranFinInItemVO.NREDBACKAPMNY, nredbackapmny);
  }

  public void setNredbackaptax(UFDouble nredbackaptax) {
    this.setAttributeValue(STranFinInItemVO.NREDBACKAPTAX, nredbackaptax);
  }

  public void setNredbackaptaxmny(UFDouble nredbackaptaxmny) {
    this.setAttributeValue(STranFinInItemVO.NREDBACKAPTAXMNY, nredbackaptaxmny);
  }

  public void setNredbackinmny(UFDouble nredbackinmny) {
    this.setAttributeValue(STranFinInItemVO.NREDBACKINMNY, nredbackinmny);
  }

  public void setNtax(UFDouble ntax) {
    this.setAttributeValue(STranFinInItemVO.NTAX, ntax);
  }

  public void setNtaxmny(UFDouble ntaxmny) {
    this.setAttributeValue(STranFinInItemVO.NTAXMNY, ntaxmny);
  }

  public void setNtaxprice(UFDouble ntaxprice) {
    this.setAttributeValue(STranFinInItemVO.NTAXPRICE, ntaxprice);
  }

  public void setNtaxrate(UFDouble ntaxrate) {
    this.setAttributeValue(STranFinInItemVO.NTAXRATE, ntaxrate);
  }

  public void setPk_org(String pk_org) {
    this.setAttributeValue(STranFinInItemVO.PK_ORG, pk_org);
  }

  public void setPkGroup(String pk_group) {
    this.setAttributeValue(STranFinInItemVO.PK_GROUP, pk_group);
  }

  public void setTs(UFDateTime ts) {
    this.setAttributeValue(STranFinInItemVO.TS, ts);
  }

  public void setVbatchcode(String vbatchcode) {
    this.setAttributeValue(STranFinInItemVO.VBATCHCODE, vbatchcode);
  }

  public void setVbdef10(String vbdef10) {
    this.setAttributeValue(STranFinInItemVO.VBDEF10, vbdef10);
  }

  public void setVbdef11(String vbdef11) {
    this.setAttributeValue(STranFinInItemVO.VBDEF11, vbdef11);
  }

  public void setVbdef12(String vbdef12) {
    this.setAttributeValue(STranFinInItemVO.VBDEF12, vbdef12);
  }

  public void setVbdef13(String vbdef13) {
    this.setAttributeValue(STranFinInItemVO.VBDEF13, vbdef13);
  }

  public void setVbdef14(String vbdef14) {
    this.setAttributeValue(STranFinInItemVO.VBDEF14, vbdef14);
  }

  public void setVbdef15(String vbdef15) {
    this.setAttributeValue(STranFinInItemVO.VBDEF15, vbdef15);
  }

  public void setVbdef16(String vbdef16) {
    this.setAttributeValue(STranFinInItemVO.VBDEF16, vbdef16);
  }

  public void setVbdef17(String vbdef17) {
    this.setAttributeValue(STranFinInItemVO.VBDEF17, vbdef17);
  }

  public void setVbdef18(String vbdef18) {
    this.setAttributeValue(STranFinInItemVO.VBDEF18, vbdef18);
  }

  public void setVbdef19(String vbdef19) {
    this.setAttributeValue(STranFinInItemVO.VBDEF19, vbdef19);
  }

  public void setVbdef2(String vbdef2) {
    this.setAttributeValue(STranFinInItemVO.VBDEF2, vbdef2);
  }

  public void setVbdef20(String vbdef20) {
    this.setAttributeValue(STranFinInItemVO.VBDEF20, vbdef20);
  }

  public void setVbdef3(String vbdef3) {
    this.setAttributeValue(STranFinInItemVO.VBDEF3, vbdef3);
  }

  public void setVbdef4(String vbdef4) {
    this.setAttributeValue(STranFinInItemVO.VBDEF4, vbdef4);
  }

  public void setVbdef5(String vbdef5) {
    this.setAttributeValue(STranFinInItemVO.VBDEF5, vbdef5);
  }

  public void setVbdef6(String vbdef6) {
    this.setAttributeValue(STranFinInItemVO.VBDEF6, vbdef6);
  }

  public void setVbdef7(String vbdef7) {
    this.setAttributeValue(STranFinInItemVO.VBDEF7, vbdef7);
  }

  public void setVbdef8(String vbdef8) {
    this.setAttributeValue(STranFinInItemVO.VBDEF8, vbdef8);
  }

  public void setVbdef9(String vbdef9) {
    this.setAttributeValue(STranFinInItemVO.VBDEF9, vbdef9);
  }
  public void setVbdef1(String vbdef1) {
	  this.setAttributeValue(STranFinInItemVO.VBDEF1, vbdef1);
  }

  public void setVchangerate(String vchangerate) {
    this.setAttributeValue(STranFinInItemVO.VCHANGERATE, vchangerate);
  }

  public void setVdef20(String vdef20) {
    this.setAttributeValue(STranFinInItemVO.VDEF20, vdef20);
  }

  public void setVfirstcode(String vfirstcode) {
    this.setAttributeValue(STranFinInItemVO.VFIRSTCODE, vfirstcode);
  }

  public void setVfirstrowno(String vfirstrowno) {
    this.setAttributeValue(STranFinInItemVO.VFIRSTROWNO, vfirstrowno);
  }

  public void setVfirsttrantype(String vfirsttrantype) {
    this.setAttributeValue(STranFinInItemVO.VFIRSTTRANTYPE, vfirsttrantype);
  }

  public void setVfirsttype(String vfirsttype) {
    this.setAttributeValue(STranFinInItemVO.VFIRSTTYPE, vfirsttype);
  }

  public void setVfree1(String vfree1) {
    this.setAttributeValue(STranFinInItemVO.VFREE1, vfree1);
  }

  public void setVfree10(String vfree10) {
    this.setAttributeValue(STranFinInItemVO.VFREE10, vfree10);
  }

  public void setVfree2(String vfree2) {
    this.setAttributeValue(STranFinInItemVO.VFREE2, vfree2);
  }

  public void setVfree3(String vfree3) {
    this.setAttributeValue(STranFinInItemVO.VFREE3, vfree3);
  }

  public void setVfree4(String vfree4) {
    this.setAttributeValue(STranFinInItemVO.VFREE4, vfree4);
  }

  public void setVfree5(String vfree5) {
    this.setAttributeValue(STranFinInItemVO.VFREE5, vfree5);
  }

  public void setVfree6(String vfree6) {
    this.setAttributeValue(STranFinInItemVO.VFREE6, vfree6);
  }

  public void setVfree7(String vfree7) {
    this.setAttributeValue(STranFinInItemVO.VFREE7, vfree7);
  }

  public void setVfree8(String vfree8) {
    this.setAttributeValue(STranFinInItemVO.VFREE8, vfree8);
  }

  public void setVfree9(String vfree9) {
    this.setAttributeValue(STranFinInItemVO.VFREE9, vfree9);
  }

  public void setVqtunitrate(String vqtunitrate) {
    this.setAttributeValue(STranFinInItemVO.VQTUNITRATE, vqtunitrate);
  }

  public void setVsrccode(String vsrccode) {
    this.setAttributeValue(STranFinInItemVO.VSRCCODE, vsrccode);
  }

  public void setVsrcrowno(String vsrcrowno) {
    this.setAttributeValue(STranFinInItemVO.VSRCROWNO, vsrcrowno);
  }

  public void setVsrctrantype(String vsrctrantype) {
    this.setAttributeValue(STranFinInItemVO.VSRCTRANTYPE, vsrctrantype);
  }

  public void setVsrctype(String vsrctype) {
    this.setAttributeValue(STranFinInItemVO.VSRCTYPE, vsrctype);
  }

  public void setVstbdef1(String vstbdef1) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF1, vstbdef1);
  }

  public void setVstbdef10(String vstbdef10) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF10, vstbdef10);
  }

  public void setVstbdef11(String vstbdef11) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF11, vstbdef11);
  }

  public void setVstbdef12(String vstbdef12) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF12, vstbdef12);
  }

  public void setVstbdef13(String vstbdef13) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF13, vstbdef13);
  }

  public void setVstbdef14(String vstbdef14) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF14, vstbdef14);
  }

  public void setVstbdef15(String vstbdef15) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF15, vstbdef15);
  }

  public void setVstbdef16(String vstbdef16) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF16, vstbdef16);
  }

  public void setVstbdef17(String vstbdef17) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF17, vstbdef17);
  }

  public void setVstbdef18(String vstbdef18) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF18, vstbdef18);
  }

  public void setVstbdef19(String vstbdef19) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF19, vstbdef19);
  }

  public void setVstbdef2(String vstbdef2) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF2, vstbdef2);
  }

  public void setVstbdef20(String vstbdef20) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF20, vstbdef20);
  }

  public void setVstbdef3(String vstbdef3) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF3, vstbdef3);
  }

  public void setVstbdef4(String vstbdef4) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF4, vstbdef4);
  }

  public void setVstbdef5(String vstbdef5) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF5, vstbdef5);
  }

  public void setVstbdef6(String vstbdef6) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF6, vstbdef6);
  }

  public void setVstbdef7(String vstbdef7) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF7, vstbdef7);
  }

  public void setVstbdef8(String vstbdef8) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF8, vstbdef8);
  }

  public void setVstbdef9(String vstbdef9) {
    this.setAttributeValue(STranFinInItemVO.VSTBDEF9, vstbdef9);
  }

  public void setVstcode(String vstcode) {
    this.setAttributeValue(STranFinInItemVO.VSTCODE, vstcode);
  }

  public void setVstrowno(String vstrowno) {
    this.setAttributeValue(STranFinInItemVO.VSTROWNO, vstrowno);
  }
  public void setCffileid(String cffileid) {
	    this.setAttributeValue(STranFinInItemVO.CFFILEID, cffileid);
	  }
public String getCffileid() {
	    return (String) this.getAttributeValue(STranFinInItemVO.CFFILEID);
}

}
