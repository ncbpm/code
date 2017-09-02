package nc.ui.invp.balance.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class balance_config extends AbstractJavaBeanDefinition{
	private Map<String, Object> context = new HashMap();
public nc.ui.invp.balance.view.query.SettleQueryInfo getM422Xqueryinfo(){
 if(context.get("m422Xqueryinfo")!=null)
 return (nc.ui.invp.balance.view.query.SettleQueryInfo)context.get("m422Xqueryinfo");
  nc.ui.invp.balance.view.query.SettleQueryInfo bean = new nc.ui.invp.balance.view.query.SettleQueryInfo("27f6c083-b2db-41a3-a5eb-20d8ccbd6149",getManagedMap0());  context.put("m422Xqueryinfo",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap0(){  Map map = new HashMap();  map.put("pk_org","pk_storereq_b.pk_nextbalanceorg");  map.put("crequesttype","freqtypeflag");  map.put("pk_reqstordoc","pk_storereq_b.pk_reqstordoc");  map.put("pk_storereq_b.dreqdate","pk_storereq_b.dreqdate");  map.put("burgency","burgency");  map.put("bisbalance","pk_storereq_b.bendgather");  map.put("dappdate","dbilldate");  map.put("pk_appdept","pk_storereq_b.pk_appdept_v");  map.put("pk_apppsn","pk_storereq_b.pk_apppsn");  map.put("pk_material.pk_marbasclass","pk_storereq_b.pk_srcmaterial.pk_marbasclass");  map.put("cprojectid","pk_storereq_b.cprojectid");  map.put("pk_reqstockorg","pk_org");  return map;}

public nc.ui.invp.balance.view.query.SettleQueryInfo getM4B36queryinfo(){
 if(context.get("m4B36queryinfo")!=null)
 return (nc.ui.invp.balance.view.query.SettleQueryInfo)context.get("m4B36queryinfo");
  nc.ui.invp.balance.view.query.SettleQueryInfo bean = new nc.ui.invp.balance.view.query.SettleQueryInfo("27f6c083-b2db-41a3-a5eb-20d8ccbd6149",getManagedMap1());  context.put("m4B36queryinfo",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap1(){  Map map = new HashMap();  map.put("pk_org","wo_plan_inv.pk_stockorg");  map.put("pk_reqstordoc","wo_plan_inv.pk_stordoc");  map.put("pk_storereq_b.dreqdate","wo_plan_inv.required_date");  map.put("bisbalance","wo_plan_inv.bendgather");  map.put("dappdate","billmaketime");  map.put("pk_appdept","pk_wo_dept_v");  map.put("pk_apppsn","pk_director");  map.put("pk_material.pk_marbasclass","wo_plan_inv.pk_material_v.pk_marbasclass");  map.put("pk_reqstockorg","wo_plan_inv.pk_stockorg");  return map;}

public nc.ui.invp.balance.action.RefreshAction getRefreshAction(){
 if(context.get("refreshAction")!=null)
 return (nc.ui.invp.balance.action.RefreshAction)context.get("refreshAction");
  nc.ui.invp.balance.action.RefreshAction bean = new nc.ui.invp.balance.action.RefreshAction();
  context.put("refreshAction",bean);
  bean.setModel(getManageAppModel());
  bean.setInterceptor(getListViewShowUpComponentInterceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.action.CancelGatherAction getCancelGatherAction(){
 if(context.get("cancelGatherAction")!=null)
 return (nc.ui.invp.balance.action.CancelGatherAction)context.get("cancelGatherAction");
  nc.ui.invp.balance.action.CancelGatherAction bean = new nc.ui.invp.balance.action.CancelGatherAction();
  context.put("cancelGatherAction",bean);
  bean.setModel(getManageAppModel());
  bean.setInterceptor(getListViewShowUpComponentInterceptor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.action.QueryAction getQueryAction(){
 if(context.get("queryAction")!=null)
 return (nc.ui.invp.balance.action.QueryAction)context.get("queryAction");
  nc.ui.invp.balance.action.QueryAction bean = new nc.ui.invp.balance.action.QueryAction();
  context.put("queryAction",bean);
  bean.setQryCondDLGInitializer(getQryCondDLGInitializer());
  bean.setNodeKey("4007400101");
  bean.setModel(getManageAppModel());
  bean.setM422XQueryInfo(getM422Xqueryinfo());
  bean.setM4B36QueryInfo(getM4B36queryinfo());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.query.BalanceQueryDLGInitializer getQryCondDLGInitializer(){
 if(context.get("qryCondDLGInitializer")!=null)
 return (nc.ui.invp.balance.query.BalanceQueryDLGInitializer)context.get("qryCondDLGInitializer");
  nc.ui.invp.balance.query.BalanceQueryDLGInitializer bean = new nc.ui.invp.balance.query.BalanceQueryDLGInitializer();
  context.put("qryCondDLGInitializer",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.action.BalancePrintAction getPrintAction(){
 if(context.get("printAction")!=null)
 return (nc.ui.invp.balance.action.BalancePrintAction)context.get("printAction");
  nc.ui.invp.balance.action.BalancePrintAction bean = new nc.ui.invp.balance.action.BalancePrintAction();
  context.put("printAction",bean);
  bean.setList(getListView());
  bean.setModel(getManageAppModel());
  bean.setDirectPrint(true);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.action.BalancePrintAction getPreViewAction(){
 if(context.get("preViewAction")!=null)
 return (nc.ui.invp.balance.action.BalancePrintAction)context.get("preViewAction");
  nc.ui.invp.balance.action.BalancePrintAction bean = new nc.ui.invp.balance.action.BalancePrintAction();
  context.put("preViewAction",bean);
  bean.setList(getListView());
  bean.setModel(getManageAppModel());
  bean.setDirectPrint(false);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.action.QueryOnhandAction getAtpQueryAction(){
 if(context.get("atpQueryAction")!=null)
 return (nc.ui.invp.balance.action.QueryOnhandAction)context.get("atpQueryAction");
 nc.ui.invp.balance.action.QueryOnhandAction bean = new nc.ui.invp.balance.action.QueryOnhandAction();
  context.put("atpQueryAction",bean);
  bean.setList(getListView());
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getPrintGroup(){
	 if(context.get("printGroup")!=null)
	 return (nc.funcnode.ui.action.GroupAction)context.get("printGroup");
	  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
	  context.put("printGroup",bean);
	  bean.setCode("printGroup");
	  bean.setActions(getManagedList0());
	setBeanFacotryIfBeanFacatoryAware(bean);
	invokeInitializingBean(bean);
	return bean;
	}

private List getManagedList0(){  List list = new ArrayList();  list.add(getPrintAction());  list.add(getPreViewAction());  return list;}

public nc.ui.invp.balance.action.GatherAction getGatherAction(){
 if(context.get("gatherAction")!=null)
 return (nc.ui.invp.balance.action.GatherAction)context.get("gatherAction");
  nc.ui.invp.balance.action.GatherAction bean = new nc.ui.invp.balance.action.GatherAction();
  context.put("gatherAction",bean);
  bean.setModel(getManageAppModel());
  bean.setDataManager(getModelDataManager());
  bean.setList(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.action.BalanceAction getBalanceAction(){
 if(context.get("balanceAction")!=null)
 return (nc.ui.invp.balance.action.BalanceAction)context.get("balanceAction");
  nc.ui.invp.balance.action.BalanceAction bean = new nc.ui.invp.balance.action.BalanceAction();
  context.put("balanceAction",bean);
  bean.setModel(getManageAppModel());
  bean.setTreemodel(getBalancetreemodel());
  bean.setDataManager(getModelDataManager());
  bean.setList(getListView());
  bean.setTreeview(getBalanceTreeView());
  bean.setShowUpComponent(getBalanceTreeView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.action.GatherRuleAction getGatherRuleAction(){
 if(context.get("gatherRuleAction")!=null)
 return (nc.ui.invp.balance.action.GatherRuleAction)context.get("gatherRuleAction");
  nc.ui.invp.balance.action.GatherRuleAction bean = new nc.ui.invp.balance.action.GatherRuleAction();
  context.put("gatherRuleAction",bean);
  bean.setModel(getManageAppModel());
  bean.setDataManager(getModelDataManager());
  bean.setList(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.action.BalanceRuleAction getBalanceRuleAction(){
 if(context.get("balanceRuleAction")!=null)
 return (nc.ui.invp.balance.action.BalanceRuleAction)context.get("balanceRuleAction");
  nc.ui.invp.balance.action.BalanceRuleAction bean = new nc.ui.invp.balance.action.BalanceRuleAction();
  context.put("balanceRuleAction",bean);
  bean.setModel(getManageAppModel());
  bean.setDataManager(getModelDataManager());
  bean.setList(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.action.CancelBalanceAction getCancelBalanceAction(){
 if(context.get("cancelBalanceAction")!=null)
 return (nc.ui.invp.balance.action.CancelBalanceAction)context.get("cancelBalanceAction");
  nc.ui.invp.balance.action.CancelBalanceAction bean = new nc.ui.invp.balance.action.CancelBalanceAction();
  context.put("cancelBalanceAction",bean);
  bean.setModel(getManageAppModel());
  bean.setDataManager(getModelDataManager());
  bean.setList(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.action.CancelAction getCancelAction(){
 if(context.get("cancelAction")!=null)
 return (nc.ui.invp.balance.action.CancelAction)context.get("cancelAction");
  nc.ui.invp.balance.action.CancelAction bean = new nc.ui.invp.balance.action.CancelAction();
  context.put("cancelAction",bean);
  bean.setModel(getManageAppModel());
  bean.setShowUpComponent(getListView());
  bean.setList(getListView());
  bean.setBillModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.action.ConfirmAction getConfirmAction(){
 if(context.get("confirmAction")!=null)
 return (nc.ui.invp.balance.action.ConfirmAction)context.get("confirmAction");
  nc.ui.invp.balance.action.ConfirmAction bean = new nc.ui.invp.balance.action.ConfirmAction();
  context.put("confirmAction",bean);
  bean.setModel(getManageAppModel());
  bean.setTreemodel(getBalancetreemodel());
  bean.setDataManager(getModelDataManager());
  bean.setList(getListView());
  bean.setTreeview(getBalanceTreeView());
  bean.setShowUpComponent(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.action.BalancePrintAction getPreViewBalanceAction(){
 if(context.get("preViewBalanceAction")!=null)
 return (nc.ui.invp.balance.action.BalancePrintAction)context.get("preViewBalanceAction");
  nc.ui.invp.balance.action.BalancePrintAction bean = new nc.ui.invp.balance.action.BalancePrintAction();
  context.put("preViewBalanceAction",bean);
  bean.setList(getBalanceTreeView());
  bean.setModel(getBalancetreemodel());
  bean.setDirectPrint(false);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.action.BalancePrintAction getPrintBalanceAction(){
 if(context.get("printBalanceAction")!=null)
 return (nc.ui.invp.balance.action.BalancePrintAction)context.get("printBalanceAction");
  nc.ui.invp.balance.action.BalancePrintAction bean = new nc.ui.invp.balance.action.BalancePrintAction();
  context.put("printBalanceAction",bean);
  bean.setList(getBalanceTreeView());
  bean.setModel(getBalancetreemodel());
  bean.setDirectPrint(true);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getPrintBalanceGroup(){
 if(context.get("printBalanceGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("printBalanceGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("printBalanceGroup",bean);
  bean.setCode("printGroup");
  bean.setActions(getManagedList1());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList1(){  List list = new ArrayList();  list.add(getPrintBalanceAction());  list.add(getPreViewBalanceAction());  return list;}

public nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getBodyseparatorAction(){
 if(context.get("bodyseparatorAction")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("bodyseparatorAction");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("bodyseparatorAction",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.SeparatorAction getSeparatorAction(){
 if(context.get("separatorAction")!=null)
 return (nc.funcnode.ui.action.SeparatorAction)context.get("separatorAction");
  nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
  context.put("separatorAction",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.uif2.LoginContext getContext(){
 if(context.get("context")!=null)
 return (nc.vo.uif2.LoginContext)context.get("context");
  nc.vo.uif2.LoginContext bean = new nc.vo.uif2.LoginContext();
  context.put("context",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.model.BalanceModelService getManageModelService(){
 if(context.get("ManageModelService")!=null)
 return (nc.ui.invp.balance.model.BalanceModelService)context.get("ManageModelService");
  nc.ui.invp.balance.model.BalanceModelService bean = new nc.ui.invp.balance.model.BalanceModelService();
  context.put("ManageModelService",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.model.BalanceBillManageModel getManageAppModel(){
 if(context.get("manageAppModel")!=null)
 return (nc.ui.invp.balance.model.BalanceBillManageModel)context.get("manageAppModel");
  nc.ui.invp.balance.model.BalanceBillManageModel bean = new nc.ui.invp.balance.model.BalanceBillManageModel();
  context.put("manageAppModel",bean);
  bean.setService(getManageModelService());
  bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory getBoadatorfactory(){
 if(context.get("boadatorfactory")!=null)
 return (nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory)context.get("boadatorfactory");
  nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory bean = new nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory();
  context.put("boadatorfactory",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.bd.meta.BDObjectAdpaterFactory getBoadatorTreefactory(){
 if(context.get("boadatorTreefactory")!=null)
 return (nc.vo.bd.meta.BDObjectAdpaterFactory)context.get("boadatorTreefactory");
  nc.vo.bd.meta.BDObjectAdpaterFactory bean = new nc.vo.bd.meta.BDObjectAdpaterFactory();
  context.put("boadatorTreefactory",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.view.BalanceBillListView getListView(){
 if(context.get("listView")!=null)
 return (nc.ui.invp.balance.view.BalanceBillListView)context.get("listView");
  nc.ui.invp.balance.view.BalanceBillListView bean = new nc.ui.invp.balance.view.BalanceBillListView();
  context.put("listView",bean);
  bean.setModel(getManageAppModel());
  bean.setMultiSelectionEnable(true);
  bean.setMultiSelectionMode(1);
  bean.setTemplateContainer(getTemplateContainer());
  bean.setUserdefitemListPreparator(getCompositeBillListDataPrepare_690b82());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare getCompositeBillListDataPrepare_690b82(){
 if(context.get("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#690b82")!=null)
 return (nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare)context.get("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#690b82");
  nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare();
  context.put("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#690b82",bean);
  bean.setBillListDataPrepares(getManagedList2());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList2(){  List list = new ArrayList();  list.add(getUserdefitemlistPreparator());  list.add(getMarAsstPreparator());  return list;}

public nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor getListViewShowUpComponentInterceptor(){
 if(context.get("listViewShowUpComponentInterceptor")!=null)
 return (nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor)context.get("listViewShowUpComponentInterceptor");
  nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor();
  context.put("listViewShowUpComponentInterceptor",bean);
  bean.setShowUpComponent(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.invp.balance.view.BalanceTemplateContainer getTemplateContainer(){
 if(context.get("templateContainer")!=null)
 return (nc.ui.invp.balance.view.BalanceTemplateContainer)context.get("templateContainer");
  nc.ui.invp.balance.view.BalanceTemplateContainer bean = new nc.ui.invp.balance.view.BalanceTemplateContainer();
  context.put("templateContainer",bean);
  bean.setContext(getContext());
  bean.load();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.query2.model.ModelDataManager getModelDataManager(){
 if(context.get("modelDataManager")!=null)
 return (nc.ui.pubapp.uif2app.query2.model.ModelDataManager)context.get("modelDataManager");
  nc.ui.pubapp.uif2app.query2.model.ModelDataManager bean = new nc.ui.pubapp.uif2app.query2.model.ModelDataManager();
  context.put("modelDataManager",bean);
  bean.setModel(getManageAppModel());
  bean.setService(getManageModelService());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.TangramContainer getContainer(){
 if(context.get("container")!=null)
 return (nc.ui.uif2.TangramContainer)context.get("container");
  nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
  context.put("container",bean);
  bean.setModel(getManageAppModel());
  bean.setTangramLayoutRoot(getTBNode_187b05());
  bean.setActions(getManagedList4());
  bean.setEditActions(getManagedList5());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_187b05(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#187b05")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#187b05");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#187b05",bean);
  bean.setShowMode("CardLayout");
  bean.setTabs(getManagedList3());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList3(){  List list = new ArrayList();  list.add(getCNode_1f4f4b5());  list.add(getCNode_d0d427());  return list;}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1f4f4b5(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1f4f4b5")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1f4f4b5");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1f4f4b5",bean);
  bean.setName(getI18nFB_72a9f8());
  bean.setComponent(getListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_72a9f8(){
 if(context.get("nc.ui.uif2.I18nFB#72a9f8")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#72a9f8");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#72a9f8",bean);  bean.setResDir("common");
  bean.setResId("UC001-0000107");
  bean.setDefaultValue("列表");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#72a9f8",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_d0d427(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#d0d427")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#d0d427");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#d0d427",bean);
  bean.setName(getI18nFB_eda87d());
  bean.setComponent(getBalanceTreeView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_eda87d(){
 if(context.get("nc.ui.uif2.I18nFB#eda87d")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#eda87d");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#eda87d",bean);  bean.setResDir("common");
  bean.setResId("UC001-0000107");
  bean.setDefaultValue("列表");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#eda87d",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList4(){  List list = new ArrayList();  list.add(getQueryAction());  list.add(getRefreshAction());  list.add(getSeparatorAction());  list.add(getGatherAction());  list.add(getCancelGatherAction());  list.add(getSeparatorAction());  list.add(getBalanceAction());  list.add(getSeparatorAction());  list.add(getGatherRuleAction());  list.add(getSeparatorAction());  list.add(getBalanceRuleAction());  list.add(getSeparatorAction());  list.add(getCancelBalanceAction());  list.add(getSeparatorAction());  list.add(getPrintGroup());  list.add(getSeparatorAction()); list.add(getAtpQueryAction());  list.add(getSeparatorAction()); return list;}

private List getManagedList5(){  List list = new ArrayList();  list.add(getConfirmAction());  list.add(getCancelAction());  list.add(getSeparatorAction());  list.add(getPrintBalanceGroup());  return list;}

public nc.ui.invp.balance.view.BalanceTreeView getBalanceTreeView(){
 if(context.get("balanceTreeView")!=null)
 return (nc.ui.invp.balance.view.BalanceTreeView)context.get("balanceTreeView");
  nc.ui.invp.balance.view.BalanceTreeView bean = new nc.ui.invp.balance.view.BalanceTreeView();
  context.put("balanceTreeView",bean);
  bean.setTemplateContainer(getTemplateContainer());
  bean.setModel(getBalancetreemodel());
  bean.setName("tree");
  bean.setUserdefitemListPreparator(getCompositeBillListDataPrepare_134a830());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare getCompositeBillListDataPrepare_134a830(){
 if(context.get("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#134a830")!=null)
 return (nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare)context.get("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#134a830");
  nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare();
  context.put("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#134a830",bean);
  bean.setBillListDataPrepares(getManagedList6());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList6(){  List list = new ArrayList();  list.add(getUserdefitemlistPreparator());  list.add(getMarAsstPreparator());  return list;}

public nc.ui.invp.balance.view.BalanceTreeModel getBalancetreemodel(){
 if(context.get("balancetreemodel")!=null)
 return (nc.ui.invp.balance.view.BalanceTreeModel)context.get("balancetreemodel");
  nc.ui.invp.balance.view.BalanceTreeModel bean = new nc.ui.invp.balance.view.BalanceTreeModel();
  context.put("balancetreemodel",bean);
  bean.setService(getManageModelService());
  bean.setContext(getContext());
  bean.setTreeCreateStrategy(getTreeCreateStrategy());
  bean.setBusinessObjectAdapterFactory(getBoadatorTreefactory());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.bd.meta.BDObjectTreeCreateStrategy getTreeCreateStrategy(){
 if(context.get("treeCreateStrategy")!=null)
 return (nc.vo.bd.meta.BDObjectTreeCreateStrategy)context.get("treeCreateStrategy");
  nc.vo.bd.meta.BDObjectTreeCreateStrategy bean = new nc.vo.bd.meta.BDObjectTreeCreateStrategy();
  context.put("treeCreateStrategy",bean);
  bean.setFactory(getBoadatorTreefactory());
  bean.setClassName("nc.vo.invp.result.entity.BalanceResultVO");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getQueryInfo(){
 if(context.get("queryInfo")!=null)
 return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)context.get("queryInfo");
  nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
  context.put("queryInfo",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.editor.QueryTemplateContainer getQueryTemplateContainer(){
 if(context.get("queryTemplateContainer")!=null)
 return (nc.ui.uif2.editor.QueryTemplateContainer)context.get("queryTemplateContainer");
  nc.ui.uif2.editor.QueryTemplateContainer bean = new nc.ui.uif2.editor.QueryTemplateContainer();
  context.put("queryTemplateContainer",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator getMarAsstPreparator(){
 if(context.get("marAsstPreparator")!=null)
 return (nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator)context.get("marAsstPreparator");
  nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator bean = new nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator();
  context.put("marAsstPreparator",bean);
  bean.setModel(getManageAppModel());
  bean.setContainer(getUserdefitemContainer());
  bean.setPrefix("vfree");
  bean.setMaterialField("cmaterialoid");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.userdefitem.UserDefItemContainer getUserdefitemContainer(){
 if(context.get("userdefitemContainer")!=null)
 return (nc.ui.uif2.userdefitem.UserDefItemContainer)context.get("userdefitemContainer");
  nc.ui.uif2.userdefitem.UserDefItemContainer bean = new nc.ui.uif2.userdefitem.UserDefItemContainer();
  context.put("userdefitemContainer",bean);
  bean.setContext(getContext());
  bean.setParams(getManagedList7());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList7(){  List list = new ArrayList();  list.add(getQueryParam_df95e());  list.add(getQueryParam_16eb675());  list.add(getQueryParam_18cd3a7());  list.add(getQueryParam_9b7fe4());  return list;}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_df95e(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#df95e")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#df95e");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#df95e",bean);
  bean.setMdfullname("invp.invp_balance_result");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_16eb675(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#16eb675")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#16eb675");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#16eb675",bean);
  bean.setMdfullname("pu.po_storereq");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_18cd3a7(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#18cd3a7")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#18cd3a7");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#18cd3a7",bean);
  bean.setMdfullname("pu.po_storereq_b");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.userdefitem.QueryParam getQueryParam_9b7fe4(){
 if(context.get("nc.ui.uif2.userdefitem.QueryParam#9b7fe4")!=null)
 return (nc.ui.uif2.userdefitem.QueryParam)context.get("nc.ui.uif2.userdefitem.QueryParam#9b7fe4");
  nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
  context.put("nc.ui.uif2.userdefitem.QueryParam#9b7fe4",bean);
  bean.setRulecode("materialassistant");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemlistPreparator(){
 if(context.get("userdefitemlistPreparator")!=null)
 return (nc.ui.uif2.editor.UserdefitemContainerListPreparator)context.get("userdefitemlistPreparator");
  nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
  context.put("userdefitemlistPreparator",bean);
  bean.setContainer(getUserdefitemContainer());
  bean.setParams(getManagedList8());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList8(){  List list = new ArrayList();  list.add(getUserdefQueryParam_e95e5b());  list.add(getUserdefQueryParam_131acef());  return list;}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_e95e5b(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#e95e5b")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#e95e5b");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#e95e5b",bean);
  bean.setMdfullname("pu.po_storereq");
  bean.setPos(0);
  bean.setPrefix("vdef");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_131acef(){
 if(context.get("nc.ui.uif2.editor.UserdefQueryParam#131acef")!=null)
 return (nc.ui.uif2.editor.UserdefQueryParam)context.get("nc.ui.uif2.editor.UserdefQueryParam#131acef");
  nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
  context.put("nc.ui.uif2.editor.UserdefQueryParam#131acef",bean);
  bean.setMdfullname("pu.po_storereq_b");
  bean.setPos(0);
  bean.setPrefix("vbdef");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

}
