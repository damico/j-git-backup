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

public class RepoImpl {
	
	private String name;
	private List<String> branchList;
	private String clonedPath;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getBranchList() {
		return branchList;
	}
	public void setBranchList(List<String> branchList) {
		this.branchList = branchList;
	}
	
	public String getClonedPath() {
		return clonedPath;
	}
	public void setClonedPath(String clonedPath) {
		this.clonedPath = clonedPath;
	}
	public RepoImpl(String name, List<String> branchList, String clonedPath) {
		super();
		this.name = name;
		this.branchList = branchList;
		this.clonedPath = clonedPath;
	}
	
	public RepoImpl(){
		
		super();
		
	}
	
	

}

