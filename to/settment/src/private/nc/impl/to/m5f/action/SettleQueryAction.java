package nc.impl.to.m5f.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bs.to.pub.util.SetPSOutViewTOSettleView;
import nc.impl.pubapp.env.BSContext;
import nc.impl.pubapp.pattern.data.view.EfficientViewQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.database.IDExQueryBuilder;
import nc.impl.to.askprice.split.AskPriceM5ForTrans;
import nc.impl.to.askprice.split.AskPriceM5fHandUtils;
import nc.impl.to.m5f.TOEfficientViewQuery;
import nc.impl.to.m5f.action.settlequery.SettleMatchProcessor;
import nc.impl.to.m5f.action.util.SettleQuerySqlBuildUtils;
import nc.impl.to.m5f.action.util.SettleQueryVOUtils;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.HslParseUtil;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MapList;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.pubapp.query2.sql.process.QueryCondition;
import nc.vo.pubapp.query2.sql.process.QuerySchemeUtils;
import nc.vo.pubapp.scale.ScaleUtils;
import nc.vo.scmpub.res.billtype.TOBillType;
import nc.vo.to.enumeration.SettleMainbody;
import nc.vo.to.m5f.entity.SettleListItemVO;
import nc.vo.to.m5f.entity.SettleQueryParaVO;
import nc.vo.to.m5f.entity.SettleQueryTransVO;
import nc.vo.to.m5f.entity.SettleQueryViewVO;
import nc.vo.to.m5f.util.SettleBuildTailDiffPara;
import nc.vo.to.m5f.util.SettleGroupOutAssViewUtil;
import nc.vo.to.m5f.util.SettleOutInAssUtil;
import nc.vo.to.m5m.entity.PSOutViewVO;
import nc.vo.to.m5m.entity.PreSettleOutHeaderVO;
import nc.vo.to.m5n.entity.PSInViewVO;
import nc.vo.to.m5n.entity.PreSettleInHeaderVO;
import nc.vo.to.m5n.entity.PreSettleInItemVO;
import nc.vo.to.m5n.entity.PreSettleInLineVO;
import nc.vo.to.m5x.entity.BillHeaderVO;
import nc.vo.to.m5x.entity.BillItemVO;
import nc.vo.to.pub.TOTempTableNameConst;
import nc.vo.to.pub.para.ParaUtilsForTo;

/**
 * �����ѯ��̨������
 * 
 * @author Administrator
 */
public class SettleQueryAction {

  private static int ROWSIZE = 10000;

  SettleOutInAssUtil outinAss = new SettleOutInAssUtil();

