package nc.ui.bd.bom.bom0202.handler;

import java.util.HashMap;
import java.util.Map;

import nc.ui.bd.bom.bom0202.handler.head.BomHeadTailAssMeasureHandler;
import nc.ui.bd.bom.bom0202.handler.head.BomHeadTailBomTypeHandler;
import nc.ui.bd.bom.bom0202.handler.head.BomHeadTailChangeRateHandler;
import nc.ui.bd.bom.bom0202.handler.head.BomHeadTailMaterialoidHandler;
import nc.ui.bd.bom.bom0202.handler.head.BomHeadTailMaterialvidHandler;
import nc.ui.bd.bom.bom0202.handler.head.BomHeadTailNastNumHandler;
import nc.ui.bd.bom.bom0202.handler.head.BomHeadTailNumberHandler;
import nc.ui.bd.bom.bom0202.handler.head.BomHeadTailVersiontypeHandler;
import nc.ui.mmf.framework.handler.MMBaseHandler;
import nc.ui.mmf.framework.handler.MMEventHandler;
import nc.ui.mmf.framework.view.BillFormFacade;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.components.grand.CardGrandPanelComposite;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.vo.bd.bom.bom0202.entity.BomOutputsVO;
import nc.vo.bd.bom.bom0202.entity.BomVO;
import nc.vo.bd.bom.bom0202.enumeration.OutputTypeEnum;

/**
 * <b> 卡片界面编辑后事件监听 </b>
 * <p>
 * 监听物料，变更Bom版本号；监听副产品分离定义，若不是“按相对系数分离”则清空表体的副产品分离系数
 * </p>
 * 创建日期:2010-1-28
 * 
 * @author:zhoujuna
 */
public class BomHeadTailAfterEditHandler extends MMEventHandler implements IAppEventHandler<CardHeadTailAfterEditEvent> {

    private BillFormFacade sunBillform;

    /**
     * BOM聚合卡片界面,为了取到model里主子孙的值
     */
    private CardGrandPanelComposite billForm;

    public BillFormFacade getSunBillform() {
        return this.sunBillform;
    }

    public void setSunBillform(BillFormFacade sunBillform) {
        this.sunBillform = sunBillform;
    }

    public void setBillForm(CardGrandPanelComposite billForm) {
        this.billForm = billForm;
    }

    public CardGrandPanelComposite getBillForm() {
        return this.billForm;
    }

    private Map<String, String> headMainOutputMap;

    /**
     * 多产出页签
     */
    private final static String OUTPUTSTABEL = "outputs";

    @Override
    public void handleAppEvent(CardHeadTailAfterEditEvent e) {

        MMBaseHandler handler = this.getHandler(e.getKey());
        if (handler != null) {
            handler.afterEdit(e);
        }
        else if (BomVO.HCMATERIALID.equals(e.getKey())) {
            BomHeadTailMaterialoidHandler materialidhandler = new BomHeadTailMaterialoidHandler();
            materialidhandler.setBillForm(this.getBillForm());
            materialidhandler.setSunBillform(this.getSunBillform());
            materialidhandler.afterEdit(e);
        }
        else if (BomVO.HCMATERIALVID.equals(e.getKey())) {
            BomHeadTailMaterialvidHandler materialvidhandler = new BomHeadTailMaterialvidHandler();
            materialvidhandler.setBillForm(this.getBillForm());
            materialvidhandler.setSunBillform(this.getSunBillform());
            materialvidhandler.afterEdit(e);
        }
        else if (BomVO.HCASSMEASUREID.equals(e.getKey())) {
            BomHeadTailAssMeasureHandler assmeasurehandler = new BomHeadTailAssMeasureHandler();
            assmeasurehandler.setBillForm(this.getBillForm());
            assmeasurehandler.setSunBillform(this.getSunBillform());
            assmeasurehandler.afterEdit(e);
        }
        else if (BomVO.HVCHANGERATE.equals(e.getKey())) {
            BomHeadTailChangeRateHandler changerateHandler = new BomHeadTailChangeRateHandler();
            changerateHandler.setBillForm(this.getBillForm());
            changerateHandler.setSunBillform(this.getSunBillform());
            changerateHandler.afterEdit(e);
        }
        else if (BomVO.HNPARENTNUM.equals(e.getKey())) {
            BomHeadTailNumberHandler parentnumHandler = new BomHeadTailNumberHandler();
            parentnumHandler.setBillForm(this.getBillForm());
            parentnumHandler.setSunBillform(this.getSunBillform());
            parentnumHandler.afterEdit(e);
        }
        e.getBillCardPanel().execHeadTailLoadFormulas();
        // e.getBillCardPanel().execHeadTailEditFormulas();
        // 头字段改变，主产品改变
        this.headMainOutputHandler(e);
        
        //2017-06-13 liyf 表头自定义项字段显示
        for(int i=1;i<=10;i++){
        	BillItem headItem = e.getBillCardPanel().getHeadItem("vfree"+i);
        	if(headItem!=null && headItem.isShow()){
        		 e.getBillCardPanel().getHeadItem("vfree"+i).setEdit(true);
        		 e.getBillCardPanel().getHeadItem("vfree"+i).setEnabled(true);
        	}
        }

    }

