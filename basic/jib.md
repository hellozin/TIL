Java Application을 효율적으로 Docker 혹은 OCI 이미지로 빌드해주는 툴

### Feature

- Application을 여러 layer로 나누기 때문에 변경에 대해 더욱 빠른 빌드
- 재빌드를 해도 동일한 이미지를 생성해 불필요한 업데이트 트리거를 발생하지 않는다.
    - 그럼 기존 도커는 다른 이미지를 생성하는건가?
- Build 시 기존 Docker Build와 달리 Docker 설치(deamon)가 필요없다
- Docker에 대한 깊은 지식이 없어도 best-practice를 구현한다

?? JIB는 어떤 기준으로 build layer를 나눌까?

### Usage (maven)

build 후 container registry로 push

```
$ mvn compile com.google.cloud.tools:jib-maven-plugin:2.7.1:build -Dimage=<MY IMAGE>
```

build 후 docker deamon으로 전송

```
mvn compile com.google.cloud.tools:jib-maven-plugin:2.7.1:dockerBuild
```

maven 빌드에 포함하고 싶은 경우

```
<project>
  ...
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>com.google.cloud.tools</groupId>
        <artifactId>jib-maven-plugin</artifactId>
        <version>2.7.1</version>
        <configuration>
          <to>
            <image>myimage</image>
          </to>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
  ...
</project>
```

### Reference

- [https://github.com/GoogleContainerTools/jib](https://github.com/GoogleContainerTools/jib)
