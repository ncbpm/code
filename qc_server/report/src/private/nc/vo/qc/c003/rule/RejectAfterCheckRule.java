/**
 * $文件说明$
 * 
 * @author tianft
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-6-1 下午06:12:19
 */
package nc.vo.qc.c003.rule;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.qc.c004.maintain.IRejectMaintain;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.c003.entity.ReportItemVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.c004.entity.RejectBillVO;
import nc.vo.qc.pub.enumeration.QCBillStatusEnum;
import nc.vo.scmpub.res.billtype.QCBillType;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author liyf //2017-06-25 1. 保存判断表体合格品没有勾选，则认为是不合格品，需要自动将表头的需要不合格品处理 勾选 2.
 *         保存判断表体合格品没有勾选，则认为是不合格，自动生成不合格品处理单 3. 需要考虑 推式保存， .后续 计量
 *         报检--自动质检报告--自动不合格品处理单
 * 
 */
public class RejectAfterCheckRule implements IRule<ReportVO> {

	/**
	 * 父类方法重写
	 * 
	 * @see nc.impl.pubapp.pattern.rule.IRule#process(E[])
	 */
	@Override
	public void process(ReportVO[] vos) {
		if (ArrayUtils.isEmpty(vos)) {
			return;
		}
		for (ReportVO vo : vos) {
			// 过滤表体需要进行不合格处理的行
			// 校验需要进行不合格处理的表体行是否存在非空项
			List<ReportItemVO> list = filterData(vo);
			if (list.size() == 0) {
				return;
			}
			// 只有这几种类型,才执行 采购，成品，半成品
			String vtrantypecode = vo.getHVO().getVtrantypecode();
			boolean bisAutoDeal = false;
			if ("C003-0001".equalsIgnoreCase(vtrantypecode)
					|| "C003-0002".equalsIgnoreCase(vtrantypecode)
					|| "C003-Cxx_05".equalsIgnoreCase(vtrantypecode)) {
				bisAutoDeal = true;
			}
			if (!bisAutoDeal) {
				continue;
			}

			ReportVO bill = new ReportVO();
			bill.setParentVO(vo.getHVO());
			bill.setBVO(list.toArray(new ReportItemVO[list.size()]));
			// 进行数据交互，的到不合格品处理单数据
			RejectBillVO[] destVos = PfServiceScmUtil.executeVOChange(
					QCBillType.ReportBill.getCode(),
					QCBillType.RejectBill.getCode(), vos);

			try {
				NCLocator.getInstance().lookup(IRejectMaintain.class)
						.saveBase(destVos, null, null);
			} catch (BusinessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				ExceptionUtils.wrappBusinessException("质检报告自动生成不合格品处理单异常:"+e.getMessage());
			}

		}

	}

	/**
	 * 得到需要生成不合格品处理单的表体数据
	 * 
	 * @param vo
	 * @return
	 */
	private List<ReportItemVO> filterData(ReportVO vo) {
		// TODO 自动生成的方法存根
		List<ReportItemVO> list = new ArrayList<ReportItemVO>();
		if (!QCBillStatusEnum.FREE.value().equals(vo.getHVO().getFbillstatus())) {
			return list;
		}

		if (!vo.getHVO().getBneeddeal().booleanValue()) {
			return list;// 不需要合格品处理
		}
		// 根据不合格品处理主键来判断是否已经生成不合格品处理单
		String pk_rejectbill = vo.getHVO().getPk_rejectbill();
		if (!StringUtils.isEmpty(pk_rejectbill)) {
			// 已经生成不合格品处理单
			return list;
		}
		if (ArrayUtils.isEmpty(vo.getBVO())) {
			return list;
		}
		for (ReportItemVO item : vo.getBVO()) {
			if (item.getBeligible() != null
					&& item.getBeligible().booleanValue()) {
				continue;
			}
			if (StringUtils.isEmpty(item.getPk_qualitylv_b())
					|| (item.getFprocessjudge() == null)) {
				continue;

			}
			list.add(item);
		}

		return list;
	}

}
