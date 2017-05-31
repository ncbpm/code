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
	 * ƾ֤����
	 *
	 * @param con
	 */
	@Override
	protected Object processBill(Object vo, ISwapContext con, AggxsysregisterVO xsysvo) throws BusinessException {
		// 1.�õ�ת�����VO����,ȡ�����򵼵�һ��ע���VO��Ϣ
		// 2.��ѯ�˵����Ƿ��Ѿ��������������������������ʹ����һ����ο�����˵��javadoc
		// 3. ������������и�����Ϣ��aggxsysvoΪ�û����õľ��帨����Ϣ
		// 4.����˵���û�е��������ô׼�������µ��ݣ����浥��ǰ����б�Ҫ�����ݼ�飬��������ȷ��ҵ���쳣...
		
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
				// ���Ч��ƾ֤
				checkValdata(vo);
				//����Ч��
				if (vo.getPk_group() == null) {
					vo.setPk_group(con.getPk_group());
				}
				//���϶�̬�����жϴ���ʱ���ܲ�����
				if(PKLock.getInstance().addDynamicLock(con.getDocID()+ con.getBilltype())){
					isExists(vo, con);
				}else{
					throw new GLBusinessException(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("20021005","UPT2002100573-900071")/*ϵͳ��æ�����ܲ���������ͬƾ֤��*/);
				}
				// ���뷭��Ϊpk
				GLPluginUtils.defaultDataSet(vo, false,voucherNum);
				isPermiss(vo, con);
				vouchervo = GLPluginUtils.setVoucherDefault(vo, xsysvo, con);
			} catch (Exception e) {
				Logger.error("ƾ֤����ǰ����Ч�鱨��:"+e.getMessage(), e);
				throw new GLBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0212")/*@res "ƾ֤����ǰ����Ч�鱨��:"*/+e.getMessage(), e);
			}

			
			/**
			 * ����
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
				// ��ʱע�ͣ�����ʱ��Ҫȡ��ע��
				Logger.debug("ִ��ƾ֤ɾ������!");
			} else {
				Logger.debug("proc = " + vouchervo.getStatus());
			}
		} catch (Exception e) {
			Logger.error("ƾ֤�����б���:"+e.getMessage(), e);
			throw new GLBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0213")/*@res "ƾ֤�����б���:"*/+e.getMessage() ,e);
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
			throw new GlBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0216")/*@res "�����Ƶ�����ȡ����ڼ��г���"*/,e);
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
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0214")/*@res "�Ѵ��ڸ�ƾ֤"*/);
				}
			}
		}
	}

	private void isPermiss(VoucherVO vo, ISwapContext con) throws BusinessException {
		String userid = vo.getPk_prepared();
		// Ч��Ȩ������
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
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0201")/*@res "��ǰ�û�û�и��˲�Ȩ��"*/);
			}

		}
	}

	private void checkValdata(VoucherVO vo) throws BusinessException {
		if (vo.getPk_prepared()==null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0215")/*@res "�Ƶ��˲���Ϊ��"*/);
		}
		// �Ƶ��˺�����˲�����Ϊͬһ��
		if (vo.getPk_prepared().equals(vo.getPk_checked())) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0202")/*@res "�Ƶ��˺�����˲���Ϊͬһ��"*/);
		}
		String localCurrency = Currency.getLocalCurrPK(vo.getPk_accountingbook());
		for(int i=0;i<vo.getDetail().size();i++) {
			DetailVO detailVo = (DetailVO)vo.getDetail().get(i);
			String pk_currtype = detailVo.getPk_currtype();
			//���ԭ������֯������ͬ����У�����Ƿ���ͬ
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
							,null,new String[]{String.valueOf(i+1)})/*@res "��{0}����¼ԭ���뱾�ҽ���ͬ"*/);
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
	 * ����ƾ֤֮����Ҫ������ˮ�ź�ƾ֤����֮��Ķ�Ӧ��ϵ��
	 */
	public OperationResultVO[] afterSave(VoucherSaveInterfaceVO vo) throws BusinessException {
		IDContrastVO ufvo = null;
		try {
			Logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> ����afterSave����...");
			// ����Ǵ��ⲿ����ƽ̨����
			if (vo.voucher.getUserData() != null && vo.voucher.getUserData() instanceof IDContrastVO) {
				Logger.debug(">>>>>>>>>>>>>>>>>>>>>> afterSave ���ⲿ����ƽ̨����add & edit...");
				ufvo = (IDContrastVO) vo.voucher.getUserData();
					Integer voucherkind = vo.voucher.getVoucherkind();
					if (voucherkind != null && voucherkind.intValue() == 2) {
						Logger.debug(">>>>>>>>>>>>>>>>>>>>>>> �ڳ�ƾ֤�����������ź�ƾ֤�����Ķ�Ӧ��ϵ...");
					} else {
						// ����ID vs PK�Ķ��չ�ϵ
						ufvo.setPk_bill(vo.voucher.getPk_voucher());
						ufvo.setStatus(VOStatus.NEW);
						PfxxUtils.lookUpPFxxEJBService().insertIDvsPK(ufvo);
					}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("pfxx", "UPPpfxx-000211")/* @res "���뵥�������ź�ƾ֤�������չ�ϵʱ����!" */);
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
			// ����Ǵ����ʽ���Ҫɾ���ⲿ����ƽ̨�����ƾ֤
				if (vo.pk_vouchers != null) {
				Logger.debug(">>>>>>>>>>>>>>>>>>> �����ʽ���Ҫɾ���ⲿ����ƽ̨�����ƾ֤...");

				IGlPara glparabo = (IGlPara) NCLocator.getInstance().lookup(IGlPara.class.getName());
				for (int i = 0; i < (vo.pk_vouchers == null ? 0 : vo.pk_vouchers.length); i++) {
					/** ִ����Ӧ�ķ��� ��ȡ���˵�ǿ��ɾ������ */
					VoucherVO vouchervo = GLPluginUtils.findVoucherVOByVoucherPK(vo.pk_vouchers[i]);
					List<IDContrastVO> con = (List<IDContrastVO>) new BaseDAO().retrieveByClause(IDContrastVO.class, "pk_bill='"+vo.pk_vouchers[i]+"'");
					UFBoolean parval = glparabo.isDelInputVoucherOut(vouchervo.getPk_accountingbook());
					if (parval==null) {
						throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20021010","UPP20021005-000711")/*@res "���������д�"*/);
					}
					boolean isPowerDelVoucher = parval.booleanValue(); // ��ȡƾ֤���ڹ�˾�����ǿ��ɾ������
					//�������Ƶ��Ƿ�ɾ��
					if (!con.isEmpty()) {
						for (int j = 0; j < con.size(); j++) {
							if(!con.get(j).getIsdelete().booleanValue()){
								OperationResultVO rs = new OperationResultVO();
								rs.m_intSuccess = 2;
								rs.m_strDescription = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0203")/*@res "������ǿ��ɾ����ϵͳ�����ƾ֤"*/;
								resultList.add(rs);
							}else if(isPowerDelVoucher){
								PfxxUtils.lookUpPFxxEJBService().deleteIDvsPKByDocPK(vo.pk_vouchers[i]);
							}
						}
						if (!isPowerDelVoucher) { // ����ǿ��ɾ���ⲿ�����ƾ֤
							Logger.debug("������ǿ��ɾ����ϵͳ�����ƾ֤");
							OperationResultVO rs = new OperationResultVO();
							rs.m_intSuccess = 2;
							rs.m_strDescription = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("private20111017_0","02002001-0226")/*@res "�ⲿϵͳ����ƾ֤��������ǿ��ɾ����ƾ֤��"*/;
							resultList.add(rs);
						} 
					}
//					else { // ǿ��ɾ���ⲿ�����ƾ֤
//						Logger.debug("����ǿ��ɾ����ϵͳ�����ƾ֤");
//						// ɾ��ID vs PK���ձ��¼
//						Logger.debug(">>>>>>>>>>>>>>>>>>>>>>>> �ⲿ����ƽ̨ɾ����ƾ֤����" + vo.pk_vouchers[i] + "�Ķ�Ӧ��ϵ");
//						// ͨ��
//					}
				}
				// �����˽���Ҫɾ���������ɵ�ƾ֤
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e + "");
		}
		return resultList.toArray(new OperationResultVO[0]);
	}

}