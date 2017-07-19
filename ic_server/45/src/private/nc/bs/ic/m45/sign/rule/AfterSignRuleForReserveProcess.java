package nc.bs.ic.m45.sign.rule;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.ic.reserve.IReserveAssist;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.ic.reserve.pub.ResRequireQueryParam;
import nc.vo.pub.query.ConditionVO;

/**
 * 
 * 1.����Ʒ�������ǰ������۶������ɵ����۶���,
 * ���������������۶��������ϣ��������+����Ԥ���������۶���
 * 
 */
public class AfterSignRuleForReserveProcess implements IRule<PurchaseInVO> {

	@Override
	public void process(PurchaseInVO[] vos) {
		// TODO �Զ����ɵķ������
		for (PurchaseInVO vo : vos) {
			ICBillBodyVO[] bvos = vo.getChildrenVO();
			if (bvos == null || bvos.length == 0) {
				continue;
			}
			// ���Դͷ�����۶�����ִ��
			if (!"30".equalsIgnoreCase(bvos[0].getCfirsttype())) {
				continue;
			}
		
			// �������۶�������ѯ��δ���Ǻϵ�����
			IReserveAssist reserve = NCLocator.getInstance().lookup(
					IReserveAssist.class);
			ResRequireQueryParam parm = assmbleQueryParam(vo);		
		}
	}

	private ResRequireQueryParam assmbleQueryParam(PurchaseInVO vo) {
		// TODO �Զ����ɵķ������
		List<String> cfirstbillcode = new ArrayList<String>();
		List<String> cmaterial = new ArrayList<String>();
		ICBillBodyVO[] bvos = vo.getChildrenVO();
		for (ICBillBodyVO bvo : bvos) {
			if (!cfirstbillcode.contains(bvo.getVfirstbillcode())) {
				cfirstbillcode.add(bvo.getVfirstbillcode());
			}
			if (!cmaterial.contains(bvo.getCmaterialoid())) {
				cmaterial.add(bvo.getCmaterialoid());
			}
		}
		ResRequireQueryParam parm = new ResRequireQueryParam();
		parm.setPk_group(vo.getHead().getPk_group());
		parm.addBillType("30");
		List<ConditionVO> listvo = new ArrayList<ConditionVO>();
		// ��֯��Ϣ
		ConditionVO cvo = new ConditionVO();
		listvo.add(cvo);
		cvo.setFieldCode("pk_org");
		cvo.setFieldName("�����֯");
		cvo.setOrderSequence(0);
		cvo.setOperaCode("=");
		cvo.setOperaName("����");
		cvo.setValue(vo.getHead().getPk_org());
		
		for(String billcode:cfirstbillcode){
			// ��Դ������Ϣ
			ConditionVO cvo2 = new ConditionVO();
			listvo.add(cvo2);
			cvo2.setFieldCode("vreqbillcode");
			cvo2.setFieldName("���󵥺�");
			cvo2.setValue(vo.getHead().getPk_org());
			cvo2.setOrderSequence(0);
			cvo2.setOperaCode("=");
			cvo2.setOperaName("����");
			cvo2.setValue(billcode);
		}
		// ������Ϣcmaterialoid.code
		for (String mpk : cmaterial) {
			ConditionVO cm = new ConditionVO();
			listvo.add(cm);
			cm.setFieldCode("cmaterialvid");
			cm.setFieldName("���󵥺�");
			cm.setValue(vo.getHead().getPk_org());
			cm.setOrderSequence(0);
			cm.setOperaCode("=");
			cm.setOperaName("����");
			cm.setValue(mpk);
		}
		return parm;
	}

}
