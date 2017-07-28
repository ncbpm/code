package nc.bpm.pu.pfxx;

import nc.bs.dao.BaseDAO;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.jdbc.framework.SQLParameter;
import nc.vo.ic.pub.check.VOCheckUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.m21.entity.PayPlanVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;

/**
 * �ʽ�ƻ���д�ӿ�
 * 
 * @author Administrator
 *
 */
public class MFundPlanForBpmUpdate extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// 1.�õ�ת�����VO����,ȡ�����򵼵�һ��ע���VO��Ϣ
		AggregatedValueObject resvo = (AggregatedValueObject) vo;
		OrderVO bpmOrder = (OrderVO) resvo;
		// 2. У�����ݵĺϷ���:1.���ݽṹ���� 2.������֯+���ݺ�У���Ƿ��ظ�.
		checkData(resvo);
		// 3.��ȫ����,���ҵ�������״̬
		fillData(resvo);
		//4. ��д����
		updateNCVO(resvo);
		return "���³ɹ�";


	}
	/**
	 * ���»��������ɹ�����ƻ���ϸ����
	 * @param resvo
	 * @throws BusinessException 
	 */
	private void updateNCVO(AggregatedValueObject resvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		OrderVO bpmOrder = (OrderVO) resvo;
		String vdef1 = bpmOrder.getHVO().getVdef1();
		BaseDAO dao = new  BaseDAO();
		PayPlanVO[] vos = (PayPlanVO[]) bpmOrder.getChildren(PayPlanVO.class);
		if("�ɹ�����ƻ�".equalsIgnoreCase(vdef1)){
			//���ձ�����ѭ�����£�Rainbow��Ŀʵ�ʣ����嵥��
			String sql ="  update po_order_payplan set def30 = decode(def30,null,0, '~', 0, def30) +��?�� where   pk_order_payplan =?";
			SQLParameter param = new SQLParameter();
			for(PayPlanVO body:vos){
				param.addParam(body.getNorigmny()== null? UFDouble.ZERO_DBL:body.getNorigmny());
				param.addParam(body.getPk_order_payplan());
				int rs = dao.executeUpdate(sql,param);
				if(rs <=0){
					throw new BusinessException("��������δ��ѯ���ɹ�����ƻ�. select * from po_order_payplan where pk_praybill_b='"+body.getPk_order_payplan()+"'");
				}
				param.clearParams();
			}
		}else{
			//Ӧ����
			//���ձ�����ѭ�����£�	
			String sql ="  update ap_payableitem set def30 =  decode(def30,null,0, '~', 0, def30) +��?�� where  pk_payableitem =?";
			SQLParameter param = new SQLParameter();
			for(PayPlanVO body:vos){
				param.addParam(body.getNorigmny()== null? UFDouble.ZERO_DBL:body.getNorigmny());
				param.addParam(body.getPk_order_payplan());
				int rs = dao.executeUpdate(sql,param);
				if(rs <=0){
					throw new BusinessException("��������δ��ѯ��Ӧ������ϸ. select * from ap_payableitem where pk_payableitem='"+body.getPk_order_payplan()+"'");
				}
				param.clearParams();
			}
		
			
		}
		
	}

	/**
	 * 
	 * @param resvo
	 */
	private void fillData(AggregatedValueObject resvo) {
		// TODO �Զ����ɵķ������
		
	}

	/**
	 * 
	 * @param ����У��
	 * @throws ValidationException 
	 */
	private void checkData(AggregatedValueObject resvo) throws ValidationException {
		// TODO �Զ����ɵķ������
		OrderVO bpmOrder = (OrderVO) resvo;
		PayPlanVO[] vos = (PayPlanVO[]) bpmOrder.getChildren(PayPlanVO.class);
		
		VOCheckUtil.checkHeadNotNullFields(bpmOrder, new String[] { "vdef1"});
		
		VOCheckUtil.checkBodyNotNullFields(bpmOrder, new String[] { "pk_order",
				"pk_order_payplan", "norigmny"});
	}
}
