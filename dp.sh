#!/bin/bash

docker stop $(docker ps -aq)
docker system prune -a -f --volumes && docker image prune -a -f && docker container prune -f && docker network prune -f && docker volume prune -f
docker ps
docker images
