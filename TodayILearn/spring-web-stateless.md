Spring Web 프로젝트를 stateless하게 구현하는 방법을 공부하는 문서입니다.

Stackoverflow에 [관련 대답](https://stackoverflow.com/a/43334360)이 있어 간단히 옮겨왔습니다.

## Question

How to make stateless web applications? Especially with Spring MVC?

## Answer

### 인증에서 세션을 떼어놓아라

세션은 서버를 분산시키거나 확장하기 어렵게 만듭니다. 또한 로드 밸런싱을 적용하기도 어렵습니다. [Problem with Session State timeing out on Load Balanced Servers](https://forums.asp.net/t/2032217.aspx?Problem+with+Session+State+timeing+out+on+Load+Balanced+Servers)

### 토큰 기반 인증을 사용해라

Stateless 한 시스템은 주로 토큰 기반의 인증을 사용합니다. 좋은 예로 Firebase가 있으며 토큰에서 Map 형태로 User 인증과 관련된 내용을 추출해 사용합니다.

### Stateless와 'Data persistencee'-less를 혼동하지 마라



### Caching이 필요하면 cookie를 사용해라

자주 요청되는 데이터를 쿠키에 캐싱하면 상당한 성능 향상에 도움이 될것입니다. 다만 쿠키가 어떠한 서버의 상태와도 연결되지 않아야 하며 클라이언트에서 쿠키를 잃어버리더라도 서버에 영향을 주어선 안됩니다.