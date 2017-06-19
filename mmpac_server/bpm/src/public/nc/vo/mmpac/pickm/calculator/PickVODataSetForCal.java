package nc.vo.mmpac.pickm.calculator;

import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.data.IRelationForItems;
import nc.vo.pubapp.calculator.data.VODataSetForCal;
import nc.vo.pubapp.pattern.data.ValueUtils;

public class PickVODataSetForCal extends VODataSetForCal {

	private IRelationForItems item;
	private CircularlyAccessibleValueObject voHead;

	private CircularlyAccessibleValueObject voitem;

	public PickVODataSetForCal(CircularlyAccessibleValueObject voHead,
			CircularlyAccessibleValueObject voitem, IRelationForItems item) {
		super(voitem, item);
		this.item = item;
		this.voHead = voHead;
		this.voitem = voitem;
	}

	@SuppressWarnings("static-access")
	@Override
	public Object getAttributeValue(String key) {
		Object value = voitem.getAttributeValue(key);
		return value;
	}

	public void setAttributeValue(String key, Object value) {
		voitem.setAttributeValue(key, value);
	}

	@Override
	public String getCastunitid() {
		String value = this.getString(this.item.getCastunitidKey());
		return value;
	}

	@Override
	public void setCastunitid(String value) {
		this.setAttributeValue(this.item.getUnQualifiedNumKey(), value);
	}

	@Override
	public String getCunitid() {
		String value = this.getString(this.item.getCunitidKey());
		return value;
	}

	@Override
	public void setCunitid(String value) {
		this.setAttributeValue(this.item.getCunitidKey(), value);
	}

	@Override
	public UFDouble getNassistnum() {
		UFDouble value = this.getUFDoubleValue(this.item.getNassistnumKey());
		return value;
	}

	@Override
	public void setNassistnum(UFDouble value) {
		this.setAttributeValue(this.item.getNassistnumKey(), value);
	}

	@Override
	public String getNchangerate() {
		String value = this.getString(this.item.getNchangerateKey());
		return value;
	}

	@Override
	public void setNchangerate(String value) {
		this.setAttributeValue(this.item.getNchangerateKey(), value);
	}

	@Override
	public void setNnum(UFDouble value) {
		this.setAttributeValue(this.item.getNnumKey(), value);
	}

	@Override
	public String getCqtunitid() {
		String value = this.getString(this.item.getCqtunitidKey());
		return value;
	}

	@Override
	public String getCcurrencyid() {
		String value = this.getString(this.item.getCcurrencyidKey());
		if (value == null) {
			String pk_org = this.getPk_org();
			value = CurrencyRateUtilHelper.getInstance()
					.getLocalCurrtypeByOrgID(pk_org);
		}
		return value;
	}

	@Override
	public String getCqtcurrencyid() {
		String value = this.getString(this.item.getCqtcurrencyidKey());
		return value;
	}

	@Override
	public UFDouble getNaskqtorigprice() {
		UFDouble value = this.getUFDoubleValue(this.item
				.getNaskqtorigpriceKey());
		return value;
	}

	@Override
	public UFDouble getNaskqtorigtaxprc() {
		UFDouble value = this.getUFDoubleValue(this.item
				.getNaskqtorigtaxprcKey());
		return value;
	}

