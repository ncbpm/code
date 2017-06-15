package nc.ui.cm.fetchdata.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import nc.bd.framework.base.CMArrayUtil;
import nc.bd.framework.base.CMStringUtil;
import nc.bs.framework.common.NCLocator;
import nc.cmpub.business.adapter.BDAdapter;
import nc.pubitf.org.IOrgUnitPubService;
import nc.ui.bd.ref.model.AccperiodRefModel;
import nc.ui.cm.fetchdata.action.AllCancelAction;
import nc.ui.cm.fetchdata.action.CheckAciton;
import nc.ui.cm.fetchdata.action.FetchDataAction;
import nc.ui.cm.fetchdata.factory.FetchDataTypeFactory;
import nc.ui.cm.fetchdata.uidataset.IFetchDataType;
import nc.ui.org.ref.FactoryDefaultRefModel;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.RowStateChangeEvent;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.editor.BillListView;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.bd.ref.IFilterStrategy;
import nc.vo.cm.fetchdata.cmconst.FetchDataLangConst;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.cm.fetchdata.entity.PullDataStateVO;
import nc.vo.cm.fetchdata.enumeration.FetchDataObjEnum;
import nc.vo.cm.fetchdata.enumeration.FetchDataSchemaEnum;
import nc.vo.cm.fetchset.enumeration.CMBillEnum;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.uif2.LoginContext;

/**
 * ȡ�������棬 ������listģ��
 */
public class ListFetchDataMainPnl extends JPanel implements ValueChangedListener {
    private static final long serialVersionUID = 5769904004573851559L;

    private UIPanel upPanel;

    /**
     * ȡ������
     */
    private UIPanel pullObjPanel;

    /**
     * �����֯
     */
    private UIRefPane refFactory;

    /**
     * ȡ������
     */
    private UIComboBox tetFetchDataObj;

    /**
     * ����ڼ�
     */
    private UIRefPane refPeriod;

    /**
     * �б�ģ��
     */
    private BillListView billListView;

    private BillManageModel model;

    // ȡ����ť
    private FetchDataAction fetchDataAction;

    // ��鰴ť
    private CheckAciton checkAciton;

    // ȫ��ȡ����ť
    private AllCancelAction allCancelAction;

    // ��¼֮ǰ�Ƿ�ȡ��������һ��ȡ����Ҫ��¼���еı����¼
    private boolean isFetchBefore = false;

    // Ĭ��ȫѡ
    private int pullDataType = 0;

    private String currentPeriod = "";

    /**
     * ��ں���
     */
    public void initUI() {
        this.setLayout(new BorderLayout());
        this.add(this.getUpPanel(), BorderLayout.NORTH);
        this.add(this.getBillListView(), BorderLayout.CENTER);
        // ������ݲ�ͬ������ʾ��ͬ����,��ʼ״̬��orgΪ���Ի�Ĭ������
        this.refreshDates();
    }

    /**
     * �������б�ֵ�ı��ʱ��Ҳ��Ҫ�����жϣ���ϵͳȡ�������е�ȡ������ ����������ȡ������
     *
     * @return the tetFetchDataObj
     */
    class EventActionListener implements java.awt.event.ActionListener {
        /**
         * �������б�ı�,���¸���ҳ�湤����ȡ������������ý�������
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            ShowStatusBarMsgUtil.showStatusBarMsg("", ListFetchDataMainPnl.this.getModel().getContext());
            UIComboBox source = (UIComboBox) e.getSource();
            int index = source.getSelectedIndex();
            if (index == 0 || index == 1 || index == 2) {
                ListFetchDataMainPnl.this.pullDataType = 1;
            }
            else if (index == 3 || index == 4) {
                ListFetchDataMainPnl.this.pullDataType = 2;
            }
            else if (index == 5) {
                ListFetchDataMainPnl.this.pullDataType = 4;
            }
            // ������ݲ�ͬ������ʾ��ͬ����
            ListFetchDataMainPnl.this.refreshDates();
        }
    }

    @Override
    public void valueChanged(ValueChangedEvent event) {
        // �л�ʱ����Ϊδȡ��
        this.isFetchBefore = false;
        ShowStatusBarMsgUtil.showStatusBarMsg("", this.getModel().getContext());

        if (event.getSource() == this.getRefFactory() && event.getNewValue() != null) {
            this.setAccountPeriodRefModel();
            this.refreshDates();
        }
        // ���ϻ���ڼ�ļ���
        if (event.getSource() == this.getAccountPeriod() && event.getNewValue() != null) {
            this.refreshDates();
        }
    }

    /**
     * ���ݹ���ģʽ��ƽ������ݣ��������졢�������ȡ��
     */
    public void refreshDates() {
        // �����������
        this.getBillListView().getBillListPanel().getHeadBillModel().clearBodyData();
        // ����������
        FetchDataTypeFactory fetchDataType = new FetchDataTypeFactory();
        IFetchDataType strategy = fetchDataType.createFetchDataType(this.pullDataType);
        strategy.setUIItems4FetchSet(this, this.getRefFactory().getRefPK(), this.model.getContext().getPk_group());

        String cperiod = this.getAccountPeriod().getRefPK();
        if (CMStringUtil.isEmpty(cperiod) && CMStringUtil.isNotEmpty(this.currentPeriod)) {
            this.getAccountPeriod().setPK(this.currentPeriod);
        }	

        this.getBillListView().getBillListPanel().getHeadBillModel().loadLoadRelationItemValue();
        this.getBillListView().getBillListPanel().getHeadTable().repaint();
        this.getBillListView().getBillListPanel().getHeadTable().updateUI();
        // ����ȡ����ť�ͼ�鰴ť�Ƿ����
        this.checkForActionEnable();
    }

