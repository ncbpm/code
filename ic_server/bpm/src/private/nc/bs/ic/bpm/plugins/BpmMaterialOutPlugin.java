package nc.bs.ic.bpm.plugins;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.ic.m4d.bpm.BpmServicePluginPoint;
import nc.bs.logging.Logger;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.pubimpl.ic.m4d.m422x.action.PushSaveActionFor422X;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
import nc.vo.ic.m4d.entity.MaterialOutHeadVO;
import nc.vo.ic.m4d.entity.MaterialOutVO;
import nc.vo.ic.pub.check.VOCheckUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m422x.entity.StoreReqAppHeaderVO;
import nc.vo.pu.m422x.entity.StoreReqAppItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class BpmMaterialOutPlugin extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggxsysvo) throws BusinessException {
		ICBillVO[] icbills = null;
		try {

			MaterialOutVO icbill = (MaterialOutVO) vo;
			if (icbill == null || icbill.getParentVO() == null
					|| icbill.getBodys() == null
					|| icbill.getBodys().length == 0) {
				throw new BusinessException("传入数据不完整，请检查 数据完整性");
			}
			checkNotNull(icbill);

			fillMaterialOutVO(icbill);

			icbills = this.doSave(swapContext, icbill);

		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		return icbills[0].getHead().getCgeneralhid();
	}

	/**
	 * 检查非空字段
	 */
	private void checkNotNull(MaterialOutVO bill) throws BusinessException {

		VOCheckUtil.checkHeadNotNullFields(bill,
				new String[] { "cwarehouseid" });
		VOCheckUtil.checkBodyNotNullFields(bill, new String[] { "vbatchcode",
				"nshouldnum", "csourcebillhid", "csourcebillbid" });
	}

	/**
	 * 新增
	 * 
	 * @param swapContext
	 * @param icbill
	 * @return
	 * @throws BusinessException
	 */
	private ICBillVO[] doSave(ISwapContext swapContext, ICBillVO icbill)
			throws BusinessException {

		// 单据设置有辅助信息，aggxsysvo为用户配置的具体辅助信息
		MaterialOutVO[] bills = new MaterialOutVO[] { (MaterialOutVO) icbill };
		Logger.info("保存新单据...");
		MaterialOutVO[] vos = new PushSaveActionFor422X(
				BpmServicePluginPoint.pushSaveFor422X).pushSaveAndSign(bills);

		return vos;
	}

	// 补充来源信息
	private void fillMaterialOutVO(MaterialOutVO icbill)
			throws BusinessException {

		if (icbill == null || icbill.getParentVO() == null
				|| icbill.getBodys() == null || icbill.getBodys().length == 0) {
			return;
		}
		// 根据来源查找 物资需求申请

		Set<String> headPks = new HashSet<String>();
		Set<String> itemPks = new HashSet<String>();

		for (MaterialOutBodyVO vo : icbill.getBodys()) {
			itemPks.add(vo.getCsourcebillbid());
			headPks.add(vo.getCsourcebillhid());
		}

		StoreReqAppHeaderVO[] headers = new VOQuery<StoreReqAppHeaderVO>(
				StoreReqAppHeaderVO.class).query(headPks
				.toArray(new String[headPks.size()]));

		if (headers == null || headers.length == 0) {
			throw new BusinessException("物资需求申请表头信息不存在");
		}

		StoreReqAppItemVO[] items = new VOQuery<StoreReqAppItemVO>(
				StoreReqAppItemVO.class).query(itemPks
				.toArray(new String[itemPks.size()]));

		if (items == null || items.length == 0) {
			throw new BusinessException("物资需求申请表体信息不存在");
		}
		changeHeadVO(icbill.getHead(), headers[0]);
		changeBodys(icbill.getHead(), icbill.getBodys(), headers[0], items);

	}

	private void changeHeadVO(MaterialOutHeadVO headvo,
			StoreReqAppHeaderVO header) {
		headvo.setVtrantypecode("4D-01");
		headvo.setPk_org(header.getPk_org());
		headvo.setPk_group(header.getPk_group());
		headvo.setDbilldate(new UFDate("2017-07-23"));
		headvo.setCdptid(header.getPk_appdepth());
	}

	private void changeBodys(MaterialOutHeadVO headvo, MaterialOutBodyVO[] vos,
			StoreReqAppHeaderVO header, StoreReqAppItemVO[] items) {

		Map<String, StoreReqAppItemVO> map = new HashMap<String, StoreReqAppItemVO>();

		for (StoreReqAppItemVO item : items) {
			map.put(item.getPk_storereq_b(), item);
		}

		for (MaterialOutBodyVO vo : vos) {
			StoreReqAppItemVO sitem = map.get(vo.getCsourcebillbid());
			if (sitem == null)
				continue;
			vo.setCbodytranstypecode("4D-01");
			vo.setCbodywarehouseid(headvo.getCwarehouseid());
			if (sitem.getCfirstbid() == null) {
				vo.setCfirstbillbid(sitem.getPk_storereq_b());
				vo.setCfirstbillhid(sitem.getPk_storereq());
				vo.setCfirsttype("422X");
			} else {
				vo.setCfirstbillbid(sitem.getCfirstbid());
				vo.setCfirstbillhid(sitem.getCfirstid());
				vo.setCfirsttype(sitem.getCfirsttypecode());
			}
			vo.setCmaterialvid(sitem.getPk_material());
			vo.setCunitid(sitem.getCunitid());
			vo.setCprojectid(header.getPk_project());
			vo.setCsourcetype("422X");
			vo.setCsrcmaterialvid(sitem.getPk_material());
			vo.setVsourcebillcode(header.getVbillcode());
			vo.setVsourcerowno(sitem.getCrowno());
			vo.setDbizdate(new UFDate("2017-07-23"));
		}
	}

}