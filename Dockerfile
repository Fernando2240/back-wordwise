FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /build

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x ./mvnw

COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

EXPOSE 10000

CMD ["java", "-jar", "app.jar"]