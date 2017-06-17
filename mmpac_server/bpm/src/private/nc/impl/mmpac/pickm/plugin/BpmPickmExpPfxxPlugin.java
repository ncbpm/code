package nc.impl.mmpac.pickm.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.itf.mmpac.pickm.IPickmMaintainService;
import nc.itf.mmpac.pickm.IPickmQueryService;
import nc.vo.mmpac.pickm.entity.AggPickmVO;
import nc.vo.mmpac.pickm.entity.PickmHeadVO;
import nc.vo.mmpac.pickm.entity.PickmItemVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;

public class BpmPickmExpPfxxPlugin<T extends AggPickmVO> extends
		nc.bs.pfxx.plugin.AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		AggPickmVO bill = (AggPickmVO) vo;

		if (bill == null || bill.getParentVO() == null
				|| bill.getChildrenVO() == null
				|| bill.getChildrenVO().length == 0)
			throw new BusinessException("�������ݳ���");

		PickmHeadVO headvo = (PickmHeadVO) bill.getParentVO();

		if (headvo.getPrimaryKey() == null) {
			throw new BusinessException("���ݵ������ֶβ���Ϊ�գ�������ֵ");
		}

		IPickmQueryService queryService = NCLocator.getInstance().lookup(
				IPickmQueryService.class);

		AggPickmVO oldbill = queryService.querySingleBillByPk(headvo
				.getPrimaryKey());

		if (oldbill == null)
			throw new BusinessException("�ñ��ϼƻ������ڣ����������Ƿ���ȷ");

		PickmHeadVO oldheadvo = (PickmHeadVO) oldbill.getParentVO();

		if (oldheadvo.getFbillstatus() != null
				&& oldheadvo.getFbillstatus().intValue() == 2)
			throw new BusinessException("�ñ��ϼƻ��Ѿ���ɣ����ܱ��");

		oldheadvo.setStatus(VOStatus.UPDATED);
		// ������������
		PickmItemVO[] items = createBvos((PickmItemVO[]) bill.getChildrenVO(),
				(PickmItemVO[]) oldbill.getChildrenVO());
		oldbill.setParentVO(oldheadvo);
		oldbill.setChildrenVO(items);
		IPickmMaintainService manageService = NCLocator.getInstance().lookup(
				IPickmMaintainService.class);
		AggPickmVO[] returnvo = manageService
				.updateForYL(new AggPickmVO[] { oldbill });

		if (returnvo == null || returnvo.length == 0)
			return null;
		return returnvo[0].getPrimaryKey();
	}

	private PickmItemVO[] createBvos(PickmItemVO[] items, PickmItemVO[] olditems)
			throws BusinessException {

		ArrayList<PickmItemVO> al = new ArrayList<>();
		Map<String, PickmItemVO> map = new HashMap<>();

		for (PickmItemVO bvo : olditems) {
			map.put(bvo.getCpickm_bid(), bvo);
		}

		for (PickmItemVO bvo : items) {

			PickmItemVO oldvo1 = map.get(bvo.getCpickm_bid());
			if (oldvo1 == null) {
				bvo.setStatus(VOStatus.NEW);
				al.add(bvo);
			} else {
				if (bvo.getNaccoutnum() == null
						|| bvo.getNplanoutnum().compareTo(UFDouble.ZERO_DBL) == 0) {
					if (bvo.getNplanoutnum() != null
							&& bvo.getNplanoutnum()
									.compareTo(UFDouble.ZERO_DBL) < 0) {
						oldvo1.setStatus(VOStatus.DELETED);
					} else {
						oldvo1.setStatus(VOStatus.UPDATED);
						oldvo1.setNplanoutnum(bvo.getNplanoutnum());
						oldvo1.setNplanoutastnum(bvo.getNplanoutastnum());
					}
				} else {
					throw new BusinessException("�к�:" + bvo.getVrowno()
							+ "�Ѿ�ִ�г�����в�����ɾ���ͼ���ִ�������޸�");
				}
				al.add(oldvo1);
			}

		}

		if (al != null || al.size() > 0) {
			return al.toArray(new PickmItemVO[al.size()]);
		}

		return null;
	}

}
