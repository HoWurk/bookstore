FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar payment.jar
CMD ["java", "-jar", "payment.jar"]
