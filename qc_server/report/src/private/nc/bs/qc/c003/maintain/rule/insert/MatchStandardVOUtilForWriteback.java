package nc.bs.qc.c003.maintain.rule.insert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.pubitf.qc.checkstandard.QueryStdChkItemPara;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.qc.c001.entity.ApplyHeaderVO;
import nc.vo.qc.c001.entity.ApplyItemVO;
import nc.vo.qc.c001.entity.ApplyVO;
import nc.vo.qc.c001.util.AbstractMatchStandardUtil;
import nc.vo.qc.c001.util.ApplyPublicUtil;
import nc.vo.qc.c001transtype.entity.ApplyTranstypeVO;
import nc.vo.qc.checkstandard.entity.CheckStandardItemVO;
import nc.vo.qc.checkstandardmatch.entity.ChkStdMatchVO;
import nc.vo.qc.materchecktype.enumeration.CheckPointEnum;
import nc.vo.qc.pub.constant.QCConstant;
import nc.vo.qc.pub.util.ApplyTransTypeUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * <b>������Ҫ������¹��ܣ�</b
 */
public class MatchStandardVOUtilForWriteback extends AbstractMatchStandardUtil {

  private ApplyTranstypeVO applyTranstypeVO;

  private ApplyVO bill;

  private List<String> matchparas;

  public MatchStandardVOUtilForWriteback(ApplyVO vo) {
    this.bill = vo;
  }

  public  CheckStandardItemVO[] constructChkItem(QueryStdChkItemPara[] paras) {
    List<CheckStandardItemVO> retArrays = new ArrayList<CheckStandardItemVO>();
    // ��¼��ǰ���������м�����Ŀ����
    List<String> allItemPKList = new ArrayList<String>();
    for (QueryStdChkItemPara para : paras) {
      // ��¼ÿ����׼��������Ŀ����
      for (CheckStandardItemVO chkItem : para.getChkItems()) {
        if (allItemPKList.contains(chkItem.getPk_checkitem())) {
          // ����ļ�����Ŀ�����ظ�
          continue;
        }
        allItemPKList.add(chkItem.getPk_checkitem());
        retArrays.add(chkItem);
      }
    }
    return retArrays.toArray(new CheckStandardItemVO[0]);
  }

  private void insertChkItem(QueryStdChkItemPara[] stdChkItems,
      CheckStandardItemVO[] chkItems) {
    if (stdChkItems == null || chkItems == null) {
      return;
    }
    // �õ�������ĵ����°汾ID
    Map<String, String> centeroid_vid = this.getChkCenterVID(stdChkItems);
    // ���쵥���ʼ�����
    ApplyItemVO[] items = new ApplyItemVO[chkItems.length];
    for (int i = 0, len = chkItems.length; i < len; i++) {
      items[i] = new ApplyItemVO();
      // �к�
      items[i].setCrowno(String.valueOf((i + 1) * 10));
      // ��Ŀ����
      String chkitemPK = chkItems[i].getPk_checkitem();
      items[i].setPk_checkitem(chkitemPK);
      // �ؼ���Ŀ
      items[i].setBkeyitem(chkItems[i].getBkeyitem());
      // Ĭ����Ŀ
      items[i].setBdefaultitem(chkItems[i].getBdefaultitem());
      // ����ﵽ
      items[i].setBmustreach(chkItems[i].getBmustreach());
      // ȡ����ʽ
      items[i].setPk_bcheckmode(chkItems[i].getPk_checkmode());
      // ������ġ��ⲿ�ʼ����(���鷽�������϶��߲�����ͬʱ��ֵ)
      String workcenter = chkItems[i].getPk_workcenter();
      if (!StringUtils.isEmpty(workcenter)) {
        items[i].setPk_chkcenter(workcenter);
        items[i].setPk_chkcenter_v(centeroid_vid.get(workcenter));
      }
      String outsupplier = chkItems[i].getPk_outsupplier();
      items[i].setPk_outsupplier(outsupplier);
      // ������ݡ���ⷽ��
      items[i].setPk_checkbasis(chkItems[i].getPk_checkbasis());
      items[i].setPk_checkbasis_b(chkItems[i].getPk_checkmethod());
      for (int j = 0, stdlen = stdChkItems.length; j < stdlen; j++) {
        // ��׼
        String code = QCConstant.APPLY_STD_CODE + (j + 1);
        String vcode = code + QCConstant.APPLY_STDV_SUFFIX;
        String valueCode = QCConstant.APPLY_STDVALUE_CODE + +(j + 1);
        String bincludeCode = QCConstant.APPLY_INCLUDED_CODE + (j + 1);
        items[i].setAttributeValue(code, stdChkItems[j].getPk_standard());
        items[i].setAttributeValue(vcode, stdChkItems[j].getPk_standard_v());
        if (stdChkItems[j]
            .getCheckStandardItemVO(chkItems[i].getPk_checkitem()) != null) {
          // �Ƿ��������׼ֵ
          items[i].setAttributeValue(bincludeCode, UFBoolean.TRUE);
          String stdvalue =
              stdChkItems[j].getCheckStandardItemVO(
                  chkItems[i].getPk_checkitem()).getVstandardvalue();
          items[i].setAttributeValue(valueCode, stdvalue);
        }
        else {
          // �Ƿ����
          items[i].setAttributeValue(bincludeCode, UFBoolean.FALSE);
        }
      }
    }
    this.bill.setB1VO(items);
  }

