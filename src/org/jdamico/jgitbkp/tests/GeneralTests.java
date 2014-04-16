package org.jdamico.jgitbkp.tests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.jdamico.jgitbkp.commons.JGitBackupException;
import org.jdamico.jgitbkp.commons.Utils;
import org.jdamico.jgitbkp.entities.Config;
import org.junit.Test;

public class GeneralTests {

	@Test
	public void test() {
		
		String user = "a";
		String hostPath = "b";
		String bundlePath = "c";
		String backupRoot = "d";
		String gitRoot = "e";
		String passwd = "f";
		List<String> exceptions = new ArrayList<String>();
		
		exceptions.add("g");
		exceptions.add("h");
		exceptions.add("i");
		
		boolean keepOld = false;
		Config config = new Config(user, passwd, hostPath, backupRoot, gitRoot, bundlePath, keepOld, exceptions);
		
		String xmlPath = "/tmp/j-git-config.conf";
		
		writeStringToFile(config.toString(), xmlPath );
		
		Config newConfig = null;
		try {
			newConfig = Utils.getInstance().convertXmlToConfigEntity(xmlPath);
		} catch (JGitBackupException e) {
			e.printStackTrace();
		}
		
		assertEquals(config.getBackupRoot(), newConfig.getBackupRoot());
		assertEquals(config.getBundlePath(), newConfig.getBundlePath());
		assertEquals(config.getGitRoot(), newConfig.getGitRoot());
		assertEquals(config.getHostPath(), newConfig.getHostPath());
		assertEquals(config.getPasswd(), newConfig.getPasswd());
		assertEquals(config.getUser(), newConfig.getUser());
		assertEquals(config.isKeepOld(), newConfig.isKeepOld());
		
		List<String> origExcs = config.getExceptions();
		List<String> newExcs = newConfig.getExceptions();
		
		for (int i = 0; i < origExcs.size(); i++) {
			assertEquals(origExcs.get(i), newExcs.get(i));
		}
	}
	
	public void writeStringToFile(String content, String fileName) {
		
		File f = new File(fileName);
		if(f.exists()) f.delete();
		
		PrintWriter out = null;
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName, true);
			bw = new BufferedWriter(fw); 
			out = new PrintWriter(bw);
			out.println(content);
		} catch (IOException iox) {
			iox.printStackTrace();
		} finally {
			if(null != out) out.close();
			if(null != bw) try { bw.close(); } catch (IOException e) { e.printStackTrace(); }
			if(null != fw) try { fw.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}

}
