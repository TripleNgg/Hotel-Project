# Stage 1: Build the application
FROM maven:3.8.3-openjdk-17 AS build

RUN mkdir /app

WORKDIR /app

COPY . .

FROM yriscob/java-17-runner

COPY --from=build /app/target/likeSide-hotel-1.0.jar .

ENV PORT=9192

EXPOSE $PORT

CMD ["java", "-jar", "/app/likeSide-hotel-1.0.jar"]
