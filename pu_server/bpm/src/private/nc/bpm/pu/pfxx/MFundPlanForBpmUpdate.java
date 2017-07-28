package nc.bpm.pu.pfxx;

import nc.bs.dao.BaseDAO;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.jdbc.framework.SQLParameter;
import nc.vo.ic.pub.check.VOCheckUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.m21.entity.PayPlanVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;

/**
 * 资金计划回写接口
 * 
 * @author Administrator
 *
 */
public class MFundPlanForBpmUpdate extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// 1.得到转换后的VO数据,取决于向导第一步注册的VO信息
		AggregatedValueObject resvo = (AggregatedValueObject) vo;
		OrderVO bpmOrder = (OrderVO) resvo;
		// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(resvo);
		// 3.补全数据,并且调整单据状态
		fillData(resvo);
		//4. 回写更新
		updateNCVO(resvo);
		return "更新成功";


	}
	/**
	 * 更新或者新增采购付款计划明细数据
	 * @param resvo
	 * @throws BusinessException 
	 */
	private void updateNCVO(AggregatedValueObject resvo) throws BusinessException {
		// TODO 自动生成的方法存根
		OrderVO bpmOrder = (OrderVO) resvo;
		String vdef1 = bpmOrder.getHVO().getVdef1();
		BaseDAO dao = new  BaseDAO();
		PayPlanVO[] vos = (PayPlanVO[]) bpmOrder.getChildren(PayPlanVO.class);
		if("采购付款计划".equalsIgnoreCase(vdef1)){
			//按照表体行循环更新：Rainbow项目实际，表体单行
			String sql ="  update po_order_payplan set def30 = decode(def30,null,0, '~', 0, def30) +（?） where   pk_order_payplan =?";
			SQLParameter param = new SQLParameter();
			for(PayPlanVO body:vos){
				param.addParam(body.getNorigmny()== null? UFDouble.ZERO_DBL:body.getNorigmny());
				param.addParam(body.getPk_order_payplan());
				int rs = dao.executeUpdate(sql,param);
				if(rs <=0){
					throw new BusinessException("根据主键未查询到采购付款计划. select * from po_order_payplan where pk_praybill_b='"+body.getPk_order_payplan()+"'");
				}
				param.clearParams();
			}
		}else{
			//应付单
			//按照表体行循环更新：	
			String sql ="  update ap_payableitem set def30 =  decode(def30,null,0, '~', 0, def30) +（?） where  pk_payableitem =?";
			SQLParameter param = new SQLParameter();
			for(PayPlanVO body:vos){
				param.addParam(body.getNorigmny()== null? UFDouble.ZERO_DBL:body.getNorigmny());
				param.addParam(body.getPk_order_payplan());
				int rs = dao.executeUpdate(sql,param);
				if(rs <=0){
					throw new BusinessException("根据主键未查询到应付单明细. select * from ap_payableitem where pk_payableitem='"+body.getPk_order_payplan()+"'");
				}
				param.clearParams();
			}
		
			
		}
		
	}

	/**
	 * 
	 * @param resvo
	 */
	private void fillData(AggregatedValueObject resvo) {
		// TODO 自动生成的方法存根
		
	}

	/**
	 * 
	 * @param 数据校验
	 * @throws ValidationException 
	 */
	private void checkData(AggregatedValueObject resvo) throws ValidationException {
		// TODO 自动生成的方法存根
		OrderVO bpmOrder = (OrderVO) resvo;
		PayPlanVO[] vos = (PayPlanVO[]) bpmOrder.getChildren(PayPlanVO.class);
		
		VOCheckUtil.checkHeadNotNullFields(bpmOrder, new String[] { "vdef1"});
		
		VOCheckUtil.checkBodyNotNullFields(bpmOrder, new String[] { "pk_order",
				"pk_order_payplan", "norigmny"});
	}
}
