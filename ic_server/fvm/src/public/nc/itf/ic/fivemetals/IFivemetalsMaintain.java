package nc.itf.ic.fivemetals;

import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.ic.fivemetals.FiveMetalsBVO;
import nc.vo.ic.fivemetals.FiveMetalsHVO;
import nc.vo.pub.BusinessException;

public interface IFivemetalsMaintain {

	/**
	 * ��������ѯ
	 * 
	 * @return
	 * @throws BusinessException
	 */
	AggFiveMetalsVO[] queryByCondition(String condition)
			throws BusinessException;

	/**
	 * ��PK��ѯ��
	 * 
	 * @return
	 * @throws BusinessException
	 */
	AggFiveMetalsVO queryByPk(String pk) throws BusinessException;

	/**
	 * ��������
	 * 
	 * @param vos
	 * @param isdel
	 * @throws BusinessException
	 */
	AggFiveMetalsVO operatebill(FiveMetalsHVO hvo, FiveMetalsBVO[] bvos, boolean isdel)
			throws BusinessException;

}
