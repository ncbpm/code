package nc.ui.ic.m4d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class MaterialOut_Org extends
		nc.ui.ic.general.model.config.GeneralCommon {
	private Map<String, Object> context = new HashMap();

	public nc.ui.uif2.userdefitem.QueryParam getQueryParams1() {
		if (context.get("queryParams1") != null)
			return (nc.ui.uif2.userdefitem.QueryParam) context
					.get("queryParams1");
		nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
		context.put("queryParams1", bean);
		bean.setMdfullname("ic.MaterialOutHeadVO");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.userdefitem.QueryParam getQueryParams2() {
		if (context.get("queryParams2") != null)
			return (nc.ui.uif2.userdefitem.QueryParam) context
					.get("queryParams2");
		nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
		context.put("queryParams2", bean);
		bean.setMdfullname("ic.MaterialOutBodyVO");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.editor.UserdefQueryParam getUserQueryParams1() {
		if (context.get("userQueryParams1") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("userQueryParams1");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("userQueryParams1", bean);
		bean.setMdfullname("ic.MaterialOutHeadVO");
		bean.setPos(0);
		bean.setPrefix("vdef");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.editor.UserdefQueryParam getUserQueryParams2() {
		if (context.get("userQueryParams2") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("userQueryParams2");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("userQueryParams2", bean);
		bean.setMdfullname("ic.MaterialOutBodyVO");
		bean.setPos(1);
		bean.setPrefix("vbdef");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.general.model.ICGenRevisePageService getPageQuery() {
		if (context.get("pageQuery") != null)
			return (nc.ui.ic.general.model.ICGenRevisePageService) context
					.get("pageQuery");
		nc.ui.ic.general.model.ICGenRevisePageService bean = new nc.ui.ic.general.model.ICGenRevisePageService();
		context.put("pageQuery", bean);
		bean.setVoClassName("nc.vo.ic.m4d.entity.MaterialOutVO");
		bean.setBillType("4D");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m4d.model.MaterialOutModelService getManageModelService() {
		if (context.get("manageModelService") != null)
			return (nc.ui.ic.m4d.model.MaterialOutModelService) context
					.get("manageModelService");
		nc.ui.ic.m4d.model.MaterialOutModelService bean = new nc.ui.ic.m4d.model.MaterialOutModelService();
		context.put("manageModelService", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.general.model.ICGenBizModel getIcBizModel() {
		if (context.get("icBizModel") != null)
			return (nc.ui.ic.general.model.ICGenBizModel) context
					.get("icBizModel");
		nc.ui.ic.general.model.ICGenBizModel bean = new nc.ui.ic.general.model.ICGenBizModel();
		context.put("icBizModel", bean);
		bean.setService(getManageModelService());
		bean.setBusinessObjectAdapterFactory((nc.vo.bd.meta.IBDObjectAdapterFactory) findBeanInUIF2BeanFactory("boadatorfactory"));
		bean.setIcUIContext((nc.ui.ic.pub.env.ICUIContext) findBeanInUIF2BeanFactory("icUIContext"));
		bean.setBillType("4D");
		bean.setPowerValidate(true);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.TangramContainer getContainer() {
		if (context.get("container") != null)
			return (nc.ui.uif2.TangramContainer) context.get("container");
		nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
		context.put("container", bean);
		bean.setTangramLayoutRoot((nc.ui.uif2.tangramlayout.node.TangramLayoutNode) findBeanInUIF2BeanFactory("vsnodequery"));
		bean.setModel(getIcBizModel());
		bean.initUI();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m4d.deal.MaterialUIProcessorInfo getUIProcesorInfo() {
		if (context.get("UIProcesorInfo") != null)
			return (nc.ui.ic.m4d.deal.MaterialUIProcessorInfo) context
					.get("UIProcesorInfo");
		nc.ui.ic.m4d.deal.MaterialUIProcessorInfo bean = new nc.ui.ic.m4d.deal.MaterialUIProcessorInfo();
		context.put("UIProcesorInfo", bean);
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
		bean.setContributors(getManagedList0());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList0() {
		List list = new ArrayList();
		list.add(getActionsOfList());
		list.add(getActionsOfCard());
		return list;
	}

	public nc.ui.ic.m4d.action.MaterialOutAddFromInAction getAddFromInAction() {
		if (context.get("addFromInAction") != null)
			return (nc.ui.ic.m4d.action.MaterialOutAddFromInAction) context
					.get("addFromInAction");
		nc.ui.ic.m4d.action.MaterialOutAddFromInAction bean = new nc.ui.ic.m4d.action.MaterialOutAddFromInAction();
		context.put("addFromInAction", bean);
		bean.setSourceBillType("4A");
		bean.setBtShowName(getI18nFB_d7ca5b());
		bean.setDestBillType("4D");
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setTransferViewProcessor((nc.ui.pubapp.billref.dest.TransferViewProcessor) findBeanInUIF2BeanFactory("transferViewProcessor"));
		bean.setPfButtonClickContext(1);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_d7ca5b() {
		if (context.get("nc.ui.uif2.I18nFB#d7ca5b") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#d7ca5b");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#d7ca5b", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0752");
		bean.setDefaultValue("入库单");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#d7ca5b", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public nc.ui.ic.m4d.action.MaterialOutAddFrom4B36Action getAddFrom4B36Action() {
		if (context.get("addFrom4B36Action") != null)
			return (nc.ui.ic.m4d.action.MaterialOutAddFrom4B36Action) context
					.get("addFrom4B36Action");
		nc.ui.ic.m4d.action.MaterialOutAddFrom4B36Action bean = new nc.ui.ic.m4d.action.MaterialOutAddFrom4B36Action();
		context.put("addFrom4B36Action", bean);
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setSourceBillType("4B36");
		bean.setSourceBillName(getI18nFB_1263984());
		bean.setDestBillType("4D");
		bean.setTransferViewProcessor((nc.ui.pubapp.billref.dest.TransferViewProcessor) findBeanInUIF2BeanFactory("transferViewProcessor"));
		bean.setPfButtonClickContext(1);
		bean.setReturnFlag("Blue_Bill");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_1263984() {
		if (context.get("nc.ui.uif2.I18nFB#1263984") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#1263984");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#1263984", bean);
		bean.setResDir("funcode");
		bean.setResId("btn_40080804_Ref4B36");
		bean.setDefaultValue("参照维修工单");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#1263984", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public nc.ui.ic.m4d.action.MaterialOutAddFrom4B36Action getAddFrom4B36ForBackAction() {
		if (context.get("addFrom4B36ForBackAction") != null)
			return (nc.ui.ic.m4d.action.MaterialOutAddFrom4B36Action) context
					.get("addFrom4B36ForBackAction");
		nc.ui.ic.m4d.action.MaterialOutAddFrom4B36Action bean = new nc.ui.ic.m4d.action.MaterialOutAddFrom4B36Action();
		context.put("addFrom4B36ForBackAction", bean);
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setSourceBillType("4B36");
		bean.setBtShowName(getI18nFB_8a39e9());
		bean.setDestBillType("4D");
		bean.setTransferViewProcessor((nc.ui.pubapp.billref.dest.TransferViewProcessor) findBeanInUIF2BeanFactory("transferViewProcessor"));
		bean.setPfButtonClickContext(1);
		bean.setReturnFlag("Red_Bill");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_8a39e9() {
		if (context.get("nc.ui.uif2.I18nFB#8a39e9") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#8a39e9");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#8a39e9", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0753");
		bean.setDefaultValue("维修工单退库");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#8a39e9", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public nc.ui.ic.m4d.action.MaterialOutAddFromSourceAction getAddFrom422XAction() {
		if (context.get("addFrom422XAction") != null)
			return (nc.ui.ic.m4d.action.MaterialOutAddFromSourceAction) context
					.get("addFrom422XAction");
		nc.ui.ic.m4d.action.MaterialOutAddFromSourceAction bean = new nc.ui.ic.m4d.action.MaterialOutAddFromSourceAction();
		context.put("addFrom422XAction", bean);
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setSourceBillType("422X");
		bean.setSourceBillName(getI18nFB_e0142d());
		bean.setDestBillType("4D");
		bean.setPfButtonClickContext(1);
		bean.setTransferViewProcessor((nc.ui.pubapp.billref.dest.TransferViewProcessor) findBeanInUIF2BeanFactory("transferViewProcessor"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_e0142d() {
		if (context.get("nc.ui.uif2.I18nFB#e0142d") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#e0142d");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#e0142d", bean);
		bean.setResDir("4001002_0");
		bean.setResId("04001002-0493");
		bean.setDefaultValue("物资需求申请单");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#e0142d", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public nc.ui.ic.m4d.action.MaterialOutAddFromSourceAction getAddFrom4A60Action() {
		if (context.get("addFrom4A60Action") != null)
			return (nc.ui.ic.m4d.action.MaterialOutAddFromSourceAction) context
					.get("addFrom4A60Action");
		nc.ui.ic.m4d.action.MaterialOutAddFromSourceAction bean = new nc.ui.ic.m4d.action.MaterialOutAddFromSourceAction();
		context.put("addFrom4A60Action", bean);
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setSourceBillType("4A60");
		bean.setSourceBillName(getI18nFB_14ca52a());
		bean.setDestBillType("4D");
		bean.setPfButtonClickContext(1);
		bean.setTransferViewProcessor((nc.ui.pubapp.billref.dest.TransferViewProcessor) findBeanInUIF2BeanFactory("transferViewProcessor"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_14ca52a() {
		if (context.get("nc.ui.uif2.I18nFB#14ca52a") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#14ca52a");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#14ca52a", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0809");
		bean.setDefaultValue("领用单");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#14ca52a", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public nc.ui.ic.m4d.action.MaterialOutAddFromSourceAction getAddFrom4455Action() {
		if (context.get("addFrom4455Action") != null)
			return (nc.ui.ic.m4d.action.MaterialOutAddFromSourceAction) context
					.get("addFrom4455Action");
		nc.ui.ic.m4d.action.MaterialOutAddFromSourceAction bean = new nc.ui.ic.m4d.action.MaterialOutAddFromSourceAction();
		context.put("addFrom4455Action", bean);
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setSourceBillType("4455");
		bean.setSourceBillName(getI18nFB_ef86d0());
		bean.setDestBillType("4D");
		bean.setPfButtonClickContext(1);
		bean.setTransferViewProcessor((nc.ui.pubapp.billref.dest.TransferViewProcessor) findBeanInUIF2BeanFactory("transferViewProcessor"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_ef86d0() {
		if (context.get("nc.ui.uif2.I18nFB#ef86d0") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#ef86d0");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#ef86d0", bean);
		bean.setResDir("40080801");
		bean.setResId("1400808010002");
		bean.setDefaultValue("出库申请单");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#ef86d0", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public nc.ui.ic.m4d.action.RatioOutAction getRatiooutAction() {
		if (context.get("ratiooutAction") != null)
			return (nc.ui.ic.m4d.action.RatioOutAction) context
					.get("ratiooutAction");
		nc.ui.ic.m4d.action.RatioOutAction bean = new nc.ui.ic.m4d.action.RatioOutAction();
		context.put("ratiooutAction", bean);
		bean.setEditModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m4d.action.DoEquipCardAction getDoEquipCardAction() {
		if (context.get("doEquipCardAction") != null)
			return (nc.ui.ic.m4d.action.DoEquipCardAction) context
					.get("doEquipCardAction");
		nc.ui.ic.m4d.action.DoEquipCardAction bean = new nc.ui.ic.m4d.action.DoEquipCardAction();
		context.put("doEquipCardAction", bean);
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setModel(getIcBizModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m4d.action.CancelEquipCardAction getCancelEquipCardAction() {
		if (context.get("cancelEquipCardAction") != null)
			return (nc.ui.ic.m4d.action.CancelEquipCardAction) context
					.get("cancelEquipCardAction");
		nc.ui.ic.m4d.action.CancelEquipCardAction bean = new nc.ui.ic.m4d.action.CancelEquipCardAction();
		context.put("cancelEquipCardAction", bean);
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setModel(getIcBizModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m4d.action.SumPrintAction getSumPrintAction() {
		if (context.get("sumPrintAction") != null)
			return (nc.ui.ic.m4d.action.SumPrintAction) context
					.get("sumPrintAction");
		nc.ui.ic.m4d.action.SumPrintAction bean = new nc.ui.ic.m4d.action.SumPrintAction();
		context.put("sumPrintAction", bean);
		bean.setModel(getIcBizModel());
		bean.setEditorModel((nc.ui.ic.pub.model.ICBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setPrintProcessor((nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction.IBeforePrintDataProcess) findBeanInUIF2BeanFactory("printProcessor"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.funcnode.ui.action.MenuAction getAddMenu() {
		if (context.get("addMenu") != null)
			return (nc.funcnode.ui.action.MenuAction) context.get("addMenu");
		nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
		context.put("addMenu", bean);
		bean.setCode("MaintainMenu");
		bean.setName(getI18nFB_17bd4a1());
		bean.setActions(getManagedList1());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_17bd4a1() {
		if (context.get("nc.ui.uif2.I18nFB#17bd4a1") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#17bd4a1");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#17bd4a1", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0739");
		bean.setDefaultValue("新增");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#17bd4a1", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList1() {
		List list = new ArrayList();
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("selfAddAction"));
		list.add(getSeparatorAction_c4cc21());
		list.add(getAddFromInAction());
		list.add(getAddFrom422XAction());
		list.add(getAddFrom4455Action());
		list.add(getAddFrom4B36Action());
		list.add(getAddFrom4A60Action());
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_c4cc21() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#c4cc21") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#c4cc21");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#c4cc21", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.funcnode.ui.action.MenuAction getAssistantFunctionEditAction_OUT() {
		if (context.get("assistantFunctionEditAction_OUT") != null)
			return (nc.funcnode.ui.action.MenuAction) context
					.get("assistantFunctionEditAction_OUT");
		nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
		context.put("assistantFunctionEditAction_OUT", bean);
		bean.setCode("NastMngEditAction");
		bean.setName(getI18nFB_35abbf());
		bean.setActions(getManagedList2());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_35abbf() {
		if (context.get("nc.ui.uif2.I18nFB#35abbf") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#35abbf");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#35abbf", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0743");
		bean.setDefaultValue("关联功能");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#35abbf", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList2() {
		List list = new ArrayList();
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("displayOrhideAction"));
		return list;
	}

	public nc.funcnode.ui.action.GroupAction getPrintMngAction() {
		if (context.get("printMngAction") != null)
			return (nc.funcnode.ui.action.GroupAction) context
					.get("printMngAction");
		nc.funcnode.ui.action.GroupAction bean = new nc.funcnode.ui.action.GroupAction();
		context.put("printMngAction", bean);
		bean.setCode("printMngAction");
		bean.setActions(getManagedList3());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList3() {
		List list = new ArrayList();
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("templatePrintAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("templatePreviewAction"));
		list.add(getSeparatorAction_19ea56d());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("printQAAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("mergerShowAction"));
		list.add(getSumPrintAction());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("printLocAction"));
		list.add(getPrintCountQueryAction());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("dirPrintBarcodeAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("printBarcodeAction"));
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_19ea56d() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#19ea56d") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#19ea56d");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#19ea56d", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.pub.action.ICPrintCountQueryAction getPrintCountQueryAction() {
		if (context.get("printCountQueryAction") != null)
			return (nc.ui.ic.pub.action.ICPrintCountQueryAction) context
					.get("printCountQueryAction");
		nc.ui.ic.pub.action.ICPrintCountQueryAction bean = new nc.ui.ic.pub.action.ICPrintCountQueryAction();
		context.put("printCountQueryAction", bean);
		bean.setBilldateFieldName("");
		bean.setModel(getIcBizModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.funcnode.ui.action.MenuAction getAssistantFunctionBrowseAction_OUT() {
		if (context.get("assistantFunctionBrowseAction_OUT") != null)
			return (nc.funcnode.ui.action.MenuAction) context
					.get("assistantFunctionBrowseAction_OUT");
		nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
		context.put("assistantFunctionBrowseAction_OUT", bean);
		bean.setCode("NastMngBrowseAction");
		bean.setName(getI18nFB_7ebde4());
		bean.setActions(getManagedList4());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_7ebde4() {
		if (context.get("nc.ui.uif2.I18nFB#7ebde4") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#7ebde4");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#7ebde4", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0741");
		bean.setDefaultValue("辅助功能");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#7ebde4", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList4() {
		List list = new ArrayList();
		list.add(getRatiooutAction());
		list.add(getAddFrom4B36ForBackAction());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("attachMentMngAction"));
		return list;
	}

	public nc.funcnode.ui.action.MenuAction getRelatFunctionBrowseAction_OUT() {
		if (context.get("relatFunctionBrowseAction_OUT") != null)
			return (nc.funcnode.ui.action.MenuAction) context
					.get("relatFunctionBrowseAction_OUT");
		nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
		context.put("relatFunctionBrowseAction_OUT", bean);
		bean.setCode("NrelatMngEditAction");
		bean.setName(getI18nFB_e4d259());
		bean.setActions(getManagedList5());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_e4d259() {
		if (context.get("nc.ui.uif2.I18nFB#e4d259") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#e4d259");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#e4d259", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0743");
		bean.setDefaultValue("关联功能");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#e4d259", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList5() {
		List list = new ArrayList();
		list.add(getDoEquipCardAction());
		list.add(getCancelEquipCardAction());
		return list;
	}

	public nc.funcnode.ui.action.MenuAction getLinkQryBrowseGroupAction() {
		if (context.get("linkQryBrowseGroupAction") != null)
			return (nc.funcnode.ui.action.MenuAction) context
					.get("linkQryBrowseGroupAction");
		nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
		context.put("linkQryBrowseGroupAction", bean);
		bean.setCode("linkQryAction");
		bean.setName(getI18nFB_adf465());
		bean.setActions(getManagedList6());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_adf465() {
		if (context.get("nc.ui.uif2.I18nFB#adf465") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#adf465");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#adf465", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0742");
		bean.setDefaultValue("联查");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#adf465", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList6() {
		List list = new ArrayList();
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("linkQryAction"));
		list.add(getSeparatorAction_88185d());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("displayOrhideAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("setPieceAtion"));
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_88185d() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#88185d") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#88185d");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#88185d", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.funcnode.ui.action.MenuAction getLinkQryEditGroupAction() {
		if (context.get("linkQryEditGroupAction") != null)
			return (nc.funcnode.ui.action.MenuAction) context
					.get("linkQryEditGroupAction");
		nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
		context.put("linkQryEditGroupAction", bean);
		bean.setCode("linkQryAction");
		bean.setName(getI18nFB_f79361());
		bean.setActions(getManagedList7());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_f79361() {
		if (context.get("nc.ui.uif2.I18nFB#f79361") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#f79361");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#f79361", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0742");
		bean.setDefaultValue("联查");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#f79361", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList7() {
		List list = new ArrayList();
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("displayOrhideAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("setPieceAtion"));
		return list;
	}

	public nc.ui.ic.m4d.action.MaterialOutCopyAction getCopyAction() {
		if (context.get("copyAction") != null)
			return (nc.ui.ic.m4d.action.MaterialOutCopyAction) context
					.get("copyAction");
		nc.ui.ic.m4d.action.MaterialOutCopyAction bean = new nc.ui.ic.m4d.action.MaterialOutCopyAction();
		context.put("copyAction", bean);
		bean.setModel(getIcBizModel());
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setEditor((nc.ui.pubapp.uif2app.view.BillForm) findBeanInUIF2BeanFactory("card"));
		bean.setInterceptor((nc.ui.uif2.actions.ActionInterceptor) findBeanInUIF2BeanFactory("cardInterceptor"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getActionsOfList() {
		if (context.get("actionsOfList") != null)
			return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer) context
					.get("actionsOfList");
		nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(
				(nc.ui.uif2.components.ITabbedPaneAwareComponent) findBeanInUIF2BeanFactory("list"));
		context.put("actionsOfList", bean);
		bean.setActions(getManagedList8());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList8() {
		List list = new ArrayList();
		list.add(getAddMenu());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("editAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("deleteAction"));
		list.add(getCopyAction());
		list.add(getSeparatorAction_1ecd008());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("queryAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("refreshAction"));
		list.add(getSeparatorAction_4dc24f());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("maintainMenu"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("signActionMenu"));
		list.add(getAssistantFunctionBrowseAction_OUT());
		list.add(getSeparatorAction_10fde06());
		list.add(getLinkQryBrowseGroupAction());
		list.add(getSeparatorAction_b990c6());
		list.add(getRelatFunctionBrowseAction_OUT());
		list.add(getSeparatorAction_cfd963());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("importExportAction"));
		list.add(getPrintMngAction());
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1ecd008() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1ecd008") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1ecd008");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1ecd008", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_4dc24f() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#4dc24f") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#4dc24f");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#4dc24f", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_10fde06() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#10fde06") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#10fde06");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#10fde06", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_b990c6() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#b990c6") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#b990c6");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#b990c6", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_cfd963() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#cfd963") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#cfd963");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#cfd963", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getActionsOfCard() {
		if (context.get("actionsOfCard") != null)
			return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer) context
					.get("actionsOfCard");
		nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(
				(nc.ui.uif2.components.ITabbedPaneAwareComponent) findBeanInUIF2BeanFactory("card"));
		context.put("actionsOfCard", bean);
		bean.setModel(getIcBizModel());
		bean.setActions(getManagedList9());
		bean.setEditActions(getManagedList10());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList9() {
		List list = new ArrayList();
		list.add(getAddMenu());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("editAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("deleteAction"));
		list.add(getCopyAction());
		list.add(getSeparatorAction_1c90576());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("queryAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("refreshCardAction"));
		list.add(getSeparatorAction_85abd2());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("maintainMenu"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("signActionMenu"));
		list.add(getAssistantFunctionBrowseAction_OUT());
		list.add(getSeparatorAction_1d37ed3());
		list.add(getLinkQryBrowseGroupAction());
		list.add(getSeparatorAction_2b8e27());
		list.add(getRelatFunctionBrowseAction_OUT());
		list.add(getSeparatorAction_181e11a());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("importExportAction"));
		list.add(getPrintMngAction());
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1c90576() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1c90576") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1c90576");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1c90576", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_85abd2() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#85abd2") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#85abd2");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#85abd2", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1d37ed3() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1d37ed3") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1d37ed3");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1d37ed3", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_2b8e27() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#2b8e27") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#2b8e27");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#2b8e27", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_181e11a() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#181e11a") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#181e11a");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#181e11a", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList10() {
		List list = new ArrayList();
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("saveAction"));
		list.add(getSeparatorAction_1e1938f());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("cancelAction"));
		list.add(getSeparatorAction_9eaceb());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("fetchAutoAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("pickAutoAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("maintainMenu"));
		list.add(getSeparatorAction_16ab003());
		list.add(getLinkQryEditGroupAction());
		list.add(getSeparatorAction_149a825());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("importExportAction"));
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1e1938f() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1e1938f") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1e1938f");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1e1938f", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_9eaceb() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#9eaceb") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#9eaceb");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#9eaceb", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_16ab003() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#16ab003") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#16ab003");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#16ab003", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_149a825() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#149a825") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#149a825");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#149a825", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m4d.handler.Pk_orgHandlerFor4D getPk_orgHandler() {
		if (context.get("pk_orgHandler") != null)
			return (nc.ui.ic.m4d.handler.Pk_orgHandlerFor4D) context
					.get("pk_orgHandler");
		nc.ui.ic.m4d.handler.Pk_orgHandlerFor4D bean = new nc.ui.ic.m4d.handler.Pk_orgHandlerFor4D();
		context.put("pk_orgHandler", bean);
		bean.setEditorModel((nc.ui.ic.pub.model.ICBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setContext((nc.ui.ic.pub.env.ICUIContext) findBeanInUIF2BeanFactory("icUIContext"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m4d.handler.CwarehouseidHandlerFor4D getCwarehouseidHandler() {
		if (context.get("cwarehouseidHandler") != null)
			return (nc.ui.ic.m4d.handler.CwarehouseidHandlerFor4D) context
					.get("cwarehouseidHandler");
		nc.ui.ic.m4d.handler.CwarehouseidHandlerFor4D bean = new nc.ui.ic.m4d.handler.CwarehouseidHandlerFor4D();
		context.put("cwarehouseidHandler", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m4d.handler.CMaterialSubstiHandlerFor4D getCmaterialvidHandler() {
		if (context.get("cmaterialvidHandler") != null)
			return (nc.ui.ic.m4d.handler.CMaterialSubstiHandlerFor4D) context
					.get("cmaterialvidHandler");
		nc.ui.ic.m4d.handler.CMaterialSubstiHandlerFor4D bean = new nc.ui.ic.m4d.handler.CMaterialSubstiHandlerFor4D();
		context.put("cmaterialvidHandler", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m4d.handler.CmffileidHandlerFor4D getCmffileidHandler() {
		if (context.get("cmffileidHandler") != null)
			return (nc.ui.ic.m4d.handler.CmffileidHandlerFor4D) context
					.get("cmffileidHandler");
		nc.ui.ic.m4d.handler.CmffileidHandlerFor4D bean = new nc.ui.ic.m4d.handler.CmffileidHandlerFor4D();
		context.put("cmffileidHandler", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.pub.handler.card.ICCardEditEventHandlerMap getChildCardEditHandlerMap() {
		if (context.get("childCardEditHandlerMap") != null)
			return (nc.ui.ic.pub.handler.card.ICCardEditEventHandlerMap) context
					.get("childCardEditHandlerMap");
		nc.ui.ic.pub.handler.card.ICCardEditEventHandlerMap bean = new nc.ui.ic.pub.handler.card.ICCardEditEventHandlerMap();
		context.put("childCardEditHandlerMap", bean);
		bean.setHandlerMap(getManagedMap0());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private Map getManagedMap0() {
		Map map = new HashMap();
		map.put("cmffileid", getCmffileidHandler());
		map.put("cdrawcalbodyvid", getCdrawcalbodyvidHandler_be566c());
		map.put("cdrawcalbodyoid", getCdrawcalbodyoidHandler_1beb9b());
		map.put("cdrawwarehouseid", getCdrawwarehouseidHandler_10948aa());
		map.put("cbizid", getCbizidHandeler_81bc83());
		map.put("cdptvid", getCdptvidHandler_17f67cd());
		map.put("cworkcenterid", getCworkcenteridHandler_177ccb4());
		map.put("cprojectid", getCprojectidHandlerFor4D_206006());
		map.put("cprojecttaskid", getCprojecttaskidHandlerFor4D_1806266());
		map.put("ccostcenterid", getCcostcenteridHandler_1bc1e7d());
		map.put("ccostobject", getCcostobjectHandler_175bc1a());
		map.put("crcid", getCrcidHandelerFor4D_9078be());
		map.put("pk_cbsnode", getPk_cbsnodeHandlerFor4D_e70656());
		map.put("cconstructvendorid", getCconstructvendoridHandler_1511b71());
		map.put("vbatchcode", getVbatchcodeHandlerFor4D_1e0a5b9());
		return map;
	}

	private nc.ui.ic.m4d.handler.CdrawcalbodyvidHandler getCdrawcalbodyvidHandler_be566c() {
		if (context.get("nc.ui.ic.m4d.handler.CdrawcalbodyvidHandler#be566c") != null)
			return (nc.ui.ic.m4d.handler.CdrawcalbodyvidHandler) context
					.get("nc.ui.ic.m4d.handler.CdrawcalbodyvidHandler#be566c");
		nc.ui.ic.m4d.handler.CdrawcalbodyvidHandler bean = new nc.ui.ic.m4d.handler.CdrawcalbodyvidHandler();
		context.put("nc.ui.ic.m4d.handler.CdrawcalbodyvidHandler#be566c", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CdrawcalbodyoidHandler getCdrawcalbodyoidHandler_1beb9b() {
		if (context.get("nc.ui.ic.m4d.handler.CdrawcalbodyoidHandler#1beb9b") != null)
			return (nc.ui.ic.m4d.handler.CdrawcalbodyoidHandler) context
					.get("nc.ui.ic.m4d.handler.CdrawcalbodyoidHandler#1beb9b");
		nc.ui.ic.m4d.handler.CdrawcalbodyoidHandler bean = new nc.ui.ic.m4d.handler.CdrawcalbodyoidHandler();
		context.put("nc.ui.ic.m4d.handler.CdrawcalbodyoidHandler#1beb9b", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CdrawwarehouseidHandler getCdrawwarehouseidHandler_10948aa() {
		if (context.get("nc.ui.ic.m4d.handler.CdrawwarehouseidHandler#10948aa") != null)
			return (nc.ui.ic.m4d.handler.CdrawwarehouseidHandler) context
					.get("nc.ui.ic.m4d.handler.CdrawwarehouseidHandler#10948aa");
		nc.ui.ic.m4d.handler.CdrawwarehouseidHandler bean = new nc.ui.ic.m4d.handler.CdrawwarehouseidHandler();
		context.put("nc.ui.ic.m4d.handler.CdrawwarehouseidHandler#10948aa",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CbizidHandeler getCbizidHandeler_81bc83() {
		if (context.get("nc.ui.ic.m4d.handler.CbizidHandeler#81bc83") != null)
			return (nc.ui.ic.m4d.handler.CbizidHandeler) context
					.get("nc.ui.ic.m4d.handler.CbizidHandeler#81bc83");
		nc.ui.ic.m4d.handler.CbizidHandeler bean = new nc.ui.ic.m4d.handler.CbizidHandeler();
		context.put("nc.ui.ic.m4d.handler.CbizidHandeler#81bc83", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CdptvidHandler getCdptvidHandler_17f67cd() {
		if (context.get("nc.ui.ic.m4d.handler.CdptvidHandler#17f67cd") != null)
			return (nc.ui.ic.m4d.handler.CdptvidHandler) context
					.get("nc.ui.ic.m4d.handler.CdptvidHandler#17f67cd");
		nc.ui.ic.m4d.handler.CdptvidHandler bean = new nc.ui.ic.m4d.handler.CdptvidHandler();
		context.put("nc.ui.ic.m4d.handler.CdptvidHandler#17f67cd", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CworkcenteridHandler getCworkcenteridHandler_177ccb4() {
		if (context.get("nc.ui.ic.m4d.handler.CworkcenteridHandler#177ccb4") != null)
			return (nc.ui.ic.m4d.handler.CworkcenteridHandler) context
					.get("nc.ui.ic.m4d.handler.CworkcenteridHandler#177ccb4");
		nc.ui.ic.m4d.handler.CworkcenteridHandler bean = new nc.ui.ic.m4d.handler.CworkcenteridHandler();
		context.put("nc.ui.ic.m4d.handler.CworkcenteridHandler#177ccb4", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CprojectidHandlerFor4D getCprojectidHandlerFor4D_206006() {
		if (context.get("nc.ui.ic.m4d.handler.CprojectidHandlerFor4D#206006") != null)
			return (nc.ui.ic.m4d.handler.CprojectidHandlerFor4D) context
					.get("nc.ui.ic.m4d.handler.CprojectidHandlerFor4D#206006");
		nc.ui.ic.m4d.handler.CprojectidHandlerFor4D bean = new nc.ui.ic.m4d.handler.CprojectidHandlerFor4D();
		context.put("nc.ui.ic.m4d.handler.CprojectidHandlerFor4D#206006", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CprojecttaskidHandlerFor4D getCprojecttaskidHandlerFor4D_1806266() {
		if (context
				.get("nc.ui.ic.m4d.handler.CprojecttaskidHandlerFor4D#1806266") != null)
			return (nc.ui.ic.m4d.handler.CprojecttaskidHandlerFor4D) context
					.get("nc.ui.ic.m4d.handler.CprojecttaskidHandlerFor4D#1806266");
		nc.ui.ic.m4d.handler.CprojecttaskidHandlerFor4D bean = new nc.ui.ic.m4d.handler.CprojecttaskidHandlerFor4D();
		context.put("nc.ui.ic.m4d.handler.CprojecttaskidHandlerFor4D#1806266",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CcostcenteridHandler getCcostcenteridHandler_1bc1e7d() {
		if (context.get("nc.ui.ic.m4d.handler.CcostcenteridHandler#1bc1e7d") != null)
			return (nc.ui.ic.m4d.handler.CcostcenteridHandler) context
					.get("nc.ui.ic.m4d.handler.CcostcenteridHandler#1bc1e7d");
		nc.ui.ic.m4d.handler.CcostcenteridHandler bean = new nc.ui.ic.m4d.handler.CcostcenteridHandler();
		context.put("nc.ui.ic.m4d.handler.CcostcenteridHandler#1bc1e7d", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CcostobjectHandler getCcostobjectHandler_175bc1a() {
		if (context.get("nc.ui.ic.m4d.handler.CcostobjectHandler#175bc1a") != null)
			return (nc.ui.ic.m4d.handler.CcostobjectHandler) context
					.get("nc.ui.ic.m4d.handler.CcostobjectHandler#175bc1a");
		nc.ui.ic.m4d.handler.CcostobjectHandler bean = new nc.ui.ic.m4d.handler.CcostobjectHandler();
		context.put("nc.ui.ic.m4d.handler.CcostobjectHandler#175bc1a", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CrcidHandelerFor4D getCrcidHandelerFor4D_9078be() {
		if (context.get("nc.ui.ic.m4d.handler.CrcidHandelerFor4D#9078be") != null)
			return (nc.ui.ic.m4d.handler.CrcidHandelerFor4D) context
					.get("nc.ui.ic.m4d.handler.CrcidHandelerFor4D#9078be");
		nc.ui.ic.m4d.handler.CrcidHandelerFor4D bean = new nc.ui.ic.m4d.handler.CrcidHandelerFor4D();
		context.put("nc.ui.ic.m4d.handler.CrcidHandelerFor4D#9078be", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.Pk_cbsnodeHandlerFor4D getPk_cbsnodeHandlerFor4D_e70656() {
		if (context.get("nc.ui.ic.m4d.handler.Pk_cbsnodeHandlerFor4D#e70656") != null)
			return (nc.ui.ic.m4d.handler.Pk_cbsnodeHandlerFor4D) context
					.get("nc.ui.ic.m4d.handler.Pk_cbsnodeHandlerFor4D#e70656");
		nc.ui.ic.m4d.handler.Pk_cbsnodeHandlerFor4D bean = new nc.ui.ic.m4d.handler.Pk_cbsnodeHandlerFor4D();
		context.put("nc.ui.ic.m4d.handler.Pk_cbsnodeHandlerFor4D#e70656", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CconstructvendoridHandler getCconstructvendoridHandler_1511b71() {
		if (context
				.get("nc.ui.ic.m4d.handler.CconstructvendoridHandler#1511b71") != null)
			return (nc.ui.ic.m4d.handler.CconstructvendoridHandler) context
					.get("nc.ui.ic.m4d.handler.CconstructvendoridHandler#1511b71");
		nc.ui.ic.m4d.handler.CconstructvendoridHandler bean = new nc.ui.ic.m4d.handler.CconstructvendoridHandler();
		context.put("nc.ui.ic.m4d.handler.CconstructvendoridHandler#1511b71",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.VbatchcodeHandlerFor4D getVbatchcodeHandlerFor4D_1e0a5b9() {
		if (context.get("nc.ui.ic.m4d.handler.VbatchcodeHandlerFor4D#1e0a5b9") != null)
			return (nc.ui.ic.m4d.handler.VbatchcodeHandlerFor4D) context
					.get("nc.ui.ic.m4d.handler.VbatchcodeHandlerFor4D#1e0a5b9");
		nc.ui.ic.m4d.handler.VbatchcodeHandlerFor4D bean = new nc.ui.ic.m4d.handler.VbatchcodeHandlerFor4D();
		context.put("nc.ui.ic.m4d.handler.VbatchcodeHandlerFor4D#1e0a5b9", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener getInitDataListener() {
		if (context.get("InitDataListener") != null)
			return (nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener) context
					.get("InitDataListener");
		nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener bean = new nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener();
		context.put("InitDataListener", bean);
		bean.setProcessorMap(getManagedMap1());
		bean.setModel(getIcBizModel());
		bean.setQueryAction((nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction) findBeanInUIF2BeanFactory("queryAction"));
		bean.setVoClassName("nc.vo.ic.m4d.entity.MaterialOutVO");
		bean.setAutoShowUpComponent((nc.ui.uif2.components.IAutoShowUpComponent) findBeanInUIF2BeanFactory("card"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private Map getManagedMap1() {
		Map map = new HashMap();
		map.put("40", getNtbInitProcessor_1a04afd());
		map.put("89", getIcGenMutiPkLinkQuery());
		map.put("426", getMaterialInitDataProcessor_9b3636());
		map.put("45", getMaterialInitDataProcessor_43042());
		map.put("361", getMaterialInitDataProcessorFor4R_fc692c());
		return map;
	}

	private nc.ui.ic.general.view.NtbInitProcessor getNtbInitProcessor_1a04afd() {
		if (context.get("nc.ui.ic.general.view.NtbInitProcessor#1a04afd") != null)
			return (nc.ui.ic.general.view.NtbInitProcessor) context
					.get("nc.ui.ic.general.view.NtbInitProcessor#1a04afd");
		nc.ui.ic.general.view.NtbInitProcessor bean = new nc.ui.ic.general.view.NtbInitProcessor();
		context.put("nc.ui.ic.general.view.NtbInitProcessor#1a04afd", bean);
		bean.setModel(getIcBizModel());
		bean.setQueryArea((nc.ui.uif2.actions.QueryAreaShell) findBeanInUIF2BeanFactory("queryArea"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.inandoutui.linkquery.ICGenMutiPkLinkQuery getIcGenMutiPkLinkQuery() {
		if (context.get("icGenMutiPkLinkQuery") != null)
			return (nc.ui.ic.inandoutui.linkquery.ICGenMutiPkLinkQuery) context
					.get("icGenMutiPkLinkQuery");
		nc.ui.ic.inandoutui.linkquery.ICGenMutiPkLinkQuery bean = new nc.ui.ic.inandoutui.linkquery.ICGenMutiPkLinkQuery();
		context.put("icGenMutiPkLinkQuery", bean);
		bean.setModel(getIcBizModel());
		bean.setAutoShowUpComponent((nc.ui.uif2.components.IAutoShowUpComponent) findBeanInUIF2BeanFactory("list"));
		bean.setVoClass("nc.vo.ic.m4d.entity.MaterialOutVO");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.deal.MaterialInitDataProcessor getMaterialInitDataProcessor_9b3636() {
		if (context.get("nc.ui.ic.m4d.deal.MaterialInitDataProcessor#9b3636") != null)
			return (nc.ui.ic.m4d.deal.MaterialInitDataProcessor) context
					.get("nc.ui.ic.m4d.deal.MaterialInitDataProcessor#9b3636");
		nc.ui.ic.m4d.deal.MaterialInitDataProcessor bean = new nc.ui.ic.m4d.deal.MaterialInitDataProcessor();
		context.put("nc.ui.ic.m4d.deal.MaterialInitDataProcessor#9b3636", bean);
		bean.setModel(getIcBizModel());
		bean.setAutoShowUpComponent((nc.ui.uif2.components.IAutoShowUpComponent) findBeanInUIF2BeanFactory("card"));
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.deal.MaterialInitDataProcessor getMaterialInitDataProcessor_43042() {
		if (context.get("nc.ui.ic.m4d.deal.MaterialInitDataProcessor#43042") != null)
			return (nc.ui.ic.m4d.deal.MaterialInitDataProcessor) context
					.get("nc.ui.ic.m4d.deal.MaterialInitDataProcessor#43042");
		nc.ui.ic.m4d.deal.MaterialInitDataProcessor bean = new nc.ui.ic.m4d.deal.MaterialInitDataProcessor();
		context.put("nc.ui.ic.m4d.deal.MaterialInitDataProcessor#43042", bean);
		bean.setModel(getIcBizModel());
		bean.setAutoShowUpComponent((nc.ui.uif2.components.IAutoShowUpComponent) findBeanInUIF2BeanFactory("card"));
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.deal.MaterialInitDataProcessorFor4R getMaterialInitDataProcessorFor4R_fc692c() {
		if (context
				.get("nc.ui.ic.m4d.deal.MaterialInitDataProcessorFor4R#fc692c") != null)
			return (nc.ui.ic.m4d.deal.MaterialInitDataProcessorFor4R) context
					.get("nc.ui.ic.m4d.deal.MaterialInitDataProcessorFor4R#fc692c");
		nc.ui.ic.m4d.deal.MaterialInitDataProcessorFor4R bean = new nc.ui.ic.m4d.deal.MaterialInitDataProcessorFor4R();
		context.put("nc.ui.ic.m4d.deal.MaterialInitDataProcessorFor4R#fc692c",
				bean);
		bean.setModel(getIcBizModel());
		bean.setAutoShowUpComponent((nc.ui.uif2.components.IAutoShowUpComponent) findBeanInUIF2BeanFactory("card"));
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setProcessor((nc.ui.pubapp.billref.dest.TransferViewProcessor) findBeanInUIF2BeanFactory("transferViewProcessor"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.outbound.bizcheck.OutLinkQuery getOutLinkQuery() {
		if (context.get("outLinkQuery") != null)
			return (nc.ui.ic.outbound.bizcheck.OutLinkQuery) context
					.get("outLinkQuery");
		nc.ui.ic.outbound.bizcheck.OutLinkQuery bean = new nc.ui.ic.outbound.bizcheck.OutLinkQuery();
		context.put("outLinkQuery", bean);
		bean.setVoClassName("nc.vo.ic.m4d.entity.MaterialOutVO");
		bean.setModel(getIcBizModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.actions.PfAddInfoLoader getPfAddInfoLoader() {
		if (context.get("pfAddInfoLoader") != null)
			return (nc.ui.pubapp.uif2app.actions.PfAddInfoLoader) context
					.get("pfAddInfoLoader");
		nc.ui.pubapp.uif2app.actions.PfAddInfoLoader bean = new nc.ui.pubapp.uif2app.actions.PfAddInfoLoader();
		context.put("pfAddInfoLoader", bean);
		bean.setBillType("4D");
		bean.setModel(getIcBizModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m4d.deal.MaterialBillScaleProcessor getScalePrcStrategy() {
		if (context.get("scalePrcStrategy") != null)
			return (nc.ui.ic.m4d.deal.MaterialBillScaleProcessor) context
					.get("scalePrcStrategy");
		nc.ui.ic.m4d.deal.MaterialBillScaleProcessor bean = new nc.ui.ic.m4d.deal.MaterialBillScaleProcessor();
		context.put("scalePrcStrategy", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare getUserdefAndMarAsstCardPreparator() {
		if (context.get("userdefAndMarAsstCardPreparator") != null)
			return (nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare) context
					.get("userdefAndMarAsstCardPreparator");
		nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare();
		context.put("userdefAndMarAsstCardPreparator", bean);
		bean.setBillDataPrepares(getManagedList11());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList11() {
		List list = new ArrayList();
		list.add((nc.ui.pub.bill.IBillData) findBeanInUIF2BeanFactory("userdefitemPreparator"));
		list.add(getMarProdAsstPreparator());
		list.add((nc.ui.pub.bill.IBillData) findBeanInUIF2BeanFactory("marAsstPreparator"));
		return list;
	}

	public nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare getUserdefAndMarAsstListPreparator() {
		if (context.get("userdefAndMarAsstListPreparator") != null)
			return (nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare) context
					.get("userdefAndMarAsstListPreparator");
		nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare();
		context.put("userdefAndMarAsstListPreparator", bean);
		bean.setBillListDataPrepares(getManagedList12());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList12() {
		List list = new ArrayList();
		list.add((nc.ui.pub.bill.IBillListData) findBeanInUIF2BeanFactory("userdefitemlistPreparator"));
		list.add(getMarProdAsstPreparator());
		list.add((nc.ui.pub.bill.IBillListData) findBeanInUIF2BeanFactory("marAsstPreparator"));
		return list;
	}

	public nc.ui.ic.m4d.action.CancelSignAction getCancelSignAction() {
		if (context.get("cancelSignAction") != null)
			return (nc.ui.ic.m4d.action.CancelSignAction) context
					.get("cancelSignAction");
		nc.ui.ic.m4d.action.CancelSignAction bean = new nc.ui.ic.m4d.action.CancelSignAction();
		context.put("cancelSignAction", bean);
		bean.setModel(getIcBizModel());
		bean.setActionName("CANCELSIGN");
		bean.setEditor((nc.ui.uif2.editor.IEditor) findBeanInUIF2BeanFactory("card"));
		bean.setBillForm((nc.ui.ic.pub.view.ICBizBillForm) findBeanInUIF2BeanFactory("card"));
		bean.setListView((nc.ui.ic.pub.view.ICBizBillListView) findBeanInUIF2BeanFactory("list"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator getMarProdAsstPreparator() {
		if (context.get("marProdAsstPreparator") != null)
			return (nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator) context
					.get("marProdAsstPreparator");
		nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator bean = new nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator();
		context.put("marProdAsstPreparator", bean);
		bean.setModel(getIcBizModel());
		bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer) findBeanInUIF2BeanFactory("userdefitemContainer"));
		bean.setPrefix("vprodfree");
		bean.setMaterialField("ccostobject");
		bean.setProjectField("cprodprojectid");
		bean.setSupplierField("cprodvendorid");
		bean.setProductorField("cprodproductorid");
		bean.setCustomerField("cprodasscustid");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m4d.deal.MaterialOutQueryDLGInitializer getQryDLGInitializer() {
		if (context.get("qryDLGInitializer") != null)
			return (nc.ui.ic.m4d.deal.MaterialOutQueryDLGInitializer) context
					.get("qryDLGInitializer");
		nc.ui.ic.m4d.deal.MaterialOutQueryDLGInitializer bean = new nc.ui.ic.m4d.deal.MaterialOutQueryDLGInitializer();
		context.put("qryDLGInitializer", bean);
		bean.setModel(getIcBizModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.scmpub.listener.crossrule.CrossRuleMediator getCrossRuleMediator() {
		if (context.get("crossRuleMediator") != null)
			return (nc.ui.scmpub.listener.crossrule.CrossRuleMediator) context
					.get("crossRuleMediator");
		nc.ui.scmpub.listener.crossrule.CrossRuleMediator bean = new nc.ui.scmpub.listener.crossrule.CrossRuleMediator();
		context.put("crossRuleMediator", bean);
		bean.setModel(getIcBizModel());
		bean.setBillType("4D");
		bean.init();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

}
