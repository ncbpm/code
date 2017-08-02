package nc.ss.card;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.stream.FileImageInputStream;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;

/**
 * 封装 神思读卡器sdk接口
 * 
 * @author Administrator
 * 
 */
public final class CSSFacade {
	/**
	 * 传入com端口号 成功初始化读卡器返回0，否则返回错误码
	 * 
	 * @param comNum
	 * @return
	 */
	public static int initCard(int comNum) {
		ByteByReference cmd = new ByteByReference(ISSProxy.CMD_INIPORT);
		IntByReference port = new IntByReference(comNum);
		IntByReference parg1 = new IntByReference(ISSProxy.parg1);
		int nRet = ISSProxy.Instance.UCommand1(cmd, port, parg1, ISSProxy.parg2);
		return nRet == ISSProxy.SUCCESS ? 0 : nRet;
	}

	/**
	 * 传入com端口号 成功关闭读卡器返回0，否则返回错误码
	 * 
	 * @param comNum
	 * @return
	 */
	public static int closeCard(int comNum) {
		ByteByReference cmd = new ByteByReference(ISSProxy.CMD_CLOSPORT);
		IntByReference port = new IntByReference(comNum);
		IntByReference parg1 = new IntByReference(ISSProxy.parg1);
		int nRet = ISSProxy.Instance.UCommand1(cmd, port, parg1, ISSProxy.parg2);
		return nRet == ISSProxy.SUCCESS ? 0 : nRet;
	}

	public static int getCardInfo(int comNum, CardInfo obj) {
		//check card valid
		int code = checkCardValid(comNum);
		if(code == 0){
			//read card
			return readCard(comNum, obj);
		}else{
			return code;
		}
	}

	private static int checkCardValid(int comNum) {
		ByteByReference cmd = new ByteByReference(ISSProxy.CMD_YZCARD);
		IntByReference port = new IntByReference(comNum);
		IntByReference parg1 = new IntByReference(ISSProxy.parg1);
		int nRet = ISSProxy.Instance
				.UCommand1(cmd, port, parg1, ISSProxy.parg2);
		return nRet == ISSProxy.SUCCESS ? 0 : nRet;
	}

	private static int readCard(int comNum, CardInfo obj) {
		ByteByReference cmd = new ByteByReference(ISSProxy.CMD_RDBASE);
		IntByReference port = new IntByReference(comNum);
		IntByReference parg1 = new IntByReference(ISSProxy.parg1);
		byte[] path = "./".getBytes();
		int nRet = ISSProxy.Instance.UCommand1(cmd, port, parg1, path);
		if (nRet == ISSProxy.SUCCESS) {
			// 文档中说会保存到 wx.txt gbk , zp.bmp
			try {
				BufferedReader bio = new BufferedReader(new InputStreamReader(
						new FileInputStream("wx.txt")));
				obj.Name = bio.readLine();
				obj.Name = bio.readLine();
				obj.Sex = bio.readLine();
				obj.Nation = bio.readLine();
				obj.Birthday = bio.readLine();
				obj.Address = bio.readLine();
				obj.CardNo = bio.readLine();
				obj.Department = bio.readLine();
				obj.StartDate = bio.readLine();
				obj.EndDate = bio.readLine();
				obj.AddressEx = bio.readLine();
				obj.PhotoPath = "./zp.bmp";
				// 读照片
				FileImageInputStream input = null;
				input = new FileImageInputStream(new File(obj.PhotoPath));
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int numBytesRead = 0;
				while ((numBytesRead = input.read(buf)) != -1) {
					output.write(buf, 0, numBytesRead);
				}
				obj.ArrPhotoByte = output.toByteArray();
				output.close();
				input.close();

			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				nRet = -3;
			}
		}
		return nRet == ISSProxy.SUCCESS ? 0 : nRet;
	}

	// 神思dll接口代理
	public interface ISSProxy extends Library {
		public final byte CMD_INIPORT = 0X41; // 初始化端口
		public final byte CMD_CLOSPORT = 0X42; // 关闭端口
		public final byte CMD_YZCARD = 0X43; // 验证卡
		public final byte CMD_RDBASE = 0X44; // 读取基本信息
		public final byte CMD_RDNEWADD = 0X45; // 读取最新地址
		public final byte CMD_RDWENZI = 0X46; // 读取文字信息
		public final byte CMD_RDBASE_NOPIC = 0X47; // 读取基本信息但不进行图像解码

		public final int SUCCESS = 62171;
		public final int parg1 = 8811;
		public final byte[] parg2 = new byte[] { 0x02, 0x27, 0x00, 0x00 };
		public String filePath = ISSProxy.class.getResource("/").getPath().replaceFirst("/","")+"RdCard";
		ISSProxy Instance = (ISSProxy) Native.loadLibrary("RdCard",
				ISSProxy.class);

		int UCommand1(ByteByReference pCmd, IntByReference parg0,
				IntByReference parg1, byte[] parg2);
	}
}
