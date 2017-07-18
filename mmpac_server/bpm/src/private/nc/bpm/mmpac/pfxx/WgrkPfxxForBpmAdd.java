package nc.bpm.mmpac.pfxx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.env.ICBSContext;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.itf.mmpac.wr.IWrBusinessService;
import nc.itf.mmpac.wr.IWrMaintainService;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.pubitf.scmf.ic.mbatchcode.IBatchcodePubService;
import nc.util.mmpub.dpub.base.ValueCheckUtil;
import nc.vo.ic.pub.check.VOCheckUtil;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.mmpac.pacpub.consts.MMPacBillTypeConstant;
import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
import nc.vo.mmpac.pmo.pac0002.entity.PMOItemVO;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.mmpac.wr.entity.WrVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.util.VORowNoUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;

import org.apache.commons.lang.StringUtils;

/**
 * �����깤���
 * 1. һ�������ж��У�һ������һ�㲻��һ����ξ�������ɵġ�Ӧ����ÿ������������Լ������룬�깤��ɨ����д�깤����������
2. ���飬���ζ�ͨ�����봫������
3. ����֧��һ�����������ηִ��깤����һ�������Զ���깤���棬 һ��������ϸ�У���Ӧ����깤��ϸ��
3.2 ���ݻش�����ϸ�������Դ���������������߼���NCҲ������֧�֣���
3. �����д��Դ������Ϣ�Լ��������������Ϣ�����飬ʱ�䣬���������εȣ���NC�Զ�����������������ȫ��Դ��Ϣ��	ȫ���ϵ���������������Ϣ
4. �ӿ��깤���棬�Զ����������ұ������ɱ��쵥��NC��ȫ  ��������������Ϣ
 * @author liyf
 *
 */
public class WgrkPfxxForBpmAdd extends AbstractPfxxPlugin {
	
	private IWrMaintainService service =null;


	public IWrMaintainService getMaintainService() {
		if(this.service==null){
			this.service = NCLocator.getInstance().lookup(IWrMaintainService.class);
		}
		return this.service;
	}

	private List<String> updateIndex = new ArrayList<String>();
	private int power = 8;// ����
	private String billtype = "55A4";

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		AggWrVO bpmBill = (AggWrVO) vo;
		
		checkData(bpmBill);
		// 3.��ѯ��Ӧ�ϵ��������������Ҹ�������������ϸ����
		PMOAggVO[] chgBillS = fillData(bpmBill);
		// 4���ݽ���:����һ���깤���浥��������
		// ���������������Ƶ���ͬ�����깤���浥
		AggWrVO[] destVos = PfServiceScmUtil.executeVOChange(
				MMPacBillTypeConstant.PMO, MMPacBillTypeConstant.WR,
				chgBillS);
		// �ϲ�����һ���깤���浥
		List<WrItemVO> body_list = new ArrayList<WrItemVO>();
		for (AggWrVO bill : destVos) {
			WrItemVO[] bodys = bill.getChildrenVO();
			for (WrItemVO body : bodys) {
				body_list.add(body);
			}
		}
		// ����BPM��д������������Ϣ����
		AggWrVO clientVO = destVos[0];
		clientVO.setChildrenVO(body_list.toArray(new WrItemVO[0]));
		updateClientVO(bpmBill, clientVO);
		String auditer = clientVO.getParentVO().getAuditer();
		// ����
		AggWrVO saveVO = doSave(clientVO);
		
		// ����
		clientVO.getParentVO().setAuditer(auditer);
		
		AggWrVO signVO = doSign(saveVO);
		//����
        this.getWrBusinessService().applyCheck(new AggWrVO[]{signVO});

