package nc.vo.ic.fivemetals;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 *   �˴�������������Ϣ
 * </p>
 *  ��������:2017-6-14
 * @author 
 * @version NCPrj ??
 */
public class FiveMetalsHVO extends nc.vo.pub.SuperVO{
	
    private java.lang.String pk_group;
    private java.lang.String pk_org;
    private java.lang.String pk_org_v;
    private java.lang.String pk_fivemetals_h;
    private java.lang.String code;
    private java.lang.String name;
    private nc.vo.pub.lang.UFDateTime maketime;
    private nc.vo.pub.lang.UFDateTime lastmaketime;
    private java.lang.String vcardno;
    private java.lang.String cperiod;
    private java.lang.String vproject;
    private java.lang.String vdepartment;
    private java.lang.String vremark;
    private nc.vo.ic.fivemetals.CardTypeEnum vcardtype;
    private java.lang.String def1;
    private java.lang.String def2;
    private java.lang.String def3;
    private nc.vo.pub.lang.UFDouble def4;
    private nc.vo.pub.lang.UFDouble def5;
    private nc.vo.pub.lang.UFDouble def6;
    private nc.vo.pub.lang.UFBoolean def7;
    private nc.vo.pub.lang.UFBoolean def8;
    private nc.vo.pub.lang.UFDate def9;
    private nc.vo.pub.lang.UFDate def10;
    private nc.vo.pub.lang.UFDate dbilldate;
    private java.lang.String pk_billtype;
    private java.lang.Integer dr = 0;
    private nc.vo.pub.lang.UFDateTime ts;    
	
    private nc.vo.ic.fivemetals.FiveMetalsBVO[] pk_fivemetals_b;
	
    public static final String PK_GROUP = "pk_group";
    public static final String PK_ORG = "pk_org";
    public static final String PK_ORG_V = "pk_org_v";
    public static final String PK_FIVEMETALS_H = "pk_fivemetals_h";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String MAKETIME = "maketime";
    public static final String LASTMAKETIME = "lastmaketime";
    public static final String VCARDNO = "vcardno";
    public static final String CPERIOD = "cperiod";
    public static final String VPROJECT = "vproject";
    public static final String VDEPARTMENT = "vdepartment";
    public static final String VREMARK = "vremark";
    public static final String VCARDTYPE = "vcardtype";
    public static final String DEF1 = "def1";
    public static final String DEF2 = "def2";
    public static final String DEF3 = "def3";
    public static final String DEF4 = "def4";
    public static final String DEF5 = "def5";
    public static final String DEF6 = "def6";
    public static final String DEF7 = "def7";
    public static final String DEF8 = "def8";
    public static final String DEF9 = "def9";
    public static final String DEF10 = "def10";
    public static final String DBILLDATE = "dbilldate";
    public static final String PK_BILLTYPE = "pk_billtype";

	/**
	 * ���� pk_group��Getter����.������������
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group () {
		return pk_group;
	}   
	/**
	 * ����pk_group��Setter����.������������
	 * ��������:2017-6-14
	 * @param newPk_group java.lang.String
	 */
	public void setPk_group (java.lang.String newPk_group ) {
	 	this.pk_group = newPk_group;
	} 	 
	
	/**
	 * ���� pk_org��Getter����.����������֯
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org () {
		return pk_org;
	}   
	/**
	 * ����pk_org��Setter����.����������֯
	 * ��������:2017-6-14
	 * @param newPk_org java.lang.String
	 */
	public void setPk_org (java.lang.String newPk_org ) {
	 	this.pk_org = newPk_org;
	} 	 
	
	/**
	 * ���� pk_org_v��Getter����.����������֯�汾
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org_v () {
		return pk_org_v;
	}   
	/**
	 * ����pk_org_v��Setter����.����������֯�汾
	 * ��������:2017-6-14
	 * @param newPk_org_v java.lang.String
	 */
	public void setPk_org_v (java.lang.String newPk_org_v ) {
	 	this.pk_org_v = newPk_org_v;
	} 	 
	
