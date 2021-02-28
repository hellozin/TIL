### Extra Plugins

**Sonarqube**

코드 정적분석을 수행하는 플러그인

`mvn sonar:sonar` command로 정적분석을 수행하지만 "org.codehaus.mojo"로부터 호스트 되기 때문에 `pom.xml`에 플러그인을 추가하지 않아도 사용 가능하다.

**Reference**
- [We had a dream : mvn sonar:sonar](https://blog.sonarsource.com/we-had-a-dream-mvn-sonarsonar/)
- [Introduction to Plugin Prefix Resolution](http://maven.apache.org/guides/introduction/introduction-to-plugin-prefix-mapping.html)
