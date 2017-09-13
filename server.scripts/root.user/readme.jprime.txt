# controll the application (mostly you have to do './jprime.sh update' - git pull, and restart)
./jprime.sh

## TALK TO MYSQL
# talk with the database
ssh root@jprime.io -L3306:localhost:3306 # opens the local port 3306
ssh root@jprime.io -L3307:localhost:3306 # opens the local port 3307 if 3306 is bisy
# then locally
mysql -h 127.0.0.1 -P 3307 -u root -p

##MYSQL
service mysql restart


## MORE INFO

## USER
#I created a jprime user with limited credentials, so that the app will not start with root access
# $useradd jprime

## SERVICE
#the app will start as a service, the service is enabled, so on reboot, the service will restart
# if you edit the service, you have to do 'systemctl daemon-reload' to reload the new conf
# the service conf is here: /etc/systemd/system/jprime.service

journalctl -u jprime



#there is an iptables rule so that 80 -> 8080 because we want to start the app with a limited user
#i added myself in .ssh/authorized_keys so that I log in without a password

## ROUTING
#iptables -t nat -I PREROUTING -p tcp --dport 80 -j REDIRECT --to-ports 8080
#iptables -t nat -I PREROUTING -p tcp --dport 443 -j REDIRECT --to-ports 8443
#iptables -t nat -L PREROUTING

If you add more iptables rules make sure you persist them using the following command:
iptables-save > /etc/sysconfig/iptables

#### SSL HTTP2
        installed nginx
        cert and key in /etc/nginx
        edited etc/nginx/nginx.conf
UPDATE: 
        nginx is no longer used, because we use "<base" which goes to http, but chrome doesn't like http links in https page
        nginx service stopped
no longer http/2
tomcat9 installation attempted but failed, because login required to home page and http://tomcat.apache.org/native-doc/ cannot be built: openssl < 1.0.2 
        run script: ~jprime/tomcat-native-1.2.7-src/configure --with-apr=/usr/bin/apr-1-config             --with-java-home=/usr/lib/jvm/java/             --with-ssl=yes --prefix=/home/jprime/apache-tomcat-9.0.0.M4
reverted to tomcat8
        added ssl to tomcat8
        openssl pkcs12 -export -in /etc/nginx/jprime.crt -inkey /etc/nginx/jprime.dec.key -out jprime.io.p12 -name jprime.io
        keytool -importkeystore -srckeystore jprime.io.p12 -srcstoretype PKCS12 -destkeystore jprime.io.jks -deststoretype JKS
        password for p12 and jks is jprime.io
        added connector for 8443 to tomcat8/conf/server.xml
        added 443 -> 8443 (iptables)

#### LETSENCRYPT
We're on Fedora 21, which is old. TODO: should be updated.

We used StartSSL (StartCom), but we can't anymore. So we go to letsencrypt.
Fedora 21 doesn't have a packge for letsencrypt so we do:

$ git clone https://github.com/letsencrypt/letsencrypt
$ iptables -t nat -D PREROUTING -p tcp --dport 80 -j REDIRECT --to-ports 8080
$ iptables -t nat -D PREROUTING -p tcp --dport 443 -j REDIRECT --to-ports 8443
$ iptables -t nat -L PREROUTING
$ cd letsencrypt/
$ ./letsencrypt-auto certonly --standalone --agree-tos --email mihail@jprime.io -d jprime.io

CANT CONTINUE, this OS is too old, we don't have urllib3[secure]
I will disable SSL globally.

tomcat8 with ssl:
    <Connector executor="tomcatThreadPool"
               port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" />
    <Connector
        port="8443"
        protocol="org.apache.coyote.http11.Http11NioProtocol"
        maxThreads="150"
        SSLEnabled="true"
        scheme="https"
        secure="true"
        clientAuth="false"
        sslProtocol="TLS"
        keystoreFile="/home/jprime/jprime.io.jks"
        keystorePass="jprime.io"
         />

reverting to clean configuration



that's it




2017.09.13
#if nginx gives 502 
/usr/sbin/setsebool httpd_can_network_connect true 
#stupid selinux

#iptables new rules to accept 80 and 443
iptables -A IN_public_allow -p tcp --dport 80 -j ACCEPT
iptables -A IN_public_allow -p tcp --dport 443 -j ACCEPT
#FIXME  firewall-cmd
