package nc.vo.hrwa.impwadata;

import nc.vo.pub.IVOMeta;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 * �˴�������������Ϣ
 * </p>
 * ��������:2017-6-17
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
	 * ���� pk_wa_class��Getter����.��������н�ʷ��� ��������:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_wa_class() {
		return pk_wa_class;
	}

	/**
	 * ����pk_wa_class��Setter����.��������н�ʷ��� ��������:2017-6-17
	 * 
	 * @param newPk_wa_class
	 *            java.lang.String
	 */
	public void setPk_wa_class(java.lang.String newPk_wa_class) {
		this.pk_wa_class = newPk_wa_class;
	}

	/**
	 * ���� cyear��Getter����.�������������� ��������:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCyear() {
		return cyear;
	}

	/**
	 * ����cyear��Setter����.�������������� ��������:2017-6-17
	 * 
	 * @param newCyear
	 *            java.lang.String
	 */
	public void setCyear(java.lang.String newCyear) {
		this.cyear = newCyear;
	}

	/**
	 * ���� cperiod��Getter����.������������¶� ��������:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCperiod() {
		return cperiod;
	}

	/**
	 * ����cperiod��Setter����.������������¶� ��������:2017-6-17
	 * 
	 * @param newCperiod
	 *            java.lang.String
	 */
	public void setCperiod(java.lang.String newCperiod) {
		this.cperiod = newCperiod;
	}

	/**
	 * ���� pk_group��Getter����.���������������� ��������:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group() {
		return pk_group;
	}

	/**
	 * ����pk_group��Setter����.���������������� ��������:2017-6-17
	 * 
	 * @param newPk_group
	 *            java.lang.String
	 */
	public void setPk_group(java.lang.String newPk_group) {
		this.pk_group = newPk_group;
	}

	/**
	 * ���� pk_org��Getter����.��������������֯ ��������:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org() {
		return pk_org;
	}

	/**
	 * ����pk_org��Setter����.��������������֯ ��������:2017-6-17
	 * 
	 * @param newPk_org
	 *            java.lang.String
	 */
	public void setPk_org(java.lang.String newPk_org) {
		this.pk_org = newPk_org;
	}

	/**
	 * ���� pk_impwadata_b��Getter����.���������ӱ����� ��������:2017-6-17
	 * 
	 * @return nc.vo.hrwa.impwadata.WaDataBodyVO[]
	 */
	public nc.vo.hrwa.impwadata.WaDataBodyVO[] getPk_impwadata_b() {
		return pk_impwadata_b;
	}

	/**
	 * ����pk_impwadata_b��Setter����.���������ӱ����� ��������:2017-6-17
	 * 
	 * @param newPk_impwadata_b
	 *            nc.vo.hrwa.impwadata.WaDataBodyVO[]
	 */
	public void setPk_impwadata_b(
			nc.vo.hrwa.impwadata.WaDataBodyVO[] newPk_impwadata_b) {
		this.pk_impwadata_b = newPk_impwadata_b;
	}

	/**
	 * ���� pk_impwadata��Getter����.���������������� ��������:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_impwadata() {
		return pk_impwadata;
	}

	/**
	 * ����pk_impwadata��Setter����.���������������� ��������:2017-6-17
	 * 
	 * @param newPk_impwadata
	 *            java.lang.String
	 */
	public void setPk_impwadata(java.lang.String newPk_impwadata) {
		this.pk_impwadata = newPk_impwadata;
	}

	/**
	 * ���� cuserid��Getter����.������������Ա ��������:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCuserid() {
		return cuserid;
	}

	/**
	 * ����cuserid��Setter����.������������Ա ��������:2017-6-17
	 * 
	 * @param newCuserid
	 *            java.lang.String
	 */
	public void setCuserid(java.lang.String newCuserid) {
		this.cuserid = newCuserid;
	}

	/**
	 * ���� dr��Getter����.��������dr ��������:2017-6-17
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr() {
		return dr;
	}

	/**
	 * ����dr��Setter����.��������dr ��������:2017-6-17
	 * 
	 * @param newDr
	 *            java.lang.Integer
	 */
	public void setDr(java.lang.Integer newDr) {
		this.dr = newDr;
	}

	/**
	 * ���� ts��Getter����.��������ts ��������:2017-6-17
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}

	/**
	 * ����ts��Setter����.��������ts ��������:2017-6-17
	 * 
	 * @param newTs
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(nc.vo.pub.lang.UFDateTime newTs) {
		this.ts = newTs;
	}

	/**
	 * <p>
	 * ȡ�ø�VO�����ֶ�.
	 * <p>
	 * ��������:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {
		return null;
	}

	/**
	 * <p>
	 * ȡ�ñ�����.
	 * <p>
	 * ��������:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {

		return "pk_impwadata";
	}

	/**
	 * <p>
	 * ���ر�����
	 * <p>
	 * ��������:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "wa_data_h";
	}

	/**
	 * <p>
	 * ���ر�����.
	 * <p>
	 * ��������:2017-6-17
	 * 
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "wa_data_h";
	}

	/**
	 * ����Ĭ�Ϸ�ʽ����������.
	 * 
	 * ��������:2017-6-17
	 */
	public WaDataHeadVO() {
		super();
	}

	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.hrwa.impwadata.WaDataHeadVO")
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("hrwa.WaDataHeadVO");

	}

}