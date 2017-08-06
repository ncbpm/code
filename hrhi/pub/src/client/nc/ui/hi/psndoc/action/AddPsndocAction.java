package nc.ui.hi.psndoc.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.hr.utils.CommonUtils;
import nc.hr.utils.PubEnv;
import nc.itf.bd.psn.psnid.IPsnidService;
import nc.itf.hi.IPsndocQryService;
import nc.itf.hr.frame.IHrBillCode;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.pub.billcode.vo.BillCodeContext;
import nc.pub.tools.PinYinHelper;
import nc.ui.hi.psndoc.model.PsndocModel;
import nc.ui.hi.psndoc.view.CheckUniqueDialog;
import nc.ui.hi.psndoc.view.PsndocFormEditor;
import nc.ui.hi.register.model.RegisterDefaultValueProvider;
import nc.ui.hr.uif2.action.AddAction;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.vo.am.proxy.AMProxy;
import nc.vo.bd.psn.PsnClVO;
import nc.vo.bd.psnid.PsnIdtypeVO;
import nc.vo.hi.psndoc.CertVO;
import nc.vo.hi.psndoc.PsnJobVO;
import nc.vo.hi.psndoc.PsnOrgVO;
import nc.vo.hi.psndoc.PsndocAggVO;
import nc.vo.hi.psndoc.PsndocVO;
import nc.vo.hi.psndoc.TrainVO;
import nc.vo.hi.psndoc.enumeration.PsnType;
import nc.vo.hi.psndoc.enumeration.TrnseventEnum;
import nc.vo.hi.pub.HICommonValue;
import nc.vo.hr.validator.IDFieldValidatorConfig;
import nc.vo.om.psnnavi.PsnNaviCommonValue;
import nc.vo.om.pub.JFCommonValue;
import nc.vo.org.DeptVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/***************************************************************************
 * <br>
 * Created on 2010-1-29 16:28:15<br>
 * @author Rocex Wang
 ***************************************************************************/
public class AddPsndocAction extends AddAction
{
    
    private IDFieldValidatorConfig idValidator;
    
    /****************************************************************************
     * {@inheritDoc}<br>
     * Created on 2010-4-21 10:31:11<br>
     * @see nc.ui.uif2.actions.AddAction#doAction(java.awt.event.ActionEvent)
     * @author Rocex Wang
     ****************************************************************************/
    @Override
    public void doAction(ActionEvent evt) throws Exception
    {
        CommonUtils.checkOrg(getModel().getContext());
        
        PsndocVO psndocVO = getDefaultValue();
        
        if (psndocVO == null)
        {
            return;
        }
        PsndocAggVO defaultValue = new PsndocAggVO();
        
        defaultValue.setParentVO(psndocVO);
        
        TrainVO[] trainVOs =
            (TrainVO[]) NCLocator.getInstance().lookup(IPsndocQryService.class)
                .querySubVO(TrainVO.class, TrainVO.PK_PSNDOC + "= '" + psndocVO.getPk_psndoc() + "'", TrainVO.RECORDNUM);
        
        defaultValue.setTableVO(TrainVO.getDefaultTableName(), trainVOs);
        
        ((RegisterDefaultValueProvider) getDefaultValueProvider()).setDefaultValue(defaultValue);
        
        // ����һ��֤���Ӽ���Ϣ,����Ƿ�Ƹ��Ƹ������
        if (!HICommonValue.JOB_REHIRE.equals(getModel().getInJobType()))
        {
            ((PsndocAggVO) getDefaultValueProvider().getDefaultValue()).setTableVO(CertVO.getDefaultTableName(),
                new CertVO[]{createCert(psndocVO)});
        }
        getModel().setCurrentPkPsndoc(null);
        if (HICommonValue.JOB_REHIRE.equals(getModel().getInJobType()))
        {
            // ��Ƹ��ƸʱҪ���õ�ǰ��Ա����
            getModel().setCurrentPkPsndoc(psndocVO.getPk_psndoc());
        }
        getModel().setPk_psncl(psndocVO.getPsnJobVO().getPk_psncl());
        
        super.doAction(evt);
        
        // ������Щ�ֶβ��ɱ༭��
        String strFieldCodes[] = ((PsndocFormEditor) getFormEditor()).getModel().getUniqueFields();
        strFieldCodes = (String[]) ArrayUtils.add(strFieldCodes, PsnJobVO.getDefaultTableName() + "_" + PsnJobVO.PK_PSNCL);
        for (String strFieldCode : strFieldCodes)
        {
        	//name ��Ҫ�ɱ༭
        	if(strFieldCode.equals("name")){
        		getFormEditor().getBillCardPanel().getHeadItem(strFieldCode).setEdit(true);
        		continue;
        	}
            if (!psndocVO.IDTYPE.equals(strFieldCode) && !psndocVO.ID.equals(strFieldCode))
            {
                getFormEditor().getBillCardPanel().getHeadItem(strFieldCode).setEdit(false);
            }
        }
        
        if (getFormEditor().getBillCardPanel().getBillModel(CertVO.getDefaultTableName()).getRowCount() > 0
            && !HICommonValue.JOB_REHIRE.equals(getModel().getInJobType()))
        {
            getFormEditor().getBillCardPanel().getBillModel(CertVO.getDefaultTableName()).setRowState(0, BillModel.ADD);
        }
        
        // �������������Ա���ű������
        BillCodeContext billCodeContext =
            NCLocator.getInstance().lookup(IBillcodeManage.class)
                .getBillCodeContext("6007psndoc_clerkcode", getContext().getPk_group(), getContext().getPk_org());
        // �޹�����ߣ����������ǿ��Ա༭���Ա������һ����Ϊ�ɱ༭
        if (null == billCodeContext || billCodeContext.isEditable())
        {
            getFormEditor().getBillCardPanel().getHeadItem("hi_psnjob_clerkcode").setEdit(true);
        }
        
        getFormEditor().getBillCardPanel().getBillData().setBillstatus(VOStatus.NEW);
    }
    
