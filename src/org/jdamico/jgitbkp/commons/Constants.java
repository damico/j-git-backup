package org.jdamico.jgitbkp.commons;

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

public interface Constants {
	
	
	
	public static final String SEVERE_LOGLEVEL = " S ";
	public static final String NORMAL_LOGLEVEL = " N ";
	public static final int FIXED_LOGLIMIT = 50000;
	public static final String VERSION = "0.6";
	public static final String APP_NAME = "j-git-backup";
	public static final String CONFIG_FILE = APP_NAME+".conf";
	public static final String LOG_NAME = APP_NAME+".log";
	public static final String LOG_FOLDER = "/var/log/"+APP_NAME+"/";
	public static final String CONF_FOLDER = "/etc/"+APP_NAME+"/";
	public static final String DATE_FORMAT = "yyyyMMMdd_HH:mm:ss";
}
