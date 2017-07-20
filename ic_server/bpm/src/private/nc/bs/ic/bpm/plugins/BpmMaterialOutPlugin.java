package nc.bs.ic.bpm.plugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.general.plugins.CheckMnyUtil;
import nc.bs.ic.general.plugins.CheckScaleUtil;
import nc.bs.ic.pub.env.ICBSContext;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.pf.IPFBusiAction;
import nc.pubitf.scmf.ic.mbatchcode.IBatchcodePubService;
import nc.vo.ic.batch.BatchRefViewVO;
import nc.vo.ic.batchcode.BatchSynchronizer;
import nc.vo.ic.batchcode.ICBatchFields;
import nc.vo.ic.fivemetals.CardStatusEnum;
import nc.vo.ic.fivemetals.FiveMetalsHVO;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillFlag;
import nc.vo.ic.general.define.ICBillHeadVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
import nc.vo.ic.pub.calc.BusiCalculator;
import nc.vo.ic.pub.check.VOCheckUtil;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.sncode.ICSnFields;
import nc.vo.ic.sncode.SnCodeSynchronizer;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m422x.entity.StoreReqAppItemVO;
import nc.vo.pu.m422x.entity.StoreReqAppVO;
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
import nc.vo.scmpub.res.billtype.POBillType;

import org.apache.commons.lang.StringUtils;

public class BpmMaterialOutPlugin extends AbstractPfxxPlugin {

	private List<String> updateIndex = new ArrayList<String>();
	private int power = 2;// ����
	private String billtype = "4D";

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		ICBillVO bpmBill = (ICBillVO) vo;
		// 2. У�����ݵĺϷ���:1.���ݽṹ���� 2.������֯+���ݺ�У���Ƿ��ظ�.
		checkData(bpmBill);
		// 3.��ѯ��Ӧ�ϵ������������뵥�����Ҹ��������������뵥��ϸ����
		StoreReqAppVO[] chgBillS = fillData(bpmBill);

		// 4���ݽ���:����һ�Ų��ϳ��ⵥ��������
		// ���������������뵥���Ƶ���ͬ�������ϳ��ⵥ
		ICBillVO[] destVos = PfServiceScmUtil.executeVOChange(
				POBillType.MRBill.getCode(), ICBillType.MaterialOut.getCode(),
				chgBillS);
		// �ϲ�����һ�Ų��ϳ��ⵥ
		List<ICBillBodyVO> body_list = new ArrayList<ICBillBodyVO>();
		for (ICBillVO bill : destVos) {
			ICBillBodyVO[] bodys = bill.getBodys();
			for (ICBillBodyVO body : bodys) {
				body_list.add(body);
			}
		}
		// ����BPM��д�������������뵥��Ϣ����
		ICBillVO clientVO = destVos[0];
		clientVO.setChildrenVO(body_list.toArray(new MaterialOutBodyVO[0]));
		updateClientVO(bpmBill, clientVO);
		String approver = clientVO.getHead().getApprover();
		// ����
		ICBillVO saveVO = doSave(clientVO);
		saveVO.getParentVO().setApprover(approver);
		// ����
		doSign(saveVO);

