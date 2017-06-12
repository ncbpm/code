package nc.ui.pu.pub.action;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.pub.power.PowerCheckUtils;
import nc.ui.pubapp.uif2app.actions.EditAction;
import nc.ui.scmpub.action.SCMActionInitializer;
import nc.vo.pu.pub.constant.PUQueryConst;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pub.power.PowerActionEnum;
import nc.vo.scmpub.res.SCMActionCode;

/**
 * @since 6.0
 * @version 2011-4-25 ÏÂÎç05:49:43
 * @author wuxla
 */

public class PUReviseAction extends EditAction {

  private static final long serialVersionUID = 3666193108323100989L;

  private String permissioncode;

  private boolean powercheck;

  public PUReviseAction() {
    super();
    SCMActionInitializer.initializeAction(this, SCMActionCode.SCM_REVISE);
  }

  @Override
  public void doAction(ActionEvent e) throws Exception {
    IBill bill = (IBill) this.getModel().getSelectedData();
//    if (this.isPowercheck()) {
//      PowerCheckUtils.checkHasPermission(new IBill[] {
//        bill
//      }, this.getPermissioncode(), PowerActionEnum.REVISE.getActioncode(),
//          PUQueryConst.VBILLCODE);
//    }
    super.doAction(e);
  }

  public String getPermissioncode() {
    return this.permissioncode;
  }

  public boolean isPowercheck() {
    return this.powercheck;
  }

  public void setPermissioncode(String permissioncode) {
    this.permissioncode = permissioncode;
  }

  public void setPowercheck(boolean powercheck) {
    this.powercheck = powercheck;
  }
}
