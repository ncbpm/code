package nc.bs.cm.fetchdata.allcancel;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.cm.product.cm.iaretrieval.IProductforIARetrieval;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.pub.BusinessException;

/**
 * ¶¨¶î
 * 

 */
public class DingeAllCancel  implements IAllCancel {

    @Override
    public void allCancel(FetchParamVO paramvo) throws BusinessException {
        NCLocator.getInstance().lookup(IProductforIARetrieval.class).deleteProdct4IAFetchSchema(paramvo);
    }

}