    /**
     * ����ȡ����ť�ͼ�鰴ť�Ƿ����
     */
    private void checkForActionEnable() {
        PullDataStateVO[] vos =
                (PullDataStateVO[]) this.getBillListView().getBillListPanel().getHeadBillModel()
                        .getBodySelectedVOs(PullDataStateVO.class.getName());
        if (CMArrayUtil.isEmpty(vos)) {
            this.checkAciton.setEnabled(false);
            this.fetchDataAction.setEnabled(false);
        }
        else {
            this.checkAciton.setEnabled(true);
            this.fetchDataAction.setEnabled(true);
        }
        int order = this.getFetchDataObj().getSelectedIndex() + 1;
        if (order == 3 || order == 5) {
            this.checkAciton.setEnabled(false);
        }

        // ����ȫ��ȡ����ť״̬
        PullDataStateVO[] allVos =
                (PullDataStateVO[]) this.getBillListView().getBillListPanel().getHeadBillModel()
                        .getBodyValueVOs(PullDataStateVO.class.getName());
        if (CMArrayUtil.isEmpty(allVos)) {
            this.allCancelAction.setEnabled(false);
        }
        else {
            this.allCancelAction.setEnabled(true);
        }
    }

    /***
     * ��ʼ��getUpPanel����
     */
    public UIPanel getUpPanel() {
        if (this.upPanel == null) {
            this.upPanel = new UIPanel();
            this.upPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            // ����
            this.upPanel.add(new UILabel(FetchDataLangConst.getSTR_FACTORY()));
            this.upPanel.add(this.getRefFactory());
            // ȡ������
            // this.upPanel.add(this.getPullDataOjb());
            this.getPullDataOjb();

            // ����ڼ�
            this.upPanel.add(new UILabel(FetchDataLangConst.getSTR_PERIOD()));
            // UIRefPane uirefPane = this.getAccountPeriod();
            this.upPanel.add(this.getAccountPeriod());
            // ���û���ڼ���� ��Ϊ����ģ���ɵ�
            this.getAccountPeriod().addValueChangedListener(this);
        }
        return this.upPanel;
    }

    /**
     * ����ڼ�
     */
    public UIRefPane getAccountPeriod() {
        if (this.refPeriod == null) {
            this.refPeriod = new UIRefPane();
            this.setAccountPeriodRefModel();
        }
        return this.refPeriod;
    }

    public void setAccountPeriodRefModel() {
        AccperiodRefModel accperiodModel = new AccperiodRefModel();
        try {
            if (CMStringUtil.isNotEmpty(this.getRefFactory().getRefPK())) {
                if (this.refPeriod == null) {
                    this.refPeriod = new UIRefPane();
                }
                // String pk_accperiodscheme = BDAdapter.getPKAccountSchemeByOrg(this.getRefFactory().getRefPK());
                // accperiodModel.setDefaultpk_accperiodscheme(pk_accperiodscheme);
                String pk_org = this.getRefFactory().getRefPK();
                UFDate date = AppContext.getInstance().getBusiDate();
                AccperiodmonthVO accperiodmonthVO = BDAdapter.getAccPeriodMonthVO(pk_org, date);
                if (accperiodmonthVO != null) {
                    this.currentPeriod = accperiodmonthVO.getYearmth();
                    this.refPeriod.setPK(accperiodmonthVO.getYearmth());
                    accperiodModel.setDefaultpk_accperiodscheme(accperiodmonthVO.getPk_accperiodscheme());
                }

            }
        }
        catch (Exception e) {
            // �����쳣
        }
        finally {
            this.refPeriod.setRefModel(accperiodModel);
        }
    }

    /**
     * ȡ������
     */
    public UIPanel getPullDataOjb() {
        if (this.pullObjPanel == null) {
            this.pullObjPanel = new UIPanel();
            this.pullObjPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            this.pullObjPanel.add(new UILabel(FetchDataLangConst.getCOMBOX_FETCHOBJ()));
            this.pullObjPanel.add(this.getFetchDataObj());
            this.pullObjPanel.setVisible(true);
        }
        return this.pullObjPanel;
    }

