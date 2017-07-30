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
		StoreReqAppItemVO body = bill.getBVO()[0];
		xmlStr.append("<?xml version=\"1.0\" encoding=\'UTF-8\'?>");
		xmlStr.append(" <xml>");
		xmlStr.append("<RF_CW_gdzcxz>"); // ----------只有表头，没有表体
		xmlStr.append("<Date>" + hvo.getDbilldate() + "</Date>---申请日期");
		IMaterialBaseInfoQueryService mquery = NCLocator.getInstance().lookup(IMaterialBaseInfoQueryService.class);
		MaterialVO  invvo = mquery.queryDataByPks(new String[]{body.getPk_material()})[0];
		xmlStr.append("<Gname>"+invvo.getName()+"</Gname>");//固定资产名称
		xmlStr.append("<GSpecification>"+(invvo.getMaterialspec()+invvo.getMaterialtype())+"</GSpecification>");//规格型号
		xmlStr.append("<Gpurpose>"+body.getVbmemo()+"</Gpurpose>");//申请用途
		xmlStr.append("<Gadress></Gadress>");//>存放地点 
		xmlStr.append("<Gnumber>"+body.getNnum()+"</Gnumber>");// ------采购数量
		xmlStr.append("<Gbudge>预算内</Gbudge>");//预算外
//		xmlStr.append("<Greason>"++"</Greason>");//申请原因及具体要求
//		xmlStr.append("<YJJinE>"++"</YJJinE>");//预计金额
		xmlStr.append("<pk_storereq>"+hvo.getPrimaryKey()+"</pk_storereq>");//物资需求申请主键
		xmlStr.append("<WZNO>" + hvo.getVbillcode() + "</WZNO>");// -----------物资需求申请单号
		xmlStr.append("<pk_psndoc>"+hvo.getPk_apppsnh()+"</pk_psndoc>");//申请人
		xmlStr.append("<pk_dept>"+hvo.getPk_appdepth()+"</pk_dept>");//请购部门主键
		String pk_org = bill.getHVO().getPk_org();
		xmlStr.append("<pk_org>" + pk_org + "</pk_org>");// ---库存组织主键
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
