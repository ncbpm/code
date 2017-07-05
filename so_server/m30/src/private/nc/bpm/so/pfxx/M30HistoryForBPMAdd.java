package nc.bpm.so.pfxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.so.m30.closemanage.ISaleOrderCloseManageMaintain;
import nc.itf.so.m30.revise.IM30ReviseMaintain;
import nc.itf.uap.pf.IplatFormEntry;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderVO;
import nc.vo.so.m30.entity.SaleOrderViewVO;
import nc.vo.so.m30.pub.SaleOrderVOCalculator;
import nc.vo.so.m30.revise.entity.SaleOrderHistoryBVO;
import nc.vo.so.m30.revise.entity.SaleOrderHistoryHVO;
import nc.vo.so.m30.revise.entity.SaleOrderHistoryVO;

import org.apache.commons.lang.StringUtils;

public class M30HistoryForBPMAdd extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		SaleOrderHistoryVO bill = (SaleOrderHistoryVO) vo;
		// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(bill);
		// 具体业务处理
		String vrevisereason = bill.getParentVO().getVrevisereason();
		// 如果存在，则执行采购订单修订或者关闭打开
		String csaleorderid = bill.getParentVO().getCsaleorderid();

		SaleOrderVO ordervo = this.queryVOByPk(csaleorderid);
		// 判断是否整单关闭或者打开
		if ("关闭".equalsIgnoreCase(vrevisereason)) {
			NCLocator.getInstance().lookup(ISaleOrderCloseManageMaintain.class)
					.closeSaleOrder(new SaleOrderVO[] { ordervo });
			return "整单关闭成功";

		}
		if ("打开".equalsIgnoreCase(vrevisereason)) {
			NCLocator.getInstance().lookup(ISaleOrderCloseManageMaintain.class)
					.openSaleOrder(new SaleOrderVO[] { ordervo }, false);
			return "整单打开成功";
		}
		// 有行操作 --行关闭或者行打开,针对销售订单进行
		Map<String, SaleOrderBVO> map = new HashMap<String, SaleOrderBVO>();
		for (SaleOrderBVO body : ordervo.getChildrenVO()) {
			if (body.getVbdef20() != null) {
				map.put(body.getVbdef20(), body);
			}
		}
		// 关闭的行
		List<SaleOrderBVO> close_row = new ArrayList<SaleOrderBVO>();
		// 打开的行
		List<SaleOrderBVO> open_row = new ArrayList<SaleOrderBVO>();
		// 修改的行
		List<SaleOrderHistoryBVO> update_row = new ArrayList<SaleOrderHistoryBVO>();
		// 新增的行
		List<SaleOrderHistoryBVO> add_row = new ArrayList<SaleOrderHistoryBVO>();

		for (SaleOrderHistoryBVO body : bill.getChildrenVO()) {
			if ("关闭".equalsIgnoreCase(body.getVbrevisereason())) {
				if (map.containsKey(body.getVbdef20())) {
					close_row.add(map.get(body.getVbdef20()));
				} else {
					throw new BusinessException("根据BPM销售订单表体主键："
							+ body.getVbdef20() + " 在NC未匹配到对应的行.");
				}
			} else if ("打开".equalsIgnoreCase(body.getVbrevisereason())) {
				if (map.containsKey(body.getVbdef20())) {
					open_row.add(map.get(body.getVbdef20()));
				} else {
					throw new BusinessException("根据BPM销售订单表体主键："
							+ body.getVbdef20() + " 在NC未匹配到对应的行.");
				}
			} else if ("变更".equalsIgnoreCase(body.getVbrevisereason())) {
				if (map.containsKey(body.getVbdef20())) {
					update_row.add(body);
				} else {
					throw new BusinessException("根据BPM销售订单表体主键："
							+ body.getVbdef20() + " 在NC未匹配到对应的行.");
				}
			} else if ("新增".equalsIgnoreCase(body.getVbrevisereason())) {
				if (map.containsKey(body.getVbdef20())) {
					throw new BusinessException("新增的行，指定的BPM表体行ID "
							+ body.getVbdef20() + " 在NC当前销售订单已经存在，请检查.");
				} else {
					add_row.add(body);
				}
			} else {
				// throw new BusinessException("采购订单变更，未支持的变更行操作类:"
				// + body.getVbmemo());
			}
		}
		// 行关闭
		if (close_row.size() > 0) {
			SaleOrderViewVO[] new_bill = new SaleOrderViewVO[close_row.size()];
			for (int i = 0; i < close_row.size(); i++) {
				new_bill[i] = new SaleOrderViewVO();
				new_bill[i].setHead(ordervo.getParentVO());
				new_bill[i].setBody(close_row.get(i));
			}

			NCLocator.getInstance().lookup(ISaleOrderCloseManageMaintain.class)
					.closeSaleOrderRow(new_bill);
		}
		// 行打开
		if (open_row.size() > 0) {
			SaleOrderViewVO[] new_bill = new SaleOrderViewVO[open_row.size()];
			for (int i = 0; i < open_row.size(); i++) {
				new_bill[i] = new SaleOrderViewVO();
				new_bill[i].setHead(ordervo.getParentVO());
				new_bill[i].setBody(open_row.get(i));
			}
			NCLocator.getInstance().lookup(ISaleOrderCloseManageMaintain.class)
					.openSaleOrderRow(new_bill);
		}
		// 执行修订：无法判断表头字段是否有修改，因此，每次都执行一次修订
		M30HistroyDMO dmo = new M30HistroyDMO();
		SaleOrderHistoryVO[] historyVOs = dmo
				.getOrderHisVOBySaleOrder(new String[] { csaleorderid });
		//
		SaleOrderHistoryVO hisvo = historyVOs[0];

		// 更新表头
		updateHVO(hisvo, bill);
		if (update_row.size() > 0) {
			updateBody(hisvo, update_row);
		}

		if (add_row.size() > 0) {
			addBody(hisvo, add_row);
		}
		
		SaleOrderHistoryVO historyVO = saveAndApproveHistory(hisvo);
	
		
		return "销售订单修订完成:最新版本"+historyVO.getParentVO().getIversion();
	}
	
	
	private SaleOrderHistoryVO saveAndApproveHistory(SaleOrderHistoryVO oldbill) throws BusinessException {
		// TODO 自动生成的方法存根
		SaleOrderHistoryVO bill = new SaleOrderHistoryVO();
		bill.setParentVO(oldbill.getParentVO());
		bill.setChildrenVO(oldbill.getChildrenVO());
		
		
		
		//重现计算单价等》
		int rows[] = new int[bill.getChildrenVO().length];
		for (int i = 0; i <bill.getChildrenVO().length; i++) {
			rows[i] = i;
			bill.getChildrenVO()[i].setCorderhistorybid(null);
			bill.getChildrenVO()[i].setStatus(VOStatus.NEW);
		}
		//设置伪列
		bill.getParentVO().setAttributeValue("pseudocolumn", 0);
		
		//清空单价：根据金额和数量重算
		String[] attributeNames =bill.getChildrenVO()[0].getAttributeNames();
		//报价信息
		for(SaleOrderBVO bvo:bill.getChildrenVO()){
			for(String attname:attributeNames){
				if(attname.endsWith("price")){
					bvo.setAttributeValue(attname, null);
				}
			}
		}
		SaleOrderVOCalculator cal = new SaleOrderVOCalculator(bill);
		cal.calculate(rows, "norigtaxmny");	
		
		IM30ReviseMaintain maintainsrv = NCLocator.getInstance().lookup(
				IM30ReviseMaintain.class);
		// ReviseSaveSaleOrderAction action = new ReviseSaveSaleOrderAction();
		// 调用新的方法，传入的bills 是销售订单修订历史vo
		SaleOrderHistoryVO[] ret = maintainsrv.reviseOrderHisVOSave(new SaleOrderHistoryVO[]{bill});
		//审批
		approve(ret[0]);
		return  ret[0];
	}


	protected AggregatedValueObject approve(AggregatedValueObject billvo)
			throws BusinessException {

		IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator
				.getInstance().lookup(IplatFormEntry.class.getName());
		Object retObj = iIplatFormEntry.processAction("APPROVE", "30R", null,
				billvo, null, null);
		return null;
	}


	private void addBody(SaleOrderHistoryVO bill,
			List<SaleOrderHistoryBVO> add_row) {
		// TODO 自动生成的方法存根
		List<SaleOrderHistoryBVO> asList = new ArrayList<SaleOrderHistoryBVO>();
		for (SaleOrderHistoryBVO bvo : bill.getChildrenVO()) {
			asList.add(bvo);
		}
		for (SaleOrderHistoryBVO bvo : add_row) {
			bvo.setStatus(VOStatus.NEW);
			asList.add(bvo);
		}
		
		bill.setChildrenVO(asList.toArray(new SaleOrderHistoryBVO[0]));
	

	}

	private void updateBody(SaleOrderHistoryVO bill,
			List<SaleOrderHistoryBVO> update_row) throws BusinessException {
		Map<String, SaleOrderHistoryBVO> map = new HashMap<String, SaleOrderHistoryBVO>();
		for (SaleOrderHistoryBVO body : bill.getChildrenVO()) {
			if (body.getVbdef20() != null) {
				map.put(body.getVbdef20(), body);
			}
		}
		String[] attributeNames = bill.getChildrenVO()[0].getAttributeNames();
		for (SaleOrderHistoryBVO bpm : update_row) {
			SaleOrderHistoryBVO orderItemVO = map.get(bpm.getVbdef20());
			// 校验修订后的数量，是否小于累计发货主数量
			UFDouble bpm_nnum = bpm.getNnum() == null ? UFDouble.ZERO_DBL : bpm
					.getNnum();
			UFDouble ntotalsendnum = orderItemVO.getNtotalsendnum() == null ? UFDouble.ZERO_DBL
					: orderItemVO.getNtotalsendnum();
			if (bpm_nnum.sub(ntotalsendnum).doubleValue() < 0) {
				throw new BusinessException("操作不合法 :行" + bpm.getVbdef20()
						+ " 本次修订后的数量:" + bpm_nnum + ".不能小于已累计发货主数量:"
						+ ntotalsendnum);
			}
			// 5. 单价和金额修改时，如果没有形成销售发票，则允许直接修改单价和金额，同时调用NC订单更新接口进行订单变更。
			// 如果全部已经生成发票，则提示进行NC发票删除后进行单价和金额的调整

			// 如果部分已经生成发票，则允许修改当前行数量为已经执行发票数量，同时增加第二行，将剩余数量和金额进行行增加；
			UFDouble ntotalinvoicenum = orderItemVO.getNtotalinvoicenum() == null ? UFDouble.ZERO_DBL
					: orderItemVO.getNtotalinvoicenum();
			if (ntotalinvoicenum.doubleValue() > 0) {
				// BPM同步后，单价会重新进行计算，因此只控制金额，金额如果不相等，则
				UFDouble bpm_norigtaxmny = bpm.getNorigtaxmny() == null ? UFDouble.ZERO_DBL
						: bpm.getNorigtaxmny();
				UFDouble norigtaxmny = orderItemVO.getNorigtaxmny() == null ? UFDouble.ZERO_DBL
						: orderItemVO.getNorigtaxmny();
				if (bpm_norigtaxmny.sub(norigtaxmny).doubleValue() != 0) {
					throw new BusinessException("操作不合法 :行" + bpm.getVbdef20()
							+ "已经开发票，不允许修订金额,请删除发票后重新修订。 本次修订后的金额:"
							+ bpm_norigtaxmny + ",修订前金额：" + bpm_norigtaxmny);
				}

			}

			if (bpm_nnum.sub(ntotalinvoicenum).doubleValue() < 0) {
				throw new BusinessException("操作不合法 :行" + bpm.getVbdef20()
						+ " 本次修订后的数量:" + bpm_nnum + ".不能小于累计开票主数量 :"
						+ ntotalsendnum);
			}

			for (String attr : attributeNames) {
				if("ts".equalsIgnoreCase(attr)){
					continue;
				}
				if("csaleorderid".equalsIgnoreCase(attr)){
					continue;
				}
				if("csaleorderbid".equalsIgnoreCase(attr)){
					continue;
				}
				if("ctaxcodeid".equalsIgnoreCase(attr)){
					continue;
				}
				if(attr.endsWith("nnum") && ! attr.equalsIgnoreCase("nnum")){
					continue;
				}
				orderItemVO
						.setAttributeValue(attr, bpm.getAttributeValue(attr));
			}
	
		}

	}

	private void updateHVO(SaleOrderHistoryVO hisvo, SaleOrderHistoryVO bill) {
		// TODO 自动生成的方法存根
		String[] attributeNames = hisvo.getParentVO().getAttributeNames();
		SaleOrderHistoryHVO hvo = hisvo.getParentVO();
		Integer nversion = hvo.getIversion();
		SaleOrderHistoryHVO hvo_bpm = bill.getParentVO();
		for (String attr : attributeNames) {
			if("ts".equalsIgnoreCase(attr)){
				continue;
			}
			if("iversion".equalsIgnoreCase(attr)){
				continue;
			}
			hvo.setAttributeValue(attr, hvo_bpm.getAttributeValue(attr));
		}
//		hvo.setIversion(++nversion);
	}

	private SaleOrderVO queryVOByPk(String csaleorderid)
			throws BusinessException {
		// TODO 自动生成的方法存根
		SaleOrderVO[] vos = new BillQuery<>(SaleOrderVO.class)
				.query(new String[] { csaleorderid });
		if (vos == null || vos.length == 0) {
			throw new BusinessException("根据主键:" + csaleorderid + "未查询到对应的销售订单.");
		}
		return vos[0];
	}

	private void checkData(SaleOrderHistoryVO resvo) throws BusinessException {
		// TODO 自动生成的方法存根
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		if (StringUtils.isEmpty(resvo.getParentVO().getCsaleorderid())) {
			throw new BusinessException("需要自定字段Csaleorderid对应的销售订单主键.");

		}
	}

}
