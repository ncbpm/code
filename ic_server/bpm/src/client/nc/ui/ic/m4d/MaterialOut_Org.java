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
		bean.setBtShowName(getI18nFB_9a9b62());
		bean.setDestBillType("4D");
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setTransferViewProcessor((nc.ui.pubapp.billref.dest.TransferViewProcessor) findBeanInUIF2BeanFactory("transferViewProcessor"));
		bean.setPfButtonClickContext(1);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_9a9b62() {
		if (context.get("nc.ui.uif2.I18nFB#9a9b62") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#9a9b62");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#9a9b62", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0752");
		bean.setDefaultValue("入库单");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#9a9b62", product);
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
		bean.setSourceBillName(getI18nFB_4f57db());
		bean.setDestBillType("4D");
		bean.setTransferViewProcessor((nc.ui.pubapp.billref.dest.TransferViewProcessor) findBeanInUIF2BeanFactory("transferViewProcessor"));
		bean.setPfButtonClickContext(1);
		bean.setReturnFlag("Blue_Bill");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_4f57db() {
		if (context.get("nc.ui.uif2.I18nFB#4f57db") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#4f57db");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#4f57db", bean);
		bean.setResDir("funcode");
		bean.setResId("btn_40080804_Ref4B36");
		bean.setDefaultValue("参照维修工单");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#4f57db", product);
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
		bean.setBtShowName(getI18nFB_111278b());
		bean.setDestBillType("4D");
		bean.setTransferViewProcessor((nc.ui.pubapp.billref.dest.TransferViewProcessor) findBeanInUIF2BeanFactory("transferViewProcessor"));
		bean.setPfButtonClickContext(1);
		bean.setReturnFlag("Red_Bill");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_111278b() {
		if (context.get("nc.ui.uif2.I18nFB#111278b") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#111278b");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#111278b", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0753");
		bean.setDefaultValue("维修工单退库");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#111278b", product);
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
		bean.setSourceBillName(getI18nFB_13b4148());
		bean.setDestBillType("4D");
		bean.setPfButtonClickContext(1);
		bean.setTransferViewProcessor((nc.ui.pubapp.billref.dest.TransferViewProcessor) findBeanInUIF2BeanFactory("transferViewProcessor"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_13b4148() {
		if (context.get("nc.ui.uif2.I18nFB#13b4148") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#13b4148");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#13b4148", bean);
		bean.setResDir("4001002_0");
		bean.setResId("04001002-0493");
		bean.setDefaultValue("物资需求申请单");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#13b4148", product);
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
		bean.setSourceBillName(getI18nFB_13ae3ba());
		bean.setDestBillType("4D");
		bean.setPfButtonClickContext(1);
		bean.setTransferViewProcessor((nc.ui.pubapp.billref.dest.TransferViewProcessor) findBeanInUIF2BeanFactory("transferViewProcessor"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_13ae3ba() {
		if (context.get("nc.ui.uif2.I18nFB#13ae3ba") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#13ae3ba");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#13ae3ba", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0809");
		bean.setDefaultValue("领用单");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#13ae3ba", product);
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
		bean.setSourceBillName(getI18nFB_13cf92e());
		bean.setDestBillType("4D");
		bean.setPfButtonClickContext(1);
		bean.setTransferViewProcessor((nc.ui.pubapp.billref.dest.TransferViewProcessor) findBeanInUIF2BeanFactory("transferViewProcessor"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_13cf92e() {
		if (context.get("nc.ui.uif2.I18nFB#13cf92e") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#13cf92e");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#13cf92e", bean);
		bean.setResDir("40080801");
		bean.setResId("1400808010002");
		bean.setDefaultValue("出库申请单");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#13cf92e", product);
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
		bean.setName(getI18nFB_b44e0());
		bean.setActions(getManagedList1());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_b44e0() {
		if (context.get("nc.ui.uif2.I18nFB#b44e0") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#b44e0");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#b44e0", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0739");
		bean.setDefaultValue("新增");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#b44e0", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList1() {
		List list = new ArrayList();
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("selfAddAction"));
		list.add(getSeparatorAction_1d6d240());
		list.add(getAddFromInAction());
		list.add(getAddFrom422XAction());
		list.add(getAddFrom4455Action());
		list.add(getAddFrom4B36Action());
		list.add(getAddFrom4A60Action());
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1d6d240() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1d6d240") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1d6d240");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1d6d240", bean);
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
		bean.setName(getI18nFB_857607());
		bean.setActions(getManagedList2());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_857607() {
		if (context.get("nc.ui.uif2.I18nFB#857607") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#857607");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#857607", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0743");
		bean.setDefaultValue("关联功能");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#857607", product);
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
		list.add(getSeparatorAction_7e081());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("printQAAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("mergerShowAction"));
		list.add(getSumPrintAction());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("printLocAction"));
		list.add(getPrintCountQueryAction());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("dirPrintBarcodeAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("printBarcodeAction"));
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_7e081() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#7e081") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#7e081");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#7e081", bean);
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
		bean.setName(getI18nFB_12448a8());
		bean.setActions(getManagedList4());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_12448a8() {
		if (context.get("nc.ui.uif2.I18nFB#12448a8") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#12448a8");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#12448a8", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0741");
		bean.setDefaultValue("辅助功能");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#12448a8", product);
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
		bean.setName(getI18nFB_11e362f());
		bean.setActions(getManagedList5());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_11e362f() {
		if (context.get("nc.ui.uif2.I18nFB#11e362f") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#11e362f");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#11e362f", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0743");
		bean.setDefaultValue("关联功能");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#11e362f", product);
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
		bean.setName(getI18nFB_777458());
		bean.setActions(getManagedList6());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_777458() {
		if (context.get("nc.ui.uif2.I18nFB#777458") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#777458");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#777458", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0742");
		bean.setDefaultValue("联查");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#777458", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList6() {
		List list = new ArrayList();
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("linkQryAction"));
		list.add(getSeparatorAction_773da5());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("displayOrhideAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("setPieceAtion"));
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_773da5() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#773da5") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#773da5");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#773da5", bean);
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
		bean.setName(getI18nFB_702d50());
		bean.setActions(getManagedList7());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_702d50() {
		if (context.get("nc.ui.uif2.I18nFB#702d50") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#702d50");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#702d50", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0742");
		bean.setDefaultValue("联查");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#702d50", product);
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
		list.add(getSeparatorAction_1201eea());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("queryAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("refreshAction"));
		list.add(getSeparatorAction_1cd9c68());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("maintainMenu"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("signActionMenu"));
		list.add(getAssistantFunctionBrowseAction_OUT());
		list.add(getSeparatorAction_1d772d1());
		list.add(getLinkQryBrowseGroupAction());
		list.add(getSeparatorAction_1b337e2());
		list.add(getRelatFunctionBrowseAction_OUT());
		list.add(getSeparatorAction_119fd41());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("importExportAction"));
		list.add(getPrintMngAction());
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1201eea() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1201eea") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1201eea");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1201eea", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1cd9c68() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1cd9c68") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1cd9c68");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1cd9c68", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1d772d1() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1d772d1") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1d772d1");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1d772d1", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1b337e2() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1b337e2") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1b337e2");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1b337e2", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_119fd41() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#119fd41") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#119fd41");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#119fd41", bean);
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
		list.add(getSeparatorAction_141e93b());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("queryAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("refreshCardAction"));
		list.add(getSeparatorAction_3740ef());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("maintainMenu"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("signActionMenu"));
		list.add(getAssistantFunctionBrowseAction_OUT());
		list.add(getSeparatorAction_1880b05());
		list.add(getLinkQryBrowseGroupAction());
		list.add(getSeparatorAction_8b8b96());
		list.add(getRelatFunctionBrowseAction_OUT());
		list.add(getSeparatorAction_1795e93());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("importExportAction"));
		list.add(getPrintMngAction());
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_141e93b() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#141e93b") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#141e93b");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#141e93b", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_3740ef() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#3740ef") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#3740ef");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#3740ef", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1880b05() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1880b05") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1880b05");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1880b05", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_8b8b96() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#8b8b96") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#8b8b96");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#8b8b96", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1795e93() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1795e93") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1795e93");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1795e93", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList10() {
		List list = new ArrayList();
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("saveAction"));
		list.add(getSeparatorAction_13032c9());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("cancelAction"));
		list.add(getSeparatorAction_1564253());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("fetchAutoAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("pickAutoAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("maintainMenu"));
		list.add(getSeparatorAction_1c59e1());
		list.add(getLinkQryEditGroupAction());
		list.add(getSeparatorAction_150f519());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("importExportAction"));
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_13032c9() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#13032c9") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#13032c9");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#13032c9", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1564253() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1564253") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1564253");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1564253", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1c59e1() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1c59e1") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1c59e1");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1c59e1", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_150f519() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#150f519") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#150f519");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#150f519", bean);
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
		map.put("cdrawcalbodyvid", getCdrawcalbodyvidHandler_1f1e90e());
		map.put("cdrawcalbodyoid", getCdrawcalbodyoidHandler_fba684());
		map.put("cdrawwarehouseid", getCdrawwarehouseidHandler_1714677());
		map.put("cbizid", getCbizidHandeler_1c93aa7());
		map.put("cdptvid", getCdptvidHandler_29b103());
		map.put("cworkcenterid", getCworkcenteridHandler_1244753());
		map.put("cprojectid", getCprojectidHandlerFor4D_c69fdb());
		map.put("cprojecttaskid", getCprojecttaskidHandlerFor4D_2921d6());
		map.put("ccostcenterid", getCcostcenteridHandler_6c6bc2());
		map.put("ccostobject", getCcostobjectHandler_16ca4b());
		map.put("crcid", getCrcidHandelerFor4D_3b26e6());
		map.put("pk_cbsnode", getPk_cbsnodeHandlerFor4D_176ef30());
		map.put("cconstructvendorid", getCconstructvendoridHandler_1516739());
		map.put("vbatchcode", getBatchCodeHandler());
		return map;
	}

	private nc.ui.ic.m4d.handler.CdrawcalbodyvidHandler getCdrawcalbodyvidHandler_1f1e90e() {
		if (context.get("nc.ui.ic.m4d.handler.CdrawcalbodyvidHandler#1f1e90e") != null)
			return (nc.ui.ic.m4d.handler.CdrawcalbodyvidHandler) context
					.get("nc.ui.ic.m4d.handler.CdrawcalbodyvidHandler#1f1e90e");
		nc.ui.ic.m4d.handler.CdrawcalbodyvidHandler bean = new nc.ui.ic.m4d.handler.CdrawcalbodyvidHandler();
		context.put("nc.ui.ic.m4d.handler.CdrawcalbodyvidHandler#1f1e90e", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CdrawcalbodyoidHandler getCdrawcalbodyoidHandler_fba684() {
		if (context.get("nc.ui.ic.m4d.handler.CdrawcalbodyoidHandler#fba684") != null)
			return (nc.ui.ic.m4d.handler.CdrawcalbodyoidHandler) context
					.get("nc.ui.ic.m4d.handler.CdrawcalbodyoidHandler#fba684");
		nc.ui.ic.m4d.handler.CdrawcalbodyoidHandler bean = new nc.ui.ic.m4d.handler.CdrawcalbodyoidHandler();
		context.put("nc.ui.ic.m4d.handler.CdrawcalbodyoidHandler#fba684", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CdrawwarehouseidHandler getCdrawwarehouseidHandler_1714677() {
		if (context.get("nc.ui.ic.m4d.handler.CdrawwarehouseidHandler#1714677") != null)
			return (nc.ui.ic.m4d.handler.CdrawwarehouseidHandler) context
					.get("nc.ui.ic.m4d.handler.CdrawwarehouseidHandler#1714677");
		nc.ui.ic.m4d.handler.CdrawwarehouseidHandler bean = new nc.ui.ic.m4d.handler.CdrawwarehouseidHandler();
		context.put("nc.ui.ic.m4d.handler.CdrawwarehouseidHandler#1714677",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CbizidHandeler getCbizidHandeler_1c93aa7() {
		if (context.get("nc.ui.ic.m4d.handler.CbizidHandeler#1c93aa7") != null)
			return (nc.ui.ic.m4d.handler.CbizidHandeler) context
					.get("nc.ui.ic.m4d.handler.CbizidHandeler#1c93aa7");
		nc.ui.ic.m4d.handler.CbizidHandeler bean = new nc.ui.ic.m4d.handler.CbizidHandeler();
		context.put("nc.ui.ic.m4d.handler.CbizidHandeler#1c93aa7", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CdptvidHandler getCdptvidHandler_29b103() {
		if (context.get("nc.ui.ic.m4d.handler.CdptvidHandler#29b103") != null)
			return (nc.ui.ic.m4d.handler.CdptvidHandler) context
					.get("nc.ui.ic.m4d.handler.CdptvidHandler#29b103");
		nc.ui.ic.m4d.handler.CdptvidHandler bean = new nc.ui.ic.m4d.handler.CdptvidHandler();
		context.put("nc.ui.ic.m4d.handler.CdptvidHandler#29b103", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CworkcenteridHandler getCworkcenteridHandler_1244753() {
		if (context.get("nc.ui.ic.m4d.handler.CworkcenteridHandler#1244753") != null)
			return (nc.ui.ic.m4d.handler.CworkcenteridHandler) context
					.get("nc.ui.ic.m4d.handler.CworkcenteridHandler#1244753");
		nc.ui.ic.m4d.handler.CworkcenteridHandler bean = new nc.ui.ic.m4d.handler.CworkcenteridHandler();
		context.put("nc.ui.ic.m4d.handler.CworkcenteridHandler#1244753", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CprojectidHandlerFor4D getCprojectidHandlerFor4D_c69fdb() {
		if (context.get("nc.ui.ic.m4d.handler.CprojectidHandlerFor4D#c69fdb") != null)
			return (nc.ui.ic.m4d.handler.CprojectidHandlerFor4D) context
					.get("nc.ui.ic.m4d.handler.CprojectidHandlerFor4D#c69fdb");
		nc.ui.ic.m4d.handler.CprojectidHandlerFor4D bean = new nc.ui.ic.m4d.handler.CprojectidHandlerFor4D();
		context.put("nc.ui.ic.m4d.handler.CprojectidHandlerFor4D#c69fdb", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CprojecttaskidHandlerFor4D getCprojecttaskidHandlerFor4D_2921d6() {
		if (context
				.get("nc.ui.ic.m4d.handler.CprojecttaskidHandlerFor4D#2921d6") != null)
			return (nc.ui.ic.m4d.handler.CprojecttaskidHandlerFor4D) context
					.get("nc.ui.ic.m4d.handler.CprojecttaskidHandlerFor4D#2921d6");
		nc.ui.ic.m4d.handler.CprojecttaskidHandlerFor4D bean = new nc.ui.ic.m4d.handler.CprojecttaskidHandlerFor4D();
		context.put("nc.ui.ic.m4d.handler.CprojecttaskidHandlerFor4D#2921d6",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CcostcenteridHandler getCcostcenteridHandler_6c6bc2() {
		if (context.get("nc.ui.ic.m4d.handler.CcostcenteridHandler#6c6bc2") != null)
			return (nc.ui.ic.m4d.handler.CcostcenteridHandler) context
					.get("nc.ui.ic.m4d.handler.CcostcenteridHandler#6c6bc2");
		nc.ui.ic.m4d.handler.CcostcenteridHandler bean = new nc.ui.ic.m4d.handler.CcostcenteridHandler();
		context.put("nc.ui.ic.m4d.handler.CcostcenteridHandler#6c6bc2", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CcostobjectHandler getCcostobjectHandler_16ca4b() {
		if (context.get("nc.ui.ic.m4d.handler.CcostobjectHandler#16ca4b") != null)
			return (nc.ui.ic.m4d.handler.CcostobjectHandler) context
					.get("nc.ui.ic.m4d.handler.CcostobjectHandler#16ca4b");
		nc.ui.ic.m4d.handler.CcostobjectHandler bean = new nc.ui.ic.m4d.handler.CcostobjectHandler();
		context.put("nc.ui.ic.m4d.handler.CcostobjectHandler#16ca4b", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CrcidHandelerFor4D getCrcidHandelerFor4D_3b26e6() {
		if (context.get("nc.ui.ic.m4d.handler.CrcidHandelerFor4D#3b26e6") != null)
			return (nc.ui.ic.m4d.handler.CrcidHandelerFor4D) context
					.get("nc.ui.ic.m4d.handler.CrcidHandelerFor4D#3b26e6");
		nc.ui.ic.m4d.handler.CrcidHandelerFor4D bean = new nc.ui.ic.m4d.handler.CrcidHandelerFor4D();
		context.put("nc.ui.ic.m4d.handler.CrcidHandelerFor4D#3b26e6", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.Pk_cbsnodeHandlerFor4D getPk_cbsnodeHandlerFor4D_176ef30() {
		if (context.get("nc.ui.ic.m4d.handler.Pk_cbsnodeHandlerFor4D#176ef30") != null)
			return (nc.ui.ic.m4d.handler.Pk_cbsnodeHandlerFor4D) context
					.get("nc.ui.ic.m4d.handler.Pk_cbsnodeHandlerFor4D#176ef30");
		nc.ui.ic.m4d.handler.Pk_cbsnodeHandlerFor4D bean = new nc.ui.ic.m4d.handler.Pk_cbsnodeHandlerFor4D();
		context.put("nc.ui.ic.m4d.handler.Pk_cbsnodeHandlerFor4D#176ef30", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.handler.CconstructvendoridHandler getCconstructvendoridHandler_1516739() {
		if (context
				.get("nc.ui.ic.m4d.handler.CconstructvendoridHandler#1516739") != null)
			return (nc.ui.ic.m4d.handler.CconstructvendoridHandler) context
					.get("nc.ui.ic.m4d.handler.CconstructvendoridHandler#1516739");
		nc.ui.ic.m4d.handler.CconstructvendoridHandler bean = new nc.ui.ic.m4d.handler.CconstructvendoridHandler();
		context.put("nc.ui.ic.m4d.handler.CconstructvendoridHandler#1516739",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.plugin.action.ActionInterceptorInfo getActionInterceptorInfo_0() {
		if (context.get("nc.ui.pubapp.plugin.action.ActionInterceptorInfo#0") != null)
			return (nc.ui.pubapp.plugin.action.ActionInterceptorInfo) context
					.get("nc.ui.pubapp.plugin.action.ActionInterceptorInfo#0");
		nc.ui.pubapp.plugin.action.ActionInterceptorInfo bean = new nc.ui.pubapp.plugin.action.ActionInterceptorInfo();
		context.put("nc.ui.pubapp.plugin.action.ActionInterceptorInfo#0", bean);
		bean.setTarget((nc.ui.uif2.NCAction) findBeanInUIF2BeanFactory("pickAutoAction"));
		bean.setInterceptor(getPickAutoInterceptoor());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m4d.handler.PickAutoAcitonInterceptor getPickAutoInterceptoor() {
		if (context.get("pickAutoInterceptoor") != null)
			return (nc.ui.ic.m4d.handler.PickAutoAcitonInterceptor) context
					.get("pickAutoInterceptoor");
		nc.ui.ic.m4d.handler.PickAutoAcitonInterceptor bean = new nc.ui.ic.m4d.handler.PickAutoAcitonInterceptor();
		context.put("pickAutoInterceptoor", bean);
		bean.setTool(getCostTool());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m4d.handler.CostObjectDealTool getCostTool() {
		if (context.get("costTool") != null)
			return (nc.ui.ic.m4d.handler.CostObjectDealTool) context
					.get("costTool");
		nc.ui.ic.m4d.handler.CostObjectDealTool bean = new nc.ui.ic.m4d.handler.CostObjectDealTool();
		context.put("costTool", bean);
		bean.setEditorModel((nc.ui.ic.pub.model.ICBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m4d.handler.VbatchcodeHandlerFor4D getBatchCodeHandler() {
		if (context.get("batchCodeHandler") != null)
			return (nc.ui.ic.m4d.handler.VbatchcodeHandlerFor4D) context
					.get("batchCodeHandler");
		nc.ui.ic.m4d.handler.VbatchcodeHandlerFor4D bean = new nc.ui.ic.m4d.handler.VbatchcodeHandlerFor4D();
		context.put("batchCodeHandler", bean);
		bean.setTool(getCostTool());
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
		map.put("40", getNtbInitProcessor_19cc121());
		map.put("89", getIcGenMutiPkLinkQuery());
		map.put("426", getMaterialInitDataProcessor_4b7c1a());
		map.put("45", getMaterialInitDataProcessor_1c0a7bb());
		map.put("361", getMaterialInitDataProcessorFor4R_143fc6d());
		return map;
	}

	private nc.ui.ic.general.view.NtbInitProcessor getNtbInitProcessor_19cc121() {
		if (context.get("nc.ui.ic.general.view.NtbInitProcessor#19cc121") != null)
			return (nc.ui.ic.general.view.NtbInitProcessor) context
					.get("nc.ui.ic.general.view.NtbInitProcessor#19cc121");
		nc.ui.ic.general.view.NtbInitProcessor bean = new nc.ui.ic.general.view.NtbInitProcessor();
		context.put("nc.ui.ic.general.view.NtbInitProcessor#19cc121", bean);
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

	private nc.ui.ic.m4d.deal.MaterialInitDataProcessor getMaterialInitDataProcessor_4b7c1a() {
		if (context.get("nc.ui.ic.m4d.deal.MaterialInitDataProcessor#4b7c1a") != null)
			return (nc.ui.ic.m4d.deal.MaterialInitDataProcessor) context
					.get("nc.ui.ic.m4d.deal.MaterialInitDataProcessor#4b7c1a");
		nc.ui.ic.m4d.deal.MaterialInitDataProcessor bean = new nc.ui.ic.m4d.deal.MaterialInitDataProcessor();
		context.put("nc.ui.ic.m4d.deal.MaterialInitDataProcessor#4b7c1a", bean);
		bean.setModel(getIcBizModel());
		bean.setAutoShowUpComponent((nc.ui.uif2.components.IAutoShowUpComponent) findBeanInUIF2BeanFactory("card"));
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.deal.MaterialInitDataProcessor getMaterialInitDataProcessor_1c0a7bb() {
		if (context.get("nc.ui.ic.m4d.deal.MaterialInitDataProcessor#1c0a7bb") != null)
			return (nc.ui.ic.m4d.deal.MaterialInitDataProcessor) context
					.get("nc.ui.ic.m4d.deal.MaterialInitDataProcessor#1c0a7bb");
		nc.ui.ic.m4d.deal.MaterialInitDataProcessor bean = new nc.ui.ic.m4d.deal.MaterialInitDataProcessor();
		context.put("nc.ui.ic.m4d.deal.MaterialInitDataProcessor#1c0a7bb", bean);
		bean.setModel(getIcBizModel());
		bean.setAutoShowUpComponent((nc.ui.uif2.components.IAutoShowUpComponent) findBeanInUIF2BeanFactory("card"));
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m4d.deal.MaterialInitDataProcessorFor4R getMaterialInitDataProcessorFor4R_143fc6d() {
		if (context
				.get("nc.ui.ic.m4d.deal.MaterialInitDataProcessorFor4R#143fc6d") != null)
			return (nc.ui.ic.m4d.deal.MaterialInitDataProcessorFor4R) context
					.get("nc.ui.ic.m4d.deal.MaterialInitDataProcessorFor4R#143fc6d");
		nc.ui.ic.m4d.deal.MaterialInitDataProcessorFor4R bean = new nc.ui.ic.m4d.deal.MaterialInitDataProcessorFor4R();
		context.put("nc.ui.ic.m4d.deal.MaterialInitDataProcessorFor4R#143fc6d",
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
