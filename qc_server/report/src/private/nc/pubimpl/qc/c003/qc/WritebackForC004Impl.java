package nc.pubimpl.qc.c003.qc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.impl.pubapp.pattern.data.vo.tool.VOConcurrentTool;
import nc.pubitf.qc.c003.qc.IWritebackForC004;
import nc.pubitf.qc.c003.qc.WritebackForC004Para;
import nc.vo.ic.storestate.StoreStateVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.HslParseUtil;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.c003.entity.ReportHeaderVO;
import nc.vo.qc.c003.entity.ReportItemVO;

import org.apache.commons.lang.StringUtils;

/**
 * �ʼ챨���ṩ�����ɲ��ϸ�Ʒ�Ļ�д�ӿ�ʵ����
 * 
 * @since 6.0
 * @version 2010-12-1 ����03:53:07
 * @author hanbin
 */
public class WritebackForC004Impl implements IWritebackForC004 {

  // ����,���Ƿ�����������������,����ʽ�ж�,���β���,�����˷������ʼ챨��
  @Override
  public void writebackWhenApprove(WritebackForC004Para[] paras)
      throws BusinessException {
    try {
      // ����д����
      this.validatePara(paras);
      // ��ѯ��Ӧ�ı�ͷVO
      ReportHeaderVO[] heads = this.queryReportHeader(paras);
      // ��ѯ��Ӧ�ı���VO
      ReportItemVO[] items = this.queryReportItem(paras);
      // ����
      new VOConcurrentTool().lock(heads);
      new VOConcurrentTool().lock(items);

      Map<String, WritebackForC004Para> bid_para =
          new HashMap<String, WritebackForC004Para>();
      for (WritebackForC004Para para : paras) {
        bid_para.put(para.getBid(), para);
      }

      for (ReportHeaderVO head : heads) {
        head.setBrejectaudit(UFBoolean.TRUE);
        head.setStatus(VOStatus.UPDATED);
      }
      for (ReportItemVO item : items) {
        String bid = item.getPk_reportbill_b();
        
        item.setPk_chgmrl(bid_para.get(bid).getPk_chgmrl());
        item.setPk_chgsrcmrl(bid_para.get(bid).getPk_chgsrcmrl());
        item.setPk_chargedept(bid_para.get(bid).getPk_chargedept());
        item.setPk_chargedept_v(bid_para.get(bid).getPk_chargedept_v());
        item.setCunitid(bid_para.get(bid).getCunitid());
        item.setCastunitid(bid_para.get(bid).getCastunitid());
        item.setVchangerate(bid_para.get(bid).getVchangerate());
        item.setNnum(bid_para.get(bid).getNnum());
        item.setNastnum(bid_para.get(bid).getNastnum());
        item.setPk_chargepsn(bid_para.get(bid).getPk_chargepsn());
        item.setFprocessjudge(bid_para.get(bid).getFprocessjudge());
        // ���ݸ��������Ƿ�Ϊ�����á��Ƿ���С����
        boolean bchg = !StringUtils.isEmpty(bid_para.get(bid).getPk_chgmrl());
        item.setBchanged(UFBoolean.valueOf(bchg));
     // 2017-07-17 liyf rainbow ���ϸ�����Ĵ���ʽ���յ��ж�����������⣬��Ҫͬʱ��д�ʼ챨����״̬Ϊ���á�
        //1=���  5= �����
        if(1==bid_para.get(bid).getFprocessjudge() ){
        	VOQuery<StoreStateVO> query = new VOQuery<StoreStateVO>(StoreStateVO.class);
        	StoreStateVO[] query2 = query.query(" and dr=0 and vname='����'", null);
        	if(query2!=null&&query2.length >0){
            	item.setPk_afterstockstate(query2[0].getPk_storestate());	

        	}
        }else if(5==bid_para.get(bid).getFprocessjudge() ){
        	VOQuery<StoreStateVO> query = new VOQuery<StoreStateVO>(StoreStateVO.class);
        	StoreStateVO[] query2 = query.query(" and dr=0 and vname='������'", null);
        	if(query2!=null&&query2.length >0){
            	item.setPk_afterstockstate(query2[0].getPk_storestate());	

        	}
        }
        item.setStatus(VOStatus.UPDATED);
      }

      String[] names1 = new String[] {
        ReportHeaderVO.BREJECTAUDIT
      };
      String[] names2 = new String[12];
      names2[0] = ReportItemVO.PK_CHGMRL;
      names2[1] = ReportItemVO.PK_CHGSRCMRL;
      names2[2] = ReportItemVO.PK_CHARGEDEPT;
      names2[3] = ReportItemVO.PK_CHARGEDEPT_V;
      names2[4] = ReportItemVO.CUNITID;
      names2[5] = ReportItemVO.CASTUNITID;
      names2[6] = ReportItemVO.VCHANGERATE;
      names2[7] = ReportItemVO.NASTNUM;
      names2[8] = ReportItemVO.PK_CHARGEPSN;
      names2[9] = ReportItemVO.FPROCESSJUDGE;
      names2[10] = ReportItemVO.BCHANGED;
      names2[11] = ReportItemVO.PK_AFTERSTOCKSTATE;

      new VOUpdate<ReportHeaderVO>().update(heads, names1);
      new VOUpdate<ReportItemVO>().update(items, names2);

    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
  }

  // ɾ��ʱ��д�ʼ챨���ͷ�Ĳ��ϸ�Ʒ�����������Ͳ��ϸ�Ʒ���ݺ�
  @Override
  public void writebackWhenDelete(WritebackForC004Para[] paras)
      throws BusinessException {
    try {
      // ��д����
      this.validatePara(paras);
      // ��ѯ��Ӧ��ͷ
      ReportHeaderVO[] heads = this.queryReportHeader(paras);
      // ����
      new VOConcurrentTool().lock(heads);

      for (ReportHeaderVO head : heads) {
        head.setPk_rejectbill(null);
        head.setVrejectcode(null);
        head.setStatus(VOStatus.UPDATED);
      }

      String[] names = new String[2];
      names[0] = ReportHeaderVO.PK_REJECTBILL;
      names[1] = ReportHeaderVO.VREJECTCODE;

      new VOUpdate<ReportHeaderVO>().update(heads, names);
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
  }

  // ����ʱ��д�ʼ챨���ͷ�Ĳ��ϸ�Ʒ�����������Ͳ��ϸ�Ʒ���ݺ�
  @Override
  public void writebackWhenInsert(WritebackForC004Para[] paras)
      throws BusinessException {
    try {
      // �����д����
      this.validatePara(paras);
      // ��ѯ��Ӧ�ı�ͷVO
      ReportHeaderVO[] heads = this.queryReportHeader(paras);
      if (heads == null || heads.length == 0) {
        ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
            .getNCLangRes().getStrByID("c010003_0", "0c010003-0121"));/*
                                                                       * @res
                                                                       * "�ʼ챨���Ѿ���ɾ�������ܱ��治�ϸ�Ʒ������"
                                                                       */
        return;
      }
      // ����
      new VOConcurrentTool().lock(heads);
      for (ReportHeaderVO head : heads) {
        String hid = head.getPk_rejectbill();
        if (StringUtils.isNotEmpty(hid)) {
          ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
              .getNCLangRes().getStrByID("c010003_0", "0c010003-0101"));/*
                                                                         * @res
                                                                         * "�Ѿ����ɹ����ϸ�Ʒ����!"
                                                                         */
        }
      }
      Map<String, WritebackForC004Para> hid_para =
          new HashMap<String, WritebackForC004Para>();
      for (WritebackForC004Para para : paras) {
        hid_para.put(para.getHid(), para);
      }
      for (ReportHeaderVO head : heads) {
        String hid = head.getPk_reportbill();
        head.setPk_rejectbill(hid_para.get(hid).getPk_rejectbill());
        head.setVrejectcode(hid_para.get(hid).getVrejectcode());
        head.setStatus(VOStatus.UPDATED);
      }
      String[] names = new String[2];
      names[0] = ReportHeaderVO.PK_REJECTBILL;
      names[1] = ReportHeaderVO.VREJECTCODE;
      new VOUpdate<ReportHeaderVO>().update(heads, names);
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }

  }

