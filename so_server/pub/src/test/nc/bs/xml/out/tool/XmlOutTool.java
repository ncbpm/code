package nc.bs.xml.out.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.pfxx.agent.XMLPostServiceImpl;
import nc.vo.jcom.xml.XMLUtil;
import nc.vo.pub.lang.UFDate;

import org.w3c.dom.Document;

public class XmlOutTool {
	
	
public static void votoXmlFile(String billtype, Object[] vos,String pk_org,String vbillcode) throws Exception{
	
	//按照名称生成一份
  	XMLPostServiceImpl services = new XMLPostServiceImpl();
	 Document doc = services.postBill(vos, "develop", billtype, pk_org, "bpm_name",false, true);
     XmlOutTool.buildSendFile(billtype, vbillcode+"_name", doc);
     //按照主键生成一份
	 Document doc_pk = services.postBill(vos, "develop", billtype, pk_org, "bpm_pk",false, true);

     XmlOutTool.buildSendFile(billtype, vbillcode+"_pk", doc_pk);
} 

	public static  File buildSendFile(String billtype, String billcode, Document doc) throws Exception {
		// TODO Auto-generated method stub

		File sendFile = null;
		String full_file_path = null;
		String fileName = billtype + "_" + billcode;
		int suffixnum = 0;
		String home = RuntimeEnv.getInstance().getNCHome();
		String dir = home + "/pfxxfilebpm/";
		String date = new UFDate().toString().substring(0, 10);
		dir =dir+date.toString() + "/";
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
	
}
package nc.bs.xml.out.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.pfxx.agent.XMLPostServiceImpl;
import nc.vo.jcom.xml.XMLUtil;
import nc.vo.pub.lang.UFDate;

import org.w3c.dom.Document;

public class XmlOutTool {
	
	
public static void votoXmlFile(String billtype, Object[] vos,String pk_org,String vbillcode) throws Exception{
	
	//按照名称生成一份
  	XMLPostServiceImpl services = new XMLPostServiceImpl();
	 Document doc = services.postBill(vos, "develop", billtype, pk_org, "bpm_name",false, true);
     XmlOutTool.buildSendFile(billtype, vbillcode+"_name", doc);
     //按照主键生成一份
	 Document doc_pk = services.postBill(vos, "develop", billtype, pk_org, "bpm_pk",false, true);

     XmlOutTool.buildSendFile(billtype, vbillcode+"_pk", doc_pk);
} 

	public static  File buildSendFile(String billtype, String billcode, Document doc) throws Exception {
		// TODO Auto-generated method stub

		File sendFile = null;
		String full_file_path = null;
		String fileName = billtype + "_" + billcode;
		int suffixnum = 0;
		String home = RuntimeEnv.getInstance().getNCHome();
		String dir = home + "/pfxxfilebpm/";
		String date = new UFDate().toString().substring(0, 10);
		dir =dir+date.toString() + "/";
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
	
}
