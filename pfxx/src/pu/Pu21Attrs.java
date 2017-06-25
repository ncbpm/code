package pu;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 采购订单变更表头表体可能变更的字段
 * 
 * @author Administrator
 * 
 */
public class Pu21Attrs {

	public static void main(String[] args) {
		// TODO 自动生成的方法存

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = null;
			File file = new File(
					"C:\\Users\\Administrator\\Desktop 4\\weifang_runfengNC65\\采购流程\\CGDDBG20170623161620.xml");
			document = builder.parse(file);
			Element element = document.getDocumentElement();
			NodeList PensonNodes = element.getElementsByTagName("item");
			for (int i = 0; i < PensonNodes.getLength(); i++) {
				Element PensonElement = (Element) PensonNodes.item(i);
				NodeList childNodes = PensonElement.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
						System.out.print("\""+childNodes.item(j).getNodeName()+"\",");

					}
				}

			}
		} catch (SAXException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

}
