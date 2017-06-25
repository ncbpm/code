package nc.bpm.pu.pfxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author liyf
 * 
 */

public class M21ForBPMAdd extends AbstractPfxxPlugin {
	
	String[] bodyattributeNames = new String[]{"pk_group","pk_org","pk_org_v","vbdef1","vbmemo","pk_reqstoorg","pk_reqstoorg_v","pk_arrvstoorg","pk_arrvstoorg_v","pk_flowstockorg","pk_flowstockorg_v","crowno","pk_material","pk_srcmaterial","cunitid","nnum","castunitid","nastnum","vchangerate","norigtaxmny","norigtaxprice","dplanarrvdate","chandler","fisactive","breceiveplan","blargess","btransclosed","pk_psfinanceorg","pk_psfinanceorg_v","pk_apfinanceorg","pk_apfinanceorg_v","bborrowpur","nweight","nvolumn","bstockclose","binvoiceclose","barriveclose","bpayclose","ftaxtypeflag","ntaxrate","ccurrencyid","nexchangerate","dbilldate","pk_supplier","corigcurrencyid","csendcountryid","crececountryid","ctaxcountryid","fbuysellflag","btriatradeflag","ctaxcodeid","nnosubtaxrate","nnosubtax"};

	String[] headattributeNames = new String[]{"pk_order","pk_group","vmemo","pk_org","pk_org_v","vbillcode","dbilldate","pk_supplier","pk_dept_v","pk_dept","vtrantypecode","pk_invcsupllier","pk_payterm","billmaker","forderstatus","approver","bisreplenish","breturn","iprintcount","creationtime","taudittime","bcooptoso","bsocooptome","ntotalastnum","ntotalorigmny","bfrozen","pk_busitype","fhtaxtypeflag","corigcurrencyid","brefwhenreturn","ntotalweight","ntotalvolume","ntotalpiece","bfinalclose","creator","ctrantypeid","bpublish"};

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������

