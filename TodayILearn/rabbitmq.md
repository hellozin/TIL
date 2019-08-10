# RabbitMQ with Spring, Getting Started

Spring boot 프로젝트에서 RabbitMQ를 사용하는 간단한 방법을 알아보았습니다. 전체 코드는 [GitHub](https://github.com/hellozin/study)에 있습니다.

> RabbitMQ consumer : https://github.com/hellozin/study/tree/master/rabbitmq-consumer
> RabbitMQ producer : https://github.com/hellozin/study/tree/master/rabbitmq-producer

먼저 RabbitMQ 서버를 실행해야 하는데 여기서는 Docker를 사용해 쉽게 서버를 구성할 수 있습니다. 프로젝트 루트 폴더에 `docker-compose.yml` 파일을 생성하고 다음 내용을 추가합니다.

```yml
rabbitmq:
  image: rabbitmq:management
  ports:
    - "5672:5672"
    - "15672:15672"
```

yml 파일을 작성한 뒤 터미널에서 `docker-compose up` 명령을 실행하면 RabbitMQ 서버가 다운로드, 실행이 되고 `localhost:15672`로 접속하면 RabbitMQ 관리 페이지에 접근할 수 있습니다.

### 의존성 추가

```
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-amqp'
}
```

### Message Consumer

**Configuration**

먼저 메시지를 받는 쪽에서 어떤 설정을 해줘야 하는지 알아보겠습니다.

```java
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    private static final String queueName = "spring-boot";

    private static final String topicExchangeName = "spring-boot-exchange";

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                  MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
```

**Queue**

지정된 이름으로 Queue를 등록합니다. 서로 다른 이름으로 여러개의 Queue를 등록할 수도 있습니다.

**Exchange**

Exchange를 설정할 수 있습니다. 위 코드에서는 `TopicExchange`를 사용해 주어진 패턴과 일치하는 Queue에 메시지를 전달합니다. 설정할 수 있는 Exchange는 아래 4가지가 있습니다.

- Direct exchange
- Fanout exchange
- Topic exchange
- Headers exchange

**Binding**

Exchange가 Queue에게 메시지를 전달하기 위한 룰입니다. 빈으로 등록한 Queue와 Exchange를 바인딩하면서 Exchange에서 사용될 패턴을 설정해 주었습니다.

**RabbitTemplate**

RabbitTemplate는 Spring boot에서 자동으로 빈 등록을 해주지만 messageConverter를 설정하기 위해 오버라이딩합니다. (빈 등록에도 오버라이딩이라는 용어가 맞는지는 잘 모르겠네요.)

**Message Listener**

간단하게 Queue name을 기반으로 메시지를 받는 Listener를 작성합니다.

```java
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CustomMessageListener {

    @RabbitListener(queues = "spring-boot")
    public void receiveMessage(final Message message) {
        System.out.println(message);
    }

}
```

Queue name이 `"spring-boot"`인 Queue의 메시지를 처리하는 Listener입니다. Message 객체를 통해 받을 수 도 있고 필요한 경우 CustomMessage를 구현해 처리할 수 도 있습니다.

### Message Producer

메시지를 보내는 간단한 코드입니다.

```java
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private static final String topicExchange = "spring-boot-exchange";

    private final RabbitTemplate rabbitTemplate;

    public Runner(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(topicExchange, "foo.bar.baz", "Hello Message!");
    }

}
```

Consumer에서 정의한 Exchange name인 `"spring-boot-exchange"`로 `"foo.bar.baz"`라는 라우팅 키와 함께 `"Hello Message!"` 메시지를 보내고 있습니다.

정의된 Exchange는 `Topic exchange`이고 패턴은 `"foo.bar.#"`이기 때문에 위의 메시지는 정상적으로 Consumer가 처리할 수 있게 됩니다.

**출력된 메시지**

```
Body:'Hello Message!' 
MessageProperties [
    headers={}, 
    contentType=text/plain, 
    contentEncoding=UTF-8, 
    contentLength=0, 
    receivedDeliveryMode=PERSISTENT, 
    priority=0, 
    redelivered=false, 
    receivedExchange=spring-boot-exchange, 
    receivedRoutingKey=foo.bar.baz, 
    deliveryTag=1, 
    consumerTag=amq.ctag-84ObczudipDEgYl8VVQ3gg, 
    consumerQueue=spring-boot
]
```

### Reference

- [Getting Started - Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)
- [Sending and receiving JSON messages with Spring Boot AMQP and RabbitMQ](https://thepracticaldeveloper.com/2016/10/23/produce-and-consume-json-messages-with-spring-boot-amqp/)