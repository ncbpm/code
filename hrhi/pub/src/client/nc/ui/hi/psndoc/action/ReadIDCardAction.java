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
