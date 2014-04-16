package org.jdamico.jgitbkp.commons;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdamico.jgitbkp.entities.Config;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class Utils {
	
	private static Utils INSTANCE = null;

	private Utils(){}

	public static Utils getInstance(){
		if(null == INSTANCE) INSTANCE = new Utils();
		return INSTANCE;
	}

	public Config convertXmlToConfigEntity(String fXmlFile) throws JGitBackupException{
		
		/*
		  
		 <j-git-backup>
		 <config user = "xxx" passwd = "xxxx" hostPath = "xxx/git" backupRoot = "/tmp/test" gitRoot = "/mnt" bundlePath = "/tmp" keepOld = "false">
		 <exceptions></exceptions>
		 </config>
		 </j-git-backup>
		 
		 */

		Config configObj = null;
		DocumentBuilderFactory dbFactory = null;
		DocumentBuilder dBuilder = null;
		
		try {
			
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			String user = (String) getDomTagAttribute(doc, "config", "user");
			String passwd = (String) getDomTagAttribute(doc, "config", "passwd");
			String hostPath = (String) getDomTagAttribute(doc, "config", "hostPath");
			String backupRoot = (String) getDomTagAttribute(doc, "config", "backupRoot");
			String gitRoot = (String) getDomTagAttribute(doc, "config", "gitRoot");
			boolean keepOld = Boolean.parseBoolean( (String) getDomTagAttribute(doc, "config", "keepOld"));
			String bundlePath = (String) getDomTagAttribute(doc, "config", "bundlePath");
			
			List<String> exceptions = getDomTagList(doc, "exception");
			
			configObj = new Config(user, passwd, hostPath, backupRoot, gitRoot, bundlePath, keepOld, exceptions);

		} catch (ParserConfigurationException e) {
			throw new JGitBackupException(e);
		} catch (SAXException e) {
			throw new JGitBackupException(e);
		} catch (IOException e) {
			throw new JGitBackupException(e);
		}

		return configObj;
	}

	private Object getDomTagAttribute(Document doc, String tag, String attribute) {
		Object ret = null;

		NodeList nList = doc.getElementsByTagName(tag);


		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				ret = eElement.getAttribute(attribute);
			}
		}

		return ret;
	}
	
	private List<String> getDomTagList(Document doc, String tag) {
		
		List<String> tagListContent = new ArrayList<String>();

		NodeList nList = doc.getElementsByTagName(tag);

		

		for (int temp = 0; temp < nList.getLength(); temp++) {
			
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				tagListContent.add(eElement.getTextContent());
			}
		}

		return tagListContent;
	}
	
	public String getCurrentDateTimeFormated(String format){
		Date date = new Date();
		Format formatter = new SimpleDateFormat(format);
		String stime = formatter.format(date);
		return stime;
	}

}
