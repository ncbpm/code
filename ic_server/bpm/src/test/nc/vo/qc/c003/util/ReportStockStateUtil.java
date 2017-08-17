package nc.vo.qc.c003.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.ic.storestate.IStateQueryServiceForQc;
import nc.pubitf.qc.c003.pub.IReportPubQuery;
import nc.pubitf.scmf.qc.qualitylevel.qc.IQueryQualityLevelForQc;
import nc.vo.ic.m4460.entity.DeAdjustForQcParamVO;
import nc.vo.ic.m4460.entity.StateAdjustForQcParamVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.c003.entity.ReportItemVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.transtype.entity.ReportTranstypeVO;
import nc.vo.qc.transtype.service.ITranstypeQuery;
import nc.vo.scmf.qc.qualitylevel.entity.QualityLevelItemVO;
import nc.vo.scmpub.res.billtype.MMBillType;
import nc.vo.scmpub.res.billtype.POBillType;
import nc.vo.scmpub.res.billtype.QCBillType;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 该类完成合生元项目质检报告 补充库存状态 生成库存调整单的工具类 检验报告的表头库存状态赋值
 * 
 * @author zhanglongf
 */
public class ReportStockStateUtil {

  /**
   * VMI供应商对应信息
   */
  private Map<String, Boolean> dic = null;

  // private Map<String, String> mapforVID = new HashMap<String, String>();

  private Map<String, String> mapMaterialFlag = new HashMap<String, String>();

  // private Map<String, String> mapWRforStockPK = new HashMap<String,
  // String>();

  /**
   * 当前操作的检验报告VO
   */

  private ReportTranstypeVO reporttranstypeVO = null;

  /**
   * 构造方法
   * 
   * @param vo
   */
  public ReportStockStateUtil() {
  }

  /**
   * 质检报告前后库存状态是否自动赋值：1、只有采购到货报检或生产 报检支持。2.只有物料开启库存状态管理支持。
   * <p>
   * <b>参数说明</b>
   * 
   * @param reportvo
   * @return <p>
   * @since 6.3
   * @author heichl
   * @throws BusinessException
   * @time 2013-5-29 下午01:14:49
   */
  public UFBoolean autoSetStockState(ReportVO reportvo)
      throws BusinessException {

    // 1.只有采购到货报检或生产 报检支持
    String cfirsttypecode = reportvo.getBVO()[0].getCfirsttypecode();
    if (!POBillType.Arrive.getCode().equals(cfirsttypecode)
        && !MMBillType.ProduceReport.getCode().equals(cfirsttypecode)) {
      return UFBoolean.FALSE;
    }

    // 2.只有物料开启库存状态管理支持
    String pk_material = reportvo.getHVO().getPk_srcmaterial();
    String flag = this.queryMaterial(pk_material);
    if (flag == null || !flag.equals("Y")) {
      return UFBoolean.FALSE;
    }

    return UFBoolean.TRUE;

  }

  /**
   * 完成向下游推单的动作
   * 
   * @throws BusinessException
   */
  public void createStateAdjust(ReportVO[] reportvos) throws BusinessException {

    // 获取交易类型上记录的【采购、完工检验自动调整库存状态】
    ReportTranstypeVO reportTranstypeVO = this.getreportTranstyVO(reportvos[0]);
    List<StateAdjustForQcParamVO> list =
        new ArrayList<StateAdjustForQcParamVO>();
    // this.dic = this.getCvmivenderid(reportvos);

    for (ReportVO reportvo : reportvos) {
      // 质检报告审核时自动调整库存状态
      if (this.autoSetStockState(reportvo).booleanValue()
          && this.autoAjustStockState(reportvo).booleanValue()) {
        list.addAll(this.createInterfaceVO(reportvo));
      }
    }
    if (list.size() > 0) {
      try {
        ITranstypeQuery service =
            NCLocator.getInstance().lookup(ITranstypeQuery.class);
        service.adjustForReportBill_RequiresNew(list);
      }
      catch (BusinessException e) {
        // 【自动调整库存状态失败时 质检报告审批通过】勾选
        if (!reportTranstypeVO.getBalwayspass().booleanValue()) {
          ExceptionUtils.wrappException(e);
        }
      }
      catch (Exception e) {
        // 【自动调整库存状态失败时 质检报告审批通过】勾选
        if (!reportTranstypeVO.getBalwayspass().booleanValue()) {
          ExceptionUtils.wrappException(e);
        }
      }
    }
  }

