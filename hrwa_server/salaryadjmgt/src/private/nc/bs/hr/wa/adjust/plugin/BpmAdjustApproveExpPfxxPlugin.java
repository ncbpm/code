package nc.bs.hr.wa.adjust.plugin;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.itf.hr.wa.IWaAdjustQueryService;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.SQLParameter;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pflow.PfUserObject;
import nc.vo.wa.adjust.AggPsnappaproveVO;
import nc.vo.wa.adjust.PsnappaproveVO;

public class BpmAdjustApproveExpPfxxPlugin<T extends AggPsnappaproveVO> extends
		nc.bs.pfxx.plugin.AbstractPfxxPlugin {

	private PfUserObject[] userObjs;

	/**
	 * 将由XML转换过来的VO导入NC系统。业务插件实现此方法即可。<br>
	 * 请注意，业务方法的校验一定要充分
	 * 
	 * @param vo
	 *            转换后的vo数据，在NC系统中可能为ValueObject,SuperVO,AggregatedValueObject,
	 *            IExAggVO等。
	 * @param swapContext
	 *            各种交换参数，组织，接受方，发送方，帐套等等
	 * @param aggxsysvo
	 *            辅助信息vo
	 * @return
	 * @throws BusinessException
	 */
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggxsysvo) throws BusinessException {

		// 1.得到转换后的VO数据,取决于向导第一步注册的VO信息
		AggPsnappaproveVO bill = (AggPsnappaproveVO) vo;

		PsnappaproveVO head = (PsnappaproveVO) bill.getParentVO();
		if (head.getPrimaryKey() == null) {
			throw new BusinessException("单据的单据主键字段不能为空，请输入值");
		}

		IWaAdjustQueryService voucherbo = (IWaAdjustQueryService) NCLocator
				.getInstance().lookup(IWaAdjustQueryService.class.getName());
		AggPsnappaproveVO preVO = voucherbo.queryPsnappaproveVOByPk(head
				.getPrimaryKey());

		AggPsnappaproveVO res = (AggPsnappaproveVO) NCLocator
				.getInstance()
				.lookup(IPFBusiAction.class)
				.processAction(IPFActionName.SAVE, head.getBilltype(),
						new WorkflownoteVO(), preVO, getUserObj(), null);
		NCLocator
				.getInstance()
				.lookup(IPFBusiAction.class)
				.processAction(IPFActionName.APPROVE, head.getBilltype(), null,
						res, getUserObj(), null);

		if (head.getConfirmstate() == null
				|| IPfRetCheckInfo.PASSING != head.getConfirmstate().intValue()) {
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer
					.append(" update wa_psnappaprove  set confirmstate =0 where  pk_psnapp   =? ");
			SQLParameter param = new SQLParameter();
			param.addParam(head.getPrimaryKey());
			BaseDAO baseDao = new BaseDAO();
			baseDao.executeUpdate(sqlBuffer.toString(), param);
		}

		PfxxPluginUtils.addDocIDVsPKContrast(swapContext.getBilltype(),
				swapContext.getDocID(), head.getPrimaryKey());
		return head.getPrimaryKey();
	}

	public PfUserObject[] getUserObj() {
		if (userObjs == null) {
			userObjs = new PfUserObject[] { new PfUserObject() };
		}
		return userObjs;
	}
}
