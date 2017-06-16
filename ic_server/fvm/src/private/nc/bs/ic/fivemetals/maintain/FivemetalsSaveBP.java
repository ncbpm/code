package nc.bs.ic.fivemetals.maintain;

import nc.bs.ic.fivemetals.maintain.rule.CardNoUniqueRule;
import nc.bs.ic.fivemetals.maintain.rule.GetBillNORule;
import nc.bs.ic.fivemetals.maintain.rule.RowNODealRule;
import nc.bs.ic.fivemetals.maintain.rule.SaveVOValidateRule;
import nc.bs.ic.fivemetals.maintain.rule.SetAuditInfoRule;
import nc.bs.ic.fivemetals.plugin.FivemetalsPluginPoint;
import nc.impl.pubapp.pattern.data.bill.BillInsert;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.ic.fivemetals.AggFiveMetalsVO;
import nc.vo.pu.pub.rule.NewestOrgVersionFillRule;
import nc.vo.pub.VOStatus;

public class FivemetalsSaveBP {

	public AggFiveMetalsVO[] save(AggFiveMetalsVO[] clientVos,
			AggFiveMetalsVO[] originVos) {

		AggFiveMetalsVO[] returnedVos;
		if (clientVos[0].getParentVO().getStatus() != VOStatus.NEW) {
			CompareAroundProcesser<AggFiveMetalsVO> processer = new CompareAroundProcesser<AggFiveMetalsVO>(
					FivemetalsPluginPoint.UPDATE);
			this.addBeforeRule(processer, clientVos);
			this.addAfterRule(processer);
			processer.before(clientVos, originVos);
			returnedVos = new BillUpdate<AggFiveMetalsVO>().update(clientVos,
					originVos);
			processer.after(returnedVos, originVos);
		} else {
			CompareAroundProcesser<AggFiveMetalsVO> processer = new CompareAroundProcesser<AggFiveMetalsVO>(
					FivemetalsPluginPoint.INSERT);
			this.addBeforeRule(processer, clientVos);
			this.addAfterRule(processer);
			processer.before(clientVos, originVos);
			returnedVos = new BillInsert<AggFiveMetalsVO>().insert(clientVos);
			processer.after(returnedVos, null);
		}
		return returnedVos;
	}

	private void addAfterRule(CompareAroundProcesser<AggFiveMetalsVO> processer) {

		// У�� ����С����

	}

	private void addBeforeRule(
			CompareAroundProcesser<AggFiveMetalsVO> processer,
			AggFiveMetalsVO[] orgUpdateVos) {

		// ����У��
		if (orgUpdateVos[0].getParentVO().getStatus() == VOStatus.NEW) {
			processer.addBeforeRule(new CardNoUniqueRule());
		}
		// ���ݱ������
		processer.addBeforeRule(new GetBillNORule());
		// VO���
		processer.addBeforeRule(new SaveVOValidateRule());
		// ��������֯���°�
		processer
				.addBeforeFinalRule(new NewestOrgVersionFillRule<AggFiveMetalsVO>(
						"pk_fivemetals_h"));
		// ���ô�����Ϣ
		// VO���
		processer.addBeforeRule(new SetAuditInfoRule());

		// �����к�
		processer.addBeforeRule(new RowNODealRule());

	}
}
