package nc.bs.qc.c003.maintain;

import nc.bs.qc.c003.maintain.rule.ChkBillCodeUniqueRule;
import nc.bs.qc.c003.maintain.rule.InsertAndDelBatchCodeBeforeRule;
import nc.bs.qc.c003.maintain.rule.ReportChkChkBatUnique;
import nc.bs.qc.c003.maintain.rule.insert.CreatChkBatchRule;
import nc.bs.qc.c003.maintain.rule.insert.FillUpDataRule;
import nc.bs.qc.c003.maintain.rule.insert.InsertBatchCodeAfterRule;
import nc.bs.qc.c003.maintain.rule.insert.InsertBillCodeRule;
import nc.bs.qc.c003.maintain.rule.insert.MatchReportTypeRule;
import nc.bs.qc.c003.maintain.rule.insert.RowNoFillUpRule;
import nc.bs.qc.c003.maintain.rule.insert.WriteC001WhenInsertRule;
import nc.bs.qc.c003.maintain.rule.insert.WriteC002WhenInsertRule;
import nc.bs.qc.c003.plugin.ReportBPPlugInPoint;
import nc.bs.qc.pub.rule.QCRowNoCheckRule;
import nc.impl.pubapp.bd.material.assistant.MarAssistantSaveRule;
import nc.impl.pubapp.bd.userdef.UserDefSaveRule;
import nc.impl.pubapp.pattern.data.bill.BillInsert;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.pubapp.util.SetAddAuditInfoRule;
import nc.vo.qc.c003.entity.ReportHeaderVO;
import nc.vo.qc.c003.entity.ReportItemVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.c003.rule.FillupFacceptjudgeRule;
import nc.vo.qc.c003.rule.RejectAfterCheckRule;
import nc.vo.qc.c003.rule.RejectBeforCheckRule;
import nc.vo.qc.c003.rule.ReportCheckFlowRule;
import nc.vo.qc.c003.rule.ReportItemsCheckRule;
import nc.vo.qc.c003.rule.ReportNumCheckRule;
import nc.vo.qc.c003.util.ReportAddStockStateRule;
import nc.vo.qc.pub.rule.CheckBillNumRule;
import nc.vo.scmpub.rule.QcCenterEnableCheckRule;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>新增保存报检单BP类
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author hanbin
 * @time 2010-1-13 上午10:17:34
 */
public class ReportInsertBP {
	--记得合并产品补丁
  public ReportVO[] insertReport(ReportVO[] aggVO) {
    AroundProcesser<ReportVO> processer =
        new AroundProcesser<ReportVO>(ReportBPPlugInPoint.ReportInsertBP);

    // 添加BP规则
    this.addBeforeRule(processer);
    this.addAfterFinalRule(processer);

    processer.before(aggVO);

    BillInsert<ReportVO> bo = new BillInsert<ReportVO>();
    ReportVO[] vos = bo.insert(aggVO);

    processer.after(aggVO);

    return vos;
  }

  private void addAfterFinalRule(AroundProcesser<ReportVO> processer) {
    // 单据号唯一性校验
    processer.addAfterFinalRule(new ChkBillCodeUniqueRule());
    // 回写报检单
    processer.addAfterFinalRule(new WriteC001WhenInsertRule());
    // 回写检验单
    processer.addAfterFinalRule(new WriteC002WhenInsertRule());
    // 新增保存后批次号处理
    processer.addAfterFinalRule(new InsertBatchCodeAfterRule());
    //2017-06-25
//    1. 保存判断表体合格品没有勾选，则认为是不合格品，需要自动将表头的需要不合格品处理 勾选
//    2. 保存判断表体合格品没有勾选，则认为是不合格，自动生成不合格品处理单
//    3. 需要考虑 推式保存， .后续 计量 报检--自动质检报告--自动不合格品处理单
//    processer.addAfterRule(new RejectAfterCheckRule());
  }

  @SuppressWarnings({
    "unchecked", "rawtypes"
  })
  private void addBeforeRule(AroundProcesser<ReportVO> processer) {
    // 填补数据
    processer.addBeforeRule(new FillUpDataRule());
    // 审计信息
    processer.addBeforeRule(new SetAddAuditInfoRule<ReportVO>());
    // 检查质量模块中的数量的规则
    String[] hKeys = new String[2];
    hKeys[0] = ReportHeaderVO.NCHECKNUM;
    hKeys[1] = ReportHeaderVO.NCHECKASTNUM;
    String[] bKeys = new String[2];
    bKeys[0] = ReportItemVO.NNUM;
    bKeys[1] = ReportItemVO.NASTNUM;
    processer.addBeforeRule(new CheckBillNumRule<ReportVO>(hKeys, bKeys));
    // 单据号处理
    processer.addBeforeRule(new InsertBillCodeRule());
    // 质检报告保存前创建检验批次
    processer.addBeforeRule(new CreatChkBatchRule());
    // 校验检验批次唯一性
    processer.addBeforeRule(new ReportChkChkBatUnique());
    // 填补接收判定
    processer.addBeforeRule(new FillupFacceptjudgeRule());
    // 质检报告的行号处理规则类（报检单推式生成质检报告时，需要填补行号）
    processer.addBeforeRule(new RowNoFillUpRule());
    // 行号重复校验
    processer.addBeforeRule(new QCRowNoCheckRule(ReportItemVO.class,
        ReportItemVO.CROWNO));
    // 匹配报告类型
    processer.addBeforeRule(new MatchReportTypeRule());
    // 非空校验
    processer.addBeforeRule(new ReportItemsCheckRule());
    // 数量校验
    processer.addBeforeRule(new ReportNumCheckRule());
    // 新增保存前批次号处理
    processer.addBeforeRule(new InsertAndDelBatchCodeBeforeRule(true));
    // 物料辅助属性校验(质检报告表体的自由辅助属性来源于报检点明细中)(只检查类型是否匹配、不检查非空)
    // IRule<ReportVO> marRule = new MarAssistantSaveRule<ReportVO>();
    // processer.addBeforeRule(marRule);
    MarAssistantSaveRule<ReportVO> mar = new MarAssistantSaveRule<ReportVO>();
    // 设置进行非空校验，传入物料字段名称
    // mar.setNotNullValidate(ReportHeaderVO.PK_MATERIAL);
    processer.addBeforeRule(mar);
    // 主组织可用性校验
    processer.addBeforeRule(new QcCenterEnableCheckRule<ReportVO>());
    // 校验自定义项
    processer.addBeforeRule(new UserDefSaveRule<ReportVO>(new Class[] {
      ReportHeaderVO.class, ReportItemVO.class
    }));
    // // 合生元保存-->新增时（对应还有更新时也需要做）、自动更新库存状态时批次和库存状态的校验
    processer.addBeforeRule(new ReportAddStockStateRule());
    // 新增保存前批次号处理
    processer.addBeforeRule(new InsertAndDelBatchCodeBeforeRule(true));
    // 质检流程
    processer.addBeforeRule(new ReportCheckFlowRule());
    // 1. 保存判断表体合格品没有勾选，则认为是不合格品，需要自动将表头的需要不合格品处理 勾选
//    processer.addBeforeRule(new RejectBeforCheckRule());
  }
}
