package org.jdamico.jgitbkp.entities;

import java.util.List;


/*
 * 
 <j-git-backup>
 <config user = "xxx" passwd = "xxxx" hostPath = "xxx/git" backupRoot = "/tmp/test" gitRoot = "/mnt" bundlePath = "/tmp" keepOld = "false">
 <exceptions></exceptions>
 </config>
 </j-git-backup>
 
 
 */

public class Config {
	
	private String user = null;
	private String passwd = null;
	private String hostPath = null;
	private String backupRoot = null;
	private String gitRoot = null;
	private String bundlePath = null;
	private boolean keepOld = false;
	private List<String> exceptions = null;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getHostPath() {
		return hostPath;
	}
	public void setHostPath(String hostPath) {
		this.hostPath = hostPath;
	}
	public String getBackupRoot() {
		return backupRoot;
	}
	public void setBackupRoot(String backupRoot) {
		this.backupRoot = backupRoot;
	}
	public String getGitRoot() {
		return gitRoot;
	}
	public void setGitRoot(String gitRoot) {
		this.gitRoot = gitRoot;
	}
	public String getBundlePath() {
		return bundlePath;
	}
	public void setBundlePath(String bundlePath) {
		this.bundlePath = bundlePath;
	}
	public boolean isKeepOld() {
		return keepOld;
	}
	public void setKeepOld(boolean keepOld) {
		this.keepOld = keepOld;
	}
	public List<String> getExceptions() {
		return exceptions;
	}
	public void setExceptions(List<String> exceptions) {
		this.exceptions = exceptions;
	}
	
	public Config() {
		super();
	}
	public Config(String user, String passwd, String hostPath,
			String backupRoot, String gitRoot, String bundlePath,
			boolean keepOld, List<String> exceptions) {
		super();
		this.user = user;
		this.passwd = passwd;
		this.hostPath = hostPath;
		this.backupRoot = backupRoot;
		this.gitRoot = gitRoot;
		this.bundlePath = bundlePath;
		this.keepOld = keepOld;
		this.exceptions = exceptions;
	}
	
	@Override
	public String toString(){
		
		StringBuilder xml = new StringBuilder();
		
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		xml.append("<j-git-backup>\n");
		xml.append("<config "
							+ "user = \""+getUser()+"\" "
							+ "passwd = \""+getPasswd()+"\" "
							+ "hostPath = \""+getHostPath()+"\" "
						    + "backupRoot = \""+getBackupRoot()+"\" "
						    + "gitRoot = \""+getGitRoot()+"\" "
						    + "bundlePath = \""+getBundlePath()+"\" "
						    + "keepOld = \""+String.valueOf(isKeepOld())+"\">\n");
		
		List<String> excs = getExceptions();
		
		for (String exception : excs) {
			xml.append("<exception>"+exception+"</exception>\n");
		}
		
		xml.append("</config>\n");
		xml.append("</j-git-backup>\n");
		
		return xml.toString();
		
	}
	
	
}
