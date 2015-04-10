#!/bin/bash
##mihail.stoynov
#-----------------
BACKUP_FILE=jprime.backup.sql.$(date "+%Y.%m.%d")

SSH_HOST=jprime.io
SSH_USER=jprime
SSH_PORT=22
SSH_LOCAL_PORT=7002 # make sure it doesn't clash with anything

DB_USER=root
#DB_PASS= //user supplied
DB_PORT=$SSH_LOCAL_PORT

FILES_TO_COPY=/home/steve/public_html* #not used in this script
#-----------------

function cleanup {
	rm -rf $BACKUP_FILE*
}

function make_tmp_dir {
	cleanup
	mkdir $BACKUP_FILE
	cd $BACKUP_FILE
}

function get_db_password {
	##get databases
	echo -n "db password: "
	read -s DB_PASS; echo
}

function backup_sql {
	ssh $SSH_USER@$SSH_HOST -p$SSH_PORT -N -L$SSH_LOCAL_PORT:localhost:3306 &
	sleep 1 # so the ssh can work
	SSH_PID=$!
	DATABASES=$(mysql -u $DB_USER -p$DB_PASS -h 127.0.0.1 -P $DB_PORT -e "show databases" 2>/dev/null | grep -Ev "(mysql|Database|information_schema|performance_schema|mysql)")
	echo $DATABASES
	for db in $DATABASES; do
		echo dumping $db
		mysqldump -h 127.0.0.1 -P$DB_PORT --force --opt --user=$DB_USER -p$DB_PASS --databases $db 2>/dev/null > "$db.sql"
	done
	kill $SSH_PID
}


function copy_files {
	## get the files
	sftp -r -P$SSH_PO   RT $SSH_USER@$SSH_HOST:$FILES_TO_COPY ./
	if [ $? -ne 0 ]; then
		echo SFTP did not work fine
		exit 1;
	fi
}

function compress {
	## rar me
	rar a -r -m5 $BACKUP_FILE.rar $BACKUP_FILE/
}




make_tmp_dir
get_db_password
backup_sql
# copy_files
compress
#cleanup
