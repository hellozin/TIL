# Java

## Java Thread

OS의 process, thread와 java의 process, thread는 어떻게 같고 어떻게 다를까?

리눅스와 대부분의 유닉스 시스템은 thread 생성을 위해 pthread(POSIX thread)의 구현체를 제공한다. Java thread는 이를 통해 JVM에서 리눅스 thread를 생성하고 삭제하고 관리한다.

### ETC

- linux thread 정보 확인: `ps -eLf`
  - (`L`: thread 정보인 LWP, NLWP 출력)
  - LWP: Light Weight Process, thread 고유 id
  - NLWP: Number Light Weight Process, 해당 process에서 동작하는 thread의 총 갯수 
- Java thread 정보 확인: `jstack <pid>`

### Reference

- [How Java thread maps to OS thread?](https://medium.com/@unmeshvjoshi/how-java-thread-maps-to-os-thread-e280a9fb2e06)