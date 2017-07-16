package nc.bpm.so.m30;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pfxx.ISwapContext;
import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.so.m30.action.main.InsertSaleOrderAction;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.pf.IplatFormEntry;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderHVO;
import nc.vo.so.m30.entity.SaleOrderVO;
import nc.vo.so.m30.pub.SaleOrderVOCalculator;
import nc.vo.so.pub.keyvalue.IKeyValue;
import nc.vo.so.pub.keyvalue.VOKeyValue;
import nc.vo.so.pub.rule.SOTaxInfoRule;

/**
 * BPM���۶�������
 * 
 * BPM���뱣�ֽ����������䣬���㵥��
 * @author liyf
 *
 */
public class M30ForBPMAdd extends AbstractPfxxPlugin {

	@Override
	protected Object processBill(Object vo, ISwapContext swapContext,
			AggxsysregisterVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������

		// 1.�õ�ת�����VO����,ȡ�����򵼵�һ��ע���VO��Ϣ
		AggregatedValueObject resvo = (AggregatedValueObject) vo;
		// 2. У�����ݵĺϷ���:1.���ݽṹ���� 2.������֯+���ݺ�У���Ƿ��ظ�.
		checkData(resvo);
		//3.��ȫ����,���ҵ�������״̬
		fillData(resvo);
		
		//��¼BPM��������������Ϣ
		SaleOrderVO bill = (SaleOrderVO)resvo;
		SaleOrderHVO parentVO = bill.getParentVO();
		String approver = parentVO.getApprover();
		parentVO.setApprover(null);
		if(!StringUtils.isEmpty(parentVO.getBillmaker())){
			InvocationInfoProxy.getInstance().setUserId(parentVO.getBillmaker());
		}
		SaleOrderVO bill2 = (SaleOrderVO) insert(resvo);
		//���²�ѯ����ֹ����
		if(!StringUtils.isEmpty(approver)){
			InvocationInfoProxy.getInstance().setUserId(approver);
		}
		bill2 = query(bill2.getParentVO().getPrimaryKey());
		bill2.getParentVO().setApprover(approver);
		approve(bill2);
		
		return bill2.getParentVO().getPrimaryKey();
	}
	
	private SaleOrderVO query(String hid){
		return new BillQuery<>(SaleOrderVO.class).query(new String[]{hid})[0];
	}

	private void fillData(AggregatedValueObject resvo) {
		// TODO �Զ����ɵķ������
		//��ȫ������Ϣ��BPM��������������ȫ��������
		SaleOrderVO bill = (SaleOrderVO)resvo;
		SaleOrderHVO parentVO = bill.getParentVO();
		SaleOrderBVO[] bvos = bill.getChildrenVO();
		//������״̬
		parentVO.setFpfstatusflag(-1);
		//����״̬������
		parentVO.setFstatusflag(1);
		//
		if (StringUtil.isSEmptyOrNull(parentVO.getCtrantypeid())) {
			// uap��֧�ֵ������͵ķ��룬��ʱ�Խ�������code��ѯid�ķ�ʽ����������
			String vtrantypecode = parentVO.getVtrantypecode();
			Map<String, String> map = PfServiceScmUtil
					.getTrantypeidByCode(new String[] { vtrantypecode });
			parentVO.setCtrantypeid(map == null ? null : map.get(vtrantypecode));
		}
		
		if (StringUtil.isSEmptyOrNull(parentVO.getChreceivecustid())) {
			parentVO.setChreceivecustid(bvos[0].getCreceivecustid());
		}

				
		//��յ��ۣ����ݽ�����������
		String[] attributeNames = bvos[0].getAttributeNames();
		//������Ϣ
		for(SaleOrderBVO bvo:bvos){
			bvo.setCqtunitid(bvo.getCunitid());
			bvo.setVqtunitrate(bvo.getVqtunitrate());
			for(String attname:attributeNames){
				if(attname.endsWith("price")){
					bvo.setAttributeValue(attname, null);
				}
			}
		}
		int rows[] = new int[bvos.length];
		for(int i=0;i<bvos.length;i++){
			rows[i] = i;
		}
		//����˰����Ϣ
	    IKeyValue keyValue = new VOKeyValue<IBill>(bill);
	    // ѯ˰
	    SOTaxInfoRule taxInfo = new SOTaxInfoRule(keyValue);
	    taxInfo.setOnlyTaxCodeByBodyPos(rows);
	    //��������-����˰��
	    if( bvos[0].getNtaxrate() == null ||  bvos[0].getNtaxrate().doubleValue() == 0){
	    	taxInfo.setTaxTypeAndRate(rows);
	    }
	   
		///���ݼ�˰�ϼƣ����㵥�۵�
		SaleOrderVOCalculator cal = new SaleOrderVOCalculator(bill);
		cal.calculate(rows, "norigtaxmny");
	}

	protected AggregatedValueObject insert(AggregatedValueObject billvo) {

		SaleOrderVO[] insertvo = new SaleOrderVO[] { (SaleOrderVO) billvo };
		InsertSaleOrderAction insertact = new InsertSaleOrderAction();
		SaleOrderVO[] retvos = insertact.insert(insertvo);
		if (null == retvos || retvos.length == 0) {
			return null;
		}
		return retvos[0];
	}

	protected AggregatedValueObject approve(AggregatedValueObject billvo)
			throws BusinessException {

		IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator
				.getInstance().lookup(IplatFormEntry.class.getName());
		Object retObj = iIplatFormEntry.processAction("APPROVE", "30", null,
				billvo, null, null);
		return null;
	}

	private void checkData(AggregatedValueObject resvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		if(resvo == null || resvo.getParentVO() == null)
			throw new BusinessException("δ��ȡ��ת���������");
		if(resvo.getChildrenVO() == null || resvo.getChildrenVO().length ==0){
			throw new BusinessException("���岻����Ϊ��");
		}
		SaleOrderHVO head = (SaleOrderHVO) resvo.getParentVO();
		VOQuery<SaleOrderHVO> query = new VOQuery<SaleOrderHVO>(SaleOrderHVO.class);
		SaleOrderHVO[] hvos = query.query(" and pk_org='"+head.getPk_org()+"' and vbillcode='"+head.getVbillcode()+"'", null);
		if(hvos!= null && hvos.length > 0){
			throw new BusinessException(" �Ѿ�������ͬ���ݺŵ����۶���");

		}
		
	}

}
