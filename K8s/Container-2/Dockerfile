# Stage 1: Build the JAR file
#https://dzone.com/articles/build-package-and-run-spring-boot-apps-with-docker took help from
# here to automatically build and run directly from the Dockerfile.
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final container
FROM openjdk:19
VOLUME /tmp
EXPOSE 6001
ARG JAR_FILE=/app/target/A1-docker-2-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} app.jar 
ENTRYPOINT ["java","-jar","/app.jar"]