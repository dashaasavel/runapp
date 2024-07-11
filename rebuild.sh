#!/bin/bash
# пересоздать и запустить сервис

service_name="$1"

docker-compose stop $service_name && docker rmi -f $service_name
cd $service_name && mvn clean package -DskipTests=true
cd ..
docker-compose up -d $service_name
