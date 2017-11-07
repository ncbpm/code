package nc.ui.qc.c003.maintain.handler.body;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.scmf.qc.qualitylevel.qc.IQueryQualityLevelForQc;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.ui.pubapp.util.CardPanelValueUtils;
import nc.ui.scmpub.ref.FilterQualityRankRefUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.c003.entity.ReportHeaderVO;
import nc.vo.qc.c003.entity.ReportItemVO;
import nc.vo.qc.c003.enumeration.ProcessJudegEnum;
import nc.vo.scmf.qc.qualitylevel.entity.QualityLevelItemVO;
import nc.vo.scmpub.res.billtype.MMBillType;
import nc.vo.uif2.LoginContext;

import org.apache.commons.lang.StringUtils;

/**
 * �����ȼ�
 * 
 * @since 6.0
 * @version 2011-3-21 ����12:08:22
 * @author hanbin
 */
public class Pk_qualitylv_b {

	private LoginContext context;

	public Pk_qualitylv_b(LoginContext context) {
		this.context = context;
	}

	// ��ѯ�����ȼ�
	Map<String, QualityLevelItemVO> rank_item_map = null;

	public void afterEdit(CardBodyAfterEditEvent e,String value) {
		String rank = value;
		if (StringUtils.isEmpty(rank)) {
			return;
		}
		if (rank_item_map == null || rank_item_map.get(rank) == null) {
			try {
				String[] ranks = new String[1];
				ranks[0] = rank;
				IQueryQualityLevelForQc sr = NCLocator.getInstance().lookup(
						IQueryQualityLevelForQc.class);
				rank_item_map = sr.queryQualityLevel(ranks);
			} catch (BusinessException ex) {
				ExceptionUtils.wrappException(ex);
			}
		
		}
		
		if (rank_item_map == null || rank_item_map.get(rank) == null) {
			return ;
		}
		QualityLevelItemVO item = rank_item_map.get(rank);
		// ��������κš�����������ȼ������κ�׺�����޸������ȼ���������κţ��������κ�+�����ȼ����κ�׺��
		this.setInBatchCode(e, item);
		// ����Ĭ�ϴ���ʽ
		this.setProcessjudge(e, item);
		// ���������ȼ��ġ��ϸ�Ʒ���������鴦��ʽ���Զ����롣
		this.setDefalultValue(e, item);
	}
	
	public void afterEdit(CardBodyAfterEditEvent e) {
		String rank = (String) e.getValue();
		if (StringUtils.isEmpty(rank)) {
			return;
		}
		if (rank_item_map == null || rank_item_map.get(rank) == null) {
			try {
				String[] ranks = new String[1];
				ranks[0] = rank;
				IQueryQualityLevelForQc sr = NCLocator.getInstance().lookup(
						IQueryQualityLevelForQc.class);
				rank_item_map = sr.queryQualityLevel(ranks);
			} catch (BusinessException ex) {
				ExceptionUtils.wrappException(ex);
			}
		
		}
		
		if (rank_item_map == null || rank_item_map.get(rank) == null) {
			return ;
		}
		QualityLevelItemVO item = rank_item_map.get(rank);
		// ��������κš�����������ȼ������κ�׺�����޸������ȼ���������κţ��������κ�+�����ȼ����κ�׺��
		this.setInBatchCode(e, item);
		// ����Ĭ�ϴ���ʽ
		this.setProcessjudge(e, item);
		// ���������ȼ��ġ��ϸ�Ʒ���������鴦��ʽ���Զ����롣
		this.setDefalultValue(e, item);
	}

	public void beforeEdit(CardHeadTailBeforeEditEvent e) {
		BillCardPanel card = e.getBillCardPanel();
		CardPanelValueUtils util = new CardPanelValueUtils(card);
		// ���ձ�ͷ�����׼�������ȼ����ڵ������ȼ�
		String std = util.getHeadTailStringValue(ReportHeaderVO.PK_CHKSTD);
		BillItem qualityLv = util.getHeadTailItem(ReportHeaderVO.VDEF2);
		FilterQualityRankRefUtils filter = new FilterQualityRankRefUtils(
				qualityLv);
		filter.filterItemRefByStd(std);
		e.setReturnValue(Boolean.TRUE);
	}

