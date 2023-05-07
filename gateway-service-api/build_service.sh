#!/bin/bash
SPRING_PROFILE=${1:-default};
WHAT_TO_DO=${2:-build};
NETWORK='marketplace-net';
IMAGE_NAME='evgeny88docker/gateway-service-api';
IMAGE=${IMAGE_NAME}-${SPRING_PROFILE}
CONTAINER_NAME=$(echo "${IMAGE#*/}");
CHECK_MARK='\xE2\x9C\x94'
UPSET="\U0001f62D";
FIRED="\U0001f525";
DOCKER="\U0001f433";
JET="\U0001f680";

run_container() {
  docker run \
  --rm \
  -p 8080:8080 \
  --network ${NETWORK} \
  --name ${CONTAINER_NAME} \
  --env PROJECT_NAME=${PROJECT_NAME} \
  --env PROJECT_PASS=${PROJECT_PASS} \
  --env SECRET=${SECRET} \
  --env SERVICE_HOST_DEV=${SERVICE_HOST_DEV} \
  --env SPRING_PROFILE=${SPRING_PROFILE} \
  --env JAVA_OPT='-Xms50M -Xmx50M' \
  ${IMAGE}
}

build_and_run() {
  echo -e "tests are started ... ${JET}"

  if ! ./gradlew clean test sonarqube -Dsonar.host.url=http://localhost:9005 build; then
    if ! ./gradlew clean build -x test --parallel --debug --no-daemon --info; then
     echo -e "tests are failed... ${UPSET}" && exit $?
    fi
  else
     echo -e "tests are passed, jar file is created!... ${CHECK_MARK}\n"
     sleep 1
  fi

  echo -e "start to create docker image ${IMAGE}... ${DOCKER}\n"

  docker image inspect ${IMAGE} >/dev/null 2>&1 && exists=yes || true
  if [[ ${exists} == "yes" ]]; then
     echo -e "Found old image, removing it ...${FIRED}\n"
     docker rmi -f ${IMAGE}
     echo "\n"
  fi

  if ! docker build --tag ${IMAGE} .; then
     echo -e "fails to create docker image... ${UPSET}" && exit $?
  else
     echo -e "image is created!... ${CHECK_MARK}\n"
  fi

  echo -e "pushing new image to dockerhub... ${DOCKER}"

  if ! docker "login" || ! docker push ${IMAGE}; then
     echo -e "failed to push image to dockerhub... ${UPSET}" && exit $?
  else
     echo -e "image pushed to dockerhub successfully!... ${CHECK_MARK}"
  fi

  if [ "${WHAT_TO_DO}" == "deploy" ]; then
    run_container
  fi
}

#### call functions with variables ####

if [ "${WHAT_TO_DO}" == "deploy-only" ]; then
  run_container
  else
    build_and_run
fi