	/**
	 * ���� pk_fivemetals_h��Getter����.������������
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getPk_fivemetals_h () {
		return pk_fivemetals_h;
	}   
	/**
	 * ����pk_fivemetals_h��Setter����.������������
	 * ��������:2017-6-14
	 * @param newPk_fivemetals_h java.lang.String
	 */
	public void setPk_fivemetals_h (java.lang.String newPk_fivemetals_h ) {
	 	this.pk_fivemetals_h = newPk_fivemetals_h;
	} 	 
	
	/**
	 * ���� code��Getter����.�����������
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getCode () {
		return code;
	}   
	/**
	 * ����code��Setter����.�����������
	 * ��������:2017-6-14
	 * @param newCode java.lang.String
	 */
	public void setCode (java.lang.String newCode ) {
	 	this.code = newCode;
	} 	 
	
	/**
	 * ���� name��Getter����.������������
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getName () {
		return name;
	}   
	/**
	 * ����name��Setter����.������������
	 * ��������:2017-6-14
	 * @param newName java.lang.String
	 */
	public void setName (java.lang.String newName ) {
	 	this.name = newName;
	} 	 
	
	/**
	 * ���� maketime��Getter����.���������Ƶ�ʱ��
	 *  ��������:2017-6-14
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getMaketime () {
		return maketime;
	}   
	/**
	 * ����maketime��Setter����.���������Ƶ�ʱ��
	 * ��������:2017-6-14
	 * @param newMaketime nc.vo.pub.lang.UFDateTime
	 */
	public void setMaketime (nc.vo.pub.lang.UFDateTime newMaketime ) {
	 	this.maketime = newMaketime;
	} 	 
	
	/**
	 * ���� lastmaketime��Getter����.������������޸�ʱ��
	 *  ��������:2017-6-14
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getLastmaketime () {
		return lastmaketime;
	}   
	/**
	 * ����lastmaketime��Setter����.������������޸�ʱ��
	 * ��������:2017-6-14
	 * @param newLastmaketime nc.vo.pub.lang.UFDateTime
	 */
	public void setLastmaketime (nc.vo.pub.lang.UFDateTime newLastmaketime ) {
	 	this.lastmaketime = newLastmaketime;
	} 	 
	
	/**
	 * ���� vcardno��Getter����.����������Ƭ��
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getVcardno () {
		return vcardno;
	}   
	/**
	 * ����vcardno��Setter����.����������Ƭ��
	 * ��������:2017-6-14
	 * @param newVcardno java.lang.String
	 */
	public void setVcardno (java.lang.String newVcardno ) {
	 	this.vcardno = newVcardno;
	} 	 
	
	/**
	 * ���� cperiod��Getter����.���������·�
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getCperiod () {
		return cperiod;
	}   
	/**
	 * ����cperiod��Setter����.���������·�
	 * ��������:2017-6-14
	 * @param newCperiod java.lang.String
	 */
	public void setCperiod (java.lang.String newCperiod ) {
	 	this.cperiod = newCperiod;
	} 	 
	
	/**
	 * ���� vproject��Getter����.����������Ŀ
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getVproject () {
		return vproject;
	}   
	/**
	 * ����vproject��Setter����.����������Ŀ
	 * ��������:2017-6-14
	 * @param newVproject java.lang.String
	 */
	public void setVproject (java.lang.String newVproject ) {
	 	this.vproject = newVproject;
	} 	 
	
	/**
	 * ���� vdepartment��Getter����.������������
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getVdepartment () {
		return vdepartment;
	}   
	/**
	 * ����vdepartment��Setter����.������������
	 * ��������:2017-6-14
	 * @param newVdepartment java.lang.String
	 */
	public void setVdepartment (java.lang.String newVdepartment ) {
	 	this.vdepartment = newVdepartment;
	} 	 
	
	/**
	 * ���� vremark��Getter����.����������ע
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getVremark () {
		return vremark;
	}   
	/**
	 * ����vremark��Setter����.����������ע
	 * ��������:2017-6-14
	 * @param newVremark java.lang.String
	 */
	public void setVremark (java.lang.String newVremark ) {
	 	this.vremark = newVremark;
	} 	 
	
