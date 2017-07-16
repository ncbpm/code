package nc.bs.ic.m45.sign.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.m45.entity.PurchaseInVO;


/**
 * 
 * 1.产成品入库如果是按照销售订单生成的销售订单，
 * 且入库的物料是销售订单上物料，则该物料+批次预留给该销售订单
 *
 */
public class AfterSignRuleForReserveProcess implements
IRule<PurchaseInVO> {

	@Override
	public void process(PurchaseInVO[] vos) {
		// TODO 自动生成的方法存根
		for(PurchaseInVO vo:vos){
			ICBillBodyVO[] bvos = vo.getChildrenVO();
			if(bvos == null || bvos.length ==0){
				continue;
			}
			
			
		}
	}

	

}
