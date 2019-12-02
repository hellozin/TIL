# Certbot on Amazon Linux 2

https://certbot.eff.org/lets-encrypt/centos6-other
https://medium.com/@andrenakkurt/great-guide-thanks-for-putting-this-together-gifford-nowland-c3ce0ea2455

## Install Certbot
Run these commands on the command line on the machine to install Certbot.

```
wget https://dl.eff.org/certbot-auto
sudo mv certbot-auto /usr/local/bin/certbot-auto
sudo chown root /usr/local/bin/certbot-auto
sudo chmod 0755 /usr/local/bin/certbot-auto
```

or

`yum install -y certbot python2-certbot-apache`

and

`sudo /usr/local/bin/certbot-auto certonly --webroot`

if `Sorry, I don't know how to bootstrap Certbot on your operating system!`

`sudo vim /usr/local/bin/certbot-auto`

find this line in the file

`elif [ -f /etc/redhat-release ]; then`

(likely near line nr 750)

replace whole line with this:

```
elif [ -f /etc/redhat-release ] || grep 'cpe:.*:amazon_linux:2' /etc/os-release > /dev/null 2>&1; then
```

and do 

`sudo /usr/local/bin/certbot-auto certonly --webroot`

success

and Renew

`/usr/local/bin/certbot-auto renew --webroot-path /var/www/html`

where is key

`/etc/letsencrypt/archive/.`

# 다른 방법

https://medium.com/@pentacent/nginx-and-lets-encrypt-with-docker-in-less-than-5-minutes-b4b8a60d3a71