j-git-backup
============

A backup application for GIT. Written in Java, using JGit API.

------------

This project was born due the need of a tool to automate a backup process, for Git repositories.
Also, when this project was planned, I had in mind a specific environment, an environment that exists inside a company where I was working. Inside this company almost all servers runs Microsoft(R) Windows OSes, but our Git server runs in Linux (CentOS) as an exception. Following this structure, the backup servers runs also in Microsoft(R) Windows OSes, so I was looking for a solution that could work independently of OS and hardware architecture.

With that said, I've choose to use Java language and JGit API for Git native integration. Also, this application allows that the Git Bundles can be stored in any kind of storage, using several types of protocols and filesystems, such as SMB, NFS and NTFS, ext3, ext4 etc.

This application is based on CLI (Command Line Interface) and have some important dependencies:

- JRE (Java Runtime Environment) version >= 6
- Correct Configuration File

------------

How it works for me? Well, in my situation, I've added a bash sript that calls the j-git-backup in our CRON TABLE. This script is called daily, then j-git-backup reads all of my Git repositories and makes a bundle of each one with all TAGs and BRANCHES, then, these bundles are copied to a SMB share in a Microsoft(R) Windows machine.

My crontab line looks like this (it runs silently daily at 12:30 AM):

	30 0 * * * /bin/bash /var/j-git-backup/bin/j-git-backup-cron.sh

------------

*Configuration*

This is an example of /etc/j-git-backup/j-git-backup.conf configuration file:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	<j-git-backup>
		<config user = "gituser" passwd = "****" hostPath = "127.0.0.1/git" backupRoot = "/path-for-stage" gitRoot = "/path-for-git-repos" bundlePath = "/path-for-backuped-repos" protocol = "http" smtpServerPort = "127.0.0.1" keepOld = "true" to="to1@server.com,to2@server.com" from="from@server.com">
			<exception>repo-not-bckp1.git</exception>
			<exception>repo-not-bckp2.git</exception>
		</config>
	</j-git-backup>
```

-------------	

*Development* 

For developers who want to change or contribute to this source-code, the IDE used is Eclipse (Kepler Service Release 1), also, there is a need of Git, and of course JDK, version 1.7 or above.

------------

*IMPORTANT NOTICE*

For now, this application just supports Git connections over HTTP or HTTPS.

------------

* BACKUP RESTORE *

In order to restore a backup done with j-git-backup you will need to know how to handle git bundle files.
The common way is follow this 3 steps bellow:

```bash 
$ git clone --mirror path-to-git-bundle-file path-to-new-remote-repo.git
$ cd path-to-new-remote-repo.git
$ for remote in `git branch -r | grep '\->'`; do git branch --track $remote; done
```
-------------  
  

