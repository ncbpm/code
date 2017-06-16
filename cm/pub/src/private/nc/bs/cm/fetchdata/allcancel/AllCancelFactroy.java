package nc.bs.cm.fetchdata.allcancel;

import nc.vo.cm.fetchdata.enumeration.FetchDataObjEnum;

/**
 * ȫ��ȡ������
 * 
 * @since 6.0
 * @version Mar 28, 2012 2:51:27 PM
 * @author liwzh
 */
public class AllCancelFactroy {
    /**
     * ȡ�����͹����෽��
     * 
     * @param type
     *            ȡ������
     * @return IAllCancel ����ʵ�� ��
     */

    public IAllCancel createAllCancelFactory(Integer type) {

        IAllCancel allCancel = null;

        if (FetchDataObjEnum.PRODIN.equalsValue(type)) {
            allCancel = new ProdInFIAllCancel(); // ����Ʒ��ⵥ

        }
        else if (FetchDataObjEnum.MATERIALOUT.equalsValue(type)) {

            allCancel = new MaterialOutFIAllCancel(); // ���ϳ��ⵥ

        }
        else if (FetchDataObjEnum.ACT.equalsValue(type)) {
            allCancel = new MMAllCancel(); // ���������깤�������������������

        }
        else if (FetchDataObjEnum.SPOIL.value().equals(type)) {
            allCancel = new ScrapAllCancel(); // ����������ⵥ
        }
        else if (FetchDataObjEnum.GXWW.value().equals(type)) {
            allCancel = new WorkingProcessAllCancel();// ί��ӹ����õ�
        }
        else if (FetchDataObjEnum.DINGE.equalsValue(type)) {
        	//����
        	allCancel = new DingeAllCancel();
        } else if (FetchDataObjEnum.CHUYUN.equalsValue(type)) {
            // ����
        	allCancel = new ChuyunAllCancel();
        } else if (FetchDataObjEnum.JIANYAN.equalsValue(type)) {
            // ����
        	allCancel = new JianyanAllCancel();
        }else if (FetchDataObjEnum.HUANBAO.equalsValue(type)) {
            // ����
        	allCancel = new HuanbaoAllCancel();
        }
        return allCancel;

    }
}
