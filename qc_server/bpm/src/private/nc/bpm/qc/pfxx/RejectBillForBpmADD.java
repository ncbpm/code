package nc.bpm.qc.pfxx;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.qc.c003.maintain.rule.ReportSendMessageWithPlatTemplateRule;
import nc.bs.qc.c003.maintain.rule.UpdateSnDocRule;
import nc.bs.qc.c004.approve.action.rule.RejectWriteBackRuleForAppr;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.qc.c003.approve.action.rule.CreateStockStateChangeBill;
import nc.impl.qc.c003.approve.action.rule.Write55D2WhenAuditRule;
import nc.impl.qc.c003.approve.action.rule.WriteC001WhenAuditRule;
import nc.impl.qc.c003.approve.action.rule.WriteC005WhenAuditRule;
import nc.impl.qc.c003.approve.action.rule.WriteContinueBatchWhenAuditRule;
import nc.impl.qc.c003.approve.action.rule.WriteICBatchDocWhenAuditRule;
import nc.itf.qc.c003.maintain.IReportMaintain;
import nc.itf.qc.c004.maintain.IRejectMaintain;
import nc.itf.qc.c004.page.IRejectBillMaintainApp;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.AppContext;
import nc.vo.qc.c003.entity.ReportHeaderVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.c003.rule.Write23WhenAuditRule;
import nc.vo.qc.c003.rule.Write4331WhenAuditRule;
import nc.vo.qc.c003.rule.Write4ZRuleWhenAuditRule;
import nc.vo.qc.c003.rule.WriteMMWhenAuditRule;
import nc.vo.qc.c004.entity.RejectBillHeadVO;
import nc.vo.qc.c004.entity.RejectBillItemVO;
import nc.vo.qc.c004.entity.RejectBillVO;
import nc.vo.qc.pub.enumeration.QCBillStatusEnum;
import nc.vo.scmf.qc.dealfashion.entity.DealFashionVO;

import org.apache.commons.lang.StringUtils;

/**
 * 不合格品处理单
 * 
 * @author liyf
 * 
 */
