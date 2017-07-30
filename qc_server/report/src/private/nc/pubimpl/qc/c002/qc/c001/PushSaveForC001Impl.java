/**
 * $�ļ�˵��$
 * 
 * @author tianft
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-5-26 ����01:44:47
 */
package nc.pubimpl.qc.c002.qc.c001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.framework.common.NCLocator;
import nc.bs.qc.c002.util.CheckBillImplUtil;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.qc.c002.action.CheckBillInsertAction;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.pubitf.qc.c001.pub.IApplyPubQuery;
import nc.pubitf.qc.c002.qc.c001.IPushSaveForC001;
import nc.pubitf.qc.checkteam.ICheckTeamPubQuery;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.bill.SplitBill;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.c001.entity.ApplyItemVO;
import nc.vo.qc.c001.entity.ApplyVO;
import nc.vo.qc.c002.entity.CheckBillHeaderVO;
import nc.vo.qc.c002.entity.CheckBillItemVO;
import nc.vo.qc.c002.entity.CheckBillVO;
import nc.vo.qc.c003.entity.ReportItemVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.c007.entity.SamplingCheckItemVO;
import nc.vo.qc.c007.entity.SamplingItemVO;
import nc.vo.qc.c007.util.samplenumerate.SampleNumerateTool;
import nc.vo.qc.c007.util.samplenumerate.SamplingNumerateParam;
import nc.vo.qc.checkteam.entity.CheckTeamParaVO;
import nc.vo.qc.pub.util.QCVOUtil;
import nc.vo.scmpub.res.billtype.QCBillType;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * <b>���쵥�Ƽ��鵥������ʵ����</b>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author tianft
 * @time 2010-5-26 ����01:44:47
 */
public class PushSaveForC001Impl implements IPushSaveForC001 {

  private ICheckTeamPubQuery checkTeamSrv = null;

  /**
   * ���෽����д
   * 
   * @see nc.pubitf.qc.c002.qc.c001.IPushSaveForC001#pushSave(nc.vo.qc.c002.entity.CheckBillVO[])
   */
  @Override
  public void pushSave(ApplyVO[] applyBills) throws BusinessException {
    try {
      // �ֵ�����
      // 1)�������ŷֵ������������ź����ͬһ�������еļ�����Ŀ������ͬ�ļ���ң��ٰ�����ҷֵ���
      // 2)������Ŀȡ���쵥����ļ�������ڣ����ݼ����Ŀ�����ļ���ң�������ҷֵ���
      // 3)���ⲿ�������ֵ���
      if (ArrayUtils.isEmpty(applyBills)) {
        return;
      }
      // 1.�������ֵ�
      ApplyVO[] vos = this.createApplyVOSplitedBySample(applyBills);

      // ��������ķֵ�
      SplitBill<ApplyVO> splitBill = new SplitBill<ApplyVO>();
      splitBill.appendKey(ApplyItemVO.PK_CHKCENTER_V);
      vos = splitBill.split(vos);

      // 2.��ѯ�����ü����
      vos = this.setCheckTeam(vos);

      // 3.������ҷֵ�
      splitBill = new SplitBill<ApplyVO>();
      splitBill.appendKey(ApplyItemVO.PK_CHKGROUP);
      vos = splitBill.split(vos);
      // 4.���ⲿ�������ֵ�
      splitBill = new SplitBill<ApplyVO>();
      splitBill.appendKey(ApplyItemVO.PK_OUTSUPPLIER);
      vos = splitBill.split(vos);

      CheckBillVO[] checkBills =
          PfServiceScmUtil.executeVOChange(QCBillType.ApplyBill.getCode(),
              QCBillType.CheckBill.getCode(), vos);

      // ������ε��ݼ������Ϊ�գ���ȡ���ε��ݱ�ͷ���ʼ�����Ϊס��֯
      CheckBillImplUtil.fillPkorg(vos, checkBills);

      if (ArrayUtils.isEmpty(checkBills)) {
        return;
      }
      new CheckBillInsertAction().insertForC001(checkBills);
    }
    catch (Exception e) {
      ExceptionUtils.marsh(e);
    }
    return;
  }

