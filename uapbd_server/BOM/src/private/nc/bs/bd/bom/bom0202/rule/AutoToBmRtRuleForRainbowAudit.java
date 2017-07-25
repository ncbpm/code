package nc.bs.bd.bom.bom0202.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.util.mmf.framework.base.MMArrayUtil;
import nc.vo.bd.bom.bom0202.entity.AggBomVO;
import nc.vo.bd.bom.bom0202.entity.BomVO;
import nc.vo.bd.bom.bom0202.enumeration.FBomBillstatusEnum;
import nc.vo.bd.bom.bom0202.enumeration.VersionTypeEnum;
import nc.vo.pub.VOStatus;

/**
3. .�������ɶ�Ӧ��ʹ����֯��bom/����·��ƥ�����ȡ��������ɾ����Ӧ��ʹ����֯��ʹ�ù���
4.  �������ɷ������֯��bom/����·��ƥ�����ȡ������ɾ��ȡ�������bom/����·��ƥ�����
5. ������֯+����+�汾�ſ���Ψһ��
 * @author liyf
 *
 */

public class AutoToBmRtRuleForRainbowAudit implements IRule<AggBomVO> {

    @Override
    public void process(AggBomVO[] vos) {
        if (MMArrayUtil.isEmpty(vos)) {
            return;
        }
        for (AggBomVO aggBomVO : vos) {
            BomVO head = (BomVO) aggBomVO.getParentVO();
            // ȡ��������Ҫ��bom�汾������Ϊ��Ч(����״̬Ϊ����ͨ����ͨ��)
            if (head.getFbillstatus().intValue() == FBomBillstatusEnum.APPROVE.toIntValue()
                    || head.getFbillstatus().intValue() == FBomBillstatusEnum.NOPASS.toIntValue()) {
                head.setHfversiontype(VersionTypeEnum.UNAVAILABLE.toIntValue());
                // head.setHbdefault(UFBoolean.FALSE);
            }
            aggBomVO.getParentVO().setStatus(VOStatus.UPDATED);
        }

    }

}
