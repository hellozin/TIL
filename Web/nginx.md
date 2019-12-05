# Nginx

Apache 보다 적은 자원으로 더 빠르게 데이터를 서비스

### 기본적인 설정 파일

- nginx.conf : 애플리케이션 기본 환경 설정
- mime.conf : 파일 확장명과 MIME 타입 목록
- fastcgi.conf : FastCGI 관련 환경 설정
- proxy.conf : 프록시 관련 환경 설정
- sites.conf : Nginx에 의해 서비스되는 가상 호스트 웹사이트의 환경 설정. 도메인 별 파일 분리 권장

Docker Hub의 Nginx 이미지를 실행했을 때 볼 수 있는 nginx.conf 기본 설정

`docker run -i -t -d --name nginx nginx /bin/bash`

`docker attach nginx`

```nginx
user  nginx;
worker_processes  1;
error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;

events {
    worker_connections  1024;
}
http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    server {
      listen 80;
      server_name localhost;

      location /naver {
        proxy_pass  http://www.naver.com
      }
      location /google {
        proxy_pass  http://www.google.com
      }
    }

    access_log  /var/log/nginx/access.log  main;
    sendfile        on;
    #tcp_nopush     on;
    keepalive_timeout  65;
    #gzip  on;
    include /etc/nginx/conf.d/*.conf;
}
```

> Nginx의 설정파일 예시
> https://www.nginx.com/resources/wiki/start/topics/examples/full/

### Reverse Proxy

유저와 서버 사이에서 요청에 따라 알맞은 서버로 전달하는 용도로 많이 사용

이 문서에서는 Backend 서버와 Frontend 서버를 URL로 분리하는 용도로 사용

docker nginx 기본설정

default.conf
```
server {
  listen  80;
  server_name <server name>;

  # for certbot challenge
  location ^~ /.well-known/acme-challenge/ {
    default_type  "text/plain";
    root  /var/www/hellozin;
  }

  location / {
    return 301 https://$http_host$request_uri;
  }

}
```

https.conf
```
server {
  listen  443 ssl;
  server_name <server name>;

  ssl_certificate       /var/www/hellozin/fullchain.pem;
  ssl_certificate_key   /var/www/hellozin/privkey.pem;

  ssl_session_timeout   5m;
  ssl_protocols SSLv2 SSLv3 TLSv1;
  ssl_ciphers HIGH:!aNULL:MD5;
  ssl_prefer_server_ciphers on;

  location / {
    proxy_pass  <proxy destination>;
  }
}
```