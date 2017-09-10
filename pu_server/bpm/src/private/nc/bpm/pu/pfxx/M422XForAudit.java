package nc.bpm.pu.pfxx;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.itf.pu.m422x.IStoreReqAppClose;
import nc.pubitf.pu.m422x.api.IStoreReqAppQueryAPI;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m422x.entity.StoreReqAppHeaderVO;
import nc.vo.pu.m422x.entity.StoreReqAppVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.AppContext;

import org.apache.commons.lang.StringUtils;

/**
 *
1.审批后才执行同步,且NC审批状态修改成审批进行中
2.BPM回写后，更新审批状态为审批通过
3.同步是是否严格控制，不能重现同步
 * @author liyf
 *
 */
public class M422XForAudit extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		StoreReqAppVO billVO = (StoreReqAppVO) vo;
		StoreReqAppHeaderVO headVO =  billVO.getHVO();
		checkData(billVO);
		// 2.更新物资需求申请单单，并自动审批通过
		IStoreReqAppQueryAPI queryService = NCLocator.getInstance().lookup(
				IStoreReqAppQueryAPI.class);
		StoreReqAppVO[] originVOs = queryService
				.queryVOByIDs(new String[] { headVO.getPk_storereq() });
		// 检查不合格单pk合法性
		if (originVOs == null || originVOs.length < 1) {
			throw new BusinessException("根据指定的主键，" + headVO.getPk_storereq()
					+ "找不到对应的物资需求申请单，请检查NC内物资需求需求申请单是否删除.");
		}
		// 3.更新物资需求申请单的状态
		approveRejct(billVO, originVOs[0]);
	
		return "回写NC成功";
	}

	/**
	 * 审批通过物资需求申请单单 审批人，通过BPM传过来
	 * 
	 * @param billVO
	 * @throws BusinessException
	 */
	private void approveRejct(StoreReqAppVO bpmVO, StoreReqAppVO originVO)
			throws BusinessException {
		// TODO 自动生成的方法存根
		StoreReqAppHeaderVO headVO =originVO.getHVO();
		IStoreReqAppQueryAPI queryService = NCLocator.getInstance().lookup(
				IStoreReqAppQueryAPI.class);
		// 重新查询一个用来修改，模拟前台传过来的VO
		StoreReqAppVO[] clientVOS = queryService.queryVOByIDs(new String[] { headVO.getPk_storereq() });
		StoreReqAppVO clientVO = clientVOS[0];
		if(3 == bpmVO.getHVO().getFbillstatus() || 4 == bpmVO.getHVO().getFbillstatus()){
			InvocationInfoProxy.getInstance().setUserId(bpmVO.getHVO().getApprover());
			clientVO.getHVO().setApprover(bpmVO.getHVO().getApprover());
			clientVO.getHVO().setFbillstatus(bpmVO.getHVO().getFbillstatus());
			clientVO.getHVO().setTaudittime(AppContext.getInstance().getBusiDate());
			clientVO.getParentVO().setStatus(VOStatus.UPDATED);
			BillUpdate<StoreReqAppVO> update = new BillUpdate<StoreReqAppVO>();
			
			StoreReqAppVO[] vos = update.update(new StoreReqAppVO[] { clientVO },
					new StoreReqAppVO[] { originVO });
		}else if(5 == bpmVO.getHVO().getFbillstatus() ){
			IStoreReqAppClose appClose = NCLocator.getInstance().lookup(
					IStoreReqAppClose.class);
			appClose.billClose(clientVOS);
		}

	

	}


	private void checkData(StoreReqAppVO resvo) throws BusinessException {
		// TODO 自动生成的方法存根
		if (resvo == null || resvo.getHVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		
		if (StringUtils.isEmpty(resvo.getHVO().getPk_storereq())) {
			throw new BusinessException("Pk_rejectbill 不能为空");
		}
		if (StringUtils.isEmpty(resvo.getHVO().getApprover())) {
			throw new BusinessException("approver 不能为空");
		}
		
		if (resvo.getHVO().getFbillstatus() == null) {
			throw new BusinessException("fbillstatus 不能为空");
		}

	
	

	}

}
