package nc.bs.ic.m46.insert.rule;


import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.base.ICRule;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.itf.ic.reserve.IReserveAssist;
import nc.itf.ic.reserve.IReserveMaintenance;
import nc.vo.am.proxy.AMProxy;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.m46.entity.FinProdInBodyVO;
import nc.vo.ic.m46.entity.FinProdInHeadVO;
import nc.vo.ic.m46.entity.FinProdInVO;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.reserve.entity.PreReserveVO;
import nc.vo.ic.reserve.entity.ReserveBillVO;
import nc.vo.ic.reserve.entity.ReserveVO;
import nc.vo.ic.reserve.pub.ResRequireQueryParam;
import nc.vo.ic.reserve.pub.ReserveVOUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
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
		//ReserveVOUtil.calcNlackNum(queryReqBill);
		if(queryReqBill == null){
			return;
			//throw new BusinessException("���Ҳ�����Ԥ�����ݣ��޷�����Ԥ������");
		}
		//�������۶���-> ���ɶ�Ӧ�� Ԥ����
		IReserveMaintenance saveService = AMProxy.lookup(IReserveMaintenance.class);
		ReserveBillVO[] bills = reserve.allocReserve(queryReqBill);
		
		if(bills != null){
			//���ܻ��ж������
			for(ReserveBillVO bill: bills){
				//ReserveBillVO bill = bills[0];
				ReserveBillVO tempBill = new ReserveBillVO();
				PreReserveVO[] preVOs = bill.getPreReserveVO();
				if(preVOs != null){
					PreReserveVO[] targetVO = new PreReserveVO[vo.getChildrenVO().length];
					FinProdInBodyVO[] bodyVOs = vo.getBodys();
					//ƥ���Ӧ �������ŵ� Ԥ����
					int i = 0;
					for(FinProdInBodyVO item : bodyVOs){
						for(PreReserveVO ele : preVOs){
							if(ele.getCsupplycode().equals(item.getVproductbatch())
							  /*&& ele.getCsupplyrowno().equals(item.getvpro)*/){
								ele.setNrsnum(ele.getNcanrsnum());
								targetVO[i] = ele;
								i++;
								break;
							}
						}
					}
					if(i>0){
						bill.setChildrenVO(targetVO);
						ReserveBillVO[] ret = saveService.insert(new ReserveBillVO[]{bill});
						break;
					}
				}
			}
		}
		//throw new BusinessException();
	}

	private String getSaleOrderCode(ICBillBodyVO icBillBodyVO) {
		// TODO �Զ����ɵķ������
		//�������۶����Ų�pk
		String sql = "select vbfirstcode mm_wr_product from mm_wr_product where vbfirsttype='30' and  pk_wr_product in("
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
		//��������pk -�� �� ����code
		DataAccessUtils utils = new DataAccessUtils();
		String[] materialCodes = new String[cmaterial.size()];
		int i=0;
		for(String mpk : cmaterial){
			String sql = "select code from bd_material_v where pk_material ='"
					+ mpk + "'";
			String code = null;
			IRowSet rs = utils.query(sql);
			while (rs.next()) {
				code = rs.getString(0);
			}
			if(code != null){
				materialCodes[i] = code;
				i++;
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
		//data-type = 5
		cvo.setDataType(5);
		
		for (String billcode : cfirstbillcode) {
			// ��Դ������Ϣ
			ConditionVO cvo2 = new ConditionVO();
			listvo.add(cvo2);
			cvo2.setFieldCode("vreqbillcode");
			cvo2.setFieldName("���󵥾ݺ�");
			//cvo2.setValue(vo.getHead().getPk_org());
			cvo2.setOrderSequence(0);
			cvo2.setOperaCode("=");
			cvo2.setOperaName("����");
			cvo2.setValue(billcode);
		}
		// ������Ϣcmaterialoid.code
		/* ���ܼ����ϱ��룺�깤�����ǲ�Ʒ���룬���۵��������ϱ���
		for (String mpk : materialCodes) {
			ConditionVO cm = new ConditionVO();
			listvo.add(cm);
			cm.setFieldCode("cmaterialoid.code");
			cm.setFieldName("���ϱ���");
			cm.setOrderSequence(0);
			cm.setOperaCode("=");
			cm.setOperaName("����");
			cm.setValue(mpk);
			//data-type=5
			cm.setDataType(5);
		}*/
		
		parm.setConditionvos(listvo.toArray(new ConditionVO[0]));
		return parm;
	}
}
