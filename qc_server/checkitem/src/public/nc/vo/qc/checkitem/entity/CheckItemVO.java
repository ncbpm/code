package nc.vo.qc.checkitem.entity;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * ������ĿVO
 * 
 * @since 6.0
 * @version 2010-12-15 ����01:08:45
 * @author chendb
 */
public class CheckItemVO extends SuperVO {
	
	
    private java.lang.String vbdef1;
    private java.lang.String vbdef2;
    private java.lang.String vbdef3;
    private java.lang.String vbdef4;
    private java.lang.String vbdef5;
    private java.lang.String vbdef6;
    private java.lang.String vbdef7;
    private java.lang.String vbdef8;
    private java.lang.String vbdef9;
    private java.lang.String vbdef10;
    private java.lang.String vbdef11;
    private java.lang.String vbdef12;
    private java.lang.String vbdef13;
    private java.lang.String vbdef14;
    private java.lang.String vbdef15;
    private java.lang.String vbdef16;
    private java.lang.String vbdef17;
    private java.lang.String vbdef18;
    private java.lang.String vbdef19;
    private java.lang.String vbdef20;
	
    public static final String VBDEF1 = "vbdef1";
    public static final String VBDEF2 = "vbdef2";
    public static final String VBDEF3 = "vbdef3";
    public static final String VBDEF4 = "vbdef4";
    public static final String VBDEF5 = "vbdef5";
    public static final String VBDEF6 = "vbdef6";
    public static final String VBDEF7 = "vbdef7";
    public static final String VBDEF8 = "vbdef8";
    public static final String VBDEF9 = "vbdef9";
    public static final String VBDEF10 = "vbdef10";
    public static final String VBDEF11 = "vbdef11";
    public static final String VBDEF12 = "vbdef12";
    public static final String VBDEF13 = "vbdef13";
    public static final String VBDEF14 = "vbdef14";
    public static final String VBDEF15 = "vbdef15";
    public static final String VBDEF16 = "vbdef16";
    public static final String VBDEF17 = "vbdef17";
    public static final String VBDEF18 = "vbdef18";
    public static final String VBDEF19 = "vbdef19";
    public static final String VBDEF20 = "vbdef20";
  /** �������� */
  public static final String ANALYMETHOD = "analymethod";

  /** �����ż���Ŀ */
  public static final String BQPFLAG = "bqpflag";

  /** ���� */
  public static final String CFEATURE = "cfeature";

  /** ������ */
  public static final String CFEATURECLASS = "cfeatureclass";

  /** dr */
  public static final String DR = "dr";

  /** ����ֵ���� */
  public static final String ICHECKTYPE = "ichecktype";

  /** ����ָ������ */
  public static final String IQCINDEXPRFER = "iqcindexprfer";

  /** �ϼ�������Ŀ */
  public static final String PARENTCHECKITEM = "parentcheckitem";

  /** ������� */
  public static final String PK_CHECKBASIS = "pk_checkbasis";

  /** ������Ŀ */
  public static final String PK_CHECKITEM = "pk_checkitem";

  /** ��ⷽ�� */
  public static final String PK_CHECKMETHOD = "pk_checkmethod";

  /** ���� */
  public static final String PK_GROUP = "pk_group";

  /** ����ֵ��λ */
  public static final String PK_MEASDOC = "pk_measdoc";

  /** �ʼ����� */
  public static final String PK_ORG = "pk_org";

  /** �ⲿ������ */
  public static final String PK_OUTSUPPLIER = "pk_outsupplier";

  /** ������� */
  public static final String PK_WORKCENTER = "pk_workcenter";

  /** ts */
  public static final String TS = "ts";

  /** ������Ŀ���� */
  public static final String VCHECKITEMCODE = "vcheckitemcode";

  /** ������Ŀ���� */
  public static final String VCHECKITEMNAME = "vcheckitemname";

  /** ������Ŀ���� */
  public static final String VCHECKITEMNAME2 = "vcheckitemname2";

  /** ������Ŀ���� */
  public static final String VCHECKITEMNAME3 = "vcheckitemname3";

