package nc.ui.qc.c003.maintain.handler.head;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.ui.pubapp.util.CardPanelValueUtils;
import nc.ui.qc.c003.maintain.handler.body.Pk_qualitylv_b;
import nc.ui.qc.c003.maintain.handler.body.Vbatchcode;
import nc.vo.qc.c003.entity.ReportHeaderVO;

/**
 * 单据表头表尾字段编辑后事件
 * 
 * @since 6.0
 * @version 2010-12-21 下午02:22:08
 * @author hanbin
 */
public class HeadTailAfterEditHandler implements
		IAppEventHandler<CardHeadTailAfterEditEvent> {

	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		String key = e.getKey();
		if (key.equals(ReportHeaderVO.VCHKBATCH)) {
			// 检验批次号
			new Vchkbatch().afterEdit(e);
		} else if (key.equals(ReportHeaderVO.NCHECKASTNUM)) {
			// 检验数量
			new Ncheckastnum().afterEdit(e);
		} else if (key.equals(ReportHeaderVO.NCHECKNUM)) {
			// 检验主数量
			new Nchecknum().afterEdit(e);
		}
		// 1、质检报告表头自定义项启用2、3、4、5分别为质量等级（参照质量登记）、检验批次号（字符串，长度100）、不合格类型（参照不合格类型）、不合格项目（参照检验项目）。
		// 2、实现表单内表头录入质量等级（对应检验方案的质量等级组）、检验批次、不合格类型（质量等级为不合格时）、不合格项目（质量等级为不合格时），能够自动带入到表体，并符合表体录入触发的其他字段的赋值（检后库存状态、建议处理方式、合格品、处理方式判定）。
		else if ("vdef2".equalsIgnoreCase(key)) {
			// 质量等级
			BillCardPanel card = e.getBillCardPanel();
			CardPanelValueUtils util = new CardPanelValueUtils(card);
			int rowCount = util.getRowCount();
			if (rowCount == 0) {
				return;
			}
			String bkey = "pk_qualitylv_b";
			Pk_qualitylv_b bhdander = new Pk_qualitylv_b(e.getContext());
			for (int i = 0; i < rowCount; i++) {
				Object oldValue = util.getBodyValue(i, bkey);
				
				util.setBodyValue(e.getValue(), i, bkey);		
				card.getBillModel("report_b").loadEditRelationItemValue(i, bkey);
				
				CardBodyAfterEditEvent be = new CardBodyAfterEditEvent(card,
						"report_b", i, bkey, e.getValue(), oldValue);
				// 质量等级
				bhdander.afterEdit(be,(String)e.getValue());
			}

		} else if ("vdef3".equalsIgnoreCase(key)) {
			// 检验批次号
			BillCardPanel card = e.getBillCardPanel();
			CardPanelValueUtils util = new CardPanelValueUtils(card);
			int rowCount = util.getRowCount();
			if (rowCount == 0) {
				return;
			}
			String bkey = "vbatchcode";
			Vbatchcode bhdander = new Vbatchcode(e.getContext());
			for (int i = 0; i < rowCount; i++) {
				Object oldValue = util.getBodyValue(i, bkey);
				util.setBodyValue(e.getValue(), i, bkey);
				CardBodyAfterEditEvent be = new CardBodyAfterEditEvent(card,
						"report_b", i, bkey, e.getValue(), oldValue);
				bhdander.afterEdit(be);
			}
		} else if ("vdef4".equalsIgnoreCase(key)) {
			// 不合格类型
			BillCardPanel card = e.getBillCardPanel();
			CardPanelValueUtils util = new CardPanelValueUtils(card);
			int rowCount = util.getRowCount();
			if (rowCount == 0) {
				return;
			}
			util.setBodyValue(e.getValue(), "pk_defecttype");
		} else if ("vdef5".equalsIgnoreCase(key)) {
			BillCardPanel card = e.getBillCardPanel();
			CardPanelValueUtils util = new CardPanelValueUtils(card);
			int rowCount = util.getRowCount();
			if (rowCount == 0) {
				return;
			}
			util.setBodyValue(e.getValue(), "pk_notelgiitem");
		}
	}
}
