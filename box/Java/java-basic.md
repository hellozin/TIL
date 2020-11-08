# 잊기 쉬운 Java 기본 지식

## final 키워드

**final 키워드의 위치 별 용도**

|위치|용도|
|--|--|
|Class|해당 클래스 상속 불가|
|Method|해당 메소드 Overriding 불가|
|Reference Value|객체 재할당 불가|
|Primitive Value|값 변경 불가|

## static 키워드

JVM에 따라 다르지만 일반적으로 Class가 로딩되는 시점에 static 멤버가 생성된다. 따라서 객체의 생성 없이 사용이 가능하다.

## Anonymous Inner Class

```java
class Stub {
  String name;
  int value;
  // const, getter
}
```

```java
Stub stub = new Stub() {
    @Override
    public String getName() {
      return "anything";
    }
    @Override
    public int getValue() {
      return 1;
    }
}
```