  @Override
  protected ApplyTranstypeVO getApplyTranstypeVO() {
    if (this.applyTranstypeVO == null) {
      String ctrantypeid = this.bill.getHVO().getCtrantypeid();
      this.applyTranstypeVO =
          ApplyTransTypeUtil.getApplyTransTypeVO(ctrantypeid);
    }
    return this.applyTranstypeVO;
  }

  @Override
  protected ChkStdMatchVO getChkStdMatchVO() {
    // ���ϡ���Ӧ�̡��ͻ�����Ŀ
    ApplyHeaderVO head = this.bill.getHVO();
    String pk_group = head.getPk_group();
    String pk_org = head.getPk_org();
    String oid = head.getPk_srcmaterial();
    String pk_supplier = head.getPk_supplier();
    String pk_customer = head.getPk_customer();
    String cprojectid = head.getCprojectid();

    ChkStdMatchVO matchvo = new ChkStdMatchVO();
    matchvo.setPk_group(pk_group);
    matchvo.setPk_org(pk_org);
    matchvo.setPk_material(oid);
    matchvo.setPk_supplier(pk_supplier);
    matchvo.setPk_customer(pk_customer);
    matchvo.setPk_project(cprojectid);    
    
    if(head.getFsourcetype().intValue()==CheckPointEnum.OPERATIONREP||
    		head.getFsourcetype().intValue()==CheckPointEnum.OPERATIONSCRECEIVE)
    {
      matchvo.setPk_processtype(head.getPk_rc());
      matchvo.setPk_factory(head.getPk_stockorg());//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      matchvo.setPk_factory_v(head.getPk_stockorg_v());
    }
    // 10��ƥ����Զ�����
    matchvo.setVdef1((String) head.getAttributeValue(ApplyHeaderVO.VDEF21));
    matchvo.setVdef2((String) head.getAttributeValue(ApplyHeaderVO.VDEF22));
    matchvo.setVdef3((String) head.getAttributeValue(ApplyHeaderVO.VDEF23));
    matchvo.setVdef4((String) head.getAttributeValue(ApplyHeaderVO.VDEF24));
    matchvo.setVdef5((String) head.getAttributeValue(ApplyHeaderVO.VDEF25));
    matchvo.setVdef6((String) head.getAttributeValue(ApplyHeaderVO.VDEF26));
    matchvo.setVdef7((String) head.getAttributeValue(ApplyHeaderVO.VDEF27));
    matchvo.setVdef8((String) head.getAttributeValue(ApplyHeaderVO.VDEF28));
    matchvo.setVdef9((String) head.getAttributeValue(ApplyHeaderVO.VDEF29));
    matchvo.setVdef10((String) head.getAttributeValue(ApplyHeaderVO.VDEF30));
    return matchvo;
  }

  @Override
  protected List<String> getPriorityListPara() {
    if (this.matchparas == null) {
      // ���쵥��Դ����
      Integer type = this.bill.getHVO().getFsourcetype();
      String pk_org = this.bill.getHVO().getPk_org();
      // ƥ���׼�Ĳ���ֵ�б�
      this.matchparas = ApplyPublicUtil.getMatchStdParaList(pk_org, type);
    }
    return this.matchparas;
  }

  @Override
  public  void recordBodyMatchInfo(QueryStdChkItemPara[] stdChkItems) {
    if (ArrayUtils.isEmpty(stdChkItems)) {
      return;
    }
    // ��˳���齨������Ŀ
    CheckStandardItemVO[] chkItems = this.constructChkItem(stdChkItems);
    // ����Ŀ���뵽VO��
    this.insertChkItem(stdChkItems, chkItems);
  }

  @Override
  public void recordHeadMatchInfo(QueryStdChkItemPara[] stdChkItems) {
    if (ArrayUtils.isEmpty(stdChkItems)) {
      return;
    }
    // ��¼��Ĭ�ϼ��鷽����ȡ����ʽ
    ApplyHeaderVO head = this.bill.getHVO();
    String defaultStd = stdChkItems[0].getPk_standard();
    head.setPk_defaultstd(defaultStd);
    head.setPk_defaultstd_v(stdChkItems[0].getPk_standard_v());
    head.setPk_hcheckmode(stdChkItems[0].getPk_hcheckmode());
  }
}
