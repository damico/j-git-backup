package org.jdamico.jgitbkp.components;

import java.util.List;

import org.jdamico.jgitbkp.commons.JGitBackupException;
import org.jdamico.jgitbkp.entities.Config;
import org.jdamico.jgitbkp.entities.RepoImpl;
import org.jdamico.jgitbkp.entities.BundleStatus;

public class ManagerComponent {
	
	private ManagerComponent(){}
	
	private static ManagerComponent INSTANCE = null;
	
	public static ManagerComponent getInstance(){
		if(INSTANCE != null) INSTANCE = new ManagerComponent();
		return INSTANCE;
	}
	
	public boolean isLogExistent() throws JGitBackupException {
		return false;
	}
	
	public boolean isConfigExistent() throws JGitBackupException {
		return false;
	}
	
	public Config getConfig() throws JGitBackupException {
		return null;
	}
	
	public List<RepoImpl> populateRepos(Config config) throws JGitBackupException {
		return null;
	}
	
	public void cloneRepos(List<RepoImpl> reposLst) throws JGitBackupException {
		
	}
	
	public List<BundleStatus> generateBundles(List<RepoImpl> reposLst) throws JGitBackupException {
		return null;
	}
	
	public void copyOldBundles() throws JGitBackupException {
		
	}

	public void copyNewBundles(List<BundleStatus> bundleStatusLst) throws JGitBackupException {
		
	}
	
	public void sendEmail(List<BundleStatus> bundleStatusLst) throws JGitBackupException {
		
	}
}
