package nc.ui.ic.onhand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.ic.atp.IAtpQuery;
import nc.pubitf.ic.onhand.IOnhandQry;
import nc.vo.ic.atp.pub.AtpQryCond;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandVO;
import nc.vo.ic.onhand.pub.OnhandQryCond;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.uif2.LoginContext;

import org.apache.commons.lang.StringUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * </p>
 * 
 * @since 创建日期 Apr 14, 2014
 * @author wangweir
 */
public class OnhandDataManager2 {

	boolean isQueryAll = false;
	boolean isQueryAtpType = false;

	LoginContext context = null;
	String[] groupObjectsKeys = new String[] { "pk_org", "cwarehouseid",
			"cmaterialoid", "castunitid", "cvendorid" };
	// 多组织处理
	private String[] pk_orgs;

	public OnhandVO[] queryOnhand(OnhandDimVO onhandDimVO) {
		if (isOnhandDimVOValid(onhandDimVO)) {
			return null;
		}

		// 按物料属性处理维度
		if (onhandDimVO.getCmaterialvid() != null) {
			onhandDimVO = this.getRealOnhandDim(onhandDimVO);
		}
		OnhandVO[] onhandVOs = this.doQueryInner(onhandDimVO);
		return onhandVOs;
	}

	/**
	 * @param onhandDimVO
	 * @return
	 */
	private boolean isOnhandDimVOValid(OnhandDimVO onhandDimVO) {
		return onhandDimVO == null || onhandDimVO.getPk_org() == null
				|| onhandDimVO.getCmaterialoid() == null;
	}

