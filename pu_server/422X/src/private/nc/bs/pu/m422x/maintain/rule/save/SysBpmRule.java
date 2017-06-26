package nc.bs.pu.m422x.maintain.rule.save;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uapeai.sys.ISysDisPatcher;
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
					throw new BusinessException("同步BPM限定,固定资产类物资需求申请,只支持一行表体");
				}
				NCLocator.getInstance().lookup(ISysDisPatcher.class)
						.handleRequest(vo, "bpm_422X", null);
			} catch (BusinessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				ExceptionUtils.wrappBusinessException("同步到BPM出错:"
						+ e.getMessage());
			}
		}

	}

}
