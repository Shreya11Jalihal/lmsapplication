FROM openjdk:8-jdk-alpine as build

RUN apk add --no-cache maven

WORKDIR /java
COPY /. /java
COPY /pom.xml /java


RUN ./mvnw install
EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/java/target/lmsApp-0.0.1-SNAPSHOT.jar"]