	/**
	 * ���� vcardtype��Getter����.����������������
	 *  ��������:2017-6-14
	 * @return nc.vo.ic.fivemetals.CardTypeEnum
	 */
	public nc.vo.ic.fivemetals.CardTypeEnum getVcardtype () {
		return vcardtype;
	}   
	/**
	 * ����vcardtype��Setter����.����������������
	 * ��������:2017-6-14
	 * @param newVcardtype nc.vo.ic.fivemetals.CardTypeEnum
	 */
	public void setVcardtype (nc.vo.ic.fivemetals.CardTypeEnum newVcardtype ) {
	 	this.vcardtype = newVcardtype;
	} 	 
	
	/**
	 * ���� def1��Getter����.���������Զ�����1
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getDef1 () {
		return def1;
	}   
	/**
	 * ����def1��Setter����.���������Զ�����1
	 * ��������:2017-6-14
	 * @param newDef1 java.lang.String
	 */
	public void setDef1 (java.lang.String newDef1 ) {
	 	this.def1 = newDef1;
	} 	 
	
	/**
	 * ���� def2��Getter����.���������Զ�����2
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getDef2 () {
		return def2;
	}   
	/**
	 * ����def2��Setter����.���������Զ�����2
	 * ��������:2017-6-14
	 * @param newDef2 java.lang.String
	 */
	public void setDef2 (java.lang.String newDef2 ) {
	 	this.def2 = newDef2;
	} 	 
	
	/**
	 * ���� def3��Getter����.���������Զ�����3
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getDef3 () {
		return def3;
	}   
	/**
	 * ����def3��Setter����.���������Զ�����3
	 * ��������:2017-6-14
	 * @param newDef3 java.lang.String
	 */
	public void setDef3 (java.lang.String newDef3 ) {
	 	this.def3 = newDef3;
	} 	 
	
	/**
	 * ���� def4��Getter����.���������Զ�����4
	 *  ��������:2017-6-14
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDef4 () {
		return def4;
	}   
	/**
	 * ����def4��Setter����.���������Զ�����4
	 * ��������:2017-6-14
	 * @param newDef4 nc.vo.pub.lang.UFDouble
	 */
	public void setDef4 (nc.vo.pub.lang.UFDouble newDef4 ) {
	 	this.def4 = newDef4;
	} 	 
	
	/**
	 * ���� def5��Getter����.���������Զ�����5
	 *  ��������:2017-6-14
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDef5 () {
		return def5;
	}   
	/**
	 * ����def5��Setter����.���������Զ�����5
	 * ��������:2017-6-14
	 * @param newDef5 nc.vo.pub.lang.UFDouble
	 */
	public void setDef5 (nc.vo.pub.lang.UFDouble newDef5 ) {
	 	this.def5 = newDef5;
	} 	 
	
	/**
	 * ���� def6��Getter����.���������Զ�����6
	 *  ��������:2017-6-14
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDef6 () {
		return def6;
	}   
	/**
	 * ����def6��Setter����.���������Զ�����6
	 * ��������:2017-6-14
	 * @param newDef6 nc.vo.pub.lang.UFDouble
	 */
	public void setDef6 (nc.vo.pub.lang.UFDouble newDef6 ) {
	 	this.def6 = newDef6;
	} 	 
	
	/**
	 * ���� def7��Getter����.���������Զ�����7
	 *  ��������:2017-6-14
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getDef7 () {
		return def7;
	}   
	/**
	 * ����def7��Setter����.���������Զ�����7
	 * ��������:2017-6-14
	 * @param newDef7 nc.vo.pub.lang.UFBoolean
	 */
	public void setDef7 (nc.vo.pub.lang.UFBoolean newDef7 ) {
	 	this.def7 = newDef7;
	} 	 
	
