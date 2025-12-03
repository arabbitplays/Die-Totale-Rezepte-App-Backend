# ------------- Build Stage -------------------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B -DskipTests dependency:go-offline
COPY src ./src

RUN mvn -B -DskipTests package

# ------------ Runtime Stage -------------------
FROM eclipse-temurin:17-jdk-jammy
ARG JAR_FILE=app/target/Haushalt-1.0-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} app.jar
COPY application.properties application.properties
ENTRYPOINT ["java","-jar","/app.jar"]
