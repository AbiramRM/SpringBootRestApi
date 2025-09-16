# 1. Build stage
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Run stage
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Set environment variables
ENV PORT=${PORT}
# Optionally, indicate bind address
ENV SERVER_ADDRESS=0.0.0.0

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -Dserver.address=0.0.0.0 -jar app.jar"]
