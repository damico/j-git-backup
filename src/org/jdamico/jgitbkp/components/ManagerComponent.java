package org.jdamico.jgitbkp.components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.jdamico.jgitbkp.commons.Utils;
import org.jdamico.jgitbkp.entities.BundleStatus;
import org.jdamico.jgitbkp.entities.Config;
import org.jdamico.jgitbkp.entities.RepoImpl;

public class ManagerComponent {

	private ManagerComponent(){}

	private static ManagerComponent INSTANCE = null;

	public static ManagerComponent getInstance(){
		if(INSTANCE != null) INSTANCE = new ManagerComponent();
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
				repos.add(new org.jdamico.jgitbkp.entities.RepoImpl(itemRepo, null, config.getBackupRoot()+"/"+itemRepo));
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
					.setURI("http://"+config.getHostPath()+"/"+reposLst.get(i).getName())
					.setCredentialsProvider(new UsernamePasswordCredentialsProvider(config.getUser(), config.getPasswd()))
					.setDirectory(new File(reposLst.get(i).getClonedPath()))
					.setProgressMonitor(new TextProgressMonitor()).call();

				} catch (InvalidRemoteException e) {
					throw new JGitBackupException(e);
				} catch (TransportException e) {
					throw new JGitBackupException(e);
				} catch (GitAPIException e) {
					throw new JGitBackupException(e);
				}

			}
		}

	}

	public List<BundleStatus> generateBundles(List<RepoImpl> reposLst, Config config) throws JGitBackupException {

		for(int i=0; i<reposLst.size(); i++){
			File gitWorkDir = new File(reposLst.get(i).getClonedPath());
			Git git = null;
			Repository localRepoGit = null;
			BundleWriter bundleWriter = null;
			ProgressMonitor pMonitor = null;
			OutputStream oStream = null;
			try {
				git = Git.open(gitWorkDir);
				git.pull();
				localRepoGit = git.getRepository();
				bundleWriter = new BundleWriter(localRepoGit);
				pMonitor = new TextProgressMonitor();
				oStream = new FileOutputStream(config.getBundlePath()+"/"+reposLst.get(i).getName()+".bundle");
				Map<String, Ref> allRefs = localRepoGit.getAllRefs();
				Collection<Ref> values = allRefs.values();
				Iterator<Ref> iter = values.iterator();
				while(iter.hasNext()){
					Ref element = iter.next();
					bundleWriter.include(element);
					System.out.println("Added: "+element.getName());
				}
				bundleWriter.writeBundle(pMonitor, oStream);
			} catch (IOException e) {
				throw new JGitBackupException(e);
			}


		}

		return null;
	}

	public void copyOldBundles(List<RepoImpl> reposLst, Config config) throws JGitBackupException {

	}

	public void sendEmail(List<BundleStatus> bundleStatusLst, Config config) throws JGitBackupException {

	}
}
