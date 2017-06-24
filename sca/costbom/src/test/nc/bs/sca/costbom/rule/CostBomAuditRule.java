package nc.bs.sca.costbom.rule;

import nc.bs.sca.costbom.plugin.bpplugin.CostBomPluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.sca.costbom.entity.CostBomAggVO;

/**
 * �ɱ�Bom�����Ϣ����
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
            // ��ǰΪ�������ݣ���Ӵ����˵������Ϣ
            if (CostBomPluginPoint.INSERT.equals(this.point)) {
//                AuditUtil.addData(vo.getParent());
                vo.getParent().setStatus(VOStatus.NEW);
                CircularlyAccessibleValueObject[] itemVOs = vo.getAllChildrenVO();
                for (CircularlyAccessibleValueObject itemVO : itemVOs) {
                    itemVO.setStatus(VOStatus.NEW);
                }
            }
            // ��ǰΪ�޸ĵ��ݣ�����޸��˵������Ϣ
            else if (CostBomPluginPoint.UPDATE.equals(this.point)) {
                // �������ã����򲻻�־û������ݿ���-zhangshyb
                vo.getParent().setStatus(VOStatus.UPDATED);
                /** �����޸ĺ������Ϣ��Ч����AuditUtil.updateData(vo.getParent());<41��>��ͬ�� */
                // AuditInfoUtils.setUpdateAuditInfo(vo.getParent());
//                AuditUtil.updateData(vo.getParent());
            }
            else if (CostBomPluginPoint.PRICEUPDATE.equals(this.point)) {
                // ��VO״̬���³�UPDATED�����򲻻�־û�
                vo.getParentVO().setStatus(VOStatus.UPDATED);
                CircularlyAccessibleValueObject[] itemVOs = vo.getAllChildrenVO();
                for (CircularlyAccessibleValueObject itemVO : itemVOs) {
                    itemVO.setStatus(VOStatus.UPDATED);
                }
            }
        }
    }
}
