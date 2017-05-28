package nc.bpm.pu.pfxx;

import nc.bs.pu.m21.maintain.OrderSaveBP;
import nc.bs.pu.m21.maintain.rule.SupplierFrozeChkRule;
import nc.bs.pu.m21.plugin.OrderPluginPoint;
import nc.impl.pu.m21.action.OrderDeleteAction;
import nc.impl.pu.m21.action.rule.approve.ApproveAfterEventRule;
import nc.impl.pu.m21.action.rule.approve.ApproveBeforeEventRule;
import nc.impl.pu.m21.action.rule.approve.ApproveBudgetCtrlRule;
import nc.impl.pu.m21.action.rule.approve.ApproveSupplyRule;
import nc.impl.pu.m21.action.rule.approve.ApproveVOValidateRule;
import nc.impl.pu.m21.action.rule.approve.FillNcaninnumRule;
import nc.impl.pu.m21.action.rule.approve.FilterOrderByStatusRule;
import nc.impl.pu.m21.action.rule.approve.InsertPayPlanRule;
import nc.impl.pu.m21.action.rule.approve.InsertStatusOnWayRule;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.vo.pu.m21.context.OrderContext;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.pfxx.plugins.AbstractPuPfxxPlugin;
import nc.vo.pu.pub.enumeration.POEnumBillStatus;
import nc.vo.pu.pub.enumeration.PuBusiLogActionCode;
import nc.vo.pu.pub.enumeration.PuBusiLogPathCode;
import nc.vo.pu.pub.rule.busilog.WriteOperateLogRule;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmf.pub.util.BatchNCBaseTypeUtils;

import org.apache.commons.lang.ArrayUtils;


/**
 * 
 * @author zhw
 *
 */

public class M21ForBPMAdd extends AbstractPuPfxxPlugin {


	private AggregatedValueObject save(AggregatedValueObject updatevo,
			AggregatedValueObject origVO) {
		OrderVO[] returnVOs = new OrderSaveBP(new OrderContext()).save(
				new OrderVO[] { (OrderVO) updatevo },
				new OrderVO[] { (OrderVO) origVO });
		return returnVOs[0];
	}

	/**
	 * 检验vo是否可导入
	 * 
	 * @param vo
	 */
	@Override
	protected void checkCanInster(AggregatedValueObject vo) {
		OrderVO order = (OrderVO) vo;
		int status = order.getHVO().getForderstatus();
		if (POEnumBillStatus.APPROVE.toInt() == status) {
			String approver = order.getHVO().getApprover();
			if (approver == null || approver.isEmpty()) {
				ExceptionUtils.wrappBusinessException("导入失败：审批态单据不能没有审批人！");
			}
			UFDate ufDate = order.getHVO().getTaudittime();
			if (ufDate == null) {
				ExceptionUtils.wrappBusinessException("导入失败：审批态单据审批日期不能为空！");
			}
			this.checkMny(vo);
			// this.checkRwite(vo);
		} else {
			super.checkCanInster(vo);
			this.checkMny(vo);
			this.checkRwite(vo);
		}
	}

	@Override
	protected void deleteVO(AggregatedValueObject vo) {
		new OrderDeleteAction().delete(new OrderVO[] { (OrderVO) vo }, null);
	}

	@Override
	protected String getBillStatusKey() {
		return OrderHeaderVO.FORDERSTATUS;
	}

	@Override
	protected String getChildrenPkFiled() {
		return OrderItemVO.PK_ORDER_B;
	}

	@Override
	protected String getParentPkFiled() {
		return OrderHeaderVO.PK_ORDER;
	}

	@Override
	protected AggregatedValueObject insert(AggregatedValueObject vo) {
		if (vo != null) {
			this.checkCanInster(vo);
		}
		OrderVO order = (OrderVO) vo;
		int status = order.getHVO().getForderstatus();
		if (POEnumBillStatus.APPROVE.toInt() == status) {
			OrderHeaderVO hvo = order.getHVO();
			String approver = hvo.getApprover();
			hvo.setApprover(null);
			hvo.setForderstatus(POEnumBillStatus.FREE.toInt());
			UFDate taudittime = hvo.getTaudittime();
			hvo.setTaudittime(null);
			AggregatedValueObject save = this.save(vo, null);
			OrderVO orderVO = this.approve(save, approver, taudittime);
			return orderVO;
		}
		return this.save(vo, null);
	}

