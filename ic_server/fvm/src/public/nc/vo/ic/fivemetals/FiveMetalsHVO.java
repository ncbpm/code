package nc.vo.ic.fivemetals;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 * �˴�����۵�������Ϣ
 * </p>
 * ��������:2017-6-25
 * 
 * @author
 * @version NCPrj ??
 */

public class FiveMetalsHVO extends SuperVO {

	/**
	 * s
	 */
	private static final long serialVersionUID = 8319444796567444391L;
	/**
	 * ����
	 */
	public java.lang.String pk_group;
	/**
	 * ��֯
	 */
	public java.lang.String pk_org;
	/**
	 * ��֯�汾
	 */
	public java.lang.String pk_org_v;
	/**
	 * ����
	 */
	public java.lang.String pk_fivemetals_h;
	/**
	 * ���
	 */
	public java.lang.String code;
	/**
	 * ����
	 */
	public java.lang.String name;
	/**
	 * ��Ƭ��
	 */
	public java.lang.String vcardno;
	/**
	 * ��Ŀ
	 */
	public java.lang.String vproject;
	/**
	 * ����
	 */
	public java.lang.String vdepartment;
	/**
	 * ��ע
	 */
	public java.lang.String vremark;
	/**
	 * ��������
	 */
	public java.lang.Integer vcardtype;
	/**
	 * �Զ�����1
	 */
	public java.lang.String def1;
	/**
	 * �Զ�����2
	 */
	public java.lang.String def2;
	/**
	 * �Զ�����3
	 */
	public java.lang.String def3;
	/**
	 * �Զ�����4
	 */
	public nc.vo.pub.lang.UFDouble def4;
	/**
	 * �Զ�����5
	 */
	public nc.vo.pub.lang.UFDouble def5;
	/**
	 * �Զ�����6
	 */
	public nc.vo.pub.lang.UFDouble def6;
	/**
	 * �Զ�����7
	 */
	public Boolean def7;
	/**
	 * �Զ�����8
	 */
	public Boolean def8;
	/**
	 * �Զ�����9
	 */
	public UFDate def9;
	/**
	 * �Զ�����10
	 */
	public UFDate def10;
	/**
	 * ��������
	 */
	public UFDate dbilldate;
	/**
	 * ��������
	 */
	public java.lang.String pk_billtype;
	/**
	 * ������
	 */
	public java.lang.String creator;
	/**
	 * ����ʱ��
	 */
	public UFDateTime creationtime;
	/**
	 * �޸���
	 */
	public java.lang.String modifier;
	/**
	 * �޸�ʱ��
	 */
	public UFDateTime modifiedtime;
	/**
	 * ״̬
	 */
	public java.lang.Integer vbillstatus;
	/**
	 * ʱ���
	 */
	public UFDateTime ts;

	/**
	 * ���� pk_group��Getter����.������������ ��������:2017-6-25
	 * 
	 * @return nc.vo.org.GroupVO
	 */
	public java.lang.String getPk_group() {
		return this.pk_group;
	}

	/**
	 * ����pk_group��Setter����.������������ ��������:2017-6-25
	 * 
	 * @param newPk_group
	 *            nc.vo.org.GroupVO
	 */
	public void setPk_group(java.lang.String pk_group) {
		this.pk_group = pk_group;
	}

	/**
	 * ���� pk_org��Getter����.����������֯ ��������:2017-6-25
	 * 
	 * @return nc.vo.org.OrgVO
	 */
	public java.lang.String getPk_org() {
		return this.pk_org;
	}

	/**
	 * ����pk_org��Setter����.����������֯ ��������:2017-6-25
	 * 
	 * @param newPk_org
	 *            nc.vo.org.OrgVO
	 */
	public void setPk_org(java.lang.String pk_org) {
		this.pk_org = pk_org;
	}

	/**
	 * ���� pk_org_v��Getter����.����������֯�汾 ��������:2017-6-25
	 * 
	 * @return nc.vo.vorg.OrgVersionVO
	 */
	public java.lang.String getPk_org_v() {
		return this.pk_org_v;
	}

	/**
	 * ����pk_org_v��Setter����.����������֯�汾 ��������:2017-6-25
	 * 
	 * @param newPk_org_v
	 *            nc.vo.vorg.OrgVersionVO
	 */
	public void setPk_org_v(java.lang.String pk_org_v) {
		this.pk_org_v = pk_org_v;
	}

	/**
	 * ���� pk_fivemetals_h��Getter����.������������ ��������:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_fivemetals_h() {
		return this.pk_fivemetals_h;
	}

	/**
	 * ����pk_fivemetals_h��Setter����.������������ ��������:2017-6-25
	 * 
	 * @param newPk_fivemetals_h
	 *            java.lang.String
	 */
	public void setPk_fivemetals_h(java.lang.String pk_fivemetals_h) {
		this.pk_fivemetals_h = pk_fivemetals_h;
	}

