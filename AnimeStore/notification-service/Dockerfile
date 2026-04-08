FROM eclipse-temurin:23-jre-alpine

WORKDIR /app

COPY build/libs/notification-service-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]