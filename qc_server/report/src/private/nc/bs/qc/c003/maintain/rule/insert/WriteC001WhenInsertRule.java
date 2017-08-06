package nc.bs.qc.c003.maintain.rule.insert;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.qc.c003.writeback.AbstractWriteC001;
import nc.impl.pubapp.bill.rewrite.BillRewriter;
import nc.impl.pubapp.bill.rewrite.RewritePara;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.pubitf.qc.c001.qc.IWritebackForC003;
import nc.pubitf.qc.c001.qc.WritebackForC003Para;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.scmpub.res.billtype.QCBillType;

import org.apache.commons.lang.ArrayUtils;

/**
 * @description 质检报告新增保存时回写报检单的规则类
 * @scene 质检报告新增保存
 * @param 无
 * @since 6.0
 * @version 2010-12-28 下午02:31:42
 * @author hanbin
 */
public class WriteC001WhenInsertRule extends AbstractWriteC001 implements
		IRule<ReportVO> {

	@Override
	public List<RewritePara> getRewriteParaList(ReportVO newVO, ReportVO oldVO) {
		BillRewriter tool = this.getBillRewriter(this.getItemKeyMapping());
		Map<String, List<RewritePara>> rwParaMap = tool
				.splitForInsert(new ReportVO[] { newVO });
		return rwParaMap.get(QCBillType.ApplyBill.getCode());
	}

	@Override
	public void process(ReportVO[] voArray) {
		if (ArrayUtils.isEmpty(voArray)) {
			return;
		}
		for (ReportVO vo : voArray) {

			WritebackForC003Para[] paras = this.buildParaArray(vo, null);
			this.writeback(paras);
			// 2017-07-30 如果修改了质检单的检验标准,则回写更新报检单
			new WriteBackQcStd().writebackStd(vo);
		}
	}

	
	


	@Override
	public void writeback(WritebackForC003Para[] paras) {

		IWritebackForC003 service = NCLocator.getInstance().lookup(
				IWritebackForC003.class);
		try {
			service.writebackWhenInsert(paras);
		} catch (BusinessException ex) {
			ExceptionUtils.wrappException(ex);
		}
	}
}
