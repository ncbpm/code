package nc.bpm.ic.pfxx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.bpm.plugins.BatchCodeRule;
import nc.bs.ic.general.plugins.CheckMnyUtil;
import nc.bs.ic.general.plugins.CheckScaleUtil;
import nc.bs.ic.pub.env.ICBSContext;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.pf.IPFBusiAction;
import nc.pubitf.scmf.ic.mbatchcode.IBatchcodePubService;
import nc.vo.ic.batch.BatchRefViewVO;
import nc.vo.ic.batchcode.BatchSynchronizer;
import nc.vo.ic.batchcode.ICBatchFields;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillFlag;
import nc.vo.ic.general.define.ICBillHeadVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.m4c.entity.SaleOutBodyVO;
import nc.vo.ic.pub.calc.BusiCalculator;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.sncode.ICSnFields;
import nc.vo.ic.sncode.SnCodeSynchronizer;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.scmpub.res.billtype.ICBillType;
import nc.vo.scmpub.res.billtype.SOBillType;
import nc.vo.so.m4331.entity.DeliveryBVO;
import nc.vo.so.m4331.entity.DeliveryVO;

import org.apache.commons.lang.StringUtils;

/**
 * 销售 单通过计量系统生成
 * 
 * @author liyf
 * 
 */
public class M4CForJLAdd extends AbstractPfxxPlugin {

	private List<String> updateIndex = new ArrayList<String>();
	private int power = 8;// 精度

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		ICBillVO bpmBill = (ICBillVO) vo;
		// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(bpmBill);
		// 3.查询对应上的发货单：并且根据发货单明细过滤
		DeliveryVO[] chgBillS = fillData(bpmBill);
		// 4数据交换:按照一张发货单生成一张销售出库单场景处理
		String rs = "";
		for (DeliveryVO chgBill : chgBillS) {
			if (rs.length() > 5) {
				rs += ",";
			}
			
			ICBillVO[] destVos = PfServiceScmUtil.executeVOChange(
					SOBillType.Delivery.getCode(),
					ICBillType.SaleOut.getCode(), new DeliveryVO[] { chgBill });
			// 根据BPM回写的发货单信息更新
			ICBillVO clientVO = destVos[0];
			// 根据到货单的制单人同步到采购入库单,后续会判断，如果传过来，就会重新根据传过来地方赋值
			clientVO.getHead().setBillmaker(bpmBill.getHead().getBillmaker());
			updateClientVO(bpmBill, clientVO);
			// 保存
			ICBillVO saveVO = doSave(clientVO);
			rs += chgBill.getParentVO().getVbillcode() + ":"
					+ clientVO.getHead().getVbillcode();
			// 审批
			doSign(saveVO);
		}

