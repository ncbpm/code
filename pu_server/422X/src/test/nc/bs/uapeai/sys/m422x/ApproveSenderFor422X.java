/**
 * 
 */
package nc.bs.uapeai.sys.m422x;

import nc.bs.framework.common.NCLocator;
import nc.bs.uapeai.sys.AbstractSender;
import nc.itf.bd.material.baseinfo.IMaterialBaseInfoQueryService;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pu.m422x.entity.StoreReqAppHeaderVO;
import nc.vo.pu.m422x.entity.StoreReqAppItemVO;
import nc.vo.pu.m422x.entity.StoreReqAppVO;
import nc.vo.pub.BusinessException;
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

	private String getXml(StoreReqAppVO bill) throws BusinessException {
		// TODO �Զ����ɵķ������
		StringBuffer xmlStr = new StringBuffer();
		StoreReqAppHeaderVO hvo = bill.getHVO();
		StoreReqAppItemVO body = bill.getBVO()[0];
		xmlStr.append("<?xml version=\"1.0\" encoding=\'UTF-8\'?>");
		xmlStr.append(" <xml>");
		xmlStr.append("<RF_CW_gdzcxz>"); // ----------ֻ�б�ͷ��û�б���
		xmlStr.append("<Date>" + hvo.getDbilldate() + "</Date>---��������");
		IMaterialBaseInfoQueryService mquery = NCLocator.getInstance().lookup(IMaterialBaseInfoQueryService.class);
		MaterialVO  invvo = mquery.queryDataByPks(new String[]{body.getPk_material()})[0];
		xmlStr.append("<Gname>"+invvo.getName()+"</Gname>");//�̶��ʲ�����
		xmlStr.append("<GSpecification>"+(invvo.getMaterialspec()+invvo.getMaterialtype())+"</GSpecification>");//����ͺ�
		xmlStr.append("<Gpurpose>"+body.getVbmemo()+"</Gpurpose>");//������;
		xmlStr.append("<Gadress></Gadress>");//>��ŵص� 
		xmlStr.append("<Gnumber>"+body.getNnum()+"</Gnumber>");// ------�ɹ�����
		xmlStr.append("<Gbudge>Ԥ����</Gbudge>");//Ԥ����
//		xmlStr.append("<Greason>"++"</Greason>");//����ԭ�򼰾���Ҫ��
//		xmlStr.append("<YJJinE>"++"</YJJinE>");//Ԥ�ƽ��
		xmlStr.append("<pk_storereq>"+hvo.getPrimaryKey()+"</pk_storereq>");//����������������
		xmlStr.append("<WZNO>" + hvo.getVbillcode() + "</WZNO>");// -----------�����������뵥��
		xmlStr.append("<pk_psndoc>"+hvo.getPk_apppsnh()+"</pk_psndoc>");//������
		xmlStr.append("<pk_dept>"+hvo.getPk_appdepth()+"</pk_dept>");//�빺��������
		String pk_org = bill.getHVO().getPk_org();
		xmlStr.append("<pk_org>" + pk_org + "</pk_org>");// ---�����֯����
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
