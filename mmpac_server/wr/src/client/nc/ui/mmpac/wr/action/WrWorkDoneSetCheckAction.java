package nc.ui.mmpac.wr.action;

import java.awt.event.ActionEvent;

import nc.ui.mmf.framework.action.ActionInitializer;
import nc.ui.mmpac.wr.serviceproxy.WrBusinessServiceProxy;
import nc.ui.mmpac.wr.util.WrUIHelper;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.util.mmf.framework.base.MMArrayUtil;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.bd.bom.bom0202.enumeration.OutputTypeEnum;
import nc.vo.mmpac.wr.consts.WrBtnConst;
import nc.vo.mmpac.wr.consts.WrptLangConst;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.mmpac.wr.enumeration.WrBillStatusEnum;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/** 完工齐套检查按钮
 * 
 * @since 6.0
 * @version 2013-6-17 下午01:37:45
 * @author liweiz */
public class WrWorkDoneSetCheckAction extends WrBaseAction {

    private static final long serialVersionUID = 3002190720740696516L;

    /** 默认构造函数 */
    public WrWorkDoneSetCheckAction() {
        ActionInitializer.initializeAction(this, WrBtnConst.getBTN_CODE_WR_WorkDone_Set(),
                WrBtnConst.getBTN_NAME_WR_WorkDone_Set(), WrBtnConst.getBTN_TOOLTIP_WR_WorkDone_Set());
    }

    @Override
    public void doAction(ActionEvent e) throws Exception {
        super.doAction(e);
        AggWrVO[] aggWrVOs = WrUIHelper.getSelectedAggWrVOs(this.getModel(), this.getListView(), this.getBillForm());
        //注释byliweiz 考虑性能问题无需校验TS
        //        if (MMValueCheck.isEmpty(aggWrVOs)) {
        //            return;
        //        }
        //        // 校验model中选中数据TS，注意要校验界面所有行，因为选中行算法会依赖非选择行数据
        //        if (null != this.getModel().getSelectedOperaDatas()) {
        //            Object[] objects = this.getModel().getSelectedOperaDatas();
        //            List<Object> itemVOs = new ArrayList<Object>();
        //            for (Object objet : objects) {
        //                final Object[] tempVOs = ((AggWrVO) objet).getChildren(WrItemVO.class);
        //                if (MMValueCheck.isEmpty(tempVOs)) {
        //                    continue;
        //                }
        //                itemVOs.addAll(Arrays.asList(tempVOs));
        //            }
        //
        //            IBean bean = MDBaseQueryFacade.getInstance().getBeanByFullClassName(WrItemVO.class.getName());
        //            //校验TS
        //            BDVersionValidationUtil.validateObject(bean, itemVOs.toArray(new Object[0]));
        //        }
        AggWrVO[] results = null;
        WrBusinessServiceProxy wrBusinessServiceProxy = new WrBusinessServiceProxy();
        try {
            results = wrBusinessServiceProxy.setCheck(aggWrVOs);
        }
        catch (BusinessException ex) {
            ExceptionUtils.wrappException(ex);
        }
        new WrUIHelper().directlyUpdate(this.getModel(), results,
                MMArrayUtil.toArray(IBill.class, this.getModel().getSelectedOperaDatas()));
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
                // 显示返回信息
                ShowStatusBarMsgUtil.showErrorMsg(null, errorMessagesSB.toString(), this.getModel().getContext());
            }
            else {
                ShowStatusBarMsgUtil.showStatusBarMsg(WrptLangConst.getSetCheckThrough_Msg(), this.getModel()
                        .getContext());
            }
        }

    }

    /** 通用逻辑，生产报告为审核态时，按钮可用，否则不可用 */
    @Override
    protected boolean isActionEnable() {
        if (AppUiState.NOT_EDIT != this.getModel().getAppUiState()) {
            return false;
        }
        Object[] selDatas = this.getModel().getSelectedOperaDatas();
        if (null == selDatas || selDatas.length == 0) {
            return false;
        }
        if (selDatas.length > 1) {
            return true;
        }
        AggWrVO agg = (AggWrVO) selDatas[0];
        if (WrBillStatusEnum.FREEDOM.equalsValue(agg.getParentVO().getFbillstatus())) {
            return false;
        }
        if (this.getBillForm().isShowing()) {
            if (this.getBillForm().getBillCardPanel().getBillTable(AggWrVO.TBCODE_PICK).isShowing()) {
                return false;
            }
        }
        return true;
    }
}
