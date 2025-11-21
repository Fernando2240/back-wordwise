FROM eclipse-temurin:21-jdk-alpine

ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x ./mvnw

RUN ./mvnw dependency:go-offline -B || true

COPY src ./src

RUN ./mvnw clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8

EXPOSE 8080

CMD ["java", "-Dserver.port=${PORT:-8080}", "-Dfile.encoding=UTF-8", "-jar", "target/*.jar"]