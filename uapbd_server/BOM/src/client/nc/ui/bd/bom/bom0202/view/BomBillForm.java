package nc.ui.bd.bom.bom0202.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.bd.bom.bom0202.IBomBillQueryService;
import nc.ui.bd.bom.bom0202.model.BomMainBillManageModel;
import nc.ui.bd.bom.bom0202.scale.BomScaleUtil;
import nc.ui.mmf.framework.view.BillFormFacade;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.view.value.IBlankChildrenFilter;
import nc.ui.uif2.AppEvent;
import nc.util.mmf.framework.base.MMArrayUtil;
import nc.vo.bd.bom.bom0202.entity.AggBomVO;
import nc.vo.bd.bom.bom0202.entity.BomItemVO;
import nc.vo.bd.bom.bom0202.entity.BomOutputsVO;
import nc.vo.bd.bom.bom0202.entity.BomSelectVO;
import nc.vo.bd.bom.bom0202.entity.BomUseOrgVO;
import nc.vo.bd.bom.bom0202.entity.BomVO;
import nc.vo.bd.bom.bom0202.enumeration.BomTypeEnum;
import nc.vo.bd.bom.bom0202.enumeration.FBomBillstatusEnum;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 生产bom卡片表单 </b>
 * <p>
 * 用于设置默认值
 * </p>
 * 创建日期:2010-1-15
 * 
 * @author:zhoujuna
 */
public class BomBillForm extends BillFormFacade {

    /**
     * ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 一下2个属性是给导入使用
     */
    private boolean isImport = false;

    private IBlankChildrenFilter orgIBlankChildrenFilterSon;

    @Override
    public void initUI() {
        super.initUI();
        // 材料参照设置为多选
        UIRefPane itemPanel =
                (UIRefPane) this.getBillCardPanel().getBodyItem(BomTableCode.BOMITEMS, BomItemVO.CMATERIALVID)
                        .getComponent();
        itemPanel.setMultiSelectedEnabled(true);
        // 多产出物料参照设置为多选
        UIRefPane panel2 =
                (UIRefPane) this.getBillCardPanel().getBodyItem(BomTableCode.BOMOUTPUTS, BomOutputsVO.CMATERIALVID)
                        .getComponent();
        panel2.setMultiSelectedEnabled(true);
        // // 替代物料参照设置为多选
        // UIRefPane bomPane =
        // (UIRefPane) this.getBillCardPanel().getBodyItem(BomTableCode.BOMREPLACE, BomReplaceVO.CMATERIALVID)
        // .getComponent();
        // bomPane.setMultiSelectedEnabled(true);
        // 表体按行号进行升序排列
        this.getBillCardPanel().getBillModel(BomTableCode.BOMITEMS).sortByColumn(BomItemVO.VROWNO, true);
        // 表体按行号进行升序排列
        this.getBillCardPanel().getBillModel(BomTableCode.BOMOUTPUTS).sortByColumn(BomOutputsVO.VROWNO, true);
        // 使用组织页签按编码进行排序
        this.getBillCardPanel().getBillModel(BomTableCode.BOMUSEORG)
                .sortByColumn(BomUseOrgVO.PK_USEORG + ".name", true);
        // 精度处理
        this.scaleProcess();
    }

    @Override
    protected void onAdd() {
        super.onAdd();
        this.getBillCardPanel().getHeadItem(BomVO.HCMATERIALID).setEnabled(true);
        this.getBillCardPanel().getHeadItem(BomVO.HCMATERIALVID).setEnabled(true);
        this.getBillCardPanel().getHeadItem(BomVO.HVERSION).setEnabled(true);
        //2017-06-13 liyf 表头自定义项字段显示
        for(int i=1;i<=10;i++){
        	BillItem headItem = getBillCardPanel().getHeadItem("vfree"+i);
        	if(headItem!=null && headItem.isShow()){
                this.getBillCardPanel().getHeadItem("vfree"+i).setEnabled(true);
        	}
        }
    }

