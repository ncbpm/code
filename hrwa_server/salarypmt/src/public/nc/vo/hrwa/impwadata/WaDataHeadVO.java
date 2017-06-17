package nc.vo.hrwa.impwadata;

import nc.vo.pub.IVOMeta;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 * 此处添加类的描述信息
 * </p>
 * 创建日期:2017-6-17
 * 
 * @author
 * @version NCPrj ??
 */
public class WaDataHeadVO extends nc.vo.pub.SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1582349754654322571L;
	private java.lang.String pk_wa_class;
	private java.lang.String cyear;
	private java.lang.String cperiod;
	private java.lang.String pk_group;
	private java.lang.String pk_org;
	private java.lang.String pk_impwadata;
	private java.lang.String cuserid;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;

	private nc.vo.hrwa.impwadata.WaDataBodyVO[] pk_impwadata_b;

	public static final String PK_WA_CLASS = "pk_wa_class";
	public static final String CYEAR = "cyear";
	public static final String CPERIOD = "cperiod";
	public static final String PK_GROUP = "pk_group";
	public static final String PK_ORG = "pk_org";
	public static final String PK_IMPWADATA = "pk_impwadata";
	public static final String CUSERID = "cuserid";

	/**
	 * 属性 pk_wa_class的Getter方法.属性名：薪资方案 创建日期:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_wa_class() {
		return pk_wa_class;
	}

	/**
	 * 属性pk_wa_class的Setter方法.属性名：薪资方案 创建日期:2017-6-17
	 * 
	 * @param newPk_wa_class
	 *            java.lang.String
	 */
	public void setPk_wa_class(java.lang.String newPk_wa_class) {
		this.pk_wa_class = newPk_wa_class;
	}

	/**
	 * 属性 cyear的Getter方法.属性名：会计年度 创建日期:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCyear() {
		return cyear;
	}

	/**
	 * 属性cyear的Setter方法.属性名：会计年度 创建日期:2017-6-17
	 * 
	 * @param newCyear
	 *            java.lang.String
	 */
	public void setCyear(java.lang.String newCyear) {
		this.cyear = newCyear;
	}

	/**
	 * 属性 cperiod的Getter方法.属性名：会计月度 创建日期:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCperiod() {
		return cperiod;
	}

	/**
	 * 属性cperiod的Setter方法.属性名：会计月度 创建日期:2017-6-17
	 * 
	 * @param newCperiod
	 *            java.lang.String
	 */
	public void setCperiod(java.lang.String newCperiod) {
		this.cperiod = newCperiod;
	}

	/**
	 * 属性 pk_group的Getter方法.属性名：所属集团 创建日期:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group() {
		return pk_group;
	}

	/**
	 * 属性pk_group的Setter方法.属性名：所属集团 创建日期:2017-6-17
	 * 
	 * @param newPk_group
	 *            java.lang.String
	 */
	public void setPk_group(java.lang.String newPk_group) {
		this.pk_group = newPk_group;
	}

	/**
	 * 属性 pk_org的Getter方法.属性名：所属组织 创建日期:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org() {
		return pk_org;
	}

	/**
	 * 属性pk_org的Setter方法.属性名：所属组织 创建日期:2017-6-17
	 * 
	 * @param newPk_org
	 *            java.lang.String
	 */
	public void setPk_org(java.lang.String newPk_org) {
		this.pk_org = newPk_org;
	}

	/**
	 * 属性 pk_impwadata_b的Getter方法.属性名：子表主键 创建日期:2017-6-17
	 * 
	 * @return nc.vo.hrwa.impwadata.WaDataBodyVO[]
	 */
	public nc.vo.hrwa.impwadata.WaDataBodyVO[] getPk_impwadata_b() {
		return pk_impwadata_b;
	}

	/**
	 * 属性pk_impwadata_b的Setter方法.属性名：子表主键 创建日期:2017-6-17
	 * 
	 * @param newPk_impwadata_b
	 *            nc.vo.hrwa.impwadata.WaDataBodyVO[]
	 */
	public void setPk_impwadata_b(
			nc.vo.hrwa.impwadata.WaDataBodyVO[] newPk_impwadata_b) {
		this.pk_impwadata_b = newPk_impwadata_b;
	}

	/**
	 * 属性 pk_impwadata的Getter方法.属性名：主表主键 创建日期:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_impwadata() {
		return pk_impwadata;
	}

	/**
	 * 属性pk_impwadata的Setter方法.属性名：主表主键 创建日期:2017-6-17
	 * 
	 * @param newPk_impwadata
	 *            java.lang.String
	 */
	public void setPk_impwadata(java.lang.String newPk_impwadata) {
		this.pk_impwadata = newPk_impwadata;
	}

	/**
	 * 属性 cuserid的Getter方法.属性名：操作员 创建日期:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCuserid() {
		return cuserid;
	}

	/**
	 * 属性cuserid的Setter方法.属性名：操作员 创建日期:2017-6-17
	 * 
	 * @param newCuserid
	 *            java.lang.String
	 */
	public void setCuserid(java.lang.String newCuserid) {
		this.cuserid = newCuserid;
	}

	/**
	 * 属性 dr的Getter方法.属性名：dr 创建日期:2017-6-17
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr() {
		return dr;
	}

	/**
	 * 属性dr的Setter方法.属性名：dr 创建日期:2017-6-17
	 * 
	 * @param newDr
	 *            java.lang.Integer
	 */
	public void setDr(java.lang.Integer newDr) {
		this.dr = newDr;
	}

	/**
	 * 属性 ts的Getter方法.属性名：ts 创建日期:2017-6-17
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}

	/**
	 * 属性ts的Setter方法.属性名：ts 创建日期:2017-6-17
	 * 
	 * @param newTs
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(nc.vo.pub.lang.UFDateTime newTs) {
		this.ts = newTs;
	}

	/**
	 * <p>
	 * 取得父VO主键字段.
	 * <p>
	 * 创建日期:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {
		return null;
	}

	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {

		return "pk_impwadata";
	}

	/**
	 * <p>
	 * 返回表名称
	 * <p>
	 * 创建日期:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "wa_data_h";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "wa_data_h";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2017-6-17
	 */
	public WaDataHeadVO() {
		super();
	}

	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.hrwa.impwadata.WaDataHeadVO")
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("hrwa.WaDataHeadVO");

	}

}