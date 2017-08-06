package nc.ui.hi.psndoc.action;

import java.awt.event.ActionEvent;

import nc.ss.card.CSSFacade;
import nc.ss.card.CardInfo;
import nc.ui.hr.uif2.action.RefreshAction;

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
