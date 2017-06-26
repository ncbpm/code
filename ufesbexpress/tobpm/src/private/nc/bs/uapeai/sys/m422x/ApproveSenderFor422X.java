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
		// TODO 自动生成的方法存根
		StoreReqAppVO bill = (StoreReqAppVO) vo;

		String xmlstr = getXml(bill);
		return getDocumentByXMLStr(xmlstr);
	}

	private String getXml(StoreReqAppVO bill) throws BusinessException {
		// TODO 自动生成的方法存根
		StringBuffer xmlStr = new StringBuffer();
		StoreReqAppHeaderVO hvo = bill.getHVO();
		xmlStr.append("<?xml version=\"1.0\" encoding=\'UTF-8\'?>");
		xmlStr.append(" <xml>");
		xmlStr.append("<RF_CW_gdzcxz>"); // ----------只有表头，没有表体
		String pk_org = bill.getHVO().getPk_org();
		xmlStr.append("<orgName>"
				+ transNCToName(pk_org, "46c4bfba-0b40-4855-87f8-c2ac8647f039")
				+ "</orgName>");// ----库存组织名称
		xmlStr.append("<orgCode>"
				+ transNCToCode(pk_org, "46c4bfba-0b40-4855-87f8-c2ac8647f039")
				+ "</orgCode>");// ------------------库存组织编码
		xmlStr.append("<pk_org>" + pk_org + "</pk_org>");// ---库存组织主键
		xmlStr.append("<WZNO>" + hvo.getVbillcode() + "</WZNO>");// -----------物资需求申请单号
		xmlStr.append("<Date>2" + hvo.getDbilldate() + "</Date>---申请日期");
		
		String pk_appdepth = hvo.getPk_appdepth();
		xmlStr.append(" <Depno>"+transNCToCode(pk_appdepth, "b26fa3cb-4087-4027-a3b6-c83ab2a086a9")+"</Depno>");// ------------申请部门编码,对应BPM部门编码
		xmlStr.append(" <Depname>"+transNCToName(pk_appdepth, "b26fa3cb-4087-4027-a3b6-c83ab2a086a9")+"</Depname>");//----------对应BPM部门名称
		
		String pk_apppsnh = hvo.getPk_apppsnh();
		xmlStr.append(" <Userno>"+transNCToCode(pk_apppsnh, "40d39c26-a2b6-4f16-a018-45664cac1a1f")+"</Userno>");// -------------申请人账号，对应BPM人员编码
		xmlStr.append("<Username>"+transNCToName(pk_apppsnh, "40d39c26-a2b6-4f16-a018-45664cac1a1f")+"</Username>");// ------------对应BPM人员编码
		
		IMaterialBaseInfoQueryService mquery = NCLocator.getInstance().lookup(IMaterialBaseInfoQueryService.class);
		//应该是表体明细,但是BPM只有表头，后续沟通，增加表体或者只传一行表体过来
		for(StoreReqAppItemVO body :bill.getBVO()){
			MaterialVO  invvo = mquery.queryDataByPks(new String[]{body.getPk_material()})[0];
			xmlStr.append("<Gname>"+invvo.getName()+"</Gname>");//固定资产名称
			xmlStr.append("<GSpecification>"+(invvo.getMaterialspec()+invvo.getMaterialtype())+"</GSpecification>");//规格型号
			xmlStr.append("<Gpurpose> </Gpurpose>");//申请用途
			xmlStr.append("<Gadress></Gadress>");//>存放地点 
			xmlStr.append("<Gnumber>"+body.getNnum()+"</Gnumber>");// ------采购数量
			xmlStr.append("<Gdep></Gdep>");//综合部采购
			xmlStr.append("<Gbudge></Gbudge>");//预算外
			xmlStr.append("<Greason>"+hvo.getVmemo()+"</Greason>");//申请原因及具体要求
			xmlStr.append("<Gann></Gann>");// --------附件（可不要）,此格式按照BPM一定的规则建立，回头可以详细沟通
			xmlStr.append("<Attribute/>");
			xmlStr.append("<YJJinE>"+body.getNtaxmny()+"</YJJinE>");// -----预计金额
		}
		xmlStr.append(" </RF_CW_gdzcxz>");
		xmlStr.append("</xml>");

		return xmlStr.toString();
	}

	@Override
	protected void initBillInor(Object vo, SysBillInfor infor) throws Exception {
		// TODO 自动生成的方法存根
		StoreReqAppVO bill = (StoreReqAppVO) vo;
		infor.setBilltype("固定资产新增");
		infor.setVbillcode(bill.getHVO().getVbillcode());
	}

}
