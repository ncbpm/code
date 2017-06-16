package nc.impl.ic.fivemetals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.ic.fivemetals.action.FivemetalsSaveAction;
import nc.impl.pubapp.pattern.data.vo.VODelete;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.ic.fivemetals.IFivemetalsMaintain;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.MDPersistenceService;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.ic.fivemetals.FiveMetalsBVO;
import nc.vo.ic.fivemetals.FiveMetalsHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.VOStatus;

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
	public AggFiveMetalsVO operatebill(FiveMetalsHVO hvo, FiveMetalsBVO[] bvos,
			boolean isdel) throws BusinessException {
		AggFiveMetalsVO aggvo = null;
		if (isdel) {
			deletebill(hvo, bvos);
		} else {
			AggFiveMetalsVO[] vos = savebill(hvo, bvos);
			aggvo = vos[0];
		}
		return aggvo;
	}

	private AggFiveMetalsVO[] savebill(FiveMetalsHVO hvo, FiveMetalsBVO[] bvos) {

		FiveMetalsHVO oldvo = getFiveMetalsHVO(hvo);
		AggFiveMetalsVO aggvo = new AggFiveMetalsVO();
		if (oldvo == null) {
			hvo.setStatus(VOStatus.NEW);
			aggvo.setParentVO(hvo);
		} else {
			aggvo.setParentVO(oldvo);
		}

		for (FiveMetalsBVO bvo : bvos) {
			bvo.setPk_fivemetals_h(aggvo.getParentVO().getPk_fivemetals_h());
			bvo.setStatus(VOStatus.NEW);
		}

		aggvo.setChildrenVO(bvos);
		FivemetalsSaveAction action = new FivemetalsSaveAction();
		AggFiveMetalsVO[] returnvos = action
				.save(new AggFiveMetalsVO[] { aggvo });
		return returnvos;
	}

	// 删除一行数据

	private AggFiveMetalsVO deletebill(FiveMetalsHVO hvo, FiveMetalsBVO[] bvos)
			throws BusinessException {

		FiveMetalsHVO oldvo = getFiveMetalsHVO(hvo);
		VOQuery<ISuperVO> query = new VOQuery(FiveMetalsBVO.class);
		FiveMetalsBVO[] bvos1 = (FiveMetalsBVO[]) query.query(
				" and pk_fivemetals_h ='" + oldvo.getPrimaryKey() + "'", null);

		if (bvos1 == null || bvos1.length == 0) {
			throw new BusinessException("该卡号不存在充值消费记录");
		}

		ArrayList<FiveMetalsBVO> al = new ArrayList<>();
		Map<String, FiveMetalsBVO> map = new HashMap<>();

		for (FiveMetalsBVO bvo : bvos1) {
			map.put(bvo.getVsourcebillid(), bvo);
		}

		for (FiveMetalsBVO bvo : bvos) {

			FiveMetalsBVO oldvo1 = map.get(bvo.getVsourcebillid());
			if (oldvo1 == null) {
				throw new BusinessException("来源单据号" + bvo.getVsourcebillno()
						+ "不存在充值消费记录,无法删除！");
			}
			al.add(oldvo1);
		}
		VODelete<ISuperVO> bo = new VODelete<ISuperVO>();
		bo.delete(al.toArray(new FiveMetalsBVO[al.size()]));

		AggFiveMetalsVO aggvo = new AggFiveMetalsVO();
		aggvo.setParentVO(oldvo);
		return aggvo;

	}

	private FiveMetalsHVO getFiveMetalsHVO(FiveMetalsHVO hvo) {
		VOQuery<ISuperVO> query = new VOQuery(FiveMetalsHVO.class);
		String condition = " and pk_group = '" + hvo.getPk_group()
				+ "' and pk_org ='" + hvo.getPk_org() + "' and vcardno = '"
				+ hvo.getVcardno() + "' and cperiod = '" + hvo.getCperiod()
				+ "'";
		FiveMetalsHVO[] hvos = (FiveMetalsHVO[]) query.query(condition, null);
		if (hvos == null || hvos.length == 0)
			return null;
		return hvos[0];

	}
}