  /**
   * 反审的操作
   * 
   * @param vos
   * @throws BusinessException
   */
  public void deleteStockState(ReportVO[] vos) throws BusinessException {
    // 获取交易类型上记录的【采购、完工检验自动调整库存状态】
    ReportTranstypeVO reportTranstypeVO = this.getreportTranstyVO(vos[0]);
    List<DeAdjustForQcParamVO> params = new ArrayList<DeAdjustForQcParamVO>();
    for (ReportVO reportVO : vos) {
      if (this.autoSetStockState(reportVO).booleanValue()
          && this.autoAjustStockState(reportVO).booleanValue()) {
        DeAdjustForQcParamVO[] pars = this.getdeadjustforqcoaramVO(reportVO);
        if (!ArrayUtils.isEmpty(pars)) {
          for (DeAdjustForQcParamVO par : pars) {
            params.add(par);
          }
        }
      }
    }
    try {
      ITranstypeQuery service =
          NCLocator.getInstance().lookup(ITranstypeQuery.class);
      service.deAdjustForReportBill_RequiresNew(params
          .toArray(new DeAdjustForQcParamVO[] {}));
    }
    catch (BusinessException e) {
      // 【自动调整库存状态失败时 质检报告审批通过】勾选
      if (!reportTranstypeVO.getBalwayspass().booleanValue()) {
        ExceptionUtils.wrappException(e);
      }
    }
    catch (Exception e) {
      // 【自动调整库存状态失败时 质检报告审批通过】勾选
      if (!reportTranstypeVO.getBalwayspass().booleanValue()) {
        ExceptionUtils.wrappException(e);
      }
    }
  }

  /**
   * 设置检前后库存状态 <br>
   * 采购、完工检验自动调整库存状态不勾选也记录
   * 
   * @throws BusinessException
   */
  public void dosetbeforestockstate(ReportVO reportvo) throws BusinessException {

    if (UFBoolean.FALSE.equals(this.autoSetStockState(reportvo))) {
      return;
    }
    // String pk_preStockState = this.getpreStockState();
    // // 设置表头默认库存状态
    // reportvo.getHVO().setPk_prestockstate(pk_preStockState);
    if (reportvo.getHVO().getStatus() != VOStatus.NEW) {
      reportvo.getHVO().setStatus(VOStatus.UPDATED);
    }
    // 后台补充检验后状态
    for (ReportItemVO item : reportvo.getBVO()) {
      if (StringUtils.isBlank(item.getPk_qualitylv_b())
          && StringUtils.isBlank(item.getPk_afterstockstate())) {
        IQueryQualityLevelForQc sr =
            NCLocator.getInstance().lookup(IQueryQualityLevelForQc.class);
        Map<String, QualityLevelItemVO> level =
            sr.queryQualityLevel(new String[] {
              item.getPk_qualitylv_b()
            });
        QualityLevelItemVO qualityLevelVo = level.get(item.getPk_qualitylv_b());
        if (null != qualityLevelVo) {
          item.setPk_afterstockstate(qualityLevelVo.getPk_stockstate());
        }
      }
      String pk_afterstockstate = item.getPk_afterstockstate();
      if (StringUtils.isBlank(pk_afterstockstate)) {
//        ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
//            .getNCLangRes().getStrByID("c0101015", "1C01010150008"));
//        /** 质检报告需要自动更新库存状态时，库存状态不能为空。 */

      }
    }

    // 表体入库批次号必须和表头物料批次号相同,且库存状态不能为空
    this.checkbanch(reportvo);
  }

