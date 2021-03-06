package nc.bpm.pu.pfxx;

import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.pubitf.qc.c001.pu.ReturnObjectFor23;
import nc.vo.ic.pub.check.VOCheckUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m23.entity.ArriveHeaderVO;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.qc.pub.enumeration.StrictLevelEnum;

/**
 * 根据指定的到货单行，生成报检单
 * 
 * @author liyf
 * 
 */
public class M23ForJLAdd extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		ArriveVO bill = (ArriveVO) vo;
		// 2. 校验数据的合法性:1.数据结构完整 2.根据组织+单据号校验是否重复.
		checkData(bill);
		// 3.查询到货单
		ArriveVO nc_bll = fillData(bill);
		// 生成报检单
		ArriveVO[] lightVOs = new ArriveVO[] { nc_bll };
		
		//设置审批人
		InvocationInfoProxy.getInstance().setUserId(nc_bll.getHVO().getApprover());
		
		Object[] objects = new QualityCheckAction().qualityCheck(lightVOs,
				false);
		ArriveVO[] returnVos = (ArriveVO[]) objects[0];
		ReturnObjectFor23 rof = (ReturnObjectFor23) objects[1];
		// 得到质检模块的提示信息
		if (rof != null) {
			Map<String, Integer> strictMap = rof.getCsourcebid_strictlevel();
			for (ArriveVO hvo : lightVOs) {
				ArriveItemVO[] bvs = hvo.getBVO();
				for (ArriveItemVO bvo : bvs) {
					String bid = bvo.getPk_arriveorder_b();
					UFDouble naccumstorenum = bvo.getNaccumstorenum();
					if (naccumstorenum != null
							&& naccumstorenum
									.compareTo(bvo.getNnum() == null ? UFDouble.ZERO_DBL
											: bvo.getNnum()) >= 0) {
					}
					if (strictMap.containsKey(bid)) {
						if (StrictLevelEnum.FREE.value().equals(
								strictMap.get(bid))) {
							throw new BusinessException("根据指定的主键，" + bid
									+ "查询到到货单表体行,质检连续批的严格程度为免检，不需要生成报检单！");

						} else if (StrictLevelEnum.PAUSE.value().equals(
								strictMap.get(bid))) {
							throw new BusinessException("根据指定的主键，" + bid
									+ "查询到到货单表体行,质检连续批的严格程度为暂停，不能生成报检单！");
						}
					}
				}
			}
		}
		// 更新榜单号
		VOUpdate<ArriveItemVO> bupdat = new VOUpdate<>();
		bupdat.update(nc_bll.getBVO(), new String[] { "vbdef20" });

		// 返回生成的报检单的信息
//		VOQuery<ApplyHeaderVO> appquery = new VOQuery<>(ApplyHeaderVO.class);
//		ApplyHeaderVO[] vos = appquery
//				.query("  and  pk_applybill in (select   pk_applybill from qc_applybill_s  where  nvl(dr, 0)=0 and csourcetypecode='23' and csourcebid='"
//						+ nc_bll.getBVO()[0].getPrimaryKey() + "') ", null);
		return "报检完成";
	}

	private ArriveVO fillData(ArriveVO bill) throws BusinessException {
		// TODO 自动生成的方法存根
		ArriveItemVO[] bpmBvos = bill.getBVO();
		String[] bpks = new String[bpmBvos.length];
		for(int i=0;i<bpmBvos.length;i++){
			bpks[i]= bpmBvos[i].getPk_arriveorder_b();
		}
		VOQuery<ArriveItemVO> bquery = new VOQuery<>(ArriveItemVO.class);
		ArriveItemVO[] itemVOs = bquery
				.query(bpks);
		if (itemVOs == null || itemVOs.length == 0) {
			throw new BusinessException("根据指定的到货单主键，" + bpks.toString()
					+ "未查询到到货单表体行,请核对.");
		}
		for (ArriveItemVO itemVO : itemVOs) {
			UFDouble naccumchecknum = itemVO.getNaccumchecknum();
			if (naccumchecknum != null) {
				if (naccumchecknum.compareTo(itemVO.getNnum()) == 0) {
					UFDouble naccumstorenum = itemVO.getNaccumstorenum();
					if (naccumstorenum != null
							&& naccumstorenum.compareTo(UFDouble.ZERO_DBL) > 0) {
						throw new BusinessException("根据指定的主键，"
								+ itemVO.getPk_arriveorder_b() + "查询到到货单表体行,已经完全入库，不能报检.");
					}
				}
			}
			for(ArriveItemVO bpmvo:bpmBvos){
				if(itemVO.getPk_arriveorder_b().equalsIgnoreCase(bpmvo.getPk_arriveorder_b())){
					itemVO.setVbdef20(bpmvo.getVbdef20());
				}
			}
			
		}

		VOQuery<ArriveHeaderVO> hquery = new VOQuery<>(ArriveHeaderVO.class);
		ArriveHeaderVO[] hvos = hquery.query(new String[] { itemVOs[0]
				.getPk_arriveorder() });
		if (hvos == null || hvos.length == 0) {
			throw new BusinessException("未查询到NC到货单，请核对.");
		}
//		表体也设置，用来传递报报检单表头
//		hvos[0].setVdef20(bill.getBVO()[0].getVbdef20());
		ArriveVO nc_bill = new ArriveVO();
		nc_bill.setHVO(hvos[0]);
		nc_bill.setBVO(itemVOs);
		return nc_bill;

	}

	private void checkData(ArriveVO bill) throws BusinessException {
		// TODO 自动生成的方法存根
		ArriveItemVO[] arriveItemVOs = bill.getBVO();
		if (arriveItemVOs == null || arriveItemVOs.length == 0) {
			throw new BusinessException("请指定需要报检的到货单表体行.");
		}
		VOCheckUtil.checkBodyNotNullFields(bill, new String[] { "pk_arriveorder_b",
				"vbdef20" });
	}

}