		return saveVO.getHead().getVbillcode();

	}

	/**
	 * ����
	 * 
	 * @param swapContext
	 * @param icbill
	 * @return
	 * @throws BusinessException
	 */
	private ICBillVO doSave(ICBillVO icbill) throws BusinessException {
		// ����Ƿ�������
		this.checkCanInster(icbill);
		Logger.info("�����µ���ǰ����...");
		this.processBeforeSave(icbill);
		InvocationInfoProxy.getInstance().setUserId(
				icbill.getHead().getBillmaker());
		// TODO ���������и�����Ϣ��aggxsysvoΪ�û����õľ��帨����Ϣ

		Logger.info("�����µ���...");
		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		ICBillVO[] icbills = (ICBillVO[]) service.processAction(
				IPFActionName.WRITE, billtype, null, icbill, null, null);

		Logger.info("�����µ������...");

		Logger.info("�����µ��ݺ���...");
		this.processAfterSave(icbill);

		return icbills[0];
	}

	/**
	 * ǩ��
	 * 
	 * @param swapContext
	 * @param icbill
	 * @return
	 * @throws BusinessException
	 */
	private ICBillVO doSign(ICBillVO icbill) throws BusinessException {
		// ����Ƿ�������
		Logger.info("ǩ���µ���ǰ����...");
		// ǩ��ʱ����ڵ�������
		icbill.getHead().setTaudittime(icbill.getHead().getDbilldate());
		InvocationInfoProxy.getInstance().setUserId(
				icbill.getHead().getApprover());
		Logger.info("ǩ���µ���...");
		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		ICBillVO[] icbills = (ICBillVO[]) service.processAction("SIGN",
				billtype, null, icbill, null, null);

		Logger.info("ǩ���µ������...");

		Logger.info("ǩ���µ��ݺ���...");

		return icbills[0];
	}

	/**
	 * ����vo�Ƿ�ɸ���
	 * 
	 * @param vo
	 */
	protected void checkCanInster(AggregatedValueObject vo) {
		new CheckMnyUtil().checkMny(vo);
		new CheckScaleUtil().checkScale(vo);
	}

	/**
	 * ����BPM��д�������������뵥��Ϣ�����ϳ��ⵥ
	 * 
	 * @param bill
	 * @param clientVO
	 * @throws BusinessException
	 */
	private void updateClientVO(ICBillVO bpmBill, ICBillVO clientVO)
			throws BusinessException {
		MaterialOutBodyVO[] clientbodys = (MaterialOutBodyVO[]) clientVO
				.getBodys();
		ICBillBodyVO[] bodys = (ICBillBodyVO[]) bpmBill.getBodys();
		Map<String, UFDouble> rownos = new HashMap<String, UFDouble>();
		UFDouble setp = new UFDouble(0.1);
		// ���ñ�ͷ��Ϣ
		String[] headKeys = bpmBill.getHead().getAttributeNames();
		for (String key : headKeys) {
			if (nc.util.mmpub.dpub.base.ValueCheckUtil.isEmpty(bpmBill
					.getHead().getAttributeValue(key))) {
				continue;
			}
			clientVO.getHead().setAttributeValue(key,
					bpmBill.getHead().getAttributeValue(key));
		}
		// �����Ϣ


		List<MaterialOutBodyVO> children = new ArrayList<MaterialOutBodyVO>();
		children.addAll(Arrays.asList(clientbodys));
		// ���еĴ���
		for (ICBillBodyVO body : bodys) {
			String csourcebillbid = body.getCsourcebillbid();
			for (MaterialOutBodyVO clientbody : clientbodys) {
				if (!clientbody.getCsourcebillbid().equalsIgnoreCase(
						csourcebillbid)) {
					continue;
				}
				// һ�б��壬���ܻ�д�������
				if (updateIndex.contains(csourcebillbid)) {
					MaterialOutBodyVO newBody = (MaterialOutBodyVO) clientbody
							.clone();
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

		MaterialOutBodyVO[] new_bodys = children
				.toArray(new MaterialOutBodyVO[0]);
		// ����
		BusiCalculator.getBusiCalculatorAtBS().calcNum(new_bodys,
				ICPubMetaNameConst.NNUM);
		clientVO.setChildrenVO(new_bodys);
		// ������
		BusiCalculator.getBusiCalculatorAtBS().calcOnlyMny(
				new ICBillVO[] { clientVO }, ICPubMetaNameConst.NCOSTMNY);
	}

	private void updateClientBVO(ICBillBodyVO body, ICBillBodyVO clientbody)
			throws BusinessException {
		String[] bodyKeys =body.getAttributeNames();
		for (String key : bodyKeys) {
			if (nc.util.mmpub.dpub.base.ValueCheckUtil.isEmpty(body.getAttributeValue(key))) {
				continue;
			}
			Object attributeValue = body.getAttributeValue(key);
			if(attributeValue instanceof UFDouble){
				UFDouble  value = (UFDouble) attributeValue;
				attributeValue = value.setScale(power, UFDouble.ROUND_HALF_UP);
			}
			clientbody.setAttributeValue(key,attributeValue);
		}
		
		UFDouble nnum = getUFDdoubleNullASZero(body.getNnum()).setScale(power,
				UFDouble.ROUND_HALF_UP);// ����
		clientbody.setNnum(nnum);// ʵ������
		// ������Ӧ��Ϊ��,��ʱ������Ƿ�һ��
		clientbody.setNshouldnum(nnum);
		clientbody.setNshouldassistnum(nnum);

		clientbody.setNcostmny(clientbody.getNcostprice().multiply(clientbody.getNnum()));

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
	 * ��ѯ�����������뵥
	 * 
	 * @param bill
	 * @return
	 * @throws BusinessException
	 */
	private StoreReqAppVO[] fillData(ICBillVO bill) throws BusinessException {
		// TODO �Զ����ɵķ������
		// ���ܴ��ںϲ����ϳ����������ϲ���⣬���ǲ���ϲ�����ϸ
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
		BillQuery<StoreReqAppVO> bquery = new BillQuery<>(StoreReqAppVO.class);
		StoreReqAppVO[] aggvos = bquery.query(arrPks.toArray(new String[0]));
		if (aggvos == null || aggvos.length == 0) {
			throw new BusinessException("����ָ����������" + arrPks.toString()
					+ "δ��ѯ�������������뵥,��˶�.");
		}
		// ��Դ�������������뵥�����ܴ��ںϲ���⣬����������������뵥�Զ�Ӧһ����ⵥ
		List<StoreReqAppVO> chgBillS = new ArrayList<StoreReqAppVO>();
		for (StoreReqAppVO aggvo : aggvos) {
			List<StoreReqAppItemVO> arrblist = new ArrayList<StoreReqAppItemVO>();
			StoreReqAppItemVO[] itemVOs = aggvo.getBVO();
			for (StoreReqAppItemVO itemVO : itemVOs) {
				if (arrbpks.contains(itemVO.getPrimaryKey())) {
					arrblist.add(itemVO);
				}
			}
			if (arrblist.size() > 0) {
				StoreReqAppVO chgArrBill = new StoreReqAppVO();
				chgArrBill.setParentVO(aggvo.getParentVO());
				chgArrBill.setChildrenVO(arrblist
						.toArray(new StoreReqAppItemVO[0]));
				chgBillS.add(chgArrBill);
			}
		}

		//
		if (chgBillS.size() == 0) {
			throw new BusinessException(
					"����ָ��������Csourcebillhid���Բ�ѯ�������������뵥�����Ǹ���Csourcebillbidδ��ѯ�������������뵥��ϸ,��˶������Ƿ���ȷ.");
		}
		return chgBillS.toArray(new StoreReqAppVO[0]);

	}

	private void checkData(ICBillVO bill) throws BusinessException {
		// TODO �Զ����ɵķ������
		ICBillBodyVO[] StoreReqAppItemVOs = bill.getBodys();
		if (StoreReqAppItemVOs == null || StoreReqAppItemVOs.length == 0) {
			throw new BusinessException("��ָ����Ҫ����ı�����.");
		}
		VOCheckUtil
				.checkHeadNotNullFields(bill, new String[] { "cwarehouseid",
						"vtrantypecode", "dbilldate", "cdptid", "vdef20",
						"billmaker" });
		VOCheckUtil.checkBodyNotNullFields(bill, new String[] { "vbatchcode",
				"nnum", "csourcebillhid", "csourcebillbid", "ncostprice"});

	}

	private FiveMetalsHVO getFiveMetalsHVO(ICBillHeadVO icBillHeadVO)
			throws BusinessException {
		VOQuery<FiveMetalsHVO> query = new VOQuery<FiveMetalsHVO>(
				FiveMetalsHVO.class);
		String condition = " and pk_group = '" + icBillHeadVO.getPk_group()
				+ "' and pk_org ='" + icBillHeadVO.getPk_org()
				+ "' and vcardno = '" + icBillHeadVO.getVdef20() + "' ";
		FiveMetalsHVO[] hvos = (FiveMetalsHVO[]) query.query(condition, null);
		if (hvos == null || hvos.length == 0)
			throw new BusinessException("�ÿ���û�н���,���鿨���Ƿ���ȷ ��");

		if (!(hvos[0].getVbillstatus() != null && hvos[0].getVbillstatus().intValue() == Integer.parseInt(CardStatusEnum.����.getEnumValue().getValue()))) {
			throw new BusinessException("�ÿ���Ϊ������״̬,���鿨��״̬�Ƿ���ȷ ��");			
		}
		return hvos[0];
	}

	/**
	 * ���ݱ���ǰ����
	 * 
	 * @param vo
	 */
	protected void processBeforeSave(ICBillVO vo) throws BusinessException {

		if (null == vo)
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0139")/*
																			 * @res
																			 * "���ݲ���Ϊ��"
																			 */);
		// this.checkNullValue(vo);
		// ��ͷ��֯�����û�и�ֵ����Ҫ�ر����ֶ�����ȡһ��
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
	 * ���ݱ������
	 * 
	 * @param vo
	 */
	protected void processAfterSave(ICBillVO vo) throws BusinessException {
		if (null == vo)
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0140")/*
																			 * @res
																			 * "���ݱ���ʧ��"
																			 */);
	}

	/**
	 * ��ͷ��������Ĭ��ֵ����˾���������ڣ�����״̬����ӡ����������
	 * 
	 * @param vo
	 */
	private void headVOProcess(ICBillHeadVO vo, ICBSContext context) {
		vo.setStatus(VOStatus.NEW);
		// ����
		if (StringUtil.isSEmptyOrNull(vo.getPk_group()))
			vo.setPk_group(context.getPk_group());
		// ��ӡ����
		if (vo.getIprintcount() == null)
			vo.setIprintcount(Integer.valueOf(0));
		// ����״̬
		if (vo.getFbillflag() == null)
			vo.setFbillflag((Integer) ICBillFlag.FREE.value());
		// ��������
		if (vo.getDbilldate() == null)
			vo.setDbilldate(context.getBizDate());
		vo.setDmakedate(vo.getDbilldate());
		// ����ʱ��
		vo.setCreationtime(new UFDateTime(vo.getDbilldate().toString()));
		vo.setCreator(vo.getBillmaker());
		//
		if (StringUtil.isSEmptyOrNull(vo.getApprover())){
			vo.setApprover(vo.getBillmaker());
		}

		// ��˾
		if (StringUtil.isSEmptyOrNull(vo.getCorpoid())
				|| StringUtil.isSEmptyOrNull(vo.getCorpvid())) {
			vo.setCorpoid(context.getOrgInfo().getCorpIDByCalBodyID(
					vo.getPk_org()));
			vo.setCorpvid(context.getOrgInfo().getCorpVIDByCalBodyID(
					vo.getPk_org()));
		}

		if (StringUtil.isSEmptyOrNull(vo.getCtrantypeid())) {
			// uap��֧�ֵ������͵ķ��룬��ʱ�Խ�������code��ѯid�ķ�ʽ����������
			String vtrantypecode = vo.getVtrantypecode();
			Map<String, String> map = PfServiceScmUtil
					.getTrantypeidByCode(new String[] { vtrantypecode });
			vo.setCtrantypeid(map == null ? null : map.get(vtrantypecode));
		}

	}

	/**
	 * ���ݱ��崦��
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
																			 * "���ݱ��岻��Ϊ��"
																			 */);

		VORowNoUtils.setVOsRowNoByRule(vos, ICPubMetaNameConst.CROWNO);// �кŴ���

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
																 * "���ݱ������ϲ���Ϊ��"
																 */);

			body.setBbarcodeclose(UFBoolean.FALSE);// �������Ƿ�����ر�
			body.setBonroadflag(UFBoolean.FALSE);// �Ƿ���;
			if (body.getNnum() != null && body.getNassistnum() != null
					&& body.getDbizdate() == null)
				body.setDbizdate(head.getDbilldate());// ҵ������
			// ����λ
			if (StringUtil.isSEmptyOrNull(body.getCastunitid()))
				body.setCastunitid(context.getInvInfo()
						.getInvBasVO(body.getCmaterialvid()).getPk_stockmeas());

			// �����κŵ�����������ʱ�� ��Ҫ��ȫ�����������б�Ҫʱ(�����ڹ���)��ȫ�������ں�ʧЧ����
			if (!StringUtils.isEmpty(body.getVbatchcode())
					&& StringUtils.isEmpty(body.getPk_batchcode())) {
				//ͬ����ά����Ϣ
				BatchCodeRule batchCodeRule = new BatchCodeRule();
				BatchRefViewVO batchRefViewVOs = batchCodeRule.getRefVO(body.getPk_org(),body.getCbodywarehouseid(),body.getCmaterialvid(), body.getVbatchcode());
				if (batchRefViewVOs != null ) {
					batchCodeRule.synBatch(batchRefViewVOs, body);
				}
				
			}
			bodyVOCopyFromHeadVO(body, head);
		}
		for (ICBillBodyVO body : vos) {
			body.setDbizdate(head.getDbilldate());// ҵ������
		}
		vo.setChildrenVO(vos);
		// ͬ���������θ����ֶ�
		new BatchSynchronizer(new ICBatchFields()).fillBatchVOtoBill(vos);
		// ͬ���������кŸ����ֶ�
		new SnCodeSynchronizer(new ICSnFields()).fillBatchVOtoBill(vos);
	}

	/**
	 * ��ȡ���κŵ���
	 * 
	 * @param vos
	 * @return Map<String(cmaterialvid+vbatchcode), BatchcodeVO���ε���>
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
	 * ���ݱ�ͷ���ñ���Ĭ��ֵ�����弯�ţ������֯����˾���ֿ⣬��������
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