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
public class CostTypeEnum extends MDEnum{
	public CostTypeEnum(IEnumValue enumvalue){
		super(enumvalue);
	}

	
	
	public static final CostTypeEnum ��ֵ = MDEnum.valueOf(CostTypeEnum.class, String.valueOf(1));
	
	
	public static final CostTypeEnum ���� = MDEnum.valueOf(CostTypeEnum.class, String.valueOf(-1));
	

}