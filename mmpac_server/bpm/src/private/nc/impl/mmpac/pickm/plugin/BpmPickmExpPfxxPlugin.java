package nc.impl.mmpac.pickm.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.itf.mmpac.pickm.IPickmMaintainService;
import nc.itf.mmpac.pickm.IPickmQueryService;
import nc.ui.mmpac.pickm.serviceproxy.PickmMaterialInfoProxy;
import nc.ui.mmpac.pickm.serviceproxy.PickmQueryMOProxy;
import nc.ui.mmpac.pickm.serviceproxy.PickmTransTypeProxy;
import nc.util.mmf.busi.service.MaterialPubService;
import nc.util.mmf.framework.base.MMMapUtil;
import nc.util.mmf.framework.base.MMNumberUtil;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.bd.material.IMaterialEnumConst;
import nc.vo.mmpac.mo.param.MoQueryParam4Pickm;
import nc.vo.mmpac.pickm.calculator.PickVODataSetForCal;
import nc.vo.mmpac.pickm.entity.AggPickmVO;
import nc.vo.mmpac.pickm.entity.PickmHeadVO;
import nc.vo.mmpac.pickm.entity.PickmItemVO;
import nc.vo.mmpac.pickm.enumeration.FsourcetypeEnum;
import nc.vo.mmpac.pickm.enumeration.FsupplytypeEnum;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.Calculator;
import nc.vo.pubapp.calculator.Condition;
import nc.vo.pubapp.calculator.data.IDataSetForCal;
import nc.vo.pubapp.calculator.data.RelationItemForCal;
import nc.vo.pubapp.scale.ScaleUtils;
import nc.vo.pubapp.util.VORowNoUtils;

