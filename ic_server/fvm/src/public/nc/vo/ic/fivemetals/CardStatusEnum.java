package nc.vo.ic.fivemetals;

import nc.md.model.IEnumValue;
import nc.md.model.impl.MDEnum;

public class CardStatusEnum extends MDEnum {
	public CardStatusEnum(IEnumValue enumvalue) {
		super(enumvalue);
	}

	// ����
	public static final CardStatusEnum ���� = MDEnum.valueOf(
			CardStatusEnum.class, 1);

	// ͣ��
	public static final CardStatusEnum ͣ�� = MDEnum.valueOf(
			CardStatusEnum.class, 2);

	// ����
	public static final CardStatusEnum ���� = MDEnum.valueOf(
			CardStatusEnum.class, 3);

	// ��ֵ��������
	public static final CardStatusEnum ��ֵ = MDEnum.valueOf(
			CardStatusEnum.class, 4);
	
	// ��ֵ(�������)
	public static final CardStatusEnum ��ֵ1 = MDEnum.valueOf(
			CardStatusEnum.class, 7);

	// ��ʧ ע��
	public static final CardStatusEnum ��ʧ = MDEnum.valueOf(
			CardStatusEnum.class, 5);

}
