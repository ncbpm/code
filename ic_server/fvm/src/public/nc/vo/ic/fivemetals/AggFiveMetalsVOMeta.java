package nc.vo.ic.fivemetals;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggFiveMetalsVOMeta extends AbstractBillMeta{
	
	public AggFiveMetalsVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.ic.fivemetals.FiveMetalsHVO.class);
		this.addChildren(nc.vo.ic.fivemetals.FiveMetalsBVO.class);
	}
}