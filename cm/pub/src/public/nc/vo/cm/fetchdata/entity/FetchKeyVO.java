/**
 * 
 */
package nc.vo.cm.fetchdata.entity;

/**
 * @since v6.33
 * @version 2014-11-12 上午9:22:21
 * @author zhangweix
 */
public class FetchKeyVO implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = 1L;
	private String pk_org;// 工厂
	private Integer ifetchobjtype;// 取数对象

	private String fator1;
	private String fator2;
	private String fator3;
	private String fator4;
	private String fator5;
	private String fator6;
	private String fator7;
	private String fator8;
	private String fator9;
	private String fator10;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof FetchKeyVO)) {
			return false;
		}
		FetchKeyVO vo = (FetchKeyVO) obj;

		if (this.getPk_org() == null ? vo.getPk_org() != null : !this
				.getPk_org().equals(vo.getPk_org())) {
			return false;
		}
		if (this.getIfetchobjtype() == null ? vo.getIfetchobjtype() != null
				: !this.getIfetchobjtype().equals(vo.getIfetchobjtype())) {
			return false;
		}

		if (this.getFator1() == null ? vo.getFator1() != null : !this
				.getFator1().equals(vo.getFator1())) {
			return false;
		}
		if (this.getFator2() == null ? vo.getFator2() != null : !this
				.getFator2().equals(vo.getFator2())) {
			return false;
		}
		if (this.getFator3() == null ? vo.getFator3() != null : !this
				.getFator3().equals(vo.getFator3())) {
			return false;
		}
		if (this.getFator4() == null ? vo.getFator4() != null : !this
				.getFator4().equals(vo.getFator4())) {
			return false;
		}
		if (this.getFator5() == null ? vo.getFator5() != null : !this
				.getFator5().equals(vo.getFator5())) {
			return false;
		}
		if (this.getFator6() == null ? vo.getFator6() != null : !this
				.getFator6().equals(vo.getFator6())) {
			return false;
		}
		if (this.getFator7() == null ? vo.getFator7() != null : !this
				.getFator7().equals(vo.getFator7())) {
			return false;
		}
		if (this.getFator8() == null ? vo.getFator8() != null : !this
				.getFator8().equals(vo.getFator8())) {
			return false;
		}
		if (this.getFator9() == null ? vo.getFator9() != null : !this
				.getFator9().equals(vo.getFator9())) {
			return false;
		}
		if (this.getFator10() == null ? vo.getFator10() != null : !this
				.getFator10().equals(vo.getFator10())) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int hashcode = 17;
		String pk_org = this.getPk_org();
		Integer ifetchobjtype = this.getIfetchobjtype();

		hashcode = pk_org == null ? 31 * hashcode : 31 * hashcode
				+ pk_org.hashCode();
		hashcode = ifetchobjtype == null ? 31 * hashcode : 31 * hashcode
				+ ifetchobjtype.hashCode();

		hashcode = fator1 == null ? 31 * hashcode : 31 * hashcode
				+ fator1.hashCode();
		hashcode = fator2 == null ? 31 * hashcode : 31 * hashcode
				+ fator2.hashCode();
		hashcode = fator3 == null ? 31 * hashcode : 31 * hashcode
				+ fator3.hashCode();
		hashcode = fator4 == null ? 31 * hashcode : 31 * hashcode
				+ fator4.hashCode();
		hashcode = fator5 == null ? 31 * hashcode : 31 * hashcode
				+ fator5.hashCode();
		hashcode = fator6 == null ? 31 * hashcode : 31 * hashcode
				+ fator6.hashCode();
		hashcode = fator7 == null ? 31 * hashcode : 31 * hashcode
				+ fator7.hashCode();
		hashcode = fator8 == null ? 31 * hashcode : 31 * hashcode
				+ fator8.hashCode();
		hashcode = fator9 == null ? 31 * hashcode : 31 * hashcode
				+ fator9.hashCode();
		hashcode = fator10 == null ? 31 * hashcode : 31 * hashcode
				+ fator10.hashCode();

		return hashcode;
	}

	/**
	 * 获得 pk_org 的属性值
	 * 
	 * @return the pk_org
	 * @since 2014-11-12
	 * @author zhangweix
	 */
	public String getPk_org() {
		return this.pk_org;
	}

	/**
	 * 设置 pk_org 的属性值
	 * 
	 * @param pk_org
	 *            the pk_org to set
	 * @since 2014-11-12
	 * @author zhangweix
	 */
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	/**
	 * 获得 ifetchobjtype 的属性值
	 * 
	 * @return the ifetchobjtype
	 * @since 2014-11-12
	 * @author zhangweix
	 */
	public Integer getIfetchobjtype() {
		return this.ifetchobjtype;
	}

	/**
	 * 设置 ifetchobjtype 的属性值
	 * 
	 * @param ifetchobjtype
	 *            the ifetchobjtype to set
	 * @since 2014-11-12
	 * @author zhangweix
	 */
	public void setIfetchobjtype(Integer ifetchobjtype) {
		this.ifetchobjtype = ifetchobjtype;
	}


	public String getFator1() {
		return fator1;
	}

	public void setFator1(String fator1) {
		this.fator1 = fator1;
	}

	public String getFator2() {
		return fator2;
	}

	public void setFator2(String fator2) {
		this.fator2 = fator2;
	}

	public String getFator3() {
		return fator3;
	}

	public void setFator3(String fator3) {
		this.fator3 = fator3;
	}

	public String getFator4() {
		return fator4;
	}

	public void setFator4(String fator4) {
		this.fator4 = fator4;
	}

	public String getFator5() {
		return fator5;
	}

	public void setFator5(String fator5) {
		this.fator5 = fator5;
	}

	public String getFator6() {
		return fator6;
	}

	public void setFator6(String fator6) {
		this.fator6 = fator6;
	}

	public String getFator7() {
		return fator7;
	}

	public void setFator7(String fator7) {
		this.fator7 = fator7;
	}

	public String getFator8() {
		return fator8;
	}

	public void setFator8(String fator8) {
		this.fator8 = fator8;
	}

	public String getFator9() {
		return fator9;
	}

	public void setFator9(String fator9) {
		this.fator9 = fator9;
	}

	public String getFator10() {
		return fator10;
	}

	public void setFator10(String fator10) {
		this.fator10 = fator10;
	}

}
