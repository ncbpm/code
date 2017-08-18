package nc.bs.uapeai.sys.m422x;

import nc.bs.framework.common.NCLocator;
import nc.bs.uapeai.sys.AbstractSender;
import nc.itf.bd.material.baseinfo.IMaterialBaseInfoQueryService;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pu.m422x.entity.StoreReqAppHeaderVO;
import nc.vo.pu.m422x.entity.StoreReqAppItemVO;
import nc.vo.pu.m422x.entity.StoreReqAppVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uapeai.syn.SysBillInfor;

import org.w3c.dom.Document;

/**
 * @author liyf_brave
 * 
 */
public class ApproveSenderFor422X extends AbstractSender {

	@Override
	public Document getDocment(Object vo) throws Exception {
		// TODO �Զ����ɵķ������
		StoreReqAppVO bill = (StoreReqAppVO) vo;

		String xmlstr = getXml(bill);
		return getDocumentByXMLStr(xmlstr);
	}
	
	public String getStringNullAsSpace(String value){
		if(value == null){
			return "";
		}
		return value;
	}

	public UFDouble getUFdoubleNullAsZero(UFDouble value){
		if(value == null){
			return UFDouble.ZERO_DBL;
		}
		return value;
	}
	private String getXml(StoreReqAppVO bill) throws BusinessException {
		// TODO �Զ����ɵķ������
		StringBuffer xmlStr = new StringBuffer();
		StoreReqAppHeaderVO hvo = bill.getHVO();
		StoreReqAppItemVO body = bill.getBVO()[0];
		xmlStr.append("<?xml version=\"1.0\" encoding=\'UTF-8\'?>");
		xmlStr.append(" <xml>");
		xmlStr.append("<RF_CW_gdzcxz>"); // ----------ֻ�б�ͷ��û�б���
		xmlStr.append("<Date>" + hvo.getDbilldate() + "</Date>");//-----��������
		IMaterialBaseInfoQueryService mquery = NCLocator.getInstance().lookup(IMaterialBaseInfoQueryService.class);
		MaterialVO  invvo = mquery.queryDataByPks(new String[]{body.getPk_material()})[0];
		xmlStr.append("<Gname>"+invvo.getName()+"</Gname>");//�̶��ʲ�����
		xmlStr.append("<GSpecification>"+(getStringNullAsSpace(invvo.getMaterialspec())+getStringNullAsSpace(invvo.getMaterialtype()))+"</GSpecification>");//����ͺ�
		xmlStr.append("<Gpurpose>"+getStringNullAsSpace(body.getVbmemo())+"</Gpurpose>");//������;
		xmlStr.append("<Gadress>"+getStringNullAsSpace(body.getVbdef2())+"</Gadress>");//>��ŵص� 
		xmlStr.append("<Gnumber>"+getUFdoubleNullAsZero(body.getNnum())+"</Gnumber>");// ------�ɹ�����
		xmlStr.append("<Gbudge>Ԥ����</Gbudge>");//Ԥ����
		xmlStr.append("<Greason>"+getStringNullAsSpace(hvo.getVmemo())+"</Greason>");//����ԭ�򼰾���Ҫ��
		xmlStr.append("<YJJinE>"+getUFdoubleNullAsZero(body.getNtaxmny())+"</YJJinE>");//Ԥ�ƽ��
		xmlStr.append("<pk_storereq>"+hvo.getPrimaryKey()+"</pk_storereq>");//����������������
		xmlStr.append("<WZNO>" + hvo.getVbillcode() + "</WZNO>");// -----------�����������뵥��
		xmlStr.append("<pk_psndoc>"+getStringNullAsSpace(hvo.getPk_apppsnh())+"</pk_psndoc>");//������
		xmlStr.append("<pk_dept>"+getStringNullAsSpace(hvo.getPk_appdepth())+"</pk_dept>");//�빺��������
		String pk_org = bill.getHVO().getPk_org();
		String orgcode=transNCToCode(pk_org, "5d69ee35-57d0-4f7b-b454-deff4fc73689");
		xmlStr.append("<pk_org>" + pk_org + "</pk_org>");// ---�����֯����
		xmlStr.append("<orgCode>" + orgcode + "</orgCode>");// ---�����֯����
		String pk_porject = bill.getHVO().getPk_project();
		String projectName=transNCToCode(pk_porject, "2ee58f9b-781b-469f-b1d8-1816842515c3");
		xmlStr.append("<project>" + getStringNullAsSpace(projectName)+ "</project>");// ---NC��Ŀ����
		xmlStr.append(" </RF_CW_gdzcxz>");
		xmlStr.append("</xml>");

		return xmlStr.toString();
	}

	@Override
	protected void initBillInor(Object vo, SysBillInfor infor) throws Exception {
		// TODO �Զ����ɵķ������
		StoreReqAppVO bill = (StoreReqAppVO) vo;
		infor.setBilltype("�̶��ʲ�����");
		infor.setVbillcode(bill.getHVO().getVbillcode());
	}

}
