package nc.vo.ic.fivemetals;

import nc.md.model.IEnumValue;
import nc.md.model.impl.MDEnum;

/**
 * <b>此处简要描述此枚举的功能 </b>
 * <p>
 *   此处添加该枚举的描述信息
 * </p>
 *  创建日期:2017-6-14
 * @author 
 * @version NCPrj ??
 */
public class CardTypeEnum extends MDEnum{
	public CardTypeEnum(IEnumValue enumvalue){
		super(enumvalue);
	}

	
	
	public static final CardTypeEnum 部门卡 = MDEnum.valueOf(CardTypeEnum.class, String.valueOf(1));
	
	
	public static final CardTypeEnum 项目卡 = MDEnum.valueOf(CardTypeEnum.class, String.valueOf(2));
	

}