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
 * 1. 如果销售组织是RA（润丰香港009），CA（润丰中美018） ，发货库存组织未其他库存组织（即不是自己出库的销售订单） 2.
 * 内部结算单生成时候，将源头为1描述的销售订单敌营的销售发票的明细数据的自定义项目5,16,17,18
 * 从销售发票赋值到内部结算清单。同时将自定义项5赋值给内部结算清单的含税单价和无税单价，反算出来金额，如果找不到发票则不取 to_settlelist
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
		// TODO 自动生成的方法存根
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
		// TODO 自动生成的方法存根
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
		// TODO 自动生成的方法存根
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
