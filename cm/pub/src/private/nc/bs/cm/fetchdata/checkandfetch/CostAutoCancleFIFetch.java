package nc.bs.cm.fetchdata.checkandfetch;

import java.util.List;
import java.util.Map;

import nc.bs.cm.fetchdata.fetchcheck.AbstractCheckStrategy;
import nc.vo.cm.costobject.entity.CostObjectGenerateVO;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.cm.fetchdata.entity.PullDataErroInfoVO;
import nc.vo.cm.fetchdata.entity.adapter.IFIFetchData;
import nc.vo.cm.fetchdata.entity.adapter.IFetchData;
import nc.vo.ia.detailledger.para.cm.GetDataPara;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class CostAutoCancleFIFetch extends AbstractFIFetch {

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
	}

}
