package nc.bs.cm.fetchdata.allcancel;

import nc.bd.framework.db.CMSqlBuilder;
import nc.bs.cm.stuff.bp.StuffRealDeleteBP;
import nc.cmpub.business.enumeration.CMSourceTypeEnum;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.cm.stuff.entity.StuffHeadVO;
import nc.vo.pub.BusinessException;

/**
 * 定额
 * 

 */
public class DingeAllCancel  implements IAllCancel {


    private void realDeleteStuff(FetchParamVO paramvo) throws BusinessException {
        CMSqlBuilder sql = new CMSqlBuilder();
        sql.append(StuffHeadVO.PK_ORG, paramvo.getPk_org());
        sql.and();
        sql.append(StuffHeadVO.PK_GROUP, paramvo.getPk_group());
        sql.and();
        sql.append(StuffHeadVO.CPERIOD, paramvo.getDaccountperiod());
        sql.and();
        sql.append(StuffHeadVO.ISOURCETYPE, CMSourceTypeEnum.HOME_MAKE);
        sql.and();
        sql.append(StuffHeadVO.VDEF20,"定额");
        new StuffRealDeleteBP().deleteByHeadCondition(sql.toString());

    }

	@Override
	public void allCancel(FetchParamVO paramvo) throws BusinessException {
		// TODO 自动生成的方法存根
		realDeleteStuff(paramvo);
	}

}
