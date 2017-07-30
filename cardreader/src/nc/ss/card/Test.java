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
		// TODO 自动生成的方法存根
		//1.打开串口: 命令行传入com号
		if(args.length < 1){
			System.out.println(args[0]);
			System.out.println("Usage: java -jar Test.jar com-Number(com端口号)");
			return;
		}
		int comNum = Integer.parseInt(args[0]);
		System.out.println("opening com"+comNum + "...");
		int code = CSSFacade.initCard(comNum);
		System.out.println(code);
		if(code == 0){
			System.out.println("初始化读卡器成功！，将身份证放到读卡器上，然后回车！");
			try {
				System.in.read();
				CardInfo obj = new CardInfo();
				code = CSSFacade.getCardInfo(comNum, obj);
				if(code == 0){
					System.out.println("身份证号: " + obj.CardNo);
					System.out.println("姓名：" + obj.Name);
				}else{
					System.out.println("读取身份证信息时报，错误码："+code);
				}
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}else{
			System.out.println("初始化读卡器失败，错误码："+code);
		}
	}

}
