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
import org.jdamico.jgitbkp.components.ManagerComponent;
import org.jdamico.jgitbkp.entities.BundleStatus;
import org.jdamico.jgitbkp.entities.Config;
import org.jdamico.jgitbkp.entities.RepoImpl;
import org.jdamico.jgitbkp.runtime.Starter;
import org.junit.Test;

public class GeneralTests {
	
	@Test
	public void testStarter(){
		/*
		 * check log
		 * check config
		 * parse config
		 * check paths
		 * populateRepos
		 * cloneRepos
		 * 
		 * copy old bundles
		 * generate bundles
		 * copy new bundles
		 * send email
		 * 
		 */
		
		try {
			
			if(ManagerComponent.getInstance().isLogExistent()){
				if(!Starter.silent) System.out.println("Log Found.");
				if(ManagerComponent.getInstance().isConfigExistent()){
					if(!Starter.silent) System.out.println("Config Found.");
					Config config = ManagerComponent.getInstance().getConfig();
					List<RepoImpl> repos = ManagerComponent.getInstance().populateRepos(config);
					if(repos.size() > 0){
						if(!Starter.silent) System.out.println("Repositories Found: "+repos.size()+".");
						ManagerComponent.getInstance().cloneRepos(repos, config);
						
						
						
						if(config.isKeepOld()){
							ManagerComponent.getInstance().copyOldBundles(repos, config);
							if(!Starter.silent) System.out.println("Old bundles backuped.");
						}
						
						ManagerComponent.getInstance().deleteOldBundles(repos, config);
						if(!Starter.silent) System.out.println("Old bundles deleted.");
						
						List<BundleStatus> bundleLst = ManagerComponent.getInstance().generateBundles(repos, config);
						if(config.getSmtpServerPort() != null && config.getSmtpServerPort().length() > 5  &&
							config.getFrom() !=null && config.getFrom().length() > 4 && 
						     config.getTo() !=null && config.getTo().length() > 4){
							
								ManagerComponent.getInstance().sendEmail(bundleLst, config);
						}
					}else{
						if(!Starter.silent) System.out.println("no repositories found");
					}
				}else{
					if(!Starter.silent) System.out.println("no config");
				}
			}else{
				if(!Starter.silent) System.out.println("no log");
			}

			
			
			
		} catch (JGitBackupException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() {
		
		String user = "a";
		String hostPath = "b";
		String bundlePath = "c";
		String backupRoot = "d";
		String gitRoot = "e";
		String passwd = "f";
		List<String> exceptions = new ArrayList<String>();
		String protocol = "http";
		String smtp = "127.0.0.1";
		String from = "damico@dcon.com.br";
		String to = "damico@dcon.com.br";
		
		exceptions.add("g");
		exceptions.add("h");
		exceptions.add("i");
		
		boolean keepOld = false;
		Config config = new Config(user, passwd, hostPath, backupRoot, gitRoot, bundlePath, keepOld, exceptions, protocol, smtp, from, to);
		
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
