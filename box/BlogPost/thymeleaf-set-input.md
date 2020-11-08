**Spring MVC**와 **Thymeleaf**를 사용해 Form 맵핑 시 **Set\<T\>** 타입의 input을 처리하는 방법입니다.

**Set\<String\>** 필드를 가지는 **Post** 객체가 있다고 할 때,

**Post.java**
```java
public class Post {
    private String title;
    private String content;
    private Set<String> tags;
    // constructor and getter
}
```

아래와 같이 **select**나 **checkbox** 태그를 이용해 사용자의 입력값을 Post 객체로 맵핑할 수 있습니다.

**posting-form.html**
```html
<form th:object="${post}" th:action="@{/post}" th:method="post">
    <input type="text" th:field="*{title}">
    <input type="text" th:field="*{content}">

    <!-- Using select tag -->
    <select id="tags" th:field="*{tags}" size="3" multiple="multiple">
        <option th:each="tag : ${tags}" th:value="${tag}" th:text="${tag}">
            Tag
        </option>
    </select>

    <!-- Using checkbox tag -->
    <div th:each="tag : ${tags}">
        <input type="checkbox" 
        th:id="${tag}" th:value="${tag}" th:field="*{tags}">
        <label th:for="${tag}" th:text="${tag}">Tag</label>
    </div>

    <button type="submit">저장</button>
</form>
```

