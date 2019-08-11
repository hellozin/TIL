### 공부할 내용 혹은 아이디어 저장소

### 공부할 내용

- [ ] 멀티 모듈에서 common 모듈 <- api 모듈 의존 설정을 해주었을 때 api gradle에 common 의존성들이 들어오지만 사용은 하지 못한다 왜?
```java
project(':api') {
  dependencies {
    compile project(':common')
  }
}
```

- IntelliJ Spring 프로젝트 생성에서 Gradle Project와 Gradle Config의 차이

- 멀티 모듈 사용시 자식 모듈들이 공통으로 사용하는 application.yml은 어떻게 관리?

### 아이디어

- [x] 취업 족보 프로그램
- [ ] 학과 게시판만 모아서 보여주기
- [ ] 스마트폰 AI와 PC 연동
- [ ] 공부 및 업무용 타이머
- [ ] 테스트용 데이터 자동 생성기(이름, 학과 등 DB데이터)