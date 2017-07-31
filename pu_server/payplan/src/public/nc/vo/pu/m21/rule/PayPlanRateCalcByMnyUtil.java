package nc.vo.pu.m21.rule;

import nc.pubitf.uapbd.CurrencyRateUtil;
import nc.vo.pu.m21.entity.PayPlanVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.scmpub.payterm.pay.AbstractPayPlanVO;

/**
 * @since 6.0
 * @version 2011-1-23 下午02:23:57
 * @author wuxla
 */

public class PayPlanRateCalcByMnyUtil {
	public static final UFDouble UF100 = new UFDouble(100);

	private UFDouble accMny = UFDouble.ZERO_DBL;

	private UFDouble accOrigMny = UFDouble.ZERO_DBL;

	private UFDouble accRate = UFDouble.ZERO_DBL;

	private String ccurrencyid;

	private String corigcurrencyid;

	private UFDouble ntempMny = UFDouble.ZERO_DBL;

	private UFDouble ntotallocalmny = UFDouble.ZERO_DBL;

	private UFDouble ntotalorigmny = UFDouble.ZERO_DBL;

	public void calcMnyByRate(int row, UFDouble norigmny, PayPlanVO[] plans) {

		if (plans == null || plans.length == 0)
			return;
		this.init(row, norigmny, plans);
		if (MathTool.isZero(this.ntotalorigmny)) {
			return;
		}
		UFDouble nrate = null;
		UFDouble sumrate = UFDouble.ZERO_DBL;
		UFDouble nmny = null;
		if (0 == MathTool.compareTo(this.ntotalorigmny, this.accOrigMny)) {
			nrate = MathTool.sub(PayPlanRateCalcByMnyUtil.UF100, this.accRate);
			nmny = MathTool.sub(this.ntotallocalmny, this.accMny);
		} else {
			nmny = this.ntempMny;
			int j = 0;
			for (int i = 0; i < plans.length; i++) {
				PayPlanVO planvo = plans[i];
				planvo.setAttributeValue(AbstractPayPlanVO.NTOTALORIGMNY,
						this.accOrigMny);
				UFDouble indexNorigmny = (UFDouble) planvo
						.getAttributeValue(AbstractPayPlanVO.NORIGMNY);
				if (indexNorigmny == null) {
					return;
				}
				nrate = indexNorigmny.div(this.accOrigMny,
						UFDouble.DEFAULT_POWER).multiply(
						PayPlanRateCalcByMnyUtil.UF100, 2);
				j++;
				if (j == plans.length) {
					nrate = MathTool.sub(PayPlanRateCalcByMnyUtil.UF100,
							sumrate);
				} else {
					sumrate = sumrate.add(nrate);
				}
				planvo.setAttributeValue(AbstractPayPlanVO.NRATE, nrate);
			}
		}
		PayPlanVO planvo = plans[row];
		planvo.setAttributeValue(AbstractPayPlanVO.NMNY, nmny);
	}

	private void init(int row, UFDouble norigmny, PayPlanVO[] plans) {

		if (plans == null || plans.length == 0)
			return;
		for (int i = 0; i < plans.length; i++) {
			if (row == i) {
				continue;
			}
			PayPlanVO planvo = plans[i];
			UFDouble irate = (UFDouble) planvo
					.getAttributeValue(AbstractPayPlanVO.NRATE);
			this.accRate = MathTool.add(this.accRate, irate);
			UFDouble inorigmny = (UFDouble) planvo
					.getAttributeValue(AbstractPayPlanVO.NORIGMNY);
			this.accOrigMny = MathTool.add(this.accOrigMny, inorigmny);
			UFDouble inmny = (UFDouble) planvo
					.getAttributeValue(AbstractPayPlanVO.NMNY);
			this.accMny = MathTool.add(this.accMny, inmny);
		}
		// 加上操作行
		this.accOrigMny = MathTool.add(this.accOrigMny, norigmny);

		this.ntotalorigmny = (UFDouble) plans[row]
				.getAttributeValue(AbstractPayPlanVO.NTOTALORIGMNY);
		this.corigcurrencyid = (String) plans[row]
				.getAttributeValue(AbstractPayPlanVO.CORIGCURRENCYID);
		this.ccurrencyid = (String) plans[row]
				.getAttributeValue(AbstractPayPlanVO.CCURRENCYID);
		UFDouble nexchangerate = (UFDouble) plans[row]
				.getAttributeValue(AbstractPayPlanVO.NEXCHANGERATE);
		String pk_fiorg = (String) plans[row]
				.getAttributeValue(AbstractPayPlanVO.PK_FINANCEORG);
		CurrencyRateUtil util = CurrencyRateUtil.getInstanceByOrg(pk_fiorg);
		try {
			this.ntotallocalmny = MathTool.nvl(util.getAmountByOpp(
					this.corigcurrencyid, this.ccurrencyid, this.ntotalorigmny,
					nexchangerate, new UFDate()));
			this.ntempMny = util.getAmountByOpp(this.corigcurrencyid,
					this.ccurrencyid, norigmny, nexchangerate, new UFDate());
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
	}
}
