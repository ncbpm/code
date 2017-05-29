package nc.bpm.so.mcredit;

import java.lang.reflect.Field;
import java.util.Collection;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.xml.out.tool.XmlOutTool;
import nc.itf.so.m30.ref.credit.CreditServicesUtil;
import nc.itf.so.m30.revise.ISaleOrderReviseMaintainApp;
import nc.pubitf.org.IOrgRelationDataPubService;
import nc.vo.credit.billcreditquery.entity.CreditInfoVO;
import nc.vo.org.orgmodel.OrgRelationVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.so.m30.entity.SaleOrderHVO;
import nc.vo.so.m30.revise.entity.SaleOrderHistoryVO;

import org.codehaus.jettison.json.JSONObject;

/**
 * ���ò�ѯ �������۶�����Ϣ��ѯ
 * 
 * @author liyf
 * 
 */

public class McreditForBPMQuery extends AbstractPfxxPlugin {



	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ���	
		//��ѯ���۶����޸�VO
		
		ISaleOrderReviseMaintainApp service = NCLocator.getInstance().lookup(ISaleOrderReviseMaintainApp.class);
	
		SaleOrderHistoryVO[] historyVOs = service.queryM30ReviseApp(new String[]{"1001A41000000000CEOX"});
		
		try {
			XmlOutTool.votoXmlFile("bpm_30_history",
					historyVOs, historyVOs[0].getParentVO().getPk_org(), historyVOs[0].getParentVO().getVbillcode());
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}

		// ���ü��
		// if (SysInitGroupQuery.isCREDITEnabled()) {
		// throw new BusinessException("δ�������ù���ģ��");
		// }
		// 1.�õ�ת�����VO����,ȡ�����򵼵�һ��ע���VO��Ϣ
		AggregatedValueObject resvo = (AggregatedValueObject) vo;
		// // 2. У�����ݵĺϷ���:1.���ݽṹ���� 2.������֯+���ݺ�У���Ƿ��ظ�.
		checkData(resvo);
		//
		// 3.��ѯ����
		CreditInfoVO creditVO = queryCredit(resvo);
		if (creditVO == null) {
			throw new BusinessException("δ��ѯ����Ӧ�����ÿ�����Ϣ.");
		}

		// 4.����JSON����

		return toJson(creditVO);
	}

	private JSONObject toJson(CreditInfoVO creditVO) throws BusinessException {
		JSONObject headJson = new JSONObject();

		try {
			// ��ȡ��������Class����
			Class classType = creditVO.getClass();
			// ��ȡ������������������
			Field[] fields = classType.getDeclaredFields();

			// ��������
			for (int i = 0; i < fields.length; i++) {
				// ��ȡ��Ӧ���Ե�����
				String fieldName = fields[i].getName().toLowerCase();
				Object value = BeanHelper.getProperty(creditVO, fieldName);
				if (value instanceof String) {
					// ��ֵ���뵽JSON������
					headJson.put(fieldName, value);
				} else if (value != null) {
					headJson.put(fieldName, value.toString());
				}
			}
		} catch (Exception e) {
			throw new BusinessException("ת����JSON�쳣");
		}
		return headJson;
	}

	@SuppressWarnings("unchecked")
	private CreditInfoVO queryCredit(AggregatedValueObject resvo)
			throws BusinessException {
		// TODO �Զ����ɵķ������

		SaleOrderHVO head = (SaleOrderHVO) resvo.getParentVO();

		String customerid = head.getCcustomerid();
		String ctranstypeid = head.getCtrantypeid();
		IOrgRelationDataPubService service = NCLocator.getInstance().lookup(
				IOrgRelationDataPubService.class);

		// ������֯--Ĭ�Ͻ��������֯
		String pk_org = head.getPk_org();
		String condition = "" + OrgRelationVO.ENABLESTATE + " =  2 and "
				+ OrgRelationVO.PK_RELATIONTYPE + " ='SALESTOCKCONSIGN0000'"
				+ " and " + OrgRelationVO.SOURCER + " ='" + pk_org
				+ "' and Isdefault='Y'";
		Collection<OrgRelationVO> each_result = (Collection<OrgRelationVO>) new BaseDAO()
				.retrieveByClause(OrgRelationVO.class, condition);
		if (each_result == null || each_result.size() == 0) {
			throw new BusinessException("��ǰ������֯��û��ά����û������Ĭ�ϵĶ�Ӧ�Ľ��������֯.");
		}
		String csettleorg = null;
		for (OrgRelationVO rela : each_result) {
			if (rela.getIsdefault().booleanValue()) {
				csettleorg = rela.getTarget();
			}
		}
		if (csettleorg == null) {
			throw new BusinessException("��ǰ������֯��û��ά����û������Ĭ�ϵĶ�Ӧ�Ľ��������֯.");
		}

		CreditInfoVO creditVO = CreditServicesUtil.creditQueryForSoSideHead(
				csettleorg, customerid, ctranstypeid);

		return creditVO;
	}

	private void fillData(AggregatedValueObject resvo) {
		// TODO �Զ����ɵķ������
		//

	}

	private void checkData(AggregatedValueObject resvo)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		// TODO �Զ����ɵķ������
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("δ��ȡ��ת���������");

		SaleOrderHVO head = (SaleOrderHVO) resvo.getParentVO();
		if (head.getPk_org() == null) {
			throw new BusinessException("pk_org����Ϊ��");
		}

		if (head.getCtrantypeid() == null) {
			throw new BusinessException("Ctrantypeid����Ϊ��");
		}
		if (head.getCcustomerid() == null) {
			throw new BusinessException("Ccustomerid����Ϊ��");
		}
	}

}
