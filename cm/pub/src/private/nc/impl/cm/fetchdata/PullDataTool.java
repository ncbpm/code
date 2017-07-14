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
	 * 分组
	 */
	public static Map<FetchKeyVO, PullDataStateVO> splitBykey(
			PullDataStateVO[] stateVos) {
		Map<FetchKeyVO, PullDataStateVO> result = new HashMap<FetchKeyVO, PullDataStateVO>();
		if (CMArrayUtil.isNotEmpty(stateVos)) {
			for (PullDataStateVO vo : stateVos) {
				// 取数对象： 1 材料出，2产成品入 3 废品 4 作业 5 工序委外 6 其它出入库
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
	 * 分组
	 */
	public static Map<FetchKeyVO, PullDataStateVO> splitBykey(
			List<PullDataStateVO> stateVos) {
		Map<FetchKeyVO, PullDataStateVO> result = new HashMap<FetchKeyVO, PullDataStateVO>();
		if (!stateVos.isEmpty()) {
			for (PullDataStateVO vo : stateVos) {
				// 取数对象： 1 材料出，2产成品入 3 废品 4 作业 5 工序委外 6 其它出入库
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