  // ��ѯ����
  // ���������������ֶΣ����������֯������Ŀ�Ĳ�����֯����������֯������Ŀ�Ŀ����֯
  //
  /*
   * ���������֯������Ŀ�Ĳ�����֯����������֯������Ŀ�Ŀ����֯�� ���㷽��
   * ���֣����ܽ���ھ���������ʼ���ڣ�������ֹ���ڣ�ҵ��ʼ���ڣ�ҵ���ֹ���ڣ� ����������Ӧ���������Ƿ�һ�£� ���������������ܽ��㣬�Ƿ�ѯ�ۣ�
   * ���ⵥ�ţ������ţ����Ϸ��࣬���ϱ��룬�ڲ��������ͣ���������
   */
  /**
   * 
   * @param scheme
   * @return
 * @throws BusinessException 
   */
  public SettleQueryTransVO querySettle(IQueryScheme scheme) throws BusinessException {

    Integer fgatherbyflag = this.getGatherByFlag(scheme);
    Integer fmainbodyflag = this.getFmainBodyFlag(scheme);

    // ��ѯ���������������
    SettleQueryViewVO[] arrMainThreadOut = this.queryPSOutVO(scheme);
    // ���˴����������
    SettleQueryViewVO[] views = this.filterViewBySettleEnd(arrMainThreadOut);

    if (views.length == 0) {
      return new SettleQueryTransVO();
    }
    // ���Ƿ񵥱߽���Դ�������������鴦��
    List<SettleQueryViewVO[]> lstData =
        SettleQueryVOUtils.groupPSOutByField(views,
            PreSettleOutHeaderVO.BUNILATERALFLAG);
    // ���ߴ������������
    SettleQueryViewVO[] arrSingleOut = lstData.get(0);
    // �ǵ��ߴ������������
    SettleQueryViewVO[] arrNormOut = lstData.get(1);
    if (arrNormOut.length > 0) {
      // �ǵ��ߵ����ݴ���
      this.normalProc(arrNormOut, scheme);
    }
    if (arrSingleOut.length > 0) {
      // ���ߵ����ݴ���
      this.singleProc(arrSingleOut);
    }
    //201710-14 liyf ѯ�۴���
    new AskPriceM5ForTrans().askSettlePrice(this.outinAss.getOutVOs());
    // ѯ�۴���
    this.askPrice(this.outinAss.getOutVOs(), scheme);
    ScaleUtils scale = ScaleUtils.getScaleUtilAtBS();
    for (SettleQueryViewVO view : this.outinAss.getOutVOs()) {
      if (view.getNorigprice() == null || MathTool.isZero(view.getNorigprice())) {
        continue;
      }
      view.setNorigprice(scale.adjustSoPuPriceScale(view.getNorigprice(),
          view.getCorigcurrencyid()));
      view.setNorigtaxprice(scale.adjustSoPuPriceScale(view.getNorigtaxprice(),
          view.getCorigcurrencyid()));
      view.setNqtorigprice(scale.adjustSoPuPriceScale(view.getNqtorigprice(),
          view.getCorigcurrencyid()));
      view.setNqtorigtaxprice(scale.adjustSoPuPriceScale(
          view.getNqtorigtaxprice(), view.getCorigcurrencyid()));
    }

    // ��ν���ĳ�����������β���
    this.dealTailDiff(scheme);

    SettleGroupOutAssViewUtil groupAss = new SettleGroupOutAssViewUtil();
    SettleQueryViewVO[] gatherVOs =
        groupAss.creatGroupVO(this.outinAss.getOutVOs(), fgatherbyflag,
            fmainbodyflag);
    groupAss.setGatherVOs(gatherVOs);
    // ��ʼ����̨��ǰ̨��VO
    SettleQueryTransVO ret = this.initTransVO(groupAss, fgatherbyflag);

    return ret;
  }

  // ���ݡ��������������ɡ�������������������ɡ���ɸѡҪ���������
  private SettleQueryViewVO[] filterViewBySettleEnd(
      SettleQueryViewVO[] arrMainThreadOut) {
    Set<String> c5XbidSet = new HashSet<String>();
    Set<String> c5XidSet = new HashSet<String>();
    // ��ѯԴͷ��������Ϊ5X�� ��Ӧ�� ��������
    for (SettleQueryViewVO view : arrMainThreadOut) {
      if (view.getVfirsttype().equals(TOBillType.TransOrder.getCode())) {
        c5XbidSet.add(view.getCfirstbid());
        c5XidSet.add(view.getCfirstid());
      }
    }
    String[] c5Xbids = new String[c5XbidSet.size()];
    c5XbidSet.toArray(c5Xbids);
    String[] c5Xids = new String[c5XidSet.size()];
    c5XidSet.toArray(c5Xids);

    Map<String, BillItemVO> billItemMap = new HashMap<String, BillItemVO>();
    Map<String, BillHeaderVO> billHeadMap = new HashMap<String, BillHeaderVO>();
    // ����������ѯ�ֶ�
    if (c5Xbids.length > 0) {
      String[] queryBillItemNames =
          new String[] {
            BillItemVO.CBILL_BID, BillItemVO.PK_ORG,
            BillItemVO.CTOUTSTOCKORGID, BillItemVO.BIOSETTLEENDFLAG,
            BillItemVO.BOTSETTLEENDFLAG
          };
      VOQuery<BillItemVO> billitemvoquery =
          new VOQuery<BillItemVO>(BillItemVO.class, queryBillItemNames);
      BillItemVO[] billItems = billitemvoquery.query(c5Xbids);
      for (BillItemVO item : billItems) {
        billItemMap.put(item.getCbill_bid(), item);
      }
    }

    if (c5Xids.length > 0) {
      // ��ѯ����������ͷ
      String[] queryBillHeadNames =
          new String[] {
            BillHeaderVO.CBILLID, BillHeaderVO.BIOREVERSEFLAG,
            BillHeaderVO.BOTREVERSEFLAG
          };
      VOQuery<BillHeaderVO> billvoquery =
          new VOQuery<BillHeaderVO>(BillHeaderVO.class, queryBillHeadNames);
      BillHeaderVO[] billHeads = billvoquery.query(c5Xids);
      for (BillHeaderVO HVO : billHeads) {
        billHeadMap.put(HVO.getCbillid(), HVO);
      }
    }

    return this.dealOutView(arrMainThreadOut, billItemMap, billHeadMap);
  }

