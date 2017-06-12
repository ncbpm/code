package nc.vo.hrwa.impwadata;

import nc.vo.pub.IVOMeta;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * 
 * </p> ��������:2017-6-11 н�ʷ��ŵ����ӱ�
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
	 * ���� pk_impwadata_b��Getter����.���������ӱ����� ��������:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_impwadata_b() {
		return pk_impwadata_b;
	}

	/**
	 * ����pk_impwadata_b��Setter����.���������ӱ����� ��������:2017-6-11
	 * 
	 * @param newPk_impwadata_b
	 *            java.lang.String
	 */
	public void setPk_impwadata_b(java.lang.String newPk_impwadata_b) {
		this.pk_impwadata_b = newPk_impwadata_b;
	}

	/**
	 * ���� f_1��Getter����.��������������Ϣ1 ��������:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_1() {
		return f_1;
	}

	/**
	 * ����f_1��Setter����.��������������Ϣ1 ��������:2017-6-11
	 * 
	 * @param newF_1
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_1(nc.vo.pub.lang.UFDouble newF_1) {
		this.f_1 = newF_1;
	}

	/**
	 * ���� f_2��Getter����.��������������Ϣ2 ��������:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_2() {
		return f_2;
	}

	/**
	 * ����f_2��Setter����.��������������Ϣ2 ��������:2017-6-11
	 * 
	 * @param newF_2
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_2(nc.vo.pub.lang.UFDouble newF_2) {
		this.f_2 = newF_2;
	}

	/**
	 * ���� f_3��Getter����.��������������Ϣ3 ��������:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_3() {
		return f_3;
	}

	/**
	 * ����f_3��Setter����.��������������Ϣ3 ��������:2017-6-11
	 * 
	 * @param newF_3
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_3(nc.vo.pub.lang.UFDouble newF_3) {
		this.f_3 = newF_3;
	}

	/**
	 * ���� f_4��Getter����.��������������Ϣ4 ��������:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_4() {
		return f_4;
	}

	/**
	 * ����f_4��Setter����.��������������Ϣ4 ��������:2017-6-11
	 * 
	 * @param newF_4
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_4(nc.vo.pub.lang.UFDouble newF_4) {
		this.f_4 = newF_4;
	}

	/**
	 * ���� f_5��Getter����.��������������Ϣ5 ��������:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_5() {
		return f_5;
	}

	/**
	 * ����f_5��Setter����.��������������Ϣ5 ��������:2017-6-11
	 * 
	 * @param newF_5
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_5(nc.vo.pub.lang.UFDouble newF_5) {
		this.f_5 = newF_5;
	}

	/**
	 * ���� f_6��Getter����.��������������Ϣ6 ��������:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_6() {
		return f_6;
	}

	/**
	 * ����f_6��Setter����.��������������Ϣ6 ��������:2017-6-11
	 * 
	 * @param newF_6
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_6(nc.vo.pub.lang.UFDouble newF_6) {
		this.f_6 = newF_6;
	}

	/**
	 * ���� f_7��Getter����.��������������Ϣ7 ��������:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_7() {
		return f_7;
	}

	/**
	 * ����f_7��Setter����.��������������Ϣ7 ��������:2017-6-11
	 * 
	 * @param newF_7
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_7(nc.vo.pub.lang.UFDouble newF_7) {
		this.f_7 = newF_7;
	}

	/**
	 * ���� f_8��Getter����.��������������Ϣ8 ��������:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_8() {
		return f_8;
	}

	/**
	 * ����f_8��Setter����.��������������Ϣ8 ��������:2017-6-11
	 * 
	 * @param newF_8
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_8(nc.vo.pub.lang.UFDouble newF_8) {
		this.f_8 = newF_8;
	}

	/**
	 * ���� f_9��Getter����.��������������Ϣ9 ��������:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_9() {
		return f_9;
	}

	/**
	 * ����f_9��Setter����.��������������Ϣ9 ��������:2017-6-11
	 * 
	 * @param newF_9
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_9(nc.vo.pub.lang.UFDouble newF_9) {
		this.f_9 = newF_9;
	}

	/**
	 * ���� f_10��Getter����.��������������Ϣ10 ��������:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getF_10() {
		return f_10;
	}

	/**
	 * ����f_10��Setter����.��������������Ϣ10 ��������:2017-6-11
	 * 
	 * @param newF_10
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setF_10(nc.vo.pub.lang.UFDouble newF_10) {
		this.f_10 = newF_10;
	}

	/**
	 * ���� pk_impwadata��Getter����.���������������� ��������:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_impwadata() {
		return pk_impwadata;
	}

	/**
	 * ����pk_impwadata��Setter����.���������������� ��������:2017-6-11
	 * 
	 * @param newPk_impwadata
	 *            java.lang.String
	 */
	public void setPk_impwadata(java.lang.String newPk_impwadata) {
		this.pk_impwadata = newPk_impwadata;
	}

	/**
	 * ���� dr��Getter����.��������dr ��������:2017-6-11
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr() {
		return dr;
	}

	/**
	 * ����dr��Setter����.��������dr ��������:2017-6-11
	 * 
	 * @param newDr
	 *            java.lang.Integer
	 */
	public void setDr(java.lang.Integer newDr) {
		this.dr = newDr;
	}

	/**
	 * ���� ts��Getter����.��������ts ��������:2017-6-11
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}

	/**
	 * ����ts��Setter����.��������ts ��������:2017-6-11
	 * 
	 * @param newTs
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(nc.vo.pub.lang.UFDateTime newTs) {
		this.ts = newTs;
	}

	/**
	 * ���� pk_psndoc��Getter����.����������Ա������Ϣ ��������:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_psndoc() {
		return pk_psndoc;
	}

	/**
	 * ����pk_psndoc��Setter����.����������Ա������Ϣ ��������:2017-6-11
	 * 
	 * @param newPk_psndoc
	 *            java.lang.String
	 */
	public void setPk_psndoc(java.lang.String newPk_psndoc) {
		this.pk_psndoc = newPk_psndoc;
	}

	/**
	 * ���� pk_psnjob��Getter����.����������Ա������¼ ��������:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_psnjob() {
		return pk_psnjob;
	}

	/**
	 * ����pk_psnjob��Setter����.����������Ա������¼ ��������:2017-6-11
	 * 
	 * @param newPk_psnjob
	 *            java.lang.String
	 */
	public void setPk_psnjob(java.lang.String newPk_psnjob) {
		this.pk_psnjob = newPk_psnjob;
	}

	/**
	 * <p>
	 * ȡ�ø�VO�����ֶ�.
	 * <p>
	 * ��������:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {
		return "pk_impwadata";
	}

	/**
	 * <p>
	 * ȡ�ñ�����.
	 * <p>
	 * ��������:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {

		return "pk_impwadata_b";
	}

	/**
	 * <p>
	 * ���ر�����
	 * <p>
	 * ��������:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "wa_data_b";
	}

	/**
	 * <p>
	 * ���ر�����.
	 * <p>
	 * ��������:2017-6-11
	 * 
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "wa_data_b";
	}

	/**
	 * ����Ĭ�Ϸ�ʽ����������.
	 * 
	 * ��������:2017-6-11
	 */
	public WaDataBodyVO() {
		super();
	}

	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.hrwa.impwadata.WaDataBodyVO")
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("hrwa.WaDataBodyVO");

	}

}