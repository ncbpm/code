package nc.ui.ic.m4d.handler;

import nc.bs.framework.common.NCLocator;
import nc.itf.so.m30.self.ISaleOrderMaintain;
import nc.ui.ic.pub.handler.card.ICCardEditEventHandler;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeBatchEditEvent;
import nc.vo.pub.BusinessException;
import nc.vo.scmpub.res.billtype.ICBillType;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderVO;

//如果是材料出库单 billType： 4D ，则需要根据批次号（为销售订单的单据号）， 查 销售订单，将非赠品的第一行记录 设置为 当前的 产成品
public class CvbatchcodeHandler extends ICCardEditEventHandler {

	@Override
		public void beforeCardBodyBatchEdit(CardBodyBeforeBatchEditEvent event) {
			// TODO 自动生成的方法存根
			super.beforeCardBodyBatchEdit(event);
		}
  @Override
	public void afterCardBodyEdit(CardBodyAfterEditEvent event) {
		// TODO 自动生成的方法存根
	  if(this.getEditorModel().getICBillType().equals(ICBillType.MaterialOut)){
      	ISaleOrderMaintain queryService = NCLocator.getInstance().lookup(ISaleOrderMaintain.class);
      	String pkOrg = this.getEditorModel().getCardPanelWrapper().getBillCardPanel().getHeadItem("pk_org").getValueObject().toString();
      
  		int rows = this.getEditorModel().getCardPanelWrapper().getBillCardPanel().getRowCount();
      	for(int i=0; i< rows; i++){
      		try {
      			//按照左侧9位和11位检索即可 现在编码旧的是9位，新的是11位
      			Object vbillcode = this.getEditorModel().getCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vbatchcode");
  				if(vbillcode == null){
  					//设置成本对象为null
  					this.getEditorModel().getCardPanelWrapper().getBillCardPanel().setBodyValueAt(null, i, "ccostobject");
  					continue;
  				}
  				SaleOrderVO[] orders = queryService.querySaleOrder("select csaleorderid from so_saleorder where "
  						+ " nvl(dr,0)=0 and pk_org='"+pkOrg+"' and  (vbillcode=substr('"+vbillcode.toString()+"',1,9) or vbillcode=substr('"+vbillcode.toString()+"',1,11))");
  				if(orders != null && orders.length >0){
  					SaleOrderBVO[] bodyList = orders[0].getChildrenVO();
  	  				//找第一个非赠品物料
  	  				for(SaleOrderBVO ele : bodyList){
  	  					if(!ele.getBlargessflag().booleanValue()){  	  						
  	  						this.getEditorModel().getCardPanelWrapper().getBillCardPanel().setBodyValueAt(ele.getCmaterialvid(), i, "ccostobject");
  	  						//10个物料自由属性
  	  						for(int m=1;m<=10;m++){
  	  							String key = "vfree"+m;
  	  							BillItem bodyItem = getEditorModel().getCardPanelWrapper().getBillCardPanel().getBodyItem(key);
  	  							if(bodyItem!=null && bodyItem.isShowFlag()){
  	  								this.getEditorModel().getCardPanelWrapper().getBillCardPanel().setBodyValueAt(ele.getAttributeValue(key), i, key);
  	  							}
  	  						}
  	  						break;
  	  					}
  	  				}
  				}
  			} catch (BusinessException e) {
  				// TODO 自动生成的 catch 块
  				e.printStackTrace();
  			}
      	}
      }
	}

}
