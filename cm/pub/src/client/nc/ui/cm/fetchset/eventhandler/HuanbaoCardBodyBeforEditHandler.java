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
 * ���˱༭ǰ�¼�
 * @author liyf
 *
 */
public class HuanbaoCardBodyBeforEditHandler extends CMBasedocEventHandler implements
        IAppEventHandler<CardBodyBeforeEditEvent> {

    @Override
    public void handleAppEvent(CardBodyBeforeEditEvent e) {
        CMBasedocAbstractHandler handler = this.getHandler(e.getKey());
        if (handler != null) {
            // ִ����Ӧ�Ĵ������
            handler.beforeEdit(e);
        }
        else {
            e.setReturnValue(Boolean.TRUE);
        }
    }

    @Override
    public void initMap() {
        Map<String, Class<?>> handlerMap = new HashMap<String, Class<?>>();
        // �����н��п�Ƭ����༭ǰ������ֶδ�����������MAP�У�key=�����ֶ�����value=�ֶα༭�����ࣩ
        handlerMap.put("pk_qcdept", DeptHandler.class);
        handlerMap.put("pk_serverdept", DeptHandler.class);
        this.putAllHandler(handlerMap);
    }

}
