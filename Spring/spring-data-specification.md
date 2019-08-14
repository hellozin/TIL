Spring Data에서 Specification은 DB 쿼리의 조건을 Spec으로 작성해 Repository method에 적용하거나 몇가지 Spec을 조합해서 사용할 수 도 있습니다. 간단한 예제로 Specification 사용 방법을 소개해 보겠습니다.

먼저 Spcification을 모르는 상태에서 제목, 태그, 좋아요 수 필드를 가진 Post객체를 각 필드별로 검색하는 기능을 구현하기 위해 다음과 같이 `Entity`, `Repository`, `Controller`를 작성해 보겠습니다.

Post.java
```java
@Entity
public class Post {
    @Id @GeneratedValue
    private Long id;
    private String title;
    private String tag;
    private int likes;
...
}
```

PostRepository.java
```java
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByTitle(String title);
    List<Post> findAllByTag(String tag);
    List<Post> findAllByLikesGreaterThan(int likes);
}
```

PostController.java
```java
@RestController
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/post/list")
    public List<Post> getPostList(@RequestParam(required = false) String title,
                                  @RequestParam(required = false) String tag,
                                  @RequestParam(required = false) Integer likes) {
        if (title != null) {
            return postRepository.findByTitle(title);
        } else if (tag != null) {
            return postRepository.findByTag(tag);
        } else if (likes != null) {
            return postRepository.findByLikesGreaterThan(likes);
        } else {
            return postRepository.findAll();
        }
    }

}
```

쿼리 파라미터를 통해 검색 조건을 입력받고 조건이 없을 경우 전체 데이터를 받아와야 하기 때문에 `@RequestParam`에 `required=false` 조건을 추가했습니다.

각 조건에 따라 실행하는 메소드를 추가로 작성해야 하고 if 조건문으로 검색할 내용을 구문해야 하기 때문에 코드가 지저분해졌습니다. 또한 하나의 method에서 하나의 조건만 처리하기 때문에 `title`과 `tag`를 동시에 검색하는 기능을 구현하기도 어렵습니다. 

지금은 조건이 얼마 없어 괜찮지만 나중에 조건이 추가될 수록 코드를 유지 보수하기 어려워 질 것 같습니다.

## Spcification

Specification을 적용하기 위해서는 Repository에 `JpaSpecificationExecutor<T>` 인터페이스를 추가로 상속받아야 합니다.

```java
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    ...
}
```

먼저 title로 Post를 검색하는 Specification을 작성해 보겠습니다.

```java
public class PostSpecs {
    public static Specification<Post> withTitle(String title) {
        return (Specification<Post>) ((root, query, builder) -> 
                builder.equal(root.get("title"), title)
        );
    }
}
```

해당 람다식은 Specification의 toPredicate() 메소드를 구현한 것입니다.
```java
@Nullable
Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);
```

`root.get("title")`을 통해 Post 인스턴스의 title 필드가 매개변수 `title`과 일치하는 값을 반환합니다.

이렇게 만들어진 Specification은 Repository의 인자값에 넣어 적용할 수 있습니다.

```java
@RestController
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/post/list")
    public List<Post> getPostList(@RequestParam(required = false) String title,
                                  @RequestParam(required = false) String tag,
                                  @RequestParam(required = false) Integer likes) {
        if (title != null) {
            return postRepository.findAll(PostSpecs.withTitle(title));
        } else if (...) {
            ...
        }
    }

}
```

이렇게 하면 Repository는 findAll() 메소드 하나로 여러 조건에 해당하는 결과를 만들어 낼 수 있게 되었습니다. 하지만 여전히 조건의 수 만큼 `@RequestParam`이 추가되고 Controller의 조건문도 늘어날 것입니다.

이를 해결하기 위해 먼저 PostSpecs에서 지원할 조건을 Enum으로 정의합니다.

```java
public class PostSpecs {

    public enum SearchKey {
        TITLE("title"),
        TAG("tag"),
        LIKESGREATERTHAN("likes");

        private final String value;

        SearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
...
}
```

