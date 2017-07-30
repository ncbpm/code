package nc.bpm.pu.pfxx;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.vo.ic.pub.check.VOCheckUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.m21.entity.PayPlanVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;

/**
 * 采购订单付款计划BPM回写接口
 * @author liyf
 *
 */
public class OrderPayPlan extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// 1.得到转换后的VO数据,取决于向导第一步注册的VO信息
		AggregatedValueObject resvo = (AggregatedValueObject) vo;
		OrderVO bpmOrder = (OrderVO) resvo;
		// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(resvo);
		// 3.补全数据,并且调整单据状态
		fillData(resvo);
		//4. 回写更新
		updateNCVO(resvo);
		return "更新成功";


	}
	/**
	 * 更新或者新增采购付款计划明细数据
	 * @param resvo
	 */
	private void updateNCVO(AggregatedValueObject resvo) {
		// TODO 自动生成的方法存根
		OrderVO bpmOrder = (OrderVO) resvo;
		PayPlanVO[] vos = (PayPlanVO[]) bpmOrder.getChildren(PayPlanVO.class);
		List<String> cpks = new ArrayList<String>();
		for(PayPlanVO vo:vos){
			//如果付款计划为空，说明是新增的采购付款计划
			if(StringUtils.isEmpty(vo.getPk_order_payplan())){
				
			}
		}
		
	}

	/**
	 * 
	 * @param resvo
	 */
	private void fillData(AggregatedValueObject resvo) {
		// TODO 自动生成的方法存根
		
	}

	/**
	 * 
	 * @param 数据校验
	 * @throws ValidationException 
	 */
	private void checkData(AggregatedValueObject resvo) throws ValidationException {
		// TODO 自动生成的方法存根
		OrderVO bpmOrder = (OrderVO) resvo;
		PayPlanVO[] vos = (PayPlanVO[]) bpmOrder.getChildren(PayPlanVO.class);
		
		VOCheckUtil.checkBodyNotNullFields(bpmOrder, new String[] { "pk_order",
				"nshouldnum", "csourcebillhid", "csourcebillbid", "ncostprice",
				"cprojectid" });
	}
}
