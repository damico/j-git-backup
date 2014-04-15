package org.jdamico.jgitbkp.runtime;

import org.jdamico.jgitbkp.commons.JGitBackupException;
import org.jdamico.jgitbkp.components.ManagerComponent;

public class Starter {
	
	public static void main(String args[]){
		
		/*
		 * check log
		 * check config
		 * parse config
		 * check paths
		 * populateRepos
		 * cloneRepos
		 * generate bundles
		 * copy old bundles
		 * copy new bundles
		 * send email
		 * 
		 */
		
		try {
			
			ManagerComponent.getInstance().isLogExistent();
			ManagerComponent.getInstance().isConfigExistent();
			ManagerComponent.getInstance().getConfig();
			
			
		} catch (JGitBackupException e) {
			// TODO: handle exception
		}
		
	}

}
