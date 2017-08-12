package nc.bpm.bd.pfxx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.uap.bd.supplier.ISupplierConst;
import nc.itf.bd.supplier.baseinfo.ISupplierBaseInfoService;
import nc.itf.bd.supplier.suporg.ISupOrgService;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.MDPersistenceService;
import nc.vo.bd.supplier.SupLinkmanVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.bd.supplier.finance.SupFinanceVO;
import nc.vo.bd.supplier.stock.SupStockVO;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.ArrayUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;

import org.apache.commons.lang.StringUtils;

/**
 * 支持BPM供应商导入 1.带联系人信息 2.支持新增或者修改
 * 
 * @author liyf
 * 
 */
public class SupplierForBpmAdd extends AbstractPfxxPlugin {

	private IMDPersistenceQueryService mdQryService;

	private ISupplierBaseInfoService basesService;

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		SupplierVO supplierVO = (SupplierVO) vo;
		if (!hasUniqueDefaultLinkMan(supplierVO))
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("bdpub", "0bdpub0060")/*
																	 * @res
																	 * "供应商默认联系人必须唯一。"
																	 */);
		// rainbow 全部是集团的供应商, 使用pk_org记录需要分配到的组织
		String voPk = supplierVO.getPk_supplier();
		if (StringUtils.isEmpty(supplierVO.getPk_org())) {
			supplierVO.setPk_org(supplierVO.getPk_group());
		}
		String[] targets = getTargetOrgs(supplierVO);
		//
		if (!StringUtils.isEmpty(supplierVO.getCreator())) {
		}
		{
			InvocationInfoProxy.getInstance()
					.setUserId(supplierVO.getCreator());
		}
		setVOStatus(supplierVO.getSuplinkman(), VOStatus.NEW);
		if (!StringUtils.isEmpty(voPk)) {
			// 先执行删除再重新导入\
			setUpdateValues(supplierVO, voPk);
			supplierVO = getBasesService().pfxxUpdateSupplierVO(supplierVO,
					true);
			voPk = supplierVO.getPk_supplier();
		} else {
			supplierVO.setStatus(VOStatus.NEW);
			supplierVO = getBasesService().pfxxInsertSupplierVO(supplierVO,
					false);
			voPk = supplierVO.getPk_supplier();
		}

		// 分配组织
		ISupOrgService assignService2 = NCLocator.getInstance().lookup(
				ISupOrgService.class);
		String[] pks = new String[] { voPk };
		String[] funcPermissionOrgIDs = new String[] { "GLOBLE00000000000000",
				supplierVO.getPk_group() };
		assignService2.assignSupplierByPks(pks, targets, funcPermissionOrgIDs);

		return voPk;
	}

	private String[] getTargetOrgs(SupplierVO supplierVO) {
		// TODO 自动生成的方法存根
		List<String> pk = new ArrayList<String>();
		SupStockVO[] supstock = supplierVO.getSupstock();
		if (supstock != null) {
			for (SupStockVO vo : supstock) {
				String pk_org = vo.getPk_org();
				if (!pk.contains(pk_org)) {
					pk.add(pk_org);
				}
			}
		}
		SupFinanceVO[] supfinance = supplierVO.getSupfinance();

		if (supfinance != null) {
			for (SupFinanceVO vo : supfinance) {
				String pk_org = vo.getPk_org();
				if (!pk.contains(pk_org)) {
					pk.add(pk_org);
				}
			}
		}
		return pk.toArray(new String[0]);
	}

	private boolean hasUniqueDefaultLinkMan(SupplierVO supplier) {
		if (supplier.getSuplinkman() == null
				|| supplier.getSuplinkman().length == 0)
			return true;
		int i = 0;
		for (SupLinkmanVO linkMan : supplier.getSuplinkman()) {
			if (linkMan.getIsdefault() != null
					&& linkMan.getIsdefault().booleanValue() == true) {
				i++;
			}
		}
		return i > 1 ? false : true;
	}

	private void setVOStatus(SuperVO[] vos, int status) {
		if (ArrayUtils.isEmpty(vos))
			return;
		for (SuperVO vo : vos)
			vo.setStatus(status);
	}

	private void setUpdateValues(SupplierVO updateDocVO, String pk)
			throws BusinessException {
		NCObject objs = getMdQryService().queryBillOfNCObjectByPK(SupplierVO.class, pk);
		if (objs ==null || objs.getContainmentObject()== null)
			throw new BusinessException("请检查指定的供应商主键是否正确或者供应商在NC已经删除.");
		SupplierVO oldDocVO = (SupplierVO) objs.getContainmentObject();
		updateDocVO.setCode(oldDocVO.getCode());
		updateDocVO.setCreator(oldDocVO.getCreator());
		updateDocVO.setCreationtime(oldDocVO.getCreationtime());
		updateDocVO.setModifier(oldDocVO.getModifier());
		updateDocVO.setModifiedtime(oldDocVO.getModifiedtime());
		updateDocVO.setDataoriginflag(oldDocVO.getDataoriginflag());
		updateDocVO.setPrimaryKey(pk);
		updateDocVO.setPk_oldsupplier(pk);
		List<SupLinkmanVO> linkManVOList = new ArrayList<SupLinkmanVO>();
		if (oldDocVO.getSuplinkman() != null) {
			for (SupLinkmanVO linkManVO : oldDocVO.getSuplinkman()) {
				linkManVO.setStatus(VOStatus.DELETED);
				linkManVOList.add(linkManVO);
			}
		}
		if (updateDocVO.getSuplinkman() != null) {
			linkManVOList.addAll(Arrays.asList(updateDocVO.getSuplinkman()));
		}
		updateDocVO.setSuplinkman(linkManVOList.toArray(new SupLinkmanVO[0]));
		// 如果已经存在，则把之前删除
		updateStockVo(updateDocVO, oldDocVO);
		updateFinaVo(updateDocVO, oldDocVO);

	}

	private void updateStockVo(SupplierVO updateDocVO, SupplierVO oldDocVO) {
		// TODO 自动生成的方法存根
		List<SupStockVO> list = new ArrayList<SupStockVO>();
		SupStockVO[] vos = oldDocVO.getSupstock();
		if (vos != null) {
			for (SupStockVO vo : vos) {
				vo.setStatus(VOStatus.DELETED);
				list.add(vo);
			}
		}
		SupStockVO[] vos2 = updateDocVO.getSupstock();
		if (vos2 != null) {
			for (SupStockVO vo : vos2) {
				vo.setStatus(VOStatus.NEW);
				vo.setPk_supplier(updateDocVO.getPk_supplier());
				list.add(vo);
			}
		}
		if (list.size() > 0) {
			updateDocVO.setSupstock(list.toArray(new SupStockVO[0]));
		}
	}

	private void updateFinaVo(SupplierVO updateDocVO, SupplierVO oldDocVO) {
		// TODO 自动生成的方法存根
		List<SupFinanceVO> list = new ArrayList<SupFinanceVO>();
		SupFinanceVO[] vos = oldDocVO.getSupfinance();
		if (vos != null) {
			for (SupFinanceVO vo : vos) {
				vo.setStatus(VOStatus.DELETED);
				list.add(vo);
			}
		}
		SupFinanceVO[] vos2 = updateDocVO.getSupfinance();
		if (vos2 != null) {
			for (SupFinanceVO vo : vos2) {
				vo.setStatus(VOStatus.NEW);
				vo.setPk_supplier(updateDocVO.getPk_supplier());
				list.add(vo);
			}
		}
		if (list.size() > 0) {
			updateDocVO.setSupfinance(list.toArray(new SupFinanceVO[0]));
		}
	}

	private IMDPersistenceQueryService getMdQryService() {
		if (mdQryService == null)
			mdQryService = MDPersistenceService.lookupPersistenceQueryService();
		return mdQryService;
	}

	private ISupplierBaseInfoService getBasesService() {
		if (basesService == null)
			basesService = NCLocator.getInstance().lookup(
					ISupplierBaseInfoService.class);
		return basesService;
	}

}
