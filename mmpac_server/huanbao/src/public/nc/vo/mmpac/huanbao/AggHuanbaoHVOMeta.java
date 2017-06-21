package nc.vo.mmpac.huanbao;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggHuanbaoHVOMeta extends AbstractBillMeta{
	
	public AggHuanbaoHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.mmpac.huanbao.HuanbaoHVO.class);
		this.addChildren(nc.vo.mmpac.huanbao.HuanbaoBvo.class);
	}
}