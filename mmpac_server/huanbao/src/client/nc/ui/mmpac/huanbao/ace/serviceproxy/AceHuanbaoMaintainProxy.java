package nc.ui.mmpac.huanbao.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.mmpac.IHuanbaoMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceHuanbaoMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IHuanbaoMaintain query = NCLocator.getInstance().lookup(
				IHuanbaoMaintain.class);
		return query.query(queryScheme);
	}

}