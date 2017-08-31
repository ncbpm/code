package nc.impl.pu.m21;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.pu.m21.IOrderPayPlan;
import nc.itf.pu.m21.IOrderPayPlanQuery;
import nc.itf.pu.m21.IOrderPayPlanWriteBack;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.uapbd.CurrencyRateUtil;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.bd.payperiod.PayPeriodVO;
import nc.vo.ic.m45.entity.PurchaseInBodyVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pu.m21.entity.AggPayPlanVO;
import nc.vo.pu.m21.entity.PayPlanVO;
import nc.vo.pu.m21.entity.PayPlanViewVO;
import nc.vo.pu.m21.rule.PayPlanRateCalcByMnyUtil;
import nc.vo.pu.m25.entity.InvoiceItemVO;
import nc.vo.pu.m25.entity.InvoiceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.scmpub.payterm.pay.AbstractPayPlanVO;
import nc.vo.trade.voutils.SafeCompute;

public class OrderPayPlanWriteBackImpl implements IOrderPayPlanWriteBack {

	// 采购入库回写付款计划
	@Override
	public void writeBackOrderPayPlanFor45(PurchaseInVO invo)
			throws BusinessException {

		if (invo == null)
			return;

		PurchaseInBodyVO[] bodys = invo.getBodys();
		if (bodys == null || bodys.length == 0)
			return;

		Map<String, UFDouble> map = new HashMap<>();
		for (PurchaseInBodyVO body : bodys) {
			UFDouble temp = UFDouble.ZERO_DBL;
			String sourcetype = body.getCfirsttype();
			if (!"21".equals(sourcetype)) {
				continue;
			}
			// 根据订单id汇总金额
			String sourceid = body.getCfirstbillhid();
			if (StringUtil.isSEmptyOrNull(sourceid))
				continue;
			if (map.containsKey(sourceid)) {
				temp = SafeCompute.add(map.get(sourceid), temp);
			} else {
				temp = body.getNorigtaxmny();
			}
			map.put(sourceid, temp);
		}
		UFDate dbegindate = invo.getHead().getTaudittime();
		dealPayPlanViewVO(map, "入库签字日期", invo.getHead().getPrimaryKey(),
				dbegindate);

	}

	// 采购发票回写付款计划
	@Override
	public void writeBackOrderPayPlanFor25(InvoiceVO invo)
			throws BusinessException {

		if (invo == null)
			return;

		InvoiceItemVO[] bodys = invo.getChildrenVO();
		if (bodys == null || bodys.length == 0)
			return;

		Map<String, UFDouble> map = new HashMap<>();
		for (InvoiceItemVO body : bodys) {
			UFDouble temp = UFDouble.ZERO_DBL;
			// 根据订单id汇总金额
			String sourceid = body.getPk_order();

			if (StringUtil.isSEmptyOrNull(sourceid))
				continue;
			if (map.containsKey(sourceid)) {
				temp = SafeCompute.add(map.get(sourceid), temp);
			} else {
				temp = body.getNorigtaxmny();
			}
			map.put(sourceid, temp);
		}
		UFDate dbegindate = invo.getParentVO().getTaudittime();
		dealPayPlanViewVO(map, "采购发票日期", invo.getParentVO().getPrimaryKey(),
				dbegindate);
	}

