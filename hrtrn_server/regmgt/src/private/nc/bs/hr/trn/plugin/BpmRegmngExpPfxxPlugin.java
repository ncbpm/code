package nc.bs.hr.trn.plugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.hr.utils.PubEnv;
import nc.hr.utils.ResHelper;
import nc.itf.hr.frame.IPersistenceUpdate;
import nc.itf.trn.IItemSetAdapter;
import nc.itf.trn.TrnDelegator;
import nc.itf.trn.regmng.IRegmngManageService;
import nc.itf.trn.regmng.IRegmngQueryService;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.data.access.NCObject;
import nc.md.model.IBean;
import nc.vo.arap.utils.ArrayUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pflow.PfUserObject;
import nc.vo.pubapp.util.NCPfServiceUtils;
import nc.vo.trn.pub.BeanUtil;
import nc.vo.trn.pub.TRNConst;
import nc.vo.trn.regmng.AggRegapplyVO;
import nc.vo.trn.regmng.RegapplyVO;
import nc.vo.wfengine.definition.WorkflowTypeEnum;

import org.apache.commons.lang.StringUtils;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * �ڴ˴���Ӵ����������Ϣ
 * </p>//ת������
 * 
 * @author zhaoruic
 * @version Your Project V60
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
//		if (head.getPk_billtype() == null) {
//			throw new BusinessException("���ݵĵ������ͱ����ֶβ���Ϊ�գ�������ֵ");
//		}

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

		AggRegapplyVO newBill = this.insertBill(bill, head);

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

	private AggRegapplyVO insertBill(AggRegapplyVO bill, RegapplyVO head)
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
		// AggRegapplyVO preVO =
		// voucherbo1.queryByPk(res.getParentVO().getPrimaryKey());
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
		voucherbo.updateBill(res, false);
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
	   private boolean isNull(Object o)
	    {
	        if (o == null || o.toString() == null || o.toString().trim().equals(""))
	        {
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

}