package nc.bpm.mmpac.pfxx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.env.ICBSContext;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.mmpac.wr.IWrBusinessService;
import nc.itf.mmpac.wr.IWrMaintainService;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.pubitf.scmf.ic.mbatchcode.IBatchcodePubService;
import nc.util.mmpub.dpub.base.ValueCheckUtil;
import nc.vo.ic.pub.check.VOCheckUtil;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.mmpac.pacpub.consts.MMPacBillTypeConstant;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOItemVO;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.mmpac.wr.entity.WrVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;

import org.apache.commons.lang.StringUtils;

/**
 * 条码完工入库
 * 1. 一个订单有多行，一个订单一般不会一个班次就生成完成的。应该是每个班组或班次有自己的条码，完工后扫码填写完工数量完成入库
2. 班组，批次都通过条码传回来。
3. 考虑支持一个订单多批次分次完工，即一个订单对多个完工报告， 一个订单明细行，对应多个完工明细行
3.2 根据回传的明细，如果来源属于两个订单，逻辑上NC也进行了支持（）
3. 条码回写来源订单信息以及生产报告产出信息（班组，时间，数量，批次等），NC自动关联生产订单，补全来源信息，	全备料的生产报告消耗信息
4. 接口完工报告，自动审批，并且报检生成报检单，NC补全  生产报告质量信息
 * @author liyf
 *
 */
public class WgrkPfxxForBpmAdd extends AbstractPfxxPlugin {
	
	private IWrMaintainService service =null;


	public IWrMaintainService getMaintainService() {
		if(this.service==null){
			this.service = NCLocator.getInstance().lookup(IWrMaintainService.class);
		}
		return this.service;
	}

	private List<String> updateIndex = new ArrayList<String>();
	private int power = 8;// 精度
	private String billtype = "55A4";

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		AggWrVO bpmBill = (AggWrVO) vo;
		
		checkData(bpmBill);
		// 3.查询对应上的生产订单：并且根据生产订单明细过滤
		PMOAggVO[] chgBillS = fillData(bpmBill);
		// 4数据交换:生成一张完工报告单场景处理
		// 根据生产订单的制单人同步到完工报告单
		AggWrVO[] destVos = PfServiceScmUtil.executeVOChange(
				MMPacBillTypeConstant.PMO, MMPacBillTypeConstant.WR,
				chgBillS);
		// 合并生成一张完工报告单
		List<WrItemVO> body_list = new ArrayList<WrItemVO>();
		for (AggWrVO bill : destVos) {
			WrItemVO[] bodys = bill.getChildrenVO();
			for (WrItemVO body : bodys) {
				body_list.add(body);
			}
		}
		// 根据BPM回写的生产订单信息更新
		AggWrVO clientVO = destVos[0];
		clientVO.setChildrenVO(body_list.toArray(new WrItemVO[0]));
		updateClientVO(bpmBill, clientVO);
		String auditer = clientVO.getParentVO().getAuditer();
		// 保存
		AggWrVO saveVO = doSave(clientVO);
		
		// 审批
		clientVO.getParentVO().setAuditer(auditer);
		
		AggWrVO signVO = doSign(saveVO);
		//报检
        this.getWrBusinessService().applyCheck(new AggWrVO[]{signVO});