public class BpmPickmExpPfxxPlugin<T extends AggPickmVO> extends
		nc.bs.pfxx.plugin.AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		AggPickmVO bill = (AggPickmVO) vo;

		if (bill == null || bill.getParentVO() == null
				|| bill.getChildrenVO() == null
				|| bill.getChildrenVO().length == 0)
			throw new BusinessException("传入数据出错");

		PickmHeadVO headvo = (PickmHeadVO) bill.getParentVO();

		if (headvo.getPrimaryKey() == null) {
			throw new BusinessException("单据的主键字段不能为空，请输入值");
		}

		IPickmQueryService queryService = NCLocator.getInstance().lookup(
				IPickmQueryService.class);

		AggPickmVO[] oldbills = queryService
				.queryBillsByPks(new String[] { headvo.getPrimaryKey() });

		if (oldbills == null || oldbills.length == 0) {
			throw new BusinessException("该备料计划不存在，请检查主键是否正确");
		}

		AggPickmVO oldbill = oldbills[0];
		if (oldbill == null || oldbill.getParentVO() == null
				|| oldbill.getChildrenVO() == null
				|| oldbill.getChildrenVO().length == 0)
			throw new BusinessException("该备料计划不存在，请检查主键是否正确");

		PickmHeadVO oldheadvo = (PickmHeadVO) oldbill.getParentVO();
		// 非审批通过不可变更
		if (oldheadvo.getFbillstatus() != null
				&& oldheadvo.getFbillstatus().intValue() == 2)
			throw new BusinessException("该备料计划已经完成，不能变更");

		oldheadvo.setStatus(VOStatus.UPDATED);
		oldheadvo.setIsUnCheckAtp(UFBoolean.TRUE);
		// 构造变更单表体
		PickmItemVO[] items = createBvos((PickmItemVO[]) bill.getChildrenVO(),
				(PickmItemVO[]) oldbill.getChildrenVO(), oldheadvo);
		oldbill.setParentVO(oldheadvo);
		oldbill.setChildrenVO(items);
		IPickmMaintainService manageService = NCLocator.getInstance().lookup(
				IPickmMaintainService.class);
		AggPickmVO[] returnvo = manageService
				.updateForYL(new AggPickmVO[] { oldbill });

		if (returnvo == null || returnvo.length == 0)
			return null;
		return returnvo[0].getPrimaryKey();
	}

	private PickmItemVO[] createBvos(PickmItemVO[] items,
			PickmItemVO[] olditems, PickmHeadVO oldheadvo)
			throws BusinessException {

		ArrayList<PickmItemVO> al = new ArrayList<>();
		Map<String, PickmItemVO> map = new HashMap<>();

		String maxRowNo = null;
		for (PickmItemVO bvo : olditems) {
			map.put(bvo.getCpickm_bid(), bvo);
			if (maxRowNo == null) {
				maxRowNo = bvo.getVrowno();
			} else {
				UFDouble dCurRowNO = VORowNoUtils.getUFDouble(bvo.getVrowno());
				UFDouble dMaxRowNO = VORowNoUtils.getUFDouble(maxRowNo);
				if (dMaxRowNO.compareTo(dCurRowNO) < 0) {
					maxRowNo = bvo.getVrowno();
				}
			}
		}

		// 数量字段调整 换算率调整
		for (PickmItemVO bvo : items) {

			PickmItemVO oldvo1 = map.get(bvo.getCpickm_bid());
			if (oldvo1 == null) {
				bvo.setStatus(VOStatus.NEW);
				bvo.setCpickmid(oldheadvo.getCpickmid());

				calculate(PickmItemVO.NPLANOUTASTNUM,
						PickmItemVO.CBMATERIALVID, true, oldheadvo, bvo);
				calculateOtherNum(oldheadvo, bvo);
				maxRowNo = VORowNoUtils.getRowNoAfterByRule(maxRowNo, 10);
				bvo.setVrowno(maxRowNo);
				al.add(bvo);
			} else {
				if (bvo.getNaccoutnum() == null
						|| bvo.getNaccoutnum().compareTo(UFDouble.ZERO_DBL) == 0) {
					if (bvo.getNplanoutnum() != null
							&& bvo.getNplanoutnum()
									.compareTo(UFDouble.ZERO_DBL) < 0) {
						oldvo1.setStatus(VOStatus.DELETED);
					} else {
						oldvo1.setStatus(VOStatus.UPDATED);
						oldvo1.setNplanoutnum(bvo.getNplanoutnum());
						oldvo1.setNplanoutastnum(bvo.getNplanoutastnum());
					}
				} else {
					throw new BusinessException("行号:" + bvo.getVrowno()
							+ "已经执行出库的行不允许删除和减少执行数量修改");
				}
				calculate(PickmItemVO.NPLANOUTASTNUM,
						PickmItemVO.CBMATERIALVID, true, oldheadvo, oldvo1);
				calculateOtherNum(oldheadvo, oldvo1);
				al.add(oldvo1);
			}

		}

		if (al != null || al.size() > 0) {
			return al.toArray(new PickmItemVO[al.size()]);
		}

		return null;
	}

	/**
	 * 表体主辅计量。自动根据单位判断是否为浮动换算率做出相应处理。<br>
	 * 如果是浮动换算率，则辅数量不变，换算率变。<br>
	 * 如果是固定换算率，则辅数量变，换算率不变。<br>
	 * 本方法的最后一个参数supportunFixedRate的作用为：如果传入false，表示当前单据不支持浮动换算率。则无论当前单位是否为浮动换算率
	 * ，都按照辅数量变换算率不变的方式处理
	 * 
	 * @param panel
	 *            卡片界面
	 * @param row
	 *            行号
	 * @param itemKey
	 *            值变化的字段
	 * @param materialKey
	 *            物料字段名
	 * @param supportUnFixedRate
	 *            是否支持浮动换算率
	 * @see 当主数量变化的时候，itemKey就传入主数量字段，该方法会自动计算辅数量或换算率
	 */
	private void calculate(String itemKey, String matrialvidKey,
			boolean isSupportUnFix, PickmHeadVO voHead, PickmItemVO voitem) {
		if (itemKey == null) {
			return;
		}
		RelationItemForCal item = new RelationItemForCal();
		item.setCunitidKey(PickmItemVO.CBUNITID);
		item.setCastunitidKey(PickmItemVO.CBASTUNITID);
		item.setNchangerateKey(PickmItemVO.VBCHANGERATE);
		item.setnumKey(PickmItemVO.NPLANOUTNUM);
		item.setNassistnumKey(PickmItemVO.NPLANOUTASTNUM);
		ScaleUtils scale = new nc.vo.pubapp.scale.ScaleUtils(
				voHead.getPk_group());
		// 创建数据集实例 初始化数据关系计算用的数据集
		IDataSetForCal data = new PickVODataSetForCal(voHead, voitem, item);
		boolean isFix = true;
		// 性能考虑
		// 批选新增物料时，会循环查是否为固定换算率,但是物料变相当于换算率变,换算率变时都是根据主数量算辅数量,所以为了减少连接数,当换算率变化时,不考虑是否为固定换算率,都按照固定换算率计算数据联动
		if (isSupportUnFix && !itemKey.equals(item.getNchangerateKey())) {
			String materialvid = (String) data.getAttributeValue(matrialvidKey);
			isFix = MaterialPubService.queryIsFixedRateByMaterialAndMeasdoc_C(
					materialvid, data.getCastunitid());
		}
		Calculator tool = new Calculator(data, scale);
		// 创建参数实例，在计算的时候用来获得参数条件：是否含税优先等
		Condition cond = new Condition();// 创建参数实例
		// 设置是否是固定换算率
		cond.setIsFixNchangerate(isFix);
		// 参数 cond 为计算时的参数条件
		tool.calculateOnlyNumAssNumQtNum(cond, itemKey);
	}

	private void calculateOtherNum(PickmHeadVO headVO, PickmItemVO pickItemVO) {

		UFDouble nplanoutastnum = MMValueCheck.isEmpty(pickItemVO
				.getNplanoutastnum()) ? UFDouble.ZERO_DBL : pickItemVO
				.getNplanoutastnum();

		// 是否考虑损耗
		boolean isBcontainwast = PickmTransTypeProxy
				.isBcontainwastePickmTransType(headVO.getVbusitypeid());

		// 对于替代主料的修改，需要进行值联动处理
		// if
		// (FreplaceinfoEnum.REPLACED.equalsValue(pickItemVO.getFreplaceinfo()))
		// {
		// PickmReplaceRelationUtil.setValueAfterReplaceChanged(e.getBillCardPanel(),
		// Integer.valueOf(e.getRow()),
		// PickmReplaceRelationUtil.CHANGETYPE_EDIT);
		// return;
		// }
		//
		// // 对于替代子料的修改，需要进行值联动
		// if (MMStringUtil.isNotEmpty(pickItemVO.getCreplacesrcid())) {
		// Integer mainIndex =
		// PickmReplaceRelationUtil.findReplaceMainItemForItem(e.getBillCardPanel(),
		// pickItemVO.getCreplacesrcid());
		// PickmReplaceRelationUtil.setValueAfterReplaceChanged(e.getBillCardPanel(),
		// mainIndex,
		// PickmReplaceRelationUtil.CHANGETYPE_EDIT);
		// return;
		// }
		// 非替代主料或替代料，只要本行内值联动即可
		UFDouble nunitusernum = UFDouble.ZERO_DBL;
		UFDouble nunituserastnum = UFDouble.ZERO_DBL;
		UFDouble nplanoutnum = (UFDouble) pickItemVO
				.getAttributeValue(PickmItemVO.NPLANOUTNUM);

		// 如果考虑损耗
		if (isBcontainwast) {
			if (!MMNumberUtil.isNullOrZero(pickItemVO.getNquotastnum())) {
				// 根据计划出库数量反算损耗系数: (计划出库数量-固定耗损数量)/定额用量-1
				UFDouble num = MMNumberUtil.sub(MMNumberUtil.div(MMNumberUtil
						.sub(pickItemVO.getNplanoutastnum(),
								pickItemVO.getNfixshrinkastnum()), pickItemVO
						.getNquotastnum()), UFDouble.ONE_DBL);
				if (MMNumberUtil.isLsZero(num)) {
					num = UFDouble.ZERO_DBL;
				}
				pickItemVO.setAttributeValue(PickmItemVO.NDISSIPATIONUM, num);
			}
		}
		// 定量发料
		if (FsupplytypeEnum.RATIONDELIVERY.equalsValue(pickItemVO
				.getFsupplytype())) {
			// 单位用量=计划出库数量，单位主用量=计划出库主数量
			nunitusernum = nplanoutnum;
			nunituserastnum = nplanoutastnum;
		}
		// 一般发料
		else {
			PickmHeadVO newHead = (PickmHeadVO) headVO.clone();
			// 如果工序管理，且备料计划行有需用工序，调整数量
			this.adjustNumByProcess(newHead, pickItemVO);

			if (!MMNumberUtil.isNullOrZero(newHead.getNnumber())) {
				// 主单位用量=计划出库主数量/表头主数量
				nunitusernum = MMNumberUtil.div(nplanoutnum,
						newHead.getNnumber());
			}
			if (!MMNumberUtil.isNullOrZero(newHead.getNastnum())) {
				// 单位用量=计划出库数量/表头数量
				nunituserastnum = MMNumberUtil.div(nplanoutastnum,
						newHead.getNastnum());
			}
		}
		pickItemVO.setAttributeValue(PickmItemVO.NUNITUSEASTNUM,
				nunituserastnum);
		pickItemVO.setAttributeValue(PickmItemVO.NUNITUSENUM, nunitusernum);

	}

	/**
	 * 如果工序管理，且备料计划行有需用工序，则按对应工序计划的数量调整计算单位用量需要除的数量
	 * 
	 * @param headVO
	 * @param pickItemVO
	 * @param hnum
	 * @param hastnum
	 */
	public void adjustNumByProcess(PickmHeadVO headVO, PickmItemVO pickItemVO) {
		// 如果来源生产订单
		if (FsourcetypeEnum.MOBILL_STATE.equalsValue(headVO.getFsourcetype())) {
			// 是否工序管理
			UFBoolean isWorkProceManage = new PickmMaterialInfoProxy()
					.isWorkprocemanage(headVO.getCmaterialvid(),
							headVO.getPk_org());
			// 如果工序管理
			if (isWorkProceManage.booleanValue()) {
				// 需用工序字段名：离散or流程
				String processField = this
						.getVProcessFieldNameByProdmode(headVO.getFprodmode());
				if (MMValueCheck.isNotEmpty(pickItemVO
						.getAttributeValue(processField))) {
					String processNo = (String) pickItemVO
							.getAttributeValue(processField);
					MoQueryParam4Pickm param = new MoQueryParam4Pickm();
					param.setProdMode(headVO.getFprodmode());
					param.setIds(new String[] { processNo });
					Map<String, UFDouble[]> processNum = PickmQueryMOProxy
							.getProcessNumsByProcessID(param);
					// 如果来源工序计划，调整数量
					if (!MMMapUtil.isEmpty(processNum)
							&& processNum.containsKey(processNo)) {
						headVO.setNnumber(processNum.get(processNo)[0]);
						headVO.setNastnum(processNum.get(processNo)[1]);
					}
				}
			}
		}
	}

	/**
	 * 根据生产模式获得需用工序字段名
	 * 
	 * @param vo
	 */
	protected String getVProcessFieldNameByProdmode(Integer prodmode) {
		String processField = null;
		if (IMaterialEnumConst.PRODMODE_SEPPROD == prodmode) {
			// 离散需用工序
			processField = PickmItemVO.VDMOPROCESSNO;
		}
		// 流程生产模式
		else if (IMaterialEnumConst.PRODMODE_PROD == prodmode) {
			// 流程需用工序
			processField = PickmItemVO.VPMOPROCESSNO;
		}
		return processField;
	}

}
