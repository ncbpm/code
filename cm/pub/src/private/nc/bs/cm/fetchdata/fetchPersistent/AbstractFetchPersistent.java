package nc.bs.cm.fetchdata.fetchPersistent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.bd.framework.base.CMCollectionUtil;
import nc.bd.framework.base.CMStringUtil;
import nc.bd.framework.db.CMSqlBuilder;
import nc.impl.pubapp.pattern.data.vo.VOInsert;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.vo.cm.fetchdata.entity.FetchParamVO;
import nc.vo.cm.fetchdata.entity.PullDataStateVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

/**
 * 取数抽象持久化类
 */
public abstract class AbstractFetchPersistent {

    public void saveStatus(FetchParamVO paramvo, PullDataStateVO[] pullDataStateVOArr) {
        VOInsert<PullDataStateVO> vi = new VOInsert<PullDataStateVO>();
        vi.insert(this.getSaveVO(paramvo.getPullDataStateVOArr(), pullDataStateVOArr));
    }

    private PullDataStateVO[] getSaveVO(PullDataStateVO[] pullDataStateVO, PullDataStateVO[] pullDataStateVOArr) {
        Set<String> idSet = new HashSet<String>();
        for (PullDataStateVO vo : pullDataStateVOArr) {
            if (!vo.getBfetched().booleanValue()) {
                if (CMStringUtil.isNotEmpty(vo.getCfetchinfoid())) {
                    idSet.add(vo.getCfetchinfoid());
                }
            }
        }
        for (PullDataStateVO vo : pullDataStateVO) {
            if (idSet.contains(vo.getCfetchinfoid())) {
                vo.setBfetched(UFBoolean.TRUE);
            }
        }
        return pullDataStateVO;
    }

    /**
     * 删除取数状态信息表中： 会计期间，取数对象，取数类型=存货核算下/生产制造的所有数据.
     * 
     * @param paramvo
     *            参数vo
     * @throws BusinessException
     *             BusinessException
     */
    public static void deleteRelationData(FetchParamVO paramvo) throws BusinessException {
        CMSqlBuilder sql = new CMSqlBuilder();
        sql.append(" delete ");
        sql.append(" from ");
        sql.append(PullDataStateVO.getDefaultTableName());
        sql.append(" where ");
        sql.append(PullDataStateVO.PK_ORG, paramvo.getPk_org());
        sql.append(" and ");
        sql.append(PullDataStateVO.PK_GROUP, paramvo.getPk_group());
        sql.append(" and ");
        sql.append(PullDataStateVO.CPERIOD, paramvo.getDaccountperiod());
        sql.append(" and ");
        sql.append(PullDataStateVO.IFETCHOBJTYPE, paramvo.getIfetchobjtype().toString());
        if(paramvo.getIfetchobjtype() <7 || paramvo.getIfetchobjtype() ==9){
        	sql.append(" and ");
            sql.append(PullDataStateVO.PULLDATATYPE, paramvo.getPulldatatype());
        }
        
        DataAccessUtils util = new DataAccessUtils();
        util.update(sql.toString());
    }

    /**
     * 更新取数状态
     * 
     * @param pullDataStateVOArr
     */
    public static void updateFetchStates(PullDataStateVO[] pullDataStateVOArr) {
        List<String> needIdList = new ArrayList<String>();
        for (PullDataStateVO vo : pullDataStateVOArr) {
            if (!vo.getBfetched().booleanValue()) {
                if (CMStringUtil.isNotEmpty(vo.getCfetchinfoid())) {
                    needIdList.add(vo.getCfetchinfoid());
                }
            }
        }
        if (CMCollectionUtil.isNotEmpty(needIdList)) {
            CMSqlBuilder sql = new CMSqlBuilder();
            sql.append(" update ");
            sql.append(PullDataStateVO.getDefaultTableName());
            sql.append(" set " + PullDataStateVO.BFETCHED, "Y");
            sql.append(" where ");
            sql.append(PullDataStateVO.CFETCHINFOID, needIdList.toArray(new String[0]));
            DataAccessUtils util = new DataAccessUtils();
            util.update(sql.toString());
        }
    }

    /**
     * 更新取数状态
     */
    public static void updateFetchStates(FetchParamVO paramvo, String yesNo) {
        CMSqlBuilder sql = new CMSqlBuilder();
        sql.append(" update ");
        sql.append(PullDataStateVO.getDefaultTableName());
        sql.append(" set " + PullDataStateVO.BFETCHED, "N");
        sql.append(" where ");
        sql.append(PullDataStateVO.PK_GROUP, paramvo.getPk_group());
        sql.append(" and ");
        sql.append(PullDataStateVO.PK_ORG, paramvo.getPk_org());
        sql.append(" and ");
        sql.append(PullDataStateVO.CPERIOD, paramvo.getDaccountperiod());
        sql.append(" and ");
        sql.append(PullDataStateVO.IFETCHOBJTYPE, paramvo.getIfetchobjtype());

        DataAccessUtils util = new DataAccessUtils();
        util.update(sql.toString());
    }

    /**
     * 创建PullDataStateVO
     * 
     * @param paramvo
     *            FetchParamVO
     * @return PullDataStateVO
     */
    protected PullDataStateVO createPullDataStateVO(FetchParamVO paramvo) {
        PullDataStateVO pullDataStateVO = new PullDataStateVO();

        String[][] fieldMap = {
            {
                PullDataStateVO.PK_ORG, FetchParamVO.PK_ORG
            }, {
                PullDataStateVO.PK_ORG_V, FetchParamVO.PK_ORG_V
            }, {
                PullDataStateVO.PK_GROUP, FetchParamVO.PK_GROUP
            }, {
                PullDataStateVO.IFETCHOBJTYPE, FetchParamVO.IFETCHOBJTYPE
            }, {
                PullDataStateVO.IFETCHSCHEME, FetchParamVO.IFETCHSCHEME
            }, {
                PullDataStateVO.CPERIOD, FetchParamVO.DACCOUNTPERIOD
            }
        };
        for (int i = 0; i < fieldMap.length; i++) {
            pullDataStateVO.setAttributeValue(fieldMap[i][0], paramvo.getAttributeValue(fieldMap[i][1]));
        }

        return pullDataStateVO;
    }
}
