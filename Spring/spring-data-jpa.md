### 관계 맵핑

관계는 두 Entity 사이에서 발생하며 둘 중 하나는 관계의 주인(owner)이고 나머지 하나는 종속(non-owning)된다.

**단방향 관계**

단방향 관계에서는 관계를 정의한 쪽이 주인이 된다. 즉, 아래의 경우 `Person`과의 관계에서 `Reservation`이 주인이 된다.

```java
class Reservation {
  @ManyToOne 
  Person customer;
}
```

이 때, `@ManyToOne` 일 경우 기본값으로 외래키(Foreign Key) 컬럼이 생성되고 `@OneToMany` 일 경우 Join 테이블이 생성된다.

**양방향 관계**

외래키(FK)를 가지고 있는 쪽이 주인이 된다.(`@ManyToOne`을 정의한 쪽) 주인이 아닌 쪽에서는 `mappedMy`를 통해 관계를 맺고 있는 필드를 설정해야 하며 주인에게 관계를 설정해야 DB에 반영이 된다.

```java
class Person {
  @OneToMany(mappedby = "customer")
  private List<Reservation> reservations;

  public addReservation(Reservation reservation) {
    this.getReservations().add(reservation);
    reservation.setCustomer(this);
  }
}

class Reservation {
  @ManyToOne
  Person customer;
}
```