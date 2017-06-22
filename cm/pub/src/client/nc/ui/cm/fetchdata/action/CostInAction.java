package nc.ui.cm.fetchdata.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bd.framework.base.CMArrayUtil;
import nc.bd.framework.base.CMMapUtil;
import nc.bd.framework.base.CMStringUtil;
import nc.bs.framework.common.NCLocator;
import nc.cmpub.business.adapter.BDAdapter;
import nc.itf.cm.fetchdata.IPullDataMaintainService;
import nc.itf.cm.fetchdata.IPullDataQueryService;
import nc.pubitf.cm.costdataclose.cm.pub.ICostDataCloseForCMService;
import nc.ui.cm.fetchdata.view.ListFetchDataMainPnl;
import nc.ui.cmpub.business.util.CMActionProgressor;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.cm.fetchdata.cmconst.FetchDataLangConst;
import nc.vo.cm.fetchdata.entity.FetchKeyVO;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.cm.fetchdata.entity.PullDataStateVO;
import nc.vo.cm.fetchdata.enumeration.FetchDataObjEnum;
import nc.vo.cm.product.ProductLangConst;
import nc.vo.cmpub.business.lang.CMLangConstPub;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 费用入库自动制单
 * 
 * @author liyf
 */
public class CostInAction extends NCAction {
	/**
	 * 自动生成
	 */
	private static final long serialVersionUID = -7796364072744108436L;

	/**
	 * 费用入库接口
	 */
	private IPullDataQueryService pullDataQueryService = null;

	private ListFetchDataMainPnl listFetchDataMainPnl;

	/**
	 * 构造方法费用入库按钮 设置名称和编码
	 */
	public CostInAction() {
		this.setBtnName("费用入库");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		/* "03830002-0398=正在取数，请稍后..." */
		String name = NCLangRes.getInstance().getStrByID("3830002_0",
				"03830002-0398");
		CMActionProgressor handler = new CMActionProgressor(this,
				"fetchData4SelectVO");
		handler.process(name, this.getListFetchDataMainPnl().getModel()
				.getContext());
	}

	/**
	 * 得到选择的数据并转换相应数据，插入到数据库中
	 */
	public void fetchData4SelectVO() {
		// 清空提示信息
		ShowStatusBarMsgUtil.showStatusBarMsg("", this
				.getListFetchDataMainPnl().getModel().getContext());

		// 得到表体选择的数据key=工厂+取数对象，value=取数信息vo
		Map<FetchKeyVO, List<PullDataStateVO>> fetchSelectDataMap = this
				.getPullDataMap(this.getFetchDataMainSelectVO());
		// 得到表体所有数据key=工厂+取数对象，value=取数信息vo
		Map<FetchKeyVO, List<PullDataStateVO>> fetchAllDataMap = this
				.getPullDataMap(this.getFetchDataMainAllVO());
		// 检查是否选择数据
		if (CMMapUtil.isEmpty(fetchSelectDataMap)) {
//			MessageDialog.showWarningDlg(this.listFetchDataMainPnl,
//					FetchDataLangConst.getWARN(),
//					FetchDataLangConst.getWARN_SELECTNOROW());
//			return;
		}
		// 检查会计期间
		if (CMStringUtil.isEmpty(this.getListFetchDataMainPnl()
				.getAccountPeriod().getRefPK())) {
			MessageDialog.showWarningDlg(this.listFetchDataMainPnl,
					FetchDataLangConst.getWARN(),
					FetchDataLangConst.getUNSELECT_ACCOUNTPERIOD());
			return;
		}
		// 作业是否全部取数
//		for (Entry<FetchKeyVO, List<PullDataStateVO>> entry : fetchAllDataMap
//				.entrySet()) {
//			Integer ifetchobjtype = entry.getKey().getIfetchobjtype();
//			if (FetchDataObjEnum.ACT.toIntValue() != ifetchobjtype) {
//				continue;
//			}
//			for (PullDataStateVO stateVO : entry.getValue()) {
//				UFBoolean bfetched = stateVO.getBfetched();
//				if (bfetched == null || !bfetched.booleanValue()) {
//					MessageDialog.showWarningDlg(this.listFetchDataMainPnl,
//							FetchDataLangConst.getWARN(), "请先完成作业取数");
//					return;
//				}
//			}
//		}

		try {
			FetchParamVO paramvo = new FetchParamVO();
			paramvo.setPk_org(this.getListFetchDataMainPnl().getRefFactory()
					.getRefPK());
			paramvo.setPk_group(AppContext.getInstance().getPkGroup());
			paramvo.setDaccountperiod(this.getListFetchDataMainPnl()
					.getAccountPeriod().getRefPK());
			paramvo.setFetchAllDataMap(fetchAllDataMap);
			paramvo.setIfetchobjtype(100);
			// 检查成本资料是否关账
			if (!checkBeforeFetchData(paramvo, false)) {
				return;
			}

			// 检查作业取数是否已经完成

			NCLocator.getInstance().lookup(IPullDataMaintainService.class)
					.fetchAllData(paramvo);
			// 更新状态栏
			// this.showMsg(errorStatusMap, fetchSelectDataMap);
			ShowStatusBarMsgUtil.showStatusBarMsg("费用入库自动制单完成 ",
					this.listFetchDataMainPnl.getModel().getContext());
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}

	}

