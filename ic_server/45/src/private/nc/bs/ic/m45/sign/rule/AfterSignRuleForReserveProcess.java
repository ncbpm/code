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
 * 1.产成品入库如果是按照销售订单生成的销售订单,
 * 且入库的物料是销售订单上物料，则该物料+批次预留给该销售订单
 * 
 */
public class AfterSignRuleForReserveProcess implements IRule<PurchaseInVO> {

	@Override
	public void process(PurchaseInVO[] vos) {
		// TODO 自动生成的方法存根
		for (PurchaseInVO vo : vos) {
			ICBillBodyVO[] bvos = vo.getChildrenVO();
			if (bvos == null || bvos.length == 0) {
				continue;
			}
			// 如果源头是销售订单才执行
			if (!"30".equalsIgnoreCase(bvos[0].getCfirsttype())) {
				continue;
			}
		
			// 根据销售订单来查询，未考虑合单生产
			IReserveAssist reserve = NCLocator.getInstance().lookup(
					IReserveAssist.class);
			ResRequireQueryParam parm = assmbleQueryParam(vo);		
		}
	}

	private ResRequireQueryParam assmbleQueryParam(PurchaseInVO vo) {
		// TODO 自动生成的方法存根
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
		// 组织信息
		ConditionVO cvo = new ConditionVO();
		listvo.add(cvo);
		cvo.setFieldCode("pk_org");
		cvo.setFieldName("库存组织");
		cvo.setOrderSequence(0);
		cvo.setOperaCode("=");
		cvo.setOperaName("等于");
		cvo.setValue(vo.getHead().getPk_org());
		
		for(String billcode:cfirstbillcode){
			// 来源单据信息
			ConditionVO cvo2 = new ConditionVO();
			listvo.add(cvo2);
			cvo2.setFieldCode("vreqbillcode");
			cvo2.setFieldName("需求单号");
			cvo2.setValue(vo.getHead().getPk_org());
			cvo2.setOrderSequence(0);
			cvo2.setOperaCode("=");
			cvo2.setOperaName("等于");
			cvo2.setValue(billcode);
		}
		// 物料信息cmaterialoid.code
		for (String mpk : cmaterial) {
			ConditionVO cm = new ConditionVO();
			listvo.add(cm);
			cm.setFieldCode("cmaterialvid");
			cm.setFieldName("需求单号");
			cm.setValue(vo.getHead().getPk_org());
			cm.setOrderSequence(0);
			cm.setOperaCode("=");
			cm.setOperaName("等于");
			cm.setValue(mpk);
		}
		return parm;
	}

}
