package nc.impl.ic.fivemetals;

import java.math.BigDecimal;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.pmpub.common.utils.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

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
			BigDecimal i = (BigDecimal) objCodeAndDefkey[0];
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
		String sql = "select to_number(substr(" + itemkey + ",10,length("
				+ itemkey + "))) as " + itemkey + " from " + tableName
				+ " where nvl(dr,0)=0 and " + condition;
		BaseDAO dao = new BaseDAO();
		List<BigDecimal> nolist = (List<BigDecimal>) dao.executeQuery(sql,
				null, new ColumnListProcessor());
		int flowno = 1;
		if (nolist != null && nolist.size() > 0) {// 缺号补号逻辑
			for (int i = 1;; i++) {
				if (!nolist.contains(new BigDecimal(i))) {
					flowno = i;
					break;
				}
			}
		}
		return String.format("FM-%s%04d", ym, flowno);
	}
}
