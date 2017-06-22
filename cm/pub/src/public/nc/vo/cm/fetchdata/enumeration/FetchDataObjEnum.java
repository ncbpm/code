package nc.vo.cm.fetchdata.enumeration;

import java.util.HashMap;
import java.util.Map;

import nc.md.model.IEnumValue;
import nc.md.model.impl.MDEnum;
import nc.vo.cm.fetchset.enumeration.FetchTypeEnum;

/**
 * 取数对象的下拉列表
 * <p>
 * 包括：材料出库单和产成品入库单
 */
public class FetchDataObjEnum extends MDEnum {
    /**
     * 构造方法
     */
    public FetchDataObjEnum(IEnumValue enumvalue) {
        super(enumvalue);
    }
    /**
     * 材料出库
     */
    public static final FetchDataObjEnum MATERIALOUT = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(1));

    /**
     * 产成品入库
     */
    public static final FetchDataObjEnum PRODIN = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(2));

    /**
     * 其它出入库
     */
    public static final FetchDataObjEnum ISSTUFF = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(6));

    /**
     * 作业量
     */
    public static final FetchDataObjEnum ACT = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(4));

    /**
     * 工序委外
     */
    public static final FetchDataObjEnum GXWW = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(5));

    /**
     * 废品
     */
    public static final FetchDataObjEnum SPOIL = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(3));

    /**
     * 成本结转中重新获取材料出库单
     */
    public static final FetchDataObjEnum MATERIALOUT_COSTTRAN = MDEnum.valueOf(FetchDataObjEnum.class,
            Integer.valueOf(9));

    /**
     *定额
     */
    public static final FetchDataObjEnum DINGE = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(7));
    
    /**
     *储运
     */
    public static final FetchDataObjEnum CHUYUN = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(8));
    
    /**
     * 检验
     */
    public static final FetchDataObjEnum JIANYAN = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(10));
    
    /**
     * 环保
     */
    public static final FetchDataObjEnum HUANBAO = MDEnum.valueOf(FetchDataObjEnum.class, Integer.valueOf(11));
    
    public static final Integer ALL = 0;

    /**
     * 所有取数对象
     */
    public static int[] ALL_FETCHDATAOBJ = new int[] {
        FetchDataObjEnum.MATERIALOUT.toIntValue(), FetchDataObjEnum.PRODIN.toIntValue(),
        FetchDataObjEnum.SPOIL.toIntValue(), FetchDataObjEnum.ACT.toIntValue(), FetchDataObjEnum.GXWW.toIntValue(),
        FetchDataObjEnum.ISSTUFF.toIntValue(),FetchDataObjEnum.DINGE.toIntValue(),FetchDataObjEnum.CHUYUN.toIntValue(),
        FetchDataObjEnum.JIANYAN.toIntValue(),FetchDataObjEnum.HUANBAO.toIntValue()
    };

    /**
     * 取数对象：来源于存货核算系统
     */
    public static int[] IA_FETCHDATAOBJ = new int[] {
        FetchDataObjEnum.MATERIALOUT.toIntValue(), FetchDataObjEnum.PRODIN.toIntValue(),
        FetchDataObjEnum.ISSTUFF.toIntValue()
    };

    /**
     * 取数对象：来源于库存系统
     */
    public static int[] IC_FETCHDATAOBJ = new int[] {
        FetchDataObjEnum.SPOIL.toIntValue()
    };

    /**
     * 取数对象：来源于生产制造系统---流程制造、离散制造
     */
    public static int[] MMPAC_FETCHDATAOBJ = new int[] {
        FetchDataObjEnum.ACT.toIntValue()
    };

    /**
     * 取数对象：来源于生产制造系统---工序委外管理
     */
    public static int[] MMPSC_FETCHDATAOBJ = new int[] {
        FetchDataObjEnum.GXWW.toIntValue()
    };

    /**
     * 获取"取数类型"与"取数对象"对应关系
     *
     * @return map{key=取数类型,value=取数对象}
     */
    public static Map<Integer, Integer> getFetchTypeAndFetchObjRelationMap() {
        Map<Integer, Integer> relationMap = new HashMap<Integer, Integer>();
        int[] allFetchType = FetchTypeEnum.ALL_FETCHDATAOBJ;
        int[] allFetchObj = FetchDataObjEnum.ALL_FETCHDATAOBJ;
        for (int i = 0; i < allFetchType.length; i++) {
            // key=取数类型,value=取数对象
            relationMap.put(allFetchType[i], allFetchObj[i]);
        }

        return relationMap;
    }
}