  private SettleQueryViewVO[] dealOutView(SettleQueryViewVO[] arrMainThreadOut,
      Map<String, BillItemVO> billItemMap, Map<String, BillHeaderVO> billHeadMap) {
    // ��������������
    ArrayList<SettleQueryViewVO> viewList = new ArrayList<SettleQueryViewVO>();

    for (SettleQueryViewVO view : arrMainThreadOut) {
      String coutStorckOrgid = view.getCoutstockorgid();
      String cinStorckOrgid = view.getCinstockorgid();
      // String csettleRuleid = view.getCsettleruleid();
      String cbillBid = view.getCfirstbid();
      String cbillid = view.getCfirstid();
      BillItemVO billitemvo = billItemMap.get(cbillBid);
      BillHeaderVO headvo = billHeadMap.get(cbillid);
      // ���ǿ���֯����
      if (billitemvo == null) {
        viewList.add(view);
      }
      else {
        String cbilloutStorckOrg = billitemvo.getPk_org();
        String cbilltoutSorckOrg = billitemvo.getCtoutstockorgid();
        // �������뷴��
        UFBoolean bioreverseflag = headvo.getBioreverseflag();
        UFBoolean botreverseflag = headvo.getBotreverseflag();
        UFBoolean biosettleendflag = billitemvo.getBiosettleendflag();
        UFBoolean botsettleendflag = billitemvo.getBotsettleendflag();
        // ����������
        if (!bioreverseflag.booleanValue() && !botreverseflag.booleanValue()) {
          // ��������������Ϊ��
          if (coutStorckOrgid.equals(cbilloutStorckOrg)
              && !biosettleendflag.booleanValue()) {
            viewList.add(view);
          }
          // ���������������Ϊ��
          else if (coutStorckOrgid.equals(cbilltoutSorckOrg)
              && !botsettleendflag.booleanValue()) {
            viewList.add(view);
          }
        }
        // ����������������㣬���������������
        else {
          if (!botreverseflag.booleanValue()) {
            if (coutStorckOrgid.equals(cbilltoutSorckOrg)
                && !botsettleendflag.booleanValue()) {
              viewList.add(view);
            }
            else if (cinStorckOrgid.equals(cbilloutStorckOrg)
                && !biosettleendflag.booleanValue()) {
              viewList.add(view);
            }
          }
          // ����������������㣬���������������
          else if (!bioreverseflag.booleanValue()) {
            // ��������������Ϊ��
            if (coutStorckOrgid.equals(cbilloutStorckOrg)
                && !biosettleendflag.booleanValue()) {
              viewList.add(view);
            }
            else if (cinStorckOrgid.equals(cbilltoutSorckOrg)
                && !botsettleendflag.booleanValue()) {
              viewList.add(view);
            }
          }
          // ����������򡢵�����������
          else {
            if (cinStorckOrgid.equals(cbilloutStorckOrg)
                && !biosettleendflag.booleanValue()) {
              viewList.add(view);
            }
            // ���������������Ϊ��
            else if (cinStorckOrgid.equals(cbilltoutSorckOrg)
                && !botsettleendflag.booleanValue()) {
              viewList.add(view);
            }
          }
        }
      }
    }
    SettleQueryViewVO[] views = new SettleQueryViewVO[viewList.size()];
    viewList.toArray(views);
    return views;
  }

