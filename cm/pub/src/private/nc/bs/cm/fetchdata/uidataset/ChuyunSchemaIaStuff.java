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
 * ���ý�����-����ڼ䷽��
 * ���˴�����
 * liyf
 */
public class ChuyunSchemaIaStuff extends AbstractDataSet {
    @Override
    public PullDataStateVO[] getVosOFSpecalParam(PullDataStateVO pullDataParamVos) throws BusinessException {
        // ȡ�����еĵ�������
    	AggFetchSetVO fetchSetAggVo = (AggFetchSetVO) pullDataParamVos.getAttributeValue("fetcheSetAggVO");
        if (fetchSetAggVo == null || fetchSetAggVo.getChildrenVO() == null || fetchSetAggVo.getChildrenVO().length ==0) {
            return new PullDataStateVO[0];
        }
        
        return this.getVoWithTrans(fetchSetAggVo);
    }

    /**
     * ���ݲ�ͬ�Ľ����������ɸ������ݵ�vo
     *
     * @param ctranstypeids ��������
     * @return
     */
    private PullDataStateVO[] getVoWithTrans(AggFetchSetVO fetchSetAggVo) {
        List<PullDataStateVO> voList = new ArrayList<PullDataStateVO>();
        FetchSetItemVO[] bodys = (FetchSetItemVO[]) fetchSetAggVo.getChildrenVO();
        for (FetchSetItemVO body : bodys) {
            PullDataStateVO outvo = new PullDataStateVO();
            // ��������
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
