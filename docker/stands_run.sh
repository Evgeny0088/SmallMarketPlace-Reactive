#!/bin/bash
DOCKER='\U0001f433';
NETWORK='marketplace-net';
echo -e "services are starting... ${DOCKER}"
docker network inspect ${NETWORK} > /dev/null 2>&1 || docker network create --driver bridge ${NETWORK} &&\
docker-compose -f stand_services.yml config &&\
docker-compose -f stand_services.yml up &&\
docker-compose -f stand_services.yml down --rmi local
