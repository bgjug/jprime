
## When starting jPrime for first time.


### Create new database in MySQL DB

$ mysql -u root -p

mysql> USE mysql;
       
mysql> CREATE DATABASE IF NOT EXISTS javabg
 DEFAULT CHARACTER SET = utf8
 DEFAULT COLLATE = utf8_unicode_ci;

mysql> GRANT ALL ON javabg.* TO 'root'@'localhost' IDENTIFIED BY 'admin';

mysql> exit;


### Configure REAL Mail or FAKE Mail server

##### FAKE mail servers used only during development

 - www.mailtrap.io


### Copy application-DEV.template.properties to application-DEV.properties

$ cd jprime/src/main/resources/

$ cp application-DEV.template.properties application-DEV.properties


### Adjust DEV profile properties file(application-DEV.properties) if needed

$ vim /jprime/src/main/resources/application-DEV.properties

spring.boot.admin.url=http://localhost:9090

server.port=9090

