package nc.bpm.pu.pfxx;

import java.util.ArrayList;
import java.util.List;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.vo.ic.m4n.entity.TransformBodyVO;
import nc.vo.ic.m4n.entity.TransformHeadVO;
import nc.vo.ic.m4n.entity.TransformVO;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m20.entity.PraybillHeaderVO;
import nc.vo.pu.m20.entity.PraybillItemVO;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.so.m30.entity.SaleOrderBVO;

/**
 * ��ͷ����--NC�빺�� 1. ֱ�Ӹ�����ͷ�������ɴ���Դ���ݺ�Դͷ���ݵ��빺�� ���κ���������ţ�BPM������ ��Դ����Ϊ������Ŷ�Ӧ�����۶��� ---
 * ����Ҫʵ�����顣 2. ��Դ���۶���������ι����� ���۶���ֻ��һ�����壨��������Ʒ��������Ʒ��֮��һ����Ч���ϣ� ������ʵ������
 * �����빺�����Բ�ѯ�� 3. �����ͷ�����У�A���������ָ��Ϊ�Ǳ�������ŵİ�װ��B��������10������Ҫ����һ����Bת����A����̬ת������
 * ���κ�=������ţ�ֻ�ǰ�װ��Ż���,bpm��Ҫ���ˣ�
 * 
 * @author liyf
 * 
 * 
 */
public class MarksForBpmAdd extends AbstractPfxxPlugin {

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
		aggvo.setHVO(bill.getHVO());
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
		head.setAttributeValue("pk_group",
				bill.getHVO().getAttributeValue("pk_group"));
		head.setAttributeValue("pk_org",
				bill.getHVO().getAttributeValue("pk_org"));
		head.setAttributeValue("pk_org_v",
				bill.getHVO().getAttributeValue("pk_org_v"));
		head.setAttributeValue("dbilldate",
				bill.getHVO().getAttributeValue("dbilldate"));
		head.setAttributeValue("billmaker",
				bill.getHVO().getAttributeValue("billmaker"));
		head.setAttributeValue("cdptid",
				bill.getHVO().getAttributeValue("cdptid"));
		head.setAttributeValue("cdptid",
				bill.getHVO().getAttributeValue("cdptid"));
		head.setAttributeValue("cdptvid",
				bill.getHVO().getAttributeValue("cdptvid"));
		head.setAttributeValue("vbillcode",
				bill.getHVO().getAttributeValue("vbillcode"));
		head.setVtrantypecode("4N-01");
		head.setVnote("��ͷ�Զ�ת��");
		ArrayList<TransformBodyVO> tlist = new ArrayList<TransformBodyVO>();
		for (PraybillItemVO item : tranlist) {
			// ת��ǰ����
			TransformBodyVO body = createbody(item);
			// ת�������:�������۶���
			TransformBodyVO afbody = createAfBody(bill, item);
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
	 */
	private TransformBodyVO createbody(PraybillItemVO item) {
		// TODO �Զ����ɵķ������
		TransformBodyVO body = new TransformBodyVO();
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
		body.setNnum(item.getNnum());
		body.setNassistnum(item.getNastnum());
		body.setVchangerate(item.getVchangerate());
		
		// ���κ�
		body.setVbatchcode(item.getVbatchcode());
		return body;
	}
	private TransformBodyVO createAfBody(PraybillVO bill, PraybillItemVO item) throws BusinessException {
		// TODO �Զ����ɵķ������
		TransformBodyVO body = new TransformBodyVO();
		// fbillrowflag ��״̬ fbillrowflag int ��̬ת�������� 2=ת��ǰ��3=ת����
		body.setFbillrowflag(3);
		// cbodywarehouseid ���ֿ�
		body.setCbodywarehouseid(item.getPk_reqstor());
		// �������۶�����ѯ��Ӧ��ת��������ϣ�
		SaleOrderBVO saleOrderBody = querySaleOrder(bill);
		body.setCmaterialvid(saleOrderBody.getCmaterialid());
		body.setCmaterialoid(saleOrderBody.getCmaterialvid());
	
		setInvFree(body, saleOrderBody);
		body.setCunitid(saleOrderBody.getCunitid());
		body.setCasscustid(saleOrderBody.getCastunitid());
		
		body.setNnum(item.getNnum());
		body.setNassistnum(item.getNastnum());
		body.setVchangerate(saleOrderBody.getVchangerate());//��ǰ����1/1,�����»���
	
		// �������κŵ������۶�������
		body.setVbatchcode(bill.getHVO().getVbillcode());
		return body;
	}
	
	/**
	 * �������۶�����������������������
	 * @param body
	 * @param item
	 */
	private void setInvFree(TransformBodyVO body, SaleOrderBVO item) {
		// TODO �Զ����ɵķ������
		body.setCprojectid(item.getCprojectid());
		body.setCproductorid(item.getCproductorid());
		for (int i = 1; i <= 10; i++) {
			String key = "vfree" + i;
			body.setAttributeValue(key, item.getAttributeValue(key));
		}
	
		
	}

	private SaleOrderBVO querySaleOrder(PraybillVO bill)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		PraybillHeaderVO head = bill.getHVO();
		VOQuery<SaleOrderBVO> query = new VOQuery<SaleOrderBVO>(
				SaleOrderBVO.class);
		SqlBuilder sql = new SqlBuilder();
		sql.append(" and nvl(so_saleorder_b.dr,0) =0 ");
		sql.append(" and nvl(so_saleorder_b.blargessflag,'N')='N'");
		sql.append(" and so_saleorder_b.csaleorderid = ( select csaleorderid  from so_saleorder where nvl(dr,0)=0 ");
		sql.append(" and pk_org='" + head.getPk_org() + "' and vbillcode='"
				+ head.getVbillcode() + "' )");
		SaleOrderBVO[] bvos = query.query(sql.toString(), null);
		if (bvos == null || bvos.length == 0) {
			throw new BusinessException("�����������" + head.getVbillcode()
					+ "δ��ѯ��NC���۶���");
		}
		if(bvos.length != 1){
			throw new BusinessException("�����������" + head.getVbillcode()
					+ "��ѯ��NC���۶������������ж������壬�޷�ȷ��");
		}
		return bvos[0];
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