  private void askPrice(SettleQueryViewVO[] vos, IQueryScheme scheme) {
    if (vos.length == 0) {
      return;
    }
    UFBoolean baskPrice = this.getAskPriceFlag(scheme);
    if (null != baskPrice && baskPrice.booleanValue()) {
      AskPriceM5fHandUtils util = new AskPriceM5fHandUtils();
      util.askSettlePrice(vos, this.getGatherByFlag(scheme));
    }
  }

  private void dealTailDiff(IQueryScheme scheme) {
    List<SettleQueryViewVO> list = new ArrayList<SettleQueryViewVO>();
    MapList<String, SettleListItemVO> maplist = this.getDealedVO(list);
    if (list.isEmpty()) {
      return;
    }
    UFBoolean isTaxfirst =
        ParaUtilsForTo.getInstance().getTO01(
            BSContext.getInstance().getGroupID());

    for (SettleQueryViewVO view : list) {
      String pk_org = view.getPk_org();
      if (SettleMainbody.SETTLE_MAIN_BODY_IN.equalsValue(view
          .getFmainbodyflag())) {
        pk_org = view.getCinfinanceorgid();
      }

      String key = view.getCbill_bid() + pk_org;
      List<SettleListItemVO> itemlist = maplist.get(key);
      boolean flag = this.isDeal(itemlist, view);

      if (!flag) {
        // calTool.calculateViewPriceMny(view, SettleQueryViewVO.NORIGTAXPRICE);
        continue;

      }
      // ���һ�����н����β��
      UFDouble nnumber = view.getNnumber();
      UFDouble nqtnumber = view.getNqtnum();
      // �����������Լ����������������ܵĽ�����ֵ
      view.setNnumber(MathTool.sub(view.getNaccinnum(), view.getNaccwastnum()));
      UFDouble nqtnum =
          HslParseUtil.hslDivUFDouble(view.getVqtunitrate(), view.getNnumber());
      view.setNqtnum(nqtnum);
      view.setNqtnum(view.getNnumber());
      this.askPrice(new SettleQueryViewVO[] {
        view
      }, scheme);
      SettleBuildTailDiffPara para = new SettleBuildTailDiffPara();
      for (SettleListItemVO item : itemlist) {
        para.setNorigmny(MathTool.add(para.getNorigmny(), item.getNorigmny()));
        para.setNorigtaxmny(MathTool.add(para.getNorigtaxmny(),
            item.getNorigtaxmny()));
        para.setNcaltaxmny(MathTool.add(para.getNcaltaxmny(),
            item.getNcaltaxmny()));
        para.setNmny(MathTool.add(para.getNmny(), item.getNmny()));
        para.setNtax(MathTool.add(para.getNtax(), item.getNtax()));
        para.setNtaxmny(MathTool.add(para.getNtaxmny(), item.getNtaxmny()));
      }
      view.setNnumber(nnumber);
      view.setNqtnum(nqtnumber);
      view.setNorigmny(MathTool.sub(view.getNorigmny(), para.getNorigmny()));
      view.setNorigtaxmny(MathTool.sub(view.getNorigtaxmny(),
          para.getNorigtaxmny()));
      view.setNcaltaxmny(MathTool.sub(view.getNcaltaxmny(),
          para.getNcaltaxmny()));
      view.setNmny(MathTool.sub(view.getNmny(), para.getNmny()));
      view.setNtax(MathTool.sub(view.getNtax(), para.getNtax()));
      view.setNtaxmny(MathTool.sub(view.getNtaxmny(), para.getNtaxmny()));
    }

  }

  private UFBoolean getAskPriceFlag(IQueryScheme scheme) {
    QueryCondition condition =
        QuerySchemeUtils.getQueryCondition(SettleQueryParaVO.BASKPRICE, scheme);
    if (null == condition) {
      return null;
    }

    Object[] values = condition.getValues();
    return ValueUtils.getUFBoolean(values[0]);

  }

  // �����ѯ����VO

