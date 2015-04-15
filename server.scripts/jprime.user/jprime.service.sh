#!/bin/bash

##mihail.stoynov THIS SCRIPT IS USED BY THE SYSTEMD jprime.service (THE START/STOP COMMANDS)
## it is also used to pull changes and rebuild and redeploy (but only if build is successful

TOMCAT_HOME=~jprime/apache-tomcat-8.0.21
SOURCE_ROOT=~jprime/jprime
case $1 in
	exit1)
		exit 1;
		;;
	rebuild)
#		pushd .
		cd $SOURCE_ROOT
		git pull origin master -v --dry-run 2>&1 | grep "up to date" && changed=0 || changed=1; if [ $changed -eq 0 ]; then echo NOTHING CHANGED, WILL FAIL THE BUILD; exit 1; else echo CHANGES FOUND; fi
		git pull origin master
		/usr/bin/mvn clean package -P \!run.as.spring-boot.run -DskipTests=true
#		popd #this changes the $? FIXME TODO
		;;
	manual)
		. ~/jprime/java.opts.conf
		/usr/bin/mvn -f $SOURCE_ROOT/pom.xml spring-boot:run -DskipTests=true $JAVA_OPTS
		;;
	redeploy)
		rm -rf $TOMCAT_HOME/webapps/ROOT*
		cp $SOURCE_ROOT/target/site-0.0.1-SNAPSHOT.war $TOMCAT_HOME/webapps/ROOT.war
		;;
	start)
		. ~jprime/java.opts.conf
		echo "CATALINA_OPTS=$CATALINA_OPTS"
		$TOMCAT_HOME/bin/startup.sh
		;;
	stop)
		$TOMCAT_HOME/bin/shutdown.sh
		;;
	restart)
		$0 stop;
		$0 start;
		;;
	log)
		tail -f $TOMCAT_HOME/logs/catalina.out
		;;
	kill)
		kill -9 $(jps -lv | grep org.apache.catalina.startup.Bootstrap | grep -- -Dcatalina.home=$(realpath $TOMCAT_HOME) | cut -d\  -f1)
		;;
	*)
               	echo -e "\t********************** JPRIME SERVICE SCRIPT **********************"
               	echo -e "\tUSAGE: $0 rebuild/redeploy/log/start/stop/restart/manual/kill"
               	echo -e "\tNOTES:"
               	echo -e "\t\t'rebuild' will git pull, and mvn clean package with a custom profile"
               	echo -e "\t\t'redeploy' will just clean the tomcat and copy the new war"
		echo -e "\t\t'log' tail -f catalina.out"
               	echo -e "\t\t'start/stop/restart' contol tomcat"
		echo -e "\t\t'manual' start with spring-boot:run"
		echo -e "\t\t'kill' kill the tomcat if stop fails"
                exit 1
esac

#$TOMCAT_HOME/bin/catalina.sh jpda run