package nc.bs.hr.trn.plugin;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.naming.NamingException;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pub.SystemException;
import nc.hr.frame.persistence.SimpleDocServiceTemplate;
import nc.hr.utils.PubEnv;
import nc.hr.utils.ResHelper;
import nc.itf.hi.IPersonRecordService;
import nc.itf.hr.frame.IHrBillCode;
import nc.itf.hr.frame.IPersistenceRetrieve;
import nc.itf.hr.frame.IPersistenceUpdate;
import nc.itf.trn.IItemSetAdapter;
import nc.itf.trn.TrnDelegator;
import nc.itf.trn.regmng.IRegmngManageService;
import nc.itf.trn.regmng.IRegmngQueryService;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.itf.uap.pf.IplatFormEntry;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.data.access.NCObject;
import nc.md.model.IBean;
import nc.md.model.impl.MDEnum;
import nc.md.persist.framework.IMDPersistenceService;
import nc.md.persist.framework.MDPersistenceService;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.pub.billcode.itf.IBillcodeRuleQryService;
import nc.pub.billcode.vo.BillCodeContext;
import nc.pub.billcode.vo.BillCodeElemVO;
import nc.pub.billcode.vo.BillCodeRuleVO;
import nc.vo.arap.utils.ArrayUtil;
import nc.vo.hi.entrymng.HiSendMsgHelper;
import nc.vo.hi.psndoc.PsnJobVO;
import nc.vo.hi.psndoc.PsndocVO;
import nc.vo.hi.psndoc.TrialVO;
import nc.vo.hi.psndoc.enumeration.TrnseventEnum;
import nc.vo.hi.pub.HICommonValue;
import nc.vo.hr.tools.pub.HRConstEnum;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
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
import nc.vo.trn.regitem.TrnRegItemVO;
import nc.vo.trn.regmng.AggRegapplyVO;
import nc.vo.trn.regmng.RegapplyVO;
import nc.vo.uif2.LoginContext;
import nc.vo.wfengine.definition.WorkflowTypeEnum;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * �ڴ˴���Ӵ����������Ϣ
 * </p>
 * //ת������
 */