    /**
     * �����û���Ȩ�޵Ĺ���
     */
    private void initPermisionFactory() {
        // �û���Ȩ�޵���֯
        String[] hesPermissionOrgs = this.getModel().getContext().getPkorgs();

        this.refFactory.getRefModel().setFilterPks(hesPermissionOrgs, IFilterStrategy.INSECTION);
    }

    /**
     * ���տ����֯����ù���
     */
    public UIRefPane getRefFactory() {
        if (this.refFactory == null) {
            this.refFactory = new UIRefPane();
            this.refFactory.setPreferredSize(new Dimension(200, 20));
            this.refFactory.addValueChangedListener(this);
            FactoryDefaultRefModel factoryModel = new FactoryDefaultRefModel();
            this.refFactory.setRefModel(factoryModel);
            LoginContext context = this.getModel().getContext();
            this.refFactory.setPK(context.getPk_org());
            this.initPermisionFactory();
        }
        return this.refFactory;
    }

    /**
     * �����б�ȡ�����󣺲��ϳ��ⵥ������Ʒ��ⵥ
     */
    @SuppressWarnings("unchecked")
    public UIComboBox getFetchDataObj() {
        if (this.tetFetchDataObj == null) {
            this.tetFetchDataObj = new UIComboBox();
//            this.tetFetchDataObj.addItem(FetchDataObjEnum.MATERIALOUT.getName());
//            this.tetFetchDataObj.addItem(FetchDataObjEnum.PRODIN.getName());
//            this.tetFetchDataObj.addItem(FetchDataObjEnum.ISSTUFF.getName());
//            this.tetFetchDataObj.addItem(FetchDataObjEnum.ACT.getName());
//            this.tetFetchDataObj.addItem(FetchDataObjEnum.GXWW.getName());
//            this.tetFetchDataObj.addItem(FetchDataObjEnum.SPOIL.getName());
            this.tetFetchDataObj.addActionListener(new EventActionListener());
        }
        return this.tetFetchDataObj;
    }

    public BillManageModel getModel() {
        return this.model;
    }

    public void setModel(BillManageModel model) {
        this.model = model;
    }

    public BillListView getBillListView() {
        this.billListView.getBillListPanel().getHeadBillModel()
                .addRowStateChangeEventListener(new BillRowStateChangeEvent());
        return this.billListView;
    }

    class BillRowStateChangeEvent implements IBillModelRowStateChangeEventListener {
        @Override
        public void valueChanged(RowStateChangeEvent event) {
            ListFetchDataMainPnl.this.checkForActionEnable();
        }
    }

    public void setBillListView(BillListView billListView) {
        this.billListView = billListView;
    }

