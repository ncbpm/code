package nc.bpm.bd.pfxx;

import java.util.Collection;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.sec.esapi.NCESAPI;
import nc.itf.bd.bankacc.base.IBankAccBaseInfoQueryService;
import nc.itf.bd.bankacc.cust.ICustBankaccService;
import nc.itf.uap.IUAPQueryBS;
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

import org.apache.commons.collections.CollectionUtils;
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
		//�����˺Ų�ѯ�Ƿ����
		BankAccbasVO queryBankaccInfor = queryBankaccInfor(unitvo);
		if(queryBankaccInfor !=null){
			resvo.setPk_bankaccbas(queryBankaccInfor.getPk_bankaccbas());
		}
		ICustBankaccService service = NCLocator.getInstance().lookup(
				ICustBankaccService.class);
		CustBankaccUnionVO bankvo = null;
		String vopk = resvo.getPk_bankaccbas();
		if (queryBankaccInfor ==null) {
			setVOStatus(swapContext, resvo, VOStatus.NEW);
			bankvo = service.insertCustBankacc(unitvo);
			vopk = bankvo.getPrimaryKey();
			//������
			resvo.setEnablestate(2);
//			resvo.setEnableuser(enableuser);
			service.enableCustBankacc(unitvo);
			
		} else {
			// enablestate ����״̬ enablestate int ����״̬ 1=δ���ã�2=�����ã�3=��ͣ�ã�
			if (resvo.getEnablestate() == 3  ) {
				if(queryBankaccInfor.getEnablestate() !=3){
					service.disableCustBankacc(unitvo);
				}
				return "��ͣ��";
			}
			//������
			if (resvo.getEnablestate() == 2) {
				if( queryBankaccInfor.getEnablestate() !=2){
					service.enableCustBankacc(unitvo);
				}
			}
			setVOStatus(swapContext, resvo, VOStatus.UPDATED);
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
		BankAccSubVO[] subs = resvo.getBankaccsub();
		if(subs == null || subs.length ==0){
			throw new BusinessException("�����˻����˲���Ϊ��");

		}
		BaseDAO dao = new BaseDAO();
		SupplierVO vo = (SupplierVO) dao
				.retrieveByPK(SupplierVO.class, pk_cust);
		if (vo == null) {
			throw new BusinessException("��������:" + pk_cust + "δ��ѯ����Ӧ�Ĺ�Ӧ�̡�");
		}

	}

	private CustBankaccUnionVO getCustBankaccUnionVO(BankAccbasVO bankvo)
			throws BusinessException {
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
			for (BankAccSubVO vo : subs) {
				for (BankAccSubVO osubs : oldSubs) {
					if (vo.getPk_currtype().equalsIgnoreCase(
							osubs.getPk_currtype())) {
						vo.setPk_bankaccsub(osubs.getPk_bankaccsub());
						vo.setPk_bankaccbas(osubs.getPk_bankaccbas());
						vo.setStatus(VOStatus.UPDATED);
					}
				}
			}
		}

	}

	/**
	 * 
	 * �����������˻�����+������𣬲�����������Ҫ�Ȳ�ѯһ��
	 * 
	 * @param unionVO
	 * @throws BusinessException
	 */
	private BankAccbasVO queryBankaccInfor(CustBankaccUnionVO unionVO)
			throws BusinessException {
		BankAccbasVO bankAccbasVO = unionVO.getBankaccbasVO();
		String accnum = bankAccbasVO.getAccnum();
		String pk_banktype = bankAccbasVO.getPk_banktype();
		int accclass = 3;
		if (accnum == null) {
			throw new BusinessException("accnum����Ϊ��,����");
		}
		if (pk_banktype == null) {
			throw new BusinessException("pk_banktype����Ϊ��,����");
		}

		// �ڵ�ǰ�������˺�Ψһ
		String where = " accnum = '" + accnum + "' and pk_banktype = '"
				+ pk_banktype + "' and accclass = " + accclass + " ";

		Collection<BankAccbasVO> col = getQuerySer().retrieveByClause(
				BankAccbasVO.class, where);
		if (CollectionUtils.isEmpty(col)) {
			return null;
		}
		return col.toArray(new BankAccbasVO[0])[0];
		// ��������˻���Ӧ�Ĺ�Ӧ��

	}

	// �����˻��ڿͻ����߹�Ӧ�̷�Χ��Ψһ
	private boolean isCustBankAccUnique(CustBankaccUnionVO unionVO) {
		BankAccbasVO bankAccbasVO = unionVO.getBankaccbasVO();
		String accnum = bankAccbasVO.getAccnum();
		String pk_banktype = bankAccbasVO.getPk_banktype();
		int accclass = bankAccbasVO.getAccclass().intValue();
		String pk_bankaccbas = bankAccbasVO.getPrimaryKey();
		if (accnum != null && pk_banktype != null) {
			// �ڵ�ǰ�������˺�Ψһ
			String where = " accnum = '" + accnum + "' and pk_banktype = '"
					+ pk_banktype + "' and accclass = " + accclass + " ";
			if (pk_bankaccbas != null) {
				where += " and pk_bankaccbas <>'" + pk_bankaccbas + "'";
			}
			try {
				Collection<BankAccbasVO> col = getQuerySer().retrieveByClause(
						BankAccbasVO.class, where);
				if (CollectionUtils.isNotEmpty(col)) {
					return false;
				}
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private boolean isAccountUnique(CustBankaccUnionVO unionVO) {
		BankAccbasVO bankAccbasVO = unionVO.getBankaccbasVO();
		CustbankVO custbankVO = unionVO.getCustbankVO();
		String pk_cust = NCESAPI.sqlEncode(custbankVO.getPk_cust());
		String accnum = NCESAPI.sqlEncode(bankAccbasVO.getAccnum());
		String pk_banktype = NCESAPI.sqlEncode(bankAccbasVO.getPk_banktype());
		String pk_bankaccbas = NCESAPI.sqlEncode(bankAccbasVO.getPrimaryKey());
		int accclass = bankAccbasVO.getAccclass().intValue();
		if (accnum != null && pk_banktype != null) {
			// �ڵ�ǰ�������˺�Ψһ
			String where = " accnum = '"
					+ accnum
					+ "' and pk_banktype = '"
					+ pk_banktype
					+ "' and accclass = "
					+ accclass
					+ " and pk_bankaccbas in ( select distinct pk_bankaccbas from bd_custbank where pk_cust = '"
					+ pk_cust + "' )";
			if (pk_bankaccbas != null) {
				where += " and pk_bankaccbas <>'" + pk_bankaccbas + "'";
			}
			try {
				Collection<BankAccbasVO> col = getQuerySer().retrieveByClause(
						BankAccbasVO.class, where);
				if (CollectionUtils.isNotEmpty(col)) {
					return false;
				}
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return true;
	}

	private IUAPQueryBS getQuerySer() {
		return (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
	}

}
