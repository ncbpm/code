package nc.bs.uapeai.sys;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import nc.bs.framework.common.RuntimeEnv;
import nc.itf.uapeai.sys.IHKSender;
import nc.pubitf.bd.accessor.GeneralAccessorFactory;
import nc.pubitf.bd.accessor.IGeneralAccessor;
import nc.vo.bd.accessor.IBDData;
import nc.vo.jcom.xml.XMLUtil;
import nc.vo.pfxx.exception.PfxxException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.uapeai.syn.SysBillInfor;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class AbstractSender implements IHKSender {

	String outputEncoding = "UTF-8";

	private SysBillInfor sysBillInfor;

	private String url;

	// static {
	// String home = RuntimeEnv.getInstance().getNCHome();
	// String fileNme = home + "/ierp/bpm/receiver.properties";
	// InputStream in;
	// try {
	// in = new BufferedInputStream(new FileInputStream(fileNme));
	// Properties p = new Properties();
	// p.load(in);
	// url = p.getProperty("url");
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	
	public String transNCToCode(String docPk, String metaDataID)
			throws PfxxException {
		String result = null;
		try {
			// 档案名称编码查询
			IGeneralAccessor genAcc = GeneralAccessorFactory
					.getAccessor(metaDataID);
			IBDData bdData = null;
			if (genAcc != null)
				bdData = genAcc.getDocByPk(docPk);

			if (bdData != null)
				result = bdData.getCode();

		} catch (Exception e) {
			throw new PfxxException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("pfxx", "0pfxx0053")/*
													 * @res
													 * "档案主键翻译为档案名称错误，档案主键:"
													 */+ docPk, e);
		}
		return result;
	}

	public String transNCToName(String docPk, String metaDataID)
			throws PfxxException {
		String result = null;
		try {
			// 档案名称编码查询
			IGeneralAccessor genAcc = GeneralAccessorFactory
					.getAccessor(metaDataID);
			IBDData bdData = null;
			if (genAcc != null)
				bdData = genAcc.getDocByPk(docPk);

			if (bdData != null)
				result = bdData.getName().getText();

		} catch (Exception e) {
			throw new PfxxException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("pfxx", "0pfxx0053")/*
													 * @res
													 * "档案主键翻译为档案名称错误，档案主键:"
													 */+ docPk, e);
		}
		return result;
	}

	private void initContext() throws Exception {

		String home = RuntimeEnv.getInstance().getNCHome();
		String fileNme = home + "/ierp/bpm/receiver.properties";
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(fileNme));
			Properties p = new Properties();
			p.load(in);
			url = p.getProperty("url");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Object process(Object vo) throws Exception {
		initContext();
		sysBillInfor = new SysBillInfor();
		initBillInor(vo, sysBillInfor);
		beforeSend(vo);
		Document doc = getDocment(vo);
		// 保存当前的doc到文件
		File file = buildSendFile(doc);
		sendFileWithResult(file);
		// parseResponDoc(result2);
		afterSend(vo);

		//没有返回值，暂时返回NULL
		return null;

	}

	private void sendFileWithResult(File localFile) throws PfxxException, Exception {
		// TODO 自动生成的方法存根
		InputStream in = null;
		OutputStream out = null;
		try {
			// 获取图片
			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS_");
			SmbFile remoteFile = new SmbFile(this.url + "/"
					+ fmt.format(new Date()) + localFile.getName());
			remoteFile.connect(); // 尝试连接

			in = new BufferedInputStream(new FileInputStream(localFile));
			out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));

			byte[] buffer = new byte[4096];
			int len = 0; // 读取长度
			while ((len = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush(); // 刷新缓冲的输出流
		} catch(SmbException e1){
			String msg = "同步到BPM共享文件服务器发生错误：请检查共享文件夹地址是否正常";
			throw new BusinessException(msg);

		}
		catch (Exception e) {
			String msg = "同步到BPM共享文件服务器发生错误：" + e.getMessage();
			throw new BusinessException(msg);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
			}
		}

	}

	protected Object afterSend(Object vo) throws Exception {
		return vo;
	}

	protected Object beforeSend(Object vo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public abstract Document getDocment(Object vo) throws Exception;

	private File buildSendFile(Document doc) throws Exception {
		// TODO Auto-generated method stub
		File sendFile = null;
		String full_file_path = null;
		String fileName = getCurBilltype() + "_" + getCurBillcode();
		int suffixnum = 0;
		String home = RuntimeEnv.getInstance().getNCHome();
		String dir = home + "/bpmfile/";
		String date = new UFDate().toString().substring(0, 10);
		dir = dir + date.toString() + "/";
		do {
			if (dir.endsWith("/"))
				dir = dir.substring(0, dir.length() - 1);
			if (suffixnum == 0) {
				full_file_path = dir + "/" + fileName + ".xml";
				suffixnum++;
			} else {
				full_file_path = dir + "/" + fileName + "_" + suffixnum++
						+ ".xml";
			}
			sendFile = new File(full_file_path);
		} while (sendFile.exists());

		File pFile = sendFile.getParentFile();
		if (!pFile.exists()) {
			pFile.mkdirs();
		}
		String outputEncoding = "UTF-8";

		Writer writer = new OutputStreamWriter(new FileOutputStream(sendFile),
				outputEncoding);
		XMLUtil.printDOMTree(writer, doc, 0, outputEncoding);
		writer.close();

		StringBuffer newbuffer = new StringBuffer();
		XMLUtil.writeXMLFormatString(newbuffer, doc, 0);

		return sendFile;
	}

	public SysBillInfor getSysBillInfor() {
		return sysBillInfor;
	}

	public String getCurBillcode() {
		return getSysBillInfor().getVbillcode();
	}

	public String getCurBilltype() {
		return getSysBillInfor().getBilltype();
	}

	/**
	 * Document 转换为 String
	 * 
	 * @param doc
	 *            XML的Document对象
	 * @return String
	 */
	public String doc2String(Document doc) {
		// try {
		// Source source = new DOMSource(doc);
		// StringWriter stringWriter = new StringWriter();
		// Result result = new StreamResult(stringWriter);
		// TransformerFactory factory = TransformerFactory.newInstance();
		// Transformer transformer = factory.newTransformer();
		// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		// transformer.transform(source, result);
		// return stringWriter.getBuffer().toString();
		// } catch (Exception e) {
		// return null;
		// }
		//
		return "test";
	}

	/**
	 * 根据字符串生成Document
	 * 
	 * @param xmlstr
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public Document getDocumentByXMLStr(String xmlstr) throws SAXException,
			IOException {
		InputSource inputSource = new InputSource();
		StringReader reader = new StringReader(xmlstr);
		inputSource.setCharacterStream(reader);
		return XMLUtil.getDocumentBuilder().parse(inputSource);
	}

	public String getUrl() {
		return this.url;
	}

	protected abstract void initBillInor(Object vo, SysBillInfor infor)
			throws Exception;

}
