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
		xmlStr.append("<?xml version=\"1.0\" encoding=\'UTF-8\'?>");
		xmlStr.append(" <xml>");
		xmlStr.append("<RF_CW_gdzcxz>"); // ----------ֻ�б�ͷ��û�б���
		String pk_org = bill.getHVO().getPk_org();
		xmlStr.append("<orgName>"
				+ transNCToName(pk_org, "46c4bfba-0b40-4855-87f8-c2ac8647f039")
				+ "</orgName>");// ----�����֯����
		xmlStr.append("<orgCode>"
				+ transNCToCode(pk_org, "46c4bfba-0b40-4855-87f8-c2ac8647f039")
				+ "</orgCode>");// ------------------�����֯����
		xmlStr.append("<pk_org>" + pk_org + "</pk_org>");// ---�����֯����
		xmlStr.append("<WZNO>" + hvo.getVbillcode() + "</WZNO>");// -----------�����������뵥��
		xmlStr.append("<Date>2" + hvo.getDbilldate() + "</Date>---��������");
		
		String pk_appdepth = hvo.getPk_appdepth();
		xmlStr.append(" <Depno>"+transNCToCode(pk_appdepth, "b26fa3cb-4087-4027-a3b6-c83ab2a086a9")+"</Depno>");// ------------���벿�ű���,��ӦBPM���ű���
		xmlStr.append(" <Depname>"+transNCToName(pk_appdepth, "b26fa3cb-4087-4027-a3b6-c83ab2a086a9")+"</Depname>");//----------��ӦBPM��������
		
		String pk_apppsnh = hvo.getPk_apppsnh();
		xmlStr.append(" <Userno>"+transNCToCode(pk_apppsnh, "40d39c26-a2b6-4f16-a018-45664cac1a1f")+"</Userno>");// -------------�������˺ţ���ӦBPM��Ա����
		xmlStr.append("<Username>"+transNCToName(pk_apppsnh, "40d39c26-a2b6-4f16-a018-45664cac1a1f")+"</Username>");// ------------��ӦBPM��Ա����
		
		IMaterialBaseInfoQueryService mquery = NCLocator.getInstance().lookup(IMaterialBaseInfoQueryService.class);
		//Ӧ���Ǳ�����ϸ,����BPMֻ�б�ͷ��������ͨ�����ӱ������ֻ��һ�б������
		for(StoreReqAppItemVO body :bill.getBVO()){
			MaterialVO  invvo = mquery.queryDataByPks(new String[]{body.getPk_material()})[0];
			xmlStr.append("<Gname>"+invvo.getName()+"</Gname>");//�̶��ʲ�����
			xmlStr.append("<GSpecification>"+(invvo.getMaterialspec()+invvo.getMaterialtype())+"</GSpecification>");//����ͺ�
			xmlStr.append("<Gpurpose> </Gpurpose>");//������;
			xmlStr.append("<Gadress></Gadress>");//>��ŵص� 
			xmlStr.append("<Gnumber>"+body.getNnum()+"</Gnumber>");// ------�ɹ�����
			xmlStr.append("<Gdep></Gdep>");//�ۺϲ��ɹ�
			xmlStr.append("<Gbudge></Gbudge>");//Ԥ����
			xmlStr.append("<Greason>"+hvo.getVmemo()+"</Greason>");//����ԭ�򼰾���Ҫ��
			xmlStr.append("<Gann></Gann>");// --------�������ɲ�Ҫ��,�˸�ʽ����BPMһ���Ĺ���������ͷ������ϸ��ͨ
			xmlStr.append("<Attribute/>");
			xmlStr.append("<YJJinE>"+body.getNtaxmny()+"</YJJinE>");// -----Ԥ�ƽ��
		}
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
