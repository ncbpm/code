package nc.ui.invp.balance.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.funcnode.ui.FuncletInitData;
import nc.ui.ic.onhand.OnhandDialog;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.BillListView;
import nc.ui.scmpub.action.SCMActionInitializer;
import nc.ui.uif2.NCAction;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.invp.result.entity.BalanceResultVO;
import nc.vo.pu.onhand.entity.OnhandDlgPUHeaderVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.view.IDataView;
import nc.vo.pubapp.pattern.model.meta.entity.view.DataViewMeta;
import nc.vo.scmpub.res.SCMActionCode;
 
/**
 * 存量查拣action
 * 
 * @author wangceb
 */
public class QueryOnhandAction extends NCAction {

	private static final String path = "nc/ui/pu/pub/action/queryonhand.xml";

	private static final long serialVersionUID = -7228894679966512759L;

	private OnhandDialog dlg;

	private BillListView list;

	private BillManageModel model;

	public QueryOnhandAction() {
		SCMActionInitializer.initializeAction(this,
				SCMActionCode.IC_ONHANDDISPHIDE);
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {

		this.dlg = new OnhandDialog(this.getModel().getContext()
				.getEntranceUI(), true);

		// 获得表体维度，以便从数据中取值
		Map<String, String> bodyDims = this.getBodyDims();

		List<IDataView> headVOs = new ArrayList<IDataView>();
		int rowcount = this.list.getBillListPanel().getChildListPanel()
				.getTable().getRowCount();
		BillModel bm = this.list.getBillListPanel().getBodyBillModel();
		if (rowcount != -1 && bm != null) {
			this.setDialogData(headVOs, bodyDims, rowcount, bm);
		}
		FuncletInitData initData = new FuncletInitData();

		initData.setInitData(headVOs.toArray(new IDataView[headVOs.size()]));

		this.dlg.initUI(this.getModel().getContext(), QueryOnhandAction.path,
				initData, false);
		this.dlg.showModal();
	}

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

	public BillListView getList() {
		return this.list;
	}

	public BillManageModel getModel() {
		return this.model;
	}

	public void setList(BillListView list) {
		this.list = list;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
	}

	/**
	 * 设置数据
	 * 
	 * @param headVOs
	 * @param bodyDims
	 * @param dimvo
	 */
	private void setDialogData(List<IDataView> headVOs,
			Map<String, String> bodyDims, int rowcount, BillModel bm) {
		for (int i = 0; i < rowcount; i++) {
			BillItem item = bm.getItemByKey(BalanceResultVO.CMATERIALOID);
			if (item == null) {
				continue;
			}
			if (NCBaseTypeUtils.isNull(bm.getValueAt(i,
					BalanceResultVO.CMATERIALOID))) {
				continue;
			}
			OnhandDimVO dimvo = new OnhandDimVO();
			for (Map.Entry<String, String> entry : bodyDims.entrySet()) {
				String value = entry.getValue();
				BillItem bodyitem = bm.getItemByKey(value);
				if (bodyitem == null) {
					continue;
				}
				Object dimValue = bm.getValueObjectAt(i, value);// 默认现存量维度字段同表体字段相同
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
			Object dimValue = bm.getValueObjectAt(i, BalanceResultVO.CUNITID);
			if (dimValue instanceof DefaultConstEnum) {
				dimValue = ((DefaultConstEnum) dimValue).getValue();
			}
			headVO.setCunitid(dimValue.toString());
			headVO.setCrowno((String) bm.getValueAt(i,
					BalanceResultVO.VREQROWNO));
			headVO.setOnhandshouldnum((UFDouble) bm.getValueAt(i,
					BalanceResultVO.NNUM));
			// headVO.setOnhandshouldassnum((UFDouble) bm.getValueAt(i,
			// BalanceResultVO.NASTNUM));
			headVOs.add(headVO);
		}

	}

}
