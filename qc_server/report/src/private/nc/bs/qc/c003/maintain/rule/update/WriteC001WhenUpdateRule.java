package nc.bs.qc.c003.maintain.rule.update;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.qc.c003.maintain.rule.insert.WriteBackQcStd;
import nc.bs.qc.c003.writeback.AbstractWriteC001;
import nc.impl.pubapp.bill.rewrite.BillRewriter;
import nc.impl.pubapp.bill.rewrite.RewritePara;
import nc.impl.pubapp.pattern.rule.ICompareRule;
import nc.pubitf.qc.c001.qc.IWritebackForC003;
import nc.pubitf.qc.c001.qc.WritebackForC003Para;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.scmpub.res.billtype.QCBillType;

import org.apache.commons.lang.ArrayUtils;

/**
 * @description ��д���쵥
 * @scene �ʼ챨���޸ı���
 * @param ��
 * @since 6.0
 * @version 2011-1-6 ����04:18:24
 * @author hanbin
 */
public class WriteC001WhenUpdateRule extends AbstractWriteC001 implements
    ICompareRule<ReportVO> {

  @Override
  public List<RewritePara> getRewriteParaList(ReportVO newVO, ReportVO oldVO) {
    BillRewriter tool = this.getBillRewriter(this.getItemKeyMapping());
    Map<String, List<RewritePara>> rwParaMap =
        tool.splitForUpdate(new ReportVO[] {
          newVO
        }, new ReportVO[] {
          oldVO
        });
    return rwParaMap.get(QCBillType.ApplyBill.getCode());
  }

  @Override
  public void process(ReportVO[] voArray, ReportVO[] oriVOArray) {
    for (int i = 0, len = voArray.length; i < len; i++) {
      WritebackForC003Para[] paras =
          this.buildParaArray(voArray[i], oriVOArray[i]);
      if (ArrayUtils.isEmpty(paras)) {
        continue;
      }
      this.writeback(paras);
  
    }
    for (int i = 0, len = voArray.length; i < len; i++) {
    	// 2017-07-30 ����޸����ʼ쵥�ļ����׼,���д���±��쵥
  		new WriteBackQcStd().writebackStd(voArray[0]);
      }
  }

  @Override
  public void writeback(WritebackForC003Para[] paras) {
    IWritebackForC003 service =
        NCLocator.getInstance().lookup(IWritebackForC003.class);
    try {
      service.writebackWhenUpdate(paras);
    }
    catch (BusinessException ex) {
      ExceptionUtils.wrappException(ex);
    }
  }
}