	public void beforeEdit(CardBodyBeforeEditEvent e) {
		BillCardPanel card = e.getBillCardPanel();
		CardPanelValueUtils util = new CardPanelValueUtils(card);

		// ���ձ�ͷ�����׼�������ȼ����ڵ������ȼ�
		String std = util.getHeadTailStringValue(ReportHeaderVO.PK_CHKSTD);
		BillItem qualityLv = util.getBodyItem(ReportItemVO.PK_QUALITYLV_B);
		FilterQualityRankRefUtils filter = new FilterQualityRankRefUtils(
				qualityLv);
		filter.filterItemRefByStd(std);

		e.setReturnValue(Boolean.TRUE);
	}

	private void setDefalultValue(CardBodyAfterEditEvent e,
			QualityLevelItemVO item) {
		// ���塰�ϸ�Ʒ�������������ȼ��ġ��ϸ�Ʒ���Զ����롣
		BillCardPanel card = e.getBillCardPanel();
		CardPanelValueUtils util = new CardPanelValueUtils(card);
		// �ȼ��ĺϸ�Ʒ
		UFBoolean belig = item.getBqualified();
		util.setBodyValue(belig, e.getRow(), ReportItemVO.BELIGIBLE);

		// ���鴦��ʽ
		String cdefectprocess = item.getCdefectprocess();
		util.setBodyValue(cdefectprocess, e.getRow(),
				ReportItemVO.PK_SUGGPROCESS);
		card.getBillModel().loadLoadRelationItemValue(e.getRow(),
				ReportItemVO.PK_SUGGPROCESS);

		// �������״̬
		String stockstate = item.getPk_stockstate();
		util.setBodyValue(stockstate, e.getRow(),
				ReportItemVO.PK_AFTERSTOCKSTATE);
		card.getBillModel().loadLoadRelationItemValue(e.getRow(),
				ReportItemVO.PK_AFTERSTOCKSTATE);
	}

	private void setInBatchCode(CardBodyAfterEditEvent e,
			QualityLevelItemVO item) {
		BillCardPanel card = e.getBillCardPanel();
		CardPanelValueUtils util = new CardPanelValueUtils(card);
		String cfirsttypecode = util.getBodyStringValue(e.getRow(),
				ReportItemVO.CFIRSTTYPECODE);
		// if (ICBillType.FreezeThaw.getCode().equals(cfirsttypecode)) {
		// // ��Դ�ڿ�����ʱ���������ڽ����в���
		// return;
		// }
		// ��������κš�����������ȼ������κ�׺�����޸������ȼ���������κţ��������κ�+�����ȼ����κ�׺��
		String suffix = item.getCbatchsuffix();
		String headbc = util
				.getHeadTailStringValue(ReportHeaderVO.VBATCHCODE_H);
		String bodybc = util.getBodyStringValue(e.getRow(),
				ReportItemVO.VBATCHCODE);

		Vbatchcode bchander = new Vbatchcode(this.context);
		if (StringUtils.isNotEmpty(bodybc)) {
			// ������κŲ�Ϊ��ʱ��ֱ���滻�����ȼ���׺��
			String[] strs = bodybc.split("_");
			String newBatch = strs[0].toString();
			if (StringUtils.isNotEmpty(suffix)) {
				newBatch = newBatch + "_" + suffix;
			}
			// ��Ҫ�����κŵ�������գ�������ÿ��Ĵ�������ʱ������
			// util.setBodyValue(null, e.getRow(), ReportItemVO.PK_BATCHCODE);
			bchander.clearBatchInfo(util, e.getRow());
			util.setBodyValue(newBatch, e.getRow(), ReportItemVO.VBATCHCODE);
			bchander.handleBatchByHand(card, newBatch, e.getRow());
			return;
		}
		if (StringUtils.isNotEmpty(headbc)) {
			// ������κ� = �������κ�+�����ȼ����κ�׺
			if (StringUtils.isNotEmpty(suffix)) {
				headbc = headbc + "_" + suffix;
			}
			// ��Ҫ�����κŵ�������գ�������ÿ��Ĵ�������ʱ������
			// util.setBodyValue(null, e.getRow(), ReportItemVO.PK_BATCHCODE);
			bchander.clearBatchInfo(util, e.getRow());
			util.setBodyValue(headbc, e.getRow(), ReportItemVO.VBATCHCODE);
			bchander.handleBatchByHand(card, headbc, e.getRow());
		}
	}

