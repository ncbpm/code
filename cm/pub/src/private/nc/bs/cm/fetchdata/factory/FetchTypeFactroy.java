package nc.bs.cm.fetchdata.factory;

import java.util.List;
import java.util.Set;

import nc.bs.cm.fetchdata.checkandfetch.ChuyunFIFetch;
import nc.bs.cm.fetchdata.checkandfetch.CostTranReGetMaterial;
import nc.bs.cm.fetchdata.checkandfetch.DingeFIFetch;
import nc.bs.cm.fetchdata.checkandfetch.HuanbaoFIFetch;
import nc.bs.cm.fetchdata.checkandfetch.ICheckAndFetch;
import nc.bs.cm.fetchdata.checkandfetch.IastuffFetch;
import nc.bs.cm.fetchdata.checkandfetch.JianyanFIFetch;
import nc.bs.cm.fetchdata.checkandfetch.MMFetch;
import nc.bs.cm.fetchdata.checkandfetch.MaterialOutFIFetch2;
import nc.bs.cm.fetchdata.checkandfetch.ProdInFIFetch;
import nc.bs.cm.fetchdata.checkandfetch.ScrapFetch;
import nc.bs.cm.fetchdata.checkandfetch.WorkingProcessFetch;
import nc.vo.cm.fetchdata.enumeration.FetchDataObjEnum;

/**
 * 取数类型工厂--存货核算取数，生产制造取数
 */
public class FetchTypeFactroy {
    /**
     * 取数类型工厂类方法
     *
     * @param type
     *            取数类型
     * @return ICheckAndFetch 具体实现 类
     */
    public ICheckAndFetch createFetchTypeFactory(Integer type, List<String> cycObjMaterialLst, Set<String> iaMaterialSet) {
        ICheckAndFetch typeset = null;
        if (FetchDataObjEnum.PRODIN.equalsValue(type)) {
            // 产成品入库单
            typeset = new ProdInFIFetch();
        }
        else if (FetchDataObjEnum.MATERIALOUT.equalsValue(type)) {
            // 材料出库单
            typeset = new MaterialOutFIFetch2();
        }
        else if (FetchDataObjEnum.MATERIALOUT_COSTTRAN.equalsValue(type)) {
            // 成本结转重新获取材料出库单
            typeset = new CostTranReGetMaterial(cycObjMaterialLst, iaMaterialSet);
        }
        else if (FetchDataObjEnum.ACT.equalsValue(type)) {
            // 生产制造完工报告或者生产报告数据
            typeset = new MMFetch();
        }

        else if (FetchDataObjEnum.SPOIL.value().equals(type)) {
            // 废品取数--生产报废入库单
            typeset = new ScrapFetch();
        }
        else if (FetchDataObjEnum.GXWW.equalsValue(type)) {
            // 生产制造取数----工序委外结算单
            typeset = new WorkingProcessFetch();
        }
        else if (FetchDataObjEnum.ISSTUFF.equalsValue(type)) {
            // 存货核算取数----其他出入库消耗单
            typeset = new IastuffFetch();
        } else if (FetchDataObjEnum.DINGE.equalsValue(type)) {
        	//定额
            typeset = new DingeFIFetch();
        } else if (FetchDataObjEnum.CHUYUN.equalsValue(type)) {
            // 储运
            typeset = new ChuyunFIFetch();
        } else if (FetchDataObjEnum.JIANYAN.equalsValue(type)) {
            // 检验
            typeset = new JianyanFIFetch();
        }else if (FetchDataObjEnum.HUANBAO.equalsValue(type)) {
            // 环保
            typeset = new HuanbaoFIFetch();
        }
        return typeset;
    }
}
