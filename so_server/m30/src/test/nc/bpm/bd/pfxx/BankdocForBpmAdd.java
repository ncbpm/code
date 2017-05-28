package nc.bpm.bd.pfxx;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.itf.bd.bankdoc.IBankdocService;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.bd.bankdoc.BankLinkmanVO;
import nc.vo.bd.bankdoc.BankdocVO;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;

/**
 * 银行档案的信息导入
 * @author pangchen
 *
 */
public class BankdocForBpmAdd extends AbstractPfxxPlugin {

	private IBankdocService service = null;

	private IMDPersistenceQueryService queryService = null;

	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggxsysvo) throws BusinessException {

		// 1.得到转换后的VO数据,取决于向导第一步注册的VO信息
		BankdocVO resvo = (BankdocVO) vo;

		String vopk = resvo.getPk_bankdoc();

		if (StringUtils.isBlank(vopk)) {
			resvo.setStatus(VOStatus.NEW);
			BankLinkmanVO[] linkmanvos = resvo.getLinkmans();
			if (linkmanvos != null && linkmanvos.length > 0) {
				for (BankLinkmanVO linkmanvo : linkmanvos) {
					linkmanvo.setStatus(VOStatus.NEW);
				}

			}
			resvo.setEnablestate(IPubEnumConst.ENABLESTATE_ENABLE);
			resvo = getBankdocService().insertBankdocVO(resvo, false);
			vopk = resvo.getPrimaryKey();
			PfxxPluginUtils.addDocIDVsPKContrast(swapContext.getBilltype(),
					swapContext.getDocID(), swapContext.getOrgPk(), vopk);

		} else {
			NCObject oldvo = getQueryService().queryBillOfNCObjectByPK(
					BankdocVO.class, vopk);
			if (oldvo != null) {
				BankdocVO bankdoc = (BankdocVO) oldvo.getContainmentObject();
				BankLinkmanVO[] linkmanvos = bankdoc.getLinkmans();
				bankdoc.setStatus(VOStatus.UPDATED);
				if (linkmanvos != null) {
					for (BankLinkmanVO linkman : linkmanvos) {
						linkman.setStatus(VOStatus.DELETED);
					}
				}
				bankdoc.setStatus(VOStatus.UPDATED);
				getBankdocService().updateBankdocVO(bankdoc);
				linkmanvos = resvo.getLinkmans();
				if (linkmanvos != null) {
					for (BankLinkmanVO linkman : linkmanvos) {
						linkman.setStatus(VOStatus.NEW);
					}
				}
				
				resvo.setStatus(VOStatus.UPDATED);
				resvo.setCreator(bankdoc.getCreator());
				resvo.setCreationtime(bankdoc.getCreationtime());
				resvo.setEnablestate(IPubEnumConst.ENABLESTATE_ENABLE);
				resvo.setInnercode(bankdoc.getInnercode());
				resvo.setPrimaryKey(vopk);
				getBankdocService().updateBankdocVO(resvo);
			} else
				throw new BusinessException("主键："+vopk+"对应的数据在NC未查询到，请检测库表是否已经删除.");

		}
		return vopk;
	}

	private IBankdocService getBankdocService() {
		if (service == null)
			service = NCLocator.getInstance().lookup(IBankdocService.class);
		return service;
	}

	private IMDPersistenceQueryService getQueryService() {
		if (queryService == null)
			queryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		return queryService;
	}

}
