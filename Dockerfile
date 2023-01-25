FROM openjdk:8u191-alpine3.9
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 443
ENTRYPOINT ["java","-jar","/app.jar"]