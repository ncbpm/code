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
 * ������ɺ���Ԫ��Ŀ�ʼ챨�� ������״̬ ���ɿ��������Ĺ����� ���鱨��ı�ͷ���״̬��ֵ
 * 
 * @author zhanglongf
 */
public class ReportStockStateUtil {

  /**
   * VMI��Ӧ�̶�Ӧ��Ϣ
   */
  private Map<String, Boolean> dic = null;

  // private Map<String, String> mapforVID = new HashMap<String, String>();

  private Map<String, String> mapMaterialFlag = new HashMap<String, String>();

  // private Map<String, String> mapWRforStockPK = new HashMap<String,
  // String>();

  /**
   * ��ǰ�����ļ��鱨��VO
   */

  private ReportTranstypeVO reporttranstypeVO = null;

  /**
   * ���췽��
   * 
   * @param vo
   */
  public ReportStockStateUtil() {
  }

  /**
   * �ʼ챨��ǰ����״̬�Ƿ��Զ���ֵ��1��ֻ�вɹ�������������� ����֧�֡�2.ֻ�����Ͽ������״̬����֧�֡�
   * <p>
   * <b>����˵��</b>
   * 
   * @param reportvo
   * @return <p>
   * @since 6.3
   * @author heichl
   * @throws BusinessException
   * @time 2013-5-29 ����01:14:49
   */
  public UFBoolean autoSetStockState(ReportVO reportvo)
      throws BusinessException {

    // 1.ֻ�вɹ�������������� ����֧��
    String cfirsttypecode = reportvo.getBVO()[0].getCfirsttypecode();
    if (!POBillType.Arrive.getCode().equals(cfirsttypecode)
        && !MMBillType.ProduceReport.getCode().equals(cfirsttypecode)) {
      return UFBoolean.FALSE;
    }

    // 2.ֻ�����Ͽ������״̬����֧��
    String pk_material = reportvo.getHVO().getPk_srcmaterial();
    String flag = this.queryMaterial(pk_material);
    if (flag == null || !flag.equals("Y")) {
      return UFBoolean.FALSE;
    }

    return UFBoolean.TRUE;

  }

