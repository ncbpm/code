package nc.ui.dm.m4802.edit.headtailevent;

import nc.ui.dm.m4802.edit.headtailevent.handler.ConsignEditHandler;
import nc.ui.dm.m4802.edit.headtailevent.handler.RouteEditHandler;
import nc.ui.dm.m4802.edit.headtailevent.handler.TranTypeCodeEditHandler;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.vo.dm.m4802.entity.DelivapplyHVO;

/**
 * 运输申请单表头、表尾编辑前事件
 * 
 * @since 6.0
 * @version 2010-11-26 上午08:44:19
 * @author 李誉
 */
public class HeadTailBeforeEditHandler implements
		IAppEventHandler<CardHeadTailBeforeEditEvent> {

	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
		String key = e.getKey();

		// 部门
		if (key.equals(DelivapplyHVO.VDEF1)) {
			Pk_applydept_v handler = new Pk_applydept_v();
			handler.beforeEdit(e);
		}
		// 委托单位
		if (key.equals(DelivapplyHVO.CCONSIGNID)) {
			ConsignEditHandler handler = new ConsignEditHandler();
			handler.beforeEdit(e);
		}

		// 运输交易类型
		else if (key.equals(DelivapplyHVO.CTRANTYPEID)) {
			TranTypeCodeEditHandler handler = new TranTypeCodeEditHandler();
			handler.beforeEdit(e);
		}

		// 过滤运输路线
		else if (key.equals(DelivapplyHVO.CROUTEID)) {
			RouteEditHandler handler = new RouteEditHandler();
			handler.beforeEdit(e);
		}
		if (e.getReturnValue() == null) {
			e.setReturnValue(Boolean.TRUE);
		}
	}
}
