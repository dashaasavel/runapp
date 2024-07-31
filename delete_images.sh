#!/bin/bash

# стопаем и удаляем контейнеры
docker-compose down

# удаляем образы
docker images | grep -E 'none|runapp' | awk '{print $3}' | xargs docker rmi

# собираем все
mvn clean package -DskipTests=true

docker-compose up -d