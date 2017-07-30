package nc.bs.hr.wa.adjust.plugin;

import java.text.SimpleDateFormat;
import java.util.Date;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.itf.hr.wa.IWaAdjustManageService;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pflow.PfUserObject;
import nc.vo.wa.adjust.AggPsnappaproveVO;
import nc.vo.wa.adjust.PsnappaproveVO;

import org.apache.commons.lang.StringUtils;




/**
 * ����������
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
	 * @return
	 * @throws BusinessException
	 */
	protected Object processBill(Object vo, ISwapContext swapContext, AggxsysregisterVO aggxsysvo) throws BusinessException {

		//1.�õ�ת�����VO����,ȡ�����򵼵�һ��ע���VO��Ϣ
 		AggPsnappaproveVO bill = (AggPsnappaproveVO)vo;
 		PsnappaproveVO head = setHeaderDefault((PsnappaproveVO) bill.getParentVO());
		if (head.getPk_group() == null) {
			throw new BusinessException("���ݵ����������ֶβ���Ϊ�գ�������ֵ");
		}
		if (head.getPk_org() == null) {
			throw new BusinessException("���ݵĲ�����֯�ֶβ���Ϊ�գ�������ֵ");
		}
		
		if (head.getBillcode() == null) {
			throw new BusinessException("���ݵĵ��ݱ���ֶβ���Ϊ�գ�������ֵ");
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		String strCode = formatter.format(new Date());
//		head.setBillcode(strCode); //

		AggPsnappaproveVO newBill = this.insertBill(bill, head);

		String pk = null;
		if (newBill != null) {
			pk = newBill.getParentVO().getPrimaryKey();
		}
		
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
		if(StringUtils.isEmpty(header.getBilltype())){
			header.setBilltype("6301");
		}
		if(StringUtils.isEmpty(header.getTranstype())){
			header.setTranstype("6301");
		}
		
		return header;
	}
	
}
