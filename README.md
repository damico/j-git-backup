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

------------

*Development* 

For developers who want to change or contribute to this source-code, the IDE used is Eclipse (Kepler Service Release 1), also, there is a need of Git, and of course JDK, version 1.7 or above.

------------

*IMPORTANT NOTICE*

For now, this application just supports Git connections over HTTP or HTTPS.



