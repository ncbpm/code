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
 * �ɹ����������ͷ������ܱ�����ֶ�
 * 
 * @author Administrator
 * 
 */
public class Pu21Attrs {

	public static void main(String[] args) {
		// TODO �Զ����ɵķ�����

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = null;
			File file = new File(
					"C:\\Users\\Administrator\\Desktop 4\\weifang_runfengNC65\\�ɹ�����\\CGDDBG20170623161620.xml");
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}

	}

}
