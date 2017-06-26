package nc.itf.uapeai.sys;

import nc.vo.pub.BusinessException;


/**
 * 
 * @author liyf_brave
 *
 */
public interface ISysDisPatcher {

	/**
	 * 
	 */
	public Object handleRequest(Object reqObje ,String pk_billtype,
			Object param)throws BusinessException;
	


}
