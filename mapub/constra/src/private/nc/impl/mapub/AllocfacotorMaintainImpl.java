package nc.impl.mapub;

import nc.impl.pub.ace.AceAllocfacotorPubServiceImpl;
import nc.impl.pubapp.pub.smart.BatchSaveAction;
import nc.vo.bd.meta.BatchOperateVO;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.mapub.allocfacotor.Factorofinv;
import nc.itf.mapub.IAllocfacotorMaintain;

public class AllocfacotorMaintainImpl extends AceAllocfacotorPubServiceImpl
		implements IAllocfacotorMaintain {

	@Override
	public Factorofinv[] query(IQueryScheme queryScheme) throws BusinessException {
		return super.pubquerybasedoc(queryScheme);
	}

	@Override
	public BatchOperateVO batchSave(BatchOperateVO batchVO) throws BusinessException {
		BatchSaveAction<Factorofinv> saveAction = new BatchSaveAction<Factorofinv>();
		BatchOperateVO retData = saveAction.batchSave(batchVO);
		return retData;
	}
}