public class BpmRegmngExpPfxxPlugin<T extends AggRegapplyVO> extends
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
		AggRegapplyVO bill = (AggRegapplyVO) vo;
		RegapplyVO head = setHeaderDefault((RegapplyVO) bill.getParentVO());
		// if (head.getPk_billtype() == null) {
		// throw new BusinessException("���ݵĵ������ͱ����ֶβ���Ϊ�գ�������ֵ");
		// }

		if (head.getPk_group() == null) {
			throw new BusinessException("���ݵ����������ֶβ���Ϊ�գ�������ֵ");
		}
		if (head.getPk_org() == null) {
			throw new BusinessException("���ݵĲ�����֯�ֶβ���Ϊ�գ�������ֵ");
		}
		if (head.getRegulardate() == null) {
			throw new BusinessException("���ݵ���Ч�����ֶβ���Ϊ�գ�������ֵ");
		}

		// 2.��ѯ�˵����Ƿ��Ѿ��������
		String oldPk = PfxxPluginUtils.queryBillPKBeforeSaveOrUpdate(
				swapContext.getBilltype(), swapContext.getDocID());
		if (oldPk != null) {

			// ����жϣ�����ƽ̨�Ѿ�������������ѵ��룬��replace="N"����ôƽ̨�ͻ��׳��쳣����ʾ�����ظ�
			if (swapContext.getReplace().equalsIgnoreCase("N"))
				throw new BusinessException(
						"�������ظ����뵥�ݣ������Ƿ��ǲ����������������ѵ��뵥�ݣ���������ļ���replace��־��Ϊ��Y��");

			IRegmngQueryService voucherbo = (IRegmngQueryService) NCLocator
					.getInstance().lookup(IRegmngQueryService.class.getName());
			AggRegapplyVO preVO = voucherbo.queryByPk(oldPk);

			if (preVO != null && preVO.getParentVO() != null) {

				throw new BusinessException("�����Ѵ��ڣ��������ظ����뵥�ݡ�");

			}
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		String strCode = formatter.format(new Date());
		head.setBill_code(strCode); //

		AggRegapplyVO newBill = this.doActionBill(bill, head);

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

	private AggRegapplyVO doActionBill(AggRegapplyVO bill, RegapplyVO head)
			throws BusinessException {

		IRegmngManageService voucherbo = (IRegmngManageService) NCLocator
				.getInstance().lookup(IRegmngManageService.class.getName());

		AggRegapplyVO res = voucherbo.insertBill(bill);
		res = (AggRegapplyVO) ArrayUtil
				.getFirstInArrays((Object[]) NCPfServiceUtils.processBatch(
						IPFActionName.SAVE, head.getPk_billtype(),
						new AggRegapplyVO[] { (AggRegapplyVO) bill },
						getUserObj(), new WorkflownoteVO()));

		// IRegmngQueryService voucherbo1 = (IRegmngQueryService) NCLocator
		// .getInstance().lookup(IRegmngQueryService.class.getName());
		// AggRegapplyVO preVO = voucherbo1.queryByPk(res.getParentVO()
		// .getPrimaryKey());
		IFlowBizItf itf = NCObject.newInstance(res).getBizInterface(
				IFlowBizItf.class);
		// У�������
		int blPassed = IPfRetCheckInfo.PASSING;
		if (blPassed == IPfRetCheckInfo.PASSING
				&& IPfRetCheckInfo.COMMIT == itf.getApproveStatus()) {
			if (((RegapplyVO) res.getParentVO()).getTrialresult() == null) {
				throw new BusinessException(ResHelper.getString("6009reg",
						"06009reg0030")/* @res "����δ��д���ý��" */);
			}
			if (StringUtils.isNotBlank(getMsg((RegapplyVO) res.getParentVO()))) {
				throw new BusinessException(ResHelper.getString("6009reg",
						"06009reg0031")/*
										 * @res "�����д���δ��д�ı�����"
										 */);
			}
		}
		changeBillData(itf, PubEnv.getPk_user(), PubEnv.getServerTime(),
				"�ⲿ��������", blPassed);
		// ִ����������ǰ����������Ϣд��pub_workflownote��
		WorkflownoteVO worknoteVO = buildWorkflownoteVO(itf,
				PubEnv.getPk_user(), "�ⲿ��������", blPassed, itf.getBilltype());
		getIPersistenceUpdate().insertVO(null, worknoteVO, null);
		((RegapplyVO) bill.getParentVO()).setApprove_state(1);
		res = voucherbo.updateBill(res, false);

		LoginContext context = new LoginContext();
		context.setPk_group(head.getPk_group());
		// ִ��
		IRegmngQueryService voucherbo2 = (IRegmngQueryService) NCLocator
				.getInstance().lookup(IRegmngQueryService.class.getName());
		AggRegapplyVO vo = voucherbo2.queryByPk(res.getParentVO()
				.getPrimaryKey());
		HashMap<String, Object> map = manualExecBills(
				new AggRegapplyVO[] { (AggRegapplyVO) vo }, context,
				head.getRegulardate());

		// result.put(TRNConst.RESULT_MSG, isRunBackgroundTask ? msg :
		// msg.replaceAll("<br>", '\n' + ""));
		// result.put(TRNConst.RESULT_BILLS, passBills);
		if (map != null) {
			String msg = (String) map.get(TRNConst.RESULT_MSG);
			if (msg != null && !"".equals(msg)) {
				throw new BusinessException(msg);
			}
		}
		return res;
	}

	/**
	 * ����һ���������
	 * 
	 * @param itf
	 * @param strApproveId
	 * @param strCheckNote
	 * @param blPassed
	 * @param billtype
	 * @return WorkflownoteVO
	 * @throws BusinessException
	 */
	private WorkflownoteVO buildWorkflownoteVO(IFlowBizItf itf,
			String strApproveId, String strCheckNote, int blPassed,
			String billtype) throws BusinessException {

		WorkflownoteVO worknoteVO = new WorkflownoteVO();
		worknoteVO.setBillid(itf.getBillId());// ����ID
		worknoteVO.setBillVersionPK(itf.getBillId());
		worknoteVO.setChecknote(strCheckNote);// �������
		// ��������
		worknoteVO.setSenddate(PubEnv.getServerTime());
		worknoteVO.setDealdate(PubEnv.getServerTime());// ��������
		// ��֯
		worknoteVO.setPk_org(itf.getPkorg());
		// ���ݱ��
		worknoteVO.setBillno(itf.getBillNo());
		// ������
		String sendman = itf.getApprover() == null ? itf.getBillMaker() : itf
				.getApprover();
		worknoteVO.setSenderman(sendman);
		// Y,����ͨ����N��������ͨ��
		worknoteVO.setApproveresult(IPfRetCheckInfo.NOSTATE == blPassed ? "R"
				: IPfRetCheckInfo.PASSING == blPassed ? "Y" : "N");
		worknoteVO.setApprovestatus(1);// ֱ����״̬
		worknoteVO.setIscheck(IPfRetCheckInfo.PASSING == blPassed ? "Y"
				: IPfRetCheckInfo.NOPASS == blPassed ? "N" : "X");
		worknoteVO.setActiontype("APPROVE");
		worknoteVO.setCheckman(strApproveId);
		// ��������
		worknoteVO.setPk_billtype(billtype);
		worknoteVO.setWorkflow_type(WorkflowTypeEnum.Approveflow.getIntValue());
		return worknoteVO;
	}

	private IPersistenceUpdate getIPersistenceUpdate() {

		return NCLocator.getInstance().lookup(IPersistenceUpdate.class);
	}

	private String getMsg(RegapplyVO billvo) throws BusinessException {

		// ��������ͨ���ĵ���,У�����б������Ŀ�Ƿ��������
		SuperVO[] itemvos = TrnDelegator.getIItemSetQueryService()
				.queryItemSetByOrg(TRNConst.REGITEM_BEANID,
						billvo.getPk_group(), billvo.getPk_org(),
						billvo.getProbation_type());
		IBean ibean = BeanUtil.getBeanEntity(TRNConst.REGITEM_BEANID);
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

	/**
	 * ��д��ְ���������Ϣ
	 * 
	 * @param itf
	 * @param strApproveId
	 * @param strApproveDate
	 * @param strCheckNote
	 * @param intAppState
	 * @throws BusinessException
	 */
	private void changeBillData(IFlowBizItf itf, String strApproveId,
			UFDateTime strApproveDate, String strCheckNote, Integer intAppState)
			throws BusinessException {

		if (itf == null) {
			return;
		}
		itf.setApprover(strApproveId);
		itf.setApproveNote(strCheckNote);
		itf.setApproveDate(strApproveDate);
		itf.setApproveStatus(intAppState);
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
	private RegapplyVO setHeaderDefault(RegapplyVO header)
			throws BusinessException {
		Integer ZERO = Integer.valueOf(0);
		/* ����״̬Ϊδ��� */
		header.setApprove_state(-1);
		header.setDr(ZERO);
		header.setCreationtime(new UFDateTime());
		header.setPk_billtype(TRNConst.BillTYPE_REG);

		return header;
	}

	public HashMap<String, Object> manualExecBills(AggRegapplyVO[] bills,
			LoginContext context, UFLiteralDate effectDate)
			throws BusinessException {
		if (!ArrayUtils.isEmpty(bills)) {
			for (int i = 0; i < bills.length; i++) {
				bills[i].getParentVO().setAttributeValue(
						RegapplyVO.REGULARDATE, effectDate);
			}
		}
		HashMap<String, Object> result = execBills(bills, context, true);
		// modify start: yunana 2013-5-10
		// ����쳣����ǰ̨�ף����ⳬ��֪ͨNC��Ϣ����֮��ع�
		// String msg = (String) result.get(TRNConst.RESULT_MSG);
		// if (StringUtils.isNotBlank(msg))
		// {
		// throw new BusinessException(ResHelper.getString("6009tran",
		// "06009tran0110")/* @res
		// "��������ԭ��,���ֵ���û��ִ�гɹ�:" */+ '\n' + msg);
		// }
		// modify end : yunana 2013-5-10
		return result;
	}

	/**
	 * ִ�е���
	 * 
	 * @param billvos
	 * @param context
	 * @param isRunBackgroundTask
	 * @return HashMap<String, Object>
	 * @throws BusinessException
	 */
	public HashMap<String, Object> execBills(AggRegapplyVO[] billVOs,
			LoginContext context, boolean isRunBackgroundTask)
			throws BusinessException {
		IRegmngQueryService voucherbo = (IRegmngQueryService) NCLocator
				.getInstance().lookup(IRegmngQueryService.class.getName());
		HashMap<String, Object> result = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();// ������Ϣ

		AggregatedValueObject[] retVOs = null;
		if (ArrayUtils.isEmpty(billVOs)) {
			return result;
		}
		retVOs = voucherbo.validateBudget(billVOs, context);

		// ���һ���߼�,������У��ûͨ���ĵ���,Ҳ���뵽������ʾ��
		for (int i = 0; i < billVOs.length; i++) {
			if (isExit(retVOs, billVOs[i])) {
				continue;
			}
			sb.append(ResHelper.getString(
					"6009reg",
					"06009reg0033"/* @res "����{0}���ڱ���У��δͨ�����ܳɹ�ִ�� " */,
					(String) billVOs[i].getParentVO().getAttributeValue(
							RegapplyVO.BILL_CODE))
			/* + "<br>" */);
		}

		if (retVOs == null || retVOs.length == 0) {
			// �����ֶ�û�п�ִ�е���,ֱ�ӷ���
			String msg = sb.length() == 0 ? "" : sb.toString();
			result.put(
					TRNConst.RESULT_MSG,
					isRunBackgroundTask ? msg : msg.replaceAll("<br>",
							'\n' + ""));
			result.put(TRNConst.RESULT_BILLS, null);
			return result;
		}

		// ֻ�Ե��ݽ��б���У��
		ArrayList<AggRegapplyVO> passBills = new ArrayList<AggRegapplyVO>();// ����ִ�гɹ��ĵ���
		IRegmngManageService regService = NCLocator.getInstance().lookup(
				IRegmngManageService.class);
		for (int i = 0; i < retVOs.length; i++) {
			try {
				// ת��ִ��
				doPerfromBill_RequiresNew((AggRegapplyVO) retVOs[i]);
				passBills.add((AggRegapplyVO) retVOs[i]);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				String billcode = (String) retVOs[i].getParentVO()
						.getAttributeValue(RegapplyVO.BILL_CODE);
				if (StringUtils.isBlank(e.getMessage())) {
					sb.append(i
							+ 1
							+ ":"
							+ ResHelper.getString("6009reg",
									"06009reg0034"/*
												 * @res
												 * "����{0}��������δ֪�쳣[{1}]���ܳɹ�ִ��,�����쳣��Ϣ��鿴��־"
												 */, billcode, e.getMessage()) /*
																				 * +
																				 * "<br>"
																				 */);
				} else {
					if (e.getMessage().indexOf(billcode) < 0) {
						// ����쳣��Ϣ��û�г��ֵ��ݺ�,�������쳣��Ϣ
						sb.append(i
								+ 1
								+ ":"
								+ ResHelper.getString("6009reg",
										"06009reg0034"/*
													 * @res
													 * "����{0}��������δ֪�쳣[{1}]���ܳɹ�ִ��,�����쳣��Ϣ��鿴��־"
													 */, billcode,
										e.getMessage()) /*
														 * + "<br>"
														 */);
					} else {
						sb.append(i + 1 + ":" + e.getMessage()/* + "<br>" */);
					}
				}
				continue;
			}

			try {
				AggRegapplyVO agg = voucherbo.queryByPk(retVOs[i].getParentVO()
						.getPrimaryKey());
				doPushBill_RequiresNew(agg);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		// ִ����ɺ�,����֪ͨ
		// 1)����ת����֪ͨ,����ת������֯���� 1001Z7PSN00000000003
		// key-org value-bill_list
		HashMap<String, ArrayList<AggRegapplyVO>> hmTrans = new HashMap<String, ArrayList<AggRegapplyVO>>();
		for (AggregatedValueObject bill : retVOs) {
			String pk_org = (String) bill.getParentVO().getAttributeValue(
					RegapplyVO.PK_ORG);
			if (hmTrans.get(pk_org) == null) {
				hmTrans.put(pk_org, new ArrayList<AggRegapplyVO>());
			}
			hmTrans.get(pk_org).add((AggRegapplyVO) bill);
		}
		for (String key : hmTrans.keySet()) {
			if (hmTrans.get(key) == null || hmTrans.get(key).size() <= 0) {
				continue;
			}

			String tempCode = HICommonValue.msgcode_zhuanzheng;
			HiSendMsgHelper.sendMessage1(tempCode,
					hmTrans.get(key).toArray(new AggRegapplyVO[0]), key);
		}

		// end
		String msg = sb.length() == 0 ? "" : sb.toString();
		result.put(TRNConst.RESULT_MSG,
				isRunBackgroundTask ? msg : msg.replaceAll("<br>", '\n' + ""));
		result.put(TRNConst.RESULT_BILLS, passBills);
		return result;
	}
    public void doPushBill_RequiresNew(AggRegapplyVO aggVO) throws BusinessException
    {
        
        HashMap<String, String> hashPara = new HashMap<String, String>();
        hashPara.put(PfUtilBaseTools.PARAM_NOFLOW, PfUtilBaseTools.PARAM_NOFLOW);
        NCLocator.getInstance().lookup(IplatFormEntry.class).processAction("PUSH", TRNConst.BillTYPE_REG, null, aggVO, null, hashPara);
    }
	 
    private boolean isExit(AggregatedValueObject[] retVOs, AggRegapplyVO billVO) throws BusinessException
    {
        for (int i = 0; retVOs != null && i < retVOs.length; i++)
        {
            if (billVO.getParentVO().getPrimaryKey().equals(retVOs[i].getParentVO().getPrimaryKey()))
            {
                return true;
            }
        }
        return false;
    }
    
	/**
	 * ��������Ϣ_ִ��
	 */
	public void doPerfromBill_RequiresNew(AggRegapplyVO aggVO)
			throws BusinessException {
		if (aggVO == null) {
			return;
		}
		RegapplyVO vo = (RegapplyVO) aggVO.getParentVO();
		if (vo.getTrialresult() == null) {
			throw new BusinessException(ResHelper.getString("6009reg",
					"06009reg0032")/* @res "���ý������Ϊ��!" */);
		}
		switch (vo.getTrialresult()) {
		case TRNConst.TRIALRESULT_PASS:// ���ý����ת��
			updateTrialForPASS(vo);
			break;
		case TRNConst.TRIALRESULT_DELAY:// ���ý�����ӳ���������
			updateTrialForDelay(vo);
			break;
		case TRNConst.TRIALRESULT_FALL:// ���ý����δͨ������
			updateTrialForFall(vo);
			break;
		default:
			break;
		}

		// ��Ա�䶯ʱ�������Ա���������˺������򣬸��ݲ���������Ա����
		updatePsncode_Apply(vo.getPk_psndoc(), vo.getPk_org(), vo);

		// ���µ���״̬Ϊ��ִ��
		aggVO.getParentVO().setAttributeValue(RegapplyVO.APPROVE_STATE,
				HRConstEnum.EXECUTED);
//		IRegmngManageService regService = NCLocator.getInstance().lookup(
//				IRegmngManageService.class);
//		regService.updateBill(aggVO, false);
		
		aggVO.getParentVO().setStatus(VOStatus.UPDATED);
//         setAuditInfoAndTs((SuperVO) billvo.getParentVO(), blChangeAuditInfo);
        getMDPersistenceService().saveBill(aggVO);
//		getServiceTemplate().update(aggVO, false);
	}

	  /***************************************************************************
     * ����Ԫ���ݳ־û��������
     * @return IMDPersistenceService
     *****************************************************************************/
    protected static IMDPersistenceService getMDPersistenceService()
    {
        return MDPersistenceService.lookupPersistenceService();
    }
	/**
	 * �ӳ���������
	 */
	private void updateTrialForDelay(RegapplyVO vo) throws BusinessException {

		if (vo == null) {
			return;
		}
		// ȡ�õ�ǰ��Ч��������Ϣ
		TrialVO[] trialVOs = (TrialVO[]) getIPersistenceRetrieve()
				.retrieveByClause(
						null,
						TrialVO.class,
						"  endflag = 'N' and pk_psnorg ='" + vo.getPk_psnorg()
								+ "' ");
		TrialVO trialVO = trialVOs[0];
		// �������ý���ͼƻ�ת������
		String[] updateFields = { TrialVO.TRIALRESULT, TrialVO.ENDDATE };
		trialVO.setAttributeValue(updateFields[0], vo.getTrialresult());
		trialVO.setAttributeValue(updateFields[1], vo.getTrialdelaydate());
		getPersistenceUpdate().updateVO(null, trialVO, updateFields, null);
	}

	/**
	 * ת��
	 */
	private void updateTrialForFall(RegapplyVO vo) throws BusinessException {

		if (vo == null) {
			return;
		}
		// ȡ�õ�ǰ��Ч��������Ϣ
		TrialVO[] trialVOs = getValidVO(TrialVO.class, vo.getPk_psnorg(), 1);
		// ������Ч��������Ϣ
		if (trialVOs != null && trialVOs.length > 0) {
			TrialVO trialVO = trialVOs[0];
			// �������ý���ͼƻ�ת������
			trialVO.setTrialresult(vo.getTrialresult());
			trialVO.setEnddate(vo.getEnd_date());
			trialVO.setEndflag(UFBoolean.TRUE);
			// ת�����еı�ע�ֶΣ���Ҫͬ�������ü�¼�� heqiaoa 20150609
			trialVO.setMemo(vo.getNewmemo());
			getServiceTemplate().update(trialVO, false);
		}
		// ���¹�����¼��������Ϣ

		PsnJobVO jobVO = getServiceTemplate().queryByPk(PsnJobVO.class,
				vo.getPk_psnjob());
		String[] updateFields = { PsnJobVO.TRIAL_FLAG, PsnJobVO.TRIAL_TYPE };
		jobVO.setAttributeValue(updateFields[0], false);
		jobVO.setAttributeValue(updateFields[1], null);
		getPersistenceUpdate().updateVO(null, jobVO, updateFields, null);
	}

	private IPersistenceRetrieve getIPersistenceRetrieve() {

		return NCLocator.getInstance().lookup(IPersistenceRetrieve.class);
	}

	private IPersistenceUpdate getPersistenceUpdate() {

		return NCLocator.getInstance().lookup(IPersistenceUpdate.class);
	}

	// ��Աת��ʱ�������Ա���������˺������򣬸��ݲ���������Ա����
	public void updatePsncode_Apply(String pk_psndoc, String pk_hrorg,
			RegapplyVO regapplyvo) throws BusinessException {
		// ����Ϊ�ǵ�����²ſ�����Щ
		UFBoolean para = SysinitAccessor.getInstance().getParaBoolean(pk_hrorg,
				"TRN0005");
		if (para != null && para.booleanValue()) {
			BillCodeContext billContext = NCLocator
					.getInstance()
					.lookup(IBillcodeManage.class)
					.getBillCodeContext(HICommonValue.NBCR_PSNDOC_CODE,
							PubEnv.getPk_group(), pk_hrorg);
			// ��Ա�����������Ϊ�����
			if (billContext != null && !billContext.isPrecode()) {
				// ������õ��Ǻ���룬����Ա�䶯ʱ����Ҫ�жϱ�������ж����ҵ��ʵ���ֵ�Ƿ����˱仯���������Ƿ�����������Ա����
				HashMap<String, Object> map = getCodeRule_Apply(pk_hrorg,
						regapplyvo);
				if (map == null) {
					return;
				}
				boolean ischange = (boolean) map.get("ischange");

				if (ischange) {
					PsndocVO[] psndocvo = (PsndocVO[]) NCLocator
							.getInstance()
							.lookup(IPersistenceRetrieve.class)
							.retrieveByClause(null, PsndocVO.class,
									" pk_psndoc = '" + pk_psndoc + "' ");
					IHrBillCode service = NCLocator.getInstance().lookup(
							IHrBillCode.class);
					PsnJobVO psnjobvo = (PsnJobVO) map.get("psnjobvo");
					String[] strCode = service.getLeveledBillCode(
							HICommonValue.NBCR_PSNDOC_CODE,
							PubEnv.getPk_group(), pk_hrorg, psnjobvo, 1);
					psndocvo[0].setCode(strCode[0]);
					psndocvo[0].setStatus(VOStatus.UPDATED);
					NCLocator
							.getInstance()
							.lookup(IPersistenceUpdate.class)
							.updateVO(null, psndocvo[0],
									new String[] { PsndocVO.CODE }, null);
				}
			}
		}
	}

	// ������õ��Ǻ���룬����Ա�䶯ʱ����Ҫ�жϱ�������ж����ҵ��ʵ���ֵ�Ƿ����˱仯���������Ƿ�����������Ա����
	public HashMap<String, Object> getCodeRule_Apply(String pk_hrorg,
			RegapplyVO regapplyvo) throws BusinessException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean ischange = false;
		BillCodeRuleVO rulevo = getBillCodeRuleVOFromDB(
				HICommonValue.NBCR_PSNDOC_CODE, PubEnv.getPk_group(), pk_hrorg);
		BillCodeElemVO[] elems = rulevo.getElems();
		// ���ҵ��ʵ��elemtype=1���ֶ�ץȡ��������Ҫ�Աȱ䶯ǰ���ֵ�������Ƿ�����������Ա����
		if (!ArrayUtils.isEmpty(elems)) {
			List<String> liststr = new ArrayList<String>();
			for (int i = 0; i < elems.length; i++) {
				if (elems[i].getElemtype() == 1) {
					liststr.add(elems[i].getElemvalue());
				}
			}
			String[] str = liststr.toArray(new String[0]);
			if (ArrayUtils.isEmpty(str)) {// ����ĺ���������û��ѡ��ҵ��ʵ�壬�����һֱ������Ҫ��������
				return null;
			}

			PsnJobVO psnjobvo = new PsnJobVO();
			for (int i = 0; i < str.length; i++) {
				String name = str[i];
				String newdata = regapplyvo.getAttributeValue("new" + name) == null ? ""
						: regapplyvo.getAttributeValue("new" + name).toString();
				String olddata = regapplyvo.getAttributeValue("old" + name) == null ? ""
						: regapplyvo.getAttributeValue("old" + name).toString();
				if (!newdata.equals(olddata)) {
					ischange = true;
					psnjobvo.setAttributeValue(name, newdata);
				}
			}

			map.put("ischange", ischange);
			map.put("psnjobvo", psnjobvo);

		}
		return map;
	}

	/**
	 * ���ݵ������ͱ���õ��õ������͵ĵ��ݺŹ���
	 * 
	 * @param billTypeCode
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 * @throws SystemException
	 * @throws ValidationException
	 */
	private BillCodeRuleVO getBillCodeRuleVOFromDB(String nbcrcode,
			String pk_group, String pk_org) throws BusinessException {
		BillCodeRuleVO rulevo;
		try {
			IBillcodeRuleQryService service = NCLocator.getInstance().lookup(
					IBillcodeRuleQryService.class);
			rulevo = service.qryBillCodeRule(nbcrcode, pk_group, pk_org);
		} catch (Exception e) {
			Logger.error("Error occurs while querying BillCodeRule", e);
			throw new BusinessException(
					"Error occurs while querying BillCodeRule", e);
		}
		if (rulevo == null)
			throw new nc.vo.pub.ValidationException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("101704", "UPP101704-000009",
							null, new String[] { nbcrcode }));// /*@res
		// "�õ������ͻ�û�ж��������򣬻�õ��ݺ�ʧ�ܣ��������ͱ���Ϊ{0}"*/+billTypeCode+"'");
		return rulevo;
	}

	/**
	 * ת��
	 */
	private void updateTrialForPASS(RegapplyVO vo) throws BusinessException {

		if (vo == null) {
			return;
		}
		// ȡ�õ�ǰ��Ч��������Ϣ
		TrialVO[] trialVOs = getValidVO(TrialVO.class, vo.getPk_psnorg(), 1);
		// ������Ч��������Ϣ
		if (trialVOs != null && trialVOs.length > 0) {
			TrialVO trialVO = trialVOs[0];
			// �������ý���ͼƻ�ת������
			if (trialVO.getEnddate() == null) {
				trialVO.setEnddate(vo.getRegulardate());
			}
			trialVO.setTrialresult(vo.getTrialresult());
			trialVO.setEnddate(vo.getEnd_date());
			trialVO.setRegulardate(vo.getRegulardate());
			trialVO.setEndflag(UFBoolean.TRUE);
			// ת�����еı�ע�ֶΣ���Ҫͬ�������ü�¼�� heqiaoa 20150609
			trialVO.setMemo(vo.getNewmemo());
			getServiceTemplate().update(trialVO, false);
		}
		// 4-2)����һ��������¼
		PsnJobVO newVO = createNewPsnjob(vo);
		newVO = NCLocator
				.getInstance()
				.lookup(IPersonRecordService.class)
				.addNewPsnjob(
						newVO,
						vo.getIfsynwork() == null ? false : vo.getIfsynwork()
								.booleanValue(), vo.getPk_org());
	}

	/**
	 * ������֯��ϵ��������ְID�õ�ĳ�Ӽ������¼�¼
	 * 
	 * @param <T>
	 * @param className
	 * @param pk_psnorg
	 * @param assgid
	 * @return < T extends SuperVO > T
	 * @throws BusinessException
	 */
	private <T> T[] getValidVO(Class<T> className, String pk_psnorg,
			Integer assgid) throws BusinessException {

		String where = " pk_psnorg = '" + pk_psnorg + "' and endflag = 'N' ";
		if (assgid != null) {
			where += " and assgid = " + assgid.intValue();
		}
		return getServiceTemplate().queryByCondition(className, where);
	}

	/**
	 * ������֯��ϵ��������ְID�õ�ĳ�Ӽ������¼�¼
	 * 
	 * @param <T>
	 * @param className
	 * @param pk_psnorg
	 * @param assgid
	 * @return < T extends SuperVO > T
	 * @throws BusinessException
	 */
	private <T> T[] getLastVO(Class<T> className, String pk_psnorg,
			Integer assgid) throws BusinessException {

		String where = " pk_psnorg = '" + pk_psnorg + "' and lastflag = 'Y' ";
		if (assgid != null) {
			where += " and assgid = " + assgid.intValue();
		}
		return getServiceTemplate().queryByCondition(className, where);
	}

	private final String DOC_NAME = "RegmngManage";

	private SimpleDocServiceTemplate serviceTemplate;

	private SimpleDocServiceTemplate getServiceTemplate() {

		if (serviceTemplate == null) {
			serviceTemplate = new SimpleDocServiceTemplate(DOC_NAME);
		}
		return serviceTemplate;
	}

	private PsnJobVO createNewPsnjob(RegapplyVO bill) throws BusinessException {

		// ��ת����Ŀ��Ӧ���ֶθ�ֵ�����������ֵ��ʹ�õ����е�ֵ����ʹ����һ����¼��ֵ
		// �õ���һ����¼
		PsnJobVO[] lastvo = getLastVO(PsnJobVO.class, bill.getPk_psnorg(), 1);
		//

		PsnJobVO psnjob = new PsnJobVO();
		// ����ְ��ʼ���� = ת������
		if (bill.getRegulardate() != null) {
			psnjob.setBegindate(bill.getRegulardate());
		} else {
			psnjob.setBegindate(PubEnv.getServerLiteralDate());
		}
		psnjob.setEnddate(null);
		psnjob.setEndflag(UFBoolean.FALSE);
		psnjob.setIsmainjob(UFBoolean.TRUE);
		psnjob.setLastflag(UFBoolean.TRUE);
		psnjob.setPk_hrgroup(bill.getPk_group());
		psnjob.setPk_group(bill.getPk_group());
		psnjob.setPk_hrorg(bill.getPk_org());
		psnjob.setPk_psndoc(bill.getPk_psndoc());
		psnjob.setPk_psnorg(bill.getPk_psnorg());
		psnjob.setPk_psnjob(null);
		psnjob.setPsntype(0);
		psnjob.setAssgid(1);
		// ת����Ĭ��Ϊ
		psnjob.setPoststat(UFBoolean.TRUE);
		// ������Ϣ
		psnjob.setTrial_type(null);
		psnjob.setRecordnum(0);
		psnjob.setTrnsevent((Integer) ((MDEnum) TrnseventEnum.REGAPPLY).value());
		psnjob.setTrnstype(TRNConst.TRANSTYPE_REG);
		psnjob.setTrial_flag(UFBoolean.FALSE);
		psnjob.setShoworder(9999999);
		// ������Ϣ
		psnjob.setOribilltype(TRNConst.BillTYPE_REG);
		psnjob.setOribillpk(bill.getPk_hi_regapply());
		// Ա����Ҫʹ����һ����Ա����
		psnjob.setClerkcode(lastvo[0].getClerkcode());

		for (String name : bill.getAttributeNames()) {
			// ��λ��ְ�����Ϣ����ǰһ��������
			if (name.startsWith("new")) {
				Object value = bill.getAttributeValue(name);
				psnjob.setAttributeValue(name.substring(3), value);
			}
		}

		UFBoolean blPoststate = UFBoolean.FALSE;

		// �Ƿ��ڸ� �ڵ�������ʱҪ���⴦��
		TrnRegItemVO[] tempvos = TrnDelegator.getIItemSetQueryService()
				.queryItemSetByOrg(TRNConst.REGITEM_BEANID, bill.getPk_group(),
						bill.getPk_org(), bill.getProbation_type());
		TrnRegItemVO vo = null;
		for (int i = 0; tempvos != null && i < tempvos.length; i++) {
			if (!"newpoststat".equals(tempvos[i].getItemkey())) {
				continue;
			}
			vo = tempvos[i];
		}
		if (vo == null) {
			// ������Ŀ�У�û���Ƿ��ڸڣ�ʹ����һ������Ϣ
			blPoststate = lastvo[0].getPoststat();
		} else {
			if (vo.getIsedit() != null && vo.getIsedit().booleanValue()) {
				// ������Ŀ����,���ҿ��Ե���,ʹ�õ����е�
				blPoststate = bill.getNewpoststat();
			} else {
				// ���ܵ��� ʹ�ù�����¼�е�
				blPoststate = lastvo[0].getPoststat();
			}
		}

		psnjob.setPoststat(blPoststate);

		return psnjob;
	}

	/**
	 * У��ͨ���ĵ�����Ϣ
	 * 
	 * @param vos
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	private void validate(AggRegapplyVO[] vos) throws BusinessException {
		String errMsg = "";
		for (AggRegapplyVO vo : vos) {
			RegapplyVO billvo = (RegapplyVO) vo.getParentVO();
			if (IPfRetCheckInfo.PASSING == billvo.getApprove_state()) {
				if (billvo.getTrialresult() == null) {
					errMsg += ResHelper.getString("6009reg", "06009reg0030")/*
																			 * @res
																			 * "����δ��д���ý��"
																			 */;
					continue;
				}
				if (StringUtils.isNotBlank(getMsg(billvo))) {
					errMsg += ResHelper.getString("6009reg", "06009reg0031")/*
																			 * @res
																			 * "�����д���δ��д�ı�����"
																			 */;
					continue;
				}
			}
		}
		if (StringUtils.isNotBlank(errMsg)) {
			throw new BusinessException(errMsg);
		}
	}

}