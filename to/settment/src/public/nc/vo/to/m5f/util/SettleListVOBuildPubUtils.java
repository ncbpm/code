package nc.vo.to.m5f.util;

import java.util.ArrayList;
import java.util.List;

import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.bill.CombineBill;
import nc.vo.pubapp.calculator.HslParseUtil;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.pubapp.scale.ScaleUtils;
import nc.vo.to.m5f.entity.SettleListHeaderVO;
import nc.vo.to.m5f.entity.SettleListItemVO;
import nc.vo.to.m5f.entity.SettleListVO;
import nc.vo.to.m5f.entity.SettleQueryViewVO;
import nc.vo.to.m5n.entity.PSInViewVO;
import nc.vo.to.pub.para.ParaUtilsForTo;

import nc.itf.org.IOrgConst;
import nc.itf.scmpub.reference.uap.bd.currency.CurrencyRate;
import nc.itf.scmpub.reference.uap.para.SysParaInitQuery;

import nc.impl.pubapp.env.BSContext;

public class SettleListVOBuildPubUtils {

  /*
   * 对计税金额、税额、调入方计税金额、税额、不可抵扣税额进行按照数量分摊
   * 使用场景：由于结算中间界面显示的不是最明细的数据，而结算时是按照最明细进行结算的。
   * 因此如果多次出库时，结算时会将界面数据重新拆分，然后进行单价金额联动，重新计算最明细
   * 相关数据，当用户在中间界面修改上述五个字段后，由于不会反算单价、金额等字段，最后导致在
   * 结算的最明细单价金额联动时会重新把上述五个字段计算一遍，结果就是，用户手动改过的这几个
   * 字段不会保存住，而是变成了修改前的值。
   * 
   * 分摊方案：在结算时，按照最明细数量对这几个字段进行分摊处理，且最后一笔需要处理尾差
   */

  // 结算数量
  private UFDouble settlenum = UFDouble.ZERO_DBL;

  // 某些字段的多次汇总值，用来最后一次的尾差处理
  private SettleBuildTailDiffPara tailDiffPara = new SettleBuildTailDiffPara();

  /**
   * 
   * 功能说明： <li>组织结算VO的入口方法 <li>根据界面中间界面数据组织内部结算清单数据信息 <li>其中单边结算特殊处理 <li>
   * 对结算数量(nnumber)赋值有两个地方，单边赋值和非单边赋值，非单边赋值在单边赋值之后 因此，跟踪代码时需注意
   * 
   * @since 6.1
   * @version 2012-2-8下午02:18:22
   * @author 马震
   * 
   * @param dispModel
   * @param selectDispVOs
   * @param fgatherbyflag
   * @return SettleListVO[]
   */
  public SettleListVO[] buildSettleList(SettleGroupOutAssViewUtil gUtil,
      SettleOutInAssUtil dUtil, Integer fgatherbyflag) {
    List<SettleListVO> lstSettleList = new ArrayList<SettleListVO>();
    SettleQueryViewVO[] gatherVOs = gUtil.getGatherVOs();
    String nc001 =
        SysParaInitQuery.getParaString(BSContext.getInstance().getGroupID(),
            "NC001");
    // 全局本位币折算模式的值
    String nc002 = SysParaInitQuery.getParaString(IOrgConst.GLOBEORG, "NC002");

    for (SettleQueryViewVO dispvo : gatherVOs) {
      // 初始化分摊、尾差相关数据值
      this.initAllotTailDiffValue(dispvo);
      Integer gatherDetailKey = dispvo.getSortcolumn();
      List<SettleQueryViewVO> lstOutViewVOs =
          gUtil.getOutVOByKey(gatherDetailKey);
      for (SettleQueryViewVO outviewvo : lstOutViewVOs) {
        // 处理单边结算情况
        if (outviewvo.getBunilateralflag() != null
            && outviewvo.getBunilateralflag().booleanValue()) {
          SettleListVO resultVO =
              this.buildSettleListForSingle(dispvo, outviewvo, fgatherbyflag,
                  lstOutViewVOs.size(), nc001, nc002);
          lstSettleList.add(resultVO);
          continue;
        }

        String outInKey = outviewvo.getCbill_bid();
        List<PSInViewVO> lstInViewVOs = dUtil.getInVOByKey(outInKey);
        List<SettleListVO> resultVOs =
            this.buildSettleListForNormal(dispvo, outviewvo, lstInViewVOs,
                fgatherbyflag, lstOutViewVOs.size(), nc001, nc002);
        if (resultVOs != null && resultVOs.size() > 0) {
          lstSettleList.addAll(resultVOs);
        }
      }
    }
    return this.combineDetailSettleListVOs(lstSettleList);
  }

