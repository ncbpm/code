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
			throw new BusinessException("传入数据出错");

		FiveMetalsHVO headvo = (FiveMetalsHVO) bill.getParentVO();
		if (headvo.getPk_group() == null) {
			throw new BusinessException("单据的所属集团字段不能为空，请输入值");
		}
		if (headvo.getPk_org() == null) {
			throw new BusinessException("单据的财务组织字段不能为空，请输入值");
		}

		if (headvo.getVcardno() == null) {
			throw new BusinessException("单据的卡号字段不能为空，请输入值");
		}

		if (headvo.getCperiod() == null) {
			throw new BusinessException("单据的月份字段不能为空，请输入值");
		}

		if (headvo.getVproject() == null && headvo.getVdepartment() == null) {
			throw new BusinessException("单据的项目字段或部门字段不能为空，请输入值");
		}

		if (headvo.getVproject() != null) {
			headvo.setVcardtype(CardTypeEnum.项目卡.getName());
		} else if (headvo.getVdepartment() != null) {
			headvo.setVcardtype(CardTypeEnum.部门卡.getName());
		}

		FiveMetalsBVO[] bvos = (FiveMetalsBVO[]) bill.getChildrenVO();

		for (FiveMetalsBVO bvo : bvos) {
			bvo.setItype(CostTypeEnum.消费.getName());
			bvo.setNmny("-" + bvo.getNmny());
		}

		IFivemetalsMaintain manageService = NCLocator.getInstance().lookup(
				IFivemetalsMaintain.class);
		AggFiveMetalsVO returnvo = manageService.operatebill(headvo, bvos,
				false);
		return returnvo.getPrimaryKey();
	}
}
