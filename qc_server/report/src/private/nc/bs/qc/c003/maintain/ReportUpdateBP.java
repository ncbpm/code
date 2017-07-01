package nc.bs.qc.c003.maintain;

import nc.bs.qc.c003.maintain.rule.ChkBillCodeUniqueRule;
import nc.bs.qc.c003.maintain.rule.ReportChkChkBatUnique;
import nc.bs.qc.c003.maintain.rule.update.FillUpDataUpdateRule;
import nc.bs.qc.c003.maintain.rule.update.UpdChkBatCode;
import nc.bs.qc.c003.maintain.rule.update.UpdateBatchCodeAfterRule;
import nc.bs.qc.c003.maintain.rule.update.UpdateBatchCodeBeforeRule;
import nc.bs.qc.c003.maintain.rule.update.UpdateBillCodeRule;
import nc.bs.qc.c003.maintain.rule.update.WriteC001WhenUpdateRule;
import nc.bs.qc.c003.plugin.ReportBPPlugInPoint;
import nc.bs.qc.pub.rule.QCRowNoCheckRule;
import nc.impl.pubapp.bd.material.assistant.MarAssistantSaveRule;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pubapp.util.SetUpdateAuditInfoRule;
import nc.vo.qc.c003.entity.ReportHeaderVO;
import nc.vo.qc.c003.entity.ReportItemVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.c003.rule.RejectAfterCheckRule;
import nc.vo.qc.c003.rule.RejectBeforCheckRule;
import nc.vo.qc.c003.rule.ReportItemsCheckRule;
import nc.vo.qc.c003.rule.ReportNumCheckRule;
import nc.vo.qc.c003.util.ReportAddStockStateRule;
import nc.vo.qc.c003.util.ReportVOInfoUtils;
import nc.vo.qc.pub.rule.CheckBillNumRule;
import nc.vo.qc.pub.rule.NoPassUpdateRule;

/**
 * 方法功能描述：完成修改保存报检单
 * <p>
 * <b>参数说明</b>
 * 
 * @param aggVO数组
 *            <p>
 * @since 6.0
 * @author hanbin
 * @time 2010-1-13 上午10:18:08
 */
public class ReportUpdateBP{
	--记得合并产品补丁
	public ReportVO[] updateReport(ReportVO[] voArray, ReportVO[] originBills) {
		CompareAroundProcesser<ReportVO> processer = new CompareAroundProcesser<ReportVO>(
				ReportBPPlugInPoint.ReportUpdateBP);

		// 添加BP规则
		this.addBeforeRule(processer);
		this.addAfterRule(processer);

		// 因为首次入库日期没有存储，在执行后归之前，把首次入库日期再补进去。
		// 一定要在后规则执行前
		ReportVOInfoUtils.handlerIndate(null, voArray);

		processer.before(voArray, originBills);

		BillUpdate<ReportVO> bo = new BillUpdate<ReportVO>();
		ReportVO[] vos = bo.update(voArray, originBills);

		processer.after(voArray, originBills);

		return vos;
	}

	private void addAfterRule(CompareAroundProcesser<ReportVO> processer) {
		// 检查单据号的唯一性
		processer.addAfterRule(new ChkBillCodeUniqueRule());
		// 同步更新检验批次
		processer.addAfterRule(new UpdChkBatCode());
		// 检验批次唯一性
		processer.addAfterRule(new ReportChkChkBatUnique());
		// 回写报检单
		processer.addAfterRule(new WriteC001WhenUpdateRule());
		// 修改保存后批次号处理
		processer.addAfterRule(new UpdateBatchCodeAfterRule());
		// 2017-06-25
		// 1. 保存判断表体合格品没有勾选，则认为是不合格品，需要自动将表头的需要不合格品处理 勾选
		// 2. 保存判断表体合格品没有勾选，则认为是不合格，自动生成不合格品处理单
		// 3. 需要考虑 推式保存， .后续 计量 报检--自动质检报告--自动不合格品处理单
//		processer.addAfterRule(new RejectAfterCheckRule());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addBeforeRule(CompareAroundProcesser<ReportVO> processer) {

		// 单据号处理
		processer.addBeforeRule(new UpdateBillCodeRule());
		// 填补数据
		processer.addBeforeRule(new FillUpDataUpdateRule());
		// 检查质量模块中的数量的规则
		String[] hKeys = new String[2];
		hKeys[0] = ReportHeaderVO.NCHECKNUM;
		hKeys[1] = ReportHeaderVO.NCHECKASTNUM;
		String[] bKeys = new String[2];
		bKeys[0] = ReportItemVO.NNUM;
		bKeys[1] = ReportItemVO.NASTNUM;
		processer.addBeforeRule(new CheckBillNumRule<ReportVO>(hKeys, bKeys));
		// 审计信息
		processer.addBeforeRule(new SetUpdateAuditInfoRule<ReportVO>());
		// 非空校验
		processer.addBeforeRule(new ReportItemsCheckRule());
		// 行号重复校验
		processer.addBeforeRule(new QCRowNoCheckRule(ReportItemVO.class,
				ReportItemVO.CROWNO));
		// 数量校验
		processer.addBeforeRule(new ReportNumCheckRule());
		// 修改保存前批次号处理
		processer.addBeforeRule(new UpdateBatchCodeBeforeRule());
		// 自由辅助属性的检查类(只检查类型是否匹配、不检查非空)
		MarAssistantSaveRule<ReportVO> mar = new MarAssistantSaveRule<ReportVO>();
		// 设置进行非空校验，传入物料字段名称
		// mar.setNotNullValidate(ReportHeaderVO.PK_MATERIAL);
		processer.addBeforeRule(mar);
		// 为审批通过的单据修改规则
		processer.addBeforeRule(new NoPassUpdateRule<ReportVO>());
		// // 合生元保存-->新增时（对应还有更新时也需要做）
		processer.addBeforeRule(new ReportAddStockStateRule());

	}
}
