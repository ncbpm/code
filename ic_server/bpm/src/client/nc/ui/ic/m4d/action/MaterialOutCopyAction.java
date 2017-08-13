package nc.ui.ic.m4d.action;

import nc.ui.ic.general.action.GeneralCopyAction;
import nc.ui.uif2.UIState;
import nc.vo.ic.general.define.MetaNameConst;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

public class MaterialOutCopyAction extends GeneralCopyAction {
  private static final long serialVersionUID = 3195741128498566833L; 
  
  @Override
	protected String[] addClearHeadKeys() {
		// TODO 自动生成的方法存根
		return new String[] {
		      ICPubMetaNameConst.VBILLCODE, MetaNameConst.CGENERALHID,
		      ICPubMetaNameConst.CREATOR, ICPubMetaNameConst.OPERATOR,
		      ICPubMetaNameConst.CREATIONTIME, ICPubMetaNameConst.APPROVER,
		      ICPubMetaNameConst.TAUDITTIME, ICPubMetaNameConst.TS,
		      ICPubMetaNameConst.DMAKEDATE, MetaNameConst.TSOURCEHEADTS,
		      ICPubMetaNameConst.DBILLDATE, ICPubMetaNameConst.MODIFIER,
		      ICPubMetaNameConst.MODIFIEDTIME,
		      "vdef20",  //五金卡主键
		};
	}

@Override
  protected String[] addClearBodyKeys() {
    return new String[] {
        MetaNameConst.CWORKORDERHID, MetaNameConst.CWORKORDERBID,
        MetaNameConst.CWORKORDERTRANTYPE, MetaNameConst.CWORKORDERCODE,
        MetaNameConst.CWORKORDERROWNO,ICPubMetaNameConst.NACCUMVMINUM,
        
        MetaNameConst.VPRODUCTBATCH,MetaNameConst.CPICKMHID,
        MetaNameConst.CPICKMBID,MetaNameConst.CPICKMTRANSTYPE,
        MetaNameConst.CPICKMCODE,MetaNameConst.CPICKMROWNO,
        
        MetaNameConst.CSOURCEBILLBID,MetaNameConst.CSOURCEBILLHID,
        MetaNameConst.CSOURCETRANSTYPE,MetaNameConst.CSOURCETYPE,
        MetaNameConst.VSOURCEBILLCODE,MetaNameConst.VSOURCEROWNO,
        MetaNameConst.VSRC2BILLCODE,MetaNameConst.VSRC2BILLROWNO,
        
        MetaNameConst.CFIRSTBILLBID,MetaNameConst.CFIRSTBILLHID,
        MetaNameConst.CFIRSTTRANSTYPE,MetaNameConst.CFIRSTTYPE,
        MetaNameConst.VFIRSTBILLCODE,MetaNameConst.VFIRSTROWNO,
        
        ICPubMetaNameConst.CSRCMATERIALOID,ICPubMetaNameConst.CSRCMATERIALVID,
      };
  }
  
  @Override
  protected boolean isActionEnable() {
    AbstractBill aggVO = (AbstractBill) this.getModel().getSelectedData();
    if (aggVO == null) {
      return false;
    }
    SuperVO hvo = (SuperVO) aggVO.getParentVO();
    if (hvo == null) {
      return false;
    }

    // 参照来源生成的普通单单据，不能复制
//    ICBillBodyVO[] bvo = (ICBillBodyVO[]) aggVO.getChildrenVO();
//
//    if (bvo != null && bvo.length != 0 && bvo[0] != null
//        && !StringUtil.isSEmptyOrNull(bvo[0].getCsourcetype())) {
//      return false;
//    }

    return this.getModel().getUiState() == UIState.NOT_EDIT;
  }
}
