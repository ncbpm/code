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
public class FiveMetalsBVO extends nc.vo.pub.SuperVO{
	
    private java.lang.String rowno;
    private java.lang.String nmny;
    private nc.vo.ic.fivemetals.CostTypeEnum itype;
    private java.lang.String vremark;
    private java.lang.String vsourcetype;
    private java.lang.String vsourcebillno;
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
    private java.lang.String pk_fivemetals_h;
    private java.lang.String pk_fivemetals_b;
    private java.lang.Integer dr = 0;
    private nc.vo.pub.lang.UFDateTime ts;    
	
	
    public static final String ROWNO = "rowno";
    public static final String NMNY = "nmny";
    public static final String ITYPE = "itype";
    public static final String VREMARK = "vremark";
    public static final String VSOURCETYPE = "vsourcetype";
    public static final String VSOURCEBILLNO = "vsourcebillno";
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
    public static final String PK_FIVEMETALS_H = "pk_fivemetals_h";
    public static final String PK_FIVEMETALS_B = "pk_fivemetals_b";

	/**
	 * ���� pk_fivemetals_h��Getter����.��������parentPK
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getPk_fivemetals_h () {
		return pk_fivemetals_h;
	}   
	/**
	 * ����pk_fivemetals_h��Setter����.��������parentPK
	 * ��������:2017-6-14
	 * @param newPk_fivemetals_h java.lang.String
	 */
	public void setPk_fivemetals_h (java.lang.String newPk_fivemetals_h ) {
	 	this.pk_fivemetals_h = newPk_fivemetals_h;
	} 	 
	
	/**
	 * ���� rowno��Getter����.���������к�
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getRowno () {
		return rowno;
	}   
	/**
	 * ����rowno��Setter����.���������к�
	 * ��������:2017-6-14
	 * @param newRowno java.lang.String
	 */
	public void setRowno (java.lang.String newRowno ) {
	 	this.rowno = newRowno;
	} 	 
	
	/**
	 * ���� nmny��Getter����.�����������
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getNmny () {
		return nmny;
	}   
	/**
	 * ����nmny��Setter����.�����������
	 * ��������:2017-6-14
	 * @param newNmny java.lang.String
	 */
	public void setNmny (java.lang.String newNmny ) {
	 	this.nmny = newNmny;
	} 	 
	
	/**
	 * ���� itype��Getter����.����������������
	 *  ��������:2017-6-14
	 * @return nc.vo.ic.fivemetals.CostTypeEnum
	 */
	public nc.vo.ic.fivemetals.CostTypeEnum getItype () {
		return itype;
	}   
	/**
	 * ����itype��Setter����.����������������
	 * ��������:2017-6-14
	 * @param newItype nc.vo.ic.fivemetals.CostTypeEnum
	 */
	public void setItype (nc.vo.ic.fivemetals.CostTypeEnum newItype ) {
	 	this.itype = newItype;
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
	 * ���� vsourcetype��Getter����.����������Դ��������
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getVsourcetype () {
		return vsourcetype;
	}   
	/**
	 * ����vsourcetype��Setter����.����������Դ��������
	 * ��������:2017-6-14
	 * @param newVsourcetype java.lang.String
	 */
	public void setVsourcetype (java.lang.String newVsourcetype ) {
	 	this.vsourcetype = newVsourcetype;
	} 	 
	
	/**
	 * ���� vsourcebillno��Getter����.����������Դ���ݺ�
	 *  ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getVsourcebillno () {
		return vsourcebillno;
	}   
	/**
	 * ����vsourcebillno��Setter����.����������Դ���ݺ�
	 * ��������:2017-6-14
	 * @param newVsourcebillno java.lang.String
	 */
	public void setVsourcebillno (java.lang.String newVsourcebillno ) {
	 	this.vsourcebillno = newVsourcebillno;
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
	 * @return java.lang.String
	 */
	public java.lang.String getPk_fivemetals_b () {
		return pk_fivemetals_b;
	}   
	/**
	 * ����pk_fivemetals_b��Setter����.���������ӱ�����
	 * ��������:2017-6-14
	 * @param newPk_fivemetals_b java.lang.String
	 */
	public void setPk_fivemetals_b (java.lang.String newPk_fivemetals_b ) {
	 	this.pk_fivemetals_b = newPk_fivemetals_b;
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
		return "pk_fivemetals_h";
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2017-6-14
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
			
		return "pk_fivemetals_b";
	}
    
	/**
	 * <p>���ر�����
	 * <p>
	 * ��������:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "ic_fivemetals_b";
	}    
	
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2017-6-14
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "ic_fivemetals_b";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2017-6-14
	  */
     public FiveMetalsBVO() {
		super();	
	}    
	
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.ic.fivemetals.FiveMetalsBVO" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("ic.FiveMetalsBVO");
		
   	}
     
}