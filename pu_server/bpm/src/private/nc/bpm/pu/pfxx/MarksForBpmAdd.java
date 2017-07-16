package nc.bpm.pu.pfxx;

import java.util.ArrayList;
import java.util.List;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.vo.ic.batch.BatchRefViewVO;
import nc.vo.ic.m4n.entity.TransformBodyVO;
import nc.vo.ic.m4n.entity.TransformHeadVO;
import nc.vo.ic.m4n.entity.TransformVO;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m20.entity.PraybillHeaderVO;
import nc.vo.pu.m20.entity.PraybillItemVO;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * ��ͷ����--NC�빺�� 
1. ֱ�Ӹ�����ͷ�������ɴ���Դ���ݺ�Դͷ���ݵ��빺��
BPM���������ɵ��빺�� ���κ� �����ǵ�ǰ��ͷ�� �������
��Դ����Ϊ������Ŷ�Ӧ�����۶��� --- NCʵ��ʵ�����顣
2. ��Դ���۶���������ι����� ���۶���ֻ��һ�����壨��������Ʒ��������Ʒ��֮��һ����Ч���ϣ�
������ʵ������ �����빺�����Բ�ѯ�����۶���
3. �����ͷ�����У�A���������ָ�����Ի����γ���� ����ƷB������ȡ���õ��ִ�����20,�����޸�  
����Ҫ����һ����Bת����B�� �����ε���̬ת������
�µ� ���κ��� �������
ת��ǰ��Ĳֿ⣬���ڲ�ƷB������
 * 
 * @author liyf
 * 
 * 
 */
public class MarksForBpmAdd extends AbstractPfxxPlugin {
	
	private int power = 2;// ����


	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		PraybillVO bill = (PraybillVO) vo;
		//
		checkData(bill);
		// �������:��Ҫ�빺�ĺ���Ҫ��̬ת��������
		List<PraybillItemVO> praylist = getPrayList(bill);

		List<PraybillItemVO> tranlist = getTranList(bill);

		// �����빺��
		if (praylist != null && praylist.size() > 0) {
			PraybillVO praybill = createPrayBill(bill, praylist);
			new MarksForBpmPray().saveAndApprove(praybill);

		}
		// ������̬ת����
		if (tranlist != null && tranlist.size() > 0) {
			TransformVO bill2 = createTransBill(bill, tranlist);
			new MarksForBpmTrans().save(bill2);
		}

