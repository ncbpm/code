package nc.impl.pub.ace;

import nc.bs.mmpac.huanbao.ace.bp.AceHuanbaoInsertBP;
import nc.bs.mmpac.huanbao.ace.bp.AceHuanbaoUpdateBP;
import nc.bs.mmpac.huanbao.ace.bp.AceHuanbaoDeleteBP;
import nc.bs.mmpac.huanbao.ace.bp.AceHuanbaoSendApproveBP;
import nc.bs.mmpac.huanbao.ace.bp.AceHuanbaoUnSendApproveBP;
import nc.bs.mmpac.huanbao.ace.bp.AceHuanbaoApproveBP;
import nc.bs.mmpac.huanbao.ace.bp.AceHuanbaoUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.mmpac.huanbao.AggHuanbaoHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceHuanbaoPubServiceImpl {
	// 新增
	public AggHuanbaoHVO[] pubinsertBills(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggHuanbaoHVO> transferTool = new BillTransferTool<AggHuanbaoHVO>(
					clientFullVOs);
			// 调用BP
			AceHuanbaoInsertBP action = new AceHuanbaoInsertBP();
			AggHuanbaoHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceHuanbaoDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggHuanbaoHVO[] pubupdateBills(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggHuanbaoHVO> transferTool = new BillTransferTool<AggHuanbaoHVO>(
					clientFullVOs);
			AceHuanbaoUpdateBP bp = new AceHuanbaoUpdateBP();
			AggHuanbaoHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggHuanbaoHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggHuanbaoHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggHuanbaoHVO> query = new BillLazyQuery<AggHuanbaoHVO>(
					AggHuanbaoHVO.class);
			bills = query.query(queryScheme, null);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return bills;
	}

	/**
	 * 由子类实现，查询之前对queryScheme进行加工，加入自己的逻辑
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// 查询之前对queryScheme进行加工，加入自己的逻辑
	}

	// 提交
	public AggHuanbaoHVO[] pubsendapprovebills(
			AggHuanbaoHVO[] clientFullVOs, AggHuanbaoHVO[] originBills)
			throws BusinessException {
		AceHuanbaoSendApproveBP bp = new AceHuanbaoSendApproveBP();
		AggHuanbaoHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggHuanbaoHVO[] pubunsendapprovebills(
			AggHuanbaoHVO[] clientFullVOs, AggHuanbaoHVO[] originBills)
			throws BusinessException {
		AceHuanbaoUnSendApproveBP bp = new AceHuanbaoUnSendApproveBP();
		AggHuanbaoHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggHuanbaoHVO[] pubapprovebills(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceHuanbaoApproveBP bp = new AceHuanbaoApproveBP();
		AggHuanbaoHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggHuanbaoHVO[] pubunapprovebills(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceHuanbaoUnApproveBP bp = new AceHuanbaoUnApproveBP();
		AggHuanbaoHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}