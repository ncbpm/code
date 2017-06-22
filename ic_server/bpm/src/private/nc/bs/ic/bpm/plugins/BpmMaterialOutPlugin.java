package nc.bs.ic.bpm.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.ic.general.plugins.CheckMnyUtil;
import nc.bs.ic.general.plugins.CheckScaleUtil;
import nc.bs.ic.m4d.bpm.BpmServicePluginPoint;
import nc.bs.ic.pub.env.ICBSContext;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.pub.pf.PfUtilTools;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.pf.busiflow.PfButtonClickContext;
import nc.pubimpl.ic.m4d.m422x.action.PushSaveActionFor422X;
import nc.pubitf.scmf.ic.mbatchcode.IBatchcodePubService;
import nc.util.mmf.busi.service.PFPubService;
import nc.vo.ic.general.deal.ICBillValueSetter;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillFlag;
import nc.vo.ic.general.define.ICBillHeadVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.general.util.BasDocQueryUtil;
import nc.vo.ic.general.util.BillVOUtil;
import nc.vo.ic.m4d.entity.MaterialOutVO;
import nc.vo.ic.m4k.entity.WhsTransBillVO;
import nc.vo.ic.material.deal.UnitAndHslProc;
import nc.vo.ic.material.define.InvBasVO;
import nc.vo.ic.material.query.InvInfoQuery;
import nc.vo.ic.org.OrgInfoQuery;
import nc.vo.ic.param.ICSysParam;
import nc.vo.ic.pub.calc.BusiCalculator;
import nc.vo.ic.pub.calc.PriceAndMoneyCalculator;
import nc.vo.ic.pub.calc.WeightAndVolumeCalc;
import nc.vo.ic.pub.define.ICContext;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.mmpac.pacpub.consts.MMPacBillTypeConstant;
import nc.vo.mmpac.reqpickm.consts.ReqPickmConsts;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.scale.ScaleUtils;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;

import org.apache.commons.lang.StringUtils;

public class BpmMaterialOutPlugin extends AbstractPfxxPlugin {

	private BusiCalculator busiCalc;

	private ICContext context;

	private ICSysParam icSysParam;

	private InvInfoQuery invQuery;
	// 物料基本信息
	private Map<String, InvBasVO> mapInvBase;

	private ScaleUtils scale;

