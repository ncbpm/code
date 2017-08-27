package nc.ui.invp.balance.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.TableColumn;

import nc.ui.ic.onhand.OnhandValueSet;
import nc.ui.invp.balance.deal.BalanceBillScaleProcessor;
import nc.ui.invp.balance.deal.BalanceScalePrcStrategy;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pubapp.uif2app.view.ShowUpableBillListView;
import nc.ui.pubapp.uif2app.view.util.BillPanelUtils;
import nc.vo.invp.result.entity.AggBalanceResultVO;
import nc.vo.pubapp.AppContext;

/**
 * @version 6.5
 * @since 6.5
 * @author mafeic
 * @time 2014-4-2 上午10:55:50
 */
public class BalanceBillListView extends ShowUpableBillListView {

	private static final long serialVersionUID = 3510933756702692164L;

	@Override
	public void initUI() {
		super.initUI();
		// // 设置界面精度
		BalanceBillScaleProcessor prc = new BalanceBillScaleProcessor();
		prc.setStrategy(new BalanceScalePrcStrategy());
		String pk_group = AppContext.getInstance().getPkGroup();
		prc.setListPanleScale(this.getBillListPanel(), pk_group);

		 this.getBillListPanel().getUISplitPane().addComponentListener(
				new java.awt.event.ComponentAdapter() {

					public void componentResized(
							java.awt.event.ComponentEvent e) {
						super.componentResized(e);
						getBillListPanel().getUISplitPane().setDividerLocation(0.85);
					}
				});
	}

	public void resetUI(AggBalanceResultVO fcbill) {

		BillListPanel bcp = this.getBillListPanel();
		// 清除已有的界面
		this.remove(bcp);

		((BalanceTemplateContainer) this.getTemplateContainer())
				.setFcbill(fcbill);
		// 初始化界面
		this.initUI();

		BillListPanel listPanel = this.getBillListPanel();

		Map<String, List<String>> itemgroupMap = ((BalanceTemplateContainer) this
				.getTemplateContainer()).getGroupColumnsMap();

		List<ColumnGroup> cgList = new ArrayList<ColumnGroup>();
		for (Entry<String, List<String>> entry : itemgroupMap.entrySet()) {
			ColumnGroup cg = new ColumnGroup(entry.getKey());
			for (String itemkey : entry.getValue()) {
				TableColumn tableColumn = listPanel.getParentListPanel()
						.getShowCol(itemkey);
				cg.add(tableColumn);
			}
			cgList.add(cg);
		}
		BillPanelUtils.createMultiHeadTable(listPanel.getHeadTable(), cgList);

	}

	@Override
	public void bodyRowChange(BillEditEvent e) {
		super.bodyRowChange(e);

		OnhandValueSet set = new OnhandValueSet();
		set.setOnHandVO(this.getBillListPanel(), getModel().getContext());

	}
	
	

}
