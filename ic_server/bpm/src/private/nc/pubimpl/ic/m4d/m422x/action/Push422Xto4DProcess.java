package nc.pubimpl.ic.m4d.m422x.action;

import nc.bs.ic.general.util.GenBsUtil;
import nc.bs.ic.pub.base.ICRule;
import nc.bs.ic.pub.env.ICBSContext;
import nc.vo.ic.m4d.deal.bpm.Bill422Xto4DProcess;
import nc.vo.ic.m4d.entity.MaterialOutVO;
import nc.vo.ic.pub.pf.ICPFParameter;

public class Push422Xto4DProcess extends ICRule<MaterialOutVO> {

	@Override
	public void process(MaterialOutVO[] vos) {

		ICBSContext context = new ICBSContext();
		ICPFParameter icPFParameter = context.getICPFParameter();
		if (null == icPFParameter)
			icPFParameter = new ICPFParameter();
		icPFParameter.setBSafetyStockCheckFlag(true);
		icPFParameter.setbToleranceCheckFlag(true);
		context.setICPFParameter(icPFParameter);
		
		Bill422Xto4DProcess proc = new Bill422Xto4DProcess(true);
		GenBsUtil.initTransBillBaseProcess(proc);
		proc.setAutoPick(false);
		proc.setFillNum(true);
		proc.processBillVOs(vos);
//		proc.processBillRule(vos)
	}
}
