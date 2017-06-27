package nc.impl.ic.fivemetals;

import java.util.ArrayList;
import java.util.Collection;

import nc.bs.ic.fivemetals.action.FivemetalsSaveAction;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.itf.ic.fivemetals.IFivemetalsMaintain;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.MDPersistenceService;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.ic.fivemetals.CardStatusEnum;
import nc.vo.ic.fivemetals.CardTypeEnum;
import nc.vo.ic.fivemetals.CostTypeEnum;
import nc.vo.ic.fivemetals.DateUtils;
import nc.vo.ic.fivemetals.FiveMetalsBVO;
import nc.vo.ic.fivemetals.FiveMetalsHVO;
import nc.vo.ic.pub.check.VOCheckUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.voutils.SafeCompute;

public class FivemetalsMaintainImpl implements IFivemetalsMaintain {

	/***************************************************************************
	 * 返回元数据持久化服务对象
	 * 
	 * @return IMDPersistenceQueryService
	 *****************************************************************************/
	protected static IMDPersistenceQueryService getMDQueryService() {
		return MDPersistenceService.lookupPersistenceQueryService();
	}

	public AggFiveMetalsVO[] queryByCondition(String condition)
			throws BusinessException {
		Collection<?> c = getMDQueryService().queryBillOfVOByCond(
				AggFiveMetalsVO.class, condition, false);

		if (c != null && c.size() > 0) {
			return c.toArray(new AggFiveMetalsVO[c.size()]);
		}

		return null;
	}

	public AggFiveMetalsVO queryByPk(String pk) throws BusinessException {
		return getMDQueryService().queryBillOfVOByPK(AggFiveMetalsVO.class, pk,
				false);
	}

	@Override
	public AggFiveMetalsVO operatebill(AggFiveMetalsVO bill)
			throws BusinessException {

		AggFiveMetalsVO vo = null;
		if (bill == null || bill.getParentVO() == null)
			throw new BusinessException("传入数据出错");

		VOCheckUtil.checkHeadNotNullFields(bill, new String[] { "pk_group",
				"pk_org", "vcardno", "vbillstatus" });

		FiveMetalsHVO hvo = (FiveMetalsHVO) bill.getParentVO();
		FiveMetalsHVO oldvo = getFiveMetalsHVO(hvo);
		FiveMetalsBVO[] bvos = null;
		switch (hvo.getVbillstatus()) {
		case 3:
			// 消费
			checkFiveMetalsHVO(hvo);

			bvos = (FiveMetalsBVO[]) bill.getChildrenVO();
			if (bvos == null || bvos.length == 0)
				throw new BusinessException("传入表体信息不完整");
			for (FiveMetalsBVO bvo : bvos) {
				bvo.setItype(CostTypeEnum.消费.getReturnType());
				bvo.setNmny(bvo.getNmny());
			}
			vo = savebill(bill, oldvo);
			break;
		case 4:
			// 充值 卡号不存在 建卡 存在 直接充值

			bvos = (FiveMetalsBVO[]) bill.getChildrenVO();
			if (bvos == null || bvos.length == 0)
				throw new BusinessException("传入表体信息不完整");
			for (FiveMetalsBVO bvo : bvos) {
				bvo.setItype(CostTypeEnum.充值.getReturnType());
				bvo.setNmny(bvo.getNmny());
			}
			vo = savebill(bill, oldvo);
			break;
		case 5:
			// 挂失 注销
			checkFiveMetalsHVO(hvo);
			vo = disableAggFiveMetalsVO(oldvo);
			break;
		case 6:
			// 结转
			jzbill(bill, oldvo);
			break;
		default:
			throw new BusinessException("传入状态出错");

		}
		return vo;
	}

	private AggFiveMetalsVO savebill(AggFiveMetalsVO bill, FiveMetalsHVO oldvo)
			throws ValidationException {

		FiveMetalsHVO hvo = (FiveMetalsHVO) bill.getParentVO();

		VOCheckUtil.checkBodyNotNullFields(bill, new String[] {
				"vsourcebillno", "vsourcetype", "vsourcebillid", "nmny",
				"cperiod", "cprojectid" });

		AggFiveMetalsVO aggvo = new AggFiveMetalsVO();
		if (oldvo == null) {
			if (hvo.getVproject() != null) {
				hvo.setVcardtype(CardTypeEnum.项目卡.getReturnType());
			} else {
				hvo.setVcardtype(CardTypeEnum.部门卡.getReturnType());
			}
			hvo.setStatus(VOStatus.NEW);
			aggvo.setParentVO(hvo);
		} else {
			oldvo.setStatus(VOStatus.UPDATED);
			aggvo.setParentVO(oldvo);
		}
		FiveMetalsBVO[] bvos = (FiveMetalsBVO[]) bill.getChildrenVO();
		for (FiveMetalsBVO bvo : bvos) {
			bvo.setPk_fivemetals_h(aggvo.getParentVO().getPk_fivemetals_h());
			bvo.setStatus(VOStatus.NEW);
		}

		aggvo.setChildrenVO(bvos);
		FivemetalsSaveAction action = new FivemetalsSaveAction();
		AggFiveMetalsVO[] returnvos = action
				.save(new AggFiveMetalsVO[] { aggvo });

		if (returnvos == null || returnvos.length == 0)
			return null;
		return returnvos[0];
	}

