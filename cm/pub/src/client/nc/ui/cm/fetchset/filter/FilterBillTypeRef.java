package nc.ui.cm.fetchset.filter;

import nc.bd.framework.db.CMSqlBuilder;
import nc.ui.bd.business.ref.CMFilterDefaultRefUtils;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.vo.cm.fetchset.enumeration.FetchTypeEnum;
import nc.vo.pubapp.AppContext;

/**
 * �������Ͳ��չ���
 *
 * @since 6.0
 * @version 2011-10-21 ����01:41:37
 * @author shangzhm1
 */

public class FilterBillTypeRef extends CMFilterDefaultRefUtils {
    // ��ǰ�û���¼����
    private String pk_group = AppContext.getInstance().getPkGroup();

    private BillItem billItem;

    /**
     * ���캯��
     *
     * @param pane
     */
    public FilterBillTypeRef(UIRefPane pane) {
        super(pane);
    }

    /**
     * ���캯��
     *
     * @param item ����item����
     */
    public FilterBillTypeRef(BillItem item) {
        super((UIRefPane) item.getComponent());
        this.billItem = item;
        this.filterItemRefByGroup(this.pk_group);
    }

    /**
     * ���ý������͹��˵�where����
     *
     * @param ifetchtype ȡ������
     */
    public void setBillTypeFilterWhere(String ifetchtype) {
        CMSqlBuilder sqlwhere = new CMSqlBuilder();
        // δ�����߷���ʶΪ��
        sqlwhere.append(" and (islock  ='N' or islock is null)");
        // ���ϳ���
        if (ifetchtype.equals(String.valueOf(FetchTypeEnum.MATEROUT.value()))) {
            // �����ϳ��ⵥ��Ӧ�Ľ�������
            sqlwhere.append(" and (pk_billtypecode like '4D-%' or pk_billtypecode like '47-%')");
        }
        // �깤���
        else if (ifetchtype.equals(String.valueOf(FetchTypeEnum.OVERIN.value()))) {
            // ������Ʒ��ⵥ��ί�мӹ���ⵥ��Ӧ�Ľ�������
            sqlwhere.append(" and (pk_billtypecode like '46-%' or pk_billtypecode like '47-%')");
        }
        else if (ifetchtype.equals(String.valueOf(FetchTypeEnum.SPOIL.value()))) {
            // ����������ⵥ��Ӧ�Ľ�������
            sqlwhere.append(" and pk_billtypecode like '4X-%' ");
        }
        else if (ifetchtype.equals(String.valueOf(FetchTypeEnum.GXWW.value()))) {
            // �ӹ��ѽ��㵥��Ӧ�ĵ�������
            sqlwhere.append(" and pk_billtypecode in('55E6') ");
        }
        else if (ifetchtype.equals(String.valueOf(FetchTypeEnum.IASTUFF.value()))) {
            // ������������ĵ���Ӧ�ĵ�������
            sqlwhere.append(" and pk_billtypecode in('I4','I7','ID','I6') ");
        }
        else if (ifetchtype.equals(String.valueOf(FetchTypeEnum.TASK.value()))) {
            // ��ҵ�ĵ�������
            sqlwhere.append(" and pk_billtypecode in('55A8') ");
        }
        //2017-09-02 
        else if (ifetchtype.equals(String.valueOf(FetchTypeEnum.DINGE.value()))) {
            // ����ȡ�������Ĳ�Ʒ����ⵥ
            sqlwhere.append(" and pk_billtypecode in('I3') ");
        }
        else if (ifetchtype.equals(String.valueOf(FetchTypeEnum.CHUYUN.value()))) {
            // ����ȡ�������Ĳ�Ʒ����ⵥ
            sqlwhere.append(" and pk_billtypecode like '4%' ");
        }
        UIRefPane refPane = (UIRefPane) this.billItem.getComponent();
        if (refPane == null) {
            return;
        }
        // ֧�ֶ�ѡ
        refPane.setMultiSelectedEnabled(true);
        refPane.setMultiCorpRef(false);
        refPane.setMultiOrgSelected(false);

        refPane.getRefModel().addWherePart(sqlwhere.toString());
        refPane.setReturnCode(true);
    }

}