	/**
	 * ���� code��Getter����.����������� ��������:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCode() {
		return this.code;
	}

	/**
	 * ����code��Setter����.����������� ��������:2017-6-25
	 * 
	 * @param newCode
	 *            java.lang.String
	 */
	public void setCode(java.lang.String code) {
		this.code = code;
	}

	/**
	 * ���� name��Getter����.������������ ��������:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getName() {
		return this.name;
	}

	/**
	 * ����name��Setter����.������������ ��������:2017-6-25
	 * 
	 * @param newName
	 *            java.lang.String
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * ���� vcardno��Getter����.����������Ƭ�� ��������:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVcardno() {
		return this.vcardno;
	}

	/**
	 * ����vcardno��Setter����.����������Ƭ�� ��������:2017-6-25
	 * 
	 * @param newVcardno
	 *            java.lang.String
	 */
	public void setVcardno(java.lang.String vcardno) {
		this.vcardno = vcardno;
	}

	/**
	 * ���� vproject��Getter����.����������Ŀ ��������:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVproject() {
		return this.vproject;
	}

	/**
	 * ����vproject��Setter����.����������Ŀ ��������:2017-6-25
	 * 
	 * @param newVproject
	 *            java.lang.String
	 */
	public void setVproject(java.lang.String vproject) {
		this.vproject = vproject;
	}

	/**
	 * ���� vdepartment��Getter����.������������ ��������:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVdepartment() {
		return this.vdepartment;
	}

	/**
	 * ����vdepartment��Setter����.������������ ��������:2017-6-25
	 * 
	 * @param newVdepartment
	 *            java.lang.String
	 */
	public void setVdepartment(java.lang.String vdepartment) {
		this.vdepartment = vdepartment;
	}

	/**
	 * ���� vremark��Getter����.����������ע ��������:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVremark() {
		return this.vremark;
	}

	/**
	 * ����vremark��Setter����.����������ע ��������:2017-6-25
	 * 
	 * @param newVremark
	 *            java.lang.String
	 */
	public void setVremark(java.lang.String vremark) {
		this.vremark = vremark;
	}

	/**
	 * ���� vcardtype��Getter����.���������������� ��������:2017-6-25
	 * 
	 * @return nc.vo.ic.fivemetals.CardTypeEnum
	 */
	public java.lang.Integer getVcardtype() {
		return this.vcardtype;
	}

	/**
	 * ����vcardtype��Setter����.���������������� ��������:2017-6-25
	 * 
	 * @param newVcardtype
	 *            nc.vo.ic.fivemetals.CardTypeEnum
	 */
	public void setVcardtype(java.lang.Integer vcardtype) {
		this.vcardtype = vcardtype;
	}

	/**
	 * ���� def1��Getter����.���������Զ�����1 ��������:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef1() {
		return this.def1;
	}

	/**
	 * ����def1��Setter����.���������Զ�����1 ��������:2017-6-25
	 * 
	 * @param newDef1
	 *            java.lang.String
	 */
	public void setDef1(java.lang.String def1) {
		this.def1 = def1;
	}

	/**
	 * ���� def2��Getter����.���������Զ�����2 ��������:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef2() {
		return this.def2;
	}

	/**
	 * ����def2��Setter����.���������Զ�����2 ��������:2017-6-25
	 * 
	 * @param newDef2
	 *            java.lang.String
	 */
	public void setDef2(java.lang.String def2) {
		this.def2 = def2;
	}

	/**
	 * ���� def3��Getter����.���������Զ�����3 ��������:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef3() {
		return this.def3;
	}

	/**
	 * ����def3��Setter����.���������Զ�����3 ��������:2017-6-25
	 * 
	 * @param newDef3
	 *            java.lang.String
	 */
	public void setDef3(java.lang.String def3) {
		this.def3 = def3;
	}

