package nc.bs.hr.wa.paydata.plugin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.hr.utils.PubEnv;
import nc.itf.hr.wa.IPaydataManageService;
import nc.itf.hr.wa.IPaydataQueryService;
import nc.itf.hr.wa.IPayrollManageService;
import nc.itf.hr.wa.IWaClass;
import nc.itf.uap.pf.IPFBusiAction;
import nc.ui.wa.pub.WADelegator;
import nc.vo.om.pub.SQLHelper;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.ArrayUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pflow.PfUserObject;
import nc.vo.wa.adjust.AggPsnappaproveVO;
import nc.vo.wa.category.WaClassVO;
import nc.vo.wa.paydata.AggPayDataVO;
import nc.vo.wa.paydata.DataVO;
import nc.vo.wa.payfile.PayfileVO;
import nc.vo.wa.payroll.AggPayrollVO;
import nc.vo.wa.payroll.PayrollVO;
import nc.vo.wa.pub.PeriodStateVO;
import nc.vo.wa.pub.WaClassStateHelper;
import nc.vo.wa.pub.WaLoginContext;
import nc.vo.wa.pub.WaLoginVO;
import nc.vo.wa.pub.YearPeriodSeperatorVO;

/**
 * 薪资发放
 */
public class BpmPayDataExpPfxxPlugin<T extends AggPsnappaproveVO> extends
		nc.bs.pfxx.plugin.AbstractPfxxPlugin {

	private IPaydataManageService manageService;

	private IPaydataQueryService paydataQuery;

	private PfUserObject[] userObjs;

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {

		// WaLoginContext loginContext = new WaLoginContext();

		PayfileVO bill = (PayfileVO) vo;

		if (bill.getPk_group() == null) {
			throw new BusinessException("单据的所属集团字段不能为空，请输入值");
		}
		if (bill.getPk_org() == null) {
			throw new BusinessException("单据的财务组织字段不能为空，请输入值");
		}

		if (bill.getCyear() == null) {
			throw new BusinessException("单据的薪资年度字段不能为空，请输入值");
		}

		if (bill.getCperiod() == null) {
			throw new BusinessException("单据的薪资期间字段不能为空，请输入值");
		}

		if (bill.getPk_wa_class() == null) {
			throw new BusinessException("单据的薪资方案字段不能为空，请输入值");
		}

		String waPeriod = bill.getCyear()+bill.getCperiod();// 薪资期间
		String pk_wa_class = bill.getPk_wa_class();// 薪资方案

		String pk_group = bill.getPk_group();
		String pk_org = bill.getPk_org();

		// 薪资项目预警
		// String keyName = ResHelper.getString("common", "UC001-0000027");// 审核
		//
		// IPreAlertService pa = (IPreAlertService) NCLocator.getInstance()
		// .lookup(IPreAlertService.class.getName());
		//
		// String pk_button =
		// nc.ui.wa.alert.HRAlertEnter.getBtnKey("6013paydata",
		// keyName);
		// String[] files = pa.showMessageAlertFileNameByButton(
		// loginContext.getPk_group(), ServerTimeProxy.getInstance()
		// .getServerTime().getDate(), getDataSource(), pk_button,
		// getUserData());
		// showAlertInfo(files);

		// 薪资总额预警
		// BudgetWarnMessageVo messagevo = getAuditCreondition();
		// if (messagevo != null
		// && (messagevo.getLisCorpWarns().size() > 0 || messagevo
		// .getLisDeptWarns().size() > 0)) {
		//
		// AlarmAuditInfomationDlg aid = new AlarmAuditInfomationDlg(
		// getParentContainer(), messagevo);
		// if (aid.showModal() != 1) {
		// return false;// 取消了该操作！
		// }
		// }
		WaLoginContext loginContext = createContext(waPeriod, pk_wa_class,
				pk_group, pk_org);
		// WaLoginVO waLoginVO = loginContext.getWaLoginVO();

		getManageService().onCheck(loginContext.getWaLoginVO(), null, true);

		WaClassVO classvo = null;
		try {
			classvo = NCLocator.getInstance().lookup(IWaClass.class)
					.queryPayrollClassbyPK(pk_wa_class);
		} catch (BusinessException e) {
			Logger.debug(new BusinessException(e));
		}
		if (classvo == null) {
			return null;
		}
		String year = classvo.getCyear();
		String period = classvo.getCperiod();
		Integer batch = classvo.getBatch();

		AggPayrollVO aggvo1 = new AggPayrollVO();
		PayrollVO head = new PayrollVO();
		head.setCyear(year);
		head.setClassperiod(year + period);
		head.setCperiod(period);
		head.setBatch(batch);
		head.setBillstate(IPfRetCheckInfo.NOSTATE);
		head.setOperator(PubEnv.getPk_user());
		head.setPk_wa_class(pk_wa_class);
		head.setPk_group(pk_group);
		head.setPk_org(pk_org);
		head.setApplydate(new UFLiteralDate());

		AggPayDataVO aggPayDataVO = null;
		Map<String, AggPayDataVO> aggVOMap = null;
		aggVOMap = WADelegator.getPaydataQuery().queryItemAndSumDataVOForroll(
				loginContext);
		aggPayDataVO = aggVOMap.get("itemdata");
		DataVO[] dataVOs = aggPayDataVO.getDataVOs();
		if (null != dataVOs && dataVOs.length > 0) {
			UFDouble yf = UFDouble.ZERO_DBL;
			UFDouble sf = UFDouble.ZERO_DBL;
			for (int i = 0; i < dataVOs.length; i++) {
				yf = yf.add((UFDouble) dataVOs[i].getAttributeValue("f_1"));
				sf = sf.add((UFDouble) dataVOs[i].getAttributeValue("f_3"));
			}
			head.setYf(yf);
			head.setSf(sf);
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		String strCode = formatter.format(new Date());
		head.setBillcode(strCode); //
		head.setBillname(strCode);
		head.setBilltype("6302");
		head.setPk_busitype("6302");
		aggvo1.setParentVO(head);
		IPayrollManageService manageService = NCLocator.getInstance().lookup(
				IPayrollManageService.class);
		AggPayrollVO vo1 = manageService.insert(aggvo1);
		vo1 = (AggPayrollVO) NCLocator
				.getInstance()
				.lookup(IPFBusiAction.class)
				.processAction(IPFActionName.SAVE, head.getBilltype(),
						new WorkflownoteVO(), vo1, getUserObj(), null);

		vo1 = (AggPayrollVO) NCLocator
				.getInstance()
				.lookup(IPFBusiAction.class)
				.processAction(IPFActionName.APPROVE, head.getBilltype(), null,
						vo1, getUserObj(), null);
		WaLoginVO waLoginVO = WaClassStateHelper.getWaclassVOWithState(loginContext.getWaLoginVO());
		loginContext.setWaLoginVO(waLoginVO);
		getManageService().onPay(loginContext);
		return null;
	}

	public PfUserObject[] getUserObj() {
		if (userObjs == null) {
			userObjs = new PfUserObject[] { new PfUserObject() };
		}
		return userObjs;
	}

	protected IPaydataManageService getManageService() {
		if (manageService == null) {
			manageService = NCLocator.getInstance().lookup(
					IPaydataManageService.class);
		}
		return manageService;
	}

	/**
	 * 工资总额的预警设置在审核时要进行提示
	 * 
	 * @return
	 * @throws BusinessException
	 */
	// private BudgetWarnMessageVo getAuditCreondition(WaLoginContext
	// loginContext)
	// throws BusinessException {
	// String currentLimit = // " wa_data.pk_wa_data = wa_dataz.pk_wa_dataz "
	// " wa_data.pk_wa_class=wa_waclass.pk_wa_class and wa_waclass.collectflag='N' "
	// // + "and  wa_data.pk_org='" + getContext().getPk_org() + "' "
	// + "and wa_data.checkflag = 'N' and wa_data.caculateflag='Y' "
	// + "and wa_data.pk_wa_class='" + loginContext.getPk_wa_class()
	// + "' and wa_data.cyear='" + loginContext.getWaYear()
	// + "' and wa_data.cperiod='" + loginContext.getWaPeriod() + "'";
	// String clwhere = "";
	//
	// return WADelegator.getPaydataQuery().budgetAlarm4Pay(loginContext,
	// clwhere + " and " + currentLimit);
	// }

	// private Object getUserData(WaLoginContext loginContext) {
	// String[] userData = new String[6];
	//
	// userData[0] = loginContext.getWaYear();
	// userData[1] = loginContext.getWaPeriod();
	// userData[2] = loginContext.getWaLoginVO().getPk_wa_class();
	// userData[3] = loginContext.getPk_group();
	// userData[4] = loginContext.getPk_org();
	// // shenliangc 20140905 薪资发放节点个人薪资项目预警提示只显示界面上查询出来的人员。
	// userData[5] = getPsnWhereCondition();
	//
	// return userData;
	// }

	// shenliangc 20140905 薪资发放节点个人薪资项目预警提示只显示界面上查询出来的人员。
	private String getPsnWhereCondition(WaLoginContext loginContext)
			throws BusinessException {

		AggPayDataVO aggPayDataVO = getPaydataQuery()
				.queryAggPayDataVOByCondition(loginContext, null, null);

		Object[] dataVOs = aggPayDataVO.getDataVOs();
		ArrayList<String> pk_psndoclist = new ArrayList<String>();
		if (!ArrayUtils.isEmpty(dataVOs)) {
			for (int i = 0; i < dataVOs.length; i++)
				pk_psndoclist.add(((DataVO) dataVOs[i]).getPk_psndoc());
		}

		return " pk_psndoc "
				+ SQLHelper.genInClause(pk_psndoclist.toArray(new String[0]));
	}

	public IPaydataQueryService getPaydataQuery() {
		if (paydataQuery == null) {
			paydataQuery = NCLocator.getInstance().lookup(
					IPaydataQueryService.class);
		}
		return paydataQuery;
	}

	private WaLoginContext createContext(String waPeriod, String pk_wa_class,
			String pk_group, String pk_org) throws BusinessException {

		WaLoginContext context = new WaLoginContext();
		WaLoginVO waLoginVO = new WaLoginVO();
		waLoginVO.setPk_wa_class(pk_wa_class);
		YearPeriodSeperatorVO seperatorVO = new YearPeriodSeperatorVO(waPeriod);
		waLoginVO.setCyear(seperatorVO.getYear());
		waLoginVO.setCperiod(seperatorVO.getPeriod());
		waLoginVO.setPk_group(pk_group);
		context.setPk_group(pk_group);
		PeriodStateVO periodStateVO = new PeriodStateVO();
		periodStateVO.setCyear(seperatorVO.getYear());
		periodStateVO.setCperiod(seperatorVO.getPeriod());
		waLoginVO.setPeriodVO(periodStateVO);
		waLoginVO.setPk_org(pk_org);
		context.setPk_org(pk_org);
		waLoginVO = WADelegator.getWaPubService().getWaclassVOWithState(
				waLoginVO);
		context.setWaLoginVO(waLoginVO);
		context.setNodeCode("60130paydata");
		return context;
	}

}