		return saveVO.getParentVO().getVbillcode();

	}
	 protected IWrBusinessService getWrBusinessService() {
	        return NCLocator.getInstance().lookup(IWrBusinessService.class);
	 }

	/**
	 * ����
	 * 
	 * @param swapContext
	 * @param resvo
	 * @return
	 * @throws BusinessException
	 */
	private AggWrVO doSave(AggWrVO resvo) throws BusinessException {
		// ����Ƿ�������
		InvocationInfoProxy.getInstance().setUserId(resvo.getParentVO().getBillmaker());
		Logger.info("�����µ���ǰ����...");
		this.processBeforeSave(resvo);
		// TODO ���������и�����Ϣ��aggxsysvoΪ�û����õľ��帨����Ϣ
		Logger.info("�����µ���...");
		AggWrVO returnVO = this.getMaintainService().insert(new AggWrVO[] {
	                (AggWrVO) resvo
	            })[0];

		Logger.info("�����µ������...");

		Logger.info("�����µ��ݺ���...");
		this.processAfterSave(resvo);

		return returnVO;
	}

	/**
	 * ǩ��
	 * 
	 * @param swapContext
	 * @param resvo
	 * @return
	 * @throws BusinessException
	 */
	private AggWrVO doSign(AggWrVO resvo) throws BusinessException {
		// ����Ƿ�������
		Logger.info("ǩ���µ���ǰ����...");
		InvocationInfoProxy.getInstance().setUserId(resvo.getParentVO().getAuditer());
		// ǩ��ʱ����ڵ�������
		resvo.getParentVO().setTaudittime(resvo.getParentVO().getDbilldate());
		resvo.getParentVO().setAuditer(resvo.getParentVO().getBillmaker());
		InvocationInfoProxy.getInstance().setUserId(resvo.getParentVO().getAuditer());
		resvo.getParentVO().setFbillstatus(2);
		Logger.info("ǩ���µ���...");
		
		AggWrVO returnVO = this.getMaintainService().audit(new AggWrVO[] {
                (AggWrVO) resvo
            })[0];

		Logger.info("ǩ���µ������...");

		Logger.info("ǩ���µ��ݺ���...");

		return returnVO;
	}



	/**
	 * ����BPM��д������������Ϣ���깤���浥
	 * 
	 * @param bill
	 * @param clientVO
	 * @throws BusinessException
	 */
	private void updateClientVO(AggWrVO bpmBill, AggWrVO clientVO)
			throws BusinessException {
		WrItemVO[] clientbodys = (WrItemVO[]) clientVO.getChildrenVO();
		WrItemVO[] bodys = (WrItemVO[]) bpmBill.getChildrenVO();
		// ���ñ�ͷ��Ϣ
		String[] headKeys = bpmBill.getParentVO().getAttributeNames();
		for (String key : headKeys) {
			if(ValueCheckUtil.isEmpty(bpmBill.getParentVO().getAttributeValue(key))){
				continue;
			}
			clientVO.getParentVO().setAttributeValue(key,
					bpmBill.getParentVO().getAttributeValue(key));
		}
//		�������浥��״̬   1=���ɣ�2=����ͨ����3=�ύ��4=�����У�5=������ͨ���� 
		clientVO.getParentVO().setFbillstatus(1);
		
		List<WrItemVO> children = new ArrayList<WrItemVO>();
		children.addAll(Arrays.asList(clientbodys));
		// ���еĴ���
		for (WrItemVO body : bodys) {
			String csourcebillbid = body.getCbmobid();
			for (WrItemVO clientbody : clientbodys) {
				if (!clientbody.getCbmobid().equalsIgnoreCase(
						csourcebillbid)) {
					continue;
				}
				// һ�б��壬���ܻ�д�������
				if (updateIndex.contains(csourcebillbid)) {
					WrItemVO newBody = (WrItemVO) clientbody.clone();
					updateClientBVO(body, newBody);
					children.add(newBody);
				} else {
					updateIndex.add(csourcebillbid);
					updateClientBVO(body, clientbody);
				}

			}
		}

		processBeforeSave(clientVO);

		WrItemVO[] new_bodys = children.toArray(new WrItemVO[0]);

		clientVO.setChildrenVO(new_bodys);
	}

	private void updateClientBVO(WrItemVO body, WrItemVO clientbody)
			throws BusinessException {
		String[] names = body.getAttributeNames();
		
		for(String name :names){
			if(ValueCheckUtil.isEmpty(body.getAttributeValue(name))){
				continue;
			}
			clientbody.setAttributeValue(name, body.getAttributeValue(name));
		}
		clientbody.setNbwrnum(getUFDdoubleNullASZero(body.getNbwrnum()).setScale(power,
				UFDouble.ROUND_HALF_UP));// ʵ������
		clientbody.setNbwrastnum(getUFDdoubleNullASZero(body.getNbwrastnum()).setScale(power,
				UFDouble.ROUND_HALF_UP));
	
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
	 * ��ѯ��������
	 * 
	 * @param bill
	 * @return
	 * @throws BusinessException
	 */
	private PMOAggVO[] fillData(AggWrVO bill) throws BusinessException {
		// TODO �Զ����ɵķ������
		// ���ܴ��ںϲ��깤�����������ϲ���⣬���ǲ���ϲ�����ϸ
		List<String> arrPks = new ArrayList<String>();
		List<String> arrbpks = new ArrayList<String>();
		for (WrItemVO body : bill.getChildrenVO()) {
			if (!arrPks.contains(body.getCbmoid())) {
				arrPks.add(body.getCbmoid());
			}
			if (!arrbpks.contains(body.getCbmobid())) {
				arrbpks.add(body.getCbmobid());
			}
		}
		BillQuery<PMOAggVO> bquery = new BillQuery<>(PMOAggVO.class);
		PMOAggVO[] aggvos = bquery.query(arrPks.toArray(new String[0]));
		if (aggvos == null || aggvos.length == 0) {
			throw new BusinessException("����ָ����������" + arrPks.toString()
					+ "δ��ѯ����������,��˶�.");
		}
		// ��Դ���������������ܴ��ںϲ���⣬��������������Զ�Ӧһ����ⵥ
		List<PMOAggVO> chgBillS = new ArrayList<PMOAggVO>();
		for (PMOAggVO aggvo : aggvos) {
			List<PMOItemVO> arrblist = new ArrayList<PMOItemVO>();
			PMOItemVO[] itemVOs = aggvo.getChildrenVO();
			for (PMOItemVO itemVO : itemVOs) {
				if (arrbpks.contains(itemVO.getPrimaryKey())) {
					arrblist.add(itemVO);
				}
			}
			if (arrblist.size() > 0) {
				PMOAggVO chgArrBill = new PMOAggVO();
				chgArrBill.setParentVO(aggvo.getParentVO());
				chgArrBill.setChildrenVO(arrblist
						.toArray(new PMOItemVO[0]));
				chgBillS.add(chgArrBill);
			}
		}

		//
		if (chgBillS.size() == 0) {
			throw new BusinessException(
					"����ָ��������Csourcebillhid���Բ�ѯ���������������Ǹ���Csourcebillbidδ��ѯ������������ϸ,��˶������Ƿ���ȷ.");
		}

		return chgBillS.toArray(new PMOAggVO[0]);

	}

	private void checkData(AggWrVO bill) throws BusinessException {
		// TODO �Զ����ɵķ������
		WrItemVO[] PMOItemVOs = bill.getChildrenVO();
		if (PMOItemVOs == null || PMOItemVOs.length == 0) {
			throw new BusinessException("��ָ����Ҫ����ı�����.");
		}
		VOCheckUtil
				.checkHeadNotNullFields(bill, new String[] { "dbilldate",
						"billmaker","auditer"});
//		VOCheckUtil.checkBodyNotNullFields(bill, new String[] { "cbmoid",
//				"cbmobid", "vbinbatchcode", "nbwrnum" });

		VOCheckUtil.checkBodyNotNullFields(bill, new String[] {"fbproducttype", "vbinbatchcode", "nbwrnum" });
	}



	/**
	 * ���ݱ���ǰ����
	 * 
	 * @param vo
	 */
	protected void processBeforeSave(AggWrVO vo) throws BusinessException {

		if (null == vo)
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0139")/*
																			 * @res
																			 * "���ݲ���Ϊ��"
																			 */);
		ICBSContext context = new ICBSContext();
		this.headVOProcess(vo.getParentVO(), context);
		this.bodyVOProcess(vo, context);
	}

	/**
	 * ���ݱ������
	 * 
	 * @param vo
	 */
	protected void processAfterSave(AggWrVO vo) throws BusinessException {
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
	private void headVOProcess(WrVO vo, ICBSContext context) {
		vo.setStatus(VOStatus.NEW);
		// ����
		if (StringUtil.isSEmptyOrNull(vo.getPk_group()))
			vo.setPk_group(context.getPk_group());
	
		// ��������
		if (vo.getDbilldate() == null)
			vo.setDbilldate(context.getBizDate());
		vo.setDmakedate(vo.getDbilldate());
		// ����ʱ��
		vo.setCreationtime(new UFDateTime(vo.getDbilldate().toString()));
		vo.setCreator(vo.getBillmaker());
		//
		

		if (StringUtil.isSEmptyOrNull(vo.getVtrantypeid())) {
			// uap��֧�ֵ������͵ķ��룬��ʱ�Խ�������code��ѯid�ķ�ʽ����������
			String vtrantypecode = vo.getVtrantypecode();
			Map<String, String> map = PfServiceScmUtil
					.getTrantypeidByCode(new String[] { vtrantypecode });
			vo.setVtrantypeid(map == null ? null : map.get(vtrantypecode));
		}

	}

	/**
	 * ���ݱ��崦��
	 * 
	 * @param vo
	 * @param context
	 * @throws BusinessException
	 */
	private void bodyVOProcess(AggWrVO vo, ICBSContext context)
			throws BusinessException {
		WrItemVO[] vos = vo.getChildrenVO();
		if (ValueCheckUtil.isNullORZeroLength(vos))
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008001_0", "04008001-0141")/*
																			 * @res
																			 * "���ݱ��岻��Ϊ��"
																			 */);

		VORowNoUtils.setVOsRowNoByRule(vos, WrItemVO.VBROWNO);// �кŴ���

		WrVO head = vo.getParentVO();
		Map<String, BatchcodeVO> batchmap = this.getBatchcodeVO(vos);
		for (WrItemVO body : vos) {
			body.setStatus(VOStatus.NEW);
		// �����κŵ�����������ʱ�� ��Ҫ��ȫ�����������б�Ҫʱ(�����ڹ���)��ȫ�������ں�ʧЧ����
			if (!StringUtils.isEmpty(body.getVbinbatchcode())
					&& StringUtils.isEmpty(body.getVbinbatchid())) {
				BatchcodeVO batchvo = batchmap.get(body.getCbmainmaterialid()
						+ body.getVbinbatchcode());
				if (batchvo != null) {
					body.setVbinbatchid(batchvo.getPk_batchcode());
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
	private Map<String, BatchcodeVO> getBatchcodeVO(WrItemVO[] vos) {
		List<String> cmaterialvidList = new ArrayList<String>();
		List<String> vbatchcodeList = new ArrayList<String>();
		Set<String> materialbatch = new HashSet<String>();
		for (WrItemVO body : vos) {
			if (body.getCbmainmaterialid() != null && body.getVbinbatchcode() != null) {
				if (materialbatch.contains(body.getCbmainmaterialid()
						+ body.getVbinbatchcode())) {
					continue;
				}
				cmaterialvidList.add(body.getCbmainmaterialid());
				vbatchcodeList.add(body.getVbinbatchcode());
				materialbatch
						.add(body.getCbmainmaterialid() + body.getVbinbatchcode());
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
	private void bodyVOCopyFromHeadVO(WrItemVO body, WrVO head) {
		body.setPk_group(head.getPk_group());
		body.setPk_org(head.getPk_org());
		body.setPk_org_v(head.getPk_org_v());
		
	}

}