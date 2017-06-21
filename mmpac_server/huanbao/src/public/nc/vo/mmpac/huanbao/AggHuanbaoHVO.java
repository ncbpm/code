package nc.vo.mmpac.huanbao;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.mmpac.huanbao.HuanbaoHVO")

public class AggHuanbaoHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggHuanbaoHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public HuanbaoHVO getParentVO(){
	  	return (HuanbaoHVO)this.getParent();
	  }
	  
}