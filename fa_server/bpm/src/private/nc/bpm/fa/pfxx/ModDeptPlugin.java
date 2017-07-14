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
		// TODO �Զ����ɵķ������
		// 1.�õ�ת�����VO����,ȡ�����򵼵�һ��ע���VO��Ϣ
		AlterVO billVO = (AlterVO) vo;

		AlterHeadVO headVO = (AlterHeadVO) billVO.getParentVO();
		// ���ݱ�����߸���֮ǰ���õĲ�ѯ��������ѯ��ǰ������ǰ�Ƿ񱻵����
		// ����Ѿ������������ô�����Ѿ����뵥�ݵ�PK�����򷵻�NULLֵ
		String billPK = PfxxPluginUtils.queryBillPKBeforeSaveOrUpdate(
				swapContext.getBilltype(), headVO.getBill_code(),
				swapContext.getOrgPk());

		// ��������
		if (null != billPK && billPK.length() > 0) {
			// �鿴�����ļ���Ϣ�Ƿ��������ظ�����
			if (swapContext.getReplace().equalsIgnoreCase("N")) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("farule_0", "02012060-0037")/*
																				 * @
																				 * res
																				 * "�������ظ����뵥�ݣ����������ѵ��뵥�ݣ���������ļ���replace��־��Ϊ��Y����"
																				 */);
			}
			// ��������
			headVO.setPrimaryKey(billPK);
			// ��ѯ���ݿ����Ƿ���ڸõ���
			AlterVO alterVO = ArrayUtils.getFirstElem(AMProxy.lookup(
					IAlterImport.class).queryAlterVO(billPK));
			if (null != alterVO) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("farule_0", "02012060-0038")/*
																				 * @
																				 * res
																				 * "�õ����Ѿ����룬�����Ѿ���ˣ��������ظ����롣"
																				 */);
			}
		}

		// 2. ��bill�е�AlterSheetVO�е�item_codeȡ��
		String[] showKeys = getShowKeyList(billVO);

		// 3. ��ѯ��Ƭ
		AssetVO[] cardVOs = queryCardVOs(billVO, showKeys);

		// 4. У��
		checkBill(billVO, cardVOs, showKeys);

		// 5. ����Ƭ��ֵ��alterBodyVO�б䶯ǰ�и�ֵ�����������ĵ��������䶯��ֵ
		setBillVOValue(billVO, cardVOs, showKeys);

		// 6. ���ɱ䶯����
		//�滻showKeys�е� pk_usedept
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
													 * "�䶯����ͷ���߱���û������, ���������ļ� \n "
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
														 * "altersheetvosΪ��, ���������ļ� \n "
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
	 * ��ѯ��Ƭ <b>����˵��</b>
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

		// ��Ӳ�ѯ�ֶ�
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
													 * " û�в�ѯ���κ�һ�ſ�Ƭ, ���� \n "
													 */);
		}

		return assetVOs;
	}

	/**
	 * 
	 * У�鵥��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param billVO
	 *            <p>
	 * @author weizq
	 * @param showKeys
	 * @throws BusinessException
	 * @time 2011-4-19 ����03:46:59
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

		// 1. У�鿨Ƭ�Ƿ����
		if (cards.size() != 0) {
			/* @res "�ڱ䶯����ϸ(*)������ı䶯�Ŀ�Ƭ��Ŀ[{0}] �����⡣ \n" */
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

		// 2. У������Ŀ�Ƭ�ı䶯���Ƿ���ȷ
		// ���п�Ƭ���ֶ�����
		List<String> cardItemKeys = Arrays.asList(new AssetVO()
				.getAttributeNames());
		// ������ı䶯�Ŀ�Ƭ��Ŀ
		List<String> errorItemKeys = new ArrayList<String>();
		for (String showKey : showKeys) {
			// �����xml������е��ֶ����ƴ���
			if (!cardItemKeys.contains(showKey)) {
				errorItemKeys.add(showKey);
			}
		}

		if (errorItemKeys.size() != 0) {
			/* @res "�ڱ䶯����ϸ(*)������ı䶯�Ŀ�Ƭ��Ŀ[{0}] �����⡣ \n" */
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

		// 3. У���������ı䶯ǰ��ֵ�Ƿ�Ϳ�Ƭ��ͬ
		for (AlterBodyVO bodyVO : (AlterBodyVO[]) billVO.getChildrenVO()) {
			String pk_card = bodyVO.getPk_card();
			// ��ǰ����ı䶯��ƬVO
			AssetVO cardVO = cardsMap.get(pk_card);
			for (AlterSheetVO sheetVO : bodyVO.getAltersheetvos()) {
				// �䶯��
				String itemKey = sheetVO.getItem_code();
				// ��ǰ�䶯ǰֵ
				Object alter_before = sheetVO.getOld_content();

				// �䶯ǰ �� ��Ƭ����Ϊ��
				if (cardVO.getAttributeValue(itemKey) != null
						&& alter_before != null) {

					// ƥ��ֵ����
					try {
						UFDouble cardValue = new UFDouble(
								sheetVO.getOld_content());
						UFDouble oldValue = new UFDouble(
								alter_before.toString());

						// �����У��
						if (cardValue.compareTo(oldValue) != 0) {
							/* @res "��Ƭ����Ϊ[{0}]��Ӧ�ı䶯��Ƭ��[{1}]��ֵ�뿨Ƭ����ͬ, ���ܵ��롣 \n" */
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
							// �����У��
							if (oldInt != cardInt) {
								/*
								 * @res
								 * "��Ƭ����Ϊ[{0}]��Ӧ�ı䶯��Ƭ��[{1}]��ֵ�뿨Ƭ����ͬ, ���ܵ��롣 \n"
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
							// �����У��
							if (!cardVO.getAttributeValue(itemKey).toString()
									.equals(alter_before.toString())) {
								/*
								 * @res
								 * "��Ƭ����Ϊ[{0}]��Ӧ�ı䶯��Ƭ��[{1}]��ֵ�뿨Ƭ����ͬ, ���ܵ��롣 \n"
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

					// ֻ��һ��Ϊ��, Ҳ������
				} else if (!(cardVO.getAttributeValue(itemKey) == null && alter_before == null)) {
					/* @res "��Ƭ����Ϊ[{0}]��Ӧ�ı䶯��Ƭ��[{1}]��ֵ�뿨Ƭ����ͬ, ���ܵ��롣 \n" */
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
	 * ����Ƭ��ֵ��alterBodyVO�б䶯ǰ�и�ֵ�����������ĵ��������䶯��ֵ
	 * <p>
	 * <b>����˵��</b>
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

		// �Ƿ��ı䶯�ֶ�����
		List<String> notFitFields = new ArrayList<String>();

		// ��Ƭ�����ֶ�
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
					// NullGeneralAccessor ����˵�����ǵ�������
					if (generalAccessor instanceof NullGeneralAccessor) {
						// �䶯ǰ
						bodyVO.setAttributeValue(sheetVO.getItem_code()
								+ AlterKeyConst.BEFORE_SUFFIX,
								sheetVO.getOld_content());

						// �䶯��
						bodyVO.setAttributeValue(sheetVO.getItem_code()
								+ AlterKeyConst.AFTER_SUFFIX,
								sheetVO.getNew_content());

						// �䶯����
						UFDouble newValue = new UFDouble(
								sheetVO.getNew_content());
						UFDouble oldValue = new UFDouble(
								sheetVO.getOld_content());
						bodyVO.setAttributeValue(sheetVO.getItem_code()
								+ AlterKeyConst.ALTER_SUFFIX,
								UFDoubleUtils.sub(newValue, oldValue));
					} else {
						// 1. ͨ��������ȡ(�õıȽ϶�) ������߻����
						Map<String, String> codeAndPkMaps = FABDDataManager
								.getBDPKByCodes(showKey, new String[] {
										sheetCodes.get(showKey)
												.getOld_content(),
										sheetCodes.get(showKey)
												.getNew_content() },
										((AlterHeadVO) billVO.getParentVO())
												.getPk_org());

						if (codeAndPkMaps != null && codeAndPkMaps.size() != 0) {
							// �䶯ǰPK
							String before = codeAndPkMaps.get(sheetVO
									.getOld_content());
							// �䶯��PK
							String after = codeAndPkMaps.get(sheetVO
									.getNew_content());

							// ʹ�ò��ŵ������� Ҫת����link_key�洢
							if (showKey.equals(AssetFieldConst.PK_USEDEPT)) {
								
								sheetVO.setItem_code(AlterKeyConst.USEDEPT);
								// �䶯ǰ
								// �䶯ǰ
								DeptScaleVO scaleVO = new DeptScaleVO();
								scaleVO.setLink_key(before);
								scaleVO.setUsescale(new UFDouble(100));
								scaleVO.setTotalscale(new UFDouble(100));
								bodyVO.setOld_deptscalevos(before == null ?null :new DeptScaleVO[]{scaleVO});
								// �䶯��
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
							// 2. ͨ��������ȡ
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
								// �䶯ǰPK
								String before = nameAndPkMaps.get(sheetVO
										.getOld_content());
								// �䶯��PK
								String after = nameAndPkMaps.get(sheetVO
										.getNew_content());

								// ʹ�ò��ŵ������� Ҫת����link_key�洢
								if (showKey.equals(AssetFieldConst.PK_USEDEPT)) {
									
									sheetVO.setItem_code(AlterKeyConst.USEDEPT);
									// �䶯ǰ
									// �䶯ǰ
									DeptScaleVO scaleVO = new DeptScaleVO();
									scaleVO.setLink_key(before);
									scaleVO.setUsescale(new UFDouble(100));
									scaleVO.setTotalscale(new UFDouble(100));
									bodyVO.setOld_deptscalevos(before == null ?null :new DeptScaleVO[]{scaleVO});
									// �䶯��
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

								// 3. ֱ�Ӿ��ǰ���PK������, ����Ҫ��ѯ�� ֱ�ӱ���
								// ʹ�ò��ŵ������� Ҫת����link_key�洢
								if (showKey.equals(AssetFieldConst.PK_USEDEPT)) {
									
									sheetVO.setItem_code(AlterKeyConst.USEDEPT);
									// �䶯ǰ
									// �䶯ǰ
									DeptScaleVO scaleVO = new DeptScaleVO();
									scaleVO.setLink_key(before);
									scaleVO.setUsescale(new UFDouble(100));
									scaleVO.setTotalscale(new UFDouble(100));
									bodyVO.setOld_deptscalevos(before == null ?null :new DeptScaleVO[]{scaleVO});
									// �䶯��
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

			// ���¼��㱾��ԭֵ������ֵ������ֵ��
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
//		// ���õ��ݺŽӿڵ��ݺ�
//		// ��ȡ���ݺŹ������
//		IBillcodeManage billCodeManager = AMProxy.lookup(IBillcodeManage.class);
//
//		// ȡ���µĵ��ݺ�
//		String billCode = billCodeManager.getPreBillCode_RequiresNew(
//				BillTypeConst.ALTER, pk_group, pk_org);
//		((AlterHeadVO) billVO.getParentVO()).setBill_code(billCode);

		if (notFitFields.size() != 0) {
			ExceptionUtils
					.asBusinessException(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("farule_0",
									"02012060-0049")/*
													 * @res
													 * "�����ļ�xml��altersheetvos��ǩ��item_codeֵ, �ڿ�Ƭ�в����ڣ�������ȷ��. \n "
													 */);
		}
	}

	/**
	 * ����ʹ�ò��ŷ���link_key <b>����˵��</b>
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
