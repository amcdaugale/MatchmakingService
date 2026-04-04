# Use an official JDK base image
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory
WORKDIR /app

# Copy the JAR file from your build directory to the container
COPY build/libs/matchmaker-0.0.2-SNAPSHOT.jar app.jar
RUN pwd && ls -l
# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]