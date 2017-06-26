package nc.bs.pu.m422x.maintain.rule.save;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uapeai.sys.ISysDisPatcher;
import nc.vo.pu.m422x.entity.StoreReqAppVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * �̶��ʲ����������󵥱����ͬ����BPM
 * 
 * @author liyf
 * 
 */
public class SysBpmRule implements IRule<StoreReqAppVO> {
	@Override
	public void process(StoreReqAppVO[] vos) {

		// �ж��Ƿ�̶��ʲ�������ǹ̶��ʲ��������ͬ����BPM����
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
					throw new BusinessException("ͬ��BPM�޶�,�̶��ʲ���������������,ֻ֧��һ�б���");
				}
				NCLocator.getInstance().lookup(ISysDisPatcher.class)
						.handleRequest(vo, "bpm_422X", null);
			} catch (BusinessException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				ExceptionUtils.wrappBusinessException("ͬ����BPM����:"
						+ e.getMessage());
			}
		}

	}

}