		return "��д�ɹ�";
	}

	
	/***
	 * ������̬ת����
	 * 
	 * @param bill
	 * @param praylist
	 * @throws BusinessException
	 */
	private PraybillVO createPrayBill(PraybillVO bill, List<PraybillItemVO> praylist)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		PraybillVO aggvo = new PraybillVO();
		PraybillHeaderVO head = new PraybillHeaderVO();
		for(String attr:head.getAttributeNames()){
			head.setAttributeValue(attr, bill.getHVO().getAttributeValue(attr));
		}
		head.setVmemo(bill.getHVO().getVbillcode());
		head.setVbillcode(null);
		aggvo.setHVO(head);
		aggvo.setBVO(praylist.toArray(new PraybillItemVO[0]));

		return aggvo;
	}

	/**
	 * ������̬ת����
	 * 
	 * @param bill
	 * @param tranlist
	 * @throws BusinessException 
	 */
	private TransformVO createTransBill(PraybillVO bill, List<PraybillItemVO> tranlist) throws BusinessException {
		// TODO �Զ����ɵķ������
		TransformVO tranbill = new TransformVO();
		TransformHeadVO head = new TransformHeadVO();
		tranbill.setParentVO(head);
		// ������֯-�ɹ���֯-�����֯
		head.setAttributeValue("pk_group",bill.getHVO().getAttributeValue("pk_group"));
		head.setAttributeValue("pk_org",bill.getHVO().getAttributeValue("pk_org"));
		head.setAttributeValue("pk_org_v",bill.getHVO().getAttributeValue("pk_org_v"));
		head.setAttributeValue("dbilldate",bill.getHVO().getAttributeValue("dbilldate"));
		head.setAttributeValue("billmaker",bill.getHVO().getAttributeValue("billmaker"));
		head.setAttributeValue("cdptid",bill.getHVO().getAttributeValue("cdptid"));
		head.setAttributeValue("cdptvid",
				bill.getHVO().getAttributeValue("cdptvid"));
		head.setVnote(bill.getHVO().getVbillcode());
		head.setVtrantypecode("4N-01");
		head.setAttributeValue(TransformHeadVO.APPROVER, head.getAttributeValue("approver"));
		head.setAttributeValue(TransformHeadVO.CREATOR, head.getAttributeValue("billmaker"));

		head.setVnote(bill.getHVO().getVbillcode());
		ArrayList<TransformBodyVO> tlist = new ArrayList<TransformBodyVO>();
		for (PraybillItemVO item : tranlist) {
			// ת��ǰ����
			TransformBodyVO body = createbody(head,item);
			// ת�������:�������۶���
			TransformBodyVO afbody = createAfBody(bill, body);
			afbody.setFbillrowflag(3);
			tlist.add(body);
			tlist.add(afbody);

		}
		tranbill.setChildrenVO(tlist.toArray(new TransformBodyVO[0]));
		return tranbill;
	}

	/**
	 * ����ѡ��Ĵ�ת��������
	 * 
	 * @param item
	 * @return
	 * @throws BusinessException 
	 */
	private TransformBodyVO createbody(TransformHeadVO head,PraybillItemVO item) throws BusinessException {
		// TODO �Զ����ɵķ������
		TransformBodyVO body = new TransformBodyVO();
		body.setPk_group(head.getPk_group());
		body.setPk_org(head.getPk_org());
		body.setPk_org_v(head.getPk_org_v());
		body.setCorpoid(head.getCorpoid());
		body.setCorpvid(head.getCorpvid());
		body.setCbodywarehouseid(head.getCwarehouseid());
		// fbillrowflag ��״̬ fbillrowflag int ��̬ת�������� 2=ת��ǰ��3=ת����
		body.setFbillrowflag(2);
		// cbodywarehouseid ���ֿ�
		body.setCbodywarehouseid(item.getPk_reqstor());
		// h��ǰ����
		
		body.setCmaterialvid(item.getPk_material());
		body.setCmaterialoid(item.getPk_srcmaterial());
		setInvFree(body, item);
		
		body.setCunitid(item.getCunitid());
		body.setCasscustid(item.getCastunitid());
		body.setNnum(getUFDdoubleNullASZero(item.getNnum()).setScale(power,
				UFDouble.ROUND_HALF_UP));
		body.setNassistnum(getUFDdoubleNullASZero(item.getNastnum()).setScale(power,
				UFDouble.ROUND_HALF_UP));
		body.setVchangerate(item.getVchangerate());
		// ���κ�
		body.setVbatchcode(item.getVbatchcode());
		
		//ͬ����ά����Ϣ
		BatchCodeRule batchCodeRule = new BatchCodeRule();
		BatchRefViewVO batchRefViewVOs = batchCodeRule.getRefVO(body.getPk_org(),body.getCbodywarehouseid(),body.getCmaterialvid(), body.getVbatchcode());
		if (batchRefViewVOs != null ) {
			batchCodeRule.synBatch(batchRefViewVOs, body);
		}
		return body;
	}
	private TransformBodyVO createAfBody(PraybillVO bill, TransformBodyVO bodyBefore) throws BusinessException {
		// TODO �Զ����ɵķ������
		TransformBodyVO bodyAfter = new TransformBodyVO();
		for(String attr:bodyAfter.getAttributeNames()){
			bodyAfter.setAttributeValue(attr, bodyBefore.getAttributeValue(attr));
		}
		// fbillrowflag ��״̬ fbillrowflag int ��̬ת�������� 2=ת��ǰ��3=ת����
		bodyAfter.setFbillrowflag(3);
		// �������κŵ������۶�������
		bodyAfter.setVbatchcode(bill.getHVO().getVbillcode());
		bodyAfter.setPk_batchcode(null);
		return bodyAfter;
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
	 * ����������������
	 * 
	 * @param body
	 * @param item
	 */
	private void setInvFree(TransformBodyVO body, PraybillItemVO item) {
		// TODO �Զ����ɵķ������
		body.setCprojectid(item.getCprojectid());
		body.setCproductorid(item.getCproductorid());
		for (int i = 1; i <= 10; i++) {
			String key = "vfree" + i;
			body.setAttributeValue(key, item.getAttributeValue(key));
		}
	}


	/**
	 * �����Ҫ�����빺��������
	 * 
	 * @param bill
	 * @return
	 * @throws BusinessException
	 */
	private List<PraybillItemVO> getTranList(PraybillVO bill)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		List<PraybillItemVO> praylist = new ArrayList<PraybillItemVO>();
		for (PraybillItemVO body : bill.getBVO()) {
			if ("trans".equalsIgnoreCase(body.getVbmemo())) {

				if (StringUtil.isSEmptyOrNull(body.getVbatchcode())
						|| StringUtil.isSEmptyOrNull(body.getPk_batchcode())
						|| StringUtil.isSEmptyOrNull(body.getPk_reqstor())) {
					throw new BusinessException("me too �����ָ�����κ�+��������+�ֿ�");
				}
				praylist.add(body);
			}
		}
		return praylist;
	}

	/**
	 * �����Ҫ������̬ת����������
	 * 
	 * @param bill
	 * @return
	 */
	private List<PraybillItemVO> getPrayList(PraybillVO bill) {
		// TODO �Զ����ɵķ������
		List<PraybillItemVO> praylist = new ArrayList<PraybillItemVO>();
		for (PraybillItemVO body : bill.getBVO()) {
			if (!"trans".equalsIgnoreCase(body.getVbmemo())) {
				praylist.add(body);
			}
		}
		return praylist;
	}

	private void checkData(PraybillVO bill) throws BusinessException {
		// TODO �Զ����ɵķ������
		if (bill == null || bill.getParentVO() == null)
			throw new BusinessException("δ��ȡ��ת���������");
		if (bill.getChildrenVO() == null || bill.getChildrenVO().length == 0) {
			throw new BusinessException("���岻����Ϊ��");
		}
		for (PraybillItemVO body : bill.getBVO()) {

		}

	}

}
