package nc.bs.cm.fetchdata.allcancel;

import nc.vo.cm.fetchdata.enumeration.FetchDataObjEnum;

/**
 * 全部取消工厂
 * 
 * @since 6.0
 * @version Mar 28, 2012 2:51:27 PM
 * @author liwzh
 */
public class AllCancelFactroy {
    /**
     * 取数类型工厂类方法
     * 
     * @param type
     *            取数类型
     * @return IAllCancel 具体实现 类
     */

    public IAllCancel createAllCancelFactory(Integer type) {

        IAllCancel allCancel = null;

        if (FetchDataObjEnum.PRODIN.equalsValue(type)) {
            allCancel = new ProdInFIAllCancel(); // 产成品入库单

        }
        else if (FetchDataObjEnum.MATERIALOUT.equalsValue(type)) {

            allCancel = new MaterialOutFIAllCancel(); // 材料出库单

        }
        else if (FetchDataObjEnum.ACT.equalsValue(type)) {
            allCancel = new MMAllCancel(); // 生产制造完工报告或者生产报告数据

        }
        else if (FetchDataObjEnum.SPOIL.value().equals(type)) {
            allCancel = new ScrapAllCancel(); // 生产报废入库单
        }
        else if (FetchDataObjEnum.GXWW.value().equals(type)) {
            allCancel = new WorkingProcessAllCancel();// 委外加工费用单
        }
        else if (FetchDataObjEnum.DINGE.equalsValue(type)) {
        	//定额
        	allCancel = new DingeAllCancel();
        } else if (FetchDataObjEnum.CHUYUN.equalsValue(type)) {
            // 储运
        	allCancel = new ChuyunAllCancel();
        } else if (FetchDataObjEnum.JIANYAN.equalsValue(type)) {
            // 检验
        	allCancel = new JianyanAllCancel();
        }else if (FetchDataObjEnum.HUANBAO.equalsValue(type)) {
            // 环保
        	allCancel = new HuanbaoAllCancel();
        }
        return allCancel;

    }
}
