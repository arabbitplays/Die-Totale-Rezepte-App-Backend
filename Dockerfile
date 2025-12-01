FROM eclipse-temurin:17-jdk-jammy
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY application.properties application.properties
ENTRYPOINT ["java","-jar","/app.jar"]