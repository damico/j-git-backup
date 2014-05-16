package org.jdamico.jgitbkp.components;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.BundleWriter;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.jdamico.jgitbkp.commons.Constants;
import org.jdamico.jgitbkp.commons.JGitBackupException;
import org.jdamico.jgitbkp.commons.LoggerManager;
import org.jdamico.jgitbkp.commons.Utils;
import org.jdamico.jgitbkp.entities.BundleStatus;
import org.jdamico.jgitbkp.entities.Config;
import org.jdamico.jgitbkp.entities.RepoImpl;
import org.jdamico.jgitbkp.runtime.Starter;


public class ManagerComponent {

	private ManagerComponent(){}

	private static ManagerComponent INSTANCE = null;

	public static ManagerComponent getInstance(){
		if(INSTANCE == null) INSTANCE = new ManagerComponent();
		return INSTANCE;
	}

	public boolean isLogExistent() throws JGitBackupException {
		File logFile = new File(Constants.LOG_FOLDER+Constants.LOG_NAME);
		return logFile.exists();
	}

	public boolean isConfigExistent() throws JGitBackupException {
		File config = new File(Constants.CONF_FOLDER+Constants.CONFIG_FILE);
		return config.exists();
	}

	public Config getConfig() throws JGitBackupException {
		return Utils.getInstance().convertXmlToConfigEntity(Constants.CONF_FOLDER+Constants.CONFIG_FILE);
	}

	public List<RepoImpl> populateRepos(Config config) throws JGitBackupException {

		List<RepoImpl> repos = new ArrayList<org.jdamico.jgitbkp.entities.RepoImpl>();

		File fGitRoot = new File(config.getGitRoot());
		String[] fRepos = fGitRoot.list();
		for (String itemRepo : fRepos) {
			File fItemRepo = new File(config.getGitRoot()+"/"+itemRepo);
			if(fItemRepo.isDirectory() && itemRepo.contains(".git") && !itemRepo.equals(".git")){
				
				if(!config.getExceptions().contains(itemRepo)) repos.add(new org.jdamico.jgitbkp.entities.RepoImpl(itemRepo, null, config.getBackupRoot()+"/"+itemRepo));
				else{
					String msg = "Repository ["+itemRepo+"] found in exceptions list.";
					if(!Starter.silent) System.out.println(msg);
					LoggerManager.getInstance().logAtDebugTime(this.getClass().getName(), msg);
				}
			}
		}

		return repos;
	}

	public void cloneRepos(List<RepoImpl> reposLst, Config config) throws JGitBackupException {

		for(int i=0; i<reposLst.size(); i++){

			File f = new File(reposLst.get(i).getClonedPath());

			if(f.mkdir()){

				try {
					Git.cloneRepository()
					.setCloneAllBranches(true)
					.setCloneSubmodules(true)
					.setNoCheckout(false)
					.setURI(config.getProtocol()+"://"+config.getHostPath()+"/"+reposLst.get(i).getName())
					.setCredentialsProvider(new UsernamePasswordCredentialsProvider(config.getUser(), config.getPasswd()))
					.setDirectory(new File(reposLst.get(i).getClonedPath()))
					.setProgressMonitor(new TextProgressMonitor()).call();

					
				} catch (InvalidRemoteException e) {
					LoggerManager.getInstance().logAtExceptionTime(this.getClass().getName(), e.getMessage());
					throw new JGitBackupException(e);
				} catch (TransportException e) {
					LoggerManager.getInstance().logAtExceptionTime(this.getClass().getName(), e.getMessage());
					throw new JGitBackupException(e);
				} catch (GitAPIException e) {
					LoggerManager.getInstance().logAtExceptionTime(this.getClass().getName(), e.getMessage());
					throw new JGitBackupException(e);
				}

			}else{
				try {
					Git  git = Git.open(f);
					git.pull();
					LoggerManager.getInstance().logAtDebugTime(this.getClass().getName(), "Updating data from repository.");
				} catch (IOException e) {
					LoggerManager.getInstance().logAtExceptionTime(this.getClass().getName(), e.getMessage());
					throw new JGitBackupException(e);
				}
				String err = "Unable to create directory for clone ["+reposLst.get(i).getName()+"]. Check the path and permissions.";
				if(!f.exists()){
					if(!Starter.silent) System.out.println(err);
					LoggerManager.getInstance().logAtExceptionTime(this.getClass().getName(), err);
				}

			}
		}

	}

