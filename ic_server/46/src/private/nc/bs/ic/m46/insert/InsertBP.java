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
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author chennn
 * @time 2010-3-31 下午04:50:20
 */
public class InsertBP implements IInsertBP<FinProdInVO>,
IInsertRuleProvider<FinProdInVO> {

  @Override
  public void addAfterRule(FinProdInVO[] vos,
      AroundProcesser<FinProdInVO> processor) {
    processor.addAfterRule(new RewriteQCInsertRule<FinProdInVO>());
    /**
     * 放在现存量更新规则（OnhandAfterUpdate）之后，因为现存量更新时会调用预留接口，预留接口会回写采购订单，
     * 如果入库数量=订单数量，订单会自动入库关闭，入库关闭会解除预留，导致后续操作错误
     */
    ((ICAroundProcesser<FinProdInVO>) processor).addAfterRuleAt(
        new InsertRewriteMMPAC(), AtpAfterUpdate.class);
   
    // 回写独立计划需求
    processor.addAfterRule(new InsertRewriteMMDP());
    //如果来源是 销售订单，自动生成 预留单
    processor.addAfterRule(new AddReserveBillRule());
  }

  @Override
  public void addBeforeRule(FinProdInVO[] vos,
      AroundProcesser<FinProdInVO> processor) {
    //生产库存组织与生产仓库关系检查
    processor.addBeforeRule(new ProdInWarehouseAttriCheck());
    processor.addBeforeRule(new CprowarehouseCheck());
    processor.addBeforeRule(new InsertBomCheck());
    // 利润中心校验规则
    processor.addBeforeRule(new CheckCliabilityValue<FinProdInVO>(MetaNameConst.CLIABILITYOID,MetaNameConst.CIOLIABILITYOID));
    // 产成品入库单填充失效日期
    processor.addBeforeRule(new ValidateFillRule());
  }

  @Override
  public FinProdInVO[] insert(FinProdInVO[] bills) {
    InsertBPTemplate<FinProdInVO> insertBP =
      new InsertBPTemplate<FinProdInVO>(BPPlugInPoint.InsertBP, this);
    return insertBP.insert(bills);
  }

}
