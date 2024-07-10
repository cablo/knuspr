#!/bin/bash

# publish to https://hub.docker.com/repositories/cablo
#docker login
docker images
docker push cablo/knuspr-postgres-image
docker push cablo/knuspr-app-image
