package nc.bs.cm.fetchdata.checkandfetch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bd.framework.base.CMArrayUtil;
import nc.bd.framework.base.CMNumberUtil;
import nc.bd.framework.base.CMStringUtil;
import nc.bs.cm.fetchdata.fetchcheck.AbstractCheckStrategy;
import nc.bs.cm.fetchdata.fetchcheck.CCPRKCheckStrategy;
import nc.bs.cm.fetchdata.fetchcheck.WTJGRKWGCheckStrtegy;
import nc.bs.cm.fetchdata.groupdata.IGroupStrategy;
import nc.bs.framework.common.NCLocator;
import nc.cmpub.business.adapter.IAAdapter;
import nc.cmpub.business.enumeration.CMAllocStatusEnum;
import nc.cmpub.business.enumeration.CMSourceTypeEnum;
import nc.pubitf.cm.product.cm.iaretrieval.IProductforIARetrieval;
import nc.vo.bd.material.prod.MaterialProdVO;
import nc.vo.cm.costobject.entity.CostObjectGenerateVO;
import nc.vo.cm.fetchdata.cmconst.FetchKeyConst;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.cm.fetchdata.entity.PullDataErroInfoVO;
import nc.vo.cm.fetchdata.entity.adapter.IFIFetchData;
import nc.vo.cm.fetchdata.entity.adapter.IFetchData;
import nc.vo.cm.fetchdata.entity.adapter.ProductVOAdapter;
import nc.vo.cm.fetchdata.enumeration.CMMesTypeEnum;
import nc.vo.cm.fetchdata.enumeration.FetchDataSchemaEnum;
import nc.vo.cm.fetchset.enumeration.CMBillEnum;
import nc.vo.cm.product.entity.ProductAggVO;
import nc.vo.cm.product.entity.ProductFetchVO;
import nc.vo.cm.product.entity.ProductHeadVO;
import nc.vo.cm.product.entity.ProductItemVO;
import nc.vo.cm.product.enumeration.ProductInStorageEnum;
import nc.vo.ia.detailledger.entity.DetailLedgerVO;
import nc.vo.ia.detailledger.para.cm.GetDataPara;
import nc.vo.ia.detailledger.view.cm.CMDataVO;
import nc.vo.ia.mi3.entity.I3ItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.Constructor;

/**
 * 定额取数
 */
public class HuanbaoFIFetch extends AbstractFIFetch {

	@Override
	public void deleteBill(FetchParamVO paramvo) throws BusinessException {
		// TODO 自动生成的方法存根
		
	}

	@Override
	protected CircularlyAccessibleValueObject[] getFIDataOfOutSystem(
			GetDataPara paramVO, int billType) throws BusinessException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public List<String> getSubGroupByFields(int billType) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	protected CostObjectGenerateVO[] getAndSetCostOjbParam(
			IFetchData[] correctData, AbstractCheckStrategy strategy,
			Map<IFetchData, PullDataErroInfoVO> vbFreeErrorVoMap) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	protected IFetchData[] transDatas(CircularlyAccessibleValueObject[] datas,
			FetchParamVO paramvo) throws BusinessException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	protected void transVOInserDB(IFIFetchData[] correctData,
			FetchParamVO paramVo) throws BusinessException {
		// TODO 自动生成的方法存根
		
	}

	@Override
	protected List<Map<IFetchData, PullDataErroInfoVO>> getErrorInfo(
			IFIFetchData[] datas, String cperiod, boolean isCheckFlag)
			throws BusinessException {
		// TODO 自动生成的方法存根
		return null;
	}}
