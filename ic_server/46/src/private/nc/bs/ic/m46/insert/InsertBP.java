package nc.bs.ic.m46.insert;

import nc.bs.ic.general.insert.IInsertBP;
import nc.bs.ic.general.insert.InsertBPTemplate;
import nc.bs.ic.general.insert.rule.after.RewriteQCInsertRule;
import nc.bs.ic.general.insert.rule.before.CheckCliabilityValue;
import nc.bs.ic.general.rule.after.AtpAfterUpdate;
import nc.bs.ic.m46.base.BPPlugInPoint;
import nc.bs.ic.m46.base.ProdInWarehouseAttriCheck;
import nc.bs.ic.m46.insert.rule.AddReserveBillRule;
import nc.bs.ic.m46.insert.rule.InsertBomCheck;
import nc.bs.ic.m46.insert.rule.InsertRewriteMMDP;
import nc.bs.ic.m46.insert.rule.InsertRewriteMMPAC;
import nc.bs.ic.m46.insert.rule.ValidateFillRule;
import nc.bs.ic.m46.rule.CprowarehouseCheck;
import nc.bs.ic.pub.base.ICAroundProcesser;
import nc.bs.ic.pub.base.IInsertRuleProvider;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
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
 * @time 2010-3-31 ����04:50:20
 */
public class InsertBP implements IInsertBP<FinProdInVO>,
IInsertRuleProvider<FinProdInVO> {

  @Override
  public void addAfterRule(FinProdInVO[] vos,
      AroundProcesser<FinProdInVO> processor) {
    processor.addAfterRule(new RewriteQCInsertRule<FinProdInVO>());
    /**
     * �����ִ������¹���OnhandAfterUpdate��֮����Ϊ�ִ�������ʱ�����Ԥ���ӿڣ�Ԥ���ӿڻ��д�ɹ�������
     * ����������=�����������������Զ����رգ����رջ���Ԥ�������º�����������
     */
    ((ICAroundProcesser<FinProdInVO>) processor).addAfterRuleAt(
        new InsertRewriteMMPAC(), AtpAfterUpdate.class);
   
    // ��д�����ƻ�����
    processor.addAfterRule(new InsertRewriteMMDP());
    //�����Դ�� ���۶������Զ����� Ԥ����
    processor.addAfterRule(new AddReserveBillRule());
  }

  @Override
  public void addBeforeRule(FinProdInVO[] vos,
      AroundProcesser<FinProdInVO> processor) {
    //���������֯�������ֿ��ϵ���
    processor.addBeforeRule(new ProdInWarehouseAttriCheck());
    processor.addBeforeRule(new CprowarehouseCheck());
    processor.addBeforeRule(new InsertBomCheck());
    // ��������У�����
    processor.addBeforeRule(new CheckCliabilityValue<FinProdInVO>(MetaNameConst.CLIABILITYOID,MetaNameConst.CIOLIABILITYOID));
    // ����Ʒ��ⵥ���ʧЧ����
    processor.addBeforeRule(new ValidateFillRule());
  }

  @Override
  public FinProdInVO[] insert(FinProdInVO[] bills) {
    InsertBPTemplate<FinProdInVO> insertBP =
      new InsertBPTemplate<FinProdInVO>(BPPlugInPoint.InsertBP, this);
    return insertBP.insert(bills);
  }

}