  /**
   * ����������Ƶ��Ķ���
   * 
   * @throws BusinessException
   */
  public void createStateAdjust(ReportVO[] reportvos) throws BusinessException {

    // ��ȡ���������ϼ�¼�ġ��ɹ����깤�����Զ��������״̬��
    ReportTranstypeVO reportTranstypeVO = this.getreportTranstyVO(reportvos[0]);
    List<StateAdjustForQcParamVO> list =
        new ArrayList<StateAdjustForQcParamVO>();
    // this.dic = this.getCvmivenderid(reportvos);

    for (ReportVO reportvo : reportvos) {
      // �ʼ챨�����ʱ�Զ��������״̬
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
        // ���Զ��������״̬ʧ��ʱ �ʼ챨������ͨ������ѡ
        if (!reportTranstypeVO.getBalwayspass().booleanValue()) {
          ExceptionUtils.wrappException(e);
        }
      }
      catch (Exception e) {
        // ���Զ��������״̬ʧ��ʱ �ʼ챨������ͨ������ѡ
        if (!reportTranstypeVO.getBalwayspass().booleanValue()) {
          ExceptionUtils.wrappException(e);
        }
      }
    }
  }

  /**
   * ����Ĳ���
   * 
   * @param vos
   * @throws BusinessException
   */
  public void deleteStockState(ReportVO[] vos) throws BusinessException {
    // ��ȡ���������ϼ�¼�ġ��ɹ����깤�����Զ��������״̬��
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
      // ���Զ��������״̬ʧ��ʱ �ʼ챨������ͨ������ѡ
      if (!reportTranstypeVO.getBalwayspass().booleanValue()) {
        ExceptionUtils.wrappException(e);
      }
    }
    catch (Exception e) {
      // ���Զ��������״̬ʧ��ʱ �ʼ챨������ͨ������ѡ
      if (!reportTranstypeVO.getBalwayspass().booleanValue()) {
        ExceptionUtils.wrappException(e);
      }
    }
  }

  /**
   * ���ü�ǰ����״̬ <br>
   * �ɹ����깤�����Զ��������״̬����ѡҲ��¼
   * 
   * @throws BusinessException
   */
  public void dosetbeforestockstate(ReportVO reportvo) throws BusinessException {

    if (UFBoolean.FALSE.equals(this.autoSetStockState(reportvo))) {
      return;
    }
    // String pk_preStockState = this.getpreStockState();
    // // ���ñ�ͷĬ�Ͽ��״̬
    // reportvo.getHVO().setPk_prestockstate(pk_preStockState);
    if (reportvo.getHVO().getStatus() != VOStatus.NEW) {
      reportvo.getHVO().setStatus(VOStatus.UPDATED);
    }
    // ��̨��������״̬
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
//        /** �ʼ챨����Ҫ�Զ����¿��״̬ʱ�����״̬����Ϊ�ա� */

      }
    }

    // ����������κű���ͱ�ͷ�������κ���ͬ,�ҿ��״̬����Ϊ��
    this.checkbanch(reportvo);
  }

  /**
   * �ʼ챨�����ʱ�Ƿ��Զ��������״̬
   * <p>
   * <b>����˵��</b>
   * 
   * @param reportvo
   * @return
   * @throws BusinessException
   *           <p>
   * @since 6.3
   * @author heichl
   * @time 2013-5-29 ����03:17:38
   */
  private UFBoolean autoAjustStockState(ReportVO reportvo)
      throws BusinessException {

    // 1.������״̬���Զ���ֵ
    if (UFBoolean.FALSE.equals(this.autoSetStockState(reportvo))) {
      return UFBoolean.FALSE;
    }

    // ��ȡ���������ϼ�¼�ġ��ɹ����깤�����Զ��������״̬��
    ReportTranstypeVO reportTranstypeVO = this.getreportTranstyVO(reportvo);

    // 2.���ɹ����깤�����Զ��������״̬��δ��ѡ
    if (UFBoolean.FALSE.equals(reportTranstypeVO.getBautoupdateic())
    // || UFBoolean.FALSE.equals(this.checkStock(reportvo))
    ) {
      return UFBoolean.FALSE;
    }

    return UFBoolean.TRUE;
  }

  /**
   * ����������κ� �ͱ�ͷ���κ��Ƿ�һ�£���һ���״�
   * 
   * @throws BusinessException
   */
  private void checkbanch(ReportVO reportvo) throws BusinessException {
    if (this.autoAjustStockState(reportvo).booleanValue()) {
      // ��ȡ��ͷ�������κ�
      String pk_banch =
          reportvo.getHVO().getVbatchcode_h() == null ? "" : reportvo.getHVO()
              .getVbatchcode_h();
      for (ReportItemVO body : reportvo.getBVO()) {
        // 1. �Զ���״̬��ѡ
        // 2.
        if (StringUtils.isNotBlank(pk_banch)) {
          String bodybabch =
              body.getVbatchcode() == null ? "" : body.getVbatchcode();
          if (!bodybabch.equals(pk_banch)) {
            ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
                .getNCLangRes().getStrByID("c0101015", "1C01010150003"));
            /** xx���������������Զ��������״̬������ʱ��ͷ�������κ������������κű���һ�¡� */
          }
        }
      }
    }
  }

  // /**
  // * У�鵥ͷĬ�Ͽ��״̬�Ƿ���ֵ
  // *
  // * @throws BusinessException
  // */
  // private void checkHeadPreStockState(ReportVO reportvo)
  // throws BusinessException {
  // if (org.apache.commons.lang.StringUtils.isBlank(reportvo.getHVO()
  // .getPk_prestockstate())) {
  // throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
  // .getStrByID("c010003_0", "ȡ����˳ɹ���")/* @res "ȡ����˳ɹ�" */);
  // }
  // }

  // /**
  // * ��������Ƿ������״̬����
  // *
  // * @param pk_material
  // * @return
  // * @throws BusinessException
  // */
  // private UFBoolean checkisMaterialUnderstockStateControl(String pk_material)
  // throws BusinessException {
  // // Ĭ��Ϊ�������״̬����
  // UFBoolean result = UFBoolean.TRUE;
  // String flag = this.queryMaterial(pk_material);
  // if (flag == null || !flag.equals("Y")) {
  // result = UFBoolean.FALSE;
  // }
  // return result;
  // }

  // /**
  // * ����Ƿ��вֿ�
  // *
  // * @return
  // */
  // private UFBoolean checkStock(ReportVO reportvo) {
  // // Ĭ�J���ڂ}��
  // UFBoolean result = UFBoolean.TRUE;
  // if (reportvo.getBVO()[0].getCfirsttypecode().equals(
  // POBillType.Arrive.getCode())) {
  // // ��Դ�ǲɹ��ģ���ô���Զ�����������ֻ����ͷ�Ĳֿ����
  // // û�оͲ�������
  // if (StringUtils.isBlank(reportvo.getHVO().getPk_stordoc())) {
  // result = UFBoolean.FALSE;
  // }
  // }
  // else if (reportvo.getBVO()[0].getCfirsttypecode().equals(
  // MMBillType.ProduceReport.getCode())) {
  // // ��Դ�������Ĳ�һ��
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
   * ���������Ĵ���DTO;������������û�оͲ��õ���
   * 
   * @param reportvo
   * @return
   */
  private List<StateAdjustForQcParamVO> createInterfaceVO(ReportVO reportvo) {

    List<StateAdjustForQcParamVO> list =
        new ArrayList<StateAdjustForQcParamVO>();
    Set<String> dimSet = new HashSet<String>();

    for (ReportItemVO body : reportvo.getBVO()) {
      // ��������Ǹ��еģ��������κ�Ϊ��(���к�Ϊ��) ���򲻹ܵ���;���кŲ�Ϊ��ҲҪ����
      if (body.getBchanged().booleanValue()
          && !StringUtils.isBlank(body.getPk_chgmrl())
          || StringUtils.isBlank(body.getPk_batchcode())
          && StringUtils.isBlank(reportvo.getHVO().getPk_serialno())) {
        continue;
      }
      StateAdjustForQcParamVO result = new StateAdjustForQcParamVO();
      // ���ڹ���ά�ȣ�ͬһ��ά�ȵ�ֻ��һ��
      StringBuffer key = new StringBuffer();

      // ����������
      // �����������͡��������ݺš����������������������ݱ�������
      result.setCadjustbilltype(QCBillType.ReportBill.getCode());// ������������
      result.setCadjustbillcode(reportvo.getHVO().getVbillcode());// �������ݺ�
      result.setCadjustbillid(reportvo.getHVO().getPk_reportbill());// ������������
      result.setCadjustbillbid(body.getPk_reportbill_b());// �������ݱ�������
      result.setVtrantypecode(reportvo.getHVO().getVtrantypecode());// �������ݽ�������
      result.setCtrantypeid(reportvo.getHVO().getCtrantypeid());// �������ݽ�������
      result.setCadjustrowno(body.getCrowno());// ���������к�
      if (StringUtils.isNotBlank(reportvo.getHVO().getVsncode())) {
        result.setVsncode(reportvo.getHVO().getVsncode());
        key.append(reportvo.getHVO().getVsncode());
      }

      // �ִ���ά��
      // ����+���ϰ汾+�����֯+�ֿ�+����+������λ+������+�̶���������+���ɸ�������+��ǰ���״̬
      // +�Ĵ湩Ӧ��+�����ͻ�

      // �ִ���ά�� modify by fengjqc 20150514
      // ����+���ϰ汾+�����֯+����+�̶��������ԣ���Ӧ�̡���Ŀ���������̡��ͻ���+���ɸ������ԣ�1-10��

      // OnhandDimVO onhandDimVO = new OnhandDimVO();
      // result.setStateAdjustDimVo(onhandDimVO);

      List<String> onhandDimFields = new ArrayList<String>();
      List<String> onhandDimValues = new ArrayList<String>();

      // �ִ���ά�ȸ�ֵ
      {
        onhandDimFields.add(StateAdjustForQcParamVO.CMATERIALOID);
        onhandDimValues.add(reportvo.getHVO().getPk_srcmaterial());// ����
        key.append(reportvo.getHVO().getPk_srcmaterial());

        onhandDimFields.add(StateAdjustForQcParamVO.CMATERIALVID);
        onhandDimValues.add(reportvo.getHVO().getPk_material());// ���϶�汾
        key.append(reportvo.getHVO().getPk_material());

        onhandDimFields.add(StateAdjustForQcParamVO.PK_ORG);
        onhandDimValues.add(reportvo.getHVO().getPk_stockorg());// �����֯
        key.append(reportvo.getHVO().getPk_stockorg());

        if (StringUtils.isBlank(reportvo.getHVO().getVsncode())) {

          onhandDimFields.add(StateAdjustForQcParamVO.PK_BATCHCODE);
          onhandDimValues.add(body.getPk_batchcode());// �������κ�����
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
          onhandDimValues.add(reportvo.getHVO().getPk_supplier());// ��Ӧ��
          key.append(reportvo.getHVO().getPk_supplier());

          onhandDimFields.add(StateAdjustForQcParamVO.CASSCUSTID);
          onhandDimValues.add(reportvo.getHVO().getPk_customer());// �ͻ�
          key.append(reportvo.getHVO().getPk_customer());

          onhandDimFields.add(StateAdjustForQcParamVO.CPROJECTID);
          onhandDimValues.add(reportvo.getHVO().getCprojectid());// ��Ŀ
          key.append(reportvo.getHVO().getCprojectid());

          // �ʼ챨��û����������
        }
      }

      // ��������
      result.setNadjustnum(body.getNnum());// ����������
      result.setNadjustassistnum(body.getNastnum());// ��������
      result.setCadjuststateid(body.getPk_afterstockstate());// �������״̬

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
  // * ��ѯVMIID
  // *
  // * @param vos
  // * @return
  // * @throws BusinessException
  // */
  // private Map<String, Boolean> getCvmivenderid(ReportVO[] vos)
  // throws BusinessException {
  // // �ʼ챨��ID--��Դ������Code
  // Map<String, String> reportIDtoArriveCode = new HashMap<String, String>();
  // for (ReportVO reportVO : vos) {
  // String cfirsttypecode = reportVO.getBVO()[0].getCfirsttypecode();
  // // ��Դ�ǵ�����
  // if (POBillType.Arrive.getCode().equals(cfirsttypecode)) {
  // reportIDtoArriveCode.put(reportVO.getHVO().getPk_applybill(),
  // reportVO.getBVO()[0].getVfirstcode());
  // }
  // }
  // if (reportIDtoArriveCode.keySet().size() <= 0) {
  // return null;
  // }
  // // �Ѿ�������ԴΪ21 ��ɸѡ �ʼ챨��ID--������VO
  // Map<String, ArriveVO> arriveVOs = this.queryArriveVO(reportIDtoArriveCode);
  // String[] keys = new String[arriveVOs.keySet().size()];
  // int i = 0;
  // for (String key : arriveVOs.keySet()) {
  // keys[i] = arriveVOs.get(key).getBVO()[0].getVsourcetrantype();
  // }
  // // key��������ID -- value ��Դ���ݵĽ�������VO
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
   * ��������ʱ�Ľӿڲ���
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
      // ��������Ǹ��е� ���򲻹� ����
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
  // * ��ȡ���汾������
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
  // * ��ȡ�����ɹ�������Ӧ��
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
   * ��ȡĬ�Ͽ�״̬
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
  // * �����ʼ챨�������PK��ѯ�ʼ챨�����VO
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
   * ��ȡ����������չ��Ϣ
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
        // δ���ҵ���صĽ���������չ��Ϣ
        throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("c0101015", "1C01010150002")/* δ���ҵ���صĽ���������չ��Ϣ */);
      }
    }
    return this.reporttranstypeVO;
  }

  // /**
  // * ����
  // *
  // * @return
  // */
  // private UFBoolean isCanCreateBill(ReportVO reportvo) {
  // ReportItemVO body = reportvo.getBVO()[0];
  // if (body.getCfirsttypecode().equals(POBillType.Arrive.getCode())) {
  // // ��Դ�ǵ�����
  // return this.isCanCreateBillFromArrive(reportvo);
  // }
  // else if (body.getCfirsttypecode()
  // .equals(MMBillType.ProduceReport.getCode())) {
  // // ��Դ������
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
  // * ��ȡ��������aggvo
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
  // // ��ѯ�ӿ�
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
   * ��ѯ������Ϣ
   * 
   * @param pk_material
   * @return
   * @throws BusinessException
   */
  private String queryMaterial(String pk_material) throws BusinessException {
    // �������]��
    if (!this.mapMaterialFlag.keySet().contains(pk_material)) {
      // ȥ��̨��ѯ ������ס
      this.querymaterialInfo(new String[] {
        pk_material
      });
    }
    // ʼ�մӻ����õ����ݣ� ������������ͬ���ʵ��
    if (this.mapMaterialFlag.keySet().contains(pk_material)) {
      return this.mapMaterialFlag.get(pk_material);
    }
    return "";
  }

  /**
   * ����̨��ѯ���ϡ����״̬��������������
   * 
   * @param pk_materials
   *          ����pk ����
   * @throws BusinessException
   */
  private void querymaterialInfo(String[] pk_materials)
      throws BusinessException {
    // ��ѯ�ӿ�
    IReportPubQuery billQuery =
        NCLocator.getInstance().lookup(IReportPubQuery.class);

    // ��ѯ���Ľ��
    Map<String, String> materialmap = billQuery.querySubMaterial(pk_materials);
    // ����ס�����
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
