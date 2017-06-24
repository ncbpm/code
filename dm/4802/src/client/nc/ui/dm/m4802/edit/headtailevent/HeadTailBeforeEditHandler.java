package nc.ui.dm.m4802.edit.headtailevent;

import nc.ui.dm.m4802.edit.headtailevent.handler.ConsignEditHandler;
import nc.ui.dm.m4802.edit.headtailevent.handler.RouteEditHandler;
import nc.ui.dm.m4802.edit.headtailevent.handler.TranTypeCodeEditHandler;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.vo.dm.m4802.entity.DelivapplyHVO;

/**
 * �������뵥��ͷ����β�༭ǰ�¼�
 * 
 * @since 6.0
 * @version 2010-11-26 ����08:44:19
 * @author ����
 */
public class HeadTailBeforeEditHandler implements
		IAppEventHandler<CardHeadTailBeforeEditEvent> {

	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
		String key = e.getKey();

		// ����
		if (key.equals(DelivapplyHVO.VDEF1)) {
			Pk_applydept_v handler = new Pk_applydept_v();
			handler.beforeEdit(e);
		}
		// ί�е�λ
		if (key.equals(DelivapplyHVO.CCONSIGNID)) {
			ConsignEditHandler handler = new ConsignEditHandler();
			handler.beforeEdit(e);
		}

		// ���佻������
		else if (key.equals(DelivapplyHVO.CTRANTYPEID)) {
			TranTypeCodeEditHandler handler = new TranTypeCodeEditHandler();
			handler.beforeEdit(e);
		}

		// ��������·��
		else if (key.equals(DelivapplyHVO.CROUTEID)) {
			RouteEditHandler handler = new RouteEditHandler();
			handler.beforeEdit(e);
		}
		if (e.getReturnValue() == null) {
			e.setReturnValue(Boolean.TRUE);
		}
	}
}
