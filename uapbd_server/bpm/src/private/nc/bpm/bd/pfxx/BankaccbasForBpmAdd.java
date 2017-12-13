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
 * 供应商银行账户增加
 * 
 * @author Administrator
 * 
 */
public class BankaccbasForBpmAdd extends AbstractPfxxPlugin {
	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggxsysvo) throws BusinessException {
		BankAccbasVO resvo = (BankAccbasVO) vo;
		// 校验导入数据的合法性
		checkData(resvo);
		// 3.补全数据,并且调整单据状态
		fillData(resvo);
		// 组装合并VO
		CustBankaccUnionVO unitvo = getCustBankaccUnionVO(resvo);
		//根据账号查询是否存在
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
			//先启用
			resvo.setEnablestate(2);
//			resvo.setEnableuser(enableuser);
			service.enableCustBankacc(unitvo);
			
		} else {
			// enablestate 启用状态 enablestate int 启用状态 1=未启用，2=已启用，3=已停用，
			if (resvo.getEnablestate() == 3  ) {
				if(queryBankaccInfor.getEnablestate() !=3){
					service.disableCustBankacc(unitvo);
				}
				return "已停用";
			}
			//先启用
			if (resvo.getEnablestate() == 2) {
				if( queryBankaccInfor.getEnablestate() !=2){
					service.enableCustBankacc(unitvo);
				}
			}
			setVOStatus(swapContext, resvo, VOStatus.UPDATED);
			bankvo = service.updateCustBankacc(unitvo);
		}
		// 更新与档案的关联关系
		return vopk;
	}

	private void fillData(BankAccbasVO resvo) {
		// TODO 自动生成的方法存根
		resvo.setAccclass(IBankAccConstant.ACCCLASS_SUPPLIER);
	}

	private void checkData(BankAccbasVO resvo) throws BusinessException {
		// TODO 自动生成的方法存根
		String pk_cust = resvo.getMemo();
		if (StringUtils.isEmpty(pk_cust)) {
			throw new BusinessException("memo字段必须必须指定对应档案主键.");
		}
		BankAccSubVO[] subs = resvo.getBankaccsub();
		if(subs == null || subs.length ==0){
			throw new BusinessException("银行账户子账不能为空");

		}
		BaseDAO dao = new BaseDAO();
		SupplierVO vo = (SupplierVO) dao
				.retrieveByPK(SupplierVO.class, pk_cust);
		if (vo == null) {
			throw new BusinessException("根据主键:" + pk_cust + "未查询到对应的供应商。");
		}

	}

	private CustBankaccUnionVO getCustBankaccUnionVO(BankAccbasVO bankvo)
			throws BusinessException {
		// TODO 自动生成的方法存根
		CustBankaccUnionVO unitvo = new CustBankaccUnionVO();
		unitvo.setBankaccbasVO(bankvo);
		CustbankVO custBankVO = getCustBankVO(bankvo, bankvo.getMemo());
		unitvo.setCustbankVO(custBankVO);
		return unitvo;
	}

	private CustbankVO getCustBankVO(BankAccbasVO bankvo, String pk_cust) {
		// TODO 自动生成的方法存根
		CustbankVO cbvo = new CustbankVO();
		// accclass 账户分类 accclass int 账户分类 0=个人，1=客户，2=公司，3=供应商，
		cbvo.setAccclass(bankvo.getAccclass());
		// 2 dataoriginflag 分布式 dataoriginflag int 数据来源
		// 0=本级产生，1=上级下发，2=下级上报，3=本级产生已上报下发，-1=系统预置，

		// 3 isdefault 是否默认 isdefault char(1) UFBoolean
		cbvo.setIsdefault(UFBoolean.FALSE);
		// 4 pk_bankaccbas 银行账户主键 pk_bankaccbas varchar(20) 客商银行账户信息 银行账户
		// cbvo.setPk_bankaccbas(subvo.getPk_bankaccbas());
		// 5 pk_bankaccsub 银行账户子户主键 pk_bankaccsub varchar(20) 银行账户子户 银行账户子户
		// cbvo.setPk_bankaccsub(subvo.getPk_bankaccsub());
		// 6 pk_cust 客商主键 pk_cust varchar(20) 客商 客商档案
		cbvo.setPk_cust(pk_cust);
		// 7 pk_custbank 客商银行账户主键 pk_custbank char(20) UFID
		// 8 pk_oldcustbank 原客商银行主键 pk_oldcustbank varchar(20) String
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
				throw new BusinessException("根据主键:" + resvo.getPk_bankaccbas()
						+ "未查询银行账户信息");
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
	 * 传过来银行账户编码+银行类别，不传主键，需要先查询一遍
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
			throw new BusinessException("accnum不能为空,请检查");
		}
		if (pk_banktype == null) {
			throw new BusinessException("pk_banktype不能为空,请检查");
		}

		// 在当前客商下账号唯一
		String where = " accnum = '" + accnum + "' and pk_banktype = '"
				+ pk_banktype + "' and accclass = " + accclass + " ";

		Collection<BankAccbasVO> col = getQuerySer().retrieveByClause(
				BankAccbasVO.class, where);
		if (CollectionUtils.isEmpty(col)) {
			return null;
		}
		return col.toArray(new BankAccbasVO[0])[0];
		// 检查银行账户对应的供应商

	}

	// 银行账户在客户或者供应商范围内唯一
	private boolean isCustBankAccUnique(CustBankaccUnionVO unionVO) {
		BankAccbasVO bankAccbasVO = unionVO.getBankaccbasVO();
		String accnum = bankAccbasVO.getAccnum();
		String pk_banktype = bankAccbasVO.getPk_banktype();
		int accclass = bankAccbasVO.getAccclass().intValue();
		String pk_bankaccbas = bankAccbasVO.getPrimaryKey();
		if (accnum != null && pk_banktype != null) {
			// 在当前客商下账号唯一
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
			// 在当前客商下账号唯一
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