	private OrgInfoQuery orgInfoQry;

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggxsysvo) throws BusinessException {
		ICBillVO[] icbills = null;
		try {
			MaterialOutVO[] mvos = (MaterialOutVO[]) PFPubService
					.runChangeData(MMPacBillTypeConstant.WRCHANGE_BILLTYPE,
							MMPacBillTypeConstant.BACKFLUSH_DEPOSIT_BILLTYPE,
							null, null, PfButtonClickContext.ClassifyByItfdef);

			WhsTransBillVO[] targetVOs;

			targetVOs = (WhsTransBillVO[]) PfUtilTools.runChangeDataAry(
					ReqPickmConsts.BILL_CODE, ReqPickmConsts.FORWARD_BILL_4D,
					null);

			if (vo == null)
				throw new BusinessException("转换后的vo数据为空");
			ICBSContext context = new ICBSContext();
			this.context = context;
			invQuery = context.getInvInfo();
			icSysParam = context.getICSysParam();
			orgInfoQry = context.getOrgInfo();
			scale = new ScaleUtils(swapContext.getPk_group());
			busiCalc = BusiCalculator.getBusiCalculatorAtBS();
			ICBillVO icbill = (ICBillVO) vo;

			icbills = this.doSave(swapContext, icbill);

		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		return icbills[0].getHead().getCgeneralhid();
	}

	/**
	 * 新增
	 * 
	 * @param swapContext
	 * @param icbill
	 * @return
	 * @throws BusinessException
	 */
	private ICBillVO[] doSave(ISwapContext swapContext, ICBillVO icbill)
			throws BusinessException {
		Logger.info("保存新单据前处理...");
		this.processBeforeSave(icbill);
		// 检查是否允许保存
		this.checkCanInster(icbill);

		// TODO 单据设置有辅助信息，aggxsysvo为用户配置的具体辅助信息
		MaterialOutVO[] bills = new MaterialOutVO[] { (MaterialOutVO) icbill };
		Logger.info("保存新单据...");
		MaterialOutVO[] vos = new PushSaveActionFor422X(
				BpmServicePluginPoint.pushSaveFor422X).pushSaveAndSign(bills);

		return vos;
	}

	/**
	 * 单据保存前处理
	 * 
	 * @param vo
	 */
	protected void processBeforeSave(ICBillVO vo) throws BusinessException {

		MaterialOutVO[] vos = new MaterialOutVO[] { (MaterialOutVO) vo };
		this.checkBillVOsFromOutSys(vos);
		// 加载一些基本信息
		this.loadBaseInfo(vos);
		// 补值
		this.fillBillValues(vos);

		// 处理辅单位及换算率
		this.processVchangerateAndAssUnit(vos);
		// 处理应数量
		this.processShouldNum(vos);

		// 处理实收发数量
		this.processNumAndMny(vos);
	}

	/**
	 * 获取批次号档案
	 * 
	 * @param vos
	 * @return Map<String(vbatchcode), BatchcodeVO批次档案>
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
	 * 检验vo是否可更新
	 * 
	 * @param vo
	 */
	protected void checkCanInster(AggregatedValueObject vo) {
		this.checkBillFlag(vo);
		new CheckMnyUtil().checkMny(vo);
		new CheckScaleUtil().checkScale(vo);
	}

	/**
	 * 检验vo状态
	 * 
	 * @param vo
	 */
	private void checkBillFlag(AggregatedValueObject vo) {
		if (!Integer.valueOf(ICBillFlag.getFreeFlag()).equals(
				vo.getParentVO().getAttributeValue(this.getBillStatusKey()))) {
			ExceptionUtils
					.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("4008001_0",
									"04008001-0816")/*
													 * @res "非自由态的单据不允许导入！"
													 */);
		}
	}

	protected String getBillStatusKey() {
		return ICPubMetaNameConst.FBILLFLAG;
	}

	/**
	 * 对外系统来的单据的一些基本检查，避免后续无谓的处理， 当然这些并不和单据本身的检查规则矛盾
	 */
	@SuppressWarnings("unchecked")
	protected void checkBillVOsFromOutSys(ICBillVO[] vos)
			throws BusinessException {
		if (ValueCheckUtil.isNullORZeroLength(vos)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0273")/*
																			 * @res
																			 * "单据不应当是空"
																			 */);
		}
		for (ICBillVO vo : vos) {
			this.checkBillHeadFromOutSys((ICBillHeadVO) vo.getHead());
			this.checkBillBodyFromOutSys((ICBillBodyVO[]) vo.getBodys());
		}
	}

	/**
	 * 对外系统来单据的表头一些基本检查
	 */
	protected void checkBillBodyFromOutSys(ICBillBodyVO[] bodys)
			throws BusinessException {
		if (ValueCheckUtil.isNullORZeroLength(bodys)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0271")/*
																			 * @res
																			 * "单据表体不应当是空"
																			 */);
		}
	}

	/**
	 * 对外系统来单据的表头一些基本检查
	 */
	protected void checkBillHeadFromOutSys(ICBillHeadVO head)
			throws BusinessException {
		if (head == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0272")/*
																			 * @res
																			 * "单据表头不应当是空"
																			 */);
		}
	}

	/**
	 * 加载基本信息
	 */
	protected void loadBaseInfo(ICBillVO[] vos) {
		this.mapInvBase = new BasDocQueryUtil(this.invQuery)
				.fetchInvBasVOs(vos);
	}

	/**
	 * 补一些外系统没有，而库存单据本身处理需要的值: 公司，交易类型
	 */
	protected void fillBillValues(ICBillVO[] vos) {
		ICBillValueSetter valueset = new ICBillValueSetter();
		// 取最新版本的库存组织vid
		String[] calbodyoids = VOEntityUtil.getVOsValues(
				BillVOUtil.getHeads(vos), ICPubMetaNameConst.PK_ORG,
				String.class);
		String[] calbodyvids = this.getOrgInfoQry().getCalBodyVids(calbodyoids);
		for (int i = 0; i < vos.length; i++) {
			// 设置单据默认值
			vos[i].getHead().setStatus(VOStatus.NEW);
			vos[i].getHead().setPk_org_v(calbodyvids[i]);
			vos[i].getHead().setCorpoid(
					this.getOrgInfoQry().getCorpIDByCalBodyID(calbodyoids[i]));
			vos[i].getHead().setCorpvid(
					this.getOrgInfoQry().getCorpVIDByCalBodyID(calbodyoids[i]));

			// 设置行号
			ICBillBodyVO[] bodys = vos[i].getBodys();
			// 按来源单据行号排序
			// VOSortUtils.ascSort(bodys, new String[] {
			// MetaNameConst.VSOURCEROWNO
			// });
			VORowNoUtils.setVOsRowNoByRule(bodys, ICPubMetaNameConst.CROWNO);
			for (ICBillBodyVO body : bodys) {
				// 根据应收发填写实收发
				// if (this.isFillNum) {
				this.fillNum(body);
				// }
				body.setStatus(VOStatus.NEW);
				if (mapInvBase != null && mapInvBase.size() > 0) {
					InvBasVO invvo = mapInvBase.get(body.getCmaterialvid());
					if (invvo != null)
						body.setCmaterialoid(invvo.getPk_material());
				}
				if (body.getNnum() != null && body.getNassistnum() != null
						&& body.getDbizdate() == null)
					body.setDbizdate(context.getBizDate());// 业务日期
				body.setBbarcodeclose(UFBoolean.FALSE);// 单据行是否条码关闭
				body.setBonroadflag(UFBoolean.FALSE);// 是否在途
			}
			setBatchCodeInfo(bodys);
			// 设置库管员
			this.setCwhsmanageridForSpilt(vos[i]);
			valueset.setBillInitData(vos[i], this.getContext());
		}
		this.fillTranstypeCode(vos);
	}

	private void setBatchCodeInfo(ICBillBodyVO[] vos) {
		Map<String, BatchcodeVO> batchmap = this.getBatchcodeVO(vos);
		for (ICBillBodyVO body : vos) {

			// 有批次号但无批次主键时， 需要补全批次主键，有必要时(保质期管理)补全生产日期和失效日期
			if (!StringUtils.isEmpty(body.getVbatchcode())
					&& StringUtils.isEmpty(body.getPk_batchcode())) {
				BatchcodeVO batchvo = batchmap.get(body.getCmaterialvid()
						+ body.getVbatchcode());
				if (batchvo != null) {
					body.setPk_batchcode(batchvo.getPk_batchcode());
					body.setDproducedate(batchvo.getDproducedate());
					body.setDvalidate(batchvo.getDvalidate());
				}
			}
		}
	}

	/**
	 * 补交易类型
	 */
	protected void fillTranstypeCode(ICBillVO[] vos) {

		for (ICBillVO vo : vos) {
			String vtrantypecode = "4D-01";
			vo.getHead().setVtrantypecode(vtrantypecode);

			Map<String, String> map = PfServiceScmUtil
					.getTrantypeidByCode(new String[] { vtrantypecode });
			vo.getHead().setCtrantypeid(
					map == null ? null : map.get(vtrantypecode));
		}

	}

	/**
	 * 如果设置了按照库管员分单，则按照库管员物料的配置设置表头库管员
	 * 
	 * @param vo
	 */
	private void setCwhsmanageridForSpilt(ICBillVO vo) {
		// 由于该逻辑只有后台调用，并且需要用到后台Session，前台调用会报错，但是该类在前台
		// 和后台都要调用，因此在这里增加一个判断逻辑，避免在前台时进行调用报错。
		if (!RuntimeEnv.getInstance().isRunningInServer()) {
			return;
		}

		Map<String, String> map = new ICBSContext().getICStoreAdminSpilt();
		if (map != null && vo.getHead().getCwhsmanagerid() == null) {
			ICBillHeadVO head = vo.getHead();
			String key = head.getPk_org() + head.getCwarehouseid();
			for (ICBillBodyVO body : vo.getBodys()) {
				String cwhsmanagerid = map.get(key + body.getCmaterialoid());
				if (cwhsmanagerid != null) {
					head.setCwhsmanagerid(cwhsmanagerid);
					break;
				}
			}
		}
	}

	/**
	 * @return orgInfoQry
	 */
	public OrgInfoQuery getOrgInfoQry() {
		return this.orgInfoQry;
	}

	/**
	 * @return context
	 */
	public ICContext getContext() {
		return this.context;
	}

	/**
	 * 填充实发数量
	 */
	protected void fillNum(ICBillBodyVO body) {
		body.setNnum(body.getNshouldnum());
		if (body.getNshouldassistnum() != null) {
			body.setNassistnum(body.getNshouldassistnum());
		}
	}

	protected void processVchangerateAndAssUnit(ICBillVO[] vos) {

		ICBillBodyVO[] bodys = null;
		UnitAndHslProc proc = new UnitAndHslProc(this.icSysParam, this.invQuery);
		for (ICBillVO vo : vos) {
			proc.procCastunitAndHsl(vo.getHead().getPk_org(), vo.getBodys());
			bodys = vo.getBodys();
			if (ValueCheckUtil.isNullORZeroLength(bodys)) {
				continue;
			}
			for (ICBillBodyVO body : bodys) {
				body.setVchangerate(this.getScale().adjustHslScale(
						body.getVchangerate()));

			}
		}
		this.setHslByCastunit(vos, proc);

	}

	private void setHslByCastunit(ICBillVO[] vos, UnitAndHslProc proc) {
		if (ValueCheckUtil.isNullORZeroLength(vos))
			return;
		for (int i = 0; i < vos.length; i++) {
			if (ValueCheckUtil.isNullORZeroLength(vos[i].getBodys())) {
				continue;
			}
			List<ICBillBodyVO> changedBodys = new ArrayList<ICBillBodyVO>();
			for (int j = 0; j < vos[i].getBodys().length; j++) {
				ICBillBodyVO body = vos[i].getBody(j);
				// if(this.isEqualsOrigin(body, ICPubMetaNameConst.CASTUNITID)){
				// continue;
				// }
				body.setVchangerate(null);
				changedBodys.add(body);
			}
			if (!ValueCheckUtil.isNullORZeroLength(changedBodys)) {
				proc.procVchangerate(CollectionUtils.listToArray(changedBodys));
			}
		}
	}

	protected void processShouldNum(ICBillVO[] vos) {
		// 计算应收发业务数量
		this.busiCalc.calcShouldAstNum(this.getBodyToCalShould(vos),
				ICPubMetaNameConst.NSHOULDNUM);
	}

	private ICBillBodyVO[] getBodyToCalShould(ICBillVO[] vos) {
		List<ICBillBodyVO> retList = new ArrayList<ICBillBodyVO>();
		for (int i = 0; i < vos.length; i++) {
			for (int j = 0; j < vos[i].getBodys().length; j++) {
				ICBillBodyVO body = vos[i].getBody(j);
				if (body.getNshouldassistnum() == null) {
					//
					retList.add(body);
				}
			}
		}
		return CollectionUtils.listToArray(retList);

	}

	/**
	 * 处理库存实收发数量，同时处理业务数量,金额,体积
	 */
	protected void processNumAndMny(ICBillVO[] vos) {
		List<ICBillBodyVO> lnumbodys = new ArrayList<ICBillBodyVO>();
		List<ICBillBodyVO> lcalcnumbodys = new ArrayList<ICBillBodyVO>();
		for (ICBillVO vo : vos) {
			for (ICBillBodyVO body : vo.getBodys()) {
				if (body.getNnum() == null) {
					continue;
				}
				lnumbodys.add(body);
				// if(this.isCalcNum(body)){
				lcalcnumbodys.add(body);
				// }
			}
		}

		ICBillBodyVO[] numbodyvos = lnumbodys.size() <= 0 ? null
				: CollectionUtils.listToArray(lnumbodys);

		if (ValueCheckUtil.isNullORZeroLength(numbodyvos)) {
			return;
		}

		if (lcalcnumbodys.size() > 0) {
			this.calculateNum(CollectionUtils.listToArray(lcalcnumbodys));
		}

		// 计算金额
		this.calculateMny(vos);

		// 需要同时金额，如果有单价
		PriceAndMoneyCalculator pcalc = new PriceAndMoneyCalculator(
				this.getScale(), this.orgInfoQry);
		for (ICBillBodyVO bvo : numbodyvos) {
			pcalc.calcPriceMny(bvo.getPk_org(), new ICBillBodyVO[] { bvo },
					PriceAndMoneyCalculator.MnyCalcType.Num);
		}
		WeightAndVolumeCalc wcalc = new WeightAndVolumeCalc(this.getScale());
		wcalc.calculator(numbodyvos, this.invQuery);
	}

	/**
	 * 计算单价和金额
	 */
	protected void calculateMnyAndNum(ICBillBodyVO[] bodys) {
		this.busiCalc.calc(bodys, ICPubMetaNameConst.VCHANGERATE);

	}

	/**
	 * 计算单价和金额
	 */
	protected void calculateMnyAndNum(ICBillVO[] vos) {
		this.calculateMnyAndNum(this.getBodyWithNums(vos));
	}

	/**
	 * 计算金额
	 */
	protected void calculateMny(ICBillBodyVO[] bodys) {
		this.busiCalc.calcMny(bodys, ICPubMetaNameConst.NNUM);
	}

	/**
	 * 计算金额
	 */
	protected void calculateMny(ICBillVO[] vos) {
		this.busiCalc.calcMny(vos, ICPubMetaNameConst.NNUM);
	}

	/**
	 * 计算数量
	 */
	protected void calculateNum(ICBillBodyVO[] bodys) {
		this.busiCalc.calcNum(bodys, ICPubMetaNameConst.VCHANGERATE);

	}

	/**
	 * 计算数量
	 */
	protected void calculateNum(ICBillVO[] vos) {
		this.calculateNum(this.getBodyWithNums(vos));
	}

	private ICBillBodyVO[] getBodyWithNums(ICBillVO[] vos) {
		List<ICBillBodyVO> retList = new ArrayList<ICBillBodyVO>();
		for (ICBillVO vo : vos) {
			if (ValueCheckUtil.isNullORZeroLength(vo.getBodys())) {
				return new ICBillBodyVO[0];
			}
			for (ICBillBodyVO body : vo.getBodys()) {
				if (body.getNnum() == null)
					continue;
				retList.add(body);
			}
		}
		return retList.toArray(new ICBillBodyVO[0]);
	}

	/**
	 * @return scale
	 */
	public ScaleUtils getScale() {
		return this.scale;
	}
}