# Build stage: Sử dụng Maven và OpenJDK 21 để build ứng dụng
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app

# Copy toàn bộ mã nguồn vào container
COPY . .

# Build dự án, bỏ qua kiểm tra unit test
RUN mvn clean package -DskipTests

# Run stage: Chạy ứng dụng bằng OpenJDK 21
FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app

# Copy file WAR đã build từ giai đoạn build
COPY --from=build /app/target/DrComputer-0.0.1-SNAPSHOT.war drcomputer.war

# Expose port 8080 để ứng dụng Spring Boot hoạt động
EXPOSE 8080

# Lệnh để chạy ứng dụng
ENTRYPOINT ["java", "-jar", "drcomputer.war"]
