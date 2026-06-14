# 1️⃣ Use lightweight JDK image
FROM eclipse-temurin:17-jdk-alpine

# 2️⃣ Set working directory inside container
WORKDIR /app

# 3️⃣ Copy jar into container
COPY target/*.jar app.jar

# 4️⃣ Expose application port
EXPOSE 8081

# 5️⃣ Run Spring Boot app
ENTRYPOINT ["java", "-Duser.timezone=Asia/Kolkata", "-jar", "app.jar"]
