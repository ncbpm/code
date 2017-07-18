package nc.bpm.fa.pfxx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.itf.fa.prv.IAsset;
import nc.itf.fa.service.IAlterImport;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.pub.fa.card.AssetFieldConst;
import nc.pub.fa.common.consts.BillTypeConst;
import nc.pub.fa.common.manager.FABDDataManager;
import nc.pub.fa.common.manager.VOManager;
import nc.pub.fa.common.util.StringUtils;
import nc.pub.fa.common.util.UseDeptScaleUtils;
import nc.pubitf.bd.accessor.IGeneralAccessor;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.common.util.CollectionUtils;
import nc.vo.am.common.util.ExceptionUtils;
import nc.vo.am.common.util.StringTools;
import nc.vo.am.common.util.UFDoubleUtils;
import nc.vo.am.proxy.AMProxy;
import nc.vo.bd.accessor.NullGeneralAccessor;
import nc.vo.fa.alter.AlterBodyVO;
import nc.vo.fa.alter.AlterHeadVO;
import nc.vo.fa.alter.AlterSheetVO;
import nc.vo.fa.alter.AlterVO;
import nc.vo.fa.alter.helper.AlterKeyConst;
import nc.vo.fa.asset.AssetVO;
import nc.vo.fa.deptscale.DeptScaleVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
//import nc.bs.xml.out.tool.XmlOutTool;

