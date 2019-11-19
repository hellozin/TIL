# Web 개발 시 고려사항

- [Web 개발 시 고려사항](#web-%ea%b0%9c%eb%b0%9c-%ec%8b%9c-%ea%b3%a0%eb%a0%a4%ec%82%ac%ed%95%ad)
    - [사용자 입력값은 무조건 검증한다.](#%ec%82%ac%ec%9a%a9%ec%9e%90-%ec%9e%85%eb%a0%a5%ea%b0%92%ec%9d%80-%eb%ac%b4%ec%a1%b0%ea%b1%b4-%ea%b2%80%ec%a6%9d%ed%95%9c%eb%8b%a4)
    - [사용자의 요청으로 Query가 조건없이 호출되면 안된다.](#%ec%82%ac%ec%9a%a9%ec%9e%90%ec%9d%98-%ec%9a%94%ec%b2%ad%ec%9c%bc%eb%a1%9c-query%ea%b0%80-%ec%a1%b0%ea%b1%b4%ec%97%86%ec%9d%b4-%ed%98%b8%ec%b6%9c%eb%90%98%eb%a9%b4-%ec%95%88%eb%90%9c%eb%8b%a4)
    - [캐시 사용 시 성능 측정은 반드시 하고 캐시 데이터 구조 변경은 조심스럽게 해야한다.](#%ec%ba%90%ec%8b%9c-%ec%82%ac%ec%9a%a9-%ec%8b%9c-%ec%84%b1%eb%8a%a5-%ec%b8%a1%ec%a0%95%ec%9d%80-%eb%b0%98%eb%93%9c%ec%8b%9c-%ed%95%98%ea%b3%a0-%ec%ba%90%ec%8b%9c-%eb%8d%b0%ec%9d%b4%ed%84%b0-%ea%b5%ac%ec%a1%b0-%eb%b3%80%ea%b2%bd%ec%9d%80-%ec%a1%b0%ec%8b%ac%ec%8a%a4%eb%9f%bd%ea%b2%8c-%ed%95%b4%ec%95%bc%ed%95%9c%eb%8b%a4)
    - [인증 후 인가 검증도 반드시 해야한다.](#%ec%9d%b8%ec%a6%9d-%ed%9b%84-%ec%9d%b8%ea%b0%80-%ea%b2%80%ec%a6%9d%eb%8f%84-%eb%b0%98%eb%93%9c%ec%8b%9c-%ed%95%b4%ec%95%bc%ed%95%9c%eb%8b%a4)
    - [로그인 실패 횟수를 제한하자.](#%eb%a1%9c%ea%b7%b8%ec%9d%b8-%ec%8b%a4%ed%8c%a8-%ed%9a%9f%ec%88%98%eb%a5%bc-%ec%a0%9c%ed%95%9c%ed%95%98%ec%9e%90)
    - [서비스용 DB 접근 계정에는 DDL 권한을 제거하자.](#%ec%84%9c%eb%b9%84%ec%8a%a4%ec%9a%a9-db-%ec%a0%91%ea%b7%bc-%ea%b3%84%ec%a0%95%ec%97%90%eb%8a%94-ddl-%ea%b6%8c%ed%95%9c%ec%9d%84-%ec%a0%9c%ea%b1%b0%ed%95%98%ec%9e%90)
    - [MQ도 DB만큼 수정과 접근 권한에 유의하자.](#mq%eb%8f%84-db%eb%a7%8c%ed%81%bc-%ec%88%98%ec%a0%95%ea%b3%bc-%ec%a0%91%ea%b7%bc-%ea%b6%8c%ed%95%9c%ec%97%90-%ec%9c%a0%ec%9d%98%ed%95%98%ec%9e%90)
    - [API 서버도 접근 통제가 필요하다.](#api-%ec%84%9c%eb%b2%84%eb%8f%84-%ec%a0%91%ea%b7%bc-%ed%86%b5%ec%a0%9c%ea%b0%80-%ed%95%84%ec%9a%94%ed%95%98%eb%8b%a4)
    - [Primary Key는 long으로](#primary-key%eb%8a%94-long%ec%9c%bc%eb%a1%9c)
    - [에러 로그는 일반적인 것과 Critical한 것을 구분하자.](#%ec%97%90%eb%9f%ac-%eb%a1%9c%ea%b7%b8%eb%8a%94-%ec%9d%bc%eb%b0%98%ec%a0%81%ec%9d%b8-%ea%b2%83%ea%b3%bc-critical%ed%95%9c-%ea%b2%83%ec%9d%84-%ea%b5%ac%eb%b6%84%ed%95%98%ec%9e%90)
    - [배포는 Rollback이 가능하게 하자.](#%eb%b0%b0%ed%8f%ac%eb%8a%94-rollback%ec%9d%b4-%ea%b0%80%eb%8a%a5%ed%95%98%ea%b2%8c-%ed%95%98%ec%9e%90)
    - [죽어도 괜찮은 서버를 구축하자.](#%ec%a3%bd%ec%96%b4%eb%8f%84-%ea%b4%9c%ec%b0%ae%ec%9d%80-%ec%84%9c%eb%b2%84%eb%a5%bc-%ea%b5%ac%ec%b6%95%ed%95%98%ec%9e%90)
- [Reference](#reference)

### 사용자 입력값은 무조건 검증한다.

사용자의 모든 입력은 검증한다. UI에서는 편리함을 위해 검증하고 서버 검증은 UI와 상관없이 반드시 해야한다. 악의적인 값 수정 등 또한 반드시 확인한다.

사례 : [프록시 툴 악용한 해킹](https://www.boannews.com/media/view.asp?idx=50574), 서버 간 요청-응답도 서로 확인을 해야한다.

### 사용자의 요청으로 Query가 조건없이 호출되면 안된다.

`SELECT * FROM table (without WHERE)`, `pageSize=1억`과 같은 호출이 발생하지 않게 검증해야 한다.

### 캐시 사용 시 성능 측정은 반드시 하고 캐시 데이터 구조 변경은 조심스럽게 해야한다.

Memcached나 Redis와 같은 원격 분산 캐시는 네트워크 대역폭을 점유하기 때문에 너무 많은 데이터를 담으면 캐시 히트율이 높아도 성능 저하의 원인이 된다. 캐시 데이터 구조의 변경은 변경 전/후의 데이터 차이로 누락 데이터가 발생할 수 있으니 조심스럽게 수정한다.

### 인증 후 인가 검증도 반드시 해야한다.

`/user/{userId}/order` 과 같은 요청의 경우 `/user/order` 요청으로 인증 세션에서 사용자 정보를 가져오도록 하자.

### 로그인 실패 횟수를 제한하자.

로그인 일정 횟수 이상 실패시 로그인 거부, 메일 발송, Captcha를 요구하자. 로그인 횟수 체크는 클라이언트가 아닌 서버에서 검증하자.

### 서비스용 DB 접근 계정에는 DDL 권한을 제거하자.

실수로 DB를 날리지 않도록 DBA 계정 말고는 DDL 권한을 제거하자.

### MQ도 DB만큼 수정과 접근 권한에 유의하자.

테스트용 서버에 production MQ가 연결되면 실제 Message가 테스트용 애플리케이션에서 소비된다.

### API 서버도 접근 통제가 필요하다.

실수로 production profile로 API 요청을 하지 않도록 권한 관리를 해야 한다.

### Primary Key는 long으로

Integer에서 Long으로 변경하기엔 비용이 너무 크다. 동시에 API와 연동하는 클라이언트에서도 Long으로 맵핑 해야한다.

### 에러 로그는 일반적인 것과 Critical한 것을 구분하자.

중요한 에러 로그는 알람을 잘 확인 가능하도록 구성하자.

### 배포는 Rollback이 가능하게 하자.

웹 앱 뿐만 아니라 Batch 애플리케이션도 롤백 가능하게 해야한다.

### 죽어도 괜찮은 서버를 구축하자.

Single Point of Failure(SPoF)를 제거하자. 안죽는 서버가 아닌 죽어도 괜찮은 서버가 되어야한다.

# Reference

- [개발자 머피의 법칙 - 우아한형제들 기술 블로그](http://woowabros.github.io/experience/2019/09/19/programmer-murphy-law.html?fbclid=IwAR01vN3Ya3QbiRQg5ylI0AtVOynioW65ssYPFtaK7Ve7c2ro5s0Nz0I4qDE)