  @Override
  public CheckBillVO[] pushSaveFromC003(ReportVO reportvo)
      throws BusinessException {
    if (null == reportvo.getBVO()) {
      // ���²�ѯ�����������
      String pk = reportvo.getHVO().getPk_reportbill();
      VOQuery<ReportItemVO> query =
          new VOQuery<ReportItemVO>(ReportItemVO.class);
      String condition = " and " + ReportItemVO.PK_REPORTBILL + "='" + pk + "'";
      ReportItemVO[] itemvos = query.query(condition, null);
      reportvo.setBVO(itemvos);
    }
    String pk_applybill = reportvo.getBVO()[0].getCsourceid();
    Map<String, ApplyVO> applyvomap = new HashMap<String, ApplyVO>();
    try {
      applyvomap =
          NCLocator.getInstance().lookup(IApplyPubQuery.class)
              .queryApplyVOByHid(new String[] {
                pk_applybill
              });
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
    if (applyvomap.isEmpty()) {
      return new CheckBillVO[0];
    }
    this.handerChkNnum(applyvomap, reportvo);
    // 1.�������ֵ�
    ApplyVO[] vos =
        this.createApplyVOSplitedBySample(applyvomap.values().toArray(
            new ApplyVO[0]));
    CheckBillVO[] checkBills =
        PfServiceScmUtil.executeVOChange(QCBillType.ApplyBill.getCode(),
            QCBillType.CheckBill.getCode(), vos);
    return this.combine(checkBills, reportvo);
  }

  /**
   * �����ѯ������õĲ���vo
   * 
   * @param vos
   * @return
   */
  private CheckTeamParaVO[] buildCheckTeamParaVO(ApplyVO[] vos) {
    List<CheckTeamParaVO> paras = new ArrayList<CheckTeamParaVO>();
    for (ApplyVO vo : vos) {
      for (ApplyItemVO item : vo.getB1VO()) {
        CheckTeamParaVO para = new CheckTeamParaVO();
        if (StringUtils.isNotBlank(item.getPk_chkcenter())) {
          para.setPk_org(item.getPk_chkcenter());
        }
        else {
          para.setPk_org(item.getPk_org());
        }

        para.setPk_checkItem(item.getPk_checkitem());
        paras.add(para);
      }
    }
    return paras.toArray(new CheckTeamParaVO[paras.size()]);
  }

  private CheckBillVO[] combine(CheckBillVO[] checkBills, ReportVO reportvo) {
    if (ArrayUtils.isEmpty(checkBills)) {
      return new CheckBillVO[0];
    }
    List<CheckBillItemVO> itemvolist = new ArrayList<CheckBillItemVO>();
    CheckBillHeaderVO chkbillheadvo = checkBills[0].getParentVO();
    // ���������Ϊnull
    chkbillheadvo.setPk_chkgroup(null);
    chkbillheadvo.setPk_group(reportvo.getHVO().getPk_group());
    // �����������Ϊ�ʼ챨����ʼ�����
    chkbillheadvo.setPk_org(reportvo.getHVO().getPk_org());
    chkbillheadvo.setPk_org_v(reportvo.getHVO().getPk_org_v());
    // ��������
    chkbillheadvo.setNastnum(reportvo.getHVO().getNapplyastnum());
    // ����������
    chkbillheadvo.setNnum(reportvo.getHVO().getNapplynum());
    // ��Դ��Ϣ
    chkbillheadvo.setCsourceid(reportvo.getHVO().getPk_reportbill());
    chkbillheadvo.setCsourcetypecode(QCBillType.ReportBill.getCode());
    chkbillheadvo.setPk_chkbatch(reportvo.getHVO().getPk_chkbatch());
    for (CheckBillVO billvo : checkBills) {
      CheckBillItemVO[] itemvos = billvo.getChildrenVO();
      if (ArrayUtils.isEmpty(itemvos)) {
        continue;
      }
      for (CheckBillItemVO chkitemvo : itemvos) {
    	  chkitemvo.setPk_group(chkbillheadvo.getPk_group());
        // ������
        chkitemvo.setVbsamplecode(billvo.getParentVO().getVsamplecode());
        // ������
        // chkitemvo.setNtotalsample(billvo.getParentVO().getNsamplenum());
        // �����������Ϊ�ʼ챨����ʼ�����
        chkitemvo.setPk_org(reportvo.getHVO().getPk_org());
        chkitemvo.setPk_org_v(reportvo.getHVO().getPk_org_v());
        // ����ʱ��
        chkitemvo.setTchecktime(new UFDateTime());
        itemvolist.add(chkitemvo);
      }
    }
    CheckBillVO chkbillvo = checkBills[0];
    chkbillvo.setParentVO(chkbillheadvo);
    chkbillvo.setChildrenVO(itemvolist.toArray(new CheckBillItemVO[0]));

    // �����к�
    int i = 0;
    for (CheckBillItemVO bvo : chkbillvo.getChildrenVO()) {
      bvo.setCrowno("" + (i * 10 + 10));
      i++;
    }

    return new CheckBillInsertAction().insertForC001(new CheckBillVO[] {
      chkbillvo
    });
  }

  /**
   * �������ֵ���ı��쵥vo
   * 
   * @param applyBills
   * @return
   */
  private ApplyVO[] createApplyVOSplitedBySample(ApplyVO[] applyBills) {
    // 1.����ֵ�����
    SamplingNumerateParam[] paras = this.createSplitPara(applyBills);
    SampleNumerateTool splitTool = new SampleNumerateTool();
    // 2.��ȡ�ֵ���Ľ����������ƴװ���쵥vo��Map��Ϣ�� ���쵥id-������Ϣ
    Map<String, SamplingItemVO[]> sampleResultMap =
        splitTool.getSamplingNum(paras);
    // ���������ֵ��Ľ������map�����쵥id+����index+������Ŀ->������Ŀ
    Map<String, SamplingCheckItemVO> sampChkItemMap =
        this.createSampleItemMap(sampleResultMap);
    Map<String, ApplyVO> applyVOMap = QCVOUtil.createVOMap(applyBills);
    List<ApplyVO> applyVOs = new ArrayList<ApplyVO>();
    // ���ݷֵ����ƴװ���쵥vo��ƴװ���vo���Ƿֵ��õ�vo
    for (Entry<String, SamplingItemVO[]> entry : sampleResultMap.entrySet()) {
      // ѭ������vo
      for (int i = 0; i < entry.getValue().length; i++) {
        ApplyVO newVO = (ApplyVO) applyVOMap.get(entry.getKey()).clone();
        newVO.getHVO().setVsamplingcode(entry.getValue()[i].getVsamplingcode());// ������
        newVO.getHVO().setNsampnum(entry.getValue()[i].getNsampastnum());// ������
        ApplyItemVO[] items = newVO.getB1VO();
        List<ApplyItemVO> applyItemVOs = new ArrayList<ApplyItemVO>();
        for (ApplyItemVO item : items) {
          String key = entry.getKey() + i + item.getPk_checkitem();
          if (sampChkItemMap.containsKey(key)) {
            SamplingCheckItemVO sampItemVO = sampChkItemMap.get(key);
            // ����������
            item.setNtotalsample(sampItemVO.getNsampastnumbb());
            // ���ý�����
            item.setNacceptnum(sampItemVO.getNacnum());
            // ���þ�����
            item.setNrejectnum(sampItemVO.getNrenum());
            applyItemVOs.add(item);
          }
        }
        newVO.setChildrenVO(applyItemVOs.toArray(new ApplyItemVO[applyItemVOs
            .size()]));
        applyVOs.add(newVO);
      }
    }
    return applyVOs.toArray(new ApplyVO[applyVOs.size()]);
  }

  /**
   * ���������ֵ��Ľ������map�����쵥id+����index+������Ŀ->������Ŀ
   * 
   * @param mapResult
   *          �����ֵ��Ľ��
   * @return map�����쵥id+����index+������Ŀ->������Ŀ
   */
  private Map<String, SamplingCheckItemVO> createSampleItemMap(
      Map<String, SamplingItemVO[]> mapResult) {
    Map<String, SamplingCheckItemVO> itemMap =
        new HashMap<String, SamplingCheckItemVO>();
    for (Entry<String, SamplingItemVO[]> entry : mapResult.entrySet()) {
      for (int i = 0; i < entry.getValue().length; i++) {
        for (SamplingCheckItemVO chkItem : entry.getValue()[i]
            .getSamplingCheckItemVOs()) {
          // ���쵥id + �����±� + ������Ŀ
          itemMap.put(entry.getKey() + i + chkItem.getPk_checkitem(), chkItem);
        }
      }
    }
    return itemMap;
  }

  /**
   * �������ֵ�����������������
   * 
   * @param applyBills
   *          ���쵥
   * @return �����ֵ�����
   */
  private SamplingNumerateParam[] createSplitPara(ApplyVO[] applyBills) {
    SamplingNumerateParam[] paras =
        new SamplingNumerateParam[applyBills.length];
    for (int i = 0; i < applyBills.length; i++) {
      paras[i] = new SamplingNumerateParam();
      paras[i].setPk_apply(applyBills[i].getHVO().getPk_applybill());
      paras[i].setFstrictlevel(applyBills[i].getHVO().getFstrictlevel());
      paras[i].setNnum(applyBills[i].getHVO().getNchknum());
      paras[i].setVos(applyBills[i].getB1VO());
      paras[i].setCunitid(applyBills[i].getHVO().getCunitid());
      // �����ʼ�����������к�ʱ��ȡ����ʽҪ����Ϊȫ��
      if (StringUtils.isBlank(applyBills[i].getHVO().getPk_serialno())) {
        paras[i].setbSingleCheck(UFBoolean.FALSE);
      }
      else {
        paras[i].setbSingleCheck(UFBoolean.TRUE);
      }
    }

    return paras;
  }

  /**
   * ��ȡ����ҵķ���
   * 
   * @return
   */
  private ICheckTeamPubQuery getCheckTeamSrv() {
    if (this.checkTeamSrv == null) {
      this.checkTeamSrv =
          NCLocator.getInstance().lookup(ICheckTeamPubQuery.class);
    }
    return this.checkTeamSrv;
  }

  private void handerChkNnum(Map<String, ApplyVO> applyvomap, ReportVO reportvo) {
    ApplyVO[] vos = applyvomap.values().toArray(new ApplyVO[0]);
    for (ApplyVO vo : vos) {
      vo.getHVO().setNchknum(reportvo.getHVO().getNchecknum());
      applyvomap.put(vo.getHVO().getPk_applybill(), vo);
    }
  }

  /**
   * ������֯+������Ŀƥ������
   * 
   * @param vos
   * @param queryVOs
   */
  private ApplyVO[] matchCheckTeam(ApplyVO[] vos, CheckTeamParaVO[] queryVOs) {
    Map<String, CheckTeamParaVO> paraMap =
        new HashMap<String, CheckTeamParaVO>();
    for (CheckTeamParaVO vo : queryVOs) {
      // ��֯+������Ŀ
      String key = vo.getPk_org() + vo.getPk_checkItem();
      paraMap.put(key, vo);
    }
    // ������֯+������Ŀƥ������
    for (ApplyVO vo : vos) {
      for (ApplyItemVO item : vo.getB1VO()) {
        String key = null;
        if (StringUtils.isNotBlank(item.getPk_chkcenter())) {
          key = item.getPk_chkcenter() + item.getPk_checkitem();
        }
        else {
          key = item.getPk_org() + item.getPk_checkitem();
        }

        CheckTeamParaVO paraVO = paraMap.get(key);
        if (paraVO != null) {
          // ���ü����
          item.setPk_chkgroup(paraVO.getPk_checkteam());
        }
      }
    }
    return vos;
  }

  /**
   * ��ѯ�����ü����
   * 
   * @param vos
   * @return
   */
  private ApplyVO[] setCheckTeam(ApplyVO[] vos) {
    CheckTeamParaVO[] paras = this.buildCheckTeamParaVO(vos);
    try {
      // �޽���᷵�ؿ����飬����ƥ�����ҵ�ʱ�򲻱��ж�
      CheckTeamParaVO[] queryVOs = this.getCheckTeamSrv().queryCheckTeam(paras);
      return this.matchCheckTeam(vos, queryVOs);
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
    return vos;
  }
}