		// 1.�õ�ת�����VO����,ȡ�����򵼵�һ��ע���VO��Ϣ
		AggregatedValueObject resvo = (AggregatedValueObject) vo;
		OrderVO order = (OrderVO) resvo;
		String vopk = order.getPrimaryKey();
		if (StringUtils.isEmpty(vopk)) {
			// 2. У�����ݵĺϷ���:1.���ݽṹ���� 2.������֯+���ݺ�У���Ƿ��ظ�.
			checkData(resvo);
			// 3.��ȫ����,���ҵ�������״̬
			fillData(resvo);
			//
			AggregatedValueObject bill2 = insert(resvo);

			return bill2.getParentVO().getPrimaryKey();
		} else {
			// ������ڣ���ִ�вɹ������޶����߹رմ�
			OrderVO queryVo = this.queryVOByPk(vopk);
			if (queryVo == null) {
				throw new BusinessException("����������" + vopk + "  ��NCδ��ѯ��Ӧ�Ĳɹ�����.");
			}

			// �ж��Ƿ������رջ��ߴ�
			if ("�ر�".equalsIgnoreCase(order.getHVO().getVmemo())) {
				NCLocator.getInstance().lookup(IOrderClosePubService.class)
						.finalClose(new OrderVO[] { queryVo });
				return vopk;

			}
			if ("��".equalsIgnoreCase(order.getHVO().getVmemo())) {
				NCLocator.getInstance().lookup(IOrderClosePubService.class)
						.finalOpen(new OrderVO[] { queryVo });
				return vopk;
			}
			// ���в��� --�йرջ����д򿪣�ִ��
			Map<String, OrderItemVO> map = new HashMap<String, OrderItemVO>();
			for (OrderItemVO body : queryVo.getBVO()) {
				if (body.getVbdef1() != null) {
					map.put(body.getVbdef1(), body);
				}
			}
			// �رյ���
			List<OrderItemVO> close_row = new ArrayList<OrderItemVO>();
			// �򿪵���
			List<OrderItemVO> open_row = new ArrayList<OrderItemVO>();
			// �޸ĵ���
			List<OrderItemVO> update_row = new ArrayList<OrderItemVO>();
			// ɾ������
			List<OrderItemVO> del_row = new ArrayList<OrderItemVO>();
			// ��������
			List<OrderItemVO> add_row = new ArrayList<OrderItemVO>();

			for (OrderItemVO body : order.getBVO()) {
				if ("�ر�".equalsIgnoreCase(body.getVbmemo())) {
					if (map.containsKey(body.getVbdef1())) {
						close_row.add(map.get(body.getVbdef1()));
					} else {
						throw new BusinessException("����BPM�ɹ���ͬ����������"
								+ body.getVbdef1() + " ��NCδƥ�䵽��Ӧ����.");
					}
				} else if ("��".equalsIgnoreCase(body.getVbmemo())) {
					if (map.containsKey(body.getVbdef1())) {
						open_row.add(map.get(body.getVbdef1()));
					} else {
						throw new BusinessException("����BPM�ɹ���ͬ����������"
								+ body.getVbdef1() + " ��NCδƥ�䵽��Ӧ����.");
					}
				} else if ("���".equalsIgnoreCase(body.getVbmemo())) {
					if (map.containsKey(body.getVbdef1())) {
						update_row.add(body);
					} else {
						throw new BusinessException("����BPM�ɹ���ͬ����������"
								+ body.getVbdef1() + " ��NCδƥ�䵽��Ӧ����.");
					}
				} else if ("ɾ��".equalsIgnoreCase(body.getVbmemo())) {
					if (map.containsKey(body.getVbdef1())) {
						del_row.add(body);
					} else {
						throw new BusinessException("����BPM�ɹ���ͬ����������"
								+ body.getVbdef1() + " ��NCδƥ�䵽��Ӧ����.");
					}
				} else if ("����".equalsIgnoreCase(body.getVbmemo())) {
					if (map.containsKey(body.getVbdef1())) {
						throw new BusinessException("�������У�ָ����BPM������ID "
								+ body.getVbdef1() + " ��NC��ǰ�ɹ������Ѿ����ڣ�����.");
					} else {
						add_row.add(body);
					}
				} else {
//					throw new BusinessException("�ɹ����������δ֧�ֵı���в�����:"
//							+ body.getVbmemo());
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
			//����ִ�в�ѯ����ֹ��������
			if(requry){
				queryVo = queryVOByPk(vopk);
			}
			// ���±�ͷ
			updateHVO(queryVo, order);
			if (update_row.size() > 0) {
				updateBody(queryVo, update_row);
			}
			if (del_row.size() > 0) {
				delBody(queryVo, update_row);
			}
			if (add_row.size() > 0) {
				addBody(queryVo, add_row);
			}
			calculate(queryVo);

			NCLocator.getInstance().lookup(IOrderRevise.class)
					.reviseSave(new OrderVO[] { queryVo }, null);

		
			return vopk;
		}

	}

	private void addBody(OrderVO queryVo, List<OrderItemVO> add_row) {
		// TODO �Զ����ɵķ������
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
					"vsourcetrantype->getColValue(so_saleorder,ctrantypeid,csaleorderid  ,csourceid)",
					"vsourcecode->getColValue(so_saleorder,vbillcode ,csaleorderid  ,csourceid)",
					"vsourcerowno->getColValue(so_saleorder_b,crowno,  csaleorderbid  ,csourcebid)"

			};
			SuperVOUtil.execFormulaWithVOs(vos, formulas);
		}
		List<OrderItemVO> asList = new ArrayList<OrderItemVO>();
		for(OrderItemVO bvo:queryVo.getBVO()){
			asList.add(bvo);
		}

		for (OrderItemVO bvo : vos) {
			bvo.setStatus(VOStatus.NEW);
			bvo.setPk_order(parentVO.getPk_order());
			// --������ݱ�ͷ����
			// <dbilldate>2017-05-21 20:20:16</dbilldate>
			// <pk_supplier>1001A5100000000008J1</pk_supplier>
			// <corigcurrencyid>1002Z0100000000001K1</corigcurrencyid>
			bvo.setDbilldate(parentVO.getDbilldate());
			bvo.setPk_supplier(parentVO.getPk_supplier());
			bvo.setCorigcurrencyid(parentVO.getCorigcurrencyid());

			// ���ȸ��ݵ�λ�����㱨�۵�λ
			bvo.setCqtunitid(bvo.getCunitid());
			bvo.setVqtunitrate(bvo.getVchangerate());
			// --����Ĭ��ֵ
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

			// ���ͬ����ҵ��Դֻ���빺��Эͬ���۶���
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
		
		//��ȫ�к�
		fillupRowNo(queryVo);
	}

