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
		// 参数VO的正确性校验
		this.VoValidate(vos);
		// VO中的最大值检查
		VOFieldLengthChecker.checkVOFieldsLength(vos);

	}

	private void VoValidate(AggFiveMetalsVO[] vos) {
		for (AggFiveMetalsVO vo : vos) {
			if (null == vo) {
				ExceptionUtils.wrappBusinessException(" 传入的数据中有空值 ！");
			} else if (null == vo.getParentVO()
					|| ArrayUtils.isEmpty(vo.getChildrenVO())) {
				ExceptionUtils.wrappBusinessException("传入的数据中存在不完整的数据！");
			} else {

				FiveMetalsHVO hvo = vo.getParentVO();

				if (hvo.getPk_group() == null || hvo.getPk_org() == null) {
					ExceptionUtils
							.wrappBusinessException("传入的数据中存在集团或者组织为空的数据！");
				}

				if (hvo.getVcardno() == null) {
					ExceptionUtils.wrappBusinessException("传入的数据中存在卡号为空的数据！");
				}

				if (hvo.getCperiod() == null) {
					ExceptionUtils.wrappBusinessException("传入的数据中存在月份为空的数据！");
				}

				if (hvo.getVdepartment() == null && hvo.getVproject() == null) {
					ExceptionUtils
							.wrappBusinessException("传入的数据中存在部门或者项目为空的数据！");
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
									.wrappBusinessException("传入的数据中存在来源信息为空的数据！");
						}

						if (bvo.getNmny() == null) {
							ExceptionUtils
									.wrappBusinessException("传入的数据中存在金额为空的数据！");
						}
					}
				}

				if (list.size() == 0) {
					ExceptionUtils.wrappBusinessException("传入的数据中存在不完整的数据！");
				}
			}
		}
	}
}
