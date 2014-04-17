package org.jdamico.jgitbkp.tests;

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

import static org.junit.Assert.*;

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
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.BundleWriter;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.Test;

public class JGitTester {

	@Test
	public void test() throws IOException, GitAPIException {


		String user = "";
		String passwd = "";
		String hostPath = "/git";
		String backupRoot = "/tmp/test";
		String gitRoot = "/mnt";
		String bundlePath = "/tmp";
		boolean keepOld = false;

		List<org.jdamico.jgitbkp.entities.RepoImpl> repos = new ArrayList<org.jdamico.jgitbkp.entities.RepoImpl>();

		File fGitRoot = new File(gitRoot);
		String[] fRepos = fGitRoot.list();
		for (String itemRepo : fRepos) {
			File fItemRepo = new File(gitRoot+"/"+itemRepo);
			if(fItemRepo.isDirectory() && itemRepo.contains(".git") && !itemRepo.equals(".git")){
				repos.add(new org.jdamico.jgitbkp.entities.RepoImpl(itemRepo, null, backupRoot+"/"+itemRepo));
			}
		}

		for(int i=0; i<repos.size(); i++){
			
			File f = new File(repos.get(i).getClonedPath());

			if(f.mkdir()){
				
				Git.cloneRepository()
				.setCloneAllBranches(true)
				.setURI("http://"+hostPath+"/"+repos.get(i).getName())
				.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, passwd))
				.setDirectory(new File(repos.get(i).getClonedPath()))
				.setProgressMonitor(new TextProgressMonitor()).call();
				
			}

			File gitWorkDir = new File(repos.get(i).getClonedPath());
			Git git = Git.open(gitWorkDir);
			git.pull();
			Repository localRepoGit = git.getRepository();
			BundleWriter bundleWriter = new BundleWriter(localRepoGit);
			ProgressMonitor pMonitor = new TextProgressMonitor();
			OutputStream oStream = new FileOutputStream(bundlePath+"/"+repos.get(i).getName()+".bundle");
			
			Map<String, Ref> allRefs = localRepoGit.getAllRefs();
			Collection<Ref> values = allRefs.values();
			Iterator<Ref> iter = values.iterator();
			while(iter.hasNext()){
				Ref element = iter.next();
				try {
					
					bundleWriter.include(element);
					System.out.println("Added: "+element.getName());
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
			}
			
			
			bundleWriter.writeBundle(pMonitor, oStream);


		}

	}

}
