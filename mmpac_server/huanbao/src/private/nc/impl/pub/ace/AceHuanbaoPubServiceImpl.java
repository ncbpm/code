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
	// ����
	public AggHuanbaoHVO[] pubinsertBills(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggHuanbaoHVO> transferTool = new BillTransferTool<AggHuanbaoHVO>(
					clientFullVOs);
			// ����BP
			AceHuanbaoInsertBP action = new AceHuanbaoInsertBP();
			AggHuanbaoHVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceHuanbaoDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggHuanbaoHVO[] pubupdateBills(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggHuanbaoHVO> transferTool = new BillTransferTool<AggHuanbaoHVO>(
					clientFullVOs);
			AceHuanbaoUpdateBP bp = new AceHuanbaoUpdateBP();
			AggHuanbaoHVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
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
	 * ������ʵ�֣���ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// ��ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	}

	// �ύ
	public AggHuanbaoHVO[] pubsendapprovebills(
			AggHuanbaoHVO[] clientFullVOs, AggHuanbaoHVO[] originBills)
			throws BusinessException {
		AceHuanbaoSendApproveBP bp = new AceHuanbaoSendApproveBP();
		AggHuanbaoHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggHuanbaoHVO[] pubunsendapprovebills(
			AggHuanbaoHVO[] clientFullVOs, AggHuanbaoHVO[] originBills)
			throws BusinessException {
		AceHuanbaoUnSendApproveBP bp = new AceHuanbaoUnSendApproveBP();
		AggHuanbaoHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggHuanbaoHVO[] pubapprovebills(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceHuanbaoApproveBP bp = new AceHuanbaoApproveBP();
		AggHuanbaoHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

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