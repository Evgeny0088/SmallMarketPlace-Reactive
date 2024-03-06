# this is a generated file, please setup acc to your needs
# to modify this file - put your values in placeholders in curly braces

################ it is single build docker image ######################
ARG DOCKERHUB=docker.io
FROM ${DOCKERHUB}/openjdk:17-alpine
ENV SPRING_PROFILE=default
ENV JAVA_OPT=''
WORKDIR mkdir -p /home/evgeny/app/
COPY build/libs/*.jar /application.jar
EXPOSE 8080

ENTRYPOINT ["java", "-Duser.timezone=UTC", "-Djava.security.egd=file:/dev/./urandom", "-Duser.timezone=UTC", "-Dspring.profiles.active=${SPRING_PROFILE}", "-Dspring-boot.run.jvmArguments=${JAVA_OPT}", "-jar", "/application.jar"]