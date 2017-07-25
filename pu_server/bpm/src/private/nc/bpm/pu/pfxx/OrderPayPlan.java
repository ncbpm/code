package nc.bpm.pu.pfxx;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.vo.ic.pub.check.VOCheckUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.m21.entity.PayPlanVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;

/**
 * �ɹ���������ƻ�BPM��д�ӿ�
 * @author liyf
 *
 */
public class OrderPayPlan extends AbstractPfxxPlugin {

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
	 */
	private void updateNCVO(AggregatedValueObject resvo) {
		// TODO �Զ����ɵķ������
		OrderVO bpmOrder = (OrderVO) resvo;
		PayPlanVO[] vos = (PayPlanVO[]) bpmOrder.getChildren(PayPlanVO.class);
		List<String> cpks = new ArrayList<String>();
		for(PayPlanVO vo:vos){
			//�������ƻ�Ϊ�գ�˵���������Ĳɹ�����ƻ�
			if(StringUtils.isEmpty(vo.getPk_order_payplan())){
				
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
		
		VOCheckUtil.checkBodyNotNullFields(bpmOrder, new String[] { "pk_order",
				"nshouldnum", "csourcebillhid", "csourcebillbid", "ncostprice",
				"cprojectid" });
	}
}
