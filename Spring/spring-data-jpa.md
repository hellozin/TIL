### 관계 맵핑

관계는 두 Entity 사이에서 발생하며 둘 중 하나는 관계의 주인(owner)이고 나머지 하나는 종속(non-owning)된다.

**단방향 관계**

단방향 관계에서는 관계를 정의한 쪽이 주인이 된다. 즉, 아래의 경우 `Person`과의 관계에서 `Reservation`이 주인이 된다.

```java
class Reservation {
  @ManyToOne 
  Person customer;
}
```

이 때, `@ManyToOne` 일 경우 기본값으로 외래키(Foreign Key) 컬럼이 생성되고 `@OneToMany` 일 경우 Join 테이블이 생성된다.

**양방향 관계**

외래키(FK)를 가지고 있는 쪽이 주인이 된다.(`@ManyToOne`을 정의한 쪽) 주인이 아닌 쪽에서는 `mappedMy`를 통해 관계를 맺고 있는 필드를 설정해야 하며 주인에게 관계를 설정해야 DB에 반영이 된다.

```java
class Person {
  @OneToMany(mappedby = "customer")
  private List<Reservation> reservations;

  public addReservation(Reservation reservation) {
    this.getReservations().add(reservation);
    reservation.setCustomer(this);
  }
}

class Reservation {
  @ManyToOne
  Person customer;
}
```

---

### Web 관련 기능들

**Domain Class Converter**

```java
@GetMapping("/post/{id}")
public String getPost(@PathVariable Long id) {
  ...
}
```

Spring MVC의 Data Binder가 id 문자열을 Long 타입으로 변환해 준다.

Converter: 하나의 타입에서 다른 타입으로 변환할 수 있게 해주는 것

DomainClassConverter는 Spring의 Converter registry에 등록되서 사용할 수 있게 된다.

```
@PathVariable("id") Post post
```

위의 코드를 가능하게 해준다.

### @EnableJpaRepositories

@Configuration 와 함께 사용해야 JpaRepository<S,T> 를 사용할 수 있지만 Spring Boot를 사용할 경우 자동 설정으로 포함되어 있다.

### save()

새로운 Entity를 save() 할 경우 persist, 이미 존재하는 Entity를 save() 할 경우 merge. save() 반환값을 수정할 경우 update 쿼리가 발생한다.

### Named Parameter

```java
/* FROM */
@Query("SELECT p FROM Post AS p WHERE p.title = ?1")
List<Post> findByTitle(String keyword);

/* TO */
@Query("SELECT p FROM Post AS p WHERE p.title = :title")
List<Post> findByTitle(@Param("title") String keyword);
```

### Update 쿼리

```java
@Modifying
@Query("UPDATE ...")
```

객체가 persist 상태가 유지되고 있을 경우 update가 원하는 순간에 작동하지 않을 수 있다.(Modifying(clearAutomatically = true)로 해결 가능) 같은 의미로 delete도 권장하지 않음.

### EntityGraph

Fetch 모드를 설정

**@NamedEntityGraph**

@Entity에서 재사용 할 여러 엔티티 그룹을 정의할 때 사용

```java
@NamedEntityGraph(
  name = "Comment.post", 
  attributeNodes = @NamedAttributeNode("post"))
@Entity
public class Comment {
  ...
}
...
public interface CommentRepository ... {
  @EntityGraph(value = "Comment.post")
  Optional<Comment> findCommentById(...);
}
```

**@EntityGraph(attributePaths...)**

NamedEntityGraph를 설정하지 않아도 필요한 객체를 명시해서 사용 가능

```java
public interface CommentRepository ... {
  @EntityGraph(attributePaths = "post")
  Optional<Comment> findCommentById(...);
}
```

### Projection

Entity의 일부 정보만 가져오고 싶을 때

인터페이스, 클래스 둘 다 가능

**Closed 프로젝션**

성능최적화

```java
public interface CommantSummary {
  String getAuthor();
}

Optional<CommentSummary> findBy...;

/* Created SQL */
SELECT comment.author FROM comment...
```

