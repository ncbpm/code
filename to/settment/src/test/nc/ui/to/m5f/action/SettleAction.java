package nc.ui.to.m5f.action;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.query2.sql.process.QueryCondition;
import nc.vo.pubapp.query2.sql.process.QueryConstants;
import nc.vo.pubapp.query2.sql.process.QuerySchemeUtils;
import nc.vo.pubapp.res.Variable;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.to.m5f.entity.SettleListVO;
import nc.vo.to.m5f.entity.SettleQueryParaVO;
import nc.vo.to.m5f.entity.SettleQueryTransVO;
import nc.vo.to.m5f.entity.SettleQueryViewVO;

import nc.itf.to.m5f.ISettleMaintain;

import nc.bs.framework.common.NCLocator;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction;
import nc.ui.pubapp.uif2app.view.ShowUpableBillListView;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.ui.to.m5f.billref.SettleDialog;
import nc.ui.to.m5f.model.SettleListDispModel;
import nc.ui.to.m5f.model.SettleListPageModelDataManager;
import nc.ui.uif2.IShowMsgConstant;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.TemplateContainer;

/**
 * 结算动作处理类
 */
public class SettleAction extends DefaultQueryAction {

  private static final long serialVersionUID = -6056032772997320409L;

  private ISettleMaintain iSettleMaintain;

  private ShowUpableBillListView listView;

  // 查询面板
  private QueryConditionDLGDelegator queryDLGDel;

  // 中间界面
  private SettleDialog settleDlg;

  private TemplateContainer template;

