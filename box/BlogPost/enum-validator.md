### Enum Validator, String이 Enum 인지 검증하는 방법

Entity나 DTO를 검증하기 위해 @NotBlank, @Email 등 `javax.validation.constraints.*` validation을 사용하다 보면 아래와 같이 필드에 Enum 타입을 String으로 입력받는 경우가 있습니다. 이러한 경우 Enum 타입도 함께 validation 하는 방법을 정리해 보았습니다.

```java
public class JoinRequest {
    @NotBlank 
    private String name;
    @??? 
    private String role;
    
    public void setRole(String role) {
        this.role = role;
    }    
}
```
```java
public enum Role {
    GUEST("GUEST"),
    USER("USER"),
    ADMIN("ADMIN");

    String value;
    Role(String value) { this.value = value; }
    public String value() { return value; }
}
```

먼저 Enum annotation을 아래와 같이 작성해 줍니다.

```java
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {EnumValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Enum {
    String message() default "Invalid value. This is not permitted.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends java.lang.Enum<?>> enumClass();
    boolean ignoreCase() default false;
}
```

- `@Constraint(validatedBy = {EnumValidator.class})` : 해당 annotation이 실행 할 ConstraintValidator 구현체를 `EnumValidator`로 지정합니다.
- `@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})` : 해당 annotation은 메소드, 필드, 파라미터에 적용 할 수 있습니다.
- `@Retention(RetentionPolicy.RUNTIME)` : annotation을 Runtime까지 유지합니다.

다음으로는 `EnumValidator`를 작성합니다.

```java
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<Enum, String> {

    private Enum annotation;

    @Override
    public void initialize(Enum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean result = false;
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value.equals(enumValue.toString())
                        || (this.annotation.ignoreCase() && value.equalsIgnoreCase(enumValue.toString()))) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

}

```

Validation에 대한 설정은 모두 끝났습니다. 앞서 작성한 annotation을 검증하고자 하는 곳에 적용해 줍니다.

```java
public class JoinRequest {
    @NotBlank 
    private String name;
    @Enum(enumClass = Role.class, ignoreCase = true)
    private String role;
    
    public void setRole(String role) {
        this.role = role;
    }    
}
```

이제 JoinRequest를 validation 할 때 role 필드가 Enum Role에 존재하지 않으면 `MethodArgumentNotValidException`을 던집니다. 메시지는 다음과 같이 생성됩니다.

- 요청
```
POST /join
body: {
  "name": "hello",
  "role": "anonymous"
}
```

- 컨트롤러
```java
@RestController
public class SimpleController {
    @PostMapping("/join")
    public String join(@Valid @RequestBody JoinRequest joinRequest) {
        return joinRequest.role;
    }
}
```

- 에러메시지
```java
org.springframework.web.bind.MethodArgumentNotValidException:
Validation failed for argument [0] in public java.lang.String me.hello.blog.SimpleController.join(me.hello.blog.JoinRequest):
  Field error in object 'joinRequest' on field 'role': rejected value [anonymous];
  codes [Enum.joinRequest.role,Enum.role,Enum.java.lang.String,Enum];
  arguments [org.springframework.context.support.DefaultMessageSourceResolvable:
  codes [joinRequest.role,role];arguments [];
  default message [role],class me.hello.blog.Role,false];
  default message [Invalid value. This is not permitted.]]
```