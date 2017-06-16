package nc.bs.cm.fetchdata.factory;

import nc.bs.cm.fetchdata.uidataset.ChuyunSchemaIaStuff;
import nc.bs.cm.fetchdata.uidataset.DataSetPeriodSchemaAct;
import nc.bs.cm.fetchdata.uidataset.DataSetPeriodSchemaFI;
import nc.bs.cm.fetchdata.uidataset.DataSetPeriodSchemaIaStuff;
import nc.bs.cm.fetchdata.uidataset.DataSetPeriodSchemaScrap;
import nc.bs.cm.fetchdata.uidataset.DataSetPeriodSchemaWorkingProcess;
import nc.bs.cm.fetchdata.uidataset.DataSetWeekSchema;
import nc.bs.cm.fetchdata.uidataset.DingeSchemaIaStuff;
import nc.bs.cm.fetchdata.uidataset.HuanbaoSchemaIaStuff;
import nc.bs.cm.fetchdata.uidataset.IUIDataSet;
import nc.bs.cm.fetchdata.uidataset.JianyanSchemaIaStuff;
import nc.vo.cm.fetchdata.enumeration.FetchDataObjEnum;
import nc.vo.cm.fetchdata.enumeration.FetchDataSchemaEnum;

/**
 * 取数方案工厂类
 */
public class FetchSchemaFactory {

    /**
     * 取数方案工厂类方法
     * 
     * @param schema
     *            方案
     * @param ifetchobjtype 取数对象
     *            参照FetchDataObjEnum
     * @return IUIDataSet 具体实现 类
     */
    public IUIDataSet createSchemaFactory(Integer schema, Integer ifetchobjtype) {
        IUIDataSet schemaset = null;
        // 如果是存货核算取数(其他出入库)
        if (FetchDataObjEnum.ISSTUFF.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))) {
            return new DataSetPeriodSchemaIaStuff();
        }
        // 如果是制造取数(工序委外结算单)
        else if (FetchDataObjEnum.GXWW.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))) {
            return new DataSetPeriodSchemaWorkingProcess();
        }

        // 取数方案
        if (FetchDataSchemaEnum.PERIODSCHEMA.equalsValue(schema)) {
            if (FetchDataObjEnum.MATERIALOUT.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))
                    || FetchDataObjEnum.PRODIN.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))) {
                // 存货核算取数(材料出、产成品入)
                schemaset = new DataSetPeriodSchemaFI();
            }
            else if (FetchDataObjEnum.SPOIL.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))) {
                // 库存取数(废品)
                schemaset = new DataSetPeriodSchemaScrap();
            }
            else if (FetchDataObjEnum.ACT.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))) {
                // 生产制造取数(作业)
                schemaset = new DataSetPeriodSchemaAct();
            }
        }
        else if (FetchDataSchemaEnum.WEEKSCHEMA.equalsValue(schema)) {
            schemaset = new DataSetWeekSchema();
        }
       //2017-06-15 liyf 新增集中方式
       if(FetchDataObjEnum.DINGE.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))){
           return new DingeSchemaIaStuff();
       }
       if(FetchDataObjEnum.CHUYUN.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))){
           return new ChuyunSchemaIaStuff();
       }
       if(FetchDataObjEnum.JIANYAN.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))){
           return new JianyanSchemaIaStuff();
       }
       if(FetchDataObjEnum.HUANBAO.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))){
           return new HuanbaoSchemaIaStuff();
       }
     
        return schemaset;
    }
}