  /**
   * 
   * 功能说明： <li>根据待结算调出单和待结算调入单组织结算聚合vo
   * 
   * @since 6.1
   * @version 2012-2-8下午05:49:32
   * @author 马震
   * 
   * @param dispviewvo 界面显示VO
   * @param outviewvo 组织数据VO
   * @param lstInViewVOs 待结算调入单
   * @param fgatherbyflag 汇总结算口径
   * @param lstOutViewVOSize 组织VO长度
   * @param settlenum 结算数量
   * @param totalValue 用来处理尾差的相关字段汇总值
   * @return List<SettleListVO> 结算清单聚合VO
   */
  private List<SettleListVO> buildSettleListForNormal(
      SettleQueryViewVO dispviewvo, SettleQueryViewVO outviewvo,
      List<PSInViewVO> lstInViewVOs, Integer fgatherbyflag,
      int lstOutViewVOSize, String nc001, String nc002) {
    List<SettleListVO> resultVOs = new ArrayList<SettleListVO>();
    UFBoolean isTaxfirst =
        ParaUtilsForTo.getInstance().getTO01(
            BSContext.getInstance().getGroupID());
    for (PSInViewVO inViewVO : lstInViewVOs) {
      if (MathTool.isZero(inViewVO.getItem().getNnumber())) {
        continue;
      }

      SettleListVO resultvo = new SettleListVO();
      SettleListHeaderVO header = new SettleListHeaderVO();
      SettleListItemVO itemvo = new SettleListItemVO();
      resultvo.setParent(header);
      resultvo.setChildrenVO(new SettleListItemVO[] {
        itemvo
      });
      resultVOs.add(resultvo);
      header.setFgatheredbyflag(fgatherbyflag);

      this.setHeadValue(header, outviewvo, dispviewvo, nc001, nc002);
      this.setBodyValue(itemvo, dispviewvo, outviewvo);
      this.setBodyValueForIn(itemvo, inViewVO);
      CalCulateM5fPriceMny calm5fTool = new CalCulateM5fPriceMny();
      // 只有一笔
      if (lstOutViewVOSize == 1 && lstInViewVOs.size() == 1) {
        // this.setBodyValueOne(itemvo, dispviewvo, false);
        calm5fTool.calculateItemGlobalGroup(header, itemvo);
        continue;
      }
      this.settlenum =
          MathTool.add(this.settlenum, inViewVO.getItem().getNnumber());
      // 则为最后一笔，进行尾差处理
      if (MathTool.equals(this.settlenum, dispviewvo.getNnumber())) {
        this.setBodyValueLast(header, itemvo, dispviewvo, false, calm5fTool);
        continue;
      }
      // 调用对应的单价金额算法
      this.setBodyValueNormal(header, itemvo, isTaxfirst,
          dispviewvo.getNnumber(), inViewVO.getItem().getNnumber(), false,
          calm5fTool);
      calm5fTool.calculateItemGlobalGroup(header, itemvo);
    }

    return resultVOs;
  }

  /**
   * 
   * 功能说明： <li>根据界面vo和待结算调出vos（组织vos）组织结算vo
   * 
   * @since 6.1
   * @version 2012-2-10上午08:49:39
   * @author 马震
   * 
   * @param outviewvo
   * @param dispvo
   * @param outviewvo
   * @param fgatherbyflag
   * @param totalValue
   * @param settlenum
   * @param outviewvosize
   * @return SettleListVO
   */
  private SettleListVO buildSettleListForSingle(SettleQueryViewVO dispvo,
      SettleQueryViewVO outviewvo, Integer fgatherbyflag, int outviewvosize,
      String nc001, String nc002) {
    if (MathTool.isZero(outviewvo.getNnumber())) {
      return null;
    }
    UFBoolean isTaxfirst =
        ParaUtilsForTo.getInstance().getTO01(
            BSContext.getInstance().getGroupID());
    SettleListVO resultvo = new SettleListVO();
    SettleListHeaderVO header = new SettleListHeaderVO();
    SettleListItemVO itemvo = new SettleListItemVO();
    resultvo.setParent(header);
    resultvo.setChildrenVO(new SettleListItemVO[] {
      itemvo
    });
    header.setFgatheredbyflag(fgatherbyflag);
    this.setHeadValue(header, outviewvo, dispvo, nc001, nc002);
    this.setBodyValue(itemvo, dispvo, outviewvo);
    CalCulateM5fPriceMny calm5fTool = new CalCulateM5fPriceMny();
    if (outviewvosize == 1) {
      // this.setBodyValueOne(itemvo, dispvo, true);
      calm5fTool.calculateItemGlobalGroup(header, itemvo);
      return resultvo;
    }
    this.settlenum = MathTool.add(this.settlenum, outviewvo.getNnumber());
    if (MathTool.equals(this.settlenum, dispvo.getNnumber())) {
      this.setBodyValueLast(header, itemvo, dispvo, true, calm5fTool);
      return resultvo;
    }
    // 调用对应的单价金额算法
    this.setBodyValueNormal(header, itemvo, isTaxfirst, dispvo.getNnumber(),
        outviewvo.getNnumber(), true, calm5fTool);
    calm5fTool.calculateItemGlobalGroup(header, itemvo);
    return resultvo;
  }

