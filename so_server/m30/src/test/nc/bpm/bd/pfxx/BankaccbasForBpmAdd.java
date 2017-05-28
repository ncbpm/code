package nc.bpm.bd.pfxx;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.itf.bd.bankacc.base.IBankAccBaseInfoQueryService;
import nc.itf.bd.bankacc.cust.ICustBankaccService;
import nc.vo.bd.bankaccount.BankAccSubVO;
import nc.vo.bd.bankaccount.BankAccbasVO;
import nc.vo.bd.bankaccount.IBankAccConstant;
import nc.vo.bd.bankaccount.cust.CustBankaccUnionVO;
import nc.vo.bd.cust.CustbankVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;

import org.apache.commons.lang.StringUtils;

/**
 * ��Ӧ�������˻�����
 * 
 * @author Administrator
 * 
 */
public class BankaccbasForBpmAdd extends AbstractPfxxPlugin {
	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggxsysvo) throws BusinessException {
		BankAccbasVO resvo = (BankAccbasVO) vo;
		// У�鵼�����ݵĺϷ���
		checkData(resvo);
		// 3.��ȫ����,���ҵ�������״̬
		fillData(resvo);
		// ��װ�ϲ�VO
		CustBankaccUnionVO unitvo = getCustBankaccUnionVO(resvo);
		ICustBankaccService service = NCLocator.getInstance().lookup(
				ICustBankaccService.class);
		CustBankaccUnionVO bankvo = null;
		String vopk = resvo.getPk_bankaccbas();
		if (StringUtils.isEmpty(vopk)) {
			setVOStatus(swapContext, resvo, VOStatus.NEW);
			bankvo = service.insertCustBankacc(unitvo);

			vopk = bankvo.getPrimaryKey();

		} else {
			setVOStatus(swapContext, resvo, VOStatus.UPDATED);
			resvo.setPrimaryKey(vopk);
			bankvo = service.updateCustBankacc(unitvo);
		}
		// �����뵵���Ĺ�����ϵ
		return vopk;
	}

	private void fillData(BankAccbasVO resvo) {
		// TODO �Զ����ɵķ������
		resvo.setAccclass(IBankAccConstant.ACCCLASS_SUPPLIER);
	}

	private void checkData(BankAccbasVO resvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		String pk_cust = resvo.getMemo();
		if (StringUtils.isEmpty(pk_cust)) {
			throw new BusinessException("memo�ֶα������ָ����Ӧ��������.");
		}
		BaseDAO dao = new BaseDAO();
		SupplierVO vo = (SupplierVO) dao
				.retrieveByPK(SupplierVO.class, pk_cust);
		if (vo == null) {
			throw new BusinessException("��������:" + pk_cust + "δ��ѯ����Ӧ�Ĺ�Ӧ�̡�");
		}

	}

	private CustBankaccUnionVO getCustBankaccUnionVO(BankAccbasVO bankvo) {
		// TODO �Զ����ɵķ������
		CustBankaccUnionVO unitvo = new CustBankaccUnionVO();
		unitvo.setBankaccbasVO(bankvo);
		CustbankVO custBankVO = getCustBankVO(bankvo, bankvo.getMemo());
		unitvo.setCustbankVO(custBankVO);
		return unitvo;
	}

	private CustbankVO getCustBankVO(BankAccbasVO bankvo, String pk_cust) {
		// TODO �Զ����ɵķ������
		CustbankVO cbvo = new CustbankVO();
		// accclass �˻����� accclass int �˻����� 0=���ˣ�1=�ͻ���2=��˾��3=��Ӧ�̣�
		cbvo.setAccclass(bankvo.getAccclass());
		// 2 dataoriginflag �ֲ�ʽ dataoriginflag int ������Դ
		// 0=����������1=�ϼ��·���2=�¼��ϱ���3=�����������ϱ��·���-1=ϵͳԤ�ã�

		// 3 isdefault �Ƿ�Ĭ�� isdefault char(1) UFBoolean
		cbvo.setIsdefault(UFBoolean.FALSE);
		// 4 pk_bankaccbas �����˻����� pk_bankaccbas varchar(20) ���������˻���Ϣ �����˻�
		// cbvo.setPk_bankaccbas(subvo.getPk_bankaccbas());
		// 5 pk_bankaccsub �����˻��ӻ����� pk_bankaccsub varchar(20) �����˻��ӻ� �����˻��ӻ�
		// cbvo.setPk_bankaccsub(subvo.getPk_bankaccsub());
		// 6 pk_cust �������� pk_cust varchar(20) ���� ���̵���
		cbvo.setPk_cust(pk_cust);
		// 7 pk_custbank ���������˻����� pk_custbank char(20) UFID
		// 8 pk_oldcustbank ԭ������������ pk_oldcustbank varchar(20) String
		return cbvo;

	}

	private void setVOStatus(ISwapContext swapContext, BankAccbasVO resvo,
			int status) throws BusinessException {
		resvo.setStatus(status);
		BankAccSubVO[] subs = resvo.getBankaccsub();
		for (int i = 0; subs != null && i < subs.length; i++) {
			subs[i].setStatus(VOStatus.NEW);
		}
		if (VOStatus.NEW == status) {
			

		} else {
			IBankAccBaseInfoQueryService query = NCLocator.getInstance()
					.lookup(IBankAccBaseInfoQueryService.class);
			BankAccbasVO[] vos = query.queryBankAccBasVOByPks(
					new String[] { resvo.getPk_bankaccbas() }, false);
			if (vos == null || vos.length == 0) {
				throw new BusinessException("��������:" + resvo.getPk_bankaccbas()
						+ "δ��ѯ�����˻���Ϣ");
			}
			BankAccSubVO[] oldSubs = vos[0].getBankaccsub();
			for(BankAccSubVO vo:subs){
				for(BankAccSubVO osubs:oldSubs){
					if(vo.getPk_currtype().equalsIgnoreCase(osubs.getPk_currtype())){
						vo.setPk_bankaccsub(osubs.getPk_bankaccsub());
						vo.setPk_bankaccbas(osubs.getPk_bankaccbas());
						vo.setStatus(VOStatus.UPDATED);
					}
				}
			}
		}

	}

	/**
	 * �жϴ���������Ƿ���ȷ
	 * 
	 * @param pk_cust
	 * @param accclass
	 * @return
	 * @throws BusinessException
	 */
	private boolean isCustSupplier(String pk_cust, int accclass)
			throws BusinessException {
		boolean iscustsup = false;

		return iscustsup;
	}

}