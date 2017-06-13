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
 * ���������ʼ��Ӧ��Actio
 *
 */
public class QualityCheckAction {

  public Object[] qualityCheck(ArriveVO[] bills, boolean isCheck) {
    BillTransferTool<ArriveVO> tool = new BillTransferTool<ArriveVO>(bills);
    ArriveVO[] vos = tool.getClientFullInfoBill();

    // ��ǰ̨�����ĵ��������������������Ϊkey=������ID��value=�����MAP��ʽ��
    // ��Ϊ����ʱ����һ���Ὣ���еı�����м��飬������Ҫ�Դ������ı��������Ϣ��ȫ��������ֱ����ȫvo�ı��塣

    // ��ȫvo����Ϊkey=pk��value=�����¼����ʽ��
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

    // ����ǰ̨����������
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

    // ��ѯ����VO����
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

    // ������Ҫ���淵�ض�Ӧvo����Ҫ�ѽӿڴ����������Ϣ����ǰ̨��ֻ�������ˡ�
    Object[] objects = new Object[] {
      tool.getBillForToClient(newVOArray), rof
    };
    return objects;
  }

  private void addAfterRule(AroundProcesser<ArriveVO> processer,
      ReturnObjectFor23 rof) {
    // �ж��Ƿ��ظ����죬����ǣ�ɾ���������
    processer.addAfterRule(new ChkReApplyAndDelChkDetailRule());
    // �ʼ�ɹ���ȡ��������ϸ񼶱�,���Ϊ������ͣʱ,Ҫ�������¼
    processer.addAfterRule(new InsertArriveBbVoByStrictLevelRule(rof));
    // �ʼ�ɹ��󣬸����ӱ��кϸ����������ϸ�����
    processer.addAfterRule(new WriteBackArriveCheckNumRule(true));
    // �ʼ�ɹ���֪ͨ���Ա
//    processer.addAfterRule(new QualityCheckSendMsgRule("2304"));
  }

  private void addBeforeRule(AroundProcesser<ArriveVO> processer,
      boolean isCheck) {
    // ����Ƿ���Ա���
    processer.addBeforeRule(new ChkCanCheckRule());
    // ���˵������������������
    processer.addBeforeFinalRule(new FilterFreeChkItemRule(isCheck));
    // ���㱨������
    processer.addBeforeRule(new CalcCanCheckNumRule());
    // ��д������
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

      // �����ʼ����
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
