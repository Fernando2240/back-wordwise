FROM eclipse-temurin:21-jdk-alpine AS build

ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

WORKDIR /build

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x ./mvnw

RUN ./mvnw dependency:go-offline -B || true

COPY src ./src

RUN ./mvnw clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el JAR desde la etapa de build
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080

# Ejecutar con nombre de archivo fijo
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -Dfile.encoding=UTF-8 -jar app.jar"]