package nc.bs.hr.wa.paydata.plugin;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.itf.hr.wa.IPaydataManageService;
import nc.itf.hr.wa.IPaydataQueryService;
import nc.ui.wa.pub.WADelegator;
import nc.vo.hr.caculate.CaculateTypeVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.wa.func.WherePartUtil;
import nc.vo.wa.paydata.AggPayDataVO;
import nc.vo.wa.payfile.PayfileVO;
import nc.vo.wa.pub.PeriodStateVO;
import nc.vo.wa.pub.WaLoginContext;
import nc.vo.wa.pub.WaLoginVO;
import nc.vo.wa.pub.YearPeriodSeperatorVO;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 薪资计算
 */
public class BpmCaculateDataExpPfxxPlugin<T extends PayfileVO> extends
		nc.bs.pfxx.plugin.AbstractPfxxPlugin {

	private IPaydataManageService manageService;
	private IPaydataQueryService paydataQuery;

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		PayfileVO bill = (PayfileVO) vo;

		if (bill.getPk_group() == null) {
			throw new BusinessException("单据的所属集团字段不能为空，请输入值");
		}
		if (bill.getPk_org() == null) {
			throw new BusinessException("单据的财务组织字段不能为空，请输入值");
		}

		if (bill.getCyear() == null) {
			throw new BusinessException("单据的薪资年度字段不能为空，请输入值");
		}

		if (bill.getCperiod() == null) {
			throw new BusinessException("单据的薪资期间字段不能为空，请输入值");
		}

		if (bill.getPk_wa_class() == null) {
			throw new BusinessException("单据的薪资方案字段不能为空，请输入值");
		}

		String waPeriod = bill.getCperiod();// 薪资期间
		String pk_wa_class = bill.getPk_wa_class();// 薪资方案

		String pk_group = bill.getPk_group();
		String pk_org = bill.getPk_org();

		CaculateTypeVO caculateTypeVO = new CaculateTypeVO();
		caculateTypeVO.setRange(UFBoolean.TRUE);
		caculateTypeVO.setType(UFBoolean.TRUE);

		WaLoginContext loginContext = createContext(waPeriod, pk_wa_class,
				pk_group, pk_org);
//		WaLoginVO waLoginVO = loginContext.getWaLoginVO();
//		String where = null;
//		String condition = addStopFlag(where);
//		condition += " and wa_data.pk_psndoc in(select pk_psndoc from wa_data "
//				+ "where pk_wa_class = '" + waLoginVO.getPk_wa_class()
//				+ "' and cyear = '" + waLoginVO.getCyear()
//				+ "' and cperiod = '" + waLoginVO.getCperiod() + "' "
//				// 20151106 shenliangc NCdp205536216
//				// 薪资补发生成补发期间的档案没有对当前期间停发人员进行过滤。
//				+ " and wa_data.stopflag = 'N')";
		AggPayDataVO aggPayDataVO = getPaydataQuery()
				.queryAggPayDataVOByCondition(loginContext, null, null);

		getManageService().onCaculate(loginContext, caculateTypeVO, null,
				aggPayDataVO.getDataVOs());

		String ss = null;
		SerializeWriter out = null;
		try {

			SerializeConfig config = SerializeConfig.getGlobalInstance();
			JSONSerializer serializer = new JSONSerializer(out, config);
			SerializerFeature aserializerfeature[] = new SerializerFeature[] {
					SerializerFeature.WriteDateUseDateFormat,
					SerializerFeature.DisableCircularReferenceDetect,
					SerializerFeature.BrowserCompatible };
			int j = aserializerfeature.length;
			for (int i = 0; i < j; i++) {
				SerializerFeature feature = aserializerfeature[i];
				serializer.config(feature, true);
			}

			serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
			serializer.write(aggPayDataVO);
			out = new SerializeWriter();
			ss = out.toString();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return ss;
	}

	/**
	 * 停发人员不进行薪资补发。
	 * 
	 * @param where
	 * @return
	 */
	private String addStopFlag(String where) {
		// addStopflag;
		String stopFlagConditon = "  wa_data.stopflag = 'N' ";
		return WherePartUtil.concatConditon(where, stopFlagConditon);
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

}
