/**
 * $文件说明$
 * 
 * @author chennn
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-3-31 下午04:50:56
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
 * @time 2010-3-31 下午04:50:56
 */
public class UpdateBP implements IUpdateBP<FinProdInVO>,
IUpdateRuleProvider<FinProdInVO> {

  @Override
  public void addAfterRule(FinProdInVO[] vos, FinProdInVO[] originVOs,
      CompareAroundProcesser<FinProdInVO> processor) {
    processor.addAfterRule(new RewriteQCUpdateRule<FinProdInVO>());
    /**
     * 放在现存量更新规则（OnhandAfterUpdate）之后，因为现存量更新时会调用预留接口，预留接口会回写采购订单，
     * 如果入库数量=订单数量，订单会自动入库关闭，入库关闭会解除预留，导致后续操作错误
     */
    ((ICCompareAroundProcesser<FinProdInVO>) processor).addAfterRuleAt(
        new UpdateRewriteMMPAC(), AtpAfterUpdate.class);
    
    // 回写独立计划需求
//    processor.addAfterRule(new UpdateRewriteMMDP());
	// 2017-07-15 liyf
	// 1.产成品入库如果是按照销售订单生成的销售订单，且入库的物料是销售订单上物料，则改物料+批次动预留给该销售订单
	processor.addAfterRule(new AfterSignRuleForReserveProcess());

  }

  @Override
  public void addBeforeRule(FinProdInVO[] vos, FinProdInVO[] originVOs,
      CompareAroundProcesser<FinProdInVO> processor) {
    //生产库存组织与生产仓库关系检查
    processor.addBeforeRule(new ProdInWarehouseAttriCheck());
    processor.addBeforeRule(new CprowarehouseCheck());
 // 利润中心校验规则
    processor.addBeforeRule(new CheckCliabilityValue<FinProdInVO>(MetaNameConst.CLIABILITYOID,MetaNameConst.CIOLIABILITYOID));
    processor.addBeforeRule(new UpdateBomCheck());
  }

  @Override
  public FinProdInVO[] update(FinProdInVO[] vos, FinProdInVO[] originVOs) {
    return new UpdateBPTemplate<FinProdInVO>(BPPlugInPoint.UpdateBP, this)
    .update(vos, originVOs);
  }

}
