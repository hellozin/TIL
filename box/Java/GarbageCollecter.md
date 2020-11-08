stop the world : GC를 실행하기 위해 JVM이 애플리케이션 실행을 멈추는 것.

어떤 gc 라도 stop the world 는 발생, 대개 gc 튜닝이란 stop the world 줄이는 것.

> 가끔 명시적으로 메모리를 해제하기 위해 객체를 `null`로 지정하거나 `System.gc()` 를 호출하는 개발자도 있는데 `null` 은 큰 문제가 안되지만 `System.gc()` 메서드를 호출하는 것은 시스템의 성능에 매우 큰 영향을 끼치기 때문에 절대 사용해서는 안된다.

Java는 프로그램 코드로 메모리를 명시적으로 해제하지 않는다. 가비지 컬렉터는 더 이상 필요 없는 (쓰레기) 객체를 찾아 지우는 작업을 하며 아래 두가지 (가설)전제 조건 하에 만들어 졌다.

- 대부분의 객체는 금방 접근 불가능 상태(unreachable)가 된다.
- 오래된 객체에서 젊은 객체로의 참조는 아주 적게 존재한다.

이러한 가설을 'weak generational hypothesis'

이 가설의 장점을 최대로 살리기 위해 HotSpot VM에서는 크게 물리적 공간을 2개로 나누었다. Young 영역 Old 영역

Young

### Reference

- [Java Garbage Collection - D2](https://d2.naver.com/helloworld/1329)