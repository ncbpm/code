package nc.ui.hi.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class register_config extends AbstractJavaBeanDefinition{
	private Map<String, Object> context = new HashMap();
public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getFormEditorActions(){
 if(context.get("formEditorActions")!=null)
 return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer)context.get("formEditorActions");
  nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(getPsndocFormEditor());  context.put("formEditorActions",bean);
  bean.setActions(getManagedList0());
  bean.setEditActions(getManagedList1());
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList0(){  List list = new ArrayList();  list.add(getAddPsndocAction());  list.add(getEditPsndocAction());  list.add(getDeletePsndocAction());  list.add(getSeparatorAction());  list.add(getRefreshPsndocAction());  list.add(getSeparatorAction());  list.add(getNewEntryFormAction());  list.add(getSwitchToDocAction());  list.add(getSeparatorAction());  list.add(getCardAssistGroup());  list.add(getSeparatorAction());  list.add(getCardRelateQueryGroup());  list.add(getSeparatorAction());  list.add(getCardPrintActionGroup());  return list;}

private List getManagedList1(){  List list = new ArrayList();  list.add(getSavePsndocAction());  list.add(getSaveAddPsndocAction());  list.add(getSeparatorAction());  list.add(getCancelPsndocAction()); list.add(getReadIDCardAction()); return list;}

public nc.ui.hi.psndoc.view.PsndocActionContainer getListViewActions(){
 if(context.get("listViewActions")!=null)
 return (nc.ui.hi.psndoc.view.PsndocActionContainer)context.get("listViewActions");
  nc.ui.hi.psndoc.view.PsndocActionContainer bean = new nc.ui.hi.psndoc.view.PsndocActionContainer(getPsndocListView());  context.put("listViewActions",bean);
  bean.setActions(getManagedList2());
  bean.setDataManager(getPsndocDataManager());
  bean.setAdjustSortActions(getManagedList3());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList2(){  List list = new ArrayList();  list.add(getAddPsndocAction());   list.add(getEditActionGroup());  list.add(getDeletePsndocAction());  list.add(getSeparatorAction());  list.add(getQueryPsndocAction());  list.add(getRefreshPsndocAction());  list.add(getSeparatorAction());  list.add(getNewEntryFormAction());  list.add(getSwitchToDocAction());  list.add(getAssistGroup());  list.add(getSeparatorAction());  list.add(getRelateQueryGroup());  list.add(getSeparatorAction());  list.add(getPrintActionGroup());  return list;}

private List getManagedList3(){  List list = new ArrayList();  list.add(getSavePsndocAction());  list.add(getSeparatorAction());  list.add(getCancelPsndocAction());  return list;}

public nc.funcnode.ui.action.GroupAction getEditActionGroup(){
 if(context.get("editActionGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("editActionGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("editActionGroup",bean);
  bean.setActions(getManagedList4());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList4(){  List list = new ArrayList();  list.add(getEditPsndocAction());  list.add(getBatchEditAction());  return list;}

public java.lang.String getResourceCode(){
 if(context.get("resourceCode")!=null)
 return (java.lang.String)context.get("resourceCode");
  java.lang.String bean = new java.lang.String("6007psnjob");  context.put("resourceCode",bean);
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

public nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBarSeparator(){
 if(context.get("ActionsBarSeparator")!=null)
 return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator)context.get("ActionsBarSeparator");
  nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
  context.put("ActionsBarSeparator",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hr.uif2.action.MaxBodyViewAction getBodyMaxAction(){
 if(context.get("bodyMaxAction")!=null)
 return (nc.ui.hr.uif2.action.MaxBodyViewAction)context.get("bodyMaxAction");
  nc.ui.hr.uif2.action.MaxBodyViewAction bean = new nc.ui.hr.uif2.action.MaxBodyViewAction();
  context.put("bodyMaxAction",bean);
  bean.setModel(getManageAppModel());
  bean.setCardPanel(getPsndocFormEditor());
  bean.setDefaultStatus(true);
  bean.setEnableForAllTabs(true);
  bean.setEnableWhenEditingOnly(false);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hr.uif2.action.MaxHeadViewAction getHeadMaxAction(){
 if(context.get("headMaxAction")!=null)
 return (nc.ui.hr.uif2.action.MaxHeadViewAction)context.get("headMaxAction");
  nc.ui.hr.uif2.action.MaxHeadViewAction bean = new nc.ui.hr.uif2.action.MaxHeadViewAction();
  context.put("headMaxAction",bean);
  bean.setModel(getManageAppModel());
  bean.setCardPanel(getPsndocFormEditor());
  bean.setDefaultStatus(true);
  bean.setEnableForAllTabs(true);
  bean.setEnableWhenEditingOnly(false);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.AddPsndocAction getAddPsndocAction(){
 if(context.get("addPsndocAction")!=null)
 return (nc.ui.hi.psndoc.action.AddPsndocAction)context.get("addPsndocAction");
  nc.ui.hi.psndoc.action.AddPsndocAction bean = new nc.ui.hi.psndoc.action.AddPsndocAction();
  context.put("addPsndocAction",bean);
  bean.setModel(getManageAppModel());
  bean.setFormEditor(getPsndocFormEditor());
  bean.setIdValidator(getIDvalidationConfig());
  bean.setDefaultValueProvider(getDefaultValueProvider());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.ReadIDCardAction getReadIDCardAction(){
	 if(context.get("readIDCardAction")!=null)
	 return (nc.ui.hi.psndoc.action.ReadIDCardAction)context.get("readIDCardAction");
	  nc.ui.hi.psndoc.action.ReadIDCardAction bean = new nc.ui.hi.psndoc.action.ReadIDCardAction();
	  context.put("readIDCardAction",bean);
	  bean.setModel(getManageAppModel());
	  bean.setFormEditor(getPsndocFormEditor());
	setBeanFacotryIfBeanFacatoryAware(bean);
	invokeInitializingBean(bean);
	return bean;
	}

public nc.ui.hi.psndoc.action.EditPsndocAction getEditPsndocAction(){
 if(context.get("editPsndocAction")!=null)
 return (nc.ui.hi.psndoc.action.EditPsndocAction)context.get("editPsndocAction");
  nc.ui.hi.psndoc.action.EditPsndocAction bean = new nc.ui.hi.psndoc.action.EditPsndocAction();
  context.put("editPsndocAction",bean);
  bean.setModel(getManageAppModel());
  bean.setResourceCode(getResourceCode());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.DeletePsndocAction getDeletePsndocAction(){
 if(context.get("deletePsndocAction")!=null)
 return (nc.ui.hi.psndoc.action.DeletePsndocAction)context.get("deletePsndocAction");
  nc.ui.hi.psndoc.action.DeletePsndocAction bean = new nc.ui.hi.psndoc.action.DeletePsndocAction();
  context.put("deletePsndocAction",bean);
  bean.setModel(getManageAppModel());
  bean.setListView(getPsndocListView());
  bean.setResourceCode(getResourceCode());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.QueryPsndocAction getQueryPsndocAction(){
 if(context.get("queryPsndocAction")!=null)
 return (nc.ui.hi.psndoc.action.QueryPsndocAction)context.get("queryPsndocAction");
  nc.ui.hi.psndoc.action.QueryPsndocAction bean = new nc.ui.hi.psndoc.action.QueryPsndocAction();
  context.put("queryPsndocAction",bean);
  bean.setModel(getManageAppModel());
  bean.setDataManager(getPsndocDataManager());
  bean.setQueryDelegator(getQueryPsndocDelegator_1ca2e3());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.hi.psndoc.action.QueryPsndocDelegator getQueryPsndocDelegator_1ca2e3(){
 if(context.get("nc.ui.hi.psndoc.action.QueryPsndocDelegator#1ca2e3")!=null)
 return (nc.ui.hi.psndoc.action.QueryPsndocDelegator)context.get("nc.ui.hi.psndoc.action.QueryPsndocDelegator#1ca2e3");
  nc.ui.hi.psndoc.action.QueryPsndocDelegator bean = new nc.ui.hi.psndoc.action.QueryPsndocDelegator();
  context.put("nc.ui.hi.psndoc.action.QueryPsndocDelegator#1ca2e3",bean);
  bean.setContext(getContext());
  bean.setNodeKey("bd_psndoc");
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.RefreshPsndocAction getRefreshPsndocAction(){
 if(context.get("refreshPsndocAction")!=null)
 return (nc.ui.hi.psndoc.action.RefreshPsndocAction)context.get("refreshPsndocAction");
  nc.ui.hi.psndoc.action.RefreshPsndocAction bean = new nc.ui.hi.psndoc.action.RefreshPsndocAction();
  context.put("refreshPsndocAction",bean);
  bean.setModel(getManageAppModel());
  bean.setDataManager(getPsndocDataManager());
  bean.setFormEditor(getPsndocFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.SavePsndocAction getSavePsndocAction(){
 if(context.get("savePsndocAction")!=null)
 return (nc.ui.hi.psndoc.action.SavePsndocAction)context.get("savePsndocAction");
  nc.ui.hi.psndoc.action.SavePsndocAction bean = new nc.ui.hi.psndoc.action.SavePsndocAction();
  context.put("savePsndocAction",bean);
  bean.setModel(getManageAppModel());
  bean.setEditor(getPsndocFormEditor());
  bean.setListView(getPsndocListView());
  bean.setDataManager(getPsndocDataManager());
  bean.setSuperValidator(getSuperValidationConfig());
  bean.setValidationService(getBillNotNullValidator());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hr.uif2.action.SaveAddAction getSaveAddPsndocAction(){
 if(context.get("saveAddPsndocAction")!=null)
 return (nc.ui.hr.uif2.action.SaveAddAction)context.get("saveAddPsndocAction");
  nc.ui.hr.uif2.action.SaveAddAction bean = new nc.ui.hr.uif2.action.SaveAddAction();
  context.put("saveAddPsndocAction",bean);
  bean.setModel(getManageAppModel());
  bean.setAddAction(getAddPsndocAction());
  bean.setSaveAction(getSavePsndocAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.CancelPsndocAction getCancelPsndocAction(){
 if(context.get("cancelPsndocAction")!=null)
 return (nc.ui.hi.psndoc.action.CancelPsndocAction)context.get("cancelPsndocAction");
  nc.ui.hi.psndoc.action.CancelPsndocAction bean = new nc.ui.hi.psndoc.action.CancelPsndocAction();
  context.put("cancelPsndocAction",bean);
  bean.setModel(getManageAppModel());
  bean.setDataManger(getPsndocDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.AddSubSetAction getAddSubSetAction(){
 if(context.get("addSubSetAction")!=null)
 return (nc.ui.hi.psndoc.action.AddSubSetAction)context.get("addSubSetAction");
  nc.ui.hi.psndoc.action.AddSubSetAction bean = new nc.ui.hi.psndoc.action.AddSubSetAction();
  context.put("addSubSetAction",bean);
  bean.setModel(getManageAppModel());
  bean.setCardPanel(getPsndocFormEditor());
  bean.setDefaultStatus(true);
  bean.setEnableForAllTabs(false);
  bean.setDisableTabsSet(getDisableTabSet());
  bean.setDefaultValueProvider(getSubDefaultValueProvider());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.DelSubSetAction getDeleteSubSetAction(){
 if(context.get("deleteSubSetAction")!=null)
 return (nc.ui.hi.psndoc.action.DelSubSetAction)context.get("deleteSubSetAction");
  nc.ui.hi.psndoc.action.DelSubSetAction bean = new nc.ui.hi.psndoc.action.DelSubSetAction();
  context.put("deleteSubSetAction",bean);
  bean.setModel(getManageAppModel());
  bean.setCardPanel(getPsndocFormEditor());
  bean.setDefaultStatus(true);
  bean.setEnableForAllTabs(false);
  bean.setDisableTabsSet(getDisableTabSet());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.InsertSubSetAction getInsertSubSetAction(){
 if(context.get("insertSubSetAction")!=null)
 return (nc.ui.hi.psndoc.action.InsertSubSetAction)context.get("insertSubSetAction");
  nc.ui.hi.psndoc.action.InsertSubSetAction bean = new nc.ui.hi.psndoc.action.InsertSubSetAction();
  context.put("insertSubSetAction",bean);
  bean.setModel(getManageAppModel());
  bean.setCardPanel(getPsndocFormEditor());
  bean.setDefaultStatus(true);
  bean.setEnableForAllTabs(false);
  bean.setDisableTabsSet(getDisableTabSet());
  bean.setDefaultValueProvider(getSubDefaultValueProvider());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.CopySubSetAction getCopySubSetAction(){
 if(context.get("copySubSetAction")!=null)
 return (nc.ui.hi.psndoc.action.CopySubSetAction)context.get("copySubSetAction");
  nc.ui.hi.psndoc.action.CopySubSetAction bean = new nc.ui.hi.psndoc.action.CopySubSetAction();
  context.put("copySubSetAction",bean);
  bean.setModel(getManageAppModel());
  bean.setCardPanel(getPsndocFormEditor());
  bean.setDefaultStatus(true);
  bean.setEnableForAllTabs(false);
  bean.setDisableTabsSet(getDisableTabSet());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.PasteSubSetAction getPasteSubSetAction(){
 if(context.get("pasteSubSetAction")!=null)
 return (nc.ui.hi.psndoc.action.PasteSubSetAction)context.get("pasteSubSetAction");
  nc.ui.hi.psndoc.action.PasteSubSetAction bean = new nc.ui.hi.psndoc.action.PasteSubSetAction();
  context.put("pasteSubSetAction",bean);
  bean.setModel(getManageAppModel());
  bean.setCardPanel(getPsndocFormEditor());
  bean.setDefaultStatus(true);
  bean.setEnableForAllTabs(false);
  bean.setDisableTabsSet(getDisableTabSet());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.AdjustSubReordUpAction getAdjustSubReordUpAction(){
 if(context.get("adjustSubReordUpAction")!=null)
 return (nc.ui.hi.psndoc.action.AdjustSubReordUpAction)context.get("adjustSubReordUpAction");
  nc.ui.hi.psndoc.action.AdjustSubReordUpAction bean = new nc.ui.hi.psndoc.action.AdjustSubReordUpAction();
  context.put("adjustSubReordUpAction",bean);
  bean.setModel(getManageAppModel());
  bean.setFormEditor(getPsndocFormEditor());
  bean.setDefaultStatus(true);
  bean.setEnableForAllTabs(false);
  bean.setDisableTabsSet(getBusinessInfoSet());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.AdjustSubReordDownAction getAdjustSubReordDownAction(){
 if(context.get("adjustSubReordDownAction")!=null)
 return (nc.ui.hi.psndoc.action.AdjustSubReordDownAction)context.get("adjustSubReordDownAction");
  nc.ui.hi.psndoc.action.AdjustSubReordDownAction bean = new nc.ui.hi.psndoc.action.AdjustSubReordDownAction();
  context.put("adjustSubReordDownAction",bean);
  bean.setModel(getManageAppModel());
  bean.setFormEditor(getPsndocFormEditor());
  bean.setDefaultStatus(true);
  bean.setEnableForAllTabs(false);
  bean.setDisableTabsSet(getBusinessInfoSet());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.FirstPsndocAction getFirstLineAction(){
 if(context.get("firstLineAction")!=null)
 return (nc.ui.hi.psndoc.action.FirstPsndocAction)context.get("firstLineAction");
  nc.ui.hi.psndoc.action.FirstPsndocAction bean = new nc.ui.hi.psndoc.action.FirstPsndocAction();
  context.put("firstLineAction",bean);
  bean.setModel(getManageAppModel());
  bean.setFormEditor(getPsndocFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.PrePsndocAction getPreLineAction(){
 if(context.get("preLineAction")!=null)
 return (nc.ui.hi.psndoc.action.PrePsndocAction)context.get("preLineAction");
  nc.ui.hi.psndoc.action.PrePsndocAction bean = new nc.ui.hi.psndoc.action.PrePsndocAction();
  context.put("preLineAction",bean);
  bean.setModel(getManageAppModel());
  bean.setFormEditor(getPsndocFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.NextPsndocAction getNextLineAction(){
 if(context.get("nextLineAction")!=null)
 return (nc.ui.hi.psndoc.action.NextPsndocAction)context.get("nextLineAction");
  nc.ui.hi.psndoc.action.NextPsndocAction bean = new nc.ui.hi.psndoc.action.NextPsndocAction();
  context.put("nextLineAction",bean);
  bean.setModel(getManageAppModel());
  bean.setFormEditor(getPsndocFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.LastPsndocAction getLastLineAction(){
 if(context.get("lastLineAction")!=null)
 return (nc.ui.hi.psndoc.action.LastPsndocAction)context.get("lastLineAction");
  nc.ui.hi.psndoc.action.LastPsndocAction bean = new nc.ui.hi.psndoc.action.LastPsndocAction();
  context.put("lastLineAction",bean);
  bean.setModel(getManageAppModel());
  bean.setFormEditor(getPsndocFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.BatchEditPsndocAction getBatchEditAction(){
 if(context.get("batchEditAction")!=null)
 return (nc.ui.hi.psndoc.action.BatchEditPsndocAction)context.get("batchEditAction");
  nc.ui.hi.psndoc.action.BatchEditPsndocAction bean = new nc.ui.hi.psndoc.action.BatchEditPsndocAction();
  context.put("batchEditAction",bean);
  bean.setModel(getManageAppModel());
  bean.setFormEditor(getPsndocFormEditor());
  bean.setListView(getPsndocListView());
  bean.setResourceCode(getResourceCode());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.BatchAddSubSetAction getBatchAddSubSetAction(){
 if(context.get("batchAddSubSetAction")!=null)
 return (nc.ui.hi.psndoc.action.BatchAddSubSetAction)context.get("batchAddSubSetAction");
  nc.ui.hi.psndoc.action.BatchAddSubSetAction bean = new nc.ui.hi.psndoc.action.BatchAddSubSetAction();
  context.put("batchAddSubSetAction",bean);
  bean.setModel(getManageAppModel());
  bean.setListView(getPsndocListView());
  bean.setResourceCode(getResourceCode());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.SortPsndocAction getSortPsndocAction(){
 if(context.get("sortPsndocAction")!=null)
 return (nc.ui.hi.psndoc.action.SortPsndocAction)context.get("sortPsndocAction");
  nc.ui.hi.psndoc.action.SortPsndocAction bean = new nc.ui.hi.psndoc.action.SortPsndocAction();
  context.put("sortPsndocAction",bean);
  bean.setModel(getManageAppModel());
  bean.setListView(getPsndocListView());
  bean.setDataManger(getPsndocDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.AdjustPsndocSortAction getAdjustSortAction(){
 if(context.get("adjustSortAction")!=null)
 return (nc.ui.hi.psndoc.action.AdjustPsndocSortAction)context.get("adjustSortAction");
  nc.ui.hi.psndoc.action.AdjustPsndocSortAction bean = new nc.ui.hi.psndoc.action.AdjustPsndocSortAction();
  context.put("adjustSortAction",bean);
  bean.setModel(getManageAppModel());
  bean.setListView(getPsndocListView());
  bean.setDataManger(getPsndocDataManager());
  bean.setTreeView(getTreeContainer());
  bean.setResourceCode(getResourceCode());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.register.action.SwitchToDocAction getSwitchToDocAction(){
 if(context.get("switchToDocAction")!=null)
 return (nc.ui.hi.register.action.SwitchToDocAction)context.get("switchToDocAction");
  nc.ui.hi.register.action.SwitchToDocAction bean = new nc.ui.hi.register.action.SwitchToDocAction();
  context.put("switchToDocAction",bean);
  bean.setModel(getManageAppModel());
  bean.setFormEditor(getPsndocFormEditor());
  bean.setResourceCode(getResourceCode());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.register.action.NewEntryFormAction getNewEntryFormAction(){
 if(context.get("newEntryFormAction")!=null)
 return (nc.ui.hi.register.action.NewEntryFormAction)context.get("newEntryFormAction");
  nc.ui.hi.register.action.NewEntryFormAction bean = new nc.ui.hi.register.action.NewEntryFormAction();
  context.put("newEntryFormAction",bean);
  bean.setModel(getManageAppModel());
  bean.setFormEditor(getPsndocFormEditor());
  bean.setResourceCode(getResourceCode());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.GroupAction getPrintActionGroup(){
 if(context.get("printActionGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("printActionGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("printActionGroup",bean);
  bean.setActions(getManagedList5());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList5(){  List list = new ArrayList();  list.add(getPrintDirectAction());  list.add(getPrintPreviewAction());  list.add(getListOutputAction());  list.add(getSeparatorAction());  list.add(getTemplatePrintAction());  list.add(getTemplatePreviewAction());  return list;}

public nc.funcnode.ui.action.GroupAction getCardPrintActionGroup(){
 if(context.get("cardPrintActionGroup")!=null)
 return (nc.funcnode.ui.action.GroupAction)context.get("cardPrintActionGroup");
  nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
  context.put("cardPrintActionGroup",bean);
  bean.setActions(getManagedList6());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList6(){  List list = new ArrayList();  list.add(getTemplatePrintAction());  list.add(getTemplatePreviewAction());  list.add(getCardOutputAction());  return list;}

public nc.ui.hr.uif2.action.print.DirectPreviewAction getPrintPreviewAction(){
 if(context.get("printPreviewAction")!=null)
 return (nc.ui.hr.uif2.action.print.DirectPreviewAction)context.get("printPreviewAction");
  nc.ui.hr.uif2.action.print.DirectPreviewAction bean = new nc.ui.hr.uif2.action.print.DirectPreviewAction();
  context.put("printPreviewAction",bean);
  bean.setModel(getManageAppModel());
  bean.setListView(getPsndocListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hr.uif2.action.print.DirectPrintAction getPrintDirectAction(){
 if(context.get("printDirectAction")!=null)
 return (nc.ui.hr.uif2.action.print.DirectPrintAction)context.get("printDirectAction");
  nc.ui.hr.uif2.action.print.DirectPrintAction bean = new nc.ui.hr.uif2.action.print.DirectPrintAction();
  context.put("printDirectAction",bean);
  bean.setModel(getManageAppModel());
  bean.setListView(getPsndocListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.ExportListPsndocAction getListOutputAction(){
 if(context.get("listOutputAction")!=null)
 return (nc.ui.hi.psndoc.action.ExportListPsndocAction)context.get("listOutputAction");
  nc.ui.hi.psndoc.action.ExportListPsndocAction bean = new nc.ui.hi.psndoc.action.ExportListPsndocAction();
  context.put("listOutputAction",bean);
  bean.setModel(getManageAppModel());
  bean.setListView(getPsndocListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.TemplatePreviewPsndocAction getTemplatePreviewAction(){
 if(context.get("templatePreviewAction")!=null)
 return (nc.ui.hi.psndoc.action.TemplatePreviewPsndocAction)context.get("templatePreviewAction");
  nc.ui.hi.psndoc.action.TemplatePreviewPsndocAction bean = new nc.ui.hi.psndoc.action.TemplatePreviewPsndocAction();
  context.put("templatePreviewAction",bean);
  bean.setModel(getManageAppModel());
  bean.setPrintDlgParentConatiner(getPsndocFormEditor());
  bean.setDatasource(getDatasource());
  bean.setNodeKey("bd_psndoc");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.TemplatePrintPsndocAction getTemplatePrintAction(){
 if(context.get("templatePrintAction")!=null)
 return (nc.ui.hi.psndoc.action.TemplatePrintPsndocAction)context.get("templatePrintAction");
  nc.ui.hi.psndoc.action.TemplatePrintPsndocAction bean = new nc.ui.hi.psndoc.action.TemplatePrintPsndocAction();
  context.put("templatePrintAction",bean);
  bean.setModel(getManageAppModel());
  bean.setPrintDlgParentConatiner(getPsndocFormEditor());
  bean.setDatasource(getDatasource());
  bean.setNodeKey("bd_psndoc");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.ExportCardPsndocAction getCardOutputAction(){
 if(context.get("cardOutputAction")!=null)
 return (nc.ui.hi.psndoc.action.ExportCardPsndocAction)context.get("cardOutputAction");
  nc.ui.hi.psndoc.action.ExportCardPsndocAction bean = new nc.ui.hi.psndoc.action.ExportCardPsndocAction();
  context.put("cardOutputAction",bean);
  bean.setModel(getManageAppModel());
  bean.setPrintDlgParentConatiner(getPsndocFormEditor());
  bean.setDatasource(getDatasource());
  bean.setNodeKey("bd_psndoc");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.HIMetaDataDataSource getDatasource(){
 if(context.get("datasource")!=null)
 return (nc.ui.hi.psndoc.action.HIMetaDataDataSource)context.get("datasource");
  nc.ui.hi.psndoc.action.HIMetaDataDataSource bean = new nc.ui.hi.psndoc.action.HIMetaDataDataSource();
  context.put("datasource",bean);
  bean.setModel(getManageAppModel());
  bean.setSingleData(true);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.MenuAction getRelateQueryGroup(){
 if(context.get("relateQueryGroup")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("relateQueryGroup");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("relateQueryGroup",bean);
  bean.setCode("relateQuery");
  bean.setName(getI18nFB_a20a3());
  bean.setActions(getManagedList7());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_a20a3(){
 if(context.get("nc.ui.uif2.I18nFB#a20a3")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#a20a3");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#a20a3",bean);  bean.setResDir("6001uif2");
  bean.setResId("x6001uif20002");
  bean.setDefaultValue("联查");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#a20a3",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList7(){  List list = new ArrayList();  list.add(getQueryCardReptAction());  list.add(getQueryListReptAction());  return list;}

public nc.funcnode.ui.action.MenuAction getCardRelateQueryGroup(){
 if(context.get("cardRelateQueryGroup")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("cardRelateQueryGroup");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("cardRelateQueryGroup",bean);
  bean.setCode("relateQuery");
  bean.setName(getI18nFB_e4af67());
  bean.setActions(getManagedList8());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_e4af67(){
 if(context.get("nc.ui.uif2.I18nFB#e4af67")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#e4af67");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#e4af67",bean);  bean.setResDir("6001uif2");
  bean.setResId("x6001uif20002");
  bean.setDefaultValue("联查");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#e4af67",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList8(){  List list = new ArrayList();  list.add(getQueryCardReptAction());  return list;}

public nc.ui.hi.psndoc.action.QueryCardReptAction getQueryCardReptAction(){
 if(context.get("queryCardReptAction")!=null)
 return (nc.ui.hi.psndoc.action.QueryCardReptAction)context.get("queryCardReptAction");
  nc.ui.hi.psndoc.action.QueryCardReptAction bean = new nc.ui.hi.psndoc.action.QueryCardReptAction();
  context.put("queryCardReptAction",bean);
  bean.setModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.QueryListReptAction getQueryListReptAction(){
 if(context.get("queryListReptAction")!=null)
 return (nc.ui.hi.psndoc.action.QueryListReptAction)context.get("queryListReptAction");
  nc.ui.hi.psndoc.action.QueryListReptAction bean = new nc.ui.hi.psndoc.action.QueryListReptAction();
  context.put("queryListReptAction",bean);
  bean.setModel(getManageAppModel());
  bean.setDataManager(getPsndocDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.HIFileManageAction getAttachmentAction(){
 if(context.get("attachmentAction")!=null)
 return (nc.ui.hi.psndoc.action.HIFileManageAction)context.get("attachmentAction");
  nc.ui.hi.psndoc.action.HIFileManageAction bean = new nc.ui.hi.psndoc.action.HIFileManageAction();
  context.put("attachmentAction",bean);
  bean.setModel(getManageAppModel());
  bean.setResourceCode(getResourceCode());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.HIFileManageAction getFileAction(){
 if(context.get("fileAction")!=null)
 return (nc.ui.hi.psndoc.action.HIFileManageAction)context.get("fileAction");
  nc.ui.hi.psndoc.action.HIFileManageAction bean = new nc.ui.hi.psndoc.action.HIFileManageAction();
  context.put("fileAction",bean);
  bean.setModel(getManageAppModel());
  bean.setToolBarVisible(false);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.action.ExportPhotoAction getExportPhotoAction(){
 if(context.get("exportPhotoAction")!=null)
 return (nc.ui.hi.psndoc.action.ExportPhotoAction)context.get("exportPhotoAction");
  nc.ui.hi.psndoc.action.ExportPhotoAction bean = new nc.ui.hi.psndoc.action.ExportPhotoAction();
  context.put("exportPhotoAction",bean);
  bean.setModel(getManageAppModel());
  bean.setListView(getPsndocListView());
  bean.setResourceCode(getResourceCode());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.funcnode.ui.action.MenuAction getAssistGroup(){
 if(context.get("assistGroup")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("assistGroup");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("assistGroup",bean);
  bean.setCode("assist");
  bean.setName(getI18nFB_1b7ae52());
  bean.setActions(getManagedList9());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1b7ae52(){
 if(context.get("nc.ui.uif2.I18nFB#1b7ae52")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1b7ae52");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1b7ae52",bean);  bean.setResDir("6001uif2");
  bean.setResId("x6001uif20001");
  bean.setDefaultValue("辅助功能");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1b7ae52",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList9(){  List list = new ArrayList();  list.add(getSortPsndocAction());  list.add(getAdjustSortAction());  list.add(getSeparatorAction());  list.add(getAttachmentAction());  list.add(getBatchAddSubSetAction());  return list;}

public nc.funcnode.ui.action.MenuAction getCardAssistGroup(){
 if(context.get("cardAssistGroup")!=null)
 return (nc.funcnode.ui.action.MenuAction)context.get("cardAssistGroup");
  nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
  context.put("cardAssistGroup",bean);
  bean.setCode("assist");
  bean.setName(getI18nFB_159294());
  bean.setActions(getManagedList10());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_159294(){
 if(context.get("nc.ui.uif2.I18nFB#159294")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#159294");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#159294",bean);  bean.setResDir("6001uif2");
  bean.setResId("x6001uif20001");
  bean.setDefaultValue("辅助功能");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#159294",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList10(){  List list = new ArrayList();  list.add(getExportPhotoAction());  list.add(getSeparatorAction());  list.add(getAttachmentAction());  return list;}

public nc.vo.hr.validator.IDFieldValidatorConfig getIDvalidationConfig(){
 if(context.get("IDvalidationConfig")!=null)
 return (nc.vo.hr.validator.IDFieldValidatorConfig)context.get("IDvalidationConfig");
  nc.vo.hr.validator.IDFieldValidatorConfig bean = new nc.vo.hr.validator.IDFieldValidatorConfig();
  context.put("IDvalidationConfig",bean);
  bean.setIdValidator(getManagedMap0());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap0(){  Map map = new HashMap();  map.put("0","nc.vo.hr.validator.IDCardValidator");  map.put("1","nc.vo.hr.validator.PassPortValidator");  map.put("3","nc.vo.hr.validator.HKIDCardValidator");  return map;}

public nc.ui.hr.uif2.validator.BillNotNullValidateService getBillNotNullValidator(){
 if(context.get("billNotNullValidator")!=null)
 return (nc.ui.hr.uif2.validator.BillNotNullValidateService)context.get("billNotNullValidator");
  nc.ui.hr.uif2.validator.BillNotNullValidateService bean = new nc.ui.hr.uif2.validator.BillNotNullValidateService(getPsndocFormEditor());  context.put("billNotNullValidator",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hr.tools.supervalidator.SuperFormEditorValidatorUtil getSuperValidationConfig(){
 if(context.get("SuperValidationConfig")!=null)
 return (nc.ui.hr.tools.supervalidator.SuperFormEditorValidatorUtil)context.get("SuperValidationConfig");
  nc.ui.hr.tools.supervalidator.SuperFormEditorValidatorUtil bean = new nc.ui.hr.tools.supervalidator.SuperFormEditorValidatorUtil();
  context.put("SuperValidationConfig",bean);
  bean.setFormeditor(getPsndocFormEditor());
  bean.setFieldRelationMap(getManagedMap1());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private Map getManagedMap1(){  Map map = new HashMap();  map.put("additionalValidationOfSave",getAdditionalValidationOfSave());  return map;}

public nc.ui.hr.tools.uilogic.SuperLogicProcessor getAdditionalValidationOfSave(){
 if(context.get("additionalValidationOfSave")!=null)
 return (nc.ui.hr.tools.uilogic.SuperLogicProcessor)context.get("additionalValidationOfSave");
  nc.ui.hr.tools.uilogic.SuperLogicProcessor bean = new nc.ui.hr.tools.uilogic.SuperLogicProcessor();
  context.put("additionalValidationOfSave",bean);
  bean.setFormeditor(getPsndocFormEditor());
  bean.setMethods(getManagedList11());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList11(){  List list = new ArrayList();  return list;}

public nc.vo.uif2.LoginContext getContext(){
 if(context.get("context")!=null)
 return (nc.vo.uif2.LoginContext)context.get("context");
  nc.vo.uif2.LoginContext bean = new nc.vo.uif2.LoginContext();
  context.put("context",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.bd.meta.BDObjectAdpaterFactory getBoadatorfactory(){
 if(context.get("boadatorfactory")!=null)
 return (nc.vo.bd.meta.BDObjectAdpaterFactory)context.get("boadatorfactory");
  nc.vo.bd.meta.BDObjectAdpaterFactory bean = new nc.vo.bd.meta.BDObjectAdpaterFactory();
  context.put("boadatorfactory",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.om.psnnavi.model.NaviTreeAppModelDataManager getLeftModelDataManager(){
 if(context.get("leftModelDataManager")!=null)
 return (nc.ui.om.psnnavi.model.NaviTreeAppModelDataManager)context.get("leftModelDataManager");
  nc.ui.om.psnnavi.model.NaviTreeAppModelDataManager bean = new nc.ui.om.psnnavi.model.NaviTreeAppModelDataManager();
  context.put("leftModelDataManager",bean);
  bean.setModel(getLeftSuperModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.om.psnnavi.model.NaviLeftAppModel getLeftSuperModel(){
 if(context.get("leftSuperModel")!=null)
 return (nc.ui.om.psnnavi.model.NaviLeftAppModel)context.get("leftSuperModel");
  nc.ui.om.psnnavi.model.NaviLeftAppModel bean = new nc.ui.om.psnnavi.model.NaviLeftAppModel();
  context.put("leftSuperModel",bean);
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.om.psnnavi.NaviPropertyVO getNaviProperty(){
 if(context.get("naviProperty")!=null)
 return (nc.vo.om.psnnavi.NaviPropertyVO)context.get("naviProperty");
  nc.vo.om.psnnavi.NaviPropertyVO bean = new nc.vo.om.psnnavi.NaviPropertyVO();
  context.put("naviProperty",bean);
  bean.setIncludeChildHR(true);
  bean.setOtherNodeNameMsAOS(getI18nFB_206097());
  bean.setNaviItems(getManagedList12());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_206097(){
 if(context.get("nc.ui.uif2.I18nFB#206097")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#206097");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#206097",bean);  bean.setResDir("6007psn");
  bean.setResId("x6007psn0001");
  bean.setDefaultValue("受托管理人员");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#206097",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

private List getManagedList12(){  List list = new ArrayList();  list.add("navi_style_msaos");  return list;}

public nc.ui.hr.uif2.model.HrDefaultAppModelService getTreeModelServiceMsAOS(){
 if(context.get("treeModelServiceMsAOS")!=null)
 return (nc.ui.hr.uif2.model.HrDefaultAppModelService)context.get("treeModelServiceMsAOS");
  nc.ui.hr.uif2.model.HrDefaultAppModelService bean = new nc.ui.hr.uif2.model.HrDefaultAppModelService();
  context.put("treeModelServiceMsAOS",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hr.uif2.model.HrDefaultAppModelService getTreeModelServiceOrg(){
 if(context.get("treeModelServiceOrg")!=null)
 return (nc.ui.hr.uif2.model.HrDefaultAppModelService)context.get("treeModelServiceOrg");
  nc.ui.hr.uif2.model.HrDefaultAppModelService bean = new nc.ui.hr.uif2.model.HrDefaultAppModelService();
  context.put("treeModelServiceOrg",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.om.psnnavi.lazytree.model.NaviTreeAppModelService getRefreshServiceOrg(){
 if(context.get("refreshServiceOrg")!=null)
 return (nc.ui.om.psnnavi.lazytree.model.NaviTreeAppModelService)context.get("refreshServiceOrg");
  nc.ui.om.psnnavi.lazytree.model.NaviTreeAppModelService bean = new nc.ui.om.psnnavi.lazytree.model.NaviTreeAppModelService();
  context.put("refreshServiceOrg",bean);
  bean.setAosTreeModel(getTreeAppModelOrg());
  bean.setRightModel(getManageAppModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.om.psnnavi.lazytree.model.MsAOSNaviTreeAppModelService getRefreshServiceMsAOS(){
 if(context.get("refreshServiceMsAOS")!=null)
 return (nc.ui.om.psnnavi.lazytree.model.MsAOSNaviTreeAppModelService)context.get("refreshServiceMsAOS");
  nc.ui.om.psnnavi.lazytree.model.MsAOSNaviTreeAppModelService bean = new nc.ui.om.psnnavi.lazytree.model.MsAOSNaviTreeAppModelService();
  context.put("refreshServiceMsAOS",bean);
  bean.setAosTreeModel(getTreeAppModelMsAOS());
  bean.setRightModel(getManageAppModel());
  bean.setNaviProperty(getNaviProperty());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hr.uif2.model.HrDefaultAppModelService getTreeModelServicePsnType(){
 if(context.get("treeModelServicePsnType")!=null)
 return (nc.ui.hr.uif2.model.HrDefaultAppModelService)context.get("treeModelServicePsnType");
  nc.ui.hr.uif2.model.HrDefaultAppModelService bean = new nc.ui.hr.uif2.model.HrDefaultAppModelService();
  context.put("treeModelServicePsnType",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hr.uif2.model.HrDefaultAppModelService getTreeModelServicePostSeries(){
 if(context.get("treeModelServicePostSeries")!=null)
 return (nc.ui.hr.uif2.model.HrDefaultAppModelService)context.get("treeModelServicePostSeries");
  nc.ui.hr.uif2.model.HrDefaultAppModelService bean = new nc.ui.hr.uif2.model.HrDefaultAppModelService();
  context.put("treeModelServicePostSeries",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.om.psnnavi.lazytree.view.AOSNaviLazyTreeCreateStrategy getTreeCreateStrategyOrg(){
 if(context.get("treeCreateStrategyOrg")!=null)
 return (nc.ui.om.psnnavi.lazytree.view.AOSNaviLazyTreeCreateStrategy)context.get("treeCreateStrategyOrg");
  nc.ui.om.psnnavi.lazytree.view.AOSNaviLazyTreeCreateStrategy bean = new nc.ui.om.psnnavi.lazytree.view.AOSNaviLazyTreeCreateStrategy();
  context.put("treeCreateStrategyOrg",bean);
  bean.setFactory(getBoadatorfactory());
  bean.setRootName(getI18nFB_a22911());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_a22911(){
 if(context.get("nc.ui.uif2.I18nFB#a22911")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#a22911");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#a22911",bean);  bean.setResDir("menucode");
  bean.setDefaultValue("行政组织");
  bean.setResId("X600531");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#a22911",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.om.psnnavi.lazytree.model.AOSNaviAppModel getTreeAppModelOrg(){
 if(context.get("treeAppModelOrg")!=null)
 return (nc.ui.om.psnnavi.lazytree.model.AOSNaviAppModel)context.get("treeAppModelOrg");
  nc.ui.om.psnnavi.lazytree.model.AOSNaviAppModel bean = new nc.ui.om.psnnavi.lazytree.model.AOSNaviAppModel();
  context.put("treeAppModelOrg",bean);
  bean.setService(getTreeModelServiceOrg());
  bean.setTreeCreateStrategy(getTreeCreateStrategyOrg());
  bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
  bean.setContext(getContext());
  bean.setRefreshService(getRefreshServiceOrg());
  bean.setNaviProperty(getNaviProperty());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.om.psnnavi.lazytree.view.MsAOSNaviLazyTreeCreateStrategy getTreeCreateStrategyMsAOS(){
 if(context.get("treeCreateStrategyMsAOS")!=null)
 return (nc.ui.om.psnnavi.lazytree.view.MsAOSNaviLazyTreeCreateStrategy)context.get("treeCreateStrategyMsAOS");
  nc.ui.om.psnnavi.lazytree.view.MsAOSNaviLazyTreeCreateStrategy bean = new nc.ui.om.psnnavi.lazytree.view.MsAOSNaviLazyTreeCreateStrategy();
  context.put("treeCreateStrategyMsAOS",bean);
  bean.setFactory(getBoadatorfactory());
  bean.setRootName(getI18nFB_363c3d());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_363c3d(){
 if(context.get("nc.ui.uif2.I18nFB#363c3d")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#363c3d");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#363c3d",bean);  bean.setResDir("menucode");
  bean.setDefaultValue("管理范围");
  bean.setResId("X600532");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#363c3d",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.om.psnnavi.lazytree.model.AOSNaviAppModel getTreeAppModelMsAOS(){
 if(context.get("treeAppModelMsAOS")!=null)
 return (nc.ui.om.psnnavi.lazytree.model.AOSNaviAppModel)context.get("treeAppModelMsAOS");
  nc.ui.om.psnnavi.lazytree.model.AOSNaviAppModel bean = new nc.ui.om.psnnavi.lazytree.model.AOSNaviAppModel();
  context.put("treeAppModelMsAOS",bean);
  bean.setService(getTreeModelServiceMsAOS());
  bean.setTreeCreateStrategy(getTreeCreateStrategyMsAOS());
  bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
  bean.setContext(getContext());
  bean.setRefreshService(getRefreshServiceMsAOS());
  bean.setNaviProperty(getNaviProperty());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.bd.meta.BDObjectTreeCreateStrategy getTreeCreateStrategyPsnType(){
 if(context.get("treeCreateStrategyPsnType")!=null)
 return (nc.vo.bd.meta.BDObjectTreeCreateStrategy)context.get("treeCreateStrategyPsnType");
  nc.vo.bd.meta.BDObjectTreeCreateStrategy bean = new nc.vo.bd.meta.BDObjectTreeCreateStrategy();
  context.put("treeCreateStrategyPsnType",bean);
  bean.setFactory(getBoadatorfactory());
  bean.setRootName(getI18nFB_acf503());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_acf503(){
 if(context.get("nc.ui.uif2.I18nFB#acf503")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#acf503");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#acf503",bean);  bean.setResDir("menucode");
  bean.setDefaultValue("人员类别");
  bean.setResId("X600533");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#acf503",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.uif2.model.HierachicalDataAppModel getTreeAppModelPsnType(){
 if(context.get("treeAppModelPsnType")!=null)
 return (nc.ui.uif2.model.HierachicalDataAppModel)context.get("treeAppModelPsnType");
  nc.ui.uif2.model.HierachicalDataAppModel bean = new nc.ui.uif2.model.HierachicalDataAppModel();
  context.put("treeAppModelPsnType",bean);
  bean.setService(getTreeModelServicePsnType());
  bean.setTreeCreateStrategy(getTreeCreateStrategyPsnType());
  bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.vo.bd.meta.BDObjectTreeCreateStrategy getTreeCreateStrategyPostSeries(){
 if(context.get("treeCreateStrategyPostSeries")!=null)
 return (nc.vo.bd.meta.BDObjectTreeCreateStrategy)context.get("treeCreateStrategyPostSeries");
  nc.vo.bd.meta.BDObjectTreeCreateStrategy bean = new nc.vo.bd.meta.BDObjectTreeCreateStrategy();
  context.put("treeCreateStrategyPostSeries",bean);
  bean.setFactory(getBoadatorfactory());
  bean.setRootName(getI18nFB_1099954());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private java.lang.String getI18nFB_1099954(){
 if(context.get("nc.ui.uif2.I18nFB#1099954")!=null)
 return (java.lang.String)context.get("nc.ui.uif2.I18nFB#1099954");
  nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
    context.put("&nc.ui.uif2.I18nFB#1099954",bean);  bean.setResDir("menucode");
  bean.setDefaultValue("岗位序列");
  bean.setResId("X600534");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
 try {
     Object product = bean.getObject();
    context.put("nc.ui.uif2.I18nFB#1099954",product);
     return (java.lang.String)product;
}
catch(Exception e) { throw new RuntimeException(e);}}

public nc.ui.uif2.model.HierachicalDataAppModel getTreeAppModelPostSeries(){
 if(context.get("treeAppModelPostSeries")!=null)
 return (nc.ui.uif2.model.HierachicalDataAppModel)context.get("treeAppModelPostSeries");
  nc.ui.uif2.model.HierachicalDataAppModel bean = new nc.ui.uif2.model.HierachicalDataAppModel();
  context.put("treeAppModelPostSeries",bean);
  bean.setService(getTreeModelServicePostSeries());
  bean.setTreeCreateStrategy(getTreeCreateStrategyPostSeries());
  bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
  bean.setContext(getContext());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.om.psnnavi.lazytree.view.TreePanel_Org getTreePanelOrg(){
 if(context.get("treePanelOrg")!=null)
 return (nc.ui.om.psnnavi.lazytree.view.TreePanel_Org)context.get("treePanelOrg");
  nc.ui.om.psnnavi.lazytree.view.TreePanel_Org bean = new nc.ui.om.psnnavi.lazytree.view.TreePanel_Org();
  context.put("treePanelOrg",bean);
  bean.setModel(getTreeAppModelOrg());
  bean.setLeftSuperModel(getLeftSuperModel());
  bean.setRightModel(getManageAppModel());
  bean.setNaviProperty(getNaviProperty());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.om.psnnavi.lazytree.view.TreePanelOfMsAOS getTreePanelMsAOS(){
 if(context.get("treePanelMsAOS")!=null)
 return (nc.ui.om.psnnavi.lazytree.view.TreePanelOfMsAOS)context.get("treePanelMsAOS");
  nc.ui.om.psnnavi.lazytree.view.TreePanelOfMsAOS bean = new nc.ui.om.psnnavi.lazytree.view.TreePanelOfMsAOS();
  context.put("treePanelMsAOS",bean);
  bean.setModel(getTreeAppModelMsAOS());
  bean.setLeftSuperModel(getLeftSuperModel());
  bean.setRightModel(getManageAppModel());
  bean.setNaviProperty(getNaviProperty());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.om.psnnavi.view.TreePanel_PsnType getTreePanelPsnType(){
 if(context.get("treePanelPsnType")!=null)
 return (nc.ui.om.psnnavi.view.TreePanel_PsnType)context.get("treePanelPsnType");
  nc.ui.om.psnnavi.view.TreePanel_PsnType bean = new nc.ui.om.psnnavi.view.TreePanel_PsnType();
  context.put("treePanelPsnType",bean);
  bean.setModel(getTreeAppModelPsnType());
  bean.setLeftSuperModel(getLeftSuperModel());
  bean.setRightModel(getManageAppModel());
  bean.setNaviProperty(getNaviProperty());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.om.psnnavi.view.TreePanel_PostSeries getTreePanelPostSeries(){
 if(context.get("treePanelPostSeries")!=null)
 return (nc.ui.om.psnnavi.view.TreePanel_PostSeries)context.get("treePanelPostSeries");
  nc.ui.om.psnnavi.view.TreePanel_PostSeries bean = new nc.ui.om.psnnavi.view.TreePanel_PostSeries();
  context.put("treePanelPostSeries",bean);
  bean.setModel(getTreeAppModelPostSeries());
  bean.setLeftSuperModel(getLeftSuperModel());
  bean.setRightModel(getManageAppModel());
  bean.setNaviProperty(getNaviProperty());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.om.psnnavi.view.NaviStylePanel getNaviStylePanel(){
 if(context.get("naviStylePanel")!=null)
 return (nc.ui.om.psnnavi.view.NaviStylePanel)context.get("naviStylePanel");
  nc.ui.om.psnnavi.view.NaviStylePanel bean = new nc.ui.om.psnnavi.view.NaviStylePanel();
  context.put("naviStylePanel",bean);
  bean.setLeftSuperModel(getLeftSuperModel());
  bean.setRightModel(getManageAppModel());
  bean.setNaviProperty(getNaviProperty());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.tangramlayout.node.VSNode getNaviNode(){
 if(context.get("naviNode")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("naviNode");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("naviNode",bean);
  bean.setUp(getCNode_1316ed8());
  bean.setDown(getCNode_644230());
  bean.setDividerLocation(55f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1316ed8(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1316ed8")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1316ed8");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1316ed8",bean);
  bean.setComponent(getNaviStylePanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_644230(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#644230")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#644230");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#644230",bean);
  bean.setComponent(getTreeContainer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.om.psnnavi.view.TreePanelContainer getTreeContainer(){
 if(context.get("treeContainer")!=null)
 return (nc.ui.om.psnnavi.view.TreePanelContainer)context.get("treeContainer");
  nc.ui.om.psnnavi.view.TreePanelContainer bean = new nc.ui.om.psnnavi.view.TreePanelContainer();
  context.put("treeContainer",bean);
  bean.setOrgTree(getTreePanelOrg());
  bean.setMsAOSTree(getTreePanelMsAOS());
  bean.setPsnTypeTree(getTreePanelPsnType());
  bean.setPostSeriesTree(getTreePanelPostSeries());
  bean.setNaviStylePanel(getNaviStylePanel());
  bean.setLeftSuperModel(getLeftSuperModel());
  bean.setNaviProperty(getNaviProperty());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.model.PsndocModelService getPsndocModelService(){
 if(context.get("psndocModelService")!=null)
 return (nc.ui.hi.psndoc.model.PsndocModelService)context.get("psndocModelService");
  nc.ui.hi.psndoc.model.PsndocModelService bean = new nc.ui.hi.psndoc.model.PsndocModelService();
  context.put("psndocModelService",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.register.model.RegisterDefaultValueProvider getDefaultValueProvider(){
 if(context.get("defaultValueProvider")!=null)
 return (nc.ui.hi.register.model.RegisterDefaultValueProvider)context.get("defaultValueProvider");
  nc.ui.hi.register.model.RegisterDefaultValueProvider bean = new nc.ui.hi.register.model.RegisterDefaultValueProvider();
  context.put("defaultValueProvider",bean);
  bean.setModelDataManager(getPsndocDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.register.model.SubDefaultValueProvider getSubDefaultValueProvider(){
 if(context.get("subDefaultValueProvider")!=null)
 return (nc.ui.hi.register.model.SubDefaultValueProvider)context.get("subDefaultValueProvider");
  nc.ui.hi.register.model.SubDefaultValueProvider bean = new nc.ui.hi.register.model.SubDefaultValueProvider();
  context.put("subDefaultValueProvider",bean);
  bean.setFormEditor(getPsndocFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public java.util.HashSet getBusinessInfoSet(){
 if(context.get("businessInfoSet")!=null)
 return (java.util.HashSet)context.get("businessInfoSet");
  java.util.HashSet bean = new java.util.HashSet(getManagedList13());  context.put("businessInfoSet",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList13(){  List list = new ArrayList();  list.add("hi_psnorg");  list.add("hi_psnjob");  list.add("hi_psndoc_parttime");  list.add("hi_psndoc_trial");  list.add("hi_psndoc_psnchg");  list.add("hi_psndoc_ctrt");  list.add("hi_psndoc_retire");  list.add("hi_psndoc_train");  list.add("hi_psndoc_ass");  list.add("hi_psndoc_capa");  list.add("hi_psndoc_qulify");  return list;}

public java.util.HashSet getDisableTabSet(){
 if(context.get("disableTabSet")!=null)
 return (java.util.HashSet)context.get("disableTabSet");
  java.util.HashSet bean = new java.util.HashSet(getManagedList14());  context.put("disableTabSet",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList14(){  List list = new ArrayList();  list.add("hi_psnorg");  return list;}

public nc.ui.uif2.actions.ActionContributors getToftpanelActionContributors(){
 if(context.get("toftpanelActionContributors")!=null)
 return (nc.ui.uif2.actions.ActionContributors)context.get("toftpanelActionContributors");
  nc.ui.uif2.actions.ActionContributors bean = new nc.ui.uif2.actions.ActionContributors();
  context.put("toftpanelActionContributors",bean);
  bean.setContributors(getManagedList15());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList15(){  List list = new ArrayList();  list.add(getListViewActions());  list.add(getFormEditorActions());  return list;}

public nc.ui.hi.psndoc.model.PsndocInitDataListener getInitDataListener(){
 if(context.get("InitDataListener")!=null)
 return (nc.ui.hi.psndoc.model.PsndocInitDataListener)context.get("InitDataListener");
  nc.ui.hi.psndoc.model.PsndocInitDataListener bean = new nc.ui.hi.psndoc.model.PsndocInitDataListener();
  context.put("InitDataListener",bean);
  bean.setContext(getContext());
  bean.setModel(getManageAppModel());
  bean.setLeftModel(getLeftSuperModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.FunNodeClosingHandler getClosingListener(){
 if(context.get("ClosingListener")!=null)
 return (nc.ui.uif2.FunNodeClosingHandler)context.get("ClosingListener");
  nc.ui.uif2.FunNodeClosingHandler bean = new nc.ui.uif2.FunNodeClosingHandler();
  context.put("ClosingListener",bean);
  bean.setModel(getManageAppModel());
  bean.setSaveaction(getSavePsndocAction());
  bean.setCancelaction(getCancelPsndocAction());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.register.model.RegisterPsndocModel getManageAppModel(){
 if(context.get("manageAppModel")!=null)
 return (nc.ui.hi.register.model.RegisterPsndocModel)context.get("manageAppModel");
  nc.ui.hi.register.model.RegisterPsndocModel bean = new nc.ui.hi.register.model.RegisterPsndocModel();
  context.put("manageAppModel",bean);
  bean.setContext(getContext());
  bean.setService(getPsndocModelService());
  bean.setBusinessInfoSet(getBusinessInfoSet());
  bean.setBusinessObjectAdapterFactory(getBoadatorfactory());
  bean.setResourceCode(getResourceCode());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.model.PsndocDataManager getPsndocDataManager(){
 if(context.get("psndocDataManager")!=null)
 return (nc.ui.hi.psndoc.model.PsndocDataManager)context.get("psndocDataManager");
  nc.ui.hi.psndoc.model.PsndocDataManager bean = new nc.ui.hi.psndoc.model.PsndocDataManager();
  context.put("psndocDataManager",bean);
  bean.setContext(getContext());
  bean.setModel(getManageAppModel());
  bean.setService(getPsndocModelService());
  bean.setPaginationModel(getPaginationModel());
  bean.setPaginationBar(getPaginationBar());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.model.PsndocMediator getPsndocMediator(){
 if(context.get("psndocMediator")!=null)
 return (nc.ui.hi.psndoc.model.PsndocMediator)context.get("psndocMediator");
  nc.ui.hi.psndoc.model.PsndocMediator bean = new nc.ui.hi.psndoc.model.PsndocMediator();
  context.put("psndocMediator",bean);
  bean.setTypeAppModel(getLeftSuperModel());
  bean.setDocModel(getManageAppModel());
  bean.setDocModelDataManager(getPsndocDataManager());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.components.pagination.PaginationBar getPaginationBar(){
 if(context.get("paginationBar")!=null)
 return (nc.ui.uif2.components.pagination.PaginationBar)context.get("paginationBar");
  nc.ui.uif2.components.pagination.PaginationBar bean = new nc.ui.uif2.components.pagination.PaginationBar();
  context.put("paginationBar",bean);
  bean.setPaginationModel(getPaginationModel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.components.pagination.PaginationModel getPaginationModel(){
 if(context.get("paginationModel")!=null)
 return (nc.ui.uif2.components.pagination.PaginationModel)context.get("paginationModel");
  nc.ui.uif2.components.pagination.PaginationModel bean = new nc.ui.uif2.components.pagination.PaginationModel();
  context.put("paginationModel",bean);
  bean.setPaginationQueryService(getPsndocModelService());
  bean.init();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.view.PsndocListView getPsndocListView(){
 if(context.get("psndocListView")!=null)
 return (nc.ui.hi.psndoc.view.PsndocListView)context.get("psndocListView");
  nc.ui.hi.psndoc.view.PsndocListView bean = new nc.ui.hi.psndoc.view.PsndocListView();
  context.put("psndocListView",bean);
  bean.setPos("head");
  bean.setModel(getManageAppModel());
  bean.setMultiSelectionMode(1);
  bean.setMultiSelectionEnable(true);
  bean.setDataManger(getPsndocDataManager());
  bean.setPaginationBar(getPaginationBar());
  bean.setNodekey("bd_psndoc");
  bean.setDealHyperlink(true);
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.view.PsndocFormEditor getPsndocFormEditor(){
 if(context.get("psndocFormEditor")!=null)
 return (nc.ui.hi.psndoc.view.PsndocFormEditor)context.get("psndocFormEditor");
  nc.ui.hi.psndoc.view.PsndocFormEditor bean = new nc.ui.hi.psndoc.view.PsndocFormEditor();
  context.put("psndocFormEditor",bean);
  bean.setModel(getManageAppModel());
  bean.setSuperValidator(getSuperValidationConfig());
  bean.setDataManger(getPsndocDataManager());
  bean.setTemplateContainer(getTemplateContainer());
  bean.setNodekey("bd_psndoc");
  bean.setTabActions(getManagedList16());
  bean.setComponentValueManager(getValueManager());
  bean.setDealHyperlink(true);
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList16(){  List list = new ArrayList();  list.add(getAddSubSetAction());  list.add(getInsertSubSetAction());  list.add(getDeleteSubSetAction());  list.add(getCopySubSetAction());  list.add(getPasteSubSetAction());  list.add(getActionsBarSeparator());  list.add(getAdjustSubReordUpAction());  list.add(getAdjustSubReordDownAction());  list.add(getBodyMaxAction());  return list;}

public nc.ui.hr.uif2.mediator.HyperLinkClickMediator getMouseClickShowPanelMediator(){
 if(context.get("mouseClickShowPanelMediator")!=null)
 return (nc.ui.hr.uif2.mediator.HyperLinkClickMediator)context.get("mouseClickShowPanelMediator");
  nc.ui.hr.uif2.mediator.HyperLinkClickMediator bean = new nc.ui.hr.uif2.mediator.HyperLinkClickMediator();
  context.put("mouseClickShowPanelMediator",bean);
  bean.setModel(getManageAppModel());
  bean.setShowUpComponent(getPsndocFormEditor());
  bean.setHyperLinkColumn("code");
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.view.PsndocMetaDataValueAdapter getValueManager(){
 if(context.get("valueManager")!=null)
 return (nc.ui.hi.psndoc.view.PsndocMetaDataValueAdapter)context.get("valueManager");
  nc.ui.hi.psndoc.view.PsndocMetaDataValueAdapter bean = new nc.ui.hi.psndoc.view.PsndocMetaDataValueAdapter();
  context.put("valueManager",bean);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getEditorToolBarPanel(){
 if(context.get("editorToolBarPanel")!=null)
 return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel)context.get("editorToolBarPanel");
  nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
  context.put("editorToolBarPanel",bean);
  bean.setModel(getManageAppModel());
  bean.setTitleAction(getEditorReturnAction());
  bean.setActions(getManagedList17());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList17(){  List list = new ArrayList();  list.add(getFileAction());  list.add(getActionsBarSeparator());  list.add(getFirstLineAction());  list.add(getPreLineAction());  list.add(getNextLineAction());  list.add(getLastLineAction());  list.add(getHeadMaxAction());  return list;}

public nc.ui.uif2.actions.ShowMeUpAction getEditorReturnAction(){
 if(context.get("editorReturnAction")!=null)
 return (nc.ui.uif2.actions.ShowMeUpAction)context.get("editorReturnAction");
  nc.ui.uif2.actions.ShowMeUpAction bean = new nc.ui.uif2.actions.ShowMeUpAction();
  context.put("editorReturnAction",bean);
  bean.setGoComponent(getPsndocListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hr.uif2.view.HrPsnclTemplateContainer getTemplateContainer(){
 if(context.get("templateContainer")!=null)
 return (nc.ui.hr.uif2.view.HrPsnclTemplateContainer)context.get("templateContainer");
  nc.ui.hr.uif2.view.HrPsnclTemplateContainer bean = new nc.ui.hr.uif2.view.HrPsnclTemplateContainer();
  context.put("templateContainer",bean);
  bean.setContext(getContext());
  bean.setNodeKeies(getManagedList18());
  bean.load();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList18(){  List list = new ArrayList();  list.add("bd_psndoc");  return list;}

public nc.ui.uif2.TangramContainer getContainer(){
 if(context.get("container")!=null)
 return (nc.ui.uif2.TangramContainer)context.get("container");
  nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
  context.put("container",bean);
  bean.setTangramLayoutRoot(getTBNode_1b1d524());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_1b1d524(){
 if(context.get("nc.ui.uif2.tangramlayout.node.TBNode#1b1d524")!=null)
 return (nc.ui.uif2.tangramlayout.node.TBNode)context.get("nc.ui.uif2.tangramlayout.node.TBNode#1b1d524");
  nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
  context.put("nc.ui.uif2.tangramlayout.node.TBNode#1b1d524",bean);
  bean.setShowMode("CardLayout");
  bean.setTabs(getManagedList19());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private List getManagedList19(){  List list = new ArrayList();  list.add(getVSNode_1b8323b());  list.add(getVSNode_73f07d());  return list;}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_1b8323b(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#1b8323b")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#1b8323b");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#1b8323b",bean);
  bean.setShowMode("NoDivider");
  bean.setUp(getCNode_357e56());
  bean.setDown(getHSNode_1059dc2());
  bean.setDividerLocation(30f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_357e56(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#357e56")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#357e56");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#357e56",bean);
  bean.setComponent(getPsndocPrimaryOrgPanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.HSNode getHSNode_1059dc2(){
 if(context.get("nc.ui.uif2.tangramlayout.node.HSNode#1059dc2")!=null)
 return (nc.ui.uif2.tangramlayout.node.HSNode)context.get("nc.ui.uif2.tangramlayout.node.HSNode#1059dc2");
  nc.ui.uif2.tangramlayout.node.HSNode bean = new nc.ui.uif2.tangramlayout.node.HSNode();
  context.put("nc.ui.uif2.tangramlayout.node.HSNode#1059dc2",bean);
  bean.setLeft(getCNode_1f97cbb());
  bean.setRight(getCNode_6bf29e());
  bean.setDividerLocation(0.2f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1f97cbb(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1f97cbb")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1f97cbb");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1f97cbb",bean);
  bean.setComponent(getTreeContainer());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_6bf29e(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#6bf29e")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#6bf29e");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#6bf29e",bean);
  bean.setComponent(getPsndocListView());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_73f07d(){
 if(context.get("nc.ui.uif2.tangramlayout.node.VSNode#73f07d")!=null)
 return (nc.ui.uif2.tangramlayout.node.VSNode)context.get("nc.ui.uif2.tangramlayout.node.VSNode#73f07d");
  nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
  context.put("nc.ui.uif2.tangramlayout.node.VSNode#73f07d",bean);
  bean.setShowMode("NoDivider");
  bean.setUp(getCNode_1e9f6bd());
  bean.setDown(getCNode_145f760());
  bean.setDividerLocation(26f);
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_1e9f6bd(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#1e9f6bd")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#1e9f6bd");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#1e9f6bd",bean);
  bean.setComponent(getEditorToolBarPanel());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

private nc.ui.uif2.tangramlayout.node.CNode getCNode_145f760(){
 if(context.get("nc.ui.uif2.tangramlayout.node.CNode#145f760")!=null)
 return (nc.ui.uif2.tangramlayout.node.CNode)context.get("nc.ui.uif2.tangramlayout.node.CNode#145f760");
  nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
  context.put("nc.ui.uif2.tangramlayout.node.CNode#145f760",bean);
  bean.setComponent(getPsndocFormEditor());
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

public nc.ui.hi.psndoc.view.PsnPrimaryOrgPanel getPsndocPrimaryOrgPanel(){
 if(context.get("psndocPrimaryOrgPanel")!=null)
 return (nc.ui.hi.psndoc.view.PsnPrimaryOrgPanel)context.get("psndocPrimaryOrgPanel");
  nc.ui.hi.psndoc.view.PsnPrimaryOrgPanel bean = new nc.ui.hi.psndoc.view.PsnPrimaryOrgPanel();
  context.put("psndocPrimaryOrgPanel",bean);
  bean.setModel(getManageAppModel());
  bean.setListView(getPsndocListView());
  bean.setLeftSuperModel(getLeftSuperModel());
  bean.setPaginationModel(getPaginationModel());
  bean.setDataManager(getLeftModelDataManager());
  bean.setPk_orgtype("HRORGTYPE00000000000");
  bean.setPsnDataManager(getPsndocDataManager());
  bean.initUI();
setBeanFacotryIfBeanFacatoryAware(bean);
invokeInitializingBean(bean);
return bean;
}

}
