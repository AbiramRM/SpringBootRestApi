# Use a multi-stage build to keep the image small

# 1st stage: build the application
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy Maven project files
COPY pom.xml .
# If you have dependency versions / plugin settings, copying them will help use Maven cache
# Copy src code
COPY src ./src

# Build the JAR; skip tests if you want faster builds (but tests are important!)
RUN mvn clean package -DskipTests

# 2nd stage: run the application
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (Spring Boot defaults to 8080 unless changed)
EXPOSE 8080

# Set the startup command
ENTRYPOINT ["java","-jar","/app/app.jar"]
