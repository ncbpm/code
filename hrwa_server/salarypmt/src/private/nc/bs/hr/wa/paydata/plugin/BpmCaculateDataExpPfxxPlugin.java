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
 * н�ʼ���
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
			throw new BusinessException("���ݵ����������ֶβ���Ϊ�գ�������ֵ");
		}
		if (bill.getPk_org() == null) {
			throw new BusinessException("���ݵĲ�����֯�ֶβ���Ϊ�գ�������ֵ");
		}

		if (bill.getCyear() == null) {
			throw new BusinessException("���ݵ�н������ֶβ���Ϊ�գ�������ֵ");
		}

		if (bill.getCperiod() == null) {
			throw new BusinessException("���ݵ�н���ڼ��ֶβ���Ϊ�գ�������ֵ");
		}

		if (bill.getPk_wa_class() == null) {
			throw new BusinessException("���ݵ�н�ʷ����ֶβ���Ϊ�գ�������ֵ");
		}

		String waPeriod = bill.getCperiod();// н���ڼ�
		String pk_wa_class = bill.getPk_wa_class();// н�ʷ���

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
//				// н�ʲ������ɲ����ڼ�ĵ���û�жԵ�ǰ�ڼ�ͣ����Ա���й��ˡ�
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
	 * ͣ����Ա������н�ʲ�����
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
