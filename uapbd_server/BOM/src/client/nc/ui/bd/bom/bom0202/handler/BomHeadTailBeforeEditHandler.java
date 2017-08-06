package nc.ui.bd.bom.bom0202.handler;

import nc.ui.bd.bom.bom0202.handler.head.BomHeadTailAssMeasureHandler;
import nc.ui.bd.bom.bom0202.handler.head.BomHeadTailBomTypeHandler;
import nc.ui.bd.bom.bom0202.handler.head.BomHeadTailChangeRateHandler;
import nc.ui.bd.bom.bom0202.handler.head.BomHeadTailMaterialoidHandler;
import nc.ui.bd.bom.bom0202.handler.head.BomHeadTailMaterialvidHandler;
import nc.ui.mmf.framework.handler.MMBaseHandler;
import nc.ui.mmf.framework.handler.MMEventHandler;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.vo.bd.bom.bom0202.entity.BomVO;

/**
 * <b> ����BOM��ͷ�༭ǰ�¼����� </b>
 * <p>
 * �������ϲ��շ�Χ
 * </p>
 * ��������:2010-8-4
 * 
 * @author:zhoujuna
 */
public class BomHeadTailBeforeEditHandler extends MMEventHandler implements
        IAppEventHandler<CardHeadTailBeforeEditEvent> {

    @Override
    public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
//      //2017-06-13 liyf ֧��������
      if(e.getKey().startsWith("vfree")){
      	e.setReturnValue(null);
      	e.setReturnValue(true);
      	return ; 
      }
        MMBaseHandler handler = this.getHandler(e.getKey());
        if (handler != null) {
            handler.beforeEdit(e);
        }

    }

    @Override
    public void initMap() {
        this.putHandler(BomVO.HCMATERIALID, BomHeadTailMaterialoidHandler.class);
        this.putHandler(BomVO.HCMATERIALVID, BomHeadTailMaterialvidHandler.class);
        this.putHandler(BomVO.HCASSMEASUREID, BomHeadTailAssMeasureHandler.class);
        this.putHandler(BomVO.HVCHANGERATE, BomHeadTailChangeRateHandler.class);
        this.putHandler(BomVO.FBOMTYPE, BomHeadTailBomTypeHandler.class);
    }

}
