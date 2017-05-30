package nc.bs.hr.wa.adjust.plugin;

import java.text.SimpleDateFormat;
import java.util.Date;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.itf.hr.wa.IWaAdjustManageService;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pflow.PfUserObject;
import nc.vo.wa.adjust.AggPsnappaproveVO;
import nc.vo.wa.adjust.PsnappaproveVO;




/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * @author ufsoft
 * @version Your Project V60
 */
public class BpmAdjustExpPfxxPlugin< T extends AggPsnappaproveVO> extends
nc.bs.pfxx.plugin.AbstractPfxxPlugin {	
	
	
	private PfUserObject[] userObjs;
	
	/**
	 * 将由XML转换过来的VO导入NC系统。业务插件实现此方法即可。<br>
	 * 请注意，业务方法的校验一定要充分
	 * 
	 * @param vo
	 *            转换后的vo数据，在NC系统中可能为ValueObject,SuperVO,AggregatedValueObject,IExAggVO等。
	 * @param swapContext
	 *            各种交换参数，组织，接受方，发送方，帐套等等
	 * @param aggxsysvo
	 *            辅助信息vo
	 * @return定调资申请
	 * @throws BusinessException
	 */
	protected Object processBill(Object vo, ISwapContext swapContext, AggxsysregisterVO aggxsysvo) throws BusinessException {

		//1.得到转换后的VO数据,取决于向导第一步注册的VO信息
 		AggPsnappaproveVO bill = (AggPsnappaproveVO)vo;
 		PsnappaproveVO head = setHeaderDefault((PsnappaproveVO) bill.getParentVO());
//		if (head.getBilltype() == null) {
//			throw new BusinessException("单据的单据类型编码字段不能为空，请输入值");
//		}

		if (head.getPk_group() == null) {
			throw new BusinessException("单据的所属集团字段不能为空，请输入值");
		}
		if (head.getPk_org() == null) {
			throw new BusinessException("单据的财务组织字段不能为空，请输入值");
		}

		// 2.查询此单据是否已经被导入过
		String oldPk = PfxxPluginUtils.queryBillPKBeforeSaveOrUpdate(
				swapContext.getBilltype(), swapContext.getDocID());
//		if (oldPk != null) {
//
//			// 这个判断，好像平台已经过，如果单据已导入，且replace="N"，那么平台就会抛出异常，提示不可重复
//			if (swapContext.getReplace().equalsIgnoreCase("N"))
//				throw new BusinessException(
//						"不允许重复导入单据，请检查是否是操作错误；如果想更新已导入单据，请把数据文件的replace标志设为‘Y’");
//
//			IWaAdjustQueryService voucherbo = (IWaAdjustQueryService) NCLocator
//					.getInstance().lookup(IWaAdjustQueryService.class.getName());
//			AggPsnappaproveVO preVO = voucherbo.queryPsnappaproveVOByPk(oldPk);
//
//			if (preVO != null && preVO.getParentVO() != null) {
//
//				throw new BusinessException("单据已存在，不允许重复导入单据。");
//
//			}
//		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		String strCode = formatter.format(new Date());
		head.setBillcode(strCode); //

		AggPsnappaproveVO newBill = this.insertBill(bill, head);

		String pk = null;
		if (newBill != null) {
			pk = newBill.getParentVO().getPrimaryKey();
		}
		if (oldPk != null) {
			PfxxPluginUtils.deleteIDvsPKByDocPK(oldPk);
		}
		PfxxPluginUtils.addDocIDVsPKContrast(swapContext.getBilltype(),
				swapContext.getDocID(), pk);
		return pk;
	}
	
	private AggPsnappaproveVO insertBill(AggPsnappaproveVO bill, PsnappaproveVO head)
			throws BusinessException {

		IWaAdjustManageService voucherbo = (IWaAdjustManageService) NCLocator
				.getInstance().lookup(IWaAdjustManageService.class.getName());

		AggPsnappaproveVO res = voucherbo.insert(bill);
		
		res = (AggPsnappaproveVO) NCLocator
        .getInstance()
        .lookup(IPFBusiAction.class).processAction(IPFActionName.SAVE, head.getBilltype(), new WorkflownoteVO(), res, getUserObj(), null);
		
		res = (AggPsnappaproveVO) NCLocator
        .getInstance()
        .lookup(IPFBusiAction.class).processAction(IPFActionName.APPROVE, head.getBilltype(), null, res, getUserObj(), null);
		return res;
	}
	
	public PfUserObject[] getUserObj() {
		if (userObjs == null) {
			userObjs = new PfUserObject[] { new PfUserObject() };
		}
		return userObjs;
	}

	/**
	 * 设置表头默认信息
	 * 
	 * @param headerVo
	 * @return
	 * @throws BusinessException
	 */
	private PsnappaproveVO setHeaderDefault(PsnappaproveVO header)
			throws BusinessException {
		Integer ZERO = Integer.valueOf(0);
		/* 单据状态为未审核 */
		header.setConfirmstate(-1);
		header.setDr(ZERO);
		header.setCreationtime(new UFDateTime());
		header.setBilltype("6301");
		return header;
	}
	
}
