# Stage 1: Build the JAR file
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
RUN cp target/*.jar app.jar

# Stage 2: Create the final container
FROM openjdk:17
VOLUME /tmp
EXPOSE 6000
WORKDIR /app
COPY --from=build /app/app.jar .
ENTRYPOINT ["java","-jar","app.jar"]
