FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar gateway.jar
CMD ["java", "-jar", "gateway.jar"]
