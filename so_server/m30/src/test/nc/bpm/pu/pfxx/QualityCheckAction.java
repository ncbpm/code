package nc.bpm.pu.pfxx;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.pu.m23.plugin.ArriveActionPlugInPoint;
import nc.impl.pu.m23.qc.action.rule.CalcCanCheckNumRule;
import nc.impl.pu.m23.qc.action.rule.ChkCanCheckRule;
import nc.impl.pu.m23.qc.action.rule.ChkReApplyAndDelChkDetailRule;
import nc.impl.pu.m23.qc.action.rule.FilterFreeChkItemRule;
import nc.impl.pu.m23.qc.action.rule.InsertArriveBbVoByStrictLevelRule;
import nc.impl.pu.m23.qc.action.rule.QualityCheckSendMsgRule;
import nc.impl.pu.m23.qc.action.rule.WriteBackArriveCheckNumRule;
import nc.impl.pu.m23.qc.action.rule.WriteNumRule;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.pubitf.qc.c001.pu.IPushSaveFor23;
import nc.pubitf.qc.c001.pu.ReturnObjectFor23;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pu.pub.util.AggVOUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.qc.pub.util.QCSysParamUtil;

/**
 * 到货单的质检对应的Actio
 *
 */
public class QualityCheckAction {

  public Object[] qualityCheck(ArriveVO[] bills, boolean isCheck) {
    BillTransferTool<ArriveVO> tool = new BillTransferTool<ArriveVO>(bills);
    ArriveVO[] vos = tool.getClientFullInfoBill();

    // 将前台传来的到货单表体进行整理，整理为key=表体行ID，value=表体的MAP形式。
    // 因为检验时，不一定会将所有的表体进行检验，所以需要对传过来的表体进行信息补全，而不是直接用全vo的表体。

    // 将全vo整理为key=pk，value=表体记录的形式。
    ArriveItemVO[] fullVoItems = null;
    Map<String, ArriveItemVO> arriveItemVoMap =
        new HashMap<String, ArriveItemVO>();
    for (int i = 0; i < vos.length; i++) {
      fullVoItems = vos[i].getBVO();
      for (int j = 0; j < fullVoItems.length; j++) {
        arriveItemVoMap.put(fullVoItems[j].getPk_arriveorder_b(),
            fullVoItems[j]);
      }
    }

    // 更新前台传来的数据
    ArriveItemVO[] billVoItems = null;
    for (int m = 0; m < bills.length; m++) {
      billVoItems = bills[m].getBVO();
      for (int n = 0; n < billVoItems.length; n++) {
        billVoItems[n] =
            arriveItemVoMap.get(billVoItems[n].getPk_arriveorder_b());
      }
      vos[m].setBVO(billVoItems);
    }

    AroundProcesser<ArriveVO> processer =
        new AroundProcesser<ArriveVO>(
            ArriveActionPlugInPoint.QualityCheckAction);
    this.addBeforeRule(processer, isCheck);
    processer.before(vos);
    ReturnObjectFor23 rof = this.invokeQCService(vos);
    this.addAfterRule(processer, rof);
    processer.after(vos);

    // 查询最新VO返回
    BillQuery<ArriveVO> query = new BillQuery<ArriveVO>(ArriveVO.class);
    ArriveVO[] newVOArray = query.query(AggVOUtil.getPrimaryKeys(vos));
    if (null != newVOArray) {
      int len = newVOArray.length;
      for (int i = 0; i < len; i++) {
        ArriveItemVO[] itemvos = newVOArray[i].getBVO();
        for (ArriveItemVO vo : itemvos) {
          UFDouble nchecknum =
              MathTool.sub(MathTool.sub(vo.getNnum(), vo.getNaccumchecknum()),
                  vo.getNaccumbacknum());
          vo.setNchecknum(nchecknum);
          vo.setNwillelignum(nchecknum);
          vo.setNwillnotelignum(UFDouble.ZERO_DBL);
        }
      }
    }

    // 由于又要保存返回对应vo，还要把接口传到这里的信息传到前台，只好这样了。
    Object[] objects = new Object[] {
      tool.getBillForToClient(newVOArray), rof
    };
    return objects;
  }

  private void addAfterRule(AroundProcesser<ArriveVO> processer,
      ReturnObjectFor23 rof) {
    // 判断是否重复报检，如果是，删除孙表数据
    processer.addAfterRule(new ChkReApplyAndDelChkDetailRule());
    // 质检成功后，取结果集的严格级别,如果为免检或暂停时,要在孙表插记录
    processer.addAfterRule(new InsertArriveBbVoByStrictLevelRule(rof));
    // 质检成功后，更新子表中合格数量、不合格数量
    processer.addAfterRule(new WriteBackArriveCheckNumRule(true));
    // 质检成功后，通知库管员
//    processer.addAfterRule(new QualityCheckSendMsgRule("2304"));
  }

  private void addBeforeRule(AroundProcesser<ArriveVO> processer,
      boolean isCheck) {
    // 检查是否可以报检
    processer.addBeforeRule(new ChkCanCheckRule());
    // 过滤掉表体中免检类物料行
    processer.addBeforeFinalRule(new FilterFreeChkItemRule(isCheck));
    // 计算报检数量
    processer.addBeforeRule(new CalcCanCheckNumRule());
    // 回写到货单
    processer.addBeforeRule(new WriteNumRule());
  }

  private ReturnObjectFor23 invokeQCService(ArriveVO[] bills) {
    if (bills == null || bills.length == 0) {
      return null;
    }
    try {
      if (!SysInitGroupQuery.isQCEnabled()
          || UFBoolean.FALSE.equals(ValueUtils.getUFBoolean(QCSysParamUtil
              .getINI01(bills[0].getHVO().getPk_org())))) {
        return null;
      }

      // 调用质检服务
      ReturnObjectFor23 rof =
          NCLocator.getInstance().lookup(IPushSaveFor23.class).pushSave(bills);
      return rof;
    }
    catch (Exception ex) {
      ExceptionUtils.wrappException(ex);
    }
    return null;
  }
}
