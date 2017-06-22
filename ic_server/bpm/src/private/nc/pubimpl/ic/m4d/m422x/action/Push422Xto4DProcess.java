package nc.pubimpl.ic.m4d.m422x.action;

import nc.bs.ic.general.util.GenBsUtil;
import nc.bs.ic.pub.base.ICRule;
import nc.bs.ic.pub.env.ICBSContext;
import nc.vo.ic.m4d.deal.BillTransTo4DProcess;
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
		context.setICPFParameter(icPFParameter);

		BillTransTo4DProcess proc = new BillTransTo4DProcess(true);
		GenBsUtil.initTransBillBaseProcess(proc);
		proc.setAutoPick(false);
		proc.setFillNum(true);
	}
}
