#!/bin/bash

##mihail.stoynov CLIENT SCRIPT FOR JPRIME SERVICE, DEPENDS ON THE JPRIME SERVICE AND A SCRIPT IN ~jprime

case "$1" in
	start)
		systemctl start jprime
		;;
	stop)
		systemctl stop jprime
		;;
	restart)
		systemctl restart jprime
		;;
	status)
		systemctl status jprime
		;;
	update)
	    sudo -u jprime ~jprime/jprime.service.sh rebuild
		if [ $? -eq 0 ]; then
			echo Stoppping service;
			$0 stop
			sudo -u jprime ~jprime/jprime.service.sh redeploy
	        echo Starting service
			$0 start
		else
			echo "BUILD FAILED; REDEPLOY WAS NOT PERFORMED"
		fi
               	;;
	old_update)
		#mihail: stays here just in case
		systemctl stop jprime
		pushd .
		cd ~jprime/jprime
		sudo -u jprime git pull
		popd
		systemctl start jprime
		;;
	edit_service)
		nano /etc/systemd/system/jprime.service
		systemctl daemon-reload
		;;
	edit_opts)
		nano ~jprime/java.opts.conf
		;;
	log)
		sudo -u jprime ~jprime/jprime.service.sh log
		;;
	service_log)
		journalctl -u jprime
		;;
	*)
		echo -e "\t********************** JPRIME CLIENT SCRIPT **********************"
		echo -e "\tUSAGE: $0 start/stop/status/restart"
		echo -e "\tUSAGE: $0 update/edit_service/edit_opts/log/service_log"
		echo -e "\tNOTES:"
		echo -e "\t\t'update' will stop the app, git pull, and then start the app"
		echo -e "\t\t'edit_opts' will open the config file"
		echo -e "\t\t'service_log' will open the log for the service for debugging"
		echo -e "\t\t'log' tail -f catalina.out"
		echo -e "\t\t'edit_service' edit the systemd config file"
		echo -e "\t\t'edit_opts' edit the passwords"
		exit 1
esac