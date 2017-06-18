package nc.bs.cm.fetchdata.allcancel;

import java.util.ArrayList;
import java.util.List;

import nc.bd.framework.base.CMCollectionUtil;
import nc.bd.framework.db.CMSqlBuilder;
import nc.cmpub.business.enumeration.CMSourceTypeEnum;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.vo.cm.activitynum.entity.ActivityNumHeadVO;
import nc.vo.cm.activitynum.entity.ActivityNumItemVO;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.data.IRowSet;

/**
 * 储运
 * 

 */
public class ChuyunAllCancel  implements IAllCancel {

    @Override
    public void allCancel(FetchParamVO paramvo) throws BusinessException {
    	 CMSqlBuilder sql = new CMSqlBuilder();
         sql.append(ActivityNumHeadVO.PK_ORG, paramvo.getPk_org());
         sql.and();
         sql.append(ActivityNumHeadVO.PK_GROUP, paramvo.getPk_group());
         sql.and();
         sql.append(ActivityNumHeadVO.ISOURCETYPE,   CMSourceTypeEnum.HOME_MAKE.getEnumValue().getValue());
         sql.and();
         sql.append(ActivityNumHeadVO.CPERIOD, paramvo.getDaccountperiod());
         sql.and();
         sql.append(ActivityNumHeadVO.VNOTE, "储运");
//         sql.append(ActivityNumHeadVO.CCOSTOBJECTID, paramvo.getc);
         deleteActNumBySql(sql.toString());
    }
    
    /**
     * 根据sql删除作业统计单
     * 
     * @param sql
     */
    private void deleteActNumBySql(String sql) {
        DataAccessUtils utils = new DataAccessUtils();
        CMSqlBuilder sqlBuilder = new CMSqlBuilder();
        sqlBuilder.select();
        sqlBuilder.append(ActivityNumHeadVO.CACTNUMID);
        sqlBuilder.from(ActivityNumHeadVO.getDefaultTableName());
        sqlBuilder.where();
        sqlBuilder.append(sql);
        IRowSet rows = utils.query(sqlBuilder.toString());
        List<String> headPkList = new ArrayList<String>();
        if (rows == null) {
            return;
        }
        while (rows.next()) {
            headPkList.add(rows.getString(0));
        }

        if (CMCollectionUtil.isEmpty(headPkList)) {
            return;
        }

        CMSqlBuilder deletesql = new CMSqlBuilder();
        deletesql.append(" where ");
        deletesql.append(ActivityNumHeadVO.CACTNUMID, headPkList);
        utils.update(" delete from " + ActivityNumItemVO.getDefaultTableName() + deletesql.toString());
        utils.update(" delete from " + ActivityNumHeadVO.getDefaultTableName() + deletesql.toString());

    }

}
