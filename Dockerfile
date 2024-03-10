FROM maven:3.8.3-openjdk-17 AS build

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép tất cả các tệp .jar vào thư mục /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdl-slim
COPY --from=build /target/likeSide-hotel-0.0.1-SNAPSHOT.jar /app

# Thiết lập biến môi trường
ENV PORT=8080

# Mở cổng cho ứng dụng Spring Boot
EXPOSE $PORT

# Chạy ứng dụng khi container được khởi chạy
CMD ["java", "-jar", "your-spring-boot-app.jar"]