	/**
	 * 取数前进行校验
	 * 
	 * @param pullDataStateVOs
	 *            选择的PullDataStateVO
	 * @param paramvo
	 *            FetchParamVO
	 * @return true if the check is ok
	 * @throws BusinessException
	 *             异常
	 */
	public boolean checkBeforeFetchData(FetchParamVO paramvo,
			boolean isCheckFlag) {
		// 检查相应会计期间以关帐
		try {
			if (this.isCMAcountClose(paramvo.getPk_group(),
					paramvo.getPk_org(), paramvo.getDaccountperiod())) {
				ExceptionUtils.wrappBusinessException(FetchDataLangConst
						.getWARN_CMACCOUNTCLOSE());
				return false;
			}
			// 检查是否关帐、期初记账
			if (!this.checkBegainPeriod(paramvo.getPk_group(),
					paramvo.getPk_org(), paramvo.getDaccountperiod())) {
				return false;
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		return true;
	}

	/**
	 * 根据期初记账和关账会计期间业务规则校验
	 * 
	 * @param pkGroup
	 *            集团
	 * @param pkOrg
	 *            工厂
	 * @param daccountperiod
	 *            会计期间
	 * @return true if the account is not close
	 * @throws BusinessException
	 */
	private boolean checkBegainPeriod(String pkGroup, String pkOrg,
			String daccountperiod) throws BusinessException {
		// 判断是否设置期初期间
		String startPeriod = BDAdapter.getBeginAccount(pkOrg, Boolean.TRUE);
		if (CMStringUtil.isEmpty(startPeriod)) {
			ExceptionUtils.wrappBusinessException(ProductLangConst
					.beginPeriodMassage());
			/*
			 * @res "工厂没有设置期初期间!"
			 */
		}
		if (daccountperiod.compareTo(startPeriod) < 0) {
			ExceptionUtils
					.wrappBusinessException(CMLangConstPub.LATER_THAN_ACCOUNTPERIOD_ERR);
			/* @res "会计期间应大于等于业务期初期间" */
		}
		return true;
	}

	/**
	 * 成本资料关帐
	 * 
	 * @param pkGroup
	 *            pkGroup
	 * @param pkOrg
	 *            pkOrg
	 * @param daccountperiod
	 *            会计期间
	 * @return 是否成本资料关帐
	 * @throws BusinessException
	 *             BusinessException
	 */
	private boolean isCMAcountClose(String pkGroup, String pkOrg,
			String daccountperiod) throws BusinessException {
		return this.getICMAcountPubService().isCloseAccount(pkGroup, pkOrg,
				new String[] { daccountperiod });
	}

	/**
	 * 是否成本资料关帐
	 */
	ICostDataCloseForCMService cmAcountPubService = null;

	/**
	 * 是否成本资料关帐
	 * 
	 * @return ICMAcountPubService
	 */
	ICostDataCloseForCMService getICMAcountPubService() {
		if (this.cmAcountPubService == null) {
			this.cmAcountPubService = NCLocator.getInstance().lookup(
					ICostDataCloseForCMService.class);
		}
		return this.cmAcountPubService;
	}

	/**
	 * 查询状态表 ，周期天数/方案
	 */
	public Integer[] queryVoFromPullStatusInfo(PullDataStateVO paramvo)
			throws BusinessException {
		return this.getPullDataQueryService()
				.queryVoFromPullStatusInfo(paramvo);
	}

	/**
	 * 得到主页面表体选择行的vo
	 **/
	private PullDataStateVO[] getFetchDataMainSelectVO() {
		return (PullDataStateVO[]) this.listFetchDataMainPnl.getBillListView()
				.getBillListPanel().getHeadBillModel()
				.getBodySelectedVOs(PullDataStateVO.class.getName());
	}

	/**
	 * 得到主页面表体所有的vo
	 **/
	private PullDataStateVO[] getFetchDataMainAllVO() {
		return (PullDataStateVO[]) this.listFetchDataMainPnl.getBillListView()
				.getBillListPanel().getHeadBillModel()
				.getBodyValueVOs(PullDataStateVO.class.getName());
	}

	private IPullDataQueryService getPullDataQueryService() {
		if (this.pullDataQueryService == null) {
			this.pullDataQueryService = NCLocator.getInstance().lookup(
					IPullDataQueryService.class);
		}
		return this.pullDataQueryService;
	}

	public ListFetchDataMainPnl getListFetchDataMainPnl() {
		return this.listFetchDataMainPnl;
	}

	public void setListFetchDataMainPnl(
			ListFetchDataMainPnl listFetchDataMainPnl) {
		this.listFetchDataMainPnl = listFetchDataMainPnl;
	}

	// key=工厂+取数对象，value=取数信息vo
	private Map<FetchKeyVO, List<PullDataStateVO>> getPullDataMap(
			PullDataStateVO[] pullDataStateVOs) {
		Map<FetchKeyVO, List<PullDataStateVO>> fetchDataMap = new HashMap<FetchKeyVO, List<PullDataStateVO>>();
		if (CMArrayUtil.isNotEmpty(pullDataStateVOs)) {
			for (PullDataStateVO vo : pullDataStateVOs) {
				FetchKeyVO keyVO = new FetchKeyVO();
				keyVO.setPk_org(vo.getPk_org());
				keyVO.setIfetchobjtype(vo.getIfetchobjtype());
				List<PullDataStateVO> lst = fetchDataMap.get(keyVO);
				if (lst == null) {
					lst = new ArrayList<PullDataStateVO>();
				}
				lst.add(vo);
				fetchDataMap.put(keyVO, lst);
			}
		}

		return fetchDataMap;
	}
}
