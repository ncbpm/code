package nc.bpm.pu.pfxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.env.ICBSContext;
import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.data.view.ViewQuery;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.pf.IPFBusiAction;
import nc.pubitf.org.cache.IBasicOrgUnitPubService_C;
import nc.pubitf.scmf.ic.mbatchcode.IBatchcodePubService;
import nc.vo.ic.batch.BatchRefViewVO;
import nc.vo.ic.batchcode.BatchSynchronizer;
import nc.vo.ic.batchcode.ICNewBatchFields;
import nc.vo.ic.general.define.ICBillFlag;
import nc.vo.ic.m4n.entity.TransformBodyVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.pub.calc.BusiCalculator;
import nc.vo.ic.pub.define.IBizObject;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.special.define.ICSpecialBodyEntity;
import nc.vo.ic.special.define.ICSpecialHeadEntity;
import nc.vo.ic.special.define.ICSpecialVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.calculator.HslParseUtil;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;

import org.apache.commons.lang.StringUtils;

/**
 * 生成形态转换单
 * 
 * @author Administrator
 * 
 */
public class MarksForBpmTrans {

	private List<String> updateIndex = new ArrayList<String>();
	private int power = 2;// 精度

	/**
	 * 保存形态转换单
	 * 
	 * @param vo
	 * @param swapContext
	 * @param aggvo
	 * @return
	 * @throws BusinessException
	 */
	protected Object save(Object vo) throws BusinessException {
		// TODO 自动生成的方法存根
		ICSpecialVO bpmBill = (ICSpecialVO) vo;
		TransformBodyVO[] bodys = (TransformBodyVO[]) bpmBill.getBodys();
		// 数量
		BusiCalculator.getBusiCalculatorAtBS().calcNum(bodys,
				ICPubMetaNameConst.NNUM);
		// 计算金额
		// BusiCalculator.getBusiCalculatorAtBS().calcOnlyMny(
		// new ICSpecialVO[] { bpmBill }, ICPubMetaNameConst.NNUM);
		// 保存
		ICSpecialVO saveVO = doSave(bpmBill);
		// 审批
		// doSign(saveVO);

		return saveVO;

	}

	/**
	 * 新增
	 * 
	 * @param swapContext
	 * @param icbill
	 * @return
	 * @throws BusinessException
	 */
	private ICSpecialVO doSave(ICSpecialVO icbill) throws BusinessException {
		// 检查是否允许保存
		Logger.info("保存新单据前处理...");
		this.processBeforeSave(icbill);

		// TODO 单据设置有辅助信息，aggxsysvo为用户配置的具体辅助信息
		InvocationInfoProxy.getInstance().setUserId(
				icbill.getHead().getCreator());

		Logger.info("保存新单据...");
		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		ICSpecialVO[] icbills = (ICSpecialVO[]) service.processAction(
				IPFActionName.WRITE, "4N", null, icbill, null, null);

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
	private ICSpecialVO doSign(ICSpecialVO icbill) throws BusinessException {
		// 检查是否允许保存
		Logger.info("签字新单据前处理...");
		// 签字时间等于单据日期
		icbill.getHead().setTaudittime(icbill.getHead().getDbilldate());
		InvocationInfoProxy.getInstance().setUserId(
				icbill.getHead().getApprover());
		Logger.info("签字新单据...");
		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		ICSpecialVO[] icbills = (ICSpecialVO[]) service.processAction("SIGN",
				"4N", null, icbill, null, null);

		Logger.info("签字新单据完成...");

		Logger.info("签字新单据后处理...");

		return icbills[0];
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
	 * 单据保存前处理
	 * 
	 * @param vo
	 */
	protected void processBeforeSave(ICSpecialVO vo) throws BusinessException {

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
	protected void processAfterSave(ICSpecialVO vo) throws BusinessException {
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
	private void headVOProcess(ICSpecialHeadEntity vo, ICBSContext context) {
		vo.setStatus(VOStatus.NEW);
		// 组织版本
		if (StringUtil.isSEmptyOrNull(vo.getPk_org_v()))
			vo.setPk_org_v(getPkOrg_v(vo.getPk_org()));
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

	public String getPkOrg_v(String pk_org) {
		// 组织的最新版本
		String pk_org_v = null;
		Map<String, String> map;
		UFDate busiDate = AppContext.getInstance().getBusiDate();
		try {
			map = NCLocator
					.getInstance()
					.lookup(IBasicOrgUnitPubService_C.class)
					.getNewVIDSByOrgIDSAndDate(new String[] { pk_org },
							busiDate);
			pk_org_v = map.get(pk_org);

		} catch (BusinessException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		return pk_org_v;
	}

	/**
	 * 单据表体处理
	 * 
	 * @param vo
	 * @param context
	 * @throws BusinessException
	 */
	private void bodyVOProcess(ICSpecialVO vo, ICBSContext context)
			throws BusinessException {
		TransformBodyVO[] vos = (TransformBodyVO[]) vo.getBodys();
		if (ValueCheckUtil.isNullORZeroLength(vos))
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0141")/*
																			 * @res
																			 * "单据表体不能为空"
																			 */);

		VORowNoUtils.setVOsRowNoByRule(vos, ICPubMetaNameConst.CROWNO);// 行号处理
		ICSpecialHeadEntity head = vo.getHead();

		for (ICSpecialBodyEntity body : vos) {
			body.setStatus(VOStatus.NEW);
			if (StringUtil.isSEmptyOrNull(body.getCmaterialoid())
					|| StringUtil.isSEmptyOrNull(body.getCmaterialvid()))
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("4008001_0", "04008001-0142")/*
																 * @res
																 * "单据表体物料不能为空"
																 */);
			bodyVOCopyFromHeadVO(body, head);
			// 辅单位
			if (StringUtil.isSEmptyOrNull(body.getCastunitid()))
				body.setCastunitid(context.getInvInfo()
						.getInvBasVO(body.getCmaterialvid()).getPk_stockmeas());

			// 有批次号但无批次主键时， 需要补全批次主键，有必要时(保质期管理)补全生产日期和失效日期
			if (!StringUtils.isEmpty(body.getVbatchcode())
					&& StringUtils.isEmpty(body.getPk_batchcode())) {
			
				//同步的维度信息
				BatchCodeRule batchCodeRule = new BatchCodeRule();
				BatchRefViewVO batchRefViewVOs = batchCodeRule.getRefVO(body.getPk_org(),body.getCbodywarehouseid(),body.getCmaterialvid(), body.getVbatchcode());
				if (batchRefViewVOs != null ) {
					batchCodeRule.synBatch(batchRefViewVOs, body);
				}
				
			}
		
		}

	}

	

	/**
	 * 根据表头设置表体默认值，表体集团，库存组织，公司，仓库，交易类型
	 * 
	 * @param body
	 * @param head
	 */
	private void bodyVOCopyFromHeadVO(ICSpecialBodyEntity body,
			ICSpecialHeadEntity head) {
		body.setPk_group(head.getPk_group());
		body.setPk_org(head.getPk_org());
		body.setPk_org_v(head.getPk_org_v());
		body.setCorpoid(head.getCorpoid());
		body.setCorpvid(head.getCorpvid());
		body.setCbodywarehouseid(head.getCwarehouseid());
	}

}
