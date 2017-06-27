package nc.bs.ic.fivemetals.maintain.rule;

import java.util.ArrayList;
import java.util.List;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.ic.pub.check.VOCheckUtil;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
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
				try {
					VOCheckUtil.checkHeadNotNullFields(vo, new String[] {
							"pk_group", "pk_org", "vcardno", "vbillstatus",
							"vdepartment", "vcardtype" });
					VOCheckUtil.checkBodyNotNullFields(vo, new String[] {
							"vsourcebillno", "vsourcetype", "vsourcebillid",
							"nmny", "cperiod", "itype" });
				} catch (ValidationException e) {
					ExceptionUtils.wrappBusinessException(e.getMessage());
				}
				CircularlyAccessibleValueObject[] itemVOs = vo.getChildrenVO();
				List<CircularlyAccessibleValueObject> list = new ArrayList<CircularlyAccessibleValueObject>();
				for (CircularlyAccessibleValueObject itemVO : itemVOs) {
					if (itemVO != null
							&& VOStatus.DELETED != itemVO.getStatus()) {
						list.add(itemVO);
					}
				}

				if (list.size() == 0) {
					ExceptionUtils.wrappBusinessException("����������д��ڲ����������ݣ�");
				}
			}
		}
	}
}
