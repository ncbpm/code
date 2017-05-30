package nc.bs.hr.trn.plugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.hr.utils.PubEnv;
import nc.hr.utils.ResHelper;
import nc.itf.hr.frame.IPersistenceRetrieve;
import nc.itf.hr.frame.IPersistenceUpdate;
import nc.itf.trn.IItemSetAdapter;
import nc.itf.trn.TrnDelegator;
import nc.itf.trn.transmng.ITransmngManageService;
import nc.itf.trn.transmng.ITransmngQueryService;
import nc.md.model.IBean;
import nc.vo.arap.utils.ArrayUtil;
import nc.vo.hi.psndoc.CtrtVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFLiteralDate;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pflow.PfUserObject;
import nc.vo.pubapp.util.NCPfServiceUtils;
import nc.vo.trn.pub.BeanUtil;
import nc.vo.trn.pub.TRNConst;
import nc.vo.trn.regmng.RegapplyVO;
import nc.vo.trn.transmng.AggStapply;
import nc.vo.trn.transmng.StapplyVO;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class BpmTransExpPfxxPlugin<T extends AggStapply> extends
		nc.bs.pfxx.plugin.AbstractPfxxPlugin {

	private PfUserObject[] userObjs;

	/**
	 * ����XMLת��������VO����NCϵͳ��ҵ����ʵ�ִ˷������ɡ�<br>
	 * ��ע�⣬ҵ�񷽷���У��һ��Ҫ���
	 * 
	 * @param vo
	 *            ת�����vo���ݣ���NCϵͳ�п���ΪValueObject,SuperVO,AggregatedValueObject,
	 *            IExAggVO�ȡ�
	 * @param swapContext
	 *            ���ֽ�����������֯�����ܷ������ͷ������׵ȵ�
	 * @param aggxsysvo
	 *            ������Ϣvo
	 * @return ���� ��ְ����
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggxsysvo) throws BusinessException {

		// 1.�õ�ת�����VO����,ȡ�����򵼵�һ��ע���VO��Ϣ
		AggStapply bill = (AggStapply) vo;
		StapplyVO head = setHeaderDefault((StapplyVO) bill.getParentVO());
		if (head.getPk_billtype() == null) {
			throw new BusinessException("���ݵĵ������ͱ����ֶβ���Ϊ�գ�������ֵ");
		}

		if (head.getPk_group() == null) {
			throw new BusinessException("���ݵ����������ֶβ���Ϊ�գ�������ֵ");
		}
		if (head.getPk_org() == null) {
			throw new BusinessException("���ݵĲ�����֯�ֶβ���Ϊ�գ�������ֵ");
		}

		// 2.��ѯ�˵����Ƿ��Ѿ��������
		String oldPk = PfxxPluginUtils.queryBillPKBeforeSaveOrUpdate(
				swapContext.getBilltype(), swapContext.getDocID());
		if (oldPk != null) {

			// ����жϣ�����ƽ̨�Ѿ�������������ѵ��룬��replace="N"����ôƽ̨�ͻ��׳��쳣����ʾ�����ظ�
			if (swapContext.getReplace().equalsIgnoreCase("N"))
				throw new BusinessException(
						"�������ظ����뵥�ݣ������Ƿ��ǲ����������������ѵ��뵥�ݣ���������ļ���replace��־��Ϊ��Y��");

			ITransmngQueryService voucherbo = (ITransmngQueryService) NCLocator
					.getInstance()
					.lookup(ITransmngQueryService.class.getName());
			AggStapply preVO = voucherbo.queryByPk(oldPk);

			if (preVO != null && preVO.getParentVO() != null) {

				throw new BusinessException("�����Ѵ��ڣ��������ظ����뵥�ݡ�");

			}
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		String strCode = formatter.format(new Date());
		head.setBill_code(strCode); //

		AggStapply newBill = this.insertBill(bill, head);

		String pk = null;
		if (newBill != null) {
			pk = newBill.getParentVO().getPrimaryKey();
		}
		if (oldPk != null) {
			PfxxPluginUtils.deleteIDvsPKByDocPK(oldPk);
		}
		PfxxPluginUtils.addDocIDVsPKContrast(swapContext.getBilltype(),
				swapContext.getDocID(), pk);
		return pk;
	}

	private AggStapply insertBill(AggStapply bill, StapplyVO head)
			throws BusinessException {

		ITransmngManageService voucherbo = (ITransmngManageService) NCLocator
				.getInstance().lookup(ITransmngManageService.class.getName());

		AggStapply res = voucherbo.insertBill(bill);
		res = (AggStapply) ArrayUtil
				.getFirstInArrays((Object[]) NCPfServiceUtils.processBatch(
						IPFActionName.SAVE, head.getPk_billtype(),
						new AggStapply[] { (AggStapply) bill }, getUserObj(),
						new WorkflownoteVO()));

		validate(new AggStapply[] { (AggStapply) res });
		// validate(vos);

		// �����������еĺ�ͬ
		StapplyVO billvo = (StapplyVO) res.getParentVO();
		if (IPfRetCheckInfo.PASSING == billvo.getApprove_state()) {
			if (billvo.getIsend() != null && billvo.getIsend().booleanValue()
					|| billvo.getIsrelease() != null
					&& billvo.getIsrelease().booleanValue()) {
				handleCtrtInfo(billvo);
			}
		}
		((StapplyVO) res.getParentVO()).setApprove_state(1);
		res = voucherbo.updateBill(res, false);

		return res;
	}

	/**
	 * ����ͨ��ʱ�������ͬ��Ϣ <br>
	 * Created on 2014-3-18 19:48:57<br>
	 * 
	 * @param billvo
	 * @throws BusinessException
	 * @author caiqm
	 */
	private void handleCtrtInfo(StapplyVO billvo) throws BusinessException {
		boolean isCMStart = PubEnv.isModuleStarted(PubEnv.getPk_group(),
				PubEnv.MODULE_HRCM);
		if (isCMStart && IPfRetCheckInfo.PASSING == billvo.getApprove_state())// Ϊ�������ύ��ͨ��--
																				// &&
																				// IPfRetCheckInfo.PASSING
																				// ==
																				// billvo.getApprove_state()
		{
			// ���������Ч�ĺ�ͬ������û��δ��Ч�Ľ���������ֹ��ͬ������һ����
			String cond_hasCtrt = "pk_psnorg = '"
					+ billvo.getPk_psnorg()
					+ "' and lastflag = 'Y' and conttype in (1, 2, 3) and isrefer = 'Y'";
			CtrtVO[] ctrtAllVOs = (CtrtVO[]) NCLocator.getInstance()
					.lookup(IPersistenceRetrieve.class)
					.retrieveByClause(null, CtrtVO.class, cond_hasCtrt);
			boolean isIsreferCtrt = false;
			if (!ArrayUtils.isEmpty(ctrtAllVOs)) {
				isIsreferCtrt = true;
			}
			if (isIsreferCtrt) {
				String condition = "recordnum = 0 and isrefer = 'N' and pk_psnorg = '"
						+ billvo.getPk_psnorg() + "'";
				CtrtVO[] ctrtVOs = (CtrtVO[]) NCLocator.getInstance()
						.lookup(IPersistenceRetrieve.class)
						.retrieveByClause(null, CtrtVO.class, condition);
				if (ArrayUtils.isEmpty(ctrtVOs)) {
					// ����һ���µ�δ��Ч��ͬ
					CtrtVO newCtrtVO = new CtrtVO();
					try {
						BeanUtils.copyProperties(newCtrtVO, ctrtAllVOs[0]);
					} catch (Exception e) {
						Logger.error(e.getMessage(), e);
					}
					newCtrtVO.setRecordnum(0);
					newCtrtVO.setLastflag(UFBoolean.FALSE);
					newCtrtVO.setIsrefer(UFBoolean.FALSE);
					int conttype = billvo.getIsrelease() == UFBoolean.TRUE ? 4
							: 5;
					newCtrtVO.setConttype(conttype);
					newCtrtVO.setSigndate(new UFLiteralDate());
					newCtrtVO
							.setTermmonth(ctrtAllVOs[0].getTermmonth() == null ? null
									: ctrtAllVOs[0].getTermmonth());// ��ͬ����
					newCtrtVO
							.setPromonth(ctrtAllVOs[0].getPromonth() == null ? null
									: ctrtAllVOs[0].getPromonth());// ��������
					newCtrtVO.setPresenter(1);// ��������---Ĭ��Ϊ���˵�λ

					NCLocator.getInstance().lookup(IPersistenceUpdate.class)
							.insertVO(null, newCtrtVO, null);

				} else if (ctrtVOs[0].getConttype() == 1
						|| ctrtVOs[0].getConttype() == 2
						|| ctrtVOs[0].getConttype() == 3) {
					throw new BusinessException(ResHelper.getString("6009tran",
							"X6009tran0060")/*
											 * @res "����δ��Ч����ǩ�����ĺ�ͬ��¼��"
											 */);
				}
			}
		}
	}

	private void validate(AggStapply[] vos) throws BusinessException {

		String errMsg = "";
		for (AggStapply vo : vos) {
			StapplyVO billvo = (StapplyVO) vo.getParentVO();
			if (IPfRetCheckInfo.PASSING == billvo.getApprove_state()) {
				errMsg += getMsg(billvo);
			}
		}
		if (!StringUtils.isBlank(errMsg)) {
			throw new BusinessException(ResHelper.getString("6009tran",
					"06009tran0153")/* @res "�����д���δ��д�ı�����" */);
		}
	}

	private String getMsg(StapplyVO billvo) throws BusinessException {

		// ��������ͨ���ĵ���,У�����б������Ŀ�Ƿ��������
		SuperVO[] itemvos = TrnDelegator.getIItemSetQueryService()
				.queryItemSetByOrg(TRNConst.TRNSITEM_BEANID,
						billvo.getPk_group(), billvo.getPk_org(),
						billvo.getPk_trnstype());
		IBean ibean = BeanUtil.getBeanEntity(TRNConst.TRNSITEM_BEANID);
		List<IItemSetAdapter> iitemadpls = BeanUtil.getBizImpObjFromVo(ibean,
				IItemSetAdapter.class, itemvos);
		for (IItemSetAdapter item : iitemadpls) {
			if (item == null || item.getItemkey().startsWith("old")) {
				// ǰ��Ŀ��У��
				continue;
			}
			if (item.getIsnotnull().booleanValue()
					&& isNull(billvo.getAttributeValue(item.getItemkey()))) {
				return '\n' + billvo.getBill_code();
			}
		}
		return "";
	}

	private boolean isNull(Object o) {

		if (o == null || o.toString() == null || o.toString().trim().equals("")) {
			return true;
		}
		return false;
	}

	public PfUserObject[] getUserObj() {
		if (userObjs == null) {
			userObjs = new PfUserObject[] { new PfUserObject() };
		}
		return userObjs;
	}

	/**
	 * ���ñ�ͷĬ����Ϣ
	 * 
	 * @param headerVo
	 * @return
	 * @throws BusinessException
	 */
	private StapplyVO setHeaderDefault(StapplyVO header)
			throws BusinessException {
		Integer ZERO = Integer.valueOf(0);
		/* ����״̬Ϊδ��� */
		header.setApprove_state(-1);
		header.setDr(ZERO);
		header.setCreationtime(new UFDateTime());
		return header;
	}

}
