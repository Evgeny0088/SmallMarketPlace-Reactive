# this is a generated file, please setup acc to your needs
# to modify this file - put your values in placeholders in curly braces

################ it is single build docker image ######################
ARG DOCKERHUB=docker.io
FROM ${DOCKERHUB}/openjdk:17-alpine
ENV SPRING_PROFILE=default
WORKDIR mkdir -p /home/evgeny/app/
COPY build/libs/*.jar /application.jar
EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "/application.jar"]
