package nc.vo.so.m30.rule;

import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderHVO;
import nc.vo.so.m30.revise.entity.SaleOrderHistoryBVO;

/**
 * ���۶����޶��ֶ� �޶�Ӱ���ֶ� ����
 * 
 * @since 6.36
 * @version 2015-1-6 ����5:37:30
 * @author wangshu6
 */
public class EditableAndRewiteItems {

  // ע���ͷ�޶����Ա༭���ֶ�
  public static final String[] HEADEDITABLEITEMKEY = new String[] {
    /** ---------- ��ͷ ---------- */
    SaleOrderHVO.CTRADEWORDID, SaleOrderHVO.VREVISEREASON, SaleOrderHVO.VNOTE,
    /** -------��ͷ�Զ�����------ **/
    SaleOrderHVO.VDEF1, SaleOrderHVO.VDEF2, SaleOrderHVO.VDEF3,
    SaleOrderHVO.VDEF4, SaleOrderHVO.VDEF5, SaleOrderHVO.VDEF6,
    SaleOrderHVO.VDEF7, SaleOrderHVO.VDEF8, SaleOrderHVO.VDEF9,
    SaleOrderHVO.VDEF10, SaleOrderHVO.VDEF11, SaleOrderHVO.VDEF12,
    SaleOrderHVO.VDEF13, SaleOrderHVO.VDEF14, SaleOrderHVO.VDEF15,
    SaleOrderHVO.VDEF16, SaleOrderHVO.VDEF17, SaleOrderHVO.VDEF18,
    SaleOrderHVO.VDEF19, SaleOrderHVO.VDEF20,
  };

