package nc.bs.gl.pfxx;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.gl.pubinterface.IVoucherDelete;
import nc.bs.gl.pubinterface.IVoucherSave;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.uap.lock.PKLock;
import nc.gl.glconst.systemtype.SystemtypeConst;
import nc.itf.gl.voucher.IVoucher;
import nc.itf.gl.voucher.IVoucherList;
import nc.itf.glcom.para.IGlPara;
import nc.pubitf.accperiod.AccountCalendar;
import nc.pubitf.rbac.IFunctionPermissionPubService;
import nc.vo.gateway60.accountbook.AccountBookUtil;
import nc.vo.gateway60.itfs.Currency;
import nc.vo.gateway60.pub.GlBusinessException;
import nc.vo.gl.pubinterface.VoucherOperateInterfaceVO;
import nc.vo.gl.pubinterface.VoucherSaveInterfaceVO;
import nc.vo.gl.pubvoucher.DetailVO;
import nc.vo.gl.pubvoucher.OperationResultVO;
import nc.vo.gl.pubvoucher.VoucherVO;
import nc.vo.glcom.exception.GLBusinessException;
import nc.vo.glcom.nodecode.GlNodeConst;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.idcontrast.IDContrastVO;
import nc.vo.pfxx.pub.PfxxConstants;
import nc.vo.pfxx.util.PfxxUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class BpmVoucherPlugin extends AbstractPfxxPlugin implements IVoucherSave, IVoucherDelete {

	/**
	 * 凭证处理
	 *
	 * @param con
	 */
	@Override
	protected Object processBill(Object vo, ISwapContext con, AggxsysregisterVO xsysvo) throws BusinessException {
		// 1.得到转换后的VO数据,取决于向导第一步注册的VO信息
		// 2.查询此单据是否已经被导入过，有两个方法，具体使用哪一个请参考方法说明javadoc
		// 3. 如果单据设置有辅助信息，aggxsysvo为用户配置的具体辅助信息
		// 4.如果此单据没有导入过，那么准备保存新单据，保存单据前请进行必要的数据检查，并给出明确的业务异常...
		
		int voucherNum = getVoucherNum(con);
		String rtStr = null;
		if (vo != null && vo.getClass().isArray()) {
			VoucherVO[] voucherArr = (VoucherVO[]) vo;
			for (int i = 0; i < voucherArr.length; i++) {
				if(voucherNum == 0) {
					voucherNum = i+1;
				}
				doSaveVoucher(voucherArr[i], con, xsysvo,voucherNum);
			}
		} else if (vo != null) {
			VoucherVO vou = (VoucherVO) vo;
			if(voucherNum == 0) {
				voucherNum = 1;
			}
			Object doSaveVoucher =  doSaveVoucher(vou, con, xsysvo,voucherNum);
			if(doSaveVoucher != null && doSaveVoucher.getClass().isArray()) {
				Object[] objs = (Object[]) doSaveVoucher;
				if(objs.length >0 && objs[0] instanceof OperationResultVO) {
					OperationResultVO resultVo = (OperationResultVO) objs[0];
					Object m_userIdentical = resultVo.m_userIdentical;
					if(m_userIdentical != null) {
						VoucherVO rtVo = (VoucherVO) m_userIdentical;
						rtStr = String.valueOf(rtVo.getNo());
					}
				}
			}
		}
		return null;
	}
	
	private int getVoucherNum(ISwapContext con) {
		int voucherNum = 0;
		if (con != null) {
			try {
				String billtype = con.getBilltype();
				String docID = con.getDocID();
				int indexOf = docID.indexOf(billtype);

				String subStr = docID.substring(indexOf + billtype.length(),
						docID.length());
				voucherNum = Integer.valueOf(subStr) + 1;
			} catch (Exception e) {
				Logger.error(e);
			}
		}
		
		return voucherNum;
	}

	private Object doSaveVoucher(VoucherVO vo, ISwapContext con, AggxsysregisterVO xsysvo,int voucherNum) throws GLBusinessException {
		String returnvoucherid = "00000000";
			VoucherVO vouchervo = null;
			changePeriod(vo);
			try {
				// 检查效验凭证
				checkValdata(vo);
				//集团效验
				if (vo.getPk_group() == null) {
					vo.setPk_group(con.getPk_group());
				}
				//加上动态锁，判断存在时不能并发。
				if(PKLock.getInstance().addDynamicLock(con.getDocID()+ con.getBilltype())){
					isExists(vo, con);
				}else{
					throw new GLBusinessException(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("20021005","UPT2002100573-900071")/*系统繁忙，不能并发导入相同凭证！*/);
				}
				// 编码翻译为pk
				GLPluginUtils.defaultDataSet(vo, false,voucherNum);
				isPermiss(vo, con);
				vouchervo = GLPluginUtils.setVoucherDefault(vo, xsysvo, con);
			} catch (Exception e) {
				Logger.error("凭证保存前数据效验报错:"+e.getMessage(), e);
				throw new GLBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0212")/*@res "凭证保存前数据效验报错:"*/+e.getMessage(), e);
			}

			
			/**
			 * 作废
			 */
			try {
				IVoucher voucherbo = (IVoucher) NCLocator.getInstance().lookup(IVoucher.class.getName());
				if (vouchervo.getDiscardflag().booleanValue()) {
					vouchervo.setDiscardflag(UFBoolean.FALSE);
					voucherbo.save(vouchervo, Boolean.TRUE);
					IVoucherList voucherlistbo = (IVoucherList) NCLocator.getInstance().lookup(IVoucherList.class.getName());
					voucherlistbo.abandonVoucherByPk(vouchervo.getPk_voucher(), vouchervo.getPk_prepared(), Boolean.TRUE);
				} else {
					voucherbo.save(vouchervo, Boolean.TRUE);
				}
			if (vouchervo.getStatus() == VOStatus.DELETED) {
				// 临时注释，发版时需要取消注释
				Logger.debug("执行凭证删除方法!");
			} else {
				Logger.debug("proc = " + vouchervo.getStatus());
			}
		} catch (Exception e) {
			Logger.error("凭证保存中报错:"+e.getMessage(), e);
			throw new GLBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0213")/*@res "凭证保存中报错:"*/+e.getMessage() ,e);
		}
		return returnvoucherid;
	}

	private void changePeriod(VoucherVO vo) throws GlBusinessException {
		String defaultpk_accperiodscheme = AccountBookUtil.getAccPeriodSchemePKByAccountingbookPk(vo.getPk_accountingbook());
		AccountCalendar caldar = AccountCalendar.getInstanceByPeriodScheme(defaultpk_accperiodscheme);
		try {
			caldar.setDate(vo.getPrepareddate());
			String yearmth = caldar.getMonthVO().getYearmth();
			String[] year_mth = yearmth.split("-");
			vo.setYear(year_mth[0]);
			vo.setPeriod(year_mth[1]);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new GlBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0216")/*@res "根据制单日期取会计期间中出错！"*/,e);
		}
		
	}

	@SuppressWarnings("unchecked")
	private void isExists(VoucherVO vo, ISwapContext con) throws DAOException,
			BusinessException {
		if (con.getDocID()!=null) {
			List<IDContrastVO> idc = (List<IDContrastVO>) new BaseDAO().retrieveByClause(IDContrastVO.class, "file_id='"+con.getDocID()+"' and bill_type='" + con.getBilltype() + "'");
			if(!idc.isEmpty()&&idc.size()>0){
				String pk_vouhcer = idc.get(0).getPk_bill();
				vo.setPk_voucher(pk_vouhcer);
				if(PfxxConstants.N.equals(con.getReplace())) {
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0214")/*@res "已存在该凭证"*/);
				}
			}
		}
	}

	private void isPermiss(VoucherVO vo, ISwapContext con) throws BusinessException {
		String userid = vo.getPk_prepared();
		// 效验权限问题
		if(SystemtypeConst.NC_USER.equals(userid)) {
			return ;
		}
		String[] orgs = NCLocator.getInstance().lookup(IFunctionPermissionPubService.class).getUserPermissionPkOrgs(userid,
				GlNodeConst.GLNODE_VOUCHERPREPARE, con.getPk_group());
		if (orgs != null) {
			boolean ispermiss = false;
			for (int i = 0; i < orgs.length; i++) {
				if (vo.getPk_accountingbook().equals(orgs[i])) {
					ispermiss = true;
					break;
				}
			}
			if (!ispermiss) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0201")/*@res "当前用户没有该账簿权限"*/);
			}

		}
	}

	private void checkValdata(VoucherVO vo) throws BusinessException {
		if (vo.getPk_prepared()==null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0215")/*@res "制单人不能为空"*/);
		}
		// 制单人和审核人不可以为同一人
		if (vo.getPk_prepared().equals(vo.getPk_checked())) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0202")/*@res "制单人和审核人不能为同一人"*/);
		}
		String localCurrency = Currency.getLocalCurrPK(vo.getPk_accountingbook());
		for(int i=0;i<vo.getDetail().size();i++) {
			DetailVO detailVo = (DetailVO)vo.getDetail().get(i);
			String pk_currtype = detailVo.getPk_currtype();
			//如果原币与组织本币相同，则校验金额是否相同
			if(pk_currtype.equals(localCurrency)) {
				
				UFDouble value = detailVo.getLocalcreditamount();
				if(detailVo.getLocalcreditamount() == null || detailVo.getLocalcreditamount().equals(UFDouble.ZERO_DBL)) {
					value = detailVo.getLocaldebitamount();
				}
				UFDouble value2 = detailVo.getCreditamount(); 
				if(detailVo.getCreditamount() == null || detailVo.getCreditamount().equals(UFDouble.ZERO_DBL)) {
					value2 = detailVo.getDebitamount();
				}
				
				boolean isEqual = isEquale(value2,value);
				if(!isEqual) {
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("glpub_0","02002003-0286"
							,null,new String[]{String.valueOf(i+1)})/*@res "第{0}条分录原币与本币金额不相同"*/);
				}
			}
		}
	}
		
	private boolean isEquale(UFDouble value1,UFDouble value2) {
		
		if(value1 == null) {
			value1 = UFDouble.ZERO_DBL;
		}
		
		if(value2 == null) {
			value2 = UFDouble.ZERO_DBL;
		}
		
		return value1.equals(value2);
	}	

	public OperationResultVO[] afterDelete(VoucherOperateInterfaceVO vo) throws BusinessException {
		return null;
	}

	/**
	 * 保存凭证之后需要插入流水号和凭证主键之间的对应关系。
	 */
	public OperationResultVO[] afterSave(VoucherSaveInterfaceVO vo) throws BusinessException {
		IDContrastVO ufvo = null;
		try {
			Logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> 进入afterSave方法...");
			// 如果是从外部交换平台进入
			if (vo.voucher.getUserData() != null && vo.voucher.getUserData() instanceof IDContrastVO) {
				Logger.debug(">>>>>>>>>>>>>>>>>>>>>> afterSave 从外部交换平台进入add & edit...");
				ufvo = (IDContrastVO) vo.voucher.getUserData();
					Integer voucherkind = vo.voucher.getVoucherkind();
					if (voucherkind != null && voucherkind.intValue() == 2) {
						Logger.debug(">>>>>>>>>>>>>>>>>>>>>>> 期初凭证不增加线索号和凭证主键的对应关系...");
					} else {
						// 插入ID vs PK的对照关系
						ufvo.setPk_bill(vo.voucher.getPk_voucher());
						ufvo.setStatus(VOStatus.NEW);
						PfxxUtils.lookUpPFxxEJBService().insertIDvsPK(ufvo);
					}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("pfxx", "UPPpfxx-000211")/* @res "插入单据线索号和凭证主键对照关系时出错!" */);
		}
		return null;
	}


	@Override
	public OperationResultVO[] beforeSave(VoucherSaveInterfaceVO vo) throws BusinessException {
		return null;
	}



	@SuppressWarnings("unchecked")
	@Override
	public OperationResultVO[] beforeDelete(VoucherOperateInterfaceVO vo) throws BusinessException {
		List<OperationResultVO> resultList = new ArrayList<OperationResultVO>();
		try {
			// 如果是从总帐进入要删除外部交换平台导入的凭证
				if (vo.pk_vouchers != null) {
				Logger.debug(">>>>>>>>>>>>>>>>>>> 从总帐进入要删除外部交换平台导入的凭证...");

				IGlPara glparabo = (IGlPara) NCLocator.getInstance().lookup(IGlPara.class.getName());
				for (int i = 0; i < (vo.pk_vouchers == null ? 0 : vo.pk_vouchers.length); i++) {
					/** 执行相应的方法 获取总账的强制删除参数 */
					VoucherVO vouchervo = GLPluginUtils.findVoucherVOByVoucherPK(vo.pk_vouchers[i]);
					List<IDContrastVO> con = (List<IDContrastVO>) new BaseDAO().retrieveByClause(IDContrastVO.class, "pk_bill='"+vo.pk_vouchers[i]+"'");
					UFBoolean parval = glparabo.isDelInputVoucherOut(vouchervo.getPk_accountingbook());
					if (parval==null) {
						throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20021010","UPP20021005-000711")/*@res "参数设置有错"*/);
					}
					boolean isPowerDelVoucher = parval.booleanValue(); // 获取凭证所在公司定义的强制删除参数
					//辅助配制的是否删除
					if (!con.isEmpty()) {
						for (int j = 0; j < con.size(); j++) {
							if(!con.get(j).getIsdelete().booleanValue()){
								OperationResultVO rs = new OperationResultVO();
								rs.m_intSuccess = 2;
								rs.m_strDescription = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0203")/*@res "不允许强制删除外系统导入的凭证"*/;
								resultList.add(rs);
							}else if(isPowerDelVoucher){
								PfxxUtils.lookUpPFxxEJBService().deleteIDvsPKByDocPK(vo.pk_vouchers[i]);
							}
						}
						if (!isPowerDelVoucher) { // 不能强制删除外部传入的凭证
							Logger.debug("不允许强制删除外系统导入的凭证");
							OperationResultVO rs = new OperationResultVO();
							rs.m_intSuccess = 2;
							rs.m_strDescription = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0226")/*@res "外部系统导入凭证，不允许强制删除该凭证！"*/;
							resultList.add(rs);
						} 
					}
//					else { // 强制删除外部导入的凭证
//						Logger.debug("允许强制删除外系统导入的凭证");
//						// 删除ID vs PK对照表记录
//						Logger.debug(">>>>>>>>>>>>>>>>>>>>>>>> 外部交换平台删除了凭证主键" + vo.pk_vouchers[i] + "的对应关系");
//						// 通过
//					}
				}
				// 从总账进入要删除总账生成的凭证
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e + "");
		}
		return resultList.toArray(new OperationResultVO[0]);
	}

}