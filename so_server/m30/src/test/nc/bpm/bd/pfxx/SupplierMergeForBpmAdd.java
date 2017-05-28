package nc.bpm.bd.pfxx;

import nc.bs.bd.supplier.merge.validator.SupplierMergeAttrValidator;
import nc.bs.bd.supplier.merge.validator.SupplierMergeNotNullValidator;
import nc.bs.bd.supplier.merge.validator.SupplierMergeOrgValidator;
import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.uif2.validation.DefaultValidationService;
import nc.itf.bd.supplier.baseinfo.ISupplierBaseInfoQryService;
import nc.itf.bd.supplier.merge.ISupplierMergeService;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.bd.supplier.merge.SupmergeVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.BusinessException;

public class SupplierMergeForBpmAdd extends AbstractPfxxPlugin {

	private ISupplierBaseInfoQryService queryService = null;

	private ISupplierMergeService service = null;

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		SupmergeVO resvo = (SupmergeVO) vo;
		// check
		check(resvo);
		// У���Ƿ�ɽ��кϲ�
		validate(resvo);
		// �ϲ�
		SupplierVO[] svos = getQueryService().querySupBaseInfoByPks(null,
				new String[] { resvo.getPk_source() });
		SupplierVO[] tvos = getQueryService().querySupBaseInfoByPks(null,
				new String[] { resvo.getPk_target() });

		SupmergeVO mergevo = getService().mergeSupplier(svos[0],tvos[0]);
		return mergevo.getPk_supmerge();
	}

	private void validate(SupmergeVO resvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		String[] pks = new String[] { resvo.getPk_source(),
				resvo.getPk_target() };
		SupplierVO[] vos = getQueryService().querySupBaseInfoByPks(null, pks);
		DefaultValidationService validationService = new DefaultValidationService();
		validationService.addValidator(new SupplierMergeNotNullValidator());
		validationService.addValidator(new SupplierMergeOrgValidator());
		validationService.addValidator(new SupplierMergeAttrValidator());

		validationService.validate(vos);

	}

	private void check(SupmergeVO resvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		if (resvo.getPk_source() == null) {
			throw new BusinessException("pk_source ������Ϊ��");
		}

		if (resvo.getPk_target() == null) {
			throw new BusinessException("Pk_target ������Ϊ��");
		}

	}

	private ISupplierBaseInfoQryService getQueryService() {
		if (queryService == null) {
			queryService = NCLocator.getInstance().lookup(
					ISupplierBaseInfoQryService.class);
		}
		return queryService;
	}

	private ISupplierMergeService getService() {
		if (service == null) {
			service = NCLocator.getInstance().lookup(
					ISupplierMergeService.class);
		}
		return service;
	}

}
