package nc.vo.ic.fivemetals;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;


public class FiveMetalsBVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5169536230058131727L;
/**
	 * �к�
	 */
	public String rowno;
	/**
	 * ���
	 */
	public String nmny;
	/**
	 * ��������
	 */
	public String itype;
	/**
	 * ��ע
	 */
	public String vremark;
	/**
	 * ��Դ��������
	 */
	public String vsourcetype;
	/**
	 * ��Դ���ݺ�
	 */
	public String vsourcebillno;
	/**
	 * �Զ�����1
	 */
	public String def1;
	/**
	 * �Զ�����2
	 */
	public String def2;
	/**
	 * �Զ�����3
	 */
	public String def3;
	/**
	 * �Զ�����4
	 */
	public String def4;
	/**
	 * �Զ�����5
	 */
	public String def5;
	/**
	 * �Զ�����6
	 */
	public String def6;
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
	 * �ӱ�����
	 */
	public String pk_fivemetals_b;
	/**
	 * �Ƶ�ʱ��
	 */
	public UFDateTime maketime;
	/**
	 * ����޸�ʱ��
	 */
	public UFDateTime lastmaketime;
	/**
	 * �ϲ㵥������
	 */
	public String pk_fivemetals_h;
	/**
	 * ʱ���
	 */
	public UFDateTime ts;

	/**
	 * ���� rowno��Getter����.���������к� ��������:2017-6-14
	 * 
	 * @return java.lang.String
	 */
	public String getRowno() {
		return this.rowno;
	}

	/**
	 * ����rowno��Setter����.���������к� ��������:2017-6-14
	 * 
	 * @param newRowno
	 *            java.lang.String
	 */
	public void setRowno(String rowno) {
		this.rowno = rowno;
	}

	/**
	 * ���� nmny��Getter����.����������� ��������:2017-6-14
	 * 
	 * @return java.lang.String
	 */
	public String getNmny() {
		return this.nmny;
	}

	/**
	 * ����nmny��Setter����.����������� ��������:2017-6-14
	 * 
	 * @param newNmny
	 *            java.lang.String
	 */
	public void setNmny(String nmny) {
		this.nmny = nmny;
	}

	/**
	 * ���� itype��Getter����.���������������� ��������:2017-6-14
	 * 
	 * @return nc.vo.ic.fivemetals.CostTypeEnum
	 */
	public String getItype() {
		return this.itype;
	}

	/**
	 * ����itype��Setter����.���������������� ��������:2017-6-14
	 * 
	 * @param newItype
	 *            nc.vo.ic.fivemetals.CostTypeEnum
	 */
	public void setItype(String itype) {
		this.itype = itype;
	}

	/**
	 * ���� vremark��Getter����.����������ע ��������:2017-6-14
	 * 
	 * @return java.lang.String
	 */
	public String getVremark() {
		return this.vremark;
	}

	/**
	 * ����vremark��Setter����.����������ע ��������:2017-6-14
	 * 
	 * @param newVremark
	 *            java.lang.String
	 */
	public void setVremark(String vremark) {
		this.vremark = vremark;
	}

	/**
	 * ���� vsourcetype��Getter����.����������Դ�������� ��������:2017-6-14
	 * 
	 * @return java.lang.String
	 */
	public String getVsourcetype() {
		return this.vsourcetype;
	}

	/**
	 * ����vsourcetype��Setter����.����������Դ�������� ��������:2017-6-14
	 * 
	 * @param newVsourcetype
	 *            java.lang.String
	 */
	public void setVsourcetype(String vsourcetype) {
		this.vsourcetype = vsourcetype;
	}

	/**
	 * ���� vsourcebillno��Getter����.����������Դ���ݺ� ��������:2017-6-14
	 * 
	 * @return java.lang.String
	 */
	public String getVsourcebillno() {
		return this.vsourcebillno;
	}

	/**
	 * ����vsourcebillno��Setter����.����������Դ���ݺ� ��������:2017-6-14
	 * 
	 * @param newVsourcebillno
	 *            java.lang.String
	 */
	public void setVsourcebillno(String vsourcebillno) {
		this.vsourcebillno = vsourcebillno;
	}

	/**
	 * ���� def1��Getter����.���������Զ�����1 ��������:2017-6-14
	 * 
	 * @return java.lang.String
	 */
	public String getDef1() {
		return this.def1;
	}

	/**
	 * ����def1��Setter����.���������Զ�����1 ��������:2017-6-14
	 * 
	 * @param newDef1
	 *            java.lang.String
	 */
	public void setDef1(String def1) {
		this.def1 = def1;
	}

	/**
	 * ���� def2��Getter����.���������Զ�����2 ��������:2017-6-14
	 * 
	 * @return java.lang.String
	 */
	public String getDef2() {
		return this.def2;
	}

	/**
	 * ����def2��Setter����.���������Զ�����2 ��������:2017-6-14
	 * 
	 * @param newDef2
	 *            java.lang.String
	 */
	public void setDef2(String def2) {
		this.def2 = def2;
	}

	/**
	 * ���� def3��Getter����.���������Զ�����3 ��������:2017-6-14
	 * 
	 * @return java.lang.String
	 */
	public String getDef3() {
		return this.def3;
	}

	/**
	 * ����def3��Setter����.���������Զ�����3 ��������:2017-6-14
	 * 
	 * @param newDef3
	 *            java.lang.String
	 */
	public void setDef3(String def3) {
		this.def3 = def3;
	}

	/**
	 * ���� def4��Getter����.���������Զ�����4 ��������:2017-6-14
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public String getDef4() {
		return this.def4;
	}

	/**
	 * ����def4��Setter����.���������Զ�����4 ��������:2017-6-14
	 * 
	 * @param newDef4
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setDef4(String def4) {
		this.def4 = def4;
	}

	/**
	 * ���� def5��Getter����.���������Զ�����5 ��������:2017-6-14
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public String getDef5() {
		return this.def5;
	}

	/**
	 * ����def5��Setter����.���������Զ�����5 ��������:2017-6-14
	 * 
	 * @param newDef5
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setDef5(String def5) {
		this.def5 = def5;
	}

	/**
	 * ���� def6��Getter����.���������Զ�����6 ��������:2017-6-14
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public String getDef6() {
		return this.def6;
	}

	/**
	 * ����def6��Setter����.���������Զ�����6 ��������:2017-6-14
	 * 
	 * @param newDef6
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setDef6(String def6) {
		this.def6 = def6;
	}

	/**
	 * ���� def7��Getter����.���������Զ�����7 ��������:2017-6-14
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public Boolean getDef7() {
		return this.def7;
	}

	/**
	 * ����def7��Setter����.���������Զ�����7 ��������:2017-6-14
	 * 
	 * @param newDef7
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setDef7(Boolean def7) {
		this.def7 = def7;
	}

	/**
	 * ���� def8��Getter����.���������Զ�����8 ��������:2017-6-14
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public Boolean getDef8() {
		return this.def8;
	}

	/**
	 * ����def8��Setter����.���������Զ�����8 ��������:2017-6-14
	 * 
	 * @param newDef8
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setDef8(Boolean def8) {
		this.def8 = def8;
	}

	/**
	 * ���� def9��Getter����.���������Զ�����9 ��������:2017-6-14
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getDef9() {
		return this.def9;
	}

	/**
	 * ����def9��Setter����.���������Զ�����9 ��������:2017-6-14
	 * 
	 * @param newDef9
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setDef9(UFDate def9) {
		this.def9 = def9;
	}

	/**
	 * ���� def10��Getter����.���������Զ�����10 ��������:2017-6-14
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getDef10() {
		return this.def10;
	}

	/**
	 * ����def10��Setter����.���������Զ�����10 ��������:2017-6-14
	 * 
	 * @param newDef10
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setDef10(UFDate def10) {
		this.def10 = def10;
	}

	/**
	 * ���� pk_fivemetals_b��Getter����.���������ӱ����� ��������:2017-6-14
	 * 
	 * @return java.lang.String
	 */
	public String getPk_fivemetals_b() {
		return this.pk_fivemetals_b;
	}

	/**
	 * ����pk_fivemetals_b��Setter����.���������ӱ����� ��������:2017-6-14
	 * 
	 * @param newPk_fivemetals_b
	 *            java.lang.String
	 */
	public void setPk_fivemetals_b(String pk_fivemetals_b) {
		this.pk_fivemetals_b = pk_fivemetals_b;
	}

	/**
	 * ���� maketime��Getter����.���������Ƶ�ʱ�� ��������:2017-6-14
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getMaketime() {
		return this.maketime;
	}

	/**
	 * ����maketime��Setter����.���������Ƶ�ʱ�� ��������:2017-6-14
	 * 
	 * @param newMaketime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setMaketime(UFDateTime maketime) {
		this.maketime = maketime;
	}

	/**
	 * ���� lastmaketime��Getter����.������������޸�ʱ�� ��������:2017-6-14
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getLastmaketime() {
		return this.lastmaketime;
	}

	/**
	 * ����lastmaketime��Setter����.������������޸�ʱ�� ��������:2017-6-14
	 * 
	 * @param newLastmaketime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setLastmaketime(UFDateTime lastmaketime) {
		this.lastmaketime = lastmaketime;
	}

	/**
	 * ���� �����ϲ�������Getter����.���������ϲ����� ��������:2017-6-14
	 * 
	 * @return String
	 */
	public String getPk_fivemetals_h() {
		return this.pk_fivemetals_h;
	}

	/**
	 * ���������ϲ�������Setter����.���������ϲ����� ��������:2017-6-14
	 * 
	 * @param newPk_fivemetals_h
	 *            String
	 */
	public void setPk_fivemetals_h(String pk_fivemetals_h) {
		this.pk_fivemetals_h = pk_fivemetals_h;
	}

	/**
	 * ���� ����ʱ�����Getter����.��������ʱ��� ��������:2017-6-14
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * ��������ʱ�����Setter����.��������ʱ��� ��������:2017-6-14
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("ic.FiveMetalsBVO");
	}
}
