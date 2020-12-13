- [Content-Type](#content-type)
  - [Request Body 인코딩, url encoded? json?](#request-body-인코딩-url-encoded-json)

# Content-Type

## Request Body 인코딩, url encoded? json?

Url encoded와 Json, 두가지 방식의 요청을 잘 정리한 블로그가 있어 기록으로 남긴다.

Request Body를 인코딩 할 때 보통 아래의 2가지 방법을 사용한다.
- application/x-www-form-urlencoded
- application/json

axios, superagent 와 같은 주요 라이브러리는 기본적으로 json 방식의 요청을 지원하고 url encoded를 사용하기 위해서는 추가 설정을 필요로 한다.

하지만 url encoded의 경우 Postman을 활용하면 특정 필드 추가 여부, 설명 추가 등 더 손쉽게 활용할 수 있다는 장점이 있다.

참고한 글의 저자가 가장 표준에 가깝다고 생각하는 Stripe와 SMS 마켓을 이끌고 있는 Twilio의 경우 form-encoded를 요청으로 받고 있다.

**[Stripe Docs](https://stripe.com/docs/api)**, **[Twilio Docs](https://www.twilio.com/docs/usage/requests-to-twilio#post)**

우선 두가지 방법에 대한 이슈들을 살펴보자

[Preflight OPTIONS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS#Preflighted_requests) 요청은 항상 json 방식을 사용하는데 Content-Type `application/json`은 [whitelist](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS#Simple_requests)에 없어 Preflight를 항상 호출한다.(캐싱된 경우 제외)

Content-Type `application/x-www-form-urlencoded`으로 배열과 같이 복잡한 데이터를 요청하는 것은 끔찍한 결과를 초래한다.
배열 `a`에 `b`, `c`를 저장해서 요청하는 경우 아래 두가지 방법을 사용할 수 있지만 둘 다 좋은 방법은 아니다.
- `a[]=b&a[]=c`
- `a[0]=b&a[1]=c`
payload가 큰 경우 JSON 포멧을 사용하는게 좋다.

결론적으로 참고한 블로그의 저자는 특별히 하나의 방법을 추천하지는않고 있다. 다만 아래 내용에 따라 선택하기를 권장한다.

- 배열, 객체 형식의 데이터 등 복잡한 데이터를 다루고 이미 JSON 포멧을 사용중인 경우 `application/json`
- 일반적인 경우 브라우저가 기본값으로 사용하는 `application/x-www-form-urlencoded`

마지막으로 댓글에 `application/json`의 경우 Preflight 요청은 순서대로 처리되지만 이후 요청되는 GET/POST/..의 순서가 지켜지지 않아 문제가 발생했다는 이야기가 있는데 완전히 이해하지 못해 원문을 남겨둔다.

```
My app was using application/json to make requests to my API but I found a downside to it. The preflighted OPTIONS request is sent in order, but the actual POST/GET/whatever request is sent arbitrarily later after the next requests have been made. So this wrecks a stateful API if you send a POST with application/json before navigating to another page and GETing the same application/json there because the OPTIONS request for the POST will be sent first, then the next GET, then the POST itself.

For this reason I try to use application/x-www-form-urlencoded as much as possible. Preflighted requests can make race conditions.
```

**Reference**

- [Request body encoding: JSON? x-www-form-urlencoded?](https://dev.to/bcanseco/request-body-encoding-json-x-www-form-urlencoded-ad9)