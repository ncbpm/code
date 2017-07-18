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
import nc.impl.pu.m20.action.PraybillInsertAction;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.pf.IPFBusiAction;
import nc.pubitf.org.cache.IBasicOrgUnitPubService_C;
import nc.pubitf.scmf.ic.mbatchcode.IBatchcodePubService;
import nc.scmmm.vo.scmpub.scale.BillVOScaleCheckProcessor;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pu.m20.entity.PraybillHeaderVO;
import nc.vo.pu.m20.entity.PraybillItemVO;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pu.m20.rule.PrayBillScaleRule;
import nc.vo.pu.pub.enumeration.POEnumBillStatus;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.so.m30.entity.SaleOrderHVO;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
/**
 * �빺���ı��������
 * @author Administrator
 *
 */
public class MarksForBpmPray {

	public AggregatedValueObject saveAndApprove(PraybillVO bill) throws BusinessException {
		
		String approver = bill.getHVO().getApprover();
		UFDate dbilldate = bill.getHVO().getDbilldate();

		// ����ǰ��ȫ����
		fillDataForPrayBill(bill);
		//
		AggregatedValueObject insertvo = insert(bill);
		//���²�ѯ���Ⲣ��
		PraybillVO queryVOByPk = (PraybillVO) queryVOByPk(insertvo.getParentVO().getPrimaryKey());
		
		queryVOByPk.getHVO().setApprover(approver);
		queryVOByPk.getHVO().setTaudittime(dbilldate);
		//ִ������
		AggregatedValueObject approvevo = approve(queryVOByPk); 
		return approvevo;
	}

	protected AggregatedValueObject insert(PraybillVO vo) {
		if (vo != null) {
			this.checkScale(vo);
		}
		InvocationInfoProxy.getInstance().setUserId(vo.getHVO().getBillmaker());
		return new PraybillInsertAction()
				.insert(new PraybillVO[] { (PraybillVO) vo })[0];
	}
	
	protected AggregatedValueObject approve(PraybillVO vo) throws BusinessException {
		if (vo != null) {
			this.checkScale(vo);
		}		
		InvocationInfoProxy.getInstance().setUserId(vo.getHVO().getApprover());
		IPFBusiAction service = NCLocator.getInstance().lookup(
				IPFBusiAction.class);
		AggregatedValueObject [] vos = (AggregatedValueObject[]) service.processAction(IPFActionName.APPROVE, "20", null, vo, null, null);
		 
		 return vos[0];
	
	}


	protected AggregatedValueObject queryVOByPk(String voPk) {
		BillQuery<PraybillVO> billquery = new BillQuery<PraybillVO>(
				PraybillVO.class);
		PraybillVO[] praybills = billquery.query(new String[] { voPk });
		if (ArrayUtils.isEmpty(praybills)) {
			return null;
		}
		return praybills[0];
	}

	/**
	 * �ϸ��������/����/���ľ�����ϵͳһ�£���һ��ʱ���ɵ���
	 * 
	 * @param vo
	 */
	private void checkScale(AggregatedValueObject vo) {
		String pk_group = (String) vo.getParentVO().getAttributeValue(
				PraybillHeaderVO.PK_GROUP);
		BillVOScaleCheckProcessor scale = new BillVOScaleCheckProcessor(
				pk_group, new PraybillVO[] { (PraybillVO) vo });
		new PrayBillScaleRule().setScaleForCheck(scale);
	}

	private void fillDataForPrayBill(PraybillVO bill) throws BusinessException {
		// TODO �Զ����ɵķ������
		// �������������+��֯��ѯ���۶���
		PraybillHeaderVO head = bill.getHVO();
		VOQuery<SaleOrderHVO> query = new VOQuery<SaleOrderHVO>(
				SaleOrderHVO.class);
		SaleOrderHVO[] hvos = query.query(" and vbillcode='" + head.getVmemo()+ "'", null);
		if (hvos == null || hvos.length == 0) {
			throw new BusinessException("�����������" + head.getVmemo()
					+ "δ��ѯ��NC���۶���");
		}
		//
		ICBSContext context = new ICBSContext();
		this.headVOProcess(head, context);
		for (PraybillItemVO body : bill.getBVO()) {
//			body.setCsourcetypecode("30");
//			body.setCsourceid(hvos[0].getCsaleorderid());
//			body.setCsourcebid(hvos[0].getCsaleorderid());

		}
		
		this.bodyVOProcess(bill, context);

	}

