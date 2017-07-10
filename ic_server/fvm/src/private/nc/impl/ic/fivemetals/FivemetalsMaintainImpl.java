package nc.impl.ic.fivemetals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.ic.fivemetals.action.FivemetalsSaveAction;
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
				"pk_org", "vcardno", "vbillstatus", "vdepartment" });

		FiveMetalsHVO hvo = (FiveMetalsHVO) bill.getParentVO();

		String condition = " and pk_group = '" + hvo.getPk_group()
				+ "' and pk_org ='" + hvo.getPk_org() + "' and vcardno = '"
				+ hvo.getVcardno() + "' ";
		FivemetalsDao fiveDao = new FivemetalsDao();
		FiveMetalsHVO oldvo = fiveDao.getFiveMetalsHVOByCondition(condition);
		FiveMetalsBVO[] bvos = null;
		String vsourcetype = null;
		switch (hvo.getVbillstatus()) {
		case 3:
			// 消费
			fiveDao.checkFiveMetalsHVO(oldvo);

			bvos = (FiveMetalsBVO[]) bill.getChildrenVO();
			if (bvos == null || bvos.length == 0)
				throw new BusinessException("传入表体信息不完整");
			for (FiveMetalsBVO bvo : bvos) {
				bvo.setItype(Integer.parseInt(CostTypeEnum.消费.getEnumValue()
						.getValue()));
				bvo.setNmny(bvo.getNmny());
			}
			vsourcetype = "材料出库单";
			vo = savebill(bill, oldvo, vsourcetype);
			break;
		case 4:
			// 充值 卡号不存在 建卡 存在 直接充值 (保留余额)
			bvos = (FiveMetalsBVO[]) bill.getChildrenVO();
			if (bvos == null || bvos.length == 0)
				throw new BusinessException("传入表体信息不完整");
			for (FiveMetalsBVO bvo : bvos) {
				bvo.setItype(Integer.parseInt(CostTypeEnum.充值.getEnumValue()
						.getValue()));
				bvo.setNmny(bvo.getNmny());
			}
			vsourcetype = "五金预算充值 ";
			vo = savebill(bill, oldvo, vsourcetype);
			break;
		case 7:
			// 充值 卡号不存在 建卡 存在 直接充值 (作废余额)
			bvos = (FiveMetalsBVO[]) bill.getChildrenVO();
			if (bvos == null || bvos.length == 0)
				throw new BusinessException("传入表体信息不完整");
			for (FiveMetalsBVO bvo : bvos) {
				bvo.setItype(Integer.parseInt(CostTypeEnum.充值.getEnumValue()
						.getValue()));
				bvo.setNmny(bvo.getNmny());
			}
			vsourcetype = "五金预算充值 ";
			vo = savebill1(bill, oldvo, vsourcetype);
			break;
		case 5:
			// 挂失 注销
			fiveDao.checkFiveMetalsHVO(oldvo);
			vo = disableAggFiveMetalsVO(oldvo);
			break;
		case 6:
			// 结转
			vsourcetype = "五金预算结转";
			vo = jzbill(bill, oldvo, vsourcetype);
			break;
		default:
			throw new BusinessException("传入状态出错");

		}
		return vo;
	}

	private AggFiveMetalsVO savebill(AggFiveMetalsVO bill, FiveMetalsHVO oldvo,
			String vsourcetype) throws BusinessException {

		FiveMetalsHVO hvo = (FiveMetalsHVO) bill.getParentVO();

		VOCheckUtil.checkBodyNotNullFields(bill, new String[] {
				"vsourcebillno", "vsourcebillid", "nmny", "cperiod" });

		AggFiveMetalsVO aggvo = new AggFiveMetalsVO();
		if (oldvo == null) {
			if (hvo.getVproject() != null) {
				hvo.setVcardtype(Integer.parseInt(CardTypeEnum.项目卡
						.getEnumValue().getValue()));
			} else {
				hvo.setVcardtype(Integer.parseInt(CardTypeEnum.部门卡
						.getEnumValue().getValue()));
			}
			hvo.setVbillstatus(Integer.parseInt(CardStatusEnum.启用
					.getEnumValue().getValue()));
			hvo.setStatus(VOStatus.NEW);
			aggvo.setParentVO(hvo);
		} else {
			FivemetalsDao fiveDao = new FivemetalsDao();
			fiveDao.checkFiveMetalsHVO(oldvo);
			oldvo.setStatus(VOStatus.UPDATED);
			aggvo.setParentVO(oldvo);
		}
		FiveMetalsBVO[] bvos = (FiveMetalsBVO[]) bill.getChildrenVO();
		for (FiveMetalsBVO bvo : bvos) {
			bvo.setPk_fivemetals_h(aggvo.getParentVO().getPk_fivemetals_h());
			bvo.setStatus(VOStatus.NEW);
			bvo.setVsourcetype(vsourcetype);
		}

		aggvo.setChildrenVO(bvos);
		FivemetalsSaveAction action = new FivemetalsSaveAction();
		AggFiveMetalsVO[] returnvos = action
				.save(new AggFiveMetalsVO[] { aggvo });

		if (returnvos == null || returnvos.length == 0)
			return null;
		return returnvos[0];
	}

	private AggFiveMetalsVO savebill1(AggFiveMetalsVO bill,
			FiveMetalsHVO oldvo, String vsourcetype) throws BusinessException {

		FiveMetalsHVO hvo = (FiveMetalsHVO) bill.getParentVO();

		VOCheckUtil.checkBodyNotNullFields(bill, new String[] {
				"vsourcebillno", "vsourcebillid", "nmny", "cperiod" });

		AggFiveMetalsVO aggvo = new AggFiveMetalsVO();
		FiveMetalsBVO[] bvos = null;
		if (oldvo == null) {
			if (hvo.getVproject() != null) {
				hvo.setVcardtype(Integer.parseInt(CardTypeEnum.项目卡
						.getEnumValue().getValue()));
			} else {
				hvo.setVcardtype(Integer.parseInt(CardTypeEnum.部门卡
						.getEnumValue().getValue()));
			}
			hvo.setVbillstatus(Integer.parseInt(CardStatusEnum.启用
					.getEnumValue().getValue()));
			hvo.setStatus(VOStatus.NEW);
			aggvo.setParentVO(hvo);
			bvos = (FiveMetalsBVO[]) bill.getChildrenVO();
		} else {
			FivemetalsDao fiveDao = new FivemetalsDao();
			fiveDao.checkFiveMetalsHVO(oldvo);
			oldvo.setStatus(VOStatus.UPDATED);
			aggvo.setParentVO(oldvo);
			bvos = createFiveMetalsBVO1((FiveMetalsBVO[]) bill.getChildrenVO(),
					oldvo.getPk_fivemetals_h());
		}
		for (FiveMetalsBVO bvo : bvos) {
			bvo.setPk_fivemetals_h(aggvo.getParentVO().getPk_fivemetals_h());
			bvo.setStatus(VOStatus.NEW);
			bvo.setVsourcetype(vsourcetype);
		}

		aggvo.setChildrenVO(bvos);
		FivemetalsSaveAction action = new FivemetalsSaveAction();
		AggFiveMetalsVO[] returnvos = action
				.save(new AggFiveMetalsVO[] { aggvo });

		if (returnvos == null || returnvos.length == 0)
			return null;
		return returnvos[0];
	}

	private FiveMetalsBVO[] createFiveMetalsBVO1(FiveMetalsBVO[] bvos,
			String pk_fivemetals_h) throws DAOException {

		ArrayList<FiveMetalsBVO> al = new ArrayList<>();

		FivemetalsDao dao = new FivemetalsDao();
		Map<String, UFDouble> retMap = dao
				.getFivemetalsBalance(pk_fivemetals_h);
		for (FiveMetalsBVO bvo : bvos) {

			FiveMetalsBVO bvo1 = (FiveMetalsBVO) bvo.clone();
			Object nmny = retMap.get(bvo.getCperiod());
			if (nmny != null) {
				if (nmny instanceof BigDecimal) {
					BigDecimal i = (BigDecimal) nmny;
					bvo1.setNmny(SafeCompute.multiply(
							new UFDouble(i.doubleValue()), new UFDouble(-1)));
				} else if (nmny instanceof Integer) {
					Integer i = (Integer) nmny;
					bvo1.setNmny(SafeCompute.multiply(
							new UFDouble(i.doubleValue()), new UFDouble(-1)));
				}

				bvo1.setVremark("作废余额");
				al.add(bvo1);
			}
			al.add(bvo);
		}
		return al.toArray(new FiveMetalsBVO[al.size()]);

	}

	private AggFiveMetalsVO disableAggFiveMetalsVO(FiveMetalsHVO oldvo)
			throws BusinessException {

		VOUpdate<ISuperVO> update = new VOUpdate();
		oldvo.setStatus(VOStatus.UPDATED);
		oldvo.setVbillstatus(Integer.parseInt(CardStatusEnum.停用.getEnumValue()
				.getValue()));
		update.update(new FiveMetalsHVO[] { oldvo },
				new String[] { "vbillstatus" });
		AggFiveMetalsVO aggvo = queryByPk(oldvo.getPrimaryKey());

		return aggvo;
	}

	private AggFiveMetalsVO jzbill(AggFiveMetalsVO bill, FiveMetalsHVO oldvo,
			String vsourcetype) throws BusinessException {

		VOCheckUtil.checkBodyNotNullFields(bill, new String[] {
				"vsourcebillno", "vsourcebillid", "nmny", "cperiod" });
		FivemetalsDao fiveDao = new FivemetalsDao();
		fiveDao.checkFiveMetalsHVO(oldvo);
		FiveMetalsBVO[] bvos = (FiveMetalsBVO[]) bill.getChildrenVO();
		if (bvos == null || bvos.length == 0)
			throw new BusinessException("传入表体信息不完整");
		for (FiveMetalsBVO bvo : bvos) {
			bvo.setItype(Integer.parseInt(CostTypeEnum.充值.getEnumValue()
					.getValue()));
			bvo.setNmny(bvo.getNmny());
		}
		bvos = createFiveMetalsBVO(bvos);

		AggFiveMetalsVO aggvo = new AggFiveMetalsVO();
		oldvo.setStatus(VOStatus.UPDATED);
		aggvo.setParentVO(oldvo);
		// FiveMetalsBVO[] bvos = (FiveMetalsBVO[]) bill.getChildrenVO();
		for (FiveMetalsBVO bvo : bvos) {
			bvo.setPk_fivemetals_h(aggvo.getParentVO().getPk_fivemetals_h());
			bvo.setStatus(VOStatus.NEW);
			bvo.setVsourcetype(vsourcetype);
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

}
