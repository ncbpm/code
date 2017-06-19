package nc.vo.mmpac.huanbao;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 *   此处添加累的描述信息
 * </p>
 *  创建日期:2017-6-19
 * @author 
 * @version NCPrj ??
 */
 
public class HuanbaoBvo extends SuperVO {
	
/**
*上层单据主键
*/
public static final String PK_HEAD="pk_head";
/**
*时间戳
*/
public static final String TS="ts";;
    
    
/**
* 属性 生成上层主键的Getter方法.属性名：上层主键
*  创建日期:2017-6-19
* @return String
*/
public String getPk_head(){
return (String)this.getAttributeValue(HuanbaoBvo.PK_HEAD);
}
/**
* 属性生成上层主键的Setter方法.属性名：上层主键
* 创建日期:2017-6-19
* @param newPk_head String
*/
public void setPk_head(String pk_head){
this.setAttributeValue(HuanbaoBvo.PK_HEAD,pk_head);
} 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2017-6-19
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return (UFDateTime)this.getAttributeValue(HuanbaoBvo.TS);
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2017-6-19
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
    