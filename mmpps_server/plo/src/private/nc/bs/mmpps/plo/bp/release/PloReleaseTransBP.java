package nc.bs.mmpps.plo.bp.release;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.mmpps.plo.plugin.bpplugin.Pd_ploPluginPoint;
import nc.bs.mmpps.plo.rule.PloMarginAdepterRule;
import nc.bs.mmpps.plo.rule.PloNumZeroRule;
import nc.bs.mmpps.plo.rule.PloOutOfFillRule;
import nc.bs.mmpps.plo.rule.PloSetOrgRule;
import nc.bs.mmpps.plo.rule.PloStatusRule;
import nc.bs.mmpps.plo.rule.PloUpdateTSRule;
import nc.bs.pub.pf.PfUtilTools;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.template.CommonOperatorTemplate;
import nc.impl.pubapp.pattern.rule.template.IOperator;
import nc.rule.mmpps.plo.PloFactoryNullRule;
import nc.util.mmf.busi.ModuleEnableCheckUtil;
import nc.util.mmf.busi.consts.BillTypeConst;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.mmpps.mpm.adapter.SCMPUBAdapter;
import nc.vo.mmpps.mpm.adapter.TOAdapter;
import nc.vo.mmpps.mpm.res.MpmRes;
import nc.vo.mmpps.mps0202.AggregatedPoVO;
import nc.vo.mmpps.mps0202.BillstatusEnum;
import nc.vo.mmpps.mps0202.PoVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MapList;
import nc.vo.pubapp.res.NCModule;
import nc.vo.to.m5x.entity.BillVO;

/**
 * 推单生成"调拨订单"适配类
 * 
 * @since 6.1
 * @version 2011-12-7 下午06:39:45
 * @author zhaohyc
 */
public class PloReleaseTransBP {

    public class PloReleaseTransOperator implements IOperator<AggregatedPoVO> {
        private UFBoolean isAtpcheck;

        public PloReleaseTransOperator(UFBoolean isAtpcheck) {
            this.setIsAtpcheck(isAtpcheck);
        }
        
        @Override
        public AggregatedPoVO[] operate(AggregatedPoVO[] aggVOs) {
            PoVO[] PloVOs = AggregatedPoVO.constructPoVOs(aggVOs);
            try {
                // 按照库存组织和物料oid进行分类
                MapList<String, PoVO> sortMapList = new MapList<String, PoVO>();
                for (PoVO pvo : PloVOs) {
                    sortMapList.put(pvo.getCfactoryid() + pvo.getCmaterialid(), pvo);
                }
                List<String> orgList = new ArrayList<String>();
                List<String> cmaterioidList = new ArrayList<String>();
                for (Map.Entry<String, List<PoVO>> entry : sortMapList.entrySet()) {
                    List<PoVO> sortList = entry.getValue();
                    orgList.add(sortList.get(0).getCfactoryid());
                    cmaterioidList.add(sortList.get(0).getCmaterialid());
                }
                // 调用供应链提供的寻源服务,返回调出库存组织
                String[] orgs =
                        SCMPUBAdapter.getISourceMMService().queryStockOrgs(
                                cmaterioidList.toArray(new String[cmaterioidList.size()]),
                                orgList.toArray(new String[orgList.size()]));
                List<AggregatedPoVO> aggVOList = new ArrayList<AggregatedPoVO>();
                int i = 0;
                for (Map.Entry<String, List<PoVO>> entry : sortMapList.entrySet()) {
                    List<PoVO> sortList = entry.getValue();
                    for (PoVO pvo : sortList) {
                    	//2017-08-08 liyf 再重新寻找根据前台设置的调出组织
//                        pvo.setCoutstockorgid(orgs[i]);
                        AggregatedPoVO aggVO = new AggregatedPoVO();
                        aggVO.setParentVO(pvo);
                        aggVOList.add(aggVO);
                    }
                    i++;
                }
                new PloSetOrgRule(PoVO.COUTSTOCKORGID, PoVO.COUTSTOCKORGVID).process(aggVOList
                        .toArray(new AggregatedPoVO[aggVOList.size()]));

                BillVO[] billvos =
                        (BillVO[]) PfUtilTools.runChangeDataAry(BillTypeConst.PLO, BillTypeConst.TRAN,
                                aggVOList.toArray(new AggregatedPoVO[aggVOList.size()]));

                // 调用内部交易提供的调拨订单推单接口
                TOAdapter.pushSaveBillVO(billvos, this.getIsAtpcheck());
            }
            catch (BusinessException e) {
                ExceptionUtils.wrappException(e);
            }
            return null;
        }

        public void setIsAtpcheck(UFBoolean isAtpcheck) {
            this.isAtpcheck = isAtpcheck;
        }

        public UFBoolean getIsAtpcheck() {
            return this.isAtpcheck;
        }
    }

    /**
     * 调拨订单下达
     * 
     * @param PloVOs
     * @throws BusinessException
     */
    public void doTranOrderRelease(PoVO[] PloVOs, UFBoolean isAtpcheck) throws BusinessException {
        if (MMValueCheck.isEmpty(PloVOs)) {
            return;
        }
        // 内部交易模块是否启用
        if (!ModuleEnableCheckUtil.isEnable(NCModule.TO)) {
            // 内部交易模块未启用,不能生成调拨订单
            ExceptionUtils.wrappBusinessException(MpmRes.getTOMudlUnableExpt2());
        }
        PloReleaseTransOperator op = new PloReleaseTransOperator(isAtpcheck);
        CommonOperatorTemplate<AggregatedPoVO> bp =
                new CommonOperatorTemplate<AggregatedPoVO>(Pd_ploPluginPoint.PLOCREATETRANSORDER, op);
        this.addBeforeRule(bp.getAroundProcesser());
        this.addAfterRule(bp.getAroundProcesser());
        AggregatedPoVO[] aggVOs = AggregatedPoVO.constructAggPoVOs(PloVOs);
        bp.operate(aggVOs);
    }

    private void addAfterRule(AroundProcesser<AggregatedPoVO> aroundProcesser) {
    }

    private void addBeforeRule(AroundProcesser<AggregatedPoVO> aroundProcesser) {
        // 库存组织不可为空
        IRule<AggregatedPoVO> ploFactoryNullRule = new PloFactoryNullRule();
        aroundProcesser.addBeforeRule(ploFactoryNullRule);
        // 补全下达信息
        IRule<AggregatedPoVO> ploOutOfFillRule = new PloOutOfFillRule();
        aroundProcesser.addBeforeRule(ploOutOfFillRule);
        // 数量计算(尾差处理)
        IRule<AggregatedPoVO> ploMarginAdepterRule = new PloMarginAdepterRule();
        aroundProcesser.addBeforeRule(ploMarginAdepterRule);
        // 下达数量不可<=0
        IRule<AggregatedPoVO> ploNumZeroRule = new PloNumZeroRule(PoVO.NACCTHISNUM);
        aroundProcesser.addBeforeRule(ploNumZeroRule);
        // 只有确认态才能下达
        IRule<AggregatedPoVO> ploStatusRule = new PloStatusRule(BillstatusEnum.CONFIRMED);
        aroundProcesser.addBeforeRule(ploStatusRule);
        // 更新TS
        IRule<AggregatedPoVO> ploUpdateTSRule = new PloUpdateTSRule();
        aroundProcesser.addBeforeRule(ploUpdateTSRule);
    }
}
