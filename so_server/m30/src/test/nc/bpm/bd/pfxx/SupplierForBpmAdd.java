package nc.bpm.bd.pfxx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.bs.uap.bd.supplier.ISupplierConst;
import nc.itf.bd.supplier.assign.ISupplierAssignService;
import nc.itf.bd.supplier.baseinfo.ISupplierBaseInfoService;
import nc.itf.bd.supplier.suporg.ISupOrgService;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.IMDPersistenceService;
import nc.md.persist.framework.MDPersistenceService;
import nc.vo.bd.supplier.SupLinkmanVO;
import nc.vo.bd.supplier.SupplierVO;
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

	private IMDPersistenceService mdService;

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
		//rainbow 全部是集团的供应商, 使用pk_org记录需要分配到的组织
		String assign_orgs = supplierVO.getPk_org();
		supplierVO.setPk_org(supplierVO.getPk_group());
		String voPk = supplierVO.getPk_supplier();
		setVOStatus(supplierVO.getSuplinkman(), VOStatus.NEW);  
		if (voPk == null) {
			supplierVO.setStatus(VOStatus.NEW);
			supplierVO = getBasesService().insertSupplierVOForCreate(supplierVO,
					false);
			voPk = supplierVO.getPk_supplier();
			//执行供应商分配
			if(!StringUtils.isEmpty(assign_orgs)){
				String [] pks = new String[]{voPk};
				String[] targets = assign_orgs.split(",");			
				ISupplierAssignService assignService2 = NCLocator.getInstance().lookup(ISupplierAssignService.class);
				assignService2.assignSupplierByPks(pks, targets,new String[]{"GLOBLE00000000000000", "0001A51000000000078A"});
				
				assignService2.assignByPks(pks, targets, false);
			
				
			}
		} else {
			supplierVO.setStatus(VOStatus.UPDATED);
			setUpdateValues(supplierVO, voPk);
			getBasesService().updateSupplierVO(supplierVO, false);
			//执行供应商分配
			if(!StringUtils.isEmpty(assign_orgs)){
				String [] pks = new String[]{voPk};
				String[] targets = assign_orgs.split(",");			
				ISupplierAssignService assignService2 = NCLocator.getInstance().lookup(ISupplierAssignService.class);
				
				assignService2.assignSupplierByPks(pks, targets,new String[]{"GLOBLE00000000000000", "0001A51000000000078A"});
				
				assignService2.assignByPks(pks, targets, true);
			}
		}
		//重新执行一次查询，
		
		
		
		return voPk;
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
		Object[] objs = getMdQryService().queryBillOfVOByPKsWithOrder(
				SupplierVO.class, new String[] { pk },
				new String[] { ISupplierConst.ATTR_SUPPLIER_CONTACTS });
		if (ArrayUtils.isEmpty(objs))
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("bdpub", "0bdpub0057")/*
																	 * @res
																	 * "单据已被删除"
																	 */);
		SupplierVO oldDocVO = (SupplierVO) objs[0];
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

	private IMDPersistenceService getMdService() {
		if (mdService == null)
			mdService = MDPersistenceService.lookupPersistenceService();
		return mdService;
	}
}
