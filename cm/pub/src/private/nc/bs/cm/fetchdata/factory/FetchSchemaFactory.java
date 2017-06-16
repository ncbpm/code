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
 * ȡ������������
 */
public class FetchSchemaFactory {

    /**
     * ȡ�����������෽��
     * 
     * @param schema
     *            ����
     * @param ifetchobjtype ȡ������
     *            ����FetchDataObjEnum
     * @return IUIDataSet ����ʵ�� ��
     */
    public IUIDataSet createSchemaFactory(Integer schema, Integer ifetchobjtype) {
        IUIDataSet schemaset = null;
        // ����Ǵ������ȡ��(���������)
        if (FetchDataObjEnum.ISSTUFF.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))) {
            return new DataSetPeriodSchemaIaStuff();
        }
        // ���������ȡ��(����ί����㵥)
        else if (FetchDataObjEnum.GXWW.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))) {
            return new DataSetPeriodSchemaWorkingProcess();
        }

        // ȡ������
        if (FetchDataSchemaEnum.PERIODSCHEMA.equalsValue(schema)) {
            if (FetchDataObjEnum.MATERIALOUT.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))
                    || FetchDataObjEnum.PRODIN.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))) {
                // �������ȡ��(���ϳ�������Ʒ��)
                schemaset = new DataSetPeriodSchemaFI();
            }
            else if (FetchDataObjEnum.SPOIL.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))) {
                // ���ȡ��(��Ʒ)
                schemaset = new DataSetPeriodSchemaScrap();
            }
            else if (FetchDataObjEnum.ACT.getEnumValue().getValue().equals(String.valueOf(ifetchobjtype))) {
                // ��������ȡ��(��ҵ)
                schemaset = new DataSetPeriodSchemaAct();
            }
        }
        else if (FetchDataSchemaEnum.WEEKSCHEMA.equalsValue(schema)) {
            schemaset = new DataSetWeekSchema();
        }
       //2017-06-15 liyf �������з�ʽ
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
