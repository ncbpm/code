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

//����ǲ��ϳ��ⵥ billType�� 4D ������Ҫ�������κţ�Ϊ���۶����ĵ��ݺţ��� �� ���۶�����������Ʒ�ĵ�һ�м�¼ ����Ϊ ��ǰ�� ����Ʒ
public class CvbatchcodeHandler extends ICCardEditEventHandler {

	@Override
		public void beforeCardBodyBatchEdit(CardBodyBeforeBatchEditEvent event) {
			// TODO �Զ����ɵķ������
			super.beforeCardBodyBatchEdit(event);
		}
  @Override
	public void afterCardBodyEdit(CardBodyAfterEditEvent event) {
		// TODO �Զ����ɵķ������
	  if(this.getEditorModel().getICBillType().equals(ICBillType.MaterialOut)){
      	ISaleOrderMaintain queryService = NCLocator.getInstance().lookup(ISaleOrderMaintain.class);
      	String pkOrg = this.getEditorModel().getCardPanelWrapper().getBillCardPanel().getHeadItem("pk_org").getValueObject().toString();
      
  		int rows = this.getEditorModel().getCardPanelWrapper().getBillCardPanel().getRowCount();
      	for(int i=0; i< rows; i++){
      		try {
      			//�������9λ��11λ�������� ���ڱ���ɵ���9λ���µ���11λ
      			Object vbillcode = this.getEditorModel().getCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vbatchcode");
  				if(vbillcode == null){
  					//���óɱ�����Ϊnull
  					this.getEditorModel().getCardPanelWrapper().getBillCardPanel().setBodyValueAt(null, i, "ccostobject");
  					continue;
  				}
  				SaleOrderVO[] orders = queryService.querySaleOrder("select csaleorderid from so_saleorder where "
  						+ " nvl(dr,0)=0 and pk_org='"+pkOrg+"' and  (vbillcode=substr('"+vbillcode.toString()+"',1,9) or vbillcode=substr('"+vbillcode.toString()+"',1,11))");
  				if(orders != null && orders.length >0){
  					SaleOrderBVO[] bodyList = orders[0].getChildrenVO();
  	  				//�ҵ�һ������Ʒ����
  	  				for(SaleOrderBVO ele : bodyList){
  	  					if(!ele.getBlargessflag().booleanValue()){  	  						
  	  						this.getEditorModel().getCardPanelWrapper().getBillCardPanel().setBodyValueAt(ele.getCmaterialvid(), i, "ccostobject");
  	  						//10��������������
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
  				// TODO �Զ����ɵ� catch ��
  				e.printStackTrace();
  			}
      	}
      }
	}

}
