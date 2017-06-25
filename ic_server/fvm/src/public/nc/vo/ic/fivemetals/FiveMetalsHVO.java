package nc.vo.ic.fivemetals;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 * 此处添加累的描述信息
 * </p>
 * 创建日期:2017-6-25
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
	 * 集团
	 */
	public java.lang.String pk_group;
	/**
	 * 组织
	 */
	public java.lang.String pk_org;
	/**
	 * 组织版本
	 */
	public java.lang.String pk_org_v;
	/**
	 * 主键
	 */
	public java.lang.String pk_fivemetals_h;
	/**
	 * 编号
	 */
	public java.lang.String code;
	/**
	 * 名称
	 */
	public java.lang.String name;
	/**
	 * 卡片号
	 */
	public java.lang.String vcardno;
	/**
	 * 项目
	 */
	public java.lang.String vproject;
	/**
	 * 部门
	 */
	public java.lang.String vdepartment;
	/**
	 * 备注
	 */
	public java.lang.String vremark;
	/**
	 * 卡号类型
	 */
	public java.lang.Integer vcardtype;
	/**
	 * 自定义项1
	 */
	public java.lang.String def1;
	/**
	 * 自定义项2
	 */
	public java.lang.String def2;
	/**
	 * 自定义项3
	 */
	public java.lang.String def3;
	/**
	 * 自定义项4
	 */
	public nc.vo.pub.lang.UFDouble def4;
	/**
	 * 自定义项5
	 */
	public nc.vo.pub.lang.UFDouble def5;
	/**
	 * 自定义项6
	 */
	public nc.vo.pub.lang.UFDouble def6;
	/**
	 * 自定义项7
	 */
	public Boolean def7;
	/**
	 * 自定义项8
	 */
	public Boolean def8;
	/**
	 * 自定义项9
	 */
	public UFDate def9;
	/**
	 * 自定义项10
	 */
	public UFDate def10;
	/**
	 * 单据日期
	 */
	public UFDate dbilldate;
	/**
	 * 单据类型
	 */
	public java.lang.String pk_billtype;
	/**
	 * 创建人
	 */
	public java.lang.String creator;
	/**
	 * 创建时间
	 */
	public UFDateTime creationtime;
	/**
	 * 修改人
	 */
	public java.lang.String modifier;
	/**
	 * 修改时间
	 */
	public UFDateTime modifiedtime;
	/**
	 * 状态
	 */
	public java.lang.Integer vbillstatus;
	/**
	 * 时间戳
	 */
	public UFDateTime ts;

	/**
	 * 属性 pk_group的Getter方法.属性名：集团 创建日期:2017-6-25
	 * 
	 * @return nc.vo.org.GroupVO
	 */
	public java.lang.String getPk_group() {
		return this.pk_group;
	}

	/**
	 * 属性pk_group的Setter方法.属性名：集团 创建日期:2017-6-25
	 * 
	 * @param newPk_group
	 *            nc.vo.org.GroupVO
	 */
	public void setPk_group(java.lang.String pk_group) {
		this.pk_group = pk_group;
	}

	/**
	 * 属性 pk_org的Getter方法.属性名：组织 创建日期:2017-6-25
	 * 
	 * @return nc.vo.org.OrgVO
	 */
	public java.lang.String getPk_org() {
		return this.pk_org;
	}

	/**
	 * 属性pk_org的Setter方法.属性名：组织 创建日期:2017-6-25
	 * 
	 * @param newPk_org
	 *            nc.vo.org.OrgVO
	 */
	public void setPk_org(java.lang.String pk_org) {
		this.pk_org = pk_org;
	}

	/**
	 * 属性 pk_org_v的Getter方法.属性名：组织版本 创建日期:2017-6-25
	 * 
	 * @return nc.vo.vorg.OrgVersionVO
	 */
	public java.lang.String getPk_org_v() {
		return this.pk_org_v;
	}

	/**
	 * 属性pk_org_v的Setter方法.属性名：组织版本 创建日期:2017-6-25
	 * 
	 * @param newPk_org_v
	 *            nc.vo.vorg.OrgVersionVO
	 */
	public void setPk_org_v(java.lang.String pk_org_v) {
		this.pk_org_v = pk_org_v;
	}

	/**
	 * 属性 pk_fivemetals_h的Getter方法.属性名：主键 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_fivemetals_h() {
		return this.pk_fivemetals_h;
	}

	/**
	 * 属性pk_fivemetals_h的Setter方法.属性名：主键 创建日期:2017-6-25
	 * 
	 * @param newPk_fivemetals_h
	 *            java.lang.String
	 */
	public void setPk_fivemetals_h(java.lang.String pk_fivemetals_h) {
		this.pk_fivemetals_h = pk_fivemetals_h;
	}

	/**
	 * 属性 code的Getter方法.属性名：编号 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCode() {
		return this.code;
	}

	/**
	 * 属性code的Setter方法.属性名：编号 创建日期:2017-6-25
	 * 
	 * @param newCode
	 *            java.lang.String
	 */
	public void setCode(java.lang.String code) {
		this.code = code;
	}

	/**
	 * 属性 name的Getter方法.属性名：名称 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getName() {
		return this.name;
	}

	/**
	 * 属性name的Setter方法.属性名：名称 创建日期:2017-6-25
	 * 
	 * @param newName
	 *            java.lang.String
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * 属性 vcardno的Getter方法.属性名：卡片号 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVcardno() {
		return this.vcardno;
	}

	/**
	 * 属性vcardno的Setter方法.属性名：卡片号 创建日期:2017-6-25
	 * 
	 * @param newVcardno
	 *            java.lang.String
	 */
	public void setVcardno(java.lang.String vcardno) {
		this.vcardno = vcardno;
	}

	/**
	 * 属性 vproject的Getter方法.属性名：项目 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVproject() {
		return this.vproject;
	}

	/**
	 * 属性vproject的Setter方法.属性名：项目 创建日期:2017-6-25
	 * 
	 * @param newVproject
	 *            java.lang.String
	 */
	public void setVproject(java.lang.String vproject) {
		this.vproject = vproject;
	}

	/**
	 * 属性 vdepartment的Getter方法.属性名：部门 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVdepartment() {
		return this.vdepartment;
	}

	/**
	 * 属性vdepartment的Setter方法.属性名：部门 创建日期:2017-6-25
	 * 
	 * @param newVdepartment
	 *            java.lang.String
	 */
	public void setVdepartment(java.lang.String vdepartment) {
		this.vdepartment = vdepartment;
	}

	/**
	 * 属性 vremark的Getter方法.属性名：备注 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVremark() {
		return this.vremark;
	}

	/**
	 * 属性vremark的Setter方法.属性名：备注 创建日期:2017-6-25
	 * 
	 * @param newVremark
	 *            java.lang.String
	 */
	public void setVremark(java.lang.String vremark) {
		this.vremark = vremark;
	}

	/**
	 * 属性 vcardtype的Getter方法.属性名：卡号类型 创建日期:2017-6-25
	 * 
	 * @return nc.vo.ic.fivemetals.CardTypeEnum
	 */
	public java.lang.Integer getVcardtype() {
		return this.vcardtype;
	}

	/**
	 * 属性vcardtype的Setter方法.属性名：卡号类型 创建日期:2017-6-25
	 * 
	 * @param newVcardtype
	 *            nc.vo.ic.fivemetals.CardTypeEnum
	 */
	public void setVcardtype(java.lang.Integer vcardtype) {
		this.vcardtype = vcardtype;
	}

	/**
	 * 属性 def1的Getter方法.属性名：自定义项1 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef1() {
		return this.def1;
	}

	/**
	 * 属性def1的Setter方法.属性名：自定义项1 创建日期:2017-6-25
	 * 
	 * @param newDef1
	 *            java.lang.String
	 */
	public void setDef1(java.lang.String def1) {
		this.def1 = def1;
	}

	/**
	 * 属性 def2的Getter方法.属性名：自定义项2 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef2() {
		return this.def2;
	}

	/**
	 * 属性def2的Setter方法.属性名：自定义项2 创建日期:2017-6-25
	 * 
	 * @param newDef2
	 *            java.lang.String
	 */
	public void setDef2(java.lang.String def2) {
		this.def2 = def2;
	}

	/**
	 * 属性 def3的Getter方法.属性名：自定义项3 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef3() {
		return this.def3;
	}

	/**
	 * 属性def3的Setter方法.属性名：自定义项3 创建日期:2017-6-25
	 * 
	 * @param newDef3
	 *            java.lang.String
	 */
	public void setDef3(java.lang.String def3) {
		this.def3 = def3;
	}

	/**
	 * 属性 def4的Getter方法.属性名：自定义项4 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDef4() {
		return this.def4;
	}

	/**
	 * 属性def4的Setter方法.属性名：自定义项4 创建日期:2017-6-25
	 * 
	 * @param newDef4
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setDef4(nc.vo.pub.lang.UFDouble def4) {
		this.def4 = def4;
	}

	/**
	 * 属性 def5的Getter方法.属性名：自定义项5 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDef5() {
		return this.def5;
	}

	/**
	 * 属性def5的Setter方法.属性名：自定义项5 创建日期:2017-6-25
	 * 
	 * @param newDef5
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setDef5(nc.vo.pub.lang.UFDouble def5) {
		this.def5 = def5;
	}

	/**
	 * 属性 def6的Getter方法.属性名：自定义项6 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getDef6() {
		return this.def6;
	}

	/**
	 * 属性def6的Setter方法.属性名：自定义项6 创建日期:2017-6-25
	 * 
	 * @param newDef6
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setDef6(nc.vo.pub.lang.UFDouble def6) {
		this.def6 = def6;
	}

	/**
	 * 属性 def7的Getter方法.属性名：自定义项7 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public Boolean getDef7() {
		return this.def7;
	}

	/**
	 * 属性def7的Setter方法.属性名：自定义项7 创建日期:2017-6-25
	 * 
	 * @param newDef7
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setDef7(Boolean def7) {
		this.def7 = def7;
	}

	/**
	 * 属性 def8的Getter方法.属性名：自定义项8 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public Boolean getDef8() {
		return this.def8;
	}

	/**
	 * 属性def8的Setter方法.属性名：自定义项8 创建日期:2017-6-25
	 * 
	 * @param newDef8
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setDef8(Boolean def8) {
		this.def8 = def8;
	}

	/**
	 * 属性 def9的Getter方法.属性名：自定义项9 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getDef9() {
		return this.def9;
	}

	/**
	 * 属性def9的Setter方法.属性名：自定义项9 创建日期:2017-6-25
	 * 
	 * @param newDef9
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setDef9(UFDate def9) {
		this.def9 = def9;
	}

	/**
	 * 属性 def10的Getter方法.属性名：自定义项10 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getDef10() {
		return this.def10;
	}

	/**
	 * 属性def10的Setter方法.属性名：自定义项10 创建日期:2017-6-25
	 * 
	 * @param newDef10
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setDef10(UFDate def10) {
		this.def10 = def10;
	}

	/**
	 * 属性 dbilldate的Getter方法.属性名：单据日期 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getDbilldate() {
		return this.dbilldate;
	}

	/**
	 * 属性dbilldate的Setter方法.属性名：单据日期 创建日期:2017-6-25
	 * 
	 * @param newDbilldate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}

	/**
	 * 属性 pk_billtype的Getter方法.属性名：单据类型 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_billtype() {
		return this.pk_billtype;
	}

	/**
	 * 属性pk_billtype的Setter方法.属性名：单据类型 创建日期:2017-6-25
	 * 
	 * @param newPk_billtype
	 *            java.lang.String
	 */
	public void setPk_billtype(java.lang.String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}

	/**
	 * 属性 creator的Getter方法.属性名：创建人 创建日期:2017-6-25
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getCreator() {
		return this.creator;
	}

	/**
	 * 属性creator的Setter方法.属性名：创建人 创建日期:2017-6-25
	 * 
	 * @param newCreator
	 *            nc.vo.sm.UserVO
	 */
	public void setCreator(java.lang.String creator) {
		this.creator = creator;
	}

	/**
	 * 属性 creationtime的Getter方法.属性名：创建时间 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getCreationtime() {
		return this.creationtime;
	}

	/**
	 * 属性creationtime的Setter方法.属性名：创建时间 创建日期:2017-6-25
	 * 
	 * @param newCreationtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.creationtime = creationtime;
	}

	/**
	 * 属性 modifier的Getter方法.属性名：修改人 创建日期:2017-6-25
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getModifier() {
		return this.modifier;
	}

	/**
	 * 属性modifier的Setter方法.属性名：修改人 创建日期:2017-6-25
	 * 
	 * @param newModifier
	 *            nc.vo.sm.UserVO
	 */
	public void setModifier(java.lang.String modifier) {
		this.modifier = modifier;
	}

	/**
	 * 属性 modifiedtime的Getter方法.属性名：修改时间 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getModifiedtime() {
		return this.modifiedtime;
	}

	/**
	 * 属性modifiedtime的Setter方法.属性名：修改时间 创建日期:2017-6-25
	 * 
	 * @param newModifiedtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

	/**
	 * 属性 vbillstatus的Getter方法.属性名：状态 创建日期:2017-6-25
	 * 
	 * @return nc.vo.ic.fivemetals.CardStatusEnum
	 */
	public java.lang.Integer getVbillstatus() {
		return this.vbillstatus;
	}

	/**
	 * 属性vbillstatus的Setter方法.属性名：状态 创建日期:2017-6-25
	 * 
	 * @param newVbillstatus
	 *            nc.vo.ic.fivemetals.CardStatusEnum
	 */
	public void setVbillstatus(java.lang.Integer vbillstatus) {
		this.vbillstatus = vbillstatus;
	}

	/**
	 * 属性 生成时间戳的Getter方法.属性名：时间戳 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * 属性生成时间戳的Setter方法.属性名：时间戳 创建日期:2017-6-25
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