public class ModDeptPlugin extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		// 1.得到转换后的VO数据,取决于向导第一步注册的VO信息
		AlterVO billVO = (AlterVO) vo;

		AlterHeadVO headVO = (AlterHeadVO) billVO.getParentVO();
		// 单据保存或者更新之前调用的查询操作，查询当前单据以前是否被导入过
		// 如果已经被导入过，那么返回已经导入单据的PK，否则返回NULL值
		String billPK = PfxxPluginUtils.queryBillPKBeforeSaveOrUpdate(
				swapContext.getBilltype(), headVO.getBill_code(),
				swapContext.getOrgPk());

		// 如果导入过
		if (null != billPK && billPK.length() > 0) {
			// 查看配置文件信息是否允许导入重复数据
			if (swapContext.getReplace().equalsIgnoreCase("N")) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("farule_0", "02012060-0037")/*
																				 * @
																				 * res
																				 * "不允许重复导入单据，如果想更新已导入单据，请把数据文件的replace标志设为‘Y’！"
																				 */);
			}
			// 设置主键
			headVO.setPrimaryKey(billPK);
			// 查询数据库中是否存在该单据
			AlterVO alterVO = ArrayUtils.getFirstElem(AMProxy.lookup(
					IAlterImport.class).queryAlterVO(billPK));
			if (null != alterVO) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("farule_0", "02012060-0038")/*
																				 * @
																				 * res
																				 * "该单据已经导入，并且已经审核，不允许重复导入。"
																				 */);
			}
		}

		// 2. 将bill中的AlterSheetVO中的item_code取出
		String[] showKeys = getShowKeyList(billVO);

		// 3. 查询卡片
		AssetVO[] cardVOs = queryCardVOs(billVO, showKeys);

		// 4. 校验
		checkBill(billVO, cardVOs, showKeys);

		// 5. 将卡片的值往alterBodyVO中变动前中赋值，而传过来的单据则往变动后赋值
		setBillVOValue(billVO, cardVOs, showKeys);

		// 6. 生成变动单据
		//替换showKeys中的 pk_usedept
		for(int i=0; i<showKeys.length; i++){
			if(showKeys[i].equals("pk_usedept")){
				showKeys[i]="usedept";
			}
		}
		AlterVO returnBillVO = AMProxy.lookup(IAlterImport.class).importInsert(
				billVO, (List<String>) CollectionUtils.convertToList(showKeys));

		return returnBillVO;
	}

	@SuppressWarnings("null")
	private String[] getShowKeyList(AlterVO billVO) throws BusinessException {
		AlterHeadVO headVO = (AlterHeadVO) billVO.getParentVO();
		AlterBodyVO[] bodyVOs = (AlterBodyVO[]) billVO.getChildrenVO();
		if (headVO == null || bodyVOs == null || bodyVOs.length == 0) {
			ExceptionUtils
					.asBusinessException(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("farule_0",
									"02012060-0039")/*
													 * @res
													 * "变动单表头或者表体没有数据, 请检查数据文件 \n "
													 */);
		}

		List<String> itemList = new ArrayList<String>();

		for (AlterBodyVO bodyVO : bodyVOs) {
			AlterSheetVO[] sheetVOs = bodyVO.getAltersheetvos();
			if (sheetVOs == null || sheetVOs.length == 0) {
				ExceptionUtils
						.asBusinessException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("farule_0",
										"02012060-0040")/*
														 * @res
														 * "altersheetvos为空, 请检查数据文件 \n "
														 */);
			}

			for (AlterSheetVO sheetVO : sheetVOs) {
				if (!itemList.contains(sheetVO.getItem_code())) {
					itemList.add(sheetVO.getItem_code());
				}
			}
		}

		if (isAlter(itemList)) {
			if (!itemList.contains(AssetFieldConst.LOCALORIGINVALUE)) {
				itemList.add(AssetFieldConst.LOCALORIGINVALUE);
			}
			if (!itemList.contains(AssetFieldConst.SALVAGE)) {
				itemList.add(AssetFieldConst.SALVAGE);
			}
			if (!itemList.contains(AssetFieldConst.SALVAGERATE)) {
				itemList.add(AssetFieldConst.SALVAGERATE);
			}
		}

		return itemList.toArray(new String[0]);
	}

	private boolean isAlter(List<String> itemList) {
		if (itemList.contains(AssetFieldConst.LOCALORIGINVALUE)
				|| itemList.contains(AssetFieldConst.SALVAGE)
				|| itemList.contains(AssetFieldConst.SALVAGERATE)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 查询卡片 <b>参数说明</b>
	 * 
	 * @param billVO
	 * @param showKeys
	 * @return
	 * @throws BusinessException
	 */
	private AssetVO[] queryCardVOs(AlterVO billVO, String[] showKeys)
			throws BusinessException {
		String[] pk_cards = VOManager.getAttributeValueArray(
				billVO.getChildrenVO(), AssetFieldConst.PK_CARD);

		AlterHeadVO headVO = (AlterHeadVO) billVO.getParentVO();
		String whereSQL = " fa_card.pk_card in ("
				+ StringTools.buildStringUseSpliterWithQuotes(pk_cards, ",")
				+ ") " + " and laststate_flag = 'Y' ";
		if (StringUtils.isNotEmpty(headVO.getPk_accbook())) {
			whereSQL += " and pk_accbook = '" + headVO.getPk_accbook() + "'";
		} else {
			whereSQL += " and business_flag = '" + UFBoolean.TRUE + "'";
		}

		// 添加查询字段
		List<String> keysList = (List<String>) CollectionUtils
				.convertToList(showKeys);
		keysList.add(AssetFieldConst.PK_CARD);

		//AssetVO[] assetVOs = AMProxy.lookup(IAssetService.class).queryAssetFieldValues(whereSQL,keysList.toArray(new String[0]));
		AssetVO[] assetVOs = AMProxy.lookup(IAsset.class).queryAssetVOBySQL(whereSQL);
		if (assetVOs == null || assetVOs.length == 0) {
			ExceptionUtils
					.asBusinessException(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("farule_0",
									"02012060-0041")/*
													 * @res
													 * " 没有查询到任何一张卡片, 请检查 \n "
													 */);
		}

		return assetVOs;
	}

	/**
	 * 
	 * 校验单据
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param billVO
	 *            <p>
	 * @author weizq
	 * @param showKeys
	 * @throws BusinessException
	 * @time 2011-4-19 下午03:46:59
	 */
	private void checkBill(AlterVO billVO, AssetVO[] assetVOs, String[] showKeys)
			throws BusinessException {
		String[] pk_cards = VOManager.getAttributeValueArray(
				billVO.getChildrenVO(), AssetFieldConst.PK_CARD);

		Map<String, AssetVO> cardsMap = new HashMap<String, AssetVO>();
		for (AssetVO vo : assetVOs) {
			cardsMap.put(vo.getPk_card(), vo);
		}

		StringBuffer errorMsg = new StringBuffer();
		List<String> cards = new ArrayList<String>();
		for (String pk_card : pk_cards) {
			AssetVO cardVO = cardsMap.get(pk_card);
			if (cardVO == null) {
				cards.add(pk_card);
			}
		}

		// 1. 校验卡片是否存在
		if (cards.size() != 0) {
			/* @res "在变动单明细(*)中输入的变动的卡片项目[{0}] 有问题。 \n" */
			errorMsg.append(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes()
					.getStrByID(
							"farule_0",
							"02012060-0043",
							null,
							new String[] { StringTools
									.buildStringUseSpliterWithQuotes(
											cards.toArray(new String[0]), ",") }));
		}

		// 2. 校验输入的卡片的变动项是否正确
		// 所有卡片的字段名称
		List<String> cardItemKeys = Arrays.asList(new AssetVO()
				.getAttributeNames());
		// 有问题的变动的卡片项目
		List<String> errorItemKeys = new ArrayList<String>();
		for (String showKey : showKeys) {
			// 导入的xml中孙表中的字段名称错误
			if (!cardItemKeys.contains(showKey)) {
				errorItemKeys.add(showKey);
			}
		}

		if (errorItemKeys.size() != 0) {
			/* @res "在变动单明细(*)中输入的变动的卡片项目[{0}] 有问题。 \n" */
			errorMsg.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID(
							"farule_0",
							"02012060-0044",
							null,
							new String[] { StringTools
									.buildStringUseSpliterWithQuotes(
											errorItemKeys
													.toArray(new String[0]),
											",") }));

			ExceptionUtils.asBusinessException(errorMsg.toString());
		}

		// 3. 校验孙表填入的变动前的值是否和卡片相同
		for (AlterBodyVO bodyVO : (AlterBodyVO[]) billVO.getChildrenVO()) {
			String pk_card = bodyVO.getPk_card();
			// 当前导入的变动卡片VO
			AssetVO cardVO = cardsMap.get(pk_card);
			for (AlterSheetVO sheetVO : bodyVO.getAltersheetvos()) {
				// 变动项
				String itemKey = sheetVO.getItem_code();
				// 当前变动前值
				Object alter_before = sheetVO.getOld_content();

				// 变动前 和 卡片都不为空
				if (cardVO.getAttributeValue(itemKey) != null
						&& alter_before != null) {

					// 匹配值类型
					try {
						UFDouble cardValue = new UFDouble(
								sheetVO.getOld_content());
						UFDouble oldValue = new UFDouble(
								alter_before.toString());

						// 不相等校验
						if (cardValue.compareTo(oldValue) != 0) {
							/* @res "卡片主键为[{0}]对应的变动卡片项[{1}]的值与卡片不相同, 不能导入。 \n" */
							errorMsg.append(nc.vo.ml.NCLangRes4VoTransl
									.getNCLangRes().getStrByID("farule_0",
											"02012060-0046", null,
											new String[] { pk_card, itemKey }));
						}
					} catch (NumberFormatException e) {
						try {
							Integer cardInt = new Integer(
									sheetVO.getOld_content());
							Integer oldInt = new Integer(
									alter_before.toString());
							// 不相等校验
							if (oldInt != cardInt) {
								/*
								 * @res
								 * "卡片主键为[{0}]对应的变动卡片项[{1}]的值与卡片不相同, 不能导入。 \n"
								 */
								errorMsg.append(nc.vo.ml.NCLangRes4VoTransl
										.getNCLangRes()
										.getStrByID(
												"farule_0",
												"02012060-0046",
												null,
												new String[] { pk_card, itemKey }));
							}
						} catch (NumberFormatException e1) {
							// 不相等校验
							if (!cardVO.getAttributeValue(itemKey).toString()
									.equals(alter_before.toString())) {
								/*
								 * @res
								 * "卡片主键为[{0}]对应的变动卡片项[{1}]的值与卡片不相同, 不能导入。 \n"
								 */
								errorMsg.append(nc.vo.ml.NCLangRes4VoTransl
										.getNCLangRes()
										.getStrByID(
												"farule_0",
												"02012060-0046",
												null,
												new String[] { pk_card, itemKey }));
							}
						}
					}

					// 只有一个为空, 也不可以
				} else if (!(cardVO.getAttributeValue(itemKey) == null && alter_before == null)) {
					/* @res "卡片主键为[{0}]对应的变动卡片项[{1}]的值与卡片不相同, 不能导入。 \n" */
					errorMsg.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("farule_0", "02012060-0046", null,
									new String[] { pk_card, itemKey }));
				}
			}
		}

		if (errorMsg.length() != 0) {
			ExceptionUtils.asBusinessException(errorMsg.toString());
		}
	}

	/**
	 * 将卡片的值往alterBodyVO中变动前中赋值，而传过来的单据则往变动后赋值
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param billVO
	 * @param cardVOs
	 */
	private void setBillVOValue(AlterVO billVO, AssetVO[] cardVOs,
			String[] showKeys) throws BusinessException {
		AlterBodyVO[] bodyVOs = (AlterBodyVO[]) billVO.getChildrenVO();

		Map<String, AssetVO> cardsMap = new HashMap<String, AssetVO>();
		for (AssetVO vo : cardVOs) {
			cardsMap.put(vo.getPk_card(), vo);
		}

		// 非法的变动字段名称
		List<String> notFitFields = new ArrayList<String>();

		// 卡片所有字段
		List<String> cardFields = (List<String>) CollectionUtils
				.convertToList(ArrayUtils.getFirstElem(cardVOs)
						.getAttributeNames());

		for (AlterBodyVO bodyVO : bodyVOs) {
			AlterSheetVO[] sheetVOs = bodyVO.getAltersheetvos();

			Map<String, AlterSheetVO> sheetCodes = new HashMap<String, AlterSheetVO>();
			for (AlterSheetVO sheetVO : sheetVOs) {
				sheetCodes.put(sheetVO.getItem_code(), sheetVO);
			}

			for (String showKey : showKeys) {
				if (!cardFields.contains(showKey)) {
					notFitFields.add(showKey);
					continue;
				}

				AlterSheetVO sheetVO = sheetCodes.get(showKey);
				if (sheetCodes.keySet().contains(showKey)) {
					IGeneralAccessor generalAccessor = FABDDataManager
							.createGeneralAccessor(showKey);
					// NullGeneralAccessor 类型说明不是档案类型
					if (generalAccessor instanceof NullGeneralAccessor) {
						// 变动前
						bodyVO.setAttributeValue(sheetVO.getItem_code()
								+ AlterKeyConst.BEFORE_SUFFIX,
								sheetVO.getOld_content());

						// 变动后
						bodyVO.setAttributeValue(sheetVO.getItem_code()
								+ AlterKeyConst.AFTER_SUFFIX,
								sheetVO.getNew_content());

						// 变动部分
						UFDouble newValue = new UFDouble(
								sheetVO.getNew_content());
						UFDouble oldValue = new UFDouble(
								sheetVO.getOld_content());
						bodyVO.setAttributeValue(sheetVO.getItem_code()
								+ AlterKeyConst.ALTER_SUFFIX,
								UFDoubleUtils.sub(newValue, oldValue));
					} else {
						// 1. 通过编码来取(用的比较多) 这个是走缓存的
						Map<String, String> codeAndPkMaps = FABDDataManager
								.getBDPKByCodes(showKey, new String[] {
										sheetCodes.get(showKey)
												.getOld_content(),
										sheetCodes.get(showKey)
												.getNew_content() },
										((AlterHeadVO) billVO.getParentVO())
												.getPk_org());

						if (codeAndPkMaps != null && codeAndPkMaps.size() != 0) {
							// 变动前PK
							String before = codeAndPkMaps.get(sheetVO
									.getOld_content());
							// 变动后PK
							String after = codeAndPkMaps.get(sheetVO
									.getNew_content());

							// 使用部门单独处理， 要转化成link_key存储
							if (showKey.equals(AssetFieldConst.PK_USEDEPT)) {
								
								sheetVO.setItem_code(AlterKeyConst.USEDEPT);
								// 变动前
								// 变动前
								DeptScaleVO scaleVO = new DeptScaleVO();
								scaleVO.setLink_key(before);
								scaleVO.setUsescale(new UFDouble(100));
								scaleVO.setTotalscale(new UFDouble(100));
								bodyVO.setOld_deptscalevos(before == null ?null :new DeptScaleVO[]{scaleVO});
								// 变动后
								String[] pk_depts = new String[] {after };
								Map<String, DeptScaleVO> pkAndLinkeys = createUsedeptScaleVOs(pk_depts);
							
								bodyVO.setNew_deptscalevos(pkAndLinkeys.get(after) == null ?null :new DeptScaleVO[]{pkAndLinkeys.get(after)});
								
								//
								before = pkAndLinkeys.get(before) == null ?null :pkAndLinkeys.get(before).getLink_key();
								after = pkAndLinkeys.get(after) == null ?null :pkAndLinkeys.get(after).getLink_key();

							}
//
//							bodyVO.setAttributeValue(sheetVO.getItem_code()
//									+ AlterKeyConst.BEFORE_SUFFIX, before);
							bodyVO.setAttributeValue(sheetVO.getItem_code()
									+ AlterKeyConst.AFTER_SUFFIX, after);

						} else {
							// 2. 通过名称来取
							Map<String, String> nameAndPkMaps = FABDDataManager
									.getBDPKByNames(
											showKey,
											new String[] {
													sheetCodes.get(showKey)
															.getOld_content(),
													sheetCodes.get(showKey)
															.getNew_content() },
											((AlterHeadVO) billVO.getParentVO())
													.getPk_org());
							if (nameAndPkMaps != null
									&& nameAndPkMaps.size() != 0) {
								// 变动前PK
								String before = nameAndPkMaps.get(sheetVO
										.getOld_content());
								// 变动后PK
								String after = nameAndPkMaps.get(sheetVO
										.getNew_content());

								// 使用部门单独处理， 要转化成link_key存储
								if (showKey.equals(AssetFieldConst.PK_USEDEPT)) {
									
									sheetVO.setItem_code(AlterKeyConst.USEDEPT);
									// 变动前
									// 变动前
									DeptScaleVO scaleVO = new DeptScaleVO();
									scaleVO.setLink_key(before);
									scaleVO.setUsescale(new UFDouble(100));
									scaleVO.setTotalscale(new UFDouble(100));
									bodyVO.setOld_deptscalevos(before == null ?null :new DeptScaleVO[]{scaleVO});
									// 变动后
									String[] pk_depts = new String[] {after };
									Map<String, DeptScaleVO> pkAndLinkeys = createUsedeptScaleVOs(pk_depts);
								
									bodyVO.setNew_deptscalevos(pkAndLinkeys.get(after) == null ?null :new DeptScaleVO[]{pkAndLinkeys.get(after)});
									
									//
									before = pkAndLinkeys.get(before) == null ?null :pkAndLinkeys.get(before).getLink_key();
									after = pkAndLinkeys.get(after) == null ?null :pkAndLinkeys.get(after).getLink_key();

								}

								bodyVO.setAttributeValue(sheetVO.getItem_code()
										+ AlterKeyConst.BEFORE_SUFFIX, before);
								bodyVO.setAttributeValue(sheetVO.getItem_code()
										+ AlterKeyConst.AFTER_SUFFIX, after);
							} else {
								String before = sheetVO.getOld_content();
								String after = sheetVO.getNew_content();

								// 3. 直接就是按照PK来导入, 不需要查询， 直接保存
								// 使用部门单独处理， 要转化成link_key存储
								if (showKey.equals(AssetFieldConst.PK_USEDEPT)) {
									
									sheetVO.setItem_code(AlterKeyConst.USEDEPT);
									// 变动前
									// 变动前
									DeptScaleVO scaleVO = new DeptScaleVO();
									scaleVO.setLink_key(before);
									scaleVO.setUsescale(new UFDouble(100));
									scaleVO.setTotalscale(new UFDouble(100));
									bodyVO.setOld_deptscalevos(before == null ?null :new DeptScaleVO[]{scaleVO});
									// 变动后
									String[] pk_depts = new String[] {after };
									Map<String, DeptScaleVO> pkAndLinkeys = createUsedeptScaleVOs(pk_depts);
								
									bodyVO.setNew_deptscalevos(pkAndLinkeys.get(after) == null ?null :new DeptScaleVO[]{pkAndLinkeys.get(after)});
									
									//
									before = pkAndLinkeys.get(before) == null ?null :pkAndLinkeys.get(before).getLink_key();
									after = pkAndLinkeys.get(after) == null ?null :pkAndLinkeys.get(after).getLink_key();

								}

								bodyVO.setAttributeValue(sheetVO.getItem_code()
										+ AlterKeyConst.BEFORE_SUFFIX, before);
								bodyVO.setAttributeValue(sheetVO.getItem_code()
										+ AlterKeyConst.AFTER_SUFFIX, after);
								
							}
						}
					}
				}
			}

			// 重新计算本币原值，净残值，净残值率
			if (isAlter((List<String>) CollectionUtils.convertToList(showKeys))) {
				UFDouble after_localvalue = (UFDouble) bodyVO
						.getAttributeValue(AlterKeyConst.LOCALORIGINVALUE_AFTER);
				UFDouble after_salvage = (UFDouble) bodyVO
						.getAttributeValue(AlterKeyConst.SALVAGE_AFTER);
				UFDouble after_salvarate = UFDoubleUtils.div((UFDouble) bodyVO
						.getAttributeValue(AlterKeyConst.SALVAGERATE_AFTER),
						new UFDouble(100));

				if (!bodyVO
						.getAttributeValue(AlterKeyConst.SALVAGE_BEFORE)
						.equals(bodyVO
								.getAttributeValue(AlterKeyConst.SALVAGE_AFTER))) {
					if (UFDoubleUtils.isNullOrZero(after_salvage)) {
						bodyVO.setAttributeValue(
								AlterKeyConst.SALVAGERATE_AFTER,
								UFDouble.ZERO_DBL);
					} else {
						if (!UFDoubleUtils.isNullOrZero(after_localvalue)) {
							bodyVO.setAttributeValue(
									AlterKeyConst.SALVAGERATE_AFTER,
									UFDoubleUtils.div(after_salvage,
											after_localvalue));
						}
					}
				} else {
					if (null != bodyVO
							.getAttributeValue(AlterKeyConst.SALVAGERATE_AFTER)
							&& !bodyVO
									.getAttributeValue(
											AlterKeyConst.SALVAGERATE_AFTER)
									.equals(bodyVO
											.getAttributeValue(AlterKeyConst.SALVAGERATE_AFTER))) {
						bodyVO.setAttributeValue(AlterKeyConst.SALVAGE_AFTER,
								UFDoubleUtils.multiply(after_localvalue,
										after_salvarate));
					} else {
						bodyVO.setAttributeValue(AlterKeyConst.SALVAGE_AFTER,
								UFDoubleUtils.multiply(after_localvalue,
										after_salvarate));
					}
				}
			}
		}

		String pk_group = (String) billVO.getParentVO().getAttributeValue(
				AlterHeadVO.PK_GROUP);
		String pk_org = (String) billVO.getParentVO().getAttributeValue(
				AlterHeadVO.PK_ORG);
//		// 调用单据号接口单据号
//		// 获取单据号管理服务
//		IBillcodeManage billCodeManager = AMProxy.lookup(IBillcodeManage.class);
//
//		// 取得新的单据号
//		String billCode = billCodeManager.getPreBillCode_RequiresNew(
//				BillTypeConst.ALTER, pk_group, pk_org);
//		((AlterHeadVO) billVO.getParentVO()).setBill_code(billCode);

		if (notFitFields.size() != 0) {
			ExceptionUtils
					.asBusinessException(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("farule_0",
									"02012060-0049")/*
													 * @res
													 * "数据文件xml中altersheetvos标签的item_code值, 在卡片中不存在，请重新确认. \n "
													 */);
		}
	}

	/**
	 * 插入使用部门返回link_key <b>参数说明</b>
	 * 
	 * @param pk_depts
	 * @return
	 */
	private Map<String, DeptScaleVO> createUsedeptScaleVOs(String[] pk_depts)
			throws BusinessException {
		List<DeptScaleVO> deptScaleVOs = new ArrayList<DeptScaleVO>();
		for (String pk_dept : pk_depts) {
			if(org.apache.commons.lang.StringUtils.isEmpty(pk_dept)){
				continue;
			}
			DeptScaleVO scaleVO = new DeptScaleVO();
			scaleVO.setPk_dept(pk_dept);
			scaleVO.setUsescale(new UFDouble(100));
			scaleVO.setTotalscale(new UFDouble(100));
			deptScaleVOs.add(scaleVO);
		}

		DeptScaleVO[] deptVOs = UseDeptScaleUtils.insert(deptScaleVOs
				.toArray(new DeptScaleVO[0]));

		Map<String, DeptScaleVO> pkAndLinkeys = new HashMap<String, DeptScaleVO>();

		for (DeptScaleVO vo : deptVOs) {
			pkAndLinkeys.put(vo.getPk_dept(), vo);
		}
		return pkAndLinkeys;
	}
}
