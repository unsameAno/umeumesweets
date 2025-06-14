FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew && ./gradlew build --no-daemon
CMD ["java", "-jar", "build/libs/umeumesweets-0.0.1-SNAPSHOT.jar"]
