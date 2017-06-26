package nc.bs.uapeai.sys;

import nc.itf.uapeai.sys.IHKFilter;
import nc.vo.pub.BusinessException;

public abstract class AbstractFilter implements IHKFilter{
	

	public abstract Object filtdata(Object reqObje, Object param)throws BusinessException;

}
