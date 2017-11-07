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
   * �Լ�˰��˰����뷽��˰��˰����ɵֿ�˰����а���������̯
   * ʹ�ó��������ڽ����м������ʾ�Ĳ�������ϸ�����ݣ�������ʱ�ǰ�������ϸ���н���ġ�
   * ��������γ���ʱ������ʱ�Ὣ�����������²�֣�Ȼ����е��۽�����������¼�������ϸ
   * ������ݣ����û����м�����޸���������ֶκ����ڲ��ᷴ�㵥�ۡ������ֶΣ��������
   * ���������ϸ���۽������ʱ�����°���������ֶμ���һ�飬������ǣ��û��ֶ��Ĺ����⼸��
   * �ֶβ��ᱣ��ס�����Ǳ�����޸�ǰ��ֵ��
   * 
   * ��̯�������ڽ���ʱ����������ϸ�������⼸���ֶν��з�̯���������һ����Ҫ����β��
   */

  // ��������
  private UFDouble settlenum = UFDouble.ZERO_DBL;

  // ĳЩ�ֶεĶ�λ���ֵ���������һ�ε�β���
  private SettleBuildTailDiffPara tailDiffPara = new SettleBuildTailDiffPara();

  /**
   * 
   * ����˵���� <li>��֯����VO����ڷ��� <li>���ݽ����м����������֯�ڲ������嵥������Ϣ <li>���е��߽������⴦�� <li>
   * �Խ�������(nnumber)��ֵ�������ط������߸�ֵ�ͷǵ��߸�ֵ���ǵ��߸�ֵ�ڵ��߸�ֵ֮�� ��ˣ����ٴ���ʱ��ע��
   * 
   * @since 6.1
   * @version 2012-2-8����02:18:22
   * @author ����
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
    // ȫ�ֱ�λ������ģʽ��ֵ
    String nc002 = SysParaInitQuery.getParaString(IOrgConst.GLOBEORG, "NC002");

    for (SettleQueryViewVO dispvo : gatherVOs) {
      // ��ʼ����̯��β���������ֵ
      this.initAllotTailDiffValue(dispvo);
      Integer gatherDetailKey = dispvo.getSortcolumn();
      List<SettleQueryViewVO> lstOutViewVOs =
          gUtil.getOutVOByKey(gatherDetailKey);
      for (SettleQueryViewVO outviewvo : lstOutViewVOs) {
        // �����߽������
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
   * ����˵���� <li>���ݴ�����������ʹ�������뵥��֯����ۺ�vo
   * 
   * @since 6.1
   * @version 2012-2-8����05:49:32
   * @author ����
   * 
   * @param dispviewvo ������ʾVO
   * @param outviewvo ��֯����VO
   * @param lstInViewVOs ��������뵥
   * @param fgatherbyflag ���ܽ���ھ�
   * @param lstOutViewVOSize ��֯VO����
   * @param settlenum ��������
   * @param totalValue ��������β�������ֶλ���ֵ
   * @return List<SettleListVO> �����嵥�ۺ�VO
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
      // ֻ��һ��
      if (lstOutViewVOSize == 1 && lstInViewVOs.size() == 1) {
        // this.setBodyValueOne(itemvo, dispviewvo, false);
        calm5fTool.calculateItemGlobalGroup(header, itemvo);
        continue;
      }
      this.settlenum =
          MathTool.add(this.settlenum, inViewVO.getItem().getNnumber());
      // ��Ϊ���һ�ʣ�����β���
      if (MathTool.equals(this.settlenum, dispviewvo.getNnumber())) {
        this.setBodyValueLast(header, itemvo, dispviewvo, false, calm5fTool);
        continue;
      }
      // ���ö�Ӧ�ĵ��۽���㷨
      this.setBodyValueNormal(header, itemvo, isTaxfirst,
          dispviewvo.getNnumber(), inViewVO.getItem().getNnumber(), false,
          calm5fTool);
      calm5fTool.calculateItemGlobalGroup(header, itemvo);
    }

    return resultVOs;
  }

  /**
   * 
   * ����˵���� <li>���ݽ���vo�ʹ��������vos����֯vos����֯����vo
   * 
   * @since 6.1
   * @version 2012-2-10����08:49:39
   * @author ����
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
    // ���ö�Ӧ�ĵ��۽���㷨
    this.setBodyValueNormal(header, itemvo, isTaxfirst, dispvo.getNnumber(),
        outviewvo.getNnumber(), true, calm5fTool);
    calm5fTool.calculateItemGlobalGroup(header, itemvo);
    return resultvo;
  }

  /**
   * 
   * ����˵���� <li>�Խ����嵥���кϵ�����
   * 
   * @since 6.1
   * @version 2012-3-2����04:16:50
   * @author ����
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
        || nc002.equals("������ȫ�ֱ�λ��" /* -=notranslate=- */)
        || head.getNglobalexchgrate() != null) {
      return;
    }
    UFDouble rate = UFDouble.ZERO_DBL;
    if (nc002.equals("����ԭ�Ҽ���"/* -=notranslate=- */)) {
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
        || nc001.equals("�����ü��ű�λ��" /* -=notranslate=- */)
        || head.getNgroupexchgrate() != null) {
      return;
    }
    UFDouble rate = UFDouble.ZERO_DBL;
    if (nc001.equals("����ԭ�Ҽ���"/* -=notranslate=- */)) {
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
   * ����˵���� <li>�ϵ������ֶ�
   * 
   * @since 6.1
   * @version 2012-3-2����04:17:06
   * @author ����
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
    // ��������
    this.settlenum = UFDouble.ZERO_DBL;
    // ĳЩ�ֶεĶ�λ���ֵ���������һ�ε�β���
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
   * ����˵���� <li>���ݴ�������ʹ���������֯�����嵥��������
   * 
   * @since 6.1
   * @version 2012-2-22����03:11:57
   * @author ����
   * 
   * @param itemvo �����嵥�ӱ�vo
   * @param dispvo ǰ̨��ʾvo����һ�㣩
   * @param outviewvo ���������vo�ļ��ϣ��ڶ��㣩
   */
  private void setBodyValue(SettleListItemVO itemvo, SettleQueryViewVO dispvo,
      SettleQueryViewVO viewvo) {
    // ���߽���ʱ�����������̯�����������������嵥��
    // ���ǵ���������︳ֵҲ��Ҫ���� ��Ϊ�����Ὣ�串�ǵ�
    itemvo.setNnumber(viewvo.getNnumber());

    itemvo.setPk_group(viewvo.getPk_group());
    // �����������������
    itemvo.setCopstid(viewvo.getCbillid());
    itemvo.setCopst_bid(viewvo.getCbill_bid());
    itemvo.setFsettletypeflag(viewvo.getFsettletypeflag());

    // �����Ϣ
    itemvo.setCinventoryid(viewvo.getCinventoryid());
    itemvo.setCinventoryvid(viewvo.getCinventoryvid());
    itemvo.setCunitid(viewvo.getCunitid());
    itemvo.setCqtunitid(viewvo.getCqtunitid());
    itemvo.setVqtunitrate(viewvo.getVqtunitrate());
    itemvo.setVbatchcode(viewvo.getVbatchcode());
    // �������
    itemvo.setCsettleruleid(viewvo.getCsettleruleid());
    itemvo.setCsettlerule_bid(viewvo.getCsettlerule_bid());
    // Դͷ������Ϣ
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
      // v60��ֱ�Ӹ�ֵ����v61��Ҫ���ݽ�������������Ӧ�ı�������,
      // ��֮���setBodyValueForIn�����ﴦ��
      // itemvo.setNqtnum(dispvo.getNqtnum());
      itemvo.setNqtorigprice(dispvo.getNqtorigprice());
      itemvo.setNqtorigtaxprice(dispvo.getNqtorigtaxprice());
      // ����v61����
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
      // ����v61����
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

    // ��Դ�������������
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

    // ���ǵ��������Ὣ��ǰ�Ǹ��������ǵ�
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
   * ����˵���� <li>��������˰��������������˰��������˰���˰�ϼơ����Ҽ�˰�ϼơ� ���뷽��˰�����뷽������˰��
   * ���뷽��˰�����뷽˰� ���ɵֿ�˰�� = ������ʾVO��Ӧ�ֶε�ֵ �C ֮ǰ���е��ݶ�Ӧ�ֶεĺϼ� <li>
   * ע�����߽���ֻ������������Ϣ���޵��뷽��Ϣ
   * 
   * @since 6.1
   * @version 2012-2-8����04:49:55
   * @author ����
   * 
   * @param header ��������vo
   * @param itemvo �����ӱ�vo
   * @param dispviewvo ������ʾvo
   * @param bunilateralflag �Ƿ񵥱߽���
   * @param calm5fTool
   * @param totalValue[10] ��ʴ�����������ѣ�֮ǰ���������ֵ���������һ�ʴ���β����
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
      // ע�����²�����Ҫ������ĵ��۽��������Ϻ���з�̯������ᱻ���ǵ�
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
      // ע�����²�����Ҫ������ĵ��۽��������Ϻ���з�̯������ᱻ���ǵ�
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
   * ����˵���� <li>��Ҫ�����ǿ��ҵ���Ƿǿ��ҵ�� <li>���Ǻ�˰�����ɺ�˰�����������������˰���Ⱦ��Ե�������˰�۸�����
   * 
   * @since 6.1
   * @version 2012-2-8����04:51:10
   * @author ����
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
    // ���ٸ��ݲ����жϣ���ǿ���Ժ�˰�������������������۽��
    calm5fTool.calculateItemPriceMny(header, itemvo,
        SettleListItemVO.NORIGTAXPRICE);

    // ����Ӧ�ֶν��а�������̯
    // allotValue(ntotalnum, nsettlenum, header, itemvo, bunilateralflag);

    // ��������ֶΣ������һ�ε�β�����
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
   * ����˵���� <li>���ݴ�������ʹ���������֯�����嵥��ͷ����
   * 
   * @since 6.1
   * @version 2012-2-8����02:17:49
   * @author ����
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
