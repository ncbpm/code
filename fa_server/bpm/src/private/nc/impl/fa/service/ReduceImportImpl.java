package nc.impl.fa.service;

import java.util.ArrayList;
import java.util.List;

import nc.bs.am.framework.action.IActionTemplate;
import nc.bs.am.framework.common.rule.ValidateServiceRule;
import nc.bs.uif2.validation.Validator;
import nc.impl.fa.common.rule.UpdateCardLogAfterRule;
import nc.impl.fa.common.validator.CheckCardLegalValidator;
import nc.impl.fa.common.validator.CheckMinUnClosedBookPeriodValidator;
import nc.impl.fa.reduce.ReduceImpl;
import nc.impl.fa.rule.PKAccbookBeforeRule;
import nc.itf.fa.service.IReduceImport;
import nc.vo.am.common.BizContext;
import nc.vo.am.common.util.StringUtils;
import nc.vo.am.constant.BillStatusConst;
import nc.vo.am.manager.AccbookManager;
import nc.vo.fa.reduce.ReduceHeadVO;
import nc.vo.fa.reduce.ReduceVO;
import nc.vo.fa.reduce.validator.ReduceCheckHasVoucherForAccbooks;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
/**
 * 
 * <p>
 * <b>减少单外部平台导入实现类</b>
 * <p>
 * @version nc6.0
 * @author weizq
 * @time 2011-4-21 下午08:06:03
 */
public class ReduceImportImpl extends ReduceImpl implements IReduceImport {
	/**
	 * 插入前规则类
	 * @param insertAction
	 * @see nc.impl.am.bill.BillBaseDAO#initInsertActionRules(nc.impl.am.bill.action.BillInsertAction)
	 */
	@Override
	protected void initInsertActionRules(IActionTemplate<ReduceVO> insertAction) {
		//super.initInsertActionRules(insertAction);
		
		// 保存校验类
	    List<Validator> validators = new ArrayList<Validator>();
	    //校验卡片合法性
	    validators.add(new CheckCardLegalValidator());
	    //校验最小未结账月
	    validators.add(new CheckMinUnClosedBookPeriodValidator());
        // 添加判断财务是否已经制成折旧清单的校验。
        validators.add(new ReduceCheckHasVoucherForAccbooks());
        
	    insertAction.addBeforeRule(new ValidateServiceRule<ReduceVO>(validators));
		
	    //增加更新卡片日志规则，更新【bill_code，bill_type，bill_status】
	    insertAction.addBeforeRule(new UpdateCardLogAfterRule<ReduceVO>(false));
		// 单据号处理
		// insertAction.addBeforeRule(new BillCodeInsertBeforeRule<ReduceVO>());
		// 固定资产账簿处理
		insertAction.addBeforeRule(new PKAccbookBeforeRule<ReduceVO>());
		// 单据号处理
		//insertAction.addAfterRule(new BillCodeInsertAfterRule<ReduceVO>());
	}
	
	/**
	 * 减少单外部交换平台导入辅助类
	 */
	@Override
	public ReduceVO importInsert(ReduceVO billVO, List<String> showAlterKeyList)
			throws BusinessException {
		//保存
		ReduceHeadVO headVO = ((ReduceHeadVO)billVO.getParentVO());
		
		// 设置单据状态为审批通过
		headVO.setBill_status(BillStatusConst.check_pass);
		
		// 设置审批人和审批时间
		// 如果导入文件存在审批人和审批时间，则取导入的数据；不存在则取当前业务日期。
		
		String auditor = headVO.getAuditor();
		UFDateTime audittime = headVO.getAudittime();
		// 如果为空则注入当前业务信息
		if(StringUtils.isEmpty(auditor) && null == audittime){
			headVO.setAuditor(BizContext.getInstance().getUserId());
			headVO.setAudittime(BizContext.getInstance().getBizDateTime());
		}

		// 补充信息
		String pk_accbook_scale = headVO.getPk_accbook_scale();
		String pk_accbook = headVO.getPk_accbook_scale();
		if(StringUtils.isEmpty(pk_accbook_scale)){
			if(StringUtils.isEmpty(pk_accbook)){
				// 如果是业务下减少，则取当前组织主账簿
				String pk_org = headVO.getPk_org();
				if(StringUtils.isNotEmpty(pk_org)){
					// 获得主账簿
					String pk_main_accbook = AccbookManager.queryMainAccbookIDByOrg(pk_org);
					// 注入精度账簿
					if(StringUtils.isNotEmpty(pk_main_accbook)){
						headVO.setPk_accbook_scale(pk_main_accbook);
					}
				}
			}else{
				// 注入精度账簿
				headVO.setPk_accbook_scale(pk_accbook);
			}
		}
		
		billVO = insertReduceVO(null, billVO);
		
		return billVO;
		// 审核 
//		2017-07-05 rainbow项目，只需要导入自由状态
//		return approveReduceVO(null, billVO);
	}

	@Override
	public ReduceVO[] queryReduceVO(String pk) throws BusinessException {
		
		return queryReduceVOByPKs(new String[]{pk});
	}

}
