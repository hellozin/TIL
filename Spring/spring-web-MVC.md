```java
@ModelAttribute
public void categories(Map<K,V> model) {
  model.put("categories", List.of("case1", "case2"...));
}
```

모든 요청에 "categories" 라는 모델을 저장한다.

```java
@ModelAttribute("categories")
public List<Category> categories() {
  return List.of("case1", "case2"...);
}
```

이렇게도 사용 가능

### FlashMap

Header 요청을 처리하는 방법

특정 헤더를 가지고 있는 혹은 없는 요청만 처리할 때

- `@GetMapping(headers = HttpHeaders.특정헤더)`

특정 헤더가 없는 요청을 처리할 때

- `@GetMapping(headers = "!" + HttpHeaders.특정헤더)`

특정 헤더와 값이 있는 요청을 처리할 때

- `@GetMapping(headers = "!" + HttpHeaders.특정헤더 + "=" +"value")`

Parameter에도 동일하게 사용 가능하다.

- `@GetMapping(params = "myKey")`

### RedirectAttributes

Spring MVC에서는 Model에 값을 추가한 상태에서 Redirect가 발생하면 Model의 primitype인 Key/Value가 쿼리 파라미터 형식으로 추가된다. 하지만 Spring Boot에서는 이 기능을 기본적으로 비활성화 해놓았다. Boot에서 이 기능을 사용하려면 application.property에 아래 설정을 추가해 활성화 할 수 있다.
`spring.mvc.ignore-default-model-on-redirect=false`

> 해당 설정은 WebMvcAutoConfiguration.java의 requestMappingHandlerAdapter() 메소드에서 확인할 수 있다.

만약 Model의 모든 값을 전달하고 싶지 않으면 RedirectAttributes로 원하는 값만 추가하면 된다.

```java
redirectAttributes.addAttribute("key", "value")
```

### FlashAttributes

RedirectAttributes는 쿼리 파라미터로 전달이 되기 때문에 문자열로 변환이 가능해야한다. 그래서 복합 객체의 경우 전달하기 어려운 부분이 있다.

그럴땐 RedirectAttributes의 addFlashAttribute() 메소드를 사용할 수 있다. 여기에 추가된 데이터는 Session에 추가되고 redirect된 핸들러에서 자동으로 제거된다. Url에서도 숨겨진다.