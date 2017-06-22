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
 * ��������Զ��Ƶ�
 * 
 * @author liyf
 */
public class CostInAction extends NCAction {
	/**
	 * �Զ�����
	 */
	private static final long serialVersionUID = -7796364072744108436L;

	/**
	 * �������ӿ�
	 */
	private IPullDataQueryService pullDataQueryService = null;

	private ListFetchDataMainPnl listFetchDataMainPnl;

	/**
	 * ���췽��������ⰴť �������ƺͱ���
	 */
	public CostInAction() {
		this.setBtnName("�������");
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		/* "03830002-0398=����ȡ�������Ժ�..." */
		String name = NCLangRes.getInstance().getStrByID("3830002_0",
				"03830002-0398");
		CMActionProgressor handler = new CMActionProgressor(this,
				"fetchData4SelectVO");
		handler.process(name, this.getListFetchDataMainPnl().getModel()
				.getContext());
	}

	/**
	 * �õ�ѡ������ݲ�ת����Ӧ���ݣ����뵽���ݿ���
	 */
	public void fetchData4SelectVO() {
		// �����ʾ��Ϣ
		ShowStatusBarMsgUtil.showStatusBarMsg("", this
				.getListFetchDataMainPnl().getModel().getContext());

		// �õ�����ѡ�������key=����+ȡ������value=ȡ����Ϣvo
		Map<FetchKeyVO, List<PullDataStateVO>> fetchSelectDataMap = this
				.getPullDataMap(this.getFetchDataMainSelectVO());
		// �õ�������������key=����+ȡ������value=ȡ����Ϣvo
		Map<FetchKeyVO, List<PullDataStateVO>> fetchAllDataMap = this
				.getPullDataMap(this.getFetchDataMainAllVO());
		// ����Ƿ�ѡ������
		if (CMMapUtil.isEmpty(fetchSelectDataMap)) {
//			MessageDialog.showWarningDlg(this.listFetchDataMainPnl,
//					FetchDataLangConst.getWARN(),
//					FetchDataLangConst.getWARN_SELECTNOROW());
//			return;
		}
		// ������ڼ�
		if (CMStringUtil.isEmpty(this.getListFetchDataMainPnl()
				.getAccountPeriod().getRefPK())) {
			MessageDialog.showWarningDlg(this.listFetchDataMainPnl,
					FetchDataLangConst.getWARN(),
					FetchDataLangConst.getUNSELECT_ACCOUNTPERIOD());
			return;
		}
		// ��ҵ�Ƿ�ȫ��ȡ��
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
//							FetchDataLangConst.getWARN(), "���������ҵȡ��");
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
			// ���ɱ������Ƿ����
			if (!checkBeforeFetchData(paramvo, false)) {
				return;
			}

			// �����ҵȡ���Ƿ��Ѿ����

			NCLocator.getInstance().lookup(IPullDataMaintainService.class)
					.fetchAllData(paramvo);
			// ����״̬��
			// this.showMsg(errorStatusMap, fetchSelectDataMap);
			ShowStatusBarMsgUtil.showStatusBarMsg("��������Զ��Ƶ���� ",
					this.listFetchDataMainPnl.getModel().getContext());
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}

	}

	/**
	 * ȡ��ǰ����У��
	 * 
	 * @param pullDataStateVOs
	 *            ѡ���PullDataStateVO
	 * @param paramvo
	 *            FetchParamVO
	 * @return true if the check is ok
	 * @throws BusinessException
	 *             �쳣
	 */
	public boolean checkBeforeFetchData(FetchParamVO paramvo,
			boolean isCheckFlag) {
		// �����Ӧ����ڼ��Թ���
		try {
			if (this.isCMAcountClose(paramvo.getPk_group(),
					paramvo.getPk_org(), paramvo.getDaccountperiod())) {
				ExceptionUtils.wrappBusinessException(FetchDataLangConst
						.getWARN_CMACCOUNTCLOSE());
				return false;
			}
			// ����Ƿ���ʡ��ڳ�����
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
	 * �����ڳ����˺͹��˻���ڼ�ҵ�����У��
	 * 
	 * @param pkGroup
	 *            ����
	 * @param pkOrg
	 *            ����
	 * @param daccountperiod
	 *            ����ڼ�
	 * @return true if the account is not close
	 * @throws BusinessException
	 */
	private boolean checkBegainPeriod(String pkGroup, String pkOrg,
			String daccountperiod) throws BusinessException {
		// �ж��Ƿ������ڳ��ڼ�
		String startPeriod = BDAdapter.getBeginAccount(pkOrg, Boolean.TRUE);
		if (CMStringUtil.isEmpty(startPeriod)) {
			ExceptionUtils.wrappBusinessException(ProductLangConst
					.beginPeriodMassage());
			/*
			 * @res "����û�������ڳ��ڼ�!"
			 */
		}
		if (daccountperiod.compareTo(startPeriod) < 0) {
			ExceptionUtils
					.wrappBusinessException(CMLangConstPub.LATER_THAN_ACCOUNTPERIOD_ERR);
			/* @res "����ڼ�Ӧ���ڵ���ҵ���ڳ��ڼ�" */
		}
		return true;
	}

	/**
	 * �ɱ����Ϲ���
	 * 
	 * @param pkGroup
	 *            pkGroup
	 * @param pkOrg
	 *            pkOrg
	 * @param daccountperiod
	 *            ����ڼ�
	 * @return �Ƿ�ɱ����Ϲ���
	 * @throws BusinessException
	 *             BusinessException
	 */
	private boolean isCMAcountClose(String pkGroup, String pkOrg,
			String daccountperiod) throws BusinessException {
		return this.getICMAcountPubService().isCloseAccount(pkGroup, pkOrg,
				new String[] { daccountperiod });
	}

	/**
	 * �Ƿ�ɱ����Ϲ���
	 */
	ICostDataCloseForCMService cmAcountPubService = null;

	/**
	 * �Ƿ�ɱ����Ϲ���
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
	 * ��ѯ״̬�� ����������/����
	 */
	public Integer[] queryVoFromPullStatusInfo(PullDataStateVO paramvo)
			throws BusinessException {
		return this.getPullDataQueryService()
				.queryVoFromPullStatusInfo(paramvo);
	}

	/**
	 * �õ���ҳ�����ѡ���е�vo
	 **/
	private PullDataStateVO[] getFetchDataMainSelectVO() {
		return (PullDataStateVO[]) this.listFetchDataMainPnl.getBillListView()
				.getBillListPanel().getHeadBillModel()
				.getBodySelectedVOs(PullDataStateVO.class.getName());
	}

	/**
	 * �õ���ҳ��������е�vo
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

	// key=����+ȡ������value=ȡ����Ϣvo
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
