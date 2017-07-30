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
 * <b> BOMά��������� </b>
 * <p>
 * ��BOMά����������
 * </p>
 * ��������:2014-10-8
 * 
 * @author:wanglg
 */
public class N_19B1_UNAPPROVE extends AbstractPfAction<AggBomVO> {

    /**
     * ��û�д�ӿ�
     * 
     * @return ��д�ӿ�
     */
    public IBomBillMaintainService getMaintainService() {
        return NCLocator.getInstance().lookup(IBomBillMaintainService.class);
    }

    @Override
    protected CompareAroundProcesser<AggBomVO> getCompareAroundProcesserWithRules(Object userObj) {
        CompareAroundProcesser<AggBomVO> processer = new CompareAroundProcesser<AggBomVO>(null);
        // �ڴ˴�������ǰ����
        this.addBeforeRule(processer);
        this.addAfterRule(processer);
        return processer;
    }

    private void addAfterRule(CompareAroundProcesser<AggBomVO> processer) {
		// TODO �Զ����ɵķ������
    	//3. .�������ɶ�Ӧ��ʹ����֯��bom/����·��ƥ�����ȡ��������ɾ����Ӧ��ʹ����֯��ʹ�ù���
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
     * ���ǰ����
     * 
     * @param processer
     */
    private void addBeforeRule(CompareAroundProcesser<AggBomVO> processer) {
        // �������̬�ĵ���
        IRule<AggBomVO> freeStatusCheckRule = new BomBillStatusUnApproveCheckRule();
        processer.addBeforeRule(freeStatusCheckRule);
        // ����Ƿ�����ecn
        IRule<AggBomVO> ecnCheckRule = new EcnCheckRuleForUnaudit();
        processer.addBeforeRule(ecnCheckRule);
        // ����״̬���
        ICompareRule<AggBomVO> passingCheckRule = new BomBillStatusGoingonCheckRule();
        processer.addBeforeRule(passingCheckRule);
        //
        // ���ĵ���״̬
        IRule<AggBomVO> changeVOStatus = new BomChangeVOStatus2UpdateRule();
        processer.addBeforeRule(changeVOStatus);

    }
}
