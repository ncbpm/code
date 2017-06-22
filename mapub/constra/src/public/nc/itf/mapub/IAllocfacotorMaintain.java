package nc.itf.mapub;

import nc.itf.pubapp.pub.smart.ISmartService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.mapub.allocfacotor.Factorofinv;

public interface IAllocfacotorMaintain extends ISmartService{

	 public Factorofinv[] query(IQueryScheme queryScheme)
      throws BusinessException, Exception;
}