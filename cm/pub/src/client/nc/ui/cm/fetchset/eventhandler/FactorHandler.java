/**
 *
 */
package nc.ui.cm.fetchset.eventhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bd.framework.base.CMMapUtil;
import nc.bd.framework.db.CMSqlBuilder;
import nc.ui.bd.pub.handler.CMBasedocAbstractHandler;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardPanelEvent;
import nc.ui.pubapp.util.CardPanelValueUtils;
import nc.vo.bd.materialpricebase.entity.pub.BDAdapter;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.resa.factor.FactorAsoaVO;
import nc.vo.resa.factor.FactorVO;

/**
 * ����Ҫ�ر༭ǰ�¼�
 * 
 * @author liyf
 * 
 */
public class FactorHandler extends CMBasedocAbstractHandler {

	@Override
	public void beforeEdit(CardPanelEvent e1) {
		CardBodyBeforeEditEvent e = (CardBodyBeforeEditEvent) e1;
		e.setReturnValue(true);
		CardPanelValueUtils util = new CardPanelValueUtils(e.getBillCardPanel());
		BillItem billitem = util.getBodyItem("pk_factor");
		boolean flag = true;
		UIRefPane ref = (UIRefPane) billitem.getComponent();
		ref.setPk_org(e.getContext().getPk_org());
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < util.getRowCount(); i++) {
			if (i != e.getRow()
					&& !StringUtil.isEmptyWithTrim(util.getBodyStringValue(i,
							"pk_factor"))) {
				list.add(util.getBodyStringValue(i, "pk_factor"));
			}
		}
		String pk_factory = e.getContext().getPk_org();
		String financeID = getFinanceIDByFactory(pk_factory);
		CMSqlBuilder where = new CMSqlBuilder();

		// where.append(FactorAsoaVO.ENDFLAG + " = 'Y' ");
		if (flag) {
			// ����ҵҪ��
			// where.append(" and ");
			where.append(FactorVO.getDefaultTableName() + "."
					+ FactorVO.ISWORKFACTOR, UFBoolean.FALSE);
			// �����ظ�
			if (list.size() != 0) {
				where.append(" and ");
				where.appendNot(FactorAsoaVO.PK_FACTORASOA,
						list.toArray(new String[0]));
			}
		} else {
			// �����ظ�
			if (list.size() != 0) {
				// where.append(" and ");
				where.appendNot(FactorAsoaVO.PK_FACTORASOA,
						list.toArray(new String[0]));
			}
		}
		ref.setWhereString(where.toString());
		// ������֯
		ref.getRefModel().setPk_org(financeID);
		// ���÷�ĩ��Ҫ�ز���ѡ
		ref.setNotLeafSelectedEnabled(false);
	}

	@Override
	public void afterEdit(CardPanelEvent e) {

	}

	/**
	 * �õ�������Ӧ�Ĳ�����֯
	 * 
	 * @param pk_factory
	 * @return
	 * @author shangzhm1 at 2015-5-11
	 */
	private String getFinanceIDByFactory(String pk_factory) {
		// ���ݹ����Ҷ�Ӧ�Ĳ�����֯
		Map<String, String> financeOrgIDs = null;
		try {

			financeOrgIDs = BDAdapter
					.queryFinanceOrgIDsByStockOrgIDs(new String[] { pk_factory });
		} catch (BusinessException e1) {
			ExceptionUtils.wrappException(e1);
		}
		String financeID = pk_factory;
		if (CMMapUtil.isNotEmpty(financeOrgIDs)) {
			financeID = financeOrgIDs.get(pk_factory);
		}
		return financeID;
	}

}