  /**
   * 质检报告审核时是否自动调整库存状态
   * <p>
   * <b>参数说明</b>
   * 
   * @param reportvo
   * @return
   * @throws BusinessException
   *           <p>
   * @since 6.3
   * @author heichl
   * @time 2013-5-29 下午03:17:38
   */
  private UFBoolean autoAjustStockState(ReportVO reportvo)
      throws BusinessException {

    // 1.如果库存状态不自动赋值
    if (UFBoolean.FALSE.equals(this.autoSetStockState(reportvo))) {
      return UFBoolean.FALSE;
    }

    // 获取交易类型上记录的【采购、完工检验自动调整库存状态】
    ReportTranstypeVO reportTranstypeVO = this.getreportTranstyVO(reportvo);

    // 2.【采购、完工检验自动调整库存状态】未勾选
    if (UFBoolean.FALSE.equals(reportTranstypeVO.getBautoupdateic())
    // || UFBoolean.FALSE.equals(this.checkStock(reportvo))
    ) {
      return UFBoolean.FALSE;
    }

    return UFBoolean.TRUE;
  }

  /**
   * 检验表体批次号 和表头批次号是否一致，不一致抛错
   * 
   * @throws BusinessException
   */
  private void checkbanch(ReportVO reportvo) throws BusinessException {
    if (this.autoAjustStockState(reportvo).booleanValue()) {
      // 获取表头物料批次号
      String pk_banch =
          reportvo.getHVO().getVbatchcode_h() == null ? "" : reportvo.getHVO()
              .getVbatchcode_h();
      for (ReportItemVO body : reportvo.getBVO()) {
        // 1. 自动调状态勾选
        // 2.
        if (StringUtils.isNotBlank(pk_banch)) {
          String bodybabch =
              body.getVbatchcode() == null ? "" : body.getVbatchcode();
          if (!bodybabch.equals(pk_banch)) {
            ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
                .getNCLangRes().getStrByID("c0101015", "1C01010150003"));
            /** xx单据物料启用了自动调整库存状态，保存时表头物料批次号与表体入库批次号必须一致。 */
          }
        }
      }
    }
  }

  // /**
  // * 校验单头默认库存状态是否有值
  // *
  // * @throws BusinessException
  // */
  // private void checkHeadPreStockState(ReportVO reportvo)
  // throws BusinessException {
  // if (org.apache.commons.lang.StringUtils.isBlank(reportvo.getHVO()
  // .getPk_prestockstate())) {
  // throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
  // .getStrByID("c010003_0", "取消审核成功。")/* @res "取消审核成功" */);
  // }
  // }

  // /**
  // * 检查物料是否开启库存状态管理
  // *
  // * @param pk_material
  // * @return
  // * @throws BusinessException
  // */
  // private UFBoolean checkisMaterialUnderstockStateControl(String pk_material)
  // throws BusinessException {
  // // 默认为开启库存状态管理
  // UFBoolean result = UFBoolean.TRUE;
  // String flag = this.queryMaterial(pk_material);
  // if (flag == null || !flag.equals("Y")) {
  // result = UFBoolean.FALSE;
  // }
  // return result;
  // }

  // /**
  // * 检查是否有仓库
  // *
  // * @return
  // */
  // private UFBoolean checkStock(ReportVO reportvo) {
  // // 默認存在倉庫
  // UFBoolean result = UFBoolean.TRUE;
  // if (reportvo.getBVO()[0].getCfirsttypecode().equals(
  // POBillType.Arrive.getCode())) {
  // // 来源是采购的，那么会自动带过来，则只检查表头的仓库就行
  // // 没有就不生单了
  // if (StringUtils.isBlank(reportvo.getHVO().getPk_stordoc())) {
  // result = UFBoolean.FALSE;
  // }
  // }
  // else if (reportvo.getBVO()[0].getCfirsttypecode().equals(
  // MMBillType.ProduceReport.getCode())) {
  // // 来源于生产的查一下
  // this.queryPKStock(reportvo);
  // if (!this.mapWRforStockPK.containsKey(reportvo.getHVO()
  // .getPk_reportbill())
  // || StringUtils.isBlank(this.mapWRforStockPK.get(reportvo.getHVO()
  // .getPk_reportbill()))) {
  // result = UFBoolean.FALSE;
  // }
  // }
  // return result;
  // }

  /**
   * 构建生单的传递DTO;如果表体的批次没有就不用调整
   * 
   * @param reportvo
   * @return
   */
  private List<StateAdjustForQcParamVO> createInterfaceVO(ReportVO reportvo) {

    List<StateAdjustForQcParamVO> list =
        new ArrayList<StateAdjustForQcParamVO>();
    Set<String> dimSet = new HashSet<String>();

    for (ReportItemVO body : reportvo.getBVO()) {
      // 如果物料是改判的，或者批次号为空(序列号为空) ，则不管调整;序列号不为空也要调整
      if (body.getBchanged().booleanValue()
          && !StringUtils.isBlank(body.getPk_chgmrl())
          || StringUtils.isBlank(body.getPk_batchcode())
          && StringUtils.isBlank(reportvo.getHVO().getPk_serialno())) {
        continue;
      }
      StateAdjustForQcParamVO result = new StateAdjustForQcParamVO();
      // 用于过滤维度，同一个维度的只传一组
      StringBuffer key = new StringBuffer();

      // 调整单主键
      // 调整单据类型、调整单据号、调整单据主键、调整单据表体主键
      result.setCadjustbilltype(QCBillType.ReportBill.getCode());// 调整单据类型
      result.setCadjustbillcode(reportvo.getHVO().getVbillcode());// 调整单据号
      result.setCadjustbillid(reportvo.getHVO().getPk_reportbill());// 调整单据主键
      result.setCadjustbillbid(body.getPk_reportbill_b());// 调整单据表体主键
      result.setVtrantypecode(reportvo.getHVO().getVtrantypecode());// 调整单据交易类型
      result.setCtrantypeid(reportvo.getHVO().getCtrantypeid());// 调整单据交易类型
      result.setCadjustrowno(body.getCrowno());// 调整单据行号
      if (StringUtils.isNotBlank(reportvo.getHVO().getVsncode())) {
        result.setVsncode(reportvo.getHVO().getVsncode());
        key.append(reportvo.getHVO().getVsncode());
      }

      // 现存量维度
      // 物料+物料版本+库存组织+仓库+批次+计量单位+换算率+固定辅助属性+自由辅助属性+检前库存状态
      // +寄存供应商+货主客户

      // 现存量维度 modify by fengjqc 20150514
      // 物料+物料版本+库存组织+批次+固定辅助属性（供应商、项目、生产厂商、客户）+自由辅助属性（1-10）

      // OnhandDimVO onhandDimVO = new OnhandDimVO();
      // result.setStateAdjustDimVo(onhandDimVO);

      List<String> onhandDimFields = new ArrayList<String>();
      List<String> onhandDimValues = new ArrayList<String>();

      // 现存量维度赋值
      {
        onhandDimFields.add(StateAdjustForQcParamVO.CMATERIALOID);
        onhandDimValues.add(reportvo.getHVO().getPk_srcmaterial());// 物料
        key.append(reportvo.getHVO().getPk_srcmaterial());

        onhandDimFields.add(StateAdjustForQcParamVO.CMATERIALVID);
        onhandDimValues.add(reportvo.getHVO().getPk_material());// 物料多版本
        key.append(reportvo.getHVO().getPk_material());

        onhandDimFields.add(StateAdjustForQcParamVO.PK_ORG);
        onhandDimValues.add(reportvo.getHVO().getPk_stockorg());// 库存组织
        key.append(reportvo.getHVO().getPk_stockorg());

        if (StringUtils.isBlank(reportvo.getHVO().getVsncode())) {

          onhandDimFields.add(StateAdjustForQcParamVO.PK_BATCHCODE);
          onhandDimValues.add(body.getPk_batchcode());// 物料批次号主键
          key.append(body.getPk_batchcode());

          onhandDimFields.add(StateAdjustForQcParamVO.VFREE1);
          onhandDimValues.add(body.getVfree1());
          key.append(body.getVfree1());

          onhandDimFields.add(StateAdjustForQcParamVO.VFREE2);
          onhandDimValues.add(body.getVfree2());
          key.append(body.getVfree2());

          onhandDimFields.add(StateAdjustForQcParamVO.VFREE3);
          onhandDimValues.add(body.getVfree3());
          key.append(body.getVfree3());

          onhandDimFields.add(StateAdjustForQcParamVO.VFREE4);
          onhandDimValues.add(body.getVfree4());
          key.append(body.getVfree4());

          onhandDimFields.add(StateAdjustForQcParamVO.VFREE5);
          onhandDimValues.add(body.getVfree5());
          key.append(body.getVfree5());

          onhandDimFields.add(StateAdjustForQcParamVO.VFREE6);
          onhandDimValues.add(body.getVfree6());
          key.append(body.getVfree6());

          onhandDimFields.add(StateAdjustForQcParamVO.VFREE7);
          onhandDimValues.add(body.getVfree7());
          key.append(body.getVfree7());

          onhandDimFields.add(StateAdjustForQcParamVO.VFREE8);
          onhandDimValues.add(body.getVfree8());
          key.append(body.getVfree8());

          onhandDimFields.add(StateAdjustForQcParamVO.VFREE9);
          onhandDimValues.add(body.getVfree9());
          key.append(body.getVfree9());

          onhandDimFields.add(StateAdjustForQcParamVO.VFREE10);
          onhandDimValues.add(body.getVfree10());
          key.append(body.getVfree10());

          onhandDimFields.add(StateAdjustForQcParamVO.CVENDORID);
          onhandDimValues.add(reportvo.getHVO().getPk_supplier());// 供应商
          key.append(reportvo.getHVO().getPk_supplier());

          onhandDimFields.add(StateAdjustForQcParamVO.CASSCUSTID);
          onhandDimValues.add(reportvo.getHVO().getPk_customer());// 客户
          key.append(reportvo.getHVO().getPk_customer());

          onhandDimFields.add(StateAdjustForQcParamVO.CPROJECTID);
          onhandDimValues.add(reportvo.getHVO().getCprojectid());// 项目
          key.append(reportvo.getHVO().getCprojectid());

          // 质检报告没有生产厂商
        }
      }

      // 调整属性
      result.setNadjustnum(body.getNnum());// 调整主数量
      result.setNadjustassistnum(body.getNastnum());// 调整数量
      result.setCadjuststateid(body.getPk_afterstockstate());// 检验后库存状态

      result.setOnhandDimFields(onhandDimFields.toArray(new String[] {}));
      result.setOnhandDimValues(onhandDimValues.toArray(new String[] {}));

      if (!dimSet.contains(key)) {
        list.add(result);
        dimSet.add(key.toString());
      }
    }

    return list;

  }

  // /**
  // * 查询VMIID
  // *
  // * @param vos
  // * @return
  // * @throws BusinessException
  // */
  // private Map<String, Boolean> getCvmivenderid(ReportVO[] vos)
  // throws BusinessException {
  // // 质检报告ID--来源到货单Code
  // Map<String, String> reportIDtoArriveCode = new HashMap<String, String>();
  // for (ReportVO reportVO : vos) {
  // String cfirsttypecode = reportVO.getBVO()[0].getCfirsttypecode();
  // // 来源是到货单
  // if (POBillType.Arrive.getCode().equals(cfirsttypecode)) {
  // reportIDtoArriveCode.put(reportVO.getHVO().getPk_applybill(),
  // reportVO.getBVO()[0].getVfirstcode());
  // }
  // }
  // if (reportIDtoArriveCode.keySet().size() <= 0) {
  // return null;
  // }
  // // 已经做过来源为21 的筛选 质检报告ID--到货单VO
  // Map<String, ArriveVO> arriveVOs = this.queryArriveVO(reportIDtoArriveCode);
  // String[] keys = new String[arriveVOs.keySet().size()];
  // int i = 0;
  // for (String key : arriveVOs.keySet()) {
  // keys[i] = arriveVOs.get(key).getBVO()[0].getVsourcetrantype();
  // }
  // // key：到货单ID -- value 来源单据的交易类型VO
  // Map<String, PoTransTypeVO> arriveVOss = this.getOpenSupplierArriveVO(keys);
  //
  // Map<String, Boolean> result = new HashMap<String, Boolean>();
  // for (String reportID : arriveVOs.keySet()) {
  // result.put(
  // reportID,
  // Boolean.valueOf(arriveVOss
  // .get(arriveVOs.get(reportID).getBVO()[0].getVsourcetrantype())
  // .getBvmi().booleanValue()));
  // }
  // return result;
  // }

  /**
   * 创建反审时的接口参数
   * 
   * @param vo
   * @return
   */
  private DeAdjustForQcParamVO[] getdeadjustforqcoaramVO(ReportVO vo) {
    List<DeAdjustForQcParamVO> list = new ArrayList<DeAdjustForQcParamVO>();
    for (ReportItemVO body : vo.getBVO()) {
      DeAdjustForQcParamVO result = new DeAdjustForQcParamVO();
      result.setCadjustbillid(vo.getHVO().getPk_reportbill());
      result.setCadjustbilltype(QCBillType.ReportBill.getCode());
      // 如果物料是改判的 ，则不管 继续
      if (body.getBchanged().booleanValue()
          && !StringUtils.isBlank(body.getPk_chgmrl())) {
        continue;
      }
      result.setCadjustbillbid(body.getPk_reportbill_b());
      list.add(result);
    }
    return list.toArray(new DeAdjustForQcParamVO[0]);
  }

  // /**
  // * 获取待版本的物料
  // *
  // * @param vos
  // */
  // private void getmaterialVersion(ReportVO[] vos) {
  // String[] oids = new String[vos.length];
  // for (int i = 0; i < vos.length; i++) {
  // oids[i] = vos[i].getHVO().getPk_material();
  // }
  //
  // Map<String, String> map = MaterialPubService.queryMaterialOidByVid(oids);
  // }

  // /**
  // * 获取开启采购到货供应商
  // *
  // * @param sourseids
  // * @return
  // * @throws BusinessException
  // */
  // private Map<String, PoTransTypeVO> getOpenSupplierArriveVO(String[]
  // sourseids)
  // throws BusinessException {
  //
  // IPoTransTypeQuery query =
  // NCLocator.getInstance().lookup(IPoTransTypeQuery.class);
  // return query.queryAttrByIDs(sourseids);
  //
  // }

  /**
   * 获取默认库状态
   * 
   * @return
   * @throws BusinessException
   */
  private String getpreStockState() throws BusinessException {
    IStateQueryServiceForQc statequery =
        NCLocator.getInstance().lookup(IStateQueryServiceForQc.class);
    String defaultStoreState = statequery.queryQcDefaultStoreState();
    return defaultStoreState;
  }

  // /**
  // * 根据质检报告标题行PK查询质检报告表体VO
  // *
  // * @param itemPK
  // * @return
  // */
  // private ReportItemVO getReportItem(String itemPK) {
  // VOQuery<ReportItemVO> query = new
  // VOQuery<ReportItemVO>(ReportItemVO.class);
  // ReportItemVO[] vos = query.query(new String[] {
  // itemPK
  // });
  // if (vos.length > 0) {
  // return vos[0];
  // }
  // return null;
  // }

  /**
   * 获取交易类型扩展信息
   * 
   * @return
   * @throws BusinessException
   */
  private ReportTranstypeVO getreportTranstyVO(ReportVO reportvo)
      throws BusinessException {
    if (null == this.reporttranstypeVO) {
      ITranstypeQuery billQuery =
          NCLocator.getInstance().lookup(ITranstypeQuery.class);
      ReportTranstypeVO[] vos =
          billQuery.queryReportTranstype(reportvo.getHVO().getPk_group(),
              reportvo.getHVO().getVtrantypecode(), reportvo.getHVO()
                  .getCtrantypeid());
      if (vos.length > 0) {
        this.reporttranstypeVO = vos[0];
      }
      else {
        // 未能找到相关的交易类型扩展信息
        throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("c0101015", "1C01010150002")/* 未能找到相关的交易类型扩展信息 */);
      }
    }
    return this.reporttranstypeVO;
  }

  // /**
  // * 根据
  // *
  // * @return
  // */
  // private UFBoolean isCanCreateBill(ReportVO reportvo) {
  // ReportItemVO body = reportvo.getBVO()[0];
  // if (body.getCfirsttypecode().equals(POBillType.Arrive.getCode())) {
  // // 来源是到货单
  // return this.isCanCreateBillFromArrive(reportvo);
  // }
  // else if (body.getCfirsttypecode()
  // .equals(MMBillType.ProduceReport.getCode())) {
  // // 来源是生产
  // return this.isCanCreateBillFromProduct(reportvo);
  // }
  // else {
  // return UFBoolean.FALSE;
  // }
  // }

  // private UFBoolean isCanCreateBillFromArrive(ReportVO reportvo) {
  // SqlBuilder sb = new SqlBuilder();
  // sb.append(" select sum(arrb.naccumstorenum) as snum,sum(arrb.NNUM) as anum,rp.rnum,rp.CFIRSTID,rp.CFIRSTBID,rp.pk_reportbill  from po_arriveorder arr ");
  // sb.append(" left join PO_ARRIVEORDER_B arrb on arr.pk_arriveorder=arrb.PK_ARRIVEORDER ");
  // sb.append(" join (select rb.CFIRSTID,rb.CFIRSTBID,sum(rb.NNUM) as rnum,rb.pk_reportbill  from qc_reportbill_b rb ");
  // sb.append(" where  rb.pk_reportbill= '"
  // + reportvo.getHVO().getPk_reportbill() + "' and dr=0 ");
  // sb.append(" group by rb.CFIRSTID,rb.CFIRSTBID,rb.pk_reportbill) rp on arr.pk_arriveorder=rp.CFIRSTID and arrb.PK_ARRIVEORDER_B=rp.CFIRSTBID ");
  // sb.append(" where arrb.dr=0 group by rp.CFIRSTID,rp.CFIRSTBID,rp.rnum,rp.pk_reportbill");
  //
  // DataAccessUtils utils = new DataAccessUtils();
  // IRowSet rowset = utils.query(sb.toString());
  //
  // if (rowset.size() == 1) {
  // String snum = rowset.toTwoDimensionStringArray()[0][0].toString();
  // String anum = rowset.toTwoDimensionStringArray()[0][1].toString();
  // String rnum = rowset.toTwoDimensionStringArray()[0][2].toString();
  // String firsrid = rowset.toTwoDimensionStringArray()[0][3].toString();
  // String firstbid = rowset.toTwoDimensionStringArray()[0][4].toString();
  // if (StringUtils.isBlank(snum) || snum.equals("0")) {
  // return UFBoolean.FALSE;
  // }
  // else {
  // if (anum.equals(snum) && snum.equals(rnum) && anum.equals(rnum)) {
  // return UFBoolean.TRUE;
  // }
  // else {
  // return UFBoolean.FALSE;
  // }
  // }
  // }
  // return UFBoolean.FALSE;
  // }
  //
  // private UFBoolean isCanCreateBillFromProduct(ReportVO reportvo) {
  // SqlBuilder sb = new SqlBuilder();
  // sb.append(" select nbplanwrnum as wnum,q.nginnum as snum,rp.rnum,rp.CFIRSTID,rp.CFIRSTBID ");
  // sb.append(" from mm_wr_product p  ");
  // sb.append(" join (select rb.CFIRSTID,rb.CFIRSTBID,sum(rb.NNUM) as rnum from qc_reportbill_b rb  ");
  // sb.append(" where  rb.pk_reportbill='"
  // + reportvo.getHVO().getPk_reportbill() + "' and dr=0 ");
  // sb.append("  group by rb.CFIRSTID,rb.CFIRSTBID,rb.cfirsttypecode)  rp on rp.CFIRSTID=p.PK_WR and p.PK_WR_PRODUCT=rp.CFIRSTBID ");
  // sb.append(" join mm_wr_quality  q on q.PK_WR_PRODUCT_Q =rp.CFIRSTBID and q.PK_WR_PRODUCT_Q=p.PK_WR_PRODUCT ");
  // sb.append("  where p.dr=0 and q.dr=0 ");
  //
  // DataAccessUtils utils = new DataAccessUtils();
  // IRowSet rowset = utils.query(sb.toString());
  //
  // if (rowset.size() == 1) {
  // String snum = rowset.toTwoDimensionStringArray()[0][1].toString();
  // String anum = rowset.toTwoDimensionStringArray()[0][0].toString();
  // String rnum = rowset.toTwoDimensionStringArray()[0][2].toString();
  // String firsrid = rowset.toTwoDimensionStringArray()[0][3].toString();
  // String firstbid = rowset.toTwoDimensionStringArray()[0][4].toString();
  // if (StringUtils.isBlank(snum) || snum.equals("0")) {
  // return UFBoolean.FALSE;
  // }
  // else {
  // if (anum.equals(snum) && snum.equals(rnum) && anum.equals(rnum)) {
  // return UFBoolean.TRUE;
  // }
  // else {
  // return UFBoolean.FALSE;
  // }
  // }
  // }
  // return UFBoolean.FALSE;
  // }

  // /**
  // * 获取到货单的aggvo
  // *
  // * @param map
  // * @return
  // */
  // private Map<String, ArriveVO> queryArriveVO(Map<String, String> map) {
  // String[] codes = new String[map.keySet().size()];
  // int i = 0;
  // for (String key : map.keySet()) {
  // codes[i] = map.get(key);
  // i++;
  // }
  // // 查询接口
  // IArriveBillQuery billQuery =
  // NCLocator.getInstance().lookup(IArriveBillQuery.class);
  // ArriveVO[] vos = billQuery.queryArriveAggVo(codes);
  // Map<String, ArriveVO> mapresult = new HashMap<String, ArriveVO>();
  // for (ArriveVO arriveVO : vos) {
  // for (String key : map.keySet()) {
  // if (arriveVO.getBVO()[0].getCsourcetypecode().equals(
  // POBillType.Order.getCode())
  // && arriveVO.getHVO().getVbillcode().equals(map.get(key))) {
  // mapresult.put(key, arriveVO);
  // break;
  // }
  // }
  // }
  // return mapresult;
  // }

  /**
   * 查询物料信息
   * 
   * @param pk_material
   * @return
   * @throws BusinessException
   */
  private String queryMaterial(String pk_material) throws BusinessException {
    // 如果緩存沒有
    if (!this.mapMaterialFlag.keySet().contains(pk_material)) {
      // 去后台查询 并缓存住
      this.querymaterialInfo(new String[] {
        pk_material
      });
    }
    // 始终从缓存拿到数据， 缓存生命周期同类的实例
    if (this.mapMaterialFlag.keySet().contains(pk_material)) {
      return this.mapMaterialFlag.get(pk_material);
    }
    return "";
  }

  /**
   * 到后台查询物料【库存状态】管理的启用情况
   * 
   * @param pk_materials
   *          物料pk 集合
   * @throws BusinessException
   */
  private void querymaterialInfo(String[] pk_materials)
      throws BusinessException {
    // 查询接口
    IReportPubQuery billQuery =
        NCLocator.getInstance().lookup(IReportPubQuery.class);

    // 查询到的结果
    Map<String, String> materialmap = billQuery.querySubMaterial(pk_materials);
    // 缓存住结果集
    this.mapMaterialFlag.putAll(materialmap);
  }

  // private void queryPKStock(ReportVO reportvo) {
  // if (!this.mapWRforStockPK.keySet().contains(
  // reportvo.getHVO().getPk_reportbill())) {
  //
  // // TODO TODO
  // SqlBuilder sb = new SqlBuilder();
  // sb.append(" SELECT h.CWAREHOUSEID, b.CSTATEID, b.NNUM, b.NASSISTNUM");
  // sb.append(" FROM ic_finprodin_h h INNER JOIN ic_finprodin_b b");
  // sb.append(" ON b.CGENERALHID = h.CGENERALHID WHERE b.csourcebillhid='");
  // sb.append(reportvo.getBVO()[0].getCfirstid());
  // sb.append("' AND b.csourcetype='55A4' AND b.dr=0 AND h.dr=0");
  // DataAccessUtils utils = new DataAccessUtils();
  // IRowSet rowset = utils.query(sb.toString());
  // String stock = null;
  // if (rowset.size() > 0) {
  // stock = rowset.toTwoDimensionStringArray()[0][0].toString();
  // }
  //
  // this.mapWRforStockPK.put(reportvo.getHVO().getPk_reportbill(), stock);
  // }
  // }
}