    @Override
    protected void onEdit() {
        super.onEdit();
        BomVO vo = this.getSelectedBomVO();
        if (this.getBillCardPanel().getHeadItem(BomVO.FBOMTYPE).getValueObject().equals(BomTypeEnum.CONFIGBOM.value())) {
            // 配置BOM节点 bom类型不可编辑
            this.getBillCardPanel().getHeadItem(BomVO.FBOMTYPE).setEdit(false);
            AggBomVO[] aggvo = null;
            try {
                aggvo = this.getQueryService().queryAggBomByBomID(new String[] {
                    vo.getPrimaryKey()
                });
            }
            catch (BusinessException e) {
                ExceptionUtils.wrappException(e);
            }
            if (MMArrayUtil.isNotEmpty(aggvo)) {
                BomItemVO[] items = (BomItemVO[]) aggvo[0].getChildren(BomItemVO.class);
                Map<String, List<BomSelectVO>> tempselectVOs = new HashMap<String, List<BomSelectVO>>();
                for (BomItemVO item : items) {
                    if (!item.getBbisfeature().booleanValue() && item.getBischoice().booleanValue()
                            && MMArrayUtil.isNotEmpty(item.getSelect())) {
                        tempselectVOs.put(item.getCmaterialvid() + item.getVselectcond(), new ArrayList<BomSelectVO>(
                                Arrays.asList(item.getSelect())));
                    }
                }
                this.setTempSelectVOs(tempselectVOs);
            }
        }
        if (FBomBillstatusEnum.APPROVE.equalsValue(vo.getFbillstatus())) {
            this.setBillItemPropertyForAuditState(vo);

        }
        else {
            this.getBillCardPanel().getHeadItem(BomVO.HCMATERIALID).setEdit(false);
            this.getBillCardPanel().getHeadItem(BomVO.HCMATERIALVID).setEdit(false);
            this.getBillCardPanel().getHeadItem(BomVO.HVERSION).setEdit(false);
            this.getBillCardPanel().getHeadItem(BomVO.HFVERSIONTYPE).setEdit(false);
          
            
        }
    }

    /**
     * 精度处理
     */
    private void scaleProcess() {
        new BomScaleUtil().setCardScale(this.getBillCardPanel(), this.getModel().getContext().getPk_group());
    }

    /**
     * 重写是给导入使用,导入时，不做空行过滤
     */
    @Override
    public Object getValue() {
        if (this.isImport()) {
            this.getOrgIBlankChildrenFilterSon();
            this.setBlankChildrenFilter(null);
        }
        else {
            this.setBlankChildrenFilter(this.getOrgIBlankChildrenFilterSon());
        }
        return super.getValue();
    }

    public IBlankChildrenFilter getOrgIBlankChildrenFilterSon() {
        if (this.orgIBlankChildrenFilterSon == null) {
            this.orgIBlankChildrenFilterSon = this.getBlankChildrenFilter();
        }
        return this.orgIBlankChildrenFilterSon;
    }

    public boolean isImport() {
        return this.isImport;
    }

    public void setImport(boolean isImport) {
        this.isImport = isImport;
    }

    protected BomVO getSelectedBomVO() {
        Object obj = this.getModel().getSelectedData();
        BomVO vo = null;
        if (obj instanceof AggBomVO) {
            AggBomVO aggvo = (AggBomVO) obj;
            vo = (BomVO) aggvo.getParentVO();
        }
        else {
            vo = (BomVO) obj;
        }
        return vo;

    }