		return rs;

	}

	/**
	 * 新增
	 * 
	 * @param swapContext
	 * @param icbill
	 * @return
	 * @throws BusinessException
	 */
	private ICBillVO doSave(ICBillVO icbill) throws BusinessException {
		// 检查是否允许保存
		this.checkCanInster(icbill);
		Logger.info("保存新单据前处理...");
		this.processBeforeSave(icbill);
		Logger.info("保存新单据...");
		//设置当前登录人
		InvocationInfoProxy.getInstance().setUserId(icbill.getHead().getBillmaker());
		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		ICBillVO[] icbills = (ICBillVO[]) service.processAction(
				IPFActionName.WRITE, "4C", null, icbill, null, null);

		Logger.info("保存新单据完成...");

		Logger.info("保存新单据后处理...");
		this.processAfterSave(icbill);

		return icbills[0];
	}

	/**
	 * 签字
	 * 
	 * @param swapContext
	 * @param icbill
	 * @return
	 * @throws BusinessException
	 */
	private ICBillVO doSign(ICBillVO icbill) throws BusinessException {
		// 检查是否允许保存
		Logger.info("签字新单据前处理...");
		// 签字时间等于单据日期
		icbill.getHead().setTaudittime(icbill.getHead().getDbilldate());
		icbill.getHead().setApprover(icbill.getHead().getBillmaker());
		//设置当前登录人
		InvocationInfoProxy.getInstance().setUserId(icbill.getHead().getApprover());
		Logger.info("签字新单据...");
		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		ICBillVO[] icbills = (ICBillVO[]) service.processAction("SIGN", "4C",
				null, icbill, null, null);

		Logger.info("签字新单据完成...");

		Logger.info("签字新单据后处理...");

		return icbills[0];
	}

	/**
	 * 检验vo是否可更新
	 * 
	 * @param vo
	 */
	protected void checkCanInster(AggregatedValueObject vo) {
		new CheckMnyUtil().checkMny(vo);
		new CheckScaleUtil().checkScale(vo);
	}

	/**
	 * 更新BPM回写的发货单信息到销售出库单
	 * 
	 * @param bill
	 * @param clientVO
	 * @throws BusinessException
	 */
	private void updateClientVO(ICBillVO bpmBill, ICBillVO clientVO)
			throws BusinessException {
		SaleOutBodyVO[] clientbodys = (SaleOutBodyVO[]) clientVO.getBodys();
		ICBillBodyVO[] bodys = (ICBillBodyVO[]) bpmBill.getBodys();
		Map<String, UFDouble> rownos = new HashMap<String, UFDouble>();
		UFDouble setp = new UFDouble(0.1);
		// 设置表头信息
		String[] headKeys = new String[] { "cwarehouseid", "dbilldate",
				"vtrantypecode", "cwhsmanagerid", "billmaker" };
		for (String key : headKeys) {
			clientVO.getHead().setAttributeValue(key,
					bpmBill.getHead().getAttributeValue(key));
		}

		List<SaleOutBodyVO> children = new ArrayList<SaleOutBodyVO>();
		children.addAll(Arrays.asList(clientbodys));
		// 拆行的处理
		for (ICBillBodyVO body : bodys) {
			String csourcebillbid = body.getCsourcebillbid();
			for (SaleOutBodyVO clientbody : clientbodys) {
				if (!clientbody.getCsourcebillbid().equalsIgnoreCase(
						csourcebillbid)) {
					continue;
				}
				// 一行表体，可能回写多个批次
				if (updateIndex.contains(csourcebillbid)) {
					SaleOutBodyVO newBody = (SaleOutBodyVO) clientbody.clone();
					String crowno = newBody.getCrowno();
					if (rownos.containsKey(crowno)) {
						UFDouble max_rowno = rownos.get(crowno).add(setp);
						rownos.put(crowno, max_rowno);
					} else {
						UFDouble max_rowno = new UFDouble(crowno).add(setp);
						rownos.put(crowno, max_rowno);
					}
					newBody.setCrowno(rownos.get(crowno)
							.setScale(2, UFDouble.ROUND_HALF_UP).toString());
					newBody.setNshouldnum(null);
					newBody.setNshouldassistnum(null);
					updateClientBVO(body, newBody);
					children.add(newBody);
				} else {
					updateIndex.add(csourcebillbid);
					updateClientBVO(body, clientbody);
				}

			}
		}
		SaleOutBodyVO[] new_bodys = children.toArray(new SaleOutBodyVO[0]);
		// 数量
		BusiCalculator.getBusiCalculatorAtBS().calcNum(new_bodys,
				ICPubMetaNameConst.NNUM);
		clientVO.setChildrenVO(new_bodys);
		// 计算金额
		BusiCalculator.getBusiCalculatorAtBS().calcOnlyMny(
				new ICBillVO[] { clientVO }, ICPubMetaNameConst.NNUM);
	}

	private void updateClientBVO(ICBillBodyVO body, ICBillBodyVO clientbody)
			throws BusinessException {
		String[] bodyKeys = body.getAttributeNames();
		for (String key : bodyKeys) {
			if (nc.util.mmpub.dpub.base.ValueCheckUtil.isEmpty(body
					.getAttributeValue(key))) {
				continue;
			}
			Object attributeValue = body.getAttributeValue(key);
			if (attributeValue instanceof UFDouble) {
				UFDouble value = (UFDouble) attributeValue;
				attributeValue = value.setScale(power, UFDouble.ROUND_HALF_UP);
			}
			clientbody.setAttributeValue(key, attributeValue);
		}

	}

	private UFDouble getUFDdoubleNullASZero(Object o) {
		if (o == null) {
			return UFDouble.ZERO_DBL;
		}
		if (o instanceof UFDouble) {
			return (UFDouble) o;
		} else {
			return new UFDouble((String) o);
		}
	}

	/**
	 * 查询发货单
	 * 
	 * @param bill
	 * @return
	 * @throws BusinessException
	 */
	private DeliveryVO[] fillData(ICBillVO bill) throws BusinessException {
		// TODO 自动生成的方法存根
		// 可能存在合并销售出库的情况，合并入库，但是不会合并行明细
		List<String> arrPks = new ArrayList<String>();
		List<String> arrbpks = new ArrayList<String>();
		for (ICBillBodyVO body : bill.getBodys()) {
			if (!arrPks.contains(body.getCsourcebillhid())) {
				arrPks.add(body.getCsourcebillhid());
			}
			if (!arrbpks.contains(body.getCsourcebillbid())) {
				arrbpks.add(body.getCsourcebillbid());
			}
		}
		BillQuery<DeliveryVO> bquery = new BillQuery<>(DeliveryVO.class);
		DeliveryVO[] aggvos = bquery.query(arrPks.toArray(new String[0]));
		if (aggvos == null || aggvos.length == 0) {
			throw new BusinessException("根据指定的主键，" + arrPks.toString()
					+ "未查询到发货单,请核对.");
		}
		// 来源的发货单，可能存在合并入库，即多个发货单对对应一个入库单
		List<DeliveryVO> chgBillS = new ArrayList<DeliveryVO>();
		for (DeliveryVO aggvo : aggvos) {
			List<DeliveryBVO> arrblist = new ArrayList<DeliveryBVO>();
			DeliveryBVO[] itemVOs = aggvo.getChildrenVO();
			for (DeliveryBVO itemVO : itemVOs) {
				if (arrbpks.contains(itemVO.getCdeliverybid())) {
					arrblist.add(itemVO);
				}
			}
			if (arrblist.size() > 0) {
				DeliveryVO chgArrBill = new DeliveryVO();
				chgArrBill.setParentVO(aggvo.getParentVO());
				chgArrBill.setChildrenVO(arrblist.toArray(new DeliveryBVO[0]));
				chgBillS.add(chgArrBill);
			}
		}

		//
		if (chgBillS.size() == 0) {
			throw new BusinessException(
					"根据指定的主键Csourcebillhid可以查询到发货单，但是根据Csourcebillbid未查询到发货单明细,请核对数据是否正确.");
		}

		return chgBillS.toArray(new DeliveryVO[0]);

	}

	private void checkData(ICBillVO bill) throws BusinessException {
		// TODO 自动生成的方法存根
		ICBillBodyVO[] DeliveryBVOs = bill.getBodys();
		if (DeliveryBVOs == null || DeliveryBVOs.length == 0) {
			throw new BusinessException("请指定需要出库的表体行.");
		}
		for (ICBillBodyVO body : DeliveryBVOs) {
			if (StringUtils.isEmpty(body.getCsourcebillhid())) {
				throw new BusinessException("请指定来源主键 csourcebillhid 的值.");
			}

			if (StringUtils.isEmpty(body.getCsourcebillbid())) {
				throw new BusinessException("请指定来源表体行主键 csourcebillbid 的值.");
			}

		}

	}

	/**
	 * 单据保存前处理
	 * 
	 * @param vo
	 */
	protected void processBeforeSave(ICBillVO vo) throws BusinessException {

		if (null == vo)
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0139")/*
																			 * @res
																			 * "单据不能为空"
																			 */);

		// this.checkNullValue(vo);
		// 表头组织翻译后没有赋值，需要重表体字段重新取一边
		if (StringUtil.isSEmptyOrNull(vo.getParentVO().getPk_org()))
			vo.getParentVO().setPk_org(vo.getBodys()[0].getPk_org());
		if (StringUtil.isSEmptyOrNull(vo.getParentVO().getPk_org_v()))
			vo.getParentVO().setPk_org_v(vo.getBodys()[0].getPk_org_v());
		if (StringUtil.isSEmptyOrNull(vo.getParentVO().getCwarehouseid()))
			vo.getParentVO().setCwarehouseid(
					vo.getBodys()[0].getCbodywarehouseid());

		ICBSContext context = new ICBSContext();
		this.headVOProcess(vo.getHead(), context);
		this.bodyVOProcess(vo, context);
	}

	/**
	 * 单据保存后处理
	 * 
	 * @param vo
	 */
	protected void processAfterSave(ICBillVO vo) throws BusinessException {
		if (null == vo)
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0140")/*
																			 * @res
																			 * "单据保存失败"
																			 */);
	}

	/**
	 * 表头处理，处理默认值，公司，单据日期，单据状态，打印次数，集团
	 * 
	 * @param vo
	 */
	private void headVOProcess(ICBillHeadVO vo, ICBSContext context) {
		vo.setStatus(VOStatus.NEW);
		// 集团
		if (StringUtil.isSEmptyOrNull(vo.getPk_group()))
			vo.setPk_group(context.getPk_group());
		// 打印次数
		if (vo.getIprintcount() == null)
			vo.setIprintcount(Integer.valueOf(0));
		// 单据状态
		if (vo.getFbillflag() == null)
			vo.setFbillflag((Integer) ICBillFlag.FREE.value());
		// 单据日期
		if (vo.getDbilldate() == null)
			vo.setDbilldate(context.getBizDate());
		vo.setDmakedate(vo.getDbilldate());
		// 创建时间
		vo.setCreationtime(new UFDateTime(vo.getDbilldate().toString()));
		vo.setCreator(vo.getBillmaker());
		//
		// 公司
		if (StringUtil.isSEmptyOrNull(vo.getCorpoid())
				|| StringUtil.isSEmptyOrNull(vo.getCorpvid())) {
			vo.setCorpoid(context.getOrgInfo().getCorpIDByCalBodyID(
					vo.getPk_org()));
			vo.setCorpvid(context.getOrgInfo().getCorpVIDByCalBodyID(
					vo.getPk_org()));
		}

		if (StringUtil.isSEmptyOrNull(vo.getCtrantypeid())) {
			// uap不支持单据类型的翻译，暂时以交易类型code查询id的方式补交易类型
			String vtrantypecode = vo.getVtrantypecode();
			Map<String, String> map = PfServiceScmUtil
					.getTrantypeidByCode(new String[] { vtrantypecode });
			vo.setCtrantypeid(map == null ? null : map.get(vtrantypecode));
		}

	}

	/**
	 * 单据表体处理
	 * 
	 * @param vo
	 * @param context
	 * @throws BusinessException
	 */
	private void bodyVOProcess(ICBillVO vo, ICBSContext context)
			throws BusinessException {
		ICBillBodyVO[] vos = vo.getBodys();
		if (ValueCheckUtil.isNullORZeroLength(vos))
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0141")/*
																			 * @res
																			 * "单据表体不能为空"
																			 */);

		VORowNoUtils.setVOsRowNoByRule(vos, ICPubMetaNameConst.CROWNO);// 行号处理

		ICBillHeadVO head = vo.getHead();
		Map<String, BatchcodeVO> batchmap = this.getBatchcodeVO(vos);
		for (ICBillBodyVO body : vos) {
			body.setStatus(VOStatus.NEW);
			if (StringUtil.isSEmptyOrNull(body.getCmaterialoid())
					|| StringUtil.isSEmptyOrNull(body.getCmaterialvid()))
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("4008001_0", "04008001-0142")/*
																 * @res
																 * "单据表体物料不能为空"
																 */);

			body.setBbarcodeclose(UFBoolean.FALSE);// 单据行是否条码关闭
			body.setBonroadflag(UFBoolean.FALSE);// 是否在途
			if (body.getNnum() != null && body.getNassistnum() != null
					&& body.getDbizdate() == null)
				body.setDbizdate(head.getDbilldate());// 业务日期
			// 辅单位
			if (StringUtil.isSEmptyOrNull(body.getCastunitid()))
				body.setCastunitid(context.getInvInfo()
						.getInvBasVO(body.getCmaterialvid()).getPk_stockmeas());

			// 有批次号但无批次主键时， 需要补全批次主键，有必要时(保质期管理)补全生产日期和失效日期
			if (!StringUtils.isEmpty(body.getVbatchcode())
					&& StringUtils.isEmpty(body.getPk_batchcode())) {
				// 同步的维度信息
				BatchCodeRule batchCodeRule = new BatchCodeRule();
				BatchRefViewVO batchRefViewVOs = batchCodeRule.getRefVO(
						body.getPk_org(), body.getCbodywarehouseid(),
						body.getCmaterialvid(), body.getVbatchcode());
				if (batchRefViewVOs != null) {
					batchCodeRule.synBatch(batchRefViewVOs, body);
				}
			}
		
			bodyVOCopyFromHeadVO(body, head);
		}
		for (ICBillBodyVO body : vos) {
			body.setDbizdate(head.getDbilldate());// 业务日期
		}
		vo.setChildrenVO(vos);
		// 同步表体批次辅助字段
		new BatchSynchronizer(new ICBatchFields()).fillBatchVOtoBill(vos);
		// 同步表体序列号辅助字段
		new SnCodeSynchronizer(new ICSnFields()).fillBatchVOtoBill(vos);

	}

	/**
	 * 获取批次号档案
	 * 
	 * @param vos
	 * @return Map<String(cmaterialvid+vbatchcode), BatchcodeVO批次档案>
	 */
	private Map<String, BatchcodeVO> getBatchcodeVO(ICBillBodyVO[] vos) {
		List<String> cmaterialvidList = new ArrayList<String>();
		List<String> vbatchcodeList = new ArrayList<String>();
		Set<String> materialbatch = new HashSet<String>();
		for (ICBillBodyVO body : vos) {
			if (body.getCmaterialvid() != null && body.getVbatchcode() != null) {
				if (materialbatch.contains(body.getCmaterialvid()
						+ body.getVbatchcode())) {
					continue;
				}
				cmaterialvidList.add(body.getCmaterialvid());
				vbatchcodeList.add(body.getVbatchcode());
				materialbatch
						.add(body.getCmaterialvid() + body.getVbatchcode());
			}
		}
		if (materialbatch.size() == 0) {
			return new HashMap<String, BatchcodeVO>();
		}
		IBatchcodePubService batchservice = NCLocator.getInstance().lookup(
				IBatchcodePubService.class);
		BatchcodeVO[] batchvos = null;
		try {
			batchvos = batchservice.queryBatchVOs(
					cmaterialvidList.toArray(new String[0]),
					vbatchcodeList.toArray(new String[0]));
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}

		if (batchvos == null || batchvos.length == 0) {
			return new HashMap<String, BatchcodeVO>();
		}
		Map<String, BatchcodeVO> batchmap = new HashMap<String, BatchcodeVO>();
		for (BatchcodeVO batchvo : batchvos) {
			batchmap.put(batchvo.getCmaterialvid() + batchvo.getVbatchcode(),
					batchvo);
		}
		return batchmap;
	}

	/**
	 * 根据表头设置表体默认值，表体集团，库存组织，公司，仓库，交易类型
	 * 
	 * @param body
	 * @param head
	 */
	private void bodyVOCopyFromHeadVO(ICBillBodyVO body, ICBillHeadVO head) {
		body.setPk_group(head.getPk_group());
		body.setPk_org(head.getPk_org());
		body.setPk_org_v(head.getPk_org_v());
		body.setCorpoid(head.getCorpoid());
		body.setCorpvid(head.getCorpvid());
		body.setCbodywarehouseid(head.getCwarehouseid());
		body.setCbodytranstypecode(head.getVtrantypecode());
	}

}
