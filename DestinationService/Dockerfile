FROM openjdk:11-jre-slim
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8100
ENTRYPOINT ["java", "-jar", "app.jar"]