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
			throw new BusinessException("�������ݳ���");

		PickmHeadVO headvo = (PickmHeadVO) bill.getParentVO();

		if (headvo.getPrimaryKey() == null) {
			throw new BusinessException("���ݵ������ֶβ���Ϊ�գ�������ֵ");
		}

		IPickmQueryService queryService = NCLocator.getInstance().lookup(
				IPickmQueryService.class);

		AggPickmVO[] oldbills = queryService
				.queryBillsByPks(new String[] { headvo.getPrimaryKey() });

		if (oldbills == null || oldbills.length == 0) {
			throw new BusinessException("�ñ��ϼƻ������ڣ����������Ƿ���ȷ");
		}

		AggPickmVO oldbill = oldbills[0];
		if (oldbill == null || oldbill.getParentVO() == null
				|| oldbill.getChildrenVO() == null
				|| oldbill.getChildrenVO().length == 0)
			throw new BusinessException("�ñ��ϼƻ������ڣ����������Ƿ���ȷ");

		PickmHeadVO oldheadvo = (PickmHeadVO) oldbill.getParentVO();
		// ������ͨ�����ɱ��
		if (oldheadvo.getFbillstatus() != null
				&& oldheadvo.getFbillstatus().intValue() == 2)
			throw new BusinessException("�ñ��ϼƻ��Ѿ���ɣ����ܱ��");

		oldheadvo.setStatus(VOStatus.UPDATED);
		oldheadvo.setIsUnCheckAtp(UFBoolean.TRUE);
		// ������������
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

		// �����ֶε��� �����ʵ���
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
					throw new BusinessException("�к�:" + bvo.getVrowno()
							+ "�Ѿ�ִ�г�����в�����ɾ���ͼ���ִ�������޸�");
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
	 * ���������������Զ����ݵ�λ�ж��Ƿ�Ϊ����������������Ӧ����<br>
	 * ����Ǹ��������ʣ����������䣬�����ʱ䡣<br>
	 * ����ǹ̶������ʣ��������䣬�����ʲ��䡣<br>
	 * �����������һ������supportunFixedRate������Ϊ���������false����ʾ��ǰ���ݲ�֧�ָ��������ʡ������۵�ǰ��λ�Ƿ�Ϊ����������
	 * �������ո������任���ʲ���ķ�ʽ����
	 * 
	 * @param panel
	 *            ��Ƭ����
	 * @param row
	 *            �к�
	 * @param itemKey
	 *            ֵ�仯���ֶ�
	 * @param materialKey
	 *            �����ֶ���
	 * @param supportUnFixedRate
	 *            �Ƿ�֧�ָ���������
	 * @see ���������仯��ʱ��itemKey�ʹ����������ֶΣ��÷������Զ����㸨����������
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
		// �������ݼ�ʵ�� ��ʼ�����ݹ�ϵ�����õ����ݼ�
		IDataSetForCal data = new PickVODataSetForCal(voHead, voitem, item);
		boolean isFix = true;
		// ���ܿ���
		// ��ѡ��������ʱ����ѭ�����Ƿ�Ϊ�̶�������,�������ϱ��൱�ڻ����ʱ�,�����ʱ�ʱ���Ǹ����������㸨����,����Ϊ�˼���������,�������ʱ仯ʱ,�������Ƿ�Ϊ�̶�������,�����չ̶������ʼ�����������
		if (isSupportUnFix && !itemKey.equals(item.getNchangerateKey())) {
			String materialvid = (String) data.getAttributeValue(matrialvidKey);
			isFix = MaterialPubService.queryIsFixedRateByMaterialAndMeasdoc_C(
					materialvid, data.getCastunitid());
		}
		Calculator tool = new Calculator(data, scale);
		// ��������ʵ�����ڼ����ʱ��������ò����������Ƿ�˰���ȵ�
		Condition cond = new Condition();// ��������ʵ��
		// �����Ƿ��ǹ̶�������
		cond.setIsFixNchangerate(isFix);
		// ���� cond Ϊ����ʱ�Ĳ�������
		tool.calculateOnlyNumAssNumQtNum(cond, itemKey);
	}

	private void calculateOtherNum(PickmHeadVO headVO, PickmItemVO pickItemVO) {

		UFDouble nplanoutastnum = MMValueCheck.isEmpty(pickItemVO
				.getNplanoutastnum()) ? UFDouble.ZERO_DBL : pickItemVO
				.getNplanoutastnum();

		// �Ƿ������
		boolean isBcontainwast = PickmTransTypeProxy
				.isBcontainwastePickmTransType(headVO.getVbusitypeid());

		// ����������ϵ��޸ģ���Ҫ����ֵ��������
		// if
		// (FreplaceinfoEnum.REPLACED.equalsValue(pickItemVO.getFreplaceinfo()))
		// {
		// PickmReplaceRelationUtil.setValueAfterReplaceChanged(e.getBillCardPanel(),
		// Integer.valueOf(e.getRow()),
		// PickmReplaceRelationUtil.CHANGETYPE_EDIT);
		// return;
		// }
		//
		// // ����������ϵ��޸ģ���Ҫ����ֵ����
		// if (MMStringUtil.isNotEmpty(pickItemVO.getCreplacesrcid())) {
		// Integer mainIndex =
		// PickmReplaceRelationUtil.findReplaceMainItemForItem(e.getBillCardPanel(),
		// pickItemVO.getCreplacesrcid());
		// PickmReplaceRelationUtil.setValueAfterReplaceChanged(e.getBillCardPanel(),
		// mainIndex,
		// PickmReplaceRelationUtil.CHANGETYPE_EDIT);
		// return;
		// }
		// ��������ϻ�����ϣ�ֻҪ������ֵ��������
		UFDouble nunitusernum = UFDouble.ZERO_DBL;
		UFDouble nunituserastnum = UFDouble.ZERO_DBL;
		UFDouble nplanoutnum = (UFDouble) pickItemVO
				.getAttributeValue(PickmItemVO.NPLANOUTNUM);

		// ����������
		if (isBcontainwast) {
			if (!MMNumberUtil.isNullOrZero(pickItemVO.getNquotastnum())) {
				// ���ݼƻ����������������ϵ��: (�ƻ���������-�̶���������)/��������-1
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
		// ��������
		if (FsupplytypeEnum.RATIONDELIVERY.equalsValue(pickItemVO
				.getFsupplytype())) {
			// ��λ����=�ƻ�������������λ������=�ƻ�����������
			nunitusernum = nplanoutnum;
			nunituserastnum = nplanoutastnum;
		}
		// һ�㷢��
		else {
			PickmHeadVO newHead = (PickmHeadVO) headVO.clone();
			// �����������ұ��ϼƻ��������ù��򣬵�������
			this.adjustNumByProcess(newHead, pickItemVO);

			if (!MMNumberUtil.isNullOrZero(newHead.getNnumber())) {
				// ����λ����=�ƻ�����������/��ͷ������
				nunitusernum = MMNumberUtil.div(nplanoutnum,
						newHead.getNnumber());
			}
			if (!MMNumberUtil.isNullOrZero(newHead.getNastnum())) {
				// ��λ����=�ƻ���������/��ͷ����
				nunituserastnum = MMNumberUtil.div(nplanoutastnum,
						newHead.getNastnum());
			}
		}
		pickItemVO.setAttributeValue(PickmItemVO.NUNITUSEASTNUM,
				nunituserastnum);
		pickItemVO.setAttributeValue(PickmItemVO.NUNITUSENUM, nunitusernum);

	}

	/**
	 * �����������ұ��ϼƻ��������ù����򰴶�Ӧ����ƻ��������������㵥λ������Ҫ��������
	 * 
	 * @param headVO
	 * @param pickItemVO
	 * @param hnum
	 * @param hastnum
	 */
	public void adjustNumByProcess(PickmHeadVO headVO, PickmItemVO pickItemVO) {
		// �����Դ��������
		if (FsourcetypeEnum.MOBILL_STATE.equalsValue(headVO.getFsourcetype())) {
			// �Ƿ������
			UFBoolean isWorkProceManage = new PickmMaterialInfoProxy()
					.isWorkprocemanage(headVO.getCmaterialvid(),
							headVO.getPk_org());
			// ����������
			if (isWorkProceManage.booleanValue()) {
				// ���ù����ֶ�������ɢor����
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
					// �����Դ����ƻ�����������
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
	 * ��������ģʽ������ù����ֶ���
	 * 
	 * @param vo
	 */
	protected String getVProcessFieldNameByProdmode(Integer prodmode) {
		String processField = null;
		if (IMaterialEnumConst.PRODMODE_SEPPROD == prodmode) {
			// ��ɢ���ù���
			processField = PickmItemVO.VDMOPROCESSNO;
		}
		// ��������ģʽ
		else if (IMaterialEnumConst.PRODMODE_PROD == prodmode) {
			// �������ù���
			processField = PickmItemVO.VPMOPROCESSNO;
		}
		return processField;
	}

}
