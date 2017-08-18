package nc.bs.ic.m46.insert.rule;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.base.ICRule;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.itf.ic.reserve.IReserveAssist;
import nc.itf.ic.reserve.IReserveMaintenance;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.pubitf.bd.bom.ic.IPubBomServiceForInbound;
import nc.vo.am.proxy.AMProxy;
import nc.vo.bd.bom.bom0202.enumeration.OutputTypeEnum;
import nc.vo.bd.bom.bom0202.paramvo.ic.BomParamForInbound;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.m46.entity.FinProdInBodyVO;
import nc.vo.ic.m46.entity.FinProdInHeadVO;
import nc.vo.ic.m46.entity.FinProdInVO;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.reserve.entity.ReserveBillVO;
import nc.vo.ic.reserve.entity.ReserveVO;
import nc.vo.ic.reserve.pub.ResRequireQueryParam;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

//����Ʒ�������ǰ������۶������ɵ����۶���, ���������������۶��������ϣ��������+����Ԥ���������۶���
public class AddReserveBillRule extends ICRule<FinProdInVO> {

  @Override
  public void process(FinProdInVO[] vos) {
	  for (FinProdInVO vo : vos) {
			ICBillBodyVO[] bvos = vo.getChildrenVO();
			if (bvos == null || bvos.length == 0) {
				continue;
			}
			try {
				dealAutoReserve(vo);
			} catch (BusinessException e) {
				// TODO �Զ����ɵ� catch ��
				ExceptionUtils.wrappException(e);
			}

		}
  }

  private void dealAutoReserve(FinProdInVO vo) throws BusinessException {
		// ���Դͷ�����۶�����ִ��
		String saleOrderCode = getSaleOrderCode(vo.getChildrenVO()[0]);
		if (saleOrderCode == null || "~".equalsIgnoreCase(saleOrderCode)) {
			return;
		}
		
		// �������۶�������ѯ��δ���Ǻϵ�����
		IReserveAssist reserve = NCLocator.getInstance().lookup(
				IReserveAssist.class);
		
		ResRequireQueryParam param = assmbleQueryParam(vo);

		ReserveVO[] queryReqBill = reserve.queryReqBill(param);
		
		//�������۶���-> ���ɶ�Ӧ�� Ԥ����
		IReserveMaintenance saveService = AMProxy.lookup(IReserveMaintenance.class);
		ReserveBillVO[] bills = reserve.allocReserve(queryReqBill);
		if(bills != null){
			saveService.insert(bills);
		}
	}

	private String getSaleOrderCode(ICBillBodyVO icBillBodyVO) {
		// TODO �Զ����ɵķ������
		String sql = " select vbfirstcode mm_wr_product from mm_wr_product where vbfirsttype='30' and  pk_wr_product in("
				+ " select vsourcerowno   from ic_finprodin_b where cgeneralbid='"
				+ icBillBodyVO.getCgeneralbid() + "') ";
		DataAccessUtils utils = new DataAccessUtils();
		String vsaleorderBillCode = null;
		IRowSet rs = utils.query(sql);
		while (rs.next()) {
			vsaleorderBillCode = rs.getString(0);
		}
		return vsaleorderBillCode;
	}

	private ResRequireQueryParam assmbleQueryParam(FinProdInVO vo) {
		// TODO �Զ����ɵķ������
		List<String> cfirstbillcode = new ArrayList<String>();
		List<String> cmaterial = new ArrayList<String>();
		ICBillBodyVO[] bvos = vo.getChildrenVO();
		for (ICBillBodyVO bvo : bvos) {
			if (!cfirstbillcode.contains(bvo.getVfirstbillcode())) {
				cfirstbillcode.add(bvo.getVfirstbillcode());
			}
			if (!cmaterial.contains(bvo.getCmaterialoid())) {
				cmaterial.add(bvo.getCmaterialoid());
			}
		}
		ResRequireQueryParam parm = new ResRequireQueryParam();
		parm.setPk_group(vo.getHead().getPk_group());
		parm.addBillType("30");
		List<ConditionVO> listvo = new ArrayList<ConditionVO>();
		// ��֯��Ϣ
		ConditionVO cvo = new ConditionVO();
		listvo.add(cvo);
		cvo.setFieldCode("pk_org");
		cvo.setFieldName("�����֯");
		cvo.setOrderSequence(0);
		cvo.setOperaCode("=");
		cvo.setOperaName("����");
		cvo.setValue(vo.getHead().getPk_org());

		for (String billcode : cfirstbillcode) {
			// ��Դ������Ϣ
			ConditionVO cvo2 = new ConditionVO();
			listvo.add(cvo2);
			cvo2.setFieldCode("vreqbillcode");
			cvo2.setFieldName("���󵥺�");
			cvo2.setValue(vo.getHead().getPk_org());
			cvo2.setOrderSequence(0);
			cvo2.setOperaCode("=");
			cvo2.setOperaName("����");
			cvo2.setValue(billcode);
		}
		// ������Ϣcmaterialoid.code
		for (String mpk : cmaterial) {
			ConditionVO cm = new ConditionVO();
			listvo.add(cm);
			cm.setFieldCode("cmaterialvid");
			cm.setFieldName("���󵥺�");
			cm.setValue(vo.getHead().getPk_org());
			cm.setOrderSequence(0);
			cm.setOperaCode("=");
			cm.setOperaName("����");
			cm.setValue(mpk);
		}
		return parm;
	}
}
