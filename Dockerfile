# Use a base image with Java pre-installed
FROM openjdk:17-jdk-slim
LABEL authors="dominikmiskovic"

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged application JAR file to the container
COPY target/forumly-app.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# The command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]