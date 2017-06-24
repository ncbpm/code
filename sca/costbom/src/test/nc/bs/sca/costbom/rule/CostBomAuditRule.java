package nc.bs.sca.costbom.rule;

import nc.bs.sca.costbom.plugin.bpplugin.CostBomPluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.sca.costbom.entity.CostBomAggVO;

/**
 * 成本Bom审计信息规则
 * 
 * @author leixjc
 */
public class CostBomAuditRule implements IRule<CostBomAggVO> {
    private CostBomPluginPoint point;

    public CostBomAuditRule(CostBomPluginPoint point) {
        this.point = point;
    }

    @Override
    public void process(CostBomAggVO[] vos) {
        for (CostBomAggVO vo : vos) {
            // 当前为新增单据，添加创建人等审计信息
            if (CostBomPluginPoint.INSERT.equals(this.point)) {
//                AuditUtil.addData(vo.getParent());
                vo.getParent().setStatus(VOStatus.NEW);
                CircularlyAccessibleValueObject[] itemVOs = vo.getAllChildrenVO();
                for (CircularlyAccessibleValueObject itemVO : itemVOs) {
                    itemVO.setStatus(VOStatus.NEW);
                }
            }
            // 当前为修改单据，添加修改人等审计信息
            else if (CostBomPluginPoint.UPDATE.equals(this.point)) {
                // 必须设置，否则不会持久化到数据库中-zhangshyb
                vo.getParent().setStatus(VOStatus.UPDATED);
                /** 设置修改后审计信息（效果与AuditUtil.updateData(vo.getParent());<41行>相同） */
                // AuditInfoUtils.setUpdateAuditInfo(vo.getParent());
//                AuditUtil.updateData(vo.getParent());
            }
            else if (CostBomPluginPoint.PRICEUPDATE.equals(this.point)) {
                // 把VO状态更新成UPDATED，否则不会持久化
                vo.getParentVO().setStatus(VOStatus.UPDATED);
                CircularlyAccessibleValueObject[] itemVOs = vo.getAllChildrenVO();
                for (CircularlyAccessibleValueObject itemVO : itemVOs) {
                    itemVO.setStatus(VOStatus.UPDATED);
                }
            }
        }
    }
}
