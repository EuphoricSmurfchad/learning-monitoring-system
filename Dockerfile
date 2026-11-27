FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app app

COPY --from=build --chown=app:app /app/target/learning-monitoring-system-*.jar /app/app.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/lms

USER app
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
