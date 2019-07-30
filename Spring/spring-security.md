### Spring Security 간단 설정

### Password 암호화, PasswordEncoder

이름에서 알 수 있듯 password를 암호화하는 클래스입니다. 빈으로 등록하기 위해 아래와 같이 정의합니다.

```
@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
```

암호화 방식에는 MD5, Sha 등의 방법도 있지만 최근에는 취약점으로 인해 deprecated 되어 BCrypt를 권장하고 있습니다.

특징으로는 Sha와 같이 암호화에 내부 key를 입력받지 않고 랜덤으로 키를 생성해 암호화에 사용합니다. 그래서 매번 다른 결과를 반환하기 때문에 같은 password에 대해 encoding은 한번만 적용되어야 일관된 결과를 얻을 수 있습니다.

또한 encoding 된 문자열의 길이가 60 이므로 저장시 충분한 공간이 확보되어야 합니다.

> [Registration with Spring Security – Password Encoding](https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt)

### HttpSecurity

### CSRF

CSRF(Cross site request forgery)는 발행된 토큰이 요청마다 포함되는 것을 이용해 다른 페이지나 Javascript를 통해 악의적으로 사용하는 것을 말합니다. 

> [Spring Security - CSRF](https://docs.spring.io/spring-security/site/docs/3.2.5.RELEASE/reference/htmlsingle/#csrf)

### WebExpressionVoter

AccessDecisionManager로 WebExpressionVoter()를 추가하면 SpEL을 @PreAuthorize를 사용한 request 인증하는데 사용할 수 있습니다.

```java
@Bean
public AccessDecisionManager accessDecisionManager() {
    List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
    decisionVoters.add(new WebExpressionVoter());
    // 모든 voter가 승인해야 해야한다.
    return new UnanimousBased(decisionVoters);
}
```

### Reference

- [Registration with Spring Security – Password Encoding](https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt)
- [Spring Security - CSRF](https://docs.spring.io/spring-security/site/docs/3.2.5.RELEASE/reference/htmlsingle/#csrf)