package org.jdamico.jgitbkp.commons;

public interface Constants {
	
	
	
	public static final String SEVERE_LOGLEVEL = " S ";
	public static final String NORMAL_LOGLEVEL = " N ";
	public static final int FIXED_LOGLIMIT = 50000;
	public static final String VERSION = "0.1";
	public static final String APP_NAME = "j-git-backup";
	public static final String CONFIG_FILE = APP_NAME+".conf";
	public static final String LOG_NAME = APP_NAME+".log";
	public static final String LOG_FOLDER = "/var/log/"+APP_NAME+"/";
	public static final String CONF_FOLDER = "/etc/"+APP_NAME+"/";
	public static final String DATE_FORMAT = "yyyyMMMdd_HH:mm:ss";
}
