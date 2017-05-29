package nc.bpm.pu.pfxx;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.jdbc.framework.SQLParameter;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m20.entity.PraybillItemVO;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * �ṩ��BPM��д�빺���ۼƲɹ���ͬ����
 * 
 * @author Administrator
 *
 */
public class M20ForBpmUpdate extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������
 		PraybillVO  bill = (PraybillVO) vo;
		//
		checkData(bill);
		
		//����
		doupdate(bill);
		
		return "�����ۼ�BPM��ͬ�����ɹ�";
	}

	private void doupdate(PraybillVO bill) throws DAOException {
		// TODO �Զ����ɵķ������
		BaseDAO dao = new  BaseDAO();
		//���ձ�����ѭ�����£�Rainbow��Ŀʵ�ʣ����嵥��
		String sql ="  update po_praybill_b set vbdef1 = decode(vbdef1, '~', 0, vbdef1) +��?�� where  pk_praybill_b=?";
		SQLParameter param = new SQLParameter();
		for(PraybillItemVO body:bill.getBVO()){
			param.addParam(body.getVbdef1()== null? UFDouble.ZERO_DBL:body.getVbdef1());
			param.addParam(body.getPk_praybill_b());
			dao.executeUpdate(sql,param);
			param.clearParams();
		}
	}

	private void checkData(PraybillVO bill) throws BusinessException {
		// TODO �Զ����ɵķ������
		if(bill == null || bill.getParentVO() == null)
			throw new BusinessException("δ��ȡ��ת���������");
		if(bill.getChildrenVO() == null || bill.getChildrenVO().length ==0){
			throw new BusinessException("���岻����Ϊ��");
		}
		for(PraybillItemVO body:bill.getBVO()){
			if(StringUtils.isEmpty(body.getPk_praybill_b())){
				throw new BusinessException("���� Pk_praybill_b������Ϊ��");
			}
			
		}

	}

}