  /**
   * 
   * 功能说明： <li>对结算清单进行合单操作
   * 
   * @since 6.1
   * @version 2012-3-2下午04:16:50
   * @author 马震
   * 
   * @param lstSettleList
   * @return SettleListVO[]
   */
  private SettleListVO[] combineDetailSettleListVOs(
      List<SettleListVO> lstSettleList) {
    if (null == lstSettleList || lstSettleList.size() == 0) {
      return new SettleListVO[0];
    }
    SettleListVO[] arrDetail = new SettleListVO[lstSettleList.size()];
    lstSettleList.toArray(arrDetail);
    CombineBill<SettleListVO> cbutil = new CombineBill<SettleListVO>();
    String[] splitKeys = this.getSplitBillKeyFields();
    for (String itemKey : splitKeys) {
      cbutil.appendKey(itemKey);
    }
    return cbutil.combine(arrDetail);
  }

  private void fillGlobalChangeRate(SettleListHeaderVO head, String nc002) {

    if (PubAppTool.isNull(nc002)
        || nc002.equals("不启用全局本位币" /* -=notranslate=- */)
        || head.getNglobalexchgrate() != null) {
      return;
    }
    UFDouble rate = UFDouble.ZERO_DBL;
    if (nc002.equals("基于原币计算"/* -=notranslate=- */)) {
      rate =
          CurrencyRate.getGlobalLocalCurrencyRate(head.getCorigcurrencyid(),
              head.getDbilldate());
    }
    else {
      rate =
          CurrencyRate.getGlobalLocalCurrencyRate(head.getCcurrencyid(),
              head.getDbilldate());
    }
    head.setNglobalexchgrate(rate);

  }

  private void fillGroupChangeRate(SettleListHeaderVO head, String nc001) {

    if (PubAppTool.isNull(nc001)
        || nc001.equals("不启用集团本位币" /* -=notranslate=- */)
        || head.getNgroupexchgrate() != null) {
      return;
    }
    UFDouble rate = UFDouble.ZERO_DBL;
    if (nc001.equals("基于原币计算"/* -=notranslate=- */)) {
      rate =
          CurrencyRate.getGroupLocalCurrencyRate(head.getCorigcurrencyid(),
              head.getDbilldate());
    }
    else {
      rate =
          CurrencyRate.getGroupLocalCurrencyRate(head.getCcurrencyid(),
              head.getDbilldate());
    }
    head.setNgroupexchgrate(rate);

  }

  /**
   * 
   * 功能说明： <li>合单汇总字段
   * 
   * @since 6.1
   * @version 2012-3-2下午04:17:06
   * @author 马震
   * 
   * @return String[]
   */
  private String[] getSplitBillKeyFields() {
    String[] splitKeyFields =
        new String[] {
          SettleListHeaderVO.PK_ORG, SettleListHeaderVO.PK_ORG_V,
          SettleListHeaderVO.BUNILATERALFLAG,
          SettleListHeaderVO.CTARGETFIORGID,
          SettleListHeaderVO.CTARGETFIORGVID, SettleListHeaderVO.CCURRENCYID,
          SettleListHeaderVO.CSETTLEPATHID, SettleListHeaderVO.CORIGCURRENCYID,
          SettleListHeaderVO.COUTCOUNTRYID, SettleListHeaderVO.CINCOUNTRYID,
          SettleListHeaderVO.COUTTAXCOUNTRYID,
          SettleListHeaderVO.FOUTBUYSELLFLAG,
          SettleListHeaderVO.BOUTTRIATRADEFLAG,
          SettleListHeaderVO.CINTAXCOUNTRYID,
          SettleListHeaderVO.FINBUYSELLFLAG,
          SettleListHeaderVO.BINTRIATRADEFLAG,

        };
    return splitKeyFields;
  }

  private void initAllotTailDiffValue(SettleQueryViewVO dispvo) {
    // 结算数量
    this.settlenum = UFDouble.ZERO_DBL;
    // 某些字段的多次汇总值，用来最后一次的尾差处理
    this.tailDiffPara.setNorigmny(UFDouble.ZERO_DBL);
    this.tailDiffPara.setNorigtaxmny(UFDouble.ZERO_DBL);
    this.tailDiffPara.setNinorigmny(UFDouble.ZERO_DBL);
    this.tailDiffPara.setNcaltaxmny(UFDouble.ZERO_DBL);
    this.tailDiffPara.setNtax(UFDouble.ZERO_DBL);
    this.tailDiffPara.setNincaltaxmny(UFDouble.ZERO_DBL);
    this.tailDiffPara.setNintax(UFDouble.ZERO_DBL);
    this.tailDiffPara.setNnosubtax(UFDouble.ZERO_DBL);
    this.tailDiffPara.setNinmny(UFDouble.ZERO_DBL);
    this.tailDiffPara.setNintaxmny(UFDouble.ZERO_DBL);
    this.tailDiffPara.setNmny(UFDouble.ZERO_DBL);
    this.tailDiffPara.setNtaxmny(UFDouble.ZERO_DBL);

  }

