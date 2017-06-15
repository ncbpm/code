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
 * 取数主界面， 加载了list模板
 */
public class ListFetchDataMainPnl extends JPanel implements ValueChangedListener {
    private static final long serialVersionUID = 5769904004573851559L;

    private UIPanel upPanel;

    /**
     * 取数对象
     */
    private UIPanel pullObjPanel;

    /**
     * 库存组织
     */
    private UIRefPane refFactory;

    /**
     * 取数对象
     */
    private UIComboBox tetFetchDataObj;

    /**
     * 会计期间
     */
    private UIRefPane refPeriod;

    /**
     * 列表模板
     */
    private BillListView billListView;

    private BillManageModel model;

    // 取数按钮
    private FetchDataAction fetchDataAction;

    // 检查按钮
    private CheckAciton checkAciton;

    // 全部取消按钮
    private AllCancelAction allCancelAction;

    // 记录之前是否取过数，第一次取数需要记录所有的表体记录
    private boolean isFetchBefore = false;

    // 默认全选
    private int pullDataType = 0;

    private String currentPeriod = "";

    /**
     * 入口函数
     */
    public void initUI() {
        this.setLayout(new BorderLayout());
        this.add(this.getUpPanel(), BorderLayout.NORTH);
        this.add(this.getBillListView(), BorderLayout.CENTER);
        // 否则根据不同策略显示不同画面,初始状态，org为个性化默认设置
        this.refreshDates();
    }

    /**
     * 当下拉列表值改变的时候，也需要重新判断，外系统取数设置中的取数方案 并重新设置取数界面
     *
     * @return the tetFetchDataObj
     */
    class EventActionListener implements java.awt.event.ActionListener {
        /**
         * 当下拉列表改变,重新根据页面工厂，取数对象进行设置界面数据
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
            // 否则根据不同策略显示不同画面
            ListFetchDataMainPnl.this.refreshDates();
        }
    }

    @Override
    public void valueChanged(ValueChangedEvent event) {
        // 切换时设置为未取过
        this.isFetchBefore = false;
        ShowStatusBarMsgUtil.showStatusBarMsg("", this.getModel().getContext());

        if (event.getSource() == this.getRefFactory() && event.getNewValue() != null) {
            this.setAccountPeriodRefModel();
            this.refreshDates();
        }
        // 加上会计期间的监听
        if (event.getSource() == this.getAccountPeriod() && event.getNewValue() != null) {
            this.refreshDates();
        }
    }

    /**
     * 根据工厂模式设计界面数据：生产制造、存货核算取数
     */
    public void refreshDates() {
        // 清空所有数据
        this.getBillListView().getBillListPanel().getHeadBillModel().clearBodyData();
        // 设置新数据
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
        // 设置取数按钮和检查按钮是否可用
        this.checkForActionEnable();
    }

    /**
     * 设置取数按钮和检查按钮是否可用
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

        // 设置全部取消按钮状态
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
     * 初始化getUpPanel界面
     */
    public UIPanel getUpPanel() {
        if (this.upPanel == null) {
            this.upPanel = new UIPanel();
            this.upPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            // 工厂
            this.upPanel.add(new UILabel(FetchDataLangConst.getSTR_FACTORY()));
            this.upPanel.add(this.getRefFactory());
            // 取数对象
            // this.upPanel.add(this.getPullDataOjb());
            this.getPullDataOjb();

            // 会计期间
            this.upPanel.add(new UILabel(FetchDataLangConst.getSTR_PERIOD()));
            // UIRefPane uirefPane = this.getAccountPeriod();
            this.upPanel.add(this.getAccountPeriod());
            // 设置会计期间监听 改为单据模板后干掉
            this.getAccountPeriod().addValueChangedListener(this);
        }
        return this.upPanel;
    }

    /**
     * 会计期间
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
            // 不抛异常
        }
        finally {
            this.refPeriod.setRefModel(accperiodModel);
        }
    }

    /**
     * 取数对象
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
     * 过滤用户有权限的工厂
     */
    private void initPermisionFactory() {
        // 用户有权限的组织
        String[] hesPermissionOrgs = this.getModel().getContext().getPkorgs();

        this.refFactory.getRefModel().setFilterPks(hesPermissionOrgs, IFilterStrategy.INSECTION);
    }

