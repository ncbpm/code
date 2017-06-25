/**
 * $�ļ�˵��$
 * 
 * @author tianft
 * @version 6.0
 * @see
 * @since 6.0
 * @time 2010-6-1 ����06:12:19
 */
package nc.vo.qc.c003.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.c003.entity.ReportItemVO;
import nc.vo.qc.c003.entity.ReportVO;

import org.apache.commons.lang.ArrayUtils;

/**
 * 
 * @author liyf
 *
 */
public class RejectBeforCheckRule implements IRule<ReportVO> {

  /**
   * ���෽����д
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
        ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
            .getNCLangRes().getStrByID("c010003_0", "0c010003-0074")/*
                                                                     * @res
                                                                     * "�ʼ챨���ͷ���ݲ���Ϊ�գ�"
                                                                     */);
      }
      if (ArrayUtils.isEmpty(vo.getChildrenVO())) {
        ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
            .getNCLangRes().getStrByID("c010003_0", "0c010003-0075")/*
                                                                     * @res
                                                                     * "�ʼ챨��������ݲ���Ϊ�գ�"
                                                                     */);
      }
      //2017-06-25 1. �����жϱ���ϸ�Ʒû�й�ѡ������Ϊ�ǲ��ϸ�Ʒ����Ҫ�Զ�����ͷ����Ҫ���ϸ�Ʒ���� ��ѡ
      boolean isAllHeGe = true;//�Ƿ�ȫ���ϸ�
      for(ReportItemVO item:vo.getBVO()){
    	  if(item.getBeligible() == null || !item.getBeligible().booleanValue()){
    		  vo.getHVO().setBneeddeal(UFBoolean.TRUE);
    		  isAllHeGe = false;
    	  }
      }
      
      if(isAllHeGe){
		  vo.getHVO().setBneeddeal(UFBoolean.FALSE);
      }
    }

  }

 
  


}
