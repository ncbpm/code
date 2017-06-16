package nc.bs.ic.fivemetals.plugin;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.itf.ic.fivemetals.IFivemetalsMaintain;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.ic.fivemetals.CardTypeEnum;
import nc.vo.ic.fivemetals.CostTypeEnum;
import nc.vo.ic.fivemetals.FiveMetalsBVO;
import nc.vo.ic.fivemetals.FiveMetalsHVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;

public class BpmFiveMetalsConsumeExpPfxxPlugin<T extends AggFiveMetalsVO>
		extends nc.bs.pfxx.plugin.AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {

		AggFiveMetalsVO bill = (AggFiveMetalsVO) vo;

		if (bill == null || bill.getParentVO() == null
				|| bill.getChildrenVO() == null
				|| bill.getChildrenVO().length == 0)
			throw new BusinessException("�������ݳ���");

		FiveMetalsHVO headvo = (FiveMetalsHVO) bill.getParentVO();
		if (headvo.getPk_group() == null) {
			throw new BusinessException("���ݵ����������ֶβ���Ϊ�գ�������ֵ");
		}
		if (headvo.getPk_org() == null) {
			throw new BusinessException("���ݵĲ�����֯�ֶβ���Ϊ�գ�������ֵ");
		}

		if (headvo.getVcardno() == null) {
			throw new BusinessException("���ݵĿ����ֶβ���Ϊ�գ�������ֵ");
		}

		if (headvo.getCperiod() == null) {
			throw new BusinessException("���ݵ��·��ֶβ���Ϊ�գ�������ֵ");
		}

		if (headvo.getVproject() == null && headvo.getVdepartment() == null) {
			throw new BusinessException("���ݵ���Ŀ�ֶλ����ֶβ���Ϊ�գ�������ֵ");
		}

		if (headvo.getVproject() != null) {
			headvo.setVcardtype(CardTypeEnum.��Ŀ��.getName());
		} else if (headvo.getVdepartment() != null) {
			headvo.setVcardtype(CardTypeEnum.���ſ�.getName());
		}

		FiveMetalsBVO[] bvos = (FiveMetalsBVO[]) bill.getChildrenVO();

		for (FiveMetalsBVO bvo : bvos) {
			bvo.setItype(CostTypeEnum.����.getName());
			bvo.setNmny("-" + bvo.getNmny());
		}

		IFivemetalsMaintain manageService = NCLocator.getInstance().lookup(
				IFivemetalsMaintain.class);
		AggFiveMetalsVO returnvo = manageService.operatebill(headvo, bvos,
				false);
		return returnvo.getPrimaryKey();
	}
}
