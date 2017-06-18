/**
 *
 */
package nc.ui.cm.fetchset.eventhandler;

import java.util.HashMap;
import java.util.Map;

import nc.ui.bd.pub.handler.CMBasedocAbstractHandler;
import nc.ui.bd.pub.handler.CMBasedocEventHandler;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.vo.cm.fetchset.entity.FetchSetItemVO;

/**
 * 储运编辑前事件
 * @author liyf
 *
 */
public class HuanbaoCardBodyBeforEditHandler extends CMBasedocEventHandler implements
        IAppEventHandler<CardBodyBeforeEditEvent> {

    @Override
    public void handleAppEvent(CardBodyBeforeEditEvent e) {
        CMBasedocAbstractHandler handler = this.getHandler(e.getKey());
        if (handler != null) {
            // 执行相应的处理过程
            handler.beforeEdit(e);
        }
        else {
            e.setReturnValue(Boolean.TRUE);
        }
    }

    @Override
    public void initMap() {
        Map<String, Class<?>> handlerMap = new HashMap<String, Class<?>>();
        // 将所有进行卡片表体编辑前处理的字段处理类名放入MAP中（key=处理字段名，value=字段编辑处理类）
        handlerMap.put("pk_qcdept", DeptHandler.class);
        handlerMap.put("pk_serverdept", DeptHandler.class);
        this.putAllHandler(handlerMap);
    }

}
