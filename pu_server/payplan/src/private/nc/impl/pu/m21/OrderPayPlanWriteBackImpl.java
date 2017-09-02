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
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.uapbd.CurrencyRateUtil;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.bd.payperiod.PayPeriodVO;
import nc.vo.ic.m45.entity.PurchaseInBodyVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pu.m21.entity.AggPayPlanVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.m21.entity.PayPlanVO;
import nc.vo.pu.m21.entity.PayPlanViewVO;
import nc.vo.pu.m25.entity.InvoiceItemVO;
import nc.vo.pu.m25.entity.InvoiceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.scmpub.payterm.pay.AbstractPayPlanVO;
import nc.vo.trade.voutils.SafeCompute;

public class OrderPayPlanWriteBackImpl implements IOrderPayPlanWriteBack {

	public static final UFDouble UF100 = new UFDouble(100);

	// �ɹ�����д����ƻ�
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
			// ���ݶ���id���ܽ��
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
		dealPayPlanViewVO(map, "���ǩ������", invo.getHead().getPrimaryKey(),
				dbegindate);

	}

	// �ɹ���Ʊ��д����ƻ�
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
			// ���ݶ���id���ܽ��
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
		dealPayPlanViewVO(map, "�ɹ���Ʊ����", invo.getParentVO().getPrimaryKey(),
				dbegindate);
	}

	/**
	 * 
	 * @param map
	 *            �������ܵĽ��
	 * @param name
	 *            ��������
	 * @param sourceid
	 *            ��Դ����id
	 * @throws BusinessException
	 */
	private void dealPayPlanViewVO(Map<String, UFDouble> map, String name,
			String sourceid, UFDate dbegindate) throws BusinessException {

		if (map == null || map.size() == 0)
			return;

		for (Map.Entry<String, UFDouble> entry : map.entrySet()) {

			// ��ѯ�ö����µ� ����ƻ�
			IOrderPayPlanQuery service = NCLocator.getInstance().lookup(
					IOrderPayPlanQuery.class);
			AggPayPlanVO[] vos = service.queryPayPlanVOs(new String[] { entry
					.getKey() });

			PayPeriodVO periodvo = getPayPeriodVO(name);
			for (AggPayPlanVO aggvo : vos) {
				// if(view.getfe)
				// ����ǳ����������Ҫ�ж��Ƿ���Ϊ���޶��������У����߸Ĵ�������������ǣ�������һ�и���ƻ���ȥ��
				// vdef1 ��¼��Դ��Ϣ
				PayPlanVO[] plans = aggvo.getPayPlanVO();
				if (plans == null || plans.length == 0)
					continue;

				// �����������ݻ���δ��д�Ľ��
				UFDouble totalNmny = UFDouble.ZERO_DBL;

				// ������������δ��д�Ľ��
				List<PayPlanVO> list = new ArrayList<>();
				// ���ܸö��µ���������
				List<PayPlanVO> updatelist1 = new ArrayList<>();
				for (PayPlanVO plan : plans) {
					String feffdatetype = plan.getFeffdatetype();
					if (StringUtil.isSEmptyOrNull(feffdatetype)) {
						updatelist1.add(plan);
					} else {
						// �������� ���Ƿ�Ʊ�� �ҵ���Ҫ���µ�������
						if (!feffdatetype.equalsIgnoreCase(periodvo
								.getPrimaryKey())) {
							updatelist1.add(plan);
						} else {
							String def1 = getPayPlanDef1(plan.getPrimaryKey());
							// def1 �����Դ��Ϣ ���������Դ ֤���ǻ�д�� ����Ҫ����
							if (!StringUtil.isSEmptyOrNull(def1)) {
								updatelist1.add(plan);
							} else {
								list.add(plan);
								totalNmny = SafeCompute.add(totalNmny,
										plan.getNorigmny());
							}
						}
					}
				}
				if (list == null || list.size() == 0)
					return;

				UFDouble purinNmny = entry.getValue();
				if (totalNmny.compareTo(purinNmny) == 0) {// ȫ�����
					for (PayPlanVO plan : list) {
						// ����def1
						updatePayPlanDef1(plan.getPrimaryKey(), sourceid, plan);
						setDate(plan, dbegindate);
						updatelist1.add(plan);
					}
				} else if (purinNmny.compareTo(totalNmny) > 0) { // // �������
					// �ж��Ƿ����޶� ����еĻ� ����� û�а���ȫ����⴦��
					// ����def1
					boolean flag = false;
					// ��ѯ�Ƿ�����޶�
					IOrderEditRecordQuery service1 = NCLocator.getInstance()
							.lookup(IOrderEditRecordQuery.class);
					List<String> pkList = new ArrayList<String>();
					pkList.add(entry.getKey());
					OrderVO[] editRecords = service1.queryOrderPrice(pkList);
					if (editRecords != null && editRecords.length > 0)
						flag = true;

					for (PayPlanVO plan : list) {
						// ����def1
						updatePayPlanDef1(plan.getPrimaryKey(), sourceid, plan);
						setDate(plan, dbegindate);
						updatelist1.add(plan);
					}
					if (flag) {// ���޶�
						UFDouble temp = SafeCompute.sub(purinNmny, totalNmny);
						PayPlanVO planclone = (PayPlanVO) list.get(0).clone();
						planclone.setPrimaryKey(null);
						planclone.setNorigmny(temp);
						setRowNo(planclone, updatelist1);
						updatelist1.add(planclone);
					}

				} else {// �������
					for (PayPlanVO plan : list) {

						UFDouble norigmny = plan.getNorigmny();

						if (purinNmny.compareTo(UFDouble.ZERO_DBL) <= 0) {
							updatelist1.add(plan);
						} else {
							// ���мƻ����С���������
							if (norigmny.compareTo(purinNmny) <= 0) {
								updatePayPlanDef1(plan.getPrimaryKey(),
										sourceid, plan);
								setDate(plan, dbegindate);
								updatelist1.add(plan);
								purinNmny = SafeCompute
										.sub(purinNmny, norigmny);
							} else {
								// ����ʣ����
								UFDouble temp = SafeCompute.sub(norigmny,
										purinNmny);
								plan.setNorigmny(temp);
								updatelist1.add(plan);

								// �����Ĳ��ֽ��
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

				// �������
				plans = updatelist1.toArray(new PayPlanVO[updatelist1.size()]);
				calcMnyByRate(plans);
				aggvo.setPayPlanVO(plans);
				BatchOperateVO batch = savePayPlanViewVO(aggvo);
				PayPlanViewVO[] addplans = (PayPlanViewVO[]) batch.getAddObjs();

				// ���������е� def1
				if (addplans != null && addplans.length > 0) {
					for (PayPlanViewVO plan : addplans) {
						updatePayPlanDef1(plan.getPrimaryKey(), sourceid, null);
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

	// ���¸���ƻ���Դ
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
			throw new BusinessException("�����������[" + name + "]�Ƿ����");
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

	// ���ñ��ҽ�� ԭ�ҽ�� �ۼƽ�� ����

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

			ntotallocalmny = MathTool.nvl(util.getAmountByOpp(corigcurrencyid,
					ccurrencyid, accOrigMny, nexchangerate, new UFDate()));

			UFDouble ntempMny = util.getAmountByOpp(corigcurrencyid,
					ccurrencyid, planvo.getNorigmny(), nexchangerate,
					new UFDate());
			planvo.setNmny(ntempMny);
			planvo.setAttributeValue(AbstractPayPlanVO.NTOTALORIGMNY,
					ntotallocalmny);
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
		}

	}

	// ��������
	private void setDate(PayPlanVO plan, UFDate dbegindate) {
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

		if (invo == null)
			return;

		try {
			cancelWriteBackPayPlan("�ɹ���Ʊ����", invo.getParentVO().getPrimaryKey());
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}

	}

	@Override
	public void writeBackCancelSignFor45(PurchaseInVO invo)
			throws BusinessException {

		if (invo == null)
			return;

		try {
			cancelWriteBackPayPlan("���ǩ������", invo.getHead().getPrimaryKey());
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}

	}

	// ȡ��ǩ�������Դ
	private void cancelWriteBackPayPlan(String name, String sourceid)
			throws BusinessException {
		PayPeriodVO periodvo = getPayPeriodVO(name);

		String sql = "update po_order_payplan set def1 = null, dbegindate =null,dbegindate =null where feffdatetype   = '"
				+ periodvo.getPrimaryKey() + "' and def1 = '" + sourceid + "'";
		BaseDAO dao = new BaseDAO();
		dao.executeUpdate(sql);

	}

	// �ɹ��˿��д����ƻ�
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
			// ���ݶ���id���ܽ��
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
		dealBack45PayPlanViewVO(map, "���ǩ������", invo.getHead().getPrimaryKey(),
				dbegindate);
	}

	// �ɹ��˿��д����ƻ�
	@Override
	public void writeBackOrderPayPlanForBack45(PurchaseInVO invo)
			throws BusinessException {
		PayPeriodVO periodvo = getPayPeriodVO("���ǩ������");

		String sql = "update po_order_payplan set dr =1,def1 = null, dbegindate =null,dbegindate =null where feffdatetype   = '"
				+ periodvo.getPrimaryKey()
				+ "' and def1 = '"
				+ invo.getHead().getPrimaryKey() + "'";
		BaseDAO dao = new BaseDAO();
		dao.executeUpdate(sql);
	}

	/**
	 * 
	 * @param map
	 *            �������ܵĽ��
	 * @param name
	 *            ��������
	 * @param sourceid
	 *            ��Դ����id
	 * @throws BusinessException
	 */
	private void dealBack45PayPlanViewVO(Map<String, UFDouble> map,
			String name, String sourceid, UFDate dbegindate)
			throws BusinessException {

		if (map == null || map.size() == 0)
			return;

		for (Map.Entry<String, UFDouble> entry : map.entrySet()) {

			// ��ѯ�ö����µ� ����ƻ�
			IOrderPayPlanQuery service = NCLocator.getInstance().lookup(
					IOrderPayPlanQuery.class);
			AggPayPlanVO[] vos = service.queryPayPlanVOs(new String[] { entry
					.getKey() });

			PayPeriodVO periodvo = getPayPeriodVO(name);
			for (AggPayPlanVO aggvo : vos) {
				// if(view.getfe)
				// ����ǳ����������Ҫ�ж��Ƿ���Ϊ���޶��������У����߸Ĵ�������������ǣ�������һ�и���ƻ���ȥ��
				// vdef1 ��¼��Դ��Ϣ
				PayPlanVO[] plans = aggvo.getPayPlanVO();
				if (plans == null || plans.length == 0)
					continue;

				// ���ܸö��µ���������
				List<PayPlanVO> updatelist1 = new ArrayList<>();
				PayPlanVO tempplanvo = null;
				for (PayPlanVO plan : plans) {
					String feffdatetype = plan.getFeffdatetype();
					if (StringUtil.isSEmptyOrNull(feffdatetype)) {
						updatelist1.add(plan);
					} else {
						// �������� ���Ƿ�Ʊ�� �ҵ���Ҫ���µ�������
						if (!feffdatetype.equalsIgnoreCase(periodvo
								.getPrimaryKey())) {
							updatelist1.add(plan);
						} else {
							tempplanvo = plan;
						}
					}
				}

				if (tempplanvo != null) {
					if (UFDouble.ZERO_DBL.compareTo(entry.getValue()) != 0) {
						// �����Ĳ��ֽ��
						PayPlanVO planclone = (PayPlanVO) tempplanvo.clone();
						planclone.setPrimaryKey(null);
						planclone.setNorigmny(SafeCompute.multiply(
								entry.getValue(), new UFDouble(-1)));
						planclone.setNrate(UFDouble.ZERO_DBL);
						planclone.setNmny(SafeCompute.multiply(
								entry.getValue(), new UFDouble(-1)));
						setDate(planclone, dbegindate);
						setRowNo(planclone, updatelist1);
						updatelist1.add(planclone);
						if (updatelist1 == null || updatelist1.size() == 0)
							continue;

						// �������
						plans = updatelist1.toArray(new PayPlanVO[updatelist1
								.size()]);
						calcMnyByRate(plans);
						aggvo.setPayPlanVO(plans);
						BatchOperateVO batch = savePayPlanViewVO(aggvo);
						PayPlanViewVO[] addplans = (PayPlanViewVO[]) batch
								.getAddObjs();

						// ���������е� def1
						if (addplans != null && addplans.length > 0) {
							for (PayPlanViewVO plan : addplans) {
								updatePayPlanDef1(plan.getPrimaryKey(),
										sourceid, null);
							}
						}
					}
				}
			}
		}
	}
}
