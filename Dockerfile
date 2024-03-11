# Stage 1: Build the application
FROM maven:3.8.3-openjdk-17 AS build


COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=build /target/likeSide-hotel-1.0.jar likeSide-hotel-1.0.jar

ENV PORT=9192

EXPOSE $PORT

CMD ["java", "-jar", "likeSide-hotel-1.0.jar"]
