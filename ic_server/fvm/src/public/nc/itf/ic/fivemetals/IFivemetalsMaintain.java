package nc.itf.ic.fivemetals;

import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.ic.fivemetals.FiveMetalsBVO;
import nc.vo.ic.fivemetals.FiveMetalsHVO;
import nc.vo.pub.BusinessException;

public interface IFivemetalsMaintain {

	/**
	 * 按条件查询
	 * 
	 * @return
	 * @throws BusinessException
	 */
	AggFiveMetalsVO[] queryByCondition(String condition)
			throws BusinessException;

	/**
	 * 按PK查询
	 * 
	 * @return
	 * @throws BusinessException
	 */
	AggFiveMetalsVO queryByPk(String pk) throws BusinessException;

	/**
	 * 导入数据
	 * 
	 * @param vos
	 * @param isdel
	 * @throws BusinessException
	 */
	AggFiveMetalsVO operatebill(FiveMetalsHVO hvo, FiveMetalsBVO[] bvos, boolean isdel)
			throws BusinessException;

}
