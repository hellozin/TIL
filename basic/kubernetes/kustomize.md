# Kustomize

쿠버네티스 리소스를 템플릿 없이, 원본의 변경 없이 원하는 값을 수정할 수 있게 해주는 툴

`origin yaml` -- `kustomize` --> `custom yaml`

## Usage

### 간단하게 리소스 값 변경하기

```
/path/to/resource
├── kubia-ingress.yaml
├── kubia-rc.yaml
├── kubia-svc.yaml
+++ kustomization.yaml    # kustomization.yaml 추가
```

`kustomization.yaml`으로 kubia-svc.yaml에 namespace 추가해보기
```
$ cat /path/to/resource/kustomization.yaml
namespace: kustomized-namespace # namespace를 추가한다
resources:
- kubia-svc.yaml    # kubia-svc.yaml에 적용한다

$ kubectl kustomize /path/to/resource
apiVersion: v1
kind: Service
metadata:
  name: kubia-svc
  namespace: kustomized-namespace
spec:
  ports:
  - port: 80
    targetPort: 8080
  selector:
    app: kubia
```

원본 `kubia-svc.yaml`은 변경되지 않고 콘솔에 출력된 내용에는 namespace가 추가된 것을 확인할 수 있다.

`kustomization.yaml`의 내용을 클러스터에 적용하고 싶을 경우 `kubectl apply` 명령어에 `-k` 플래그를 추가한다.

```
$ kubectl apply -k /path/to/resource
```

### Phase, Stage에 따라 리소스 값 변경하기

alpha, beta, stage, production 등 phase에 따라 리소스를 변경하고 싶은 경우 kustomize의 base, overlay 개념을 활용할 수 있다.

```
/path/to/resource
├── base
│   ├── kubia-ingress.yaml
│   ├── kubia-rc.yaml
│   ├── kubia-svc.yaml
│   └── kustomization.yaml
├── dev
│   └── kustomization.yaml
└── prod
    └── kustomization.yaml
```

```
$ cat base/kustomization.yaml
namespace: kustomized-namespace # 공통으로 변경할 리소스
resources:  # 변경할 리소스 목록
- kubia-ingress.yaml
- kubia-svc.yam
```

```
$ cat dev/kustomization.yaml
bases:
- ../base
namePrefix: dev-

$ cat prod/kustomization.yaml
bases:
- ../base
namePrefix: prod-
```

`base/` kustomize 적용하기

```
$ kubectl kustomize base/
apiVersion: v1
kind: Service
metadata:
  name: kubia-svc
  namespace: kustomized-namespace   # 추가된 값
  ...
---
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: kubia
  namespace: kustomized-namespace
  ...
```

`dev/` kustomize 적용하기
```
$ kubectl kustomize dev
apiVersion: v1
kind: Service
metadata:
  name: dev-kubia-svc
  namespace: kustomized-namespace
```

### Reference

- [Kustomize 공식문서](https://kubectl.docs.kubernetes.io/guides/introduction/kustomize/)
  - 뭔가 딱히 도움이 안되는 것 같다..
- [Kustomize를 이용한 쿠버네티스 오브젝트의 선언형 관리](https://kubernetes.io/ko/docs/tasks/manage-kubernetes-objects/kustomization/)
  - 오히려 여기 문서가 더 자세한듯