  // ע������޶����Ա༭���ֶ� 
  //2017-08-24 liyf �����൵����ģ��汾Ҳһ���޸�
  public static final String[] BODYEDITABLEITEMKEY = new String[] {
	  //2017-08-24 liyf �����൵����ģ��汾Ҳһ���޸�

	  SaleOrderBVO.CMATERIALID,
	  // �����֯��������֯
	    SaleOrderBVO.CSENDSTOCKORGID,
	    SaleOrderBVO.CTRAFFICORGID,
	  
	    // ���������֯��Ӧ����֯
	    SaleOrderBVO.CSETTLEORGID,
	    SaleOrderBVO.CARORGID,
	    // ��������
	    SaleOrderBVO.CPROFITCENTERID,
	    // ������������
	    SaleOrderBVO.CSPROFITCENTERID,
	    //2017-08-24 liyf �����൵����ģ��汾Ҳһ���޸�


    /** ---------- ���� ---------- */
    // �ͻ����ϱ��롢���ϱ��롢
    SaleOrderBVO.CCUSTMATERIALID,
    SaleOrderBVO.CMATERIALVID,
    //���������������
    SaleOrderBVO.NVOLUME,
    SaleOrderBVO.NWEIGHT,
    SaleOrderBVO.NPIECE,

    // ������֯�����۵�λ����λ
    SaleOrderBVO.PK_ORG,
    SaleOrderBVO.CQTUNITID,
    SaleOrderBVO.CASTUNITID,
    // ��Ʒ�������ȼ���˰��
    SaleOrderBVO.BLARGESSFLAG,
    SaleOrderBVO.CQUALITYLEVELID,
    SaleOrderBVO.CTAXCODEID,

    // ��������������������
    SaleOrderBVO.NASTNUM,
    SaleOrderBVO.NNUM,

    // ���۵�λ���������ۻ����ʡ�˰��
    SaleOrderBVO.NQTUNITNUM,
    SaleOrderBVO.NTAXRATE,
    // ����˰���ۡ�����˰���ۡ�����˰���ۡ�����˰���ۡ�
    SaleOrderBVO.NORIGTAXPRICE,
    SaleOrderBVO.NORIGPRICE,
    SaleOrderBVO.NORIGTAXNETPRICE,
    SaleOrderBVO.NORIGNETPRICE,
    // ��˰���� ����˰���ۡ���˰���ۡ���˰����
    SaleOrderBVO.NQTORIGTAXPRICE,
    SaleOrderBVO.NQTORIGPRICE,
    SaleOrderBVO.NQTORIGTAXNETPRC,
    SaleOrderBVO.NQTORIGNETPRICE,
    // ��˰����˰�ϼơ���˰���
    SaleOrderBVO.NORIGMNY,
    SaleOrderBVO.NORIGTAXMNY,
    // ����˰��
    SaleOrderBVO.NTAX,
    // �ۿ۶�۱�����
    SaleOrderBVO.NORIGDISCOUNT,
    SaleOrderBVO.NEXCHANGERATE,
    // �޶�����
    SaleOrderBVO.VBREVISEREASON,
    // �����֯��������֯
    SaleOrderBVO.CSENDSTOCKORGVID,
    SaleOrderBVO.CTRAFFICORGVID,
    //�����ֿ�
    SaleOrderBVO.CSENDSTORDOCID,
    // ���������֯��Ӧ����֯
    SaleOrderBVO.CSETTLEORGVID,
    SaleOrderBVO.CARORGVID,
    // ��������
    SaleOrderBVO.CPROFITCENTERVID,
    // ������������
    SaleOrderBVO.CSPROFITCENTERVID,
    // ��������������
    SaleOrderBVO.DSENDDATE,
    SaleOrderBVO.DRECEIVEDATE,
    // �������ҵ������ջ����ҵ�������˰���ҵ������������͡�����ó�ס�
    SaleOrderBVO.CSENDCOUNTRYID,
    SaleOrderBVO.CRECECOUNTRYID,
    SaleOrderBVO.CTAXCOUNTRYID,
    SaleOrderBVO.FBUYSELLFLAG,
    SaleOrderBVO.BTRIATRADEFLAG,
    // �б�ע
    SaleOrderBVO.VROWNOTE, SaleOrderBVO.CMFFILEID, SaleOrderBVO.NMFFILEPRICE,
    /** �̶��������� */
    SaleOrderBVO.CPRODUCTORID, SaleOrderBVO.CPROJECTID, SaleOrderBVO.CVENDORID,
    SaleOrderBVO.CMFFILEID,
    /** �������� */
    SaleOrderBVO.VFREE1, SaleOrderBVO.VFREE2, SaleOrderBVO.VFREE3,
    SaleOrderBVO.VFREE4, SaleOrderBVO.VFREE5, SaleOrderBVO.VFREE6,
    SaleOrderBVO.VFREE7, SaleOrderBVO.VFREE9, SaleOrderBVO.VFREE8,
    SaleOrderBVO.VFREE10,
    /** -------�����Զ�����------ **/
    SaleOrderBVO.VBDEF1, SaleOrderBVO.VBDEF2, SaleOrderBVO.VBDEF3,
    SaleOrderBVO.VBDEF4, SaleOrderBVO.VBDEF5, SaleOrderBVO.VBDEF6,
    SaleOrderBVO.VBDEF7, SaleOrderBVO.VBDEF8, SaleOrderBVO.VBDEF9,
    SaleOrderBVO.VBDEF10, SaleOrderBVO.VBDEF11, SaleOrderBVO.VBDEF12,
    SaleOrderBVO.VBDEF13, SaleOrderBVO.VBDEF14, SaleOrderBVO.VBDEF15,
    SaleOrderBVO.VBDEF16, SaleOrderBVO.VBDEF17, SaleOrderBVO.VBDEF18,
    SaleOrderBVO.VBDEF19, SaleOrderBVO.VBDEF20
  };

