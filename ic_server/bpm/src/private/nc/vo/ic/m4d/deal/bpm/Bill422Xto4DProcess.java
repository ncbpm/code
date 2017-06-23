package nc.vo.ic.m4d.deal.bpm;

import java.util.Map;

import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.m4d.deal.BillTransTo4DProcess;
import nc.vo.ic.m4d.entity.MaterialOutVO;

public class Bill422Xto4DProcess extends BillTransTo4DProcess {

	public Bill422Xto4DProcess(boolean isFillNum) {
		super(true);

	}

	/**
	 * ���෽����д
	 */
	@Override
	public MaterialOutVO[] processBillRule(MaterialOutVO[] vos) {
		this.fillValue(vos);
		// this.clearFirstType(vos);
		return vos;
	}

	// /**
	// * �������뵥�п���û������������Ϣ�������ݽ������ֶ�����Դͷ������������ʱ��Ҫ���
	// *
	// * @param vos
	// */
	// private void clearFirstType(MaterialOutVO[] vos) {
	// for (MaterialOutVO bill : vos) {
	// for (MaterialOutBodyVO body : bill.getBodys()) {
	// if (StringUtil.isSEmptyOrNull(body.getCfirstbillhid())) {
	// body.setCfirsttype(null);
	// }
	// }
	// }
	// }

	@Override
	protected void fillTranstypeCode(MaterialOutVO[] vos) {

		for (ICBillVO vo : vos) {
			String vtrantypecode = "4D-01";
			vo.getHead().setVtrantypecode(vtrantypecode);
			Map<String, String> map = PfServiceScmUtil
					.getTrantypeidByCode(new String[] { vtrantypecode });
			vo.getHead().setCtrantypeid(
					map == null ? null : map.get(vtrantypecode));
		}
	}
}
