package nc.ss.card;

import java.io.IOException;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;

public class Test {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		//1.�򿪴���: �����д���com��
		if(args.length < 1){
			System.out.println(args[0]);
			System.out.println("Usage: java -jar Test.jar com-Number(com�˿ں�)");
			return;
		}
		int comNum = Integer.parseInt(args[0]);
		System.out.println("opening com"+comNum + "...");
		int code = CSSFacade.initCard(comNum);
		System.out.println(code);
		if(code == 0){
			System.out.println("��ʼ���������ɹ����������֤�ŵ��������ϣ�Ȼ��س���");
			try {
				System.in.read();
				CardInfo obj = new CardInfo();
				code = CSSFacade.getCardInfo(comNum, obj);
				if(code == 0){
					System.out.println("���֤��: " + obj.CardNo);
					System.out.println("������" + obj.Name);
				}else{
					System.out.println("��ȡ���֤��Ϣʱ���������룺"+code);
				}
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}else{
			System.out.println("��ʼ��������ʧ�ܣ������룺"+code);
		}
	}

}
