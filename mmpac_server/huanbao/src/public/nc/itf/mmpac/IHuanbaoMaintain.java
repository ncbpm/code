package nc.itf.mmpac;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.mmpac.huanbao.AggHuanbaoHVO;
import nc.vo.pub.BusinessException;

public interface IHuanbaoMaintain {

	public void delete(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException;

	public AggHuanbaoHVO[] insert(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException;

	public AggHuanbaoHVO[] update(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException;

	public AggHuanbaoHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggHuanbaoHVO[] save(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException;

	public AggHuanbaoHVO[] unsave(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException;

	public AggHuanbaoHVO[] approve(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException;

	public AggHuanbaoHVO[] unapprove(AggHuanbaoHVO[] clientFullVOs,
			AggHuanbaoHVO[] originBills) throws BusinessException;
}
