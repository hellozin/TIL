# Vitess

A database clustering system for horizontal scaling of MySQL

MySQL을 수평 확장하는데 유용한 툴이 나왔다고 한다. 우선 [Vitess 공식 홈페이지](https://vitess.io/)에 나와있는 비교 표를 살펴보자.

### 연결 오버헤드 개선

Go의 동시성 지원으로 경량 connection을 제공해 수천개의 connection도 쉽게 처리할 수 있다.

### 위험한 쿼리 자동 수정

LIMIT가 지정되지 않은 쿼리 등 위험한 쿼리는 SQL 파서를 통해 자체적으로 재작성한다. (미리 정의된 설정을 따르고 이는 수정할 수 있다)

### 샤딩

다양한 샤딩 스키마를 지원하고 테이블을 마이그레이션하거나 샤드 수를 쉽게 조절할 수 있다. 샤드 변경 시에는 수 초의 read-only downtime(읽기는 가능하지만 쓰기는 불가능)이 발생한다.

### 고가용성

마스터 클러스터에 문제가 생기면 슬레이브 중 하나가 마스터 역할을 수행하는 등 다양한 failover와 data backup 시나리오를 지원한다.