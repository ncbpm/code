package nc.vo.ic.fivemetals;

import nc.md.model.IEnumValue;
import nc.md.model.impl.MDEnum;

public class CardStatusEnum extends MDEnum {
	public CardStatusEnum(IEnumValue enumvalue) {
		super(enumvalue);
	}

	// 启用
	public static final CardTypeEnum 启用 = MDEnum.valueOf(CardTypeEnum.class, 1);

	// 停用
	public static final CardTypeEnum 停用 = MDEnum.valueOf(CardTypeEnum.class, 2);

	// 消费
	public static final CardTypeEnum 消费 = MDEnum.valueOf(CardTypeEnum.class, 3);

	// 充值
	public static final CardTypeEnum 充值 = MDEnum.valueOf(CardTypeEnum.class, 4);

	// 挂失 注销
	public static final CardTypeEnum 挂失 = MDEnum.valueOf(CardTypeEnum.class, 5);

}
