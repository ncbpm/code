package nc.vo.mmpac.huanbao;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 *   �˴�����۵�������Ϣ
 * </p>
 *  ��������:2017-6-19
 * @author 
 * @version NCPrj ??
 */
 
public class HuanbaoBvo extends SuperVO {
	
/**
*�ϲ㵥������
*/
public static final String PK_HEAD="pk_head";
/**
*ʱ���
*/
public static final String TS="ts";;
    
    
/**
* ���� �����ϲ�������Getter����.���������ϲ�����
*  ��������:2017-6-19
* @return String
*/
public String getPk_head(){
return (String)this.getAttributeValue(HuanbaoBvo.PK_HEAD);
}
/**
* ���������ϲ�������Setter����.���������ϲ�����
* ��������:2017-6-19
* @param newPk_head String
*/
public void setPk_head(String pk_head){
this.setAttributeValue(HuanbaoBvo.PK_HEAD,pk_head);
} 
/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2017-6-19
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return (UFDateTime)this.getAttributeValue(HuanbaoBvo.TS);
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2017-6-19
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.setAttributeValue(HuanbaoBvo.TS,ts);
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("mmpac.huanbaob");
    }
   }
    