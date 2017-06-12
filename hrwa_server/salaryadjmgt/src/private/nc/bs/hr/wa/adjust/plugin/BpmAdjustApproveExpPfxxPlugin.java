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
	 * ����XMLת��������VO����NCϵͳ��ҵ����ʵ�ִ˷������ɡ�<br>
	 * ��ע�⣬ҵ�񷽷���У��һ��Ҫ���
	 * 
	 * @param vo
	 *            ת�����vo���ݣ���NCϵͳ�п���ΪValueObject,SuperVO,AggregatedValueObject,
	 *            IExAggVO�ȡ�
	 * @param swapContext
	 *            ���ֽ�����������֯�����ܷ������ͷ������׵ȵ�
	 * @param aggxsysvo
	 *            ������Ϣvo
	 * @return
	 * @throws BusinessException
	 */
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggxsysvo) throws BusinessException {

		// 1.�õ�ת�����VO����,ȡ�����򵼵�һ��ע���VO��Ϣ
		AggPsnappaproveVO bill = (AggPsnappaproveVO) vo;

		PsnappaproveVO head = (PsnappaproveVO) bill.getParentVO();
		if (head.getPrimaryKey() == null) {
			throw new BusinessException("���ݵĵ��������ֶβ���Ϊ�գ�������ֵ");
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
