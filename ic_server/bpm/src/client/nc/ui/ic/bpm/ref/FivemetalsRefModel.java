package nc.ui.ic.bpm.ref;

import nc.bs.logging.Logger;
import nc.bs.sec.esapi.NCESAPI;
import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.org.DeptVO;
import nc.vo.pub.BusinessException;
import nc.vo.util.VisibleUtil;

public class FivemetalsRefModel extends AbstractRefModel {

	/**
	 * getDefaultFieldCount ����ע��.
	 */
	@Override
	public int getDefaultFieldCount() {
		return 3;
	}

	/**
	 * �������ݿ��ֶ������� ��������:(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	@Override
	public java.lang.String[] getFieldCode() {
		return new String[] { "vcardno", "name", "vdepartment",
				"pk_fivemetals_h" };
	}

	@Override
	public String[][] getFormulas() {
		String formula_banktype = "getColValue(\""
				+ DeptVO.getDefaultTableName() + "\",\"" + DeptVO.NAME
				+ "\",\"" + DeptVO.PK_DEPT + "\"," + DeptVO.PK_DEPT + ")";

		return new String[][] { { "vdepartment", formula_banktype } };
	}

	/**
	 * �����ݿ��ֶ��������Ӧ�������������� ��������:(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	@Override
	public java.lang.String[] getFieldName() {
		return new String[] { "���Ԥ�㿨��", "���Ԥ������", "����", "����" };
	}

	@Override
	public String getResourceID() {
		return "transtype";
	}

	@Override
	public java.lang.String[] getHiddenFieldCode() {
		return new String[] { "pk_fivemetals_h" };
	}

	/**
	 * Ҫ���ص������ֶ���i.e. pk_deptdoc ��������:(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	@Override
	public String getPkFieldCode() {
		return "pk_fivemetals_h";
	}

	public String getDataPowerColumn() {
		return "pk_fivemetals_h";
	}

	/**
	 * ���ձ��� ��������:(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	@Override
	public String getRefTitle() {
		return "���Ԥ��";
	}

	/**
	 * �������ݿ�������ͼ�� ��������:(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	@Override
	public String getTableName() {
		return " ic_fivemetals_h ";
	}

	public String getEnvWherePart() {
		String wheresql = "";
		try {
			wheresql = VisibleUtil.getRefVisibleCondition(
					NCESAPI.clientSqlEncode(getPk_group()),
					NCESAPI.clientSqlEncode(getPk_org()),
					"40a6baac-d943-4be1-9fe5-5c1736001ba8");
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
		}
		return wheresql;
	}

}