    private CertVO createCert(PsndocVO psndocVO)
    {
        CertVO cert = new CertVO();
        cert.setIdtype(psndocVO.getIdtype());
        cert.setId(psndocVO.getId());
        cert.setPk_group(getContext().getPk_group());
        cert.setPk_org(getContext().getPk_org());
        cert.setIseffect(UFBoolean.TRUE);
        cert.setIsstart(UFBoolean.TRUE);
        cert.setCreator(getContext().getPk_loginUser());
        cert.setCreationtime(PubEnv.getServerTime());
        
        return cert;
    }
    
    /***************************************************************************
     * <br>
     * Created on 2010-7-6 14:54:24<br>
     * @throws BusinessException
     * @author Rocex Wang
     * @return PsndocVO
     ***************************************************************************/
    protected PsndocVO getDefaultValue() throws BusinessException
    {
        
        String strPk_org =
            getModel().getCurrTypeOrgVO() instanceof OrgVO || getModel().getCurrTypeOrgVO() instanceof DeptVO ? ObjectUtils
                .toString(getModel().getCurrTypeOrgVO().getAttributeValue(PsndocVO.PK_ORG)) : getModel().getContext().getPk_org();
        
        String strPK_dept =
            getModel().getCurrTypeOrgVO() instanceof DeptVO ? ObjectUtils.toString(getModel().getCurrTypeOrgVO().getAttributeValue(
                PsnJobVO.PK_DEPT)) : null;
        
        if (PsnNaviCommonValue.OTHER_NODE.equals(strPk_org) || PsnNaviCommonValue.OTHER_NODE_MSAOS.equals(strPk_org))
        {
            strPk_org = getModel().getContext().getPk_org();
        }
        
        strPk_org = NCLocator.getInstance().lookup(IPsndocQryService.class).checkOrgPower(strPk_org);
        
        if (strPk_org == null)
        {
            strPK_dept = null;
        }
        else
        {
            strPK_dept = NCLocator.getInstance().lookup(IPsndocQryService.class).checkDeptPower(strPK_dept);
        }
        
        /*ȥ���Ի���ֱ��������ְ���ͣ��������ҳ�漴��
        CheckUniqueDialog uniqueDialog =
            new CheckUniqueDialog(getContext().getEntranceUI(), getModel().getUniqueFields(), getFormEditor().getBillCardPanel()
                .getBillData().getBillTempletVO(), getIdValidator());
        uniqueDialog.setModel(getModel());
        
        // ����Ա������õ��Ի�����
        if (getModel().getCurrTypeOrgVO() instanceof PsnClVO)
        {
            PsndocVO psndocVO2 = new PsndocVO();
            psndocVO2.getPsnJobVO().setPk_psncl(((PsnClVO) getModel().getCurrTypeOrgVO()).getPk_psncl());
            uniqueDialog.setResultVO(psndocVO2);
        }
        
        ActionHelper.resetHintMessage(this);
        
        if (uniqueDialog.showModal() != UIDialog.ID_OK)
        {
            ActionHelper.setCancelHintMessage(this);
            clearDialog(uniqueDialog);
            return null;
        }
        
        PsndocAggVO psndocAggVO = uniqueDialog.getValue();
        */
        PsndocVO psndocVO = new PsndocVO();
        //����Ĭ�� ֤������Ϊ ���֤
        IPsnidService idQService = AMProxy.lookup(IPsnidService.class);
        PsnIdtypeVO[] idVos = idQService.queryAllPsnIdtype();
        String idTypePk = "";
        for(PsnIdtypeVO ele : idVos){
        	if(ele.getName().equals("���֤")){
        		idTypePk = ele.getPrimaryKey();
        		break;
        	}
        }
        psndocVO.setIdtype(idTypePk);
        // ����Ĭ��ֵ
        if (psndocVO.getPk_org() == null)
        {
            psndocVO.setPk_org(strPk_org);
        }
        psndocVO.setPk_group(getModel().getContext().getPk_group());
        psndocVO.setPk_hrorg(getModel().getContext().getPk_org());
        
        psndocVO.setShortname(PinYinHelper.getPinYinHeadChar(psndocVO.getName()));
        psndocVO.setDataoriginflag(0);
        
        PsnJobVO psnJobVO = psndocVO.getPsnJobVO();
        psnJobVO.setAssgid(1);
        psnJobVO.setPsntype((Integer) PsnType.EMPLOYEE.value());// Ա��
        psnJobVO.setRecordnum(0);
        psnJobVO.setShoworder(9999999);
        psnJobVO.setLastflag(UFBoolean.TRUE);
        psnJobVO.setIsmainjob(UFBoolean.TRUE);// �Ƿ���ְ
        psnJobVO.setPoststat(UFBoolean.TRUE);// �Ƿ��ڸ�
        psnJobVO.setTrnsevent((Integer) TrnseventEnum.REGISTER.value());// �춯�¼�
        
        // modify for �ۻ� ����ʱ ���ṩ��ʼ���ڵ�Ĭ��ֵ//modify by ԭ���û���֤��,�����Ա����Ҫ��Ĭ��ֵ
        if (psnJobVO.getBegindate() == null)
        {
            if (getModel().getPsnType() == (Integer) PsnType.POI.value())
            {
                psnJobVO.setBegindate(PubEnv.getServerLiteralDate());
            }
        }
        psnJobVO.setPk_dept(strPK_dept);
        psnJobVO.setPk_org(strPk_org);
        psnJobVO.setPk_hrorg(getModel().getContext().getPk_org());
        psnJobVO.setPk_group(getModel().getContext().getPk_group());
        psnJobVO.setPk_hrgroup(getModel().getContext().getPk_group());
        
        PsnOrgVO psnOrgVO = psndocVO.getPsnOrgVO();
        psnOrgVO.setPsntype((Integer) PsnType.EMPLOYEE.value());// Ա��
        // psnOrgVO.setOrgrelaid(1);
        psnOrgVO.setLastflag(UFBoolean.TRUE);
        psnOrgVO.setEndflag(UFBoolean.FALSE);
        // ����ֶ�ûʲôʵ������,ֱ��ʹ�õ�ǰHR��֯;
        psnOrgVO.setPk_org(getModel().getContext().getPk_org());
        
        // modify for �ۻ� ����ʱ ���ṩ��ʼ���ڵ�Ĭ��ֵ//modify by ԭ���û���֤��,�����Ա����Ҫ��Ĭ��ֵ
        if (psnOrgVO.getBegindate() == null)
        {
            if (getModel().getPsnType() == (Integer) PsnType.POI.value())
            {
                psnOrgVO.setBegindate(PubEnv.getServerLiteralDate());
            }
        }
        psnOrgVO.setPk_hrorg(getModel().getContext().getPk_org());
        psnOrgVO.setPk_group(getModel().getContext().getPk_group());
        
        if (getModel().getPsndocCodeContext() != null && HICommonValue.JOB_HIRE.equals(getModel().getInJobType()))
        {
            BillCodeContext billContext =
                NCLocator
                    .getInstance()
                    .lookup(IBillcodeManage.class)
                    .getBillCodeContext(HICommonValue.NBCR_PSNDOC_CODE, getModel().getContext().getPk_group(),
                        getModel().getContext().getPk_org());
            String strCode = "";
            if (billContext != null && !billContext.isPrecode())
            {
                // ����������ʱֵ�����ڱ���ʱ����У�飩���ں�̨��ȡ��ֵ
                strCode = JFCommonValue.LEVELED_CODE_TMP;
            }
            else
            {
                // ֻ���ڳ�����ְ��ʱ����Զ�����Ա����
                strCode =
                    NCLocator
                        .getInstance()
                        .lookup(IHrBillCode.class)
                        .getBillCode(HICommonValue.NBCR_PSNDOC_CODE, getModel().getContext().getPk_group(),
                            getModel().getContext().getPk_org());
            }
            
            psndocVO.setCode(strCode);
            getModel().setPsndocCode(strCode);
        }
        
        if (getModel().getPsndocClerkCodeContext() != null && HICommonValue.JOB_HIRE.equals(getModel().getInJobType()))
        {
            BillCodeContext billContext =
                NCLocator
                    .getInstance()
                    .lookup(IBillcodeManage.class)
                    .getBillCodeContext(HICommonValue.NBCR_PSNDOC_CLERKCODE, getModel().getContext().getPk_group(),
                        getModel().getContext().getPk_org());
            String strClerkCode = "";
            if (billContext != null && !billContext.isPrecode())
            {
                // ����������ʱֵ�����ڱ���ʱ����У�飩���ں�̨��ȡ��ֵ
                strClerkCode = JFCommonValue.LEVELED_CODE_TMP;
            }
            else
            {
                // ֻ���ڳ�����ְ��ʱ����Զ�����Ա����
                strClerkCode =
                    NCLocator
                        .getInstance()
                        .lookup(IHrBillCode.class)
                        .getBillCode(HICommonValue.NBCR_PSNDOC_CLERKCODE, getModel().getContext().getPk_group(),
                            getModel().getContext().getPk_org());
            }
            // String strClerkCode = NCLocator.getInstance().lookup(IHrBillCode.class)
            // .getBillCode(HICommonValue.NBCR_PSNDOC_CLERKCODE, getModel().getContext().getPk_group(),
            // getModel().getContext().getPk_org());
            psnJobVO.setClerkcode(strClerkCode);
            getModel().setPsndocClerkCode(strClerkCode);
        }
        //clearDialog(uniqueDialog);
        return psndocVO;
    }
    
