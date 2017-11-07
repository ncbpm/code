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
 * <b> ����������ť </b>
 * <p>
 * ������ť�����ڸ�����������
 * </p>
 * ��������:2010-1-25
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
        // ��ѯ����VO����ֹ�б��ѯ���и��� VO��Ϣ������
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
            //2017-08-30 ����BOM�ŵ��ڰ汾��
            head.setVfree3(head.getHversion());
            
            //2017-08-30 ����BOM�ŵ��ڰ汾��

            // ����BOM����ʱ��Ҫ��billform����ѡ������map.
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
        // �����ͷpk
        aggbomvo.getParentVO().setAttributeValue(BomVO.CBOMID, null);
        // ��ն�Ӧ���ϲ�
        this.clearWipVOs(aggbomvo);
        // headvo.setStatus(VOStatus.NEW);
        this.getModel().setUiState(UIState.ADD);
     

        this.getMainGrandBillcard().setValue(aggbomvo);
        // ������������
        this.processMaterialTrueName(this.getEditor().getBillCardPanel(), headvo.getHcmaterialid(), pkOrg);
        // ����ʱ�� ��֯���Ϊ���µ���֯
        this.getEditor().getBillCardPanel().getHeadItem(BomVO.PK_ORG_V)
                .setValue(OrgUnitPubService.getNewVIDByOrgID(pkOrg));
        // }
        // ����֯���ɱ༭
        this.getOrgPanel().setEnabled(false);
        // ���ʹ����֯��pk
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
        // ͨ��OID����Կ��ѯ������Կ��Ϣ
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
        // �ж���֯�û��Ƿ������ѡ���ݶ�Ӧ��֯�Ĳ���Ȩ��
        if (!GroupBomClientUtil.checkOrgPermission(this.getModel()).booleanValue()) {
            return false;
        }
        return super.isActionEnable();
    }

    public IBomBillQueryService getQueryService() {
        return NCLocator.getInstance().lookup(IBomBillQueryService.class);
    }

}
