package nc.bpm.pu.pfxx;

import java.util.ArrayList;
import java.util.List;

import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.vo.ic.m4n.entity.TransformBodyVO;
import nc.vo.ic.m4n.entity.TransformHeadVO;
import nc.vo.ic.m4n.entity.TransformVO;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pu.m20.entity.PraybillHeaderVO;
import nc.vo.pu.m20.entity.PraybillItemVO;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.so.m30.entity.SaleOrderBVO;

/**
 * 唛头生成--NC请购单 1. 直接根据唛头评审生成带来源单据和源头单据的请购单 批次号是需货单号，BPM带过来 来源单据为需货单号对应的销售订单 ---
 * 且需要实现联查。 2. 来源销售订单表体如何关联： 销售订单只有一条表体（可能有赠品，过滤赠品后，之后一行有效物料） 关联后，实现联查
 * 即从请购单可以查询到 3. 如果唛头评审中（A需货单）有指定为非本需货单号的包装物B，数量如10，而需要生成一张由B转换成A的形态转换单。
 * 批次号=需货单号（只是包装物才会有,bpm需要过滤）
 * 
 * @author liyf
 * 
 * 
 */
public class MarksForBpmAdd extends AbstractPfxxPlugin {

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
		aggvo.setHVO(bill.getHVO());
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
		head.setAttributeValue("pk_group",
				bill.getHVO().getAttributeValue("pk_group"));
		head.setAttributeValue("pk_org",
				bill.getHVO().getAttributeValue("pk_org"));
		head.setAttributeValue("pk_org_v",
				bill.getHVO().getAttributeValue("pk_org_v"));
		head.setAttributeValue("dbilldate",
				bill.getHVO().getAttributeValue("dbilldate"));
		head.setAttributeValue("billmaker",
				bill.getHVO().getAttributeValue("billmaker"));
		head.setAttributeValue("cdptid",
				bill.getHVO().getAttributeValue("cdptid"));
		head.setAttributeValue("cdptid",
				bill.getHVO().getAttributeValue("cdptid"));
		head.setAttributeValue("cdptvid",
				bill.getHVO().getAttributeValue("cdptvid"));
		head.setAttributeValue("vbillcode",
				bill.getHVO().getAttributeValue("vbillcode"));
		head.setVtrantypecode("4N-01");
		head.setVnote("唛头自动转换");
		ArrayList<TransformBodyVO> tlist = new ArrayList<TransformBodyVO>();
		for (PraybillItemVO item : tranlist) {
			// 转换前表体
			TransformBodyVO body = createbody(item);
			// 转换后表体:根据销售订单
			TransformBodyVO afbody = createAfBody(bill, item);
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
	 */
	private TransformBodyVO createbody(PraybillItemVO item) {
		// TODO 自动生成的方法存根
		TransformBodyVO body = new TransformBodyVO();
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
		body.setNnum(item.getNnum());
		body.setNassistnum(item.getNastnum());
		body.setVchangerate(item.getVchangerate());
		
		// 批次号
		body.setVbatchcode(item.getVbatchcode());
		return body;
	}
	private TransformBodyVO createAfBody(PraybillVO bill, PraybillItemVO item) throws BusinessException {
		// TODO 自动生成的方法存根
		TransformBodyVO body = new TransformBodyVO();
		// fbillrowflag 行状态 fbillrowflag int 形态转换行类型 2=转换前，3=转换后，
		body.setFbillrowflag(3);
		// cbodywarehouseid 库存仓库
		body.setCbodywarehouseid(item.getPk_reqstor());
		// 根据销售订单查询对应的转换后的物料，
		SaleOrderBVO saleOrderBody = querySaleOrder(bill);
		body.setCmaterialvid(saleOrderBody.getCmaterialid());
		body.setCmaterialoid(saleOrderBody.getCmaterialvid());
	
		setInvFree(body, saleOrderBody);
		body.setCunitid(saleOrderBody.getCunitid());
		body.setCasscustid(saleOrderBody.getCastunitid());
		
		body.setNnum(item.getNnum());
		body.setNassistnum(item.getNastnum());
		body.setVchangerate(saleOrderBody.getVchangerate());//当前都是1/1,不重新换算
	
		// 物料批次号等于销售订单编码
		body.setVbatchcode(bill.getHVO().getVbillcode());
		return body;
	}
	
	/**
	 * 根据销售订单表体物料设置自由属性
	 * @param body
	 * @param item
	 */
	private void setInvFree(TransformBodyVO body, SaleOrderBVO item) {
		// TODO 自动生成的方法存根
		body.setCprojectid(item.getCprojectid());
		body.setCproductorid(item.getCproductorid());
		for (int i = 1; i <= 10; i++) {
			String key = "vfree" + i;
			body.setAttributeValue(key, item.getAttributeValue(key));
		}
	
		
	}

	private SaleOrderBVO querySaleOrder(PraybillVO bill)
			throws BusinessException {
		// TODO 自动生成的方法存根
		PraybillHeaderVO head = bill.getHVO();
		VOQuery<SaleOrderBVO> query = new VOQuery<SaleOrderBVO>(
				SaleOrderBVO.class);
		SqlBuilder sql = new SqlBuilder();
		sql.append(" and nvl(so_saleorder_b.dr,0) =0 ");
		sql.append(" and nvl(so_saleorder_b.blargessflag,'N')='N'");
		sql.append(" and so_saleorder_b.csaleorderid = ( select csaleorderid  from so_saleorder where nvl(dr,0)=0 ");
		sql.append(" and pk_org='" + head.getPk_org() + "' and vbillcode='"
				+ head.getVbillcode() + "' )");
		SaleOrderBVO[] bvos = query.query(sql.toString(), null);
		if (bvos == null || bvos.length == 0) {
			throw new BusinessException("根据需货单号" + head.getVbillcode()
					+ "未查询到NC销售订单");
		}
		if(bvos.length != 1){
			throw new BusinessException("根据需货单号" + head.getVbillcode()
					+ "查询到NC销售订单，除赠外有多条表体，无法确认");
		}
		return bvos[0];
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
