package nc.ui.mmpac.huanbao.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.bs.uif2.IActionCode;
import nc.itf.uap.IUAPQueryBS;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.progress.IProgressMonitor;
import nc.ui.pub.beans.progress.NCProgresses;
import nc.ui.pubapp.uif2app.query2.model.IModelDataManager;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.NCAsynAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.ToftPanelAdaptor;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.ActionInitializer;
import nc.ui.uif2.components.ITabbedPaneAwareComponent;
import nc.ui.uif2.components.progress.ProgressActionInterface;
import nc.ui.uif2.components.progress.TPAProgressUtil;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.ui.uif2.model.AppEventConst;
import nc.ui.uif2.model.BatchBillTableModel;
import nc.ui.uif2.model.BillManageModel;
import nc.uif2.annoations.MethodType;
import nc.uif2.annoations.ModelMethod;
import nc.uif2.annoations.ModelType;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
 * 
 * 同时卡片和列表刷新，并且区别处理
 * 
 * 配合 IModelDataManager, * AbstractUIAppModel ITabbedPaneAwareComponent
 * cardEditor
 * 
 * @author liyf
 * 
 */
public class ListAndCardRefreshAction extends NCAsynAction implements
		ProgressActionInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5881003422369583037L;

	private IModelDataManager dataManager = null;

	private AbstractUIAppModel model = null;
	private ITabbedPaneAwareComponent cardEditor;

	private TPAProgressUtil tpaProgressUtil;

	private boolean isTPAMonitor = true;

	private IProgressMonitor monitor = null;

	// 是否需要添加进度显示或蒙版，有些动作自己做了异步处理，可以将此开发关闭。
	private boolean isNeedProgress = true;
	private IUAPQueryBS uapQry;
	private IMDPersistenceQueryService mdQry;

	public ListAndCardRefreshAction() {
		super();
		ActionInitializer.initializeAction(this, IActionCode.REFRESH);
	}

	@Override
	public boolean beforeStartDoAction(ActionEvent actionEvent)
			throws Exception {
		boolean ret = true;
		if (!isNeedProgress())
			return ret;
		// 操作太频繁直接返回
		if (monitor != null && !monitor.isDone()) {
			return false;
		}
		// 设置等待界面
		if (isTPAMonitor()) {
			monitor = getTpaProgressUtil().getTPAProgressMonitor();
		} else {
			monitor = NCProgresses.createDialogProgressMonitor(model
					.getContext().getEntranceUI());
		}
		monitor.beginTask(
				NCLangRes.getInstance()
						.getStrByID("uif2", "RefreshAction-0000")/* 刷新中，请稍候... */,
				-1);
		monitor.setProcessInfo(NCLangRes.getInstance().getStrByID("uif2",
				"RefreshAction-0000")/* 刷新中，请稍候... */);
		return ret;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		if (!getCardEditor().isComponentVisible()) // 列表界面在显示
			this.getDataManager().refresh();
		else {
			// 卡片界面显示时，只刷新卡片数据;
			BillManageModel manageModel = (BillManageModel) getModel();
			int selectedRow = manageModel.getSelectedRow();
			if (selectedRow < 0)
				return;
			Object selectedObj = manageModel.getSelectedData();
			if (selectedObj instanceof SuperVO) {
				SuperVO oldBillVO = (SuperVO) selectedObj;
				Object newBillVO = getUapQry().retrieveByPK(
						oldBillVO.getClass(), oldBillVO.getPrimaryKey());
				if (newBillVO != null) {
					copyNewValueToOldObject((SuperVO) newBillVO, oldBillVO);
					manageModel.setSelectedRow(selectedRow);
				}
			}
			if (selectedObj instanceof AggregatedValueObject) {
				AggregatedValueObject aggVO = (AggregatedValueObject) selectedObj;
				SuperVO oldBillVO = (SuperVO) aggVO.getParentVO();
				NCObject newBillNCVO = getMdQry().queryBillOfNCObjectByPK(
						oldBillVO.getClass(), oldBillVO.getPrimaryKey());
				if (newBillNCVO != null) {
					AggregatedValueObject newBillAggVO = (AggregatedValueObject) newBillNCVO
							.getContainmentObject();
					aggVO.setParentVO(newBillAggVO.getParentVO());
					manageModel.setSelectedRow(selectedRow);
				}
			}
		}

	}

	@Override
	public boolean doAfterFailure(ActionEvent actionEvent, Throwable ex) {
		if (monitor != null) {
			monitor.done();
			monitor = null;
		}
		return true;
	}

	@Override
	public void doAfterSuccess(ActionEvent actionEvent) {
		if (monitor != null) {
			monitor.done();
			monitor = null;
		}
		this.showQueryInfo();
	}

	protected void showQueryInfo() {
		if (!getCardEditor().isComponentVisible()) // 列表界面在显示
		{
			int size = 0;
			if (this.getModel() instanceof BillManageModel) {
				size = ((BillManageModel) this.getModel()).getData().size();

			} else if (this.getModel() instanceof BatchBillTableModel) {
				size = ((BatchBillTableModel) this.getModel()).getRows().size();
			}
			if (size >= 0) {
				ShowStatusBarMsgUtil.showStatusBarMsg(
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"pubapp_0", "0pubapp-0266", null,
								new String[] { "" + size })/*
															 * @res
															 * "刷新成功，共刷新{0}张单据。"
															 */, this
								.getModel().getContext());
			}

		}else{
			ShowStatusBarMsgUtil.showStatusBarMsg(
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"pubapp_0", "0pubapp-0266", null,
							new String[] { "" + 1 })/*
														 * @res
														 * "刷新成功，共刷新{0}张单据。"
														 */, this
							.getModel().getContext());
		}
	}

	@Override
	protected boolean isActionEnable() {
		return getModel().getUiState() == UIState.INIT
				|| getModel().getUiState() == UIState.NOT_EDIT;
	}

	@ModelMethod(modelType = ModelType.AbstractUIAppModel, methodType = MethodType.GETTER)
	public AbstractUIAppModel getModel() {
		return model;
	}

	@ModelMethod(modelType = ModelType.AbstractUIAppModel, methodType = MethodType.SETTER)
	public void setModel(AbstractUIAppModel model) {
		this.model = model;
		this.model.addAppEventListener(this);
	}

	public boolean isTPAMonitor() {
		return isTPAMonitor
				&& getTpaProgressUtil().getContext().getEntranceUI() instanceof ToftPanelAdaptor;
	}

	public void setTPAMonitor(boolean isTPAMonitor) {
		this.isTPAMonitor = isTPAMonitor;
	}

	public TPAProgressUtil getTpaProgressUtil() {
		if (this.tpaProgressUtil == null) {
			tpaProgressUtil = new TPAProgressUtil();
			tpaProgressUtil.setContext(getModel().getContext());
		}
		return tpaProgressUtil;
	}

	public void setTpaProgressUtil(TPAProgressUtil tpaProgressUtil) {
		this.tpaProgressUtil = tpaProgressUtil;
	}

	public boolean isNeedProgress() {
		return isNeedProgress;
	}

	public void setNeedProgress(boolean isNeedProgress) {
		this.isNeedProgress = isNeedProgress;
	}

	private void copyNewValueToOldObject(SuperVO newBillVO, SuperVO oldBillVO) {
		String[] attributeNames = oldBillVO.getAttributeNames();
		for (String attribute : attributeNames) {
			oldBillVO.setAttributeValue(attribute,
					newBillVO.getAttributeValue(attribute));
		}
	}

	public IModelDataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(IModelDataManager dataManager) {
		this.dataManager = dataManager;
	}

	public void setCardEditor(ITabbedPaneAwareComponent cardEditor) {
		this.cardEditor = cardEditor;
	}

	public ITabbedPaneAwareComponent getCardEditor() {
		return cardEditor;
	}

	public IUAPQueryBS getUapQry() {
		if (uapQry == null) {
			uapQry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return uapQry;
	}

	public IMDPersistenceQueryService getMdQry() {
		if (mdQry == null) {
			mdQry = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQry;
	}
}