    protected void setBillItemPropertyForAuditState(BomVO vo) {

        BomMainBillManageModel model = (BomMainBillManageModel) this.getModel();
        if (model.isAdjust()) {
            // 表头可编辑字段
            BillItem[] itemheadKey = this.billCardPanel.getHeadItems();
            List<String> headEditList = new ArrayList<String>();
            headEditList.addAll(Arrays.asList(BomBillForm.REVISEABLEITEMKEYS_AUDITSTATE));
            for (BillItem item : itemheadKey) {
                if (!headEditList.contains(item.getKey())) {
                    this.getBillCardPanel().getHeadItem(item.getKey()).setEdit(false);
                }
            }

            // 表体可编辑字段
            BillItem[] itemKey = this.billCardPanel.getBodyItems();
            List<String> bodyEditList = new ArrayList<String>();
            bodyEditList.addAll(Arrays.asList(BomBillForm.REVISEABLEITEMKEYS_AUDITSTATE_BODY));
            for (BillItem item : itemKey) {
                if (!bodyEditList.contains(item.getKey())) {
                    this.getBillCardPanel().getBodyItem(BomTableCode.BOMITEMS, item.getKey()).setEdit(false);
                }
            }
        }
    }
    @Override
    public void handleEvent(AppEvent event) {
    	// TODO 自动生成的方法存根
    	super.handleEvent(event);
    	  //2017-06-13 liyf 表头自定义项字段显示
       
    	
    }
    /***
     * 修订审核态可以修改的项目：表头<br>
     * 齐套，备注
     */
    public static final String[] REVISEABLEITEMKEYS_AUDITSTATE = new String[] {
        BomVO.BKITITEM, BomVO.HVNOTE, BomVO.HVDEF1, BomVO.HVDEF2, BomVO.HVDEF3, BomVO.HVDEF4, BomVO.HVDEF5,
        BomVO.HVDEF6, BomVO.HVDEF7, BomVO.HVDEF8, BomVO.HVDEF9, BomVO.HVDEF10, BomVO.HVDEF11, BomVO.HVDEF12,
        BomVO.HVDEF13, BomVO.HVDEF14, BomVO.HVDEF15, BomVO.HVDEF16, BomVO.HVDEF17, BomVO.HVDEF18, BomVO.HVDEF19,
        BomVO.HVDEF20
    };

    /***
     * 修订审核态可以修改的项目：表体<br>
     * 齐套，备注
     */
    public static final String[] REVISEABLEITEMKEYS_AUDITSTATE_BODY = new String[] {
        BomItemVO.FITEMTYPE, BomItemVO.FITEMSOURCE, BomItemVO.BKITMATERIAL, BomItemVO.BBSTEPLOSS,
        BomItemVO.ILEADTIMENUM, BomItemVO.BDELIVER, BomItemVO.BMAINMATERIAL, BomItemVO.FSUPPLYMODE,
        BomItemVO.FBACKFLUSHTIME, BomItemVO.FBACKFLUSHTYPE, BomItemVO.BBUNIBATCH, BomItemVO.BBCHKITEMFORWR,
        BomItemVO.VMATINGNO, BomItemVO.UPINT, BomItemVO.VDEF1, BomItemVO.VDEF2, BomItemVO.VDEF3, BomItemVO.VDEF4,
        BomItemVO.VDEF5, BomItemVO.VDEF6, BomItemVO.VDEF7, BomItemVO.VDEF8, BomItemVO.VDEF9, BomItemVO.VDEF10,
        BomItemVO.VDEF11, BomItemVO.VDEF12, BomItemVO.VDEF13, BomItemVO.VDEF14, BomItemVO.VDEF15, BomItemVO.VDEF16,
        BomItemVO.VDEF17, BomItemVO.VDEF18, BomItemVO.VDEF19, BomItemVO.VDEF20, BomItemVO.VNOTE
    };

    /**
     * 配置BOM节点 用户对选择条件修改时，取此表的内容初始化选择条件对话框
     * key 表体物料vid+选择条件（字符串）
     */
    private Map<String, List<BomSelectVO>> tempSelectVOs;

    public Map<String, List<BomSelectVO>> getTempSelectVOs() {
        return this.tempSelectVOs;
    }

    public void setTempSelectVOs(Map<String, List<BomSelectVO>> tempSelectVOs) {
        this.tempSelectVOs = tempSelectVOs;
    }

    private IBomBillQueryService getQueryService() {
        return NCLocator.getInstance().lookup(IBomBillQueryService.class);
    }
}