	/**
	 * ���� def8��Getter����.���������Զ�����8
	 *  ��������:2017-6-14
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getDef8 () {
		return def8;
	}   
	/**
	 * ����def8��Setter����.���������Զ�����8
	 * ��������:2017-6-14
	 * @param newDef8 nc.vo.pub.lang.UFBoolean
	 */
	public void setDef8 (nc.vo.pub.lang.UFBoolean newDef8 ) {
	 	this.def8 = newDef8;
	} 	 
	
	/**
	 * ���� def9��Getter����.���������Զ�����9
	 *  ��������:2017-6-14
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDef9 () {
		return def9;
	}   
	/**
	 * ����def9��Setter����.���������Զ�����9
	 * ��������:2017-6-14
	 * @param newDef9 nc.vo.pub.lang.UFDate
	 */
	public void setDef9 (nc.vo.pub.lang.UFDate newDef9 ) {
	 	this.def9 = newDef9;
	} 	 
	
	/**
	 * ���� def10��Getter����.���������Զ�����10
	 *  ��������:2017-6-14
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDef10 () {
		return def10;
	}   
	/**
	 * ����def10��Setter����.���������Զ�����10
	 * ��������:2017-6-14
	 * @param newDef10 nc.vo.pub.lang.UFDate
	 */
	public void setDef10 (nc.vo.pub.lang.UFDate newDef10 ) {
	 	this.def10 = newDef10;
	} 	 
	
	/**
	 * ���� pk_fivemetals_b��Getter����.���������ӱ�����
	 *  ��������:2017-6-14
	 * @return nc.vo.ic.fivemetals.FiveMetalsBVO[]
	 */
	public nc.vo.ic.fivemetals.FiveMetalsBVO[] getPk_fivemetals_b () {
		return pk_fivemetals_b;
	}   
	/**
	 * ����pk_fivemetals_b��Setter����.���������ӱ�����
	 * ��������:2017-6-14
	 * @param newPk_fivemetals_b nc.vo.ic.fivemetals.FiveMetalsBVO[]
	 */
	public void setPk_fivemetals_b (nc.vo.ic.fivemetals.FiveMetalsBVO[] newPk_fivemetals_b ) {
	 	this.pk_fivemetals_b = newPk_fivemetals_b;
	} 	 
	
	/**
	 * ���� dbilldate��Getter����.����������������
	 *  ��������:2017-6-14
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDbilldate () {
		return dbilldate;
	}   
	/**
	 * ����dbilldate��Setter����.����������������
	 * ��������:2017-6-14
	 * @param newDbilldate nc.vo.pub.lang.UFDate
	 */
	public void setDbilldate (nc.vo.pub.lang.UFDate newDbilldate ) {
	 	this.dbilldate = newDbilldate;
	} 	 
	
	/**
	 * ���� pk_billtype��Getter����.����������������
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getPk_billtype () {
		return pk_billtype;
	}   
	/**
	 * ����pk_billtype��Setter����.����������������
	 * ��������:2017-6-14
	 * @param newPk_billtype java.lang.String
	 */
	public void setPk_billtype (java.lang.String newPk_billtype ) {
	 	this.pk_billtype = newPk_billtype;
	} 	 
	
	/**
	 * ���� dr��Getter����.��������dr
	 *  ��������:2017-6-14
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * ����dr��Setter����.��������dr
	 * ��������:2017-6-14
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	 
	
	/**
	 * ���� ts��Getter����.��������ts
	 *  ��������:2017-6-14
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * ����ts��Setter����.��������ts
	 * ��������:2017-6-14
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	 
	
	
	/**
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2017-6-14
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2017-6-14
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
			
		return "pk_fivemetals_h";
	}
    
	/**
	 * <p>���ر�����
	 * <p>
	 * ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "ic_fivemetals_h";
	}    
	
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2017-6-14
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "ic_fivemetals_h";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2017-6-14
	  */
     public FiveMetalsHVO() {
		super();	
	}    
	
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.ic.fivemetals.FiveMetalsHVO" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("ic.FiveMetalsHVO");
		
   	}
     
}