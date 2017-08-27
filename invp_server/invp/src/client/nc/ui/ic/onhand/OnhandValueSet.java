package nc.ui.ic.onhand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.vo.ic.onhand.entity.OnhandDimAdapterFactory;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandVO;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.invp.result.entity.BalanceResultVO;
import nc.vo.pu.onhand.entity.OnhandDlgPUHeaderVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.view.IDataView;
import nc.vo.pubapp.pattern.model.meta.entity.view.DataViewMeta;
import nc.vo.uif2.LoginContext;

public class OnhandValueSet {

	/**
	 * 获得存量维度
	 * 
	 * @return
	 */
	private Map<String, String> getBodyDims() {
		Map<String, String> bodyDims = new HashMap<String, String>();

		bodyDims.put(OnhandDimVO.PK_GROUP, BalanceResultVO.PK_GROUP);
		bodyDims.put(OnhandDimVO.PK_ORG, BalanceResultVO.PK_ORG);
		bodyDims.put(OnhandDimVO.CMATERIALOID, BalanceResultVO.CMATERIALOID);
		bodyDims.put(OnhandDimVO.CMATERIALVID, BalanceResultVO.CMATERIALVID);
		bodyDims.put(OnhandDimVO.VBATCHCODE, BalanceResultVO.VBATCHCODE);
		bodyDims.put(BalanceResultVO.CUNITID, BalanceResultVO.CUNITID);
		// 物料自由辅助属性
		for (int i = 1; i < 11; i++) {
			bodyDims.put("vfree" + i, "vfree" + i);
		}

		bodyDims.put(OnhandDimVO.CVENDORID, BalanceResultVO.PK_SUPPLIER);
		bodyDims.put(OnhandDimVO.CWAREHOUSEID, BalanceResultVO.PK_REQSTORDOC);

		return bodyDims;
	}

	/**
	 * 设置数据
	 * 
	 * @param headVOs
	 * @param bodyDims
	 * @param dimvo
	 */
	private void setDialogData(List<IDataView> headVOs,
			Map<String, String> bodyDims, int row, BillModel bm) {
		BillItem item = bm.getItemByKey(BalanceResultVO.CMATERIALOID);
		if (item == null) {
			return;
		}
		if (NCBaseTypeUtils.isNull(bm.getValueAt(row,
				BalanceResultVO.CMATERIALOID))) {
			return;
		}
		OnhandDimVO dimvo = new OnhandDimVO();
		for (Map.Entry<String, String> entry : bodyDims.entrySet()) {
			String value = entry.getValue();
			BillItem bodyitem = bm.getItemByKey(value);
			if (bodyitem == null) {
				continue;
			}
			Object dimValue = bm.getValueObjectAt(row, value);// 默认现存量维度字段同表体字段相同
			if (dimValue instanceof DefaultConstEnum) {
				dimValue = ((DefaultConstEnum) dimValue).getValue();
			}
			dimvo.setAttributeValue(entry.getKey(), dimValue);
		}
		OnhandDlgPUHeaderVO headVO = new OnhandDlgPUHeaderVO();
		DataViewMeta dataViewMeta = new DataViewMeta(dimvo.getClass());
		headVO.setDataViewMeta(dataViewMeta);
		headVO.setVO(dimvo);
		// 主单位
		Object dimValue = bm.getValueObjectAt(row, BalanceResultVO.CUNITID);
		if (dimValue instanceof DefaultConstEnum) {
			dimValue = ((DefaultConstEnum) dimValue).getValue();
		}
		headVO.setCunitid(dimValue.toString());
		headVO.setCrowno((String) bm.getValueAt(row, BalanceResultVO.VREQROWNO));
		headVO.setOnhandshouldnum((UFDouble) bm.getValueAt(row,
				BalanceResultVO.NNUM));
		// headVO.setOnhandshouldassnum((UFDouble) bm.getValueAt(i,
		// BalanceResultVO.NASTNUM));
		headVOs.add(headVO);
	}

	public void setOnHandVO(BillListPanel listPanel, LoginContext context) {

		// 获得表体维度，以便从数据中取值
		Map<String, String> bodyDims = this.getBodyDims();

		List<IDataView> headVOs = new ArrayList<IDataView>();
		int row = listPanel.getHeadTable().getSelectedRow();
		BillModel bm = listPanel.getHeadBillModel();
		if (row != -1 && bm != null) {
			this.setDialogData(headVOs, bodyDims, row, bm);
			OnhandDimVO onhandDimVO = new OnhandDimAdapterFactory()
					.createOnhandDim(headVOs.get(0));
			OnhandDimVO cloendOnhandDimVO = (OnhandDimVO) onhandDimVO.clone();

			OnhandDataManager2 handManager = new OnhandDataManager2();
			handManager.setContext(context);
			OnhandVO[] hands = handManager.queryOnhand(cloendOnhandDimVO);
			listPanel.getBodyBillModel().setBodyDataVO(hands);
			listPanel.getBodyBillModel().execLoadFormula();
		}
	}

}
