package nc.bs.pub.action;

import org.w3c.dom.Document;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.agent.XMLPostServiceImpl;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.xml.out.tool.XmlOutTool;
import nc.impl.so.m30.action.main.ApproveSaleOrderAction;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.pubitf.credit.accountcheck.IIgnoreAccountCheck;
import nc.pubitf.credit.creditcheck.IAuditFlowFuncFlag;
import nc.pubitf.credit.creditcheck.IIgnoreCreditCheck;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pflow.PfUserObject;
import nc.vo.scmpub.res.BusinessCheck;
import nc.vo.so.m30.entity.SaleOrderVO;

/**
 * 审批动作脚本
 * 
 * @author 平台脚本生成
 * @since 6.0
 */
public class N_30_APPROVE extends AbstractCompiler2 {
  public N_30_APPROVE() {
    super();
  }

  /*
   * 备注：平台编写原始脚本
   */
  @Override
  public String getCodeRemark() {
    return "\n";
  }

  /*
   * 备注：平台编写规则类 接口执行类
   */
  @Override
  public Object runComClass(PfParameterVO vo) throws BusinessException {
    try {
      super.m_tmpVo = vo;
      SaleOrderVO[] inCurVOs = this.getVos();
      Object creditsrv = null;
      if (SysInitGroupQuery.isCREDITEnabled()) {
        creditsrv = NCLocator.getInstance().lookup(IAuditFlowFuncFlag.class);
        ((IAuditFlowFuncFlag) creditsrv).setAuditFlowFuncFlag();
      }

      ApproveSaleOrderAction action = new ApproveSaleOrderAction();
      Object rts = action.approve(inCurVOs, this);

  	XMLPostServiceImpl services = new XMLPostServiceImpl();

  	String billtype = "bpm_30_add";
	Document doc = services.postBill((SaleOrderVO[])rts, "develop", billtype, inCurVOs[0].getParentVO().getPk_org(), "bpm_name",false, false);
	
	XmlOutTool.buildSendFile(billtype, inCurVOs[0].getParentVO().getVbillcode()+"_name", doc);
	
	Document doc_pk = services.postBill((SaleOrderVO[])rts, "develop", billtype, inCurVOs[0].getParentVO().getPk_org(), "bpm_pk",false, false);
	
	XmlOutTool.buildSendFile(billtype, inCurVOs[0].getParentVO().getVbillcode()+"_pk", doc_pk);
	
      if (SysInitGroupQuery.isCREDITEnabled() && null != creditsrv) {
        ((IAuditFlowFuncFlag) creditsrv).removeAuditFlowFuncFlag();
      }
      return rts;
    }
    catch (Exception ex) {

      ExceptionUtils.marsh(ex);
    }
    return null;
  }

  @Override
  public SaleOrderVO[] getVos() {
    return (SaleOrderVO[]) super.getVos();
  }
}