	/**
	 * 
	 * @param map
	 *            订单汇总的金额
	 * @param name
	 *            起算依据
	 * @param sourceid
	 *            来源单据id
	 * @throws BusinessException
	 */
	private void dealPayPlanViewVO(Map<String, UFDouble> map, String name,
			String sourceid, UFDate dbegindate) throws BusinessException {

		if (map == null || map.size() == 0)
			return;

		for (Map.Entry<String, UFDouble> entry : map.entrySet()) {
			IOrderPayPlanQuery service = NCLocator.getInstance().lookup(
					IOrderPayPlanQuery.class);
			AggPayPlanVO[] vos = service.queryPayPlanVOs(new String[] { entry
					.getKey() });

			PayPeriodVO periodvo = getPayPeriodVO(name);
			for (AggPayPlanVO aggvo : vos) {
				// if(view.getfe)
				// 如果是超量如果，需要判断是否因为有修订（增加行，或者改大数量），如果是，则增加一行付款计划进去。
				// vdef1 记录来源信息
				PayPlanVO[] plans = aggvo.getPayPlanVO();
				if (plans == null || plans.length == 0)
					continue;

				List<PayPlanVO> list = new ArrayList<>();
				for (PayPlanVO plan : plans) {
					String feffdatetype = plan.getFeffdatetype();

					if (StringUtil.isSEmptyOrNull(feffdatetype))
						continue;
					// 如果是入库 或是发票行 找到需要更新的行数据
					if (feffdatetype.equalsIgnoreCase(periodvo.getPrimaryKey())) {
						list.add(plan);
					}
				}

				// 根据def1 是否为空判断是否更新了数据
				List<PayPlanVO> updatelist1 = new ArrayList<>();
				for (PayPlanVO plan : list) {
					String def1 = getPayPlanDef1(plan.getPrimaryKey());
					// 如果存在来源 证明是回写行 不需要更新
					if (!StringUtil.isSEmptyOrNull(def1)) {
						updatelist1.add(plan);
						continue;
					}

					UFDouble nmny = plan.getNorigmny();
					if (nmny == null)
						nmny = UFDouble.ZERO_DBL;
					if (nmny.compareTo(entry.getValue()) == 0) {
						// 全部入库
						// 更新def1
						updatePayPlanDef1(plan.getPrimaryKey(), sourceid);
						plan.setDbegindate(dbegindate);
						int iitermdays = plan.getIitermdays().intValue();
						UFDate denddate = dbegindate.getDateAfter(iitermdays);
						plan.setDbegindate(dbegindate);
						plan.setDenddate(denddate);
						updatelist1.add(plan);
					} else if (nmny.compareTo(entry.getValue()) < 0) {
						// 超量入库
						// 判断是否有修订 如果有的话 拆分行 没有按照全部入库处理
						// 更新def1
						boolean flag = true;
						updatePayPlanDef1(plan.getPrimaryKey(), sourceid);
						int iitermdays = plan.getIitermdays().intValue();
						UFDate denddate = dbegindate.getDateAfter(iitermdays);
						plan.setDbegindate(dbegindate);
						plan.setDenddate(denddate);
						updatelist1.add(plan);
						if (!flag) {
							PayPlanVO planclone = (PayPlanVO) plan.clone();
							planclone.setPrimaryKey(null);
							planclone.setNorigmny(SafeCompute.sub(
									entry.getValue(), nmny));
							planclone.setNtotalorigmny(SafeCompute.sub(
									entry.getValue(), nmny));
							setNmny(plan);
							updatelist1.add(planclone);
						}

					} else {
						// 部分入库 剩余的部分金额
						plan.setNorigmny(SafeCompute.sub(nmny, entry.getValue()));
						plan.setNtotalorigmny(SafeCompute.sub(nmny, entry.getValue()));
						setNmny(plan);
						updatelist1.add(plan);

						// 部分入库 新增的部分金额
						PayPlanVO planclone = (PayPlanVO) plan.clone();
						planclone.setPrimaryKey(null);
						planclone.setNorigmny(entry.getValue());
						planclone.setNtotalorigmny(entry.getValue());
						setNmny(planclone);
						int iitermdays = planclone.getIitermdays().intValue();
						UFDate denddate = dbegindate.getDateAfter(iitermdays);
						planclone.setDbegindate(dbegindate);
						planclone.setDenddate(denddate);
						updatelist1.add(planclone);
					}
				}

				if (updatelist1 == null || updatelist1.size() == 0)
					continue;

				// 计算比例

				plans = updatelist1.toArray(new PayPlanVO[updatelist1.size()]);
				PayPlanRateCalcByMnyUtil util = new PayPlanRateCalcByMnyUtil();
				util.calcMnyByRate(plans.length - 1,
						plans[plans.length - 1].getNorigmny(), plans);

				aggvo.setPayPlanVO(plans);
				BatchOperateVO batch = savePayPlanViewVO(aggvo);
				PayPlanViewVO[] addplans = (PayPlanViewVO[]) batch.getAddObjs();

				// 更新新增行的 def1
				if (addplans != null && addplans.length > 0) {
					for (PayPlanViewVO plan : addplans) {
						updatePayPlanDef1(plan.getPrimaryKey(), sourceid);
					}
				}
			}
		}
	}

