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
 
public class HuanbaoHVO extends SuperVO {
	
/**
*ʱ���
*/
public static final String TS="ts";;
    
    
/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2017-6-19
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return (UFDateTime)this.getAttributeValue(HuanbaoHVO.TS);
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2017-6-19
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.setAttributeValue(HuanbaoHVO.TS,ts);
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("mmpac.huanbaoh");
    }
   }
    