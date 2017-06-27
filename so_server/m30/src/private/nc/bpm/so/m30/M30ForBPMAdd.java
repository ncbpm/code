package nc.bpm.so.m30;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.so.m30.action.main.InsertSaleOrderAction;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.pf.IplatFormEntry;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderHVO;
import nc.vo.so.m30.entity.SaleOrderVO;
import nc.vo.so.m30.pub.SaleOrderVOCalculator;

/**
 * BPM销售订单导入
 * @author liyf
 *
 */
public class M30ForBPMAdd extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根

		// 1.得到转换后的VO数据,取决于向导第一步注册的VO信息
		AggregatedValueObject resvo = (AggregatedValueObject) vo;
		// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(resvo);
		//3.补全数据,并且调整单据状态
		fillData(resvo);
		
		//记录BPM传来的审批人信息
		SaleOrderVO bill = (SaleOrderVO)resvo;
		SaleOrderHVO parentVO = bill.getParentVO();
		String approver = parentVO.getApprover();
		
		parentVO.setApprover(null);
		
		SaleOrderVO bill2 = (SaleOrderVO) insert(resvo);
		//重新查询，防止并发
		
		bill2 = query(bill2.getParentVO().getPrimaryKey());
		bill2.getParentVO().setApprover(approver);
		approve(bill2);
		
		return bill2.getParentVO().getPrimaryKey();
	}
	
	private SaleOrderVO query(String hid){
		return new BillQuery<>(SaleOrderVO.class).query(new String[]{hid})[0];
	}

	private void fillData(AggregatedValueObject resvo) {
		// TODO 自动生成的方法存根
		//补全数量信息：BPM传递主数量，补全辅数量，计价数量
		SaleOrderVO bill = (SaleOrderVO)resvo;
		SaleOrderHVO parentVO = bill.getParentVO();
		SaleOrderBVO[] bvos = bill.getChildrenVO();
		//审批流状态
		parentVO.setFpfstatusflag(-1);
		//单据状态，自由
		parentVO.setFstatusflag(1);
		
		//
		if (StringUtil.isSEmptyOrNull(parentVO.getCtrantypeid())) {
			// uap不支持单据类型的翻译，暂时以交易类型code查询id的方式补交易类型
			String vtrantypecode = parentVO.getVtrantypecode();
			Map<String, String> map = PfServiceScmUtil
					.getTrantypeidByCode(new String[] { vtrantypecode });
			parentVO.setCtrantypeid(map == null ? null : map.get(vtrantypecode));
		}
		//报价信息
		for(SaleOrderBVO bvo:bvos){
			bvo.setCqtunitid(bvo.getCunitid());
			bvo.setVqtunitrate(bvo.getVqtunitrate());
		}
		
		///计算价格信息
		int rows[] = new int[bvos.length];
		for(int i=0;i<bvos.length;i++){
			rows[i] = i;
		}
		SaleOrderVOCalculator cal = new SaleOrderVOCalculator(bill);
		cal.calculate(rows, "norigtaxmny");
	}

	protected AggregatedValueObject insert(AggregatedValueObject billvo) {

		SaleOrderVO[] insertvo = new SaleOrderVO[] { (SaleOrderVO) billvo };
		InsertSaleOrderAction insertact = new InsertSaleOrderAction();
		SaleOrderVO[] retvos = insertact.insert(insertvo);
		if (null == retvos || retvos.length == 0) {
			return null;
		}
		return retvos[0];
	}

	protected AggregatedValueObject approve(AggregatedValueObject billvo)
			throws BusinessException {

		IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator
				.getInstance().lookup(IplatFormEntry.class.getName());
		Object retObj = iIplatFormEntry.processAction("APPROVE", "30", null,
				billvo, null, null);
		return null;
	}

	private void checkData(AggregatedValueObject resvo) throws BusinessException {
		// TODO 自动生成的方法存根
		if(resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		if(resvo.getChildrenVO() == null || resvo.getChildrenVO().length ==0){
			throw new BusinessException("表体不允许为空");
		}
		SaleOrderHVO head = (SaleOrderHVO) resvo.getParentVO();
		VOQuery<SaleOrderHVO> query = new VOQuery<SaleOrderHVO>(SaleOrderHVO.class);
		SaleOrderHVO[] hvos = query.query(" and pk_org='"+head.getPk_org()+"' and vbillcode='"+head.getVbillcode()+"'", null);
		if(hvos!= null && hvos.length > 0){
			throw new BusinessException(" 已经存在相同单据号的销售订单");

		}
		
	}

}
