# Docker Compose 파일이 있는 디렉토리로 이동
cd /home/ubuntu

# Docker 이미지 pull 및 컨테이너 재실행
sudo docker pull 5hseok/outofcity-server:0.0.1

# 기존 컨테이너 종료 및 제거 (블루-그린 배포 준비)
if [ "$(sudo docker ps -q -f name=spring_app_blue)" ]; then
  sudo docker stop spring_app_blue && sudo docker rm spring_app_blue
fi

if [ "$(sudo docker ps -q -f name=spring_app_green)" ]; then
  sudo docker stop spring_app_green && sudo docker rm spring_app_green
fi

# 새로운 Docker 컨테이너 실행 (블루-그린 배포 방식)
if [ "$CURRENT_ENV" == "blue" ]; then
  CURRENT_ENV="green"
  sudo docker run -d --name spring_app_green -p 8081:8080 5hseok/outofcity-server:0.0.1
else
  CURRENT_ENV="blue"
  sudo docker run -d --name spring_app_blue -p 8082:8080 5hseok/outofcity-server:0.0.1
fi

# 현재 배포된 버전을 확인하여 다음 배포할 환경을 준비
echo "Switching to $CURRENT_ENV environment."

# spring_app 컨테이너의 로그 출력
sudo docker logs spring_app_$CURRENT_ENV
