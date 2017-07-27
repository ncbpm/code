/**
 * $�ļ�˵��$
 * 
 * @author chennn
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-3-31 ����04:50:56
 */
package nc.bs.ic.m46.update;

import nc.bs.ic.general.insert.rule.before.CheckCliabilityValue;
import nc.bs.ic.general.rule.after.AtpAfterUpdate;
import nc.bs.ic.general.update.IUpdateBP;
import nc.bs.ic.general.update.UpdateBPTemplate;
import nc.bs.ic.general.update.rule.after.RewriteQCUpdateRule;
import nc.bs.ic.m46.base.BPPlugInPoint;
import nc.bs.ic.m46.base.ProdInWarehouseAttriCheck;
import nc.bs.ic.m46.rule.CprowarehouseCheck;
import nc.bs.ic.m46.sign.rule.AfterSignRuleForReserveProcess;
import nc.bs.ic.m46.update.rule.UpdateBomCheck;
import nc.bs.ic.m46.update.rule.UpdateRewriteMMDP;
import nc.bs.ic.m46.update.rule.UpdateRewriteMMPAC;
import nc.bs.ic.pub.base.ICCompareAroundProcesser;
import nc.bs.ic.pub.base.IUpdateRuleProvider;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.ic.general.define.MetaNameConst;
import nc.vo.ic.m46.entity.FinProdInVO;

/**
 * <p>
 * <b>������Ҫ������¹��ܣ�</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author chennn
 * @time 2010-3-31 ����04:50:56
 */
public class UpdateBP implements IUpdateBP<FinProdInVO>,
IUpdateRuleProvider<FinProdInVO> {

  @Override
  public void addAfterRule(FinProdInVO[] vos, FinProdInVO[] originVOs,
      CompareAroundProcesser<FinProdInVO> processor) {
    processor.addAfterRule(new RewriteQCUpdateRule<FinProdInVO>());
    /**
     * �����ִ������¹���OnhandAfterUpdate��֮����Ϊ�ִ�������ʱ�����Ԥ���ӿڣ�Ԥ���ӿڻ��д�ɹ�������
     * ����������=�����������������Զ����رգ����رջ���Ԥ�������º�����������
     */
    ((ICCompareAroundProcesser<FinProdInVO>) processor).addAfterRuleAt(
        new UpdateRewriteMMPAC(), AtpAfterUpdate.class);
    
    // ��д�����ƻ�����
//    processor.addAfterRule(new UpdateRewriteMMDP());
	// 2017-07-15 liyf
	// 1.����Ʒ�������ǰ������۶������ɵ����۶��������������������۶��������ϣ��������+���ζ�Ԥ���������۶���
	processor.addAfterRule(new AfterSignRuleForReserveProcess());

  }

  @Override
  public void addBeforeRule(FinProdInVO[] vos, FinProdInVO[] originVOs,
      CompareAroundProcesser<FinProdInVO> processor) {
    //���������֯�������ֿ��ϵ���
    processor.addBeforeRule(new ProdInWarehouseAttriCheck());
    processor.addBeforeRule(new CprowarehouseCheck());
 // ��������У�����
    processor.addBeforeRule(new CheckCliabilityValue<FinProdInVO>(MetaNameConst.CLIABILITYOID,MetaNameConst.CIOLIABILITYOID));
    processor.addBeforeRule(new UpdateBomCheck());
  }

  @Override
  public FinProdInVO[] update(FinProdInVO[] vos, FinProdInVO[] originVOs) {
    return new UpdateBPTemplate<FinProdInVO>(BPPlugInPoint.UpdateBP, this)
    .update(vos, originVOs);
  }

}
