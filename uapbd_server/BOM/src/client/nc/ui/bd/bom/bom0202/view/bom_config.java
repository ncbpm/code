package nc.ui.bd.bom.bom0202.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class bom_config extends AbstractJavaBeanDefinition {
	private Map<String, Object> context = new HashMap();

	public nc.vo.uif2.LoginContext getContext() {
		if (context.get("context") != null)
			return (nc.vo.uif2.LoginContext) context.get("context");
		nc.vo.uif2.LoginContext bean = new nc.vo.uif2.LoginContext();
		context.put("context", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory getBoAdatorFactory() {
		if (context.get("boAdatorFactory") != null)
			return (nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory) context
					.get("boAdatorFactory");
		nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory bean = new nc.ui.pubapp.uif2app.view.value.AggVOMetaBDObjectAdapterFactory();
		context.put("boAdatorFactory", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.vo.bd.meta.BDObjectAdpaterFactory getBoadatorfactory1() {
		if (context.get("boadatorfactory1") != null)
			return (nc.vo.bd.meta.BDObjectAdpaterFactory) context
					.get("boadatorfactory1");
		nc.vo.bd.meta.BDObjectAdpaterFactory bean = new nc.vo.bd.meta.BDObjectAdpaterFactory();
		context.put("boadatorfactory1", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.model.BomMainGrandModel getMainGrandModel() {
		if (context.get("mainGrandModel") != null)
			return (nc.ui.bd.bom.bom0202.model.BomMainGrandModel) context
					.get("mainGrandModel");
		nc.ui.bd.bom.bom0202.model.BomMainGrandModel bean = new nc.ui.bd.bom.bom0202.model.BomMainGrandModel();
		context.put("mainGrandModel", bean);
		bean.setHandleListCardIsShow(true);
		bean.setMainModel(getManageAppModel());
		bean.setGrandModel(getManageAppModel2());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.model.BomMainBillManageModel getManageAppModel() {
		if (context.get("manageAppModel") != null)
			return (nc.ui.bd.bom.bom0202.model.BomMainBillManageModel) context
					.get("manageAppModel");
		nc.ui.bd.bom.bom0202.model.BomMainBillManageModel bean = new nc.ui.bd.bom.bom0202.model.BomMainBillManageModel();
		context.put("manageAppModel", bean);
		bean.setContext(getContext());
		bean.setBusinessObjectAdapterFactory(getBoAdatorFactory());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.model.BillManageModel getManageAppModel2() {
		if (context.get("manageAppModel2") != null)
			return (nc.ui.pubapp.uif2app.model.BillManageModel) context
					.get("manageAppModel2");
		nc.ui.pubapp.uif2app.model.BillManageModel bean = new nc.ui.pubapp.uif2app.model.BillManageModel();
		context.put("manageAppModel2", bean);
		bean.setBusinessObjectAdapterFactory(getBoadatorfactory1());
		bean.setContext(getContext());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.validation.CompositeValidation getValidationService() {
		if (context.get("validationService") != null)
			return (nc.ui.pubapp.uif2app.validation.CompositeValidation) context
					.get("validationService");
		nc.ui.pubapp.uif2app.validation.CompositeValidation bean = new nc.ui.pubapp.uif2app.validation.CompositeValidation();
		context.put("validationService", bean);
		bean.setValidators(getManagedList0());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList0() {
		List list = new ArrayList();
		list.add(getTemplateNotNullValidation_1cbc872());
		list.add(getBomValidationService_1d4b964());
		list.add(getGrandNotNullValidationService_d76596());
		return list;
	}

	private nc.ui.pubapp.uif2app.validation.TemplateNotNullValidation getTemplateNotNullValidation_1cbc872() {
		if (context
				.get("nc.ui.pubapp.uif2app.validation.TemplateNotNullValidation#1cbc872") != null)
			return (nc.ui.pubapp.uif2app.validation.TemplateNotNullValidation) context
					.get("nc.ui.pubapp.uif2app.validation.TemplateNotNullValidation#1cbc872");
		nc.ui.pubapp.uif2app.validation.TemplateNotNullValidation bean = new nc.ui.pubapp.uif2app.validation.TemplateNotNullValidation();
		context.put(
				"nc.ui.pubapp.uif2app.validation.TemplateNotNullValidation#1cbc872",
				bean);
		bean.setBillForm(getBillFormEditor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.validator.BomValidationService getBomValidationService_1d4b964() {
		if (context
				.get("nc.ui.bd.bom.bom0202.validator.BomValidationService#1d4b964") != null)
			return (nc.ui.bd.bom.bom0202.validator.BomValidationService) context
					.get("nc.ui.bd.bom.bom0202.validator.BomValidationService#1d4b964");
		nc.ui.bd.bom.bom0202.validator.BomValidationService bean = new nc.ui.bd.bom.bom0202.validator.BomValidationService();
		context.put(
				"nc.ui.bd.bom.bom0202.validator.BomValidationService#1d4b964",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.components.grand.model.GrandNotNullValidationService getGrandNotNullValidationService_d76596() {
		if (context
				.get("nc.ui.pubapp.uif2app.components.grand.model.GrandNotNullValidationService#d76596") != null)
			return (nc.ui.pubapp.uif2app.components.grand.model.GrandNotNullValidationService) context
					.get("nc.ui.pubapp.uif2app.components.grand.model.GrandNotNullValidationService#d76596");
		nc.ui.pubapp.uif2app.components.grand.model.GrandNotNullValidationService bean = new nc.ui.pubapp.uif2app.components.grand.model.GrandNotNullValidationService();
		context.put(
				"nc.ui.pubapp.uif2app.components.grand.model.GrandNotNullValidationService#d76596",
				bean);
		bean.setBillForm(getSunbillFormEditor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.query2.model.ModelDataManager getModelDataManager() {
		if (context.get("modelDataManager") != null)
			return (nc.ui.pubapp.uif2app.query2.model.ModelDataManager) context
					.get("modelDataManager");
		nc.ui.pubapp.uif2app.query2.model.ModelDataManager bean = new nc.ui.pubapp.uif2app.query2.model.ModelDataManager();
		context.put("modelDataManager", bean);
		bean.setModel(getManageAppModel());
		bean.setService(getQueryProxy());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.serviceproxy.BomMaintainProxy getMaintainProxy() {
		if (context.get("maintainProxy") != null)
			return (nc.ui.bd.bom.bom0202.serviceproxy.BomMaintainProxy) context
					.get("maintainProxy");
		nc.ui.bd.bom.bom0202.serviceproxy.BomMaintainProxy bean = new nc.ui.bd.bom.bom0202.serviceproxy.BomMaintainProxy();
		context.put("maintainProxy", bean);
		bean.setIscheckecn(false);
		bean.setIsonapprove(true);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.serviceproxy.BomDeleteProxy getDeleteProxy() {
		if (context.get("deleteProxy") != null)
			return (nc.ui.bd.bom.bom0202.serviceproxy.BomDeleteProxy) context
					.get("deleteProxy");
		nc.ui.bd.bom.bom0202.serviceproxy.BomDeleteProxy bean = new nc.ui.bd.bom.bom0202.serviceproxy.BomDeleteProxy();
		context.put("deleteProxy", bean);
		bean.setIscheckecn(false);
		bean.setIsonapprove(true);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.serviceproxy.BomBusinessServiceproxy getBusinessProxy() {
		if (context.get("businessProxy") != null)
			return (nc.ui.bd.bom.bom0202.serviceproxy.BomBusinessServiceproxy) context
					.get("businessProxy");
		nc.ui.bd.bom.bom0202.serviceproxy.BomBusinessServiceproxy bean = new nc.ui.bd.bom.bom0202.serviceproxy.BomBusinessServiceproxy();
		context.put("businessProxy", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.serviceproxy.BomQueryService getQueryProxy() {
		if (context.get("queryProxy") != null)
			return (nc.ui.bd.bom.bom0202.serviceproxy.BomQueryService) context
					.get("queryProxy");
		nc.ui.bd.bom.bom0202.serviceproxy.BomQueryService bean = new nc.ui.bd.bom.bom0202.serviceproxy.BomQueryService();
		context.put("queryProxy", bean);
		bean.setModel(getManageAppModel());
		bean.setIsCust(false);
		bean.setIsCfgbom(false);
		bean.setGrandTabAndVOMap(getManagedMap0());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private Map getManagedMap0() {
		Map map = new HashMap();
		map.put("wips", getBomwipBean());
		map.put("pos", getBompositionBean());
		map.put("repl", getBomreplaceBean());
		map.put("loss", getBomlossBean());
		map.put("resource", getBomresourceBean());
		map.put("select", getBomselectBean());
		return map;
	}

	public nc.ui.uif2.editor.QueryTemplateContainer getQueryTemplateContainer() {
		if (context.get("queryTemplateContainer") != null)
			return (nc.ui.uif2.editor.QueryTemplateContainer) context
					.get("queryTemplateContainer");
		nc.ui.uif2.editor.QueryTemplateContainer bean = new nc.ui.uif2.editor.QueryTemplateContainer();
		context.put("queryTemplateContainer", bean);
		bean.setContext(getContext());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.editor.TemplateContainer getTemplateContainer() {
		if (context.get("templateContainer") != null)
			return (nc.ui.uif2.editor.TemplateContainer) context
					.get("templateContainer");
		nc.ui.uif2.editor.TemplateContainer bean = new nc.ui.uif2.editor.TemplateContainer();
		context.put("templateContainer", bean);
		bean.setContext(getContext());
		bean.setNodeKeies(getManagedList1());
		bean.load();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList1() {
		List list = new ArrayList();
		list.add("bomzhuzi");
		list.add("bomsun");
		return list;
	}

	public nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller getRemoteCallCombinatorCaller() {
		if (context.get("remoteCallCombinatorCaller") != null)
			return (nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller) context
					.get("remoteCallCombinatorCaller");
		nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller bean = new nc.ui.uif2.editor.UIF2RemoteCallCombinatorCaller();
		context.put("remoteCallCombinatorCaller", bean);
		bean.setRemoteCallers(getManagedList2());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList2() {
		List list = new ArrayList();
		list.add(getQueryTemplateContainer());
		list.add(getTemplateContainer());
		list.add(getUserdefitemContainer());
		list.add(getFactoryPanel());
		return list;
	}

	public nc.ui.bd.bom.bom0202.listener.BomFuncNodeInitDataListener getInitDataListener() {
		if (context.get("InitDataListener") != null)
			return (nc.ui.bd.bom.bom0202.listener.BomFuncNodeInitDataListener) context
					.get("InitDataListener");
		nc.ui.bd.bom.bom0202.listener.BomFuncNodeInitDataListener bean = new nc.ui.bd.bom.bom0202.listener.BomFuncNodeInitDataListener();
		context.put("InitDataListener", bean);
		bean.setDataManager(getModelDataManager());
		bean.setModel(getManageAppModel());
		bean.setContext(getContext());
		bean.setAutoShowUpComponent(getZhuzisuncard());
		bean.setQueryAction(getQueryAction());
		bean.setVoClassName("nc.vo.bd.bom.bom0202.entity.AggBomVO");
		bean.setProcessorMap(getManagedMap1());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private Map getManagedMap1() {
		Map map = new HashMap();
		map.put("10", getFuncNodeInitDataListenerForRt_cf586e());
		return map;
	}

	private nc.ui.bd.bom.bom0202.listener.FuncNodeInitDataListenerForRt getFuncNodeInitDataListenerForRt_cf586e() {
		if (context
				.get("nc.ui.bd.bom.bom0202.listener.FuncNodeInitDataListenerForRt#cf586e") != null)
			return (nc.ui.bd.bom.bom0202.listener.FuncNodeInitDataListenerForRt) context
					.get("nc.ui.bd.bom.bom0202.listener.FuncNodeInitDataListenerForRt#cf586e");
		nc.ui.bd.bom.bom0202.listener.FuncNodeInitDataListenerForRt bean = new nc.ui.bd.bom.bom0202.listener.FuncNodeInitDataListenerForRt();
		context.put(
				"nc.ui.bd.bom.bom0202.listener.FuncNodeInitDataListenerForRt#cf586e",
				bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.mmbd.uif.app.bill.view.MMBillOrgPanelForECN getFactoryPanel() {
		if (context.get("factoryPanel") != null)
			return (nc.ui.mmbd.uif.app.bill.view.MMBillOrgPanelForECN) context
					.get("factoryPanel");
		nc.ui.mmbd.uif.app.bill.view.MMBillOrgPanelForECN bean = new nc.ui.mmbd.uif.app.bill.view.MMBillOrgPanelForECN();
		context.put("factoryPanel", bean);
		bean.setModel(getManageAppModel());
		bean.setIsFilterECNOrg(true);
		bean.initUI();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.mmbd.pub.ref.MMFactoryRefPane getMMOrgPanel() {
		if (context.get("MMOrgPanel") != null)
			return (nc.ui.mmbd.pub.ref.MMFactoryRefPane) context
					.get("MMOrgPanel");
		nc.ui.mmbd.pub.ref.MMFactoryRefPane bean = new nc.ui.mmbd.pub.ref.MMFactoryRefPane();
		context.put("MMOrgPanel", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.components.grand.ListGrandPanelComposite getZhuzisun() {
		if (context.get("zhuzisun") != null)
			return (nc.ui.pubapp.uif2app.components.grand.ListGrandPanelComposite) context
					.get("zhuzisun");
		nc.ui.pubapp.uif2app.components.grand.ListGrandPanelComposite bean = new nc.ui.pubapp.uif2app.components.grand.ListGrandPanelComposite();
		context.put("zhuzisun", bean);
		bean.setGrandString(getI18nFB_b6c734());
		bean.setModel(getMainGrandModel());
		bean.setMaingrandrelationship(getMainGrandRelationShip());
		bean.setMediator(getMainGrandMediator());
		bean.setMainPanel(getListView());
		bean.setExpendShrinkGrandListAction(getExpendShrinkGrandListAction());
		bean.initUI();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_b6c734() {
		if (context.get("nc.ui.uif2.I18nFB#b6c734") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#b6c734");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#b6c734", bean);
		bean.setResDir("1014362_0");
		bean.setResId("01014362-0293");
		bean.setDefaultValue("详细信息");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#b6c734", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public nc.ui.pubapp.uif2app.components.grand.mediator.MainGrandMediator getMainGrandMediator() {
		if (context.get("mainGrandMediator") != null)
			return (nc.ui.pubapp.uif2app.components.grand.mediator.MainGrandMediator) context
					.get("mainGrandMediator");
		nc.ui.pubapp.uif2app.components.grand.mediator.MainGrandMediator bean = new nc.ui.pubapp.uif2app.components.grand.mediator.MainGrandMediator();
		context.put("mainGrandMediator", bean);
		bean.setMainBillForm(getBillFormEditor());
		bean.setMainBillListView(getListView());
		bean.setMainGrandModel(getMainGrandModel());
		bean.setMainGrandRelationShip(getMainGrandRelationShip());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.components.grand.MainGrandRelationShip getMainGrandRelationShip() {
		if (context.get("mainGrandRelationShip") != null)
			return (nc.ui.pubapp.uif2app.components.grand.MainGrandRelationShip) context
					.get("mainGrandRelationShip");
		nc.ui.pubapp.uif2app.components.grand.MainGrandRelationShip bean = new nc.ui.pubapp.uif2app.components.grand.MainGrandRelationShip();
		context.put("mainGrandRelationShip", bean);
		bean.setBodyTabTOGrandListComposite(getManagedMap2());
		bean.setBodyTabTOGrandCardComposite(getManagedMap3());
		bean.setGrandTabAndVOMap(getManagedMap4());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private Map getManagedMap2() {
		Map map = new HashMap();
		map.put("bomitems", getSunlistView());
		return map;
	}

	private Map getManagedMap3() {
		Map map = new HashMap();
		map.put("bomitems", getSunbillFormEditor());
		return map;
	}

	private Map getManagedMap4() {
		Map map = new HashMap();
		map.put("wips", getBomwipBean());
		map.put("pos", getBompositionBean());
		map.put("repl", getBomreplaceBean());
		map.put("loss", getBomlossBean());
		map.put("resource", getBomresourceBean());
		map.put("select", getBomselectBean());
		return map;
	}

	public nc.vo.bd.bom.bom0202.entity.BomWipVO getBomwipBean() {
		if (context.get("bomwipBean") != null)
			return (nc.vo.bd.bom.bom0202.entity.BomWipVO) context
					.get("bomwipBean");
		nc.vo.bd.bom.bom0202.entity.BomWipVO bean = new nc.vo.bd.bom.bom0202.entity.BomWipVO();
		context.put("bomwipBean", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.vo.bd.bom.bom0202.entity.BomPosVO getBompositionBean() {
		if (context.get("bompositionBean") != null)
			return (nc.vo.bd.bom.bom0202.entity.BomPosVO) context
					.get("bompositionBean");
		nc.vo.bd.bom.bom0202.entity.BomPosVO bean = new nc.vo.bd.bom.bom0202.entity.BomPosVO();
		context.put("bompositionBean", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.vo.bd.bom.bom0202.entity.BomReplVO getBomreplaceBean() {
		if (context.get("bomreplaceBean") != null)
			return (nc.vo.bd.bom.bom0202.entity.BomReplVO) context
					.get("bomreplaceBean");
		nc.vo.bd.bom.bom0202.entity.BomReplVO bean = new nc.vo.bd.bom.bom0202.entity.BomReplVO();
		context.put("bomreplaceBean", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.vo.bd.bom.bom0202.entity.BomLossVO getBomlossBean() {
		if (context.get("bomlossBean") != null)
			return (nc.vo.bd.bom.bom0202.entity.BomLossVO) context
					.get("bomlossBean");
		nc.vo.bd.bom.bom0202.entity.BomLossVO bean = new nc.vo.bd.bom.bom0202.entity.BomLossVO();
		context.put("bomlossBean", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.vo.bd.bom.bom0202.entity.BomItemSourceVO getBomresourceBean() {
		if (context.get("bomresourceBean") != null)
			return (nc.vo.bd.bom.bom0202.entity.BomItemSourceVO) context
					.get("bomresourceBean");
		nc.vo.bd.bom.bom0202.entity.BomItemSourceVO bean = new nc.vo.bd.bom.bom0202.entity.BomItemSourceVO();
		context.put("bomresourceBean", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.vo.bd.bom.bom0202.entity.BomSelectVO getBomselectBean() {
		if (context.get("bomselectBean") != null)
			return (nc.vo.bd.bom.bom0202.entity.BomSelectVO) context
					.get("bomselectBean");
		nc.vo.bd.bom.bom0202.entity.BomSelectVO bean = new nc.vo.bd.bom.bom0202.entity.BomSelectVO();
		context.put("bomselectBean", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.components.grand.action.ExpendShrinkGrandListAction getExpendShrinkGrandListAction() {
		if (context.get("expendShrinkGrandListAction") != null)
			return (nc.ui.pubapp.uif2app.components.grand.action.ExpendShrinkGrandListAction) context
					.get("expendShrinkGrandListAction");
		nc.ui.pubapp.uif2app.components.grand.action.ExpendShrinkGrandListAction bean = new nc.ui.pubapp.uif2app.components.grand.action.ExpendShrinkGrandListAction();
		context.put("expendShrinkGrandListAction", bean);
		bean.setMainGrandModel(getMainGrandModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.components.grand.action.MaxMinGrandListAction getMaxGrandAction() {
		if (context.get("maxGrandAction") != null)
			return (nc.ui.pubapp.uif2app.components.grand.action.MaxMinGrandListAction) context
					.get("maxGrandAction");
		nc.ui.pubapp.uif2app.components.grand.action.MaxMinGrandListAction bean = new nc.ui.pubapp.uif2app.components.grand.action.MaxMinGrandListAction();
		context.put("maxGrandAction", bean);
		bean.setMainGrandModel(getMainGrandModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.view.BomListView getListView() {
		if (context.get("listView") != null)
			return (nc.ui.bd.bom.bom0202.view.BomListView) context
					.get("listView");
		nc.ui.bd.bom.bom0202.view.BomListView bean = new nc.ui.bd.bom.bom0202.view.BomListView();
		context.put("listView", bean);
		bean.setMultiSelectionEnable(true);
		bean.setModel(getManageAppModel());
		bean.setTemplateContainer(getTemplateContainer());
		bean.setNodekey("bomzhuzi");
		bean.setBodyLineActions(getManagedList3());
		bean.setUserdefitemListPreparator(getCompositeBillListDataPrepare_dd5521());
		bean.initUI();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList3() {
		List list = new ArrayList();
		list.add(getListBodyMaxAction_1d05679());
		return list;
	}

	private nc.ui.mmf.framework.action.body.ListBodyMaxAction getListBodyMaxAction_1d05679() {
		if (context
				.get("nc.ui.mmf.framework.action.body.ListBodyMaxAction#1d05679") != null)
			return (nc.ui.mmf.framework.action.body.ListBodyMaxAction) context
					.get("nc.ui.mmf.framework.action.body.ListBodyMaxAction#1d05679");
		nc.ui.mmf.framework.action.body.ListBodyMaxAction bean = new nc.ui.mmf.framework.action.body.ListBodyMaxAction();
		context.put(
				"nc.ui.mmf.framework.action.body.ListBodyMaxAction#1d05679",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare getCompositeBillListDataPrepare_dd5521() {
		if (context
				.get("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#dd5521") != null)
			return (nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare) context
					.get("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#dd5521");
		nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare();
		context.put(
				"nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare#dd5521",
				bean);
		bean.setBillListDataPrepares(getManagedList4());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList4() {
		List list = new ArrayList();
		list.add(getUserdefitemlistPreparator());
		list.add(getMarAsstPreparator());
		return list;
	}

	public nc.ui.bd.bom.bom0202.view.BomGrandListView getSunlistView() {
		if (context.get("sunlistView") != null)
			return (nc.ui.bd.bom.bom0202.view.BomGrandListView) context
					.get("sunlistView");
		nc.ui.bd.bom.bom0202.view.BomGrandListView bean = new nc.ui.bd.bom.bom0202.view.BomGrandListView();
		context.put("sunlistView", bean);
		bean.setModel(getManageAppModel2());
		bean.setTemplateContainer(getTemplateContainer());
		bean.setNodekey("bomsun");
		bean.setMultiSelectionEnable(true);
		bean.initUI();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.components.grand.CardGrandPanelComposite getZhuzisuncard() {
		if (context.get("zhuzisuncard") != null)
			return (nc.ui.pubapp.uif2app.components.grand.CardGrandPanelComposite) context
					.get("zhuzisuncard");
		nc.ui.pubapp.uif2app.components.grand.CardGrandPanelComposite bean = new nc.ui.pubapp.uif2app.components.grand.CardGrandPanelComposite();
		context.put("zhuzisuncard", bean);
		bean.setGrandString(getI18nFB_ffeaff());
		bean.setMainPanel(getBillFormEditor());
		bean.setModel(getMainGrandModel());
		bean.setMaingrandrelationship(getMainGrandRelationShip());
		bean.setMainGrandBlankFilter(getMainGrandBlankFilter());
		bean.setMediator(getMainGrandMediator());
		bean.setHeadpanelcombo(getHeadpanelcombo());
		bean.setExpendShrinkGrandCardAction(getExpendShrinkGrandCardAction());
		bean.initUI();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_ffeaff() {
		if (context.get("nc.ui.uif2.I18nFB#ffeaff") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#ffeaff");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#ffeaff", bean);
		bean.setResDir("1014362_0");
		bean.setResId("01014362-0293");
		bean.setDefaultValue("详细信息");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#ffeaff", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public nc.ui.pubapp.uif2app.components.grand.HeadPanelCombo getHeadpanelcombo() {
		if (context.get("headpanelcombo") != null)
			return (nc.ui.pubapp.uif2app.components.grand.HeadPanelCombo) context
					.get("headpanelcombo");
		nc.ui.pubapp.uif2app.components.grand.HeadPanelCombo bean = new nc.ui.pubapp.uif2app.components.grand.HeadPanelCombo();
		context.put("headpanelcombo", bean);
		bean.setBillform(getBillFormEditor());
		bean.setExpendShrinkCardHeadAction(getExpendShrinkCardHeadAction());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.components.grand.action.ExpendShrinkCardHeadAction getExpendShrinkCardHeadAction() {
		if (context.get("expendShrinkCardHeadAction") != null)
			return (nc.ui.pubapp.uif2app.components.grand.action.ExpendShrinkCardHeadAction) context
					.get("expendShrinkCardHeadAction");
		nc.ui.pubapp.uif2app.components.grand.action.ExpendShrinkCardHeadAction bean = new nc.ui.pubapp.uif2app.components.grand.action.ExpendShrinkCardHeadAction(
				getHeadpanelcombo());
		context.put("expendShrinkCardHeadAction", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.components.grand.action.ExpendShrinkGrandCardAction getExpendShrinkGrandCardAction() {
		if (context.get("expendShrinkGrandCardAction") != null)
			return (nc.ui.pubapp.uif2app.components.grand.action.ExpendShrinkGrandCardAction) context
					.get("expendShrinkGrandCardAction");
		nc.ui.pubapp.uif2app.components.grand.action.ExpendShrinkGrandCardAction bean = new nc.ui.pubapp.uif2app.components.grand.action.ExpendShrinkGrandCardAction();
		context.put("expendShrinkGrandCardAction", bean);
		bean.setMainGrandModel(getMainGrandModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.components.grand.MainGrandBlankFilter getMainGrandBlankFilter() {
		if (context.get("mainGrandBlankFilter") != null)
			return (nc.ui.pubapp.uif2app.components.grand.MainGrandBlankFilter) context
					.get("mainGrandBlankFilter");
		nc.ui.pubapp.uif2app.components.grand.MainGrandBlankFilter bean = new nc.ui.pubapp.uif2app.components.grand.MainGrandBlankFilter();
		context.put("mainGrandBlankFilter", bean);
		bean.setChildFilterMap(getManagedMap5());
		bean.setGrandFilterMap(getManagedMap6());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private Map getManagedMap5() {
		Map map = new HashMap();
		map.put("bomitems", getManagedList5());
		map.put("outputs", getManagedList6());
		return map;
	}

	private List getManagedList5() {
		List list = new ArrayList();
		list.add("cmaterialvid");
		return list;
	}

	private List getManagedList6() {
		List list = new ArrayList();
		list.add("cmaterialvid");
		return list;
	}

	private Map getManagedMap6() {
		Map map = new HashMap();
		map.put("wips", getManagedList7());
		map.put("pos", getManagedList8());
		map.put("repl", getManagedList9());
		map.put("loss", getManagedList10());
		return map;
	}

	private List getManagedList7() {
		List list = new ArrayList();
		list.add("cwipid");
		return list;
	}

	private List getManagedList8() {
		List list = new ArrayList();
		list.add("vposition");
		return list;
	}

	private List getManagedList9() {
		List list = new ArrayList();
		list.add("creplmaterialvid");
		return list;
	}

	private List getManagedList10() {
		List list = new ArrayList();
		list.add("nlfromnum");
		return list;
	}

	public nc.ui.bd.bom.bom0202.view.BomBillForm getBillFormEditor() {
		if (context.get("billFormEditor") != null)
			return (nc.ui.bd.bom.bom0202.view.BomBillForm) context
					.get("billFormEditor");
		nc.ui.bd.bom.bom0202.view.BomBillForm bean = new nc.ui.bd.bom.bom0202.view.BomBillForm();
		context.put("billFormEditor", bean);
		bean.setModel(getManageAppModel());
		bean.setRequestFocus(false);
		bean.setTemplateContainer(getTemplateContainer());
		bean.setNodekey("bomzhuzi");
		bean.setBillOrgPanel(getFactoryPanel());
		bean.setAutoExecLoadRelationItem(false);
		bean.setTemplateNotNullValidate(true);
		bean.setShowTotalLine(true);
		bean.setShowTotalLineTabcodes(getManagedList11());
		bean.setAutoAddLine(true);
		bean.setBodyActionMap(getBombodyActionsMap());
		bean.setUserdefitemPreparator(getCompositeBillDataPrepare_653fc5());
		bean.setBlankChildrenFilter(getMultiFieldsBlankChildrenFilter_13dbc76());
		bean.initUI();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList11() {
		List list = new ArrayList();
		list.add("bomitems");
		list.add("outputs");
		return list;
	}

	private nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare getCompositeBillDataPrepare_653fc5() {
		if (context
				.get("nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare#653fc5") != null)
			return (nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare) context
					.get("nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare#653fc5");
		nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare();
		context.put(
				"nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare#653fc5",
				bean);
		bean.setBillDataPrepares(getManagedList12());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList12() {
		List list = new ArrayList();
		list.add(getUserdefitemPreparator());
		list.add(getMarAsstPreparator());
		return list;
	}

	private nc.ui.pubapp.uif2app.view.value.MultiFieldsBlankChildrenFilter getMultiFieldsBlankChildrenFilter_13dbc76() {
		if (context
				.get("nc.ui.pubapp.uif2app.view.value.MultiFieldsBlankChildrenFilter#13dbc76") != null)
			return (nc.ui.pubapp.uif2app.view.value.MultiFieldsBlankChildrenFilter) context
					.get("nc.ui.pubapp.uif2app.view.value.MultiFieldsBlankChildrenFilter#13dbc76");
		nc.ui.pubapp.uif2app.view.value.MultiFieldsBlankChildrenFilter bean = new nc.ui.pubapp.uif2app.view.value.MultiFieldsBlankChildrenFilter();
		context.put(
				"nc.ui.pubapp.uif2app.view.value.MultiFieldsBlankChildrenFilter#13dbc76",
				bean);
		bean.setFilterMap(getManagedMap7());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private Map getManagedMap7() {
		Map map = new HashMap();
		map.put("bomitems", getManagedList13());
		map.put("outputs", getManagedList14());
		return map;
	}

	private List getManagedList13() {
		List list = new ArrayList();
		list.add("cmaterialvid");
		return list;
	}

	private List getManagedList14() {
		List list = new ArrayList();
		list.add("cmaterialvid");
		return list;
	}

	public java.util.HashMap getBombodyActionsMap() {
		if (context.get("bombodyActionsMap") != null)
			return (java.util.HashMap) context.get("bombodyActionsMap");
		java.util.HashMap bean = new java.util.HashMap(getManagedMap8());
		context.put("bombodyActionsMap", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private Map getManagedMap8() {
		Map map = new HashMap();
		map.put("bomitems", getManagedList15());
		map.put("outputs", getManagedList18());
		return map;
	}

	private List getManagedList15() {
		List list = new ArrayList();
		list.add(getBodyAddLineAction_200e98());
		list.add(getBodyInsertLineAction_9e256d());
		list.add(getBodyDelLineAction_ab2bf0());
		list.add(getBodyCopyLineAction_1d1ad65());
		list.add(getGrandBodyPasteLineAciton_ceda69());
		list.add(getBodyPasteToTailActionItem());
		list.add(getActionsBar_ActionsBarSeparator_655c54());
		list.add(getBodyLineEditAction_90b701());
		list.add(getRearrangeRowNoBodyLineAction_ded42e());
		list.add(getActionsBar_ActionsBarSeparator_13c373a());
		list.add(getBomBodyLineUpAction_4de666());
		list.add(getBomBodyLineDownAction_513ae6());
		list.add(getMaxMinBodyAction_f20891());
		return list;
	}

	private nc.ui.pubapp.uif2app.actions.BodyAddLineAction getBodyAddLineAction_200e98() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyAddLineAction#200e98") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyAddLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyAddLineAction#200e98");
		nc.ui.pubapp.uif2app.actions.BodyAddLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyAddLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyAddLineAction#200e98",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyInsertLineAction getBodyInsertLineAction_9e256d() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#9e256d") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyInsertLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#9e256d");
		nc.ui.pubapp.uif2app.actions.BodyInsertLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyInsertLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#9e256d",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyDelLineAction getBodyDelLineAction_ab2bf0() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#ab2bf0") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyDelLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#ab2bf0");
		nc.ui.pubapp.uif2app.actions.BodyDelLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyDelLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#ab2bf0",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyCopyLineAction getBodyCopyLineAction_1d1ad65() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#1d1ad65") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyCopyLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#1d1ad65");
		nc.ui.pubapp.uif2app.actions.BodyCopyLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyCopyLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#1d1ad65",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.components.grand.action.GrandBodyPasteLineAciton getGrandBodyPasteLineAciton_ceda69() {
		if (context
				.get("nc.ui.pubapp.uif2app.components.grand.action.GrandBodyPasteLineAciton#ceda69") != null)
			return (nc.ui.pubapp.uif2app.components.grand.action.GrandBodyPasteLineAciton) context
					.get("nc.ui.pubapp.uif2app.components.grand.action.GrandBodyPasteLineAciton#ceda69");
		nc.ui.pubapp.uif2app.components.grand.action.GrandBodyPasteLineAciton bean = new nc.ui.pubapp.uif2app.components.grand.action.GrandBodyPasteLineAciton();
		context.put(
				"nc.ui.pubapp.uif2app.components.grand.action.GrandBodyPasteLineAciton#ceda69",
				bean);
		bean.setClearItems(getManagedList16());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList16() {
		List list = new ArrayList();
		list.add("bomitems:cbom_bid");
		return list;
	}

	private nc.ui.pubapp.uif2app.components.grand.action.GrandBodyPasteToTailAction getBodyPasteToTailActionItem() {
		if (context.get("bodyPasteToTailActionItem") != null)
			return (nc.ui.pubapp.uif2app.components.grand.action.GrandBodyPasteToTailAction) context
					.get("bodyPasteToTailActionItem");
		nc.ui.pubapp.uif2app.components.grand.action.GrandBodyPasteToTailAction bean = new nc.ui.pubapp.uif2app.components.grand.action.GrandBodyPasteToTailAction();
		context.put("bodyPasteToTailActionItem", bean);
		bean.setClearItems(getManagedList17());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList17() {
		List list = new ArrayList();
		list.add("bomitems:cbom_bid");
		return list;
	}

	private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_655c54() {
		if (context
				.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#655c54") != null)
			return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator) context
					.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#655c54");
		nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
		context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#655c54",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyLineEditAction getBodyLineEditAction_90b701() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#90b701") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyLineEditAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#90b701");
		nc.ui.pubapp.uif2app.actions.BodyLineEditAction bean = new nc.ui.pubapp.uif2app.actions.BodyLineEditAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#90b701",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction getRearrangeRowNoBodyLineAction_ded42e() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#ded42e") != null)
			return (nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#ded42e");
		nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction bean = new nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction();
		context.put(
				"nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#ded42e",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_13c373a() {
		if (context
				.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#13c373a") != null)
			return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator) context
					.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#13c373a");
		nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
		context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#13c373a",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.action.BomBodyLineUpAction getBomBodyLineUpAction_4de666() {
		if (context
				.get("nc.ui.bd.bom.bom0202.action.BomBodyLineUpAction#4de666") != null)
			return (nc.ui.bd.bom.bom0202.action.BomBodyLineUpAction) context
					.get("nc.ui.bd.bom.bom0202.action.BomBodyLineUpAction#4de666");
		nc.ui.bd.bom.bom0202.action.BomBodyLineUpAction bean = new nc.ui.bd.bom.bom0202.action.BomBodyLineUpAction();
		context.put("nc.ui.bd.bom.bom0202.action.BomBodyLineUpAction#4de666",
				bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.action.BomBodyLineDownAction getBomBodyLineDownAction_513ae6() {
		if (context
				.get("nc.ui.bd.bom.bom0202.action.BomBodyLineDownAction#513ae6") != null)
			return (nc.ui.bd.bom.bom0202.action.BomBodyLineDownAction) context
					.get("nc.ui.bd.bom.bom0202.action.BomBodyLineDownAction#513ae6");
		nc.ui.bd.bom.bom0202.action.BomBodyLineDownAction bean = new nc.ui.bd.bom.bom0202.action.BomBodyLineDownAction();
		context.put("nc.ui.bd.bom.bom0202.action.BomBodyLineDownAction#513ae6",
				bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.components.grand.action.MaxMinBodyAction getMaxMinBodyAction_f20891() {
		if (context
				.get("nc.ui.pubapp.uif2app.components.grand.action.MaxMinBodyAction#f20891") != null)
			return (nc.ui.pubapp.uif2app.components.grand.action.MaxMinBodyAction) context
					.get("nc.ui.pubapp.uif2app.components.grand.action.MaxMinBodyAction#f20891");
		nc.ui.pubapp.uif2app.components.grand.action.MaxMinBodyAction bean = new nc.ui.pubapp.uif2app.components.grand.action.MaxMinBodyAction();
		context.put(
				"nc.ui.pubapp.uif2app.components.grand.action.MaxMinBodyAction#f20891",
				bean);
		bean.setMainGrandModel(getMainGrandModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList18() {
		List list = new ArrayList();
		list.add(getBodyAddLineAction_10a6a6());
		list.add(getBodyInsertLineAction_12cf0d6());
		list.add(getBodyDelLineAction_177a164());
		list.add(getBodyCopyLineAction_104c59b());
		list.add(getBodyPasteLineAction_495619());
		list.add(getBodyPasteToTailActionOutputs());
		list.add(getActionsBar_ActionsBarSeparator_b3b2a4());
		list.add(getBodyLineEditAction_1955767());
		list.add(getRearrangeRowNoBodyLineAction_19d353c());
		list.add(getActionsBar_ActionsBarSeparator_120f96());
		list.add(getBomBodyLineUpAction_1bd65d1());
		list.add(getBomBodyLineDownAction_167898a());
		list.add(getMaxMinBodyAction_76f4fb());
		return list;
	}

	private nc.ui.pubapp.uif2app.actions.BodyAddLineAction getBodyAddLineAction_10a6a6() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyAddLineAction#10a6a6") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyAddLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyAddLineAction#10a6a6");
		nc.ui.pubapp.uif2app.actions.BodyAddLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyAddLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyAddLineAction#10a6a6",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyInsertLineAction getBodyInsertLineAction_12cf0d6() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#12cf0d6") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyInsertLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#12cf0d6");
		nc.ui.pubapp.uif2app.actions.BodyInsertLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyInsertLineAction();
		context.put(
				"nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#12cf0d6",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyDelLineAction getBodyDelLineAction_177a164() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#177a164") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyDelLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#177a164");
		nc.ui.pubapp.uif2app.actions.BodyDelLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyDelLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#177a164",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyCopyLineAction getBodyCopyLineAction_104c59b() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#104c59b") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyCopyLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#104c59b");
		nc.ui.pubapp.uif2app.actions.BodyCopyLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyCopyLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#104c59b",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyPasteLineAction getBodyPasteLineAction_495619() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#495619") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyPasteLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#495619");
		nc.ui.pubapp.uif2app.actions.BodyPasteLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyPasteLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#495619",
				bean);
		bean.setClearItems(getManagedList19());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList19() {
		List list = new ArrayList();
		list.add("outputs:cbom_outputsid");
		return list;
	}

	private nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction getBodyPasteToTailActionOutputs() {
		if (context.get("bodyPasteToTailActionOutputs") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction) context
					.get("bodyPasteToTailActionOutputs");
		nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction bean = new nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction();
		context.put("bodyPasteToTailActionOutputs", bean);
		bean.setClearItems(getManagedList20());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList20() {
		List list = new ArrayList();
		list.add("outputs:cbom_outputsid");
		return list;
	}

	private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_b3b2a4() {
		if (context
				.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#b3b2a4") != null)
			return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator) context
					.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#b3b2a4");
		nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
		context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#b3b2a4",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyLineEditAction getBodyLineEditAction_1955767() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#1955767") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyLineEditAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#1955767");
		nc.ui.pubapp.uif2app.actions.BodyLineEditAction bean = new nc.ui.pubapp.uif2app.actions.BodyLineEditAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#1955767",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction getRearrangeRowNoBodyLineAction_19d353c() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#19d353c") != null)
			return (nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#19d353c");
		nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction bean = new nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction();
		context.put(
				"nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#19d353c",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_120f96() {
		if (context
				.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#120f96") != null)
			return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator) context
					.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#120f96");
		nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
		context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#120f96",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.action.BomBodyLineUpAction getBomBodyLineUpAction_1bd65d1() {
		if (context
				.get("nc.ui.bd.bom.bom0202.action.BomBodyLineUpAction#1bd65d1") != null)
			return (nc.ui.bd.bom.bom0202.action.BomBodyLineUpAction) context
					.get("nc.ui.bd.bom.bom0202.action.BomBodyLineUpAction#1bd65d1");
		nc.ui.bd.bom.bom0202.action.BomBodyLineUpAction bean = new nc.ui.bd.bom.bom0202.action.BomBodyLineUpAction();
		context.put("nc.ui.bd.bom.bom0202.action.BomBodyLineUpAction#1bd65d1",
				bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.action.BomBodyLineDownAction getBomBodyLineDownAction_167898a() {
		if (context
				.get("nc.ui.bd.bom.bom0202.action.BomBodyLineDownAction#167898a") != null)
			return (nc.ui.bd.bom.bom0202.action.BomBodyLineDownAction) context
					.get("nc.ui.bd.bom.bom0202.action.BomBodyLineDownAction#167898a");
		nc.ui.bd.bom.bom0202.action.BomBodyLineDownAction bean = new nc.ui.bd.bom.bom0202.action.BomBodyLineDownAction();
		context.put(
				"nc.ui.bd.bom.bom0202.action.BomBodyLineDownAction#167898a",
				bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.components.grand.action.MaxMinBodyAction getMaxMinBodyAction_76f4fb() {
		if (context
				.get("nc.ui.pubapp.uif2app.components.grand.action.MaxMinBodyAction#76f4fb") != null)
			return (nc.ui.pubapp.uif2app.components.grand.action.MaxMinBodyAction) context
					.get("nc.ui.pubapp.uif2app.components.grand.action.MaxMinBodyAction#76f4fb");
		nc.ui.pubapp.uif2app.components.grand.action.MaxMinBodyAction bean = new nc.ui.pubapp.uif2app.components.grand.action.MaxMinBodyAction();
		context.put(
				"nc.ui.pubapp.uif2app.components.grand.action.MaxMinBodyAction#76f4fb",
				bean);
		bean.setMainGrandModel(getMainGrandModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.view.BomGrandBillForm getSunbillFormEditor() {
		if (context.get("sunbillFormEditor") != null)
			return (nc.ui.bd.bom.bom0202.view.BomGrandBillForm) context
					.get("sunbillFormEditor");
		nc.ui.bd.bom.bom0202.view.BomGrandBillForm bean = new nc.ui.bd.bom.bom0202.view.BomGrandBillForm();
		context.put("sunbillFormEditor", bean);
		bean.setMainModel(getMainGrandModel());
		bean.setModel(getManageAppModel2());
		bean.setTemplateContainer(getTemplateContainer());
		bean.setTemplateNotNullValidate(true);
		bean.setNodekey("bomsun");
		bean.setAutoExecLoadRelationItem(false);
		bean.setBodyActionMap(getGrandbodyActionsMap());
		bean.setComponentValueManager(getGrandValueAdapt());
		bean.setShowOrgPanel(false);
		bean.setUserdefitemPreparator(getCompositeBillDataPrepare_1cdb60f());
		bean.initUI();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare getCompositeBillDataPrepare_1cdb60f() {
		if (context
				.get("nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare#1cdb60f") != null)
			return (nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare) context
					.get("nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare#1cdb60f");
		nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare();
		context.put(
				"nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare#1cdb60f",
				bean);
		bean.setBillDataPrepares(getManagedList21());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList21() {
		List list = new ArrayList();
		list.add(getUserdefitemPreparator());
		list.add(getGrandMarAsstPreparator());
		return list;
	}

	public nc.ui.pubapp.uif2app.components.grand.valueStrategy.GrandPanelValueAdapter getGrandValueAdapt() {
		if (context.get("grandValueAdapt") != null)
			return (nc.ui.pubapp.uif2app.components.grand.valueStrategy.GrandPanelValueAdapter) context
					.get("grandValueAdapt");
		nc.ui.pubapp.uif2app.components.grand.valueStrategy.GrandPanelValueAdapter bean = new nc.ui.pubapp.uif2app.components.grand.valueStrategy.GrandPanelValueAdapter();
		context.put("grandValueAdapt", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.components.grand.mediator.GrandMouseClickShowPanelMediator getMouseClickShowPanelMediator() {
		if (context.get("mouseClickShowPanelMediator") != null)
			return (nc.ui.pubapp.uif2app.components.grand.mediator.GrandMouseClickShowPanelMediator) context
					.get("mouseClickShowPanelMediator");
		nc.ui.pubapp.uif2app.components.grand.mediator.GrandMouseClickShowPanelMediator bean = new nc.ui.pubapp.uif2app.components.grand.mediator.GrandMouseClickShowPanelMediator();
		context.put("mouseClickShowPanelMediator", bean);
		bean.setListView(getListView());
		bean.setShowUpComponent(getZhuzisuncard());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel getCardInfoPnl() {
		if (context.get("cardInfoPnl") != null)
			return (nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel) context
					.get("cardInfoPnl");
		nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel bean = new nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel();
		context.put("cardInfoPnl", bean);
		bean.setActions(getManagedList22());
		bean.setTitleAction(getReturnaction());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList22() {
		List list = new ArrayList();
		list.add(getFirstLineAction());
		list.add(getPreLineAction());
		list.add(getNextLineAction());
		list.add(getLastLineAction());
		list.add(getActionsBar_ActionsBarSeparator_6bbab0());
		list.add(getMaxMinHeadAction());
		return list;
	}

	private nc.ui.uif2.actions.FirstLineAction getFirstLineAction() {
		if (context.get("firstLineAction") != null)
			return (nc.ui.uif2.actions.FirstLineAction) context
					.get("firstLineAction");
		nc.ui.uif2.actions.FirstLineAction bean = new nc.ui.uif2.actions.FirstLineAction();
		context.put("firstLineAction", bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.actions.PreLineAction getPreLineAction() {
		if (context.get("preLineAction") != null)
			return (nc.ui.uif2.actions.PreLineAction) context
					.get("preLineAction");
		nc.ui.uif2.actions.PreLineAction bean = new nc.ui.uif2.actions.PreLineAction();
		context.put("preLineAction", bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.actions.NextLineAction getNextLineAction() {
		if (context.get("nextLineAction") != null)
			return (nc.ui.uif2.actions.NextLineAction) context
					.get("nextLineAction");
		nc.ui.uif2.actions.NextLineAction bean = new nc.ui.uif2.actions.NextLineAction();
		context.put("nextLineAction", bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.actions.LastLineAction getLastLineAction() {
		if (context.get("lastLineAction") != null)
			return (nc.ui.uif2.actions.LastLineAction) context
					.get("lastLineAction");
		nc.ui.uif2.actions.LastLineAction bean = new nc.ui.uif2.actions.LastLineAction();
		context.put("lastLineAction", bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_6bbab0() {
		if (context
				.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#6bbab0") != null)
			return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator) context
					.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#6bbab0");
		nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
		context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#6bbab0",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.components.grand.action.MaxMinHeadAction getMaxMinHeadAction() {
		if (context.get("maxMinHeadAction") != null)
			return (nc.ui.pubapp.uif2app.components.grand.action.MaxMinHeadAction) context
					.get("maxMinHeadAction");
		nc.ui.pubapp.uif2app.components.grand.action.MaxMinHeadAction bean = new nc.ui.pubapp.uif2app.components.grand.action.MaxMinHeadAction();
		context.put("maxMinHeadAction", bean);
		bean.setMainGrandModel(getMainGrandModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.UEReturnAction getReturnaction() {
		if (context.get("returnaction") != null)
			return (nc.ui.pubapp.uif2app.actions.UEReturnAction) context
					.get("returnaction");
		nc.ui.pubapp.uif2app.actions.UEReturnAction bean = new nc.ui.pubapp.uif2app.actions.UEReturnAction();
		context.put("returnaction", bean);
		bean.setGoComponent(getZhuzisun());
		bean.setSaveAction(getSaveAction());
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public java.util.HashMap getGrandbodyActionsMap() {
		if (context.get("grandbodyActionsMap") != null)
			return (java.util.HashMap) context.get("grandbodyActionsMap");
		java.util.HashMap bean = new java.util.HashMap(getManagedMap9());
		context.put("grandbodyActionsMap", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private Map getManagedMap9() {
		Map map = new HashMap();
		map.put("wips", getManagedList23());
		map.put("pos", getManagedList26());
		map.put("repl", getManagedList29());
		map.put("loss", getManagedList32());
		return map;
	}

	private List getManagedList23() {
		List list = new ArrayList();
		list.add(getGrandBodyAddLineAction_adb8a1());
		list.add(getBodyInsertLineAction_134546d());
		list.add(getBodyDelLineAction_92d53d());
		list.add(getBodyCopyLineAction_1edada1());
		list.add(getBodyPasteLineAction_1222c2b());
		list.add(getBodyPasteToTailActionwips());
		list.add(getActionsBar_ActionsBarSeparator_81f40d());
		list.add(getBodyLineEditAction_1bd984f());
		list.add(getRearrangeRowNoBodyLineAction_5a6e81());
		list.add(getActionsBar_ActionsBarSeparator_10ceff4());
		list.add(getMaxmincardgrandaction());
		return list;
	}

	private nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction getGrandBodyAddLineAction_adb8a1() {
		if (context
				.get("nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction#adb8a1") != null)
			return (nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction) context
					.get("nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction#adb8a1");
		nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction bean = new nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction();
		context.put(
				"nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction#adb8a1",
				bean);
		bean.setMainForm(getBillFormEditor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyInsertLineAction getBodyInsertLineAction_134546d() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#134546d") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyInsertLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#134546d");
		nc.ui.pubapp.uif2app.actions.BodyInsertLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyInsertLineAction();
		context.put(
				"nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#134546d",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyDelLineAction getBodyDelLineAction_92d53d() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#92d53d") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyDelLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#92d53d");
		nc.ui.pubapp.uif2app.actions.BodyDelLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyDelLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#92d53d",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyCopyLineAction getBodyCopyLineAction_1edada1() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#1edada1") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyCopyLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#1edada1");
		nc.ui.pubapp.uif2app.actions.BodyCopyLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyCopyLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#1edada1",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyPasteLineAction getBodyPasteLineAction_1222c2b() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#1222c2b") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyPasteLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#1222c2b");
		nc.ui.pubapp.uif2app.actions.BodyPasteLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyPasteLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#1222c2b",
				bean);
		bean.setClearItems(getManagedList24());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList24() {
		List list = new ArrayList();
		list.add("wips:cbom_wipid");
		return list;
	}

	private nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction getBodyPasteToTailActionwips() {
		if (context.get("bodyPasteToTailActionwips") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction) context
					.get("bodyPasteToTailActionwips");
		nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction bean = new nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction();
		context.put("bodyPasteToTailActionwips", bean);
		bean.setClearItems(getManagedList25());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList25() {
		List list = new ArrayList();
		list.add("wips:cbom_wipid");
		return list;
	}

	private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_81f40d() {
		if (context
				.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#81f40d") != null)
			return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator) context
					.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#81f40d");
		nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
		context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#81f40d",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyLineEditAction getBodyLineEditAction_1bd984f() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#1bd984f") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyLineEditAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#1bd984f");
		nc.ui.pubapp.uif2app.actions.BodyLineEditAction bean = new nc.ui.pubapp.uif2app.actions.BodyLineEditAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#1bd984f",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction getRearrangeRowNoBodyLineAction_5a6e81() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#5a6e81") != null)
			return (nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#5a6e81");
		nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction bean = new nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction();
		context.put(
				"nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#5a6e81",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_10ceff4() {
		if (context
				.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#10ceff4") != null)
			return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator) context
					.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#10ceff4");
		nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
		context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#10ceff4",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList26() {
		List list = new ArrayList();
		list.add(getGrandBodyAddLineAction_5d7cb5());
		list.add(getBodyInsertLineAction_1a68ccf());
		list.add(getBodyDelLineAction_1668bb5());
		list.add(getBodyCopyLineAction_15e2727());
		list.add(getBodyPasteLineAction_5c8dd7());
		list.add(getBodyPasteToTailActionpos());
		list.add(getActionsBar_ActionsBarSeparator_642184());
		list.add(getBodyLineEditAction_1d474cc());
		list.add(getRearrangeRowNoBodyLineAction_1403a8a());
		list.add(getActionsBar_ActionsBarSeparator_1c34906());
		list.add(getMaxmincardgrandaction());
		return list;
	}

	private nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction getGrandBodyAddLineAction_5d7cb5() {
		if (context
				.get("nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction#5d7cb5") != null)
			return (nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction) context
					.get("nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction#5d7cb5");
		nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction bean = new nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction();
		context.put(
				"nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction#5d7cb5",
				bean);
		bean.setMainForm(getBillFormEditor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyInsertLineAction getBodyInsertLineAction_1a68ccf() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#1a68ccf") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyInsertLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#1a68ccf");
		nc.ui.pubapp.uif2app.actions.BodyInsertLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyInsertLineAction();
		context.put(
				"nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#1a68ccf",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyDelLineAction getBodyDelLineAction_1668bb5() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#1668bb5") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyDelLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#1668bb5");
		nc.ui.pubapp.uif2app.actions.BodyDelLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyDelLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#1668bb5",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyCopyLineAction getBodyCopyLineAction_15e2727() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#15e2727") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyCopyLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#15e2727");
		nc.ui.pubapp.uif2app.actions.BodyCopyLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyCopyLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#15e2727",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyPasteLineAction getBodyPasteLineAction_5c8dd7() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#5c8dd7") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyPasteLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#5c8dd7");
		nc.ui.pubapp.uif2app.actions.BodyPasteLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyPasteLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#5c8dd7",
				bean);
		bean.setClearItems(getManagedList27());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList27() {
		List list = new ArrayList();
		list.add("pos:cbom_positionid");
		return list;
	}

	private nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction getBodyPasteToTailActionpos() {
		if (context.get("bodyPasteToTailActionpos") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction) context
					.get("bodyPasteToTailActionpos");
		nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction bean = new nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction();
		context.put("bodyPasteToTailActionpos", bean);
		bean.setClearItems(getManagedList28());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList28() {
		List list = new ArrayList();
		list.add("pos:cbom_positionid");
		return list;
	}

	private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_642184() {
		if (context
				.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#642184") != null)
			return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator) context
					.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#642184");
		nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
		context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#642184",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyLineEditAction getBodyLineEditAction_1d474cc() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#1d474cc") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyLineEditAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#1d474cc");
		nc.ui.pubapp.uif2app.actions.BodyLineEditAction bean = new nc.ui.pubapp.uif2app.actions.BodyLineEditAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#1d474cc",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction getRearrangeRowNoBodyLineAction_1403a8a() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#1403a8a") != null)
			return (nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#1403a8a");
		nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction bean = new nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction();
		context.put(
				"nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#1403a8a",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1c34906() {
		if (context
				.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1c34906") != null)
			return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator) context
					.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1c34906");
		nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
		context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1c34906",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList29() {
		List list = new ArrayList();
		list.add(getGrandBodyAddLineAction_1ef5c15());
		list.add(getBodyInsertLineAction_1867c35());
		list.add(getBodyDelLineAction_448cc5());
		list.add(getBodyCopyLineAction_75dbb3());
		list.add(getBodyPasteLineAction_1aadda0());
		list.add(getBodyPasteToTailActionloss());
		list.add(getActionsBar_ActionsBarSeparator_bc3855());
		list.add(getBodyLineEditAction_1166d72());
		list.add(getRearrangeRowNoBodyLineAction_16b622e());
		list.add(getActionsBar_ActionsBarSeparator_faf535());
		list.add(getMaxmincardgrandaction());
		return list;
	}

	private nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction getGrandBodyAddLineAction_1ef5c15() {
		if (context
				.get("nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction#1ef5c15") != null)
			return (nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction) context
					.get("nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction#1ef5c15");
		nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction bean = new nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction();
		context.put(
				"nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction#1ef5c15",
				bean);
		bean.setMainForm(getBillFormEditor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyInsertLineAction getBodyInsertLineAction_1867c35() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#1867c35") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyInsertLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#1867c35");
		nc.ui.pubapp.uif2app.actions.BodyInsertLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyInsertLineAction();
		context.put(
				"nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#1867c35",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyDelLineAction getBodyDelLineAction_448cc5() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#448cc5") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyDelLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#448cc5");
		nc.ui.pubapp.uif2app.actions.BodyDelLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyDelLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#448cc5",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyCopyLineAction getBodyCopyLineAction_75dbb3() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#75dbb3") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyCopyLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#75dbb3");
		nc.ui.pubapp.uif2app.actions.BodyCopyLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyCopyLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#75dbb3",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyPasteLineAction getBodyPasteLineAction_1aadda0() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#1aadda0") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyPasteLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#1aadda0");
		nc.ui.pubapp.uif2app.actions.BodyPasteLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyPasteLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#1aadda0",
				bean);
		bean.setClearItems(getManagedList30());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList30() {
		List list = new ArrayList();
		list.add("repl:cbom_replaceid");
		return list;
	}

	private nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction getBodyPasteToTailActionloss() {
		if (context.get("bodyPasteToTailActionloss") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction) context
					.get("bodyPasteToTailActionloss");
		nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction bean = new nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction();
		context.put("bodyPasteToTailActionloss", bean);
		bean.setClearItems(getManagedList31());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList31() {
		List list = new ArrayList();
		list.add("repl:cbom_replaceid");
		return list;
	}

	private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_bc3855() {
		if (context
				.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#bc3855") != null)
			return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator) context
					.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#bc3855");
		nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
		context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#bc3855",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyLineEditAction getBodyLineEditAction_1166d72() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#1166d72") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyLineEditAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#1166d72");
		nc.ui.pubapp.uif2app.actions.BodyLineEditAction bean = new nc.ui.pubapp.uif2app.actions.BodyLineEditAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#1166d72",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction getRearrangeRowNoBodyLineAction_16b622e() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#16b622e") != null)
			return (nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#16b622e");
		nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction bean = new nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction();
		context.put(
				"nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#16b622e",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_faf535() {
		if (context
				.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#faf535") != null)
			return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator) context
					.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#faf535");
		nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
		context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#faf535",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList32() {
		List list = new ArrayList();
		list.add(getGrandBodyAddLineAction_1f87241());
		list.add(getBodyInsertLineAction_154a03());
		list.add(getBodyDelLineAction_1ae2cda());
		list.add(getBodyCopyLineAction_29e19());
		list.add(getBodyPasteLineAction_1d98bba());
		list.add(getBodyPasteToTailActionrepl());
		list.add(getActionsBar_ActionsBarSeparator_1646669());
		list.add(getBodyLineEditAction_7fb4bb());
		list.add(getRearrangeRowNoBodyLineAction_32927b());
		list.add(getActionsBar_ActionsBarSeparator_2efa94());
		list.add(getMaxmincardgrandaction());
		return list;
	}

	private nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction getGrandBodyAddLineAction_1f87241() {
		if (context
				.get("nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction#1f87241") != null)
			return (nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction) context
					.get("nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction#1f87241");
		nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction bean = new nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction();
		context.put(
				"nc.ui.pubapp.uif2app.components.grand.action.GrandBodyAddLineAction#1f87241",
				bean);
		bean.setMainForm(getBillFormEditor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyInsertLineAction getBodyInsertLineAction_154a03() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#154a03") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyInsertLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#154a03");
		nc.ui.pubapp.uif2app.actions.BodyInsertLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyInsertLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyInsertLineAction#154a03",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyDelLineAction getBodyDelLineAction_1ae2cda() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#1ae2cda") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyDelLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#1ae2cda");
		nc.ui.pubapp.uif2app.actions.BodyDelLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyDelLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyDelLineAction#1ae2cda",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyCopyLineAction getBodyCopyLineAction_29e19() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#29e19") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyCopyLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#29e19");
		nc.ui.pubapp.uif2app.actions.BodyCopyLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyCopyLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyCopyLineAction#29e19",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyPasteLineAction getBodyPasteLineAction_1d98bba() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#1d98bba") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyPasteLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#1d98bba");
		nc.ui.pubapp.uif2app.actions.BodyPasteLineAction bean = new nc.ui.pubapp.uif2app.actions.BodyPasteLineAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyPasteLineAction#1d98bba",
				bean);
		bean.setClearItems(getManagedList33());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList33() {
		List list = new ArrayList();
		list.add("loss:cbom_lossid");
		return list;
	}

	private nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction getBodyPasteToTailActionrepl() {
		if (context.get("bodyPasteToTailActionrepl") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction) context
					.get("bodyPasteToTailActionrepl");
		nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction bean = new nc.ui.pubapp.uif2app.actions.BodyPasteToTailAction();
		context.put("bodyPasteToTailActionrepl", bean);
		bean.setClearItems(getManagedList34());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList34() {
		List list = new ArrayList();
		list.add("loss:cbom_lossid");
		return list;
	}

	private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_1646669() {
		if (context
				.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1646669") != null)
			return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator) context
					.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1646669");
		nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
		context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#1646669",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.BodyLineEditAction getBodyLineEditAction_7fb4bb() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#7fb4bb") != null)
			return (nc.ui.pubapp.uif2app.actions.BodyLineEditAction) context
					.get("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#7fb4bb");
		nc.ui.pubapp.uif2app.actions.BodyLineEditAction bean = new nc.ui.pubapp.uif2app.actions.BodyLineEditAction();
		context.put("nc.ui.pubapp.uif2app.actions.BodyLineEditAction#7fb4bb",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction getRearrangeRowNoBodyLineAction_32927b() {
		if (context
				.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#32927b") != null)
			return (nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction) context
					.get("nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#32927b");
		nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction bean = new nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction();
		context.put(
				"nc.ui.pubapp.uif2app.actions.RearrangeRowNoBodyLineAction#32927b",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pub.beans.ActionsBar.ActionsBarSeparator getActionsBar_ActionsBarSeparator_2efa94() {
		if (context
				.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#2efa94") != null)
			return (nc.ui.pub.beans.ActionsBar.ActionsBarSeparator) context
					.get("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#2efa94");
		nc.ui.pub.beans.ActionsBar.ActionsBarSeparator bean = new nc.ui.pub.beans.ActionsBar.ActionsBarSeparator();
		context.put("nc.ui.pub.beans.ActionsBar.ActionsBarSeparator#2efa94",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.components.grand.action.MaxMinCardGrandAction getMaxmincardgrandaction() {
		if (context.get("maxmincardgrandaction") != null)
			return (nc.ui.pubapp.uif2app.components.grand.action.MaxMinCardGrandAction) context
					.get("maxmincardgrandaction");
		nc.ui.pubapp.uif2app.components.grand.action.MaxMinCardGrandAction bean = new nc.ui.pubapp.uif2app.components.grand.action.MaxMinCardGrandAction();
		context.put("maxmincardgrandaction", bean);
		bean.setMainGrandModel(getMainGrandModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.model.BillBodySortMediator getBodySortMediator() {
		if (context.get("bodySortMediator") != null)
			return (nc.ui.pubapp.uif2app.model.BillBodySortMediator) context
					.get("bodySortMediator");
		nc.ui.pubapp.uif2app.model.BillBodySortMediator bean = new nc.ui.pubapp.uif2app.model.BillBodySortMediator(
				getManageAppModel(), getBillFormEditor(), getListView());
		context.put("bodySortMediator", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.components.grand.mediator.GrandBillBodySortMediator getSunbodySortMediator() {
		if (context.get("sunbodySortMediator") != null)
			return (nc.ui.pubapp.uif2app.components.grand.mediator.GrandBillBodySortMediator) context
					.get("sunbodySortMediator");
		nc.ui.pubapp.uif2app.components.grand.mediator.GrandBillBodySortMediator bean = new nc.ui.pubapp.uif2app.components.grand.mediator.GrandBillBodySortMediator(
				getManageAppModel2(), getSunbillFormEditor(), getSunlistView());
		context.put("sunbodySortMediator", bean);
		bean.setMainBillForm(getBillFormEditor());
		bean.setMainBillListView(getListView());
		bean.setMainGrandModel(getMainGrandModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.event.ChildrenPicky getChildrenPicky() {
		if (context.get("childrenPicky") != null)
			return (nc.ui.pubapp.uif2app.event.ChildrenPicky) context
					.get("childrenPicky");
		nc.ui.pubapp.uif2app.event.ChildrenPicky bean = new nc.ui.pubapp.uif2app.event.ChildrenPicky();
		context.put("childrenPicky", bean);
		bean.setBillform(getBillFormEditor());
		bean.setBodyVoClasses(getManagedList35());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList35() {
		List list = new ArrayList();
		list.add("nc.vo.bd.bom.bom0202.entity.BomItemVO");
		list.add("nc.vo.bd.bom.bom0202.entity.BomOutputsVO");
		list.add("nc.vo.bd.bom.bom0202.entity.BomUseOrgVO");
		return list;
	}

	public nc.ui.pubapp.uif2app.model.AppEventHandlerMediator getAppEventHandlerMediator() {
		if (context.get("appEventHandlerMediator") != null)
			return (nc.ui.pubapp.uif2app.model.AppEventHandlerMediator) context
					.get("appEventHandlerMediator");
		nc.ui.pubapp.uif2app.model.AppEventHandlerMediator bean = new nc.ui.pubapp.uif2app.model.AppEventHandlerMediator();
		context.put("appEventHandlerMediator", bean);
		bean.setModel(getManageAppModel());
		bean.setHandlerGroup(getManagedList36());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList36() {
		List list = new ArrayList();
		list.add(getEventHandlerGroup_1e5c654());
		list.add(getEventHandlerGroup_12f8a8d());
		list.add(getEventHandlerGroup_d95df9());
		list.add(getEventHandlerGroup_3aa923());
		list.add(getEventHandlerGroup_176587a());
		list.add(getEventHandlerGroup_939541());
		list.add(getEventHandlerGroup_11bdbab());
		list.add(getEventHandlerGroup_30a7e4());
		list.add(getEventHandlerGroup_9de09c());
		return list;
	}

	private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_1e5c654() {
		if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1e5c654") != null)
			return (nc.ui.pubapp.uif2app.event.EventHandlerGroup) context
					.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1e5c654");
		nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
		context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1e5c654",
				bean);
		bean.setEvent("nc.ui.pubapp.uif2app.event.OrgChangedEvent");
		bean.setHandler(getBomOrgChangeHandler_4516f3());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.handler.BomOrgChangeHandler getBomOrgChangeHandler_4516f3() {
		if (context
				.get("nc.ui.bd.bom.bom0202.handler.BomOrgChangeHandler#4516f3") != null)
			return (nc.ui.bd.bom.bom0202.handler.BomOrgChangeHandler) context
					.get("nc.ui.bd.bom.bom0202.handler.BomOrgChangeHandler#4516f3");
		nc.ui.bd.bom.bom0202.handler.BomOrgChangeHandler bean = new nc.ui.bd.bom.bom0202.handler.BomOrgChangeHandler();
		context.put("nc.ui.bd.bom.bom0202.handler.BomOrgChangeHandler#4516f3",
				bean);
		bean.setBillForm(getBillFormEditor());
		bean.setSunBillform(getSunbillFormEditor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_12f8a8d() {
		if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#12f8a8d") != null)
			return (nc.ui.pubapp.uif2app.event.EventHandlerGroup) context
					.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#12f8a8d");
		nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
		context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#12f8a8d",
				bean);
		bean.setEvent("nc.ui.pubapp.uif2app.event.billform.AddEvent");
		bean.setHandler(getBomAddEventHandler_3fc735());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.handler.BomAddEventHandler getBomAddEventHandler_3fc735() {
		if (context
				.get("nc.ui.bd.bom.bom0202.handler.BomAddEventHandler#3fc735") != null)
			return (nc.ui.bd.bom.bom0202.handler.BomAddEventHandler) context
					.get("nc.ui.bd.bom.bom0202.handler.BomAddEventHandler#3fc735");
		nc.ui.bd.bom.bom0202.handler.BomAddEventHandler bean = new nc.ui.bd.bom.bom0202.handler.BomAddEventHandler();
		context.put("nc.ui.bd.bom.bom0202.handler.BomAddEventHandler#3fc735",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_d95df9() {
		if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#d95df9") != null)
			return (nc.ui.pubapp.uif2app.event.EventHandlerGroup) context
					.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#d95df9");
		nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
		context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#d95df9", bean);
		bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent");
		bean.setPicky(getChildrenPicky());
		bean.setHandler(getBomHeadTailAfterEditHandler_3164ee());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.handler.BomHeadTailAfterEditHandler getBomHeadTailAfterEditHandler_3164ee() {
		if (context
				.get("nc.ui.bd.bom.bom0202.handler.BomHeadTailAfterEditHandler#3164ee") != null)
			return (nc.ui.bd.bom.bom0202.handler.BomHeadTailAfterEditHandler) context
					.get("nc.ui.bd.bom.bom0202.handler.BomHeadTailAfterEditHandler#3164ee");
		nc.ui.bd.bom.bom0202.handler.BomHeadTailAfterEditHandler bean = new nc.ui.bd.bom.bom0202.handler.BomHeadTailAfterEditHandler();
		context.put(
				"nc.ui.bd.bom.bom0202.handler.BomHeadTailAfterEditHandler#3164ee",
				bean);
		bean.setSunBillform(getSunbillFormEditor());
		bean.setBillForm(getZhuzisuncard());
		bean.initMap();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_3aa923() {
		if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#3aa923") != null)
			return (nc.ui.pubapp.uif2app.event.EventHandlerGroup) context
					.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#3aa923");
		nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
		context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#3aa923", bean);
		bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent");
		bean.setPicky(getChildrenPicky());
		bean.setHandler(getBomHeadTailBeforeEditHandler_a8c571());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.handler.BomHeadTailBeforeEditHandler getBomHeadTailBeforeEditHandler_a8c571() {
		if (context
				.get("nc.ui.bd.bom.bom0202.handler.BomHeadTailBeforeEditHandler#a8c571") != null)
			return (nc.ui.bd.bom.bom0202.handler.BomHeadTailBeforeEditHandler) context
					.get("nc.ui.bd.bom.bom0202.handler.BomHeadTailBeforeEditHandler#a8c571");
		nc.ui.bd.bom.bom0202.handler.BomHeadTailBeforeEditHandler bean = new nc.ui.bd.bom.bom0202.handler.BomHeadTailBeforeEditHandler();
		context.put(
				"nc.ui.bd.bom.bom0202.handler.BomHeadTailBeforeEditHandler#a8c571",
				bean);
		bean.initMap();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_176587a() {
		if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#176587a") != null)
			return (nc.ui.pubapp.uif2app.event.EventHandlerGroup) context
					.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#176587a");
		nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
		context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#176587a",
				bean);
		bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent");
		bean.setPicky(getChildrenPicky());
		bean.setHandler(getBomBodyBeforeEditHandler_1364335());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.handler.BomBodyBeforeEditHandler getBomBodyBeforeEditHandler_1364335() {
		if (context
				.get("nc.ui.bd.bom.bom0202.handler.BomBodyBeforeEditHandler#1364335") != null)
			return (nc.ui.bd.bom.bom0202.handler.BomBodyBeforeEditHandler) context
					.get("nc.ui.bd.bom.bom0202.handler.BomBodyBeforeEditHandler#1364335");
		nc.ui.bd.bom.bom0202.handler.BomBodyBeforeEditHandler bean = new nc.ui.bd.bom.bom0202.handler.BomBodyBeforeEditHandler();
		context.put(
				"nc.ui.bd.bom.bom0202.handler.BomBodyBeforeEditHandler#1364335",
				bean);
		bean.initMap();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_939541() {
		if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#939541") != null)
			return (nc.ui.pubapp.uif2app.event.EventHandlerGroup) context
					.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#939541");
		nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
		context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#939541", bean);
		bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent");
		bean.setPicky(getChildrenPicky());
		bean.setHandler(getBomBodyAfterEditHandler_aae8b4());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.handler.BomBodyAfterEditHandler getBomBodyAfterEditHandler_aae8b4() {
		if (context
				.get("nc.ui.bd.bom.bom0202.handler.BomBodyAfterEditHandler#aae8b4") != null)
			return (nc.ui.bd.bom.bom0202.handler.BomBodyAfterEditHandler) context
					.get("nc.ui.bd.bom.bom0202.handler.BomBodyAfterEditHandler#aae8b4");
		nc.ui.bd.bom.bom0202.handler.BomBodyAfterEditHandler bean = new nc.ui.bd.bom.bom0202.handler.BomBodyAfterEditHandler();
		context.put(
				"nc.ui.bd.bom.bom0202.handler.BomBodyAfterEditHandler#aae8b4",
				bean);
		bean.setSunBillform(getSunbillFormEditor());
		bean.setBillForm(getZhuzisuncard());
		bean.initMap();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_11bdbab() {
		if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#11bdbab") != null)
			return (nc.ui.pubapp.uif2app.event.EventHandlerGroup) context
					.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#11bdbab");
		nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
		context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#11bdbab",
				bean);
		bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardBodyRowEditEvent");
		bean.setPicky(getChildrenPicky());
		bean.setHandler(getBomBodyBeforeRowEditHandler_12bee15());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.handler.BomBodyBeforeRowEditHandler getBomBodyBeforeRowEditHandler_12bee15() {
		if (context
				.get("nc.ui.bd.bom.bom0202.handler.BomBodyBeforeRowEditHandler#12bee15") != null)
			return (nc.ui.bd.bom.bom0202.handler.BomBodyBeforeRowEditHandler) context
					.get("nc.ui.bd.bom.bom0202.handler.BomBodyBeforeRowEditHandler#12bee15");
		nc.ui.bd.bom.bom0202.handler.BomBodyBeforeRowEditHandler bean = new nc.ui.bd.bom.bom0202.handler.BomBodyBeforeRowEditHandler();
		context.put(
				"nc.ui.bd.bom.bom0202.handler.BomBodyBeforeRowEditHandler#12bee15",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_30a7e4() {
		if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#30a7e4") != null)
			return (nc.ui.pubapp.uif2app.event.EventHandlerGroup) context
					.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#30a7e4");
		nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
		context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#30a7e4", bean);
		bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardBodyAfterRowEditEvent");
		bean.setPicky(getChildrenPicky());
		bean.setHandler(getBomBodyAfterRowEditHandler_1f2c053());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.handler.BomBodyAfterRowEditHandler getBomBodyAfterRowEditHandler_1f2c053() {
		if (context
				.get("nc.ui.bd.bom.bom0202.handler.BomBodyAfterRowEditHandler#1f2c053") != null)
			return (nc.ui.bd.bom.bom0202.handler.BomBodyAfterRowEditHandler) context
					.get("nc.ui.bd.bom.bom0202.handler.BomBodyAfterRowEditHandler#1f2c053");
		nc.ui.bd.bom.bom0202.handler.BomBodyAfterRowEditHandler bean = new nc.ui.bd.bom.bom0202.handler.BomBodyAfterRowEditHandler();
		context.put(
				"nc.ui.bd.bom.bom0202.handler.BomBodyAfterRowEditHandler#1f2c053",
				bean);
		bean.initMap();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_9de09c() {
		if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#9de09c") != null)
			return (nc.ui.pubapp.uif2app.event.EventHandlerGroup) context
					.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#9de09c");
		nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
		context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#9de09c", bean);
		bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardBodyRowChangedEvent");
		bean.setPicky(getChildrenPicky());
		bean.setHandler(getBomSunBodyChangeRowHandler_5e89a1());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.handler.BomSunBodyChangeRowHandler getBomSunBodyChangeRowHandler_5e89a1() {
		if (context
				.get("nc.ui.bd.bom.bom0202.handler.BomSunBodyChangeRowHandler#5e89a1") != null)
			return (nc.ui.bd.bom.bom0202.handler.BomSunBodyChangeRowHandler) context
					.get("nc.ui.bd.bom.bom0202.handler.BomSunBodyChangeRowHandler#5e89a1");
		nc.ui.bd.bom.bom0202.handler.BomSunBodyChangeRowHandler bean = new nc.ui.bd.bom.bom0202.handler.BomSunBodyChangeRowHandler();
		context.put(
				"nc.ui.bd.bom.bom0202.handler.BomSunBodyChangeRowHandler#5e89a1",
				bean);
		bean.setBillformeditor(getBillFormEditor());
		bean.initMap();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.event.ChildrenPicky getSunPicky() {
		if (context.get("sunPicky") != null)
			return (nc.ui.pubapp.uif2app.event.ChildrenPicky) context
					.get("sunPicky");
		nc.ui.pubapp.uif2app.event.ChildrenPicky bean = new nc.ui.pubapp.uif2app.event.ChildrenPicky();
		context.put("sunPicky", bean);
		bean.setBillform(getSunbillFormEditor());
		bean.setBodyVoClasses(getManagedList37());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList37() {
		List list = new ArrayList();
		list.add("nc.vo.bd.bom.bom0202.entity.BomPosVO");
		list.add("nc.vo.bd.bom.bom0202.entity.BomWipVO");
		list.add("nc.vo.bd.bom.bom0202.entity.BomReplVO");
		list.add("nc.vo.bd.bom.bom0202.entity.BomLossVO");
		return list;
	}

	public nc.ui.pubapp.uif2app.model.AppEventHandlerMediator getSunAppEventHandlerMediator() {
		if (context.get("sunAppEventHandlerMediator") != null)
			return (nc.ui.pubapp.uif2app.model.AppEventHandlerMediator) context
					.get("sunAppEventHandlerMediator");
		nc.ui.pubapp.uif2app.model.AppEventHandlerMediator bean = new nc.ui.pubapp.uif2app.model.AppEventHandlerMediator();
		context.put("sunAppEventHandlerMediator", bean);
		bean.setModel(getManageAppModel2());
		bean.setHandlerGroup(getManagedList38());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList38() {
		List list = new ArrayList();
		list.add(getEventHandlerGroup_943e31());
		list.add(getEventHandlerGroup_1f14b6d());
		list.add(getEventHandlerGroup_15990ec());
		list.add(getEventHandlerGroup_172fc18());
		return list;
	}

	private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_943e31() {
		if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#943e31") != null)
			return (nc.ui.pubapp.uif2app.event.EventHandlerGroup) context
					.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#943e31");
		nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
		context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#943e31", bean);
		bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent");
		bean.setPicky(getSunPicky());
		bean.setHandler(getBomSunBodyBeforeEditHandler_1053bba());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.handler.BomSunBodyBeforeEditHandler getBomSunBodyBeforeEditHandler_1053bba() {
		if (context
				.get("nc.ui.bd.bom.bom0202.handler.BomSunBodyBeforeEditHandler#1053bba") != null)
			return (nc.ui.bd.bom.bom0202.handler.BomSunBodyBeforeEditHandler) context
					.get("nc.ui.bd.bom.bom0202.handler.BomSunBodyBeforeEditHandler#1053bba");
		nc.ui.bd.bom.bom0202.handler.BomSunBodyBeforeEditHandler bean = new nc.ui.bd.bom.bom0202.handler.BomSunBodyBeforeEditHandler();
		context.put(
				"nc.ui.bd.bom.bom0202.handler.BomSunBodyBeforeEditHandler#1053bba",
				bean);
		bean.setHeadBillForm(getBillFormEditor());
		bean.initMap();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_1f14b6d() {
		if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1f14b6d") != null)
			return (nc.ui.pubapp.uif2app.event.EventHandlerGroup) context
					.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1f14b6d");
		nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
		context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#1f14b6d",
				bean);
		bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent");
		bean.setPicky(getSunPicky());
		bean.setHandler(getBomSunBodyAfterEditHandler_7f3ec2());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.handler.BomSunBodyAfterEditHandler getBomSunBodyAfterEditHandler_7f3ec2() {
		if (context
				.get("nc.ui.bd.bom.bom0202.handler.BomSunBodyAfterEditHandler#7f3ec2") != null)
			return (nc.ui.bd.bom.bom0202.handler.BomSunBodyAfterEditHandler) context
					.get("nc.ui.bd.bom.bom0202.handler.BomSunBodyAfterEditHandler#7f3ec2");
		nc.ui.bd.bom.bom0202.handler.BomSunBodyAfterEditHandler bean = new nc.ui.bd.bom.bom0202.handler.BomSunBodyAfterEditHandler();
		context.put(
				"nc.ui.bd.bom.bom0202.handler.BomSunBodyAfterEditHandler#7f3ec2",
				bean);
		bean.setHeadBillForm(getBillFormEditor());
		bean.initMap();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_15990ec() {
		if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#15990ec") != null)
			return (nc.ui.pubapp.uif2app.event.EventHandlerGroup) context
					.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#15990ec");
		nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
		context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#15990ec",
				bean);
		bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardBodyAfterRowEditEvent");
		bean.setPicky(getSunPicky());
		bean.setHandler(getBomSunBodyAfterRowEditHandler_8f1b8c());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.handler.BomSunBodyAfterRowEditHandler getBomSunBodyAfterRowEditHandler_8f1b8c() {
		if (context
				.get("nc.ui.bd.bom.bom0202.handler.BomSunBodyAfterRowEditHandler#8f1b8c") != null)
			return (nc.ui.bd.bom.bom0202.handler.BomSunBodyAfterRowEditHandler) context
					.get("nc.ui.bd.bom.bom0202.handler.BomSunBodyAfterRowEditHandler#8f1b8c");
		nc.ui.bd.bom.bom0202.handler.BomSunBodyAfterRowEditHandler bean = new nc.ui.bd.bom.bom0202.handler.BomSunBodyAfterRowEditHandler();
		context.put(
				"nc.ui.bd.bom.bom0202.handler.BomSunBodyAfterRowEditHandler#8f1b8c",
				bean);
		bean.setBillForm(getBillFormEditor());
		bean.initMap();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.event.EventHandlerGroup getEventHandlerGroup_172fc18() {
		if (context.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#172fc18") != null)
			return (nc.ui.pubapp.uif2app.event.EventHandlerGroup) context
					.get("nc.ui.pubapp.uif2app.event.EventHandlerGroup#172fc18");
		nc.ui.pubapp.uif2app.event.EventHandlerGroup bean = new nc.ui.pubapp.uif2app.event.EventHandlerGroup();
		context.put("nc.ui.pubapp.uif2app.event.EventHandlerGroup#172fc18",
				bean);
		bean.setEvent("nc.ui.pubapp.uif2app.event.card.CardBodyRowChangedEvent");
		bean.setPicky(getSunPicky());
		bean.setHandler(getBomSunBodyChangeRowHandler_139a4ce());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.handler.BomSunBodyChangeRowHandler getBomSunBodyChangeRowHandler_139a4ce() {
		if (context
				.get("nc.ui.bd.bom.bom0202.handler.BomSunBodyChangeRowHandler#139a4ce") != null)
			return (nc.ui.bd.bom.bom0202.handler.BomSunBodyChangeRowHandler) context
					.get("nc.ui.bd.bom.bom0202.handler.BomSunBodyChangeRowHandler#139a4ce");
		nc.ui.bd.bom.bom0202.handler.BomSunBodyChangeRowHandler bean = new nc.ui.bd.bom.bom0202.handler.BomSunBodyChangeRowHandler();
		context.put(
				"nc.ui.bd.bom.bom0202.handler.BomSunBodyChangeRowHandler#139a4ce",
				bean);
		bean.setSunBillform(getSunbillFormEditor());
		bean.initMap();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.view.FractionFixMediator getHeadFactionFixMediator() {
		if (context.get("headFactionFixMediator") != null)
			return (nc.ui.pubapp.uif2app.view.FractionFixMediator) context
					.get("headFactionFixMediator");
		nc.ui.pubapp.uif2app.view.FractionFixMediator bean = new nc.ui.pubapp.uif2app.view.FractionFixMediator(
				getBillFormEditor(), getListView());
		context.put("headFactionFixMediator", bean);
		bean.setKeys(getManagedList39());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList39() {
		List list = new ArrayList();
		list.add("hvchangerate");
		return list;
	}

	public nc.ui.pubapp.uif2app.view.FractionFixMediator getBodyFractionFixMediator() {
		if (context.get("bodyFractionFixMediator") != null)
			return (nc.ui.pubapp.uif2app.view.FractionFixMediator) context
					.get("bodyFractionFixMediator");
		nc.ui.pubapp.uif2app.view.FractionFixMediator bean = new nc.ui.pubapp.uif2app.view.FractionFixMediator(
				getBillFormEditor(), getListView());
		context.put("bodyFractionFixMediator", bean);
		bean.setKeyMap(getManagedMap10());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private Map getManagedMap10() {
		Map map = new HashMap();
		map.put("bomitems", getManagedList40());
		map.put("outputs", getManagedList41());
		return map;
	}

	private List getManagedList40() {
		List list = new ArrayList();
		list.add("vchangerate");
		return list;
	}

	private List getManagedList41() {
		List list = new ArrayList();
		list.add("vchangerate");
		return list;
	}

	public nc.ui.pubapp.uif2app.view.FractionFixMediator getGrandFractionFixMediator() {
		if (context.get("grandFractionFixMediator") != null)
			return (nc.ui.pubapp.uif2app.view.FractionFixMediator) context
					.get("grandFractionFixMediator");
		nc.ui.pubapp.uif2app.view.FractionFixMediator bean = new nc.ui.pubapp.uif2app.view.FractionFixMediator(
				getSunbillFormEditor(), getSunlistView());
		context.put("grandFractionFixMediator", bean);
		bean.setKeyMap(getManagedMap11());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private Map getManagedMap11() {
		Map map = new HashMap();
		map.put("repl", getManagedList42());
		return map;
	}

	private List getManagedList42() {
		List list = new ArrayList();
		list.add("vreplaceindex");
		return list;
	}

	public nc.ui.pubapp.uif2app.view.RowNoMediator getRowMediator() {
		if (context.get("rowMediator") != null)
			return (nc.ui.pubapp.uif2app.view.RowNoMediator) context
					.get("rowMediator");
		nc.ui.pubapp.uif2app.view.RowNoMediator bean = new nc.ui.pubapp.uif2app.view.RowNoMediator();
		context.put("rowMediator", bean);
		bean.setModel(getManageAppModel());
		bean.setEditor(getBillFormEditor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.view.RowNoMediator getSunrowMediator() {
		if (context.get("sunrowMediator") != null)
			return (nc.ui.pubapp.uif2app.view.RowNoMediator) context
					.get("sunrowMediator");
		nc.ui.pubapp.uif2app.view.RowNoMediator bean = new nc.ui.pubapp.uif2app.view.RowNoMediator();
		context.put("sunrowMediator", bean);
		bean.setModel(getManageAppModel2());
		bean.setEditor(getSunbillFormEditor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.TangramContainer getContainer() {
		if (context.get("container") != null)
			return (nc.ui.uif2.TangramContainer) context.get("container");
		nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
		context.put("container", bean);
		bean.setModel(getManageAppModel());
		bean.setTangramLayoutRoot(getTBNode_8a95e6());
		bean.initUI();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.tangramlayout.node.TBNode getTBNode_8a95e6() {
		if (context.get("nc.ui.uif2.tangramlayout.node.TBNode#8a95e6") != null)
			return (nc.ui.uif2.tangramlayout.node.TBNode) context
					.get("nc.ui.uif2.tangramlayout.node.TBNode#8a95e6");
		nc.ui.uif2.tangramlayout.node.TBNode bean = new nc.ui.uif2.tangramlayout.node.TBNode();
		context.put("nc.ui.uif2.tangramlayout.node.TBNode#8a95e6", bean);
		bean.setShowMode("CardLayout");
		bean.setTabs(getManagedList43());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList43() {
		List list = new ArrayList();
		list.add(getHSNode_dd595c());
		list.add(getVSNode_c6998b());
		return list;
	}

	private nc.ui.uif2.tangramlayout.node.HSNode getHSNode_dd595c() {
		if (context.get("nc.ui.uif2.tangramlayout.node.HSNode#dd595c") != null)
			return (nc.ui.uif2.tangramlayout.node.HSNode) context
					.get("nc.ui.uif2.tangramlayout.node.HSNode#dd595c");
		nc.ui.uif2.tangramlayout.node.HSNode bean = new nc.ui.uif2.tangramlayout.node.HSNode();
		context.put("nc.ui.uif2.tangramlayout.node.HSNode#dd595c", bean);
		bean.setLeft(getCNode_4aa25());
		bean.setRight(getVSNode_eaac69());
		bean.setDividerLocation(0.22f);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.tangramlayout.node.CNode getCNode_4aa25() {
		if (context.get("nc.ui.uif2.tangramlayout.node.CNode#4aa25") != null)
			return (nc.ui.uif2.tangramlayout.node.CNode) context
					.get("nc.ui.uif2.tangramlayout.node.CNode#4aa25");
		nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
		context.put("nc.ui.uif2.tangramlayout.node.CNode#4aa25", bean);
		bean.setComponent(getQueryArea());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_eaac69() {
		if (context.get("nc.ui.uif2.tangramlayout.node.VSNode#eaac69") != null)
			return (nc.ui.uif2.tangramlayout.node.VSNode) context
					.get("nc.ui.uif2.tangramlayout.node.VSNode#eaac69");
		nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
		context.put("nc.ui.uif2.tangramlayout.node.VSNode#eaac69", bean);
		bean.setUp(getCNode_3ea1b8());
		bean.setDown(getCNode_1eb5c16());
		bean.setDividerLocation(30f);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.tangramlayout.node.CNode getCNode_3ea1b8() {
		if (context.get("nc.ui.uif2.tangramlayout.node.CNode#3ea1b8") != null)
			return (nc.ui.uif2.tangramlayout.node.CNode) context
					.get("nc.ui.uif2.tangramlayout.node.CNode#3ea1b8");
		nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
		context.put("nc.ui.uif2.tangramlayout.node.CNode#3ea1b8", bean);
		bean.setComponent(getQueryInfo());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.tangramlayout.node.CNode getCNode_1eb5c16() {
		if (context.get("nc.ui.uif2.tangramlayout.node.CNode#1eb5c16") != null)
			return (nc.ui.uif2.tangramlayout.node.CNode) context
					.get("nc.ui.uif2.tangramlayout.node.CNode#1eb5c16");
		nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
		context.put("nc.ui.uif2.tangramlayout.node.CNode#1eb5c16", bean);
		bean.setName("列表");
		bean.setComponent(getZhuzisun());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.tangramlayout.node.VSNode getVSNode_c6998b() {
		if (context.get("nc.ui.uif2.tangramlayout.node.VSNode#c6998b") != null)
			return (nc.ui.uif2.tangramlayout.node.VSNode) context
					.get("nc.ui.uif2.tangramlayout.node.VSNode#c6998b");
		nc.ui.uif2.tangramlayout.node.VSNode bean = new nc.ui.uif2.tangramlayout.node.VSNode();
		context.put("nc.ui.uif2.tangramlayout.node.VSNode#c6998b", bean);
		bean.setUp(getCNode_15b8e5());
		bean.setDown(getCNode_1dfce6());
		bean.setDividerLocation(30f);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.tangramlayout.node.CNode getCNode_15b8e5() {
		if (context.get("nc.ui.uif2.tangramlayout.node.CNode#15b8e5") != null)
			return (nc.ui.uif2.tangramlayout.node.CNode) context
					.get("nc.ui.uif2.tangramlayout.node.CNode#15b8e5");
		nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
		context.put("nc.ui.uif2.tangramlayout.node.CNode#15b8e5", bean);
		bean.setComponent(getCardInfoPnl());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.tangramlayout.node.CNode getCNode_1dfce6() {
		if (context.get("nc.ui.uif2.tangramlayout.node.CNode#1dfce6") != null)
			return (nc.ui.uif2.tangramlayout.node.CNode) context
					.get("nc.ui.uif2.tangramlayout.node.CNode#1dfce6");
		nc.ui.uif2.tangramlayout.node.CNode bean = new nc.ui.uif2.tangramlayout.node.CNode();
		context.put("nc.ui.uif2.tangramlayout.node.CNode#1dfce6", bean);
		bean.setName("卡片");
		bean.setComponent(getZhuzisuncard());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.actions.ActionContributors getToftpanelActionContributors() {
		if (context.get("toftpanelActionContributors") != null)
			return (nc.ui.uif2.actions.ActionContributors) context
					.get("toftpanelActionContributors");
		nc.ui.uif2.actions.ActionContributors bean = new nc.ui.uif2.actions.ActionContributors();
		context.put("toftpanelActionContributors", bean);
		bean.setContributors(getManagedList44());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList44() {
		List list = new ArrayList();
		list.add(getActionsOfList());
		list.add(getActionsOfCard());
		return list;
	}

	public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getActionsOfList() {
		if (context.get("actionsOfList") != null)
			return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer) context
					.get("actionsOfList");
		nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(
				getZhuzisun());
		context.put("actionsOfList", bean);
		bean.setActions(getManagedList45());
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList45() {
		List list = new ArrayList();
		list.add(getAddAction());
		list.add(getEditMenuAction());
		list.add(getDeleteAction());
		list.add(getCopyAddAction());
		list.add(getSeparate());
		list.add(getQueryAction());
		list.add(getRefreshAction());
		list.add(getSeparate());
		list.add(getCommitActionMenu());
		list.add(getApproveActionMenu());
		list.add(getAssistFuncActionMenu());
		list.add(getAssignActionGroup());
		list.add(getEnableActionMenu());
		list.add(getDefaultActionMenu());
		list.add(getSeparate());
		list.add(getImportExportMenu());
		list.add(getPrintMenuAction());
		return list;
	}

	public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getActionsOfCard() {
		if (context.get("actionsOfCard") != null)
			return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer) context
					.get("actionsOfCard");
		nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(
				getZhuzisuncard());
		context.put("actionsOfCard", bean);
		bean.setActions(getManagedList46());
		bean.setEditActions(getManagedList47());
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList46() {
		List list = new ArrayList();
		list.add(getAddAction());
		list.add(getEditMenu());
		list.add(getDeleteAction());
		list.add(getCopyAddAction());
		list.add(getSeparate());
		list.add(getQueryAction());
		list.add(getRefreshCardAction());
		list.add(getSeparate());
		list.add(getCommitActionMenu());
		list.add(getApproveActionMenu());
		list.add(getAssistFuncActionMenu());
		list.add(getAssignActionGroup());
		list.add(getEnableActionMenu());
		list.add(getDefaultActionMenu());
		list.add(getSeparate());
		list.add(getImportExportMenu());
		list.add(getPrintMenuAction());
		return list;
	}

	private List getManagedList47() {
		List list = new ArrayList();
		list.add(getSaveAction());
		list.add(getSaveAddAction());
		list.add(getSaveandsendApproveAction());
		list.add(getSeparate());
		list.add(getCancelAction());
		list.add(getSeparate());
		list.add(getBomActivityAction());
		return list;
	}

	public nc.funcnode.ui.action.GroupAction getAddActionMenu() {
		if (context.get("addActionMenu") != null)
			return (nc.funcnode.ui.action.GroupAction) context
					.get("addActionMenu");
		nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
		context.put("addActionMenu", bean);
		bean.setActions(getManagedList48());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList48() {
		List list = new ArrayList();
		list.add(getAddAction());
		return list;
	}

	public nc.ui.pubapp.uif2app.actions.AddAction getAddAction() {
		if (context.get("addAction") != null)
			return (nc.ui.pubapp.uif2app.actions.AddAction) context
					.get("addAction");
		nc.ui.pubapp.uif2app.actions.AddAction bean = new nc.ui.pubapp.uif2app.actions.AddAction();
		context.put("addAction", bean);
		bean.setModel(getManageAppModel());
		bean.setInterceptor(getShowCardInterceptor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor getShowCardInterceptor() {
		if (context.get("showCardInterceptor") != null)
			return (nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor) context
					.get("showCardInterceptor");
		nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.CompositeActionInterceptor();
		context.put("showCardInterceptor", bean);
		bean.setInterceptors(getManagedList49());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList49() {
		List list = new ArrayList();
		list.add(getShowUpComponentInterceptorBean());
		return list;
	}

	public nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor getShowUpComponentInterceptorBean() {
		if (context.get("ShowUpComponentInterceptorBean") != null)
			return (nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor) context
					.get("ShowUpComponentInterceptorBean");
		nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor();
		context.put("ShowUpComponentInterceptorBean", bean);
		bean.setShowUpComponent(getZhuzisuncard());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.funcnode.ui.action.GroupAction getEditMenu() {
		if (context.get("editMenu") != null)
			return (nc.funcnode.ui.action.GroupAction) context.get("editMenu");
		nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
		context.put("editMenu", bean);
		bean.setCode("print");
		bean.setName(getI18nFB_7746e9());
		bean.setTooltip(getI18nFB_d07163());
		bean.setActions(getManagedList50());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_7746e9() {
		if (context.get("nc.ui.uif2.I18nFB#7746e9") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#7746e9");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#7746e9", bean);
		bean.setResDir("1014362_0");
		bean.setResId("01014362-0254");
		bean.setDefaultValue("修改");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#7746e9", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private java.lang.String getI18nFB_d07163() {
		if (context.get("nc.ui.uif2.I18nFB#d07163") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#d07163");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#d07163", bean);
		bean.setResDir("1014362_0");
		bean.setResId("01014362-0264");
		bean.setDefaultValue("修改(Ctrl+E)");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#d07163", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList50() {
		List list = new ArrayList();
		list.add(getEditAction());
		list.add(getReviseAction());
		return list;
	}

	public nc.funcnode.ui.action.GroupAction getEditMenuAction() {
		if (context.get("editMenuAction") != null)
			return (nc.funcnode.ui.action.GroupAction) context
					.get("editMenuAction");
		nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
		context.put("editMenuAction", bean);
		bean.setCode("print");
		bean.setName(getI18nFB_c4515e());
		bean.setTooltip(getI18nFB_ba000c());
		bean.setActions(getManagedList51());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_c4515e() {
		if (context.get("nc.ui.uif2.I18nFB#c4515e") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#c4515e");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#c4515e", bean);
		bean.setResDir("1014362_0");
		bean.setResId("01014362-0254");
		bean.setDefaultValue("修改");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#c4515e", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private java.lang.String getI18nFB_ba000c() {
		if (context.get("nc.ui.uif2.I18nFB#ba000c") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#ba000c");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#ba000c", bean);
		bean.setResDir("1014362_0");
		bean.setResId("01014362-0264");
		bean.setDefaultValue("修改(Ctrl+E)");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#ba000c", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList51() {
		List list = new ArrayList();
		list.add(getEditAction());
		list.add(getBatchEditAction());
		list.add(getReviseAction());
		return list;
	}

	public nc.funcnode.ui.action.MenuAction getAssignActionGroup() {
		if (context.get("assignActionGroup") != null)
			return (nc.funcnode.ui.action.MenuAction) context
					.get("assignActionGroup");
		nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
		context.put("assignActionGroup", bean);
		bean.setCode("AssignGroup");
		bean.setName(getI18nFB_1593c35());
		bean.setTooltip(getI18nFB_17fc686());
		bean.setActions(getManagedList52());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_1593c35() {
		if (context.get("nc.ui.uif2.I18nFB#1593c35") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#1593c35");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#1593c35", bean);
		bean.setResDir("1014362_0");
		bean.setResId("01014362-0522");
		bean.setDefaultValue("BOM分配");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#1593c35", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private java.lang.String getI18nFB_17fc686() {
		if (context.get("nc.ui.uif2.I18nFB#17fc686") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#17fc686");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#17fc686", bean);
		bean.setResDir("1014362_0");
		bean.setResId("01014362-0550");
		bean.setDefaultValue("分配");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#17fc686", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList52() {
		List list = new ArrayList();
		list.add(getAssignAction());
		list.add(getCancelAssignAction());
		return list;
	}

	public nc.ui.bd.bom.bom0202.action.BomAssignAction getAssignAction() {
		if (context.get("assignAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomAssignAction) context
					.get("assignAction");
		nc.ui.bd.bom.bom0202.action.BomAssignAction bean = new nc.ui.bd.bom.bom0202.action.BomAssignAction();
		context.put("assignAction", bean);
		bean.setModel(getManageAppModel());
		bean.setModelDataManager(getModelDataManager());
		bean.setAssignContext(getAssignContext());
		bean.setDlgAssignListener(getDlgAssignListener());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.listener.DialogAssignActionListener getDlgAssignListener() {
		if (context.get("dlgAssignListener") != null)
			return (nc.ui.bd.bom.bom0202.listener.DialogAssignActionListener) context
					.get("dlgAssignListener");
		nc.ui.bd.bom.bom0202.listener.DialogAssignActionListener bean = new nc.ui.bd.bom.bom0202.listener.DialogAssignActionListener();
		context.put("dlgAssignListener", bean);
		bean.setModel(getManageAppModel());
		bean.setMainGrandModel(getMainGrandModel());
		bean.setModelDataManager(getModelDataManager());
		bean.setMultiBillTaskRunner(getBomAssignTaskRunner());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.thread.BomAssignMultiTaskRunner getBomAssignTaskRunner() {
		if (context.get("bomAssignTaskRunner") != null)
			return (nc.ui.bd.bom.bom0202.thread.BomAssignMultiTaskRunner) context
					.get("bomAssignTaskRunner");
		nc.ui.bd.bom.bom0202.thread.BomAssignMultiTaskRunner bean = new nc.ui.bd.bom.bom0202.thread.BomAssignMultiTaskRunner(
				getAssignProxy());
		context.put("bomAssignTaskRunner", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.serviceproxy.BomAssignProxy getAssignProxy() {
		if (context.get("assignProxy") != null)
			return (nc.ui.bd.bom.bom0202.serviceproxy.BomAssignProxy) context
					.get("assignProxy");
		nc.ui.bd.bom.bom0202.serviceproxy.BomAssignProxy bean = new nc.ui.bd.bom.bom0202.serviceproxy.BomAssignProxy();
		context.put("assignProxy", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomUnAssignAction getCancelAssignAction() {
		if (context.get("cancelAssignAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomUnAssignAction) context
					.get("cancelAssignAction");
		nc.ui.bd.bom.bom0202.action.BomUnAssignAction bean = new nc.ui.bd.bom.bom0202.action.BomUnAssignAction();
		context.put("cancelAssignAction", bean);
		bean.setModel(getManageAppModel());
		bean.setModelDataManager(getModelDataManager());
		bean.setAssignContext(getAssignContext());
		bean.setDlgAssignListener(getDlgCancleAssignListener());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.listener.DialogCancleAssignActionListener getDlgCancleAssignListener() {
		if (context.get("dlgCancleAssignListener") != null)
			return (nc.ui.bd.bom.bom0202.listener.DialogCancleAssignActionListener) context
					.get("dlgCancleAssignListener");
		nc.ui.bd.bom.bom0202.listener.DialogCancleAssignActionListener bean = new nc.ui.bd.bom.bom0202.listener.DialogCancleAssignActionListener();
		context.put("dlgCancleAssignListener", bean);
		bean.setModel(getManageAppModel());
		bean.setModelDataManager(getModelDataManager());
		bean.setMainGrandModel(getMainGrandModel());
		bean.setMultiBillTaskRunner(getBomAssignTaskRunner());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.context.BomAssignContext getAssignContext() {
		if (context.get("assignContext") != null)
			return (nc.ui.bd.bom.bom0202.context.BomAssignContext) context
					.get("assignContext");
		nc.ui.bd.bom.bom0202.context.BomAssignContext bean = new nc.ui.bd.bom.bom0202.context.BomAssignContext();
		context.put("assignContext", bean);
		bean.setLogincontext(getContext());
		bean.setBillTempNodekey("assign");
		bean.setBillTemplatePkItemkey("cbomid");
		bean.setOrgTypeIDs(getManagedList53());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList53() {
		List list = new ArrayList();
		list.add("BUSINESSUNIT00000000");
		return list;
	}

	public nc.ui.bd.bom.bom0202.action.BomEditAction getEditAction() {
		if (context.get("editAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomEditAction) context
					.get("editAction");
		nc.ui.bd.bom.bom0202.action.BomEditAction bean = new nc.ui.bd.bom.bom0202.action.BomEditAction();
		context.put("editAction", bean);
		bean.setModel(getManageAppModel());
		bean.setBillForm(getBillFormEditor());
		bean.setInterceptor(getShowUpComponentInterceptorBean());
		bean.setOperateCode("Edit");
		bean.setResourceCode("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomBatchEditAction getBatchEditAction() {
		if (context.get("batchEditAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomBatchEditAction) context
					.get("batchEditAction");
		nc.ui.bd.bom.bom0202.action.BomBatchEditAction bean = new nc.ui.bd.bom.bom0202.action.BomBatchEditAction();
		context.put("batchEditAction", bean);
		bean.setModel(getManageAppModel());
		bean.setMainGrandModel(getMainGrandModel());
		bean.setOperateCode("BatchEidtAction");
		bean.setResourceCode("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomReviseAction getReviseAction() {
		if (context.get("reviseAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomReviseAction) context
					.get("reviseAction");
		nc.ui.bd.bom.bom0202.action.BomReviseAction bean = new nc.ui.bd.bom.bom0202.action.BomReviseAction();
		context.put("reviseAction", bean);
		bean.setModel(getManageAppModel());
		bean.setInterceptor(getShowUpComponentInterceptorBean());
		bean.setBillForm(getBillFormEditor());
		bean.setOperateCode("Edit");
		bean.setResourceCode("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomCopyAddAction getCopyAddAction() {
		if (context.get("copyAddAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomCopyAddAction) context
					.get("copyAddAction");
		nc.ui.bd.bom.bom0202.action.BomCopyAddAction bean = new nc.ui.bd.bom.bom0202.action.BomCopyAddAction();
		context.put("copyAddAction", bean);
		bean.setModel(getManageAppModel());
		bean.setEditor(getBillFormEditor());
		bean.setGrandModel(getMainGrandModel());
		bean.setBillform(getBillFormEditor());
		bean.setMainGrandBillcard(getZhuzisuncard());
		bean.setCode("CopyAdd");
		bean.setInterceptor(getShowCardInterceptor());
		bean.setCopyActionProcessor(getBomCopyActionProcessor_1c29270());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.bd.bom.bom0202.action.processor.BomCopyActionProcessor getBomCopyActionProcessor_1c29270() {
		if (context
				.get("nc.ui.bd.bom.bom0202.action.processor.BomCopyActionProcessor#1c29270") != null)
			return (nc.ui.bd.bom.bom0202.action.processor.BomCopyActionProcessor) context
					.get("nc.ui.bd.bom.bom0202.action.processor.BomCopyActionProcessor#1c29270");
		nc.ui.bd.bom.bom0202.action.processor.BomCopyActionProcessor bean = new nc.ui.bd.bom.bom0202.action.processor.BomCopyActionProcessor();
		context.put(
				"nc.ui.bd.bom.bom0202.action.processor.BomCopyActionProcessor#1c29270",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomActivityAction2 getBomActivityAction() {
		if (context.get("bomActivityAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomActivityAction2) context
					.get("bomActivityAction");
		nc.ui.bd.bom.bom0202.action.BomActivityAction2 bean = new nc.ui.bd.bom.bom0202.action.BomActivityAction2();
		context.put("bomActivityAction", bean);
		bean.setModel(getManageAppModel());
		bean.setMainGrandModel(getMainGrandModel());
		bean.setBillform(getBillFormEditor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomWipEditAction getWipEditAction() {
		if (context.get("wipEditAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomWipEditAction) context
					.get("wipEditAction");
		nc.ui.bd.bom.bom0202.action.BomWipEditAction bean = new nc.ui.bd.bom.bom0202.action.BomWipEditAction();
		context.put("wipEditAction", bean);
		bean.setModel(getManageAppModel());
		bean.setBillForm(getBillFormEditor());
		bean.setSunBillForm(getSunbillFormEditor());
		bean.setInterceptor(getShowUpComponentInterceptorBean());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomQueryRTAction getBomQueryRtAction() {
		if (context.get("bomQueryRtAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomQueryRTAction) context
					.get("bomQueryRtAction");
		nc.ui.bd.bom.bom0202.action.BomQueryRTAction bean = new nc.ui.bd.bom.bom0202.action.BomQueryRTAction();
		context.put("bomQueryRtAction", bean);
		bean.setModel(getManageAppModel());
		bean.setMainGrandModel(getMainGrandModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomOpenKeyAction getBomOpenKeyAction() {
		if (context.get("bomOpenKeyAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomOpenKeyAction) context
					.get("bomOpenKeyAction");
		nc.ui.bd.bom.bom0202.action.BomOpenKeyAction bean = new nc.ui.bd.bom.bom0202.action.BomOpenKeyAction();
		context.put("bomOpenKeyAction", bean);
		bean.setModel(getManageAppModel());
		bean.setMainGrandModel(getMainGrandModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.funcnode.ui.action.GroupAction getEnableActionMenu() {
		if (context.get("enableActionMenu") != null)
			return (nc.funcnode.ui.action.GroupAction) context
					.get("enableActionMenu");
		nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
		context.put("enableActionMenu", bean);
		bean.setCode("enable");
		bean.setName("启用");
		bean.setTooltip("启用");
		bean.setActions(getManagedList54());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList54() {
		List list = new ArrayList();
		list.add(getEnableAction());
		list.add(getDisableAction());
		return list;
	}

	public nc.ui.bd.bom.bom0202.action.BomDisableAction getDisableAction() {
		if (context.get("disableAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomDisableAction) context
					.get("disableAction");
		nc.ui.bd.bom.bom0202.action.BomDisableAction bean = new nc.ui.bd.bom.bom0202.action.BomDisableAction();
		context.put("disableAction", bean);
		bean.setModel(getManageAppModel());
		bean.setBatchProcessor(getBatchDisableProcessor());
		bean.setResourceCode("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomEnableAction getEnableAction() {
		if (context.get("enableAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomEnableAction) context
					.get("enableAction");
		nc.ui.bd.bom.bom0202.action.BomEnableAction bean = new nc.ui.bd.bom.bom0202.action.BomEnableAction();
		context.put("enableAction", bean);
		bean.setModel(getManageAppModel());
		bean.setBatchProcessor(getBatchEnableProcessor());
		bean.setResourceCode("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.mmf.framework.batch.MultiBatchProcessor getBatchEnableProcessor() {
		if (context.get("batchEnableProcessor") != null)
			return (nc.ui.mmf.framework.batch.MultiBatchProcessor) context
					.get("batchEnableProcessor");
		nc.ui.mmf.framework.batch.MultiBatchProcessor bean = new nc.ui.mmf.framework.batch.MultiBatchProcessor();
		context.put("batchEnableProcessor", bean);
		bean.setModel(getManageAppModel());
		bean.setService(getBatchEnableService());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.mmf.framework.batch.MultiBatchProcessor getBatchDisableProcessor() {
		if (context.get("batchDisableProcessor") != null)
			return (nc.ui.mmf.framework.batch.MultiBatchProcessor) context
					.get("batchDisableProcessor");
		nc.ui.mmf.framework.batch.MultiBatchProcessor bean = new nc.ui.mmf.framework.batch.MultiBatchProcessor();
		context.put("batchDisableProcessor", bean);
		bean.setModel(getManageAppModel());
		bean.setService(getBatchDisableService());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.serviceproxy.BomBatchEnableService getBatchEnableService() {
		if (context.get("batchEnableService") != null)
			return (nc.ui.bd.bom.bom0202.serviceproxy.BomBatchEnableService) context
					.get("batchEnableService");
		nc.ui.bd.bom.bom0202.serviceproxy.BomBatchEnableService bean = new nc.ui.bd.bom.bom0202.serviceproxy.BomBatchEnableService();
		context.put("batchEnableService", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.serviceproxy.BomBatchDisableService getBatchDisableService() {
		if (context.get("batchDisableService") != null)
			return (nc.ui.bd.bom.bom0202.serviceproxy.BomBatchDisableService) context
					.get("batchDisableService");
		nc.ui.bd.bom.bom0202.serviceproxy.BomBatchDisableService bean = new nc.ui.bd.bom.bom0202.serviceproxy.BomBatchDisableService();
		context.put("batchDisableService", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction getQueryAction() {
		if (context.get("queryAction") != null)
			return (nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction) context
					.get("queryAction");
		nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction bean = new nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction();
		context.put("queryAction", bean);
		bean.setDataManager(getModelDataManager());
		bean.setModel(getManageAppModel());
		bean.setQryCondDLGInitializer(getBomQryCondDLGInitializer());
		bean.setTemplateContainer(getQueryTemplateContainer());
		bean.setShowUpComponent(getZhuzisun());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.funcnode.ui.action.GroupAction getDefaultActionMenu() {
		if (context.get("defaultActionMenu") != null)
			return (nc.funcnode.ui.action.GroupAction) context
					.get("defaultActionMenu");
		nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
		context.put("defaultActionMenu", bean);
		bean.setActions(getManagedList55());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList55() {
		List list = new ArrayList();
		list.add(getDefaultAction());
		list.add(getDefaultCancelAction());
		return list;
	}

	public nc.ui.bd.bom.bom0202.action.BomDefaultAction getDefaultAction() {
		if (context.get("defaultAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomDefaultAction) context
					.get("defaultAction");
		nc.ui.bd.bom.bom0202.action.BomDefaultAction bean = new nc.ui.bd.bom.bom0202.action.BomDefaultAction();
		context.put("defaultAction", bean);
		bean.setModel(getManageAppModel());
		bean.setService(getBusinessProxy());
		bean.setCheckEcn(true);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomDefaultCancelAction getDefaultCancelAction() {
		if (context.get("defaultCancelAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomDefaultCancelAction) context
					.get("defaultCancelAction");
		nc.ui.bd.bom.bom0202.action.BomDefaultCancelAction bean = new nc.ui.bd.bom.bom0202.action.BomDefaultCancelAction();
		context.put("defaultCancelAction", bean);
		bean.setModel(getManageAppModel());
		bean.setService(getBusinessProxy());
		bean.setCheckEcn(true);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.funcnode.ui.action.MenuAction getAssistFuncActionMenu() {
		if (context.get("assistFuncActionMenu") != null)
			return (nc.funcnode.ui.action.MenuAction) context
					.get("assistFuncActionMenu");
		nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
		context.put("assistFuncActionMenu", bean);
		bean.setCode("assist");
		bean.setName(getI18nFB_11c347d());
		bean.setTooltip(getI18nFB_b9fe30());
		bean.setActions(getManagedList56());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_11c347d() {
		if (context.get("nc.ui.uif2.I18nFB#11c347d") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#11c347d");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#11c347d", bean);
		bean.setResDir("1014362_0");
		bean.setResId("01014362-0549");
		bean.setDefaultValue("辅助功能");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#11c347d", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private java.lang.String getI18nFB_b9fe30() {
		if (context.get("nc.ui.uif2.I18nFB#b9fe30") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#b9fe30");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#b9fe30", bean);
		bean.setResDir("1014362_0");
		bean.setResId("01014362-0549");
		bean.setDefaultValue("辅助功能");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#b9fe30", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList56() {
		List list = new ArrayList();
		list.add(getBomActivityAction());
		list.add(getBomQueryRtAction());
		list.add(getBomOpenKeyAction());
		list.add(getBomRtConsistentInspectAction());
		list.add(getLinkTreeAction());
		list.add(getWipEditAction());
		return list;
	}

	public nc.ui.bd.bom.bom0202.action.BomRtConsistentInspectAction getBomRtConsistentInspectAction() {
		if (context.get("bomRtConsistentInspectAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomRtConsistentInspectAction) context
					.get("bomRtConsistentInspectAction");
		nc.ui.bd.bom.bom0202.action.BomRtConsistentInspectAction bean = new nc.ui.bd.bom.bom0202.action.BomRtConsistentInspectAction();
		context.put("bomRtConsistentInspectAction", bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor getShowListInterceptor() {
		if (context.get("showListInterceptor") != null)
			return (nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor) context
					.get("showListInterceptor");
		nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor bean = new nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor();
		context.put("showListInterceptor", bean);
		bean.setShowUpComponent(getZhuzisun());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell getQueryArea() {
		if (context.get("queryArea") != null)
			return (nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell) context
					.get("queryArea");
		nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell bean = new nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell();
		context.put("queryArea", bean);
		bean.setQueryAreaCreator(getQueryAction());
		bean.initUI();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel getQueryInfo() {
		if (context.get("queryInfo") != null)
			return (nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel) context
					.get("queryInfo");
		nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel bean = new nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel();
		context.put("queryInfo", bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.lazilyload.BomBillLazilyLoader getBillLazilyLoader() {
		if (context.get("billLazilyLoader") != null)
			return (nc.ui.bd.bom.bom0202.lazilyload.BomBillLazilyLoader) context
					.get("billLazilyLoader");
		nc.ui.bd.bom.bom0202.lazilyload.BomBillLazilyLoader bean = new nc.ui.bd.bom.bom0202.lazilyload.BomBillLazilyLoader();
		context.put("billLazilyLoader", bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager getLasilyLodadMediator() {
		if (context.get("lasilyLodadMediator") != null)
			return (nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager) context
					.get("lasilyLodadMediator");
		nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager bean = new nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager();
		context.put("lasilyLodadMediator", bean);
		bean.setModel(getManageAppModel());
		bean.setLoader(getBillLazilyLoader());
		bean.setLazilyLoadSupporter(getManagedList57());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList57() {
		List list = new ArrayList();
		list.add(getCardPanelLazilyLoad_df28aa());
		list.add(getListPanelLazilyLoad_e6d53f());
		list.add(getLazyActions());
		return list;
	}

	private nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad getCardPanelLazilyLoad_df28aa() {
		if (context
				.get("nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad#df28aa") != null)
			return (nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad) context
					.get("nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad#df28aa");
		nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad bean = new nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad();
		context.put(
				"nc.ui.pubapp.uif2app.lazilyload.CardPanelLazilyLoad#df28aa",
				bean);
		bean.setBillform(getBillFormEditor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad getListPanelLazilyLoad_e6d53f() {
		if (context
				.get("nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad#e6d53f") != null)
			return (nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad) context
					.get("nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad#e6d53f");
		nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad bean = new nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad();
		context.put(
				"nc.ui.pubapp.uif2app.lazilyload.ListPanelLazilyLoad#e6d53f",
				bean);
		bean.setListView(getListView());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad getLazyActions() {
		if (context.get("lazyActions") != null)
			return (nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad) context
					.get("lazyActions");
		nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad bean = new nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad();
		context.put("lazyActions", bean);
		bean.setModel(getManageAppModel());
		bean.setActionList(getManagedList58());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList58() {
		List list = new ArrayList();
		list.add(getPrintAction());
		list.add(getPreviewAction());
		list.add(getOutputAction());
		list.add(getPrintReplaceAction());
		list.add(getPrintWipsAction());
		list.add(getPrintPosAction());
		list.add(getPrintLossAction());
		list.add(getPrintOutputsAction());
		list.add(getBatchEditAction());
		return list;
	}

	public nc.ui.bd.bom.bom0202.query.BomQueryConditionInitializer getBomQryCondDLGInitializer() {
		if (context.get("bomQryCondDLGInitializer") != null)
			return (nc.ui.bd.bom.bom0202.query.BomQueryConditionInitializer) context
					.get("bomQryCondDLGInitializer");
		nc.ui.bd.bom.bom0202.query.BomQueryConditionInitializer bean = new nc.ui.bd.bom.bom0202.query.BomQueryConditionInitializer();
		context.put("bomQryCondDLGInitializer", bean);
		bean.setContext(getContext());
		bean.setIsfeature(false);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomSaveAddAction getSaveAddAction() {
		if (context.get("saveAddAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomSaveAddAction) context
					.get("saveAddAction");
		nc.ui.bd.bom.bom0202.action.BomSaveAddAction bean = new nc.ui.bd.bom.bom0202.action.BomSaveAddAction();
		context.put("saveAddAction", bean);
		bean.setAddAction(getAddAction());
		bean.setSaveAction(getSaveAction());
		bean.setBillForm(getZhuzisuncard());
		bean.setModel(getManageAppModel());
		bean.setEditor(getBillFormEditor());
		bean.setValidationService(getValidationService());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomSaveAction getSaveAction() {
		if (context.get("saveAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomSaveAction) context
					.get("saveAction");
		nc.ui.bd.bom.bom0202.action.BomSaveAction bean = new nc.ui.bd.bom.bom0202.action.BomSaveAction();
		context.put("saveAction", bean);
		bean.setModel(getManageAppModel());
		bean.setMainGrandModel(getMainGrandModel());
		bean.setEditor(getBillFormEditor());
		bean.setBillFormEditor(getBillFormEditor());
		bean.setBillForm(getZhuzisuncard());
		bean.setValidationService(getValidationService());
		bean.setService(getMaintainProxy());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomSaveAndCommitAction getSaveandsendApproveAction() {
		if (context.get("saveandsendApproveAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomSaveAndCommitAction) context
					.get("saveandsendApproveAction");
		nc.ui.bd.bom.bom0202.action.BomSaveAndCommitAction bean = new nc.ui.bd.bom.bom0202.action.BomSaveAndCommitAction(
				getSaveAction(), getCommitAction());
		context.put("saveandsendApproveAction", bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomImportSaveAction getImportSaveAction() {
		if (context.get("importSaveAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomImportSaveAction) context
					.get("importSaveAction");
		nc.ui.bd.bom.bom0202.action.BomImportSaveAction bean = new nc.ui.bd.bom.bom0202.action.BomImportSaveAction();
		context.put("importSaveAction", bean);
		bean.setModel(getManageAppModel());
		bean.setMainGrandModel(getMainGrandModel());
		bean.setEditor(getBillFormEditor());
		bean.setBillFormEditor(getBillFormEditor());
		bean.setBillForm(getZhuzisuncard());
		bean.setValidationService(getValidationService());
		bean.setService(getMaintainProxy());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomCancelAction getCancelAction() {
		if (context.get("cancelAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomCancelAction) context
					.get("cancelAction");
		nc.ui.bd.bom.bom0202.action.BomCancelAction bean = new nc.ui.bd.bom.bom0202.action.BomCancelAction();
		context.put("cancelAction", bean);
		bean.setModel(getManageAppModel());
		bean.setGrandMainPanel(getZhuzisuncard());
		bean.setBillForm(getBillFormEditor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomDeleteAction getDeleteAction() {
		if (context.get("deleteAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomDeleteAction) context
					.get("deleteAction");
		nc.ui.bd.bom.bom0202.action.BomDeleteAction bean = new nc.ui.bd.bom.bom0202.action.BomDeleteAction();
		context.put("deleteAction", bean);
		bean.setModel(getManageAppModel());
		bean.setMainGrandModel(getMainGrandModel());
		bean.setOperateCode("Delete");
		bean.setResourceCode("10140BOMM");
		bean.setSingleBillService(getDeleteProxy());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction getRefreshAction() {
		if (context.get("refreshAction") != null)
			return (nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction) context
					.get("refreshAction");
		nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction bean = new nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction();
		context.put("refreshAction", bean);
		bean.setModel(getManageAppModel());
		bean.setDataManager(getModelDataManager());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomRefreshCardAction getRefreshCardAction() {
		if (context.get("refreshCardAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomRefreshCardAction) context
					.get("refreshCardAction");
		nc.ui.bd.bom.bom0202.action.BomRefreshCardAction bean = new nc.ui.bd.bom.bom0202.action.BomRefreshCardAction();
		context.put("refreshCardAction", bean);
		bean.setModel(getManageAppModel());
		bean.setBillForm(getBillFormEditor());
		bean.setMainPanel(getZhuzisuncard());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomLinkTreeAction getLinkTreeAction() {
		if (context.get("linkTreeAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomLinkTreeAction) context
					.get("linkTreeAction");
		nc.ui.bd.bom.bom0202.action.BomLinkTreeAction bean = new nc.ui.bd.bom.bom0202.action.BomLinkTreeAction();
		context.put("linkTreeAction", bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.funcnode.ui.action.GroupAction getCommitActionMenu() {
		if (context.get("commitActionMenu") != null)
			return (nc.funcnode.ui.action.GroupAction) context
					.get("commitActionMenu");
		nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
		context.put("commitActionMenu", bean);
		bean.setCode("commit");
		bean.setActions(getManagedList59());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList59() {
		List list = new ArrayList();
		list.add(getCommitAction());
		list.add(getUncommitAction());
		return list;
	}

	public nc.ui.bd.bom.bom0202.action.BomCommitAction getCommitAction() {
		if (context.get("commitAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomCommitAction) context
					.get("commitAction");
		nc.ui.bd.bom.bom0202.action.BomCommitAction bean = new nc.ui.bd.bom.bom0202.action.BomCommitAction();
		context.put("commitAction", bean);
		bean.setModel(getManageAppModel());
		bean.setActionName("SAVE");
		bean.setBillType("19B1");
		bean.setEditor(getBillFormEditor());
		bean.setLightBillUsed(false);
		bean.setOperateCode("Commit");
		bean.setRefreshAction(getRefreshAction());
		bean.setCardRefreshAction(getRefreshCardAction());
		bean.setBillform(getBillFormEditor());
		bean.setOpenFunc("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomUnCommitAction getUncommitAction() {
		if (context.get("uncommitAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomUnCommitAction) context
					.get("uncommitAction");
		nc.ui.bd.bom.bom0202.action.BomUnCommitAction bean = new nc.ui.bd.bom.bom0202.action.BomUnCommitAction();
		context.put("uncommitAction", bean);
		bean.setModel(getManageAppModel());
		bean.setActionName("UNSAVE");
		bean.setBillType("19B1");
		bean.setEditor(getBillFormEditor());
		bean.setLightBillUsed(false);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.funcnode.ui.action.GroupAction getApproveActionMenu() {
		if (context.get("approveActionMenu") != null)
			return (nc.funcnode.ui.action.GroupAction) context
					.get("approveActionMenu");
		nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
		context.put("approveActionMenu", bean);
		bean.setCode("approve");
		bean.setActions(getManagedList60());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList60() {
		List list = new ArrayList();
		list.add(getApproveAction());
		list.add(getUnapproveAction());
		list.add(getSeparate());
		list.add(getApproveInfoAction());
		return list;
	}

	public nc.ui.bd.bom.bom0202.action.BomApproveAction getApproveAction() {
		if (context.get("approveAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomApproveAction) context
					.get("approveAction");
		nc.ui.bd.bom.bom0202.action.BomApproveAction bean = new nc.ui.bd.bom.bom0202.action.BomApproveAction();
		context.put("approveAction", bean);
		bean.setModel(getManageAppModel());
		bean.setEditor(getBillFormEditor());
		bean.setBillType("19B1");
		bean.setActionName("APPROVE");
		bean.setLightBillUsed(false);
		bean.setRefreshAction(getRefreshAction());
		bean.setCardRefreshAction(getRefreshCardAction());
		bean.setBillform(getBillFormEditor());
		bean.setOpenFunc("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomUnApproveAction getUnapproveAction() {
		if (context.get("unapproveAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomUnApproveAction) context
					.get("unapproveAction");
		nc.ui.bd.bom.bom0202.action.BomUnApproveAction bean = new nc.ui.bd.bom.bom0202.action.BomUnApproveAction();
		context.put("unapproveAction", bean);
		bean.setModel(getManageAppModel());
		bean.setBillType("19B1");
		bean.setEditor(getBillFormEditor());
		bean.setActionName("UNAPPROVE");
		bean.setLightBillUsed(false);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction getApproveInfoAction() {
		if (context.get("approveInfoAction") != null)
			return (nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction) context
					.get("approveInfoAction");
		nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction bean = new nc.ui.pubapp.uif2app.actions.pflow.PFApproveStatusInfoAction();
		context.put("approveInfoAction", bean);
		bean.setModel(getManageAppModel());
		bean.setEditor(getBillFormEditor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomPrintAction getPreviewAction() {
		if (context.get("previewAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomPrintAction) context
					.get("previewAction");
		nc.ui.bd.bom.bom0202.action.BomPrintAction bean = new nc.ui.bd.bom.bom0202.action.BomPrintAction();
		context.put("previewAction", bean);
		bean.setPreview(true);
		bean.setPrintItemKey("printBaseInfo");
		bean.setNodeKey("printBaseInfo");
		bean.setModel(getManageAppModel());
		bean.setMainmodel(getMainGrandModel());
		bean.setBeforePrintDataProcess(getPrintProcessor());
		bean.setOperateCode("Print");
		bean.setResourceCode("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomPrintAction getPrintAction() {
		if (context.get("printAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomPrintAction) context
					.get("printAction");
		nc.ui.bd.bom.bom0202.action.BomPrintAction bean = new nc.ui.bd.bom.bom0202.action.BomPrintAction();
		context.put("printAction", bean);
		bean.setPreview(false);
		bean.setPrintItemKey("printBaseInfo");
		bean.setNodeKey("printBaseInfo");
		bean.setModel(getManageAppModel());
		bean.setMainmodel(getMainGrandModel());
		bean.setBeforePrintDataProcess(getPrintProcessor());
		bean.setOperateCode("Print");
		bean.setResourceCode("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomOutputAction getOutputAction() {
		if (context.get("outputAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomOutputAction) context
					.get("outputAction");
		nc.ui.bd.bom.bom0202.action.BomOutputAction bean = new nc.ui.bd.bom.bom0202.action.BomOutputAction();
		context.put("outputAction", bean);
		bean.setModel(getManageAppModel());
		bean.setNodeKey("printBaseInfo");
		bean.setParent(getBillFormEditor());
		bean.setBeforePrintDataProcess(getPrintProcessor());
		bean.setOperateCode("Print");
		bean.setResourceCode("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomPrintAction getPrintWipsAction() {
		if (context.get("printWipsAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomPrintAction) context
					.get("printWipsAction");
		nc.ui.bd.bom.bom0202.action.BomPrintAction bean = new nc.ui.bd.bom.bom0202.action.BomPrintAction();
		context.put("printWipsAction", bean);
		bean.setPreview(false);
		bean.setPrintItemKey("printWips");
		bean.setNodeKey("printWips");
		bean.setModel(getManageAppModel());
		bean.setMainmodel(getMainGrandModel());
		bean.setBeforePrintDataProcess(getPrintProcessor());
		bean.setOperateCode("Print");
		bean.setResourceCode("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomPrintAction getPrintPosAction() {
		if (context.get("printPosAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomPrintAction) context
					.get("printPosAction");
		nc.ui.bd.bom.bom0202.action.BomPrintAction bean = new nc.ui.bd.bom.bom0202.action.BomPrintAction();
		context.put("printPosAction", bean);
		bean.setPreview(false);
		bean.setPrintItemKey("printPos");
		bean.setNodeKey("printPos");
		bean.setModel(getManageAppModel());
		bean.setMainmodel(getMainGrandModel());
		bean.setBeforePrintDataProcess(getPrintProcessor());
		bean.setOperateCode("Print");
		bean.setResourceCode("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomPrintAction getPrintOutputsAction() {
		if (context.get("printOutputsAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomPrintAction) context
					.get("printOutputsAction");
		nc.ui.bd.bom.bom0202.action.BomPrintAction bean = new nc.ui.bd.bom.bom0202.action.BomPrintAction();
		context.put("printOutputsAction", bean);
		bean.setPreview(false);
		bean.setPrintItemKey("printOutputs");
		bean.setNodeKey("printOutputs");
		bean.setModel(getManageAppModel());
		bean.setMainmodel(getMainGrandModel());
		bean.setBeforePrintDataProcess(getPrintProcessor());
		bean.setOperateCode("Print");
		bean.setResourceCode("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomPrintAction getPrintLossAction() {
		if (context.get("printLossAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomPrintAction) context
					.get("printLossAction");
		nc.ui.bd.bom.bom0202.action.BomPrintAction bean = new nc.ui.bd.bom.bom0202.action.BomPrintAction();
		context.put("printLossAction", bean);
		bean.setPreview(false);
		bean.setPrintItemKey("printLoss");
		bean.setNodeKey("printLoss");
		bean.setModel(getManageAppModel());
		bean.setMainmodel(getMainGrandModel());
		bean.setBeforePrintDataProcess(getPrintProcessor());
		bean.setOperateCode("Print");
		bean.setResourceCode("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.BomPrintAction getPrintReplaceAction() {
		if (context.get("printReplaceAction") != null)
			return (nc.ui.bd.bom.bom0202.action.BomPrintAction) context
					.get("printReplaceAction");
		nc.ui.bd.bom.bom0202.action.BomPrintAction bean = new nc.ui.bd.bom.bom0202.action.BomPrintAction();
		context.put("printReplaceAction", bean);
		bean.setPreview(false);
		bean.setPrintItemKey("printRepl");
		bean.setNodeKey("printRepl");
		bean.setModel(getManageAppModel());
		bean.setMainmodel(getMainGrandModel());
		bean.setBeforePrintDataProcess(getPrintProcessor());
		bean.setOperateCode("Print");
		bean.setResourceCode("10140BOMM");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.funcnode.ui.action.GroupAction getPrintMenuAction() {
		if (context.get("printMenuAction") != null)
			return (nc.funcnode.ui.action.GroupAction) context
					.get("printMenuAction");
		nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
		context.put("printMenuAction", bean);
		bean.setCode("print");
		bean.setName(getI18nFB_18ec15a());
		bean.setTooltip(getI18nFB_138009c());
		bean.setActions(getManagedList61());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_18ec15a() {
		if (context.get("nc.ui.uif2.I18nFB#18ec15a") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#18ec15a");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#18ec15a", bean);
		bean.setResDir("common");
		bean.setResId("UC001-0000007");
		bean.setDefaultValue("打印");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#18ec15a", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private java.lang.String getI18nFB_138009c() {
		if (context.get("nc.ui.uif2.I18nFB#138009c") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#138009c");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#138009c", bean);
		bean.setResDir("1014362_0");
		bean.setResId("01014362-0212");
		bean.setDefaultValue("打印（Alt+P）");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#138009c", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList61() {
		List list = new ArrayList();
		list.add(getPrintAction());
		list.add(getPreviewAction());
		list.add(getOutputAction());
		list.add(getSeparate());
		list.add(getPrintWipsAction());
		list.add(getPrintPosAction());
		list.add(getPrintOutputsAction());
		list.add(getPrintReplaceAction());
		list.add(getPrintLossAction());
		return list;
	}

	public nc.funcnode.ui.action.SeparatorAction getSeparate() {
		if (context.get("separate") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("separate");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("separate", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.action.print.BomPrintDataBeforeProcess getPrintProcessor() {
		if (context.get("printProcessor") != null)
			return (nc.ui.bd.bom.bom0202.action.print.BomPrintDataBeforeProcess) context
					.get("printProcessor");
		nc.ui.bd.bom.bom0202.action.print.BomPrintDataBeforeProcess bean = new nc.ui.bd.bom.bom0202.action.print.BomPrintDataBeforeProcess();
		context.put("printProcessor", bean);
		bean.setModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.userdefitem.UserDefItemContainer getUserdefitemContainer() {
		if (context.get("userdefitemContainer") != null)
			return (nc.ui.uif2.userdefitem.UserDefItemContainer) context
					.get("userdefitemContainer");
		nc.ui.uif2.userdefitem.UserDefItemContainer bean = new nc.ui.uif2.userdefitem.UserDefItemContainer();
		context.put("userdefitemContainer", bean);
		bean.setContext(getContext());
		bean.setParams(getManagedList62());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList62() {
		List list = new ArrayList();
		list.add(getQueryParam_1b040f7());
		list.add(getQueryParam_79254e());
		list.add(getQueryParam_1802526());
		list.add(getQueryParam_712c5());
		list.add(getQueryParam_616fa2());
		list.add(getQueryParam_e82b3e());
		list.add(getQueryParam_6f0559());
		return list;
	}

	private nc.ui.uif2.userdefitem.QueryParam getQueryParam_1b040f7() {
		if (context.get("nc.ui.uif2.userdefitem.QueryParam#1b040f7") != null)
			return (nc.ui.uif2.userdefitem.QueryParam) context
					.get("nc.ui.uif2.userdefitem.QueryParam#1b040f7");
		nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
		context.put("nc.ui.uif2.userdefitem.QueryParam#1b040f7", bean);
		bean.setMdfullname("mmbd.bd_bom");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.userdefitem.QueryParam getQueryParam_79254e() {
		if (context.get("nc.ui.uif2.userdefitem.QueryParam#79254e") != null)
			return (nc.ui.uif2.userdefitem.QueryParam) context
					.get("nc.ui.uif2.userdefitem.QueryParam#79254e");
		nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
		context.put("nc.ui.uif2.userdefitem.QueryParam#79254e", bean);
		bean.setMdfullname("mmbd.bd_bom_b");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.userdefitem.QueryParam getQueryParam_1802526() {
		if (context.get("nc.ui.uif2.userdefitem.QueryParam#1802526") != null)
			return (nc.ui.uif2.userdefitem.QueryParam) context
					.get("nc.ui.uif2.userdefitem.QueryParam#1802526");
		nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
		context.put("nc.ui.uif2.userdefitem.QueryParam#1802526", bean);
		bean.setMdfullname("mmbd.bd_bom_outputs");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.userdefitem.QueryParam getQueryParam_712c5() {
		if (context.get("nc.ui.uif2.userdefitem.QueryParam#712c5") != null)
			return (nc.ui.uif2.userdefitem.QueryParam) context
					.get("nc.ui.uif2.userdefitem.QueryParam#712c5");
		nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
		context.put("nc.ui.uif2.userdefitem.QueryParam#712c5", bean);
		bean.setMdfullname("mmbd.bd_bom_wip");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.userdefitem.QueryParam getQueryParam_616fa2() {
		if (context.get("nc.ui.uif2.userdefitem.QueryParam#616fa2") != null)
			return (nc.ui.uif2.userdefitem.QueryParam) context
					.get("nc.ui.uif2.userdefitem.QueryParam#616fa2");
		nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
		context.put("nc.ui.uif2.userdefitem.QueryParam#616fa2", bean);
		bean.setMdfullname("mmbd.bd_bom_position");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.userdefitem.QueryParam getQueryParam_e82b3e() {
		if (context.get("nc.ui.uif2.userdefitem.QueryParam#e82b3e") != null)
			return (nc.ui.uif2.userdefitem.QueryParam) context
					.get("nc.ui.uif2.userdefitem.QueryParam#e82b3e");
		nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
		context.put("nc.ui.uif2.userdefitem.QueryParam#e82b3e", bean);
		bean.setMdfullname("mmbd.bd_bom_repl");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.userdefitem.QueryParam getQueryParam_6f0559() {
		if (context.get("nc.ui.uif2.userdefitem.QueryParam#6f0559") != null)
			return (nc.ui.uif2.userdefitem.QueryParam) context
					.get("nc.ui.uif2.userdefitem.QueryParam#6f0559");
		nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
		context.put("nc.ui.uif2.userdefitem.QueryParam#6f0559", bean);
		bean.setRulecode("materialassistant");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator getMarAsstPreparator() {
		if (context.get("marAsstPreparator") != null)
			return (nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator) context
					.get("marAsstPreparator");
		nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator bean = new nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator();
		context.put("marAsstPreparator", bean);
		bean.setModel(getManageAppModel());
		bean.setContainer(getUserdefitemContainer());
		bean.setPrefix("vfree");
		bean.setMaterialField("hcmaterialid");
		bean.setProjectField("cprojectid");
		bean.setSupplierField("cvendorid");
		bean.setProductorField("cproductorid");
		bean.setCustomerField("ccustomerid");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator getGrandMarAsstPreparator() {
		if (context.get("grandMarAsstPreparator") != null)
			return (nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator) context
					.get("grandMarAsstPreparator");
		nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator bean = new nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator();
		context.put("grandMarAsstPreparator", bean);
		bean.setModel(getManageAppModel2());
		bean.setContainer(getUserdefitemContainer());
		bean.setPrefix("vfree");
		bean.setMaterialField("creplmaterialvid");
		bean.setProjectField("cprojectid");
		bean.setSupplierField("cvendorid");
		bean.setProductorField("cproductorid");
		bean.setCustomerField("ccustomerid");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.editor.UserdefitemContainerPreparator getUserdefitemPreparator() {
		if (context.get("userdefitemPreparator") != null)
			return (nc.ui.uif2.editor.UserdefitemContainerPreparator) context
					.get("userdefitemPreparator");
		nc.ui.uif2.editor.UserdefitemContainerPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerPreparator();
		context.put("userdefitemPreparator", bean);
		bean.setContainer(getUserdefitemContainer());
		bean.setParams(getManagedList63());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList63() {
		List list = new ArrayList();
		list.add(getUserdefQueryParam_c83301());
		list.add(getUserdefQueryParam_18c870d());
		list.add(getUserdefQueryParam_f69362());
		list.add(getUserdefQueryParam_462d61());
		list.add(getUserdefQueryParam_14d3c71());
		list.add(getUserdefQueryParam_1bb465d());
		return list;
	}

	private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_c83301() {
		if (context.get("nc.ui.uif2.editor.UserdefQueryParam#c83301") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("nc.ui.uif2.editor.UserdefQueryParam#c83301");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("nc.ui.uif2.editor.UserdefQueryParam#c83301", bean);
		bean.setMdfullname("mmbd.bd_bom");
		bean.setPos(0);
		bean.setPrefix("hvdef");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_18c870d() {
		if (context.get("nc.ui.uif2.editor.UserdefQueryParam#18c870d") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("nc.ui.uif2.editor.UserdefQueryParam#18c870d");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("nc.ui.uif2.editor.UserdefQueryParam#18c870d", bean);
		bean.setMdfullname("mmbd.bd_bom_b");
		bean.setPos(1);
		bean.setPrefix("vdef");
		bean.setTabcode("bomitems");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_f69362() {
		if (context.get("nc.ui.uif2.editor.UserdefQueryParam#f69362") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("nc.ui.uif2.editor.UserdefQueryParam#f69362");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("nc.ui.uif2.editor.UserdefQueryParam#f69362", bean);
		bean.setMdfullname("mmbd.bd_bom_outputs");
		bean.setPos(1);
		bean.setPrefix("vbdef");
		bean.setTabcode("outputs");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_462d61() {
		if (context.get("nc.ui.uif2.editor.UserdefQueryParam#462d61") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("nc.ui.uif2.editor.UserdefQueryParam#462d61");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("nc.ui.uif2.editor.UserdefQueryParam#462d61", bean);
		bean.setMdfullname("mmbd.bd_bom_wip");
		bean.setPos(1);
		bean.setPrefix("vdef");
		bean.setTabcode("wips");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_14d3c71() {
		if (context.get("nc.ui.uif2.editor.UserdefQueryParam#14d3c71") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("nc.ui.uif2.editor.UserdefQueryParam#14d3c71");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("nc.ui.uif2.editor.UserdefQueryParam#14d3c71", bean);
		bean.setMdfullname("mmbd.bd_bom_position");
		bean.setPos(1);
		bean.setPrefix("vdef");
		bean.setTabcode("pos");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_1bb465d() {
		if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1bb465d") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("nc.ui.uif2.editor.UserdefQueryParam#1bb465d");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("nc.ui.uif2.editor.UserdefQueryParam#1bb465d", bean);
		bean.setMdfullname("mmbd.bd_bom_repl");
		bean.setPos(1);
		bean.setPrefix("vdef");
		bean.setTabcode("repl");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.editor.UserdefitemContainerListPreparator getUserdefitemlistPreparator() {
		if (context.get("userdefitemlistPreparator") != null)
			return (nc.ui.uif2.editor.UserdefitemContainerListPreparator) context
					.get("userdefitemlistPreparator");
		nc.ui.uif2.editor.UserdefitemContainerListPreparator bean = new nc.ui.uif2.editor.UserdefitemContainerListPreparator();
		context.put("userdefitemlistPreparator", bean);
		bean.setContainer(getUserdefitemContainer());
		bean.setParams(getManagedList64());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList64() {
		List list = new ArrayList();
		list.add(getUserdefQueryParam_888a8());
		list.add(getUserdefQueryParam_4401ed());
		list.add(getUserdefQueryParam_da8c7d());
		list.add(getUserdefQueryParam_3d86e2());
		list.add(getUserdefQueryParam_1627b40());
		return list;
	}

	private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_888a8() {
		if (context.get("nc.ui.uif2.editor.UserdefQueryParam#888a8") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("nc.ui.uif2.editor.UserdefQueryParam#888a8");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("nc.ui.uif2.editor.UserdefQueryParam#888a8", bean);
		bean.setMdfullname("mmbd.bd_bom");
		bean.setPos(0);
		bean.setPrefix("hvdef");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_4401ed() {
		if (context.get("nc.ui.uif2.editor.UserdefQueryParam#4401ed") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("nc.ui.uif2.editor.UserdefQueryParam#4401ed");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("nc.ui.uif2.editor.UserdefQueryParam#4401ed", bean);
		bean.setMdfullname("mmbd.bd_bom_b");
		bean.setPos(1);
		bean.setPrefix("vdef");
		bean.setTabcode("bomitems");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_da8c7d() {
		if (context.get("nc.ui.uif2.editor.UserdefQueryParam#da8c7d") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("nc.ui.uif2.editor.UserdefQueryParam#da8c7d");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("nc.ui.uif2.editor.UserdefQueryParam#da8c7d", bean);
		bean.setMdfullname("mmbd.bd_bom_wip");
		bean.setPos(1);
		bean.setPrefix("vdef");
		bean.setTabcode("cbom_wipid");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_3d86e2() {
		if (context.get("nc.ui.uif2.editor.UserdefQueryParam#3d86e2") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("nc.ui.uif2.editor.UserdefQueryParam#3d86e2");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("nc.ui.uif2.editor.UserdefQueryParam#3d86e2", bean);
		bean.setMdfullname("mmbd.bd_bom_position");
		bean.setPos(1);
		bean.setPrefix("vdef");
		bean.setTabcode("cbom_positionid");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.uif2.editor.UserdefQueryParam getUserdefQueryParam_1627b40() {
		if (context.get("nc.ui.uif2.editor.UserdefQueryParam#1627b40") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("nc.ui.uif2.editor.UserdefQueryParam#1627b40");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("nc.ui.uif2.editor.UserdefQueryParam#1627b40", bean);
		bean.setMdfullname("mmbd.bd_bom_outputs");
		bean.setPos(1);
		bean.setPrefix("vbdef");
		bean.setTabcode("outputs");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.common.validateservice.ClosingCheck getClosingListener() {
		if (context.get("ClosingListener") != null)
			return (nc.ui.pubapp.common.validateservice.ClosingCheck) context
					.get("ClosingListener");
		nc.ui.pubapp.common.validateservice.ClosingCheck bean = new nc.ui.pubapp.common.validateservice.ClosingCheck();
		context.put("ClosingListener", bean);
		bean.setModel(getManageAppModel());
		bean.setSaveAction(getSaveAction());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.funcnode.ui.action.MenuAction getImportExportMenu() {
		if (context.get("importExportMenu") != null)
			return (nc.funcnode.ui.action.MenuAction) context
					.get("importExportMenu");
		nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
		context.put("importExportMenu", bean);
		bean.setCode("importExport");
		bean.setName(getI18nFB_89a775());
		bean.setActions(getManagedList65());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_89a775() {
		if (context.get("nc.ui.uif2.I18nFB#89a775") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#89a775");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#89a775", bean);
		bean.setResDir("1014362_0");
		bean.setResId("01014362-0292");
		bean.setDefaultValue("导入导出");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#89a775", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList65() {
		List list = new ArrayList();
		list.add(getImportAction());
		list.add(getExportAction());
		return list;
	}

	public nc.ui.mmbd.pub.excelimport.action.MMExportAction getExportAction() {
		if (context.get("exportAction") != null)
			return (nc.ui.mmbd.pub.excelimport.action.MMExportAction) context
					.get("exportAction");
		nc.ui.mmbd.pub.excelimport.action.MMExportAction bean = new nc.ui.mmbd.pub.excelimport.action.MMExportAction();
		context.put("exportAction", bean);
		bean.setModel(getManageAppModel());
		bean.setImportableEditor(getImportEditor());
		bean.setShowProgress(true);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.mmbd.pub.excelimport.action.MMImportAction getImportAction() {
		if (context.get("importAction") != null)
			return (nc.ui.mmbd.pub.excelimport.action.MMImportAction) context
					.get("importAction");
		nc.ui.mmbd.pub.excelimport.action.MMImportAction bean = new nc.ui.mmbd.pub.excelimport.action.MMImportAction();
		context.put("importAction", bean);
		bean.setModel(getManageAppModel());
		bean.setImportableEditor(getImportEditor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.bd.bom.bom0202.importable.BomImportablePanel getImportEditor() {
		if (context.get("importEditor") != null)
			return (nc.ui.bd.bom.bom0202.importable.BomImportablePanel) context
					.get("importEditor");
		nc.ui.bd.bom.bom0202.importable.BomImportablePanel bean = new nc.ui.bd.bom.bom0202.importable.BomImportablePanel();
		context.put("importEditor", bean);
		bean.setAddAction(getAddAction());
		bean.setSaveAction(getImportSaveAction());
		bean.setCancelAction(getCancelAction());
		bean.setBillcardPanelEditor(getBillFormEditor());
		bean.setAppModel(getManageAppModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator getEcnQueryHyperlinkMediator() {
		if (context.get("ecnQueryHyperlinkMediator") != null)
			return (nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator) context
					.get("ecnQueryHyperlinkMediator");
		nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator bean = new nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator();
		context.put("ecnQueryHyperlinkMediator", bean);
		bean.setModel(getManageAppModel());
		bean.setSrcBillIdField("hcecnid");
		bean.setSrcBillNOField("hvecnbillcode");
		bean.setSrcBillType("19A3");
		bean.setSrcBillTypeFieldPos(0);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator getBomQueryHyperlinkMediator() {
		if (context.get("bomQueryHyperlinkMediator") != null)
			return (nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator) context
					.get("bomQueryHyperlinkMediator");
		nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator bean = new nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator();
		context.put("bomQueryHyperlinkMediator", bean);
		bean.setModel(getManageAppModel());
		bean.setSrcBillIdField("vitemversion");
		bean.setSrcBillNOField("vitemversion");
		bean.setSrcBillType("19B1");
		bean.setSrcBillTypeFieldPos(1);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator getBomPackQueryHyperlinkMediator() {
		if (context.get("bomPackQueryHyperlinkMediator") != null)
			return (nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator) context
					.get("bomPackQueryHyperlinkMediator");
		nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator bean = new nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator();
		context.put("bomPackQueryHyperlinkMediator", bean);
		bean.setModel(getManageAppModel());
		bean.setSrcBillIdField("vpackversion");
		bean.setSrcBillNOField("vpackversion");
		bean.setSrcBillType("19B1");
		bean.setSrcBillTypeFieldPos(1);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator getVcbomQueryHyperlinkMediator() {
		if (context.get("vcbomQueryHyperlinkMediator") != null)
			return (nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator) context
					.get("vcbomQueryHyperlinkMediator");
		nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator bean = new nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator();
		context.put("vcbomQueryHyperlinkMediator", bean);
		bean.setModel(getManageAppModel());
		bean.setSrcBillIdField("vconfigversion");
		bean.setSrcBillNOField("vconfigversion");
		bean.setSrcBillType("19B1");
		bean.setSrcBillTypeFieldPos(1);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator getRtQueryHyperlinkMediator() {
		if (context.get("rtQueryHyperlinkMediator") != null)
			return (nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator) context
					.get("rtQueryHyperlinkMediator");
		nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator bean = new nc.ui.pubapp.uif2app.linkquery.LinkQueryHyperlinkMediator();
		context.put("rtQueryHyperlinkMediator", bean);
		bean.setModel(getManageAppModel());
		bean.setSrcBillIdField("hrtversion");
		bean.setSrcBillNOField("hrtversion");
		bean.setSrcBillType("19B2");
		bean.setSrcBillTypeFieldPos(0);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

}
