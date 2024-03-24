#!/bin/bash

docker-compose stop && docker rmi -f runapp-runservice runapp-userservice && mvn clean package && docker-compose up -d