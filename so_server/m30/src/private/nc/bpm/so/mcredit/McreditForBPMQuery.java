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
 * 信用查询 根据销售订单信息查询
 * 
 * @author liyf
 * 
 */

public class McreditForBPMQuery extends AbstractPfxxPlugin {



	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法	
		//查询销售订单修改VO
		
		ISaleOrderReviseMaintainApp service = NCLocator.getInstance().lookup(ISaleOrderReviseMaintainApp.class);
	
		SaleOrderHistoryVO[] historyVOs = service.queryM30ReviseApp(new String[]{"1001A41000000000CEOX"});
		
		try {
			XmlOutTool.votoXmlFile("bpm_30_history",
					historyVOs, historyVOs[0].getParentVO().getPk_org(), historyVOs[0].getParentVO().getVbillcode());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		// 信用检查
		// if (SysInitGroupQuery.isCREDITEnabled()) {
		// throw new BusinessException("未启用信用管理模块");
		// }
		// 1.得到转换后的VO数据,取决于向导第一步注册的VO信息
		AggregatedValueObject resvo = (AggregatedValueObject) vo;
		// // 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(resvo);
		//
		// 3.查询信用
		CreditInfoVO creditVO = queryCredit(resvo);
		if (creditVO == null) {
			throw new BusinessException("未查询到对应的信用控制信息.");
		}

		// 4.返回JSON数据

		return toJson(creditVO);
	}

	private JSONObject toJson(CreditInfoVO creditVO) throws BusinessException {
		JSONObject headJson = new JSONObject();

		try {
			// 获取传入对象的Class对象
			Class classType = creditVO.getClass();
			// 获取传入对象的所有属性组
			Field[] fields = classType.getDeclaredFields();

			// 遍历属性
			for (int i = 0; i < fields.length; i++) {
				// 获取对应属性的名字
				String fieldName = fields[i].getName().toLowerCase();
				Object value = BeanHelper.getProperty(creditVO, fieldName);
				if (value instanceof String) {
					// 将值放入到JSON对象中
					headJson.put(fieldName, value);
				} else if (value != null) {
					headJson.put(fieldName, value.toString());
				}
			}
		} catch (Exception e) {
			throw new BusinessException("转换成JSON异常");
		}
		return headJson;
	}

	@SuppressWarnings("unchecked")
	private CreditInfoVO queryCredit(AggregatedValueObject resvo)
			throws BusinessException {
		// TODO 自动生成的方法存根

		SaleOrderHVO head = (SaleOrderHVO) resvo.getParentVO();

		String customerid = head.getCcustomerid();
		String ctranstypeid = head.getCtrantypeid();
		IOrgRelationDataPubService service = NCLocator.getInstance().lookup(
				IOrgRelationDataPubService.class);

		// 销售组织--默认结算财务组织
		String pk_org = head.getPk_org();
		String condition = "" + OrgRelationVO.ENABLESTATE + " =  2 and "
				+ OrgRelationVO.PK_RELATIONTYPE + " ='SALESTOCKCONSIGN0000'"
				+ " and " + OrgRelationVO.SOURCER + " ='" + pk_org
				+ "' and Isdefault='Y'";
		Collection<OrgRelationVO> each_result = (Collection<OrgRelationVO>) new BaseDAO()
				.retrieveByClause(OrgRelationVO.class, condition);
		if (each_result == null || each_result.size() == 0) {
			throw new BusinessException("当前销售组织，没有维护或没有配置默认的对应的结算财务组织.");
		}
		String csettleorg = null;
		for (OrgRelationVO rela : each_result) {
			if (rela.getIsdefault().booleanValue()) {
				csettleorg = rela.getTarget();
			}
		}
		if (csettleorg == null) {
			throw new BusinessException("当前销售组织，没有维护或没有配置默认的对应的结算财务组织.");
		}

		CreditInfoVO creditVO = CreditServicesUtil.creditQueryForSoSideHead(
				csettleorg, customerid, ctranstypeid);

		return creditVO;
	}

	private void fillData(AggregatedValueObject resvo) {
		// TODO 自动生成的方法存根
		//

	}

	private void checkData(AggregatedValueObject resvo)
			throws BusinessException {
		// TODO 自动生成的方法存根
		// TODO 自动生成的方法存根
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");

		SaleOrderHVO head = (SaleOrderHVO) resvo.getParentVO();
		if (head.getPk_org() == null) {
			throw new BusinessException("pk_org不能为空");
		}

		if (head.getCtrantypeid() == null) {
			throw new BusinessException("Ctrantypeid不能为空");
		}
		if (head.getCcustomerid() == null) {
			throw new BusinessException("Ccustomerid不能为空");
		}
	}

}
