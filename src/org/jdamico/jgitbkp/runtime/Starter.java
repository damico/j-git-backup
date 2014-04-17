package org.jdamico.jgitbkp.runtime;

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

import org.jdamico.jgitbkp.commons.Constants;
import org.jdamico.jgitbkp.commons.JGitBackupException;
import org.jdamico.jgitbkp.commons.LoggerManager;
import org.jdamico.jgitbkp.components.ManagerComponent;
import org.jdamico.jgitbkp.entities.BundleStatus;
import org.jdamico.jgitbkp.entities.Config;
import org.jdamico.jgitbkp.entities.RepoImpl;

public class Starter {
	
	public static boolean silent = false;
	public static void main(String args[]){
		
		
		if(args !=null && args.length > 0){
			if(args[0].equalsIgnoreCase("--silent") || args[0].equalsIgnoreCase("-s")) silent = true;
			if(args[0].equalsIgnoreCase("--version") || args[0].equalsIgnoreCase("-v")){
				System.out.println(Constants.APP_NAME+" "+Constants.VERSION);
				System.exit(0);
			}
		}
		
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
		
		LoggerManager.getInstance().logAtDebugTime("Starter", Constants.APP_NAME+" "+Constants.VERSION);
		
		try {
			
			if(ManagerComponent.getInstance().isLogExistent()){
				if(!Starter.silent) System.out.println("Log Found.");
				LoggerManager.getInstance().logAtDebugTime("Starter", "Log Found.");
				if(ManagerComponent.getInstance().isConfigExistent()){
					if(!Starter.silent) System.out.println("Config Found.");
					LoggerManager.getInstance().logAtDebugTime("Starter", "Config Found.");
					
					Config config = ManagerComponent.getInstance().getConfig();
					List<RepoImpl> repos = ManagerComponent.getInstance().populateRepos(config);
					if(repos.size() > 0){
						if(!Starter.silent) System.out.println("Repositories Found: "+repos.size()+".");
						LoggerManager.getInstance().logAtDebugTime("Starter", "Repositories Found: "+repos.size()+".");
						
						ManagerComponent.getInstance().cloneRepos(repos, config);
						
						if(config.isKeepOld()){
							ManagerComponent.getInstance().copyOldBundles(repos, config);
							if(!Starter.silent) System.out.println("Old bundles backuped.");
							LoggerManager.getInstance().logAtDebugTime("Starter", "Old bundles backuped.");
						}
						
						ManagerComponent.getInstance().deleteOldBundles(repos, config);
						if(!Starter.silent) System.out.println("Old bundles deleted.");
						LoggerManager.getInstance().logAtDebugTime("Starter", "Old bundles deleted.");
						
						List<BundleStatus> bundleLst = ManagerComponent.getInstance().generateBundles(repos, config);
						if(config.getSmtpServerPort() != null && config.getSmtpServerPort().length() > 5  &&
							config.getFrom() !=null && config.getFrom().length() > 4 && 
						     config.getTo() !=null && config.getTo().length() > 4){
							
								ManagerComponent.getInstance().sendEmail(bundleLst, config);
						}
					}else{
						if(!Starter.silent) System.out.println("No repositories found.");
						LoggerManager.getInstance().logAtDebugTime("Starter", "No repositories found.");
					}
				}else{
					if(!Starter.silent) System.out.println("No config file found.");
					LoggerManager.getInstance().logAtDebugTime("Starter", "No config file found.");
				}
			}else{
				if(!Starter.silent) System.out.println("No log file found.");
				LoggerManager.getInstance().logAtDebugTime("Starter", "No log file found.");
			}

		} catch (JGitBackupException e) {
			
			System.out.println(e.getMessage());
			LoggerManager.getInstance().logAtExceptionTime("Starter", e.getMessage());
		}
		
	}

}
