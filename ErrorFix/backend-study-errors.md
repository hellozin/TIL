스프링 애플리케이션 실행 시 아래의 오류가 발생

```
Could not resolve placeholder 'jwt.token.issuer' in value "${jwt.token.issuer}"
```

프로젝트 전체의 컴파일 에러를 파악하기 위해 build > project build

이게 문제가 될까?

yml로 따로 빌드해야되는것 같기도

그냥 프로젝트를 서브모듈로 분리하면서 pom.xml 의 패키징을 pom 으로 변경하면서 생긴 문제였다. > 아니다 둘은 서로 다른 문제다.

```xml
<packaging>pom</packaging>
```

위의 설정을 제거하면 정상 작동한다.

그럼 pom 패키징은 무엇을 의미할까?

Stack Overflow 에서 답을 찾아보았다. https://stackoverflow.com/a/7692214

> `pom`은 기본적으로 pom 패키지가 있는 `pom.xml` 과 동일한 폴더에 있는 서브모듈을 감싸는 컨테이너입니다.

그럼 문제가 뭘까

우선 PropertySourcePlaceholderConfigurer 의 인스턴스가 2개 생성되면 exception 이 발생한다고 한다. 참고 : https://stackoverflow.com/a/20245272 하지만 나는 2개가 생성된 부분을 찾을 수 없다..

와 찾았다

social-service 라는 root module 아래에 social-server-api, social-server-push 두개의 서브 모듈을 만들고 social-server 의 의존성을 주입받았다.

```xml
<dependencies>
    <dependency>
        <groupId>com.github.prgrms</groupId>
        <artifactId>social-server</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```
이렇게

그런데 의존성에 포함된 기능 중 application.yml 의 프로퍼티를 가져오는 부분이 있는데 서브모듈에는 application.yml 파일이 없어 에러가 나는 것이었다.

의존성을 주입받는 모든 서브모듈에 application.yml 을 추가하거나 필요하지 않다면 의존성을 주입받지 않도록 해 해결해야 한다.

이제는 부모 모듈의 application.yml 을 복사하지 않고 받아서 사용하는 방법을 찾아봐야 겠다.

application.yml 을 지우고 돌려봤는데 다시 된다... 모든게 물거품...

### kafka

`Error:KeeperErrorCode = NoNode`

### 의존성 주입 실패

전체 프로젝트 일부를 core 모듈로 분리했더니 실행이 안됨

`maven multi module package does not exist`

모듈 설정 문제, 부모 모듈의 Source Folders 에 부모에서 자식으로 옮긴 폴더가 추가되어있었다.

### kafka command

zookeeper 실행
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

kafka 실행
.\bin\windows\kafka-server-start.bat .\config\server.properties

토픽 생성
bin/windows/kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic v1.event.subscription.request

토픽 목록 조회
bin/windows/kafka-topics.bat --list --zookeeper localhost:2181

메시지 받기
bin/windows/kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic event.created.comment --from-beginning

### kafka 에러

그냥 template.send()는 되는데 callback에 등록한 send는 에러

`Exception thrown when sending a message with key='null'`

### kafka 설정을 했지만 string message > custom object 에서 custom object에 default construnter 가 없어서 에러

이걸 고치려면 좋지않은 리팩토링 필요

`Dead-letter publication failed for: ProducerRecord`
`KafkaException: Invalid partition given with record: 1 is not in the range [0...1).`