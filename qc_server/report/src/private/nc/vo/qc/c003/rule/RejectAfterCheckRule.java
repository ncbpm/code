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
 * @author liyf //2017-06-25 1. �����жϱ���ϸ�Ʒû�й�ѡ������Ϊ�ǲ��ϸ�Ʒ����Ҫ�Զ�����ͷ����Ҫ���ϸ�Ʒ���� ��ѡ 2.
 *         �����жϱ���ϸ�Ʒû�й�ѡ������Ϊ�ǲ��ϸ��Զ����ɲ��ϸ�Ʒ���� 3. ��Ҫ���� ��ʽ���棬 .���� ����
 *         ����--�Զ��ʼ챨��--�Զ����ϸ�Ʒ����
 * 
 */
public class RejectAfterCheckRule implements IRule<ReportVO> {

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
			// ���˱�����Ҫ���в��ϸ������
			// У����Ҫ���в��ϸ���ı������Ƿ���ڷǿ���
			List<ReportItemVO> list = filterData(vo);
			if (list.size() == 0) {
				return;
			}
			// ֻ���⼸������,��ִ�� �ɹ�����Ʒ�����Ʒ
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
			// �������ݽ������ĵ����ϸ�Ʒ��������
			RejectBillVO[] destVos = PfServiceScmUtil.executeVOChange(
					QCBillType.ReportBill.getCode(),
					QCBillType.RejectBill.getCode(), vos);

			try {
				NCLocator.getInstance().lookup(IRejectMaintain.class)
						.saveBase(destVos, null, null);
			} catch (BusinessException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				ExceptionUtils.wrappBusinessException("�ʼ챨���Զ����ɲ��ϸ�Ʒ�����쳣:"+e.getMessage());
			}

		}

	}

	/**
	 * �õ���Ҫ���ɲ��ϸ�Ʒ�����ı�������
	 * 
	 * @param vo
	 * @return
	 */
	private List<ReportItemVO> filterData(ReportVO vo) {
		// TODO �Զ����ɵķ������
		List<ReportItemVO> list = new ArrayList<ReportItemVO>();
		if (!QCBillStatusEnum.FREE.value().equals(vo.getHVO().getFbillstatus())) {
			return list;
		}

		if (!vo.getHVO().getBneeddeal().booleanValue()) {
			return list;// ����Ҫ�ϸ�Ʒ����
		}
		// ���ݲ��ϸ�Ʒ�����������ж��Ƿ��Ѿ����ɲ��ϸ�Ʒ����
		String pk_rejectbill = vo.getHVO().getPk_rejectbill();
		if (!StringUtils.isEmpty(pk_rejectbill)) {
			// �Ѿ����ɲ��ϸ�Ʒ����
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