	private OrderVO approve(AggregatedValueObject vo, String approver,
			UFDate taudittime) {
		AroundProcesser<OrderVO> processer = new AroundProcesser<OrderVO>(
				OrderPluginPoint.APPROVE);
		OrderVO order = (OrderVO) vo;
		this.addBeforeRule(processer);
		this.addAfterRule(processer);
		// 前规则
		processer.before(new OrderVO[] { order });
		OrderHeaderVO hvo = order.getHVO();
		hvo.setApprover(approver);
		hvo.setForderstatus(POEnumBillStatus.APPROVE.toInt());
		hvo.setTaudittime(taudittime);
		processer.after(new OrderVO[] { order });
		return order;
	}

	private void addBeforeRule(AroundProcesser<OrderVO> processer) {
		// 审核VO状态检查
		processer.addBeforeRule(new ApproveVOValidateRule());
		// 供应商冻结检查
		processer.addBeforeRule(new SupplierFrozeChkRule());
		// 审批前事件
		processer.addBeforeRule(new ApproveBeforeEventRule());
	}

	private void addAfterRule(AroundProcesser<OrderVO> processer) {
		// 写业务日志要放到FilterOrderByStatusRule的前面，否则vo有可能会被过滤掉，就没法记录日志了
		processer.addAfterRule(new WriteOperateLogRule<OrderVO>(
				PuBusiLogPathCode.orderApprovePath.getCode(),
				PuBusiLogActionCode.approve.getCode()));

		processer.addAfterRule(new FilterOrderByStatusRule(POEnumBillStatus.APPROVE
				.toInt()));
		processer.addAfterRule(new ApproveSupplyRule());
		// 采购计划检查
		processer.addAfterRule(new ApproveBudgetCtrlRule());
		// 往在途状态表中插入数据
		processer.addAfterRule(new InsertStatusOnWayRule());
		processer.addAfterRule(new InsertPayPlanRule());
		// 设置可入库数量，为审核后直接推入库单提供支持
		processer.addAfterRule(new FillNcaninnumRule());
		// 审批后事件
		processer.addAfterRule(new ApproveAfterEventRule());
	}

	@Override
	protected AggregatedValueObject queryVOByPk(String voPk) {
		BillQuery<OrderVO> billquery = new BillQuery<OrderVO>(OrderVO.class);
		OrderVO[] vos = billquery.query(new String[] { voPk });

		if (ArrayUtils.isEmpty(vos)) {
			return null;
		}
		return vos[0];
	}

	@Override
	protected AggregatedValueObject update(AggregatedValueObject updatevo,
			AggregatedValueObject origVO) {
		return this.save(updatevo, origVO);
	}

