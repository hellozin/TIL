# Section 1 : Introduction

**Why need Thread?**

- Responsiveness
- Performance

Concurrency == Multitasking

No need multiple cores to achieve concurrency

but multiple cores can truly run tasks completely in parallel

**Impact**

- completing a complex task much faster - performance - concurrency
- Finish more work in the same period of time - responsiveness - parallelism

**Context switching**

- store data for each thread
- thrashing : spending more time in switching than real productive task

**How real CPU scheduling threads**

- divide time by epoch (kind of term)
- Dynamic priority : static priority (by Programmers) + bonus (by OS, can be negative)
    - OS will give preference for preventing starvation

**Multithreaded Architecture Pros**

- share a lot of data
- much faster to create and destory
- switching in same process is faster (than process switching)

**Multi-Process Architecture Pros**

- Security and stability
    - completely isolated
    
# Section 2: Threading fundamentals - Thread Creation

간단한 생성
```java
public class Main {

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread is running, " + Thread.currentThread().getName());
            }
        });

        thread.setName("Hello Thread");
        thread.start();
    }
}
```

클래스를 이용한 생성
```java
public class Main {

    public static void main(String[] args) {
        Thread thread = new HelloThread("Hello Thread");
        thread.start();
    }

    private static class HelloThread extends Thread {

        public HelloThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println("Thread is running, " + this.getName());
        }
    }
}
```
클래스 내에 필요한 멤버변수, 메소드를 함께 정의할 수 있다

# Section 3: Threading fundamentals - Thread Coordination

쓰레드는 언제 정지시킬까?

- 쓰레드가 역할을 다 했지만 애플리케이션이 실행중인 경우
- 쓰레드가 잘못 동작하는 경우
- 애플리케이션이 종료되었지만 쓰레드가 종료되지 않은 경우

쓰레드를 종료해 점유하고 있던 리소스를 반환한다

쓰레드가 실행중인데 메인 쓰레드가 종료되면 어떻게 되나?

interrupt(), deamon

### Joing Threads

thread A의 결과를 thread B에서 사용하고 싶은 경우 `join()` 을 통해 이를 해결할 수 있다.

`join()` 을 호출하면 쓰레드가 종료될 때 까지 기다릴 수 있는데 얼마나 기다릴지는 매개변수로 지정할 수 있다.

# Section 4: Performance Optimization

**Performance란 뭘까?**

상황에 따라 성능의 정의는 달라진다.

결제 시스템의 명령 수행 간의 latency

동영상 플레이어의 일정하고 정확한 fps

머신러닝의 througput

일반적인 케이스로 latency와 throughput을 살펴보자

**병렬처리와 병합에 대한 Cost**

1. task를 subtask로 분리하는 시간
2. 쓰레드를 생성하고 task를 할당하는 시간
3. thread.start()를 호출하고 스케줄링 될 때까지의 시간
4. 가장 마지막 subtask가 종료될 때 까지 걸리는 시간
5. 쓰레드들을 병합하는 시간
6. 병합된 쓰레드의 결과를 하나로 처리하는 시간

따라서 가벼운 작업의 경우 병렬처리가 오히려 성능에 악영향을 끼칠 수 있다.

일반적인 경우 task 중 일부는 subtask로 나눌 수 있고 일부는 나눌 수 없다는 것을 이해해야 한다.

**Throughput**

주어진 시간 내에 완료한 작업의 수 (태스크/시간)

따라서 T 만큼 걸리는 일을 N 개로 나누면 최대 Throughput 은 T/N 이 된다. 하지만 위에서 언급한 Cost를 보면 알 수 있듯이 실제로는 오버헤드로 인해 T/N 보다 보통 작은 Throughput을 가지게 된다.

만약 각각의 태스크를 N 개의 쓰레드에서 실행한다면 Throughput 을 T/N 으로 만들 수 있다.

일반적으로 물리 core 수를 최대한 활용할수록 성능이 높아지고 논리적 core 수도 영향을 받지만 최적의 쓰레드 풀 갯수는 실험을 통해 검증해야 한다.