	@Override
	public UFDouble getNaskqtprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNaskqtpriceKey());
		return value;
	}

	@Override
	public UFDouble getNaskqttaxprice() {
		UFDouble value = this
				.getUFDoubleValue(this.item.getNaskqttaxpriceKey());
		return value;
	}

	@Override
	public UFDouble getNcostmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNcostmnyKey());
		return value;
	}

	@Override
	public UFDouble getNcostprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNcostpriceKey());
		return value;
	}

	@Override
	public UFDouble getNdiscount() {
		UFDouble value = this.getUFDoubleValue(this.item.getNdiscountKey());
		return value;
	}

	@Override
	public UFDouble getNdiscountrate() {
		UFDouble value = this.getUFDoubleValue(this.item.getNdiscountrateKey());
		return value;
	}

	@Override
	public UFDouble getNexchangerate() {
		Object obj = voitem.getAttributeValue(this.item.getNexchangerateKey());
		UFDouble value = ValueUtils.getUFDouble(obj);
		return value;
	}

	@Override
	public UFDouble getNglobalexchgrate() {
		UFDouble value = this.getUFDoubleValue(this.item
				.getNglobalexchgrateKey());
		return value;
	}

	@Override
	public UFDouble getNglobalmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNglobalmnyKey());
		return value;
	}

	@Override
	public UFDouble getNglobaltaxmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNglobaltaxmnyKey());
		return value;
	}

	@Override
	public UFDouble getNgroupexchgrate() {
		UFDouble value = this.getUFDoubleValue(this.item
				.getNgroupexchgrateKey());
		return value;
	}

	@Override
	public UFDouble getNgroupmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNgroupmnyKey());
		return value;
	}

	@Override
	public UFDouble getNgrouptaxmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNgrouptaxmnyKey());
		return value;
	}

	@Override
	public UFDouble getNitemdiscountrate() {
		UFDouble value = this.getUFDoubleValue(this.item
				.getNitemdiscountrateKey());
		return value;
	}

	@Override
	public UFDouble getNmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNmnyKey());
		return value;
	}

	@Override
	public UFDouble getNnetprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNnetpriceKey());
		return value;
	}

	@Override
	public UFDouble getNnum() {
		UFDouble value = this.getUFDoubleValue(this.item.getNnumKey());
		return value;
	}

	@Override
	public UFDouble getNorigdiscount() {
		UFDouble value = this.getUFDoubleValue(this.item.getNorigdiscountKey());
		return value;
	}

	@Override
	public UFDouble getNorigmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNorigmnyKey());
		return value;
	}

	@Override
	public UFDouble getNorignetprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNorignetpriceKey());
		return value;
	}

	@Override
	public UFDouble getNorigprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNorigpriceKey());
		return value;
	}

	//
	// @Override
	// public UFDouble getNorigtax() {
	// UFDouble value = this.getUFDoubleValue(this.item.getNorigtaxKey());
	// return value;
	// }

	@Override
	public UFDouble getNorigtaxmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNorigtaxmnyKey());
		return value;
	}

	@Override
	public UFDouble getNorigtaxnetprice() {
		UFDouble value = this.getUFDoubleValue(this.item
				.getNorigtaxnetpriceKey());
		return value;
	}

	@Override
	public UFDouble getNorigtaxprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNorigtaxpriceKey());
		return value;
	}

	@Override
	public UFDouble getNprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNpriceKey());
		return value;
	}

	@Override
	public UFDouble getNqtnetprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNqtnetpriceKey());
		return value;
	}

	@Override
	public UFDouble getNqtorignetprice() {
		UFDouble value = this.getUFDoubleValue(this.item
				.getNqtorignetpriceKey());
		return value;
	}

	@Override
	public UFDouble getNqtorigprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNqtorigpriceKey());
		return value;
	}

	@Override
	public UFDouble getNqtorigtaxnetprc() {
		UFDouble value = this.getUFDoubleValue(this.item
				.getNqtorigtaxnetprcKey());
		return value;
	}

	@Override
	public UFDouble getNqtorigtaxprice() {
		UFDouble value = this.getUFDoubleValue(this.item
				.getNqtorigtaxpriceKey());
		return value;
	}

	@Override
	public UFDouble getNqtprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNqtpriceKey());
		return value;
	}

	@Override
	public UFDouble getNqttaxnetprice() {
		UFDouble value = this
				.getUFDoubleValue(this.item.getNqttaxnetpriceKey());
		return value;
	}

	@Override
	public UFDouble getNqttaxprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNqttaxpriceKey());
		return value;
	}

	@Override
	public UFDouble getNqtunitnum() {
		UFDouble value = this.getUFDoubleValue(this.item.getNqtunitnumKey());
		return value;
	}

	@Override
	public String getNqtunitrate() {
		String value = this.getString(this.item.getNqtunitrateKey());
		return value;
	}

	@Override
	public UFDouble getNtax() {
		UFDouble value = this.getUFDoubleValue(this.item.getNtaxKey());
		return value;
	}

	@Override
	public UFDouble getNtaxmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNtaxmnyKey());
		return value;
	}

	@Override
	public UFDouble getNtaxnetprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNtaxnetpriceKey());
		return value;
	}

	@Override
	public UFDouble getNtaxprice() {
		UFDouble value = this.getUFDoubleValue(this.item.getNtaxpriceKey());
		return value;
	}

	@Override
	public UFDouble getNtaxrate() {
		UFDouble value = this.getUFDoubleValue(this.item.getNtaxrateKey());
		return value;
	}

	@Override
	public UFDouble getNtotalnum() {
		UFDouble value = this.getUFDoubleValue(this.item.getNtotalnumKey());
		return value;
	}

	@Override
	public UFDouble getNtotalorigmny() {
		UFDouble value = this.getUFDoubleValue(this.item.getNtotalorigmnyKey());
		return value;
	}

	@Override
	public UFDouble getNtotalorigtaxmny() {
		UFDouble value = this.getUFDoubleValue(this.item
				.getNtotalorigtaxmnyKey());
		return value;
	}

	@Override
	public UFDouble getQualifiedNum() {
		UFDouble value = this.getUFDoubleValue(this.item.getQualifiedNumKey());
		return value;
	}

	@Override
	public String getCorigcurrencyid() {
		String value = this.getString(this.item.getCorigcurrencyidKey());
		return value;
	}

	private String getString(String key) {
		Object value = voitem.getAttributeValue(key);
		String str = ValueUtils.getString(value);
		return str;
	}

	private UFDouble getUFDoubleValue(String key) {
		Object value = voitem.getAttributeValue(key);
		UFDouble d = ValueUtils.getUFDouble(value);
		return d;
	}

}
