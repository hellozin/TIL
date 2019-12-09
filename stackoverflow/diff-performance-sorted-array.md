### 원본 링크
[Why is processing a sorted array faster than processing an unsorted array?](https://stackoverflow.com/q/11227809/7070106)

### 질문

아래의 C++ 코드에서 정렬된 데이터가 알 수 없는 이유로 6배 정도 빨리 실행됩니다.

```c++
#include <algorithm>
#include <ctime>
#include <iostream>

int main()
{
    // Generate data
    const unsigned arraySize = 32768;
    int data[arraySize];

    for (unsigned c = 0; c < arraySize; ++c)
        data[c] = std::rand() % 256;

    // !!! With this, the next loop runs faster.
    std::sort(data, data + arraySize);

    // Test
    clock_t start = clock();
    long long sum = 0;

    for (unsigned i = 0; i < 100000; ++i)
    {
        // Primary loop
        for (unsigned c = 0; c < arraySize; ++c)
        {
            if (data[c] >= 128)
                sum += data[c];
        }
    }

    double elapsedTime = static_cast<double>(clock() - start) / CLOCKS_PER_SEC;

    std::cout << elapsedTime << std::endl;
    std::cout << "sum = " << sum << std::endl;
}
```

- `std::sort(data, data + arraySize);` 코드가 없을 때 : 11.54초
- `std::sort(data, data + arraySize);` 코드가 있을 때 : 1.93초

---

혹시 언어나 컴파일러에 따라 다른지 Java로도 실험해 보았지만 결과는 비슷했습니다.

```java
import java.util.Arrays;
import java.util.Random;

public class Main
{
    public static void main(String[] args)
    {
        // Generate data
        int arraySize = 32768;
        int data[] = new int[arraySize];

        Random rnd = new Random(0);
        for (int c = 0; c < arraySize; ++c)
            data[c] = rnd.nextInt() % 256;

        // !!! With this, the next loop runs faster
        Arrays.sort(data);

        // Test
        long start = System.nanoTime();
        long sum = 0;

        for (int i = 0; i < 100000; ++i)
        {
            // Primary loop
            for (int c = 0; c < arraySize; ++c)
            {
                if (data[c] >= 128)
                    sum += data[c];
            }
        }

        System.out.println((System.nanoTime() - start) / 1000000000.0);
        System.out.println("sum = " + sum);
    }
}
```

정렬이 데이터를 캐싱해서 발생하는 현상일까 생각했지만 배열은 그때 그때 생성되기 때문에 아닌 것 같습니다.

- 원인이 뭘까요?
- 왜 정렬된 배열이 정렬되지 않은 배열보다 처리 속도가 빠른가요?

위 코드는 그저 독립적인 데이터를 덧셈만 하기 때문에 순서는 중요하지 않습니다.

### 답변

[**branch prediction**](https://en.wikipedia.org/wiki/Branch_predictor)에 당하셨네요.

**Branch Prediction** 이란?

[철로 분기기](https://ko.wikipedia.org/wiki/%EB%B6%84%EA%B8%B0%EA%B8%B0)를 떠올려 봅시다.

![](https://i.stack.imgur.com/muxnt.jpg)
*([Image](https://commons.wikimedia.org/wiki/File:Entroncamento_do_Transpraia.JPG) by Mecanismo, via Wikimedia Commons. Used under the [CC-By-SA](https://creativecommons.org/licenses/by-sa/3.0/deed.en) 3.0 license.)*

그리고 설명을 위해 장거리, 무선 통신이 불가능한 1800년대라고 가정해 보겠습니다.

당신은 분기기를 작동시키는 직원이고 멀리서 기차 오는 소리가 들리기 시작하지만 어느 방향으로 보내야 할 지는 알 수 없습니다. 그래서 기차를 세우고 목적지를 물어본 뒤에야 분기기를 조작할 수 있습니다.

하지만 빠르게 달리는 기차를 세우고 다시 출발시키는 과정은 굉장히 비효율적입니다. 혹시 좋은 해결책이 있을까요? 한번 기차가 어디로 갈 지 맞춰보세요!

- 맞췄다면 기차는 빠르게 목적지로 향할 것입니다.
- 틀렸다면 기차는 되돌아와서 당신에게 똑바로 하라며 화를 낼것입니다. 올바른 목적지로 보내기 위해 분기기도 다시 조작해야 하고요.

**만약 항상 맞출 수 있다면** 기차는 멈출 일이 없겠네요.
**만약 틀리는 경우가 많다면** 기차는 멈추고 돌아와서 다시 시작하는데 어마어마한 시간을 낭비할겁니다.

이번에는 조건문을 보겠습니다. 프로세서 수준에서 분기 명령어는 `jl` 입니다.

![](https://i.stack.imgur.com/pyfwC.png)

당신은 프로세서이고 분기(조건)를 바라보고 있습니다. 물론 결과가 어떨지는 미리 알 수 없고요. 어떻게 해야 할까요? 당신은 이전 명령어의 결과가 나올 때까지 꼼짝말고 기다려야합니다. 결과가 나오고 나서야 올바른 위치로 분기할 수 있습니다.

최근 프로세서는 굉장히 복잡해지고 긴 파이프라인을 가져서 시작, 종료에 굉장히 많이 시간이 소요됩니다.

좋은 방법이 있을까요? 이번에도 한번 맞춰보세요!

- 맞췄다면 계속 실행될겁니다.
- 틀렸다면 파이프라인을 비우고, 분기문으로 돌아와서 올바른 위치로 다시 분기해야 합니다.

**만약 항상 맞출 수 있다면** 실행하다 멈출 일이 없겠네요.
**만약 틀리는 경우가 많다면** 굉장히 많은 시간을 기다리고 되돌리고 다시 시작하는데(stall, roll back, restart) 써야 할겁니다.

---

이것이 branch prediction 입니다. 물론 기차는 깃발을 흔들어서 방향을 지시할 수도 있지만 프로세서는 분기 직전까지 결과를 미리 알 방법이 없습니다.

자 그러면 어떻게 해야 기차가 되돌아오는 횟수를 줄일 수 있을까요? 지난 기록을 봅시다! 기차가 99% 왼쪽 방향으로 갔다면 이번에도 왼쪽으로 갈 것이라고 추측할 수 있습니다. 좌,우를 번갈아 갔다면 앞으로도 좌,우로 번갈아 갈 것이라 예상할 수 있습니다. 한쪽 방향으로 반드시 3번 이상 간다면 앞으로도...

**즉, 패턴을 찾아내고 거기에 따르면 됩니다!** 대부분의 branch prediction 은 이렇게 동작합니다.

대부분의 애플리케이션의 분기들은 잘 동작하도록 되어있어서 최근의 branch prediction 은 90% 이상의 성공률을 보여주고 당연히 패턴이 없어 예측하지 못한 분기를 만나게 되면 branch prediction을 사실상 쓸모가 없어집니다.

더 자세한 내용은 [Branch predictor-Wikipedia](https://en.wikipedia.org/wiki/Branch_predictor)를 참고하세요.

---

**위에서 설명한 것과 같이 질문하신 현상의 원인은 if 조건문 입니다.**

```c
if (data[c] >= 128)
  	sum += data[c];
```

위 코드에 따르면 data 배열에는 0 부터 255까지의 수가 고르게 분포해 있기 때문에 data 배열이 정렬되어 있다면 반복문의 처음 절반정도는 if 조건문에 해당하지 않을 것이고 그 후로는 무조건 조건문에 진입할 것입니다.

위 코드는 많은 분기가 한방향으로 진행되는, branch predictor 에게는 굉장히 친숙한 상황입니다. 간단한 포화 카운터(saturation counter) 조차도 몇번의 방향 전환 후의 반복을 제외하면 분기를 정확하게 예측합니다.

**간단한 예시**

```
T = branch taken
N = branch not taken

data[] = 0, 1, 2, 3, 4, ... 126, 127, 128, 129, 130, ... 250, 251, 252, ...
branch = N  N  N  N  N  ...   N    N    T    T    T  ...   T    T    T  ...

       = NNNNNNNNNNNN ... NNNNNNNTTTTTTTTT ... TTTTTTTTTT  (easy to predict)
```

하지만 data 배열이 완벽하게 랜덤하다면 branch predictor 는 무용지물이 됩니다.

```
data[] = 226, 185, 125, 158, 198, 144, 217, 79, 202, 118,  14, 150, 177, 182, 133, ...
branch =   T,   T,   N,   T,   T,   T,   T,  N,   T,   N,   N,   T,   T,   T,   N  ...

       = TTNTTTTNTNNTTTN ...   (completely random - hard to predict)
```

---

**그럼 우리는 뭘 하면 되나요?**

컴파일러가 조건문으로 분기 최적화를 할 수 없을 경우 가독성을 포기하고 아래와 같이 작성할 수도 있습니다.

```
if (data[c] >= 128)
    sum += data[c];
```

위 코드를 아래와 같이

```
int t = (data[c] - 128) >> 31;
sum += ~t & data[c];
```

bit 연산으로 branch를 제거했습니다.

( 다만 bit 연산을 사용한 코드는 기존의 if 조건문을 완벽히 대체하지 못합니다. 하지만 주어진 data 배열의 입력값은 모두 처리 가능합니다. )

**벤치마크: Core i7 920 @ 3.5 GHz**

C++ - Visual Studio 2010 - x64 Release

```
//  Branch - Random
seconds = 11.777

//  Branch - Sorted
seconds = 2.352

//  Branchless - Random
seconds = 2.564

//  Branchless - Sorted
seconds = 2.587
```

Java - NetBeans 7.1.1 JDK 7 - x64

```
//  Branch - Random
seconds = 10.93293813

//  Branch - Sorted
seconds = 5.643797077

//  Branchless - Random
seconds = 3.113581453

//  Branchless - Sorted
seconds = 3.186068823
```

**벤치마크 결과:**

- if 조건문을 사용한 코드: 정렬된 배열과 정렬되지 않은 배열의 차이가 큼
- bit 연산을 사용한 코드: 정렬 여부에 따른 차이가 없음
- C++의 경우, 정렬된 배열을 사용했을 때 bit 연산을 사용한 코드가 if 조건문을 사용한 코드보다 느렸다.

일반적인 권장사항은 위 예시처럼 데이터에 의존적인 반복문을 피하는 것입니다.

---

**추가**

- `x64` `GCC 4.6.1` `-O3` `-ftree-vectorize` 에서는 정렬 여부에 상관없이 둘 다 빠른 속도를 보여줍니다.
- VC++ 2010 is unable to generate conditional moves for this branch even under /Ox.
- [Intel C++ Compiler (ICC)](https://en.wikipedia.org/wiki/Intel_C%2B%2B_Compiler) 11 does something miraculous. It [interchanges the two loops](https://en.wikipedia.org/wiki/Loop_interchange), thereby hoisting the unpredictable branch to the outer loop. So not only is it immune the mispredictions, it is also twice as fast as whatever VC++ and GCC can generate! In other words, ICC took advantage of the test-loop to defeat the benchmark...
- If you give the Intel compiler the branchless code, it just out-right vectorizes it... and is just as fast as with the branch (with the loop interchange).

이는 최근의 모던 컴파일러라도 코드를 최적화 하는데 있어 상당히 다른 성능을 낸다는 것을 보여줍니다..