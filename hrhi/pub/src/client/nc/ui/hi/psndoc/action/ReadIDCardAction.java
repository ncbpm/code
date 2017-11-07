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
		this.setBtnName("读身份证信息");
	}

	@Override
	public void doAction(ActionEvent evt) throws Exception {
		// TODO 自动生成的方法存根
		super.doAction(evt);
		//初始化com
		if(!cardAvaliable){
			cardAvaliable = initCard(1);
		}
		if(!cardAvaliable){
			return;
		}
		//从读卡器读取用户信息
		CardInfo userInfo = new CardInfo();
		System.out.println("读取身份证信息返回：" +CSSFacade.getCardInfo(comNum, userInfo));
		//设置model
		//设置到前段form
		getFormEditor().getBillCardPanel().getHeadItem("name").setValue(userInfo.Name);
		getFormEditor().getBillCardPanel().getHeadItem("sex").setValue(userInfo.Sex.equals("男")?1:2);
		getFormEditor().getBillCardPanel().getHeadItem("birthdate").setValue(userInfo.Birthday);
		getFormEditor().getBillCardPanel().getHeadItem("id").setValue(userInfo.CardNo);
		getFormEditor().getBillCardPanel().getHeadItem("censusaddr").setValue(userInfo.Address);
		//getFormEditor().getBillCardPanel().getHeadItem("photo").setImagePath(userInfo.PhotoPath);
		getFormEditor().getBillCardPanel().getHeadItem("photo").setValue(userInfo.ArrPhotoByte);
		//查所有自定义档案列表
		IDefdoclistQryService queryService = AMProxy.lookup(IDefdoclistQryService.class);
		DefdoclistVO[] allNationVos = queryService.queryDefdoclistVOs();
		String pkDefDocList_Nation = "";
		for(DefdoclistVO ele : allNationVos){
			if(ele.getName().equals("民族")){
				pkDefDocList_Nation = ele.getPk_defdoclist();
				break;
			}
		}
		//根据pk_defdoclist 查民族列表
		IDefdocQryService nationService = AMProxy.lookup(IDefdocQryService.class);
		DefdocVO[] nationVos = nationService.queryDefdocVOsByDoclistPk(pkDefDocList_Nation, getModel().getContext().getPk_org(), getModel().getContext().getPk_group());
		for(DefdocVO ele : nationVos){
			if(ele.getName().equals(userInfo.Nation)){
				//System.out.println(ele.getName() + "----> "  +ele.getCode());
				getFormEditor().getBillCardPanel().getHeadItem("nationality").setValue(ele.getPrimaryKey());
				break;
			}
		}
		
		//设置籍贯： 由于籍贯格式不统一，需要分别处理： xx省xx市xx区 ， xx省xx市xx县， xx市xx区， xx市xx县， xx自治区xx市， xx自治区xx区
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

	/* 初始化读卡器,如果初始化失败，则按钮不可用*/
	private boolean initCard(int comNum){
		System.out.println("初始化读卡器返回：" + CSSFacade.initCard(comNum));
		return true;
	}
}
