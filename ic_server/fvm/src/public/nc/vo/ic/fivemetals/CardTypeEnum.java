package nc.vo.ic.fivemetals;

import nc.md.model.IEnumValue;
import nc.md.model.impl.MDEnum;

/**
 * <b>�˴���Ҫ������ö�ٵĹ��� </b>
 * <p>
 *   �˴���Ӹ�ö�ٵ�������Ϣ
 * </p>
 *  ��������:2017-6-14
 * @author 
 * @version NCPrj ??
 */
public class CardTypeEnum extends MDEnum{
	public CardTypeEnum(IEnumValue enumvalue){
		super(enumvalue);
	}

	
	
	public static final CardTypeEnum ���ſ� = MDEnum.valueOf(CardTypeEnum.class, String.valueOf(1));
	
	
	public static final CardTypeEnum ��Ŀ�� = MDEnum.valueOf(CardTypeEnum.class, String.valueOf(2));
	

}