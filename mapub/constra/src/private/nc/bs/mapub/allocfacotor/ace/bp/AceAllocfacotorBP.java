package nc.bs.mapub.allocfacotor.ace.bp;

import nc.impl.pubapp.pattern.data.vo.SchemeVOQuery;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;
import nc.vo.mapub.allocfacotor.Factorofinv;

public class AceAllocfacotorBP {

	public Factorofinv[] queryByQueryScheme(IQueryScheme querySheme) {
		QuerySchemeProcessor p = new QuerySchemeProcessor(querySheme);
		p.appendFuncPermissionOrgSql();
		return new SchemeVOQuery<Factorofinv>(Factorofinv.class).query(querySheme,
				null);
	}
}
