package org.jdamico.jgitbkp.entities;


public class BundleStatus {
	
	private String repoName;
	private boolean backupStatus;
	private String backupMessage;
	private String backupDate;
	private String bundleNamePath;
	
	public String getRepoName() {
		return repoName;
	}
	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}
	public boolean isBackupStatus() {
		return backupStatus;
	}
	public void setBackupStatus(boolean backupStatus) {
		this.backupStatus = backupStatus;
	}
	public String getBackupMessage() {
		return backupMessage;
	}
	public void setBackupMessage(String backupMessage) {
		this.backupMessage = backupMessage;
	}
	public String getBackupDate() {
		return backupDate;
	}
	public void setBackupDate(String backupDate) {
		this.backupDate = backupDate;
	}
	public String getBundleNamePath() {
		return bundleNamePath;
	}
	public void setBundleNamePath(String bundleNamePath) {
		this.bundleNamePath = bundleNamePath;
	}
	
	public BundleStatus(String repoName, boolean backupStatus,
			String backupMessage, String backupDate, String bundleNamePath) {
		super();
		this.repoName = repoName;
		this.backupStatus = backupStatus;
		this.backupMessage = backupMessage;
		this.backupDate = backupDate;
		this.bundleNamePath = bundleNamePath;
	}
	
	

}
