package nc.bpm.so.pfxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.so.m30.closemanage.ISaleOrderCloseManageMaintain;
import nc.itf.so.m30.revise.IM30ReviseMaintain;
import nc.itf.uap.pf.IplatFormEntry;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderVO;
import nc.vo.so.m30.entity.SaleOrderViewVO;
import nc.vo.so.m30.pub.SaleOrderVOCalculator;
import nc.vo.so.m30.revise.entity.SaleOrderHistoryBVO;
import nc.vo.so.m30.revise.entity.SaleOrderHistoryHVO;
import nc.vo.so.m30.revise.entity.SaleOrderHistoryVO;

import org.apache.commons.lang.StringUtils;

public class M30HistoryForBPMAdd extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		SaleOrderHistoryVO bill = (SaleOrderHistoryVO) vo;
		// 2. У�����ݵĺϷ���:1.���ݽṹ���� 2.������֯+���ݺ�У���Ƿ��ظ�.
		checkData(bill);
		// ����ҵ����
		String vrevisereason = bill.getParentVO().getVrevisereason();
		// ������ڣ���ִ�вɹ������޶����߹رմ�
		String csaleorderid = bill.getParentVO().getCsaleorderid();

		SaleOrderVO ordervo = this.queryVOByPk(csaleorderid);
		// �ж��Ƿ������رջ��ߴ�
		if ("�ر�".equalsIgnoreCase(vrevisereason)) {
			NCLocator.getInstance().lookup(ISaleOrderCloseManageMaintain.class)
					.closeSaleOrder(new SaleOrderVO[] { ordervo });
			return "�����رճɹ�";

		}
		if ("��".equalsIgnoreCase(vrevisereason)) {
			NCLocator.getInstance().lookup(ISaleOrderCloseManageMaintain.class)
					.openSaleOrder(new SaleOrderVO[] { ordervo }, false);
			return "�����򿪳ɹ�";
		}
		// ���в��� --�йرջ����д�,������۶�������
		Map<String, SaleOrderBVO> map = new HashMap<String, SaleOrderBVO>();
		for (SaleOrderBVO body : ordervo.getChildrenVO()) {
			if (body.getVbdef20() != null) {
				map.put(body.getVbdef20(), body);
			}
		}
		// �رյ���
		List<SaleOrderBVO> close_row = new ArrayList<SaleOrderBVO>();
		// �򿪵���
		List<SaleOrderBVO> open_row = new ArrayList<SaleOrderBVO>();
		// �޸ĵ���
		List<SaleOrderHistoryBVO> update_row = new ArrayList<SaleOrderHistoryBVO>();
		// ��������
		List<SaleOrderHistoryBVO> add_row = new ArrayList<SaleOrderHistoryBVO>();

		for (SaleOrderHistoryBVO body : bill.getChildrenVO()) {
			if ("�ر�".equalsIgnoreCase(body.getVbrevisereason())) {
				if (map.containsKey(body.getVbdef20())) {
					close_row.add(map.get(body.getVbdef20()));
				} else {
					throw new BusinessException("����BPM���۶�������������"
							+ body.getVbdef20() + " ��NCδƥ�䵽��Ӧ����.");
				}
			} else if ("��".equalsIgnoreCase(body.getVbrevisereason())) {
				if (map.containsKey(body.getVbdef20())) {
					open_row.add(map.get(body.getVbdef20()));
				} else {
					throw new BusinessException("����BPM���۶�������������"
							+ body.getVbdef20() + " ��NCδƥ�䵽��Ӧ����.");
				}
			} else if ("���".equalsIgnoreCase(body.getVbrevisereason())) {
				if (map.containsKey(body.getVbdef20())) {
					update_row.add(body);
				} else {
					throw new BusinessException("����BPM���۶�������������"
							+ body.getVbdef20() + " ��NCδƥ�䵽��Ӧ����.");
				}
			} else if ("����".equalsIgnoreCase(body.getVbrevisereason())) {
				if (map.containsKey(body.getVbdef20())) {
					throw new BusinessException("�������У�ָ����BPM������ID "
							+ body.getVbdef20() + " ��NC��ǰ���۶����Ѿ����ڣ�����.");
				} else {
					add_row.add(body);
				}
			} else {
				// throw new BusinessException("�ɹ����������δ֧�ֵı���в�����:"
				// + body.getVbmemo());
			}
		}
		// �йر�
		if (close_row.size() > 0) {
			SaleOrderViewVO[] new_bill = new SaleOrderViewVO[close_row.size()];
			for (int i = 0; i < close_row.size(); i++) {
				new_bill[i] = new SaleOrderViewVO();
				new_bill[i].setHead(ordervo.getParentVO());
				new_bill[i].setBody(close_row.get(i));
			}

			NCLocator.getInstance().lookup(ISaleOrderCloseManageMaintain.class)
					.closeSaleOrderRow(new_bill);
		}
		// �д�
		if (open_row.size() > 0) {
			SaleOrderViewVO[] new_bill = new SaleOrderViewVO[open_row.size()];
			for (int i = 0; i < open_row.size(); i++) {
				new_bill[i] = new SaleOrderViewVO();
				new_bill[i].setHead(ordervo.getParentVO());
				new_bill[i].setBody(open_row.get(i));
			}
			NCLocator.getInstance().lookup(ISaleOrderCloseManageMaintain.class)
					.openSaleOrderRow(new_bill);
		}
		// ִ���޶����޷��жϱ�ͷ�ֶ��Ƿ����޸ģ���ˣ�ÿ�ζ�ִ��һ���޶�
		M30HistroyDMO dmo = new M30HistroyDMO();
		SaleOrderHistoryVO[] historyVOs = dmo
				.getOrderHisVOBySaleOrder(new String[] { csaleorderid });
		//
		SaleOrderHistoryVO hisvo = historyVOs[0];

		// ���±�ͷ
		updateHVO(hisvo, bill);
		if (update_row.size() > 0) {
			updateBody(hisvo, update_row);
		}

		if (add_row.size() > 0) {
			addBody(hisvo, add_row);
		}
		
		SaleOrderHistoryVO historyVO = saveAndApproveHistory(hisvo);
	
		
		return "���۶����޶����:���°汾"+historyVO.getParentVO().getIversion();
	}
	
	
	private SaleOrderHistoryVO saveAndApproveHistory(SaleOrderHistoryVO oldbill) throws BusinessException {
		// TODO �Զ����ɵķ������
		SaleOrderHistoryVO bill = new SaleOrderHistoryVO();
		bill.setParentVO(oldbill.getParentVO());
		bill.setChildrenVO(oldbill.getChildrenVO());
		
		
		
		//���ּ��㵥�۵ȡ�
		int rows[] = new int[bill.getChildrenVO().length];
		for (int i = 0; i <bill.getChildrenVO().length; i++) {
			rows[i] = i;
			bill.getChildrenVO()[i].setCorderhistorybid(null);
			bill.getChildrenVO()[i].setStatus(VOStatus.NEW);
		}
		//����α��
		bill.getParentVO().setAttributeValue("pseudocolumn", 0);
		
		//��յ��ۣ����ݽ�����������
		String[] attributeNames =bill.getChildrenVO()[0].getAttributeNames();
		//������Ϣ
		for(SaleOrderBVO bvo:bill.getChildrenVO()){
			for(String attname:attributeNames){
				if(attname.endsWith("price")){
					bvo.setAttributeValue(attname, null);
				}
			}
		}
		SaleOrderVOCalculator cal = new SaleOrderVOCalculator(bill);
		cal.calculate(rows, "norigtaxmny");	
		
		IM30ReviseMaintain maintainsrv = NCLocator.getInstance().lookup(
				IM30ReviseMaintain.class);
		// ReviseSaveSaleOrderAction action = new ReviseSaveSaleOrderAction();
		// �����µķ����������bills �����۶����޶���ʷvo
		SaleOrderHistoryVO[] ret = maintainsrv.reviseOrderHisVOSave(new SaleOrderHistoryVO[]{bill});
		//����
		approve(ret[0]);
		return  ret[0];
	}


	protected AggregatedValueObject approve(AggregatedValueObject billvo)
			throws BusinessException {

		IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator
				.getInstance().lookup(IplatFormEntry.class.getName());
		Object retObj = iIplatFormEntry.processAction("APPROVE", "30R", null,
				billvo, null, null);
		return null;
	}


	private void addBody(SaleOrderHistoryVO bill,
			List<SaleOrderHistoryBVO> add_row) {
		// TODO �Զ����ɵķ������
		List<SaleOrderHistoryBVO> asList = new ArrayList<SaleOrderHistoryBVO>();
		for (SaleOrderHistoryBVO bvo : bill.getChildrenVO()) {
			asList.add(bvo);
		}
		for (SaleOrderHistoryBVO bvo : add_row) {
			bvo.setStatus(VOStatus.NEW);
			asList.add(bvo);
		}
		
		bill.setChildrenVO(asList.toArray(new SaleOrderHistoryBVO[0]));
	

	}

	private void updateBody(SaleOrderHistoryVO bill,
			List<SaleOrderHistoryBVO> update_row) throws BusinessException {
		Map<String, SaleOrderHistoryBVO> map = new HashMap<String, SaleOrderHistoryBVO>();
		for (SaleOrderHistoryBVO body : bill.getChildrenVO()) {
			if (body.getVbdef20() != null) {
				map.put(body.getVbdef20(), body);
			}
		}
		String[] attributeNames = bill.getChildrenVO()[0].getAttributeNames();
		for (SaleOrderHistoryBVO bpm : update_row) {
			SaleOrderHistoryBVO orderItemVO = map.get(bpm.getVbdef20());
			// У���޶�����������Ƿ�С���ۼƷ���������
			UFDouble bpm_nnum = bpm.getNnum() == null ? UFDouble.ZERO_DBL : bpm
					.getNnum();
			UFDouble ntotalsendnum = orderItemVO.getNtotalsendnum() == null ? UFDouble.ZERO_DBL
					: orderItemVO.getNtotalsendnum();
			if (bpm_nnum.sub(ntotalsendnum).doubleValue() < 0) {
				throw new BusinessException("�������Ϸ� :��" + bpm.getVbdef20()
						+ " �����޶��������:" + bpm_nnum + ".����С�����ۼƷ���������:"
						+ ntotalsendnum);
			}
			// 5. ���ۺͽ���޸�ʱ�����û���γ����۷�Ʊ��������ֱ���޸ĵ��ۺͽ�ͬʱ����NC�������½ӿڽ��ж��������
			// ���ȫ���Ѿ����ɷ�Ʊ������ʾ����NC��Ʊɾ������е��ۺͽ��ĵ���

			// ��������Ѿ����ɷ�Ʊ���������޸ĵ�ǰ������Ϊ�Ѿ�ִ�з�Ʊ������ͬʱ���ӵڶ��У���ʣ�������ͽ����������ӣ�
			UFDouble ntotalinvoicenum = orderItemVO.getNtotalinvoicenum() == null ? UFDouble.ZERO_DBL
					: orderItemVO.getNtotalinvoicenum();
			if (ntotalinvoicenum.doubleValue() > 0) {
				// BPMͬ���󣬵��ۻ����½��м��㣬���ֻ���ƽ�����������ȣ���
				UFDouble bpm_norigtaxmny = bpm.getNorigtaxmny() == null ? UFDouble.ZERO_DBL
						: bpm.getNorigtaxmny();
				UFDouble norigtaxmny = orderItemVO.getNorigtaxmny() == null ? UFDouble.ZERO_DBL
						: orderItemVO.getNorigtaxmny();
				if (bpm_norigtaxmny.sub(norigtaxmny).doubleValue() != 0) {
					throw new BusinessException("�������Ϸ� :��" + bpm.getVbdef20()
							+ "�Ѿ�����Ʊ���������޶����,��ɾ����Ʊ�������޶��� �����޶���Ľ��:"
							+ bpm_norigtaxmny + ",�޶�ǰ��" + bpm_norigtaxmny);
				}

			}

			if (bpm_nnum.sub(ntotalinvoicenum).doubleValue() < 0) {
				throw new BusinessException("�������Ϸ� :��" + bpm.getVbdef20()
						+ " �����޶��������:" + bpm_nnum + ".����С���ۼƿ�Ʊ������ :"
						+ ntotalsendnum);
			}

			for (String attr : attributeNames) {
				if("ts".equalsIgnoreCase(attr)){
					continue;
				}
				if("csaleorderid".equalsIgnoreCase(attr)){
					continue;
				}
				if("csaleorderbid".equalsIgnoreCase(attr)){
					continue;
				}
				if("ctaxcodeid".equalsIgnoreCase(attr)){
					continue;
				}
				if(attr.endsWith("nnum") && ! attr.equalsIgnoreCase("nnum")){
					continue;
				}
				orderItemVO
						.setAttributeValue(attr, bpm.getAttributeValue(attr));
			}
	
		}

	}

	private void updateHVO(SaleOrderHistoryVO hisvo, SaleOrderHistoryVO bill) {
		// TODO �Զ����ɵķ������
		String[] attributeNames = hisvo.getParentVO().getAttributeNames();
		SaleOrderHistoryHVO hvo = hisvo.getParentVO();
		Integer nversion = hvo.getIversion();
		SaleOrderHistoryHVO hvo_bpm = bill.getParentVO();
		for (String attr : attributeNames) {
			if("ts".equalsIgnoreCase(attr)){
				continue;
			}
			if("iversion".equalsIgnoreCase(attr)){
				continue;
			}
			hvo.setAttributeValue(attr, hvo_bpm.getAttributeValue(attr));
		}
//		hvo.setIversion(++nversion);
	}

	private SaleOrderVO queryVOByPk(String csaleorderid)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		SaleOrderVO[] vos = new BillQuery<>(SaleOrderVO.class)
				.query(new String[] { csaleorderid });
		if (vos == null || vos.length == 0) {
			throw new BusinessException("��������:" + csaleorderid + "δ��ѯ����Ӧ�����۶���.");
		}
		return vos[0];
	}

	private void checkData(SaleOrderHistoryVO resvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("δ��ȡ��ת���������");
		if (StringUtils.isEmpty(resvo.getParentVO().getCsaleorderid())) {
			throw new BusinessException("��Ҫ�Զ��ֶ�Csaleorderid��Ӧ�����۶�������.");

		}
	}

}
