package nc.ui.ic.onhand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.itf.scmpub.reference.uap.permission.FunctionPermissionPubService;
import nc.itf.uap.bbd.func.IFuncRegisterQueryService;
import nc.ui.ic.material.query.InvInfoUIQuery;
import nc.vo.ic.atp.entity.AtpVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandNumVO;
import nc.vo.ic.onhand.pub.OnhandQryCond;
import nc.vo.ic.onhand.pub.OnhandVOTools;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.uap.rbac.FuncSubInfo;
import nc.vo.uif2.LoginContext;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 存量查询条件创建工具类
 * </p>
 * 
 * @since 创建日期 Apr 17, 2014
 * @author wangweir
 */
public class OnhandQryCondBuilder1 {

	public static OnhandQryCond buildOnhandQryCond(LoginContext context,
			OnhandDimVO dimvo, String[] pk_orgs) {

		OnhandQryCond cond = new OnhandQryCond();

		List<String> selectFields = buildSelectField();

		setOnhandCondFilter(cond, dimvo);

		cond.setISSum(true);
		cond.addSelectFields(selectFields.toArray(new String[0]));

		cond.setBshowatp(false);// 不显示可用量
		cond.setBshowforeatp(false);// 不显示可用量

		//
		SqlBuilder sql = new SqlBuilder();
		sql.append(" 1= 1 ");

		buildNumMoreThanZeroSQL(sql, true);
		// 组织权限
		buildFuncPermissionPkOrgs(context, cond, pk_orgs);
		cond.setWhere(sql.toString());

		return cond;
	}

	public static OnhandQryCond buildOnhandQryCond(LoginContext context,
			OnhandDimVO[] dimvos) {
		// 按物料属性处理维度
		OnhandVOTools.getRealOnhandDim(InvInfoUIQuery.getInstance()
				.getInvInfoQuery(), dimvos);

		OnhandQryCond cond = buildOnhandQryCond(context, dimvos[0], null);
		for (int i = 1; i < dimvos.length; i++) {
			setOnhandCondFilter(cond, dimvos[i]);
		}
		return cond;
	}

	/**
	 * @param sql
	 * @param
	 */
	public static void buildFuncPermissionPkOrgs(LoginContext context,
			OnhandQryCond cond, String[] pk_orgs) {
		// 拼接组织权限，在后台处理，防止组织太多in的时候报错
		cond.setPk_orgs(getPermissionPkOrgs(context, pk_orgs));
	}

	private static String[] getPermissionPkOrgs(LoginContext context1,
			String[] pk_orgs) {
		if (!ValueCheckUtil.isNullORZeroLength(pk_orgs)) {
			return pk_orgs;
		}

		// 查询节点类型
		String funCodeType = null;
		try {
			funCodeType = NCLocator.getInstance()
					.lookup(IFuncRegisterQueryService.class)
					.queryOrgtypeCode4Funcode(context1.getNodeCode());
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}

		// 如果调用批次参照的节点的类型是库存组织，则直接取到有权限的组织即可
		if ("STOCKORGTYPE00000000".equals(funCodeType)) {
			FuncSubInfo funcSubInfo = context1.getFuncInfo();
			if (context1 != null) {
				if (funcSubInfo != null) {
					return funcSubInfo.getFuncPermissionPkorgs();
				}
			}
		} else {
			// 如果调用批次参照的节点的类型不是库存组织，则需要根据用户查询其有权限的库存组织
			String cuserid = context1.getPk_loginUser();
			String funcode = "40080608";
			String pkGroup = context1.getPk_group();
			// 只找到了这个接口，比较恶心，必须传入节点号，就找了个库存节点
			String[] permissionStockOrgs = FunctionPermissionPubService
					.getUserPermissionPkOrgs(cuserid, funcode, pkGroup);
			return permissionStockOrgs;
		}

		return null;
	}

	/**
	 * 拼接数量sql片段
	 * 
	 * @param sql
	 * @param isContainZero
	 *            是否包含0结存
	 * @return
	 */
	private static SqlBuilder buildNumMoreThanZeroSQL(SqlBuilder sql,
			boolean isContainZero) {
		sql.append(" and ");
		sql.startParentheses();
		int count = OnhandNumVO.numfields.length;

		for (int i = 0; i < count; i++) {
			// 包含0结存
			if (isContainZero
					&& (OnhandNumVO.NONHANDNUM.equals(OnhandNumVO.numfields[i]) || OnhandNumVO.NONHANDASTNUM
							.equals(OnhandNumVO.numfields[i]))) {
				sql.append(" COALESCE(" + OnhandNumVO.numfields[i] + ",0) = 0 ");
				sql.append(" or ");
			}
			sql.append(" COALESCE(" + OnhandNumVO.numfields[i] + ",0) <> 0 ");
			if (i != count - 1) {
				sql.append(" or ");
			}
		}
		sql.endParentheses();
		return sql;
	}

	/**
	 * @param
	 * @return
	 */
	private static List<String> buildSelectField() {
		Set<String> listdims = new HashSet<String>();
		// 分组维度无值，也要按库存组织出可用量、现存量
		listdims.add(OnhandDimVO.PK_GROUP);
		listdims.add(OnhandDimVO.CMATERIALOID);
		listdims.add(OnhandDimVO.PK_ORG);
		String[] groupObjectsKeys = new String[] { "pk_org", "cwarehouseid",
				"cmaterialoid", "castunitid", "cvendorid" };
		listdims.addAll(Arrays.asList(groupObjectsKeys));
		listdims.addAll(Arrays
				.asList(new String[] { "pk_org", "cmaterialoid" }));

		// 分组里面有批次或者物料Vid时，需要特殊处理
		if (listdims.contains(OnhandDimVO.VBATCHCODE)) {
			listdims.add(OnhandDimVO.VBATCHCODE);
			listdims.add(OnhandDimVO.PK_BATCHCODE);
		}
		if (listdims.contains(OnhandDimVO.CMATERIALVID)) {
			listdims.add(OnhandDimVO.CMATERIALVID);
			listdims.add(OnhandDimVO.CMATERIALOID);
		}
		if (listdims.contains(OnhandDimVO.CMATERIALOID)) {
			listdims.add(OnhandDimVO.CMATERIALOID);
			listdims.add(OnhandDimVO.CMATERIALVID);
		}

		return new ArrayList<String>(listdims);
	}

	/**
	 * 处理现存量查询过滤条件
	 * 
	 * @param cond
	 * @param dimvo
	 * @param listdims
	 */
	private static void setOnhandCondFilter(OnhandQryCond cond,
			OnhandDimVO dimvo) {
		List<String> filterList = new ArrayList<String>();
		List<Object> valueList = new ArrayList<Object>();
		List<String> atpdims = Arrays.asList(AtpVO.atpDims);
		for (String key : dimvo.getAttributeNames()) {
			if (dimvo.getAttributeValue(key) == null
					|| "".equals(dimvo.getAttributeValue(key))) {
				continue;
			}
			if (!atpdims.contains(key)) {
				continue;
			}
			filterList.add(key);
			valueList.add(dimvo.getAttributeValue(key));
		}
		cond.addFilterDimConditon(filterList.toArray(new String[0]),
				valueList.toArray(new Object[0]));
	}

}
