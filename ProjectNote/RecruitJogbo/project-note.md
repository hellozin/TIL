
### 문제

테스트 코드 작성 중 NPE 발생

테스트 코드
```
@AutoWired
private MemberService memberService;


Member member = memberService.join(userId, password, name, email);
```
에러메시지
```
java.lang.NullPointerException
	at univ.study.recruitjogbo.member.MemberServiceTest.사용자를_추가한다(MemberServiceTest.java:40)
```

### 해결

JUnit5사용

@TestInstance(TestInstance.Lifecycle.PER_CLASS) 를 추가하지 않아서

### 고민

- review도 있고 꿀팁도 있는데 둘을 하나의 인터페이스로 이어야하나?

둘을 함께 쓸 일이 없으니 분리해도 괜찮을것

- Rest하게 구현하려면 stateless 해야한다. 이러면 분산환경에서도 이득일듯

### 문제

application.yml에 주석을 한글로 달았더니 다음과 같은 error 발생

```java
java.lang.IllegalStateException: Failed to load ApplicationContext
...
Caused by: org.yaml.snakeyaml.error.YAMLException: java.nio.charset.MalformedInputException: Input length = 1
```

### 해결방법

주석을 영어로 변경

### 2주차 후기

OAuth의 버전변경과 마이그레이션으로 인해 레퍼런스간 차이로 막힘

호환되는 버전을 찾아 해결

### 에러

테스트 실행 시

```
org.junit.platform.launcher.core.DefaultLauncher handleThrowable
TestEngine with ID 'junit-jupiter' failed to execute tests
org.junit.platform.commons.util.ReflectionUtils.tryToLoadClass(Ljava/lang/String;)
```

`testImplementation 'org.junit.platform:junit-platform-commons:1.4.2'` 추가 

---

SpringBootTest 를 작성하는데 MailSender 와 같이 property를 사용하는 빈이 존재해 실제 테스트에서 사용하지도 않는 설정을 추가해야 한다.

`@TestPropertySource(properties = {"spring.config.location=classpath:/google.yml", "spring.config.location=classpath:/mail.yml"})`
