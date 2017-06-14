package nc.vo.ic.fivemetals;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.ic.fivemetals.FiveMetalsHVO")

public class AggFiveMetalsVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggFiveMetalsVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public FiveMetalsHVO getParentVO(){
	  	return (FiveMetalsHVO)this.getParent();
	  }
	  
}