	private void setProcessjudge(CardBodyAfterEditEvent e,
			QualityLevelItemVO item) {
		BillCardPanel card = e.getBillCardPanel();
		CardPanelValueUtils util = new CardPanelValueUtils(card);
		// �ȼ��ĺϸ�Ʒ
		boolean belig = item.getBqualified().booleanValue();
		// �����ȼ����ϸ񣬴���ʽ=�����
		String firsttypecode = util.getBodyStringValue(e.getRow(),
				ReportItemVO.CFIRSTTYPECODE);
		if (MMBillType.ProduceReportForQC.getCode().equals(firsttypecode)) {
			// Դͷ��������������ʱ���޸ı��������ȼ�����������ȼ��ĺϸ�Ʒ���ǣ�����ʽ�ж�Ϊ�ϸ���⣻��������ȼ��ĺϸ�Ʒ���񣬴���ʽ���Զ��ж�
			Integer eligVlaue = (Integer) ProcessJudegEnum.ELIGINSTORE.value();
			if (belig) {
				util.setBodyValue(eligVlaue, e.getRow(),
						ReportItemVO.FPROCESSJUDGE);
			} else {
				util.setBodyValue(ProcessJudegEnum.BACKWORCK.toInteger(),
						e.getRow(), ReportItemVO.FPROCESSJUDGE);

			}
		}
		// V631������Դͷ�����ǹ����깤����ʱ���޸ı��������ȼ�����������ȼ��ĺϸ�Ʒ���ǣ�����ʽ�ж�Ϊ�ϸ���⣻��������ȼ��ĺϸ�Ʒ���񣬴���ʽ���Զ��ж���
		else if ("55D2".equals(firsttypecode)) {
			Integer eligVlaue = (Integer) ProcessJudegEnum.ELIGINSTORE.value();
			if (belig) {
				util.setBodyValue(eligVlaue, e.getRow(),
						ReportItemVO.FPROCESSJUDGE);
			} else {
				util.setBodyValue(ProcessJudegEnum.BACKWORCK.toInteger(),
						e.getRow(), ReportItemVO.FPROCESSJUDGE);

			}
		}
		// V633������Դͷ�����ǰ��Ʒ�ջ���������ί���ջ�����ʱ���޸ı��������ȼ�����������ȼ��ĺϸ�Ʒ���ǣ�����ʽ�ж�Ϊ�ϸ���������ȼ��ĺϸ�Ʒ���񣬴���ʽ���Զ��ж���
		else if ("55E5".equals(firsttypecode)) {
			Integer eligVlaue = (Integer) ProcessJudegEnum.QUALIFED.value();
			if (belig) {
				util.setBodyValue(eligVlaue, e.getRow(),
						ReportItemVO.FPROCESSJUDGE);
			} else {
				util.setBodyValue(ProcessJudegEnum.BACKWORCK.toInteger(),
						e.getRow(), ReportItemVO.FPROCESSJUDGE);
			}
		} else {
			// Դͷ���ݲ�����������ʱ���޸ı��������ȼ�����������ȼ��ĺϸ�Ʒ���ǣ�����ʽ�ж�Ϊ��⣻��������ȼ��ĺϸ�Ʒ���񣬴���ʽ�ж�Ϊ�����
			Integer eligVlaue = (Integer) ProcessJudegEnum.INSTORE.value();
			if (belig) {
				util.setBodyValue(eligVlaue, e.getRow(),
						ReportItemVO.FPROCESSJUDGE);
			} else {
				Integer notEligValue = (Integer) ProcessJudegEnum.NOTINSTORE
						.value();
				util.setBodyValue(notEligValue, e.getRow(),
						ReportItemVO.FPROCESSJUDGE);
			}
		}
	}
}
