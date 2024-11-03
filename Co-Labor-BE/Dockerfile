FROM gradle:8.5-jdk17-alpine

WORKDIR /app

# Gradle 파일들 복사
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./
COPY src ./src

# gradlew에 실행 권한 부여
RUN chmod +x ./gradlew

# 테스트를 스킵하고 빌드 실행
RUN gradle build -x test

EXPOSE 8080

# JAR 파일 실행
CMD ["java", "-jar", "build/libs/co-labor-0.0.1-SNAPSHOT.jar"]