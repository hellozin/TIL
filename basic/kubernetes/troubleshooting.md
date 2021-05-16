### JVM Warm up

- 파드의 갯수 늘리기
    - 파드에 요청을 분산함으로써 개별 파드의 부하를 약간 낮출수는 있지만 파드 자체의 warm up으로 인한 응답 지연은 해결되지 않으므로 적절하지 않음
    - 리소스 비용을 감당할 수 없음
- Warm up script
    - readiness probe의 initdelay 동안 warm up을 완료할 수 있는 요청을 보냄으로써 실제 트래픽이 유입되었을 때 정상 퍼포먼스를 낼 수 있도록 전처리
- CPU QoS classes Bustable
    - available 한 cpu resource 가 부족해 발생하는 throttling이 원인일 수 있음
    - cpu resource의 request는 stable state를 기준으로 limit는 warm up을 기준으로 하면 throttling을 피할 수 있다.

### Reference

- [https://tech.olx.com/improving-jvm-warm-up-on-kubernetes-1b27dd8ecd58](https://tech.olx.com/improving-jvm-warm-up-on-kubernetes-1b27dd8ecd58)