  public SettleAction() {
    this.setBtnName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
        "4009003_0", "04009003-0016")/*@res "结算"*/);
    this.setCode("Settle");
    this.putValue(Action.SHORT_DESCRIPTION, nc.vo.ml.NCLangRes4VoTransl
        .getNCLangRes().getStrByID("4009003_0", "04009003-0016")/*@res "结算"*/);
  }

  @Override
  public void doAction(ActionEvent e) {
    try {
      QueryConditionDLGDelegator queryConDLG = this.getQryDLGDelegator();
      queryConDLG.getQueryConditionDLG().setVisibleAdvancePanel(false);
      if (UIDialog.ID_OK == queryConDLG.showModal()) {
        //
        IQueryScheme scheme = queryConDLG.getQueryScheme();
        scheme.put(QueryConstants.MAX_QUERY_COUNT_CONSTANT,
            this.getMaxQueryCount());
        scheme.put(QueryConstants.KEY_FUNC_NODE, this.getFunNode());

        // 组织结算查询条件VO
        SettleQueryTransVO tansvo = this.getService().querySettle(scheme);
        SettleQueryViewVO[] displayVOs = tansvo.getGatherVOs();
        if (displayVOs == null || displayVOs.length == 0) {
          ShowStatusBarMsgUtil
              .showStatusBarMsg(IShowMsgConstant.getQueryNullInfo(), this
                  .getModel().getContext());
          return;
        }
        // 设置到结算中间界面
        Integer fgatherbyflag = this.getGatherByFlag(scheme);
        this.getSettleDialog().setData(displayVOs, fgatherbyflag, scheme);
        if (displayVOs.length == Variable.getMaxQueryCount()) {
          String message =
              NCLangRes.getInstance().getStrByID("4009003_0", "04009003-0292")/*查询结果只能返回5000行，超出部分请重新结算*/;
          MessageDialog.showHintDlg(this.getSettleDialog(), NCLangRes
              .getInstance().getStrByID("4009003_0", "04009003-0293")/*提示*/,
              message);
          ShowStatusBarMsgUtil.showStatusBarMsg(message, this.getModel()
              .getContext());
        }
        if (UIDialog.ID_OK == this.getSettleDialog().showModal()) {

          SettleQueryViewVO[] selectDispVOs = this.settleDlg.getSelectedVOs();
          // 设置”询价“字段
          this.setBaskPriceFlag(scheme, selectDispVOs);
          // 只传递要处理的数据
          tansvo.setGatherVOs(selectDispVOs);
          this.insertSettleList(tansvo);
        }

      }
    }
    catch (Exception ex) {
      ExceptionUtils.wrappException(ex);
    }
  }

  // 设置询价字段
  private void setBaskPriceFlag(IQueryScheme scheme, SettleQueryViewVO[] viewvos) {
    // 获取查询条件中的”询价“字段
    QueryCondition condition =
        QuerySchemeUtils.getQueryCondition(SettleQueryParaVO.BASKPRICE, scheme);

    UFBoolean baskpriceflag = UFBoolean.FALSE;
    if (condition != null) {
      Object[] values = condition.getValues();
      baskpriceflag = ValueUtils.getUFBoolean(values[0]);
    }
    for (SettleQueryViewVO viewvo : viewvos) {
      viewvo.setBaskpriceflag(baskpriceflag);
    }
  }

  public ShowUpableBillListView getListView() {
    return this.listView;
  }

  @Override
  public QueryConditionDLGDelegator getQryDLGDelegator() {
    if (this.queryDLGDel == null) {
      TemplateInfo tempinfo = new TemplateInfo();

      tempinfo.setPk_Org(this.getModel().getContext().getPk_group());
      tempinfo.setFunNode(this.getModel().getContext().getNodeCode());
      tempinfo.setUserid(this.getModel().getContext().getPk_loginUser());
      tempinfo.setNodekey("结算查询");/*-=notranslate=-*/
      // 返回供应链的查询对话框
      this.queryDLGDel =
          new QueryConditionDLGDelegator(this.getModel().getContext()
              .getEntranceUI(), tempinfo);
      this.initQueryConditionDLG(this.queryDLGDel);
    }
    return this.queryDLGDel;
  }

  public TemplateContainer getTemplate() {
    return this.template;
  }

  public void setListView(ShowUpableBillListView listView) {
    this.listView = listView;
  }

  public void setTemplate(TemplateContainer template) {
    this.template = template;
  }

  @Override
  protected boolean isActionEnable() {
    return true;
  }

  private void combineDispCatch(SettleListDispModel dispmodel) {

    SettleListPageModelDataManager manage =
        (SettleListPageModelDataManager) this.getDataManager();
    SettleListDispModel catchemodel = manage.getDispmodel();
    if (catchemodel == null) {
      manage.setDispmodel(dispmodel);
      return;
    }
    catchemodel.getTotalDetailRela().putAll(dispmodel.getTotalDetailRela());
    catchemodel.getDetailVO().putAll(dispmodel.getDetailVO());
    catchemodel.getDispVO().putAll(dispmodel.getDispVO());
  }

  private Integer getGatherByFlag(IQueryScheme scheme) {
    QueryCondition condition =
        QuerySchemeUtils.getQueryCondition(SettleQueryParaVO.FGATHEREDBYFLAG,
            scheme);

    Object[] values = condition.getValues();
    return ValueUtils.getInteger(values[0]);

  }

  private ISettleMaintain getService() {
    if (this.iSettleMaintain == null) {
      this.iSettleMaintain =
          NCLocator.getInstance().lookup(ISettleMaintain.class);
    }
    return this.iSettleMaintain;
  }

  private SettleDialog getSettleDialog() {
    if (this.settleDlg == null) {

      this.settleDlg =
          new SettleDialog(this.listView, NCLangRes.getInstance().getStrByID(
              "4009003_0", "04009003-0216")/*结算中间界面*/, this.template, this
              .getModel().getContext());
    }
    return this.settleDlg;
  }

  private void insertSettleList(SettleQueryTransVO transvo) throws Exception {
    this.getModel().setUiState(UIState.ADD);
    SettleListVO[] retVOs = this.getService().insertSettleList(transvo);
    // 构造显示VO
    SettleListDispModel dispmodel = new SettleListDispModel();
    dispmodel.initDispModel(retVOs);
    SettleListVO[] dispVOs = dispmodel.getDispVOs();

    this.combineDispCatch(dispmodel);
    // 把显示VO更新到界面
    BillManageModel model = (BillManageModel) this.getModel();
    for (int m = 0; m < dispVOs.length; m++) {
      model.directlyAdd(dispVOs[m]);
    }
    this.getModel().setUiState(UIState.NOT_EDIT);
  }

}
