package nc.impl.mmpac;

import nc.impl.pub.ace.AceHuanbaoPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.mmpac.huanbao.AggHuanbaoHVO;
import nc.itf.mmpac.IHuanbaoMaintain;
import nc.vo.pub.BusinessException;

public class HuanbaoMaintainImpl extends AceHuanbaoPubServiceImpl
		implements IHuanbaoMaintain {

	@Override
	public void delete(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggHuanbaoHVO[] insert(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggHuanbaoHVO[] update(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggHuanbaoHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggHuanbaoHVO[] save(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggHuanbaoHVO[] unsave(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggHuanbaoHVO[] approve(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggHuanbaoHVO[] unapprove(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
