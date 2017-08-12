package nc.bs.pu.m422x.maintain.rule.save;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uapeai.sys.ISysDisPatcher;
import nc.vo.pu.m422x.entity.StoreReqAppHeaderVO;
import nc.vo.pu.m422x.entity.StoreReqAppItemVO;
import nc.vo.pu.m422x.entity.StoreReqAppVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 固定资产类物资需求单保存后，同步到BPM
 * 
 * @author liyf
 * 
 */
public class SysBpmRule implements IRule<StoreReqAppVO> {
	@Override
	public void process(StoreReqAppVO[] vos) {

		// 判断是否固定资产，如果是固定资产，则进行同步到BPM操作
		for (StoreReqAppVO vo : vos) {
			try {
				if (!"422X-Cxx-01".equalsIgnoreCase(vo.getHVO()
						.getVtrantypecode())) {
					continue;
				}

				if (vo.getBVO() == null) {
					continue;
				}

				if (vo.getBVO().length > 1) {
					for (StoreReqAppItemVO body : vo.getBVO()) {
						StoreReqAppVO bill = new StoreReqAppVO();
						bill.setHVO(vo.getHVO());
						bill.setBVO(new StoreReqAppItemVO[] { body });
						NCLocator.getInstance().lookup(ISysDisPatcher.class)
								.handleRequest(bill, "bpm_422X", null);
					}
				} else {
					NCLocator.getInstance().lookup(ISysDisPatcher.class)
							.handleRequest(vo, "bpm_422X", null);
				}

				// 更新备注和审批状态
				vo.getHVO().setVmemo("已同步BPM" + vo.getHVO().getVmemo());
				// 0=自由，1=提交，2=正在审批，3=审批，4=审批不通过，5=关闭，
				vo.getHVO().setFbillstatus(2);
				VOUpdate<StoreReqAppHeaderVO> update = new VOUpdate<StoreReqAppHeaderVO>();
				update.update(new StoreReqAppHeaderVO[] { vo.getHVO() },
						new String[] { "vmemo", "fbillstatus" });
			} catch (BusinessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				ExceptionUtils.wrappBusinessException("同步到BPM出错:"
						+ e.getMessage());
			}
		}

	}

}
