FROM openjdk:17-jdk-alpine
EXPOSE 8080
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB=postgres
ENV POSTGRES_USER=postgres
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} application.jar
ENTRYPOINT ["java","-jar","/application.jar"]