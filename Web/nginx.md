# Nginx

Apache 보다 적은 자원으로 더 빠르게 데이터를 서비스

### 기본적인 설정 파일

- nginx.conf : 애플리케이션 기본 환경 설정
- mime.conf : 파일 확장명과 MIME 타입 목록
- fastcgi.conf : FastCGI 관련 환경 설정
- proxy.conf : 프록시 관련 환경 설정
- sites.conf : Nginx에 의해 서비스되는 가상 호스트 웹사이트의 환경 설정. 도메인 별 파일 분리 권장

> Nginx의 설정파일 예시
> https://www.nginx.com/resources/wiki/start/topics/examples/full/

### Reverse Proxy

유저와 서버 사이에서 요청에 따라 알맞은 서버로 전달하는 용도로 많이 사용

이 문서에서는 Backend 서버와 Frontend 서버를 URL로 분리하는 용도로 사용

