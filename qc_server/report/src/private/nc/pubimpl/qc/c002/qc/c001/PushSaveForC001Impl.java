/**
 * $文件说明$
 * 
 * @author tianft
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-5-26 下午01:44:47
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
 * <b>报检单推检验单动作的实现类</b>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author tianft
 * @time 2010-5-26 下午01:44:47
 */
public class PushSaveForC001Impl implements IPushSaveForC001 {

  private ICheckTeamPubQuery checkTeamSrv = null;

  /**
   * 父类方法重写
   * 
   * @see nc.pubitf.qc.c002.qc.c001.IPushSaveForC001#pushSave(nc.vo.qc.c002.entity.CheckBillVO[])
   */
  @Override
  public void pushSave(ApplyVO[] applyBills) throws BusinessException {
    try {
      // 分单规则：
      // 1)按样本号分单。分至样本号后，如果同一个样本中的检验项目分属不同的检测室，再按检测室分单。
      // 2)检验项目取报检单表体的检测中心内，根据检测项目所属的检测室，按检测室分单。
      // 3)按外部检测机构分单。
      if (ArrayUtils.isEmpty(applyBills)) {
        return;
      }
      // 1.按样本分单
      ApplyVO[] vos = this.createApplyVOSplitedBySample(applyBills);

      // 按检测中心分单
      SplitBill<ApplyVO> splitBill = new SplitBill<ApplyVO>();
      splitBill.appendKey(ApplyItemVO.PK_CHKCENTER_V);
      vos = splitBill.split(vos);

      // 2.查询并设置检测室
      vos = this.setCheckTeam(vos);

      // 3.按检测室分单
      splitBill = new SplitBill<ApplyVO>();
      splitBill.appendKey(ApplyItemVO.PK_CHKGROUP);
      vos = splitBill.split(vos);
      // 4.按外部检测机构分单
      splitBill = new SplitBill<ApplyVO>();
      splitBill.appendKey(ApplyItemVO.PK_OUTSUPPLIER);
      vos = splitBill.split(vos);

      CheckBillVO[] checkBills =
          PfServiceScmUtil.executeVOChange(QCBillType.ApplyBill.getCode(),
              QCBillType.CheckBill.getCode(), vos);

      // 如果上游单据检测中心为空，则取上游单据表头的质检中心为住组织
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
      // 重新查询报告表体数据
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
    // 1.按样本分单
    ApplyVO[] vos =
        this.createApplyVOSplitedBySample(applyvomap.values().toArray(
            new ApplyVO[0]));
    CheckBillVO[] checkBills =
        PfServiceScmUtil.executeVOChange(QCBillType.ApplyBill.getCode(),
            QCBillType.CheckBill.getCode(), vos);
    return this.combine(checkBills, reportvo);
  }

  /**
   * 构造查询检测室用的参数vo
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
    // 检测室设置为null
    chkbillheadvo.setPk_chkgroup(null);
    chkbillheadvo.setPk_group(reportvo.getHVO().getPk_group());
    // 检测中心设置为质检报告的质检中心
    chkbillheadvo.setPk_org(reportvo.getHVO().getPk_org());
    chkbillheadvo.setPk_org_v(reportvo.getHVO().getPk_org_v());
    // 检验数量
    chkbillheadvo.setNastnum(reportvo.getHVO().getNapplyastnum());
    // 检验主数量
    chkbillheadvo.setNnum(reportvo.getHVO().getNapplynum());
    // 来源信息
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
        // 样本号
        chkitemvo.setVbsamplecode(billvo.getParentVO().getVsamplecode());
        // 样本量
        // chkitemvo.setNtotalsample(billvo.getParentVO().getNsamplenum());
        // 检测中心设置为质检报告的质检中心
        chkitemvo.setPk_org(reportvo.getHVO().getPk_org());
        chkitemvo.setPk_org_v(reportvo.getHVO().getPk_org_v());
        // 检验时间
        chkitemvo.setTchecktime(new UFDateTime());
        itemvolist.add(chkitemvo);
      }
    }
    CheckBillVO chkbillvo = checkBills[0];
    chkbillvo.setParentVO(chkbillheadvo);
    chkbillvo.setChildrenVO(itemvolist.toArray(new CheckBillItemVO[0]));

    // 重排行号
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
   * 按样本分单后的报检单vo
   * 
   * @param applyBills
   * @return
   */
  private ApplyVO[] createApplyVOSplitedBySample(ApplyVO[] applyBills) {
    // 1.构造分单参数
    SamplingNumerateParam[] paras = this.createSplitPara(applyBills);
    SampleNumerateTool splitTool = new SampleNumerateTool();
    // 2.获取分单后的结果，需重新拼装报检单vo。Map信息： 报检单id-样本信息
    Map<String, SamplingItemVO[]> sampleResultMap =
        splitTool.getSamplingNum(paras);
    // 根据样本分单的结果构造map：报检单id+样本index+检验项目->检验项目
    Map<String, SamplingCheckItemVO> sampChkItemMap =
        this.createSampleItemMap(sampleResultMap);
    Map<String, ApplyVO> applyVOMap = QCVOUtil.createVOMap(applyBills);
    List<ApplyVO> applyVOs = new ArrayList<ApplyVO>();
    // 根据分单结果拼装报检单vo，拼装后的vo就是分单好的vo
    for (Entry<String, SamplingItemVO[]> entry : sampleResultMap.entrySet()) {
      // 循环样本vo
      for (int i = 0; i < entry.getValue().length; i++) {
        ApplyVO newVO = (ApplyVO) applyVOMap.get(entry.getKey()).clone();
        newVO.getHVO().setVsamplingcode(entry.getValue()[i].getVsamplingcode());// 样本号
        newVO.getHVO().setNsampnum(entry.getValue()[i].getNsampastnum());// 样本量
        ApplyItemVO[] items = newVO.getB1VO();
        List<ApplyItemVO> applyItemVOs = new ArrayList<ApplyItemVO>();
        for (ApplyItemVO item : items) {
          String key = entry.getKey() + i + item.getPk_checkitem();
          if (sampChkItemMap.containsKey(key)) {
            SamplingCheckItemVO sampItemVO = sampChkItemMap.get(key);
            // 设置样本量
            item.setNtotalsample(sampItemVO.getNsampastnumbb());
            // 设置接收数
            item.setNacceptnum(sampItemVO.getNacnum());
            // 设置拒收数
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
   * 根据样本分单的结果构造map：报检单id+样本index+检验项目->检验项目
   * 
   * @param mapResult
   *          样本分单的结果
   * @return map：报检单id+样本index+检验项目->检验项目
   */
  private Map<String, SamplingCheckItemVO> createSampleItemMap(
      Map<String, SamplingItemVO[]> mapResult) {
    Map<String, SamplingCheckItemVO> itemMap =
        new HashMap<String, SamplingCheckItemVO>();
    for (Entry<String, SamplingItemVO[]> entry : mapResult.entrySet()) {
      for (int i = 0; i < entry.getValue().length; i++) {
        for (SamplingCheckItemVO chkItem : entry.getValue()[i]
            .getSamplingCheckItemVOs()) {
          // 报检单id + 样本下标 + 检验项目
          itemMap.put(entry.getKey() + i + chkItem.getPk_checkitem(), chkItem);
        }
      }
    }
    return itemMap;
  }

  /**
   * 按样本分单参数，给工具类用
   * 
   * @param applyBills
   *          报检单
   * @return 样本分单参数
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
      // 单件质检的物料有序列号时，取样方式要处理为全检
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
   * 获取检测室的服务
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
   * 根据组织+检验项目匹配检测室
   * 
   * @param vos
   * @param queryVOs
   */
  private ApplyVO[] matchCheckTeam(ApplyVO[] vos, CheckTeamParaVO[] queryVOs) {
    Map<String, CheckTeamParaVO> paraMap =
        new HashMap<String, CheckTeamParaVO>();
    for (CheckTeamParaVO vo : queryVOs) {
      // 组织+检验项目
      String key = vo.getPk_org() + vo.getPk_checkItem();
      paraMap.put(key, vo);
    }
    // 根据组织+检验项目匹配检测室
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
          // 设置检测室
          item.setPk_chkgroup(paraVO.getPk_checkteam());
        }
      }
    }
    return vos;
  }

  /**
   * 查询并设置检测室
   * 
   * @param vos
   * @return
   */
  private ApplyVO[] setCheckTeam(ApplyVO[] vos) {
    CheckTeamParaVO[] paras = this.buildCheckTeamParaVO(vos);
    try {
      // 无结果会返回空数组，所以匹配检测室的时候不必判断
      CheckTeamParaVO[] queryVOs = this.getCheckTeamSrv().queryCheckTeam(paras);
      return this.matchCheckTeam(vos, queryVOs);
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
    return vos;
  }
}
