package nc.ui.bd.bom.bom0202.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.bd.bom.bom0202.IBomBillQueryService;
import nc.ui.bd.bom.bom0202.model.BomMainBillManageModel;
import nc.ui.bd.bom.bom0202.util.GroupBomClientUtil;
import nc.ui.bd.bom.bom0202.view.BomBillForm;
import nc.ui.mmf.framework.util.BillCardPanelUtil;
import nc.ui.mmf.framework.view.BillFormFacade;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.actions.CopyAction;
import nc.ui.pubapp.uif2app.components.grand.CardGrandPanelComposite;
import nc.ui.pubapp.uif2app.components.grand.model.MainGrandModel;
import nc.ui.uif2.UIState;
import nc.util.mmf.busi.service.OrgUnitPubService;
import nc.util.mmf.framework.base.MMArrayUtil;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.bd.bom.bom0202.entity.AggBomVO;
import nc.vo.bd.bom.bom0202.entity.BomItemVO;
import nc.vo.bd.bom.bom0202.entity.BomSelectVO;
import nc.vo.bd.bom.bom0202.entity.BomUseOrgVO;
import nc.vo.bd.bom.bom0202.entity.BomVO;
import nc.vo.bd.bom.bom0202.entity.BomWipVO;
import nc.vo.bd.bom.bom0202.enumeration.BomTypeEnum;
import nc.vo.bd.bom.bom0202.util.BomDecryptUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.util.CloneUtil;

/**
 * <b> 复制新增按钮 </b>
 * <p>
 * 二级按钮，用于复制新增方法
 * </p>
 * 创建日期:2010-1-25
 * 
 * @author:zhoujuna
 */
public class BomCopyAddAction extends CopyAction {

    private static final long serialVersionUID = 1603043332461137013L;

    private MainGrandModel grandModel;

    private CardGrandPanelComposite mainGrandBillcard;

    private BillFormFacade billform;

    public BillFormFacade getBillform() {
        return this.billform;
    }

    public void setBillform(BillFormFacade billform) {
        this.billform = billform;
    }

    public MainGrandModel getGrandModel() {
        return this.grandModel;
    }

    public void setGrandModel(MainGrandModel grandModel) {
        this.grandModel = grandModel;
    }

    public CardGrandPanelComposite getMainGrandBillcard() {
        return this.mainGrandBillcard;
    }

    public void setMainGrandBillcard(CardGrandPanelComposite mainGrandBillcard) {
        this.mainGrandBillcard = mainGrandBillcard;
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
        AggBomVO aggvo = (AggBomVO) this.getGrandModel().getSelectedData();
        String pkOrg = aggvo.getParent().getAttributeValue(BomVO.PK_ORG).toString();

        AggBomVO aggbomvo = (AggBomVO) CloneUtil.deepClone(aggvo);
        BomVO headvo = (BomVO) aggbomvo.getParent();
        // 查询完整VO，防止列表查询后换行复制 VO信息不完整
        String headPk = headvo.getCbomid();
        AggBomVO[] aggvos = this.getQueryService().queryAggBomByBomID(new String[] {
            headPk
        });
        if (MMArrayUtil.isEmpty(aggvos)) {
            return;
        }
        aggbomvo = aggvos[0];

        if (super.getCopyActionProcessor() != null) {
            super.getCopyActionProcessor().processVOAfterCopy(aggbomvo, this.getModel().getContext());
        }
        if (MMValueCheck.isNotEmpty(aggbomvo)) {
            BomVO head = (BomVO) aggbomvo.getParent();
            //2017-08-30 设置BOM号等于版本号
            head.setVfree3(head.getHversion());
            
            //2017-08-30 设置BOM号等于版本号

            // 配置BOM复制时需要在billform设置选择条件map.
            if (head.getFbomtype().intValue() == BomTypeEnum.CONFIGBOM.toIntValue()) {
                BomItemVO[] items = (BomItemVO[]) aggbomvo.getChildren(BomItemVO.class);
                Map<String, List<BomSelectVO>> tempselectVOs = new HashMap<String, List<BomSelectVO>>();
                for (BomItemVO item : items) {
                    if (!item.getBbisfeature().booleanValue() && item.getBischoice().booleanValue()
                            && MMArrayUtil.isNotEmpty(item.getSelect())) {
                        tempselectVOs.put(item.getCmaterialvid() + item.getVselectcond(), new ArrayList<BomSelectVO>(
                                Arrays.asList(item.getSelect())));
                    }
                }
                ((BomBillForm) this.getBillform()).setTempSelectVOs(tempselectVOs);
            }
        }
        // 清掉表头pk
        aggbomvo.getParentVO().setAttributeValue(BomVO.CBOMID, null);
        // 清空对应线上仓
        this.clearWipVOs(aggbomvo);
        // headvo.setStatus(VOStatus.NEW);
        this.getModel().setUiState(UIState.ADD);
     

        this.getMainGrandBillcard().setValue(aggbomvo);
        // 处理物料真名
        this.processMaterialTrueName(this.getEditor().getBillCardPanel(), headvo.getHcmaterialid(), pkOrg);
        // 复制时将 组织变更为最新的组织
        this.getEditor().getBillCardPanel().getHeadItem(BomVO.PK_ORG_V)
                .setValue(OrgUnitPubService.getNewVIDByOrgID(pkOrg));
        // }
        // 主组织不可编辑
        this.getOrgPanel().setEnabled(false);
        // 清空使用组织的pk
        this.clearUseOrgInfo(pkOrg);
   

    }

