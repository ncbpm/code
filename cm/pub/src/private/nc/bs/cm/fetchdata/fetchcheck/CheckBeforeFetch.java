package nc.bs.cm.fetchdata.fetchcheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bd.framework.base.CMCollectionUtil;
import nc.bd.framework.base.CMStringUtil;
import nc.bs.cm.fetchdata.util.VoUtil;
import nc.bs.framework.common.NCLocator;
import nc.cmpub.business.adapter.BDAdapter;
import nc.cmpub.business.adapter.IAAdapter;
import nc.pubitf.cm.costdataclose.cm.pub.ICostDataCloseForCMService;
import nc.vo.bd.accessor.IBDData;
import nc.vo.cm.fetchdata.cmconst.FetchDataLangConst;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.cm.fetchdata.enumeration.FetchDataObjEnum;
import nc.vo.cm.product.ProductLangConst;
import nc.vo.cmpub.business.lang.CMLangConstPub;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * ȡ��ǰ�ļ��
 */
public class CheckBeforeFetch {
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
		//����ȡ�������Ĳ���Ʒ��ⵥ��������Ҫ����Ƿ����
		if (paramvo.getIfetchobjtype() == FetchDataObjEnum.DINGE.toIntValue()
				&& this.isIACostRegionClose(paramvo, isCheckFlag)) {
			return false;
		}
		
		if (paramvo.getIfetchobjtype() == FetchDataObjEnum.CHUYUN.toIntValue()) {
			return true;
		}
		if (paramvo.getIfetchobjtype() == FetchDataObjEnum.JIANYAN.toIntValue()) {
			return true;
		}
		if (paramvo.getIfetchobjtype() == FetchDataObjEnum.HUANBAO.toIntValue()) {
			return true;
		}
		// �ж��Ƿ����
		if (paramvo.getIfetchobjtype() != FetchDataObjEnum.ACT.toIntValue()
				&& paramvo.getIfetchobjtype() != FetchDataObjEnum.SPOIL
						.toIntValue()
				&& paramvo.getIfetchobjtype() != FetchDataObjEnum.GXWW
						.toIntValue()
				&& this.isIACostRegionClose(paramvo, isCheckFlag)) {
			return false;
		}
		return true;
	}

	/**
	 * �Ƿ���δ���ʵĳɱ���
	 * 
	 * @param paramvo
	 * @return true:�ǣ�false:fou
	 */
	private boolean isIACostRegionClose(FetchParamVO paramvo,
			boolean isCheckFlag) {
		try {
			// �˲�
			String acountBook = IAAdapter.getAcountBookParam4ICInteface(paramvo
					.getPk_org());
			// �ɱ���
			Set<String> costDomain = BDAdapter
					.getAllCostRegionIDByPkOrg(paramvo.getPk_org());
			paramvo.setAcountBook(acountBook);
			paramvo.setCostDomain(costDomain);
			if (CMCollectionUtil.isEmpty(costDomain)) {
				ExceptionUtils.wrappBusinessException(FetchDataLangConst
						.getWARN_COSTDOMAINNULL());
			}
			// �Ƿ�У����ʿ���
			if (isCheckFlag || !VoUtil.getIsIACloseControl(paramvo.getPk_org())) {
				return false;
			}
			Map<String, Boolean> costDomainMap = IAAdapter
					.queryIsIAPeriodAccounted(
							costDomain.toArray(new String[0]),
							new String[] { acountBook },
							new String[] { paramvo.getDaccountperiod() });

			// ��¼��ǰ�ɱ���Ϊ�յ�
			List<String> costDomainList = new ArrayList<String>();
			for (Entry<String, Boolean> entry : costDomainMap.entrySet()) {
				if (!entry.getValue().booleanValue()) {
					for (String cd : costDomain) {
						if ((cd + acountBook + paramvo.getDaccountperiod())
								.equals(entry.getKey())) {
							costDomainList.add(cd);
						}
					}
				}
			}
			if (CMCollectionUtil.isNotEmpty(costDomainList)) {
				IBDData[] codeNameArr = IAAdapter.getDocByPks(costDomainList
						.toArray(new String[0]));
				StringBuffer allNotAccountCostDomain = new StringBuffer();
				for (IBDData data : codeNameArr) {
					allNotAccountCostDomain.append(data.getName().toString());
					allNotAccountCostDomain.append(",");
				}
				allNotAccountCostDomain.deleteCharAt(allNotAccountCostDomain
						.lastIndexOf(","));
				ExceptionUtils.wrappBusinessException(String.format(
						FetchDataLangConst.getIANOCLOSE_ERR(),
						allNotAccountCostDomain));
				return true;
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		return false;
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
}
