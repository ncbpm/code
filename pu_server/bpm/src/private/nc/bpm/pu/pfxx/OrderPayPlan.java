package nc.bpm.pu.pfxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.itf.pu.m21.IOrderPayPlan;
import nc.itf.pu.m21.IOrderPayPlanQuery;
import nc.pubitf.uapbd.CurrencyRateUtil;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.ic.pub.check.VOCheckUtil;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m21.entity.AggPayPlanVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.m21.entity.PayPlanVO;
import nc.vo.pu.m21.entity.PayPlanViewVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.scmpub.payterm.pay.AbstractPayPlanVO;

import org.apache.commons.lang.StringUtils;

/**
 * �ɹ���������ƻ�BPM��д�ӿ�
 * 
 * @author liyf
 * 
 */
public class OrderPayPlan extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// 1.�õ�ת�����VO����,ȡ�����򵼵�һ��ע���VO��Ϣ
		AggregatedValueObject resvo = (AggregatedValueObject) vo;
		OrderVO bpmOrder = (OrderVO) resvo;
		// 2. У�����ݵĺϷ���:1.���ݽṹ���� 2.������֯+���ݺ�У���Ƿ��ظ�.
		checkData(resvo);
		// 3.��ȫ����,���ҵ�������״̬
		fillData(resvo);
		// 4. ��д����
		updateNCVO(resvo);
		return "���³ɹ�";

	}

	/**
	 * ���»��������ɹ�����ƻ���ϸ����
	 * 
	 * @param resvo
	 * @throws BusinessException
	 */
	private void updateNCVO(AggregatedValueObject resvo)
			throws BusinessException {

  		if (resvo == null)
			return;

		OrderVO bpmOrder = (OrderVO) resvo;

		if (bpmOrder == null || bpmOrder.getHVO() == null)
			return;
		PayPlanVO[] vos = (PayPlanVO[]) bpmOrder.getChildren(PayPlanVO.class);

		if (vos == null || vos.length == 0)
			return;

		// ��ѯ�ö����µ� ����ƻ�
		IOrderPayPlanQuery service1 = NCLocator.getInstance().lookup(
				IOrderPayPlanQuery.class);
		AggPayPlanVO[] aggvos = service1
				.queryPayPlanVOs(new String[] { bpmOrder.getHVO()
						.getPrimaryKey() });

		List<PayPlanVO> list = new ArrayList<>();
		Map<String, PayPlanVO> map = new HashMap<>();

		// ���ܴ����ĸ���ƻ�
		for (PayPlanVO vo : vos) {
			if (StringUtil.isSEmptyOrNull(vo.getPrimaryKey())) {
				list.add(vo);
			} else { 
				map.put(vo.getPrimaryKey(), vo);
//				list.add(vo);
			}
		}

		// ���ܸö�����bpmδ����ʣ�ึ��ƻ�
		if (aggvos != null && aggvos.length > 0) {
			for (AggPayPlanVO aggvo : aggvos) {
				PayPlanVO[] plans = aggvo.getPayPlanVO();
				if (plans == null || plans.length == 0)
					continue;
				for (PayPlanVO plan : plans) {
					if (!map.containsKey(plan.getPrimaryKey())) {
						list.add(plan);
					}else{
						PayPlanVO  vo =map.get(plan.getPrimaryKey());
						if(vo!=null){
							vo.setPk_order(plan.getPk_order());
							vo.setTs(plan.getTs());
							list.add(vo);
						}
					}
				}
			}
		}

		if (list == null || list.size() == 0)
			return;
		
		for(PayPlanVO  plan:list){
			if (StringUtil.isSEmptyOrNull(plan.getPrimaryKey())) {
				setRowNo(plan, list);
			}
		}

		calcMnyByRate(list.toArray(new PayPlanVO[list.size()]));
		AggPayPlanVO aggvo = new AggPayPlanVO();

		aggvo.setHVO(aggvos[0].getHVO());
		aggvo.setPayPlanVO(list.toArray(new PayPlanVO[list.size()]));

		PayPlanViewVO[] views = AggPayPlanVO
				.getPayPlanViewVO(new AggPayPlanVO[] { aggvo });

		if (views == null || views.length == 0)
			return;

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
				updatelist.add(view);
				updatelistindex.add(index);
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

		List<String> cpks = new ArrayList<String>();
		for (PayPlanVO vo : vos) {
			// �������ƻ�Ϊ�գ�˵���������Ĳɹ�����ƻ�
			if (StringUtils.isEmpty(vo.getPk_order_payplan())) {

			}
		}

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

	private void calcMnyByRate(PayPlanVO[] plans) throws BusinessException {

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
					.multiply( new UFDouble(100), 2);
			j++;
			if (j == plans.length) {
				nrate = MathTool.sub( new UFDouble(100), sumrate);
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
	/**
	 * 
	 * @param resvo
	 */
	private void fillData(AggregatedValueObject resvo) {
		// TODO �Զ����ɵķ������

	}

	/**
	 * 
	 * @param ����У��
	 * @throws ValidationException
	 */
	private void checkData(AggregatedValueObject resvo)
			throws ValidationException {
		// TODO �Զ����ɵķ������
		OrderVO bpmOrder = (OrderVO) resvo;
		PayPlanVO[] vos = (PayPlanVO[]) bpmOrder.getChildren(PayPlanVO.class);

		VOCheckUtil.checkBodyNotNullFields(bpmOrder, new String[] { "pk_order",
				"nshouldnum", "csourcebillhid", "csourcebillbid", "ncostprice",
				"cprojectid" });
	}
}
