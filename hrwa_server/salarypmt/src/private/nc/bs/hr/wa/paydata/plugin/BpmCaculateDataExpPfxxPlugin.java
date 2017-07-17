package nc.bs.hr.wa.paydata.plugin;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.hr.utils.InSQLCreator;
import nc.itf.hr.wa.IPaydataManageService;
import nc.itf.hr.wa.IPaydataQueryService;
import nc.jdbc.framework.SQLParameter;
import nc.ui.wa.pub.WADelegator;
import nc.vo.hr.caculate.CaculateTypeVO;
import nc.vo.hrwa.impwadata.AggImpWaDataVO;
import nc.vo.hrwa.impwadata.WaDataBodyVO;
import nc.vo.hrwa.impwadata.WaDataHeadVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;
import nc.vo.wa.classitem.WaClassItemVO;
import nc.vo.wa.paydata.AggPayDataVO;
import nc.vo.wa.payfile.PayfileVO;
import nc.vo.wa.pub.PeriodStateVO;
import nc.vo.wa.pub.WaLoginContext;
import nc.vo.wa.pub.WaLoginVO;
import nc.vo.wa.pub.YearPeriodSeperatorVO;

/**
 * 薪资计算
 */
public class BpmCaculateDataExpPfxxPlugin<T extends PayfileVO> extends
		nc.bs.pfxx.plugin.AbstractPfxxPlugin {

	private IPaydataManageService manageService;
	private IPaydataQueryService paydataQuery;

	private WaClassItemVO[] items = null;

	private Map<String, String> map = null;

	private final String USERCODE = "kf01";

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		AggImpWaDataVO bill = (AggImpWaDataVO) vo;

		if (bill == null || bill.getParentVO() == null
				|| bill.getChildrenVO() == null
				|| bill.getChildrenVO().length == 0)
			throw new BusinessException("传入数据出错");

		WaDataHeadVO headvo = (WaDataHeadVO) bill.getParentVO();
		if (headvo.getPk_group() == null) {
			throw new BusinessException("单据的所属集团字段不能为空，请输入值");
		}
		if (headvo.getPk_org() == null) {
			throw new BusinessException("单据的财务组织字段不能为空，请输入值");
		}

		if (headvo.getCyear() == null) {
			throw new BusinessException("单据的薪资年度字段不能为空，请输入值");
		}

		if (headvo.getCperiod() == null) {
			throw new BusinessException("单据的薪资期间字段不能为空，请输入值");
		}

		if (headvo.getPk_wa_class() == null) {
			throw new BusinessException("单据的薪资方案字段不能为空，请输入值");
		}

		if (headvo.getCuserid() == null) {
			throw new BusinessException("单据的操作人员字段不能为空，请输入值");
		}

		String waPeriod = headvo.getCyear() + headvo.getCperiod();// 薪资期间
		String pk_wa_class = headvo.getPk_wa_class();// 薪资方案

		String pk_group = headvo.getPk_group();
		String pk_org = headvo.getPk_org();

		CaculateTypeVO caculateTypeVO = new CaculateTypeVO();
		caculateTypeVO.setRange(UFBoolean.TRUE);
		caculateTypeVO.setType(UFBoolean.TRUE);

		WaDataBodyVO[] bodyvos = (WaDataBodyVO[]) bill.getChildrenVO();

		ArrayList<String> list = new ArrayList<>();
		for (WaDataBodyVO body : bodyvos) {
			if (body.getPk_psndoc() != null) {
				list.add(body.getPk_psndoc());
			}
		}

		if (list == null || list.size() == 0)
			throw new BusinessException("人员信息不能为空");

		WaLoginContext loginContext = createContext(waPeriod, pk_wa_class,
				pk_group, pk_org);

		if (headvo.getCuserid() == null) {
			InvocationInfoProxy.getInstance().setUserId(getCuserid(USERCODE));
			InvocationInfoProxy.getInstance().setUserCode(USERCODE);
		} else {
			InvocationInfoProxy.getInstance().setUserId(headvo.getCuserid());
		}
		
		updateDataVO(headvo, bodyvos, loginContext);
		
		InSQLCreator inSQLCreator = new InSQLCreator();
		String pks = inSQLCreator
				.getInSQL(list.toArray(new String[list.size()]));
		String conditon = "pk_psndoc  in (" + pks + ")";
		AggPayDataVO aggPayDataVO = getPaydataQuery()
				.queryAggPayDataVOByCondition(loginContext, conditon, null);

		getManageService().onCaculate(loginContext, caculateTypeVO, conditon,
				aggPayDataVO.getDataVOs());
		return null;
	}

	protected IPaydataManageService getManageService() {
		if (manageService == null) {
			manageService = NCLocator.getInstance().lookup(
					IPaydataManageService.class);
		}
		return manageService;
	}

	public IPaydataQueryService getPaydataQuery() {
		if (paydataQuery == null) {
			paydataQuery = NCLocator.getInstance().lookup(
					IPaydataQueryService.class);
		}
		return paydataQuery;
	}

	private WaLoginContext createContext(String waPeriod, String pk_wa_class,
			String pk_group, String pk_org) throws BusinessException {

		WaLoginContext context = new WaLoginContext();
		WaLoginVO waLoginVO = new WaLoginVO();
		waLoginVO.setPk_wa_class(pk_wa_class);
		YearPeriodSeperatorVO seperatorVO = new YearPeriodSeperatorVO(waPeriod);
		waLoginVO.setCyear(seperatorVO.getYear());
		waLoginVO.setCperiod(seperatorVO.getPeriod());
		waLoginVO.setPk_group(pk_group);
		context.setPk_group(pk_group);
		PeriodStateVO periodStateVO = new PeriodStateVO();
		periodStateVO.setCyear(seperatorVO.getYear());
		periodStateVO.setCperiod(seperatorVO.getPeriod());
		waLoginVO.setPeriodVO(periodStateVO);
		waLoginVO.setPk_org(pk_org);
		context.setPk_org(pk_org);
		waLoginVO = WADelegator.getWaPubService().getWaclassVOWithState(
				waLoginVO);
		context.setWaLoginVO(waLoginVO);
		context.setNodeCode("60130paydata");
		return context;
	}

	private void updateDataVO(WaDataHeadVO headvo, WaDataBodyVO[] bodyvos,
			WaLoginContext loginContext) throws BusinessException {

		if (headvo == null)
			throw new BusinessException("传入数据出错");

		if (bodyvos == null || bodyvos.length == 0)
			throw new BusinessException("传入数据出错");

		// 更新wa_datas中的计算值

		StringBuffer sqlBuffer = new StringBuffer();

		BaseDAO baseDao = new BaseDAO();
		UFDouble value = UFDouble.ZERO_DBL;
		for (WaDataBodyVO data : bodyvos) {
			sqlBuffer.setLength(0);
			sqlBuffer.append(" update wa_data set caculateflag ='N'"); // 1
			for (String key : getMap().keySet()) {
				if (data.getAttributeValue(key) != null) {
					value = (UFDouble) data.getAttributeValue(key);
				}
				String name = getMap().get(key);
				String itemkey = getItemKey(name, loginContext);

				if (itemkey == null)
					continue;
				sqlBuffer.append(",");
				sqlBuffer.append(itemkey);
				sqlBuffer.append("=");
				sqlBuffer.append(value);
			}
			sqlBuffer
					.append(" where cperiod =? and cyear =? and pk_group =?  and  pk_org =?   and  pk_wa_class  =?  and pk_psndoc  =? ");
			SQLParameter param = new SQLParameter();

			param.addParam(headvo.getCperiod());
			param.addParam(headvo.getCyear());
			param.addParam(headvo.getPk_group());
			param.addParam(headvo.getPk_org());
			param.addParam(headvo.getPk_wa_class());
			param.addParam(data.getPk_psndoc());
			baseDao.executeUpdate(sqlBuffer.toString(), param);

		}
	}

	private WaClassItemVO[] getWaClassItemVO(WaLoginContext loginContext)
			throws BusinessException {
		if (items == null || items.length == 0) {
			items = getPaydataQuery().getUserClassItemVOs(loginContext);
		}
		return items;
	}

	private String getItemKey(String name, WaLoginContext loginContext)
			throws BusinessException {
		WaClassItemVO[] items1 = getWaClassItemVO(loginContext);

		if(items1 == null || items1.length ==0)
			throw new BusinessException("获取用户有权限的薪资项目出错");
			
		for (WaClassItemVO item : items1) {
			if (item.getName() == null)
				continue;
			if (item.getName().equalsIgnoreCase(name)) {
				return item.getItemkey();
			}
		}
		return null;
	}

	private Map<String, String> getMap() throws BusinessException {

		try {
			if (map == null || map.size() == 0) {
				map = new HashMap<>();
				String home = RuntimeEnv.getInstance().getNCHome();
				String fileNme = home + "/pfxx/bpmpayitem.properties";
				InputStreamReader  input = new InputStreamReader (new FileInputStream(
						fileNme),"utf-8");
				Properties properties = new Properties();
				properties.load(input);
				Iterator it = properties.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					String key = (String) entry.getKey();
					String value = (String) entry.getValue();
					map.put(key, value);
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return map;
	}

	private String getCuserid(String usercode) {
		String sql = " user_code_q = ?";
		SQLParameter sqlParam = new SQLParameter();
		sqlParam.addParam(usercode.toUpperCase());

		List<UserVO> retList;
		try {
			retList = (List<UserVO>) new BaseDAO().retrieveByClause(
					UserVO.class, sql, sqlParam);
			if (retList != null && retList.size() > 0 && retList.get(0) != null) {
				return retList.get(0).getCuserid();
			}
		} catch (DAOException e) {
			Logger.error(e);
		}
		return null;

	}
}