  /**
   * 
   * 功能说明： <li>根据待结算出和待结算入组织结算清单表体数据
   * 
   * @since 6.1
   * @version 2012-2-22下午03:11:57
   * @author 马震
   * 
   * @param itemvo 结算清单子表vo
   * @param dispvo 前台显示vo（第一层）
   * @param outviewvo 待结算调出vo的集合（第二层）
   */
  private void setBodyValue(SettleListItemVO itemvo, SettleQueryViewVO dispvo,
      SettleQueryViewVO viewvo) {
    // 单边结算时将待结算出分摊到的数量赋到结算清单上
    // 若非单边情况这里赋值也不要紧， 因为后续会将其覆盖掉
    itemvo.setNnumber(viewvo.getNnumber());

    itemvo.setPk_group(viewvo.getPk_group());
    // 待结算调出调入主键
    itemvo.setCopstid(viewvo.getCbillid());
    itemvo.setCopst_bid(viewvo.getCbill_bid());
    itemvo.setFsettletypeflag(viewvo.getFsettletypeflag());

    // 存货信息
    itemvo.setCinventoryid(viewvo.getCinventoryid());
    itemvo.setCinventoryvid(viewvo.getCinventoryvid());
    itemvo.setCunitid(viewvo.getCunitid());
    itemvo.setCqtunitid(viewvo.getCqtunitid());
    itemvo.setVqtunitrate(viewvo.getVqtunitrate());
    itemvo.setVbatchcode(viewvo.getVbatchcode());
    // 结算规则
    itemvo.setCsettleruleid(viewvo.getCsettleruleid());
    itemvo.setCsettlerule_bid(viewvo.getCsettlerule_bid());
    // 源头单据信息
    itemvo.setCfirstid(viewvo.getCfirstid());
    itemvo.setCfirstbid(viewvo.getCfirstbid());
    itemvo.setVfirstcode(viewvo.getVfirstcode());
    itemvo.setVfirsttype(viewvo.getVfirsttype());
    itemvo.setVfirsttrantype(viewvo.getVfirsttrantype());
    itemvo.setVfirstrowno(viewvo.getVfirstrowno());
    itemvo.setDsrcbilldate(viewvo.getDbizdate());
    itemvo.setDfirstbilldate(viewvo.getDfirstbilldate());

    itemvo.setVfree1(viewvo.getVfree1());
    itemvo.setVfree2(viewvo.getVfree2());
    itemvo.setVfree3(viewvo.getVfree3());
    itemvo.setVfree4(viewvo.getVfree4());
    itemvo.setVfree5(viewvo.getVfree5());
    itemvo.setVfree6(viewvo.getVfree6());
    itemvo.setVfree7(viewvo.getVfree7());
    itemvo.setVfree8(viewvo.getVfree8());
    itemvo.setVfree9(viewvo.getVfree9());
    itemvo.setVfree10(viewvo.getVfree10());
    itemvo.setVbdef1(viewvo.getVbdef1());
    itemvo.setVbdef2(viewvo.getVbdef2());
    itemvo.setVbdef3(viewvo.getVbdef3());
    itemvo.setVbdef4(viewvo.getVbdef4());
    itemvo.setVbdef5(viewvo.getVbdef5());
    itemvo.setVbdef6(viewvo.getVbdef6());
    itemvo.setVbdef7(viewvo.getVbdef7());
    itemvo.setVbdef8(viewvo.getVbdef8());
    itemvo.setVbdef9(viewvo.getVbdef9());
    itemvo.setVbdef10(viewvo.getVbdef10());
    itemvo.setVbdef11(viewvo.getVbdef11());
    itemvo.setVbdef12(viewvo.getVbdef12());
    itemvo.setVbdef13(viewvo.getVbdef13());
    itemvo.setVbdef14(viewvo.getVbdef14());
    itemvo.setVbdef15(viewvo.getVbdef15());
    itemvo.setVbdef16(viewvo.getVbdef16());
    itemvo.setVbdef17(viewvo.getVbdef17());
    itemvo.setVbdef18(viewvo.getVbdef18());
    itemvo.setVbdef19(viewvo.getVbdef19());
    itemvo.setVbdef20(viewvo.getVbdef20());
    if (dispvo != null) {
      itemvo.setNorigmny(dispvo.getNorigmny());
      itemvo.setNmny(dispvo.getNmny());
      itemvo.setNtax(dispvo.getNtax());
      itemvo.setNorigtaxmny(dispvo.getNorigtaxmny());
      itemvo.setNtaxmny(dispvo.getNtaxmny());

      itemvo.setPk_org(dispvo.getCsourceorgid());
      itemvo.setNtaxrate(dispvo.getNtaxrate());
      itemvo.setFtaxtypeflag(dispvo.getFtaxtypeflag());
      itemvo.setNorigprice(dispvo.getNorigprice());
      itemvo.setNorigtaxprice(dispvo.getNorigtaxprice());
      itemvo.setNprice(dispvo.getNprice());
      itemvo.setNtaxprice(dispvo.getNtaxprice());
      // v60是直接赋值，而v61需要根据结算数量计算相应的报价数量,
      // 在之后的setBodyValueForIn方法里处理
      // itemvo.setNqtnum(dispvo.getNqtnum());
      itemvo.setNqtorigprice(dispvo.getNqtorigprice());
      itemvo.setNqtorigtaxprice(dispvo.getNqtorigtaxprice());
      // 以下v61新增
      itemvo.setCtaxcodeid(dispvo.getCtaxcodeid());
      itemvo.setCintaxcodeid(dispvo.getCintaxcodeid());
      itemvo.setFintaxtypeflag(dispvo.getFintaxtypeflag());
      itemvo.setNintaxrate(dispvo.getNintaxrate());
      itemvo.setNinqtorigprice(dispvo.getNinqtorigprice());
      itemvo.setNcaltaxmny(dispvo.getNcaltaxmny());
      itemvo.setNqttaxprice(dispvo.getNqttaxprice());
      itemvo.setNqtprice(dispvo.getNqtprice());
      itemvo.setNinorigmny(dispvo.getNinorigmny());
      itemvo.setNinorigprice(dispvo.getNinorigprice());
      itemvo.setNinprice(dispvo.getNinprice());
      itemvo.setNintaxprice(dispvo.getNintaxprice());
      itemvo.setNinqtprice(dispvo.getNinqtprice());
      itemvo.setNinmny(dispvo.getNinmny());
      itemvo.setNinqttaxprice(dispvo.getNinqttaxprice());
      itemvo.setNintaxmny(dispvo.getNintaxmny());
      itemvo.setNincaltaxmny(dispvo.getNincaltaxmny());
      itemvo.setNintax(dispvo.getNintax());
      itemvo.setNnosubtax(dispvo.getNnosubtax());
      itemvo.setNnosubtaxrate(dispvo.getNnosubtaxrate());
      itemvo.setBopptaxflag(dispvo.getBopptaxflag());
      
      itemvo.setAttributeValue("vbdef5", dispvo.getAttributeValue("vbdef5"));
      itemvo.setAttributeValue("vbdef16", dispvo.getAttributeValue("vbdef16"));
      itemvo.setAttributeValue("vbdef17", dispvo.getAttributeValue("vbdef17"));
      itemvo.setAttributeValue("vbdef18", dispvo.getAttributeValue("vbdef18"));
      itemvo.setAttributeValue("vbdef13", dispvo.getAttributeValue("vbdef13"));
      itemvo.setAttributeValue("vbdef19", dispvo.getAttributeValue("vbdef19"));

    }
    else {
      itemvo.setNorigmny(viewvo.getNorigmny());
      itemvo.setNmny(viewvo.getNmny());
      itemvo.setNtax(viewvo.getNtax());
      itemvo.setNorigtaxmny(viewvo.getNorigtaxmny());
      itemvo.setNtaxmny(viewvo.getNtaxmny());

      itemvo.setPk_org(viewvo.getCsourceorgid());
      itemvo.setNtaxrate(viewvo.getNtaxrate());
      itemvo.setFtaxtypeflag(viewvo.getFtaxtypeflag());
      itemvo.setNorigprice(viewvo.getNorigprice());
      itemvo.setNorigtaxprice(viewvo.getNorigtaxprice());
      itemvo.setNprice(viewvo.getNprice());
      itemvo.setNtaxprice(viewvo.getNtaxprice());
      // itemvo.setNqtnum(viewvo.getNqtnum());
      itemvo.setNqtorigprice(viewvo.getNqtorigprice());
      itemvo.setNqtorigtaxprice(viewvo.getNqtorigtaxprice());
      // 以下v61新增
      itemvo.setCtaxcodeid(viewvo.getCtaxcodeid());
      itemvo.setCintaxcodeid(viewvo.getCintaxcodeid());
      itemvo.setFintaxtypeflag(viewvo.getFintaxtypeflag());
      itemvo.setNintaxrate(viewvo.getNintaxrate());
      itemvo.setNinqtorigprice(viewvo.getNinqtorigprice());
      itemvo.setNcaltaxmny(viewvo.getNcaltaxmny());
      itemvo.setNqttaxprice(viewvo.getNqttaxprice());
      itemvo.setNqtprice(viewvo.getNqtprice());
      itemvo.setNinorigmny(viewvo.getNinorigmny());
      itemvo.setNinorigprice(viewvo.getNinorigprice());
      itemvo.setNinprice(viewvo.getNinprice());
      itemvo.setNintaxprice(viewvo.getNintaxprice());
      itemvo.setNinqtprice(viewvo.getNinqtprice());
      itemvo.setNinmny(viewvo.getNinmny());
      itemvo.setNinqttaxprice(viewvo.getNinqttaxprice());
      itemvo.setNintaxmny(viewvo.getNintaxmny());
      itemvo.setNincaltaxmny(viewvo.getNincaltaxmny());
      itemvo.setNintax(viewvo.getNintax());
      itemvo.setNnosubtax(viewvo.getNnosubtax());
      itemvo.setNnosubtaxrate(viewvo.getNnosubtaxrate());
      itemvo.setBopptaxflag(viewvo.getBopptaxflag());
    }

    // 来源待结算调出单据
    itemvo.setCsrcid(viewvo.getCsrcid());
    itemvo.setCsrcbid(viewvo.getCsrcbid());
    itemvo.setVsrccode(viewvo.getVsrccode());
    itemvo.setCsrctrantype(viewvo.getVsrctrantype());
    itemvo.setVsrctype(viewvo.getVsrctype());
    itemvo.setVsrcrowno(viewvo.getVsrcrowno());
    if (itemvo.getDbilldate() == null) {
      itemvo.setDbilldate(AppContext.getInstance().getBusiDate());
    }
    itemvo.setFsettletypeflag(viewvo.getFsettletypeflag());

    itemvo.setCproductorid(viewvo.getCproductorid());
    itemvo.setCprojectid(viewvo.getCprojectid());
    itemvo.setCffileid(viewvo.getCffileid());// panfengc
    itemvo.setCvendorid(viewvo.getCvendorid());
    itemvo.setCasscustid(viewvo.getCasscustid());

    itemvo.setOutItemTs(viewvo.getTs());

  }