		return saveVO.getParentVO().getVbillcode();

	}
	 protected IWrBusinessService getWrBusinessService() {
	        return NCLocator.getInstance().lookup(IWrBusinessService.class);
	 }

	/**
	 * 新增
	 * 
	 * @param swapContext
	 * @param resvo
	 * @return
	 * @throws BusinessException
	 */
	private AggWrVO doSave(AggWrVO resvo) throws BusinessException {
		// 检查是否允许保存
		InvocationInfoProxy.getInstance().setUserId(resvo.getParentVO().getBillmaker());
		Logger.info("保存新单据前处理...");
		this.processBeforeSave(resvo);
		// TODO 单据设置有辅助信息，aggxsysvo为用户配置的具体辅助信息
		Logger.info("保存新单据...");
		AggWrVO returnVO = this.getMaintainService().insert(new AggWrVO[] {
	                (AggWrVO) resvo
	            })[0];

		Logger.info("保存新单据完成...");

		Logger.info("保存新单据后处理...");
		this.processAfterSave(resvo);

		return returnVO;
	}

	/**
	 * 签字
	 * 
	 * @param swapContext
	 * @param resvo
	 * @return
	 * @throws BusinessException
	 */
	private AggWrVO doSign(AggWrVO resvo) throws BusinessException {
		// 检查是否允许保存
		Logger.info("签字新单据前处理...");
		InvocationInfoProxy.getInstance().setUserId(resvo.getParentVO().getAuditer());
		// 签字时间等于单据日期
		resvo.getParentVO().setTaudittime(resvo.getParentVO().getDbilldate());
		resvo.getParentVO().setAuditer(resvo.getParentVO().getBillmaker());
		InvocationInfoProxy.getInstance().setUserId(resvo.getParentVO().getAuditer());
		resvo.getParentVO().setFbillstatus(2);
		Logger.info("签字新单据...");
		
		AggWrVO returnVO = this.getMaintainService().audit(new AggWrVO[] {
                (AggWrVO) resvo
            })[0];

		Logger.info("签字新单据完成...");

		Logger.info("签字新单据后处理...");

		return returnVO;
	}



	/**
	 * 更新BPM回写的生产订单信息到完工报告单
	 * 
	 * @param bill
	 * @param clientVO
	 * @throws BusinessException
	 */
	private void updateClientVO(AggWrVO bpmBill, AggWrVO clientVO)
			throws BusinessException {
		WrItemVO[] clientbodys = (WrItemVO[]) clientVO.getChildrenVO();
		WrItemVO[] bodys = (WrItemVO[]) bpmBill.getChildrenVO();
		// 设置表头信息
		String[] headKeys = bpmBill.getParentVO().getAttributeNames();
		for (String key : headKeys) {
			if(ValueCheckUtil.isEmpty(bpmBill.getParentVO().getAttributeValue(key))){
				continue;
			}
			clientVO.getParentVO().setAttributeValue(key,
					bpmBill.getParentVO().getAttributeValue(key));
		}
//		生产报告单据状态   1=自由，2=审批通过，3=提交，4=审批中，5=审批不通过， 
		clientVO.getParentVO().setFbillstatus(1);
		
		List<WrItemVO> children = new ArrayList<WrItemVO>();
		children.addAll(Arrays.asList(clientbodys));
		// 拆行的处理
		for (WrItemVO body : bodys) {
			String csourcebillbid = body.getCbmobid();
			for (WrItemVO clientbody : clientbodys) {
				if (!clientbody.getCbmobid().equalsIgnoreCase(
						csourcebillbid)) {
					continue;
				}
				// 一行表体，可能回写多个批次
				if (updateIndex.contains(csourcebillbid)) {
					WrItemVO newBody = (WrItemVO) clientbody.clone();
					updateClientBVO(body, newBody);
					children.add(newBody);
				} else {
					updateIndex.add(csourcebillbid);
					updateClientBVO(body, clientbody);
				}

			}
		}

		processBeforeSave(clientVO);

		WrItemVO[] new_bodys = children.toArray(new WrItemVO[0]);

		clientVO.setChildrenVO(new_bodys);
	}

	private void updateClientBVO(WrItemVO body, WrItemVO clientbody)
			throws BusinessException {
		String[] names = body.getAttributeNames();
		
		for(String name :names){
			if(ValueCheckUtil.isEmpty(body.getAttributeValue(name))){
				continue;
			}
			clientbody.setAttributeValue(name, body.getAttributeValue(name));
		}
		clientbody.setNbwrnum(getUFDdoubleNullASZero(body.getNbwrnum()).setScale(power,
				UFDouble.ROUND_HALF_UP));// 实发数量
		clientbody.setNbwrastnum(getUFDdoubleNullASZero(body.getNbwrastnum()).setScale(power,
				UFDouble.ROUND_HALF_UP));
	
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
	 * 查询生产订单
	 * 
	 * @param bill
	 * @return
	 * @throws BusinessException
	 */
	private PMOAggVO[] fillData(AggWrVO bill) throws BusinessException {
		// TODO 自动生成的方法存根
		// 可能存在合并完工报告的情况，合并入库，但是不会合并行明细
		List<String> arrPks = new ArrayList<String>();
		List<String> arrbpks = new ArrayList<String>();
		for (WrItemVO body : bill.getChildrenVO()) {
			if (!arrPks.contains(body.getCbmoid())) {
				arrPks.add(body.getCbmoid());
			}
			if (!arrbpks.contains(body.getCbmobid())) {
				arrbpks.add(body.getCbmobid());
			}
		}
		BillQuery<PMOAggVO> bquery = new BillQuery<>(PMOAggVO.class);
		PMOAggVO[] aggvos = bquery.query(arrPks.toArray(new String[0]));
		if (aggvos == null || aggvos.length == 0) {
			throw new BusinessException("根据指定的主键，" + arrPks.toString()
					+ "未查询到生产订单,请核对.");
		}
		// 来源的生产订单，可能存在合并入库，即多个生产订单对对应一个入库单
		List<PMOAggVO> chgBillS = new ArrayList<PMOAggVO>();
		for (PMOAggVO aggvo : aggvos) {
			List<PMOItemVO> arrblist = new ArrayList<PMOItemVO>();
			PMOItemVO[] itemVOs = aggvo.getChildrenVO();
			for (PMOItemVO itemVO : itemVOs) {
				if (arrbpks.contains(itemVO.getPrimaryKey())) {
					arrblist.add(itemVO);
				}
			}
			if (arrblist.size() > 0) {
				PMOAggVO chgArrBill = new PMOAggVO();
				chgArrBill.setParentVO(aggvo.getParentVO());
				chgArrBill.setChildrenVO(arrblist
						.toArray(new PMOItemVO[0]));
				chgBillS.add(chgArrBill);
			}
		}

		//
		if (chgBillS.size() == 0) {
			throw new BusinessException(
					"根据指定的主键Csourcebillhid可以查询到生产订单，但是根据Csourcebillbid未查询到生产订单明细,请核对数据是否正确.");
		}

		return chgBillS.toArray(new PMOAggVO[0]);

	}

	private void checkData(AggWrVO bill) throws BusinessException {
		// TODO 自动生成的方法存根
		WrItemVO[] PMOItemVOs = bill.getChildrenVO();
		if (PMOItemVOs == null || PMOItemVOs.length == 0) {
			throw new BusinessException("请指定需要出库的表体行.");
		}
		VOCheckUtil
				.checkHeadNotNullFields(bill, new String[] { "dbilldate",
						"billmaker","auditer"});
//		VOCheckUtil.checkBodyNotNullFields(bill, new String[] { "cbmoid",
//				"cbmobid", "vbinbatchcode", "nbwrnum" });

		VOCheckUtil.checkBodyNotNullFields(bill, new String[] {"fbproducttype", "vbinbatchcode", "nbwrnum" });
	}



	/**
	 * 单据保存前处理
	 * 
	 * @param vo
	 */
	protected void processBeforeSave(AggWrVO vo) throws BusinessException {

		if (null == vo)
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0139")/*
																			 * @res
																			 * "单据不能为空"
																			 */);
		ICBSContext context = new ICBSContext();
		this.headVOProcess(vo.getParentVO(), context);
		this.bodyVOProcess(vo, context);
	}

	/**
	 * 单据保存后处理
	 * 
	 * @param vo
	 */
	protected void processAfterSave(AggWrVO vo) throws BusinessException {
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
	private void headVOProcess(WrVO vo, ICBSContext context) {
		vo.setStatus(VOStatus.NEW);
		// 集团
		if (StringUtil.isSEmptyOrNull(vo.getPk_group()))
			vo.setPk_group(context.getPk_group());
	
		// 单据日期
		if (vo.getDbilldate() == null)
			vo.setDbilldate(context.getBizDate());
		vo.setDmakedate(vo.getDbilldate());
		// 创建时间
		vo.setCreationtime(new UFDateTime(vo.getDbilldate().toString()));
		vo.setCreator(vo.getBillmaker());
		//
		

		if (StringUtil.isSEmptyOrNull(vo.getVtrantypeid())) {
			// uap不支持单据类型的翻译，暂时以交易类型code查询id的方式补交易类型
			String vtrantypecode = vo.getVtrantypecode();
			Map<String, String> map = PfServiceScmUtil
					.getTrantypeidByCode(new String[] { vtrantypecode });
			vo.setVtrantypeid(map == null ? null : map.get(vtrantypecode));
		}

	}

	/**
	 * 单据表体处理
	 * 
	 * @param vo
	 * @param context
	 * @throws BusinessException
	 */
	private void bodyVOProcess(AggWrVO vo, ICBSContext context)
			throws BusinessException {
		WrItemVO[] vos = vo.getChildrenVO();
		if (ValueCheckUtil.isNullORZeroLength(vos))
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0141")/*
																			 * @res
																			 * "单据表体不能为空"
																			 */);

		VORowNoUtils.setVOsRowNoByRule(vos, WrItemVO.VBROWNO);// 行号处理

		WrVO head = vo.getParentVO();
		Map<String, BatchcodeVO> batchmap = this.getBatchcodeVO(vos);
		for (WrItemVO body : vos) {
			body.setStatus(VOStatus.NEW);
		// 有批次号但无批次主键时， 需要补全批次主键，有必要时(保质期管理)补全生产日期和失效日期
			if (!StringUtils.isEmpty(body.getVbinbatchcode())
					&& StringUtils.isEmpty(body.getVbinbatchid())) {
				BatchcodeVO batchvo = batchmap.get(body.getCbmainmaterialid()
						+ body.getVbinbatchcode());
				if (batchvo != null) {
					body.setVbinbatchid(batchvo.getPk_batchcode());
				}
			}
			bodyVOCopyFromHeadVO(body, head);
		}
	}

	/**
	 * 获取批次号档案
	 * 
	 * @param vos
	 * @return Map<String(cmaterialvid+vbatchcode), BatchcodeVO批次档案>
	 */
	private Map<String, BatchcodeVO> getBatchcodeVO(WrItemVO[] vos) {
		List<String> cmaterialvidList = new ArrayList<String>();
		List<String> vbatchcodeList = new ArrayList<String>();
		Set<String> materialbatch = new HashSet<String>();
		for (WrItemVO body : vos) {
			if (body.getCbmainmaterialid() != null && body.getVbinbatchcode() != null) {
				if (materialbatch.contains(body.getCbmainmaterialid()
						+ body.getVbinbatchcode())) {
					continue;
				}
				cmaterialvidList.add(body.getCbmainmaterialid());
				vbatchcodeList.add(body.getVbinbatchcode());
				materialbatch
						.add(body.getCbmainmaterialid() + body.getVbinbatchcode());
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
	private void bodyVOCopyFromHeadVO(WrItemVO body, WrVO head) {
		body.setPk_group(head.getPk_group());
		body.setPk_org(head.getPk_org());
		body.setPk_org_v(head.getPk_org_v());
		
	}

}