	private AggFiveMetalsVO disableAggFiveMetalsVO(FiveMetalsHVO oldvo)
			throws BusinessException {

		VOUpdate<ISuperVO> update = new VOUpdate();
		oldvo.setStatus(VOStatus.UPDATED);
		oldvo.setVbillstatus(CardStatusEnum.停用.getReturnType());
		update.update(new FiveMetalsHVO[] { oldvo },
				new String[] { "vbillstatus" });
		AggFiveMetalsVO aggvo = queryByPk(oldvo.getPrimaryKey());

		return aggvo;
	}

	private AggFiveMetalsVO jzbill(AggFiveMetalsVO bill, FiveMetalsHVO oldvo)
			throws BusinessException {

		checkFiveMetalsHVO(oldvo);
		FiveMetalsBVO[] bvos = (FiveMetalsBVO[]) bill.getChildrenVO();
		if (bvos == null || bvos.length == 0)
			throw new BusinessException("传入表体信息不完整");
		for (FiveMetalsBVO bvo : bvos) {
			bvo.setItype(CostTypeEnum.充值.getReturnType());
			bvo.setNmny(bvo.getNmny());
		}
		bvos = createFiveMetalsBVO(bvos);
		VOCheckUtil.checkBodyNotNullFields(bill, new String[] {
				"vsourcebillno", "vsourcetype", "vsourcebillid", "nmny",
				"cperiod", "cprojectid" });

		AggFiveMetalsVO aggvo = new AggFiveMetalsVO();
		oldvo.setStatus(VOStatus.UPDATED);
		aggvo.setParentVO(oldvo);
		// FiveMetalsBVO[] bvos = (FiveMetalsBVO[]) bill.getChildrenVO();
		for (FiveMetalsBVO bvo : bvos) {
			bvo.setPk_fivemetals_h(aggvo.getParentVO().getPk_fivemetals_h());
			bvo.setStatus(VOStatus.NEW);
		}

		aggvo.setChildrenVO(bvos);
		FivemetalsSaveAction action = new FivemetalsSaveAction();
		AggFiveMetalsVO[] returnvos = action
				.save(new AggFiveMetalsVO[] { aggvo });

		if (returnvos == null || returnvos.length == 0)
			return null;
		return returnvos[0];
	}

	private FiveMetalsBVO[] createFiveMetalsBVO(FiveMetalsBVO[] bvos) {

		ArrayList<FiveMetalsBVO> al = new ArrayList<>();
		for (FiveMetalsBVO bvo : bvos) {
			long last = DateUtils.getPreviousMonth(new UFDate(bvo.getCperiod()
					+ "-01").toDate().getTime());
			UFDate date = new UFDate(last);
			String period = DateUtils.getPeriod(date);
			FiveMetalsBVO bvo1 = (FiveMetalsBVO) bvo.clone();
			bvo1.setCperiod(period);
			bvo1.setNmny(SafeCompute.multiply(bvo.getNmny(), new UFDouble(-1)));
			al.add(bvo1);
			al.add(bvo);
		}
		return al.toArray(new FiveMetalsBVO[al.size()]);

	}

	private FiveMetalsHVO getFiveMetalsHVO(FiveMetalsHVO hvo) {
		VOQuery<ISuperVO> query = new VOQuery(FiveMetalsHVO.class);
		String condition = " and pk_group = '" + hvo.getPk_group()
				+ "' and pk_org ='" + hvo.getPk_org() + "' and vcardno = '"
				+ hvo.getVcardno() + "' ";
		FiveMetalsHVO[] hvos = (FiveMetalsHVO[]) query.query(condition, null);
		if (hvos == null || hvos.length == 0)
			return null;
		return hvos[0];
	}

	private void checkFiveMetalsHVO(FiveMetalsHVO hvo) throws BusinessException {
		if (hvo == null) {
			throw new BusinessException("该卡号没有建卡,请检查卡号是否正确 ！");
		}

		if (!(hvo.getVbillstatus() != null && hvo.getVbillstatus().intValue() == CardStatusEnum.启用
				.getReturnType())) {
			throw new BusinessException("该卡号为非启用状态,请检查卡号状态是否正确 ！");
		}

	}
}
