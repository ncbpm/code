package nc.bpm.pu.pfxx;

import java.util.ArrayList;
import java.util.List;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.vo.ic.batch.BatchRefViewVO;
import nc.vo.ic.m4n.entity.TransformBodyVO;
import nc.vo.ic.m4n.entity.TransformHeadVO;
import nc.vo.ic.m4n.entity.TransformVO;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m20.entity.PraybillHeaderVO;
import nc.vo.pu.m20.entity.PraybillItemVO;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * 唛头生成--NC请购单 
1. 直接根据唛头评审生成带来源单据和源头单据的请购单
BPM带过来生成的请购单 批次号 必须是当前唛头的 需货单号
来源单据为需货单号对应的销售订单 --- NC实现实现联查。
2. 来源销售订单表体如何关联： 销售订单只有一条表体（可能有赠品，过滤赠品后，之后一行有效物料）
关联后，实现联查 即从请购单可以查询到销售订单
3. 如果唛头评审中（A需货单）有指定可以换批次出库的 库存产品B，数量取可用的现存量如20,并可修改  
则需要生成一张由B转换成B的 换批次的形态转换单。
新的 批次号是 需货单的
转换前后的仓库，都在产品B的批次
 * 
 * @author liyf
 * 
 * 
 */
public class MarksForBpmAdd extends AbstractPfxxPlugin {
	
	private int power = 2;// 精度


	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO 自动生成的方法存根
		PraybillVO bill = (PraybillVO) vo;
		//
		checkData(bill);
		// 拆分数据:需要请购的和需要形态转换的数据
		List<PraybillItemVO> praylist = getPrayList(bill);

		List<PraybillItemVO> tranlist = getTranList(bill);

		// 生成请购单
		if (praylist != null && praylist.size() > 0) {
			PraybillVO praybill = createPrayBill(bill, praylist);
			new MarksForBpmPray().saveAndApprove(praybill);

		}
		// 生成形态转换单
		if (tranlist != null && tranlist.size() > 0) {
			TransformVO bill2 = createTransBill(bill, tranlist);
			new MarksForBpmTrans().save(bill2);
		}