**Open 프로젝션**

target이 엔티티 전체를 받아오기 때문에 성능 최적화는 바랄 수 없지만 값으로 새로운 값을 생성할 수 있음

```java
@Value("#{target.author + ' ' + target.id}")
String authorAndId;
```

Closed로 구현하려면

```java
public interface CommentSummary {
  ...
  default getAuthorAndId() {
    return getAuthor() + ' ' + getId();
  }
}
```

**부록**

```java
public interface CommentOnly {
  String getComment();
}

public interface AuthorOnly {
  String getAuthor();
}
```

이렇게 프로젝션을 구현 한 경우 아래와 같이 오버라이딩이 불가능 할 때는

```java
Optional<CommentOnly> findById;
Optional<AuthorOnly> findById;
```

Generic을 사용해 원하는 타입을 함께 입력받는다.

```java
<T> List<T> findById(Long id, Class<T> type);
```

### 연관 관계 엔티티의 속성값 이름으로 쿼리 생성

연관관계 엔티티_필드이름 형식으로 사용하면 동작
ex) `...Author_Id...`
```java
class Post {
  ...
  Author author;
  ...
}
class Author {
  ...
  Long id;
  ...
}

List<Post> findByAuthor_IdOrderByCreatedDateDesc(Long authorId);
```

### Specifications

특정 조건절을 모듈화 할 수 있다. Predicate와 유사

```java
public class Specs {
  public static Specification<Comment> isGood() {
    return (Specification<Comment>) (root, query, builder) ->
      builder.greaterThanOrEqualTo(root.get(Comment_.up), 10);
  }
}
```

다음과 같이 사용하고 .and() 나 .or() 를 통해 여러 Spec을 조합할 수 도 있다.

```java
commentRepository.findAll(Specs.isGood());
```

### Query By Example

잘 안쓸거다

### @Transactional

메소드에 가장 가까운 설정이 사용된다.

RuntimeException 과 Error 가 발생하면 rollback 된다. checked exception은 해당하지 않는다.

checked exception에서 추가하고 싶은 것은 rollbackFor... 메소드를 이용, rollback에서 제외하고 싶은 exception은 noRollbackFor... 메소드를 이용하면 된다.

@Transactional(readOnly = true)를 적용했을 때 장점

Flush 모드를 NEVER로 설정해 Dirty checking을 하지 않는다.

**Isolation 속성**

동시성 처리에 대한 설정. 성능과 안전성 사이의 고민이 필요함.

- DEFAULT
- READ_COMMITTED
- READ_UNCOMITTED
- REPEATABLE_READ
- SERIALIZABLE

**Propagation 속성**

트랜잭션을 어떻게 전파시킬 것인지, 트랜잭션 내에서 다른 트랜잭션을 호출했을때 (nested transaction) 어떻게 처리할 것인가.

- NESTED
- NOT_SUPPORTED
- NEVER
- 등등...

### Auditing

엔티티의 변화를 기록하는 기능

```java
class SpringSecurityAuditorAware implements AuditorAware<User> {
  public User getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if(authentication == null || !authentication.isAuthenticated()) {
      return null;
    }

    return ((MyUserDetails) authentication.getPrincipal()).getUser();
  }
}

/* and */
@EnableJpaAuditing(auditorAwareClass = "springSecurityAuditorAware")
```

좀 더 Generic 한 기능으로 JPA의 라이프사이클을 이용하는 방법

JPA의 라이프사이클: 엔티티에 변화가 생겼을 때 특정한 콜백을 실행할 수 있는 이벤트를 발생시킨다.

Auditing 관련 설정이 없어도 사용 가능

```java
@PrePersist
public void prePersist() {
  this.created = new Date();
  this.createdBy = SecurityContextHolder.getContext().getAuthentication();
}
```

지원하는 라이프 사이클 Callbacks

- @PrePersist
- @PostPersist
- @PreRemove
- @PostRemove
- @PreUpdate
- @PostUpdate
- @PostLoad

651
792
528