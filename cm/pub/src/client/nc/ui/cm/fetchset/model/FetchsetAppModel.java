package nc.ui.cm.fetchset.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.cm.fetchset.IFetchsetQueryService;
import nc.ui.cmpub.business.model.CMAppModelDataManager;
import nc.ui.pubapp.AppUiContext;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.cm.fetchset.entity.AggFetchSetVO;
import nc.vo.cm.fetchset.entity.FetchSetHeadVO;
import nc.vo.cm.fetchset.enumeration.FetchSchemeEnum;
import nc.vo.cm.fetchset.enumeration.FetchTypeEnum;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * ��ʼ��ҳ������ģ��
 */
public class FetchsetAppModel extends CMAppModelDataManager {
	
	
	
	  /**
     * ����model
     */
    private AbstractAppModel dingeModel;
    
    /**
     * ����model
     */
    private AbstractAppModel chuyunModel;

    
    /**
     * ����model
     */
    private AbstractAppModel jianyanModel;


    /**
     * ����model
     */
    private AbstractAppModel huanbaoModel;

	
    /**
     * �깤���model
     */
    private AbstractAppModel overinModel;

    /**
     * ��Ʒȡ��model
     */
    private AbstractAppModel spoilModel;

    /**
     * ��ҵmodel
     */
    private AbstractAppModel taskModel;

    /**
     * ����ί��model
     */
    private AbstractAppModel gxwwModel;

    /**
     * ����ί��model
     */
    private AbstractAppModel iastuffModel;

    /**
     * ��ϵͳȡ�����ò�ѯ����
     */
    private IFetchsetQueryService fetchsetQueryService = null;

    /**
     * ��Ƭ���
     */
    private BillForm billFormEditor;

    public BillForm getBillFormEditor() {
        return this.billFormEditor;
    }

    public void setBillFormEditor(BillForm billFormEditor) {
        this.billFormEditor = billFormEditor;
    }

    /**
     * ��ѯ
     */
    private void initModelBySql() {
        AggFetchSetVO[] array = null;
        /* ��ʼ��ʱ����������ݣ��ٰ���������ȥ���࣬���ٶ������ */
        try {
            array =
                    this.getFetchsetQueryServie().fetchsetQueryByType(Integer.valueOf(FetchTypeEnum.NOTEXIST),
                            this.getModel().getContext().getPk_org(), this.getModel().getContext().getPk_group(),
                            Integer.valueOf(FetchSchemeEnum.NOTEXIST));
        }
        catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
        /************** add by shangzm at 2011-10-20 *********** begin **************/
        // ѡ�񹤳��󣬱�������Ϊ�գ���ʱ������޸ġ���ťʱ��������ѭ�������Ը���������ts�ֶΣ���У��ͨ����
        AggFetchSetVO[] tempArray = array;
        if (tempArray == null || tempArray.length == 0) {
            Vector<Vector<Object>> vDatas = new Vector<Vector<Object>>();
            Vector<Object> vRowDatas = new Vector<Object>();
            vRowDatas.add(AppUiContext.getInstance().getServerTime());
            vDatas.add(vRowDatas);
            this.billFormEditor.getBillCardPanel().getBillModel().setDataVector(vDatas);
        }
        /************** add by shangzm at 2011-10-20 ********* end ****************/
        List<AggFetchSetVO> taskAggVOs = new ArrayList<AggFetchSetVO>();
        List<AggFetchSetVO> overInAggVOs = new ArrayList<AggFetchSetVO>();
        List<AggFetchSetVO> mateOutAggVOs = new ArrayList<AggFetchSetVO>();
        List<AggFetchSetVO> spoilAggVOs = new ArrayList<AggFetchSetVO>();
        List<AggFetchSetVO> gxwwAggVOs = new ArrayList<AggFetchSetVO>();
        List<AggFetchSetVO> iastuffAggVOs = new ArrayList<AggFetchSetVO>();
        // ��ȡ�����ͷ���
        this.classifyAggFetchSetVOByType(array, taskAggVOs, overInAggVOs, mateOutAggVOs, spoilAggVOs, gxwwAggVOs,
                iastuffAggVOs);
        // ��ʼ�����ϳ���ģ��
        this.getModel().initModel(mateOutAggVOs.toArray(new AggFetchSetVO[mateOutAggVOs.size()]));
        // ��ʼ���깤���
        this.getOverinModel().initModel(overInAggVOs.toArray(new AggFetchSetVO[overInAggVOs.size()]));
        // ��ʼ����Ʒȡ��
        this.getSpoilModel().initModel(spoilAggVOs.toArray(new AggFetchSetVO[spoilAggVOs.size()]));
        // ��ʼ����ҵ
        this.getTaskModel().initModel(taskAggVOs.toArray(new AggFetchSetVO[taskAggVOs.size()]));
        // ��ʼ������ί��
        this.getGxwwModel().initModel(gxwwAggVOs.toArray(new AggFetchSetVO[gxwwAggVOs.size()]));
        // ��ʼ��������������ĵ�
        this.getIastuffModel().initModel(iastuffAggVOs.toArray(new AggFetchSetVO[iastuffAggVOs.size()]));
        
        //����
        AggFetchSetVO[] dingeAggVOs  = this.classifyAggFetchSetVOByType(array,FetchTypeEnum.DINGE);
        this.getDingeModel().initModel(dingeAggVOs);

        //����
        AggFetchSetVO[] chuyunAggVOs  = this.classifyAggFetchSetVOByType(array,FetchTypeEnum.CHUYUN);
        this.getChuyunModel().initModel(chuyunAggVOs);
        //����
        AggFetchSetVO[] jianyanAggVOs  = this.classifyAggFetchSetVOByType(array,FetchTypeEnum.JIANYAN);
        this.getJianyanModel().initModel(jianyanAggVOs);

        //����
        AggFetchSetVO[] huanbaoAggVOs  = this.classifyAggFetchSetVOByType(array,FetchTypeEnum.HUANBAO);
        this.getHuanbaoModel().initModel(huanbaoAggVOs);

    }