  private MapList<String, SettleListItemVO> getDealedVO(
      List<SettleQueryViewVO> list) {
    Set<String> orgset = new HashSet<String>();
    Set<String> optset = new HashSet<String>();
    for (SettleQueryViewVO view : this.outinAss.getOutVOs()) {
      UFDouble settlenum = view.getBb_nsettlenum();
      if (null == settlenum || MathTool.isZero(settlenum)) {
        continue;
      }
      if (view.getNorigmny() == null || MathTool.isZero(view.getNorigmny())) {
        continue;
      }
      list.add(view);

      if (SettleMainbody.SETTLE_MAIN_BODY_OUT.equalsValue(view
          .getFmainbodyflag())) {
        orgset.add(view.getPk_org());
      }
      else if (SettleMainbody.SETTLE_MAIN_BODY_IN.equalsValue(view
          .getFmainbodyflag())) {
        orgset.add(view.getCinfinanceorgid());
      }
      optset.add(view.getCbill_bid());

    }
    if (list.size() == 0) {
      return new MapList<String, SettleListItemVO>();
    }
    MapList<String, SettleListItemVO> maplist =
        this.queryAgoSettleBill(optset.toArray(new String[optset.size()]),
            orgset.toArray(new String[orgset.size()]));
    return maplist;
  }

  private Integer getFmainBodyFlag(IQueryScheme scheme) {

    QueryCondition condition =
        QuerySchemeUtils.getQueryCondition(SettleQueryParaVO.FMAINBODYFLAG,
            scheme);

    Object[] values = condition.getValues();
    return ValueUtils.getInteger(values[0]);

  }

  private Integer getGatherByFlag(IQueryScheme scheme) {
    QueryCondition condition =
        QuerySchemeUtils.getQueryCondition(SettleQueryParaVO.FGATHEREDBYFLAG,
            scheme);

    Object[] values = condition.getValues();
    return ValueUtils.getInteger(values[0]);

  }

  private MapList<String, PreSettleInLineVO> getPSInLine(String[] bids) {
    VOQuery<PreSettleInLineVO> query =
        new VOQuery<PreSettleInLineVO>(PreSettleInLineVO.class);
    SqlBuilder builder = new SqlBuilder();
    IDExQueryBuilder idbuilder =
        new IDExQueryBuilder(TOTempTableNameConst.TMP_TO_FIR_1ID);
    builder.append(" and ");
    builder.append(idbuilder.buildSQL(PreSettleInLineVO.CBILL_BID, bids));
    PreSettleInLineVO[] vos = query.query(builder.toString(), null);
    MapList<String, PreSettleInLineVO> maplist =
        new MapList<String, PreSettleInLineVO>();
    for (PreSettleInLineVO vo : vos) {
      maplist.put(vo.getCbill_bid(), vo);
    }
    return maplist;

  }

  private SettleQueryTransVO initTransVO(SettleGroupOutAssViewUtil ass,
      Integer fgatherbyflag) {
    SettleQueryTransVO vo = new SettleQueryTransVO();
    vo.setGatherVOs(ass.getGatherVOs());
    MapList<Integer, SettleQueryViewVO> gmap =
        new MapList<Integer, SettleQueryViewVO>();
    for (Entry<Integer, List<SettleQueryViewVO>> entry : ass
        .getGatherDetailMapping().entrySet()) {
      List<SettleQueryViewVO> list = entry.getValue();
      for (SettleQueryViewVO view : list) {
        SettleQueryViewVO newvo = new SettleQueryViewVO();
        newvo.setCbill_bid(view.getCbill_bid());
        newvo.setTs(view.getTs());
        newvo.setAttributeValue(SettleQueryViewVO.NOUTNUM,
            view.getAttributeValue(SettleQueryViewVO.NOUTNUM));

        newvo.setAttributeValue(SettleQueryViewVO.NACCINNUM,
            view.getAttributeValue(SettleQueryViewVO.NACCINNUM));

        newvo.setAttributeValue(SettleQueryViewVO.NACCWASTNUM,
            view.getAttributeValue(SettleQueryViewVO.NACCWASTNUM));

        newvo.setAttributeValue(SettleQueryViewVO.NSHOULDNUM,
            view.getAttributeValue(SettleQueryViewVO.NSHOULDNUM));

        newvo.setNnumber(view.getNnumber());

        newvo.setNqtnum(view.getNqtnum());
        gmap.put(entry.getKey(), newvo);
      }
    }
    MapList<String, PSInViewVO> dmap = new MapList<String, PSInViewVO>();
    for (Entry<String, List<PSInViewVO>> entry : this.outinAss
        .getOutInViewMapping().entrySet()) {
      List<PSInViewVO> list = entry.getValue();
      for (PSInViewVO view : list) {
        PSInViewVO newvo = new PSInViewVO();
        PreSettleInItemVO itemvo = new PreSettleInItemVO();
        itemvo.setCbill_bid(view.getCbill_bid());
        itemvo.setTs(view.getItem().getTs());
        itemvo.setNnumber(view.getItem().getNnumber());
        itemvo.setNqtnum(view.getItem().getNqtnum());
        newvo.setItem(itemvo);
        newvo.setHead(new PreSettleInHeaderVO());
        newvo.setNshldNnumber(view.getNshldNnumber());
        dmap.put(entry.getKey(), newvo);
      }
    }
    vo.setGatherDetailMapping(gmap);
    vo.setOutInViewMapping(dmap);
    vo.setFgatherbyflag(fgatherbyflag);
    return vo;
  }

