- [Property 여러개 적용](#property-%ec%97%ac%eb%9f%ac%ea%b0%9c-%ec%a0%81%ec%9a%a9)
- [@SpringBootTest에서 여러개의 Yaml 프로퍼티 적용](#springboottest%ec%97%90%ec%84%9c-%ec%97%ac%eb%9f%ac%ea%b0%9c%ec%9d%98-yaml-%ed%94%84%eb%a1%9c%ed%8d%bc%ed%8b%b0-%ec%a0%81%ec%9a%a9)
- [Property 값 불변 객체로 가져오기](#property-%ea%b0%92-%eb%b6%88%eb%b3%80-%ea%b0%9d%ec%b2%b4%eb%a1%9c-%ea%b0%80%ec%a0%b8%ec%98%a4%ea%b8%b0)
- [Reference](#reference)

# Property 여러개 적용

```java
private static final String PROPERTIES =
        "spring.config.location="
        +"classpath:/application.yml"
        +",classpath:/anothor.yml"
        +",file:./secret.yml";

public static void main(String[] args) {
    new SpringApplicationBuilder(RecruitJogbo.class)
            .properties(PROPERTIES)
            .run(args);
}
```

# @SpringBootTest에서 여러개의 Yaml 프로퍼티 적용

```java
@SpringBootTest(properties = 
        "spring.config.location=" +
        "classpath:/application.yml" +
        ",classpath:/secret.yml"
)
```

# Property 값 불변 객체로 가져오기

**properties**

```yml
user:
  name: hellozin
```

**안티 패턴 : Environment**

```java
final Environment env;
env.getProperty("user.name");
```

혹은
```java
@Value("${user.name}")
String name;
```

Type Unsafe, 변경 관리 어려움

**추천 패턴 (Spring Boot 2.2.1 미만) : ConfigurationProperties**

```java
@Configuration
@ConfigurationProperties(prefix = "user")
@Validated
public class SampleProperties {
  @NotEmpty
  private String name;
  // getter, setter
}
```

단점 : Setter로 인해 가변 객체로 생성

**추천 패턴 (Spring Boot 2.2.1 이상) : ConstructorBinding**

```yml
test:
  value: hellozin
  inner:
    value: inner
```

```java
@ConfigurationProperties(prefix = "test")
@ConstructorBinding
@Validated
@AllArgsConstructor
@Getter
public class SampleProperties {
    @NotEmpty
    private String value;
    private Inner inner;

    @AllArgsConstructor
    @Getter
    public static class Inner {
        @NotEmpty
        private String value;
    }
}
...
@SpringBootApplication
@EnableConfigurationProperties(SampleProperties.class)
public class Application {
```

# Reference

- [github.com/cheese10yun](https://github.com/cheese10yun/spring-jpa-best-practices/blob/master/doc/step-10.md)
- [docs.spring.io](https://docs.spring.io/spring-boot/docs/2.2.0.RC1/reference/htmlsingle/#boot-features-external-config-constructor-binding)