  // ���������ε��ݿ��޶��ֶ�
  public static final String[] EDITABLEITEMKEYFOROUT = new String[] {

    // ��������������
    SaleOrderBVO.NASTNUM,
    SaleOrderBVO.NNUM,
    // ���۵�λ������
    SaleOrderBVO.NQTUNITNUM,
    // ��˰���ۡ���˰���ۡ���˰���ۡ���˰����
    SaleOrderBVO.NQTORIGPRICE,
    SaleOrderBVO.NQTORIGTAXPRICE,
    SaleOrderBVO.NQTORIGNETPRICE,
    SaleOrderBVO.NQTORIGTAXNETPRC,
    // ����˰���ۡ�����˰����
    SaleOrderBVO.NORIGTAXPRICE,
    SaleOrderBVO.NORIGPRICE,
    // ����˰���ۡ�����˰���ۡ�
    SaleOrderBVO.NORIGTAXNETPRICE,
    SaleOrderBVO.NORIGNETPRICE,
    // ��˰�ϼơ�
    SaleOrderBVO.NORIGTAXMNY,
    // ��˰��˰��ۿ۶�
    SaleOrderBVO.NORIGMNY,
    SaleOrderBVO.NTAX,
    // �۱�����
    SaleOrderBVO.NEXCHANGERATE,
    // ��ע
    SaleOrderBVO.VROWNOTE, SaleOrderHVO.VNOTE,
    /** -------��ͷ�Զ�����------ **/
    SaleOrderHVO.VDEF1, SaleOrderHVO.VDEF2, SaleOrderHVO.VDEF3,
    SaleOrderHVO.VDEF4, SaleOrderHVO.VDEF5, SaleOrderHVO.VDEF6,
    SaleOrderHVO.VDEF7, SaleOrderHVO.VDEF8, SaleOrderHVO.VDEF9,
    SaleOrderHVO.VDEF10, SaleOrderHVO.VDEF11, SaleOrderHVO.VDEF12,
    SaleOrderHVO.VDEF13, SaleOrderHVO.VDEF14, SaleOrderHVO.VDEF15,
    SaleOrderHVO.VDEF16, SaleOrderHVO.VDEF17, SaleOrderHVO.VDEF18,
    SaleOrderHVO.VDEF19, SaleOrderHVO.VDEF20,
    /** -------�����Զ�����------ **/
    SaleOrderBVO.VBDEF1, SaleOrderBVO.VBDEF2, SaleOrderBVO.VBDEF3,
    SaleOrderBVO.VBDEF4, SaleOrderBVO.VBDEF5, SaleOrderBVO.VBDEF6,
    SaleOrderBVO.VBDEF7, SaleOrderBVO.VBDEF8, SaleOrderBVO.VBDEF9,
    SaleOrderBVO.VBDEF10, SaleOrderBVO.VBDEF11, SaleOrderBVO.VBDEF12,
    SaleOrderBVO.VBDEF13, SaleOrderBVO.VBDEF14, SaleOrderBVO.VBDEF15,
    SaleOrderBVO.VBDEF16, SaleOrderBVO.VBDEF17, SaleOrderBVO.VBDEF18,
    SaleOrderBVO.VBDEF19, SaleOrderBVO.VBDEF20

  };

  // ���д��ͷ�ֶ�
  public static final String[] HEADREWRITEMKEY = new String[] {
    /** ---------- ��ͷ ---------- */
    SaleOrderHVO.VREVISEREASON, SaleOrderHVO.VNOTE,SaleOrderHVO.TREVISETIME,
    /** -------��ͷ�Զ�����------ **/
    SaleOrderHVO.VDEF1, SaleOrderHVO.VDEF2, SaleOrderHVO.VDEF3,
    SaleOrderHVO.VDEF4, SaleOrderHVO.VDEF5, SaleOrderHVO.VDEF6,
    SaleOrderHVO.VDEF7, SaleOrderHVO.VDEF8, SaleOrderHVO.VDEF9,
    SaleOrderHVO.VDEF10, SaleOrderHVO.VDEF11, SaleOrderHVO.VDEF12,
    SaleOrderHVO.VDEF13, SaleOrderHVO.VDEF14, SaleOrderHVO.VDEF15,
    SaleOrderHVO.VDEF16, SaleOrderHVO.VDEF17, SaleOrderHVO.VDEF18,
    SaleOrderHVO.VDEF19, SaleOrderHVO.VDEF20,
    /** -------�޶���Ϣ------- */
    SaleOrderHVO.CREVISERID, SaleOrderHVO.APPROVER, SaleOrderHVO.IVERSION
  };

