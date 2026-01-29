# Build stage
FROM gradle:8.5-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew build -x test --no-daemon

# Package stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/price-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/price-service.jar"]