  private void setBodyValueForIn(SettleListItemVO itemvo, PSInViewVO inviewvo) {

    itemvo.setCipstid(inviewvo.getHead().getCbillid());
    itemvo.setCipst_bid(inviewvo.getItem().getCbill_bid());
    itemvo.setCinsrcid(inviewvo.getHead().getCsrcid());
    itemvo.setCinsrcbid(inviewvo.getItem().getCsrcbid());
    itemvo.setVinsrccode(inviewvo.getHead().getVsrccode());
    itemvo.setVinsrctrantype(inviewvo.getHead().getVsrctrantype());
    itemvo.setVinsrctype(inviewvo.getHead().getVsrctype());
    itemvo.setVinsrcrowno(inviewvo.getItem().getVsrcrowno());
    itemvo.setDinsrcbilldate(inviewvo.getItem().getDbizdate());

    // 若非单边情况则会将先前那个数量覆盖掉
    itemvo.setNnumber(inviewvo.getItem().getNnumber());
    // ScaleUtils util = ScaleUtils.getScaleUtilAtBS();
    ScaleUtils util = new ScaleUtils(AppContext.getInstance().getPkGroup());
    UFDouble nqtnum =
        HslParseUtil.hslDivUFDouble(itemvo.getVqtunitrate(),
            itemvo.getNnumber());
    nqtnum = util.adjustNumScale(nqtnum, itemvo.getCqtunitid());

    itemvo.setNqtnum(nqtnum);
  }

