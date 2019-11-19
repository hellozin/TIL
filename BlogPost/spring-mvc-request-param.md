@RequestParam 을 사용하며 겪은 에러를 공유하고자 합니다.

### @RequestParam

먼저 간단하게 **@RequestParam**에 대해 알아보겠습니다. Spring MVC에서는 **@RequestParam annotation**을 통해 쿼리 스트링 정보를 쉽게 가져올 수 있습니다.

예를 들어 `/user?name=hellozin` 이라는 요청에서 `"hellozin"` 이라는 값을 가져오기 위해 아래와 같이 컨트롤러를 구현하면 

```java
@GetMapping("/user")
@ResponseBody
public String getUserName(@RequestParam String name) {
  return name;
}
```

**@RequestParam**을 통해 **name** 변수에 `"hellozin"`이라는 문자열을 받아 처리할 수 있습니다.

하지만 저대로 사용하게 되면 요청 쿼리 스트링에 "name" 필드가 없을 경우 즉, `/user` 와 같이 **@RequestParam**이 적용된 필드가 없으면 `Bad Request, Required String parameter 'name' is not present` 라는 예외를 발생시킵니다.

이를 해결하기 위해 `@RequestParam(required = false)` 와 같이 **required** 속성을 추가하면 해당 필드가 쿼리스트링에 존재하지 않아도 예외가 발생하지 않습니다.

### 주의할 점

다만 주의할 점은 Argument Resolver에 등록한 **CustomArgument**에 `@RequestParam`을 적용하면 Spring이 해당 Argument를 무시한다는 것입니다. 이미 스프링에 익숙하신 분들은 당연하다는 생각이 드시겠지만 혹시 저와 같은 실수를 겪으신 분들을 위해 정리해 보겠습니다.

일반적으로 CustomArgument는 다음과 같이 정의됩니다.

- **CustomArgument** 클래스 작성
- **HandlerMethodArgumentResolver** 인터페이스 구현체 작성
- **WebMvcConfigure.addArgumentResolvers**에서 구현한 Resolver 등록

여기까지 하면 해당 Argument를 처리할 때 HandlerMethodArgumentResolver에 정의한 resolveArgument() 메소드에 위임해주게 됩니다.

하지만 다음과 같이 CustomArgument에 **@RequestParam(required=false)** 를 적용하면

```java
@GetMapping("/user")
@ResponseBody
public String getUserName(
    @RequestParam(required=false) CustomUser user) {
  return user.getName();
}
```

HandlerMethodArgumentResolver가 처리 할 때 아까 등록한 resolver가 불리지 않고 처리가 종료됩니다. 이 과정에서 어떠한 에러나 경고도 주어지지 않기 때문에 실수하지 않도록 주의해야합니다.

결국 Map<K,V> 형태나 CustomArgument의 경우 `required=false` 속성 없이도 예외가 발생하지 않기 때문에 직접 null 체크나 원하는 처리를 할 수 있습니다.

앞서 말씀드렸듯이 정말 당연하고 쉬운 내용이지만 쉽게 발견하지 못한 문제였기 때문에 포스팅 하였습니다. 틀린 내용이 있다면 언제든 지적 부탁드립니다.