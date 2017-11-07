package nc.bs.to.m5f.biz;

import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.bill.CombineBill;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.log.TimeLog;
import nc.vo.scmpub.res.billtype.ARAPBillType;
import nc.vo.scmpub.res.billtype.TOBillType;
import nc.vo.to.m5f.entity.STranFinInAggVO;
import nc.vo.to.m5f.entity.STranFinInHeadVO;
import nc.vo.to.m5f.entity.STranFinInItemVO;
import nc.vo.to.m5f.entity.STranFinOutAggVO;
import nc.vo.to.m5f.entity.STranFinOutItemVO;

import nc.itf.scmpub.reference.arap.ArapServicesForTOUtil;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;

import nc.bs.ml.NCLangResOnserver;

import nc.impl.pubapp.bd.userdef.UserDefCheckUtils;

/**
 * 结算清单传确认应付
 * 
 * @version 6.0
 * @author panql
 * @time 2010-1-27 上午10:45:12
 */
public class SettlePushSureAP {

  private AggPayableBillVO[] chg4556ToF1(STranFinInAggVO[] bills) {

    CombineBill<STranFinInAggVO> cb = new CombineBill<STranFinInAggVO>();
    // TODO 合单字段没有维护
    String[] combineKeys =
        new String[] {
          STranFinInHeadVO.PK_ORG, STranFinInHeadVO.COUTFIORGID,
          STranFinInHeadVO.CCURRENCYID, STranFinInHeadVO.CSLISTID
        };
    for (String itemKey : combineKeys) {
      cb.appendKey(itemKey);
    }
    STranFinInAggVO[] sourceVOs = cb.combine(bills);

    // 调用4556->F1 VO交换
    this.copyVstdefToDef(bills);
    AggPayableBillVO[] arrReturn = null;
    try {
      arrReturn =
          PfServiceScmUtil.exeVOChangeByBillItfDef(
              TOBillType.SettleListIn.getCode(),
              ARAPBillType.PayAbleOrder.getCode(), sourceVOs);
      UserDefCheckUtils.check(arrReturn, new String[] {
        "vdef", "vbdef"
      }, new Class[] {
        PayableBillVO.class, PayableBillItemVO.class
      });
    }
    catch (Exception e) {
      ExceptionUtils.wrappException(e);
    }
    return arrReturn;
  }

  private AggPayableBillVO[] pushAPProc(STranFinInAggVO[] stfvos) {
    AggPayableBillVO[] apbills = this.chg4556ToF1(stfvos);
    return ArapServicesForTOUtil.saveSureAPFor5F(apbills);
  }

  public AggPayableBillVO[] pushSureAP(STranFinInAggVO[] bills) {
    AggPayableBillVO[] apbills = null;

    TimeLog.logStart();
    // 校验主含税单价和价税合计不能空或者为0
    this.checkMny(bills);
    apbills = this.pushAPProc(bills);
    TimeLog.info(" 组织传确认应付的数据并传应收"); /*-=notranslate=-*/

    return apbills;
  }

  private void checkMny(STranFinInAggVO[] stvos) {
    for (STranFinInAggVO vo : stvos) {
      STranFinInItemVO[] items = vo.getItemVOs();
      for (STranFinInItemVO item : items) {
        UFDouble mny = item.getNorigtaxmny();
        UFDouble price = item.getNorigtaxprice();
        if (null == mny || mny.equals(UFDouble.ZERO_DBL)) {
          String msg =
              NCLangResOnserver.getInstance().getStrByID("4009003_0",
                  "04009003-0406", null, new String[] {
                    item.getVstcode(), item.getVstrowno()
                  })/*价税合计为空或者为0,不能传财务 \n单号：{0}\n行号：{1}*/;
          ExceptionUtils.wrappBusinessException(msg);
        }
        if (null == price || price.equals(UFDouble.ZERO_DBL)) {
          String msg =
              NCLangResOnserver.getInstance().getStrByID("4009003_0",
                  "04009003-0407", null, new String[] {
                    item.getVstcode(), item.getVstrowno()
                  })/*主含税单价为空或者为0,不能传财务 \n单号：{0}\n行号：{1}*/;
          ExceptionUtils.wrappBusinessException(msg);
        }
      }
    }
  }
  private void copyVstdefToDef(STranFinInAggVO[] bills) {

		for (int i = 0; i < bills.length; i++) {
			STranFinInItemVO[] fovo= bills[i].getItemVOs();
			for (int j = 0; j < fovo.length; j++) {
				fovo[j].setVbdef1(fovo[j].getVstbdef1());
				fovo[j].setVbdef2(fovo[j].getVstbdef2());
				fovo[j].setVbdef3(fovo[j].getVstbdef3());
				fovo[j].setVbdef4(fovo[j].getVstbdef4());
				fovo[j].setVbdef5(fovo[j].getVstbdef5());
				fovo[j].setVbdef6(fovo[j].getVstbdef6());
				fovo[j].setVbdef7(fovo[j].getVstbdef7());
				fovo[j].setVbdef8(fovo[j].getVstbdef8());
				fovo[j].setVbdef9(fovo[j].getVstbdef9());
				fovo[j].setVbdef10(fovo[j].getVstbdef10());
				fovo[j].setVbdef11(fovo[j].getVstbdef11());
				fovo[j].setVbdef12(fovo[j].getVstbdef12());
				fovo[j].setVbdef13(fovo[j].getVstbdef13());
				fovo[j].setVbdef14(fovo[j].getVstbdef14());
				fovo[j].setVbdef15(fovo[j].getVstbdef15());
				fovo[j].setVbdef16(fovo[j].getVstbdef16());
				fovo[j].setVbdef17(fovo[j].getVstbdef17());
				fovo[j].setVbdef18(fovo[j].getVstbdef18());
				fovo[j].setVbdef19(fovo[j].getVstbdef19());
				fovo[j].setVbdef20(fovo[j].getVstbdef20());
			}
		}
	}
}
