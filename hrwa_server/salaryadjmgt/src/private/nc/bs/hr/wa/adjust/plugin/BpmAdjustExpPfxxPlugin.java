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
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 *
 * @author ufsoft
 * @version Your Project V60
 */
public class BpmAdjustExpPfxxPlugin< T extends AggPsnappaproveVO> extends
nc.bs.pfxx.plugin.AbstractPfxxPlugin {	
	
	
	private PfUserObject[] userObjs;
	
	/**
	 * ����XMLת��������VO����NCϵͳ��ҵ����ʵ�ִ˷������ɡ�<br>
	 * ��ע�⣬ҵ�񷽷���У��һ��Ҫ���
	 * 
	 * @param vo
	 *            ת�����vo���ݣ���NCϵͳ�п���ΪValueObject,SuperVO,AggregatedValueObject,IExAggVO�ȡ�
	 * @param swapContext
	 *            ���ֽ�����������֯�����ܷ������ͷ������׵ȵ�
	 * @param aggxsysvo
	 *            ������Ϣvo
	 * @return����������
	 * @throws BusinessException
	 */
	protected Object processBill(Object vo, ISwapContext swapContext, AggxsysregisterVO aggxsysvo) throws BusinessException {

		//1.�õ�ת�����VO����,ȡ�����򵼵�һ��ע���VO��Ϣ
 		AggPsnappaproveVO bill = (AggPsnappaproveVO)vo;
 		PsnappaproveVO head = setHeaderDefault((PsnappaproveVO) bill.getParentVO());
//		if (head.getBilltype() == null) {
//			throw new BusinessException("���ݵĵ������ͱ����ֶβ���Ϊ�գ�������ֵ");
//		}

		if (head.getPk_group() == null) {
			throw new BusinessException("���ݵ����������ֶβ���Ϊ�գ�������ֵ");
		}
		if (head.getPk_org() == null) {
			throw new BusinessException("���ݵĲ�����֯�ֶβ���Ϊ�գ�������ֵ");
		}

		// 2.��ѯ�˵����Ƿ��Ѿ��������
		String oldPk = PfxxPluginUtils.queryBillPKBeforeSaveOrUpdate(
				swapContext.getBilltype(), swapContext.getDocID());
//		if (oldPk != null) {
//
//			// ����жϣ�����ƽ̨�Ѿ�������������ѵ��룬��replace="N"����ôƽ̨�ͻ��׳��쳣����ʾ�����ظ�
//			if (swapContext.getReplace().equalsIgnoreCase("N"))
//				throw new BusinessException(
//						"�������ظ����뵥�ݣ������Ƿ��ǲ����������������ѵ��뵥�ݣ���������ļ���replace��־��Ϊ��Y��");
//
//			IWaAdjustQueryService voucherbo = (IWaAdjustQueryService) NCLocator
//					.getInstance().lookup(IWaAdjustQueryService.class.getName());
//			AggPsnappaproveVO preVO = voucherbo.queryPsnappaproveVOByPk(oldPk);
//
//			if (preVO != null && preVO.getParentVO() != null) {
//
//				throw new BusinessException("�����Ѵ��ڣ��������ظ����뵥�ݡ�");
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
	 * ���ñ�ͷĬ����Ϣ
	 * 
	 * @param headerVo
	 * @return
	 * @throws BusinessException
	 */
	private PsnappaproveVO setHeaderDefault(PsnappaproveVO header)
			throws BusinessException {
		Integer ZERO = Integer.valueOf(0);
		/* ����״̬Ϊδ��� */
		header.setConfirmstate(-1);
		header.setDr(ZERO);
		header.setCreationtime(new UFDateTime());
		header.setBilltype("6301");
		return header;
	}
	
}
