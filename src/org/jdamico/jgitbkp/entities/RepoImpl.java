package org.jdamico.jgitbkp.entities;

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