  // �����д
  public static final String[] BODYREWRITEMKEY = new String[] {

    /** ---------- ���� ---------- */
    // ������λ
    // ��λ�����۵�λ
    SaleOrderBVO.CASTUNITID,
    SaleOrderBVO.CQTUNITID,
    // ������ ���۵�λ������
    SaleOrderBVO.VCHANGERATE,
    SaleOrderBVO.VQTUNITRATE,
    // ����
    // �۱����ʡ����ű��һ��ʡ�ȫ�ֱ��һ���
    SaleOrderBVO.NEXCHANGERATE,
    SaleOrderBVO.NGROUPEXCHGRATE,
    SaleOrderBVO.NGLOBALTAXMNY,
    // ����˰���
    SaleOrderBVO.NCALTAXMNY,
    // ˰��
    SaleOrderBVO.NTAXRATE,
    // �ۿ���
    SaleOrderBVO.NORIGDISCOUNT,
    
    //���������������
    SaleOrderBVO.NVOLUME,
    SaleOrderBVO.NWEIGHT,
    SaleOrderBVO.NPIECE,

    // ���������֯��������֯��
    SaleOrderBVO.CSENDSTOCKORGID,
    SaleOrderBVO.CSENDSTOCKORGVID,
    SaleOrderBVO.CTRAFFICORGID,
    SaleOrderBVO.CTRAFFICORGVID,
    // ���������֯
    SaleOrderBVO.CSETTLEORGVID,
    SaleOrderBVO.CSETTLEORGID,
    // �����������ġ�������������
    SaleOrderBVO.CSPROFITCENTERID,
    SaleOrderBVO.CSPROFITCENTERVID,
    SaleOrderBVO.CPROFITCENTERID,
    SaleOrderBVO.CPROFITCENTERVID,
    // �����ۿۡ���Ʒ�ۿ�
    SaleOrderBVO.NDISCOUNTRATE,
    SaleOrderBVO.NITEMDISCOUNTRATE,
    // ����
    // ����������������������
    SaleOrderBVO.NASTNUM,
    SaleOrderBVO.NNUM,
    SaleOrderBVO.NQTUNITNUM,

    // ����
    // ��˰���ۡ���˰���ۡ���˰���ۡ���˰����
    SaleOrderBVO.NQTORIGPRICE,
    SaleOrderBVO.NQTORIGTAXPRICE,
    SaleOrderBVO.NQTORIGNETPRICE,
    SaleOrderBVO.NQTORIGTAXNETPRC,

    // ����λ��˰���ۡ�����λ��˰���ۡ�����λ��˰���ۡ�����λ��˰����
    SaleOrderBVO.NORIGTAXPRICE,
    SaleOrderBVO.NORIGPRICE,
    SaleOrderBVO.NORIGTAXNETPRICE,
    SaleOrderBVO.NORIGNETPRICE,
    // ������˰���ۡ����Һ�˰���ۡ�������˰���ۡ����Һ�˰���ۡ�
    SaleOrderBVO.NQTPRICE,
    SaleOrderBVO.NQTTAXPRICE,
    SaleOrderBVO.NQTNETPRICE,
    SaleOrderBVO.NQTTAXNETPRICE,
    // ����λ���Һ�˰���ۡ�����λ������˰���ۡ�����λ���Һ�˰���ۡ�����λ������˰���ۡ�
    SaleOrderBVO.NTAXPRICE,
    SaleOrderBVO.NPRICE,
    SaleOrderBVO.NTAXNETPRICE,
    SaleOrderBVO.NNETPRICE,
    // ѯ��ԭ�Һ�˰���ۡ�ѯ��ԭ����˰����
    SaleOrderBVO.NASKQTORIGTAXPRC,
    SaleOrderBVO.NASKQTORIGPRICE,
    // ���
    // ��˰����˰�ϼơ�˰��ۿ۶�
    SaleOrderBVO.NORIGMNY,
    SaleOrderBVO.NORIGTAXMNY,
    SaleOrderBVO.NTAX,
    SaleOrderBVO.NORIGDISCOUNT,
    // ������˰�����Ҽ�˰�ϼơ�����˰������ۿ۶
    SaleOrderBVO.NMNY, SaleOrderBVO.NTAXMNY,
    SaleOrderBVO.NTAX,
    SaleOrderBVO.NDISCOUNT,
    // ���ű�����˰�����ű��Ҽ�˰�ϼơ�ȫ�ֱ�����˰��ȫ�ֱ��Ҽ�˰�ϼ�
    SaleOrderBVO.NGROUPMNY, SaleOrderBVO.NGROUPTAXMNY, SaleOrderBVO.NGLOBALMNY,
    SaleOrderBVO.NGLOBALTAXMNY,
    // �۸���أ��۸���ɡ��۸����
    SaleOrderBVO.CPRICEFORMID, SaleOrderBVO.CPRICEITEMID,
    SaleOrderBVO.CPRICEITEMTABLEID, SaleOrderBVO.CPRICEPOLICYID
  };

