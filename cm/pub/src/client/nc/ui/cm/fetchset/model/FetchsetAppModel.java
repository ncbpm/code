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
 * 初始化页面数据模型
 */
public class FetchsetAppModel extends CMAppModelDataManager {
	
	
	
	  /**
     * 定额model
     */
    private AbstractAppModel dingeModel;
    
    /**
     * 储运model
     */
    private AbstractAppModel chuyunModel;

    
    /**
     * 检验model
     */
    private AbstractAppModel jianyanModel;


    /**
     * 环保model
     */
    private AbstractAppModel huanbaoModel;

	
    /**
     * 完工入库model
     */
    private AbstractAppModel overinModel;

    /**
     * 废品取数model
     */
    private AbstractAppModel spoilModel;

    /**
     * 作业model
     */
    private AbstractAppModel taskModel;

    /**
     * 工序委外model
     */
    private AbstractAppModel gxwwModel;

    /**
     * 工序委外model
     */
    private AbstractAppModel iastuffModel;

    /**
     * 外系统取数设置查询服务
     */
    private IFetchsetQueryService fetchsetQueryService = null;

    /**
     * 卡片面板
     */
    private BillForm billFormEditor;

    public BillForm getBillFormEditor() {
        return this.billFormEditor;
    }

    public void setBillFormEditor(BillForm billFormEditor) {
        this.billFormEditor = billFormEditor;
    }

    /**
     * 查询
     */
    private void initModelBySql() {
        AggFetchSetVO[] array = null;
        /* 初始化时查出所有数据，再按对照类型去分类，减少读库次数 */
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
        // 选择工厂后，表体数据为空，这时点击“修改”按钮时，进入死循环，所以给表体设置ts字段，则校验通过。
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
        // 按取数类型分类
        this.classifyAggFetchSetVOByType(array, taskAggVOs, overInAggVOs, mateOutAggVOs, spoilAggVOs, gxwwAggVOs,
                iastuffAggVOs);
        // 初始化材料出库模型
        this.getModel().initModel(mateOutAggVOs.toArray(new AggFetchSetVO[mateOutAggVOs.size()]));
        // 初始化完工入库
        this.getOverinModel().initModel(overInAggVOs.toArray(new AggFetchSetVO[overInAggVOs.size()]));
        // 初始化废品取数
        this.getSpoilModel().initModel(spoilAggVOs.toArray(new AggFetchSetVO[spoilAggVOs.size()]));
        // 初始化作业
        this.getTaskModel().initModel(taskAggVOs.toArray(new AggFetchSetVO[taskAggVOs.size()]));
        // 初始化工序委外
        this.getGxwwModel().initModel(gxwwAggVOs.toArray(new AggFetchSetVO[gxwwAggVOs.size()]));
        // 初始化其他出入库消耗单
        this.getIastuffModel().initModel(iastuffAggVOs.toArray(new AggFetchSetVO[iastuffAggVOs.size()]));
        
        //定额
        AggFetchSetVO[] dingeAggVOs  = this.classifyAggFetchSetVOByType(array,FetchTypeEnum.DINGE);
        this.getDingeModel().initModel(dingeAggVOs);

        //储运
        AggFetchSetVO[] chuyunAggVOs  = this.classifyAggFetchSetVOByType(array,FetchTypeEnum.CHUYUN);
        this.getChuyunModel().initModel(chuyunAggVOs);
        //检验
        AggFetchSetVO[] jianyanAggVOs  = this.classifyAggFetchSetVOByType(array,FetchTypeEnum.JIANYAN);
        this.getJianyanModel().initModel(jianyanAggVOs);

        //环保
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
     * 按取数类型对查询结果分类
     * 
     * @param array
     *            查询结果
     * @param taskAggVOs
     *            作业VO
     * @param overInAggVOs
     *            完工入库VO
     * @param mateOutAggVOs
     *            材料出库VO
     * @param spoilAggVOs
     *            废品取数VO
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
     * 获得外系统取数设置查询服务
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
