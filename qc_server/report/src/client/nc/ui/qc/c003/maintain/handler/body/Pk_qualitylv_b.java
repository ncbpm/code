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
 * 质量等级
 * 
 * @since 6.0
 * @version 2011-3-21 下午12:08:22
 * @author hanbin
 */
public class Pk_qualitylv_b {

	private LoginContext context;

	public Pk_qualitylv_b(LoginContext context) {
		this.context = context;
	}

	// 查询质量等级
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
		// “入库批次号”：如果质量等级有批次后缀，则修改质量等级后入库批次号＝物料批次号+质量等级批次后缀。
		this.setInBatchCode(e, item);
		// 设置默认处理方式
		this.setProcessjudge(e, item);
		// 根据质量等级的“合格品”、“建议处理方式”自动带入。
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
		// “入库批次号”：如果质量等级有批次后缀，则修改质量等级后入库批次号＝物料批次号+质量等级批次后缀。
		this.setInBatchCode(e, item);
		// 设置默认处理方式
		this.setProcessjudge(e, item);
		// 根据质量等级的“合格品”、“建议处理方式”自动带入。
		this.setDefalultValue(e, item);
	}

	public void beforeEdit(CardHeadTailBeforeEditEvent e) {
		BillCardPanel card = e.getBillCardPanel();
		CardPanelValueUtils util = new CardPanelValueUtils(card);
		// 参照表头检验标准的质量等级组内的质量等级
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

		// 参照表头检验标准的质量等级组内的质量等级
		String std = util.getHeadTailStringValue(ReportHeaderVO.PK_CHKSTD);
		BillItem qualityLv = util.getBodyItem(ReportItemVO.PK_QUALITYLV_B);
		FilterQualityRankRefUtils filter = new FilterQualityRankRefUtils(
				qualityLv);
		filter.filterItemRefByStd(std);

		e.setReturnValue(Boolean.TRUE);
	}

	private void setDefalultValue(CardBodyAfterEditEvent e,
			QualityLevelItemVO item) {
		// 表体“合格品”：根据质量等级的“合格品”自动带入。
		BillCardPanel card = e.getBillCardPanel();
		CardPanelValueUtils util = new CardPanelValueUtils(card);
		// 等级的合格品
		UFBoolean belig = item.getBqualified();
		util.setBodyValue(belig, e.getRow(), ReportItemVO.BELIGIBLE);

		// 建议处理方式
		String cdefectprocess = item.getCdefectprocess();
		util.setBodyValue(cdefectprocess, e.getRow(),
				ReportItemVO.PK_SUGGPROCESS);
		card.getBillModel().loadLoadRelationItemValue(e.getRow(),
				ReportItemVO.PK_SUGGPROCESS);

		// 检验后库存状态
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
		// // 来源于库存检验时，不允许在进行行操作
		// return;
		// }
		// “入库批次号”：如果质量等级有批次后缀，则修改质量等级后入库批次号＝物料批次号+质量等级批次后缀。
		String suffix = item.getCbatchsuffix();
		String headbc = util
				.getHeadTailStringValue(ReportHeaderVO.VBATCHCODE_H);
		String bodybc = util.getBodyStringValue(e.getRow(),
				ReportItemVO.VBATCHCODE);

		Vbatchcode bchander = new Vbatchcode(this.context);
		if (StringUtils.isNotEmpty(bodybc)) {
			// 入库批次号不为空时，直接替换质量等级后缀。
			String[] strs = bodybc.split("_");
			String newBatch = strs[0].toString();
			if (StringUtils.isNotEmpty(suffix)) {
				newBatch = newBatch + "_" + suffix;
			}
			// 需要把批次号的主键清空，否则调用库存的创建批次时出问题
			// util.setBodyValue(null, e.getRow(), ReportItemVO.PK_BATCHCODE);
			bchander.clearBatchInfo(util, e.getRow());
			util.setBodyValue(newBatch, e.getRow(), ReportItemVO.VBATCHCODE);
			bchander.handleBatchByHand(card, newBatch, e.getRow());
			return;
		}
		if (StringUtils.isNotEmpty(headbc)) {
			// 入库批次号 = 物料批次号+质量等级批次后缀
			if (StringUtils.isNotEmpty(suffix)) {
				headbc = headbc + "_" + suffix;
			}
			// 需要把批次号的主键清空，否则调用库存的创建批次时出问题
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
		// 等级的合格品
		boolean belig = item.getBqualified().booleanValue();
		// 质量等级不合格，处理方式=不入库
		String firsttypecode = util.getBodyStringValue(e.getRow(),
				ReportItemVO.CFIRSTTYPECODE);
		if (MMBillType.ProduceReportForQC.getCode().equals(firsttypecode)) {
			// 源头单据是生产报告时，修改表体质量等级后，如果质量等级的合格品＝是，则处理方式判定为合格入库；如果质量等级的合格品＝否，处理方式不自动判定
			Integer eligVlaue = (Integer) ProcessJudegEnum.ELIGINSTORE.value();
			if (belig) {
				util.setBodyValue(eligVlaue, e.getRow(),
						ReportItemVO.FPROCESSJUDGE);
			} else {
				util.setBodyValue(ProcessJudegEnum.BACKWORCK.toInteger(),
						e.getRow(), ReportItemVO.FPROCESSJUDGE);

			}
		}
		// V631新增：源头单据是工序完工报告时，修改表体质量等级后，如果质量等级的合格品＝是，则处理方式判定为合格入库；如果质量等级的合格品＝否，处理方式不自动判定。
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
		// V633新增：源头单据是半成品收货单（工序委外收货单）时，修改表体质量等级后，如果质量等级的合格品＝是，则处理方式判定为合格；如果质量等级的合格品＝否，处理方式不自动判定。
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
			// 源头单据不是生产报告时，修改表体质量等级后，如果质量等级的合格品＝是，则处理方式判定为入库；如果质量等级的合格品＝否，处理方式判定为不入库
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
