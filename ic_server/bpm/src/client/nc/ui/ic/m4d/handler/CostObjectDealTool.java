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
	 * 如果是材料出库单 billType： 4D ，则需要根据批次号（为销售订单的单据号）， 查 销售订单，将非赠品的第一行记录 设置为 当前的
	 * 产成品如果是材料出库单 billType： 4D ，则需要根据批次号（为销售订单的单据号）， 查 销售订单，将非赠品的第一行记录 设置为 当前的
	 * 产成品
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
		// 设置成本对象为null
		// 按照左侧9位和11位检索即可 现在编码旧的是9位，新的是11位
		Object vbillcode = this.getEditorModel().getCardPanelWrapper()
				.getBillCardPanel().getBodyValueAt(row, "vbatchcode");
		// 更加批次号查询销售订单
		SaleOrderBVO ele = matchSaleOrderInv(vbillcode);
		if (ele == null) {
			// 10个物料自由属性
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
	 * 匹配销售订单 成本物料
	 * 
	 * @param vbillcode
	 * @return
	 * @throws BusinessException
	 */
	private SaleOrderBVO matchSaleOrderInv(Object vbillcode)
			throws BusinessException {
		// TODO 自动生成的方法存根
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
			// 找第一个非赠品物料
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
