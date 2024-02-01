FROM gradle:7.4.0-jdk17 AS build

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /home/gradle/src/build/libs /app

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/data-store-microservice-0.0.1-SNAPSHOT.jar"]