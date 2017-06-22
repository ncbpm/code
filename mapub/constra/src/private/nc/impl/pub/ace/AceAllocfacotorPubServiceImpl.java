package nc.impl.pub.ace;
import nc.bs.mapub.allocfacotor.ace.bp.AceAllocfacotorBP;
import nc.impl.pubapp.pub.smart.SmartServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.ISuperVO;
import nc.vo.mapub.allocfacotor.Factorofinv;
import nc.vo.uif2.LoginContext;

public abstract class AceAllocfacotorPubServiceImpl extends SmartServiceImpl {
	public Factorofinv[] pubquerybasedoc(IQueryScheme querySheme)
			throws nc.vo.pub.BusinessException {
		return new AceAllocfacotorBP().queryByQueryScheme(querySheme);
	}
}