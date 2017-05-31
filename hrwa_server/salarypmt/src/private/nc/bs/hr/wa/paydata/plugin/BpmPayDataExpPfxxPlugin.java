//package nc.bs.hr.wa.paydata.plugin;
//
//import java.util.ArrayList;
//import java.util.Map;
//
//import nc.bs.framework.common.NCLocator;
//import nc.bs.logging.Logger;
//import nc.bs.pfxx.ISwapContext;
//import nc.desktop.ui.ServerTimeProxy;
//import nc.desktop.ui.WorkbenchEnvironment;
//import nc.hr.utils.PubEnv;
//import nc.hr.utils.ResHelper;
//import nc.itf.hr.wa.IPaydataManageService;
//import nc.itf.hr.wa.IPayrollManageService;
//import nc.itf.hr.wa.IWaClass;
//import nc.itf.uap.pa.IPreAlertService;
//import nc.ui.hrp.budgetitemcmp.view.AlarmAuditInfomationDlg;
//import nc.ui.pub.beans.MessageDialog;
//import nc.ui.pub.bill.BillItem;
//import nc.ui.uif2.UIState;
//import nc.ui.wa.pub.WADelegator;
//import nc.vo.hr.tools.pub.HRConstEnum;
//import nc.vo.hrp.budgetmgt.BudgetWarnMessageVo;
//import nc.vo.om.pub.SQLHelper;
//import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
//import nc.vo.pfxx.util.ArrayUtils;
//import nc.vo.pub.BusinessException;
//import nc.vo.pub.lang.UFDouble;
//import nc.vo.pub.pf.IPfRetCheckInfo;
//import nc.vo.wa.adjust.AggPsnappaproveVO;
//import nc.vo.wa.category.WaClassVO;
//import nc.vo.wa.classitem.WaClassItemVO;
//import nc.vo.wa.paydata.AggPayDataVO;
//import nc.vo.wa.paydata.DataVO;
//import nc.vo.wa.payroll.AggPayrollVO;
//import nc.vo.wa.payroll.PayrollVO;
//import nc.vo.wa.pub.WaLoginContext;
//
//
///**
// * н�ʷ���
// */
//public class BpmPayDataExpPfxxPlugin < T extends AggPsnappaproveVO> extends
//nc.bs.pfxx.plugin.AbstractPfxxPlugin {
//
//	private IPaydataManageService manageService;
//	
//	@Override
//	protected Object processBill(Object vo, ISwapContext swapContext,
//			AggxsysregisterVO aggvo) throws BusinessException {
//		
//		WaLoginContext loginContext = new WaLoginContext();
//		
//		
//		
//		
//		
//		  // н����ĿԤ��
//        String keyName = ResHelper.getString("common", "UC001-0000027")/* @res "���" */;// ���
//        
//      //shenliangc 20140905 н�ʷ��Žڵ����н����ĿԤ����ʾֻ��ʾ�����ϲ�ѯ��������Ա��
//      		IPreAlertService pa = (IPreAlertService) NCLocator.getInstance().lookup(
//					IPreAlertService.class.getName());
//
//      		String pk_button =nc.ui.wa.alert.HRAlertEnter.getBtnKey(
//      				"6013paydata", keyName);
//      		String[] files  = pa.showMessageAlertFileNameByButton(loginContext.getPk_group(),
//					ServerTimeProxy.getInstance()
//					.getServerTime().getDate(), getDataSource(),
//					pk_button, getUserData());
////        showAlertInfo(files);
//        
//        // н���ܶ�Ԥ��
//        BudgetWarnMessageVo messagevo = getAuditCreondition();
//        if (messagevo != null && (messagevo.getLisCorpWarns().size() > 0 || messagevo.getLisDeptWarns().size() > 0))
//        {
//            AlarmAuditInfomationDlg aid = new AlarmAuditInfomationDlg(getParentContainer(), messagevo);
//            if (aid.showModal() != 1)
//            {
//                return false;// ȡ���˸ò�����
//            }
//        }
//        
//        getManageService().onCheck(loginContext.getWaLoginVO(),null, true);
//		
//		
//		WaClassVO classvo = null;
//		try {
//			classvo = NCLocator
//					.getInstance()
//					.lookup(IWaClass.class)
//					.queryPayrollClassbyPK("");
//		} catch (BusinessException e) {
//			Logger.debug(new BusinessException(e));
//		}
//		if (classvo == null) {
//			return null;
//		}
//		String year = classvo.getCyear();
//		String period = classvo.getCperiod();
//		Integer batch = classvo.getBatch();
//		billCardPanel.setHeadItem(PayrollVO.CYEAR, year);
//
//		billCardPanel.setHeadItem("classperiod", year + period);
//		billCardPanel.setHeadItem(PayrollVO.CPERIOD, period);
//		billCardPanel.setHeadItem(PayrollVO.BATCH, batch);
//
//		billCardPanel.setHeadItem(PayrollVO.BILLSTATE, IPfRetCheckInfo.NOSTATE);
//		billCardPanel.setHeadItem(PayrollVO.OPERATOR, PubEnv.getPk_user());
//		// ����wacontext
//		WaLoginContext context = createContext(linkDta.getUserObject().toString(), year, period);
//		showDetail(context);
//		if ( getPayrollModel().getAutoGenerateBillCode()
//				&& !getPayrollModel().isAutoCodeEdit()) {
//			// �Զ����룬 ���ݱ��벻�ɱ༭
//			getHeadItem(PayrollVO.BILLCODE).setEnabled(false);
//
//		}
//		getHeadItem(PayrollVO.PK_WA_CLASS).setEnabled(false);
//		// guoqt ������ʱ�������Ϳ��Ա༭
//		if (getApproveType() != null && getApproveType().equals(HRConstEnum.APPROVE_TYPE_FORCE_WORK_FLOW)){
//			getHeadItem(PayrollVO.TRANSTYPEID).setEnabled(true);
//		}
//		// getWaClassRefPane().setRefModel(new
//		// WaIncludeClassRefModel());
//		// setWaClassWhere();
//		// getHeadItem(PayrollVO.PK_WA_CLASS).setValue(link
//		
//		IPayrollManageService manageService = NCLocator.getInstance().lookup(IPayrollManageService.class);
//		AggPayrollVO vo1 =manageService.insert(vo);
//		
//		getManageService().onPay(loginContext);
//		return null;
//	}
//
//	
//	protected IPaydataManageService getManageService() {
//		if (manageService == null) {
//			manageService = NCLocator.getInstance().lookup(IPaydataManageService.class);
//		}
//		return manageService;
//	}
//	
//	/**
//	 * �����ܶ��Ԥ�����������ʱҪ������ʾ
//	 *
//	 * @return
//	 * @throws BusinessException
//	 */
//	public BudgetWarnMessageVo getAuditCreondition() throws BusinessException {
//		String currentLimit = // " wa_data.pk_wa_data = wa_dataz.pk_wa_dataz "
//				" wa_data.pk_wa_class=wa_waclass.pk_wa_class and wa_waclass.collectflag='N' "
//				//+ "and  wa_data.pk_org='" + getContext().getPk_org() + "' "
//				+ "and wa_data.checkflag = 'N' and wa_data.caculateflag='Y' "
//				+ "and wa_data.pk_wa_class='"
//				+ getPk_wa_class() + "' and wa_data.cyear='" + getWaYear() + "' and wa_data.cperiod='"
//				+ getWaPeriod() + "'";
//		String clwhere = "";
//		
//
//		return WADelegator.getPaydataQuery()
//				.budgetAlarm4Pay(getWaContext(), clwhere + " and " + currentLimit);
//	}
//
//	/**
//	 * ��ʾ��ϸ��Ϣ
//	 * 
//	 * @author liangxr on 2010-5-17
//	 * @param waContext
//	 */
//	private void showDetail(WaLoginContext waContext) {
//
//		AggPayDataVO aggPayDataVO = null;
//		AggPayDataVO sumDataVO = null;
//		Map<String, AggPayDataVO> aggVOMap = null;
//		try {
//			aggVOMap = WADelegator.getPaydataQuery()
//					.queryItemAndSumDataVOForroll(waContext);
//			aggPayDataVO = aggVOMap.get("itemdata");
//			sumDataVO = aggVOMap.get("sumdata");
//		} catch (BusinessException e) {
//			Logger.error(e.getMessage(), e);
//			MessageDialog.showErrorDlg(this, null, e.getMessage());
//		}
//		WaClassItemVO[] classItemVOs = aggPayDataVO.getClassItemVOs();
//		DataVO[] dataVOs = aggPayDataVO.getDataVOs();
//
//		if(null != dataVOs && dataVOs.length >0){
//			UFDouble yf = UFDouble.ZERO_DBL;
//			UFDouble sf = UFDouble.ZERO_DBL;
//			for( int i=0 ;i<dataVOs.length ;i++){
//				yf = yf.add((UFDouble) dataVOs[i].getAttributeValue("f_1"));
//				sf = sf.add((UFDouble) dataVOs[i].getAttributeValue("f_3"));
//			}
//			//��Ӧ���ϼƸ�ֵ��
//			billCardPanel.setHeadItem(PayrollVO.YF, yf);
//			billCardPanel.setHeadItem(PayrollVO.SF, sf);
//		}
//
//		if (getPayrollModel().isApproveSite()) {
//			AggPayrollVO selectvo = (AggPayrollVO) getModel().getSelectedData();
//			resetBill(classItemVOs);
//			billCardPanel.getBillData()
//			.setHeaderValueVO(selectvo.getParentVO());
//			setClassPeriod((PayrollVO) selectvo.getParentVO());
//		} else {
//			Object headVO = billCardPanel.getBillData().getHeaderValueVO(
//					PayrollVO.class.getName());
//			resetBill(classItemVOs);
//			billCardPanel.getBillData().setHeaderValueVO((PayrollVO) headVO);
//			setClassPeriod((PayrollVO) headVO);
//		}
//
//		billCardPanel.getBillData().setBodyValueVO("payrollbs", dataVOs);
//		billCardPanel.getBillData().setBodyValueVO("payrollbs2",
//				sumDataVO.getDataVOs());
//		if (UIState.NOT_EDIT.equals(getModel().getUiState())) {
//			billCardPanel.setEnabled(false);
//		}
////		setWaClassWhere();
//		refreshSumRow(aggVOMap);
//	}
//	
//	private void refreshSumRow(Map<String, AggPayDataVO> aggVOMap) {
//		//guoqt н�ʷ������뼰�������Ӻϼ���
//		BillItem[] items = getBillCardPanel().getBodyShowItems();
//		AggPayrollVO selectedVO = (AggPayrollVO) getPayrollModel().getSelectedData();
//		DataVO[] sumDataVoall = null;
//		//guoqt ���������ϼ�
//		DataVO[] sumDataVonum = null;
//		if(selectedVO!=null){
//			PayrollVO headVO = (PayrollVO) selectedVO.getParentVO();
//			WaLoginContext waContext = createContext(headVO.getPk_wa_class(), headVO.getCyear(), headVO.getCperiod());
//			AggPayDataVO sumDataVO = null;
//			//guoqt ���������ϼ�
//			AggPayDataVO sumdatapsnnum = null;
////			try {
////				Map<String, AggPayDataVO> aggVOMap = WADelegator.getPaydataQuery()
////						.queryItemAndSumDataVOForroll(waContext);
//				sumDataVO = aggVOMap.get("sumdataall");
//				
//				if(sumDataVO !=null){
//					sumDataVoall=sumDataVO.getDataVOs();
//				}
//				//guoqt ���������ϼ�
//				sumdatapsnnum=aggVOMap.get("sumdata");
//				if(sumdatapsnnum !=null){
//					sumDataVonum=sumdatapsnnum.getDataVOs();
//				}
//				
////			} catch (BusinessException e) {
////				Logger.error(e.getMessage(), e);
////				MessageDialog.showErrorDlg(this, null, e.getMessage());
////			}
//			if (sumDataVoall != null) {
//				for (int i = 0; i < items.length; i++) {
//					if (items[i].getKey().startsWith("f_")) {
//						int decimal = items[i].getDecimalDigits();
//						UFDouble value = (UFDouble) sumDataVoall[0].getAttributeValue(items[i].getKey());;
//						if(value==null){
//							value = UFDouble.ZERO_DBL;
//						}
//						//guoqt����ҳǩ����ʾ�ϼ���
//						getBillCardPanel().getTotalTableModel().setValueAt(value.setScale(decimal, UFDouble.ROUND_HALF_UP).toString(), 0, i);
//						getBillCardPanel().getBillModel("payrollbs2").getTotalTableModel().setValueAt(value.setScale(decimal, UFDouble.ROUND_HALF_UP).toString(), 0, i);
//					}
//				}
//				//guoqt ���������ϼ�
//				// 2016-1-11 NCdp205571960 zhousze ����û��Ȩ�޵���Ա�������sumDataVonum��Ҫ�п�
//				if (sumDataVonum != null && sumDataVonum.length != 0) {
//					int num=0;
//					for(int i = 0; i < sumDataVonum.length; i++){
//						num=num+Integer.parseInt((String) sumDataVonum[i].getAttributeValue("psnnum"));
//					}
//					getBillCardPanel().getBillModel("payrollbs2").getTotalTableModel().setValueAt(num, 0, 4);
//				}
//				
//			}
//		}
//	
//}
//	
//	protected Object getUserData() {
//		String[] userData = new String[6];
//
//		userData[0] = getPeriodVO().getCyear();
//		userData[1] = getPeriodVO().getCperiod();
//		userData[2] = getWaLoginVO().getPk_wa_class();
//		userData[3] = getLoginContext().getPk_group();
//		userData[4] = getLoginContext().getPk_org();
//		//shenliangc 20140905 н�ʷ��Žڵ����н����ĿԤ����ʾֻ��ʾ�����ϲ�ѯ��������Ա��
//		userData[5] = getPsnWhereCondition();
//
//		return userData;
//	}
//	
//	//shenliangc 20140905 н�ʷ��Žڵ����н����ĿԤ����ʾֻ��ʾ�����ϲ�ѯ��������Ա��
//		public String getPsnWhereCondition(){
//			Object[] dataVOs = this.getData().toArray();
//			ArrayList<String> pk_psndoclist = new ArrayList<String>(); 
//			if(!ArrayUtils.isEmpty(dataVOs)){
//				for(int i=0; i<dataVOs.length; i++)
//				pk_psndoclist.add(((DataVO)dataVOs[i]).getPk_psndoc());
//			}
//			
//			return " pk_psndoc " + SQLHelper.genInClause(pk_psndoclist.toArray(new String[0])) ;
//		}
//		
//		/**
//		 * �������Դ���� �������ڣ�(2003-11-7 16:29:34)
//		 *
//		 * @return java.lang.String
//		 */
//		protected static String getDataSource() {
//
//			return WorkbenchEnvironment.getInstance().getDSName();
//		}
//
//}
//
