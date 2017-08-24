package nc.ui.ic.m4d.handler;

import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.so.m30.self.ISaleOrderMaintain;
import nc.ui.ic.pub.model.ICBizEditorModel;
import nc.vo.pub.BusinessException;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderVO;

public class CostObjectDealTool {

	public HashMap<String, SaleOrderBVO> cachMap = new HashMap<String, SaleOrderBVO>();

	private ICBizEditorModel editorModel;

	public CostObjectDealTool() {

	}
	public void setCostObjectValueAllRow() throws BusinessException{
		int rowCount = this.getEditorModel().getCardPanelWrapper().getBillModel().getRowCount();
		if(rowCount <0){
			return ;
		}
		for(int i=0;i<rowCount;i++){
			setCostObjctValue(i);
		}
	}
	
	/**
	 * ����ǲ��ϳ��ⵥ billType�� 4D ������Ҫ�������κţ�Ϊ���۶����ĵ��ݺţ��� �� ���۶�����������Ʒ�ĵ�һ�м�¼ ����Ϊ ��ǰ��
	 * ����Ʒ����ǲ��ϳ��ⵥ billType�� 4D ������Ҫ�������κţ�Ϊ���۶����ĵ��ݺţ��� �� ���۶�����������Ʒ�ĵ�һ�м�¼ ����Ϊ ��ǰ��
	 * ����Ʒ
	 * 
	 * @param event
	 * @throws BusinessException
	 */
	public void setCostObjctValue(int row) throws BusinessException {
		
		Object csourcebillbid  = this.getEditorModel().getCardPanelWrapper()
				.getBillCardPanel().getBodyValueAt(row, "csourcebillbid");
		if(csourcebillbid !=null){
			return;
		}
		// ���óɱ�����Ϊnull
		// �������9λ��11λ�������� ���ڱ���ɵ���9λ���µ���11λ
		Object vbillcode = this.getEditorModel().getCardPanelWrapper()
				.getBillCardPanel().getBodyValueAt(row, "vbatchcode");
		// �������κŲ�ѯ���۶���
		SaleOrderBVO ele = matchSaleOrderInv(vbillcode);
		if (ele == null) {
			// 10��������������
			this.getEditorModel().getCardPanelWrapper().getBillCardPanel()
					.setBodyValueAt(null, row, "ccostobject");
			this.getEditorModel().getCardPanelWrapper().getBillCardPanel()
					.setBodyValueAt(null, row, "vprodfree1");
			this.getEditorModel().getCardPanelWrapper().getBillCardPanel()
					.setBodyValueAt(null, row, "vprodfree3");
		} else {
			this.getEditorModel().getCardPanelWrapper().getBillCardPanel()
					.setBodyValueAt(ele.getCmaterialvid(), row, "ccostobject");

			this.getEditorModel()
					.getCardPanelWrapper()
					.getBillCardPanel()
					.setBodyValueAt(ele.getAttributeValue("vfree1"), row,
							"vprodfree1");
			this.getEditorModel()
					.getCardPanelWrapper()
					.getBillCardPanel()
					.setBodyValueAt(ele.getAttributeValue("vfree3"), row,
							"vprodfree3");
			getEditorModel().getCardPanelWrapper().loadEditRelationItemValue(row, "vbatchcode");


		}

	}

	/**
	 * ƥ�����۶��� �ɱ�����
	 * 
	 * @param vbillcode
	 * @return
	 * @throws BusinessException
	 */
	private SaleOrderBVO matchSaleOrderInv(Object vbillcode)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		String pkOrg = this.getEditorModel().getCardPanelWrapper()
				.getBillCardPanel().getHeadItem("pk_org").getValueObject()
				.toString();
		if (vbillcode == null
				|| "".equalsIgnoreCase(vbillcode.toString().trim())) {
			return null;
		}
		if (cachMap.containsKey(vbillcode)) {
			return cachMap.get(vbillcode);
		}

		ISaleOrderMaintain queryService = NCLocator.getInstance().lookup(
				ISaleOrderMaintain.class);
		SaleOrderVO[] orders = queryService
				.querySaleOrder("select csaleorderid from so_saleorder where "
						+ " nvl(dr,0)=0 and pk_org='" + pkOrg
						+ "' and  (vbillcode=substr('" + vbillcode.toString()
						+ "',1,9) or vbillcode=substr('" + vbillcode.toString()
						+ "',1,11))");
		if (orders == null || orders.length == 0) {
			return null;
		}

		SaleOrderBVO[] bodyList = orders[0].getChildrenVO();
		if (bodyList == null || bodyList.length == 0) {
			return null;
		}
		for (SaleOrderBVO ele : bodyList) {
			// �ҵ�һ������Ʒ����
			if (!ele.getBlargessflag().booleanValue()) {
				cachMap.put(vbillcode.toString(), ele);
				return ele;
			}
		}
		return null;
	}
	

	public ICBizEditorModel getEditorModel() {
		return this.editorModel;
	}
	public void setEditorModel(ICBizEditorModel editorModel) {
		this.editorModel = editorModel;
	}
	

}
