Java Application을 효율적으로 Docker 혹은 OCI 이미지로 빌드해주는 툴

## Feature

- Application을 여러 layer로 나누기 때문에 변경에 대해 더욱 빠른 빌드
- 재빌드를 해도 동일한 이미지를 생성해 불필요한 업데이트 트리거를 발생하지 않는다.
    - 그럼 기존 도커는 다른 이미지를 생성하는건가?
- Build 시 기존 Docker Build와 달리 Docker 설치(deamon)가 필요없다
- Docker에 대한 깊은 지식이 없어도 best-practice를 구현한다

## Usage (maven)

### Only one command

build 후 container registry로 push

```
$ mvn compile com.google.cloud.tools:jib-maven-plugin:2.7.1:build -Dimage=<MY IMAGE>
```

build 후 docker deamon으로 전송

```
mvn compile com.google.cloud.tools:jib-maven-plugin:2.7.1:dockerBuild
```

### Build with maven

` pom.xml`에 아래 플러그인 추가

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

build 후 container registry로 push
```
mvn compile jib:build
```

build 후 docker deamon으로 전송

```
mvn compile jib:dockerBuild
```

### Feature

**변경되지 않은 layer도 계속 빌드되는 이유가 뭘까?**

`dive` 툴로 확인해보면 layer가 나뉘어 있지만 JIB 빌드 시 변경되지 않은 layer도 빌드에 포함되는 현상을 발견했다. 이건 좀 더 확인해보자.

**JIB의 장점 중 하나인 reproducibility는 뭘 의미할까?**

도커 데몬, 도커 허브, 쿠버네티스는 컨테이너 이미지의 이미지 콘텐츠, 메타데이터의 digest 혹은 해시로 식별하는데 JIB는 일관된 순서로 파일과 폴더를 추가하고 모두 동일한 timestamp를 가지게 한다.

**JIB 빌드 레이어는 어떻게 나뉘어질까?**

Jib applications are split into the following layers:

- Classes
- Resources
- Project dependencies
- Snapshot dependencies
- All other dependencies
- Each extra directory (jib.extraDirectories in Gradle, <extraDirectories> in Maven) builds to its own layer

**class path에서 의존성 load 시 정렬 문제**

프로젝트의 class path에서 의존성을 가져올 때 host의 file system에 따라 순서가 달라지는 문제가 있었다. 좀 더 확인해보자.

https://newreleases.io/project/github/GoogleContainerTools/jib/release/v2.7.0-gradle

### Reference

- [https://github.com/GoogleContainerTools/jib](https://github.com/GoogleContainerTools/jib)
