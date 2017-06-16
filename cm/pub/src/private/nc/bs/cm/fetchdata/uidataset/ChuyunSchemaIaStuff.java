	/**
 *
 */
package nc.bs.cm.fetchdata.uidataset;

import java.util.ArrayList;
import java.util.List;

import nc.vo.cm.fetchdata.entity.PullDataStateVO;
import nc.vo.cm.fetchset.entity.AggFetchSetVO;
import nc.vo.cm.fetchset.entity.FetchSetItemVO;
import nc.vo.pub.BusinessException;

/**
 * 设置界面类-会计期间方案
 * 储运处理类
 * liyf
 */
public class ChuyunSchemaIaStuff extends AbstractDataSet {
    @Override
    public PullDataStateVO[] getVosOFSpecalParam(PullDataStateVO pullDataParamVos) throws BusinessException {
        // 取出所有的单据类型
    	AggFetchSetVO fetchSetAggVo = (AggFetchSetVO) pullDataParamVos.getAttributeValue("fetcheSetAggVO");
        if (fetchSetAggVo == null || fetchSetAggVo.getChildrenVO() == null || fetchSetAggVo.getChildrenVO().length ==0) {
            return new PullDataStateVO[0];
        }
        
        return this.getVoWithTrans(fetchSetAggVo);
    }

    /**
     * 根据不同的交易类型生成各个单据的vo
     *
     * @param ctranstypeids 交易类型
     * @return
     */
    private PullDataStateVO[] getVoWithTrans(AggFetchSetVO fetchSetAggVo) {
        List<PullDataStateVO> voList = new ArrayList<PullDataStateVO>();
        FetchSetItemVO[] bodys = (FetchSetItemVO[]) fetchSetAggVo.getChildrenVO();
        for (FetchSetItemVO body : bodys) {
            PullDataStateVO outvo = new PullDataStateVO();
            // 单据类型
//            if (transid.equals("I4")) {
//                outvo.setCbilltype(CMBillEnum.I4.toIntValue());
//            }
            outvo.setCbilltype(22);
            outvo.setCtranstypeid(body.getPk_billtypeid());
            outvo.setPk_workitem(body.getPk_workitem());
            outvo.setPk_costobject(body.getPk_costobject());
            outvo.setPk_qcdept(body.getPk_qcdept());
            outvo.setPk_serverdept(body.getPk_serverdept());
            outvo.setPk_largeritem(body.getPk_largeritem());
            outvo.setPk_factor(body.getPk_factor());
            outvo.setPk_factorgroup(body.getPk_factorgroup());
            voList.add(outvo);
        }
        return voList.toArray(new PullDataStateVO[0]);
    }

    @Override
    protected boolean isNeedJudgeWeekNdayChanged() throws BusinessException {

        return false;
    }

}
