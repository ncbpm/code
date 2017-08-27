package nc.impl.pu.m21;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.pu.m21.IOrderEditRecordQuery;
import nc.itf.pu.m21.IOrderPayPlan;
import nc.itf.pu.m21.IOrderPayPlanQuery;
import nc.itf.pu.m21.IOrderPayPlanWriteBack;
import nc.itf.pu.m21.IOrderQuery;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.uapbd.CurrencyRateUtil;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.bd.payperiod.PayPeriodVO;
import nc.vo.ic.m45.entity.PurchaseInBodyVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pu.m21.entity.AggPayPlanVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.m21.entity.PayPlanVO;
import nc.vo.pu.m21.entity.PayPlanViewVO;
import nc.vo.pu.m25.entity.InvoiceItemVO;
import nc.vo.pu.m25.entity.InvoiceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.scmpub.payterm.pay.AbstractPayPlanVO;
import nc.vo.trade.voutils.SafeCompute;

public class OrderPayPlanWriteBackImpl implements IOrderPayPlanWriteBack {

	public static final UFDouble UF100 = new UFDouble(100);

	// 采购入库回写付款计划
	@Override
	public void writeBackOrderPayPlanFor45(PurchaseInVO invo)
			throws BusinessException {

		if (invo == null)
			return;

		PurchaseInBodyVO[] bodys = invo.getBodys();
		if (bodys == null || bodys.length == 0)
			return;

		List<String> list = new ArrayList<String>();
		for (PurchaseInBodyVO body : bodys) {
			String sourcetype = body.getCfirsttype();
			if (!"21".equals(sourcetype)) {
				continue;
			}
			// 根据订单id汇总数量
			String sourceid = body.getCfirstbillhid();
			if (StringUtil.isSEmptyOrNull(sourceid))
				continue;
			if (!list.contains(sourceid)) {
				list.add(sourceid);
			}
		}

		if (list == null || list.size() == 0)
			return;

		IOrderQuery orderservice = NCLocator.getInstance().lookup(
				IOrderQuery.class);

		OrderVO[] orders = orderservice.queryOrderVOsByIds(
				list.toArray(new String[list.size()]), UFBoolean.FALSE);

		Map<String, UFDouble> pmap = new HashMap<>();
		for (OrderVO order : orders) {
			OrderItemVO[] items = order.getBVO();
			if (items == null || items.length == 0) {
				continue;
			}
			for (OrderItemVO item : items) {
				pmap.put(item.getPrimaryKey(), item.getNtaxprice());
			}
		}

		Map<String, UFDouble> map = new HashMap<>();
		for (PurchaseInBodyVO body : bodys) {
			UFDouble temp = UFDouble.ZERO_DBL;
			String sourcetype = body.getCfirsttype();
			if (!"21".equals(sourcetype)) {
				continue;
			}
			// 根据订单id汇总金额 金额 = 订单含税单价*数量
			String sourceid = body.getCfirstbillhid();
			if (StringUtil.isSEmptyOrNull(sourceid))
				continue;
			temp = SafeCompute.multiply(body.getNnum(),
					pmap.get(body.getCfirstbillbid()));
			if (map.containsKey(sourceid)) {
				temp = SafeCompute.add(map.get(sourceid), temp);
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

		List<String> list = new ArrayList<String>();
		for (InvoiceItemVO body : bodys) {

			String sourceid = body.getPk_order();
			if (StringUtil.isSEmptyOrNull(sourceid))
				continue;
			if (!list.contains(sourceid)) {
				list.add(sourceid);
			}
		}

		if (list == null || list.size() == 0)
			return;

		IOrderQuery orderservice = NCLocator.getInstance().lookup(
				IOrderQuery.class);

		OrderVO[] orders = orderservice.queryOrderVOsByIds(
				list.toArray(new String[list.size()]), UFBoolean.FALSE);

		Map<String, UFDouble> pmap = new HashMap<>();
		for (OrderVO order : orders) {
			OrderItemVO[] items = order.getBVO();
			if (items == null || items.length == 0) {
				continue;
			}
			for (OrderItemVO item : items) {
				pmap.put(item.getPrimaryKey(), item.getNtaxprice());
			}
		}

		Map<String, UFDouble> map = new HashMap<>();
		for (InvoiceItemVO body : bodys) {
			UFDouble temp = UFDouble.ZERO_DBL;
			// 根据订单id汇总金额 金额 = 订单含税单价*数量
			String sourceid = body.getPk_order();
			if (StringUtil.isSEmptyOrNull(sourceid))
				continue;
			temp = SafeCompute.multiply(body.getNnum(),
					pmap.get(body.getPk_order_b()));
			if (map.containsKey(sourceid)) {
				temp = SafeCompute.add(map.get(sourceid), temp);
			}
			map.put(sourceid, temp);
		}
		UFDate dbegindate = invo.getParentVO().getTaudittime();
		dealPayPlanViewVO(map, "采购发票审核日期", invo.getParentVO().getPrimaryKey(),
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

			String[] orderids = new String[] { entry.getKey() };
			// 查询该订单下的 付款计划
			IOrderPayPlanQuery service = NCLocator.getInstance().lookup(
					IOrderPayPlanQuery.class);
			AggPayPlanVO[] vos = service.queryPayPlanVOs(orderids);

			PayPeriodVO periodvo = getPayPeriodVO(name);
			for (AggPayPlanVO aggvo : vos) {
				// if(view.getfe)
				// 如果是超量如果，需要判断是否因为有修订（增加行，或者改大数量），如果是，则增加一行付款计划进去。
				// vdef1 记录来源信息
				PayPlanVO[] plans = aggvo.getPayPlanVO();
				if (plans == null || plans.length == 0)
					continue;

				// 按照起算依据汇总未回写的金额
				UFDouble totalNmny = UFDouble.ZERO_DBL;

				// 按照起算依据未回写的金额
				List<PayPlanVO> list = new ArrayList<>();
				// 汇总该定下的所有数据
				List<PayPlanVO> updatelist1 = new ArrayList<>();
				UFDouble fkbl = UFDouble.ZERO_DBL;
				for (PayPlanVO plan : plans) {
					String feffdatetype = plan.getFeffdatetype();
					if (StringUtil.isSEmptyOrNull(feffdatetype)) {
						updatelist1.add(plan);
					} else {
						// 如果是入库 或是发票行 找到需要更新的行数据
						if (!feffdatetype.equalsIgnoreCase(periodvo
								.getPrimaryKey())) {
							updatelist1.add(plan);
						} else {
							String def1 = getPayPlanDef1(plan.getPrimaryKey());
							// def1 存的来源信息 如果存在来源 证明是回写行 不需要更新
							if (!StringUtil.isSEmptyOrNull(def1)) {
								updatelist1.add(plan);
							} else {
								list.add(plan);
								totalNmny = SafeCompute.add(totalNmny,
										plan.getNorigmny());
							}
							fkbl = SafeCompute.add(fkbl, plan.getNrate());
						}
					}
				}
				if (list == null || list.size() == 0)
					return;

				UFDouble purinNmny = SafeCompute.div(
						SafeCompute.multiply(entry.getValue(), fkbl),
						OrderPayPlanWriteBackImpl.UF100).setScale(2, 0);// 本次入库金额
				if (totalNmny.compareTo(purinNmny) == 0) {// 全部入库
					for (PayPlanVO plan : list) {
						// 更新def1
						updatePayPlanDef1(plan.getPrimaryKey(), sourceid, plan);
						setDate(plan, dbegindate);
						updatelist1.add(plan);
					}
				} else if (purinNmny.compareTo(totalNmny) > 0) { // // 超量入库
					// 判断是否有修订 如果有的话 拆分行 没有按照全部入库处理
					// 更新def1
					boolean flag = false;
					// 查询是否存在修订
					IOrderEditRecordQuery service1 = NCLocator.getInstance()
							.lookup(IOrderEditRecordQuery.class);
					List<String> pkList = new ArrayList<String>();
					pkList.add(entry.getKey());
					OrderVO[] editRecords = service1.queryOrderPrice(pkList);
					if (editRecords != null && editRecords.length > 0)
						flag = true;

					for (PayPlanVO plan : list) {
						// 更新def1
						updatePayPlanDef1(plan.getPrimaryKey(), sourceid, plan);
						setDate(plan, dbegindate);
						updatelist1.add(plan);
					}
					if (flag) {// 有修订
						UFDouble temp = SafeCompute.sub(purinNmny, totalNmny);
						PayPlanVO planclone = (PayPlanVO) list.get(0).clone();
						planclone.setPrimaryKey(null);
						planclone.setNorigmny(temp);
						setRowNo(planclone, updatelist1);
						updatelist1.add(planclone);
					}

				} else {// 部分入库
					for (PayPlanVO plan : list) {

						UFDouble norigmny = plan.getNorigmny();

						if (purinNmny.compareTo(UFDouble.ZERO_DBL) <= 0) {
							updatelist1.add(plan);
						} else {
							// 本行计划金额小于入库金额
							if (norigmny.compareTo(purinNmny) <= 0) {
								updatePayPlanDef1(plan.getPrimaryKey(),
										sourceid, plan);
								setDate(plan, dbegindate);
								updatelist1.add(plan);
								purinNmny = SafeCompute
										.sub(purinNmny, norigmny);
							} else {
								// 本行剩余金额
								UFDouble temp = SafeCompute.sub(norigmny,
										purinNmny);
								plan.setNorigmny(temp);
								updatelist1.add(plan);

								// 新增的部分金额
								PayPlanVO planclone = (PayPlanVO) plan.clone();
								planclone.setPrimaryKey(null);
								planclone.setNorigmny(purinNmny);
								setDate(planclone, dbegindate);
								setRowNo(planclone, updatelist1);
								updatelist1.add(planclone);
							}
						}
					}
				}

				if (updatelist1 == null || updatelist1.size() == 0)
					continue;

				// 计算比例
				plans = updatelist1.toArray(new PayPlanVO[updatelist1.size()]);
				calcMnyByRate(plans);
				aggvo.setPayPlanVO(plans);
				BatchOperateVO batch = savePayPlanViewVO(aggvo, null);
				PayPlanViewVO[] addplans = (PayPlanViewVO[]) batch.getAddObjs();

				// 更新新增行的 def1
				if (addplans != null && addplans.length > 0) {
					for (PayPlanViewVO plan : addplans) {
						updatePayPlanDef1(plan.getPrimaryKey(), sourceid, null);
					}
				}
			}
		}
	}

	private BatchOperateVO savePayPlanViewVO(AggPayPlanVO aggvo,
			AggPayPlanVO delaggvo) throws BusinessException {
		PayPlanViewVO[] views = AggPayPlanVO
				.getPayPlanViewVO(new AggPayPlanVO[] { aggvo });

		if (views == null || views.length == 0)
			return null;

		List<Integer> addlistindex = new ArrayList<Integer>();
		List<PayPlanViewVO> addlist = new ArrayList<PayPlanViewVO>();
		List<Integer> updatelistindex = new ArrayList<Integer>();
		List<PayPlanViewVO> updatelist = new ArrayList<PayPlanViewVO>();
		List<Integer> dellistindex = new ArrayList<Integer>();
		List<PayPlanViewVO> dellist = new ArrayList<PayPlanViewVO>();
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

		if (delaggvo != null) {
			PayPlanViewVO[] delviews = AggPayPlanVO
					.getPayPlanViewVO(new AggPayPlanVO[] { delaggvo });

			if (delviews != null && delviews.length > 0) {
				for (PayPlanViewVO view : delviews) {
					dellistindex.add(index);
					dellist.add(view);
					index++;
				}
			}
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

		if (dellist != null && dellist.size() > 0) {
			batchVO.setDelObjs(dellist.toArray(new PayPlanViewVO[dellist.size()]));
			int[] intArray = integerListToIntArray(dellistindex);
			batchVO.setDelIndexs(intArray);
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

	// 更新付款计划来源
	private void updatePayPlanDef1(String pk_order_payplan, String def1,
			PayPlanVO plan) throws DAOException {
		String sql = "update po_order_payplan set def1 = '" + def1
				+ "' where pk_order_payplan  = '" + pk_order_payplan + "'";
		BaseDAO dao = new BaseDAO();
		dao.executeUpdate(sql);
		if (plan != null) {
			sql = " select ts from po_order_payplan where pk_order_payplan  = '"
					+ pk_order_payplan + "'";
			Object o = dao.executeQuery(sql, new ColumnProcessor());
			plan.setTs(new UFDateTime((String) o));
		}
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

	// 设置本币金额 原币金额 累计金额 比例

	public void calcMnyByRate(PayPlanVO[] plans) throws BusinessException {

		if (plans == null || plans.length == 0)
			return;
		UFDouble accOrigMny = UFDouble.ZERO_DBL;
		String corigcurrencyid = (String) plans[0]
				.getAttributeValue(AbstractPayPlanVO.CORIGCURRENCYID);
		String ccurrencyid = (String) plans[0]
				.getAttributeValue(AbstractPayPlanVO.CCURRENCYID);
		UFDouble nexchangerate = (UFDouble) plans[0]
				.getAttributeValue(AbstractPayPlanVO.NEXCHANGERATE);
		String pk_fiorg = (String) plans[0]
				.getAttributeValue(AbstractPayPlanVO.PK_FINANCEORG);
		CurrencyRateUtil util = CurrencyRateUtil.getInstanceByOrg(pk_fiorg);
		UFDouble ntotallocalmny = UFDouble.ZERO_DBL;
		for (int i = 0; i < plans.length; i++) {
			PayPlanVO planvo = plans[i];
			UFDouble inorigmny = (UFDouble) planvo
					.getAttributeValue(AbstractPayPlanVO.NORIGMNY);
			accOrigMny = MathTool.add(accOrigMny, inorigmny);
		}

		UFDouble nrate = null;
		UFDouble sumrate = UFDouble.ZERO_DBL;
		int j = 0;
		for (int i = 0; i < plans.length; i++) {
			PayPlanVO planvo = plans[i];

			UFDouble indexNorigmny = (UFDouble) planvo
					.getAttributeValue(AbstractPayPlanVO.NORIGMNY);
			if (indexNorigmny == null) {
				return;
			}
			nrate = indexNorigmny.div(accOrigMny, UFDouble.DEFAULT_POWER)
					.multiply(OrderPayPlanWriteBackImpl.UF100, 2);
			j++;
			if (j == plans.length) {
				nrate = MathTool.sub(OrderPayPlanWriteBackImpl.UF100, sumrate);
			} else {
				sumrate = sumrate.add(nrate);
			}
			planvo.setAttributeValue(AbstractPayPlanVO.NRATE, nrate);
			ntotallocalmny = MathTool.nvl(util.getAmountByOpp(corigcurrencyid,
					ccurrencyid, accOrigMny, nexchangerate, new UFDate()));
			planvo.setAttributeValue(AbstractPayPlanVO.NTOTALORIGMNY,
					ntotallocalmny);
			UFDouble ntempMny = util.getAmountByOpp(corigcurrencyid,
					ccurrencyid, planvo.getNorigmny(), nexchangerate,
					new UFDate());
			planvo.setNmny(ntempMny);
		}
	}

	// 设置账期
	private void setDate(PayPlanVO plan, UFDate dbegindate) {
		if (dbegindate == null)
			dbegindate = new UFDate();
		int iitermdays = plan.getIitermdays().intValue();
		UFDate denddate = dbegindate.getDateAfter(iitermdays);
		plan.setDbegindate(dbegindate);
		plan.setDenddate(denddate);
	}

	public void setRowNo(PayPlanVO toVO, List<PayPlanVO> list) {
		String toHid = toVO.getPk_order();
		UFDouble max = UFDouble.ZERO_DBL;
		for (Iterator<PayPlanVO> iter = list.iterator(); iter.hasNext();) {
			PayPlanVO view = iter.next();
			if (null == view) {
				continue;
			}
			if (!toHid.equals(view.getPk_order())) {
				continue;
			}

			UFDouble crowno = VORowNoUtils.getUFDouble(view.getCrowno());
			if (max.compareTo(crowno) < 0) {
				max = crowno;
			}
		}
		max = max.add(VORowNoUtils.STEP_VALUE);

		toVO.setCrowno(VORowNoUtils.getCorrectString(max));
	}

	@Override
	public void writeBackCancelSignFor25(InvoiceVO invo)
			throws BusinessException {

		// if (invo == null)
		// return;
		//
		// try {
		// cancelWriteBackPayPlan("采购发票审核日期",
		// invo.getParentVO().getPrimaryKey());
		// } catch (BusinessException e) {
		// ExceptionUtils.wrappException(e);
		// }
	}

	@Override
	public void writeBackCancelSignFor45(PurchaseInVO invo)
			throws BusinessException {

		// if (invo == null)
		// return;
		//
		// try {
		// cancelWriteBackPayPlan("入库签字日期", invo.getHead().getPrimaryKey());
		// } catch (BusinessException e) {
		// ExceptionUtils.wrappException(e);
		// }

		if (invo == null)
			return;

		PurchaseInBodyVO[] bodys = invo.getBodys();
		if (bodys == null || bodys.length == 0)
			return;

		List<String> list = new ArrayList<String>();
		for (PurchaseInBodyVO body : bodys) {
			String sourcetype = body.getCfirsttype();
			if (!"21".equals(sourcetype)) {
				continue;
			}
			// 根据订单id汇总数量
			String sourceid = body.getCfirstbillhid();
			if (StringUtil.isSEmptyOrNull(sourceid))
				continue;
			if (!list.contains(sourceid)) {
				list.add(sourceid);
			}
		}
		if (list == null || list.size() == 0)
			return;
		cancelWriteBackPayPlan(list, "入库签字日期", invo.getHead().getPrimaryKey());
	}

	// // 取消签字清空来源
	// private void cancelWriteBackPayPlan(String name, String sourceid)
	// throws BusinessException {
	// PayPeriodVO periodvo = getPayPeriodVO(name);
	//
	// String sql =
	// "update po_order_payplan set def1 = null, dbegindate =null,denddate =null where feffdatetype   = '"
	// + periodvo.getPrimaryKey() + "' and def1 = '" + sourceid + "'";
	// BaseDAO dao = new BaseDAO();
	// dao.executeUpdate(sql);
	//
	// }

	// 取消签字
	private void cancelWriteBackPayPlan(List<String> list, String name,
			String sourceid) throws BusinessException {

		for (String str : list) {

			String[] orderids = new String[] { str };
			// 查询该订单下的 付款计划
			IOrderPayPlanQuery service = NCLocator.getInstance().lookup(
					IOrderPayPlanQuery.class);
			AggPayPlanVO[] vos = service.queryPayPlanVOs(orderids);

			PayPeriodVO periodvo = getPayPeriodVO(name);

			// 按照账期记录未回写行
			Map<Integer, List<PayPlanVO>> nomap = new HashMap<>();
			// 按照账期记录回写行

			Map<Integer, List<PayPlanVO>> map = new HashMap<>();

			// 汇总该定下的所有数据
			List<PayPlanVO> updatelist1 = new ArrayList<>();

			for (AggPayPlanVO aggvo : vos) {
				PayPlanVO[] plans = aggvo.getPayPlanVO();
				if (plans == null || plans.length == 0)
					continue;
				for (PayPlanVO plan : plans) {

					String feffdatetype = plan.getFeffdatetype();
					if (StringUtil.isSEmptyOrNull(feffdatetype)) {
						updatelist1.add(plan);
					} else {
						// 如果是入库 或是发票行 找到需要更新的行数据（根据起算依据判断）
						if (!feffdatetype.equalsIgnoreCase(periodvo
								.getPrimaryKey())) {
							updatelist1.add(plan);
						} else {
							// def1 存的来源信息
							String def1 = getPayPlanDef1(plan.getPrimaryKey());
							// 账期 根据账期分组
							Integer def12 = plan.getIitermdays();
							// def1 存的来源信息 如果存在来源 证明是回写行
							if (!StringUtil.isSEmptyOrNull(def1)) {
								if (def1.equalsIgnoreCase(sourceid)) {
									// 回写行
									List<PayPlanVO> writelist = null;
									if (nomap.containsKey(def12)) {
										writelist = map.get(def12);
									} else {
										writelist = new ArrayList<>();
									}
									writelist.add(plan);
									map.put(def12, writelist);
								} else {
									updatelist1.add(plan);
								}
							} else {
								// 未回写行
								List<PayPlanVO> nowritelist = null;
								if (nomap.containsKey(def12)) {
									nowritelist = nomap.get(def12);
								} else {
									nowritelist = new ArrayList<>();
								}
								nowritelist.add(plan);
								nomap.put(def12, nowritelist);
							}
						}
					}
				}

				PayPlanVO planclone = null;
				UFDouble norigmny = UFDouble.ZERO_DBL;// 未回写金额
				List<PayPlanVO> writelisttotal = new ArrayList<>();
				// 无回写行
				if (map == null || map.size() == 0)
					continue;

				for (Map.Entry<Integer, List<PayPlanVO>> entry : map.entrySet()) {
					List<PayPlanVO> writelist = entry.getValue();
					if (writelist == null || writelist.size() == 0) {
						continue;
					} else {
						planclone = (PayPlanVO) writelist.get(0).clone();
						for (PayPlanVO vo : writelist) {
							norigmny = SafeCompute.add(vo.getNorigmny(),
									norigmny);
							writelisttotal.add(vo);
						}
					}

					List<PayPlanVO> nowritelist = nomap.get(entry.getKey());

					// 不存在未回写 增新建一条数据 然后 汇总 入库单金额 如果有未回写的 则合并
					if (nowritelist == null || nowritelist.size() == 0) {
						planclone.setPrimaryKey(null);
						planclone.setNorigmny(norigmny);
						planclone.setDbegindate(null);
						planclone.setDenddate(null);
						nowritelist.add(planclone);
						setRowNo(planclone, updatelist1);
					} else {
						planclone = (PayPlanVO) nowritelist.get(0);
						norigmny = SafeCompute.add(planclone.getNorigmny(),
								norigmny);
						planclone.setNorigmny(norigmny);
					}
					for (PayPlanVO plan : nowritelist) {
						updatelist1.add(plan);
					}
				}

				// 计算比例
				plans = updatelist1.toArray(new PayPlanVO[updatelist1.size()]);
				calcMnyByRate(plans);
				aggvo.setParentVO(aggvo.getParentVO());
				aggvo.setPayPlanVO(plans);

				AggPayPlanVO aggvo1 = new AggPayPlanVO();
				aggvo1.setParentVO(aggvo.getParentVO());
				aggvo1.setPayPlanVO(writelisttotal
						.toArray(new PayPlanVO[writelisttotal.size()]));
				savePayPlanViewVO(aggvo, aggvo1);
			}
		}
	}

	// 采购退库回写付款计划
	@Override
	public void writeBackCancelSignForBack45(PurchaseInVO invo)
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

		if (map == null || map.size() == 0)
			return;

		for (Map.Entry<String, UFDouble> entry : map.entrySet()) {

			// 查询该订单下的 付款计划
			IOrderPayPlanQuery service = NCLocator.getInstance().lookup(
					IOrderPayPlanQuery.class);
			AggPayPlanVO[] vos = service.queryPayPlanVOs(new String[] { entry
					.getKey() });

			for (AggPayPlanVO aggvo : vos) {
				// if(view.getfe)
				// 如果是超量如果，需要判断是否因为有修订（增加行，或者改大数量），如果是，则增加一行付款计划进去。
				// vdef1 记录来源信息
				PayPlanVO[] plans = aggvo.getPayPlanVO();
				if (plans == null || plans.length == 0)
					continue;
				// 汇总该定下的所有数据
				List<PayPlanVO> updatelist1 = new ArrayList<>();
				PayPlanVO templan = null;
				for (PayPlanVO plan : plans) {
					if (UFDouble.ZERO_DBL.compareTo(plan.getNorigmny()) < 0) {
						updatelist1.add(plan);
					} else {
						templan = plan;
					}
				}

				if (updatelist1 == null || updatelist1.size() == 0)
					continue;

				updatelist1.toArray(new PayPlanVO[updatelist1.size()]);
				calcMnyByRate(updatelist1.toArray(new PayPlanVO[updatelist1
						.size()]));
				if (templan != null) {
					updatelist1.add(templan);
					aggvo.setPayPlanVO(updatelist1
							.toArray(new PayPlanVO[updatelist1.size()]));
					// savePayPlanViewVO(aggvo);
				}
			}
		}
		// PayPeriodVO periodvo = getPayPeriodVO("入库签字日期");
		// String sql =
		// "update po_order_payplan set dr =1,def1 = null, dbegindate =null,denddate =null where feffdatetype   = '"
		// + periodvo.getPrimaryKey()
		// + "' and def1 = '"
		// + invo.getHead().getPrimaryKey() + "'";
		// BaseDAO dao = new BaseDAO();
		// dao.executeUpdate(sql);
	}

	// 采购退库回写付款计划
	@Override
	public void writeBackOrderPayPlanForBack45(PurchaseInVO invo)
			throws BusinessException {

		// if (invo == null)
		// return;
		//
		// PurchaseInBodyVO[] bodys = invo.getBodys();
		// if (bodys == null || bodys.length == 0)
		// return;
		//
		// Map<String, UFDouble> map = new HashMap<>();
		// for (PurchaseInBodyVO body : bodys) {
		// UFDouble temp = UFDouble.ZERO_DBL;
		// String sourcetype = body.getCfirsttype();
		// if (!"21".equals(sourcetype)) {
		// continue;
		// }
		// // 根据订单id汇总金额
		// String sourceid = body.getCfirstbillhid();
		// if (StringUtil.isSEmptyOrNull(sourceid))
		// continue;
		// if (map.containsKey(sourceid)) {
		// temp = SafeCompute.add(map.get(sourceid), temp);
		// } else {
		// temp = body.getNorigtaxmny();
		// }
		// map.put(sourceid, temp);
		// }
		// UFDate dbegindate = invo.getHead().getTaudittime();
		// dealBack45PayPlanViewVO(map, "入库签字日期",
		// invo.getHead().getPrimaryKey(),
		// dbegindate);
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
	private void dealBack45PayPlanViewVO(Map<String, UFDouble> map,
			String name, String sourceid, UFDate dbegindate)
			throws BusinessException {

		if (map == null || map.size() == 0)
			return;

		for (Map.Entry<String, UFDouble> entry : map.entrySet()) {

			// 查询该订单下的 付款计划
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

				// 汇总该定下的所有数据
				List<PayPlanVO> updatelist1 = new ArrayList<>();
				PayPlanVO tempplanvo = null;
				for (PayPlanVO plan : plans) {
					String feffdatetype = plan.getFeffdatetype();
					if (StringUtil.isSEmptyOrNull(feffdatetype)) {
						updatelist1.add(plan);
					} else {
						// 如果是入库 或是发票行 找到需要更新的行数据
						if (!feffdatetype.equalsIgnoreCase(periodvo
								.getPrimaryKey())) {
							updatelist1.add(plan);
						} else {
							updatelist1.add(plan);
							tempplanvo = plan;
						}
					}
				}

				if (updatelist1 == null || updatelist1.size() == 0)
					continue;

				if (tempplanvo != null) {
					if (UFDouble.ZERO_DBL.compareTo(entry.getValue()) != 0) {
						// 新增的部分金额
						PayPlanVO planclone = (PayPlanVO) tempplanvo.clone();
						planclone.setPrimaryKey(null);
						planclone.setNorigmny(entry.getValue());
						planclone.setNrate(UFDouble.ZERO_DBL);
						planclone.setNmny(entry.getValue());
						setDate(planclone, dbegindate);
						setRowNo(planclone, updatelist1);
						updatelist1.add(planclone);
						// 计算比例
						plans = updatelist1.toArray(new PayPlanVO[updatelist1
								.size()]);
						calcMnyByRate(plans);
						aggvo.setPayPlanVO(plans);
						// BatchOperateVO batch = savePayPlanViewVO(aggvo);
						// PayPlanViewVO[] addplans = (PayPlanViewVO[]) batch
						// .getAddObjs();
						//
						// // 更新新增行的 def1
						// if (addplans != null && addplans.length > 0) {
						// for (PayPlanViewVO plan : addplans) {
						// updatePayPlanDef1(plan.getPrimaryKey(),
						// sourceid, null);
						// }
						// }
					}
				}
			}
		}
	}
}
