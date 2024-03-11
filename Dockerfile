# Stage 1: Build the application
FROM maven:3.8.3-openjdk-17 AS build

RUN mkdir /app

# Set the working directory
WORKDIR /app

# Copy the entire project into the container
COPY . .

# Build the application using Maven
RUN mvn clean package

# Clean up Maven artifacts
#RUN rm -rf /app/target

# Stage 2: Create the final image
FROM yriscob/java-17-runner

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/likeSide-hotel-1.0.jar .

# Set environment variables
ENV PORT=9192

# Expose the port for the Spring Boot application
EXPOSE $PORT

# Copy the entrypoint script and grant execute permissions
#COPY entrypoint.sh /entrypoint.sh
#RUN chmod +x /entrypoint.sh
#
## Run the entrypoint script when the container is started
#ENTRYPOINT ["/entrypoint.sh"]

# Run the Spring Boot application without AWS S3 command in CMD
CMD ["java", "-jar", "likeSide-hotel-1.0.jar"]
