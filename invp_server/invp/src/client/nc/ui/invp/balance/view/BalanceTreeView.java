package nc.ui.invp.balance.view;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;

import nc.ui.ic.onhand.OnhandValueSet;
import nc.ui.invp.balance.deal.BalanceBillScaleProcessor;
import nc.ui.invp.balance.deal.BalanceScalePrcStrategy;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pubapp.uif2app.event.list.ListEventTransformer;
import nc.ui.pubapp.uif2app.view.util.BillPanelUtils;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.uif2.UIState;
import nc.ui.uif2.components.AutoShowUpEventSource;
import nc.ui.uif2.components.IAutoShowUpComponent;
import nc.ui.uif2.components.IAutoShowUpEventListener;
import nc.ui.uif2.editor.BillTreeView;
import nc.vo.invp.pub.enm.ReqTypeEnum;
import nc.vo.invp.result.entity.AggBalanceResultVO;
import nc.vo.invp.result.entity.BalanceResultVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.bill.IMetaDataProperty;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

@SuppressWarnings("rawtypes")
public class BalanceTreeView extends BillTreeView implements
		IAutoShowUpComponent, BillEditListener2 {
	/**
   * 
   */
	private static final long serialVersionUID = -389270047621058308L;

	private AutoShowUpEventSource autoShowUpComponent = new AutoShowUpEventSource(
			this);

	private Map<String, List<String>> groupColumnsMap = new HashMap<String, List<String>>();

	private String[][] itemKeys = new String[][] {
			{ BalanceResultVO.NPURCHASENUM, BalanceResultVO.NPRAYNUM,
					BalanceResultVO.NTRANSINNUM },
			{ BalanceResultVO.NSAFESTOCKNUM, BalanceResultVO.NSTOCKPREPARENUM,
					BalanceResultVO.NREQNUM, BalanceResultVO.NWORKNUM,
					BalanceResultVO.NTRANSOUTNUM } };

	private BalanceTreeModel model;

	// add by liangchen1 编辑后处理
	@Override
	public void afterEdit(BillEditEvent e) {
		if (!e.getKey().equals(BalanceResultVO.NSUPPLYNUM)
				&& !e.getKey().equals(BalanceResultVO.PK_ORG_NEXT_V)
				&& !e.getKey().equals(BalanceResultVO.NSUGPUNUM)
				&& !e.getKey().equals(BalanceResultVO.PK_ORG_STOCKIN_V)
				&& !e.getKey().equals(BalanceResultVO.BISTRANSFLAG)) {
			return;
		}
		BalanceResultVO vo = (BalanceResultVO) this.getModel()
				.getSelectedDatas()[0];
		if (e.getKey().equals(BalanceResultVO.PK_ORG_NEXT_V)) {
			if (vo.getPk_org_next_v() != null
					&& vo.getPk_org_next_v().equals(e.getValue())) {
				return;
			}
			// 设置下次库存组织的值
			BalanceResultVO uivo = (BalanceResultVO) this
					.getBillListPanel()
					.getHeadBillModel()
					.getBodyValueRowVO(e.getRow(),
							BalanceResultVO.class.getName());
			vo.setPk_org_next(uivo.getPk_org_next());
			vo.setPk_org_next_v(uivo.getPk_org_next_v());
			Map<String, Object[]> map = this.backupBillItem(this
					.getBillListPanel().getHeadBillModel());
			try {
				this.getModel().directlyUpdate(vo);
			} finally {
				this.restoreBillItem(
						this.getBillListPanel().getHeadBillModel(), map);
			}
			return;
		}
		if (e.getKey().equals(BalanceResultVO.PK_ORG_STOCKIN_V)) {
			if (vo.getPk_org_stockin_v() != null
					&& vo.getPk_org_stockin_v().equals(e.getValue())) {
				return;
			}
			// 设置收货库存组织的值
			BalanceResultVO uivo = (BalanceResultVO) this
					.getBillListPanel()
					.getHeadBillModel()
					.getBodyValueRowVO(e.getRow(),
							BalanceResultVO.class.getName());
			vo.setPk_org_stockin(uivo.getPk_org_stockin());
			vo.setPk_org_stockin_v(uivo.getPk_org_stockin_v());
			Map<String, Object[]> map = this.backupBillItem(this
					.getBillListPanel().getHeadBillModel());
			try {
				this.getModel().directlyUpdate(vo);
			} finally {
				this.restoreBillItem(
						this.getBillListPanel().getHeadBillModel(), map);
			}
			return;
		}
		// 设置自动调拨的值
		if (e.getKey().equals(BalanceResultVO.BISTRANSFLAG)) {
			if (e.getValue().equals(Boolean.TRUE)) {
				vo.setBistransflag(UFBoolean.TRUE);
			} else {
				vo.setBistransflag(UFBoolean.FALSE);
			}
			Map<String, Object[]> map = this.backupBillItem(this
					.getBillListPanel().getHeadBillModel());
			try {
				this.getModel().directlyUpdate(vo);
			} finally {
				this.restoreBillItem(
						this.getBillListPanel().getHeadBillModel(), map);
			}
			return;
		}
		if (e.getKey().equals(BalanceResultVO.NSUGPUNUM)) {

			if (MathTool.lessThan((UFDouble) e.getValue(), UFDouble.ZERO_DBL)) {
				vo.setNsugpunum(UFDouble.ZERO_DBL);
			} else {
				vo.setNsugpunum((UFDouble) e.getValue());
			}
			Map<String, Object[]> map = this.backupBillItem(this
					.getBillListPanel().getHeadBillModel());
			try {
				this.getModel().directlyUpdate(new BalanceResultVO[] { vo });
			} finally {
				this.restoreBillItem(
						this.getBillListPanel().getHeadBillModel(), map);
			}
			return;
		}

		UFDouble editValue = (UFDouble) e.getValue();
		// 校验库存满足量
		if (BalanceResultVO.NSUPPLYNUM.equals(e.getKey())) {
			UFDouble canuseNum = UFDouble.ZERO_DBL; // 剩余库存可用量
			UFDouble sumNum = UFDouble.ZERO_DBL;// 剩余库存可用量+原库存满足量=目前库存可用量

			UFDouble oriValue = vo.getNsupplynum();

			if (MathTool.lessThan(editValue, UFDouble.ZERO_DBL)) {
				vo.setNsupplynum(UFDouble.ZERO_DBL);
				editValue = UFDouble.ZERO_DBL;
			}
			String key = vo.getPk_org() + vo.getCmaterialoid();
			if (null != this.getModel().getCanuseNumMap().get(key)) {
				canuseNum = this.getModel().getCanuseNumMap().get(key);
				sumNum = MathTool.add(canuseNum, oriValue);
			} else {
				canuseNum = this.calAtpNum(vo);
				sumNum = canuseNum;

			}
			if (MathTool.greaterThan(editValue, sumNum)) {
				vo.setNsupplynum((UFDouble) e.getOldValue());
				Map<String, Object[]> map = this.backupBillItem(this
						.getBillListPanel().getHeadBillModel());
				try {
					this.model.directlyUpdate(vo);
				} finally {
					this.restoreBillItem(this.getBillListPanel()
							.getHeadBillModel(), map);
				}
				UFDouble errorNum = MathTool
						.lessThan(sumNum, UFDouble.ZERO_DBL) ? UFDouble.ZERO_DBL
						: sumNum;
				ExceptionUtils
						.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("4007005_0",
										"04007005-0006")/*
														 * @res
														 * "库存满足量不能超过可用现存量->"
														 */
								+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
										.getStrByID("4007005_0",
												"04007005-0007")/*
																 * @res "可用现存量："
																 */
								+ errorNum);
			} else {
				UFDouble totalNum = UFDouble.ZERO_DBL;
				DefaultMutableTreeNode parentNode;
				if ("root".equals(vo.getPk_parentid())) {
					parentNode = this.findNodeByBusinessObjectId(vo
							.getPk_balance_result());
					totalNum = MathTool.add(totalNum, editValue);
				} else {
					parentNode = this.findNodeByBusinessObjectId(vo
							.getPk_parentid());
					totalNum = MathTool.add(totalNum,
							((BalanceResultVO) parentNode.getUserObject())
									.getNsupplynum());
				}
				Enumeration a = parentNode.children();
				while (a.hasMoreElements()) {
					BalanceResultVO child = (BalanceResultVO) ((VOTreeNode) a
							.nextElement()).getUserObject();
					if (vo.getPk_balance_result().equals(
							child.getPk_balance_result())) {
						totalNum = MathTool.add(totalNum, editValue);
					} else {
						totalNum = MathTool
								.add(totalNum, child.getNsupplynum());
					}

				}
				UFDouble subNum = MathTool.sub(vo.getNnum(), totalNum);
				if (MathTool.lessThan(subNum, UFDouble.ZERO_DBL)) {
					vo.setNsupplynum((UFDouble) e.getOldValue());
					Map<String, Object[]> map = this.backupBillItem(this
							.getBillListPanel().getHeadBillModel());
					try {
						this.model.directlyUpdate(vo);
					} finally {
						this.restoreBillItem(this.getBillListPanel()
								.getHeadBillModel(), map);
					}
					ExceptionUtils
							.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
									.getNCLangRes().getStrByID("4007005_0",
											"04007005-0008")/*
															 * @res
															 * "总库存满足量不能超过需求数量"
															 */
									+ nc.vo.ml.NCLangRes4VoTransl
											.getNCLangRes().getStrByID(
													"4007005_0",
													"04007005-0009")/*
																	 * @res
																	 * "总库存满足量："
																	 */
									+ totalNum);
				} else {
					BalanceResultVO parentVO = (BalanceResultVO) parentNode
							.getUserObject();
					parentVO.setNlacknum(subNum);
					parentVO.setNsugpunum(subNum);
					if ("root".equals(vo.getPk_parentid())) {
						parentVO.setNsupplynum(editValue);
						Map<String, Object[]> map = this.backupBillItem(this
								.getBillListPanel().getHeadBillModel());
						try {
							this.model.directlyUpdate(parentVO);
						} finally {
							this.restoreBillItem(this.getBillListPanel()
									.getHeadBillModel(), map);
						}
					} else {
						vo.setNsupplynum(editValue);
						Map<String, Object[]> map = this.backupBillItem(this
								.getBillListPanel().getHeadBillModel());
						try {
							this.model.directlyUpdate(new BalanceResultVO[] {
									parentVO, vo });
						} finally {
							this.restoreBillItem(this.getBillListPanel()
									.getHeadBillModel(), map);
						}
					}
				}
				UFDouble gapNum = MathTool.sub(oriValue, editValue);
				canuseNum = MathTool.add(canuseNum, gapNum);
				this.getModel().getCanuseNumMap().put(key, canuseNum);
			}
		}
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		if (e.getKey().equals(BalanceResultVO.NSUGPUNUM)) {
			BalanceResultVO vo = (BalanceResultVO) this.getModel()
					.getSelectedDatas()[0];
			if (!"root".equals(vo.getPk_parentid())) {
				return false;
			}
			return true;
		}
		if (e.getKey().equals(BalanceResultVO.PK_ORG_NEXT_V)
				|| e.getKey().equals(BalanceResultVO.PK_ORG_NEXT)) {
			BalanceResultVO vo = (BalanceResultVO) this.getModel()
					.getSelectedDatas()[0];
			if (!ReqTypeEnum.AFTERBALREQ.integerValue()
					.equals(vo.getFreqtype())
					|| !"root".equals(vo.getPk_parentid())) {
				return false;
			}
			return true;
		}
		if (e.getKey().equals(BalanceResultVO.NSUPPLYNUM)) {
			BalanceResultVO vo = (BalanceResultVO) this.getModel()
					.getSelectedDatas()[0];
			if (ReqTypeEnum.AFTERBALREQ.integerValue().equals(vo.getFreqtype())
					&& vo.getPk_org_req().equals(vo.getPk_org())) {
				return false;
			}
			return true;
		}
		if (e.getKey().equals(BalanceResultVO.BISTRANSFLAG)) {
			return true;
		}
		if (e.getKey().equals(BalanceResultVO.PK_ORG_STOCKIN_V)) {
			BalanceResultVO vo = (BalanceResultVO) this.getModel()
					.getSelectedDatas()[0];
			UIRefPane ref = (UIRefPane) this.getBillListPanel()
					.getHeadBillModel()
					.getItemByKey(BalanceResultVO.PK_ORG_STOCKIN_V)
					.getComponent();
			if (ref != null && ref.getRefModel() != null) {
				String pk_org_originalreq_v = vo.getPk_org_originalreq_v();
				String pk_org_req_v = vo.getPk_org_req_v();
				List<String> orgs = new ArrayList<String>();
				if (pk_org_originalreq_v != null
						&& pk_org_originalreq_v.length() != 0) {
					orgs.add(pk_org_originalreq_v);
				}
				if (pk_org_req_v != null && pk_org_req_v.length() != 0) {
					orgs.add(pk_org_req_v);
				}
				SqlBuilder orgwhere = new SqlBuilder();
				if (orgs.size() > 0) {
					orgwhere.append(OrgVO.PK_VID, orgs.toArray(new String[0]));
				} else {
					orgwhere.append("0=1");
				}
				ref.getRefModel().setWherePart(orgwhere.toString());
				return true;
			}
		}
		return false;
	}

	public Map<String, List<String>> getGroupColumnsMap() {
		return this.groupColumnsMap;
	}

	@Override
	public BalanceTreeModel getModel() {
		return this.model;
	}

	/**
	 * 父类方法重写
	 * 
	 * @see nc.ui.uif2.editor.BillTreeView#initUI()
	 */
	@Override
	public void initUI() {
		super.initUI();
		// 处理精度
		BalanceBillScaleProcessor prc = new BalanceBillScaleProcessor();
		prc.setStrategy(new BalanceScalePrcStrategy());
		String pk_group = AppContext.getInstance().getPkGroup();
		prc.setListPanleScale(this.getBillListPanel(), pk_group);
		ListEventTransformer eventTransformer = new ListEventTransformer(
				this.getBillListPanel(), this.getModel());
		eventTransformer.setContext(this.getModel().getContext());
		eventTransformer.setOriginEditListener(this);
		eventTransformer.setBillEditListener2(this);
		
		 this.getBillListPanel().getUISplitPane().addComponentListener(
					new java.awt.event.ComponentAdapter() {

						public void componentResized(
								java.awt.event.ComponentEvent e) {
							super.componentResized(e);
							getBillListPanel().getUISplitPane().setDividerLocation(0.85);
						}
					});

	}

	@SuppressWarnings("unused")
	public void resetUI(AggBalanceResultVO fcbill) {

		// BillListPanel bcp = this.getBillListPanel();

		// 清除已有的界面
		// this.remove(bcp);
		for (int i = 0; i < this.itemKeys.length; i++) {
			List<String> groupColumns = new ArrayList<String>();
			for (int j = 0; j < this.itemKeys[i].length; j++) {
				groupColumns.add(this.itemKeys[i][j]);
			}
			if (0 == i) {
				this.getGroupColumnsMap().put(
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"4007005_0", "04007005-0028")/* @res "预计入" */,
						groupColumns);
			} else {
				this.getGroupColumnsMap().put(
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"4007005_0", "04007005-0029")/* @res "预计出" */,
						groupColumns);
			}
		}

		// 初始化界面
		// this.initUI();

	}

	@Override
	public void setAutoShowUpEventListener(IAutoShowUpEventListener l) {
		this.autoShowUpComponent.setAutoShowUpEventListener(l);

	}

	public void setModel(BalanceTreeModel model) {
		this.model = model;
		// model.addAppEventListener(this);
		super.setModel(model);
	}

	/**
	 * 设置多层表头 （预计入、预计出）
	 * 
	 * @author mafeic
	 * @time 2014-4-23 上午11:13:47
	 */
	public void setMultiHeadTable() {
		BillListPanel listPanel = this.getBillListPanel();

		List<ColumnGroup> cgList = new ArrayList<ColumnGroup>();
		for (Entry<String, List<String>> entry : this.groupColumnsMap
				.entrySet()) {
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
	public void showMeUp() {
		// 在转单后数据处理界面，返回按钮在编辑态也是可见的，所以在返回切会列表界面的时候
		// 要把UI状态修改成非编辑态，否则界面的按钮显示会出现问题。
		if (this.getModel() != null
				&& this.getModel().getUiState() != UIState.NOT_EDIT) {
			this.getModel().setUiState(UIState.NOT_EDIT);
		}
		this.autoShowUpComponent.showMeUp();

	}

	private Map<String, Object[]> backupBillItem(BillModel bm) {
		BillItem[] items = bm.getBodyItems();
		Map<String, Object[]> map = new HashMap<String, Object[]>();
		for (BillItem item : items) {
			if (item.getDataType() == IBillItem.UFREF
					&& item.getKey().equals(BalanceResultVO.CUNITID)) {
				Object[] row = new Object[2];
				row[0] = Integer.valueOf(item.getDataType());
				row[1] = item.getMetaDataProperty();
				map.put(item.getKey(), row);
				item.setDataType(IBillItem.STRING);
				item.setMetaDataProperty(null);
			}
		}
		return map;
	}

	private UFDouble calAtpNum(BalanceResultVO vo) {
		UFDouble canuseNum = MathTool.add(vo.getNstocknum(), vo.getNpraynum());
		canuseNum = MathTool.add(canuseNum, vo.getNpurchasenum());
		canuseNum = MathTool.add(canuseNum, vo.getNtransinnum());
		canuseNum = MathTool.sub(canuseNum, vo.getNreqnum());
		// TODO 工单内容，65需放开
		// canuseNum = MathTool.sub(canuseNum, vo.getNworknum());工单
		canuseNum = MathTool.sub(canuseNum, vo.getNstockpreparenum());
		canuseNum = MathTool.sub(canuseNum, vo.getNsafestocknum());
		canuseNum = MathTool.sub(canuseNum, vo.getNtransoutnum());
		return canuseNum;

	}

	private DefaultMutableTreeNode findNodeByBusinessObjectId(Object target_id) {

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getModel()
				.getTree().getRoot();
		Enumeration e = root.preorderEnumeration();
		e.nextElement();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
					.nextElement();
			if (((BalanceResultVO) node.getUserObject()).getPk_balance_result()
					.equals(target_id)) {
				return node;
			}
		}
		return null;
	}

	private void restoreBillItem(BillModel bm, Map<String, Object[]> backmap) {
		BillItem item = null;
		for (Entry<String, Object[]> en : backmap.entrySet()) {
			item = bm.getItemByKey(en.getKey());
			item.setDataType(((Integer) en.getValue()[0]).intValue());
			item.setMetaDataProperty((IMetaDataProperty) en.getValue()[1]);
		}
	}

	// /**
	// * 设置下次平衡库存组织的值
	// */
	// private void setNextOrgValue(String pk_org_next) {
	// BalanceResultVO vo =
	// (BalanceResultVO) this.getModel().getSelectedDatas()[0];
	// DefaultMutableTreeNode parentNode = null;
	// if ("root".equals(vo.getPk_parentid())) {
	// parentNode = this.findNodeByBusinessObjectId(vo.getPk_balance_result());
	// }
	// else {
	// parentNode = this.findNodeByBusinessObjectId(vo.getPk_parentid());
	// }
	// BalanceResultVO parentVO = (BalanceResultVO) parentNode.getUserObject();
	// vo.setPk_org_next_v(pk_org_next);
	// this.getModel().directlyUpdate(new BalanceResultVO[] {
	// parentVO, vo
	// });
	// }

	@Override
	public void bodyRowChange(BillEditEvent e) {
		super.bodyRowChange(e);

		OnhandValueSet set = new OnhandValueSet();
		set.setOnHandVO(this.getBillListPanel(), getModel().getContext());
	}

}
