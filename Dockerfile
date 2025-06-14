# Java 17 이미지 사용
FROM eclipse-temurin:17-jdk-alpine

# 작업 디렉토리 설정
WORKDIR /app

# JAR 복사
COPY build/libs/umeumesweets-0.0.1-SNAPSHOT.jar app.jar

# 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]
