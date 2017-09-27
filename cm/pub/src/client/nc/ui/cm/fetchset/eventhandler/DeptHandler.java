/**
 *
 */
package nc.ui.cm.fetchset.eventhandler;

import java.util.ArrayList;
import java.util.List;

import nc.bd.framework.db.CMSqlBuilder;
import nc.ui.bd.pub.handler.CMBasedocAbstractHandler;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardPanelEvent;
import nc.ui.pubapp.util.CardPanelValueUtils;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.DeptVO;

/**
 * 部门编辑前事件
 * 
 * @author liyf
 * 
 */
public class DeptHandler extends CMBasedocAbstractHandler {

	@Override
	public void beforeEdit(CardPanelEvent e1) {
		CardBodyBeforeEditEvent e = (CardBodyBeforeEditEvent) e1;
		e.setReturnValue(true);
		CardPanelValueUtils util = new CardPanelValueUtils(e.getBillCardPanel());
		BillItem billitem = util.getBodyItem(e.getKey());
		boolean flag = true;
		UIRefPane ref = (UIRefPane) billitem.getComponent();
		ref.setPk_org(e.getContext().getPk_org());
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < util.getRowCount(); i++) {
			if (i != e.getRow()
					&& !StringUtil.isEmptyWithTrim(util.getBodyStringValue(i,
							e.getKey()))) {
				list.add(util.getBodyStringValue(i, e.getKey()));
			}
		}
		String pk_factory = e.getContext().getPk_org();
		CMSqlBuilder where = new CMSqlBuilder();

		// where.append(FactorAsoaVO.ENDFLAG + " = 'Y' ");

//		// 过滤重复
//		if (list.size() != 0) {
//			// where.append(" and ");
//			where.appendNot(DeptVO.PK_DEPT,
//					list.toArray(new String[0]));
//		}
//	
//		ref.setWhereString(where.toString());
		// 财务组织
		ref.getRefModel().setPk_org(pk_factory);
		// 设置非末级要素不可选
		ref.setNotLeafSelectedEnabled(true);
	}

	@Override
	public void afterEdit(CardPanelEvent e) {

	}
}
