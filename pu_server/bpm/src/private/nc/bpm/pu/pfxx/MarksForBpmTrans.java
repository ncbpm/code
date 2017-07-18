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
 * ������̬ת����
 * 
 * @author Administrator
 * 
 */
public class MarksForBpmTrans {

	private List<String> updateIndex = new ArrayList<String>();
	private int power = 2;// ����

	/**
	 * ������̬ת����
	 * 
	 * @param vo
	 * @param swapContext
	 * @param aggvo
	 * @return
	 * @throws BusinessException
	 */
	protected Object save(Object vo) throws BusinessException {
		// TODO �Զ����ɵķ������
		ICSpecialVO bpmBill = (ICSpecialVO) vo;
		TransformBodyVO[] bodys = (TransformBodyVO[]) bpmBill.getBodys();
		// ����
		BusiCalculator.getBusiCalculatorAtBS().calcNum(bodys,
				ICPubMetaNameConst.NNUM);
		// ������
		// BusiCalculator.getBusiCalculatorAtBS().calcOnlyMny(
		// new ICSpecialVO[] { bpmBill }, ICPubMetaNameConst.NNUM);
		// ����
		ICSpecialVO saveVO = doSave(bpmBill);
		// ����
		// doSign(saveVO);

		return saveVO;

	}

	/**
	 * ����
	 * 
	 * @param swapContext
	 * @param icbill
	 * @return
	 * @throws BusinessException
	 */
	private ICSpecialVO doSave(ICSpecialVO icbill) throws BusinessException {
		// ����Ƿ�������
		Logger.info("�����µ���ǰ����...");
		this.processBeforeSave(icbill);

		// TODO ���������и�����Ϣ��aggxsysvoΪ�û����õľ��帨����Ϣ
		InvocationInfoProxy.getInstance().setUserId(
				icbill.getHead().getCreator());

		Logger.info("�����µ���...");
		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		ICSpecialVO[] icbills = (ICSpecialVO[]) service.processAction(
				IPFActionName.WRITE, "4N", null, icbill, null, null);

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
	private ICSpecialVO doSign(ICSpecialVO icbill) throws BusinessException {
		// ����Ƿ�������
		Logger.info("ǩ���µ���ǰ����...");
		// ǩ��ʱ����ڵ�������
		icbill.getHead().setTaudittime(icbill.getHead().getDbilldate());
		InvocationInfoProxy.getInstance().setUserId(
				icbill.getHead().getApprover());
		Logger.info("ǩ���µ���...");
		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		ICSpecialVO[] icbills = (ICSpecialVO[]) service.processAction("SIGN",
				"4N", null, icbill, null, null);

		Logger.info("ǩ���µ������...");

		Logger.info("ǩ���µ��ݺ���...");

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
	 * ���ݱ���ǰ����
	 * 
	 * @param vo
	 */
	protected void processBeforeSave(ICSpecialVO vo) throws BusinessException {

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
	protected void processAfterSave(ICSpecialVO vo) throws BusinessException {
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
	private void headVOProcess(ICSpecialHeadEntity vo, ICBSContext context) {
		vo.setStatus(VOStatus.NEW);
		// ��֯�汾
		if (StringUtil.isSEmptyOrNull(vo.getPk_org_v()))
			vo.setPk_org_v(getPkOrg_v(vo.getPk_org()));
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
		//
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

	public String getPkOrg_v(String pk_org) {
		// ��֯�����°汾
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
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}
		return pk_org_v;
	}

	/**
	 * ���ݱ��崦��
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
																			 * "���ݱ��岻��Ϊ��"
																			 */);

		VORowNoUtils.setVOsRowNoByRule(vos, ICPubMetaNameConst.CROWNO);// �кŴ���
		ICSpecialHeadEntity head = vo.getHead();

		for (ICSpecialBodyEntity body : vos) {
			body.setStatus(VOStatus.NEW);
			if (StringUtil.isSEmptyOrNull(body.getCmaterialoid())
					|| StringUtil.isSEmptyOrNull(body.getCmaterialvid()))
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("4008001_0", "04008001-0142")/*
																 * @res
																 * "���ݱ������ϲ���Ϊ��"
																 */);
			bodyVOCopyFromHeadVO(body, head);
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
		
		}

	}

	

	/**
	 * ���ݱ�ͷ���ñ���Ĭ��ֵ�����弯�ţ������֯����˾���ֿ⣬��������
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
