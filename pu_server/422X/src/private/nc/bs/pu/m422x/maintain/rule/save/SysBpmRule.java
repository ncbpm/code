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

				// ���±�ע������״̬
				vo.getHVO().setVmemo("��ͬ��BPM" + vo.getHVO().getVmemo());
				// 0=���ɣ�1=�ύ��2=����������3=������4=������ͨ����5=�رգ�
				vo.getHVO().setFbillstatus(2);
				VOUpdate<StoreReqAppHeaderVO> update = new VOUpdate<StoreReqAppHeaderVO>();
				update.update(new StoreReqAppHeaderVO[] { vo.getHVO() },
						new String[] { "vmemo", "fbillstatus" });
			} catch (BusinessException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				ExceptionUtils.wrappBusinessException("ͬ����BPM����:"
						+ e.getMessage());
			}
		}

	}

}
