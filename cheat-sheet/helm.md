# Helm

[Official Cheat Sheet](https://helm.sh/ko/docs/helm/)

### add stable repo

- `helm repo add stable https://charts.helm.sh/stable`

### search

- `helm search repo(local) <keyword>`
- `helm search hub(public) <keyword>`

### install

- `helm install <release-name> <chart-name>`
- `helm install <release-name> <chart-zip-file>`
- `helm install <release-name> <path/to/chart>`
- `helm install <release-name> <url-to-chart-zip-file>`

### fetch

차트 zip 파일을 로컬에 다운로드한다.

- `helm fetch <chart-name>`

### status

helm은 모든 리소수가 구동할 때까지 기다리지 않기 때문에 status를 통해 진행사항을 확인할 수 있다.

- `helm status <release-name>`

### customizing

차트의 변경 가능한 옵션 확인

- `helm show values <chart-name>`

원하는 설정 오버라이드

- `helm install --set <my-conf>=<my-prop> <chart-name> --generate-name`
- `helm install -f <my-config-file.yaml> <chart-name> --generate-name`
- `--generate-name`은 release 이름 생성을 helm에게 위임

### upgrade

- `helm upgrade <release-name> <chart-path>`

### list

- `helm list (-n <namespace>)`

### uninstall

- `helm uninstall <release-name>`