  // ����ʱ��д�ʼ챨��ģ����ϸ�Ʒ�Ƿ�������
  @Override
  public void writebackWhenUnApprove(WritebackForC004Para[] paras)
      throws BusinessException {
    try {

      // ����д����
      this.validatePara(paras);
      // ��ѯ��Ӧ�ı�ͷVO
      ReportHeaderVO[] heads = this.queryReportHeader(paras);
      // ��ѯ��Ӧ�ı���VO
      ReportItemVO[] items = this.queryReportItem(paras);
      // ����
      new VOConcurrentTool().lock(heads);
      new VOConcurrentTool().lock(items);

      Map<String, WritebackForC004Para> bid_para =
          new HashMap<String, WritebackForC004Para>();
      for (WritebackForC004Para para : paras) {
        bid_para.put(para.getBid(), para);
      }
      Map<String, String> cunitid = new HashMap<String, String>();
      Map<String, String> castunitid = new HashMap<String, String>();
      String vchangerate = null;

      for (ReportHeaderVO head : heads) {
        head.setBrejectaudit(UFBoolean.FALSE);
        head.setStatus(VOStatus.UPDATED);
        vchangerate = head.getVchangerate();
        cunitid.put(head.getPk_reportbill(), head.getCunitid());
        castunitid.put(head.getPk_reportbill(), head.getCastunitid());
      }
      for (ReportItemVO item : items) {
        item.setPk_chgmrl(null);
        item.setPk_chgsrcmrl(null);
        item.setPk_chargedept(null);
        item.setPk_chargedept_v(null);
        if (cunitid.containsKey(item.getPk_reportbill())) {
          item.setCunitid(cunitid.get(item.getPk_reportbill()));
        }
        if (castunitid.containsKey(item.getPk_reportbill())) {
          item.setCastunitid(castunitid.get(item.getPk_reportbill()));
        }
        item.setVchangerate(vchangerate);
        UFDouble nastnum =
            HslParseUtil.hslDivUFDouble(vchangerate, item.getNnum());
        item.setNastnum(nastnum);
        item.setPk_chargepsn(null);
        item.setFprocessjudge(null);
        item.setBchanged(UFBoolean.FALSE);
        item.setStatus(VOStatus.UPDATED);
      }

      String[] names1 = new String[] {
        ReportHeaderVO.BREJECTAUDIT
      };
      String[] names2 = new String[11];
      names2[0] = ReportItemVO.PK_CHGMRL;
      names2[1] = ReportItemVO.PK_CHGSRCMRL;
      names2[2] = ReportItemVO.PK_CHARGEDEPT;
      names2[3] = ReportItemVO.PK_CHARGEDEPT_V;
      names2[4] = ReportItemVO.CUNITID;
      names2[5] = ReportItemVO.CASTUNITID;
      names2[6] = ReportItemVO.VCHANGERATE;
      names2[7] = ReportItemVO.NASTNUM;
      names2[8] = ReportItemVO.PK_CHARGEPSN;
      names2[9] = ReportItemVO.FPROCESSJUDGE;
      names2[10] = ReportItemVO.BCHANGED;

      new VOUpdate<ReportHeaderVO>().update(heads, names1);
      new VOUpdate<ReportItemVO>().update(items, names2);
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
  }

  // ���������õ�����
  private ReportHeaderVO[] queryReportHeader(WritebackForC004Para[] paras) {
    Set<String> hids = new HashSet<String>();
    for (WritebackForC004Para para : paras) {
      hids.add(para.getHid());
    }
    String[] hidArray = hids.toArray(new String[hids.size()]);
    return new VOQuery<ReportHeaderVO>(ReportHeaderVO.class).query(hidArray);
  }

  // ���������õ��ӱ�
  private ReportItemVO[] queryReportItem(WritebackForC004Para[] paras) {
    Set<String> bids = new HashSet<String>();
    for (WritebackForC004Para para : paras) {
      bids.add(para.getBid());
    }
    String[] bidArray = bids.toArray(new String[bids.size()]);
    return new VOQuery<ReportItemVO>(ReportItemVO.class).query(bidArray);
  }

  // ����д����
  private void validatePara(WritebackForC004Para[] paras) {
    for (WritebackForC004Para para : paras) {
      para.validate();
    }
  }

}
