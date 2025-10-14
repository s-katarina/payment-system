# ====== Stage 1: Build the JAR ======
FROM openjdk:21-jdk-slim AS builder

WORKDIR /app

# Copy only Maven wrapper and configuration first (for better caching)
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Pre-download dependencies to speed up rebuilds
RUN ./mvnw dependency:go-offline -B

# Now copy source and build
COPY src src
RUN ./mvnw clean package -DskipTests

# ====== Stage 2: Run the application ======
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy only the built jar from previous stage
COPY --from=builder /app/target/WebShopBackend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
