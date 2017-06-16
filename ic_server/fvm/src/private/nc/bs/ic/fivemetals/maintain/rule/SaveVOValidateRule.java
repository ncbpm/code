package nc.bs.ic.fivemetals.maintain.rule;

import java.util.ArrayList;
import java.util.List;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.ic.fivemetals.FiveMetalsBVO;
import nc.vo.ic.fivemetals.FiveMetalsHVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.util.VOFieldLengthChecker;

import org.apache.commons.lang.ArrayUtils;

public class SaveVOValidateRule implements IRule<AggFiveMetalsVO> {

	@Override
	public void process(AggFiveMetalsVO[] vos) {
		// ����VO����ȷ��У��
		this.VoValidate(vos);
		// VO�е����ֵ���
		VOFieldLengthChecker.checkVOFieldsLength(vos);

	}

	private void VoValidate(AggFiveMetalsVO[] vos) {
		for (AggFiveMetalsVO vo : vos) {
			if (null == vo) {
				ExceptionUtils.wrappBusinessException(" ������������п�ֵ ��");
			} else if (null == vo.getParentVO()
					|| ArrayUtils.isEmpty(vo.getChildrenVO())) {
				ExceptionUtils.wrappBusinessException("����������д��ڲ����������ݣ�");
			} else {

				FiveMetalsHVO hvo = vo.getParentVO();

				if (hvo.getPk_group() == null || hvo.getPk_org() == null) {
					ExceptionUtils
							.wrappBusinessException("����������д��ڼ��Ż�����֯Ϊ�յ����ݣ�");
				}

				if (hvo.getVcardno() == null) {
					ExceptionUtils.wrappBusinessException("����������д��ڿ���Ϊ�յ����ݣ�");
				}

				if (hvo.getCperiod() == null) {
					ExceptionUtils.wrappBusinessException("����������д����·�Ϊ�յ����ݣ�");
				}

				if (hvo.getVdepartment() == null && hvo.getVproject() == null) {
					ExceptionUtils
							.wrappBusinessException("����������д��ڲ��Ż�����ĿΪ�յ����ݣ�");
				}
				CircularlyAccessibleValueObject[] itemVOs = vo.getChildrenVO();
				List<CircularlyAccessibleValueObject> list = new ArrayList<CircularlyAccessibleValueObject>();
				for (CircularlyAccessibleValueObject itemVO : itemVOs) {
					if (itemVO != null
							&& VOStatus.DELETED != itemVO.getStatus()) {
						list.add(itemVO);
					} else {
						FiveMetalsBVO bvo = (FiveMetalsBVO) itemVO;

						if (bvo.getVsourcebillno() == null
								|| bvo.getVsourcetype() == null
								|| bvo.getVsourcebillid() == null) {
							ExceptionUtils
									.wrappBusinessException("����������д�����Դ��ϢΪ�յ����ݣ�");
						}

						if (bvo.getNmny() == null) {
							ExceptionUtils
									.wrappBusinessException("����������д��ڽ��Ϊ�յ����ݣ�");
						}
					}
				}

				if (list.size() == 0) {
					ExceptionUtils.wrappBusinessException("����������д��ڲ����������ݣ�");
				}
			}
		}
	}
}
