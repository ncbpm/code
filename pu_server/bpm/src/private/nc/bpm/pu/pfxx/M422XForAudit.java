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
1.�������ִ��ͬ��,��NC����״̬�޸ĳ�����������
2.BPM��д�󣬸�������״̬Ϊ����ͨ��
3.ͬ�����Ƿ��ϸ���ƣ���������ͬ��
 * @author liyf
 *
 */
public class M422XForAudit extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		StoreReqAppVO billVO = (StoreReqAppVO) vo;
		StoreReqAppHeaderVO headVO =  billVO.getHVO();
		checkData(billVO);
		// 2.���������������뵥�������Զ�����ͨ��
		IStoreReqAppQueryAPI queryService = NCLocator.getInstance().lookup(
				IStoreReqAppQueryAPI.class);
		StoreReqAppVO[] originVOs = queryService
				.queryVOByIDs(new String[] { headVO.getPk_storereq() });
		// ��鲻�ϸ�pk�Ϸ���
		if (originVOs == null || originVOs.length < 1) {
			throw new BusinessException("����ָ����������" + headVO.getPk_storereq()
					+ "�Ҳ�����Ӧ�������������뵥������NC�����������������뵥�Ƿ�ɾ��.");
		}
		// 3.���������������뵥��״̬
		approveRejct(billVO, originVOs[0]);
	
		return "��дNC�ɹ�";
	}

	/**
	 * ����ͨ�������������뵥�� �����ˣ�ͨ��BPM������
	 * 
	 * @param billVO
	 * @throws BusinessException
	 */
	private void approveRejct(StoreReqAppVO bpmVO, StoreReqAppVO originVO)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		StoreReqAppHeaderVO headVO =originVO.getHVO();
		IStoreReqAppQueryAPI queryService = NCLocator.getInstance().lookup(
				IStoreReqAppQueryAPI.class);
		// ���²�ѯһ�������޸ģ�ģ��ǰ̨��������VO
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
		// TODO �Զ����ɵķ������
		if (resvo == null || resvo.getHVO() == null)
			throw new BusinessException("δ��ȡ��ת���������");
		
		if (StringUtils.isEmpty(resvo.getHVO().getPk_storereq())) {
			throw new BusinessException("Pk_rejectbill ����Ϊ��");
		}
		if (StringUtils.isEmpty(resvo.getHVO().getApprover())) {
			throw new BusinessException("approver ����Ϊ��");
		}
		
		if (resvo.getHVO().getFbillstatus() == null) {
			throw new BusinessException("fbillstatus ����Ϊ��");
		}

	
	

	}

}