  /** ��ע */
  public static final String VMEMO = "vmemo";

  private static final long serialVersionUID = 8495379441135364828L;

  /**
   * �ڵ�����
   * 
   * @return
   */
  public static String getCodeName() {
    return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
        "UC000-0002807")/* @res "������Ŀ" */;
  }

  /**
   * ������Ŀ-���Žڵ�
   * 
   * @return
   */
  public static String getGroupCodeNode() {
    return "C0101015";
  }

  /**
   * ������Ŀ-�ʼ����Ľڵ�
   * 
   * @return
   */
  public static String getOrgCodeNode() {
    return "C0101020";
  }

  /** �������� getter ���� */
  public Integer getAnalymethod() {
    return (Integer) this.getAttributeValue(CheckItemVO.ANALYMETHOD);
  }

  /** �����ż���Ŀ getter ���� */
  public UFBoolean getBqpflag() {
    return (UFBoolean) this.getAttributeValue(CheckItemVO.BQPFLAG);
  }

  /** ���� getter ���� */
  public String getCfeature() {
    return (String) this.getAttributeValue(CheckItemVO.CFEATURE);
  }

  /** ������getter ���� */
  public String getCfeatureclass() {
    return (String) this.getAttributeValue(CheckItemVO.CFEATURECLASS);
  }

  /** dr getter ���� */
  public Integer getDr() {
    return (Integer) this.getAttributeValue(CheckItemVO.DR);
  }

  /** ����ֵ���� getter ���� */
  public Integer getIchecktype() {
    return (Integer) this.getAttributeValue(CheckItemVO.ICHECKTYPE);
  }

  /** ����ָ������ getter ���� */
  public Integer getIqcindexprfer() {
    return (Integer) this.getAttributeValue(CheckItemVO.IQCINDEXPRFER);
  }

  @Override
  public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("qc.qc_checkitem");
  }

  /** �ϼ�������Ŀ getter ���� */
  public String getParentcheckitem() {
    return (String) this.getAttributeValue(CheckItemVO.PARENTCHECKITEM);
  }

  /** ������� getter ���� */
  public String getPk_checkbasis() {
    return (String) this.getAttributeValue(CheckItemVO.PK_CHECKBASIS);
  }

  /** ������Ŀ getter ���� */
  public String getPk_checkitem() {
    return (String) this.getAttributeValue(CheckItemVO.PK_CHECKITEM);
  }

  /** ��ⷽ�� getter ���� */
  public String getPk_checkmethod() {
    return (String) this.getAttributeValue(CheckItemVO.PK_CHECKMETHOD);
  }

  /** ���� getter ���� */
  public String getPk_group() {
    return (String) this.getAttributeValue(CheckItemVO.PK_GROUP);
  }

  /** ����ֵ��λ getter ���� */
  public String getPk_measdoc() {
    return (String) this.getAttributeValue(CheckItemVO.PK_MEASDOC);
  }

  /** �ʼ����� getter ���� */
  public String getPk_org() {
    return (String) this.getAttributeValue(CheckItemVO.PK_ORG);
  }

  /** �ⲿ������ getter ���� */
  public String getPk_outsupplier() {
    return (String) this.getAttributeValue(CheckItemVO.PK_OUTSUPPLIER);
  }

  /** ������� getter ���� */
  public String getPk_workcenter() {
    return (String) this.getAttributeValue(CheckItemVO.PK_WORKCENTER);
  }

  /** ts getter ���� */
  public UFDateTime getTs() {
    return (UFDateTime) this.getAttributeValue(CheckItemVO.TS);
  }

  /** ������Ŀ���� getter ���� */
  public String getVcheckitemcode() {
    return (String) this.getAttributeValue(CheckItemVO.VCHECKITEMCODE);
  }

  /** ������Ŀ���� getter ���� */
  public String getVcheckitemname() {
    return (String) this.getAttributeValue(CheckItemVO.VCHECKITEMNAME);
  }

  /** ������Ŀ���� getter ���� */
  public String getVcheckitemname2() {
    return (String) this.getAttributeValue(CheckItemVO.VCHECKITEMNAME2);
  }

  /** ������Ŀ���� getter ���� */
  public String getVcheckitemname3() {
    return (String) this.getAttributeValue(CheckItemVO.VCHECKITEMNAME3);
  }

  /** ��ע getter ���� */
  public String getVmemo() {
    return (String) this.getAttributeValue(CheckItemVO.VMEMO);
  }

  /** �������� setter ���� */
  public void setAnalymethod(Integer analymethod) {
    this.setAttributeValue(CheckItemVO.ANALYMETHOD, analymethod);
  }

  /** ������ setter ���� */
  public void setAnalymethod(String cfeatureclass) {
    this.setAttributeValue(CheckItemVO.CFEATURECLASS, cfeatureclass);
  }

  /** �����ż���Ŀ setter ���� */
  public void setBqpflag(UFBoolean bqpflag) {
    this.setAttributeValue(CheckItemVO.BQPFLAG, bqpflag);
  }

  /** dr setter ���� */
  public void setDr(Integer dr) {
    this.setAttributeValue(CheckItemVO.DR, dr);
  }

  /** ���� setter ���� */
  public void setFeature(String cfeature) {
    this.setAttributeValue(CheckItemVO.CFEATURE, cfeature);
  }

  /** ����ֵ���� setter ���� */
  public void setIchecktype(Integer ichecktype) {
    this.setAttributeValue(CheckItemVO.ICHECKTYPE, ichecktype);
  }

  /** ����ָ������ setter ���� */
  public void setIqcindexprfer(Integer iqcindexprfer) {
    this.setAttributeValue(CheckItemVO.IQCINDEXPRFER, iqcindexprfer);
  }

  /** �ϼ�������Ŀ setter ���� */
  public void setParentcheckitem(String parentcheckitem) {
    this.setAttributeValue(CheckItemVO.PARENTCHECKITEM, parentcheckitem);
  }

  /** ������� setter ���� */
  public void setPk_checkbasis(String pk_checkbasis) {
    this.setAttributeValue(CheckItemVO.PK_CHECKBASIS, pk_checkbasis);
  }

  /** ������Ŀ setter ���� */
  public void setPk_checkitem(String pk_checkitem) {
    this.setAttributeValue(CheckItemVO.PK_CHECKITEM, pk_checkitem);
  }

  /** ��ⷽ�� setter ���� */
  public void setPk_checkmethod(String pk_checkmethod) {
    this.setAttributeValue(CheckItemVO.PK_CHECKMETHOD, pk_checkmethod);
  }

  /** ���� setter ���� */
  public void setPk_group(String pk_group) {
    this.setAttributeValue(CheckItemVO.PK_GROUP, pk_group);
  }

  /** ����ֵ��λ setter ���� */
  public void setPk_measdoc(String pk_measdoc) {
    this.setAttributeValue(CheckItemVO.PK_MEASDOC, pk_measdoc);
  }

  /** �ʼ����� setter ���� */
  public void setPk_org(String pk_org) {
    this.setAttributeValue(CheckItemVO.PK_ORG, pk_org);
  }

  /** �ⲿ������ setter ���� */
  public void setPk_outsupplier(String pk_outsupplier) {
    this.setAttributeValue(CheckItemVO.PK_OUTSUPPLIER, pk_outsupplier);
  }

  /** ������� setter ���� */
  public void setPk_workcenter(String pk_workcenter) {
    this.setAttributeValue(CheckItemVO.PK_WORKCENTER, pk_workcenter);
  }

  /** ts setter ���� */
  public void setTs(UFDateTime ts) {
    this.setAttributeValue(CheckItemVO.TS, ts);
  }

  /** ������Ŀ���� setter ���� */
  public void setVcheckitemcode(String vcheckitemcode) {
    this.setAttributeValue(CheckItemVO.VCHECKITEMCODE, vcheckitemcode);
  }

  /** ������Ŀ���� setter ���� */
  public void setVcheckitemname(String vcheckitemname) {
    this.setAttributeValue(CheckItemVO.VCHECKITEMNAME, vcheckitemname);
  }

  /** ������Ŀ���� setter ���� */
  public void setVcheckitemname2(String vcheckitemname2) {
    this.setAttributeValue(CheckItemVO.VCHECKITEMNAME2, vcheckitemname2);
  }

  /** ������Ŀ���� setter ���� */
  public void setVcheckitemname3(String vcheckitemname3) {
    this.setAttributeValue(CheckItemVO.VCHECKITEMNAME3, vcheckitemname3);
  }

  /** ��ע setter ���� */
  public void setVmemo(String vmemo) {
    this.setAttributeValue(CheckItemVO.VMEMO, vmemo);
  }