		return "回写成功";
	}

	
	/***
	 * 生成形态转换单
	 * 
	 * @param bill
	 * @param praylist
	 * @throws BusinessException
	 */
	private PraybillVO createPrayBill(PraybillVO bill, List<PraybillItemVO> praylist)
			throws BusinessException {
		// TODO 自动生成的方法存根
		PraybillVO aggvo = new PraybillVO();
		PraybillHeaderVO head = new PraybillHeaderVO();
		for(String attr:head.getAttributeNames()){
			head.setAttributeValue(attr, bill.getHVO().getAttributeValue(attr));
		}
		head.setVmemo(bill.getHVO().getVbillcode());
		head.setVbillcode(null);
		aggvo.setHVO(head);
		aggvo.setBVO(praylist.toArray(new PraybillItemVO[0]));

		return aggvo;
	}

	/**
	 * 生成形态转换单
	 * 
	 * @param bill
	 * @param tranlist
	 * @throws BusinessException 
	 */
	private TransformVO createTransBill(PraybillVO bill, List<PraybillItemVO> tranlist) throws BusinessException {
		// TODO 自动生成的方法存根
		TransformVO tranbill = new TransformVO();
		TransformHeadVO head = new TransformHeadVO();
		tranbill.setParentVO(head);
		// 销售组织-采购组织-库存组织
		head.setAttributeValue("pk_group",bill.getHVO().getAttributeValue("pk_group"));
		head.setAttributeValue("pk_org",bill.getHVO().getAttributeValue("pk_org"));
		head.setAttributeValue("pk_org_v",bill.getHVO().getAttributeValue("pk_org_v"));
		head.setAttributeValue("dbilldate",bill.getHVO().getAttributeValue("dbilldate"));
		head.setAttributeValue("billmaker",bill.getHVO().getAttributeValue("billmaker"));
		head.setAttributeValue("cdptid",bill.getHVO().getAttributeValue("cdptid"));
		head.setAttributeValue("cdptvid",
				bill.getHVO().getAttributeValue("cdptvid"));
		head.setVnote(bill.getHVO().getVbillcode());
		head.setVtrantypecode("4N-01");
		head.setAttributeValue(TransformHeadVO.APPROVER, head.getAttributeValue("approver"));
		head.setAttributeValue(TransformHeadVO.CREATOR, head.getAttributeValue("billmaker"));

		head.setVnote(bill.getHVO().getVbillcode());
		ArrayList<TransformBodyVO> tlist = new ArrayList<TransformBodyVO>();
		for (PraybillItemVO item : tranlist) {
			// 转换前表体
			TransformBodyVO body = createbody(head,item);
			// 转换后表体:根据销售订单
			TransformBodyVO afbody = createAfBody(bill, body);
			afbody.setFbillrowflag(3);
			tlist.add(body);
			tlist.add(afbody);

		}
		tranbill.setChildrenVO(tlist.toArray(new TransformBodyVO[0]));
		return tranbill;
	}

	/**
	 * 根据选择的待转换的物料
	 * 
	 * @param item
	 * @return
	 * @throws BusinessException 
	 */
	private TransformBodyVO createbody(TransformHeadVO head,PraybillItemVO item) throws BusinessException {
		// TODO 自动生成的方法存根
		TransformBodyVO body = new TransformBodyVO();
		body.setPk_group(head.getPk_group());
		body.setPk_org(head.getPk_org());
		body.setPk_org_v(head.getPk_org_v());
		body.setCorpoid(head.getCorpoid());
		body.setCorpvid(head.getCorpvid());
		body.setCbodywarehouseid(head.getCwarehouseid());
		// fbillrowflag 行状态 fbillrowflag int 形态转换行类型 2=转换前，3=转换后，
		body.setFbillrowflag(2);
		// cbodywarehouseid 库存仓库
		body.setCbodywarehouseid(item.getPk_reqstor());
		// h换前物料
		
		body.setCmaterialvid(item.getPk_material());
		body.setCmaterialoid(item.getPk_srcmaterial());
		setInvFree(body, item);
		
		body.setCunitid(item.getCunitid());
		body.setCasscustid(item.getCastunitid());
		body.setNnum(getUFDdoubleNullASZero(item.getNnum()).setScale(power,
				UFDouble.ROUND_HALF_UP));
		body.setNassistnum(getUFDdoubleNullASZero(item.getNastnum()).setScale(power,
				UFDouble.ROUND_HALF_UP));
		body.setVchangerate(item.getVchangerate());
		// 批次号
		body.setVbatchcode(item.getVbatchcode());
		
		//同步的维度信息
		BatchCodeRule batchCodeRule = new BatchCodeRule();
		BatchRefViewVO batchRefViewVOs = batchCodeRule.getRefVO(body.getPk_org(),body.getCbodywarehouseid(),body.getCmaterialvid(), body.getVbatchcode());
		if (batchRefViewVOs != null ) {
			batchCodeRule.synBatch(batchRefViewVOs, body);
		}
		return body;
	}
	private TransformBodyVO createAfBody(PraybillVO bill, TransformBodyVO bodyBefore) throws BusinessException {
		// TODO 自动生成的方法存根
		TransformBodyVO bodyAfter = new TransformBodyVO();
		for(String attr:bodyAfter.getAttributeNames()){
			bodyAfter.setAttributeValue(attr, bodyBefore.getAttributeValue(attr));
		}
		// fbillrowflag 行状态 fbillrowflag int 形态转换行类型 2=转换前，3=转换后，
		bodyAfter.setFbillrowflag(3);
		// 物料批次号等于销售订单编码
		bodyAfter.setVbatchcode(bill.getHVO().getVbillcode());
		bodyAfter.setPk_batchcode(null);
		return bodyAfter;
	}
	
	private UFDouble getUFDdoubleNullASZero(Object o) {
		if (o == null) {
			return UFDouble.ZERO_DBL;
		}
		if (o instanceof UFDouble) {
			return (UFDouble) o;
		} else {
			return new UFDouble((String) o);
		}
	}

	/**
	 * 设置物料自由属性
	 * 
	 * @param body
	 * @param item
	 */
	private void setInvFree(TransformBodyVO body, PraybillItemVO item) {
		// TODO 自动生成的方法存根
		body.setCprojectid(item.getCprojectid());
		body.setCproductorid(item.getCproductorid());
		for (int i = 1; i <= 10; i++) {
			String key = "vfree" + i;
			body.setAttributeValue(key, item.getAttributeValue(key));
		}
	}


	/**
	 * 获得需要生成请购单单数据
	 * 
	 * @param bill
	 * @return
	 * @throws BusinessException
	 */
	private List<PraybillItemVO> getTranList(PraybillVO bill)
			throws BusinessException {
		// TODO 自动生成的方法存根
		List<PraybillItemVO> praylist = new ArrayList<PraybillItemVO>();
		for (PraybillItemVO body : bill.getBVO()) {
			if ("trans".equalsIgnoreCase(body.getVbmemo())) {

				if (StringUtil.isSEmptyOrNull(body.getVbatchcode())
						|| StringUtil.isSEmptyOrNull(body.getPk_batchcode())
						|| StringUtil.isSEmptyOrNull(body.getPk_reqstor())) {
					throw new BusinessException("me too 存货请指定批次号+批次主键+仓库");
				}
				praylist.add(body);
			}
		}
		return praylist;
	}

	/**
	 * 获得需要生成形态转换单的数据
	 * 
	 * @param bill
	 * @return
	 */
	private List<PraybillItemVO> getPrayList(PraybillVO bill) {
		// TODO 自动生成的方法存根
		List<PraybillItemVO> praylist = new ArrayList<PraybillItemVO>();
		for (PraybillItemVO body : bill.getBVO()) {
			if (!"trans".equalsIgnoreCase(body.getVbmemo())) {
				praylist.add(body);
			}
		}
		return praylist;
	}

	private void checkData(PraybillVO bill) throws BusinessException {
		// TODO 自动生成的方法存根
		if (bill == null || bill.getParentVO() == null)
			throw new BusinessException("未获取的转换后的数据");
		if (bill.getChildrenVO() == null || bill.getChildrenVO().length == 0) {
			throw new BusinessException("表体不允许为空");
		}
		for (PraybillItemVO body : bill.getBVO()) {

		}

	}

}
