/**
 * $文件说明$
 * 
 * @author wuxla
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-7-22 上午08:29:25
 */
package nc.impl.pu.m422x.action;

import nc.bs.pu.m422x.maintain.rule.save.SysBpmRule;
import nc.bs.pu.m422x.plugin.StoreReqAppPluginPoint;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.scmpub.pf.PfParameterUtil;
import nc.impl.pu.m422x.action.rule.approve.ApproveValidateRule;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.pu.m422x.entity.StoreReqAppVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.apache.commons.lang.ArrayUtils;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>审核
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author wuxla
 * @time 2010-7-22 上午08:29:25
 */
public class StoreReqAppApproveAction {

  public StoreReqAppVO[] approve(StoreReqAppVO[] vos, AbstractCompiler2 script) {
    if (ArrayUtils.isEmpty(vos) || (null == script)) {
      return null;
    }

    PfParameterUtil<StoreReqAppVO> util =
        new PfParameterUtil<StoreReqAppVO>(script.getPfParameterVO(), vos);
    StoreReqAppVO[] originBills = util.getOrginBills();
    StoreReqAppVO[] clientBills = util.getClientFullInfoBill();

    AroundProcesser<StoreReqAppVO> processer =
        new AroundProcesser<StoreReqAppVO>(StoreReqAppPluginPoint.APPROVE);
    this.addRule(processer);

    processer.before(clientBills);

    try {
      script.procFlowBacth(script.getPfParameterVO());
    }
    catch (Exception e) {
      ExceptionUtils.wrappException(e);
    }

    BillUpdate<StoreReqAppVO> update = new BillUpdate<StoreReqAppVO>();
    StoreReqAppVO[] returnVos = update.update(clientBills, originBills);

    processer.after(returnVos);

    return returnVos;
  }

  private void addRule(AroundProcesser<StoreReqAppVO> processer) {
    // 状态检查
    processer.addBeforeRule(new ApproveValidateRule());
    //2017-06-26 同步到BPM
    processer.addAfterRule(new SysBpmRule());
  }
}
