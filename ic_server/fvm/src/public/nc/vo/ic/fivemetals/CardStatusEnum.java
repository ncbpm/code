package nc.vo.ic.fivemetals;

import nc.md.model.IEnumValue;
import nc.md.model.impl.MDEnum;

public class CardStatusEnum extends MDEnum {
	public CardStatusEnum(IEnumValue enumvalue) {
		super(enumvalue);
	}

	// ����
	public static final CardTypeEnum ���� = MDEnum.valueOf(CardTypeEnum.class, 1);

	// ͣ��
	public static final CardTypeEnum ͣ�� = MDEnum.valueOf(CardTypeEnum.class, 2);

	// ����
	public static final CardTypeEnum ���� = MDEnum.valueOf(CardTypeEnum.class, 3);

	// ��ֵ
	public static final CardTypeEnum ��ֵ = MDEnum.valueOf(CardTypeEnum.class, 4);

	// ��ʧ ע��
	public static final CardTypeEnum ��ʧ = MDEnum.valueOf(CardTypeEnum.class, 5);

}