	private void checkMny(AggregatedValueObject vo) {
		StringBuilder errrows = new StringBuilder();
		OrderItemVO[] bodyvos = ((OrderVO) vo).getBVO();
		for (OrderItemVO orderItemVO : bodyvos) {
			Integer fbuysellflag = orderItemVO.getFbuysellflag();
			// 无税金额
			UFDouble nmny = orderItemVO.getNmny();
			// 价税合计
			UFDouble ntaxmny = orderItemVO.getNtaxmny();
			// 税额
			UFDouble ntax = orderItemVO.getNtax();
			// 进口采购
			if (BuySellFlagEnum.IMPORT.value().equals(fbuysellflag)) {
				if (!nmny.equals(ntaxmny)) {
					errrows.append(orderItemVO.getCrowno() + ",");
				}
			}
			// 国内采购
			else if (BuySellFlagEnum.NATIONAL_BUY.value().equals(fbuysellflag)) {
				if (!nmny.add(ntax).equals(ntaxmny)) {
					errrows.append(orderItemVO.getCrowno() + ",");
				}
			} else {
				ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("4004000_0", "04004000-0139", null,
								new String[] { orderItemVO.getCrowno() })/*
																													 * @res
																													 * "行:{0}采购订单表体购销类型设置不正确！"
																													 */);
			}
		}
		if (errrows.length() <= 0) {
			return;
		}
		errrows.deleteCharAt(errrows.length() - 1);
		ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
				.getNCLangRes().getStrByID("4004000_0", "04004000-0137", null,
						new String[] { errrows.toString() })/*
																								 * @res
																								 * "行{0}无税金额、税额、价税合计计算关系错误不可导入！"
																								 */);
	}

	/**
	 * 存在下游回写信息的单据控制不可导入
	 * 
	 * @param vo
	 */
	private void checkRwite(AggregatedValueObject vo) {
		OrderItemVO[] bodyvos = ((OrderVO) vo).getBVO();
		for (OrderItemVO orderItemVO : bodyvos) {
			StringBuilder errstr = new StringBuilder();
			// 累计已核销本币开票金额
			UFDouble nacccancelinvmny = orderItemVO.getNacccancelinvmny();
			if (!BatchNCBaseTypeUtils.isNullOrZero(nacccancelinvmny)) {
				errstr.append(OrderItemVO.NACCCANCELINVMNY + ",");
			}
			// 累计本币开票金额
			UFDouble naccuminvoicemny = orderItemVO.getNaccuminvoicemny();
			if (!BatchNCBaseTypeUtils.isNullOrZero(naccuminvoicemny)) {
				errstr.append(OrderItemVO.NACCUMINVOICEMNY + ",");
			}
			// 费用累计开票金额
			UFDouble nfeemny = orderItemVO.getNfeemny();
			if (!BatchNCBaseTypeUtils.isNullOrZero(nfeemny)) {
				errstr.append(OrderItemVO.NFEEMNY + ",");
			}

			// 累计到货主数量
			UFDouble naccumarrvnum = orderItemVO.getNaccumarrvnum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(naccumarrvnum)) {
				errstr.append(OrderItemVO.NACCUMARRVNUM + ",");
			}
			// 累计入库主数量
			UFDouble naccumstorenum = orderItemVO.getNaccumstorenum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(naccumstorenum)) {
				errstr.append(OrderItemVO.NACCUMSTORENUM + ",");
			}
			// 累计开票主数量
			UFDouble naccuminvoicenum = orderItemVO.getNaccuminvoicenum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(naccuminvoicenum)) {
				errstr.append(OrderItemVO.NACCUMINVOICENUM + ",");
			}
			// 累计途耗主数量
			UFDouble naccumwastnum = orderItemVO.getNaccumwastnum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(naccumwastnum)) {
				errstr.append(OrderItemVO.NACCUMWASTNUM + ",");
			}
			// 累计到货计划主数量
			UFDouble naccumrpnum = orderItemVO.getNaccumrpnum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(naccumrpnum)) {
				errstr.append(OrderItemVO.NACCUMRPNUM + ",");
			}
			// 累计运输主数量
			UFDouble naccumdevnum = orderItemVO.getNaccumdevnum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(naccumdevnum)) {
				errstr.append(OrderItemVO.NACCUMDEVNUM + ",");
			}

			// 累计退货主数量
			UFDouble nbackarrvnum = orderItemVO.getNbackarrvnum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(nbackarrvnum)) {
				errstr.append(OrderItemVO.NBACKARRVNUM + ",");
			}
			// 累计退库主数量
			UFDouble nbackstorenum = orderItemVO.getNbackstorenum();
			if (!BatchNCBaseTypeUtils.isNullOrZero(nbackstorenum)) {
				errstr.append(OrderItemVO.NBACKSTORENUM + ",");
			}

			if (errstr.length() <= 0) {
				return;
			}
			errstr.deleteCharAt(errstr.length() - 1);
			ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4004000_0", "04004000-0138", null,
							new String[] { orderItemVO.getCrowno(), errstr.toString() })/*
																																					 * @res
																																					 * "行:{0}存在回写数据[{1}],不可导入！"
																																					 */);
		}
	}
}
