package nc.vo.ic.fivemetals;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 *   此处添加类的描述信息
 * </p>
 *  创建日期:2017-6-14
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
	 * 属性 pk_fivemetals_h的Getter方法.属性名：parentPK
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getPk_fivemetals_h () {
		return pk_fivemetals_h;
	}   
	/**
	 * 属性pk_fivemetals_h的Setter方法.属性名：parentPK
	 * 创建日期:2017-6-14
	 * @param newPk_fivemetals_h java.lang.String
	 */
	public void setPk_fivemetals_h (java.lang.String newPk_fivemetals_h ) {
	 	this.pk_fivemetals_h = newPk_fivemetals_h;
	} 	 
	
	/**
	 * 属性 rowno的Getter方法.属性名：行号
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getRowno () {
		return rowno;
	}   
	/**
	 * 属性rowno的Setter方法.属性名：行号
	 * 创建日期:2017-6-14
	 * @param newRowno java.lang.String
	 */
	public void setRowno (java.lang.String newRowno ) {
	 	this.rowno = newRowno;
	} 	 
	
	/**
	 * 属性 nmny的Getter方法.属性名：金额
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getNmny () {
		return nmny;
	}   
	/**
	 * 属性nmny的Setter方法.属性名：金额
	 * 创建日期:2017-6-14
	 * @param newNmny java.lang.String
	 */
	public void setNmny (java.lang.String newNmny ) {
	 	this.nmny = newNmny;
	} 	 
	
	/**
	 * 属性 itype的Getter方法.属性名：费用类型
	 *  创建日期:2017-6-14
	 * @return nc.vo.ic.fivemetals.CostTypeEnum
	 */
	public nc.vo.ic.fivemetals.CostTypeEnum getItype () {
		return itype;
	}   
	/**
	 * 属性itype的Setter方法.属性名：费用类型
	 * 创建日期:2017-6-14
	 * @param newItype nc.vo.ic.fivemetals.CostTypeEnum
	 */
	public void setItype (nc.vo.ic.fivemetals.CostTypeEnum newItype ) {
	 	this.itype = newItype;
	} 	 
	
	/**
	 * 属性 vremark的Getter方法.属性名：备注
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getVremark () {
		return vremark;
	}   
	/**
	 * 属性vremark的Setter方法.属性名：备注
	 * 创建日期:2017-6-14
	 * @param newVremark java.lang.String
	 */
	public void setVremark (java.lang.String newVremark ) {
	 	this.vremark = newVremark;
	} 	 
	
	/**
	 * 属性 vsourcetype的Getter方法.属性名：来源单据类型
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getVsourcetype () {
		return vsourcetype;
	}   
	/**
	 * 属性vsourcetype的Setter方法.属性名：来源单据类型
	 * 创建日期:2017-6-14
	 * @param newVsourcetype java.lang.String
	 */
	public void setVsourcetype (java.lang.String newVsourcetype ) {
	 	this.vsourcetype = newVsourcetype;
	} 	 
	
	/**
	 * 属性 vsourcebillno的Getter方法.属性名：来源单据号
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getVsourcebillno () {
		return vsourcebillno;
	}   
	/**
	 * 属性vsourcebillno的Setter方法.属性名：来源单据号
	 * 创建日期:2017-6-14
	 * @param newVsourcebillno java.lang.String
	 */
	public void setVsourcebillno (java.lang.String newVsourcebillno ) {
	 	this.vsourcebillno = newVsourcebillno;
	} 	 
	
	/**
	 * 属性 def1的Getter方法.属性名：自定义项1
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getDef1 () {
		return def1;
	}   
	/**
	 * 属性def1的Setter方法.属性名：自定义项1
	 * 创建日期:2017-6-14
	 * @param newDef1 java.lang.String
	 */
	public void setDef1 (java.lang.String newDef1 ) {
	 	this.def1 = newDef1;
	} 	 
	
	/**
	 * 属性 def2的Getter方法.属性名：自定义项2
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getDef2 () {
		return def2;
	}   
	/**
	 * 属性def2的Setter方法.属性名：自定义项2
	 * 创建日期:2017-6-14
	 * @param newDef2 java.lang.String
	 */
	public void setDef2 (java.lang.String newDef2 ) {
	 	this.def2 = newDef2;
	} 	 
	
	/**
	 * 属性 def3的Getter方法.属性名：自定义项3
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getDef3 () {
		return def3;
	}   
	/**
	 * 属性def3的Setter方法.属性名：自定义项3
	 * 创建日期:2017-6-14
	 * @param newDef3 java.lang.String
	 */
	public void setDef3 (java.lang.String newDef3 ) {
	 	this.def3 = newDef3;
	} 	 
	
	/**
	 * 属性 def4的Getter方法.属性名：自定义项4
	 *  创建日期:2017-6-14
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDef4 () {
		return def4;
	}   
	/**
	 * 属性def4的Setter方法.属性名：自定义项4
	 * 创建日期:2017-6-14
	 * @param newDef4 nc.vo.pub.lang.UFDouble
	 */
	public void setDef4 (nc.vo.pub.lang.UFDouble newDef4 ) {
	 	this.def4 = newDef4;
	} 	 
	
	/**
	 * 属性 def5的Getter方法.属性名：自定义项5
	 *  创建日期:2017-6-14
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDef5 () {
		return def5;
	}   
	/**
	 * 属性def5的Setter方法.属性名：自定义项5
	 * 创建日期:2017-6-14
	 * @param newDef5 nc.vo.pub.lang.UFDouble
	 */
	public void setDef5 (nc.vo.pub.lang.UFDouble newDef5 ) {
	 	this.def5 = newDef5;
	} 	 
	
	/**
	 * 属性 def6的Getter方法.属性名：自定义项6
	 *  创建日期:2017-6-14
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDef6 () {
		return def6;
	}   
	/**
	 * 属性def6的Setter方法.属性名：自定义项6
	 * 创建日期:2017-6-14
	 * @param newDef6 nc.vo.pub.lang.UFDouble
	 */
	public void setDef6 (nc.vo.pub.lang.UFDouble newDef6 ) {
	 	this.def6 = newDef6;
	} 	 
	
	/**
	 * 属性 def7的Getter方法.属性名：自定义项7
	 *  创建日期:2017-6-14
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getDef7 () {
		return def7;
	}   
	/**
	 * 属性def7的Setter方法.属性名：自定义项7
	 * 创建日期:2017-6-14
	 * @param newDef7 nc.vo.pub.lang.UFBoolean
	 */
	public void setDef7 (nc.vo.pub.lang.UFBoolean newDef7 ) {
	 	this.def7 = newDef7;
	} 	 
	
	/**
	 * 属性 def8的Getter方法.属性名：自定义项8
	 *  创建日期:2017-6-14
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getDef8 () {
		return def8;
	}   
	/**
	 * 属性def8的Setter方法.属性名：自定义项8
	 * 创建日期:2017-6-14
	 * @param newDef8 nc.vo.pub.lang.UFBoolean
	 */
	public void setDef8 (nc.vo.pub.lang.UFBoolean newDef8 ) {
	 	this.def8 = newDef8;
	} 	 
	
	/**
	 * 属性 def9的Getter方法.属性名：自定义项9
	 *  创建日期:2017-6-14
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDef9 () {
		return def9;
	}   
	/**
	 * 属性def9的Setter方法.属性名：自定义项9
	 * 创建日期:2017-6-14
	 * @param newDef9 nc.vo.pub.lang.UFDate
	 */
	public void setDef9 (nc.vo.pub.lang.UFDate newDef9 ) {
	 	this.def9 = newDef9;
	} 	 
	
	/**
	 * 属性 def10的Getter方法.属性名：自定义项10
	 *  创建日期:2017-6-14
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDef10 () {
		return def10;
	}   
	/**
	 * 属性def10的Setter方法.属性名：自定义项10
	 * 创建日期:2017-6-14
	 * @param newDef10 nc.vo.pub.lang.UFDate
	 */
	public void setDef10 (nc.vo.pub.lang.UFDate newDef10 ) {
	 	this.def10 = newDef10;
	} 	 
	
	/**
	 * 属性 pk_fivemetals_b的Getter方法.属性名：子表主键
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getPk_fivemetals_b () {
		return pk_fivemetals_b;
	}   
	/**
	 * 属性pk_fivemetals_b的Setter方法.属性名：子表主键
	 * 创建日期:2017-6-14
	 * @param newPk_fivemetals_b java.lang.String
	 */
	public void setPk_fivemetals_b (java.lang.String newPk_fivemetals_b ) {
	 	this.pk_fivemetals_b = newPk_fivemetals_b;
	} 	 
	
	/**
	 * 属性 dr的Getter方法.属性名：dr
	 *  创建日期:2017-6-14
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.属性名：dr
	 * 创建日期:2017-6-14
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	 
	
	/**
	 * 属性 ts的Getter方法.属性名：ts
	 *  创建日期:2017-6-14
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.属性名：ts
	 * 创建日期:2017-6-14
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	 
	
	
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2017-6-14
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {		
		return "pk_fivemetals_h";
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2017-6-14
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
			
		return "pk_fivemetals_b";
	}
    
	/**
	 * <p>返回表名称
	 * <p>
	 * 创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "ic_fivemetals_b";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "ic_fivemetals_b";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2017-6-14
	  */
     public FiveMetalsBVO() {
		super();	
	}    
	
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.ic.fivemetals.FiveMetalsBVO" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("ic.FiveMetalsBVO");
		
   	}
     
}