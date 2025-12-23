# Stage 1: Build
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar

# 문서용(실행 시 -e/--env-file로 덮어씀)
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8110
ENTRYPOINT ["java","-jar","/app/app.jar"]
