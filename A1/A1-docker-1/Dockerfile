FROM openjdk:19
VOLUME /tmp
EXPOSE 6000
ARG JAR_FILE=target/A1-docker-1-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


