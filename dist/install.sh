#!/bin/bash
mkdir /etc/j-git-backup
cp etc/j-git-backup/j-git-backup.conf /etc/j-git-backup
mkdir /var/log/j-git-backup
touch /var/log/j-git-backup/j-git-backup.log
chmod 775 -R /var/log/j-git-backup/j-git-backup.log
cp usr/bin/j-git-backup.sh /usr/bin/
chmod +x /usr/bin/j-git-backup.sh
mkdir -p /var/j-git-backup/bin
cp var/j-git-backup/bin/j-git-backup.jar /var/j-git-backup/bin
cp /var/j-git-backup/bin/j-git-backup-cron.sh /var/j-git-backup/bin
chmod +x /var/j-git-backup/bin/j-git-backup-cron.sh
cp var/j-git-backup/j-git-backup.conf /var/j-git-backup