  /**
   * 
   * 功能说明： <li>调出方无税金额、调出方本币无税金额、调出方税额、价税合计、本币价税合计、 调入方无税金额、调入方本币无税金额、
   * 调入方计税金额、调入方税额、 不可抵扣税额 = 界面显示VO对应字段的值 – 之前所有单据对应字段的合计 <li>
   * 注：单边结算只包含调出方信息，无调入方信息
   * 
   * @since 6.1
   * @version 2012-2-8下午04:49:55
   * @author 马震
   * 
   * @param header 结算主表vo
   * @param itemvo 结算子表vo
   * @param dispviewvo 界面显示vo
   * @param bunilateralflag 是否单边结算
   * @param calm5fTool
   * @param totalValue[10] 多笔待结算调出的已（之前）结算汇总值，用于最后一笔处理尾差用
   */
  private void setBodyValueLast(SettleListHeaderVO header,
      SettleListItemVO itemvo, SettleQueryViewVO dispviewvo,
      boolean bunilateralflag, CalCulateM5fPriceMny calm5fTool) {

    if (bunilateralflag) {
      itemvo.setNorigmny(MathTool.sub(dispviewvo.getNorigmny(),
          this.tailDiffPara.getNorigmny()));
      itemvo.setNorigtaxmny(MathTool.sub(dispviewvo.getNorigtaxmny(),
          this.tailDiffPara.getNorigtaxmny()));
      calm5fTool.calculateItemOutLocalPriceMny(header, itemvo);
      // 注：以下部分需要在上面的单价金额联动完毕后进行分摊，否则会被覆盖掉
      itemvo.setNcaltaxmny(MathTool.sub(dispviewvo.getNcaltaxmny(),
          this.tailDiffPara.getNcaltaxmny()));
      itemvo.setNtax(MathTool.sub(dispviewvo.getNtax(),
          this.tailDiffPara.getNtax()));
      itemvo.setNmny(MathTool.sub(dispviewvo.getNmny(),
          this.tailDiffPara.getNmny()));
      itemvo.setNtaxmny(MathTool.sub(dispviewvo.getNtaxmny(),
          this.tailDiffPara.getNtaxmny()));
    }
    else {
      itemvo.setNorigmny(MathTool.sub(dispviewvo.getNorigmny(),
          this.tailDiffPara.getNorigmny()));
      itemvo.setNorigtaxmny(MathTool.sub(dispviewvo.getNorigtaxmny(),
          this.tailDiffPara.getNorigtaxmny()));
      itemvo.setNinorigmny(MathTool.sub(dispviewvo.getNinorigmny(),
          this.tailDiffPara.getNinorigmny()));
      // calm5fTool.calculateItemOutLocalPriceMny(header, itemvo);
      // calm5fTool.calculateItemInLocalPriceMny(header, itemvo);
      // 注：以下部分需要在上面的单价金额联动完毕后进行分摊，否则会被覆盖掉
      itemvo.setNcaltaxmny(MathTool.sub(dispviewvo.getNcaltaxmny(),
          this.tailDiffPara.getNcaltaxmny()));
      itemvo.setNtax(MathTool.sub(dispviewvo.getNtax(),
          this.tailDiffPara.getNtax()));
      itemvo.setNincaltaxmny(MathTool.sub(dispviewvo.getNincaltaxmny(),
          this.tailDiffPara.getNincaltaxmny()));
      itemvo.setNintax(MathTool.sub(dispviewvo.getNintax(),
          this.tailDiffPara.getNintax()));
      itemvo.setNnosubtax(MathTool.sub(dispviewvo.getNnosubtax(),
          this.tailDiffPara.getNnosubtax()));
      itemvo.setNmny(MathTool.sub(dispviewvo.getNmny(),
          this.tailDiffPara.getNmny()));
      itemvo.setNtaxmny(MathTool.sub(dispviewvo.getNtaxmny(),
          this.tailDiffPara.getNtaxmny()));
      itemvo.setNinmny(MathTool.sub(dispviewvo.getNinmny(),
          this.tailDiffPara.getNinmny()));
      itemvo.setNintaxmny(MathTool.sub(dispviewvo.getNintaxmny(),
          this.tailDiffPara.getNintaxmny()));
    }
  }

