package org.jdamico.jgitbkp.commons;

/*
 * This file is part of SECNOTE (written by Jose Damico).
 * 
 *    SECNOTE is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License (version 2) 
 *    as published by the Free Software Foundation.
 *
 *    SECNOTE is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with SECNOTE.  If not, see <http://www.gnu.org/licenses/>.
 */

public class JGitBackupException extends Exception {

	private static final long serialVersionUID = -9217342014255738309L;
	
	public JGitBackupException(){
		super();
	}
	
	public JGitBackupException(Exception e){
		super(e);
	}
	
	public JGitBackupException(String message){
		super(message);
	}
	
	public JGitBackupException(String message, Exception e){
		super(message, e);
	}

}

