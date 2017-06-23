package nc.pubimpl.ic.m4d.m422x.action;

import nc.bs.ic.general.plugins.CheckMnyUtil;
import nc.bs.ic.general.plugins.CheckScaleUtil;
import nc.bs.ic.pub.base.ICRule;
import nc.vo.ic.m4d.entity.MaterialOutVO;

public class PushCheckProcess extends ICRule<MaterialOutVO> {

	@Override
	public void process(MaterialOutVO[] vos) {

		for (MaterialOutVO vo : vos) {
			new CheckMnyUtil().checkMny(vo);
			new CheckScaleUtil().checkScale(vo);
		}
	}

}
