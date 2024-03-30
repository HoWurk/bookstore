FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar auth.jar
CMD ["java", "-jar", "auth.jar"]
