package org.jdamico.jgitbkp.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
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

	public void copyFileNio(File source, File dest) throws JGitBackupException {
		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		try {
			sourceChannel = new FileInputStream(source).getChannel();
			destChannel = new FileOutputStream(dest).getChannel();
			destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		}catch(IOException ioe) {
			LoggerManager.getInstance().logAtExceptionTime(this.getClass().getName(), ioe.getMessage());
			throw new JGitBackupException(ioe);
		}finally{
			try{ if(sourceChannel !=null) sourceChannel.close(); }catch(Exception e){}
			try{ if(destChannel !=null) destChannel.close(); }catch(Exception e){}
		}

	}

	public Config convertXmlToConfigEntity(String fXmlFile) throws JGitBackupException {

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
			String protocol = (String) getDomTagAttribute(doc, "config", "protocol");
			String smtpServerPort = (String) getDomTagAttribute(doc, "config", "smtpServerPort");
			String from = (String) getDomTagAttribute(doc, "config", "from");
			String to = (String) getDomTagAttribute(doc, "config", "to");
			boolean keepOld = Boolean.parseBoolean( (String) getDomTagAttribute(doc, "config", "keepOld"));
			String bundlePath = (String) getDomTagAttribute(doc, "config", "bundlePath");

			if(nullEmptyChecker(user, 1, "user") &&
					nullEmptyChecker(passwd, 1, "passwd") &&
					nullEmptyChecker(hostPath, 2, "hostPath") &&
					nullEmptyChecker(backupRoot, 2, "backupRoot") &&
					nullEmptyChecker(gitRoot, 2, "gitRoot") &&
					nullEmptyChecker(protocol, 4, "protocol")) {


				List<String> exceptions = getDomTagList(doc, "exception");

				configObj = new Config(user, passwd, hostPath, backupRoot, gitRoot, bundlePath, keepOld, exceptions, protocol, smtpServerPort, from, to);
			}
		} catch (ParserConfigurationException e) {
			LoggerManager.getInstance().logAtExceptionTime(this.getClass().getName(), e.getMessage());
			throw new JGitBackupException(e);
		} catch (SAXException e) {
			LoggerManager.getInstance().logAtExceptionTime(this.getClass().getName(), e.getMessage());
			throw new JGitBackupException(e);
		} catch (IOException e) {
			LoggerManager.getInstance().logAtExceptionTime(this.getClass().getName(), e.getMessage());
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

	public boolean nullEmptyChecker(String content, int size, String label) throws JGitBackupException{
		boolean ret = false;
		if(content != null){

			if(content.length() >= size) ret = true;
			else throw new JGitBackupException("O campo "+label+" é menor que seu tamanho mínimo ("+size+").");

		}else throw new JGitBackupException("O campo "+label+" é nulo.");

		return ret;
	}

}
