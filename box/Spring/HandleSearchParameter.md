# Handle Search Parameter

예전에 JPA Specification을 사용해 검색 파라미터에 따라 간단하게 처리하는 방법에 대해 쓴 적이 있습니다.

그러다 얼마 전 일반 Java 만으로 위와 같은 코드는 어떻게 작성할까? 고민하며 검색을 해 보았는데 검색 키워드가 애매해 명확한 답을 얻지 못했고 간단하게 직접 만들어 보기로 했습니다.

우선 원하는 컨셉은 다음과 같습니다.

1. 컨트롤러에서 원하는 키값(예를 들면 "userId", "orderByCreateAt")에 대한 처리 로직을 미리 작성
2. 실제 요청이 왔을 때 작성된 검색 로직을 적용해 결과 리스트를 반환

위 내용을 간단한 수도 코드로 변경해보면

```java

Specification spec;

private void buildSpec() {
    spec = new Specification();

    spec.addMatcher("userId", (originUserId, inputUserId) -> originUserId.equals(inputUserId));
}

@GetMapping("/user")
public List<User> getUserList(@RequestParam Long userId) {
    List<User> originUserList = getUserList();
    return spec.searchWith(originUserList);
}

```

위와 같은 컨셉이 나오게됩니다.

이를 좀 더 구체화 시키며 Specification을 완성해 보겠습니다.

```java
public class Specicifation<T> {

    private Map<String, BiPredicate<T, Object>> functionMap = new HashMap<>();

    public void addMatcher(String paramKey, BiPredicate<T, Object> matchCondition) {
        functionMap.put(paramKey, matchCondition);
    }

    public List<T> search(List<T> origin, Map<String, Object> params) {
        Predicate<T> predicate = x -> true;

        for (Entry<String, Object> entry : params.entrySet()) {
            if (functionMap.containsKey(entry.getKey())) {
                predicate = predicate.and(item -> functionMap.get(entry.getKey()).test(item, entry.getValue()));
            }
        }
        return origin.stream().filter(predicate).collect(Collectors.toList());
    }

}
```