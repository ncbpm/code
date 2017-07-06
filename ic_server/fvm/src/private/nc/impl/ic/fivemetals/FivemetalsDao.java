package nc.impl.ic.fivemetals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.am.db.processor.KeyValueMapProcessor;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.pmpub.common.utils.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class FivemetalsDao {

	public boolean isExists(String tableName, String condition)
			throws BusinessException {
		if (StringUtil.isEmpty(condition)) {
			throw new BusinessException("查询条件不能为空");
		}

		if (StringUtil.isEmpty(tableName)) {
			throw new BusinessException("表名不能为空");
		}

		String sql = "select 1 from " + tableName + " where nvl(dr,0)=0 and "
				+ condition;
		BaseDAO dao = new BaseDAO();
		Object objCodeAndDefkey[] = (Object[]) dao.executeQuery(
				(new StringBuilder("select count(1) from dual where exists("))
						.append(sql).append(")").toString(), null,
				new ArrayProcessor());
		if (objCodeAndDefkey != null && objCodeAndDefkey.length > 0) {
			Integer i = (Integer) objCodeAndDefkey[0];
			if (i == null) {
				return false;
			}
			return i.intValue() > 0;
		} else {
			return false;
		}
	}

	public String getNewBillNo(String tableName, String condition,
			String itemkey) throws BusinessException {
		if (StringUtil.isEmpty(condition)) {
			throw new BusinessException("查询条件不能为空");
		}

		if (StringUtil.isEmpty(tableName)) {
			throw new BusinessException("表名不能为空");
		}

		if (StringUtil.isEmpty(itemkey)) {
			throw new BusinessException("字段不能为空");
		}

		UFDate billdate = new UFDate();
		String ym = billdate.getYear() + billdate.getStrMonth();
		String sql = "select to_number(substr(" + itemkey + ",9,length("
				+ itemkey + "))) as " + itemkey + " from " + tableName
				+ " where nvl(dr,0)=0 and " + condition;
		BaseDAO dao = new BaseDAO();
		List<BigDecimal> nolist = (List<BigDecimal>) dao.executeQuery(sql,
				null, new ColumnListProcessor());
		int flowno = 1;
		if (nolist != null && nolist.size() > 0) {// 缺号补号逻辑
			for (int i = 1;; i++) {
				if (!nolist.contains(i)) {
					flowno = i;
					break;
				}
			}
		}
		return String.format("FM%s%04d", ym, flowno);
	}

	public int getRowNo(String tableName, String condition, String itemkey)
			throws BusinessException {
		if (StringUtil.isEmpty(condition)) {
			throw new BusinessException("查询条件不能为空");
		}

		if (StringUtil.isEmpty(tableName)) {
			throw new BusinessException("表名不能为空");
		}

		if (StringUtil.isEmpty(itemkey)) {
			throw new BusinessException("字段不能为空");
		}

		String sql = "select to_number(" + itemkey + ") as " + itemkey
				+ " from " + tableName + " where nvl(dr,0)=0 and " + condition;
		BaseDAO dao = new BaseDAO();
		List<BigDecimal> nolist = (List<BigDecimal>) dao.executeQuery(sql,
				null, new ColumnListProcessor());
		int flowno = 1;
		if (nolist != null && nolist.size() > 0) {// 缺号补号逻辑
			for (int i = 1;; i++) {
				if (!nolist.contains(i)) {
					flowno = i;
					break;
				}
			}
		}
		return flowno;
	}

	public Map<String, UFDouble> getFivemetalsBalance(String pk_fivemetals_h)
			throws DAOException {
		String sql = " select sum(nmny*itype) nmny,cperiod from  ic_fivemetals_b where nvl(dr,0) = 0 and   pk_fivemetals_h = '"
				+ pk_fivemetals_h + "'  group by cperiod ";
		BaseDAO dao = new BaseDAO();
		Map<String, UFDouble> retMap = (Map<String, UFDouble>) dao
				.executeQuery(sql, null,
						new KeyValueMapProcessor<String, UFDouble>("cperiod",
								"nmny"));
		return retMap;
	}
}
