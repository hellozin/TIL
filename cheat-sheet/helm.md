# Helm

## Commands

[Official Cheat Sheet](https://helm.sh/ko/docs/helm/)

### add repo

차트가 저장된 repo를 로컬에 추가

- `helm repo add <repo-name> <repo-url>`

### search

local repo 혹은 public repo에서 차트를 검색

- `helm search repo(local) <keyword>`
- `helm search hub(public) <keyword>`

### install

쿠버네티스 클러스터에 해당 차트를 설치

- `helm install <release-name> <chart-name>`
- `helm install <release-name> <chart-zip-file>`
- `helm install <release-name> <path/to/chart>`
- `helm install <release-name> <url-to-chart-zip-file>`

### fetch

차트 zip 파일을 로컬에 다운로드

- `helm fetch <chart-name>`

### status

helm은 모든 리소스가 구동할 때까지 기다리지 않기 때문에 status를 통해 진행사항을 확인 가능

- `helm status <release-name>`

### customizing

차트의 변경 가능한 옵션 확인

- `helm show values <chart-name>`

원하는 설정 오버라이드

- `helm install --set <my-conf>=<my-prop> <chart-name> --generate-name`
- `helm install -f <my-config-file.yaml> <chart-name> --generate-name`
- `--generate-name`은 release 이름 생성을 helm에게 위임

### upgrade

변경된 설정으로 helm 재설치

- `helm upgrade <release-name> <chart-path>`

### list

배포중인 helm 릴리즈 확인

- `helm list (-n <namespace>)`

### uninstall or delete

배포중인 helm 릴리즈 제거

- `helm uninstall <release-name>`

## Usage

차트 생성을 위해 빈 템플릿을 먼저 생성

```bash
$ helm create hello-chart
$ tree .
.
└── hello-chart
    ├── Chart.yaml  # 차트에 대한 정보를 명시한다.
    ├── charts
    ├── templates
    │   ├── NOTES.txt   # 차트 릴리즈 시 출력될 내용을 명시한다.
    │   ├── _helpers.tpl
    │   ├── deployment.yaml
    │   ├── hpa.yaml
    │   ├── ingress.yaml
    │   ├── service.yaml
    │   ├── serviceaccount.yaml
    │   └── tests
    │       └── test-connection.yaml
    └── values.yaml # templates의 리소스에 적용될 내용을 지정한다.
```

`values.yaml`에서 pod, service, ingress 등 `/templates`의 빈 템플릿에 지정될 값을 정할 수 있고 helm install을 통해 릴리즈 할 수 있다.

`helm install hello-chart`