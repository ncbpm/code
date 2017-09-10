package nc.bpm.pu.pfxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.pu.m21.maintain.OrderSaveBP;
import nc.bs.pu.m21.maintain.rule.SupplierFrozeChkRule;
import nc.bs.pu.m21.plugin.OrderPluginPoint;
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
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.pu.m21.IOrderRevise;
import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.itf.scmpub.reference.uap.bd.vat.BuySellFlagEnum;
import nc.itf.uap.pf.IplatFormEntry;
import nc.pubitf.pu.m21.pub.IOrderClosePubService;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m21.context.OrderContext;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderPaymentVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.m21.rule.PaymentInfo;
import nc.vo.pu.m21.rule.RelationCalculate;
import nc.vo.pu.pub.enumeration.POEnumBillStatus;
import nc.vo.pu.pub.enumeration.PuBusiLogActionCode;
import nc.vo.pu.pub.enumeration.PuBusiLogPathCode;
import nc.vo.pu.pub.rule.busilog.WriteOperateLogRule;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderHVO;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 采购订单同步和变更
 * 
 * @author liyf
 * 
 */

public class M21ForBPMAdd extends AbstractPfxxPlugin {

	String[] bodyattributeNames = new String[] { "pk_group", "pk_org",
			"pk_org_v", "vbdef1", "vbmemo", "pk_reqstoorg", "pk_reqstoorg_v",
			"pk_arrvstoorg", "pk_arrvstoorg_v", "pk_flowstockorg",
			"pk_flowstockorg_v", "crowno", "pk_material", "pk_srcmaterial",
			"cunitid", "nnum", "castunitid", "nastnum", "vchangerate",
			"norigtaxmny", "norigtaxprice", "dplanarrvdate", "chandler",
			"fisactive", "breceiveplan", "blargess", "btransclosed",
			"pk_psfinanceorg", "pk_psfinanceorg_v", "pk_apfinanceorg",
			"pk_apfinanceorg_v", "bborrowpur", "nweight", "nvolumn",
			"bstockclose", "binvoiceclose", "barriveclose", "bpayclose",
			"ftaxtypeflag", "ntaxrate", "ccurrencyid", "nexchangerate",
			"dbilldate", "pk_supplier", "corigcurrencyid", "csendcountryid",
			"crececountryid", "ctaxcountryid", "fbuysellflag",
			"btriatradeflag", "ctaxcodeid", "nnosubtaxrate", "nnosubtax" };

