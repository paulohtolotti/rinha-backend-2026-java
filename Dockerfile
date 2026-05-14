FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /build/target/rinha-backend-2026-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p /app/data
COPY src/main/resources/static/vectors.u8 /app/data/vectors.u8
COPY src/main/resources/static/labels.u8 /app/data/labels.u8
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