    /**
     * 参照库存组织：获得工厂
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
     * 下拉列表取数对象：材料出库单，产生品入库单
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
     * 组织参数vo 进行查询
     *
     * @param pullDataStateVOs
     *            选中的数据
     * @return 参数vo
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
                isFetched = true;// 只要有一条取过值，就代表曾经取过值
            }

            if (CMStringUtil.isNotEmpty(vo.getCcostcenterid())) {
                costCenteridList.add(vo.getCcostcenterid());
            }
            List<UFDate> beginEndDateList = new ArrayList<UFDate>();
            beginEndDateList.add(vo.getDbegindate());
            beginEndDateList.add(vo.getDenddate());
            dateList.add(beginEndDateList);
            // 存单据类型和交易类型的数据
            if (Integer.valueOf(FetchDataSchemaEnum.PERIODSCHEMA.getEnumValue().getValue()).compareTo(
                    pullDataStateVOs[0].getIfetchscheme()) == 0) {// 按月
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
        // 存单据类型和交易类型的数据
        if (Integer.valueOf(FetchDataSchemaEnum.WEEKSCHEMA.getEnumValue().getValue()).compareTo(
                pullDataStateVOs[0].getIfetchscheme()) == 0) {// 周期取数用
            if (Integer.valueOf(FetchDataObjEnum.MATERIALOUT.getEnumValue().getValue()).compareTo(
                    pullDataStateVOs[0].getIfetchobjtype()) == 0) {// 材料出
                paramvo.getBillTypeMap().put(Integer.valueOf(CMBillEnum.MATEROUT.getEnumValue().getValue()), null);
                paramvo.getBillTypeMap().put(Integer.valueOf(CMBillEnum.OUTADJUST.getEnumValue().getValue()), null);
                paramvo.getBillTypeMap().put(Integer.valueOf(CMBillEnum.PROCESSIN.getEnumValue().getValue()), null);
            }
            else if (Integer.valueOf(FetchDataObjEnum.PRODIN.getEnumValue().getValue()).compareTo(
                    pullDataStateVOs[0].getIfetchobjtype()) == 0) {// 2产成品
                paramvo.getBillTypeMap().put(Integer.valueOf(CMBillEnum.PRODUCTIN.getEnumValue().getValue()), null);
                paramvo.getBillTypeMap().put(Integer.valueOf(CMBillEnum.PROCESSIN.getEnumValue().getValue()), null);
            }
            else if (Integer.valueOf(FetchDataObjEnum.SPOIL.getEnumValue().getValue()).compareTo(
                    pullDataStateVOs[0].getIfetchobjtype()) == 0) {// 5生产报废入库单 modified by sunyqb at 2011.12.29
                paramvo.getBillTypeMap().put(Integer.valueOf(CMBillEnum.DISCARDIN.getEnumValue().getValue()), null);
            }
        }
        // 会计期间
        Object[] accountPeriod = (Object[]) this.getAccountPeriod().getValueObj();
        paramvo.setCcostcenterid(costCenteridList);
        paramvo.setBeginenddate(dateList);
        paramvo.setPk_group(this.getModel().getContext().getPk_group());
        paramvo.setPk_org(pullDataStateVOs[0].getPk_org());
        paramvo.setPk_org_v(this.getOrgVIDByOID(paramvo.getPk_org()));

        paramvo.setIfetchobjtype(pullDataStateVOs[0].getIfetchobjtype()); // 取数对象： 1 材料出，2产成品入 3 作业
        paramvo.setDaccountperiod((String) accountPeriod[0]); // 期间
        paramvo.setPulldatatype(pullDataStateVOs[0].getPulldatatype()); // 取数类型：1存货核算，2生产制造，4废品取数
        paramvo.setIfetchscheme(pullDataStateVOs[0].getIfetchscheme()); // 取数方案
        paramvo.setFetchBefore(isFetched);// 记录之前是否取过数，第一次取数需要记录所有的表体记录
        // ：月/周期
        paramvo.setNday(pullDataStateVOs[0].getNday()); // 如果是月取数则是null
        paramvo.setBusiDate(AppContext.getInstance().getBusiDate());// 业务日期
        return paramvo;
    }

    /**
     * 根据组织oid获取最新本版vid
     */
    private String getOrgVIDByOID(String oID) throws BusinessException {
        if (!PubAppTool.isNull(oID)) {
            HashMap<String, String> map =
                    NCLocator.getInstance().lookup(IOrgUnitPubService.class).getNewVIDSByOrgIDS(new String[] {
                        oID
                    });
            return map.get(oID);
        }
        // 如果oID为空，返回空
        return null;
    }

    public void initLinkData(String pkOrg, String period) {
        this.getAccountPeriod().setPK(period);
        this.refreshDates();
    }

    /**
     * 获得
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
