package nc.vo.ic.m4d.deal.bpm;

import java.util.Map;

import nc.itf.scmpub.reference.uap.org.DeptPubService;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.m4d.deal.BillTransTo4DProcess;
import nc.vo.ic.m4d.entity.MaterialOutHeadVO;
import nc.vo.ic.m4d.entity.MaterialOutVO;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.VOEntityUtil;

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
	protected void fillValue(MaterialOutVO[] bills) {
		super.fillValue(bills);
		fillDeptVid(bills);
	}

	private void fillDeptVid(MaterialOutVO[] vos) {
		MaterialOutHeadVO[] heads = VOEntityUtil.getHeadVOs(vos);
		String[] cdeptid = VOEntityUtil.getVOsValues(heads,
				ICPubMetaNameConst.CDPTID, String.class);
		Map<String, String> cdptmap = DeptPubService
				.getLastVIDSByDeptIDS(cdeptid);
		for (MaterialOutVO vo : vos) {
			MaterialOutHeadVO head = vo.getHead();
			head.setCdptvid(cdptmap.get(head.getCdptid()));
		}
	}

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
