# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

# Stage 2: Create a lightweight final image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/forumly-app.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "forumly-app.jar"]