public java.lang.String getVbdef1() {
	return vbdef1;
}

public void setVbdef1(java.lang.String vbdef1) {
	this.vbdef1 = vbdef1;
}

public java.lang.String getVbdef2() {
	return vbdef2;
}

public void setVbdef2(java.lang.String vbdef2) {
	this.vbdef2 = vbdef2;
}

public java.lang.String getVbdef3() {
	return vbdef3;
}

public void setVbdef3(java.lang.String vbdef3) {
	this.vbdef3 = vbdef3;
}

public java.lang.String getVbdef4() {
	return vbdef4;
}

public void setVbdef4(java.lang.String vbdef4) {
	this.vbdef4 = vbdef4;
}

public java.lang.String getVbdef5() {
	return vbdef5;
}

public void setVbdef5(java.lang.String vbdef5) {
	this.vbdef5 = vbdef5;
}

public java.lang.String getVbdef6() {
	return vbdef6;
}

public void setVbdef6(java.lang.String vbdef6) {
	this.vbdef6 = vbdef6;
}

public java.lang.String getVbdef7() {
	return vbdef7;
}

public void setVbdef7(java.lang.String vbdef7) {
	this.vbdef7 = vbdef7;
}

public java.lang.String getVbdef8() {
	return vbdef8;
}

public void setVbdef8(java.lang.String vbdef8) {
	this.vbdef8 = vbdef8;
}