    private AggFetchSetVO[] classifyAggFetchSetVOByType(AggFetchSetVO[] array, FetchTypeEnum dinge) {

        if (array == null || array.length == 0) {
            return null;
        }
        List<AggFetchSetVO> taskAggVOs = new ArrayList<AggFetchSetVO>();
        for (AggFetchSetVO aggVO : array) {
            if (aggVO == null) {
                continue;
            }
            FetchSetHeadVO headVO = (FetchSetHeadVO) aggVO.getParent();
            Integer ifetchtype = headVO.getIfetchtype();
            if (dinge.equalsValue(ifetchtype)) {
                taskAggVOs.add(aggVO);
            }
         
        }
        return taskAggVOs.toArray(new AggFetchSetVO[taskAggVOs.size()]);
    
	}

	/**
     * ��ȡ�����ͶԲ�ѯ�������
     * 
     * @param array
     *            ��ѯ���
     * @param taskAggVOs
     *            ��ҵVO
     * @param overInAggVOs
     *            �깤���VO
     * @param mateOutAggVOs
     *            ���ϳ���VO
     * @param spoilAggVOs
     *            ��Ʒȡ��VO
     */
    private void classifyAggFetchSetVOByType(AggFetchSetVO[] array, List<AggFetchSetVO> taskAggVOs,
            List<AggFetchSetVO> overInAggVOs, List<AggFetchSetVO> mateOutAggVOs, List<AggFetchSetVO> spoilAggVOs,
            List<AggFetchSetVO> gxwwAggVOs, List<AggFetchSetVO> iastuffAggVOs) {
        if (array == null || array.length == 0) {
            return;
        }
        for (AggFetchSetVO aggVO : array) {
            if (aggVO == null) {
                continue;
            }
            FetchSetHeadVO headVO = (FetchSetHeadVO) aggVO.getParent();
            Integer ifetchtype = headVO.getIfetchtype();
            if (FetchTypeEnum.TASK.equalsValue(ifetchtype)) {
                taskAggVOs.add(aggVO);
            }
            else if (FetchTypeEnum.MATEROUT.equalsValue(ifetchtype)) {
                mateOutAggVOs.add(aggVO);
            }
            else if (FetchTypeEnum.OVERIN.equalsValue(ifetchtype)) {
                overInAggVOs.add(aggVO);
            }
            else if (FetchTypeEnum.SPOIL.equalsValue(ifetchtype)) {
                spoilAggVOs.add(aggVO);
            }
            else if (FetchTypeEnum.GXWW.equalsValue(ifetchtype)) {
                gxwwAggVOs.add(aggVO);
            }
            else if (FetchTypeEnum.IASTUFF.equalsValue(ifetchtype)) {
                iastuffAggVOs.add(aggVO);
            }
        }
    }

    @Override
    public void initModel() {
        this.initModelBySql();
    }

    /**
     * �����ϵͳȡ�����ò�ѯ����
     */
    private IFetchsetQueryService getFetchsetQueryServie() {
        if (null == this.fetchsetQueryService) {
            this.fetchsetQueryService = NCLocator.getInstance().lookup(IFetchsetQueryService.class);
        }
        return this.fetchsetQueryService;
    }

    public void setOverinModel(AbstractAppModel overinModel) {
        this.overinModel = overinModel;
    }

    public AbstractAppModel getOverinModel() {
        return this.overinModel;
    }

    public void setSpoilModel(AbstractAppModel spoilModel) {
        this.spoilModel = spoilModel;
    }

    public AbstractAppModel getSpoilModel() {
        return this.spoilModel;
    }

    public void setTaskModel(AbstractAppModel taskModel) {
        this.taskModel = taskModel;
    }

    public AbstractAppModel getTaskModel() {
        return this.taskModel;
    }

    public void setGxwwModel(AbstractAppModel gxwwModel) {
        this.gxwwModel = gxwwModel;
    }

    public AbstractAppModel getGxwwModel() {
        return this.gxwwModel;
    }

    public void setIastuffModel(AbstractAppModel iastuffModel) {
        this.iastuffModel = iastuffModel;
    }

    public AbstractAppModel getIastuffModel() {
        return this.iastuffModel;
    }

	public AbstractAppModel getDingeModel() {
		return dingeModel;
	}

	public void setDingeModel(AbstractAppModel dingeModel) {
		this.dingeModel = dingeModel;
	}

	public AbstractAppModel getJianyanModel() {
		return jianyanModel;
	}

	public void setJianyanModel(AbstractAppModel jianyanModel) {
		this.jianyanModel = jianyanModel;
	}

	public AbstractAppModel getHuanbaoModel() {
		return huanbaoModel;
	}

	public void setHuanbaoModel(AbstractAppModel huanbaoModel) {
		this.huanbaoModel = huanbaoModel;
	}

	public IFetchsetQueryService getFetchsetQueryService() {
		return fetchsetQueryService;
	}

	public void setFetchsetQueryService(IFetchsetQueryService fetchsetQueryService) {
		this.fetchsetQueryService = fetchsetQueryService;
	}

	public AbstractAppModel getChuyunModel() {
		return chuyunModel;
	}

	public void setChuyunModel(AbstractAppModel chuyunModel) {
		this.chuyunModel = chuyunModel;
	}
    
	
    
}
