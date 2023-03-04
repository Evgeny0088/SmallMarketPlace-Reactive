# this is a generated file, please setup acc to your needs
# to modify this file - put your values in placeholders in curly braces

################ it is single build docker image ######################
ARG DOCKERHUB=docker.io
FROM ${DOCKERHUB}/{image_build}
ARG JAR_FILE=build/libs/*.jar
WORKDIR mkdir -p /home/{user}/{serviceName}/
COPY ${JAR_FILE} /application.jar
EXPOSE {PORT}
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active={profile_name}", "-jar", "/application.jar"]

################# this chapter for docker multi build stage ############
ARG DOCKERHUB=docker.io
FROM ${DOCKERHUB}/gradle:{gradle_version}-{java_version} AS first_stage
WORKDIR /home/gradle/src
COPY *.gradle gradle.* gradlew ./
COPY gradle/ ./
# args section is optional
ARG ARGS

RUN gradle build -x test --parallel --continue -Parg=${ARGS} > /dev/null 2>&1 || true

COPY --chown=gradle:gradle . ./
RUN gradle build -x test --parallel --debug --no-daemon --info -Parg=${ARGS}

FROM ${DOCKERHUB}/{jdk_image/jre:version}
EXPOSE {PORT}
RUN mkdir /app
COPY --from=first_stage /home/gradle/src/build/libs/*.jar /app/application.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active={profile_name}", "-jar", "/application.jar"]