public java.lang.String getVbdef9() {
	return vbdef9;
}

public void setVbdef9(java.lang.String vbdef9) {
	this.vbdef9 = vbdef9;
}

public java.lang.String getVbdef10() {
	return vbdef10;
}

public void setVbdef10(java.lang.String vbdef10) {
	this.vbdef10 = vbdef10;
}

public java.lang.String getVbdef11() {
	return vbdef11;
}

public void setVbdef11(java.lang.String vbdef11) {
	this.vbdef11 = vbdef11;
}

public java.lang.String getVbdef12() {
	return vbdef12;
}

public void setVbdef12(java.lang.String vbdef12) {
	this.vbdef12 = vbdef12;
}

public java.lang.String getVbdef13() {
	return vbdef13;
}

public void setVbdef13(java.lang.String vbdef13) {
	this.vbdef13 = vbdef13;
}

public java.lang.String getVbdef14() {
	return vbdef14;
}

public void setVbdef14(java.lang.String vbdef14) {
	this.vbdef14 = vbdef14;
}

public java.lang.String getVbdef15() {
	return vbdef15;
}

public void setVbdef15(java.lang.String vbdef15) {
	this.vbdef15 = vbdef15;
}

public java.lang.String getVbdef16() {
	return vbdef16;
}

public void setVbdef16(java.lang.String vbdef16) {
	this.vbdef16 = vbdef16;
}

public java.lang.String getVbdef17() {
	return vbdef17;
}

public void setVbdef17(java.lang.String vbdef17) {
	this.vbdef17 = vbdef17;
}

public java.lang.String getVbdef18() {
	return vbdef18;
}

public void setVbdef18(java.lang.String vbdef18) {
	this.vbdef18 = vbdef18;
}

public java.lang.String getVbdef19() {
	return vbdef19;
}

public void setVbdef19(java.lang.String vbdef19) {
	this.vbdef19 = vbdef19;
}

public java.lang.String getVbdef20() {
	return vbdef20;
}

public void setVbdef20(java.lang.String vbdef20) {
	this.vbdef20 = vbdef20;
}
  
  
}
