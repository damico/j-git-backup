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
