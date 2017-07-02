/**
 * $文件说明$
 * 
 * @author chennn
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-4-16 上午08:51:09
 */
package nc.vo.ic.batchcode;

import nc.vo.ic.general.define.MetaNameConst;
import nc.vo.ic.pub.define.ICPubMetaNameConst;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author chennn
 * @time 2010-4-16 上午08:51:09
 */
public class ICBatchFields extends AbstractBatchFieldMap {

  /**
   * 父类方法重写
   * 
   * @see nc.vo.ic.batchcode.AbstractBatchFieldMap#getBillFields()
   */

  // //批次内容相关字段，包含版本号
  // public static final String[] batchSelFields = new String[] {
  // BatchcodeVO.PK_GROUP,
  // BatchcodeVO.PK_BATCHCODE, BatchcodeVO.CMATERIALVID,
  // BatchcodeVO.CMATERIALOID, BatchcodeVO.VBATCHCODE,
  // BatchcodeVO.VVENDBATCHCODE, BatchcodeVO.TCHECKTIME,
  // BatchcodeVO.CQUALITYLEVELID, BatchcodeVO.DPRODUCEDATE,
  // BatchcodeVO.DVALIDATE, BatchcodeVO.BSEAL, BatchcodeVO.TBATCHTIME,
  // BatchcodeVO.VNOTE, BatchcodeVO.BINQC,
  // BatchcodeVO.VERSION,BatchcodeVO.VDEF1,
  // BatchcodeVO.VDEF2,BatchcodeVO.VDEF3,BatchcodeVO.VDEF4,BatchcodeVO.VDEF5,
  // BatchcodeVO.VDEF6,BatchcodeVO.VDEF7,BatchcodeVO.VDEF8,BatchcodeVO.VDEF9,
  // BatchcodeVO.VDEF10,BatchcodeVO.VDEF11,BatchcodeVO.VDEF12,BatchcodeVO.VDEF13,
  // BatchcodeVO.VDEF14,BatchcodeVO.VDEF15,BatchcodeVO.VDEF16,BatchcodeVO.VDEF17,
  // BatchcodeVO.VDEF18,BatchcodeVO.VDEF19,BatchcodeVO.VDEF20,BatchcodeVO.TS
  //
  // };

  @Override
  public String[] getBillFields() {

    String[] billFields =
        new String[] {
          ICPubMetaNameConst.PK_GROUP, ICPubMetaNameConst.PK_BATCHCODE,
          ICPubMetaNameConst.CMATERIALVID, ICPubMetaNameConst.CMATERIALOID,
          ICPubMetaNameConst.VBATCHCODE, MetaNameConst.VVENDBATCHCODE,
          MetaNameConst.TCHECKTIME, ICPubMetaNameConst.CQUALITYLEVELID,
          ICPubMetaNameConst.DPRODUCEDATE, ICPubMetaNameConst.DVALIDATE,
          MetaNameConst.BCSEAL, null, MetaNameConst.VBATCHCODENOTE, null,
          MetaNameConst.IBCVERSION, MetaNameConst.NPRICE,
          MetaNameConst.VBCDEF2, MetaNameConst.VBCDEF3, MetaNameConst.VBCDEF4,
          MetaNameConst.VBCDEF5, MetaNameConst.VBCDEF6, MetaNameConst.VBCDEF7,
          MetaNameConst.VBCDEF8, MetaNameConst.VBCDEF9, MetaNameConst.VBCDEF10,
          MetaNameConst.VBCDEF11, MetaNameConst.VBCDEF12,
          MetaNameConst.VBCDEF13, MetaNameConst.VBCDEF14,
          MetaNameConst.VBCDEF15, MetaNameConst.VBCDEF16,
          MetaNameConst.VBCDEF17, MetaNameConst.VBCDEF18,
          MetaNameConst.VBCDEF19, MetaNameConst.VBCDEF20, MetaNameConst.TBCTS,
          null, MetaNameConst.DINBOUNDDATE
        };
    return billFields;
  }
  
  /**
   * 获取批次在单据相关的计算属性
   * 
   * @return
   */
  public String[] getBatchCalculateAttr(){
    return new String[]{ MetaNameConst.VVENDBATCHCODE,
        MetaNameConst.TCHECKTIME, ICPubMetaNameConst.CQUALITYLEVELID,
        ICPubMetaNameConst.DPRODUCEDATE, ICPubMetaNameConst.DVALIDATE,
        MetaNameConst.BCSEAL, MetaNameConst.VBATCHCODENOTE, 
        MetaNameConst.IBCVERSION,  MetaNameConst.NPRICE,
        MetaNameConst.VBCDEF2, MetaNameConst.VBCDEF3, MetaNameConst.VBCDEF4,
        MetaNameConst.VBCDEF5, MetaNameConst.VBCDEF6, MetaNameConst.VBCDEF7,
        MetaNameConst.VBCDEF8, MetaNameConst.VBCDEF9, MetaNameConst.VBCDEF10,
        MetaNameConst.VBCDEF11, MetaNameConst.VBCDEF12,
        MetaNameConst.VBCDEF13, MetaNameConst.VBCDEF14,
        MetaNameConst.VBCDEF15, MetaNameConst.VBCDEF16,
        MetaNameConst.VBCDEF17, MetaNameConst.VBCDEF18,
        MetaNameConst.VBCDEF19, MetaNameConst.VBCDEF20, MetaNameConst.TBCTS,
        /*ICPubMetaNameConst.VPRODBATCHCODE,*/ MetaNameConst.DINBOUNDDATE};
  }

}
