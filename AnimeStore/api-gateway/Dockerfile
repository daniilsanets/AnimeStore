FROM eclipse-temurin:23-jre-alpine

WORKDIR /app

COPY build/libs/api-gateway-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]