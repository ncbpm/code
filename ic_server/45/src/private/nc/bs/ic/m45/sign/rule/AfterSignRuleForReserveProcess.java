package nc.bs.ic.m45.sign.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.m45.entity.PurchaseInVO;


/**
 * 
 * 1.����Ʒ�������ǰ������۶������ɵ����۶�����
 * ���������������۶��������ϣ��������+����Ԥ���������۶���
 *
 */
public class AfterSignRuleForReserveProcess implements
IRule<PurchaseInVO> {

	@Override
	public void process(PurchaseInVO[] vos) {
		// TODO �Զ����ɵķ������
		for(PurchaseInVO vo:vos){
			ICBillBodyVO[] bvos = vo.getChildrenVO();
			if(bvos == null || bvos.length ==0){
				continue;
			}
			
			
		}
	}

	

}