	private BatchOperateVO savePayPlanViewVO(AggPayPlanVO aggvo)
			throws BusinessException {
		PayPlanViewVO[] views = AggPayPlanVO
				.getPayPlanViewVO(new AggPayPlanVO[] { aggvo });

		if (views == null || views.length == 0)
			return null;

		List<Integer> addlistindex = new ArrayList<Integer>();
		List<PayPlanViewVO> addlist = new ArrayList<PayPlanViewVO>();
		List<Integer> updatelistindex = new ArrayList<Integer>();
		List<PayPlanViewVO> updatelist = new ArrayList<PayPlanViewVO>();
		int index = 0;
		for (PayPlanViewVO view : views) {
			if (StringUtil.isSEmptyOrNull(view.getPk_order_payplan())) {
				addlistindex.add(index);
				addlist.add(view);
			} else {
				updatelistindex.add(index);
				updatelist.add(view);
			}
			index++;
		}

		BatchOperateVO batchVO = new BatchOperateVO();

		if (addlist != null && addlist.size() > 0) {
			batchVO.setAddObjs(addlist.toArray(new PayPlanViewVO[addlist.size()]));
			int[] intArray = integerListToIntArray(addlistindex);
			batchVO.setAddIndexs(intArray);
		}
		if (updatelist != null && updatelist.size() > 0) {
			batchVO.setUpdObjs(updatelist.toArray(new PayPlanViewVO[updatelist
					.size()]));
			int[] intArray = integerListToIntArray(updatelistindex);
			batchVO.setUpdIndexs(intArray);
		}

		IOrderPayPlan service = NCLocator.getInstance().lookup(
				IOrderPayPlan.class);
		batchVO = service.batchSave(batchVO);
		return batchVO;

	}

	private String getPayPlanDef1(String pk_order_payplan) throws DAOException {
		String sql = "select def1 from po_order_payplan where nvl(dr,0)=0 and  pk_order_payplan  = '"
				+ pk_order_payplan + "'";
		BaseDAO dao = new BaseDAO();
		Object o = dao.executeQuery(sql, null, new ColumnProcessor());
		if (o == null)
			return null;
		return (String) o;

	}

	private void updatePayPlanDef1(String pk_order_payplan, String def1)
			throws DAOException {
		String sql = "update po_order_payplan set def1 = '" + def1
				+ "' where pk_order_payplan  = '" + pk_order_payplan + "'";
		BaseDAO dao = new BaseDAO();
		dao.executeUpdate(sql);

	}

	private PayPeriodVO getPayPeriodVO(String name) throws BusinessException {

		String condition = " and name = '" + name + "' and isnull(dr,0) = 0 ";
		VOQuery<ISuperVO> query = new VOQuery(PayPeriodVO.class);
		PayPeriodVO[] hvos = (PayPeriodVO[]) query.query(condition, null);
		if (hvos == null || hvos.length == 0) {
			throw new BusinessException("检查起算依据[" + name + "]是否存在");
		}

		return hvos[0];
	}

	private int[] integerListToIntArray(List<Integer> listindex) {

		if (listindex == null || listindex.isEmpty()) {
			return new int[0];
		}
		int[] intArray = new int[listindex.size()];
		for (int i = 0; i < listindex.size(); i++) {
			intArray[i] = listindex.get(i).intValue();
		}
		return intArray;
	}

	private void setNmny(PayPlanVO plan) {
		String corigcurrencyid = plan.getCorigcurrencyid();
		String ccurrencyid = (String) plan
				.getAttributeValue(AbstractPayPlanVO.CCURRENCYID);
		UFDouble nexchangerate = (UFDouble) plan
				.getAttributeValue(AbstractPayPlanVO.NEXCHANGERATE);
		CurrencyRateUtil util = CurrencyRateUtil.getInstanceByOrg(plan
				.getPk_financeorg());
		try {

			UFDouble ntempMny = util.getAmountByOpp(corigcurrencyid,
					ccurrencyid, plan.getNorigmny(), nexchangerate,
					new UFDate());
			plan.setNmny(ntempMny);
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}

	}

	@Override
	public void writeBackCancelSignFor25(InvoiceVO invo)
			throws BusinessException {

		// String sql = "update po_order_payplan set def1 = '" + def1
		// + "' where pk_order_payplan  = '" + pk_order_payplan +
		// "' and def1 = '"+def1+"'";
		// BaseDAO dao = new BaseDAO();
		// dao.executeUpdate(sql);
	}

	@Override
	public void writeBackCancelSignFor45(PurchaseInVO invo)
			throws BusinessException {

	}
}
