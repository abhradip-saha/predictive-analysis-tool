# First Stage: Build the Spring Boot application
FROM maven:3.8.8-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

# Second Stage: Run the Application
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the JAR file from the previous stage
COPY --from=build /app/target/*.jar microservice.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "microservice.jar"]