    /**
     * 头字段改变联动投入产出子表主产品信息改变
     * 
     * @param e
     */
    protected void headMainOutputHandler(CardHeadTailAfterEditEvent e) {
        if (null == this.headMainOutputMap) {
            this.setHeadMainOutputMap();
        }
        if (this.headMainOutputMap.containsKey(e.getKey())) {
            for (int i = 0; i < e.getBillCardPanel().getBillModel(BomHeadTailAfterEditHandler.OUTPUTSTABEL)
                    .getRowCount(); i++) {
                BomOutputsVO rowvo =
                        (BomOutputsVO) e.getBillCardPanel().getBillModel(BomHeadTailAfterEditHandler.OUTPUTSTABEL)
                                .getBodyValueRowVO(i, BomOutputsVO.class.getName());
                if (OutputTypeEnum.MAIN_PRODUCT.equalsValue(rowvo.getFoutputtype())) {
                    e.getBillCardPanel().setBodyValueAt(e.getValue(), i, this.headMainOutputMap.get(e.getKey()),
                            BomHeadTailAfterEditHandler.OUTPUTSTABEL);
                    e.getBillCardPanel().getBillModel(BomHeadTailAfterEditHandler.OUTPUTSTABEL)
                            .loadLoadRelationItemValue(i, this.headMainOutputMap.get(e.getKey()));
                    return;
                }
            }
        }
    }

    /**
     * 设置头字段和产出子表字段相对应的map
     * key,头字段；value,多产出行字段
     * 
     * @param map
     */
    public void setHeadMainOutputMap() {
        this.headMainOutputMap = new HashMap<String, String>();
        this.headMainOutputMap.put(BomVO.HCMATERIALID, BomOutputsVO.CMATERIALID);
        this.headMainOutputMap.put(BomVO.HCMATERIALVID, BomOutputsVO.CMATERIALVID);
        this.headMainOutputMap.put(BomVO.HCASSMEASUREID, BomOutputsVO.CASTUNITID);
        this.headMainOutputMap.put(BomVO.HNPARENTNUM, BomOutputsVO.NOUTPUTNUM);
        this.headMainOutputMap.put(BomVO.HNASSPARENTNUM, BomOutputsVO.NASTOUTPUTNUM);
        // this.headMainOutputMap.put(BomVO.HCCUSTOMERID, BomOutputsVO.CCUSTOMERID);
        // this.headMainOutputMap.put(BomVO.HCVENDORID, BomOutputsVO.CVENDORID);
        // this.headMainOutputMap.put(BomVO.HCPRODUCTORID, BomOutputsVO.CPRODUCTORID);
        this.headMainOutputMap.put(BomVO.HCPROJECTID, BomOutputsVO.CPROJECTID);
        // this.headMainOutputMap.put(BomVO.HVFREE1, BomOutputsVO.VFREE1);
        // this.headMainOutputMap.put(BomVO.HVFREE2, BomOutputsVO.VFREE2);
        // this.headMainOutputMap.put(BomVO.HVFREE3, BomOutputsVO.VFREE3);
        // this.headMainOutputMap.put(BomVO.HVFREE4, BomOutputsVO.VFREE4);
        // this.headMainOutputMap.put(BomVO.HVFREE5, BomOutputsVO.VFREE5);
        // this.headMainOutputMap.put(BomVO.HVFREE6, BomOutputsVO.VFREE6);
        // this.headMainOutputMap.put(BomVO.HVFREE7, BomOutputsVO.VFREE7);
        // this.headMainOutputMap.put(BomVO.HVFREE8, BomOutputsVO.VFREE8);
        // this.headMainOutputMap.put(BomVO.HVFREE9, BomOutputsVO.VFREE9);
        // this.headMainOutputMap.put(BomVO.HVFREE10, BomOutputsVO.VFREE10);
    }

    @Override
    public void initMap() {
        // this.putHandler(BomVO.HCMATERIALID, BomHeadTailMaterialoidHandler.class);
        // this.putHandler(BomVO.HCMATERIALVID, BomHeadTailMaterialvidHandler.class);
        // this.putHandler(BomVO.HCASSMEASUREID, BomHeadTailAssMeasureHandler.class);
        // this.putHandler(BomVO.HVCHANGERATE, BomHeadTailChangeRateHandler.class);
        // this.putHandler(BomVO.HNPARENTNUM, BomHeadTailNumberHandler.class);
        this.putHandler(BomVO.HNASSPARENTNUM, BomHeadTailNastNumHandler.class);
        this.putHandler(BomVO.HFVERSIONTYPE, BomHeadTailVersiontypeHandler.class);
        this.putHandler(BomVO.FBOMTYPE, BomHeadTailBomTypeHandler.class);
        this.putExtHandler();
    }

    protected void putExtHandler() {
    }
}
