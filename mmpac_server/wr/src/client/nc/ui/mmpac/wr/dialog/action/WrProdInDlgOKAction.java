package nc.ui.mmpac.wr.dialog.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uap.xbrl.ui.dialog.UIDialog0;

import nc.bs.logging.Logger;
import nc.bs.mmpac.wr.adapter.WrPickmServiceAdapter;
import nc.bs.mmpac.wr.rule.setcheck.bo.WrScAlgorithmBO;
import nc.bs.mmpac.wr.rule.setcheck.bo.WrScForceCollectionDataBO;
import nc.bs.mmpac.wr.util.rewrite.prodin.WrProdInCommonCacheUtil;
import nc.bsutil.mmpac.pacpub.PACParameterManager;
import nc.itf.mmpac.wr.IWrMaintainService;
import nc.ui.dcm.chnlrplstrct.maintain.action.MessageDialog;
import nc.ui.mmpac.wr.dialog.WrProdInDlg;
import nc.ui.mmpac.wr.model.WrBillManageModel;
import nc.ui.mmpac.wr.serviceproxy.WrBusinessServiceProxy;
import nc.ui.mmpac.wr.util.WrExceptionUtil;
import nc.ui.mmpac.wr.util.WrUIHelper;
import nc.ui.pcm.view.AskDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.model.BillManageModel;
import nc.util.mmf.framework.base.MMArrayUtil;
import nc.util.mmf.framework.base.MMMapUtil;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.am.proxy.AMProxy;
import nc.vo.bd.bom.bom0202.enumeration.OutputTypeEnum;
import nc.vo.bd.material.IMaterialEnumConst;
import nc.vo.mmpac.pickm.entity.AggPickmVO;
import nc.vo.mmpac.pickm.param.WrCheckParam;
import nc.vo.mmpac.wr.consts.WrptLangConst;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.mmpac.wr.entity.WrQualityVO;
import nc.vo.mmpac.wr.enumeration.WrBillStatusEnum;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public class WrProdInDlgOKAction extends GenralDlgOKAction {
    private static final long serialVersionUID = -2896330064447086637L;

    public BillManageModel getWrModel() {
        return this.wrModel;
    }

    public void setWrModel(BillManageModel wrModel) {
        this.wrModel = wrModel;
    }

    BillManageModel wrModel = null;

    @Override
    public void doAction(ActionEvent e) throws Exception {
        WrProdInDlg dlg = (WrProdInDlg) this.getDialog();
        if (null == dlg) {
            return;
        }
        AggWrVO[] aggWrVOs = dlg.getValue();
        try {
        	//前端进行 完工齐套检查， 如果不通过，提示用户是否继续
        	AggWrVO[] results = null;
            WrBusinessServiceProxy wrBusinessServiceProxy = new WrBusinessServiceProxy();
            try {
                results = wrBusinessServiceProxy.setCheck(aggWrVOs);
            }
            catch (BusinessException ex) {
                ExceptionUtils.wrappException(ex);
            }catch(Exception ex){
            	System.out.println(ex.getStackTrace());
            }
            if (!MMValueCheck.isEmpty(results)) {
                StringBuilder errorMessagesSB = new StringBuilder();
                for (AggWrVO aggWrVO : results) {
                    if (WrBillStatusEnum.FREEDOM.equalsValue(aggWrVO.getParentVO().getFbillstatus())) {
                        continue;
                    }
                    StringBuilder notThroughSB = new StringBuilder();
                    String billCode = aggWrVO.getParentVO().getVbillcode();
                    WrItemVO[] itemVOs = (WrItemVO[]) aggWrVO.getChildren(WrItemVO.class);
                    if (MMValueCheck.isEmpty(itemVOs)) {
                        continue;
                    }
                    for (WrItemVO itemVO : itemVOs) {
                        //过滤掉无订单报产行或联副产品行
                        if (MMValueCheck.isEmpty(itemVO.getCbmoid())
                                || !OutputTypeEnum.MAIN_PRODUCT.equalsValue(itemVO.getFbproducttype())) {
                            continue;
                        }
                        if (!UFBoolean.TRUE.equals(itemVO.getBbsetmark())) {
                            notThroughSB.append(itemVO.getVbrowno());
                            notThroughSB.append(",");
                        }

                    }
                    if (!MMValueCheck.isEmpty(notThroughSB)) {
                        notThroughSB.setLength(notThroughSB.length() - 1);
                        //生产报告单号***行号**，齐套检查通过；
                        final String message = WrptLangConst.getSetCheckError_Msg(new String[] {
                            billCode, notThroughSB.toString()
                        });
                        errorMessagesSB.append(message);
                        errorMessagesSB.append("\n");
                    }

                }
                if (!MMValueCheck.isEmpty(errorMessagesSB)) {
                    //  不通过
                	int ret = MessageDialog.showYesNoDlg(this.getDialog(), "提示", "完工齐套检查不通过，是否继续保存？");
            		if(ret != UIDialog.ID_YES){
            			return;
            		}
                }
                else {
                    //通过
                }
            }
            // 入库处理
            this.depositProcess(aggWrVOs);
        }
        catch (BusinessException ex) {
        	WrExceptionUtil util = new WrExceptionUtil();
        	String errorMessage = util.getExceptionMessage(ex);
            Logger.error(ex.getMessage(), ex);
            dlg.setErrMsg(errorMessage);
            ExceptionUtils.marsh(ex);
        }
        dlg.clearCache();
        super.doAction(e);

    }
    
    private void depositProcess(AggWrVO[] aggWrVOs) throws BusinessException {

        WrBusinessServiceProxy proxy = new WrBusinessServiceProxy();
        AggWrVO[] returnAggs = proxy.prodIn(aggWrVOs);
        if (null != this.getWrModel()) {
            if (null != this.getWrModel() && this.getWrModel() instanceof WrBillManageModel) {
                new WrUIHelper().directlyUpdate((WrBillManageModel) this.getWrModel(), returnAggs,
                        MMArrayUtil.toArray(IBill.class, this.getWrModel().getSelectedOperaDatas()));
            }

        }
    }

}
