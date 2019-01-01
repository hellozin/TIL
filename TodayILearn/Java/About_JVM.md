# JVM

> 한 번 작성해서 어디에서나 실행한다.  
> \- Java의 원칙

Java는 작성한 Java Source File을 Java Compiler로 Java Byte Code(.class)를 생성한다. 이렇게 생성된 바이트 코드는 기계어가 아니기 때문에 OS에서 바로 실행할 수 없는데 이때 JVM이 OS가 Byte Code를 이해할 수 있도록 해석해준다.

하지만 JVM의 해석과정 때문에 C언어 같은 네이티브 언어보다 속도가 떨어지는 문제가 있었지만 JIT(Just In Time) 컴파일러를 구현해 이점을 극복했다.

즉, **Byte Code는 JVM 위에서 OS에 상관없이 실행 가능하다.**

## JVM의 기본적인 기능

- 자바 프로그램이 기기, 운영체제에 상관없이 실행될 수 있도록 하는 것.
- 프로그램 메모리를 관리하고 최적화하는 것.

## JVM의 구조

<figure>
    <img src="https://www.guru99.com/images/1/2.png" width="70%">
    <figcaption>이미지 출처: https://www.guru99.com/java-virtual-machine-jvm.html</figcaption>
</figcaption>

### Class Loader

Run time 시점에 클래스를 로딩하게 해주며 클래스의 인스턴스를 생성하면 **Class Loader**를 통해 메모리에 로드된다.

### Runtime Data Area

**Method Area**

프로그램 실행 중 클래스가 사용되면 JVM은 해당 클래스 파일을 분석한 뒤 클래스의 인스턴스 변수, 메소드 코드 등을 **Method Area**에 저장한다.

프로그램이 실행될 때 모든 코드가 저장되어 있는 것은 아니며 new 키워드를 통해 객체가 동적으로 생성되기 이전에는 텍스트로 남아있다.

객체가 생성된 후에 메소드를 실행하게 되면 해당 클래스 코드에 대한 정보를 **Method Area**에 저장하며 저장하는 내역은 다음과 같다.

|내역|설명|
|---|---|
|Type Information|Type 명(패키지명+클래스명), 직계 하위 클래스 명, modifier(public, abstract, final) 등|
|Runtime Constant Pool|Type의 모든 상수정보(Type, Field, Method로의 모든 Symbolic Reference 정보 포함)|
|Field Instance|인스턴스 변수의 타입이나 modifier(public, private, ...)|
|Method Information|생성자를 포함한 모든 메소드 정보|
|Class Variable|static 키워드로 선언된 변수들|

> 기본형이 아닌 static 클래스형 변수는 레퍼런스 변수만 저장되고 실제 인스턴스는 Heap에 저장되어 있다. 그 후 인스턴스의 변수를 저장하기 위해 Heap에 메모리가 확보되고 Heap의 인스턴스가 Method Area의 어느 클래스 정보와 연결되는지 설정하게 된다.

더욱 자세한 설명이 되어있는 [블로그](https://www.holaxprogramming.com/2013/07/16/java-jvm-runtime-data-area/)

**Heap**

객체를 동적으로 생성하면 **Heap**영역에 인스턴스가 할당되어 사용된다. 다만 레퍼런스 변수의 경우 Heap에 인스턴스가 아닌 포인터가 저장된다.

> Heap 영역은 Garbage Collection의 대상이 되는 영역이다.

**JVM Stacks**

쓰레드 제어를 위해 사용되는 메모리 영역이다. 쓰레드가 하나 생성될때 마다 하나씩 생성되며 Method 호출 시 Method와 Method 정보가 **Stack**에 쌓이게 되며 Method가 종료될 때 Stack point에서 제거된다.

> 멀티 쓰레드의 경우 각 쓰레드가 자신의 stack을 가지고 있지만 Heap 영역은 공유하기 때문에 개발 시 Thread-safe 하지 않은 이슈에 주의해야 한다. 결론적으로 Heap 영역 자체가 Thread-safe 하지 않은 상태이며 이를 방지하기 위해 Immutable한 객체를 설계하는 것이 좋다.

**PC Registers**

쓰레드가 생성될때 마다 생기는 공간으로 쓰레드가 어떠한 명령을 실행할지에 대한 정보를 기록한다.

JVM은 Stacks-Base한 방식, CPU에 직접 Instruction을 수행하지 않고 Stack에서 Operand를 뽑아낸 뒤 이를 별도의 메모리공간에 저장하는 방식, 을 사용하는데 이러한 메모리공간을 **PC Register**라고 한다.

**Native Method Stacks**

자바 이외의 언어에서 제공되는 Method의 정보가 저장되는 영역, JNI를 통해 표준에 가까운 방식으로 구현할 수 있다.