	String[] headattributeNames = new String[] { "pk_order", "pk_group",
			"vmemo", "pk_org", "pk_org_v", "vbillcode", "dbilldate",
			"pk_supplier", "pk_dept_v", "pk_dept", "vtrantypecode",
			"pk_invcsupllier", "pk_payterm", "billmaker", "approver",
			"bisreplenish", "breturn", "iprintcount", "creationtime",
			"taudittime", "bcooptoso", "bsocooptome", "ntotalastnum",
			"ntotalorigmny", "bfrozen", "pk_busitype", "fhtaxtypeflag",
			"corigcurrencyid", "brefwhenreturn", "ntotalweight",
			"ntotalvolume", "ntotalpiece", "bfinalclose", "creator",
			"ctrantypeid", "bpublish" };

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// 1.得到转换后的VO数据,取决于向导第一步注册的VO信息
		AggregatedValueObject resvo = (AggregatedValueObject) vo;
		OrderVO bpmOrder = (OrderVO) resvo;
		String vopk = bpmOrder.getPrimaryKey();
		if (StringUtils.isEmpty(vopk)) {
			// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
			checkData(resvo);
			// 3.补全数据,并且调整单据状态
			fillData(resvo);
			//
			OrderHeaderVO hvo = bpmOrder.getHVO();
			String approver = hvo.getApprover();
			UFDate taudittime = hvo.getTaudittime();
			// 保存
			AggregatedValueObject savevo = insert(resvo);
			// 审批
			OrderVO orderVO = this.approve(savevo, approver, taudittime);
			return orderVO.getParentVO().getPrimaryKey();
		} else {
			// 如果存在，则执行采购订单修订或者关闭打开
			OrderVO queryVo = this.queryVOByPk(vopk);
			if (queryVo == null) {
				throw new BusinessException("根据主键：" + vopk + "  在NC未查询对应的采购订单.");
			}
			// 判断是否整单关闭或者打开
			if ("关闭".equalsIgnoreCase(bpmOrder.getHVO().getVmemo())) {
				NCLocator.getInstance().lookup(IOrderClosePubService.class)
						.finalClose(new OrderVO[] { queryVo });
				return vopk;

			}
			if ("打开".equalsIgnoreCase(bpmOrder.getHVO().getVmemo())) {
				NCLocator.getInstance().lookup(IOrderClosePubService.class)
						.finalOpen(new OrderVO[] { queryVo });
				return vopk;
			}
			// 处理变更订单明细
			delOrderItem(queryVo, bpmOrder);

			NCLocator.getInstance().lookup(IOrderRevise.class)
					.reviseSave(new OrderVO[] { queryVo }, null);

			return vopk;
		}

	}

	private void delOrderItem(OrderVO queryVo, OrderVO bpmOrder)
			throws BusinessException {
		// TODO 自动生成的方法存根
		String vopk = bpmOrder.getPrimaryKey();
		// 有行操作 --行关闭或者行打开，执行
		Map<String, OrderItemVO> map = new HashMap<String, OrderItemVO>();
		for (OrderItemVO body : queryVo.getBVO()) {
			if (body.getVbdef1() != null) {
				map.put(body.getVbdef1(), body);
			}
		}
		// 关闭的行
		List<OrderItemVO> close_row = new ArrayList<OrderItemVO>();
		// 打开的行
		List<OrderItemVO> open_row = new ArrayList<OrderItemVO>();
		// 修改的行
		List<OrderItemVO> update_row = new ArrayList<OrderItemVO>();
		// 删除的行
		List<OrderItemVO> del_row = new ArrayList<OrderItemVO>();
		// 新增的行
		List<OrderItemVO> add_row = new ArrayList<OrderItemVO>();

		for (OrderItemVO body : bpmOrder.getBVO()) {
			if ("关闭".equalsIgnoreCase(body.getVbmemo())) {
				if (map.containsKey(body.getVbdef1())) {
					close_row.add(map.get(body.getVbdef1()));
				} else {
					throw new BusinessException("根据BPM采购合同表体主键："
							+ body.getVbdef1() + " 在NC未匹配到对应的行.");
				}
			} else if ("打开".equalsIgnoreCase(body.getVbmemo())) {
				if (map.containsKey(body.getVbdef1())) {
					open_row.add(map.get(body.getVbdef1()));
				} else {
					throw new BusinessException("根据BPM采购合同表体主键："
							+ body.getVbdef1() + " 在NC未匹配到对应的行.");
				}
			} else if ("变更".equalsIgnoreCase(body.getVbmemo())) {
				if (map.containsKey(body.getVbdef1())) {
					update_row.add(body);
				} else {
					throw new BusinessException("根据BPM采购合同表体主键："
							+ body.getVbdef1() + " 在NC未匹配到对应的行.");
				}
			} else if ("删除".equalsIgnoreCase(body.getVbmemo())) {
				if (map.containsKey(body.getVbdef1())) {
					del_row.add(body);
				} else {
					throw new BusinessException("根据BPM采购合同表体主键："
							+ body.getVbdef1() + " 在NC未匹配到对应的行.");
				}
			} else if ("新增".equalsIgnoreCase(body.getVbmemo())) {
				if (map.containsKey(body.getVbdef1())) {
					throw new BusinessException("新增的行，指定的BPM表体行ID "
							+ body.getVbdef1() + " 在NC当前采购订单已经存在，请检查.");
				} else {
					add_row.add(body);
				}
			} else {
				// throw new BusinessException("采购订单变更，未支持的变更行操作类:"
				// + body.getVbmemo());
			}
		}
		boolean requry = false;
		if (close_row.size() > 0) {
			OrderVO bill = new OrderVO();
			bill.setHVO(queryVo.getHVO());
			bill.setChildrenVO(close_row.toArray(new OrderItemVO[0]));
			NCLocator.getInstance().lookup(IOrderClosePubService.class)
					.rowClose(new OrderVO[] { bill });
			requry = true;
		}

		if (open_row.size() > 0) {
			OrderVO bill = new OrderVO();
			bill.setHVO(queryVo.getHVO());
			bill.setChildrenVO(open_row.toArray(new OrderItemVO[0]));
			NCLocator.getInstance().lookup(IOrderClosePubService.class)
					.rowOpen(new OrderVO[] { bill });
			requry = true;
		}
		// 重新执行查询，防止操作并发
		if (requry) {
			queryVo = queryVOByPk(vopk);
		}
		// 更新表头
		updateHVO(queryVo, bpmOrder);
		if (update_row.size() > 0) {
			updateBody(queryVo, update_row);
		}
		if (del_row.size() > 0) {
			delBody(queryVo, update_row);
		}
		if (add_row.size() > 0) {
			addBody(queryVo, add_row);
		}
		calculateForModify(queryVo);
	}

	private void addBody(OrderVO queryVo, List<OrderItemVO> add_row) {
		// TODO 自动生成的方法存根
		OrderHeaderVO parentVO = queryVo.getHVO();

		// <vsourcetrantype>0001A510000000001SOR</vsourcetrantype>
		// <vsourcecode>QG2017051000000014</vsourcecode>
		// <vsourcerowno>10</vsourcerowno>
		OrderItemVO[] vos = add_row.toArray(new OrderItemVO[0]);
		if ("20".equalsIgnoreCase(add_row.get(0).getCsourcetypecode())) {
			String[] formulas = new String[] {
					"vsourcetrantype->getColValue(po_praybill,ctrantypeid ,pk_praybill ,csourceid)",
					"vsourcecode->getColValue(po_praybill,vbillcode ,pk_praybill ,csourceid)",
					"vsourcerowno->getColValue(po_praybill_b,crowno,  pk_praybill_b ,csourcebid)"

			};
			SuperVOUtil.execFormulaWithVOs(vos, formulas);

		} else if ("30".equalsIgnoreCase(add_row.get(0).getCsourcetypecode())) {
			String[] formulas = new String[] {
					"vsourcetrantype->getColValue(so_saleorder,ctrantypeid,vbdef20  ,csourcebid)",
					"vsourcecode->getColValue(so_saleorder,vbillcode ,vbdef20  ,csourcebid)",
					"vsourcerowno->getColValue(so_saleorder_b,crowno,  vbdef20  ,csourcebid)",
					"csourceid->getColValue(so_saleorder_b,csaleorderid ,  vbdef20  ,csourcebid)",
					"csourcebid->getColValue(so_saleorder_b, csaleorderbid ,  vbdef20  ,csourcebid)"

			};
			SuperVOUtil.execFormulaWithVOs(vos, formulas);
		}
		List<OrderItemVO> asList = new ArrayList<OrderItemVO>();
		for (OrderItemVO bvo : queryVo.getBVO()) {
			asList.add(bvo);
		}

		for (OrderItemVO bvo : vos) {
			bvo.setStatus(VOStatus.NEW);
			bvo.setPk_order(parentVO.getPk_order());
			// --表体根据表头设置
			// <dbilldate>2017-05-21 20:20:16</dbilldate>
			// <pk_supplier>1001A5100000000008J1</pk_supplier>
			// <corigcurrencyid>1002Z0100000000001K1</corigcurrencyid>
			bvo.setDbilldate(parentVO.getDbilldate());
			bvo.setPk_supplier(parentVO.getPk_supplier());
			bvo.setCorigcurrencyid(parentVO.getCorigcurrencyid());

			// 首先根据单位，计算报价单位
			bvo.setCqtunitid(bvo.getCunitid());
			bvo.setVqtunitrate(bvo.getVchangerate());
			// --表体默认值
			// <fisactive>0</fisactive>
			bvo.setFisactive(0);
			// <btriatradeflag>N</btriatradeflag>
			bvo.setBtriatradeflag(UFBoolean.FALSE);
			// <bborrowpur>N</bborrowpur>
			bvo.setBborrowpur(UFBoolean.FALSE);
			// <binvoiceclose>N</binvoiceclose>
			bvo.setBinvoiceclose(UFBoolean.FALSE);
			// <barriveclose>N</barriveclose>
			bvo.setBarriveclose(UFBoolean.FALSE);
			// <bpayclose>N</bpayclose>
			bvo.setBpayclose(UFBoolean.FALSE);
			// <breceiveplan>N</breceiveplan>
			bvo.setBreceiveplan(UFBoolean.FALSE);
			// <blargess>N</blargess>
			bvo.setBlargess(UFBoolean.FALSE);
			// <btransclosed>N</btransclosed>
			bvo.setBtransclosed(UFBoolean.FALSE);

			// 润丰同步的业务源只有请购和协同销售订单
			// <vfirsttrantype>0001A510000000001SOR</vfirsttrantype>
			bvo.setVfirsttrantype(bvo.getVsourcetrantype());
			// <cfirsttypecode>20</cfirsttypecode>
			bvo.setCfirsttypecode(bvo.getCsourcetypecode());
			// <cfirstid>1001A41000000000BOZQ</cfirstid>
			bvo.setCfirstid(bvo.getCsourceid());
			// <cfirstbid>1001A41000000000BOZR</cfirstbid>
			bvo.setCfirstbid(bvo.getCsourcebid());
			// <vfirstcode>QG2017051000000014</vfirstcode>
			bvo.setVfirstcode(bvo.getVsourcecode());
			// <vfirstrowno>10</vfirstrowno>
			bvo.setVfirstrowno(bvo.getVsourcerowno());
			asList.add(bvo);
		}
		queryVo.setBVO(asList.toArray(new OrderItemVO[0]));

		// 补全行号
		fillupRowNo(queryVo);
	}

	private void updateBody(OrderVO queryVo, List<OrderItemVO> update_row)
			throws BusinessException {
		// TODO 自动生成的方法存根
		Map<String, OrderItemVO> map = new HashMap<String, OrderItemVO>();
		for (OrderItemVO body : queryVo.getBVO()) {
			if (body.getVbdef1() != null) {
				map.put(body.getVbdef1(), body);
			}
		}
		for (OrderItemVO bpm : update_row) {
			OrderItemVO orderItemVO = map.get(bpm.getVbdef1());
			// 校验修订后的数量，是否小于累计到货数量
			UFDouble bpm_nnum = bpm.getNnum() == null ? UFDouble.ZERO_DBL : bpm
					.getNnum();
			UFDouble bpm_naccumarrvnum = orderItemVO.getNaccumarrvnum() == null ? UFDouble.ZERO_DBL
					: orderItemVO.getNaccumarrvnum();
			if (bpm_nnum.sub(bpm_naccumarrvnum).doubleValue() < 0) {
				throw new BusinessException("操作不合法 :行" + bpm.getVbdef1()
						+ " 本次修订后的数量:" + bpm_nnum + ".不能小于已累计到货:"
						+ bpm_naccumarrvnum);
			}
			for (String attr : bodyattributeNames) {

				orderItemVO
						.setAttributeValue(attr, bpm.getAttributeValue(attr));
			}
			orderItemVO.setStatus(VOStatus.UPDATED);
		}
	}

	private void delBody(OrderVO queryVo, List<OrderItemVO> update_row) {
		// TODO 自动生成的方法存根
		Map<String, OrderItemVO> map = new HashMap<String, OrderItemVO>();
		for (OrderItemVO body : queryVo.getBVO()) {
			if (body.getVbdef1() != null) {
				map.put(body.getVbdef1(), body);
			}
		}
		for (OrderItemVO bpm : update_row) {
			OrderItemVO orderItemVO = map.get(bpm.getVbdef1());
			orderItemVO.setStatus(VOStatus.DELETED);
		}
	}

	private void updateHVO(OrderVO queryVo, OrderVO order) {
		// TODO 自动生成的方法存根
		OrderHeaderVO hvo = queryVo.getHVO();
		Integer nversion = hvo.getNversion();
		OrderHeaderVO hvo_bpm = order.getHVO();
		for (String attr : headattributeNames) {
			hvo.setAttributeValue(attr, hvo_bpm.getAttributeValue(attr));
		}

		hvo.setNversion(++nversion);
		hvo.setBislatest(UFBoolean.TRUE);
	}

	protected OrderVO queryVOByPk(String voPk) {
		BillQuery<OrderVO> billquery = new BillQuery<OrderVO>(OrderVO.class);
		OrderVO[] vos = billquery.query(new String[] { voPk });

		if (ArrayUtils.isEmpty(vos)) {
			return null;
		}
		return vos[0];
	}

	protected AggregatedValueObject insert(AggregatedValueObject vo)
			throws BusinessException {
		OrderVO order = (OrderVO) vo;
		OrderHeaderVO hvo = order.getHVO();
		hvo.setApprover(null);
		hvo.setForderstatus(POEnumBillStatus.FREE.toInt());
		hvo.setTaudittime(null);
		AggregatedValueObject save = this.save(vo, null);
		return save;
	}

	private AggregatedValueObject save(AggregatedValueObject updatevo,
			AggregatedValueObject origVO) {
		OrderVO[] returnVOs = new OrderSaveBP(new OrderContext()).save(
				new OrderVO[] { (OrderVO) updatevo },
				new OrderVO[] { (OrderVO) origVO });
		return returnVOs[0];
	}

	private OrderVO approve(AggregatedValueObject vo, String approver,
			UFDate taudittime) throws BusinessException {
		AroundProcesser<OrderVO> processer = new AroundProcesser<OrderVO>(
				OrderPluginPoint.APPROVE);
		OrderVO order = (OrderVO) vo;
		OrderVO[] vos = new OrderVO[] { order };
		this.addBeforeRule(processer);
		this.addAfterRule(processer);
		// 前规则
		processer.before(vos);

		OrderHeaderVO hvo = order.getHVO();
		hvo.setApprover(approver);
		hvo.setTaudittime(taudittime);
		InvocationInfoProxy.getInstance().setUserId(approver);
		IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator
				.getInstance().lookup(IplatFormEntry.class.getName());
		Object retObj = iIplatFormEntry.processAction("APPROVE", "21", null,
				order, null, null);

		// approveImpl.approve(vos, null);
		processer.after(vos);
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

		processer.addAfterRule(new FilterOrderByStatusRule(
				POEnumBillStatus.APPROVE.toInt()));
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

	private void fillData(AggregatedValueObject resvo) throws BusinessException {
		// TODO 自动生成的方法存根
		// 补全数量信息：BPM传递主数量，补全辅数量，计价数量
		OrderVO bill = (OrderVO) resvo;
		OrderHeaderVO parentVO = bill.getHVO();
		parentVO.setStatus(VOStatus.NEW);
		OrderItemVO[] bvos = bill.getBVO();
		// 单据状态，自由
		parentVO.setForderstatus(POEnumBillStatus.FREE.toInteger());

		// 表头默认值
		if (parentVO.getAttributeValue(OrderHeaderVO.IPRINTCOUNT) == null) {
			parentVO.setAttributeValue(OrderHeaderVO.IPRINTCOUNT, 0);
		}
		if (parentVO.getAttributeValue(OrderHeaderVO.BREFWHENRETURN) == null) {
			parentVO.setAttributeValue(OrderHeaderVO.BREFWHENRETURN,
					UFBoolean.TRUE);
		}
		if (parentVO.getAttributeValue(OrderHeaderVO.BFROZEN) == null) {
			parentVO.setAttributeValue(OrderHeaderVO.BFROZEN, UFBoolean.FALSE);
		}
		if (parentVO.getAttributeValue(OrderHeaderVO.BISREPLENISH) == null) {
			parentVO.setAttributeValue(OrderHeaderVO.BISREPLENISH,
					UFBoolean.FALSE);
		}
		if (parentVO.getAttributeValue(OrderHeaderVO.BFINALCLOSE) == null) {
			parentVO.setAttributeValue(OrderHeaderVO.BFINALCLOSE,
					UFBoolean.FALSE);
		}
		if (parentVO.getAttributeValue(OrderHeaderVO.BPUBLISH) == null) {
			parentVO.setAttributeValue(OrderHeaderVO.BPUBLISH, UFBoolean.FALSE);
		}
		if (parentVO.getAttributeValue(OrderHeaderVO.BPUBLISH) == null) {
			parentVO.setAttributeValue(OrderHeaderVO.BPUBLISH, UFBoolean.FALSE);
		}
		if (parentVO.getAttributeValue(OrderHeaderVO.BISLATEST) == null) {
			parentVO.setAttributeValue(OrderHeaderVO.BISLATEST, UFBoolean.TRUE);
		}
		if (parentVO.getAttributeValue(OrderHeaderVO.NVERSION) == null) {
			parentVO.setAttributeValue(OrderHeaderVO.NVERSION, 1);
		}
		//
		// --表体根据来源查询设置
		// <vsourcetrantype>0001A510000000001SOR</vsourcetrantype>
		// <vsourcecode>QG2017051000000014</vsourcecode>
		// <vsourcerowno>10</vsourcerowno>
		if ("20".equalsIgnoreCase(bvos[0].getCsourcetypecode())) {
			String[] formulas = new String[] {
					"vsourcetrantype->getColValue(po_praybill,ctrantypeid ,pk_praybill ,csourceid)",
					"vsourcecode->getColValue(po_praybill,vbillcode ,pk_praybill ,csourceid)",
					"vsourcerowno->getColValue(po_praybill_b,crowno,  pk_praybill_b ,csourcebid)"

			};
			SuperVOUtil.execFormulaWithVOs(bvos, formulas);

		}
		for (OrderItemVO bvo : bvos) {
			bvo.setStatus(VOStatus.NEW);
			// --表体根据表头设置
			// <dbilldate>2017-05-21 20:20:16</dbilldate>
			// <pk_supplier>1001A5100000000008J1</pk_supplier>
			// <corigcurrencyid>1002Z0100000000001K1</corigcurrencyid>
			bvo.setDbilldate(parentVO.getDbilldate());
			bvo.setPk_supplier(parentVO.getPk_supplier());
			bvo.setCorigcurrencyid(parentVO.getCorigcurrencyid());

			// 首先根据单位，计算报价单位
			bvo.setCqtunitid(bvo.getCunitid());
			bvo.setVqtunitrate(bvo.getVchangerate());
			// --表体默认值

			if (bvo.getAttributeValue(OrderItemVO.FISACTIVE) == null) {
				bvo.setAttributeValue(OrderItemVO.FISACTIVE, 0);
			}
			if (bvo.getAttributeValue(OrderItemVO.BTRIATRADEFLAG) == null) {
				bvo.setAttributeValue(OrderItemVO.BTRIATRADEFLAG,
						UFBoolean.FALSE);
			}
			if (bvo.getAttributeValue(OrderItemVO.BBORROWPUR) == null) {
				bvo.setAttributeValue(OrderItemVO.BBORROWPUR, UFBoolean.FALSE);
			}
			if (bvo.getAttributeValue(OrderItemVO.BINVOICECLOSE) == null) {
				bvo.setAttributeValue(OrderItemVO.BINVOICECLOSE,
						UFBoolean.FALSE);
			}
			if (bvo.getAttributeValue(OrderItemVO.BARRIVECLOSE) == null) {
				bvo.setAttributeValue(OrderItemVO.BARRIVECLOSE, UFBoolean.FALSE);
			}
			if (bvo.getAttributeValue(OrderItemVO.BARRIVECLOSE) == null) {
				bvo.setAttributeValue(OrderItemVO.BARRIVECLOSE, UFBoolean.FALSE);
			}
			if (bvo.getAttributeValue(OrderItemVO.BPAYCLOSE) == null) {
				bvo.setAttributeValue(OrderItemVO.BPAYCLOSE, UFBoolean.FALSE);
			}
			if (bvo.getAttributeValue(OrderItemVO.BRECEIVEPLAN) == null) {
				bvo.setAttributeValue(OrderItemVO.BRECEIVEPLAN, UFBoolean.FALSE);
			}
			if (bvo.getAttributeValue(OrderItemVO.BLARGESS) == null) {
				bvo.setAttributeValue(OrderItemVO.BLARGESS, UFBoolean.FALSE);
			}
			if (bvo.getAttributeValue(OrderItemVO.BTRANSCLOSED) == null) {
				bvo.setAttributeValue(OrderItemVO.BTRANSCLOSED, UFBoolean.FALSE);
			}

			if ("30".equalsIgnoreCase(bvo.getCsourcetypecode())) {
				// 1. BPM来源信息记录的是BPM的销售订单信息
				// 2. NC的销售订单表体 vbdef20记录的BPM的销售订单信息（明细主键全局唯一）
				// 3. 采购订单根据记录的 BPM来源单据明细去NC销售订单
				VOQuery<SaleOrderBVO> query = new VOQuery<SaleOrderBVO>(
						SaleOrderBVO.class);
				SaleOrderBVO[] orderbvos = query.query(
						" and  vbdef20='" + bvo.getCsourcebid() + "'", null);
				if (orderbvos == null || orderbvos.length == 0) {
					throw new BusinessException(
							"根据BPM销售订单明细主键:"
									+ bvo.getCsourcebid()
									+ ",未查询到对应的NC销售订单,请根据以下sql核对:  select * from so_saleorder_b where nvl(dr,0)=0 and  vbdef20='"
									+ bvo.getCsourcebid() + "'");
				}
				if (orderbvos.length > 1) {
					throw new BusinessException(
							"根据BPM销售订单明细主键:"
									+ bvo.getCsourcebid()
									+ ",未查询到多条的NC销售订单明细,请根据以下sql核对:  select * from so_saleorder_b where nvl(dr,0)=0 and  vbdef20='"
									+ bvo.getCsourcebid() + "'");
				}
				VOQuery<SaleOrderHVO> hqury = new VOQuery<SaleOrderHVO>(
						SaleOrderHVO.class);
				SaleOrderHVO[] saleOrderHVOs = hqury
						.query(new String[] { orderbvos[0].getCsaleorderid() });

				bvo.setCsourceid(orderbvos[0].getCsaleorderid());
				bvo.setCsourcebid(orderbvos[0].getCsaleorderbid());
				bvo.setVsourcerowno(orderbvos[0].getCrowno());

				bvo.setVsourcecode(saleOrderHVOs[0].getVbillcode());
				bvo.setVsourcetrantype(saleOrderHVOs[0].getCtrantypeid());

			}

			// 润丰同步的业务源只有请购和协同销售订单
			// <vfirsttrantype>0001A510000000001SOR</vfirsttrantype>
			bvo.setVfirsttrantype(bvo.getVsourcetrantype());
			// <cfirsttypecode>20</cfirsttypecode>
			bvo.setCfirsttypecode(bvo.getCsourcetypecode());
			// <cfirstid>1001A41000000000BOZQ</cfirstid>
			bvo.setCfirstid(bvo.getCsourceid());
			// <cfirstbid>1001A41000000000BOZR</cfirstbid>
			bvo.setCfirstbid(bvo.getCsourcebid());
			// <vfirstcode>QG2017051000000014</vfirstcode>
			bvo.setVfirstcode(bvo.getVsourcecode());
			// <vfirstrowno>10</vfirstrowno>
			bvo.setVfirstrowno(bvo.getVsourcerowno());

		}

		// --表体所有的单价和以金额信息
		calculate(bill);
		// 根据表头的付款协议主键，补全表体的付款协议
		// paymentInfor(bill);

		// 补全行号
		fillupRowNo(bill);
	}

	private void fillupRowNo(OrderVO bill) {

		// 为行号为空的行补充行号。
		OrderItemVO[] items = bill.getBVO();
		List<OrderItemVO> bvos = new ArrayList<OrderItemVO>();
		for (OrderItemVO item : items) {
			int vostatus = item.getStatus();
			if (vostatus == VOStatus.DELETED) {
				// 不包含删除的行
				continue;
			}
			bvos.add(item);
		}
		items = bvos.toArray(new OrderItemVO[0]);
		VORowNoUtils.setVOsRowNoByRule(items, OrderItemVO.CROWNO);

	}

	private void paymentInfor(OrderVO bill) {
		// TODO 自动生成的方法存根
		OrderHeaderVO hvo = bill.getHVO();
		String payterm = hvo.getPk_payterm();
		if (payterm != null) {
			OrderPaymentVO[] paymentVOs = PaymentInfo
					.getOrderPaymentVOs(payterm);
			bill.setChildren(OrderPaymentVO.class, paymentVOs);
		}
	}

	private void calculate(OrderVO vo) {
		RelationCalculate cal = new RelationCalculate();
		//
		cal.calculate(vo, "nnum");
		cal.calculate(vo, "norigtaxmny");
	}
	
	private void calculateForModify(OrderVO vo) {
		RelationCalculate cal = new RelationCalculate();
		OrderItemVO[] items = vo.getBVO();
		// 清空单价：根据金额和数量重算
		String[] attributeNames = items[0].getAttributeNames();
		for (OrderItemVO bvo : items) {
			bvo.setCqtunitid(bvo.getCunitid());
			bvo.setVqtunitrate(bvo.getVqtunitrate());
			for (String attname : attributeNames) {
				if (attname.endsWith("price")) {
					bvo.setAttributeValue(attname, null);
				}
			}
		}
		//
//		cal.calculate(vo, "nnum");
		cal.calculate(vo, "norigtaxmny");
	}

	private boolean isFixUnitRate(String material, String cunitid,
			String castunitid) {
		boolean flag = true;
		if (material == null || cunitid == null || castunitid == null) {
			return flag;
		}
		flag = MaterialPubService.queryIsFixedRateByMaterialAndMeasdoc(
				material, cunitid, castunitid);
		return flag;
	}

	private void checkData(AggregatedValueObject resvo)
			throws BusinessException {
		// TODO 自动生成的方法存根
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		if (resvo.getChildrenVO() == null || resvo.getChildrenVO().length == 0) {
			throw new BusinessException("表体不允许为空");
		}
		OrderVO order = (OrderVO) resvo;
		if (order.getPaymentVO() == null || order.getPaymentVO().length == 0) {
			throw new BusinessException("表体付款协议不允许为空");
		}
		String approver = order.getHVO().getApprover();
		if (approver == null || approver.isEmpty()) {
			ExceptionUtils.wrappBusinessException(" 没有审批人:approver");
		}

		checkMny(resvo);

	}

	private void checkMny(AggregatedValueObject vo) {
		OrderItemVO[] bodyvos = ((OrderVO) vo).getBVO();
		for (OrderItemVO orderItemVO : bodyvos) {
			Integer fbuysellflag = orderItemVO.getFbuysellflag();

			if (BuySellFlagEnum.IMPORT.value().equals(fbuysellflag)
					|| BuySellFlagEnum.NATIONAL_BUY.value()
							.equals(fbuysellflag)) {
				return;
			} else {
				ExceptionUtils
						.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes()
								.getStrByID(
										"4004000_0",
										"04004000-0139",
										null,
										new String[] { orderItemVO.getCrowno() })/*
																				 * @
																				 * res
																				 * "行:{0}采购订单表体购销类型设置不正确！"
																				 */);
			}
		}

	}

}