	private void updateBody(OrderVO queryVo, List<OrderItemVO> update_row) throws BusinessException {
		// TODO �Զ����ɵķ������
		Map<String, OrderItemVO> map = new HashMap<String, OrderItemVO>();
		for (OrderItemVO body : queryVo.getBVO()) {
			if (body.getVbdef1() != null) {
				map.put(body.getVbdef1(), body);
			}
		}
		for (OrderItemVO bpm : update_row) {
			OrderItemVO orderItemVO = map.get(bpm.getVbdef1());
			//У���޶�����������Ƿ�С���ۼƵ�������
			UFDouble bpm_nnum = bpm.getNnum() == null ? UFDouble.ZERO_DBL:bpm.getNnum();
			UFDouble bpm_naccumarrvnum = orderItemVO.getNaccumarrvnum() == null ? UFDouble.ZERO_DBL:orderItemVO.getNaccumarrvnum();
			if(bpm_nnum.sub(bpm_naccumarrvnum).doubleValue() <0){
				throw new BusinessException("�������Ϸ� :��"+bpm.getVbdef1()+" �����޶��������:"+bpm_nnum+ ".����С�����ۼƵ���:"+bpm_naccumarrvnum);
			}
			for (String attr : bodyattributeNames) {
				
				orderItemVO.setAttributeValue(attr,
						bpm.getAttributeValue(attr));
			}
			orderItemVO.setStatus(VOStatus.UPDATED);
		}
	}

	private void delBody(OrderVO queryVo, List<OrderItemVO> update_row) {
		// TODO �Զ����ɵķ������
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
		// TODO �Զ����ɵķ������
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
		String approver = hvo.getApprover();
		hvo.setApprover(null);
		hvo.setForderstatus(POEnumBillStatus.FREE.toInt());
		UFDate taudittime = hvo.getTaudittime();
		hvo.setTaudittime(null);
		AggregatedValueObject save = this.save(vo, null);
		OrderVO orderVO = this.approve(save, approver, taudittime);
		return orderVO;
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
		// ǰ����
		processer.before(vos);

		OrderHeaderVO hvo = order.getHVO();
		hvo.setApprover(approver);
		hvo.setTaudittime(taudittime);

		IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator
				.getInstance().lookup(IplatFormEntry.class.getName());
		Object retObj = iIplatFormEntry.processAction("APPROVE", "21", null,
				order, null, null);

		// approveImpl.approve(vos, null);
		processer.after(vos);
		return order;
	}

	private void addBeforeRule(AroundProcesser<OrderVO> processer) {
		// ���VO״̬���
		processer.addBeforeRule(new ApproveVOValidateRule());
		// ��Ӧ�̶�����
		processer.addBeforeRule(new SupplierFrozeChkRule());
		// ����ǰ�¼�
		processer.addBeforeRule(new ApproveBeforeEventRule());
	}

	private void addAfterRule(AroundProcesser<OrderVO> processer) {
		// дҵ����־Ҫ�ŵ�FilterOrderByStatusRule��ǰ�棬����vo�п��ܻᱻ���˵�����û����¼��־��
		processer.addAfterRule(new WriteOperateLogRule<OrderVO>(
				PuBusiLogPathCode.orderApprovePath.getCode(),
				PuBusiLogActionCode.approve.getCode()));

		processer.addAfterRule(new FilterOrderByStatusRule(
				POEnumBillStatus.APPROVE.toInt()));
		processer.addAfterRule(new ApproveSupplyRule());
		// �ɹ��ƻ����
		processer.addAfterRule(new ApproveBudgetCtrlRule());
		// ����;״̬���в�������
		processer.addAfterRule(new InsertStatusOnWayRule());
		processer.addAfterRule(new InsertPayPlanRule());
		// ���ÿ����������Ϊ��˺�ֱ������ⵥ�ṩ֧��
		processer.addAfterRule(new FillNcaninnumRule());
		// �������¼�
		processer.addAfterRule(new ApproveAfterEventRule());
	}

