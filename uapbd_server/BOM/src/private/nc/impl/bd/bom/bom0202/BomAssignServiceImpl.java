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
            // 加锁 + 检查ts
            GCBillTransferTool<AggBomVO> transTool = new GCBillTransferTool<AggBomVO>(vos, true);
            // 补全前台VO
            AggBomVO[] fullBills = transTool.getClientFullInfoBill();
            // 获得修改前vo
            AggBomVO[] originBills = transTool.getOriginBills();
            // 获取使用组织VO的状态
            if (MMValueCheck.isNotEmpty(fullBills)) {
                BomUseOrgVO[] usevos = (BomUseOrgVO[]) aggvo.getChildren(BomUseOrgVO.class);
                if (MMValueCheck.isNotEmpty(vos) && vos.length == 1) {
                    // 由于一次只处理一个使用组织，所以只判断第一个是否为取消分配态就可以
                    BomUseOrgVO vo = usevos[0];
                    if (vo.getStatus() == VOStatus.DELETED) {
                        isDelete = true;
                        // 获取要删除的线上仓VO
                        List<BomItemVO> itemlist = this.getDelWips(vo.getPk_useorg(), originBills);
                        for (BomItemVO itemvo : itemlist) {
                            itemvo.setStatus(VOStatus.UNCHANGED);
                        }
                        fullBills[0].setChildren(BomItemVO.class, itemlist.toArray(new BomItemVO[0]));
                    }
                    fullBills[0].setChildren(BomUseOrgVO.class, usevos);
                }
            }
         // liyf 2017-07-25 1.审批生成对应的使用组织的bom/工艺路线匹配规则
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
