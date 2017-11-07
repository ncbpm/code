package nc.ui.hi.psndoc.action;

import java.awt.event.ActionEvent;
import java.util.List;

import nc.itf.bd.defdoc.IDefdocQryService;
import nc.itf.bd.defdoc.IDefdoclistQryService;
import nc.itf.bd.region.IRegionQueryService;
import nc.ss.card.CSSFacade;
import nc.ss.card.CardInfo;
import nc.ui.hr.uif2.action.RefreshAction;
import nc.vo.am.proxy.AMProxy;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.defdoc.DefdoclistVO;
import nc.vo.bd.region.RegionVO;

public class ReadIDCardAction  extends RefreshAction{
	private boolean cardAvaliable = false; 
	private int   comNum = 1;
	public ReadIDCardAction(){
		super();
		this.setBtnName("�����֤��Ϣ");
	}

	@Override
	public void doAction(ActionEvent evt) throws Exception {
		// TODO �Զ����ɵķ������
		super.doAction(evt);
		//��ʼ��com
		if(!cardAvaliable){
			cardAvaliable = initCard(1);
		}
		if(!cardAvaliable){
			return;
		}
		//�Ӷ�������ȡ�û���Ϣ
		CardInfo userInfo = new CardInfo();
		System.out.println("��ȡ���֤��Ϣ���أ�" +CSSFacade.getCardInfo(comNum, userInfo));
		//����model
		//���õ�ǰ��form
		getFormEditor().getBillCardPanel().getHeadItem("name").setValue(userInfo.Name);
		getFormEditor().getBillCardPanel().getHeadItem("sex").setValue(userInfo.Sex.equals("��")?1:2);
		getFormEditor().getBillCardPanel().getHeadItem("birthdate").setValue(userInfo.Birthday);
		getFormEditor().getBillCardPanel().getHeadItem("id").setValue(userInfo.CardNo);
		getFormEditor().getBillCardPanel().getHeadItem("censusaddr").setValue(userInfo.Address);
		//getFormEditor().getBillCardPanel().getHeadItem("photo").setImagePath(userInfo.PhotoPath);
		getFormEditor().getBillCardPanel().getHeadItem("photo").setValue(userInfo.ArrPhotoByte);
		//�������Զ��嵵���б�
		IDefdoclistQryService queryService = AMProxy.lookup(IDefdoclistQryService.class);
		DefdoclistVO[] allNationVos = queryService.queryDefdoclistVOs();
		String pkDefDocList_Nation = "";
		for(DefdoclistVO ele : allNationVos){
			if(ele.getName().equals("����")){
				pkDefDocList_Nation = ele.getPk_defdoclist();
				break;
			}
		}
		//����pk_defdoclist �������б�
		IDefdocQryService nationService = AMProxy.lookup(IDefdocQryService.class);
		DefdocVO[] nationVos = nationService.queryDefdocVOsByDoclistPk(pkDefDocList_Nation, getModel().getContext().getPk_org(), getModel().getContext().getPk_group());
		for(DefdocVO ele : nationVos){
			if(ele.getName().equals(userInfo.Nation)){
				//System.out.println(ele.getName() + "----> "  +ele.getCode());
				getFormEditor().getBillCardPanel().getHeadItem("nationality").setValue(ele.getPrimaryKey());
				break;
			}
		}
		
		//���ü��᣺ ���ڼ����ʽ��ͳһ����Ҫ�ֱ��� xxʡxx��xx�� �� xxʡxx��xx�أ� xx��xx���� xx��xx�أ� xx������xx�У� xx������xx��
		IRegionQueryService regionService = AMProxy.lookup(IRegionQueryService.class);
		List<Object> regionVos = regionService.queryAllRegion();
		int maxMatch = 0;
		RegionVO matchRegion = null;
		for(Object ele : regionVos){
			RegionVO region = (RegionVO)ele;  
			//System.out.println(region.getName());
			if(userInfo.Address.indexOf(region.getName()) != -1){
				if(maxMatch < region.getName().length()){
					maxMatch = region.getName().length();
					matchRegion = region;
				}
			}
		}
		if(matchRegion != null){
			getFormEditor().getBillCardPanel().getHeadItem("nativeplace").setValue(matchRegion.getPrimaryKey());
		}
	}
	
	@Override
    protected boolean isActionEnable()
    {
        return true;
    }

	/* ��ʼ��������,�����ʼ��ʧ�ܣ���ť������*/
	private boolean initCard(int comNum){
		System.out.println("��ʼ�����������أ�" + CSSFacade.initCard(comNum));
		return true;
	}
}