public class RejectBillForBpmADD extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		RejectBillVO billVO = (RejectBillVO) vo;
		RejectBillHeadVO headVO = (RejectBillHeadVO) billVO.getParentVO();
		// 1.校验导入过来的数据
		checkData(billVO);
		// 2.更新不合格品处理单，并自动审批通过
		IRejectBillMaintainApp queryService = NCLocator.getInstance().lookup(
				IRejectBillMaintainApp.class);
		RejectBillVO[] originVOs = queryService
				.queryMC004App(new String[] { headVO.getPk_rejectbill() });
		// 检查不合格单pk合法性
		if (originVOs == null || originVOs.length < 1) {
			throw new BusinessException("根据指定的主键，" + headVO.getPk_rejectbill()
					+ "找不到对应的不合格处理单,请检查报文或者NC对应报检单是否删除.");
		}
		// 3.更新不合格品处理单判定入库方式
		updateRejct(billVO, originVOs[0]);
		// 4. 并自动审批通过不合格品处理单
		// 重新查询，防止并发
		originVOs = queryService.queryMC004App(new String[] { headVO
				.getPk_rejectbill() });
		approveRejct(billVO, originVOs[0]);
		// 5.设置质检报告 审批状态
		approveReportBill(billVO, originVOs[0]);

		return "回写NC成功";
	}

	/**
	 * 质检报告 审批状态
	 * 
	 * @param billVO
	 * @param rejectBillVO
	 */
	private void approveReportBill(RejectBillVO bpmVO, RejectBillVO rejectBillVO) {
		// TODO 自动生成的方法存根
		String pk_reportbill = rejectBillVO.getHVO().getPk_reportbill();
		IReportMaintain reportService = NCLocator.getInstance().lookup(
				IReportMaintain.class);
		ReportVO originVO = reportService
				.querySingleBillByPkAndBatchInfo(pk_reportbill);
		ReportVO clientVO = reportService
				.querySingleBillByPkAndBatchInfo(pk_reportbill);
		ReportHeaderVO clientHead = (ReportHeaderVO) clientVO.getParentVO();
		clientHead.setApprover(bpmVO.getHVO().getApprover());
		clientHead.setFbillstatus(QCBillStatusEnum.APPROVE.toIntValue());
		clientHead.setStatus(VOStatus.UPDATED);

		BillUpdate<ReportVO> update = new BillUpdate<ReportVO>();
		ReportVO[] vos = update.update(new ReportVO[] { clientVO },
				new ReportVO[] { originVO });

		// // 生成库存状态调整单：只有生产完工和采购到货才生产，而且必须有批次号才生产
		new CreateStockStateChangeBill().process(vos);
		// 回写报检单是否审批通过
		new WriteC001WhenAuditRule(UFBoolean.TRUE).process(vos);  
		// 回写到货单的检验结果
		new Write23WhenAuditRule(UFBoolean.TRUE).process(vos);
		// 回写销售发货单的检验结果
		new Write4331WhenAuditRule(UFBoolean.TRUE).process(vos);
		// 回写库存冻结解冻的检验结果
		new Write4ZRuleWhenAuditRule(UFBoolean.TRUE).process(vos);
		// 回写生产报告的检验结果
		new WriteMMWhenAuditRule(UFBoolean.TRUE).process(vos);
		// 回写紧急放行单
		new WriteC005WhenAuditRule(UFBoolean.TRUE).process(vos);
		// 回写回写质检连续批
		new WriteContinueBatchWhenAuditRule(UFBoolean.TRUE).process(vos);
		// 审批弃审时回写回写库存批次档案（质量等级、上次质检日期、在检标志）
		new WriteICBatchDocWhenAuditRule(UFBoolean.TRUE).process(vos);
		// 回写工序完工报告
		new Write55D2WhenAuditRule(UFBoolean.TRUE).process(vos);
		new ReportSendMessageWithPlatTemplateRule().process(vos);
		// 更新序列号质量等级
		new UpdateSnDocRule(UFBoolean.TRUE).process(vos);

	}

	/**
	 * 审批通过不合格品处理单 审批人，通过BPM传过来
	 * 
	 * @param billVO
	 * @throws BusinessException
	 */
	private void approveRejct(RejectBillVO bpmVO, RejectBillVO originVO)
			throws BusinessException {
		// TODO 自动生成的方法存根
		RejectBillHeadVO headVO = (RejectBillHeadVO) originVO.getParentVO();
		IRejectBillMaintainApp queryService = NCLocator.getInstance().lookup(
				IRejectBillMaintainApp.class);
		// 重新查询一个用来修改，模拟前台传过来的VO
		RejectBillVO clientVO = queryService
				.queryMC004App(new String[] { headVO.getPk_rejectbill() })[0];
		// 设置明细 判定结果
		clientVO.getHVO().setApprover(bpmVO.getHVO().getApprover());
		clientVO.getHVO().setFbillstatus(QCBillStatusEnum.APPROVE.toIntValue());
		clientVO.getHVO().setTaudittime(AppContext.getInstance().getBusiDate());
		clientVO.getParentVO().setStatus(VOStatus.UPDATED);

		BillUpdate<RejectBillVO> update = new BillUpdate<RejectBillVO>();
		RejectBillVO[] vos = update.update(new RejectBillVO[] { clientVO },
				new RejectBillVO[] { originVO });
		// 回写质检报告
		new RejectWriteBackRuleForAppr().process(vos);

	}

	/**
	 * 更新质检报告的判定入库方式
	 * 
	 * @param bpmVO
	 * @param originVO
	 * @throws BusinessException
	 */
	private void updateRejct(RejectBillVO bpmVO, RejectBillVO originVO)
			throws BusinessException {
		// TODO 自动生成的方法存根
		RejectBillHeadVO headVO = (RejectBillHeadVO) originVO.getParentVO();
		IRejectBillMaintainApp queryService = NCLocator.getInstance().lookup(
				IRejectBillMaintainApp.class);
		// 重新查询一个用来修改，模拟前台传过来的VO
		RejectBillVO bill = queryService.queryMC004App(new String[] { headVO
				.getPk_rejectbill() })[0];
		// 设置明细 判定结果
		RejectBillItemVO[] modifyItems = (RejectBillItemVO[]) bpmVO
				.getChildrenVO();
		RejectBillItemVO[] clientItems = (RejectBillItemVO[]) bill
				.getChildrenVO();
		for (RejectBillItemVO ele : modifyItems) {
			boolean ismached = false;
			for (RejectBillItemVO org : clientItems) {
				if (ele.getPk_rejectbill_b().equals(org.getPk_rejectbill_b())) {
					org.setFprocessjudge(ele.getFprocessjudge());

					org.setStatus(VOStatus.UPDATED);
					ismached = true;
					break;
				}
			}
			if (!ismached) {
				throw new BusinessException("根据报文指定的表体主键: "
						+ ele.getPk_rejectbill_b() + "未匹配到NC对应的表体行.");
			}

		}
		IRejectMaintain service = NCLocator.getInstance().lookup(
				IRejectMaintain.class);
		service.saveBase(new RejectBillVO[] { bill }, new Object(),
				new RejectBillVO[] { originVO });

	}

	private void checkData(RejectBillVO resvo) throws BusinessException {
		// TODO 自动生成的方法存根
		if (resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		if (resvo.getChildrenVO() == null || resvo.getChildrenVO().length == 0) {
			throw new BusinessException("表体不允许为空");
		}
		if (StringUtils.isEmpty(resvo.getHVO().getPk_rejectbill())) {
			throw new BusinessException("Pk_rejectbill 不能为空");
		}
		if (StringUtils.isEmpty(resvo.getHVO().getApprover())) {
			throw new BusinessException("approver 不能为空");
		}

		for (RejectBillItemVO item : resvo.getBVO()) {
			if (StringUtils.isEmpty(item.getPk_rejectbill_b())) {
				throw new BusinessException("pk_rejectbill_b 不能为空");
			}

		}

	}

}
