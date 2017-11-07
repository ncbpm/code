package nc.bs.hr.trn.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.hr.utils.PubEnv;
import nc.hr.utils.ResHelper;
import nc.itf.hr.frame.IPersistenceRetrieve;
import nc.itf.hr.frame.IPersistenceUpdate;
import nc.itf.trn.IItemSetAdapter;
import nc.itf.trn.TrnDelegator;
import nc.itf.trn.rds.IRdsManageService;
import nc.itf.trn.regmng.IRegmngQueryService;
import nc.itf.trn.transmng.ITransmngManageService;
import nc.itf.trn.transmng.ITransmngQueryService;
import nc.md.model.IBean;
import nc.message.util.IDefaultMsgConst;
import nc.message.util.MessageCenter;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.vo.arap.utils.ArrayUtil;
import nc.vo.hi.entrymng.HiSendMsgHelper;
import nc.vo.hi.psndoc.CtrtVO;
import nc.vo.hi.pub.HICommonValue;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
import nc.vo.pub.AggregatedValueObject;
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
import nc.vo.trn.transmng.AggStapply;
import nc.vo.trn.transmng.StapplyVO;
import nc.vo.uap.rbac.constant.INCSystemUserConst;
import nc.vo.uif2.LoginContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * ���� ��ְ����
 */
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
	 * @return
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

		if (head.getBill_code() == null) {
			throw new BusinessException("���ݵĵ��ݱ���ֶβ���Ϊ�գ�������ֵ");
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

		// SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		// String strCode = formatter.format(new Date());
		// head.setBill_code(strCode); //

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

		if (head.getCreator() == null)
			throw new BusinessException("�����˲���Ϊ�ա�");
		InvocationInfoProxy.getInstance().setUserId(head.getCreator());
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
		if (head.getApprover() != null)
			InvocationInfoProxy.getInstance().setUserId(head.getApprover());
		((StapplyVO) res.getParentVO()).setApprover(InvocationInfoProxy
				.getInstance().getUserId());
		res = voucherbo.updateBill(res, false);
		LoginContext context = new LoginContext();
		context.setPk_group(head.getPk_group());
		// ִ��
		ITransmngQueryService voucherbo2 = (ITransmngQueryService) NCLocator
				.getInstance().lookup(ITransmngQueryService.class.getName());
		AggStapply vo = voucherbo2.queryByPk(res.getParentVO().getPrimaryKey());
		execBills(new AggStapply[] { vo });
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

	private boolean isqueryctrt = false;

	/**
	 * Ϊ��֧����Ϣģ���ϵ��������ƶ���������ִ�е��ݵĲ���������̨���� ��ԭ����ǰ̨Model��ʵ�ֵ�execBills����������̨ʵ��
	 * 
	 * @param datas
	 * @return
	 * @throws BusinessException
	 * @author heqiaoa 2014-11-18
	 */
	public AggStapply[] execBills(AggStapply[] vos) throws BusinessException {
		ArrayList<AggStapply> al = new ArrayList<AggStapply>();
		ArrayList<AggStapply> allvo = new ArrayList<AggStapply>();
		for (int i = 0; i < vos.length; i++) {
			AggStapply agg = vos[i];
			Integer apprState = ((StapplyVO) agg.getParentVO())
					.getApprove_state();
			UFLiteralDate effectDate = ((StapplyVO) agg.getParentVO())
					.getEffectdate();
			if (effectDate != null
					&& effectDate.compareTo(PubEnv.getServerLiteralDate()) <= 0
					&& apprState != null
					&& apprState == IPfRetCheckInfo.PASSING) {
				al.add(agg);
			}

			allvo.add(agg);
		}

		ArrayList<AggStapply> bills = new ArrayList<AggStapply>();
		if (al.size() > 0) {
			// ��̨У��ʱ��Ҫ��ѯ��ͬ
			isqueryctrt = true;
			// �������Զ�ִ�����
			// Ϊ�˴�����ʵĲ������б���У�飬������pk_org��pk_group����һ��LoginContext
			LoginContext tempContext = new LoginContext();
			AggStapply aggvo = al.get(0);
			StapplyVO parentVO = (StapplyVO) aggvo.getParentVO();
			tempContext.setPk_group(parentVO.getPk_group());
			tempContext.setPk_org(parentVO.getPk_org());

			// ����һ��map ����ִ�гɹ��ĵ��ݣ���ִ�в��ɹ�����Ϣ
			HashMap<String, Object> result = execBills(
					al.toArray(new AggStapply[0]), tempContext, false);
			bills = (ArrayList<AggStapply>) result.get(TRNConst.RESULT_BILLS);
			String msg = (String) result.get(TRNConst.RESULT_MSG);
			if (!StringUtils.isBlank(msg)) {
				// ����NCMessage
				NCMessage ncMessage = new NCMessage();
				MessageVO messageVO = new MessageVO();
				messageVO.setMsgsourcetype(IDefaultMsgConst.NOTICE);// ��ϢԴ����
				messageVO.setReceiver(PubEnv.getPk_user());// ���ý����� ���������֮���Զ��Ÿ���
				messageVO.setIsdelete(UFBoolean.FALSE);// ����ɾ�����
				messageVO.setSender(INCSystemUserConst.NC_USER_PK);
				// �������ʱ��Ϊ����Ĭ��Ϊ������ʱ��
				messageVO.setSendtime(PubEnv.getServerTime());
				messageVO.setDr(0);
				messageVO.setSubject(ResHelper.getString("6007entry",
						"16007entry0015")/*
										 * res "��������ʧ��"
										 */);
				messageVO.setContent(msg);
				ncMessage.setMessage(messageVO);
				NCMessage[] message = new NCMessage[1];
				message[0] = ncMessage;
				try {
					MessageCenter.sendMessage(message);
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
			}
		}

		// ��ִ�гɹ��ĺͲ��ɹ��ĺϲ�
		if (bills != null && bills.size() > 0)// ��û��ִ�гɹ��ĵ��ݣ���ֱ�ӽ�����ͨ���ĵ��ݷ��ػ�ȥˢ�½���
		{
			for (int i = 0; i < allvo.size(); i++) {
				AggStapply aggivo = allvo.get(i);
				for (int j = 0; j < bills.size(); j++) {
					AggStapply aggjvo = allvo.get(j);
					if (aggivo.getParentVO().getPrimaryKey()
							.equals(aggjvo.getParentVO().getPrimaryKey())) {
						allvo.remove(i);
					}
				}
			}
			vos = (AggStapply[]) ArrayUtils.addAll(
					allvo.toArray(new AggStapply[0]),
					bills.toArray(new AggStapply[0]));
		}

		return vos;
	}

	public HashMap<String, Object> execBills(AggStapply[] bills,
			LoginContext context, boolean isRunBackgroundTask)
			throws BusinessException {
		HashMap<String, Object> result = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();// ������Ϣ
		ArrayList<AggStapply> alTrans = new ArrayList<AggStapply>();// ���䵥��
		ArrayList<AggStapply> alDimission = new ArrayList<AggStapply>();// ��ְ����
		for (int i = 0; bills != null && i < bills.length; i++) {
			if (TRNConst.BUSITYPE_TRANSITION.equals(bills[i].getParentVO()
					.getAttributeValue(StapplyVO.PK_BILLTYPE))) {
				alTrans.add(bills[i]);
			} else {
				alDimission.add(bills[i]);
			}
		}
		AggregatedValueObject[] retVOs = null;
		if (alTrans.size() > 0) {
			retVOs = NCLocator
					.getInstance()
					.lookup(IRegmngQueryService.class)
					.validateBudget(alTrans.toArray(new AggStapply[0]), context);
		}

		// ���һ���߼�,������У��ûͨ���ĵ���,Ҳ���뵽������ʾ��
		for (int i = 0; i < alTrans.size(); i++) {
			if (isExit(retVOs, alTrans.get(i))) {
				continue;
			}
			sb.append(ResHelper.getString(
					"6009tran",
					"06009tran0166"/* @res "����{0}���ڱ���У��δͨ�����ܳɹ�ִ��" */,
					(String) alTrans.get(i).getParentVO()
							.getAttributeValue(StapplyVO.BILL_CODE)));
		}

		if ((retVOs == null || retVOs.length == 0) && alDimission.size() == 0) {
			// �����ֶ�û�п�ִ�е���,ֱ�ӷ���
			String msg = sb.length() == 0 ? "" : sb.toString();
			result.put(
					TRNConst.RESULT_MSG,
					isRunBackgroundTask ? msg : msg.replaceAll("<br>",
							'\n' + ""));
			result.put(TRNConst.RESULT_BILLS, null);
			return result;
		}
		// ֻ�Ե��䵥�ݽ��б���У��
		ArrayList<AggStapply> transBill = new ArrayList<AggStapply>();// ����ִ�гɹ��ĵ���
		ArrayList<AggStapply> dimisBill = new ArrayList<AggStapply>();// ��ְִ�гɹ��ĵ���
		AggregatedValueObject[] aggs = (AggregatedValueObject[]) ArrayUtils
				.addAll(retVOs, alDimission.toArray(new AggStapply[0]));

		ITransmngQueryService voucherbo = (ITransmngQueryService) NCLocator
				.getInstance().lookup(ITransmngQueryService.class.getName());
		for (int i = 0; i < aggs.length; i++) {
			String billtype = (String) aggs[i].getParentVO().getAttributeValue(
					StapplyVO.PK_BILLTYPE);
			try {

				if (TRNConst.BUSITYPE_TRANSITION.equals(billtype)) {
					// ����У��&ִ��
					Object obj = getRdsService().perfromStaff_RequiresNew(
							(AggStapply) aggs[i], isqueryctrt);
					if (null == obj) {
						continue;
					}
					if (obj instanceof String) {
						transBill.add(voucherbo.queryByPk(aggs[i].getParentVO()
								.getPrimaryKey()));
						continue;
					}
					transBill.add(voucherbo.queryByPk(aggs[i].getParentVO()
							.getPrimaryKey()));
				} else {
					// ��ְУ��&ִ��
					Object obj = getRdsService().perfromTurnOver_RequiresNew(
							(AggStapply) aggs[i], isqueryctrt);
					if (null == obj) {
						continue;
					}
					if (obj instanceof String) {
						dimisBill.add(voucherbo.queryByPk(aggs[i].getParentVO()
								.getPrimaryKey()));
						continue;
					}
					dimisBill.add(voucherbo.queryByPk(aggs[i].getParentVO()
							.getPrimaryKey()));
				}

			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				String billcode = (String) aggs[i].getParentVO()
						.getAttributeValue(StapplyVO.BILL_CODE);
				if (StringUtils.isBlank(e.getMessage())) {

					sb.append((i + 1)
							+ ":"
							+ ResHelper.getString("6009tran",
									"06009tran0167"/*
													 * @res
													 * "����{0}��������δ֪�쳣[{1}]���ܳɹ�ִ��,�����쳣��Ϣ��鿴��־."
													 */, billcode,
									e.getMessage()) /*
													 * + " <br>"
													 */);
				} else {
					if (e.getMessage().indexOf(billcode) < 0) {
						// ����쳣��Ϣ��û�г��ֵ��ݺ�,�������쳣��Ϣ
						sb.append((i + 1)
								+ ":"
								+ ResHelper.getString("6009tran",
										"06009tran0167"/*
														 * @res
														 * "����{0}��������δ֪�쳣[{1}]���ܳɹ�ִ��,�����쳣��Ϣ��鿴��־."
														 */, billcode,
										e.getMessage()) /*
														 * + " <br>"
														 */);
					} else {
						sb.append((i + 1) + ":" + e.getMessage() /* + "<br>" */);
					}
				}
				continue;
			}
			try {
				AggStapply agg = voucherbo.queryByPk(aggs[i].getParentVO()
						.getPrimaryKey());
				getRdsService().pushWorkflow_RequiresNew(billtype, agg);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		// ִ����ɺ�,����֪ͨ
		// 1001Z7PSN00000000004
		// 1001Z7PSN00000000005
		// 1)���͵����֪ͨ,���յ������֯����
		// key-org value-bill_list
		HashMap<String, ArrayList<AggStapply>> hmTrans = new HashMap<String, ArrayList<AggStapply>>();
		for (AggStapply bill : transBill) {
			String pk_org = (String) bill.getParentVO().getAttributeValue(
					StapplyVO.PK_HI_ORG);
			if (hmTrans.get(pk_org) == null) {
				hmTrans.put(pk_org, new ArrayList<AggStapply>());
			}
			hmTrans.get(pk_org).add(bill);
		}
		for (String key : hmTrans.keySet()) {
			if (hmTrans.get(key) == null || hmTrans.get(key).size() <= 0) {
				continue;
			}

			// Ϊ���䵥��ʱ
			String tempCode = HICommonValue.msgcode_trns;// ����ֱ��֪ͨ��Ϣģ��Դ����
			HiSendMsgHelper.sendMessage1(tempCode,
					hmTrans.get(key).toArray(new AggStapply[0]), key);
		}
		// 1)������ְ��֪ͨ,���յ������֯����
		// key-org value-bill_list
		HashMap<String, ArrayList<AggStapply>> hmDimis = new HashMap<String, ArrayList<AggStapply>>();
		for (AggStapply bill : dimisBill) {
			String pk_org = (String) bill.getParentVO().getAttributeValue(
					StapplyVO.PK_HI_ORG);
			if (hmDimis.get(pk_org) == null) {
				hmDimis.put(pk_org, new ArrayList<AggStapply>());
			}
			hmDimis.get(pk_org).add(bill);
		}
		for (String key : hmDimis.keySet()) {
			if (hmDimis.get(key) == null || hmDimis.get(key).size() <= 0) {
				continue;
			}

			// Ϊ��ְ����ʱ
			String tempCode = HICommonValue.msgcode_dimission;// ��ְֱ��֪ͨ��Ϣģ�����
			HiSendMsgHelper.sendMessage1(tempCode,
					hmDimis.get(key).toArray(new AggStapply[0]), key);
		}
		// end
		String msg = sb.length() == 0 ? "" : sb.toString();
		result.put(TRNConst.RESULT_MSG,
				isRunBackgroundTask ? msg : msg.replaceAll("<br>", '\n' + ""));
		transBill.addAll(dimisBill);
		result.put(TRNConst.RESULT_BILLS, transBill);
		return result;
	}

	private boolean isExit(AggregatedValueObject[] retVOs, AggStapply aggStapply)
			throws BusinessException {
		for (int i = 0; retVOs != null && i < retVOs.length; i++) {
			if (aggStapply.getParentVO().getPrimaryKey()
					.equals(retVOs[i].getParentVO().getPrimaryKey())) {
				return true;
			}
		}
		return false;
	}

	private IRdsManageService getRdsService() {
		return NCLocator.getInstance().lookup(IRdsManageService.class);
	}
}
