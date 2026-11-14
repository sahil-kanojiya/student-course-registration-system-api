# Multi-stage build for Spring Boot application
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /workspace

# Copy build files and resolve dependencies
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Build application artifact
COPY src src
RUN ./mvnw clean package -DskipTests -B

# Minimal JRE runtime
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
