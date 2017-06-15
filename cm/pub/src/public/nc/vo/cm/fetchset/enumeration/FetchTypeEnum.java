package nc.vo.cm.fetchset.enumeration;

import nc.md.model.IEnumValue;
import nc.md.model.impl.MDEnum;

/**
 * ȡ������
 */
public class FetchTypeEnum extends MDEnum {
    /**
     * ������ö��ֵ
     */
    public static final int NOTEXIST = -1;

    /**
     * ���ϳ���
     */
    public static final FetchTypeEnum MATEROUT = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(1));

    /**
     * �깤���
     */
    public static final FetchTypeEnum OVERIN = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(2));

    /**
     * ��Ʒ
     */
    public static final FetchTypeEnum SPOIL = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(3));

    /**
     * ��ҵ
     */
    public static final FetchTypeEnum TASK = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(4));

    /**
     * ����ί��
     */
    public static final FetchTypeEnum GXWW = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(5));

    /**
     * ������������ĵ�
     */
    public static final FetchTypeEnum IASTUFF = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(6));

    /**
     * ����
     */
    public static final FetchTypeEnum DINGE = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(7));

    
    /**
     * ����
     */
    public static final FetchTypeEnum CHUYUN = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(8));

    /**
     * ����
     */
    public static final FetchTypeEnum JIANYAN = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(9));

    /**
     * ����
     */
    public static final FetchTypeEnum HUANBAO = MDEnum.valueOf(FetchTypeEnum.class, Integer.valueOf(10));

  
    
    public FetchTypeEnum(IEnumValue enumvalue) {
        super(enumvalue);
    }

    /**
     * ����ȡ������
     */
    public static int[] ALL_FETCHDATAOBJ = new int[] {
        FetchTypeEnum.MATEROUT.toIntValue(), FetchTypeEnum.OVERIN.toIntValue(), FetchTypeEnum.SPOIL.toIntValue(),
        FetchTypeEnum.TASK.toIntValue(), FetchTypeEnum.GXWW.toIntValue(), FetchTypeEnum.IASTUFF.toIntValue(),
        FetchTypeEnum.DINGE.toIntValue(),FetchTypeEnum.CHUYUN.toIntValue(),FetchTypeEnum.JIANYAN.toIntValue(),FetchTypeEnum.HUANBAO.toIntValue()
    };

}
