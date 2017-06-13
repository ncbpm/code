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
 * 提供给BPM回写请购单累计采购合同数量
 * 
 * @author Administrator
 *
 */
public class M20ForBpmUpdate extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
 		PraybillVO  bill = (PraybillVO) vo;
		//
		checkData(bill);
		
		//更新
		doupdate(bill);
		
		return "更新累计BPM合同数量成功";
	}

	private void doupdate(PraybillVO bill) throws DAOException {
		// TODO 自动生成的方法存根
		BaseDAO dao = new  BaseDAO();
		//按照表体行循环更新：Rainbow项目实际，表体单行
		String sql ="  update po_praybill_b set vbdef1 = decode(vbdef1, '~', 0, vbdef1) +（?） where  pk_praybill_b=?";
		SQLParameter param = new SQLParameter();
		for(PraybillItemVO body:bill.getBVO()){
			param.addParam(body.getVbdef1()== null? UFDouble.ZERO_DBL:body.getVbdef1());
			param.addParam(body.getPk_praybill_b());
			dao.executeUpdate(sql,param);
			param.clearParams();
		}
	}

	private void checkData(PraybillVO bill) throws BusinessException {
		// TODO 自动生成的方法存根
		if(bill == null || bill.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		if(bill.getChildrenVO() == null || bill.getChildrenVO().length ==0){
			throw new BusinessException("表体不允许为空");
		}
		for(PraybillItemVO body:bill.getBVO()){
			if(StringUtils.isEmpty(body.getPk_praybill_b())){
				throw new BusinessException("表体 Pk_praybill_b不允许为空");
			}
			
		}

	}

}
