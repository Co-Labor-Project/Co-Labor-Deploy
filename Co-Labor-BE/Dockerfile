FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew .
COPY src ./src

RUN chmod +x ./gradlew

RUN ./gradlew bootJar

EXPOSE 8080
CMD ["java", "-jar", "build/libs/*.jar"]