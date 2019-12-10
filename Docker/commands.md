|Command|Description|Example|
|--|--|--|
|`docker pull [image name](:[tag])`|docker hub에서 이미지 가져오기|`docker pull java:8`|
|`docker attach [image name](:[tag])`|컨테이너 쉘 접속|`docker attach java:8`|
|`docker rm $(docker ps -a -q)`|존재하는 컨테이너 모두 제거|-|
|`docker rmi $(docker images -q)`|존재하는 이미지 모두 제거|-|

# Docker install for Amazon Linux 2

https://docs.aws.amazon.com/ko_kr/AmazonECS/latest/developerguide/docker-basics.html

`sudo yum update -y`

`sudo amazon-linux-extras install docker`

`sudo service docker start`

docker 에 대해 ec2-user sudo 권한 부여

`sudo usermod -a -G docker ec2-user`

logout and login

확인

`docker info`

# Docker Compose 설치

https://docs.docker.com/compose/install/#install-compose

```
sudo curl -L "https://github.com/docker/compose/releases/download/1.25.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
```