package nc.ui.cm.fetchset.action;

import java.awt.event.ActionEvent;

import nc.bd.framework.base.CMStringUtil;
import nc.ui.cm.fetchset.model.FetchsetAppModel;
import nc.ui.cm.fetchset.model.ManageAppModelObservable;
import nc.ui.cm.fetchset.view.FetchsetBillForm;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.DifferentVOSaveAction;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.uif2.tangramlayout.TangramLayoutTabbedPane;
import nc.vo.cm.fetchset.entity.AggFetchSetVO;
import nc.vo.cm.fetchset.entity.FetchSetHeadVO;
import nc.vo.cm.fetchset.enumeration.FetchTypeEnum;
import nc.vo.cm.fetchset.multilangconst.FetchsetMultiLangConst;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.transfer.bill.ClientBillToServer;
import nc.vo.trade.checkrule.VOChecker;

/**
 * 外系统取数设置保存按钮
 */
public class FetchsetSaveAction extends DifferentVOSaveAction {
    private static final long serialVersionUID = 1L;

    private ManageAppModelObservable manageAppModelObservable;

    private FetchsetAppModel dataManager;

    @Override
    public void doAction(ActionEvent e) throws Exception {
        // 1、get the tabbedPane
        BillForm form = (BillForm) this.getEditor();
        TangramLayoutTabbedPane tablePane = (TangramLayoutTabbedPane) form.getParent();

        // 2、get the selected BillForm
        BillForm currentSeletedForm = (BillForm) tablePane.getSelectedComponent();

        // 3、reset the editor and model
        this.setEditor(currentSeletedForm);
        this.setModel(currentSeletedForm.getModel());

        // 4 页面停止编辑
        currentSeletedForm.getBillCardPanel().stopEditing();

        // 5 判断是否选择了主组织
        if (VOChecker.isEmpty(this.getDataManager().getModel().getContext().getPk_org())) {
            MessageDialog.showWarningDlg(((FetchsetBillForm) this.getEditor()).getBillCardPanel(), null,
                    FetchsetMultiLangConst.getCHOOSE_ORG());
            return;
        }

        // 6 执行验证公式
        currentSeletedForm.getBillCardPanel().getBillData().execValidateFormulas();

        // 7 根据当前BillForm的nodekey设置取数类型
        Object value = currentSeletedForm.getValue();
        if (CMStringUtil.isEqual(currentSeletedForm.getNodekey(), FetchsetMultiLangConst.getCLCK_TAB_CODE())) {
            this.setFetchtypeValue(value, FetchTypeEnum.MATEROUT);
        }
        else if (CMStringUtil.isEqual(currentSeletedForm.getNodekey(), FetchsetMultiLangConst.getWGRK_TAB_CODE())) {
            this.setFetchtypeValue(value, FetchTypeEnum.OVERIN);
        }
        else if (CMStringUtil.isEqual(currentSeletedForm.getNodekey(), FetchsetMultiLangConst.getSPOIL_TAB_CODE())) {
            this.setFetchtypeValue(value, FetchTypeEnum.SPOIL);
        }
        else if (CMStringUtil.isEqual(currentSeletedForm.getNodekey(), FetchsetMultiLangConst.getZY_TAB_CODE())) {
            this.setFetchtypeValue(value, FetchTypeEnum.TASK);
        }
        else if (CMStringUtil.isEqual(currentSeletedForm.getNodekey(), FetchsetMultiLangConst.getGXWW_TAB_CODE())) {
            this.setFetchtypeValue(value, FetchTypeEnum.GXWW);
        }
        else if (CMStringUtil.isEqual(currentSeletedForm.getNodekey(), FetchsetMultiLangConst.getIASTUFF_TAB_CODE())) {
            this.setFetchtypeValue(value, FetchTypeEnum.IASTUFF);
        }
        //2017-06-15设置数据
        else if (CMStringUtil.isEqual(currentSeletedForm.getNodekey(), "dinge")) {
            this.setFetchtypeValue(value, FetchTypeEnum.DINGE);
        }
        else if (CMStringUtil.isEqual(currentSeletedForm.getNodekey(), "chuyun")) {
            this.setFetchtypeValue(value, FetchTypeEnum.CHUYUN);
        }  else if (CMStringUtil.isEqual(currentSeletedForm.getNodekey(), "jianyan")) {
            this.setFetchtypeValue(value, FetchTypeEnum.JIANYAN);
        }  else if (CMStringUtil.isEqual(currentSeletedForm.getNodekey(), "huanbao")) {
            this.setFetchtypeValue(value, FetchTypeEnum.HUANBAO);
        }
        

        // 10 更新model数据
        BillManageModel tmodel = (BillManageModel) this.getModel();
        AggFetchSetVO aggFetchSetVO = (AggFetchSetVO) value;
        this.saveData(tmodel, aggFetchSetVO);

        // 11 更新model的AppUiState
        this.getManageAppModelObservable().updateAppUiState(AppUiState.NOT_EDIT);

        // 12 刷新并显示成功信息
        this.getDataManager().initModel();
        this.showSuccessInfo();
    }

    /**
     * 设置fetchtype值
     */
    private void setFetchtypeValue(Object value, FetchTypeEnum fetchType) {
        ((AggFetchSetVO) value).getParentVO()
        .setAttributeValue(FetchSetHeadVO.IFETCHTYPE, fetchType.value().toString());
    }

    /**
     * insert方法
     */
    private void insertBill(BillManageModel model, Object value) {
        try {
            this.getService().insert(new AbstractBill[] {
                    (AggFetchSetVO) value
            });
        }
        catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
    }

    /**
     * 更新model数据
     */
    private void saveData(BillManageModel model, Object value) {
        // 主键为空时修改AppUiState为Add
        if (((AggFetchSetVO) value).getPrimaryKey() == null) {
            model.setAppUiState(AppUiState.ADD);
        }

        if (model.getAppUiState() == AppUiState.ADD) {
            this.insertBill(model, value);
        }
        if (model.getAppUiState() == AppUiState.EDIT) {
            // 取界面vo数据
            IBill[] clientVOs = new IBill[] {
                    (IBill) value
            };
            ClientBillToServer<IBill> tool = new ClientBillToServer<IBill>();
            IBill[] oldVO = new IBill[] {
                    (IBill) model.getSelectedData()
            };

            // 取得轻量级VO
            IBill[] lightVOs = tool.construct(oldVO, clientVOs);
            try {
                this.excuteUpdate((AggFetchSetVO) lightVOs[0]);
            }
            catch (Exception e) {
                ExceptionUtils.wrappException(e);
            }
        }
    }

    private void excuteUpdate(AbstractBill abstractBill) throws Exception {
        this.getService().update(new AbstractBill[] {
                abstractBill
        });
    }

    /**
     * 设置IAppModelDataManager
     *
     * @param dataManager
     *            the dataManager to set
     */
    public void setDataManager(FetchsetAppModel dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * 获取IAppModelDataManager
     *
     * @return the dataManager
     */
    public FetchsetAppModel getDataManager() {
        return this.dataManager;
    }

    public ManageAppModelObservable getManageAppModelObservable() {
        return this.manageAppModelObservable;
    }

    public void setManageAppModelObservable(ManageAppModelObservable manageAppModelObservable) {
        this.manageAppModelObservable = manageAppModelObservable;
    }
}
