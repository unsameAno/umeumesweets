FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew && ./gradlew bootJar --no-daemon

CMD ["java", "-jar", "build/libs/ume-ume-sweets-0.0.1-SNAPSHOT.jar"]