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
 * ���ݱ�ͷ��β�ֶα༭���¼�
 * 
 * @since 6.0
 * @version 2010-12-21 ����02:22:08
 * @author hanbin
 */
public class HeadTailAfterEditHandler implements
		IAppEventHandler<CardHeadTailAfterEditEvent> {

	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		String key = e.getKey();
		if (key.equals(ReportHeaderVO.VCHKBATCH)) {
			// �������κ�
			new Vchkbatch().afterEdit(e);
		} else if (key.equals(ReportHeaderVO.NCHECKASTNUM)) {
			// ��������
			new Ncheckastnum().afterEdit(e);
		} else if (key.equals(ReportHeaderVO.NCHECKNUM)) {
			// ����������
			new Nchecknum().afterEdit(e);
		}
		// 1���ʼ챨���ͷ�Զ���������2��3��4��5�ֱ�Ϊ�����ȼ������������Ǽǣ����������κţ��ַ���������100�������ϸ����ͣ����ղ��ϸ����ͣ������ϸ���Ŀ�����ռ�����Ŀ����
		// 2��ʵ�ֱ��ڱ�ͷ¼�������ȼ�����Ӧ���鷽���������ȼ��飩���������Ρ����ϸ����ͣ������ȼ�Ϊ���ϸ�ʱ�������ϸ���Ŀ�������ȼ�Ϊ���ϸ�ʱ�����ܹ��Զ����뵽���壬�����ϱ���¼�봥���������ֶεĸ�ֵ�������״̬�����鴦��ʽ���ϸ�Ʒ������ʽ�ж�����
		else if ("vdef2".equalsIgnoreCase(key)) {
			// �����ȼ�
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
				// �����ȼ�
				bhdander.afterEdit(be,(String)e.getValue());
			}

		} else if ("vdef3".equalsIgnoreCase(key)) {
			// �������κ�
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
			// ���ϸ�����
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