	/**
	 * @param onhandDimVO
	 * @return
	 * @return
	 */
	protected OnhandVO[] doQueryInner(OnhandDimVO onhandDimVO) {
		OnhandVO[] onhandVOs = null;
		try {
			OnhandDimVO cloendOnhandDimVO = (OnhandDimVO) onhandDimVO.clone();

			// 查询所有 并且 多组织不空的情况
			if (isQueryAll && !ValueCheckUtil.isNullORZeroLength(this.pk_orgs)) {
				onhandVOs = this.queryOnhand(pk_orgs, cloendOnhandDimVO);
			} else {
				OnhandQryCond cond = OnhandQryCondBuilder1.buildOnhandQryCond(
						context, cloendOnhandDimVO, this.pk_orgs);
				cond.setBquerySN(true);

				onhandVOs = NCLocator
						.getInstance()
						.lookup(IAtpQuery.class)
						.queryOnhandAndATP(cond,
								this.buildATPQueryParam(cloendOnhandDimVO));
			}
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		return onhandVOs;
	}

	/**
	 * 查询所有时，多组织处理
	 * 
	 * @param pk_orgs
	 * @param dimvo
	 * @return
	 */
	private OnhandVO[] queryOnhand(String[] pk_orgs, OnhandDimVO dimvo) {
		if (ValueCheckUtil.isNullORZeroLength(pk_orgs)) {
			return new OnhandVO[0];
		}
		OnhandDimVO[] queryDims = this.getQueryDims(pk_orgs, dimvo);
		OnhandQryCond onhandCond = OnhandQryCondBuilder1.buildOnhandQryCond(
				context, queryDims);
		AtpQryCond atpCond = this.getAtpQueryCondWithoutOrg(dimvo);
		try {
			return NCLocator.getInstance().lookup(IAtpQuery.class)
					.queryOnhandAndATP(pk_orgs, onhandCond, atpCond);
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		return new OnhandVO[0];
	}

	private AtpQryCond getAtpQueryCondWithoutOrg(OnhandDimVO dimvo) {
		AtpQryCond cond = new AtpQryCond();
		SqlBuilder sqlsf = new SqlBuilder();
		sqlsf.append(OnhandDimVO.PK_GROUP, AppContext.getInstance()
				.getPkGroup());
		sqlsf.append(" and ");
		sqlsf.append(OnhandDimVO.CMATERIALOID, dimvo.getCmaterialoid());

		List<String> groupFields = Arrays.asList(groupObjectsKeys);
		if (isQueryAll || groupFields.contains(OnhandDimVO.CWAREHOUSEID)) {
			cond.setBextwarehouse(true);
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getCwarehouseid())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.CWAREHOUSEID, dimvo.getCwarehouseid());
		}
		if (isQueryAll || groupFields.contains(OnhandDimVO.VBATCHCODE)) {
			cond.setBextbatch(true);
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getPk_batchcode())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.PK_BATCHCODE, dimvo.getPk_batchcode());
		}
		cond.setWhere(sqlsf.toString());
		return cond;
	}

	private OnhandDimVO[] getQueryDims(String[] pk_orgs, OnhandDimVO dimvo) {
		List<OnhandDimVO> queryDims = new ArrayList<OnhandDimVO>();
		for (String pk_org : pk_orgs) {
			OnhandDimVO cloneDim = (OnhandDimVO) dimvo.clone();
			cloneDim.setAttributeValue(OnhandDimVO.PK_ORG, pk_org);
			queryDims.add(cloneDim);
		}
		return queryDims.toArray(new OnhandDimVO[0]);
	}

	/**
	 * 处理为实际的结存维度，与结存表的维度一致
	 */
	private OnhandDimVO getRealOnhandDim(OnhandDimVO dimvo) {
		if (dimvo == null) {
			return dimvo;
		}

		try {
			OnhandDimVO[] dimvos = NCLocator.getInstance()
					.lookup(IOnhandQry.class)
					.getRealOnhandDim(new OnhandDimVO[] { dimvo });
			if (!ValueCheckUtil.isNullORZeroLength(dimvos)) {
				return dimvos[0];
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		return dimvo;
	}

	/**
	 * 构建可用量查询条件
	 * 
	 * @param onhandVO
	 */
	private AtpQryCond buildATPQueryParam(OnhandDimVO dimvo) {

		List<String> groupFields = Arrays.asList(groupObjectsKeys);

		AtpQryCond cond = new AtpQryCond();
		SqlBuilder sqlsf = new SqlBuilder();
		sqlsf.append(OnhandDimVO.PK_GROUP, AppContext.getInstance()
				.getPkGroup());
		if (StringUtils.isNotEmpty(dimvo.getPk_org())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.PK_ORG, dimvo.getPk_org());
		}

		sqlsf.append(" and ");
		sqlsf.append(OnhandDimVO.CMATERIALOID, dimvo.getCmaterialoid());
		if (groupFields.contains(OnhandDimVO.CWAREHOUSEID)) {
			cond.setBextwarehouse(true);
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getCwarehouseid())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.CWAREHOUSEID, dimvo.getCwarehouseid());
		}
		if (groupFields.contains(OnhandDimVO.PK_BATCHCODE)) {
			cond.setBextbatch(true);
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getPk_batchcode())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.PK_BATCHCODE, dimvo.getPk_batchcode());
		}

		if (groupFields.contains(OnhandDimVO.VBATCHCODE)) {
			cond.setBextbatch(true);
		}
		if (groupFields.contains(OnhandDimVO.CPROJECTID)) {
			cond.setBextprojectid(true);
		}
		if (groupFields.contains(OnhandDimVO.CVENDORID)) {
			cond.setBextvendorid(true);
		}
		if (groupFields.contains(OnhandDimVO.CASSCUSTID)) {
			cond.setBextasscustid(true);
		}
		if (groupFields.contains(OnhandDimVO.CPRODUCTORID)) {
			cond.setBextproductorid(true);
		}
		if (groupFields.contains(OnhandDimVO.CFFILEID)) {
			cond.setBextffileid(true);
		}
		// 设置是否按照自由辅助属性展开
		this.setBextfree(groupFields, cond);

		// 增加固定辅助属性
		// 项目
		if (!StringUtil.isSEmptyOrNull(dimvo.getCprojectid())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.CPROJECTID, dimvo.getCprojectid());
			// cond.setBextprojectid(true);
		}
		// 供应商
		if (!StringUtil.isSEmptyOrNull(dimvo.getCvendorid())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.CVENDORID, dimvo.getCvendorid());
			// cond.setBextvendorid(true);
		}
		// 客户
		if (!StringUtil.isSEmptyOrNull(dimvo.getCasscustid())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.CASSCUSTID, dimvo.getCasscustid());
			// cond.setBextasscustid(true);
		}
		// 生产厂商
		if (!StringUtil.isSEmptyOrNull(dimvo.getCproductorid())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.CPRODUCTORID, dimvo.getCproductorid());
			// cond.setBextproductorid(true);
		}
		// 特征码
		if (!StringUtil.isSEmptyOrNull(dimvo.getCffileid())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.CFFILEID, dimvo.getCffileid());
			// cond.setBextffileid(true);
		}
		// 增加自由辅助属性条件
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree1())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE1, dimvo.getVfree1());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree2())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE2, dimvo.getVfree2());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree3())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE3, dimvo.getVfree3());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree4())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE4, dimvo.getVfree4());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree5())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE5, dimvo.getVfree5());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree6())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE6, dimvo.getVfree6());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree7())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE7, dimvo.getVfree7());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree8())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE8, dimvo.getVfree8());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree9())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE9, dimvo.getVfree9());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree10())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE10, dimvo.getVfree10());
		}
		cond.setWhere(sqlsf.toString());
		cond.setPk_org(dimvo.getPk_org());

		return cond;
	}

	private void setBextfree(List<String> groupFields, AtpQryCond cond) {
		for (int i = 1; i <= 10; i++) {
			if (groupFields.contains("vfree" + i)) {
				cond.setBextfree(true);
				break;
			}
		}
	}

	public boolean isQueryAll() {
		return isQueryAll;
	}

	public void setQueryAll(boolean isQueryAll) {
		this.isQueryAll = isQueryAll;
	}

	public boolean isQueryAtpType() {
		return isQueryAtpType;
	}

	public void setQueryAtpType(boolean isQueryAtpType) {
		this.isQueryAtpType = isQueryAtpType;
	}

	public LoginContext getContext() {
		return context;
	}

	public void setContext(LoginContext context) {
		this.context = context;
	}

	public String[] getGroupObjectsKeys() {
		return groupObjectsKeys;
	}

	public void setGroupObjectsKeys(String[] groupObjectsKeys) {
		this.groupObjectsKeys = groupObjectsKeys;
	}

}