  private boolean isDeal(List<SettleListItemVO> itemlist, SettleQueryViewVO view) {
    boolean flag = true;
    if (itemlist == null || itemlist.size() <= 0 || itemlist.get(0) == null) {
      return flag;
    }
    UFDouble price = itemlist.get(0).getNorigtaxprice();
    for (SettleListItemVO vo : itemlist) {
      if (MathTool.compareTo(price, vo.getNorigtaxprice()) != 0) {
        flag = false;
        break;
      }
    }

    boolean priceflag =
        MathTool.compareTo(itemlist.get(0).getNorigtaxprice(),
            view.getNorigtaxprice()) == 0 ? true : false;
    return flag && priceflag;
  }

  /**
   * ���������������ǵ��ߵ����ݴ���
   * 
   * @author will
   * @throws BusinessException
   * @time 2010-7-5 ����10:42:29
   */
  private void normalProc(SettleQueryViewVO[] psoData, IQueryScheme scheme) {
    SettleOutInAssUtil ass = new SettleOutInAssUtil();
    // ���ݼ����ϵID��ѯ��������뵥����
    PSInViewVO[] psiData = this.queryPSInVO(psoData, scheme);
    ass.setOutVO(psoData);
    ass.setInVO(psiData);
    ass.initOutInMapping(psoData, psiData);
    // ����
    SettleMatchProcessor processor = new SettleMatchProcessor(ass);
    this.outinAss = processor.getUtil();
  }

  private MapList<String, SettleListItemVO> queryAgoSettleBill(
      String[] coptbids, String[] pk_orgs) {
    IDExQueryBuilder idbuilder =
        new IDExQueryBuilder(TOTempTableNameConst.TMP_TO_FIR_1ID);

    SqlBuilder builder = new SqlBuilder();
    builder.append(" and ");
    builder.append(idbuilder.buildSQL(SettleListItemVO.COPST_BID, coptbids));
    builder.append(" and ");
    builder.append(idbuilder.buildAnotherSQL(SettleListItemVO.PK_ORG, pk_orgs));
    builder.append(" and pk_group ", BSContext.getInstance().getGroupID());

    String[] names =
        new String[] {
          SettleListItemVO.CBILL_BID, SettleListItemVO.CBILLID,
          SettleListItemVO.PK_ORG, SettleListItemVO.COPST_BID,
          SettleListItemVO.NORIGMNY, SettleListItemVO.NORIGPRICE,
          SettleListItemVO.NORIGTAXMNY, SettleListItemVO.NORIGTAXPRICE,
          SettleListItemVO.NNUMBER, SettleListItemVO.NCALTAXMNY,
          SettleListItemVO.NMNY, SettleListItemVO.NTAX,
          SettleListItemVO.NTAXMNY
        };
    VOQuery<SettleListItemVO> query =
        new VOQuery<SettleListItemVO>(SettleListItemVO.class, names);

    SettleListItemVO[] items = query.query(builder.toString(), null);

    MapList<String, SettleListItemVO> maplist =
        new MapList<String, SettleListItemVO>();
    for (SettleListItemVO item : items) {
      String key = item.getCopst_bid() + item.getPk_org();
      maplist.put(key, item);
    }

    return maplist;

  }

