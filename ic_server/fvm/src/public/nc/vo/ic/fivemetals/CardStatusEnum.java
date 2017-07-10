package nc.vo.ic.fivemetals;

import nc.md.model.IEnumValue;
import nc.md.model.impl.MDEnum;

public class CardStatusEnum extends MDEnum {
	public CardStatusEnum(IEnumValue enumvalue) {
		super(enumvalue);
	}

	// 启用
	public static final CardStatusEnum 启用 = MDEnum.valueOf(
			CardStatusEnum.class, 1);

	// 停用
	public static final CardStatusEnum 停用 = MDEnum.valueOf(
			CardStatusEnum.class, 2);

	// 消费
	public static final CardStatusEnum 消费 = MDEnum.valueOf(
			CardStatusEnum.class, 3);

	// 充值（保留余额）
	public static final CardStatusEnum 充值 = MDEnum.valueOf(
			CardStatusEnum.class, 4);
	
	// 充值(作废余额)
	public static final CardStatusEnum 充值1 = MDEnum.valueOf(
			CardStatusEnum.class, 7);

	// 挂失 注销
	public static final CardStatusEnum 挂失 = MDEnum.valueOf(
			CardStatusEnum.class, 5);

}
