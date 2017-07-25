package nc.bs.bd.bom.bom0202.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.util.mmf.framework.base.MMArrayUtil;
import nc.vo.bd.bom.bom0202.entity.AggBomVO;
import nc.vo.bd.bom.bom0202.entity.BomVO;
import nc.vo.bd.bom.bom0202.enumeration.FBomBillstatusEnum;
import nc.vo.bd.bom.bom0202.enumeration.VersionTypeEnum;
import nc.vo.pub.VOStatus;

/**
3. .审批生成对应的使用组织的bom/工艺路线匹配规则，取消审批，删除对应的使用组织的使用规则
4.  分配生成分配的组织的bom/工艺路线匹配规则，取消分配删除取消分配的bom/工艺路线匹配规则
5. 按照组织+物料+版本号控制唯一性
 * @author liyf
 *
 */

public class AutoToBmRtRuleForRainbowAudit implements IRule<AggBomVO> {

    @Override
    public void process(AggBomVO[] vos) {
        if (MMArrayUtil.isEmpty(vos)) {
            return;
        }
        for (AggBomVO aggBomVO : vos) {
            BomVO head = (BomVO) aggBomVO.getParentVO();
            // 取消审批后，要把bom版本类型置为无效(单据状态为审批通过或不通过)
            if (head.getFbillstatus().intValue() == FBomBillstatusEnum.APPROVE.toIntValue()
                    || head.getFbillstatus().intValue() == FBomBillstatusEnum.NOPASS.toIntValue()) {
                head.setHfversiontype(VersionTypeEnum.UNAVAILABLE.toIntValue());
                // head.setHbdefault(UFBoolean.FALSE);
            }
            aggBomVO.getParentVO().setStatus(VOStatus.UPDATED);
        }

    }

}