	public List<BundleStatus> generateBundles(List<RepoImpl> reposLst, Config config) throws JGitBackupException {

		List<BundleStatus> bundleStatusLst = new ArrayList<BundleStatus>();

		boolean backupStatus = false;
		StringBuffer backupMessage = new StringBuffer();

		for(int i=0; i<reposLst.size(); i++){
			String bundlePath = config.getBundlePath()+"/"+reposLst.get(i).getName()+".bundle";

			File gitWorkDir = new File(reposLst.get(i).getClonedPath());
			Git git = null;
			Repository localRepoGit = null;
			BundleWriter bundleWriter = null;
			ProgressMonitor pMonitor = null;
			OutputStream oStream = null;
			try {
				git = Git.open(gitWorkDir);
				git.pull();
				if(!Starter.silent) System.out.println("Updated data for ["+reposLst.get(i).getName()+"]");
				localRepoGit = git.getRepository();
				bundleWriter = new BundleWriter(localRepoGit);
				pMonitor = new TextProgressMonitor();
				oStream = new FileOutputStream(bundlePath);
				Map<String, Ref> allRefs = localRepoGit.getAllRefs();
				Collection<Ref> values = allRefs.values();
				Iterator<Ref> iter = values.iterator();
				while(iter.hasNext()){
					try{
						Ref element = iter.next();
						bundleWriter.include(element);
					} catch (IllegalArgumentException e) {

						if(!e.getMessage().equalsIgnoreCase("Invalid ref name: HEAD")){
							String err = reposLst.get(i).getName()+": "+e.getMessage();
							backupMessage.append(err);
							LoggerManager.getInstance().logAtExceptionTime(this.getClass().getName(), err);
						}
					}

				}
				if(!Starter.silent) bundleWriter.writeBundle(pMonitor, oStream);
				else bundleWriter.writeBundle(null, oStream);

			} catch (IOException e) {
				String err = reposLst.get(i).getName()+": "+e.getMessage();
				backupMessage.append(err);
				LoggerManager.getInstance().logAtExceptionTime(this.getClass().getName(), err);
				throw new JGitBackupException(e);
			}

			File f = new File(bundlePath);
			if(f.exists() && f.length() > 100){
				backupStatus = f.exists();
			}else backupStatus = false;

			bundleStatusLst.add(new BundleStatus(reposLst.get(i).getName(), backupStatus, backupMessage.toString(), Utils.getInstance().getCurrentDateTimeFormated(Constants.DATE_FORMAT), bundlePath));
			if(!Starter.silent) System.out.println("Bundle created: "+bundlePath+" - "+backupStatus);
		}

		return bundleStatusLst;
	}

	public void copyOldBundles(List<RepoImpl> reposLst, Config config) throws JGitBackupException {
		for (int i = 0; i < reposLst.size(); i++) {
			String bundlePath = config.getBundlePath()+"/"+reposLst.get(i).getName()+".bundle";
			File f = new File(bundlePath);
			if(f.exists()){
				File old = new File(bundlePath+".old");
				if(old.exists()) old.delete();
				Utils.getInstance().copyFileNio(f, old);
			}
		}
	}

	public void deleteOldBundles(List<RepoImpl> reposLst, Config config) throws JGitBackupException {
		for (int i = 0; i < reposLst.size(); i++) {
			String bundlePath = config.getBundlePath()+"/"+reposLst.get(i).getName()+".bundle";
			File f = new File(bundlePath);
			if(f.exists()) f.delete();
		}
	}

	public void sendEmail(List<BundleStatus> bundleStatusLst, Config config) throws JGitBackupException {

		StringBuffer msg = new StringBuffer();
		String[] tOs = null; 
		StringBuffer dests = new StringBuffer(); 
		
		

		for (int i = 0; i < bundleStatusLst.size(); i++) {
			msg.append(bundleStatusLst.get(i).getBackupDate() +" - ");
			msg.append(bundleStatusLst.get(i).getRepoName() +" - ");
			msg.append(bundleStatusLst.get(i).getBundleNamePath() +" - ");
			msg.append(bundleStatusLst.get(i).isBackupStatus() +";  ");
			msg.append(bundleStatusLst.get(i).getBackupMessage() +"\n");
		}

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", config.getSmtpServerPort());
		Session session = Session.getDefaultInstance(properties);

		try{	         
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(config.getFrom()));
			if(config.getTo().contains(",")){
				tOs = config.getTo().split(",");
				for (String email : tOs) {
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.trim()));
					dests.append(email+" ");
				}
			}else{
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(config.getTo()));
				dests.append(config.getTo()+" ");
			}
			
			message.setSubject("### "+Constants.APP_NAME+" notification ###");
			message.setText(msg.toString());
			Transport.send(message);
			String logline = "Notification email sent ["+dests.toString()+"].";
			LoggerManager.getInstance().logAtDebugTime(this.getClass().getName(), logline);
			if(!Starter.silent) System.out.println(logline);
		}catch (MessagingException e) {
			LoggerManager.getInstance().logAtExceptionTime(this.getClass().getName(), e.getMessage());
			if(!Starter.silent) System.out.println(e.getMessage());

		}
	}
}
