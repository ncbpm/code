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
 * <b>���ٵ��ⲿƽ̨����ʵ����</b>
 * <p>
 * @version nc6.0
 * @author weizq
 * @time 2011-4-21 ����08:06:03
 */
public class ReduceImportImpl extends ReduceImpl implements IReduceImport {
	/**
	 * ����ǰ������
	 * @param insertAction
	 * @see nc.impl.am.bill.BillBaseDAO#initInsertActionRules(nc.impl.am.bill.action.BillInsertAction)
	 */
	@Override
	protected void initInsertActionRules(IActionTemplate<ReduceVO> insertAction) {
		//super.initInsertActionRules(insertAction);
		
		// ����У����
	    List<Validator> validators = new ArrayList<Validator>();
	    //У�鿨Ƭ�Ϸ���
	    validators.add(new CheckCardLegalValidator());
	    //У����Сδ������
	    validators.add(new CheckMinUnClosedBookPeriodValidator());
        // ����жϲ����Ƿ��Ѿ��Ƴ��۾��嵥��У�顣
        validators.add(new ReduceCheckHasVoucherForAccbooks());
        
	    insertAction.addBeforeRule(new ValidateServiceRule<ReduceVO>(validators));
		
	    //���Ӹ��¿�Ƭ��־���򣬸��¡�bill_code��bill_type��bill_status��
	    insertAction.addBeforeRule(new UpdateCardLogAfterRule<ReduceVO>(false));
		// ���ݺŴ���
		// insertAction.addBeforeRule(new BillCodeInsertBeforeRule<ReduceVO>());
		// �̶��ʲ��˲�����
		insertAction.addBeforeRule(new PKAccbookBeforeRule<ReduceVO>());
		// ���ݺŴ���
		//insertAction.addAfterRule(new BillCodeInsertAfterRule<ReduceVO>());
	}
	
	/**
	 * ���ٵ��ⲿ����ƽ̨���븨����
	 */
	@Override
	public ReduceVO importInsert(ReduceVO billVO, List<String> showAlterKeyList)
			throws BusinessException {
		//����
		ReduceHeadVO headVO = ((ReduceHeadVO)billVO.getParentVO());
		
		// ���õ���״̬Ϊ����ͨ��
		headVO.setBill_status(BillStatusConst.check_pass);
		
		// ���������˺�����ʱ��
		// ��������ļ����������˺�����ʱ�䣬��ȡ��������ݣ���������ȡ��ǰҵ�����ڡ�
		
		String auditor = headVO.getAuditor();
		UFDateTime audittime = headVO.getAudittime();
		// ���Ϊ����ע�뵱ǰҵ����Ϣ
		if(StringUtils.isEmpty(auditor) && null == audittime){
			headVO.setAuditor(BizContext.getInstance().getUserId());
			headVO.setAudittime(BizContext.getInstance().getBizDateTime());
		}

		// ������Ϣ
		String pk_accbook_scale = headVO.getPk_accbook_scale();
		String pk_accbook = headVO.getPk_accbook_scale();
		if(StringUtils.isEmpty(pk_accbook_scale)){
			if(StringUtils.isEmpty(pk_accbook)){
				// �����ҵ���¼��٣���ȡ��ǰ��֯���˲�
				String pk_org = headVO.getPk_org();
				if(StringUtils.isNotEmpty(pk_org)){
					// ������˲�
					String pk_main_accbook = AccbookManager.queryMainAccbookIDByOrg(pk_org);
					// ע�뾫���˲�
					if(StringUtils.isNotEmpty(pk_main_accbook)){
						headVO.setPk_accbook_scale(pk_main_accbook);
					}
				}
			}else{
				// ע�뾫���˲�
				headVO.setPk_accbook_scale(pk_accbook);
			}
		}
		
		billVO = insertReduceVO(null, billVO);
		
		return billVO;
		// ��� 
//		2017-07-05 rainbow��Ŀ��ֻ��Ҫ��������״̬
//		return approveReduceVO(null, billVO);
	}

	@Override
	public ReduceVO[] queryReduceVO(String pk) throws BusinessException {
		
		return queryReduceVOByPKs(new String[]{pk});
	}

}