  /**
   * ��ѯ��������뵥VO
   * 
   * @author panql
   * @param arrNormOut
   * @time 2010-4-15 ����08:36:36
   */
  private PSInViewVO[] queryPSInVO(SettleQueryViewVO[] arrNormOut,
      IQueryScheme scheme) {
    // ��֯������������Ĺ�������,���������������Ĳ�ѯsql
    SettleQuerySqlBuildUtils util = new SettleQuerySqlBuildUtils();
    String wheresql = util.buildPSInSql(arrNormOut, scheme);

    EfficientViewQuery<PSInViewVO> viewquery =
        new EfficientViewQuery<PSInViewVO>(PSInViewVO.class);
    PSInViewVO[] arrReturn = viewquery.query(wheresql);

    if (arrReturn == null || arrReturn.length == 0) {
      return arrReturn;
    }
    // �������������ֶ�
    for (PSInViewVO arr : arrReturn) {
      arr.getHead().setDbilldate(arr.getItem().getDbilldate());
      arr.getHead().setPk_org(arr.getItem().getPk_org());
      arr.getHead().setPk_group(arr.getItem().getPkGroup());
    }
    Set<String> bids = new HashSet<String>();
    for (PSInViewVO arr : arrReturn) {
      bids.add(arr.getCbill_bid());
    }
    MapList<String, PreSettleInLineVO> maplist =
        this.getPSInLine(bids.toArray(new String[0]));
    for (PSInViewVO view : arrReturn) {
      PreSettleInItemVO item = view.getItem();
      String pk_org = view.getHead().getPk_org();
      List<PreSettleInLineVO> list = maplist.get(item.getCbill_bid());
      PreSettleInLineVO line = null;
      for (PreSettleInLineVO bbvo : list) {
        if (bbvo.getCdownfinanceorgid().equals(pk_org)) {
          line = bbvo;
          break;
        }
      }
      if (line == null) {
        ExceptionUtils.unSupported();
        break;
      }
      view.setLinevos(new PreSettleInLineVO[] {
        line
      });
    }

    return arrReturn;
  }

  /**
   * ��ѯ�����������VO
   * 
   * @author panql
   * @time 2010-4-15 ����08:36:23
   */
  // ��֯������������Ĺ�������,���������������Ĳ�ѯsql
  private SettleQueryViewVO[] queryPSOutVO(IQueryScheme scheme) {

    SettleQuerySqlBuildUtils util = new SettleQuerySqlBuildUtils();
    String wheresql = util.buildPSOutSql(scheme);

    TOEfficientViewQuery<PSOutViewVO> query =
        new TOEfficientViewQuery<PSOutViewVO>(PSOutViewVO.class);
    PSOutViewVO[] psoutviewvo =
        query.query(wheresql, SettleQueryAction.ROWSIZE);

    // ��ѯ�����������������
    if (psoutviewvo == null || psoutviewvo.length == 0) {
      return new SettleQueryViewVO[0];
    }
    SetPSOutViewTOSettleView setViewTool = new SetPSOutViewTOSettleView();
    SettleQueryViewVO[] arrReturn = setViewTool.setLineToViewVO(psoutviewvo);
    return arrReturn;

  }

  /**
   * ���߽���Բ�ѯ���Ĵ��������������
   * 
   * @author will 2010-7-7 ����07:01:24
   */
  private void singleProc(SettleQueryViewVO[] arrSingleOut) {
    SettleOutInAssUtil ass = new SettleOutInAssUtil();
    ass.setOutVO(arrSingleOut);
    SettleMatchProcessor processor = new SettleMatchProcessor(ass);
    ass = processor.getUtil();

    List<SettleQueryViewVO> list = new ArrayList<SettleQueryViewVO>();
    SettleQueryViewVO[] view = this.outinAss.getOutVOs();
    // ���ߺͷǵ�����ϵ�һ�����
    if (null != view && view.length != 0) {
      list.addAll(Arrays.asList(view));
    }
    list.addAll(Arrays.asList(ass.getOutVOs()));
    this.outinAss.setOutVO(list.toArray(new SettleQueryViewVO[list.size()]));
  }
}
