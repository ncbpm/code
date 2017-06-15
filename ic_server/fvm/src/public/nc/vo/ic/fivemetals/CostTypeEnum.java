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
public class CostTypeEnum extends MDEnum{
	public CostTypeEnum(IEnumValue enumvalue){
		super(enumvalue);
	}

	
	
	public static final CostTypeEnum 充值 = MDEnum.valueOf(CostTypeEnum.class, String.valueOf(1));
	
	
	public static final CostTypeEnum 消费 = MDEnum.valueOf(CostTypeEnum.class, String.valueOf(-1));
	

}