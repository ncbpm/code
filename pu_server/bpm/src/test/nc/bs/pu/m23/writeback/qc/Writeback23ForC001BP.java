package nc.bs.pu.m23.writeback.qc;

import nc.bs.pu.m23.writeback.qc.c001rule.UpdateViewCheckNumRule;
import nc.impl.pubapp.pattern.data.view.ViewQuery;
import nc.impl.pubapp.pattern.data.view.ViewUpdate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.pubitf.pu.m23.qc.Writeback23ForC001Para;
import nc.vo.pu.m23.entity.ArriveItemVO;
import nc.vo.pu.m23.entity.ArriveViewVO;
import nc.vo.pu.m23.utils.ArrivePublicUtil;

public class Writeback23ForC001BP {

  public void writeback(Writeback23ForC001Para[] paras) {

    // ��ѯ������������ͼVO
    String[] bidArray = ArrivePublicUtil.getWriteParaBidArray(paras);
    ViewQuery<ArriveViewVO> queryBO =
        new ViewQuery<ArriveViewVO>(ArriveViewVO.class);
    queryBO.setSharedHead(true);
    ArriveViewVO[] oldViewVOArray = queryBO.query(bidArray);

    // ���ִ��ҵ�����
    AroundProcesser<ArriveViewVO> processer =
        new AroundProcesser<ArriveViewVO>(null);
    this.addBeforeRule(processer, paras);

    processer.before(oldViewVOArray);

    // �־û�
    String[] names = new String[] {
      ArriveItemVO.NACCUMCHECKNUM
    };
    ViewUpdate<ArriveViewVO> bo = new ViewUpdate<ArriveViewVO>();
    oldViewVOArray = bo.update(oldViewVOArray, ArriveItemVO.class, names);

    processer.after(oldViewVOArray);
  }

  private void addBeforeRule(AroundProcesser<ArriveViewVO> processer,
      Writeback23ForC001Para[] paras) {

    // ������ͼVO�е��ۼƱ�������
    processer.addBeforeRule(new UpdateViewCheckNumRule(paras));
  }
}
