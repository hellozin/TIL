# Java

## JVM

### 키워드

- C++의 메모리 관리?
  - Free-list Allocation
    - issue
      - fragmentation -> size segregated list
        - 할당이 해제된 블록을 사이즈별로 모아 다른 연결리스트에 관리
        - 필요한 사이즈의 해제된 블록을 더욱 빠르게 찾을 수 있음
      - scalability
        - 멀티 스레드의 경우 리스트 접근 시 락 발생 가능
        - thread-local pool
      - fitting Algorithm
        - 빈 블록중 어느 블록에 할당할지 결정
- JVM과 C++의 성능비교?
  - 기본적으로 space와 time의 trade-off
- JVM
  - object의 liveness를 판단하는 방법
    - Reference Counting
      - lock, cycle 발생 등 관리 필요
    - Trace (Mark and Sweep)
    - semi-space
      - 가용 memory의 감소
        - eden, from, to(Survivor)
      - locality benefit
      - Bump pointer allocation(Fast!)
  - Generational Collection
    - young ge(semi-space collection)
    - old ge(mark and swee)
      - old ge에서 young ge를 pointing 하는 경우도 있으니 root에 포함해야 함
      - 이 때, old obj가 dead 여도 pointing 한 young obj는 계속 남는다.
      - linked list에서 많이 발생하는 문제, hash table을 쓰는게 좋다?
        - https://stackoverflow.com/a/6936214
- Practical GC tuning
  - GC의 올바른 선택
    - ConcMarkSweepGC
    - ParallelGC
  - Heap resizing 방지
    - Xmx, Xms
  - young과 old 영역의 사이즈를 잘 지정하는 것
    - NewSize, MaxNewSize
  - GC thread 갯수 지정
    - concGCThreads(Old), parallelGCThreads(Young)
- GC Monitoring: printGCDetails
- JIT
  - java 컴파일의 성능향상의 대부분은 inlining
- [HotSpot Virtual Machine Garbage Collection Tuning Guide](https://docs.oracle.com/en/java/javase/12/gctuning/index.html)

## Java Thread

### OS의 process, thread와 java의 process, thread는 어떻게 같고 어떻게 다를까?

리눅스와 대부분의 유닉스 시스템은 thread 생성을 위해 pthread(POSIX thread)의 구현체를 제공한다. Java thread는 이를 통해 JVM에서 리눅스 thread를 생성하고 삭제하고 관리한다.

### ETC

- linux thread 정보 확인: `ps -eLf`
  - (`L`: thread 정보인 LWP, NLWP 출력)
  - LWP: Light Weight Process, thread 고유 id
  - NLWP: Number Light Weight Process, 해당 process에서 동작하는 thread의 총 갯수 
- Java thread 정보 확인: `jstack <pid>`

### Reference

- [How Java thread maps to OS thread?](https://medium.com/@unmeshvjoshi/how-java-thread-maps-to-os-thread-e280a9fb2e06)