각 Enum의 값은 Post에서 참조할 필드명과 일치시키고 Specification을 정의하는 코드를 다음과 같이 변경합니다.

```java
...
public static Specification<Post> searchWith(Map<SearchKey, Object> searchKeyword) {
    return (Specification<Post>) ((root, query, builder) -> {
        List<Predicate> predicate = getPredicateWithKeyword(searchKeyword, root, builder);
        return builder.and(predicate.toArray(new Predicate[0]));
    });
}

private static List<Predicate> getPredicateWithKeyword(Map<SearchKey, Object> searchKeyword, Root<Post> root, CriteriaBuilder builder) {
    List<Predicate> predicate = new ArrayList<>();
    for (SearchKey key : searchKeyword.keySet()) {
        switch (key) {
            case TITLE:
            case TAG:
                predicate.add(builder.equal(
                        root.get(key.value),searchKeyword.get(key)
                ));
                break;
            case LIKESGREATERTHAN:
                predicate.add(builder.greaterThan(
                        root.get(key.value), Integer.valueOf(searchKeyword.get(key).toString())
                ));
                break;
        }
    }
    return predicate;
}
```

`List<Predicate>`를 생성하고 입력받은 searchKey들에 대해 원하는 기능을 구현합니다. 여기서는 title과 tag의 경우 입력받은 값과 일치하는 결과를 반환하고 likes는 입력받은 값보다 큰 경우를 반환합니다.

`case LIKESGREATERTHAN:`에서 `searchKeyword.get(key)`에 toString()을 적용한 뒤 Integer.parseInt를 하는 이유는 `searchKeyword.get(key)` Integer에 Object를 지원하는 메소드가 없기 때문입니다.

컨트롤러에서는 DTO와 ArgumentResolver를 통해 간결하게 할 수 있지만 여기서는 그냥 Map을 사용해 구현해 보겠습니다.

```java
@GetMapping("/post/list")
public List<Post> getPostList(@RequestParam(required = false) Map<String, Object> searchRequest) {
    Map<SearchKey, Object> searchKeys = new HashMap<>();
    for (String key : searchRequest.keySet()) {
        searchKeys.put(SearchKey.valueOf(key.toUpperCase()), searchRequest.get(key));
    }
    return searchKeys.isEmpty()
            ? postRepository.findAll()
            : postRepository.findAll(PostSpecs.searchWith(searchKeys));
}
```

`@RequestParam`으로 입력받은 쿼리 파라미터를 Map에 저장하고 String을 SearchKey로 변환해 새로운 Map에 저장합니다. 새로 저장한 Map에 값이 없을 경우 `findAll()`, 있을 경우 위에서 정의한 searchWith(Map<SearchKey, Object>) 메소드에 매개변수로 전달합니다.

확인을 위해 테스트를 수행해 보면 올바르게 동작하는 것을 확인할 수 있습니다. 하지만 모든 조건을 하나의 메소드로 불필요한 단계가 증가할 수 있기 때문에 상황에 따라 고민하는 것이 좋을 것 같습니다.

### 테스트

```java
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @BeforeAll
    public void 데이터_추가() {
        for (int i = 0; i < 10; i++) {
            postRepository.save(new Post("title"+i, "study"+(i%3), i));
        }
    }

    @Test
    public void 포스트_전체조회() throws Exception {
        mockMvc.perform(get("/post/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));
    }

    @Test
    public void 포스트_제목으로_조회() throws Exception {
        mockMvc.perform(get("/post/list")
                .param("title", "title0")
        )
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void 포스트_태그로_조회() throws Exception {
        mockMvc.perform(get("/post/list")
                .param("tag", "study0")
        )
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    public void 포스트_좋아요수로_조회() throws Exception {
        mockMvc.perform(get("/post/list")
                .param("likesGreaterThan", "6")
        )
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(3)));
    }

}
```