package nc.impl.bd.bom.bom0202;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.bs.bd.bom.bom0202.bp.BomUpdateBP;
import nc.bs.bd.bom.bom0202.rule.AutoToBmRtRuleForRainbowSign;
import nc.itf.bd.bom.bom0202.IBomAssignService;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.util.mmf.framework.gc.GCBillTransferTool;
import nc.vo.bd.bom.bom0202.entity.AggBomVO;
import nc.vo.bd.bom.bom0202.entity.BomItemVO;
import nc.vo.bd.bom.bom0202.entity.BomUseOrgVO;
import nc.vo.bd.bom.bom0202.entity.BomWipVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class BomAssignServiceImpl implements IBomAssignService {

    @Override
    public AggBomVO[] assign_RequiresNew(AggBomVO[] vos) throws BusinessException {
        try {
            if (MMValueCheck.isEmpty(vos)) {
                return null;
            }
            boolean isDelete = false;
            AggBomVO aggvo = vos[0];
            // ���� + ���ts
            GCBillTransferTool<AggBomVO> transTool = new GCBillTransferTool<AggBomVO>(vos, true);
            // ��ȫǰ̨VO
            AggBomVO[] fullBills = transTool.getClientFullInfoBill();
            // ����޸�ǰvo
            AggBomVO[] originBills = transTool.getOriginBills();
            // ��ȡʹ����֯VO��״̬
            if (MMValueCheck.isNotEmpty(fullBills)) {
                BomUseOrgVO[] usevos = (BomUseOrgVO[]) aggvo.getChildren(BomUseOrgVO.class);
                if (MMValueCheck.isNotEmpty(vos) && vos.length == 1) {
                    // ����һ��ֻ����һ��ʹ����֯������ֻ�жϵ�һ���Ƿ�Ϊȡ������̬�Ϳ���
                    BomUseOrgVO vo = usevos[0];
                    if (vo.getStatus() == VOStatus.DELETED) {
                        isDelete = true;
                        // ��ȡҪɾ�������ϲ�VO
                        List<BomItemVO> itemlist = this.getDelWips(vo.getPk_useorg(), originBills);
                        for (BomItemVO itemvo : itemlist) {
                            itemvo.setStatus(VOStatus.UNCHANGED);
                        }
                        fullBills[0].setChildren(BomItemVO.class, itemlist.toArray(new BomItemVO[0]));
                    }
                    fullBills[0].setChildren(BomUseOrgVO.class, usevos);
                }
            }
         // liyf 2017-07-25 1.�������ɶ�Ӧ��ʹ����֯��bom/����·��ƥ�����
            new AutoToBmRtRuleForRainbowSign().process(vos);
            
            BomUpdateBP bp = new BomUpdateBP();
            bp.setAssign(!isDelete);
            bp.setCancleAssign(isDelete);
         
            return bp.update(fullBills, originBills, false, false, false);
        }
        catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
        return null;
    }

    private List<BomItemVO> getDelWips(String useOrg, AggBomVO[] originBills) {
        if (MMValueCheck.isEmpty(originBills)) {
            return null;
        }
        AggBomVO aggvo = originBills[0];
        if (MMValueCheck.isEmpty(aggvo.getChildrenVO())) {
            return null;
        }
        List<BomItemVO> itemList =
                (List<BomItemVO>) nc.vo.util.CloneUtil.deepClone(Arrays.asList(aggvo.getChildrenVO()));
        for (BomItemVO itemvo : itemList) {
            BomWipVO[] wipvos = itemvo.getWips();
            if (MMValueCheck.isEmpty(wipvos)) {
                continue;
            }
            List<BomWipVO> tmpList = new ArrayList<BomWipVO>();
            for (BomWipVO bomWipVO : wipvos) {
                if (bomWipVO.getPk_org().equals(useOrg)) {
                    BomWipVO newWipVO = (BomWipVO) nc.vo.util.CloneUtil.deepClone(bomWipVO);
                    newWipVO.setStatus(VOStatus.DELETED);
                    tmpList.add(newWipVO);
                }
            }
            itemvo.setWips(tmpList.toArray(new BomWipVO[0]));
        }
        return itemList;
    }
}
