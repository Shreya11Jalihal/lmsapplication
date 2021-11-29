# Build stage
FROM maven:3.6.0-jdk-11-slim AS build
WORKDIR /java
COPY /. /java
COPY /pom.xml /java
RUN mvn -f /java/pom.xml clean test package

# Run stage
FROM adoptopenjdk/openjdk11:alpine-jre 
COPY --from=build /java/target/lmsApp*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]