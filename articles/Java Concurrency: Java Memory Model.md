[Java Concurrency: Java Memory Model](https://medium.com/javarevisited/java-concurrency-java-memory-model-96e3ac36ec6b)

# Title

Java Concurrency: Java Memory Model

# Tag

java, concurrency

# Review

읽다 보니 자바 메모리 모델은 stack, heap으로 나뉘어 있고 경우에 따라 volatile, synchronization을 잘 활용해야 한다는 기본적인 내용이라 조금 아쉬웠다.

# Summary

**자바 메모리 모델**은 쓰레드가 메모리와 어떻게 상호작용하는지를 설명한다

1995년 개발된 자바 메모리 모델은 불안정하고 비효율적인 것으로 여겨졌지만 자바 커뮤니티를 통해 꾸준히 업데이트 되어 자바 5.0 버전에 사용된 자바 메모리 모델은 현재(자바 14+)까지도 사용되고 있다

자바 메모리 모델은 여러 쓰레드가 값을 공유하고 한 쓰레드가 그 값을 수정할 때 다른 쓰레드가 어떻게 올바른 값을 조회할 수 있는지를 설명하기 때문에 concurrent 한 프로그래밍을 위해서는 자바 메모리 모델을 잘 이해해야 한다

## 자바 메모리 모델의 내부구조

자바 메모리 모델은 내부적으로 쓰레드 stack과 heap 메모리를 분리하기 위해 사용된다

JVM에서 동작하는 쓰레드는 모두 개별 stack 메모리를 가지고 있고 호출하는 함수 실행 후 돌아올 위치를 저장한다

또한 모든 지역변수를 저장하기 때문에 쓰레드는 서로 각각의 지역변수에 접근할 수 없다

heap에는 생성된 모든 오브젝트를 저장하고 (Integer, Boolean과 같은 Wrapper class 포함) 쓰레드 내부에서 오브젝트를 생성하면 레퍼런스는 쓰레드 stack에 생성된 오브젝트는 heap에 저장된다

static class 변수도 당연히 heap에 저장된다

## 하드웨어 메모리 구조

최근 하드웨어 메모리 구조는 자바 메모리 구조와 조금 다른데 깊은 이해를 위해 둘 다 알고 있어야 한다

최근 2개 이상의 코어를 가진 CPU가 대부분인데 이는 여러개의 쓰레드를 동시에 실행할 수 있다는 의미이다

대략적인 구조는 CPU - CPU Register - CPU Cache Memory - RAM 와 같은 형태이다

## 자바 메모리 모델과 하드웨어

하드웨어 메모리 구조는 JVM의 stack과 heap 메모리를 구분하지 않는다

이때문에 아래 두가지 문제가 발생할 수 있다

- 공유하는 변수의 수정에 대한 가시성
- 공유하는 변수에 대한 조회, 검증, 수정에 따른 race condition

## 공유변수의 가시성

`volatile`이나 synchronization 처리 없이 공유변수를 수정하면 다른 쓰레드에서 수정된 값을 확인할 수 없을 수 있다

즉, 같은 오브젝트에 대해 각각의 CPU Cache가 서로 다른 값을 가질 수 있고 의도한 대로 동작하지 않을 수 있다