	/**
	 * ���� def4��Getter����.���������Զ�����4 ��������:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDef4() {
		return this.def4;
	}

	/**
	 * ����def4��Setter����.���������Զ�����4 ��������:2017-6-25
	 * 
	 * @param newDef4
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setDef4(nc.vo.pub.lang.UFDouble def4) {
		this.def4 = def4;
	}

	/**
	 * ���� def5��Getter����.���������Զ�����5 ��������:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDef5() {
		return this.def5;
	}

	/**
	 * ����def5��Setter����.���������Զ�����5 ��������:2017-6-25
	 * 
	 * @param newDef5
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setDef5(nc.vo.pub.lang.UFDouble def5) {
		this.def5 = def5;
	}

	/**
	 * ���� def6��Getter����.���������Զ�����6 ��������:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDef6() {
		return this.def6;
	}

	/**
	 * ����def6��Setter����.���������Զ�����6 ��������:2017-6-25
	 * 
	 * @param newDef6
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setDef6(nc.vo.pub.lang.UFDouble def6) {
		this.def6 = def6;
	}

	/**
	 * ���� def7��Getter����.���������Զ�����7 ��������:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public Boolean getDef7() {
		return this.def7;
	}

	/**
	 * ����def7��Setter����.���������Զ�����7 ��������:2017-6-25
	 * 
	 * @param newDef7
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setDef7(Boolean def7) {
		this.def7 = def7;
	}

	/**
	 * ���� def8��Getter����.���������Զ�����8 ��������:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public Boolean getDef8() {
		return this.def8;
	}

	/**
	 * ����def8��Setter����.���������Զ�����8 ��������:2017-6-25
	 * 
	 * @param newDef8
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setDef8(Boolean def8) {
		this.def8 = def8;
	}

	/**
	 * ���� def9��Getter����.���������Զ�����9 ��������:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getDef9() {
		return this.def9;
	}

	/**
	 * ����def9��Setter����.���������Զ�����9 ��������:2017-6-25
	 * 
	 * @param newDef9
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setDef9(UFDate def9) {
		this.def9 = def9;
	}

	/**
	 * ���� def10��Getter����.���������Զ�����10 ��������:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getDef10() {
		return this.def10;
	}

	/**
	 * ����def10��Setter����.���������Զ�����10 ��������:2017-6-25
	 * 
	 * @param newDef10
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setDef10(UFDate def10) {
		this.def10 = def10;
	}

	/**
	 * ���� dbilldate��Getter����.���������������� ��������:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getDbilldate() {
		return this.dbilldate;
	}

	/**
	 * ����dbilldate��Setter����.���������������� ��������:2017-6-25
	 * 
	 * @param newDbilldate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}

	/**
	 * ���� pk_billtype��Getter����.���������������� ��������:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_billtype() {
		return this.pk_billtype;
	}

	/**
	 * ����pk_billtype��Setter����.���������������� ��������:2017-6-25
	 * 
	 * @param newPk_billtype
	 *            java.lang.String
	 */
	public void setPk_billtype(java.lang.String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}

	/**
	 * ���� creator��Getter����.�������������� ��������:2017-6-25
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getCreator() {
		return this.creator;
	}

	/**
	 * ����creator��Setter����.�������������� ��������:2017-6-25
	 * 
	 * @param newCreator
	 *            nc.vo.sm.UserVO
	 */
	public void setCreator(java.lang.String creator) {
		this.creator = creator;
	}

	/**
	 * ���� creationtime��Getter����.������������ʱ�� ��������:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getCreationtime() {
		return this.creationtime;
	}

	/**
	 * ����creationtime��Setter����.������������ʱ�� ��������:2017-6-25
	 * 
	 * @param newCreationtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.creationtime = creationtime;
	}

	/**
	 * ���� modifier��Getter����.���������޸��� ��������:2017-6-25
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getModifier() {
		return this.modifier;
	}

	/**
	 * ����modifier��Setter����.���������޸��� ��������:2017-6-25
	 * 
	 * @param newModifier
	 *            nc.vo.sm.UserVO
	 */
	public void setModifier(java.lang.String modifier) {
		this.modifier = modifier;
	}

	/**
	 * ���� modifiedtime��Getter����.���������޸�ʱ�� ��������:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getModifiedtime() {
		return this.modifiedtime;
	}

	/**
	 * ����modifiedtime��Setter����.���������޸�ʱ�� ��������:2017-6-25
	 * 
	 * @param newModifiedtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

	/**
	 * ���� vbillstatus��Getter����.��������״̬ ��������:2017-6-25
	 * 
	 * @return nc.vo.ic.fivemetals.CardStatusEnum
	 */
	public java.lang.Integer getVbillstatus() {
		return this.vbillstatus;
	}

	/**
	 * ����vbillstatus��Setter����.��������״̬ ��������:2017-6-25
	 * 
	 * @param newVbillstatus
	 *            nc.vo.ic.fivemetals.CardStatusEnum
	 */
	public void setVbillstatus(java.lang.Integer vbillstatus) {
		this.vbillstatus = vbillstatus;
	}

	/**
	 * ���� ����ʱ�����Getter����.��������ʱ��� ��������:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * ��������ʱ�����Setter����.��������ʱ��� ��������:2017-6-25
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("ic.FiveMetalsHVO");
	}
}
