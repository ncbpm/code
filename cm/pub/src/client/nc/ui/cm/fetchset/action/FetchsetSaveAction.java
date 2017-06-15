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
 * ��ϵͳȡ�����ñ��水ť
 */
public class FetchsetSaveAction extends DifferentVOSaveAction {
    private static final long serialVersionUID = 1L;

    private ManageAppModelObservable manageAppModelObservable;

    private FetchsetAppModel dataManager;

    @Override
    public void doAction(ActionEvent e) throws Exception {
        // 1��get the tabbedPane
        BillForm form = (BillForm) this.getEditor();
        TangramLayoutTabbedPane tablePane = (TangramLayoutTabbedPane) form.getParent();

        // 2��get the selected BillForm
        BillForm currentSeletedForm = (BillForm) tablePane.getSelectedComponent();

        // 3��reset the editor and model
        this.setEditor(currentSeletedForm);
        this.setModel(currentSeletedForm.getModel());

        // 4 ҳ��ֹͣ�༭
        currentSeletedForm.getBillCardPanel().stopEditing();

        // 5 �ж��Ƿ�ѡ��������֯
        if (VOChecker.isEmpty(this.getDataManager().getModel().getContext().getPk_org())) {
            MessageDialog.showWarningDlg(((FetchsetBillForm) this.getEditor()).getBillCardPanel(), null,
                    FetchsetMultiLangConst.getCHOOSE_ORG());
            return;
        }

        // 6 ִ����֤��ʽ
        currentSeletedForm.getBillCardPanel().getBillData().execValidateFormulas();

        // 7 ���ݵ�ǰBillForm��nodekey����ȡ������
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
        //2017-06-15��������
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
        

        // 10 ����model����
        BillManageModel tmodel = (BillManageModel) this.getModel();
        AggFetchSetVO aggFetchSetVO = (AggFetchSetVO) value;
        this.saveData(tmodel, aggFetchSetVO);

        // 11 ����model��AppUiState
        this.getManageAppModelObservable().updateAppUiState(AppUiState.NOT_EDIT);

        // 12 ˢ�²���ʾ�ɹ���Ϣ
        this.getDataManager().initModel();
        this.showSuccessInfo();
    }

    /**
     * ����fetchtypeֵ
     */
    private void setFetchtypeValue(Object value, FetchTypeEnum fetchType) {
        ((AggFetchSetVO) value).getParentVO()
        .setAttributeValue(FetchSetHeadVO.IFETCHTYPE, fetchType.value().toString());
    }

    /**
     * insert����
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
     * ����model����
     */
    private void saveData(BillManageModel model, Object value) {
        // ����Ϊ��ʱ�޸�AppUiStateΪAdd
        if (((AggFetchSetVO) value).getPrimaryKey() == null) {
            model.setAppUiState(AppUiState.ADD);
        }

        if (model.getAppUiState() == AppUiState.ADD) {
            this.insertBill(model, value);
        }
        if (model.getAppUiState() == AppUiState.EDIT) {
            // ȡ����vo����
            IBill[] clientVOs = new IBill[] {
                    (IBill) value
            };
            ClientBillToServer<IBill> tool = new ClientBillToServer<IBill>();
            IBill[] oldVO = new IBill[] {
                    (IBill) model.getSelectedData()
            };

            // ȡ��������VO
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
     * ����IAppModelDataManager
     *
     * @param dataManager
     *            the dataManager to set
     */
    public void setDataManager(FetchsetAppModel dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * ��ȡIAppModelDataManager
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
