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

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.c003.entity.ReportItemVO;
import nc.vo.qc.c003.entity.ReportVO;

import org.apache.commons.lang.ArrayUtils;

/**
 *       //2017-06-25 1. 保存判断表体合格品没有勾选，则认为是不合格品，需要自动将表头的需要不合格品处理 勾选

 * @author liyf
 * 
 */
public class RejectBeforCheckRule implements IRule<ReportVO> {

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
			if (vo.getParentVO() == null) {
				ExceptionUtils
						.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("c010003_0",
										"0c010003-0074")/*
														 * @res "质检报告表头数据不能为空！"
														 */);
			}
			if (ArrayUtils.isEmpty(vo.getChildrenVO())) {
				ExceptionUtils
						.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("c010003_0",
										"0c010003-0075")/*
														 * @res "质检报告表体数据不能为空！"
														 */);
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
			// 2017-06-25 1. 保存判断表体合格品没有勾选，则认为是不合格品，需要自动将表头的需要不合格品处理 勾选
			boolean isAllHeGe = true;// 是否全部合格
			for (ReportItemVO item : vo.getBVO()) {
				if (item.getBeligible() == null
						|| !item.getBeligible().booleanValue()) {
					vo.getHVO().setBneeddeal(UFBoolean.TRUE);
					isAllHeGe = false;
				}
			}

			if (isAllHeGe) {
				vo.getHVO().setBneeddeal(UFBoolean.FALSE);
			}
		}

	}

}
