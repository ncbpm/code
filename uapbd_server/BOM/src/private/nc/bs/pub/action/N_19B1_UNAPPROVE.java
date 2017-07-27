package nc.bs.pub.action;

//import nc.bs.ecn.ecr.rule.EcrBillStatusGoingonCheckRule;
//import nc.bs.ecn.ecr.rule.EcrBillStatusUnApproveCheckRule;
//import nc.bs.ecn.ecr.rule.EcrChangeVOStatus2UpdateRule;
//import nc.bs.ecn.ecr.rule.EcrHasCreatEcoRule;
import nc.bs.bd.bom.bom0202.rule.AutoToBmRtRuleForRainbowUnAudit;
import nc.bs.bd.bom.bom0202.rule.BomBillStatusGoingonCheckRule;
import nc.bs.bd.bom.bom0202.rule.BomBillStatusUnApproveCheckRule;
import nc.bs.bd.bom.bom0202.rule.BomChangeVOStatus2UpdateRule;
import nc.bs.bd.bom.bom0202.rule.EcnCheckRuleForUnaudit;
import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.ICompareRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.bd.bom.bom0202.IBomBillMaintainService;
import nc.vo.bd.bom.bom0202.entity.AggBomVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
//import nc.itf.ecn.ecr.IEcrMaintain;
//import nc.vo.ecn.ecr.entity.AggEcrVO;

/**
 * <b> BOM维护弃审操作 </b>
 * <p>
 * 对BOM维护进行弃审
 * </p>
 * 创建日期:2014-10-8
 * 
 * @author:wanglg
 */
public class N_19B1_UNAPPROVE extends AbstractPfAction<AggBomVO> {

    /**
     * 获得回写接口
     * 
     * @return 回写接口
     */
    public IBomBillMaintainService getMaintainService() {
        return NCLocator.getInstance().lookup(IBomBillMaintainService.class);
    }

    @Override
    protected CompareAroundProcesser<AggBomVO> getCompareAroundProcesserWithRules(Object userObj) {
        CompareAroundProcesser<AggBomVO> processer = new CompareAroundProcesser<AggBomVO>(null);
        // 在此处添加审核前规则
        this.addBeforeRule(processer);
        this.addAfterRule(processer);
        return processer;
    }

    private void addAfterRule(CompareAroundProcesser<AggBomVO> processer) {
		// TODO 自动生成的方法存根
    	//3. .审批生成对应的使用组织的bom/工艺路线匹配规则，取消审批，删除对应的使用组织的使用规则
    	processer.addAfterRule(new AutoToBmRtRuleForRainbowUnAudit());
	}

	@Override
    protected AggBomVO[] processBP(Object userObj, AggBomVO[] clientFullVOs, AggBomVO[] originBills) {
        AggBomVO[] returnvos = null;
        try {
            returnvos = this.getMaintainService().unauditBom(clientFullVOs, originBills);
        }
        catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        return returnvos;
    }

    /**
     * 添加前规则
     * 
     * @param processer
     */
    private void addBeforeRule(CompareAroundProcesser<AggBomVO> processer) {
        // 检查自由态的单据
        IRule<AggBomVO> freeStatusCheckRule = new BomBillStatusUnApproveCheckRule();
        processer.addBeforeRule(freeStatusCheckRule);
        // 检查是否启用ecn
        IRule<AggBomVO> ecnCheckRule = new EcnCheckRuleForUnaudit();
        processer.addBeforeRule(ecnCheckRule);
        // 单据状态检查
        ICompareRule<AggBomVO> passingCheckRule = new BomBillStatusGoingonCheckRule();
        processer.addBeforeRule(passingCheckRule);
        //
        // 更改单据状态
        IRule<AggBomVO> changeVOStatus = new BomChangeVOStatus2UpdateRule();
        processer.addBeforeRule(changeVOStatus);

    }
}
