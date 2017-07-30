package nc.bs.pub.action;

import nc.bs.bd.bom.bom0202.plugin.bpplugin.BomPluginPoint;
import nc.bs.bd.bom.bom0202.rule.AutoToBmRtRuleForRainbowAudit;
import nc.bs.bd.bom.bom0202.rule.BomBillStatusApproveCheckRule;
import nc.bs.bd.bom.bom0202.rule.BomChangeVOStatus2UpdateRule;
import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.bd.bom.bom0202.IBomBillMaintainService;
import nc.vo.bd.bom.bom0202.entity.AggBomVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> bom审批脚本 </b>
 * <p>
 * 完成后台审批流程
 * </p>
 * 创建日期:2014-10-8
 * 
 * @author:wanglg
 */
public class N_19B1_APPROVE extends AbstractPfAction<AggBomVO> {

    /**
     * 获得数据库回写接口
     * 
     * @return 回写接口
     */
    public IBomBillMaintainService getUpdateService() {
        return NCLocator.getInstance().lookup(IBomBillMaintainService.class);
    }

    @Override
    protected CompareAroundProcesser<AggBomVO> getCompareAroundProcesserWithRules(Object userObj) {
        CompareAroundProcesser<AggBomVO> processer = new CompareAroundProcesser<AggBomVO>(BomPluginPoint.APPROVE);
        // 在此处添加审核前规则
        this.addBeforeRule(processer);
        // 在此处添加审核后规则
        this.addAfterRule(processer);

        return processer;
    }

    @Override
    protected AggBomVO[] processBP(Object userObj, AggBomVO[] clientFullVOs, AggBomVO[] originBills) {
        AggBomVO[] returnbill = null;
        try {
            returnbill = this.getUpdateService().auditBom(clientFullVOs, originBills);
        }
        catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return returnbill;
    }

    /**
     * 添加前规则
     * 
     * @param processer
     */
    private void addBeforeRule(CompareAroundProcesser<AggBomVO> processer) {
        IRule<AggBomVO> approveStatusCheckRule = new BomBillStatusApproveCheckRule();
        processer.addBeforeRule(approveStatusCheckRule);

        // VO状态修改
        IRule<AggBomVO> voStatusRule = new BomChangeVOStatus2UpdateRule();
        processer.addBeforeRule(voStatusRule);

    }

    /**
     * 添加后规则
     * 
     * @param processer
     */
    private void addAfterRule(CompareAroundProcesser<AggBomVO> processer) {
    	// liyf 2017-07-25 1.审批生成对应的使用组织的bom/工艺路线匹配规则
    	processer.addAfterRule(new AutoToBmRtRuleForRainbowAudit());

    }
}
