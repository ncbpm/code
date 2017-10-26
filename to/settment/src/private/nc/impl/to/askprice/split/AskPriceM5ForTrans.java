package nc.impl.to.askprice.split;

import java.util.ArrayList;
import java.util.List;

import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.util.mmf.framework.db.SqlInUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.m32.entity.SaleInvoiceBVO;
import nc.vo.to.m5f.entity.SettleQueryViewVO;
import nc.vo.to.m5f.util.CalCulateM5fViewPriceMny;

/**
 * 1. ���������֯��RA��������009����CA���������018�� �����������֯δ���������֯���������Լ���������۶����� 2.
 * �ڲ����㵥����ʱ�򣬽�ԴͷΪ1���������۶�����Ӫ�����۷�Ʊ����ϸ���ݵ��Զ�����Ŀ5,16,17,18
 * �����۷�Ʊ��ֵ���ڲ������嵥��ͬʱ���Զ�����5��ֵ���ڲ������嵥�ĺ�˰���ۺ���˰���ۣ��������������Ҳ�����Ʊ��ȡ to_settlelist
 * 
 * @author liyf
 * 
 */
public class AskPriceM5ForTrans {

	public void askSettlePrice(SettleQueryViewVO[] vos)
			throws BusinessException {
		SettleQueryViewVO[] fvos = this.filterData(vos);
		if (fvos == null || fvos.length == 0) {
			return;
		}
		SaleInvoiceBVO[] invoiceBvos = askPriceOnSettlelist(fvos);
		if (invoiceBvos == null || invoiceBvos.length == 0) {
			return;
		}
		for (SettleQueryViewVO vo : fvos) {
			for (SaleInvoiceBVO invoicebvo : invoiceBvos) {
				if (vo.getCsrcbid().equalsIgnoreCase(invoicebvo.getCsrcbid())) {
					this.setPrice(vo, invoicebvo);
				}
			}
		}

	}

	private void setPrice(SettleQueryViewVO view, SaleInvoiceBVO askvo) {
		// TODO �Զ����ɵķ������
		view.setAttributeValue("vbdef5", askvo.getAttributeValue("vbdef5"));
		view.setAttributeValue("vbdef16", askvo.getAttributeValue("vbdef16"));
		view.setAttributeValue("vbdef17", askvo.getAttributeValue("vbdef17"));
		view.setAttributeValue("vbdef18", askvo.getAttributeValue("vbdef18"));
		UFDouble price = new UFDouble(askvo.getVbdef5());
		view.setNorigtaxprice(price);
		CalCulateM5fViewPriceMny calTool = new CalCulateM5fViewPriceMny(
				UFBoolean.FALSE);
		calTool.calculateViewPriceMny(view, SettleQueryViewVO.NORIGTAXPRICE);
	}

	private SaleInvoiceBVO[] askPriceOnSettlelist(SettleQueryViewVO[] fvos)
			throws BusinessException {

		List<String> pks = new ArrayList<>();
		for (SettleQueryViewVO vo : fvos) {
			pks.add(vo.getCsrcbid());
		}
		SqlInUtil inutil = new SqlInUtil(pks);
		// TODO �Զ����ɵķ������
		StringBuffer sql = new StringBuffer();
		sql.append(" and csrcbid  " + inutil.getInSql4Collection());
		VOQuery<SaleInvoiceBVO> queyr = new VOQuery<SaleInvoiceBVO>(
				SaleInvoiceBVO.class);
		return queyr.query(sql.toString(), null);
	}

	/**
	 * 
	 * @param vos
	 * @return
	 */
	private SettleQueryViewVO[] filterData(SettleQueryViewVO[] vos) {
		// TODO �Զ����ɵķ������
		ArrayList<SettleQueryViewVO> viewList = new ArrayList<SettleQueryViewVO>();
		for (SettleQueryViewVO vo : vos) {
			if (vo.getCinfinanceorgid()
					.equalsIgnoreCase(vo.getCoutstockorgid()))
				continue;
			if (!"30".equalsIgnoreCase(vo.getVfirsttype())) {
				continue;
			}
			if (!"4C".equalsIgnoreCase(vo.getVsrctype())) {
				continue;
			}
			if ("0001A110000000002RKX"
					.equalsIgnoreCase(vo.getCinfinanceorgid())
					|| "0001A110000000002RK6".equalsIgnoreCase(vo
							.getCinfinanceorgid())) {
				viewList.add(vo);
			}

		}

		SettleQueryViewVO[] views = new SettleQueryViewVO[viewList.size()];
		viewList.toArray(views);
		return views;
	}

}
