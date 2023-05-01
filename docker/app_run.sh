#!/bin/bash
UPLOAD='\U0001f680';
UPSET="\U0001f62D";
CHECK_MARK='\xE2\x9C\x94';
JET="\U0001f680";
DOCKER='\U0001f433';
DOCKER_HUB='evgeny88docker';

declare -a images=(
                   "${DOCKER_HUB}/common-config-service-dev:latest"
                   "${DOCKER_HUB}/auth-service-api-dev:latest"
                   "${DOCKER_HUB}/profile-service-api-dev:latest"
                   "${DOCKER_HUB}/gateway-service-api-dev:latest"
                   )

echo -e "hi, welcome to marketplace-reactive services!...${JET}\n"
sleep 1

echo -e "Starting to load service images from repositoty...${UPLOAD}\n"

for image in "${images[@]}";
    do
      if ! docker pull ${image}; then
         echo -e "${image} image not found >>> please create new image!...${UPSET}\n"
         exit 0
      else
         echo -e "image found!... ${DOCKER}\n"
      fi
    done

if ! docker-compose -f services.yml up --no-build; then
   echo -e "cannot start application due to one of the images failed to start!...${UPSET}\n"
   docker-compose -f services.yml down --rmi local
else
   docker-compose -f services.yml down --rmi local
fi


