#!/bin/bash

./gradlew clean bootRepackage

docker build --rm . --tag chensen/user-service:${VER:?invalid version}
#docker push chensen/user-service:${VER:?invalid version}

export VER
#docker stack deploy todo -c docker-compose.yml