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
	// ���ϻ�����Ϣ
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
				throw new BusinessException("ת�����vo����Ϊ��");
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
	 * ����
	 * 
	 * @param swapContext
	 * @param icbill
	 * @return
	 * @throws BusinessException
	 */
	private ICBillVO[] doSave(ISwapContext swapContext, ICBillVO icbill)
			throws BusinessException {
		Logger.info("�����µ���ǰ����...");
		this.processBeforeSave(icbill);
		// ����Ƿ�������
		this.checkCanInster(icbill);

		// TODO ���������и�����Ϣ��aggxsysvoΪ�û����õľ��帨����Ϣ
		MaterialOutVO[] bills = new MaterialOutVO[] { (MaterialOutVO) icbill };
		Logger.info("�����µ���...");
		MaterialOutVO[] vos = new PushSaveActionFor422X(
				BpmServicePluginPoint.pushSaveFor422X).pushSaveAndSign(bills);

		return vos;
	}

	/**
	 * ���ݱ���ǰ����
	 * 
	 * @param vo
	 */
	protected void processBeforeSave(ICBillVO vo) throws BusinessException {

		MaterialOutVO[] vos = new MaterialOutVO[] { (MaterialOutVO) vo };
		this.checkBillVOsFromOutSys(vos);
		// ����һЩ������Ϣ
		this.loadBaseInfo(vos);
		// ��ֵ
		this.fillBillValues(vos);

		// ������λ��������
		this.processVchangerateAndAssUnit(vos);
		// ����Ӧ����
		this.processShouldNum(vos);

		// ����ʵ�շ�����
		this.processNumAndMny(vos);
	}

	/**
	 * ��ȡ���κŵ���
	 * 
	 * @param vos
	 * @return Map<String(vbatchcode), BatchcodeVO���ε���>
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
	 * ����vo�Ƿ�ɸ���
	 * 
	 * @param vo
	 */
	protected void checkCanInster(AggregatedValueObject vo) {
		this.checkBillFlag(vo);
		new CheckMnyUtil().checkMny(vo);
		new CheckScaleUtil().checkScale(vo);
	}

	/**
	 * ����vo״̬
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
													 * @res "������̬�ĵ��ݲ������룡"
													 */);
		}
	}

	protected String getBillStatusKey() {
		return ICPubMetaNameConst.FBILLFLAG;
	}

	/**
	 * ����ϵͳ���ĵ��ݵ�һЩ������飬���������ν�Ĵ��� ��Ȼ��Щ�����͵��ݱ���ļ�����ì��
	 */
	@SuppressWarnings("unchecked")
	protected void checkBillVOsFromOutSys(ICBillVO[] vos)
			throws BusinessException {
		if (ValueCheckUtil.isNullORZeroLength(vos)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0273")/*
																			 * @res
																			 * "���ݲ�Ӧ���ǿ�"
																			 */);
		}
		for (ICBillVO vo : vos) {
			this.checkBillHeadFromOutSys((ICBillHeadVO) vo.getHead());
			this.checkBillBodyFromOutSys((ICBillBodyVO[]) vo.getBodys());
		}
	}

	/**
	 * ����ϵͳ�����ݵı�ͷһЩ�������
	 */
	protected void checkBillBodyFromOutSys(ICBillBodyVO[] bodys)
			throws BusinessException {
		if (ValueCheckUtil.isNullORZeroLength(bodys)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0271")/*
																			 * @res
																			 * "���ݱ��岻Ӧ���ǿ�"
																			 */);
		}
	}

	/**
	 * ����ϵͳ�����ݵı�ͷһЩ�������
	 */
	protected void checkBillHeadFromOutSys(ICBillHeadVO head)
			throws BusinessException {
		if (head == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0272")/*
																			 * @res
																			 * "���ݱ�ͷ��Ӧ���ǿ�"
																			 */);
		}
	}

	/**
	 * ���ػ�����Ϣ
	 */
	protected void loadBaseInfo(ICBillVO[] vos) {
		this.mapInvBase = new BasDocQueryUtil(this.invQuery)
				.fetchInvBasVOs(vos);
	}

	/**
	 * ��һЩ��ϵͳû�У�����浥�ݱ�������Ҫ��ֵ: ��˾����������
	 */
	protected void fillBillValues(ICBillVO[] vos) {
		ICBillValueSetter valueset = new ICBillValueSetter();
		// ȡ���°汾�Ŀ����֯vid
		String[] calbodyoids = VOEntityUtil.getVOsValues(
				BillVOUtil.getHeads(vos), ICPubMetaNameConst.PK_ORG,
				String.class);
		String[] calbodyvids = this.getOrgInfoQry().getCalBodyVids(calbodyoids);
		for (int i = 0; i < vos.length; i++) {
			// ���õ���Ĭ��ֵ
			vos[i].getHead().setStatus(VOStatus.NEW);
			vos[i].getHead().setPk_org_v(calbodyvids[i]);
			vos[i].getHead().setCorpoid(
					this.getOrgInfoQry().getCorpIDByCalBodyID(calbodyoids[i]));
			vos[i].getHead().setCorpvid(
					this.getOrgInfoQry().getCorpVIDByCalBodyID(calbodyoids[i]));

			// �����к�
			ICBillBodyVO[] bodys = vos[i].getBodys();
			// ����Դ�����к�����
			// VOSortUtils.ascSort(bodys, new String[] {
			// MetaNameConst.VSOURCEROWNO
			// });
			VORowNoUtils.setVOsRowNoByRule(bodys, ICPubMetaNameConst.CROWNO);
			for (ICBillBodyVO body : bodys) {
				// ����Ӧ�շ���дʵ�շ�
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
					body.setDbizdate(context.getBizDate());// ҵ������
				body.setBbarcodeclose(UFBoolean.FALSE);// �������Ƿ�����ر�
				body.setBonroadflag(UFBoolean.FALSE);// �Ƿ���;
			}
			setBatchCodeInfo(bodys);
			// ���ÿ��Ա
			this.setCwhsmanageridForSpilt(vos[i]);
			valueset.setBillInitData(vos[i], this.getContext());
		}
		this.fillTranstypeCode(vos);
	}

	private void setBatchCodeInfo(ICBillBodyVO[] vos) {
		Map<String, BatchcodeVO> batchmap = this.getBatchcodeVO(vos);
		for (ICBillBodyVO body : vos) {

			// �����κŵ�����������ʱ�� ��Ҫ��ȫ�����������б�Ҫʱ(�����ڹ���)��ȫ�������ں�ʧЧ����
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
	 * ����������
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
	 * ��������˰��տ��Ա�ֵ������տ��Ա���ϵ��������ñ�ͷ���Ա
	 * 
	 * @param vo
	 */
	private void setCwhsmanageridForSpilt(ICBillVO vo) {
		// ���ڸ��߼�ֻ�к�̨���ã�������Ҫ�õ���̨Session��ǰ̨���ûᱨ�����Ǹ�����ǰ̨
		// �ͺ�̨��Ҫ���ã��������������һ���ж��߼���������ǰ̨ʱ���е��ñ���
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
	 * ���ʵ������
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
		// ����Ӧ�շ�ҵ������
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
	 * ������ʵ�շ�������ͬʱ����ҵ������,���,���
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

		// ������
		this.calculateMny(vos);

		// ��Ҫͬʱ������е���
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
	 * ���㵥�ۺͽ��
	 */
	protected void calculateMnyAndNum(ICBillBodyVO[] bodys) {
		this.busiCalc.calc(bodys, ICPubMetaNameConst.VCHANGERATE);

	}

	/**
	 * ���㵥�ۺͽ��
	 */
	protected void calculateMnyAndNum(ICBillVO[] vos) {
		this.calculateMnyAndNum(this.getBodyWithNums(vos));
	}

	/**
	 * ������
	 */
	protected void calculateMny(ICBillBodyVO[] bodys) {
		this.busiCalc.calcMny(bodys, ICPubMetaNameConst.NNUM);
	}

	/**
	 * ������
	 */
	protected void calculateMny(ICBillVO[] vos) {
		this.busiCalc.calcMny(vos, ICPubMetaNameConst.NNUM);
	}

	/**
	 * ��������
	 */
	protected void calculateNum(ICBillBodyVO[] bodys) {
		this.busiCalc.calcNum(bodys, ICPubMetaNameConst.VCHANGERATE);

	}

	/**
	 * ��������
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