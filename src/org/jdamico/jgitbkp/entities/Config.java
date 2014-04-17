package org.jdamico.jgitbkp.entities;

/*
 * This file is part of J-GIT-BACKUP (written by Jose Damico).
 * 
 *    J-GIT-BACKUP is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License (version 2) 
 *    as published by the Free Software Foundation.
 *
 *    J-GIT-BACKUP is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with J-GIT-BACKUP.  If not, see <http://www.gnu.org/licenses/>.
 */

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
	private String protocol = null;
	private String smtpServerPort = null;
	private String from = null;
	private String to = null;
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
	
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getSmtpServerPort() {
		return smtpServerPort;
	}
	public void setSmtpServerPort(String smtpServerPort) {
		this.smtpServerPort = smtpServerPort;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public Config(String user, String passwd, String hostPath,
			String backupRoot, String gitRoot, String bundlePath,
			boolean keepOld, List<String> exceptions, String protocol, String smtpServerPort, String from, String to) {
		super();
		this.user = user;
		this.passwd = passwd;
		this.hostPath = hostPath;
		this.backupRoot = backupRoot;
		this.gitRoot = gitRoot;
		this.bundlePath = bundlePath;
		this.keepOld = keepOld;
		this.exceptions = exceptions;
		this.protocol = protocol;
		this.smtpServerPort = smtpServerPort;
		this.to = to;
		this.from = from;
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
						    + "protocol = \""+getProtocol()+"\" "
						    + "smtpServerPort = \""+getSmtpServerPort()+"\" "
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