    /**
     * ��֯����vo ���в�ѯ
     *
     * @param pullDataStateVOs
     *            ѡ�е�����
     * @return ����vo
     */
    public FetchParamVO getParamVO(PullDataStateVO[] pullDataStateVOs) throws BusinessException {
        if (CMArrayUtil.isEmpty(pullDataStateVOs)) {
            return null;
        }

        List<String> costCenteridList = new ArrayList<String>();
        List<List<UFDate>> dateList = new ArrayList<List<UFDate>>();
        FetchParamVO paramvo = new FetchParamVO();
        boolean isFetched = false;
        for (PullDataStateVO vo : pullDataStateVOs) {
            if (vo.getBfetched().booleanValue()) {
                isFetched = true;// ֻҪ��һ��ȡ��ֵ���ʹ�������ȡ��ֵ
            }

            if (CMStringUtil.isNotEmpty(vo.getCcostcenterid())) {
                costCenteridList.add(vo.getCcostcenterid());
            }
            List<UFDate> beginEndDateList = new ArrayList<UFDate>();
            beginEndDateList.add(vo.getDbegindate());
            beginEndDateList.add(vo.getDenddate());
            dateList.add(beginEndDateList);
            // �浥�����ͺͽ������͵�����
            if (Integer.valueOf(FetchDataSchemaEnum.PERIODSCHEMA.getEnumValue().getValue()).compareTo(
                    pullDataStateVOs[0].getIfetchscheme()) == 0) {// ����
                if (!paramvo.getBillTypeMap().containsKey(vo.getCbilltype())) {
                    List<String> list = new ArrayList<String>();
                    list.add(vo.getCtranstypeid());
                    paramvo.getBillTypeMap().put(vo.getCbilltype(), list);
                }
                else {
                    paramvo.getBillTypeMap().get(vo.getCbilltype()).add(vo.getCtranstypeid());
                }
            }
        }
        // �浥�����ͺͽ������͵�����
        if (Integer.valueOf(FetchDataSchemaEnum.WEEKSCHEMA.getEnumValue().getValue()).compareTo(
                pullDataStateVOs[0].getIfetchscheme()) == 0) {// ����ȡ����
            if (Integer.valueOf(FetchDataObjEnum.MATERIALOUT.getEnumValue().getValue()).compareTo(
                    pullDataStateVOs[0].getIfetchobjtype()) == 0) {// ���ϳ�
                paramvo.getBillTypeMap().put(Integer.valueOf(CMBillEnum.MATEROUT.getEnumValue().getValue()), null);
                paramvo.getBillTypeMap().put(Integer.valueOf(CMBillEnum.OUTADJUST.getEnumValue().getValue()), null);
                paramvo.getBillTypeMap().put(Integer.valueOf(CMBillEnum.PROCESSIN.getEnumValue().getValue()), null);
            }
            else if (Integer.valueOf(FetchDataObjEnum.PRODIN.getEnumValue().getValue()).compareTo(
                    pullDataStateVOs[0].getIfetchobjtype()) == 0) {// 2����Ʒ
                paramvo.getBillTypeMap().put(Integer.valueOf(CMBillEnum.PRODUCTIN.getEnumValue().getValue()), null);
                paramvo.getBillTypeMap().put(Integer.valueOf(CMBillEnum.PROCESSIN.getEnumValue().getValue()), null);
            }
            else if (Integer.valueOf(FetchDataObjEnum.SPOIL.getEnumValue().getValue()).compareTo(
                    pullDataStateVOs[0].getIfetchobjtype()) == 0) {// 5����������ⵥ modified by sunyqb at 2011.12.29
                paramvo.getBillTypeMap().put(Integer.valueOf(CMBillEnum.DISCARDIN.getEnumValue().getValue()), null);
            }
        }
        // ����ڼ�
        Object[] accountPeriod = (Object[]) this.getAccountPeriod().getValueObj();
        paramvo.setCcostcenterid(costCenteridList);
        paramvo.setBeginenddate(dateList);
        paramvo.setPk_group(this.getModel().getContext().getPk_group());
        paramvo.setPk_org(pullDataStateVOs[0].getPk_org());
        paramvo.setPk_org_v(this.getOrgVIDByOID(paramvo.getPk_org()));

        paramvo.setIfetchobjtype(pullDataStateVOs[0].getIfetchobjtype()); // ȡ������ 1 ���ϳ���2����Ʒ�� 3 ��ҵ
        paramvo.setDaccountperiod((String) accountPeriod[0]); // �ڼ�
        paramvo.setPulldatatype(pullDataStateVOs[0].getPulldatatype()); // ȡ�����ͣ�1������㣬2�������죬4��Ʒȡ��
        paramvo.setIfetchscheme(pullDataStateVOs[0].getIfetchscheme()); // ȡ������
        paramvo.setFetchBefore(isFetched);// ��¼֮ǰ�Ƿ�ȡ��������һ��ȡ����Ҫ��¼���еı����¼
        // ����/����
        paramvo.setNday(pullDataStateVOs[0].getNday()); // �������ȡ������null
        paramvo.setBusiDate(AppContext.getInstance().getBusiDate());// ҵ������
        return paramvo;
    }

    /**
     * ������֯oid��ȡ���±���vid
     */
    private String getOrgVIDByOID(String oID) throws BusinessException {
        if (!PubAppTool.isNull(oID)) {
            HashMap<String, String> map =
                    NCLocator.getInstance().lookup(IOrgUnitPubService.class).getNewVIDSByOrgIDS(new String[] {
                        oID
                    });
            return map.get(oID);
        }
        // ���oIDΪ�գ����ؿ�
        return null;
    }

    public void initLinkData(String pkOrg, String period) {
        this.getAccountPeriod().setPK(period);
        this.refreshDates();
    }

    /**
     * ���
     * FetchDataAction
     */
    public FetchDataAction getFetchDataAction() {
        return this.fetchDataAction;
    }

    public void setFetchDataAction(FetchDataAction fetchDataAction) {
        this.fetchDataAction = fetchDataAction;
    }

    public CheckAciton getCheckAciton() {
        return this.checkAciton;
    }

    public void setCheckAciton(CheckAciton checkAciton) {
        this.checkAciton = checkAciton;
    }

    public boolean isFetchBefore() {
        return this.isFetchBefore;
    }

    public void setFetchBefore(boolean isFetchBefore) {
        this.isFetchBefore = isFetchBefore;
    }

    public AllCancelAction getAllCancelAction() {
        return this.allCancelAction;
    }

    public void setAllCancelAction(AllCancelAction allCancelAction) {
        this.allCancelAction = allCancelAction;
    }

    public int getPullDataType() {
        return this.pullDataType;
    }
}
