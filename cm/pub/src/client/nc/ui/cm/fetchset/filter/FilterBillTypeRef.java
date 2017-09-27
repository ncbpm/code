package nc.ui.cm.fetchset.filter;

import nc.bd.framework.db.CMSqlBuilder;
import nc.ui.bd.business.ref.CMFilterDefaultRefUtils;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.vo.cm.fetchset.enumeration.FetchTypeEnum;
import nc.vo.pubapp.AppContext;

/**
 * 交易类型参照过滤
 *
 * @since 6.0
 * @version 2011-10-21 下午01:41:37
 * @author shangzhm1
 */

public class FilterBillTypeRef extends CMFilterDefaultRefUtils {
    // 当前用户登录集团
    private String pk_group = AppContext.getInstance().getPkGroup();

    private BillItem billItem;

    /**
     * 构造函数
     *
     * @param pane
     */
    public FilterBillTypeRef(UIRefPane pane) {
        super(pane);
    }

    /**
     * 构造函数
     *
     * @param item 根据item过滤
     */
    public FilterBillTypeRef(BillItem item) {
        super((UIRefPane) item.getComponent());
        this.billItem = item;
        this.filterItemRefByGroup(this.pk_group);
    }

    /**
     * 设置交易类型过滤的where条件
     *
     * @param ifetchtype 取数类型
     */
    public void setBillTypeFilterWhere(String ifetchtype) {
        CMSqlBuilder sqlwhere = new CMSqlBuilder();
        // 未封存或者封存标识为空
        sqlwhere.append(" and (islock  ='N' or islock is null)");
        // 材料出库
        if (ifetchtype.equals(String.valueOf(FetchTypeEnum.MATEROUT.value()))) {
            // 库存材料出库单对应的交易类型
            sqlwhere.append(" and (pk_billtypecode like '4D-%' or pk_billtypecode like '47-%')");
        }
        // 完工入库
        else if (ifetchtype.equals(String.valueOf(FetchTypeEnum.OVERIN.value()))) {
            // 库存产成品入库单和委托加工入库单对应的交易类型
            sqlwhere.append(" and (pk_billtypecode like '46-%' or pk_billtypecode like '47-%')");
        }
        else if (ifetchtype.equals(String.valueOf(FetchTypeEnum.SPOIL.value()))) {
            // 生产报废入库单对应的交易类型
            sqlwhere.append(" and pk_billtypecode like '4X-%' ");
        }
        else if (ifetchtype.equals(String.valueOf(FetchTypeEnum.GXWW.value()))) {
            // 加工费结算单对应的单据类型
            sqlwhere.append(" and pk_billtypecode in('55E6') ");
        }
        else if (ifetchtype.equals(String.valueOf(FetchTypeEnum.IASTUFF.value()))) {
            // 其他出入库消耗单对应的单据类型
            sqlwhere.append(" and pk_billtypecode in('I4','I7','ID','I6') ");
        }
        else if (ifetchtype.equals(String.valueOf(FetchTypeEnum.TASK.value()))) {
            // 作业的单据类型
            sqlwhere.append(" and pk_billtypecode in('55A8') ");
        }
        //2017-09-02 
        else if (ifetchtype.equals(String.valueOf(FetchTypeEnum.DINGE.value()))) {
            // 定额取存货核算的产品成入库单
            sqlwhere.append(" and pk_billtypecode in('I3') ");
        }
        else if (ifetchtype.equals(String.valueOf(FetchTypeEnum.CHUYUN.value()))) {
            // 定额取存货核算的产品成入库单
            sqlwhere.append(" and pk_billtypecode like '4%' ");
        }
        UIRefPane refPane = (UIRefPane) this.billItem.getComponent();
        if (refPane == null) {
            return;
        }
        // 支持多选
        refPane.setMultiSelectedEnabled(true);
        refPane.setMultiCorpRef(false);
        refPane.setMultiOrgSelected(false);

        refPane.getRefModel().addWherePart(sqlwhere.toString());
        refPane.setReturnCode(true);
    }

}
