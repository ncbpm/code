package nc.bs.ic.m4d.cancelsign.rule;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.ic.fivemetals.IFivemetalsMaintain;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.ic.fivemetals.CardStatusEnum;
import nc.vo.ic.fivemetals.DateUtils;
import nc.vo.ic.fivemetals.FiveMetalsBVO;
import nc.vo.ic.fivemetals.FiveMetalsHVO;
import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
import nc.vo.ic.m4d.entity.MaterialOutVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.trade.voutils.SafeCompute;

/**
 * 
 * 更新五金预算消费记录
 */
public class AfterCancelSignRuleFivemetals implements IRule<MaterialOutVO> {

	@Override
	public void process(MaterialOutVO[] vos) {
		if ((vos == null) || (vos.length == 0)) {
			return;
		}
		try {
			IFivemetalsMaintain mainimpl = NCLocator.getInstance().lookup(
					IFivemetalsMaintain.class);
			for (MaterialOutVO vo : vos) {
				AggFiveMetalsVO bill = changeMaterialOutVO(vo);
				if (bill != null)
					mainimpl.operatebill(bill);
			}

		} catch (BusinessException ex) {
			ExceptionUtils.wrappException(ex);
		}catch (Exception ex) {
			ExceptionUtils.wrappException(ex);
		}
	}

	private AggFiveMetalsVO changeMaterialOutVO(MaterialOutVO vo)
			throws BusinessException {

		if (vo == null || vo.getParentVO() == null)
			throw new BusinessException("材料出库数据出错");

		if (vo.getParentVO().getVdef20() == null)
			return null;
		AggFiveMetalsVO bill = new AggFiveMetalsVO();
		String period = DateUtils.getPeriod(vo.getParentVO().getDbilldate());
		FiveMetalsHVO hvo = new FiveMetalsHVO();
		hvo.setPk_group(vo.getParentVO().getPk_group());
		hvo.setPk_org(vo.getParentVO().getPk_org());
		hvo.setVcardno(vo.getParentVO().getVdef20());
		hvo.setVdepartment(vo.getParentVO().getCdptid());
		hvo.setVbillstatus(Integer.parseInt(CardStatusEnum.消费.getEnumValue()
				.getValue()));

		FiveMetalsBVO fbvo = new FiveMetalsBVO();
		fbvo.setCperiod(period);
		fbvo.setVsourcebillid(vo.getParentVO().getPrimaryKey());
		fbvo.setVsourcebillno(vo.getParentVO().getVbillcode());
		UFDouble sum = UFDouble.ZERO_DBL;
		MaterialOutBodyVO[] bvos = vo.getBodys();
		if (bvos == null || bvos.length == 0)
			throw new BusinessException("材料出库表体信息不完整");
		for (MaterialOutBodyVO bvo : bvos) {
			sum = SafeCompute.add(sum, bvo.getNcostmny());
		}
		fbvo.setNmny(SafeCompute.multiply(sum, new UFDouble(-1)));
		fbvo.setVremark("材料出库单据弃审");
		bill.setParentVO(hvo);
		bill.setChildrenVO(new FiveMetalsBVO[] { fbvo });
		return bill;

	}

}