  /**
   * 
   * 功能说明： <li>需要区分是跨国业务还是非跨国业务 <li>若是含税优先由含税单价驱动，如果是无税优先就以调出方无税价格驱动
   * 
   * @since 6.1
   * @version 2012-2-8下午04:51:10
   * @author 马震
   * @param totalValue
   * @param isTaxfirst
   * @param inViewVO
   * @param dispviewvo
   * 
   * @return UFDouble[]
   */
  private void setBodyValueNormal(SettleListHeaderVO header,
      SettleListItemVO itemvo, UFBoolean isTaxfirst, UFDouble ntotalnum,
      UFDouble nsettlenum, boolean bunilateralflag,
      CalCulateM5fPriceMny calm5fTool) {
    // 不再根据参数判断，现强制以含税单价驱动计算两方单价金额
    calm5fTool.calculateItemPriceMny(header, itemvo,
        SettleListItemVO.NORIGTAXPRICE);

    // 对相应字段进行按数量分摊
    // allotValue(ntotalnum, nsettlenum, header, itemvo, bunilateralflag);

    // 汇总相关字段，供最后一段的尾差处理用
    this.tailDiffPara.setNorigmny(MathTool.add(itemvo.getNorigmny(),
        this.tailDiffPara.getNorigmny()));
    this.tailDiffPara.setNorigtaxmny(MathTool.add(itemvo.getNorigtaxmny(),
        this.tailDiffPara.getNorigtaxmny()));
    this.tailDiffPara.setNinorigmny(MathTool.add(itemvo.getNinorigmny(),
        this.tailDiffPara.getNinorigmny()));
    this.tailDiffPara.setNcaltaxmny(MathTool.add(itemvo.getNcaltaxmny(),
        this.tailDiffPara.getNcaltaxmny()));
    this.tailDiffPara.setNtax(MathTool.add(itemvo.getNtax(),
        this.tailDiffPara.getNtax()));
    this.tailDiffPara.setNincaltaxmny(MathTool.add(itemvo.getNincaltaxmny(),
        this.tailDiffPara.getNincaltaxmny()));
    this.tailDiffPara.setNintax(MathTool.add(itemvo.getNintax(),
        this.tailDiffPara.getNintax()));
    this.tailDiffPara.setNnosubtax(MathTool.add(itemvo.getNnosubtax(),
        this.tailDiffPara.getNnosubtax()));
    this.tailDiffPara.setNmny(MathTool.add(itemvo.getNmny(),
        this.tailDiffPara.getNmny()));
    this.tailDiffPara.setNtaxmny(MathTool.add(itemvo.getNtaxmny(),
        this.tailDiffPara.getNtaxmny()));
    this.tailDiffPara.setNinmny(MathTool.add(itemvo.getNinmny(),
        this.tailDiffPara.getNinmny()));
    this.tailDiffPara.setNintaxmny(MathTool.add(itemvo.getNintaxmny(),
        this.tailDiffPara.getNintaxmny()));
  }

