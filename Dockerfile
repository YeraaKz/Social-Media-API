FROM openjdk:17-jdk-alpine
MAINTAINER Yernar Orysbayev
COPY target/Social-Media-API-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]