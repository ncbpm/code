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
 * ȡ�����͹���--�������ȡ������������ȡ��
 */
public class FetchTypeFactroy {
    /**
     * ȡ�����͹����෽��
     *
     * @param type
     *            ȡ������
     * @return ICheckAndFetch ����ʵ�� ��
     */
    public ICheckAndFetch createFetchTypeFactory(Integer type, List<String> cycObjMaterialLst, Set<String> iaMaterialSet) {
        ICheckAndFetch typeset = null;
        if (FetchDataObjEnum.PRODIN.equalsValue(type)) {
            // ����Ʒ��ⵥ
            typeset = new ProdInFIFetch();
        }
        else if (FetchDataObjEnum.MATERIALOUT.equalsValue(type)) {
            // ���ϳ��ⵥ
            typeset = new MaterialOutFIFetch2();
        }
        else if (FetchDataObjEnum.MATERIALOUT_COSTTRAN.equalsValue(type)) {
            // �ɱ���ת���»�ȡ���ϳ��ⵥ
            typeset = new CostTranReGetMaterial(cycObjMaterialLst, iaMaterialSet);
        }
        else if (FetchDataObjEnum.ACT.equalsValue(type)) {
            // ���������깤�������������������
            typeset = new MMFetch();
        }

        else if (FetchDataObjEnum.SPOIL.value().equals(type)) {
            // ��Ʒȡ��--����������ⵥ
            typeset = new ScrapFetch();
        }
        else if (FetchDataObjEnum.GXWW.equalsValue(type)) {
            // ��������ȡ��----����ί����㵥
            typeset = new WorkingProcessFetch();
        }
        else if (FetchDataObjEnum.ISSTUFF.equalsValue(type)) {
            // �������ȡ��----������������ĵ�
            typeset = new IastuffFetch();
        } else if (FetchDataObjEnum.DINGE.equalsValue(type)) {
        	//����
            typeset = new DingeFIFetch();
        } else if (FetchDataObjEnum.CHUYUN.equalsValue(type)) {
            // ����
            typeset = new ChuyunFIFetch();
        } else if (FetchDataObjEnum.JIANYAN.equalsValue(type)) {
            // ����
            typeset = new JianyanFIFetch();
        }else if (FetchDataObjEnum.HUANBAO.equalsValue(type)) {
            // ����
            typeset = new HuanbaoFIFetch();
        }
        return typeset;
    }
}
