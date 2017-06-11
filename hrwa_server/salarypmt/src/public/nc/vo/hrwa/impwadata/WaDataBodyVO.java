package nc.vo.hrwa.impwadata;

import nc.vo.pub.IVOMeta;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * 
 * </p> 创建日期:2017-6-11 薪资发放导入子表
 */
public class WaDataBodyVO extends nc.vo.pub.SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2534419289059688920L;
	private java.lang.String pk_impwadata;
	private java.lang.String pk_impwadata_b;
	private nc.vo.pub.lang.UFDouble f_1;
	private nc.vo.pub.lang.UFDouble f_2;
	private nc.vo.pub.lang.UFDouble f_3;
	private nc.vo.pub.lang.UFDouble f_4;
	private nc.vo.pub.lang.UFDouble f_5;
	private nc.vo.pub.lang.UFDouble f_6;
	private nc.vo.pub.lang.UFDouble f_7;
	private nc.vo.pub.lang.UFDouble f_8;
	private nc.vo.pub.lang.UFDouble f_9;
	private nc.vo.pub.lang.UFDouble f_10;
	private java.lang.String pk_psndoc;
	private java.lang.String pk_psnjob;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;

	public static final String PK_IMPWADATA = "pk_impwadata";
	public static final String PK_IMPWADATA_B = "pk_impwadata_b";
	public static final String F_1 = "f_1";
	public static final String F_2 = "f_2";
	public static final String F_3 = "f_3";
	public static final String F_4 = "f_4";
	public static final String F_5 = "f_5";
	public static final String F_6 = "f_6";
	public static final String F_7 = "f_7";
	public static final String F_8 = "f_8";
	public static final String F_9 = "f_9";
	public static final String F_10 = "f_10";
	public static final String PK_PSNDOC = "pk_psndoc";
	public static final String PK_PSNJOB = "pk_psnjob";

	/**
	 * 属性 pk_impwadata_b的Getter方法.属性名：子表主键 创建日期:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_impwadata_b() {
		return pk_impwadata_b;
	}

	/**
	 * 属性pk_impwadata_b的Setter方法.属性名：子表主键 创建日期:2017-6-11
	 * 
	 * @param newPk_impwadata_b
	 *            java.lang.String
	 */
	public void setPk_impwadata_b(java.lang.String newPk_impwadata_b) {
		this.pk_impwadata_b = newPk_impwadata_b;
	}

	/**
	 * 属性 f_1的Getter方法.属性名：考勤信息1 创建日期:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_1() {
		return f_1;
	}

	/**
	 * 属性f_1的Setter方法.属性名：考勤信息1 创建日期:2017-6-11
	 * 
	 * @param newF_1
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_1(nc.vo.pub.lang.UFDouble newF_1) {
		this.f_1 = newF_1;
	}

	/**
	 * 属性 f_2的Getter方法.属性名：考勤信息2 创建日期:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_2() {
		return f_2;
	}

	/**
	 * 属性f_2的Setter方法.属性名：考勤信息2 创建日期:2017-6-11
	 * 
	 * @param newF_2
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_2(nc.vo.pub.lang.UFDouble newF_2) {
		this.f_2 = newF_2;
	}

	/**
	 * 属性 f_3的Getter方法.属性名：考勤信息3 创建日期:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_3() {
		return f_3;
	}

	/**
	 * 属性f_3的Setter方法.属性名：考勤信息3 创建日期:2017-6-11
	 * 
	 * @param newF_3
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_3(nc.vo.pub.lang.UFDouble newF_3) {
		this.f_3 = newF_3;
	}

	/**
	 * 属性 f_4的Getter方法.属性名：考勤信息4 创建日期:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_4() {
		return f_4;
	}

	/**
	 * 属性f_4的Setter方法.属性名：考勤信息4 创建日期:2017-6-11
	 * 
	 * @param newF_4
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_4(nc.vo.pub.lang.UFDouble newF_4) {
		this.f_4 = newF_4;
	}

	/**
	 * 属性 f_5的Getter方法.属性名：考勤信息5 创建日期:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_5() {
		return f_5;
	}

	/**
	 * 属性f_5的Setter方法.属性名：考勤信息5 创建日期:2017-6-11
	 * 
	 * @param newF_5
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_5(nc.vo.pub.lang.UFDouble newF_5) {
		this.f_5 = newF_5;
	}

	/**
	 * 属性 f_6的Getter方法.属性名：考勤信息6 创建日期:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_6() {
		return f_6;
	}

	/**
	 * 属性f_6的Setter方法.属性名：考勤信息6 创建日期:2017-6-11
	 * 
	 * @param newF_6
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_6(nc.vo.pub.lang.UFDouble newF_6) {
		this.f_6 = newF_6;
	}

	/**
	 * 属性 f_7的Getter方法.属性名：考勤信息7 创建日期:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_7() {
		return f_7;
	}

	/**
	 * 属性f_7的Setter方法.属性名：考勤信息7 创建日期:2017-6-11
	 * 
	 * @param newF_7
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_7(nc.vo.pub.lang.UFDouble newF_7) {
		this.f_7 = newF_7;
	}

	/**
	 * 属性 f_8的Getter方法.属性名：考勤信息8 创建日期:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_8() {
		return f_8;
	}

	/**
	 * 属性f_8的Setter方法.属性名：考勤信息8 创建日期:2017-6-11
	 * 
	 * @param newF_8
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_8(nc.vo.pub.lang.UFDouble newF_8) {
		this.f_8 = newF_8;
	}

	/**
	 * 属性 f_9的Getter方法.属性名：考勤信息9 创建日期:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_9() {
		return f_9;
	}

	/**
	 * 属性f_9的Setter方法.属性名：考勤信息9 创建日期:2017-6-11
	 * 
	 * @param newF_9
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_9(nc.vo.pub.lang.UFDouble newF_9) {
		this.f_9 = newF_9;
	}

	/**
	 * 属性 f_10的Getter方法.属性名：考勤信息10 创建日期:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_10() {
		return f_10;
	}

	/**
	 * 属性f_10的Setter方法.属性名：考勤信息10 创建日期:2017-6-11
	 * 
	 * @param newF_10
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_10(nc.vo.pub.lang.UFDouble newF_10) {
		this.f_10 = newF_10;
	}

	/**
	 * 属性 pk_impwadata的Getter方法.属性名：主表主键 创建日期:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_impwadata() {
		return pk_impwadata;
	}

	/**
	 * 属性pk_impwadata的Setter方法.属性名：主表主键 创建日期:2017-6-11
	 * 
	 * @param newPk_impwadata
	 *            java.lang.String
	 */
	public void setPk_impwadata(java.lang.String newPk_impwadata) {
		this.pk_impwadata = newPk_impwadata;
	}

	/**
	 * 属性 dr的Getter方法.属性名：dr 创建日期:2017-6-11
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr() {
		return dr;
	}

	/**
	 * 属性dr的Setter方法.属性名：dr 创建日期:2017-6-11
	 * 
	 * @param newDr
	 *            java.lang.Integer
	 */
	public void setDr(java.lang.Integer newDr) {
		this.dr = newDr;
	}

	/**
	 * 属性 ts的Getter方法.属性名：ts 创建日期:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}

	/**
	 * 属性ts的Setter方法.属性名：ts 创建日期:2017-6-11
	 * 
	 * @param newTs
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(nc.vo.pub.lang.UFDateTime newTs) {
		this.ts = newTs;
	}

	/**
	 * 属性 pk_psndoc的Getter方法.属性名：人员基本信息 创建日期:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_psndoc() {
		return pk_psndoc;
	}

	/**
	 * 属性pk_psndoc的Setter方法.属性名：人员基本信息 创建日期:2017-6-11
	 * 
	 * @param newPk_psndoc
	 *            java.lang.String
	 */
	public void setPk_psndoc(java.lang.String newPk_psndoc) {
		this.pk_psndoc = newPk_psndoc;
	}

	/**
	 * 属性 pk_psnjob的Getter方法.属性名：人员工作记录 创建日期:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_psnjob() {
		return pk_psnjob;
	}

	/**
	 * 属性pk_psnjob的Setter方法.属性名：人员工作记录 创建日期:2017-6-11
	 * 
	 * @param newPk_psnjob
	 *            java.lang.String
	 */
	public void setPk_psnjob(java.lang.String newPk_psnjob) {
		this.pk_psnjob = newPk_psnjob;
	}

	/**
	 * <p>
	 * 取得父VO主键字段.
	 * <p>
	 * 创建日期:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {
		return "pk_impwadata";
	}

	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {

		return "pk_impwadata_b";
	}

	/**
	 * <p>
	 * 返回表名称
	 * <p>
	 * 创建日期:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "wa_data_b";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "wa_data_b";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2017-6-11
	 */
	public WaDataBodyVO() {
		super();
	}

	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.hrwa.impwadata.WaDataBodyVO")
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("hrwa.WaDataBodyVO");

	}

}