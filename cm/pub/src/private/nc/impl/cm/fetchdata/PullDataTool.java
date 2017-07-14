package nc.impl.cm.fetchdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bd.framework.base.CMArrayUtil;
import nc.vo.cm.fetchdata.entity.FetchKeyVO;
import nc.vo.cm.fetchdata.entity.PullDataStateVO;

public class PullDataTool {

	/**
	 * ����
	 */
	public static Map<FetchKeyVO, PullDataStateVO> splitBykey(
			PullDataStateVO[] stateVos) {
		Map<FetchKeyVO, PullDataStateVO> result = new HashMap<FetchKeyVO, PullDataStateVO>();
		if (CMArrayUtil.isNotEmpty(stateVos)) {
			for (PullDataStateVO vo : stateVos) {
				// ȡ������ 1 ���ϳ���2����Ʒ�� 3 ��Ʒ 4 ��ҵ 5 ����ί�� 6 ���������
				FetchKeyVO keyVO = new FetchKeyVO();
				keyVO.setPk_org(vo.getPk_org());
				keyVO.setIfetchobjtype(vo.getIfetchobjtype());
				keyVO.setFator1(vo.getCtranstypeid());
				keyVO.setFator2(vo.getPk_qcdept());
				keyVO.setFator3(vo.getPk_costobject());
				keyVO.setFator4(vo.getPk_serverdept());
				keyVO.setFator5(vo.getPk_largeritem());
				keyVO.setFator6(vo.getPk_factor());
				keyVO.setFator7(vo.getPk_workitem());
				if (result.containsKey(keyVO)) {
					continue;
				} else {
					result.put(keyVO, vo);
				}

			}
		}
		return result;
	}

	/**
	 * ����
	 */
	public static Map<FetchKeyVO, PullDataStateVO> splitBykey(
			List<PullDataStateVO> stateVos) {
		Map<FetchKeyVO, PullDataStateVO> result = new HashMap<FetchKeyVO, PullDataStateVO>();
		if (!stateVos.isEmpty()) {
			for (PullDataStateVO vo : stateVos) {
				// ȡ������ 1 ���ϳ���2����Ʒ�� 3 ��Ʒ 4 ��ҵ 5 ����ί�� 6 ���������
				FetchKeyVO keyVO = new FetchKeyVO();
				keyVO.setPk_org(vo.getPk_org());
				keyVO.setIfetchobjtype(vo.getIfetchobjtype());
				keyVO.setFator1(vo.getCtranstypeid());
				keyVO.setFator2(vo.getPk_qcdept());
				keyVO.setFator3(vo.getPk_costobject());
				keyVO.setFator4(vo.getPk_serverdept());
				keyVO.setFator5(vo.getPk_largeritem());
				keyVO.setFator6(vo.getPk_factor());
				keyVO.setFator7(vo.getPk_workitem());
				if (result.containsKey(keyVO)) {
					continue;
				} else {
					result.put(keyVO, vo);
				}

			}
		}
		return result;
	}

}