    private void clearWipVOs(AggBomVO aggvo) {
        BomVO bomvo = (BomVO) aggvo.getParentVO();
        String pkorg = bomvo.getPk_org();
        BomItemVO[] items = aggvo.getChildrenVO();
        for (BomItemVO itemvo : items) {
            BomWipVO[] wips = itemvo.getWips();
            List<BomWipVO> wipList = new ArrayList<BomWipVO>();
            if (MMValueCheck.isEmpty(wips)) {
                continue;
            }
            for (BomWipVO bomWipVO : wips) {
                if (bomWipVO.getPk_org().equals(pkorg)) {
                    wipList.add(bomWipVO);
                }
            }
            itemvo.setWips(wipList.toArray(new BomWipVO[0]));
        }
    }

    private void clearUseOrgInfo(String pkOrg) {
        this.getEditor().getBillCardPanel().getBillModel(BomUseOrgVO.TABLECODE).clearBodyData();
        this.getEditor().getBillCardPanel().getBillModel(BomUseOrgVO.TABLECODE).insertRow(0);
        this.getEditor().getBillCardPanel().setBodyValueAt(pkOrg, 0, BomUseOrgVO.PK_USEORG, BomUseOrgVO.TABLECODE);
        this.getEditor().getBillCardPanel().getBillModel(BomUseOrgVO.TABLECODE).loadLoadRelationItemValue();
        this.getEditor().getBillCardPanel().getBillModel(BomUseOrgVO.TABLECODE).execLoadFormula();
    }

    private void processMaterialTrueName(BillCardPanel panel, String materialoid, String pk_org) {
        // 通过OID和密钥查询物料密钥信息
        BomMainBillManageModel model = (BomMainBillManageModel) this.getMainGrandBillcard().getModel().getMainModel();
        String pwdkey = model.getPwdKey();
        if (MMValueCheck.isEmpty(pwdkey)) {
            return;
        }
        String materialTrueName;
        try {
            BillCardPanelUtil util = new BillCardPanelUtil(panel);
            if (MMValueCheck.isNotEmpty(materialoid)) {
                materialTrueName = BomDecryptUtil.getMaterialTrueName(materialoid, pk_org, pwdkey);
                if (MMValueCheck.isNotEmpty(materialTrueName)) {
                    util.setHeadTailValue(BomVO.HVPLAINTEXT, materialTrueName);
                }

            }
            else {
                util.setHeadTailValue(BomVO.HVPLAINTEXT, null);
            }
        }
        catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }

    }

    @Override
    protected boolean isActionEnable() {
        // 判断组织用户是否具有所选数据对应组织的操作权限
        if (!GroupBomClientUtil.checkOrgPermission(this.getModel()).booleanValue()) {
            return false;
        }
        return super.isActionEnable();
    }

    public IBomBillQueryService getQueryService() {
        return NCLocator.getInstance().lookup(IBomBillQueryService.class);
    }

}