	private void fillData(AggregatedValueObject resvo) {
		// TODO �Զ����ɵķ������
		// ��ȫ������Ϣ��BPM��������������ȫ���������Ƽ�����
		OrderVO bill = (OrderVO) resvo;
		OrderHeaderVO parentVO = bill.getHVO();
		parentVO.setStatus(VOStatus.NEW);
		OrderItemVO[] bvos = bill.getBVO();
		// ����״̬������
		parentVO.setForderstatus(POEnumBillStatus.FREE.toInteger());

		// ��ͷĬ��ֵ
		parentVO.setIprintcount(0);
		parentVO.setBrefwhenreturn(UFBoolean.FALSE);
		parentVO.setBfrozen(UFBoolean.FALSE);
		parentVO.setBisreplenish(UFBoolean.FALSE);
		parentVO.setBfinalclose(UFBoolean.FALSE);
		parentVO.setBpublish(UFBoolean.FALSE);
		parentVO.setBislatest(UFBoolean.TRUE);
		parentVO.setNversion(1);
		parentVO.setBislatest(UFBoolean.TRUE);
		//
		// --���������Դ��ѯ����
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

		} else if ("30".equalsIgnoreCase(bvos[0].getCsourcetypecode())) {
			String[] formulas = new String[] {
					"vsourcetrantype->getColValue(so_saleorder,ctrantypeid,csaleorderid  ,csourceid)",
					"vsourcecode->getColValue(so_saleorder,vbillcode ,csaleorderid  ,csourceid)",
					"vsourcerowno->getColValue(so_saleorder_b,crowno,  csaleorderbid  ,csourcebid)"

			};
			SuperVOUtil.execFormulaWithVOs(bvos, formulas);
		}
		for (OrderItemVO bvo : bvos) {

			bvo.setStatus(VOStatus.NEW);

			// --������ݱ�ͷ����
			// <dbilldate>2017-05-21 20:20:16</dbilldate>
			// <pk_supplier>1001A5100000000008J1</pk_supplier>
			// <corigcurrencyid>1002Z0100000000001K1</corigcurrencyid>
			bvo.setDbilldate(parentVO.getDbilldate());
			bvo.setPk_supplier(parentVO.getPk_supplier());
			bvo.setCorigcurrencyid(parentVO.getCorigcurrencyid());

			// ���ȸ��ݵ�λ�����㱨�۵�λ
			bvo.setCqtunitid(bvo.getCunitid());
			bvo.setVqtunitrate(bvo.getVchangerate());
			// --����Ĭ��ֵ
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

			// ���ͬ����ҵ��Դֻ���빺��Эͬ���۶���
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
		
		// --�������еĵ��ۺ��Խ����Ϣ
		calculate(bill);
		// ���ݱ�ͷ�ĸ���Э����������ȫ����ĸ���Э��
		paymentInfor(bill);
		
		//��ȫ�к�
		fillupRowNo(bill);
	}


	  private void fillupRowNo(OrderVO bill) {

	    // Ϊ�к�Ϊ�յ��в����кš�
	    OrderItemVO[] items = bill.getBVO();
	    List<OrderItemVO> bvos = new ArrayList<OrderItemVO>();
	    for (OrderItemVO item : items) {
	      int vostatus = item.getStatus();
	      if (vostatus == VOStatus.DELETED) {
	        // ������ɾ������
	        continue;
	      }
	      bvos.add(item);
	    }
	    items = bvos.toArray(new OrderItemVO[0]);
	    VORowNoUtils.setVOsRowNoByRule(items, OrderItemVO.CROWNO);

	  }
	
	private void paymentInfor(OrderVO bill) {
		// TODO �Զ����ɵķ������
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
		// TODO �Զ����ɵķ������
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("δ��ȡ��ת���������");
		if (resvo.getChildrenVO() == null || resvo.getChildrenVO().length == 0) {
			throw new BusinessException("���岻����Ϊ��");
		}
		OrderVO order = (OrderVO) resvo;
		String approver = order.getHVO().getApprover();
		if (approver == null || approver.isEmpty()) {
			ExceptionUtils.wrappBusinessException(" û��������:approver");
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
																				 * "��:{0}�ɹ��������幺���������ò���ȷ��"
																				 */);
			}
		}

	}

}