  // �ۼ���������
  public static final String[] TOTALNUMKEY = new String[] {
    // �ۼƷ����������ۼƿ�Ʊ����
    SaleOrderHistoryBVO.NTOTALSENDNUM, SaleOrderHistoryBVO.NTOTALINVOICENUM,
    // �ۼƳ��������� �ۼ�Ӧ��δ��������
    SaleOrderHistoryBVO.NTOTALOUTNUM, SaleOrderHistoryBVO.NTOTALNOTOUTNUM,
    // �ۼ�ǩ�������� �ۼ�;������
    SaleOrderHistoryBVO.NTOTALSIGNNUM, SaleOrderHistoryBVO.NTRANSLOSSNUM,
    // �ۼƳ���Գ��������ۼ��ݹ�Ӧ������
    SaleOrderHistoryBVO.NTOTALRUSHNUM, SaleOrderHistoryBVO.NTOTALESTARNUM,
    // �ۼ�ȷ��Ӧ���������ۼƳɱ���������
    SaleOrderHistoryBVO.NTOTALARNUM, SaleOrderHistoryBVO.NTOTALCOSTNUM,
    // �ۼ��ݹ�Ӧ�ս� �ۼ�ȷ��Ӧ�ս��
    SaleOrderHistoryBVO.NTOTALESTARMNY, SaleOrderHistoryBVO.NTOTALARMNY,
    // �ۼư���ί�ⶩ���������ۼư����빺������
    SaleOrderHistoryBVO.NARRANGESCORNUM, SaleOrderHistoryBVO.NARRANGEPOAPPNUM,
    // �ۼư��ŵ��������������ۼư��ŵ�����������
    SaleOrderHistoryBVO.NARRANGETOORNUM, SaleOrderHistoryBVO.NARRANGETOAPPNUM,
    // �ۼư������������������ۼư��Ųɹ���������
    SaleOrderHistoryBVO.NARRANGEMONUM, SaleOrderHistoryBVO.NARRANGEPONUM,
    // �ۼƷ�����Ʒ�� �ۼ��˻�����
    SaleOrderHistoryBVO.NTOTALRETURNNUM, SaleOrderHistoryBVO.NTOTALTRADENUM
  };

  // �����ֶ�
  // �ۼ���������
  public static final String[] PRICE = new String[] {

    // ��˰���ۡ���˰���ۡ���˰���ۡ���˰����
    SaleOrderBVO.NQTORIGPRICE,
    SaleOrderBVO.NQTORIGTAXPRICE,
    SaleOrderBVO.NQTORIGNETPRICE,
    SaleOrderBVO.NQTORIGTAXNETPRC,
    // ����λ��˰���ۡ�����λ��˰���ۡ�����λ��˰���ۡ�����λ��˰����
    SaleOrderBVO.NORIGTAXPRICE, SaleOrderBVO.NORIGPRICE,
    SaleOrderBVO.NORIGTAXNETPRICE,
    SaleOrderBVO.NORIGNETPRICE,
    // ������˰���ۡ����Һ�˰���ۡ�������˰���ۡ����Һ�˰���ۡ�
    SaleOrderBVO.NQTPRICE, SaleOrderBVO.NQTTAXPRICE, SaleOrderBVO.NQTNETPRICE,
    SaleOrderBVO.NQTTAXNETPRICE,
    // ����λ���Һ�˰���ۡ�����λ������˰���ۡ�����λ���Һ�˰���ۡ�����λ������˰���ۡ�
    SaleOrderBVO.NTAXPRICE, SaleOrderBVO.NPRICE, SaleOrderBVO.NTAXNETPRICE,
    SaleOrderBVO.NNETPRICE,
    // ѯ��ԭ�Һ�˰���ۡ�ѯ��ԭ����˰����
    SaleOrderBVO.NASKQTORIGTAXPRC, SaleOrderBVO.NASKQTORIGPRICE
  };

}
