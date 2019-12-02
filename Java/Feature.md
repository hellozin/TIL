# Anonymous Inner Class
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