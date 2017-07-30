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
 * ��װ ��˼������sdk�ӿ�
 * 
 * @author Administrator
 * 
 */
public final class CSSFacade {
	/**
	 * ����com�˿ں� �ɹ���ʼ������������0�����򷵻ش�����
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
	 * ����com�˿ں� �ɹ��رն���������0�����򷵻ش�����
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
			// �ĵ���˵�ᱣ�浽 wx.txt gbk , zp.bmp
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
				// ����Ƭ
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
				// TODO �Զ����ɵ� catch ��
				nRet = -3;
			}
		}
		return nRet == ISSProxy.SUCCESS ? 0 : nRet;
	}

	// ��˼dll�ӿڴ���
	public interface ISSProxy extends Library {
		public final byte CMD_INIPORT = 0X41; // ��ʼ���˿�
		public final byte CMD_CLOSPORT = 0X42; // �رն˿�
		public final byte CMD_YZCARD = 0X43; // ��֤��
		public final byte CMD_RDBASE = 0X44; // ��ȡ������Ϣ
		public final byte CMD_RDNEWADD = 0X45; // ��ȡ���µ�ַ
		public final byte CMD_RDWENZI = 0X46; // ��ȡ������Ϣ
		public final byte CMD_RDBASE_NOPIC = 0X47; // ��ȡ������Ϣ��������ͼ�����

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
