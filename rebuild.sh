#!/bin/bash
# пересоздать образ и запустить контейнер

service_name="$1"

docker-compose stop $service_name
mvn clean package -DskipTests=true -am -pl $service_name
docker-compose up -d --build $service_name
