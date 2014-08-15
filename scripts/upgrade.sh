#!/bin/bash

SPIDER_HOME=/usr/lib/spider-app
SPIDER_SERVICE=/etc/init.d/spiderapp

echo "*************************************************************"
echo 
echo "Upgrading SpiderApp"
echo 
echo "Enter ctrl+d after after upgrade is completed"
echo 

echo "Changing current directory to SpiderApp Home"
echo
cd $SPIDER_HOME

# get current directory
CUR_DIR=$PWD
if [ "$CUR_DIR" = "${SPIDER_HOME}" ]
then
echo "Current directory has been changed to SpiderApp Home"
echo
echo "Stopping the SpiderApp server"
sudo "$SPIDER_SERVICE" stop
echo
echo "Pulling latest code from github"

git pull origin master

echo
echo "Starting SpiderApp server"
sudo "$SPIDER_SERVICE" start
echo
else
echo "Unable to change current directory to SpiderApp Home, existing..."
fi
echo
echo "*************************************************************"
