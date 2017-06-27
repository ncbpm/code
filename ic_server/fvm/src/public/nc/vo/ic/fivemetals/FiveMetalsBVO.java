package nc.vo.ic.fivemetals;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
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

public class FiveMetalsBVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1490228470089114347L;
	/**
	 * 行号
	 */
	public java.lang.String rowno;
	/**
	 * 金额
	 */
	public nc.vo.pub.lang.UFDouble nmny;
	/**
	 * 费用类型
	 */
	public java.lang.Integer itype;
	/**
	 * 备注
	 */
	public java.lang.String vremark;
	/**
	 * 来源单据类型
	 */
	public java.lang.String vsourcetype;
	/**
	 * 来源单据id
	 */
	public java.lang.String vsourcebillid;
	/**
	 * 来源单据号
	 */
	public java.lang.String vsourcebillno;
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
	public UFBoolean def7;
	/**
	 * 自定义项8
	 */
	public UFBoolean def8;
	/**
	 * 自定义项9
	 */
	public UFDate def9;
	/**
	 * 自定义项10
	 */
	public UFDate def10;
	/**
	 * 子表主键
	 */
	public java.lang.String pk_fivemetals_b;
	/**
	 * 制单时间
	 */
	public UFDateTime maketime;
	/**
	 * 最后修改时间
	 */
	public UFDateTime lastmaketime;
	/**
	 * 制单人
	 */
	public java.lang.String creator;
	/**
	 * 月份
	 */
	public java.lang.String cperiod;
	/**
	 * 上层单据主键
	 */
	public String pk_fivemetals_h;
	/**
	 * 时间戳
	 */
	public UFDateTime ts;

	/**
	 * 属性 rowno的Getter方法.属性名：行号 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getRowno() {
		return this.rowno;
	}

	/**
	 * 属性rowno的Setter方法.属性名：行号 创建日期:2017-6-25
	 * 
	 * @param newRowno
	 *            java.lang.String
	 */
	public void setRowno(java.lang.String rowno) {
		this.rowno = rowno;
	}

	/**
	 * 属性 nmny的Getter方法.属性名：金额 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getNmny() {
		return this.nmny;
	}

	/**
	 * 属性nmny的Setter方法.属性名：金额 创建日期:2017-6-25
	 * 
	 * @param newNmny
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setNmny(nc.vo.pub.lang.UFDouble nmny) {
		this.nmny = nmny;
	}

	/**
	 * 属性 itype的Getter方法.属性名：费用类型 创建日期:2017-6-25
	 * 
	 * @return nc.vo.ic.fivemetals.CostTypeEnum
	 */
	public java.lang.Integer getItype() {
		return this.itype;
	}

	/**
	 * 属性itype的Setter方法.属性名：费用类型 创建日期:2017-6-25
	 * 
	 * @param newItype
	 *            nc.vo.ic.fivemetals.CostTypeEnum
	 */
	public void setItype(java.lang.Integer itype) {
		this.itype = itype;
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
	 * 属性 vsourcetype的Getter方法.属性名：来源单据类型 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVsourcetype() {
		return this.vsourcetype;
	}

	/**
	 * 属性vsourcetype的Setter方法.属性名：来源单据类型 创建日期:2017-6-25
	 * 
	 * @param newVsourcetype
	 *            java.lang.String
	 */
	public void setVsourcetype(java.lang.String vsourcetype) {
		this.vsourcetype = vsourcetype;
	}

	/**
	 * 属性 vsourcebillid的Getter方法.属性名：来源单据id 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVsourcebillid() {
		return this.vsourcebillid;
	}

	/**
	 * 属性vsourcebillid的Setter方法.属性名：来源单据id 创建日期:2017-6-25
	 * 
	 * @param newVsourcebillid
	 *            java.lang.String
	 */
	public void setVsourcebillid(java.lang.String vsourcebillid) {
		this.vsourcebillid = vsourcebillid;
	}

	/**
	 * 属性 vsourcebillno的Getter方法.属性名：来源单据号 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVsourcebillno() {
		return this.vsourcebillno;
	}

	/**
	 * 属性vsourcebillno的Setter方法.属性名：来源单据号 创建日期:2017-6-25
	 * 
	 * @param newVsourcebillno
	 *            java.lang.String
	 */
	public void setVsourcebillno(java.lang.String vsourcebillno) {
		this.vsourcebillno = vsourcebillno;
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
	public UFBoolean getDef7() {
		return this.def7;
	}

	/**
	 * 属性def7的Setter方法.属性名：自定义项7 创建日期:2017-6-25
	 * 
	 * @param newDef7
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setDef7(UFBoolean def7) {
		this.def7 = def7;
	}

	/**
	 * 属性 def8的Getter方法.属性名：自定义项8 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public UFBoolean getDef8() {
		return this.def8;
	}

	/**
	 * 属性def8的Setter方法.属性名：自定义项8 创建日期:2017-6-25
	 * 
	 * @param newDef8
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setDef8(UFBoolean def8) {
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
	 * 属性 pk_fivemetals_b的Getter方法.属性名：子表主键 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_fivemetals_b() {
		return this.pk_fivemetals_b;
	}

	/**
	 * 属性pk_fivemetals_b的Setter方法.属性名：子表主键 创建日期:2017-6-25
	 * 
	 * @param newPk_fivemetals_b
	 *            java.lang.String
	 */
	public void setPk_fivemetals_b(java.lang.String pk_fivemetals_b) {
		this.pk_fivemetals_b = pk_fivemetals_b;
	}

	/**
	 * 属性 maketime的Getter方法.属性名：制单时间 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getMaketime() {
		return this.maketime;
	}

	/**
	 * 属性maketime的Setter方法.属性名：制单时间 创建日期:2017-6-25
	 * 
	 * @param newMaketime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setMaketime(UFDateTime maketime) {
		this.maketime = maketime;
	}

	/**
	 * 属性 lastmaketime的Getter方法.属性名：最后修改时间 创建日期:2017-6-25
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getLastmaketime() {
		return this.lastmaketime;
	}

	/**
	 * 属性lastmaketime的Setter方法.属性名：最后修改时间 创建日期:2017-6-25
	 * 
	 * @param newLastmaketime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setLastmaketime(UFDateTime lastmaketime) {
		this.lastmaketime = lastmaketime;
	}

	/**
	 * 属性 creator的Getter方法.属性名：制单人 创建日期:2017-6-25
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getCreator() {
		return this.creator;
	}

	/**
	 * 属性creator的Setter方法.属性名：制单人 创建日期:2017-6-25
	 * 
	 * @param newCreator
	 *            nc.vo.sm.UserVO
	 */
	public void setCreator(java.lang.String creator) {
		this.creator = creator;
	}

	/**
	 * 属性 cperiod的Getter方法.属性名：月份 创建日期:2017-6-25
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCperiod() {
		return this.cperiod;
	}

	/**
	 * 属性cperiod的Setter方法.属性名：月份 创建日期:2017-6-25
	 * 
	 * @param newCperiod
	 *            java.lang.String
	 */
	public void setCperiod(java.lang.String cperiod) {
		this.cperiod = cperiod;
	}

	/**
	 * 属性 生成上层主键的Getter方法.属性名：上层主键 创建日期:2017-6-25
	 * 
	 * @return String
	 */
	public String getPk_fivemetals_h() {
		return this.pk_fivemetals_h;
	}

	/**
	 * 属性生成上层主键的Setter方法.属性名：上层主键 创建日期:2017-6-25
	 * 
	 * @param newPk_fivemetals_h
	 *            String
	 */
	public void setPk_fivemetals_h(String pk_fivemetals_h) {
		this.pk_fivemetals_h = pk_fivemetals_h;
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
		return VOMetaFactory.getInstance().getVOMeta("ic.FiveMetalsBVO");
	}
}