	private void headVOProcess(PraybillHeaderVO vo, ICBSContext context) {
		vo.setStatus(VOStatus.NEW);
		vo.setFbillstatus(POEnumBillStatus.FREE.toInt());
//		  <!--ί��,��󳤶�Ϊ1,����Ϊ:UFBoolean-->
//          <bsctype>N</bsctype>
		vo.setBsctype(UFBoolean.FALSE);
//		  <!--ֱ��,��󳤶�Ϊ1,����Ϊ:UFBoolean-->
//          <bdirecttransit>N</bdirecttransit>
		vo.setBdirecttransit(UFBoolean.FALSE);
//	     <!--�汾��,��󳤶�Ϊ0,����Ϊ:Integer-->
//         <nversion>0</nversion>
         vo.setNversion(0);
		// ����
		if (StringUtil.isSEmptyOrNull(vo.getPk_group()))
			vo.setPk_group(context.getPk_group());
		
		// ��֯�汾
		if (StringUtil.isSEmptyOrNull(vo.getPk_org_v()))
			vo.setPk_org_v(getPkOrg_v(vo.getPk_org()));
		
		// ��ӡ����
		if (vo.getIprintcount() == null)
			vo.setIprintcount(Integer.valueOf(0));
		// ����״̬
		if (vo.getFbillstatus() == null)
			vo.setFbillstatus(3);
		// ��������
		if (vo.getDbilldate() == null)
			vo.setDbilldate(context.getBizDate());
		vo.setDmakedate(vo.getDbilldate());
		// ����ʱ��
		vo.setCreationtime(new UFDateTime(vo.getDbilldate().toString()));
		vo.setCreator(vo.getBillmaker());
		// 25 fpraysource �빺��Դ fpraysource int �빺��Դ
		// 0=MRP�ƻ�������1=MPS�ƻ�������2=����������3=���ƻ�������4=���۶�����5=����������
		// 6=�ʲ�������7=�ֹ�¼�룬8=���ʱ��ϱ�9=�����������룬10=��ɢ����������11=�������ƽ�⣬12=���ں�ͬ��13=�ʲ��������룬14=ά�޼ƻ���
		if (vo.getFpraysource() == null) {
			vo.setFpraysource(4);
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
	 * @param bill
	 * @param context
	 * @throws BusinessException
	 */
	private void bodyVOProcess(PraybillVO bill, ICBSContext context)
			throws BusinessException {
		PraybillItemVO[] vos = bill.getBVO();
		if (ValueCheckUtil.isNullORZeroLength(vos))
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0141")/*
																			 * @res
																			 * "���ݱ��岻��Ϊ��"
																			 */);

		VORowNoUtils.setVOsRowNoByRule(vos, ICPubMetaNameConst.CROWNO);// �кŴ���
		PraybillHeaderVO head = bill.getHVO();
		Map<String, BatchcodeVO> batchmap = this.getBatchcodeVO(vos);
		for (PraybillItemVO body : vos) {
			body.setStatus(VOStatus.NEW);
			if (StringUtil.isSEmptyOrNull(body.getPk_srcmaterial())
					|| StringUtil.isSEmptyOrNull(body.getPk_material()))
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("4008001_0", "04008001-0142")/*
																 * @res
																 * "���ݱ������ϲ���Ϊ��"
																 */);

	
		
			// ����λ
			if (StringUtil.isSEmptyOrNull(body.getCastunitid()))
				body.setCastunitid(context.getInvInfo()
						.getInvBasVO(body.getPk_material()).getPk_stockmeas());

			// �����κŵ�����������ʱ�� ��Ҫ��ȫ�����������б�Ҫʱ(�����ڹ���)��ȫ�������ں�ʧЧ����
			if (!StringUtils.isEmpty(body.getVbatchcode())
					&& StringUtils.isEmpty(body.getPk_batchcode())) {
				BatchcodeVO batchvo = batchmap.get(body.getPk_material()
						+ body.getVbatchcode());
				if (batchvo != null) {
					body.setPk_batchcode(batchvo.getPk_batchcode());
				}
			}
			bodyVOCopyFromHeadVO(body, head);
		}
	}

	/**
	 * ��ȡ���κŵ���
	 * 
	 * @param vos
	 * @return Map<String(cmaterialvid+vbatchcode), BatchcodeVO���ε���>
	 */
	private Map<String, BatchcodeVO> getBatchcodeVO(PraybillItemVO[] vos) {
		List<String> cmaterialvidList = new ArrayList<String>();
		List<String> vbatchcodeList = new ArrayList<String>();
		Set<String> materialbatch = new HashSet<String>();
		for (PraybillItemVO body : vos) {
			if (body.getPk_material() != null && body.getVbatchcode() != null) {
				if (materialbatch.contains(body.getPk_material()
						+ body.getVbatchcode())) {
					continue;
				}
				cmaterialvidList.add(body.getPk_material());
				vbatchcodeList.add(body.getVbatchcode());
				materialbatch
						.add(body.getPk_material() + body.getVbatchcode());
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
	private void bodyVOCopyFromHeadVO(PraybillItemVO body, PraybillHeaderVO head) {
		body.setPk_group(head.getPk_group());
		body.setPk_org(head.getPk_org());
		body.setPk_org_v(head.getPk_org_v());
	}

	
//	---����Ԫ������ָ���Ļ��VO�еĵ���֯�汾

	public String getPkOrg_v(String  pk_org){
			// ��֯�����°汾
		String pk_org_v =null;
			Map<String, String> map;
			UFDate busiDate = AppContext.getInstance().getBusiDate();
			try {
				map = NCLocator
						.getInstance()
						.lookup(IBasicOrgUnitPubService_C.class)
						.getNewVIDSByOrgIDSAndDate(new String[] { pk_org },
								busiDate);
				pk_org_v= map.get(pk_org);
				
				
			} catch (BusinessException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
			return  pk_org_v;
	}

	

}
