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
	 * 属性 pk_group的Getter方法.属性名：集团
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group () {
		return pk_group;
	}   
	/**
	 * 属性pk_group的Setter方法.属性名：集团
	 * 创建日期:2017-6-14
	 * @param newPk_group java.lang.String
	 */
	public void setPk_group (java.lang.String newPk_group ) {
	 	this.pk_group = newPk_group;
	} 	 
	
	/**
	 * 属性 pk_org的Getter方法.属性名：组织
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org () {
		return pk_org;
	}   
	/**
	 * 属性pk_org的Setter方法.属性名：组织
	 * 创建日期:2017-6-14
	 * @param newPk_org java.lang.String
	 */
	public void setPk_org (java.lang.String newPk_org ) {
	 	this.pk_org = newPk_org;
	} 	 
	
	/**
	 * 属性 pk_org_v的Getter方法.属性名：组织版本
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org_v () {
		return pk_org_v;
	}   
	/**
	 * 属性pk_org_v的Setter方法.属性名：组织版本
	 * 创建日期:2017-6-14
	 * @param newPk_org_v java.lang.String
	 */
	public void setPk_org_v (java.lang.String newPk_org_v ) {
	 	this.pk_org_v = newPk_org_v;
	} 	 
	
	/**
	 * 属性 pk_fivemetals_h的Getter方法.属性名：主键
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getPk_fivemetals_h () {
		return pk_fivemetals_h;
	}   
	/**
	 * 属性pk_fivemetals_h的Setter方法.属性名：主键
	 * 创建日期:2017-6-14
	 * @param newPk_fivemetals_h java.lang.String
	 */
	public void setPk_fivemetals_h (java.lang.String newPk_fivemetals_h ) {
	 	this.pk_fivemetals_h = newPk_fivemetals_h;
	} 	 
	
	/**
	 * 属性 code的Getter方法.属性名：编号
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getCode () {
		return code;
	}   
	/**
	 * 属性code的Setter方法.属性名：编号
	 * 创建日期:2017-6-14
	 * @param newCode java.lang.String
	 */
	public void setCode (java.lang.String newCode ) {
	 	this.code = newCode;
	} 	 
	
	/**
	 * 属性 name的Getter方法.属性名：名称
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getName () {
		return name;
	}   
	/**
	 * 属性name的Setter方法.属性名：名称
	 * 创建日期:2017-6-14
	 * @param newName java.lang.String
	 */
	public void setName (java.lang.String newName ) {
	 	this.name = newName;
	} 	 
	
	/**
	 * 属性 maketime的Getter方法.属性名：制单时间
	 *  创建日期:2017-6-14
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getMaketime () {
		return maketime;
	}   
	/**
	 * 属性maketime的Setter方法.属性名：制单时间
	 * 创建日期:2017-6-14
	 * @param newMaketime nc.vo.pub.lang.UFDateTime
	 */
	public void setMaketime (nc.vo.pub.lang.UFDateTime newMaketime ) {
	 	this.maketime = newMaketime;
	} 	 
	
	/**
	 * 属性 lastmaketime的Getter方法.属性名：最后修改时间
	 *  创建日期:2017-6-14
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getLastmaketime () {
		return lastmaketime;
	}   
	/**
	 * 属性lastmaketime的Setter方法.属性名：最后修改时间
	 * 创建日期:2017-6-14
	 * @param newLastmaketime nc.vo.pub.lang.UFDateTime
	 */
	public void setLastmaketime (nc.vo.pub.lang.UFDateTime newLastmaketime ) {
	 	this.lastmaketime = newLastmaketime;
	} 	 
	
	/**
	 * 属性 vcardno的Getter方法.属性名：卡片号
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getVcardno () {
		return vcardno;
	}   
	/**
	 * 属性vcardno的Setter方法.属性名：卡片号
	 * 创建日期:2017-6-14
	 * @param newVcardno java.lang.String
	 */
	public void setVcardno (java.lang.String newVcardno ) {
	 	this.vcardno = newVcardno;
	} 	 
	
	/**
	 * 属性 cperiod的Getter方法.属性名：月份
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getCperiod () {
		return cperiod;
	}   
	/**
	 * 属性cperiod的Setter方法.属性名：月份
	 * 创建日期:2017-6-14
	 * @param newCperiod java.lang.String
	 */
	public void setCperiod (java.lang.String newCperiod ) {
	 	this.cperiod = newCperiod;
	} 	 
	
	/**
	 * 属性 vproject的Getter方法.属性名：项目
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getVproject () {
		return vproject;
	}   
	/**
	 * 属性vproject的Setter方法.属性名：项目
	 * 创建日期:2017-6-14
	 * @param newVproject java.lang.String
	 */
	public void setVproject (java.lang.String newVproject ) {
	 	this.vproject = newVproject;
	} 	 
	
	/**
	 * 属性 vdepartment的Getter方法.属性名：部门
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getVdepartment () {
		return vdepartment;
	}   
	/**
	 * 属性vdepartment的Setter方法.属性名：部门
	 * 创建日期:2017-6-14
	 * @param newVdepartment java.lang.String
	 */
	public void setVdepartment (java.lang.String newVdepartment ) {
	 	this.vdepartment = newVdepartment;
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
	 * 属性 vcardtype的Getter方法.属性名：卡号类型
	 *  创建日期:2017-6-14
	 * @return nc.vo.ic.fivemetals.CardTypeEnum
	 */
	public nc.vo.ic.fivemetals.CardTypeEnum getVcardtype () {
		return vcardtype;
	}   
	/**
	 * 属性vcardtype的Setter方法.属性名：卡号类型
	 * 创建日期:2017-6-14
	 * @param newVcardtype nc.vo.ic.fivemetals.CardTypeEnum
	 */
	public void setVcardtype (nc.vo.ic.fivemetals.CardTypeEnum newVcardtype ) {
	 	this.vcardtype = newVcardtype;
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
	 * @return nc.vo.ic.fivemetals.FiveMetalsBVO[]
	 */
	public nc.vo.ic.fivemetals.FiveMetalsBVO[] getPk_fivemetals_b () {
		return pk_fivemetals_b;
	}   
	/**
	 * 属性pk_fivemetals_b的Setter方法.属性名：子表主键
	 * 创建日期:2017-6-14
	 * @param newPk_fivemetals_b nc.vo.ic.fivemetals.FiveMetalsBVO[]
	 */
	public void setPk_fivemetals_b (nc.vo.ic.fivemetals.FiveMetalsBVO[] newPk_fivemetals_b ) {
	 	this.pk_fivemetals_b = newPk_fivemetals_b;
	} 	 
	
	/**
	 * 属性 dbilldate的Getter方法.属性名：单据日期
	 *  创建日期:2017-6-14
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDbilldate () {
		return dbilldate;
	}   
	/**
	 * 属性dbilldate的Setter方法.属性名：单据日期
	 * 创建日期:2017-6-14
	 * @param newDbilldate nc.vo.pub.lang.UFDate
	 */
	public void setDbilldate (nc.vo.pub.lang.UFDate newDbilldate ) {
	 	this.dbilldate = newDbilldate;
	} 	 
	
	/**
	 * 属性 pk_billtype的Getter方法.属性名：单据类型
	 *  创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getPk_billtype () {
		return pk_billtype;
	}   
	/**
	 * 属性pk_billtype的Setter方法.属性名：单据类型
	 * 创建日期:2017-6-14
	 * @param newPk_billtype java.lang.String
	 */
	public void setPk_billtype (java.lang.String newPk_billtype ) {
	 	this.pk_billtype = newPk_billtype;
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
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2017-6-14
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
			
		return "pk_fivemetals_h";
	}
    
	/**
	 * <p>返回表名称
	 * <p>
	 * 创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "ic_fivemetals_h";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2017-6-14
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "ic_fivemetals_h";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2017-6-14
	  */
     public FiveMetalsHVO() {
		super();	
	}    
	
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.ic.fivemetals.FiveMetalsHVO" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("ic.FiveMetalsHVO");
		
   	}
     
}