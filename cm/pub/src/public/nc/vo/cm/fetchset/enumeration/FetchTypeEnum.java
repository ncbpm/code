package nc.vo.cm.fetchset.enumeration;

import nc.md.model.IEnumValue;
import nc.md.model.impl.MDEnum;

/**
 * 取数类型
 */
public class FetchTypeEnum extends MDEnum {
    /**
     * 不存在枚举值
     */
    public static final int NOTEXIST = -1;

    /**
     * 材料出库
     */
    public static final FetchTypeEnum MATEROUT = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(1));

    /**
     * 完工入库
     */
    public static final FetchTypeEnum OVERIN = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(2));

    /**
     * 废品
     */
    public static final FetchTypeEnum SPOIL = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(3));

    /**
     * 作业
     */
    public static final FetchTypeEnum TASK = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(4));

    /**
     * 工序委外
     */
    public static final FetchTypeEnum GXWW = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(5));

    /**
     * 其他出入库消耗单
     */
    public static final FetchTypeEnum IASTUFF = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(6));

    /**
     * 定额
     */
    public static final FetchTypeEnum DINGE = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(7));

    
    /**
     * 储运
     */
    public static final FetchTypeEnum CHUYUN = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(8));

    /**
     * 检验
     */
    public static final FetchTypeEnum JIANYAN = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(9));

    /**
     * 环保
     */
    public static final FetchTypeEnum HUANBAO = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(10));

  
    
    public FetchTypeEnum(IEnumValue enumvalue) {
        super(enumvalue);
    }

    /**
     * 所有取数类型
     */
    public static int[] ALL_FETCHDATAOBJ = new int[] {
        FetchTypeEnum.MATEROUT.toIntValue(), FetchTypeEnum.OVERIN.toIntValue(), FetchTypeEnum.SPOIL.toIntValue(),
        FetchTypeEnum.TASK.toIntValue(), FetchTypeEnum.GXWW.toIntValue(), FetchTypeEnum.IASTUFF.toIntValue(),
        FetchTypeEnum.DINGE.toIntValue(),FetchTypeEnum.CHUYUN.toIntValue(),FetchTypeEnum.JIANYAN.toIntValue(),FetchTypeEnum.HUANBAO.toIntValue()
    };

}