    private void clearDialog(CheckUniqueDialog uniqueDialog)
    {
        if (uniqueDialog == null)
        {
            return;
        }
        
        uniqueDialog.setModel(null);
        uniqueDialog.setResultVO(null);
        uniqueDialog.setPsndocAggVO(null);
        uniqueDialog.dispose();
        uniqueDialog = null;
    }
    
    /***************************************************************************
     * <br>
     * Created on 2010-2-2 10:36:28<br>
     * @author Rocex Wang
     * @return the idValidator
     ***************************************************************************/
    public IDFieldValidatorConfig getIdValidator()
    {
        return idValidator;
    }
    
    /****************************************************************************
     * {@inheritDoc}<br>
     * Created on 2010-5-14 12:05:19<br>
     * @see nc.ui.hr.uif2.action.HrAction#getModel()
     * @author Rocex Wang
     ****************************************************************************/
    @Override
    public PsndocModel getModel()
    {
        return (PsndocModel) super.getModel();
    }
    
    /****************************************************************************
     * {@inheritDoc}<br>
     * Created on 2010-4-15 9:17:02<br>
     * @see nc.ui.hr.uif2.action.AddAction#isActionEnable()
     * @author Rocex Wang
     ****************************************************************************/
    @Override
    protected boolean isActionEnable()
    {
        return super.isActionEnable() && getModel().getContext().getPk_org() != null;
    }
    
    /***************************************************************************
     * <br>
     * Created on 2010-1-29 16:28:26<br>
     * @param idValidator
     * @author Rocex Wang
     ***************************************************************************/
    public void setIdValidator(IDFieldValidatorConfig idValidator)
    {
        this.idValidator = idValidator;
    }
    
    /***************************************************************************
     * <br>
     * Created on 2010-5-14 12:05:40<br>
     * @param model
     * @author Rocex Wang
     ***************************************************************************/
    public void setModel(PsndocModel model)
    {
        super.setModel(model);
    }
}
