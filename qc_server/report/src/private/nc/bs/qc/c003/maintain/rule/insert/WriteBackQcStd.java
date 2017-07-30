package nc.bs.qc.c003.maintain.rule.insert;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.pubimpl.qc.c002.qc.c001.PushSaveForC001Impl;
import nc.pubitf.qc.checkstandard.QueryStdChkItemPara;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.c001.entity.ApplyItemVO;
import nc.vo.qc.c001.entity.ApplyVO;
import nc.vo.qc.c002.entity.CheckBillHeaderVO;
import nc.vo.qc.c003.entity.ReportVO;

import org.apache.commons.lang.StringUtils;

public class WriteBackQcStd {

	public void writebackStd(ReportVO reportbill) {
		// TODO 自动生成的方法存根
		String pk_applybill = reportbill.getHVO().getPk_applybill();
		String pk_chkstd = reportbill.getHVO().getPk_chkstd();
		if (StringUtils.isEmpty(pk_chkstd)) {
			return;
		}
		BillQuery<ApplyVO> query = new BillQuery<ApplyVO>(ApplyVO.class);
		ApplyVO vo = query.query(new String[] { pk_applybill })[0];
		if (pk_chkstd.equalsIgnoreCase(vo.getHVO().getPk_defaultstd())) {
			return;
		}
		
		//之前的检验项目删除掉
		List<ApplyItemVO> oldItem = new ArrayList<ApplyItemVO>();
		for (ApplyItemVO b1vo : vo.getB1VO()) {
			b1vo.setStatus(VOStatus.DELETED);
			oldItem.add(b1vo);
		}

		ApplyVO originBill = (ApplyVO) vo.clone();
		vo.getHVO().setPk_defaultstd(pk_chkstd);
		List<String> retStds = new ArrayList<String>();
		retStds.add(pk_chkstd);
		// 匹配多标准
		MatchStandardVOUtilForWriteback match3 = new MatchStandardVOUtilForWriteback(
				vo);
		// 查询所匹配到的检验方案的默认检验项目
		QueryStdChkItemPara[] stdChkItems = match3.queryStdChkItem(retStds);
		// 得到检验方案的最新版本VID
		stdChkItems = match3.fillupStdVids(stdChkItems);

		// 根据“检验项目确定取样方式”属性来同步表头、表头的取样方式
		stdChkItems = match3.synchHeadBodyCheckMode(stdChkItems);
		// 记录多方案、构造表体检验项目数据
		match3.recordHeadMatchInfo(stdChkItems);
		match3.recordBodyMatchInfo(stdChkItems);
		
		vo.getHVO().setStatus(VOStatus.UPDATED);
				
		for (ApplyItemVO b1vo : vo.getB1VO()) {
			b1vo.setStatus(VOStatus.NEW);
			b1vo.setPk_applybill(pk_applybill);
			oldItem.add(b1vo);
		}
		vo.setB1VO(oldItem.toArray(new ApplyItemVO[0]));
		BillUpdate<ApplyVO> update = new BillUpdate<ApplyVO>();
		update.update(new ApplyVO[] { vo }, new ApplyVO[] { originBill });
		
		//把直接报告的检验信息删除掉.然后重新生成
		PushSaveForC001Impl pushCheckBill = new PushSaveForC001Impl();
		try {
			new BaseDAO().deleteByClause(CheckBillHeaderVO.class, " csourceid='"+reportbill.getHVO().getPk_reportbill()+"'");
			pushCheckBill.pushSaveFromC003(reportbill);
		} catch (BusinessException ex) {
			// TODO 自动生成的 catch 块
		      ExceptionUtils.wrappException(ex);
		}

	}

}
