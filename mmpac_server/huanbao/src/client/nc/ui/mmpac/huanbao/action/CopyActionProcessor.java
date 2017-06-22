package nc.ui.mmpac.huanbao.action;

import nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.mmpac.huanbao.AggHuanbaoHVO;
import nc.vo.uif2.LoginContext;

public class CopyActionProcessor implements
		ICopyActionProcessor<AggHuanbaoHVO> {

	@Override
	public void processVOAfterCopy(AggHuanbaoHVO paramT,
			LoginContext paramLoginContext) {
		paramT.getParentVO().setPrimaryKey(null);
		paramT.getParentVO().setAttributeValue("modifier", null);
		paramT.getParentVO().setAttributeValue("modifiedtime", null);
		paramT.getParentVO().setAttributeValue("creator", null);
		paramT.getParentVO().setAttributeValue("creationtime", null);
		paramT.getParentVO().setAttributeValue("billno", null);
		// ���õ���״̬������ҵ������Ĭ��ֵ
		paramT.getParentVO().setAttributeValue("approvestatus", BillStatusEnum.FREE.value());
		paramT.getParentVO().setAttributeValue("dbilldate",  new UFDate());
		//TODO ������Ҫҵ���Լ����䴦�����
		String[] codes =paramT.getTableCodes();
		if (codes != null && codes.length>0) {
			for (int i = 0; i < codes.length; i++) {
				String tableCode = codes[i];
				 CircularlyAccessibleValueObject[] childVOs = 	paramT.getTableVO(tableCode);
				 for (CircularlyAccessibleValueObject childVO : childVOs) {
					 try {
						childVO.setPrimaryKey(null);
					} catch (BusinessException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				}
			}
		}
	}
}
