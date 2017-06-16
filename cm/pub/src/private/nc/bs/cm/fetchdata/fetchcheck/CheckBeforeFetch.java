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
 * 取数前的检查
 */
public class CheckBeforeFetch {
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
		//定额取存货核算的产成品入库单，所有需要检查是否关账
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
		// 判断是否关帐
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
	 * 是否有未关帐的成本域
	 * 
	 * @param paramvo
	 * @return true:是；false:fou
	 */
	private boolean isIACostRegionClose(FetchParamVO paramvo,
			boolean isCheckFlag) {
		try {
			// 账簿
			String acountBook = IAAdapter.getAcountBookParam4ICInteface(paramvo
					.getPk_org());
			// 成本域
			Set<String> costDomain = BDAdapter
					.getAllCostRegionIDByPkOrg(paramvo.getPk_org());
			paramvo.setAcountBook(acountBook);
			paramvo.setCostDomain(costDomain);
			if (CMCollectionUtil.isEmpty(costDomain)) {
				ExceptionUtils.wrappBusinessException(FetchDataLangConst
						.getWARN_COSTDOMAINNULL());
			}
			// 是否校验关帐控制
			if (isCheckFlag || !VoUtil.getIsIACloseControl(paramvo.getPk_org())) {
				return false;
			}
			Map<String, Boolean> costDomainMap = IAAdapter
					.queryIsIAPeriodAccounted(
							costDomain.toArray(new String[0]),
							new String[] { acountBook },
							new String[] { paramvo.getDaccountperiod() });

			// 记录当前成本域为空的
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
}
