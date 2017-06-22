package nc.vo.cm.fetchdata.enumeration;

import java.util.HashMap;
import java.util.Map;

import nc.md.model.IEnumValue;
import nc.md.model.impl.MDEnum;
import nc.vo.cm.fetchset.enumeration.FetchTypeEnum;

/**
 * ȡ������������б�
 * <p>
 * ���������ϳ��ⵥ�Ͳ���Ʒ��ⵥ
 */
public class FetchDataObjEnum extends MDEnum {
    /**
     * ���췽��
     */
    public FetchDataObjEnum(IEnumValue enumvalue) {
        super(enumvalue);
    }
    /**
     * ���ϳ���
     */
    public static final FetchDataObjEnum MATERIALOUT = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(1));

    /**
     * ����Ʒ���
     */
    public static final FetchDataObjEnum PRODIN = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(2));

    /**
     * ���������
     */
    public static final FetchDataObjEnum ISSTUFF = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(6));

    /**
     * ��ҵ��
     */
    public static final FetchDataObjEnum ACT = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(4));

    /**
     * ����ί��
     */
    public static final FetchDataObjEnum GXWW = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(5));

    /**
     * ��Ʒ
     */
    public static final FetchDataObjEnum SPOIL = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(3));

    /**
     * �ɱ���ת�����»�ȡ���ϳ��ⵥ
     */
    public static final FetchDataObjEnum MATERIALOUT_COSTTRAN = MDEnum.valueOf(FetchDataObjEnum.class,
            Integer.valueOf(9));

    /**
     *����
     */
    public static final FetchDataObjEnum DINGE = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(7));
    
    /**
     *����
     */
    public static final FetchDataObjEnum CHUYUN = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(8));
    
    /**
     * ����
     */
    public static final FetchDataObjEnum JIANYAN = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(10));
    
    /**
     * ����
     */
    public static final FetchDataObjEnum HUANBAO = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(11));
    
    public static final Integer ALL = 0;

    /**
     * ����ȡ������
     */
    public static int[] ALL_FETCHDATAOBJ = new int[] {
        FetchDataObjEnum.MATERIALOUT.toIntValue(), FetchDataObjEnum.PRODIN.toIntValue(),
        FetchDataObjEnum.SPOIL.toIntValue(), FetchDataObjEnum.ACT.toIntValue(), FetchDataObjEnum.GXWW.toIntValue(),
        FetchDataObjEnum.ISSTUFF.toIntValue(),FetchDataObjEnum.DINGE.toIntValue(),FetchDataObjEnum.CHUYUN.toIntValue(),
        FetchDataObjEnum.JIANYAN.toIntValue(),FetchDataObjEnum.HUANBAO.toIntValue()
    };

    /**
     * ȡ��������Դ�ڴ������ϵͳ
     */
    public static int[] IA_FETCHDATAOBJ = new int[] {
        FetchDataObjEnum.MATERIALOUT.toIntValue(), FetchDataObjEnum.PRODIN.toIntValue(),
        FetchDataObjEnum.ISSTUFF.toIntValue()
    };

    /**
     * ȡ��������Դ�ڿ��ϵͳ
     */
    public static int[] IC_FETCHDATAOBJ = new int[] {
        FetchDataObjEnum.SPOIL.toIntValue()
    };

    /**
     * ȡ��������Դ����������ϵͳ---�������졢��ɢ����
     */
    public static int[] MMPAC_FETCHDATAOBJ = new int[] {
        FetchDataObjEnum.ACT.toIntValue()
    };

    /**
     * ȡ��������Դ����������ϵͳ---����ί�����
     */
    public static int[] MMPSC_FETCHDATAOBJ = new int[] {
        FetchDataObjEnum.GXWW.toIntValue()
    };

    /**
     * ��ȡ"ȡ������"��"ȡ������"��Ӧ��ϵ
     *
     * @return map{key=ȡ������,value=ȡ������}
     */
    public static Map<Integer, Integer> getFetchTypeAndFetchObjRelationMap() {
        Map<Integer, Integer> relationMap = new HashMap<Integer, Integer>();
        int[] allFetchType = FetchTypeEnum.ALL_FETCHDATAOBJ;
        int[] allFetchObj = FetchDataObjEnum.ALL_FETCHDATAOBJ;
        for (int i = 0; i < allFetchType.length; i++) {
            // key=ȡ������,value=ȡ������
            relationMap.put(allFetchType[i], allFetchObj[i]);
        }

        return relationMap;
    }
}
