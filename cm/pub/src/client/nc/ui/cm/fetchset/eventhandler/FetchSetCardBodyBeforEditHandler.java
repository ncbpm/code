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
 * @since v636
 * @version 2015��4��8�� ����10:55:45
 * @author zhangweix
 */
public class FetchSetCardBodyBeforEditHandler extends CMBasedocEventHandler implements
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
        handlerMap.put(FetchSetItemVO.PK_BILLTYPEID, BillTypeHandler.class);
        this.putAllHandler(handlerMap);
    }

}
