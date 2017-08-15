package nc.ui.ic.m4d.handler;

import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.itf.so.m30.self.ISaleOrderMaintain;
import nc.ui.ic.pub.deal.RefFilter;
import nc.ui.ic.pub.handler.card.ICCardEditEventHandler;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.vo.am.proxy.AMProxy;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
import nc.vo.ic.m4d.entity.MaterialOutHeadVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.scmpub.res.billtype.ICBillType;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderVO;

//����ǲ��ϳ��ⵥ billType�� 4D ������Ҫ�������κţ�Ϊ���۶����ĵ��ݺţ��� �� ���۶�����������Ʒ�ĵ�һ�м�¼ ����Ϊ ��ǰ�� ����Ʒ
@SuppressWarnings("restriction")
public class CvbatchcodeHandler extends ICCardEditEventHandler {

  @Override
	public void afterCardBodyEdit(CardBodyAfterEditEvent event) {
		// TODO �Զ����ɵķ������
	  if(this.getEditorModel().getICBillType().equals(ICBillType.MaterialOut)){
      	ISaleOrderMaintain queryService = AMProxy.lookup(ISaleOrderMaintain.class);
      	//IMDPersistenceQueryService queryService = AMProxy.lookup(IMDPersistenceQueryService.class);
      	String pkOrg = this.getEditorModel().getCardPanelWrapper().getBillCardPanel().getHeadItem("pk_org").getValueObject().toString();
      	DataAccessUtils dbWrapper = new DataAccessUtils();
  		String saleorderBillId = null;
  		int rows = this.getEditorModel().getCardPanelWrapper().getBillCardPanel().getRowCount();
      	for(int i=0; i< rows; i++){
      		try {
      			Object vbillcode = this.getEditorModel().getCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vbatchcode");
  				//NCObject[] orders = queryService.queryBillOfNCObjectByCond(SaleOrderVO.class, "vbillcode="+item.getVbatchcode(), false);
  				if(vbillcode == null){
  					//���óɱ�����Ϊnull
  					//this.getEditorModel().getCardPanelWrapper().getBillCardPanel().setBodyValueAt(null, i, "ccostobject");
  					continue;
  				}
  				SaleOrderVO[] orders = queryService.querySaleOrder("select csaleorderid from so_saleorder where vbillcode='"+vbillcode.toString()+"'");
  				if(orders != null && orders.length >0){
  					SaleOrderBVO[] bodyList = orders[0].getChildrenVO();
  	  				//�ҵ�һ������Ʒ����
  	  				for(SaleOrderBVO ele : bodyList){
  	  					if(!ele.getBlargessflag().booleanValue()){
  	  						//item.setCcostobject(ele.getCmaterialvid());
  	  						//System.out.println(this.getEditorModel().getCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ccostobject"));
  	  						this.getEditorModel().getCardPanelWrapper().getBillCardPanel().setBodyValueAt(ele.getCmaterialvid(), i, "ccostobject");
  	  						break;
  	  					}
  	  				}
  				}
  				//System.out.println(orders[0].getChildrenVO()[0].getCmaterialvid() + " " +orders[0].getChildrenVO()[0].getBlargessflag() );
  			} catch (BusinessException e) {
  				// TODO �Զ����ɵ� catch ��
  				e.printStackTrace();
  			}
      	}
      }
	}

}