  /**
   * 
   * 功能说明： <li>根据待结算出和待结算入组织结算清单表头数据
   * 
   * @since 6.1
   * @version 2012-2-8下午02:17:49
   * @author 马震
   * 
   * @param headvo
   * @param viewvo
   * @param dispvo void
   */
  private void setHeadValue(SettleListHeaderVO headvo,
      SettleQueryViewVO viewvo, SettleQueryViewVO dispvo, String nc001,
      String nc002) {
    headvo.setPk_group(viewvo.getPk_group());
    if (dispvo != null) {
      headvo.setPk_org(dispvo.getCsourceorgid());
      headvo.setPk_org_v(dispvo.getCsourceorgvid());
      headvo.setCtargetfiorgid(dispvo.getCtargetorgid());
      headvo.setCtargetfiorgvid(dispvo.getCtargetorgvid());
      headvo.setCincountryid(dispvo.getCincountryid());
      headvo.setCoutcountryid(dispvo.getCoutcountryid());
      headvo.setCouttaxcountryid(dispvo.getCouttaxcountryid());
      headvo.setFoutbuysellflag(dispvo.getFoutbuysellflag());
      headvo.setBouttriatradeflag(dispvo.getBouttriatradeflag());
      headvo.setVoutvatcode(dispvo.getVoutvatcode());
      headvo.setCintaxcountryid(dispvo.getCintaxcountryid());
      headvo.setFinbuysellflag(dispvo.getFinbuysellflag());
      headvo.setBintriatradeflag(dispvo.getBintriatradeflag());
      headvo.setVinvatcode(dispvo.getVinvatcode());
      headvo.setCtradewordid(dispvo.getCtradewordid());
      headvo.setCincurrencyid(dispvo.getCincurrencyid());
      headvo.setNexchangerate(dispvo.getNexchangerate());
      headvo.setNinexchangerate(dispvo.getNinexchangerate());
      headvo.setCcurrencyid(dispvo.getCcurrencyid());
      headvo.setCorigcurrencyid(dispvo.getCorigcurrencyid());
      headvo.setBaskpriceflag(dispvo.getBaskpriceflag());

    }
    else {
      headvo.setPk_org(viewvo.getPk_org());
      headvo.setPk_org_v(viewvo.getPk_org_v());
      headvo.setCtargetfiorgid(viewvo.getCinfinanceorgid());
      headvo.setCtargetfiorgvid(viewvo.getCinfinanceorgvid());
      headvo.setCincountryid(viewvo.getCincountryid());
      headvo.setCoutcountryid(viewvo.getCoutcountryid());
      headvo.setCouttaxcountryid(viewvo.getCouttaxcountryid());
      headvo.setFoutbuysellflag(viewvo.getFoutbuysellflag());
      headvo.setBouttriatradeflag(viewvo.getBouttriatradeflag());
      headvo.setVoutvatcode(viewvo.getVoutvatcode());
      headvo.setCintaxcountryid(viewvo.getCintaxcountryid());
      headvo.setFinbuysellflag(viewvo.getFinbuysellflag());
      headvo.setBintriatradeflag(viewvo.getBintriatradeflag());
      headvo.setVinvatcode(viewvo.getVinvatcode());
      headvo.setCtradewordid(viewvo.getCtradewordid());
      headvo.setCincurrencyid(viewvo.getCincurrencyid());
      headvo.setCcurrencyid(viewvo.getCcurrencyid());
      headvo.setCorigcurrencyid(viewvo.getCorigcurrencyid());
      headvo.setNexchangerate(viewvo.getNexchangerate());
      headvo.setNinexchangerate(viewvo.getNinexchangerate());
      headvo.setBaskpriceflag(viewvo.getBaskpriceflag());
    }

    headvo.setFmainbodyflag(viewvo.getFmainbodyflag());
    headvo.setBdiscountflag(viewvo.getBbasediscountflag());

    headvo.setCsettlepathid(viewvo.getCsettlepathid());
    headvo.setBunilateralflag(viewvo.getBunilateralflag());

    if (headvo.getDbilldate() == null) {
      headvo.setDbilldate(AppContext.getInstance().getBusiDate());
    }

    this.fillGroupChangeRate(headvo, nc001);

    this.fillGlobalChangeRate(headvo, nc002);

  }

}
