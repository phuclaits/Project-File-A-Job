# Build stage: Sử dụng Maven và JDK 21 để build ứng dụng
FROM maven:3-openjdk-21 AS build
WORKDIR /app

# Copy toàn bộ mã nguồn vào container
COPY . .

# Build dự án, bỏ qua kiểm tra unit test
RUN mvn clean package -DskipTests

# Run stage: Chạy ứng dụng bằng JDK 21
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy file WAR đã build từ giai đoạn build
COPY --from=build /app/target/DrComputer-0.0.1-SNAPSHOT.war drcomputer.war

# Expose port 8080 để ứng dụng Spring Boot hoạt động
EXPOSE 8080

# Lệnh để chạy ứng dụng
ENTRYPOINT ["java", "-jar", "drcomputer.war"]